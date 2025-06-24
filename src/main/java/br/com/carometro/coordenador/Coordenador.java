package br.com.carometro.coordenador;

import java.time.LocalDate;

import br.com.carometro.curso.Curso;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
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
@EqualsAndHashCode(of = "id")
public class Coordenador {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coordenador_id")
	private Long id;
	private String nome; 
	@Email
	private String email;
	
	private String senha;
	private LocalDate vencimentoMandato;
	@OneToOne
	@JoinColumn(name="curso_id")
	private Curso curso;
	

	
	public Coordenador(DadosCadastroCoordenador dados) {
		this.nome = dados.nome();
		this.email = dados.email();
		this.senha = dados.senha();
		this.vencimentoMandato = dados.vencimentoMandato();
	}

	public void atualizarInformacoes(DadosAtualizacaoCoordenador dados) {
		if (dados.nome() != null && !dados.nome().isBlank()) {
			this.nome = dados.nome();
		}
		if (dados.email() != null && !dados.email().isBlank()) {
			this.email = dados.email();
		}
		//Senha é criptografada e alterada no controller
		if (dados.vencimentoMandato() != null) {
			this.vencimentoMandato = dados.vencimentoMandato();
		}
		
	}
	
}
