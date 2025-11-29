package com.Aero.Beauty.Entities;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddress {

    private String street;
    private String city;
    private String zip;
    private String country;
    private String phone;
}
