package com.example.caronaSpring.Aluno;

import com.example.caronaSpring.curso.Curso;
import com.example.caronaSpring.historico.Historico;
import com.example.caronaSpring.links.Links;

import jakarta.persistence.OneToOne;

public record DadosAtualizacaoAluno(Long id, String nome, String matricula, int idade,
		String foto,String comentarioFATEC, String comentario, int ano, Curso curso, 
		Historico historico, Links links) {

}
