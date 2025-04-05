package com.example.caronaSpring.coordenador;

import java.time.LocalDate;

import com.example.caronaSpring.curso.Curso;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "matricula")
public class Coordenador {
	@Id
	private String matricula;	
	private String nome;
	private String cpf;
	private String email;
	private String especializacao;
	private LocalDate dtNascimento;
	private String estadoCivil;
	@OneToOne
	@JoinColumn(name="curso_id")
	private Curso curso;
	
}
