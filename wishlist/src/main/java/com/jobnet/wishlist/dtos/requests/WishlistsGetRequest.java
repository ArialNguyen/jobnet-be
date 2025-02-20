package com.jobnet.wishlist.dtos.requests;

import com.jobnet.common.utils.pagination.PaginationRequest;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class WishlistsGetRequest extends PaginationRequest {
}
