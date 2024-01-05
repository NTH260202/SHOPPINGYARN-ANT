package com.artiexh.model.mapper;

import com.artiexh.model.domain.CampaignType;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CampaignTypeMapper {
	default Byte toValue(CampaignType type) {
		return type.getByteValue();
	}

	default CampaignType campaignTypeFrom(byte value) {
		return CampaignType.fromValue(value);
	}
}
