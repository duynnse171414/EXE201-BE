package com.example.web_petvibe.model.request;

import com.example.web_petvibe.entity.OrderDetails;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private Long accountId;
    private String shippingAddress;
    private String phoneContact;
    private String note;
    private List<OrderDetailsRequest> items;

    @Getter
    @Setter
    public static class OrderDetailsRequest {
        private Long productId;
        private Integer quantity;
    }
}
