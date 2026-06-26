# Respostas - Arquitetura de Microsserviços e DDD

## 1. Transformar monólitos em microsserviços eficazes, aplicando princípios de DDD e técnicas de decomposição

### Implemente a classe entity e seu respectivo repository que represente o agregado mais representativo do microsserviço PetFriends_Almoxarifado.
**Entity: `PreparacaoPedido.java`**
```java
package com.petfriends.almoxarifado.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class PreparacaoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pedidoId;
    
    @Embedded
    private EnderecoEntrega enderecoEntrega;
    
    @ElementCollection
    @CollectionTable(name = "itens_preparacao", joinColumns = @JoinColumn(name = "preparacao_id"))
    private List<ItemPreparacao> itens;

    @Enumerated(EnumType.STRING)
    private StatusPreparacao status = StatusPreparacao.NOVO;

    public PreparacaoPedido() {}
    public PreparacaoPedido(Long pedidoId, EnderecoEntrega enderecoEntrega, List<ItemPreparacao> itens) {
        this.pedidoId = pedidoId;
        this.enderecoEntrega = enderecoEntrega;
        this.itens = itens;
    }
    // Getters e setters omitidos por brevidade
}
```

**Repository: `PreparacaoPedidoRepository.java`**
```java
package com.petfriends.almoxarifado.repository;

import com.petfriends.almoxarifado.model.PreparacaoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PreparacaoPedidoRepository extends JpaRepository<PreparacaoPedido, Long> {
    Optional<PreparacaoPedido> findByPedidoId(Long pedidoId);
}
```

### Implemente um exemplo de Value Object a ser usado na classe entity da questão anterior.
**Value Object: `EnderecoEntrega.java`**
```java
package com.petfriends.almoxarifado.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class EnderecoEntrega {
    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    public EnderecoEntrega() {}
    // Um Value Object idealmente deve ser imutável (sem setters públicos) e ter equals/hashCode baseados em atributos
}
```

### Implemente a classe entity e seu respectivo repository que represente o agregado mais representativo do microsserviço PetFriends_Transporte.
**Entity: `Entrega.java`**
```java
package com.petfriends.almoxarifado.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pedidoId;
    
    @Column(unique = true)
    private String codigoRastreio;
    
    @Embedded
    private EnderecoDestino enderecoDestino;

    @Enumerated(EnumType.STRING)
    private StatusEntrega status = StatusEntrega.EM_TRANSITO;

    public Entrega() {}
    public Entrega(Long pedidoId, String codigoRastreio, EnderecoDestino enderecoDestino) {
        this.pedidoId = pedidoId;
        this.codigoRastreio = codigoRastreio;
        this.enderecoDestino = enderecoDestino;
    }
}
```

**Repository: `EntregaRepository.java`**
```java
package com.petfriends.transporte.repository;

import com.petfriends.transporte.model.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {
    Optional<Entrega> findByPedidoId(Long pedidoId);
}
```

### Implemente um exemplo de Value Object a ser usado na classe entity da questão anterior.
**Value Object: `EnderecoDestino.java`**
```java
package com.petfriends.transporte.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class EnderecoDestino {
    private String logradouro;
    private String numero;
    private String cep;

    protected EnderecoDestino() {}
    
    public EnderecoDestino(String logradouro, String numero, String cep) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.cep = cep;
    }
}
```

---

## 2. Projetar softwares usando "domain events"

### O módulo PetFriends_Web foi desenvolvido em ReactJS, acessando os microsserviços de Clientes, Produtos e Pedidos de forma síncrona, via REST API. Que funcionalidade síncrona executada pelo cliente é diretamente afetada pelos eventos de domínio?
A funcionalidade de **Acompanhamento/Rastreamento do Status do Pedido**. Quando o cliente acessa a tela de acompanhamento no ReactJS, ele consome uma API síncrona do microsserviço de Pedidos. Esse status exibido (Em Preparação, Despachado, Em Trânsito) é o reflexo direto dos eventos assíncronos que os módulos de Almoxarifado e Transporte processaram e notificaram em background.

### Explique de forma sucinta qual é a diferença de enviar eventos somente com o ID do agregado e enviar um payload completo?
- **Somente ID (Event Notification):** O evento carrega apenas o ID (ex: `pedidoId: 1`). Obriga o microsserviço consumidor a fazer uma requisição síncrona (REST) de volta ao emissor para buscar o restante dos dados. Reduz o tamanho da mensagem, mas aumenta o acoplamento e o tráfego de rede.
- **Payload completo (Event-Carried State Transfer):** O evento já carrega todos os dados que o consumidor vai precisar (ex: ID, itens, endereço de entrega). Elimina a necessidade de requisições adicionais, promovendo desacoplamento total, mas aumenta o tamanho da mensagem na fila do broker.

### Como você projetaria o evento a ser enviado pelo PetFriends_Pedido para o PetFriends_Almoxarifado?
Utilizaria a abordagem de **Event-Carried State Transfer**. O evento conteria o ID do Pedido, a lista completa dos itens (ID e quantidade) e o endereço de entrega completo, além de dados do cliente. Como o Almoxarifado precisa exatamente dessas informações para separar o pacote fisicamente, receber tudo via evento elimina o acoplamento síncrono com a API de Pedidos.

### Como você projetaria o evento a ser enviado pelo PetFriends_Pedido para o PetFriends_Transporte?
Também utilizaria um **Payload completo**. Para o transporte, os dados vitais seriam: ID do pedido, peso/volumes totais, endereço completo de destino e telefone de contato do cliente. Isso permite que a transportadora crie o pacote no sistema e gere o código de rastreio de forma independente.

---

## 3. Desenvolver microsserviços event-driven e padrões de comunicação assíncrona

### No microsserviço PetFriends_Almoxarifado, implemente a classe de configuração para o tratamento de mensagens.
```java
package com.petfriends.almoxarifado.config;

import com.petfriends.almoxarifado.events.PedidoEnviadoParaAlmoxarifadoEvent;
import com.petfriends.almoxarifado.service.AlmoxarifadoEventService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.function.Consumer;

@Configuration
public class AlmoxarifadoMessagingConfig {
    
    private final AlmoxarifadoEventService service;
    public AlmoxarifadoMessagingConfig(AlmoxarifadoEventService service) { this.service = service; }

    @Bean
    public Consumer<PedidoEnviadoParaAlmoxarifadoEvent> processarPedido() {
        return evento -> service.processarPedidoRecebido(evento);
    }
}
```

### No microsserviço PetFriends_Almoxarifado, implemente o serviço que receberá os eventos.
```java
package com.petfriends.almoxarifado.service;

import org.springframework.stereotype.Service;

@Service
public class AlmoxarifadoEventService {
    
    private final PreparacaoPedidoRepository repository;
    public AlmoxarifadoEventService(PreparacaoPedidoRepository repo) { this.repository = repo; }

    public void processarPedidoRecebido(PedidoEnviadoParaAlmoxarifadoEvent evento) {
        // Idempotência: Checa se já foi processado
        if (repository.findByPedidoId(evento.getPedidoId()).isPresent()) return;

        // Cria a entidade, processa os itens e salva
        PreparacaoPedido preparacao = new PreparacaoPedido(evento.getPedidoId(), ...);
        repository.save(preparacao);
    }
}
```

### No microsserviço PetFriends_Transporte, implemente a classe de configuração para o tratamento de mensagens.
```java
package com.petfriends.transporte.config;

import com.petfriends.transporte.events.PedidoDespachadoParaTransporteEvent;
import com.petfriends.transporte.service.TransporteEventService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.function.Consumer;

@Configuration
public class TransporteMessagingConfig {
    
    private final TransporteEventService service;
    public TransporteMessagingConfig(TransporteEventService service) { this.service = service; }

    @Bean
    public Consumer<PedidoDespachadoParaTransporteEvent> processarDespacho() {
        return evento -> service.processarPedidoDespachado(evento);
    }
}
```

### No microsserviço PetFriends_Transporte, implemente o serviço que receberá os eventos.
```java
package com.petfriends.transporte.service;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class TransporteEventService {
    
    private final EntregaRepository repository;
    public TransporteEventService(EntregaRepository repo) { this.repository = repo; }

    public void processarPedidoDespachado(PedidoDespachadoParaTransporteEvent evento) {
        if (repository.findByPedidoId(evento.getPedidoId()).isPresent()) return;
        
        String codigoRastreio = "PF" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Entrega entrega = new Entrega(evento.getPedidoId(), codigoRastreio, evento.getEnderecoEntrega());
        repository.save(entrega);
    }
}
```

---

## 4. Implementar testes e observabilidade com Zipkin, Micrometer e ELK Stack

### Explique de forma sucinta o que é um Gateway de Serviço, suas vantagens e desvantagens.
O API Gateway é a "porta de entrada" única para todos os clientes em uma arquitetura de microsserviços. Ele roteia requisições externas para os serviços internos corretos. 
- **Vantagens:** Centraliza funcionalidades transversais como autenticação, autorização, rate-limiting e SSL, simplificando o código do frontend.
- **Desvantagens:** Pode se tornar um Ponto Único de Falha (Single Point of Failure) se cair, e um gargalo de performance se não for bem dimensionado, além de adicionar complexidade na configuração da infraestrutura.

### O que é ID de Correlação e quais são os seus pré-requisitos?
É um identificador único (geralmente um UUID) anexado a uma requisição no momento em que ela entra no sistema. Ele é repassado em todas as chamadas de serviços subsequentes daquela mesma requisição.
- **Pré-requisitos:** Todos os serviços da cadeia precisam receber esse ID, logá-lo, e ter o cuidado de propagá-lo nos Headers HTTP ou Metadados de mensagens para o próximo serviço da fila.

### Qual é a função do Micrometer e sua relação com o serviço Zipkin?
O **Micrometer** funciona como uma fachada (facade) de coleta de métricas e tracing (como o SLF4J é para logs). No Spring Boot, o *Micrometer Tracing* captura automaticamente as interações do sistema (gerando spans e o ID de correlação). A sua relação com o **Zipkin** é de dependência: o Micrometer formata esses rastreios e os *envia* para o Zipkin, que é o servidor responsável por receber, armazenar e apresentar essas conexões de forma visual.

### Explique de forma sucinta o que é Agregador de Logs, suas vantagens e desvantagens.
Um agregador de logs (como o ELK Stack: Elasticsearch, Logstash, Kibana) é um sistema que coleta os logs gerados em arquivos de dezenas de microsserviços diferentes e os unifica em um único banco de dados otimizado para busca.
- **Vantagens:** Torna possível buscar toda a trilha de execução de um problema usando o ID de Correlação, acelerando drasticamente o troubleshooting de sistemas distribuídos.
- **Desvantagens:** Exige infraestrutura robusta, pois o volume de dados consumido é enorme, e adiciona complexidade de manutenção operacional para gerenciar o armazenamento.
