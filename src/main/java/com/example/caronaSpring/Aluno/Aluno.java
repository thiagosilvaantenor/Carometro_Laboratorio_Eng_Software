package com.example.caronaSpring.Aluno;

import com.example.caronaSpring.curso.Curso;
import com.example.caronaSpring.curso.DadosAtualizacaoCurso;
import com.example.caronaSpring.historico.Historico;
import com.example.caronaSpring.links.Links;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "aluno")
@Entity(name = "aluno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Aluno {

	public Aluno(DadosCadastroAluno dados) {
		this.nome = dados.nome();
		this.matricula = dados.matricula();
		this.idade = dados.idade();
		this.foto = dados.foto();
		this.comentarioFATEC = dados.comentarioFATEC();
		this.comentario = dados.comentario();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "aluno_id")
	private Long id;
	private String nome;
	private String matricula;
	private int idade;
	private String foto;
	private String comentarioFATEC;
	private String comentario;
	// PERGUNTAR SE é ano de ingressão ou ano do curso ou semestre
	private int ano;

	@OneToOne(mappedBy = "curso")
	private Curso curso;

	@OneToOne(mappedBy = "historico")
	private Historico historico;

	@OneToOne(mappedBy = "links")
	private Links links;

	public void atualizarInformacoes(DadosAtualizacaoAluno dados) {
		if (dados.nome() != null) {
			this.nome = dados.nome();
		}
		if (dados.matricula() != null) {
			this.matricula = dados.matricula();
		}
		if (dados.idade() != 0) {
			this.idade = dados.idade();
		}
		if (dados.foto() != null) {
			this.foto = dados.foto();
		}
		if (dados.comentarioFATEC() != null) {
			this.comentarioFATEC = dados.comentarioFATEC();
		}
		if (dados.comentario() != null) {
			this.comentario = dados.comentario();
		}

	}
}
