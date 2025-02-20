package com.jobnet.wishlist.services.Impl;

import com.jobnet.clients.post.PostClient;
import com.jobnet.clients.post.PostResponse;
import com.jobnet.common.utils.pagination.PaginationResponse;
import com.jobnet.common.exceptions.DataIntegrityViolationException;
import com.jobnet.common.exceptions.ResourceNotFoundException;
import com.jobnet.common.utils.pagination.PaginationUtil;
import com.jobnet.wishlist.dtos.requests.WishlistsGetRequest;
import com.jobnet.wishlist.dtos.requests.WishlistRequest;
import com.jobnet.wishlist.dtos.responses.WishlistResponse;
import com.jobnet.wishlist.mappers.WishlistMapper;
import com.jobnet.wishlist.models.Wishlist;
import com.jobnet.wishlist.repositories.WishlistRepository;
import com.jobnet.wishlist.services.IWishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistService implements IWishlistService {

    private final WishlistRepository wishlistRepository;
    private final PostClient postClient;
    private final WishlistMapper wishlistMapper;

    @Override
    @Cacheable(value = "wishlists", keyGenerator = "wishlistsKeyGenerator", unless = "#result.totalElements == 0")
    public PaginationResponse<List<WishlistResponse>> getWishlistsByUserId(String userId, WishlistsGetRequest request) {
        Pageable pageable = PaginationUtil.getPageable(request);
        Page<Wishlist> page = wishlistRepository.findAllByUserId(userId, pageable);
        PaginationResponse<List<WishlistResponse>> response =
            PaginationUtil.getPaginationResponse(page, this::getWishlistResponse);

        log.info("Get wishlist by auth - userId={}, request={}", userId, request);
        return response;
    }

    @Override
    @CacheEvict(value = "wishlists", allEntries = true)
    public WishlistResponse createWishlist(String userId, WishlistRequest wishlistRequest) {
        if (wishlistRepository.existsByUserIdAndPostId(
            userId,
            wishlistRequest.getPostId()
        ))
            throw new DataIntegrityViolationException("Wishlist already exists.");

        PostResponse postResponse = postClient.getPostById(wishlistRequest.getPostId());

        Wishlist _wishlist = wishlistMapper.convertToWishlist.apply(wishlistRequest);
        _wishlist.setUserId(userId);
        _wishlist.setCreatedAt(LocalDate.now());
        Wishlist wishlist = wishlistRepository.save(_wishlist);

        WishlistResponse wishlistResponse = wishlistMapper.convertToWishlistResponse.apply(wishlist);
        wishlistResponse.setUserId(wishlist.getUserId());
        wishlistResponse.setPost(postResponse);

        log.info("Create wishlist by auth - userId={}, request={}: {}",
            userId, wishlistRequest, wishlistResponse
        );
        return wishlistResponse;
    }

    @Override
    @CacheEvict(value = "wishlists", allEntries = true)
    public void deleteWishlist(String userId, WishlistRequest wishlistRequest) {

        if (!wishlistRepository.existsByUserIdAndPostId(
            userId,
            wishlistRequest.getPostId()
        ))
            throw new ResourceNotFoundException("Wishlist not found.");

        log.info("Delete wishlist by auth - userId={}: {}",
            userId, wishlistRequest
        );
        wishlistRepository.deleteByUserIdAndPostId(
            userId,
            wishlistRequest.getPostId()
        );
    }

    @Override
    public boolean existsWishlist(String jobSeekerId, String postId) {
        return wishlistRepository.existsByUserIdAndPostId(jobSeekerId, postId);
    }

    private WishlistResponse getWishlistResponse(Wishlist wishlist) {
        PostResponse postResponse = postClient.getPostById(wishlist.getPostId());
        WishlistResponse wishlistResponse = wishlistMapper.convertToWishlistResponse.apply(wishlist);
        wishlistResponse.setUserId(wishlist.getUserId());
        wishlistResponse.setPost(postResponse);
        return wishlistResponse;
    }
}
