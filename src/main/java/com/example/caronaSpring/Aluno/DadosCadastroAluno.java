package com.example.caronaSpring.Aluno;

import com.example.caronaSpring.curso.Curso;
import com.example.caronaSpring.historico.Historico;
import com.example.caronaSpring.links.Links;

import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;

public record DadosCadastroAluno(
		@NotBlank
		String nome,
		String matricula,
		int idade,
		String foto,
		String comentarioFATEC,
		String comentario,
		int ano,
		Curso curso,
		Historico historico,
		Links links
		) {

}
