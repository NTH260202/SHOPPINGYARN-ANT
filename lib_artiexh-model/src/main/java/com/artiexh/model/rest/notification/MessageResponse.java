package com.artiexh.model.rest.notification;

import com.artiexh.data.jpa.entity.embededmodel.ReferenceData;
import com.artiexh.model.domain.NotificationType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	private String title;
	private String content;
	private NotificationType type;
	private Instant readAt;
	private Instant createdDate;
	private Instant modifiedDate;
	private ReferenceData referenceData;
}
