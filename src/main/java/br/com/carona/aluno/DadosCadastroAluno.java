package br.com.carona.aluno;

import br.com.carona.curso.Curso;
import br.com.carona.historico.Historico;
import br.com.carona.links.Links;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;

public record DadosCadastroAluno(
		@NotBlank
		String nome,
		String matricula,
		int idade,
		String foto,
		String comentarioFATEC,
		String comentario,
		int ano,
		Curso curso,
		Historico historico,
		Links links
		) {

}
