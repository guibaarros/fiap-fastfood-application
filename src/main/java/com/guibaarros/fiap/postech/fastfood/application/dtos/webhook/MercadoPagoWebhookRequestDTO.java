package com.guibaarros.fiap.postech.fastfood.application.dtos.webhook;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MercadoPagoWebhookRequestDTO {
    private Long externalId;
    private String Status;
}
