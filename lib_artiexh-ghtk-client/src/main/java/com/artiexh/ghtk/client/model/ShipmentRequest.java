package com.artiexh.ghtk.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentRequest {
	private String hash;
	private String label_id;
	private String partner_id;
	private Integer status_id;
	private String action_time;
	private String reason_code;
	private String reason;
	private Float weight;
	private Integer fee;
	private Integer return_part_package;
}
