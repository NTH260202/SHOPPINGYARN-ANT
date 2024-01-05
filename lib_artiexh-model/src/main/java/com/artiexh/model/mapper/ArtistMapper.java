package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.ArtistEntity;
import com.artiexh.model.domain.Artist;
import com.artiexh.model.rest.account.AccountProfile;
import com.artiexh.model.rest.artist.request.UpdateArtistProfileRequest;
import com.artiexh.model.rest.artist.response.ArtistProfileResponse;
import org.mapstruct.*;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {AccountMapper.class,
		ProductMapper.class,
		ProvinceMapper.class,
		ProductCategoryMapper.class,
		ProductTagMapper.class,
		ProductAttachMapper.class,
		ShopMapper.class,
		PasswordMapper.class,
		AddressMapper.class,
		CustomProductMapper.class,
		CampaignMapper.class,
		CampaignTypeMapper.class,
		CampaignSaleMapper.class
	}
)
public abstract class ArtistMapper {

	@Named("basicArtistInfo")
	@Mapping(target = "subscriptionsTo", ignore = true)
	@Mapping(target = "shoppingCart", ignore = true)
	@Mapping(target = "products", ignore = true)
	@Mapping(target = "subscriptionsFrom", ignore = true)
	public abstract Artist basicArtistInfo(ArtistEntity artist);

	public abstract AccountProfile entityToAccountProfile(ArtistEntity userEntity);

	@Mapping(target = "shoppingCart", ignore = true)
	@Mapping(target = "products", ignore = true)
	public abstract Artist entityToDomain(ArtistEntity userEntity);

	abstract ArtistEntity domainToEntity(Artist user);

	@Named("idToDomain")
	public abstract Artist idToDomain(Long id);

	@Mapping(target = "numOfSubscriptions", ignore = true)
	@Mapping(target = "subscriptionsFrom", ignore = true)
	public abstract ArtistProfileResponse entityToProfileResponse(ArtistEntity entity);

	public abstract ArtistEntity requestToEntity(UpdateArtistProfileRequest request, @MappingTarget ArtistEntity entity);
}
