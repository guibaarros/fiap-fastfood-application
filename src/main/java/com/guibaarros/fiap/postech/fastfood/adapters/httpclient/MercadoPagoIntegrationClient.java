package com.guibaarros.fiap.postech.fastfood.adapters.httpclient;

import com.guibaarros.fiap.postech.fastfood.adapters.httpclient.dto.PaymentServiceResponseDTO;
import com.guibaarros.fiap.postech.fastfood.application.port.outgoing.order.CreatePaymentServiceOrderPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

/*
 * Integração fake com o Mercado Pago *
 * TODO integrar com o ambiente de dev do Mercado Pago *
 */
@Component
@AllArgsConstructor
public class MercadoPagoIntegrationClient implements CreatePaymentServiceOrderPort {

    @Override
    public PaymentServiceResponseDTO createPaymentServiceOrder(final Long id, final BigDecimal totalAmount) {
        final PaymentServiceResponseDTO paymentServiceResponseDTO = new PaymentServiceResponseDTO();
        paymentServiceResponseDTO.setExternalId(id);
        paymentServiceResponseDTO.setQrData(
                "00020101021243650016COM.MERCADOLIBRE02013063638f1192a-5fd1-4180-a180-8bcae3556bc35204000053039865802" +
                "BR5925IZABEL AAAA DE MELO6007BARUERI62070503***63040B6D");
        return paymentServiceResponseDTO;
    }
}
