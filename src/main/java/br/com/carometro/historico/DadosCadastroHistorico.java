package br.com.carometro.historico;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroHistorico(
		Long id,
		@NotBlank(message = "Nome da empresa é obrigatório")
		String empresaTrabalho,
		@NotBlank(message = "Descrição é obrigatório")
		String descricaoTrabalho,
		@NotBlank(message = "Data de inicio é obrigatório, mesmo que se lembre apenas o ano")
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		LocalDate dtInicio,
		@NotBlank(message = "Data de fim é obrigatório, mesmo que se lembre apenas o ano")
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		LocalDate dtFim
		) {

}
