package com.guibaarros.fiap.postech.fastfood.application.port.outgoing.order;

import java.time.LocalDateTime;

public interface CountOrderBetweenDatePort {
    int countOrderBetweenDate(LocalDateTime initialDate, LocalDateTime finalDate);
}
