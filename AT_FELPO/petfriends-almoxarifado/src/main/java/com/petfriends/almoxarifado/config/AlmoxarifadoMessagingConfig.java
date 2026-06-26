package com.petfriends.almoxarifado.config;

import com.petfriends.almoxarifado.events.PedidoEnviadoParaAlmoxarifadoEvent;
import com.petfriends.almoxarifado.service.AlmoxarifadoEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class AlmoxarifadoMessagingConfig {

    private static final Logger logger = LoggerFactory.getLogger(AlmoxarifadoMessagingConfig.class);

    private final AlmoxarifadoEventService almoxarifadoEventService;

    public AlmoxarifadoMessagingConfig(AlmoxarifadoEventService almoxarifadoEventService) {
        this.almoxarifadoEventService = almoxarifadoEventService;
    }

    @Bean
    public Consumer<PedidoEnviadoParaAlmoxarifadoEvent> processarPedido() {
        return evento -> {
            logger.info(">>> MENSAGEM RECEBIDA NO ALMOXARIFADO | Pedido ID: {}", evento.getPedidoId());
            try {
                almoxarifadoEventService.processarPedidoRecebido(evento);
            } catch (Exception e) {
                logger.error("Erro ao processar", e);
                throw e;
            }
        };
    }
}
