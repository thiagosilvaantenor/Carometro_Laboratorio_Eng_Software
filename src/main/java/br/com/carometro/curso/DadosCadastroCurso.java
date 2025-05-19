package br.com.carometro.curso;

import jakarta.validation.constraints.NotBlank;


public record DadosCadastroCurso(@NotBlank 
		String nome, String descricao, int duracao, String modalidade, String turno,
		String areaAtuacao, String nomeFatec) {

}
