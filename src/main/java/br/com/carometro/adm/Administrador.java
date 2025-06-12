package br.com.carometro.adm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
	private Long id;
	
	@NotBlank(message = "Nome é obrigatório")
	private String nome;
	@NotBlank(message = "Email é obrigatório")
	@Email
	private String email;
	@NotBlank(message = "Senha é obrigatória")
	private String senha;
	

	public Administrador(@Valid DadosCadastroAdministrador dados) {
		this.nome = dados.nome();
		this.email = dados.email();
		this.senha = dados.senha();
//		relação unidFatec é feita no controller
	}

	public void atualizarInformacoes(DadosAtualizacaoAdministrador dados) {
		if (dados.nome() != null) {
			this.nome = dados.nome();
		}
		if (dados.email() != null) {
			this.email = dados.email();
		}
		//Atualização da unidFatec e da senha é feita no controller
	}
}
