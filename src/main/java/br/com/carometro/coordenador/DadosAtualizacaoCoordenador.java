package br.com.carometro.coordenador;

import java.time.LocalDate;

import br.com.carometro.curso.Curso;

public record DadosAtualizacaoCoordenador(
		Long id,
		String nome,
		String email,
		String senha,
		LocalDate vencimentoMandato,
		Curso curso
		) {

}
