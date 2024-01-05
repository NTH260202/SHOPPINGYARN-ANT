package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.AccountEntity;
import com.artiexh.data.jpa.entity.ArtistEntity;
import com.artiexh.model.domain.Shop;
import com.artiexh.model.rest.address.AddressResponse;
import com.artiexh.model.rest.artist.request.RegistrationArtistRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {ArtistMapper.class, AddressMapper.class}
)
public interface ShopMapper {

	ArtistEntity domainToEntity(Shop shop);

	Shop requestToDomain(RegistrationArtistRequest request);

	@Named("basicShopInfo")
		//@Mapping(target = "owner", source = ".", qualifiedByName = "basicArtistInfo")
	Shop entityToDomain(ArtistEntity entity);

	//@Mapping(target = "owner", source = ".", qualifiedByName = "basicArtistInfo")
	Shop entityToDomain(AccountEntity entity);

	AddressResponse entityToShopAddressResponse(ArtistEntity entity);

}
