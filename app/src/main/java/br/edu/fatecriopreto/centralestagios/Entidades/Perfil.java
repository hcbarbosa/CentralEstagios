package br.edu.fatecriopreto.centralestagios.Entidades;

public class Perfil {

    private long LoginRm;
    private long CursoId;
    private String Cidade;
    private String Telefone;
    private long Cep;
    private long Ano;
    private String Uf;
    private String Bairro;
    private String Logradouro;
    private String Complemento;
    private String Nome;
    private String Email;
    private String Semestre;
    private String rememberRm;


    public Perfil(long aLong, long aLong1, String string, String string1, String string2, long aLong2, String string3, String string4, String string5, String string6, String string7, String string8, long string9) {

    }

    public Perfil(){}


    public long getRm() {
        return LoginRm;
    }

    public void setRm(long rm) {
        this.LoginRm = rm;
    }

    public long getCursoId() {
        return CursoId;
    }

    public void setCursoId(long cursoId) {
        this.CursoId = cursoId;
    }

    public String getCidade() {
        return Cidade;
    }

    public void setCidade(String cidade) {
        this.Cidade = cidade;
    }

    public String getTelefone() {
        return Telefone;
    }

    public void setTelefone(String telefone) {
        this.Telefone = telefone;
    }

    public long getCEP() {
        return Cep;
    }

    public void setCEP(long CEP) {
        this.Cep = CEP;
    }

    public long getAno() {
        return Ano;
    }

    public void setAno(long ano) {
        this.Ano = ano;
    }

    public String getUf() {
        return Uf;
    }

    public void setUf(String uf) {
        this.Uf = uf;
    }

    public String getBairro() {
        return Bairro;
    }

    public void setBairro(String bairro) {
        this.Bairro = bairro;
    }

    public String getLogradouro() {
        return Logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.Logradouro = logradouro;
    }

    public String getComplemento() {
        return Complemento;
    }

    public void setComplemento(String complemento) {
        this.Complemento = complemento;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        this.Nome = nome;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getSemestre() {
        return Semestre;
    }

    public void setSemestre(String semestre) {
        this.Semestre = semestre;
    }

    public void setRememberRm(String rememberRm) {
        this.rememberRm = rememberRm;
    }

    public String getRememberRm() {
        return rememberRm;
    }

}

