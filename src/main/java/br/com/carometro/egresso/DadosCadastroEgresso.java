package br.com.carometro.egresso;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.carometro.curso.Curso;
import br.com.carometro.historico.DadosCadastroHistorico;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosCadastroEgresso(
		@NotBlank(message = "Nome é obrigatório")
		String nome,
		@NotBlank(message = "Email é obrigatório")
		@Email
		String email,
		@NotBlank(message = "Senha é obrigatório")
		@Size(min = 8)
		String senha,
		@Size(max = 100)
		String comentarioFATEC,
		@Size(max = 100)
		String comentario,
		@NotBlank(message = "Ano de conclusão é obrigatório")
		@Size(max = 4)
		Integer ano,
		@Size(max = 100)
		String sobre,
		@Size(max = 100)
		String sobreProfissional,
		@Size(max = 100)
		String sobreFatec,
		@NotBlank(message = "Curso é obrigatório")
		Curso curso,
		List<DadosCadastroHistorico> historico,
		String gitHub,
		String linkedIn,
		String lattesCNPQ,
		@NotNull(message = "É necessário concordar com a divulgação para prosseguir.")
		@AssertTrue(message = "É necessário concordar com a divulgação para prosseguir.")
		Boolean consentimentoDivulgacao
		) {


}
