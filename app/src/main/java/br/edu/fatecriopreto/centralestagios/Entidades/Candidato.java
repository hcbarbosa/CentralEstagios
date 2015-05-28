package br.edu.fatecriopreto.centralestagios.Entidades;

public class Candidato {

    private Perfil Perfil;
    private String PerfilRm;
    private Vaga Vaga;
    private int VagaId;

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
}
