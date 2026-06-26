package com.petfriends.almoxarifado.service;

import com.petfriends.almoxarifado.events.PedidoEnviadoParaAlmoxarifadoEvent;
import com.petfriends.almoxarifado.events.PedidoDespachadoParaTransporteEvent;
import com.petfriends.almoxarifado.model.EnderecoEntrega;
import com.petfriends.almoxarifado.model.ItemPreparacao;
import com.petfriends.almoxarifado.model.PreparacaoPedido;
import com.petfriends.almoxarifado.repository.PreparacaoPedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlmoxarifadoEventService {

    private static final Logger logger = LoggerFactory.getLogger(AlmoxarifadoEventService.class);

    private final PreparacaoPedidoRepository preparacaoPedidoRepository;
    private final StreamBridge streamBridge;

    public AlmoxarifadoEventService(PreparacaoPedidoRepository preparacaoPedidoRepository, StreamBridge streamBridge) {
        this.preparacaoPedidoRepository = preparacaoPedidoRepository;
        this.streamBridge = streamBridge;
    }

    @Transactional
    public void processarPedidoRecebido(PedidoEnviadoParaAlmoxarifadoEvent evento) {
        if (preparacaoPedidoRepository.findByPedidoId(evento.getPedidoId()).isPresent()) {
            logger.warn("Pedido {} já foi processado. Ignorando.", evento.getPedidoId());
            return;
        }

        EnderecoEntrega endereco = new EnderecoEntrega(
                evento.getEnderecoEntrega().getLogradouro(),
                evento.getEnderecoEntrega().getNumero(),
                evento.getEnderecoEntrega().getComplemento(),
                evento.getEnderecoEntrega().getBairro(),
                evento.getEnderecoEntrega().getCidade(),
                evento.getEnderecoEntrega().getEstado(),
                evento.getEnderecoEntrega().getCep()
        );

        List<ItemPreparacao> itens = evento.getItens().stream()
                .map(item -> new ItemPreparacao(item.getProdutoId(), item.getNomeProduto(), item.getQuantidade()))
                .collect(Collectors.toList());

        PreparacaoPedido preparacao = new PreparacaoPedido(evento.getPedidoId(), endereco, itens);
        preparacaoPedidoRepository.save(preparacao);

        logger.info("PreparacaoPedido criada. Pedido ID: {}", evento.getPedidoId());

        simularPreparacaoEDespacho(preparacao, evento);
    }

    private void simularPreparacaoEDespacho(PreparacaoPedido preparacao, PedidoEnviadoParaAlmoxarifadoEvent eventoOriginal) {
        logger.info("Iniciando simulação de preparação e despacho do pedido {}...", preparacao.getPedidoId());
        
        preparacao.iniciarPreparacao("Operador Teste");
        preparacao.concluirPreparacao();
        preparacao.despachar();
        preparacaoPedidoRepository.save(preparacao);

        PedidoDespachadoParaTransporteEvent despachoEvent = new PedidoDespachadoParaTransporteEvent();
        despachoEvent.setPedidoId(preparacao.getPedidoId());
        despachoEvent.setClienteId(eventoOriginal.getClienteId());
        despachoEvent.setNomeCliente(eventoOriginal.getNomeCliente());
        despachoEvent.setTelefoneCliente("11999999999");
        despachoEvent.setEnderecoEntrega(eventoOriginal.getEnderecoEntrega());
        despachoEvent.setQuantidadeVolumes(preparacao.getItens().size());
        despachoEvent.setPesoTotal(10.5);
        despachoEvent.setDataDespacho(preparacao.getDataDespacho());
        despachoEvent.setObservacoes("Despacho simulado via Almoxarifado.");

        streamBridge.send("despacharPedido-out-0", despachoEvent);
        logger.info("Pedido {} despachado! Evento gerado para transporte.", preparacao.getPedidoId());
    }
}
