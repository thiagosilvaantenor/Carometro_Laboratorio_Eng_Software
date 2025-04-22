package br.com.carometro.coordenador;

import java.time.LocalDate;

import br.com.carometro.curso.Curso;

public record DadosAtualizacaoCoordenador(
		String matricula,
		String nome,
		String cpf,
		String email,
		String senha,
		String especializacao,
		LocalDate dtNascimento,
		String estadoCivil,
		Curso curso
		) {

}
