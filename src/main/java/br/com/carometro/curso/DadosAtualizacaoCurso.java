package br.com.carometro.curso;

import java.util.List;

import br.com.carometro.coordenador.Coordenador;
import br.com.carometro.egresso.Egresso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosAtualizacaoCurso(
		@NotBlank(message = "Id é obrigatório para atualização")
		long id,
		@NotBlank(message = "Nome é obrigatório") 
		String nome,
		@NotBlank(message = "Descrição é obrigatória")
		@Size(max = 100)
		String descricao,
		@NotBlank(message = "Duração é obrigatória")
		int duracao, 
		String modalidade, 
		String turno,
		String areaAtuacao, 
		Coordenador coordenador, 
		List<Egresso> aluno) {

}
