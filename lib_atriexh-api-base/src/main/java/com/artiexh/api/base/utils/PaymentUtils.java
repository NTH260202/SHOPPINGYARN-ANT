package com.artiexh.api.base.utils;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PaymentUtils {
	private PaymentUtils() {
	}

	public static String generatePaymentUrl(String vnpVersion,
											String vnpCommand,
											String vnpOrderInfo,
											String vnpTxnRef,
											String vnpIpAddr,
											String vnpTmnCode,
											String vnpReturnUrl,
											String amount,
											String vnpCurrCode,
											String vnpLocale,
											String vnpPaymentViewUrl,
											String vnpSecretHash,
											Duration expireTime) {
		Map<String, String> vnpParams = new HashMap<>();
		vnpParams.put("vnp_Version", vnpVersion);
		vnpParams.put("vnp_Command", vnpCommand);
		vnpParams.put("vnp_TmnCode", vnpTmnCode);
		vnpParams.put("vnp_Amount", amount);
		vnpParams.put("vnp_CurrCode", vnpCurrCode);
		//vnpParams.put("vnp_BankCode", "ACB");
		vnpParams.put("vnp_TxnRef", vnpTxnRef);
		vnpParams.put("vnp_OrderInfo", vnpOrderInfo);
		vnpParams.put("vnp_OrderType", "billpayment");

		if (vnpLocale != null && !vnpCommand.isEmpty()) {
			vnpParams.put("vnp_Locale", vnpLocale);
		} else {
			vnpParams.put("vnp_Locale", "vn");
		}
		vnpParams.put("vnp_ReturnUrl", vnpReturnUrl);
		if (vnpIpAddr.equals("0:0:0:0:0:0:0:1") || vnpIpAddr.equals("127.0.0.1")) {
			try {
				InetAddress hostAddress = InetAddress.getLocalHost();
				vnpIpAddr = hostAddress.getHostAddress();
			} catch (UnknownHostException e) {
				throw new IllegalArgumentException(e);
			}
		}
		vnpParams.put("vnp_IpAddr", vnpIpAddr);

		ZonedDateTime time = ZonedDateTime.now()              // Current moment in a particular time zone.
			.withZoneSameInstant(ZoneId.of("Asia/Ho_Chi_Minh"));

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String vnpCreateDate = time.format(formatter);

		vnpParams.put("vnp_CreateDate", vnpCreateDate);
		time = time.plus(expireTime);
		String vnpExpireDate = time.format(formatter);
		vnpParams.put("vnp_ExpireDate", vnpExpireDate);
		List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
		Collections.sort(fieldNames);
		StringBuilder hashData = new StringBuilder();
		StringBuilder query = new StringBuilder();
		Iterator<String> itr = fieldNames.iterator();
		while (itr.hasNext()) {
			String fieldName = itr.next();
			String fieldValue = vnpParams.get(fieldName);
			if ((fieldValue != null) && !fieldValue.isEmpty()) {
				//Build hash data
				hashData.append(fieldName);
				hashData.append('=');
				hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
				//Build query
				query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
				query.append('=');
				query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

				if (itr.hasNext()) {
					query.append('&');
					hashData.append('&');
				}
			}
		}
		String queryUrl = query.toString();
		String vnpSecureHash = hmacSecureHash(vnpSecretHash, hashData.toString(), HmacAlgorithms.HMAC_SHA_512);
		queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
		return vnpPaymentViewUrl + "?" + queryUrl;
	}

	public static String hmacSecureHash(String key, String data, HmacAlgorithms algorithm) {
		return new HmacUtils(algorithm, key).hmacHex(data);
	}

	public static String hashAllFields(Map<String, String> fields, String secretKey) {
		List<String> fieldNames = new ArrayList<>(fields.keySet());
		Collections.sort(fieldNames);
		StringBuilder sb = new StringBuilder();
		Iterator<String> itr = fieldNames.iterator();
		while (itr.hasNext()) {
			String fieldName = itr.next();
			String fieldValue = fields.get(fieldName);
			if ((fieldValue != null) && !fieldValue.isEmpty()) {
				sb.append(fieldName);
				sb.append("=");
				sb.append(fieldValue);
			}
			if (itr.hasNext()) {
				sb.append("&");
			}
		}
		return hmacSecureHash(secretKey, sb.toString(), HmacAlgorithms.HMAC_SHA_512);
	}

}
