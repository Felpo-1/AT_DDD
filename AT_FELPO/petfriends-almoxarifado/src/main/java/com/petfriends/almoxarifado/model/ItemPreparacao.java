package com.petfriends.almoxarifado.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ItemPreparacao {

    @Column(name = "produto_id")
    private Long produtoId;

    @Column(name = "nome_produto")
    private String nomeProduto;

    @Column(name = "quantidade")
    private Integer quantidade;

    public ItemPreparacao() {}

    public ItemPreparacao(Long produtoId, String nomeProduto, Integer quantidade) {
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
    }

    public Long getProdutoId() { return produtoId; }
    public String getNomeProduto() { return nomeProduto; }
    public Integer getQuantidade() { return quantidade; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPreparacao that = (ItemPreparacao) o;
        return Objects.equals(produtoId, that.produtoId) &&
               Objects.equals(nomeProduto, that.nomeProduto) &&
               Objects.equals(quantidade, that.quantidade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produtoId, nomeProduto, quantidade);
    }
}
