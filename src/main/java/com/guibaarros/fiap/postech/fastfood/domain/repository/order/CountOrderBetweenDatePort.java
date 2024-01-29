package com.guibaarros.fiap.postech.fastfood.domain.repository.order;

import java.time.LocalDateTime;

public interface CountOrderBetweenDatePort {
    int countOrderBetweenDate(LocalDateTime initialDate, LocalDateTime finalDate);
}
