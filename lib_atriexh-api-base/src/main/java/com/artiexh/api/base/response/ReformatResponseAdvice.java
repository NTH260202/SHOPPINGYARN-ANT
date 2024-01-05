package com.artiexh.api.base.response;

import com.artiexh.api.base.common.Endpoint;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class ReformatResponseAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return !returnType.getContainingClass().getPackage().getName().contains("org.springdoc");
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
								  Class<? extends HttpMessageConverter<?>> selectedConverterType,
								  ServerHttpRequest request, ServerHttpResponse response) {
		Instant now = Instant.now();
		int status = ((ServletServerHttpResponse) response).getServletResponse().getStatus();
		HttpStatus httpStatus = HttpStatus.resolve(status);
		Assert.notNull(httpStatus, "Invalid http status");
		String path = request.getURI().getPath();

		if (path.contains(Endpoint.Media.ROOT + Endpoint.Media.DOWNLOAD) && !selectedContentType.equals(MediaType.APPLICATION_PROBLEM_JSON)) {
			return body;
		}

		if (path.contains(Endpoint.Order.ROOT + Endpoint.Order.PAYMENT_RETURN)) {
			return body;
		}

		if (body instanceof ProblemDetail problemDetail) {
			return new ResponseModel(now, null, status, httpStatus.name(), problemDetail.getDetail(), path, null);
		} else if ("/error".equals(path)) {
			var bodyMap = (Map<String, Object>) body;
			return new ResponseModel(now, (String) bodyMap.get("code"), status, httpStatus.name(), (String) bodyMap.get("message"), (String) bodyMap.get("path"), null);
		} else if (body instanceof ResponseModel responseModel) {
			return new ResponseModel(now, responseModel.code(), status, httpStatus.name(), responseModel.message(), path, responseModel.data());
		} else {
			return new ResponseModel(now, null, status, httpStatus.name(), null, path, body);
		}
	}

}
