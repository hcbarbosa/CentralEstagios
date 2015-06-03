package br.edu.fatecriopreto.centralestagios.Entidades;

public class RM {
    public long getRm() {
        return rm;
    }

    public void setRm(long rm) {
        this.rm = rm;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private long rm;
    private int status;

    public RM(long rm, int status){
        this.rm = rm;
        this.status = status;
    }
}
