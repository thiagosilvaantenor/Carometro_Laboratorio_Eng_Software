package br.com.carona.curso;

import java.util.List;

import br.com.carona.aluno.Aluno;
import br.com.carona.coordenador.Coordenador;

public record DadosAtualizacaoCurso(long id, String nome, String descricao, int duracao, String modalidade, String turno,
		String areaAtuacao, Coordenador coordenador, List<Aluno> aluno) {

}
