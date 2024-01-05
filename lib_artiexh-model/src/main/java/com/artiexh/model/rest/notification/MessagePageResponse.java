package com.artiexh.model.rest.notification;

import com.artiexh.model.rest.PageResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessagePageResponse<T> extends PageResponse<T> {
	private int unreadCount;

	public MessagePageResponse(Page<T> page, int unreadCount) {
		super(page);
		this.unreadCount = unreadCount;
	}
}
