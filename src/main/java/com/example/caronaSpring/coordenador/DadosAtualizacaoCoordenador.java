package com.example.caronaSpring.coordenador;

import java.time.LocalDate;

import com.example.caronaSpring.curso.Curso;

public record DadosAtualizacaoCoordenador(
		String matricula,
		String nome,
		String cpf,
		String email,
		String especializacao,
		LocalDate dtNascimento,
		String estadoCivil,
		Curso curso
		) {

}
