package com.petfriends.transporte.config;

import com.petfriends.transporte.events.PedidoDespachadoParaTransporteEvent;
import com.petfriends.transporte.service.TransporteEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class TransporteMessagingConfig {

    private static final Logger logger = LoggerFactory.getLogger(TransporteMessagingConfig.class);

    private final TransporteEventService transporteEventService;

    public TransporteMessagingConfig(TransporteEventService transporteEventService) {
        this.transporteEventService = transporteEventService;
    }

    @Bean
    public Consumer<PedidoDespachadoParaTransporteEvent> processarDespacho() {
        return evento -> {
            logger.info(">>> MENSAGEM RECEBIDA NO TRANSPORTE | Pedido ID: {} DESPACHADO!", evento.getPedidoId());
            try {
                transporteEventService.processarPedidoDespachado(evento);
            } catch (Exception e) {
                logger.error("Erro ao processar despacho", e);
                throw e;
            }
        };
    }
}
