package gallery.backend.dto;

import lombok.Getter;

/**
 * Post mapping 시 받아올 것들...
 */
@Getter
public class OrderDto {

    private String name;
    private String address;
    private String payment;
    private String cardNumber;
    private String items;

}
