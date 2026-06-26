package com.petfriends.almoxarifado.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "preparacao_pedido")
public class PreparacaoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pedido_id", nullable = false)
    private Long pedidoId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPreparacao status;

    @Embedded
    private EnderecoEntrega enderecoEntrega;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "itens_preparacao", joinColumns = @JoinColumn(name = "preparacao_id"))
    private List<ItemPreparacao> itens = new ArrayList<>();

    @Column(name = "data_recebimento")
    private LocalDateTime dataRecebimento;

    @Column(name = "data_despacho")
    private LocalDateTime dataDespacho;

    @Column(name = "responsavel")
    private String responsavel;

    public PreparacaoPedido() {}

    public PreparacaoPedido(Long pedidoId, EnderecoEntrega enderecoEntrega, List<ItemPreparacao> itens) {
        this.pedidoId = pedidoId;
        this.enderecoEntrega = enderecoEntrega;
        this.itens = itens;
        this.status = StatusPreparacao.RECEBIDO;
        this.dataRecebimento = LocalDateTime.now();
    }

    public void iniciarPreparacao(String responsavel) {
        if (this.status != StatusPreparacao.RECEBIDO) {
            throw new IllegalStateException("Só é possível iniciar preparação de pedidos recebidos.");
        }
        this.responsavel = responsavel;
        this.status = StatusPreparacao.EM_PREPARACAO;
    }

    public void concluirPreparacao() {
        if (this.status != StatusPreparacao.EM_PREPARACAO) {
            throw new IllegalStateException("Só é possível concluir pedidos em preparação.");
        }
        this.status = StatusPreparacao.PRONTO_PARA_DESPACHO;
    }

    public void despachar() {
        if (this.status != StatusPreparacao.PRONTO_PARA_DESPACHO) {
            throw new IllegalStateException("Só é possível despachar pedidos prontos.");
        }
        this.status = StatusPreparacao.DESPACHADO;
        this.dataDespacho = LocalDateTime.now();
    }

    public void cancelar() {
        if (this.status == StatusPreparacao.DESPACHADO) {
            throw new IllegalStateException("Não é possível cancelar um pedido já despachado.");
        }
        this.status = StatusPreparacao.CANCELADO;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public StatusPreparacao getStatus() { return status; }
    public void setStatus(StatusPreparacao status) { this.status = status; }
    public EnderecoEntrega getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(EnderecoEntrega enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }
    public List<ItemPreparacao> getItens() { return itens; }
    public void setItens(List<ItemPreparacao> itens) { this.itens = itens; }
    public LocalDateTime getDataRecebimento() { return dataRecebimento; }
    public void setDataRecebimento(LocalDateTime dataRecebimento) { this.dataRecebimento = dataRecebimento; }
    public LocalDateTime getDataDespacho() { return dataDespacho; }
    public void setDataDespacho(LocalDateTime dataDespacho) { this.dataDespacho = dataDespacho; }
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
}
