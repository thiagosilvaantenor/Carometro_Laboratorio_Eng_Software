package br.com.carometro.curso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record DadosCadastroCurso(
		@NotBlank(message = "Nome é obrigatório") 
		String nome,
		@NotBlank(message = "Descrição é obrigatória")
		@Size(max = 100)
		String descricao,
		@NotBlank(message = "Duração é obrigatória")
		@Size(max = 2)
		int duracao,
		String modalidade,
		String turno,
		String areaAtuacao) {

}
