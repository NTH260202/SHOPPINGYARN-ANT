package com.artiexh.api.base.utils;

import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class S3Util {
	private S3Util() {
	}

	public static String getPresignedString(String regionName, String bucketName, String awsAccessKey, String awsSecretKey, String fileName, boolean isPublic) {
		URL endpointUrl;
		String endpointUrlString;
		try {
			if (regionName.equals("us-east-1")) {
				endpointUrlString = "https://s3.amazonaws.com/" + bucketName + "/" + fileName;

			} else {
				endpointUrlString = "https://s3-" + regionName + ".amazonaws.com/" + bucketName + "/" + fileName;
			}
			if (isPublic) {
				return endpointUrlString;
			}
			endpointUrl = new URL(endpointUrlString);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Unable to parse service endpoint: " + e.getMessage());
		}

		// construct the query parameter string to accompany the url
		Map<String, String> queryParams = new HashMap<>();

		// for SignatureV4, the max expiry for a presigned url is 7 days,
		// expressed in seconds
		int expiresIn = 7 * 24 * 60 * 60;
		queryParams.put("X-Amz-Expires", "" + expiresIn);

		// we have no headers for this sample, but the signer will add 'host'
		Map<String, String> headers = new HashMap<>();

		AWS4SignerForQueryParameterAuth signer = new AWS4SignerForQueryParameterAuth(
			endpointUrl, "GET", "s3", regionName);
		String authorizationQueryParameters = signer.computeSignature(headers,
			queryParams,
			AWS4SignerBase.UNSIGNED_PAYLOAD,
			awsAccessKey,
			awsSecretKey);

		// build the presigned url to incorporate the authorization elements as query parameters
		return endpointUrl + "?" + authorizationQueryParameters;
	}

	public static String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}
}
