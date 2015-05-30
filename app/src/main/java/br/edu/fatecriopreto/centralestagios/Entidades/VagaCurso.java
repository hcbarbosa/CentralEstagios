package br.edu.fatecriopreto.centralestagios.Entidades;

import java.util.List;

public class VagaCurso {

    public Vaga Vaga;
    public int VagaId;
    public Curso Curso;
    public int CursoId;
    public List<Curso> Cursos;

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

    public br.edu.fatecriopreto.centralestagios.Entidades.Curso getCurso() {
        return Curso;
    }

    public void setCurso(br.edu.fatecriopreto.centralestagios.Entidades.Curso curso) {
        Curso = curso;
    }

    public int getCursoId() {
        return CursoId;
    }

    public void setCursoId(int cursoId) {
        CursoId = cursoId;
    }

    public List<br.edu.fatecriopreto.centralestagios.Entidades.Curso> getCursos() {
        return Cursos;
    }

    public void setCursos(List<br.edu.fatecriopreto.centralestagios.Entidades.Curso> cursos) {
        Cursos = cursos;
    }


}
