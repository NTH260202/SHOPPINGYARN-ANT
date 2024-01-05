package com.artiexh.model.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ResponseCode {
	SUCCESS("00", "Giao dịch thành công"),
	CANCEL("24", "Khách hàng hủy giao dịch"),
	FAILED("15", "Giao dịch thất bại"),
	OTHER("99", "Các lỗi khác");
	private final String code;
	private final String message;

	public static ResponseCode fromCode(String code) {
		for (ResponseCode responseCode : ResponseCode.values()) {
			if (responseCode.getCode().equals(code)) {
				return responseCode;
			}
		}
		throw new IndexOutOfBoundsException("No such code for ResponseCode: " + code);
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
