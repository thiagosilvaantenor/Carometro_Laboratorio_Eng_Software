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
		@NotBlank
		String matricula,
		@NotBlank
		String cpf,
		@NotBlank
		String email,
		@NotBlank
		String senha,
		LocalDate dtNascimento,
		String unidFATEC,
		String comentarioFATEC,
		String comentario,
		Integer ano,
		Curso curso,
		String empresaTrabalho,
		String descricaoTrabalho,
		int tempoTrabalho,
		String gitHub,
		String linkedIn,
		String lattesCNPQ,
		byte[] foto
		) {


}
