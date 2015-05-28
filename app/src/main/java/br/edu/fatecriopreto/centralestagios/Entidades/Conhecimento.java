package br.edu.fatecriopreto.centralestagios.Entidades;

public class Conhecimento {

    private int Id ;
    private String Descricao ;
    private int Status;
    private boolean EstaSelecionado;

    public boolean isEstaSelecionado() {
        return EstaSelecionado;
    }

    public void setEstaSelecionado(boolean estaSelecionado) {
        EstaSelecionado = estaSelecionado;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }



}
