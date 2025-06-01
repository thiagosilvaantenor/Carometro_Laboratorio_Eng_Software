package br.com.carometro.historico;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public record DadosCadastroHistorico(
		Long id,
		String empresaTrabalho,
		String descricaoTrabalho,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		LocalDate dtInicio,
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		LocalDate dtFim
		) {

}
