package com.example.caronaSpring.adm;

import java.util.List;

import com.example.caronaSpring.Aluno.Aluno;
import com.example.caronaSpring.coordenador.Coordenador;
import com.example.caronaSpring.curso.Curso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
public class Administrador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "admin_id")

	Long id;
	String nome;
	String cpf;
	String unidFatec;
	String email;
	String senha;

	public Administrador(@Valid DadosCadastroAdministrador dados) {
		this.nome = dados.nome();
		this.cpf = dados.cpf();
		this.unidFatec = dados.unidFatec();
		this.email = dados.email();
		this.senha = dados.senha();
	}

	public void atualizarInformacoes(DadosAtualizacaoAdministrador dados) {
		if (dados.nome() != null) {
			this.nome = dados.nome();
		}
		
		if (dados.cpf() != null) {
			this.cpf = dados.cpf();
		}
		
		if (dados.unidFatec() != null) {
			this.unidFatec = dados.unidFatec();
		}
		if (dados.email() != null) {
			this.email = dados.email();
		}
		if (dados.senha() != null) {
			this.senha = dados.senha();
		}
	}
}
