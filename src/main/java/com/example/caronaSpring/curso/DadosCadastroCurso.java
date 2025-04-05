package com.example.caronaSpring.curso;

import java.util.List;

import com.example.caronaSpring.Aluno.Aluno;
import com.example.caronaSpring.coordenador.Coordenador;

import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;


public record DadosCadastroCurso(@NotBlank 
		String nome, String descricao, int duracao, String modalidade, String turno,
		String areaAtuacao, Coordenador coordenador, List<Aluno> aluno) {

}
