package br.com.carometro.curso;

import java.util.List;

import br.com.carometro.aluno.Aluno;
import br.com.carometro.coordenador.Coordenador;
import jakarta.validation.constraints.NotBlank;


public record DadosCadastroCurso(@NotBlank 
		String nome, String descricao, int duracao, String modalidade, String turno,
		String areaAtuacao, Coordenador coordenador, List<Aluno> aluno) {

}
