package com.example.caronaSpring.coordenador;

import java.time.LocalDate;

import com.example.caronaSpring.curso.Curso;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;

public record DadosCadastroCoordenador(@NotBlank
		String matricula,
		String nome,
		String cpf,
		String email,
		String especializacao,
		LocalDate dtNascimento,
		String estadoCivil,
		Curso curso) {

}
