package br.com.carona.adm;

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
	String email;
	String senha;
	String unidFATEC;

	public Administrador(@Valid DadosCadastroAdministrador dados) {
		this.nome = dados.nome();
		this.cpf = dados.cpf();
		this.unidFATEC = dados.unidFATEC();
		this.email = dados.email();
		this.senha = dados.senha();
	}

	public void atualizarInformacoes(DadosAtualizacaoAdministrador dados) {
		if (dados.nome() != null) {
			this.nome = dados.nome();
		}
		if (dados.email() != null) {
			this.email = dados.email();
		}
		if (dados.senha() != null) {
			this.senha = dados.senha();
		}
		if (dados.cpf() != null) {
			this.cpf = dados.cpf();
		}
		if (dados.unidFATEC() != null) {
			this.unidFATEC = dados.unidFATEC();
		}
	}
}
