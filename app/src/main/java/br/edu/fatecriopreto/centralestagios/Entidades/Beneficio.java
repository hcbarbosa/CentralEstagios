package br.edu.fatecriopreto.centralestagios.Entidades;

public class Beneficio {

    private int Id;
    private boolean AuxilioOdontologico;
    private boolean PlanoSaude;
    private boolean ValeTransporte;
    private boolean ValeAlimentacao;
    private String Outros;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public boolean isAuxilioOdontologico() {
        return AuxilioOdontologico;
    }

    public void setAuxilioOdontologico(boolean auxilioOdontologico) {
        AuxilioOdontologico = auxilioOdontologico;
    }

    public boolean isPlanoSaude() {
        return PlanoSaude;
    }

    public void setPlanoSaude(boolean planoSaude) {
        PlanoSaude = planoSaude;
    }

    public boolean isValeTransporte() {
        return ValeTransporte;
    }

    public void setValeTransporte(boolean valeTransporte) {
        ValeTransporte = valeTransporte;
    }

    public boolean isValeAlimentacao() {
        return ValeAlimentacao;
    }

    public void setValeAlimentacao(boolean valeAlimentacao) {
        ValeAlimentacao = valeAlimentacao;
    }

    public String getOutros() {
        return Outros;
    }

    public void setOutros(String outros) {
        Outros = outros;
    }
}
