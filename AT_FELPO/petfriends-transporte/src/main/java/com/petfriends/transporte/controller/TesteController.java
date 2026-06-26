package com.petfriends.transporte.controller;

import com.petfriends.transporte.events.PedidoDespachadoParaTransporteEvent;
import com.petfriends.transporte.model.Entrega;
import com.petfriends.transporte.repository.EntregaRepository;
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
@RequestMapping("/api/transporte")
public class TesteController {

    private final StreamBridge streamBridge;
    private final EntregaRepository repository;

    public TesteController(StreamBridge streamBridge, EntregaRepository repository) {
        this.streamBridge = streamBridge;
        this.repository = repository;
    }


    @PostMapping("/simular-despacho")
    public ResponseEntity<String> simularDespacho(@RequestBody PedidoDespachadoParaTransporteEvent evento) {
        streamBridge.send("processarDespacho-in-0", evento);
        return ResponseEntity.ok("Evento simulado enviado para a fila 'pedido-despachado'. Verifique os logs do Transporte!");
    }


    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Entrega> buscarEntrega(@PathVariable Long pedidoId) {
        Optional<Entrega> entrega = repository.findByPedidoId(pedidoId);
        return entrega.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
}
