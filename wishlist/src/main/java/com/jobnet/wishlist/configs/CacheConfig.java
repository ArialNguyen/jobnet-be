package com.jobnet.wishlist.configs;

import com.jobnet.wishlist.dtos.requests.WishlistsGetRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Configuration
public class CacheConfig {

    @Bean
    public KeyGenerator wishlistsKeyGenerator() {
        return new KeyGenerator() {

            @NotNull
            @Override
            public Object generate(@NotNull Object target, @NotNull Method method, @NotNull Object... params) {
                if (params.length < 2 || !(params[1] instanceof WishlistsGetRequest filter)) {
                    throw new IllegalArgumentException("Expected userId and GetWishlistsFilter as parameters");
                }
                String userId = (String) params[0];

                // Generate a unique cache key based on userId and filter
                return generateKeyFromUserIdAndFilter(userId, filter);
            }
        };
    }

    private String generateKeyFromUserIdAndFilter(String userId, WishlistsGetRequest filter) {
        // Implement your logic to generate a unique cache key
        return "wishlists:" + userId + ":" + filter.toString();
    }
}
