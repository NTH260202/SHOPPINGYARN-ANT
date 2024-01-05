package com.artiexh.api.base.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "Tham số không hợp lệ"),
	ENUM_CONVERT(HttpStatus.BAD_REQUEST, "Enum không hợp lệ"),
	OPERATION_UNSUPPORTED(HttpStatus.NOT_IMPLEMENTED, "Hành động không được hỗ trợ"),
	ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "Thực thể không tồn tại"),
	USER_NO_ROLE(HttpStatus.UNAUTHORIZED, "Người dùng không có role"),
	USER_NO_USERNAME(HttpStatus.UNAUTHORIZED, "Username không tồn tại"),
	//Registration
	USER_NAME_EXISTED(HttpStatus.BAD_REQUEST, "Username này đã tồn tại"),
	PASSWORD_PROVIDER_SUB_NOT_FOUND(HttpStatus.BAD_REQUEST, "Request has no password or invalid provider sub"),
	ARTIST_REGISTRATION_NOT_ALLOWED(HttpStatus.FORBIDDEN, "Chỉ có tài khoản người dùng mới có thể đăng kí làm Artist"),
	STAFF_REGISTRATION_NOT_ALLOWED(HttpStatus.FORBIDDEN, "Chỉ có tài khoản quản trị viên  có thể tạo nhân viên"),
	//Product
	PRODUCT_EXISTED(HttpStatus.BAD_REQUEST, "Product is existed with Id: "),
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Product is not found"),
	PRODUCT_CURRENCY_INVALID(HttpStatus.BAD_REQUEST, "Product currency is invalid"),
	UNAVAILABLE_PRODUCT(HttpStatus.BAD_REQUEST, "Hiện không thể đặt hàng sản phẩm "),
	PAYMENT_METHOD_UNACCEPTED(HttpStatus.BAD_REQUEST, "Hiện tại không hỗ trợ phương thưc thanh toán cho sản phẩm "),
	OFF_PAYMENT_TIME(HttpStatus.BAD_REQUEST, "Hệ thống đang bảo trì, không thể thanh toán đơn hàng"),
	LIMIT_PER_ORDER(HttpStatus.BAD_REQUEST, "có số lượng sản phẩm trong đơn hàng vượt quá số lượng cho phép "),

	//Product Inventory
	QUANTITY_INVALID(HttpStatus.BAD_REQUEST, "Sản phẩm đã bán nhiều hơn số lượng sản phẩm yêu cầu"),
	QUANTITY_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "Không đủ số lượng sản phẩm để thực hiện hành động này"),
	PRODUCT_INVENTORY_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "Không tìn thấy thông tin sản phẩm trong kho"),
	PRODUCT_INVENTORY_OWNER_INVALID(HttpStatus.BAD_REQUEST, "Người sỡ hữu chiến dịch bán không sở hữu sản phẩm"),
	PRODUCT_CODE_DUPLICATED(HttpStatus.BAD_REQUEST, "Mã sản phẩm đã bị trùng lặp. Vui lòng kiểm tra lại"),
	//Product In Sale
	PRODUCT_IN_SALE_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm bán"),
	PRODUCT_IN_SALE_INVALID(HttpStatus.BAD_REQUEST, "Sản phẩm khng đủ thông tin hợp lệ"),
	//Attachment
	ATTACHMENT_NOT_FOUND(HttpStatus.OK, "Attachment is not found"),

	//Category
	CATEGORY_NOT_FOUND(HttpStatus.OK, "Category is not found"),

	//User
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User is not found"),
	USER_NOT_ALLOWED(HttpStatus.FORBIDDEN, "User is not allowed to perform this action"),

	//USER_ADDRESS
	USER_ADDRESS_NOT_FOUND(HttpStatus.BAD_REQUEST, "Không tìm thấy địa chỉ giao hàng của bạn"),

	//Artist
	ARTIST_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "Không tìm thấy thông tin artist"),
	ARTIST_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy thông tin artist"),
	ARTIST_NOT_VALID(HttpStatus.BAD_REQUEST, "Artist is not valid"),
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy bài đăng"),

	//Cart
	UPDATE_CART_ITEM_FAILED(HttpStatus.BAD_REQUEST, "Bạn không thể thêm sản phẩm của chính bạn vào giỏ hàng"),
	INVALID_ITEM(HttpStatus.BAD_REQUEST, "Sản phẩm trong giỏ hàng không hợp lệ"),

	//Order
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Đơn hàng không tồn tại"),
	ORDER_IS_NOT_ALLOWED(HttpStatus.FORBIDDEN, "Bạn không thể xem thông tin đơn hàng này"),
	ORDER_STATUS_NOT_ALLOWED(HttpStatus.FORBIDDEN, "Chỉ có quản trị viên hoặc chủ đơn hàng mới có thể hủy đơn"),
	CREATE_GHTK_ORDER_FAILED(HttpStatus.BAD_REQUEST, "Không thể tạo đơn vận chuyển Giao Hàng Tiết Kiệm"),
	CANCEL_GHTK_ORDER_FAILED(HttpStatus.BAD_REQUEST, "Không thể hủy đơn vận chuyển Giao Hàng Tiết Kiệm"),
	GET_GHTK_SHIPPING_FEE_FAILED(HttpStatus.BAD_REQUEST, "Lỗi không tính được phí vận chuyển"),
	CANCEL_ORDER_FAIL(HttpStatus.BAD_REQUEST, "Hủy đơn hàng thất bại"),
	TIMEOUT_ORDER(HttpStatus.BAD_REQUEST, "Đơn hàng đã quá thời gian cho phép"),

	//Campaign Order
	CAMPAIGN_ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Đơn hàng không tồn tại"),
	UPDATE_CAMPAIGN_ORDER_STATUS_FAILED(HttpStatus.BAD_REQUEST, "Cập nhật trạng thái đơn hàng thất bại"),
	//Provider
	PROVIDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy nhà cung cấp"),
	PROVIDER_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "Không tìm thấy nhà cung cấp"),
	PROVIDER_INVALID(HttpStatus.BAD_REQUEST, "Some providers are not allowed to access or not found or duplicated"),
	PROVIDER_EXISTED(HttpStatus.BAD_REQUEST, "Nhà cung cấp với mã doanh nghiệp này tồn tại trong hệ thống"),
	PROVIDED_PRODUCT_INVALID(HttpStatus.BAD_REQUEST, "Provided product is invalid with Id: "),

	//Option - Option Value
	REQUIRED_OPTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "Required option is not found for this product "),
	OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Option is not found with Id: "),
	OPTION_VALUE_INVALID(HttpStatus.BAD_REQUEST, "Option value is not matched with Option Id: "),

	//Variant
	VARIANT_NOT_FOUND(HttpStatus.NOT_FOUND, "Variant is not found "),
	VARIANT_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "Không tìm thấy mẫu sản phẩm"),
	NOT_ALLOWED_VARIANT_UPDATED(HttpStatus.BAD_REQUEST, "Không cho phép cập nhật thông tin mẫu sản phẩm"),

	//Template
	TEMPLATE_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy mẫu san phẩm"),

	//Custom Product
	CUSTOM_PRODUCT_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "Không tìm thấy sản phẩm thiết kế "),
	CUSTOM_PRODUCT_OWNER_INVALID(HttpStatus.FORBIDDEN, "Bạn không sở hữu sản phẩm thiết kế "),
	UNSUPPORTED_PROVIDER(HttpStatus.BAD_REQUEST, "Bản thiết kế không được nhà cung cấp hỗ trợ"),
	QUANTITY_RANGE_INVALID(HttpStatus.BAD_REQUEST, "Số lượng sản phẩm phải lớn hơn số lượng sản xuất tối thiểu"),
	PRICE_RANGE_INVALID(HttpStatus.BAD_REQUEST, "Giá sản phẩm phải lớn hơn giá sản xuất tối thiểu"),
	LOCKED_CUSTOM_PRODUCT(HttpStatus.BAD_REQUEST, "Thiết kế của bạn hiện tại đang thuộc chiến dịch khác"),
	DELETED_CUSTOM_PRODUCT(HttpStatus.BAD_REQUEST, "Thiết kế của bạn đã bị xóa"),
	COMBINATION_CODE_INVALID(HttpStatus.BAD_REQUEST, "Combination code không hợp lệ"),
	IMAGE_SET_POSITION_INVALID(HttpStatus.BAD_REQUEST, "Vị trí ảnh không hợp lệ"),
	IMAGE_SET_INVALID(HttpStatus.BAD_REQUEST, "Không thể tạo image set mà không có combination code"),
	MOCKUP_IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "Không tìm thấy ảnh thiết kế"),
	MANUFACTURING_IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "Không tìm thấy ảnh sản xuất"),
	CUSTOM_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm bản thiết kế của bạn"),
	//Media
	UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Upload file is failed!"),
	DOWNLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Download file is failed!"),
	MEDIA_NOT_FOUND(HttpStatus.BAD_REQUEST, "Không tìm thấy ảnh"),
	DOWNLOAD_NOT_ALLOWED(HttpStatus.FORBIDDEN, "You are not allowed to download this file"),
	OWNER_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "Shared User can not be owner"),
	//AUTH
	ACCOUNT_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "Can not get account information from request"),

	//Account
	ROOT_ACCOUNT_NOT_ALLOWED_UPDATED(HttpStatus.FORBIDDEN, "Tài khoản của quản trị viên không được phép cập nhật trạng thái"),
	ACCOUNT_NOT_ALLOWED_UPDATED(HttpStatus.FORBIDDEN, "Bạn không được phép cập nhật trạng thái tài khoản này"),
	USER_STATUS_NOT_ALLOWED_UPDATED(HttpStatus.BAD_REQUEST, "Bạn không thể cập nhật trạng thái này cho tài khoản"),
	//Config
	ARTIEXH_CONFIG_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Configuration is not found"),

	//CAMPAIGN
	FINALIZED_CAMPAIGN_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "Chiến dịch chỉ có thể xác nhận lần cuối sau khi chiến dịch được chấp nhận và không kết thúc"),
	CAMPAIGN_REQUEST_FINALIZED(HttpStatus.BAD_REQUEST, "Chiến dịch đã được xác nhận lần cuối"),
	FINALIZED_CAMPAIGN_PRODUCT_VALIDATION(HttpStatus.BAD_REQUEST, "Tất cả sản phẩm của chiến dịch phải được xác nhận lần cuối"),
	PRODUCT_CAMPAIGN_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST, "Không tìm thấy sản phẩm "),
	PRODUCT_CAMPAIGN_QUANTITY_VALIDATION(HttpStatus.BAD_REQUEST, "Sản phẩm phải có thông tin số lượng"),
	PRODUCT_CAMPAIGN_PRICE_VALIDATION(HttpStatus.BAD_REQUEST, "Sản phẩm phải có thông tin giá"),
	PRODUCT_CAMPAIGN_VALIDATION(HttpStatus.BAD_REQUEST, "Chiến dịch phải có sản phẩm"),
	FROM_TO_VALIDATION(HttpStatus.BAD_REQUEST, "Thời gian mở bán và kết thúc chiến dịch phải được cập nhật"),
	UPDATE_CAMPAIGN_STATUS_FAILED(HttpStatus.BAD_REQUEST, "Cập nhật trạng thái chiến dịch không thành công"),
	CAMPAIGN_UNPUBLISHED(HttpStatus.BAD_REQUEST, "You are not allowed to view unpublished campaign"),
	CAMPAIGN_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy yêu cầu chiến dịch"),
	CAMPAIGN_REQUEST_NOT_FINALIZED(HttpStatus.BAD_REQUEST, "Yêu cầu chiến dịch phải được xác nhận lần cuối trước khi tạo chiến dịch bán"),
	CAMPAIGN_REQUEST_USED(HttpStatus.BAD_REQUEST, "Đã có chiến dịch bán từ yêu cầu campaign này"),
	PRODUCT_FINALIZED_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "Xác nhận yêu cầu chiến dịch phải có đủ thông tin của tất cả các sản phẩm"),
	CAMPAIGN_OWNER_INVALID(HttpStatus.FORBIDDEN, "Bạn không sở hữu chiến dịch này"),
	INVALID_PUBLIC_CAMPAIGN_OWNER(HttpStatus.FORBIDDEN, "Người sở hữu chiến dịch không hợp lệ"),
	UPDATE_CAMPAIGN_REQUEST(HttpStatus.BAD_REQUEST, "Yêu cầu chiến dịch chỉ có thể cập nhật thông tin khi ở trạng thái DRAFT hoặc REQUEST_CHANGE"),
	//CAMPAIGN_SALE
	CAMPAIGN_SALE_UNOPENED(HttpStatus.BAD_REQUEST, "Chiến dịch hiện không cho phép đặt hàng sản phẩm"),
	ADD_PRODUCT_CAMPAIGN_SALE_FAILED(HttpStatus.BAD_REQUEST, "Không thể thêm sản phẩm khi chiến dịch không ở trạng thái DRAFT"),
	UPDATE_PRODUCT_CAMPAIGN_SALE_FAILED(HttpStatus.BAD_REQUEST, "Không thể cập nhật sản phẩm khi chiến dịch ở trạng thái CLOSED"),
	UPDATE_PRODUCT_CAMPAIGN_SALE_FROM_REQUEST_FAILED(HttpStatus.BAD_REQUEST, "Không thể cập nhật sản phẩm khi chiến dịch được tạo từ yêu cầu chiến dịch"),
	ADD_PRODUCT_CAMPAIGN_SALE_FROM_REQUEST_FAILED(HttpStatus.BAD_REQUEST, "Không thể thêm sản phẩm vào chiến dịch được tạo từ yêu cầu chiến dịch"),
	DELETE_PRODUCT_CAMPAIGN_SALE_FAILED(HttpStatus.BAD_REQUEST, "Không thể xóa sản phẩm khi chiến dịch không ở trạng thái DRAFT"),
	DELETE_PRODUCT_CAMPAIGN_SALE_FROM_REQUEST_FAILED(HttpStatus.BAD_REQUEST, "Không thể xóa sản phẩm vào chiến dịch được tạo từ yêu cầu chiến dịch"),
	UPDATE_SALE_CAMPAIGN_FAILED(HttpStatus.BAD_REQUEST, "Không thể cập nhật thông tin chiến dịch bán sau khi chiến dịch kết thúc"),
	PUBLIC_DATE_INVALID(HttpStatus.BAD_REQUEST, "Ngày giới thiệu chiến dịch phải diễn ra trước hoặc cùng thời điểm với ngày mở bán"),
	UPDATE_FROM_FAILED(HttpStatus.BAD_REQUEST, "Không thể cập nhật thời gian mở bán sau khi chiến dịch bán đã mở bán"),
	UPDATE_PUBLIC_DATE_FAILED(HttpStatus.BAD_REQUEST, "Không thể cập nhật thời gian giới thiệu sau khi chiến dịch bán đã mở bán"),
	FROM_DATE_INVALID(HttpStatus.BAD_REQUEST, "Ngày mở bán chiến dịch phải diễn ra trước ngày kết thúc chiến dịch"),
	NOT_ALLOWED_OWNER_UPDATED(HttpStatus.BAD_REQUEST, "Không cho phép cập nhật thông tin người sở hữu"),
	NOT_ALLOWED_CLOSED_CAMPAIGN(HttpStatus.BAD_REQUEST, "Không cho phép đóng campaign"),
	NOT_ALLOWED_DRAFT_CAMPAIGN(HttpStatus.BAD_REQUEST, "Không cho phép đưa campaign về trạng thái nháp"),
	OWNER_NOT_FOUND(HttpStatus.BAD_REQUEST, "Chiến dịch bán phải có người sở hữu trước khi thêm sản phẩm"),
	CAMPAIGN_SALE_NOT_FOUND(HttpStatus.NOT_FOUND, "Chiến dịch bán hàng này không tồn tại");
	private final HttpStatus statusCode;
	private final String message;

	ErrorCode(HttpStatus statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}

	public HttpStatus getCode() {
		return statusCode;
	}

	public String getMessage() {
		return message;
	}

}
