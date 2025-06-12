package br.com.carometro.egresso;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.carometro.curso.Curso;
import br.com.carometro.historico.DadosCadastroHistorico;
import jakarta.validation.constraints.NotBlank;

public record DadosCadastroEgresso(
		@NotBlank
		String nome,
		@NotBlank
		String email,
		@NotBlank
		String senha,
		LocalDate dtNascimento,
		String comentarioFATEC,
		String comentario,
		Integer ano,
		Curso curso,
		List<DadosCadastroHistorico> historico,
		String gitHub,
		String linkedIn,
		String lattesCNPQ,
		MultipartFile foto
		) {


}
