package br.edu.fatecriopreto.centralestagios.Entidades;


import java.util.List;

public class VagaConhecimento {

    public Vaga Vaga;
    public int VagaId;
    public Conhecimento Conhecimento;
    public int ConhecimentoId;
    public List<Conhecimento> Conhecimentos;

    public br.edu.fatecriopreto.centralestagios.Entidades.Vaga getVaga() {
        return Vaga;
    }

    public void setVaga(br.edu.fatecriopreto.centralestagios.Entidades.Vaga vaga) {
        Vaga = vaga;
    }

    public int getVagaId() {
        return VagaId;
    }

    public void setVagaId(int vagaId) {
        VagaId = vagaId;
    }

    public br.edu.fatecriopreto.centralestagios.Entidades.Conhecimento getConhecimento() {
        return Conhecimento;
    }

    public void setConhecimento(br.edu.fatecriopreto.centralestagios.Entidades.Conhecimento conhecimento) {
        Conhecimento = conhecimento;
    }

    public int getConhecimentoId() {
        return ConhecimentoId;
    }

    public void setConhecimentoId(int conhecimentoId) {
        ConhecimentoId = conhecimentoId;
    }

    public List<br.edu.fatecriopreto.centralestagios.Entidades.Conhecimento> getConhecimentos() {
        return Conhecimentos;
    }

    public void setConhecimentos(List<br.edu.fatecriopreto.centralestagios.Entidades.Conhecimento> conhecimentos) {
        Conhecimentos = conhecimentos;
    }


}
