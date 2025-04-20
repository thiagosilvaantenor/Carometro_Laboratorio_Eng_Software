package br.com.carona.aluno;

import java.time.LocalDate;

import br.com.carona.curso.Curso;
import br.com.carona.curso.DadosAtualizacaoCurso;
import br.com.carona.historico.Historico;
import br.com.carona.links.Links;
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
	private int ano;
	private String unidFATEC;
	@Lob
	@Column(name = "foto", columnDefinition = "LONGBLOB")
	private byte[] foto;

	//Como é UM CURSO para MUITOS alunos, alunos é a classe Many, portanto recebe o @ManyToOne
	@ManyToOne
	@JoinColumn(name="curso_id", nullable=false)
	private Curso curso;
	//Talvez seria: Um aluno possui Um histórico?Com um id pra cada lista teria um registro especifico para cada aluno
	@ManyToOne
	@JoinColumn(name="historico_id", nullable=false)
	private Historico historico;
	//Talvez seria: Um aluno possui Uma lista de links?Com um id pra cada lista teria um registro especifico para cada aluno
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
		this.foto = dados.foto();
		this.unidFATEC = dados.unidFATEC();
		this.comentarioFATEC = dados.comentarioFATEC();
		this.comentario = dados.comentario();
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
		if (dados.foto() != null) {
			this.foto = dados.foto();
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
		if (dados.curso() != null) {
			this.curso = dados.curso();
		}

	}
}
