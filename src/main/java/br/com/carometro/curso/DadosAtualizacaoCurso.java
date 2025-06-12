package br.com.carometro.curso;

import java.util.List;

import br.com.carometro.coordenador.Coordenador;
import br.com.carometro.egresso.Egresso;

public record DadosAtualizacaoCurso(long id, String nome, String descricao, int duracao, String modalidade, String turno,
		String areaAtuacao, Coordenador coordenador, List<Egresso> aluno) {

}
