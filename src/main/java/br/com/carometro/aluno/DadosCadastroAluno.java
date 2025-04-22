package br.com.carometro.aluno;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import br.com.carometro.curso.Curso;
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
		MultipartFile foto
		) {


}
