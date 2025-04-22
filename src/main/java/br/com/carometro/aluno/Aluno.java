package br.com.carometro.aluno;

import java.time.LocalDate;

import br.com.carometro.curso.Curso;
import br.com.carometro.curso.DadosAtualizacaoCurso;
import br.com.carometro.historico.Historico;
import br.com.carometro.links.Links;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
	private String matricula;
	private String cpf;
	private String email;
	private String senha;
	private LocalDate dtNascimento;	
	private String comentarioFATEC;
	private String comentario;
	private Integer ano;
	private String unidFATEC;
	@Lob
	@Column(name = "foto", columnDefinition = "LONGBLOB")
	private byte[] foto;

	//Como é UM CURSO para MUITOS alunos, alunos é a classe Many, portanto recebe o @ManyToOne
	@ManyToOne
	@JoinColumn(name="curso_id", nullable=false)
	private Curso curso;
	@ManyToOne
	@JoinColumn(name="historico_id", nullable=false)
	private Historico historico;
	@ManyToOne
	@JoinColumn(name="links_id", nullable=false)
	private Links links;
	
	
	public Aluno(DadosCadastroAluno dados) {
		this.nome = dados.nome();
		this.matricula = dados.matricula();
		this.cpf = dados.cpf();
		this.email = dados.email();
		this.senha = dados.senha();
		this.dtNascimento = dados.dtNascimento();
		this.unidFATEC = dados.unidFATEC();
		this.comentarioFATEC = dados.comentarioFATEC();
		this.comentario = dados.comentario();
		this.ano = dados.ano();
		this.curso = dados.curso();
	}
	
	public void atualizarInformacoes(DadosAtualizacaoAluno dados) {
		if (dados.nome() != null) {
			this.nome = dados.nome();
		}
		if (dados.matricula() != null) {
			this.matricula = dados.matricula();
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
		if (dados.empresaTrabalho() != null) {
			this.historico.setEmpresaTrabalho(dados.empresaTrabalho());
		}
		if (dados.descricaoTrabalho() != null) {
			this.historico.setDescricaoTrabalho(dados.descricaoTrabalho());
		}
		if (dados.tempoTrabalho() != 0) {
			this.historico.setTempoTrabalho(dados.tempoTrabalho());
		}
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
