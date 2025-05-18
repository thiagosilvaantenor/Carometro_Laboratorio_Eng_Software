package br.com.carometro.historico;

import java.time.LocalDate;

public record DadosCadastroHistorico(
		String empresaTrabalho,
		String descricaoTrabalho,
		LocalDate dtInicio,
		LocalDate dtFim
		) {

}
