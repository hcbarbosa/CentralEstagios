package br.edu.fatecriopreto.centralestagios.Entidades;


public class Observacao {

    public int Id;
    public Vaga Vaga;
    public int VagaId;
    public Perfil PerfilAluno;
    public String AlunoId;
    public String AdmId;
    public Perfil PerfilDono;
    public String DonoMsg;
    public String Descricao;
    public int Status;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public Perfil getPerfilAluno() {
        return PerfilAluno;
    }

    public void setPerfilAluno(Perfil perfilAluno) {
        PerfilAluno = perfilAluno;
    }

    public String getAlunoId() {
        return AlunoId;
    }

    public void setAlunoId(String alunoId) {
        AlunoId = alunoId;
    }

    public String getAdmId() {
        return AdmId;
    }

    public void setAdmId(String admId) {
        AdmId = admId;
    }

    public Perfil getPerfilDono() {
        return PerfilDono;
    }

    public void setPerfilDono(Perfil perfilDono) {
        PerfilDono = perfilDono;
    }

    public String getDonoMsg() {
        return DonoMsg;
    }

    public void setDonoMsg(String donoMsg) {
        DonoMsg = donoMsg;
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



}
