package com.guibaarros.fiap.postech.fastfood.infrastructure.persistence.client;

import com.guibaarros.fiap.postech.fastfood.domain.entities.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientJpaRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByCpf(Long cpf);

    boolean existsByCpfAndNameAndEmail(Long cpf, String name, String email);

    Optional<Client> findByEmail(String email);
}
