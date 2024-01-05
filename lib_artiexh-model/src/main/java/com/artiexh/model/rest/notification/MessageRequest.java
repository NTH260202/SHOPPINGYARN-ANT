package com.artiexh.model.rest.notification;

import com.artiexh.data.jpa.entity.embededmodel.ReferenceData;
import com.artiexh.model.domain.NotificationType;
import com.artiexh.model.domain.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
	@NotBlank
	private String title;
	@NotBlank
	private String content;
	@NotNull
	private Role group;
	private ReferenceData referenceData;
}
