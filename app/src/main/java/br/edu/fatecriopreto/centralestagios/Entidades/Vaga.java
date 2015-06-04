package br.edu.fatecriopreto.centralestagios.Entidades;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Vaga {

    public int Id;
    public Beneficio Beneficio;
    public int BeneficioId;
    public String Descricao;
    public String TelefoneEmpresa;
    public String Horario;
    public String PessoaContato;
    public String Periodo;
    public String TipoVaga;
    public String Empresa;
    public double Remuneracao;
    public String EmailEmpresa;
    public String Observacoes;
    private ArrayList<Conhecimento> Conhecimentos;
    public Date DataCriacao;
    private boolean Candidatado;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public br.edu.fatecriopreto.centralestagios.Entidades.Beneficio getBeneficio() {
        return Beneficio;
    }

    public void setBeneficio(br.edu.fatecriopreto.centralestagios.Entidades.Beneficio beneficio) {
        Beneficio = beneficio;
    }

    public int getBeneficioId() {
        return BeneficioId;
    }

    public void setBeneficioId(int beneficioId) {
        BeneficioId = beneficioId;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public String getTelefoneEmpresa() {
        return TelefoneEmpresa;
    }

    public void setTelefoneEmpresa(String telefoneEmpresa) {
        TelefoneEmpresa = telefoneEmpresa;
    }

    public String getHorario() {
        return Horario;
    }

    public void setHorario(String horario) {
        Horario = horario;
    }

    public String getPessoaContato() {
        return PessoaContato;
    }

    public void setPessoaContato(String pessoaContato) {
        PessoaContato = pessoaContato;
    }

    public String getPeriodo() {
        return Periodo;
    }

    public void setPeriodo(String periodo) {
        Periodo = periodo;
    }

    public String getTipoVaga() {
        return TipoVaga;
    }

    public void setTipoVaga(String tipoVaga) {
        TipoVaga = tipoVaga;
    }

    public String getEmpresa() {
        return Empresa;
    }

    public void setEmpresa(String empresa) {
        Empresa = empresa;
    }

    public double getRemuneracao() {
        return Remuneracao;
    }

    public void setRemuneracao(double remuneracao) {
        Remuneracao = remuneracao;
    }

    public String getEmailEmpresa() {
        return EmailEmpresa;
    }

    public void setEmailEmpresa(String emailEmpresa) {
        EmailEmpresa = emailEmpresa;
    }

    public String getObservacoes() {
        return Observacoes;
    }

    public void setObservacoes(String observacoes) {
        Observacoes = observacoes;
    }

    public Date getDataCriacao() {
        return DataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        DataCriacao = dataCriacao;
    }


    public ArrayList<Conhecimento> getConhecimentos() {
        return Conhecimentos;
    }

    public void setConhecimentos(ArrayList<Conhecimento> conhecimentos) {
        Conhecimentos = conhecimentos;
    }

    public boolean isCandidatado() {
        return Candidatado;
    }

    public void setCandidatado(boolean candidatado) {
        Candidatado = candidatado;
    }
}
