package com.petfriends.transporte.repository;

import com.petfriends.transporte.model.Entrega;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {
    Optional<Entrega> findByPedidoId(Long pedidoId);
    Optional<Entrega> findByCodigoRastreio(String codigoRastreio);
}
