package br.com.carona.coordenador;

import java.time.LocalDate;

import br.com.carona.curso.Curso;

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
