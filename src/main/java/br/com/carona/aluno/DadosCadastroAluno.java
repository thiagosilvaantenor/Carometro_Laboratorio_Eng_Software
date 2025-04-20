package br.com.carona.aluno;

import java.time.LocalDate;

import br.com.carona.curso.Curso;
import br.com.carona.historico.Historico;
import br.com.carona.links.Links;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;

public record DadosCadastroAluno(
		@NotBlank
		String nome,
		String matricula,
		String cpf,
		String email,
		String senha,
		LocalDate dtNascimento,
		String unidFATEC,
		String comentarioFATEC,
		String comentario,
		int ano,
		Curso curso,
		Historico historico,
		Links links,
		byte[] foto
		) {


}
