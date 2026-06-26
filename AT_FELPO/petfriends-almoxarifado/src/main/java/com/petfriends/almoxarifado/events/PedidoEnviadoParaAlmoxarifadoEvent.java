package com.petfriends.almoxarifado.events;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoEnviadoParaAlmoxarifadoEvent {

    private Long pedidoId;
    private Long clienteId;
    private String nomeCliente;
    private LocalDateTime dataPedido;
    private List<ItemPedidoDTO> itens;
    private EnderecoDTO enderecoEntrega;
    private String observacoes;

    public PedidoEnviadoParaAlmoxarifadoEvent() {}

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }
    public List<ItemPedidoDTO> getItens() { return itens; }
    public void setItens(List<ItemPedidoDTO> itens) { this.itens = itens; }
    public EnderecoDTO getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(EnderecoDTO enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
