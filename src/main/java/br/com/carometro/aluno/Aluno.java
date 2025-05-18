package br.com.carometro.aluno;

import java.time.LocalDate;
import java.util.Set;

import br.com.carometro.curso.Curso;
import br.com.carometro.historico.Historico;
import br.com.carometro.links.Links;
import br.com.carometro.unidfatec.UnidFatec;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "aluno_id")
	private Long id;
	private String nome;
	private String email;
	private String senha;
	private LocalDate dtNascimento;	
	private String comentarioFATEC;
	private String comentario;
	private Integer ano;
	@OneToOne
	@JoinColumn(name="unid_fatec_id", nullable=false)
	private UnidFatec unidFATEC;
	//TODO: Trocar maneira de salvar a foto
	@Lob
	@Column(name = "foto", columnDefinition = "LONGBLOB")
	private byte[] foto;

	//Como é UM CURSO para MUITOS alunos, alunos é a classe Many, portanto recebe o @ManyToOne
	@ManyToOne
	@JoinColumn(name="curso_id", nullable=false)
	private Curso curso;
	//Um aluno vai ter muitos históricos
	@OneToMany(mappedBy = "aluno")
	private Set<Historico> historico;
	@OneToOne(mappedBy = "aluno", cascade = CascadeType.ALL)
	private Links links;
	
	
	public Aluno(DadosCadastroAluno dados) {
		this.nome = dados.nome();
		this.email = dados.email();
		this.senha = dados.senha();
		this.dtNascimento = dados.dtNascimento();
		//TODO: Verificar se os dados capturados no html realmente criam a entidade unidFatec
		this.comentarioFATEC = dados.comentarioFATEC();
		this.comentario = dados.comentario();
		this.ano = dados.ano();
		this.curso = dados.curso();
		//TODO: Add DTO do histórico
	}
	
	public void atualizarInformacoes(DadosAtualizacaoAluno dados) {
		if (dados.nome() != null) {
			this.nome = dados.nome();
		}
		if (dados.email() != null) {
			this.email = dados.email();
		}
		if (dados.senha() != null) {
			this.senha = dados.senha();
		}		
		if (dados.dtNascimento() != null) {
			this.dtNascimento = dados.dtNascimento();
		}
		if (dados.unidFATEC() != null) {
			this.unidFATEC = dados.unidFATEC();
		}
		if (dados.comentarioFATEC() != null) {
			this.comentarioFATEC = dados.comentarioFATEC();
		}
		if (dados.comentario() != null) {
			this.comentario = dados.comentario();
		}
		if (dados.ano() != null) {
			this.ano = dados.ano();
		}	
		//TODO: Método deve receber um set de históricos
		
//		if (dados.empresaTrabalho() != null) {
//			this.historico.setEmpresaTrabalho(dados.empresaTrabalho());
//		}
//		if (dados.descricaoTrabalho() != null) {
//			this.historico.setDescricaoTrabalho(dados.descricaoTrabalho());
//		}
//		if (dados.tempoTrabalho() != 0) {
//			this.historico.setTempoTrabalho(dados.tempoTrabalho());
//		}
		if (dados.gitHub() != null) {
			this.links.setGitHub(dados.gitHub());
		}
		if (dados.linkedIn() != null) {
			this.links.setLinkedIn(dados.linkedIn());
		}
		if (dados.lattesCNPQ() != null) {
			this.links.setLattesCNPQ(dados.lattesCNPQ());
		}
		
	}
}
