package com.petfriends.almoxarifado.events;

public class ItemPedidoDTO {
    private Long produtoId;
    private String nomeProduto;
    private Integer quantidade;

    public ItemPedidoDTO() {}
    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }
    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
