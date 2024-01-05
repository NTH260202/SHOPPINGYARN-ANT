package com.artiexh.api.base.monitor.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@WebFilter(filterName = "AccessLoggingFilter", urlPatterns = "/*")
@Log4j2
public class AccessLoggingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		var requestWrapper = new ContentCachingRequestWrapper(request);
		var responseWrapper = new ContentCachingResponseWrapper(response);

		Instant requestTime = Instant.now();
		ThreadContext.put("requestId", UUID.randomUUID().toString());
		log.info("Request {} {} {} {} headers=[{}] parameters=[{}]",
			requestWrapper::getRemoteAddr,
			requestWrapper::getProtocol,
			requestWrapper::getMethod,
			requestWrapper::getServletPath,
			() -> getHeaderAsString(requestWrapper),
			() -> getParametersAsString(requestWrapper));

		try {
			filterChain.doFilter(requestWrapper, responseWrapper);
		} finally {
			Duration processingTime = Duration.between(requestTime, Instant.now());
			log.info("Response processingTime={}ms status={} {}",
				processingTime::toMillis,
				responseWrapper::getStatus,
				() -> HttpStatus.valueOf(responseWrapper.getStatus()).getReasonPhrase());

			responseWrapper.copyBodyToResponse();

			ThreadContext.clearAll();
		}
	}

	private String getHeaderAsString(ContentCachingRequestWrapper requestWrapper) {
		var headerNames = requestWrapper.getHeaderNames();
		List<String> headers = new ArrayList<>();
		while (headerNames.hasMoreElements()) {
			var headerName = headerNames.nextElement();
			headers.add(headerName + "=" + requestWrapper.getHeader(headerName));
		}
		return StringUtils.collectionToCommaDelimitedString(headers);
	}

	private String getParametersAsString(ContentCachingRequestWrapper requestWrapper) {
		var parametersArray = requestWrapper.getParameterMap().entrySet().stream()
			.map(entry -> entry.getKey() + "=" + StringUtils.arrayToCommaDelimitedString(entry.getValue()))
			.toArray();
		return StringUtils.arrayToCommaDelimitedString(parametersArray);
	}

}
