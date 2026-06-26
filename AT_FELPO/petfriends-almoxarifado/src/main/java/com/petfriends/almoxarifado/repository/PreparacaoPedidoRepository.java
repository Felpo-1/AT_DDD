package com.petfriends.almoxarifado.repository;

import com.petfriends.almoxarifado.model.PreparacaoPedido;
import com.petfriends.almoxarifado.model.StatusPreparacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreparacaoPedidoRepository extends JpaRepository<PreparacaoPedido, Long> {
    Optional<PreparacaoPedido> findByPedidoId(Long pedidoId);
    List<PreparacaoPedido> findByStatus(StatusPreparacao status);
}
