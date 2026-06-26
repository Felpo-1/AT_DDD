package com.petfriends.almoxarifado.controller;

import com.petfriends.almoxarifado.events.PedidoEnviadoParaAlmoxarifadoEvent;
import com.petfriends.almoxarifado.model.PreparacaoPedido;
import com.petfriends.almoxarifado.repository.PreparacaoPedidoRepository;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/almoxarifado")
public class TesteController {

    private final StreamBridge streamBridge;
    private final PreparacaoPedidoRepository repository;

    public TesteController(StreamBridge streamBridge, PreparacaoPedidoRepository repository) {
        this.streamBridge = streamBridge;
        this.repository = repository;
    }


    @PostMapping("/simular-pedido")
    public ResponseEntity<String> simularPedido(@RequestBody PedidoEnviadoParaAlmoxarifadoEvent evento) {
        streamBridge.send("processarPedido-in-0", evento);
        return ResponseEntity.ok("Pedido simulado enviado para a fila 'pedido-enviado'. Verifique os logs!");
    }


    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<PreparacaoPedido> buscarPreparacao(@PathVariable Long pedidoId) {
        Optional<PreparacaoPedido> preparacao = repository.findByPedidoId(pedidoId);
        return preparacao.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
}
