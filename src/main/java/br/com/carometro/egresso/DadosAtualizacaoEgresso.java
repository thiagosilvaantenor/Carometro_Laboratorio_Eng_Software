package br.com.carometro.egresso;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.carometro.curso.Curso;
import br.com.carometro.historico.DadosCadastroHistorico;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosAtualizacaoEgresso(
		@NotBlank(message = "Id é obrigatório para atualização")
		Long id, 
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
		String gitHub,
		String linkedIn,
		String lattesCNPQ,
		List<DadosCadastroHistorico> historico
		) {

}
