package br.edu.fatecriopreto.centralestagios.Entidades;


public class PerfilConhecimento {

    public Perfil Perfil;
    public String PerfilRm;
    public Conhecimento Conhecimento;
    public int ConhecimentoId;

    public br.edu.fatecriopreto.centralestagios.Entidades.Perfil getPerfil() {
        return Perfil;
    }

    public void setPerfil(br.edu.fatecriopreto.centralestagios.Entidades.Perfil perfil) {
        Perfil = perfil;
    }

    public String getPerfilRm() {
        return PerfilRm;
    }

    public void setPerfilRm(String perfilRm) {
        PerfilRm = perfilRm;
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



}
