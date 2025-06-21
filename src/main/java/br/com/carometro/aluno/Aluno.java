package br.com.carometro.aluno;

import br.com.carometro.curso.Curso;
import br.com.carometro.egresso.DadosAtualizacaoEgresso;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Aluno {
	//Entidade para lidar com os alunos que ainda não se formara, mas vão se cadastrar com antecedencia na plataforma

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "aluno_id")
	private Long id;
	@Column(nullable = false)
	private String nome;
	@Email
	@Column(nullable = false)
	private String email;
	//Senha do aluno pode ser nula, caso quem cadastre ele não seja ele
	@Column(nullable = true)
	private String senha;
	@Column(nullable = true)
	private String telefone;
	//1 curso para N alunos
	@ManyToOne
	@JoinColumn(name="curso_id", nullable=false)
	private Curso curso;

	public Aluno(AlunoDadosCadastro dados) {
		this.nome = dados.nome();
		this.email = dados.email();
		this.senha = dados.senha();
		this.telefone = dados.telefone();
	}
	
	public void atualizarInformacoes(AlunoDadosCadastro dados) {
		if(dados.nome() != null) {
			this.nome = dados.nome();
		}
		if(dados.email() != null) {
			this.email = dados.email();
		}
		if(dados.senha() != null) {
			this.senha = dados.senha();
		}
		if(dados.telefone() != null) {
			this.telefone = dados.telefone();
		}
		//Atualização do curso é feita no controller
	}
	
	//Construtor para dados vindos de arquivo
	public Aluno(String nome, String email, String telefone) {
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
	}
}
