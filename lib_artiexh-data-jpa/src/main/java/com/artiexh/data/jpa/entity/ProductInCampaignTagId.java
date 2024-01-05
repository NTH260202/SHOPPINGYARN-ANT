package com.artiexh.data.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInCampaignTagId implements Serializable {

	private Long productInCampaignId;

	private String name;

}