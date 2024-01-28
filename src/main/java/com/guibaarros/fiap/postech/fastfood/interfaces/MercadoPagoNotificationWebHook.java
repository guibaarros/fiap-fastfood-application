package com.guibaarros.fiap.postech.fastfood.interfaces;

import com.guibaarros.fiap.postech.fastfood.infrastructure.web.exceptionhandler.ErrorDTO;
import com.guibaarros.fiap.postech.fastfood.application.dtos.webhook.MercadoPagoWebhookRequestDTO;
import com.guibaarros.fiap.postech.fastfood.application.usecases.order.ConfirmPaymentUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mercadopago/webhook")
@AllArgsConstructor
@Tag(name = "Webhook de integração Fake com o Mercado Pago")
@Slf4j
public class MercadoPagoNotificationWebHook {

    private final ConfirmPaymentUseCase confirmPaymentUseCase;

    @Operation(summary = "Webhook de notificação de pagamento do pedido pelo Mercado Pago")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Pagamento do pedido confirmado"),
            @ApiResponse(responseCode = "400",
                    description = "Falha ao confirmar o pagamento do pedido",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @PostMapping
    public ResponseEntity<Void> confirmPaymentWebhookNotification(
            @RequestBody final MercadoPagoWebhookRequestDTO mercadoPagoWebhookRequestDTO) {

        confirmPaymentUseCase.confirmPayment(
                mercadoPagoWebhookRequestDTO.getExternalId(),
                mercadoPagoWebhookRequestDTO.getStatus()
        );

        return ResponseEntity.ok().build();
    }
}
