package com.guibaarros.fiap.postech.fastfood.adapters.persistence.client;

import com.guibaarros.fiap.postech.fastfood.application.domain.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientJpaRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByCpf(Long cpf);

    boolean existsByCpfAndNameAndEmail(Long cpf, String name, String email);
}
