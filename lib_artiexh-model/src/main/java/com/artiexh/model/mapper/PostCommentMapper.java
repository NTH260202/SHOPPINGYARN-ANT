package com.artiexh.model.mapper;

import com.artiexh.data.jpa.entity.PostCommentEntity;
import com.artiexh.model.domain.PostComment;
import com.artiexh.model.rest.post.PostCommentDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {
		PostMapper.class,
		AccountMapper.class,
		ArtistMapper.class,
		DateTimeMapper.class,
		UserMapper.class}
)
public interface PostCommentMapper {
	@Mapping(target = "owner", source = "owner", qualifiedByName = "entityToBasicUser")
	@Mapping(target = "post", ignore = true)
	PostComment entityToDomain(PostCommentEntity entity);

	@Mapping(target = "owner", source = "owner", qualifiedByName = "domainToAccountProfile")
	PostCommentDetail domainToDetail(PostComment post);

	PostComment detailToDomain(PostCommentDetail postDetail);

	PostCommentEntity domainToEntity(PostComment post);
}
