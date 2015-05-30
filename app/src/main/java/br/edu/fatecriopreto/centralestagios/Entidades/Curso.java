package br.edu.fatecriopreto.centralestagios.Entidades;

public class Curso {

    public int Id;
    public String Descricao;
    public int Status;
    public boolean EstaSelecionado;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public boolean isEstaSelecionado() {
        return EstaSelecionado;
    }

    public void setEstaSelecionado(boolean estaSelecionado) {
        EstaSelecionado = estaSelecionado;
    }


}
