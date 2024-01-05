package com.artiexh.api.service.impl;

import com.artiexh.api.service.ArtistService;
import com.artiexh.api.service.CampaignOrderService;
import com.artiexh.api.service.PostService;
import com.artiexh.api.service.marketplace.ProductService;
import com.artiexh.data.jpa.entity.ArtistEntity;
import com.artiexh.data.jpa.entity.CampaignOrderEntity;
import com.artiexh.data.jpa.entity.SubscriptionEntity;
import com.artiexh.data.jpa.repository.ArtistRepository;
import com.artiexh.data.jpa.repository.CampaignOrderRepository;
import com.artiexh.data.jpa.repository.OrderHistoryRepository;
import com.artiexh.data.jpa.repository.SubscriptionRepository;
import com.artiexh.model.domain.Post;
import com.artiexh.model.mapper.ArtistMapper;
import com.artiexh.model.mapper.CampaignOrderMapper;
import com.artiexh.model.mapper.ProductMapper;
import com.artiexh.model.mapper.SubscriptionMapper;
import com.artiexh.model.rest.PageResponse;
import com.artiexh.model.rest.artist.request.UpdateArtistProfileRequest;
import com.artiexh.model.rest.artist.response.ArtistProfileResponse;
import com.artiexh.model.rest.order.user.response.CampaignOrderResponsePage;
import com.artiexh.model.rest.order.user.response.UserCampaignOrderDetailResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ArtistServiceImpl implements ArtistService {

	private final ArtistRepository artistRepository;
	private final SubscriptionRepository subscriptionRepository;
	private final CampaignOrderRepository campaignOrderRepository;
	private final OrderHistoryRepository orderHistoryRepository;
	private final ProductService productService;
	private final CampaignOrderService campaignOrderService;
	private final PostService postService;
	private final ProductMapper productMapper;
	private final CampaignOrderMapper campaignOrderMapper;
	private final ArtistMapper artistMapper;
	private final SubscriptionMapper subscriptionMapper;
	@Value("${artiexh.security.admin.id}")
	private Long rootAdminId;

	@Override
	public ArtistProfileResponse getProfile(long id) {
		ArtistEntity artistEntity = artistRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Artist id: " + id + " not found"));
		return getProfile(artistEntity);
	}

	@Override
	public Page<ArtistProfileResponse> getAllProfile(Specification<ArtistEntity> specification, Pageable pageable) {
		return artistRepository.findAll(specification, pageable).map(artistMapper::entityToProfileResponse);
	}

	@Override
	public ArtistProfileResponse getProfile(String username) {
		ArtistEntity artistEntity = artistRepository.findByUsername(username)
			.orElseThrow(() -> new EntityNotFoundException("Artist: " + username + " not found"));
		return getProfile(artistEntity);
	}

	private ArtistProfileResponse getProfile(ArtistEntity artistEntity) {
		ArtistProfileResponse result = artistMapper.entityToProfileResponse(artistEntity);

		Pageable pageable = PageRequest.of(1, 10);
		Page<SubscriptionEntity> subscriptionPage = subscriptionRepository.findAllByArtistId(artistEntity.getId(), pageable);

		result.setNumOfSubscriptions(subscriptionPage.getTotalElements());
		result.setSubscriptionsFrom(subscriptionPage.map(subscriptionMapper::subscriptionEntityToArtistSubscription).toSet());

		return result;
	}

//	@Override
//	public PageResponse<ProductResponse> getAllProducts(Query query, Pageable pageable) {
//		Page<Product> productPage = productService.getInPage(query, pageable);
//		return new PageResponse<>(productMapper.productPageToProductResponsePage(productPage));
//	}

	@Override
	public UserCampaignOrderDetailResponse getOrderById(Long orderId, Long artistId) {
		return campaignOrderService.getOrderByIdAndOwner(orderId, artistId);
	}

	@Override
	public PageResponse<CampaignOrderResponsePage> getAllOrder(Specification<CampaignOrderEntity> specification,
															   Pageable pageable) {
		Page<CampaignOrderResponsePage> orderPage = campaignOrderService.getCampaignOrderInPage(specification, pageable);
		return new PageResponse<>(orderPage);
	}

	@Override
	public Page<Post> getArtistPost(Long artistId, Pageable pageable) {
		if (!artistRepository.existsById(artistId)) {
			throw new EntityNotFoundException("Arist id: " + artistId + " not existed");
		}
		return postService.getAllPost(artistId, pageable);
	}

	@Override
	@Transactional
	public ArtistProfileResponse updateArtistProfile(Long artistId, UpdateArtistProfileRequest request) {
		ArtistEntity entity = artistRepository.findById(artistId).orElseThrow(EntityNotFoundException::new);

		entity = artistMapper.requestToEntity(request, entity);

		artistRepository.save(entity);

		ArtistProfileResponse profile = artistMapper.entityToProfileResponse(entity);
		profile.setNumOfSubscriptions(subscriptionRepository.countByUserId(entity.getId()));
		return profile;
	}
}
