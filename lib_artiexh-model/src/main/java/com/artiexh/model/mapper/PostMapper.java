package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.PostEntity;
import com.artiexh.model.domain.Post;
import com.artiexh.model.rest.post.PostDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {AccountMapper.class, ArtistMapper.class, DateTimeMapper.class, ProductAttachMapper.class}
)
public interface PostMapper {
	@Mapping(target = "owner", source = "owner", qualifiedByName = "basicArtistInfo")
	Post entityToDomain(PostEntity entity);

	PostDetail domainToDetail(Post post);

	Post detailToDomain(PostDetail postDetail);

	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "modifiedDate", ignore = true)
	PostEntity domainToEntity(Post post);
}
