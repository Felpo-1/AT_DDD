package com.petfriends.transporte.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "entrega")
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pedido_id", nullable = false)
    private Long pedidoId;

    @Column(name = "codigo_rastreio", unique = true)
    private String codigoRastreio;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEntrega status;

    @Embedded
    private EnderecoDestino enderecoDestino;

    @Column(name = "transportadora")
    private String transportadora;

    @Column(name = "data_despacho")
    private LocalDateTime dataDespacho;

    @Column(name = "data_previsao_entrega")
    private LocalDateTime dataPrevisaoEntrega;

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;

    @Column(name = "motivo_devolucao")
    private String motivoDevolucao;

    public Entrega() {}

    public Entrega(Long pedidoId, String codigoRastreio, EnderecoDestino enderecoDestino, String transportadora) {
        this.pedidoId = pedidoId;
        this.codigoRastreio = codigoRastreio;
        this.enderecoDestino = enderecoDestino;
        this.transportadora = transportadora;
        this.status = StatusEntrega.EM_TRANSITO;
        this.dataDespacho = LocalDateTime.now();
        this.dataPrevisaoEntrega = LocalDateTime.now().plusDays(15);
    }

    public void confirmarEntrega() {
        if (this.status != StatusEntrega.EM_TRANSITO) {
            throw new IllegalStateException("Só é possível confirmar entrega de pedidos em trânsito.");
        }
        this.status = StatusEntrega.ENTREGUE;
        this.dataEntrega = LocalDateTime.now();
    }

    public void registrarDevolucao(String motivo) {
        if (this.status != StatusEntrega.EM_TRANSITO) {
            throw new IllegalStateException("Só é possível devolver pedidos em trânsito.");
        }
        this.status = StatusEntrega.DEVOLVIDO;
        this.motivoDevolucao = motivo;
        this.dataEntrega = LocalDateTime.now();
    }

    public void registrarExtravio() {
        if (this.status != StatusEntrega.EM_TRANSITO) {
            throw new IllegalStateException("Só é possível extraviar pedidos em trânsito.");
        }
        this.status = StatusEntrega.EXTRAVIADO;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public String getCodigoRastreio() { return codigoRastreio; }
    public void setCodigoRastreio(String codigoRastreio) { this.codigoRastreio = codigoRastreio; }
    public StatusEntrega getStatus() { return status; }
    public void setStatus(StatusEntrega status) { this.status = status; }
    public EnderecoDestino getEnderecoDestino() { return enderecoDestino; }
    public void setEnderecoDestino(EnderecoDestino enderecoDestino) { this.enderecoDestino = enderecoDestino; }
    public String getTransportadora() { return transportadora; }
    public void setTransportadora(String transportadora) { this.transportadora = transportadora; }
    public LocalDateTime getDataDespacho() { return dataDespacho; }
    public void setDataDespacho(LocalDateTime dataDespacho) { this.dataDespacho = dataDespacho; }
    public LocalDateTime getDataPrevisaoEntrega() { return dataPrevisaoEntrega; }
    public void setDataPrevisaoEntrega(LocalDateTime dataPrevisaoEntrega) { this.dataPrevisaoEntrega = dataPrevisaoEntrega; }
    public LocalDateTime getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(LocalDateTime dataEntrega) { this.dataEntrega = dataEntrega; }
    public String getMotivoDevolucao() { return motivoDevolucao; }
    public void setMotivoDevolucao(String motivoDevolucao) { this.motivoDevolucao = motivoDevolucao; }
}
