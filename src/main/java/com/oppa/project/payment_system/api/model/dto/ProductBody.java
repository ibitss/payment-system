package com.oppa.project.payment_system.api.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductBody {

    private Long id;

    @NotNull(message = "Product name is required")
    @NotBlank(message = "Product name is required")
    private String name;

    @NotNull(message = "Minimum value is required")
    private Double minValue;

    @NotNull(message = "Maximum value is required")
    private Double maxValue;
}
