package br.com.carometro.aluno;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import jakarta.validation.constraints.Email;
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
	@Email
	private String email;
	private String senha;
	private LocalDate dtNascimento;	
	private String comentarioFATEC;
	private String comentario;
	private Integer ano;
	//TODO: Trocar maneira de salvar a foto
	@Lob
	@Column(name = "foto", columnDefinition = "LONGBLOB")
	private byte[] foto;

	//1 curso para N alunos
	@ManyToOne
	@JoinColumn(name="curso_id", nullable=false)
	private Curso curso;
	//1 Aluno para N Historicos , cascata do tipo All para quando for salvar um aluno salvar o histórico e o mesmo para remover
	@OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
	private List<Historico> historico = new ArrayList<>();
	//Um aluno para 1 lista de redes, cascata do tipo All para quando for salvar um aluno salvar os links e o mesmo para remover
	@OneToOne(mappedBy = "aluno", cascade = CascadeType.ALL)
	private Links links;
	
	
	public Aluno(DadosCadastroAluno dados) {
		this.nome = dados.nome();
		this.email = dados.email();
		this.senha = dados.senha();
		this.dtNascimento = dados.dtNascimento();
		this.comentarioFATEC = dados.comentarioFATEC();
		this.comentario = dados.comentario();
		this.ano = dados.ano();
		this.curso = dados.curso();
		//DTO do historico é adicionado no controller
		//Relações de historico e links são feitas no controller
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
		if (dados.comentarioFATEC() != null) {
			this.comentarioFATEC = dados.comentarioFATEC();
		}
		if (dados.comentario() != null) {
			this.comentario = dados.comentario();
		}
		if (dados.ano() != null) {
			this.ano = dados.ano();
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
