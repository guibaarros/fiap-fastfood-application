
package com.guibaarros.fiap.postech.fastfood.domain.repository.product;

import com.guibaarros.fiap.postech.fastfood.domain.entities.product.ProductValueObject;

public interface ValidateProductValueObjectPort {
    boolean existsProductValueObject(ProductValueObject productValueObject);
}
