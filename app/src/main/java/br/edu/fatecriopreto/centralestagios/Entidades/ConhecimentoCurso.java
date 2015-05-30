package br.edu.fatecriopreto.centralestagios.Entidades;

import java.util.List;

public class ConhecimentoCurso {

    public Conhecimento Conhecimento;
    public int ConhecimentoId;
    public Curso Curso;
    public List<Curso> Cursos;
    public int CursoId;

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

    public br.edu.fatecriopreto.centralestagios.Entidades.Curso getCurso() {
        return Curso;
    }

    public void setCurso(br.edu.fatecriopreto.centralestagios.Entidades.Curso curso) {
        Curso = curso;
    }

    public List<br.edu.fatecriopreto.centralestagios.Entidades.Curso> getCursos() {
        return Cursos;
    }

    public void setCursos(List<br.edu.fatecriopreto.centralestagios.Entidades.Curso> cursos) {
        Cursos = cursos;
    }

    public int getCursoId() {
        return CursoId;
    }

    public void setCursoId(int cursoId) {
        CursoId = cursoId;
    }


}
