package com.example.caronaSpring.curso;

import java.util.List;

import com.example.caronaSpring.Aluno.Aluno;
import com.example.caronaSpring.coordenador.Coordenador;

public record DadosAtualizacaoCurso(long id, String nome, String descricao, int duracao, String modalidade, String turno,
		String areaAtuacao, Coordenador coordenador, List<Aluno> aluno) {

}
