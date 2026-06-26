package com.petfriends.almoxarifado.events;

import java.time.LocalDateTime;

public class PedidoDespachadoParaTransporteEvent {

    private Long pedidoId;
    private Long clienteId;
    private String nomeCliente;
    private String telefoneCliente;
    private EnderecoDTO enderecoEntrega;
    private Integer quantidadeVolumes;
    private Double pesoTotal;
    private LocalDateTime dataDespacho;
    private String observacoes;

    public PedidoDespachadoParaTransporteEvent() {}

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    public String getTelefoneCliente() { return telefoneCliente; }
    public void setTelefoneCliente(String telefoneCliente) { this.telefoneCliente = telefoneCliente; }
    public EnderecoDTO getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(EnderecoDTO enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }
    public Integer getQuantidadeVolumes() { return quantidadeVolumes; }
    public void setQuantidadeVolumes(Integer quantidadeVolumes) { this.quantidadeVolumes = quantidadeVolumes; }
    public Double getPesoTotal() { return pesoTotal; }
    public void setPesoTotal(Double pesoTotal) { this.pesoTotal = pesoTotal; }
    public LocalDateTime getDataDespacho() { return dataDespacho; }
    public void setDataDespacho(LocalDateTime dataDespacho) { this.dataDespacho = dataDespacho; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
