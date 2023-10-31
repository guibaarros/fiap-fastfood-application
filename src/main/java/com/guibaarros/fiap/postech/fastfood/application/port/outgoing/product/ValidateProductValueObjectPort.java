
package com.guibaarros.fiap.postech.fastfood.application.port.outgoing.product;

import com.guibaarros.fiap.postech.fastfood.application.domain.product.ProductValueObject;

public interface ValidateProductValueObjectPort {
    boolean existsProductValueObject(ProductValueObject productValueObject);
}
