package br.edu.fatecriopreto.centralestagios.Entidades;


public class Login {

    private String Rm;
    private String Senha;
    private String ConfirmaSenha;
    private int Status;
    private boolean StatusAdmin;

    private String Id;


    public Login(){}

   public Login(String rm, String senha, String confirmaSenha, int status, boolean statusAdmin){
       this.Rm = rm;
       this.Senha = senha;
       this.ConfirmaSenha = confirmaSenha;
       this.Status = status;
       this.StatusAdmin = statusAdmin;
   }


    public String getRm() {
        return Rm;
    }

    public void setRm(String rm) {
        Rm = rm;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String senha) {
        Senha = senha;
    }

    public String getConfirmaSenha() {
        return ConfirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        ConfirmaSenha = confirmaSenha;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public boolean isStatusAdmin() {
        return StatusAdmin;
    }

    public void setStatusAdmin(boolean statusAdmin) {
        StatusAdmin = statusAdmin;
    }


    public String getId() {
        return this.Id;
    }

    public final void setId(String id) {
        this.Id = id;
    }



}
