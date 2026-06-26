package com.petfriends.transporte.service;

import com.petfriends.transporte.events.PedidoDespachadoParaTransporteEvent;
import com.petfriends.transporte.model.EnderecoDestino;
import com.petfriends.transporte.model.Entrega;
import com.petfriends.transporte.repository.EntregaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TransporteEventService {

    private static final Logger logger = LoggerFactory.getLogger(TransporteEventService.class);

    private final EntregaRepository entregaRepository;

    public TransporteEventService(EntregaRepository entregaRepository) {
        this.entregaRepository = entregaRepository;
    }

    @Transactional
    public void processarPedidoDespachado(PedidoDespachadoParaTransporteEvent evento) {

        if (entregaRepository.findByPedidoId(evento.getPedidoId()).isPresent()) {
            logger.warn("Entrega para o Pedido {} já existe. Ignorando.", evento.getPedidoId());
            return;
        }

        EnderecoDestino endereco = new EnderecoDestino(
                evento.getEnderecoEntrega().getLogradouro(),
                evento.getEnderecoEntrega().getNumero(),
                evento.getEnderecoEntrega().getComplemento(),
                evento.getEnderecoEntrega().getBairro(),
                evento.getEnderecoEntrega().getCidade(),
                evento.getEnderecoEntrega().getEstado(),
                evento.getEnderecoEntrega().getCep()
        );

        String codigoRastreio = "PF" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Entrega entrega = new Entrega(
                evento.getPedidoId(),
                codigoRastreio,
                endereco,
                "Transportadora PetFriends"
        );

        entregaRepository.save(entrega);

        logger.info("Entrega criada! Pedido ID: {} | Código de Rastreio: {}", evento.getPedidoId(), codigoRastreio);
    }
}
