package br.com.carometro.coordenador;

import java.time.LocalDate;

import br.com.carometro.curso.Curso;
import br.com.carometro.unidfatec.UnidFatec;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@EqualsAndHashCode(of = "id")
public class Coordenador {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coordenador_id")
	private Long id;
	private String nome; 
	private String email;
	private String senha;
	private LocalDate vencimentoMandato;
	@OneToOne
	@JoinColumn(name="curso_id")
	private Curso curso;
	@OneToOne
	@JoinColumn(name="unid_fatec_id")
	private UnidFatec unidFatec;
	
	public Coordenador(DadosCadastroCoordenador dados) {
		this.nome = dados.nome();
		this.email = dados.email();
		this.senha = dados.senha();
		this.vencimentoMandato = dados.vencimentoMandato();
		this.curso = dados.curso();
		//TODO: add unidFatec
	}

	public void atualizarInformacoes(DadosAtualizacaoCoordenador dados) {
		if (dados.nome() != null) {
			this.nome = dados.nome();
		}
		if (dados.email() != null) {
			this.email = dados.email();
		}
		if (dados.senha() != null) {
			this.senha = dados.senha();
		}
		if (dados.vencimentoMandato() != null) {
			this.vencimentoMandato = dados.vencimentoMandato();
		}
		if (dados.curso() != null) {
			this.curso = dados.curso();
		}
		//TODO: add unidFatec		
	}
	
}
