package br.com.carometro.coordenador;

import java.time.LocalDate;

import br.com.carometro.curso.Curso;
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
	private String senha;
	private String especializacao;
	private LocalDate dtNascimento;
	private String estadoCivil;
	@OneToOne
	@JoinColumn(name="curso_id")
	private Curso curso;
	
	public Coordenador(DadosCadastroCoordenador dados) {
		this.matricula = dados.matricula();
		this.nome = dados.nome();
		this.cpf = dados.cpf();
		this.email = dados.email();
		this.senha = dados.senha();
		this.especializacao = dados.especializacao();
		this.dtNascimento = dados.dtNascimento();
		this.estadoCivil = dados.estadoCivil();
		this.curso = dados.curso();
	}

	public void atualizarInformacoes(DadosAtualizacaoCoordenador dados) {
		if (dados.nome() != null) {
			this.nome = dados.nome();
		}
		if (dados.cpf() != null) {
			this.cpf = dados.cpf();
		}
		if (dados.email() != null) {
			this.email = dados.email();
		}
		if (dados.senha() != null) {
			this.senha = dados.senha();
		}
		if (dados.especializacao() != null) {
			this.especializacao = dados.especializacao();
		}
		if (dados.dtNascimento() != null) {
			this.dtNascimento = dados.dtNascimento();
		}
		if (dados.estadoCivil() != null) {
			this.estadoCivil = dados.estadoCivil();
		}
		if (dados.curso() != null) {
			this.curso = dados.curso();
		}
		
	}
	
}
