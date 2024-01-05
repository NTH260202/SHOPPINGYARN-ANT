package com.artiexh.api.base.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class HttpUtils {
	private HttpUtils() {
	}

	/**
	 * Makes a http request to the specified endpoint
	 */
	public static String invokeHttpRequest(URL endpointUrl,
										   String httpMethod,
										   Map<String, String> headers,
										   String requestBody) {
		HttpURLConnection connection = createHttpConnection(endpointUrl, httpMethod, headers);
		try {
			if (requestBody != null) {
				DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
				wr.writeBytes(requestBody);
				wr.flush();
				wr.close();
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Request failed. " + e.getMessage(), e);
		}
		return executeHttpRequest(connection);
	}

	public static String executeHttpRequest(HttpURLConnection connection) {
		InputStream is = getInputStream(connection);

		try (BufferedReader rd = new BufferedReader(new InputStreamReader(is))) {
			// Get Response
			String line;
			StringBuilder response = new StringBuilder();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			return response.toString();
		} catch (Exception e) {
			throw new IllegalArgumentException("Request failed. " + e.getMessage(), e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private static InputStream getInputStream(HttpURLConnection connection) {
		try {
			return connection.getInputStream();
		} catch (IOException e) {
			return connection.getErrorStream();
		}
	}

	public static HttpURLConnection createHttpConnection(URL endpointUrl,
														 String httpMethod,
														 Map<String, String> headers) {
		try {
			HttpURLConnection connection = (HttpURLConnection) endpointUrl.openConnection();
			connection.setRequestMethod(httpMethod);

			if (headers != null) {
				for (String headerKey : headers.keySet()) {
					log.info(headerKey + ": " + headers.get(headerKey));
					connection.setRequestProperty(headerKey, headers.get(headerKey));
				}
			}

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			return connection;
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot create connection. " + e.getMessage(), e);
		}
	}

	public static String urlEncode(String url, boolean keepPathSlash) {
		String encoded;
		encoded = URLEncoder.encode(url, StandardCharsets.UTF_8);
		if (keepPathSlash) {
			encoded = encoded.replace("%2F", "/");
		}
		return encoded;
	}
}
