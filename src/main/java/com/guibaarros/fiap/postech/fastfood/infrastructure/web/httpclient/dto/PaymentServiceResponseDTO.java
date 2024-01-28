package com.guibaarros.fiap.postech.fastfood.infrastructure.web.httpclient.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentServiceResponseDTO {
    private Long externalId;
    private String qrData;
}
