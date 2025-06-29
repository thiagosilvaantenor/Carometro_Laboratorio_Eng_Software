package br.com.carometro.egresso;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import br.com.carometro.curso.Curso;
import br.com.carometro.historico.Historico;
import br.com.carometro.links.Links;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

@Table(name = "egresso")
@Entity(name = "egresso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Egresso {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "egresso_id")
	private Long id;
	private String nome;
	@Email
	private String email;
	private String senha;
	private String comentarioFATEC;
	private String comentario;
	private Integer ano;
	//Atributo texto'sobre mim' do egresso, opcional
	@Column(name = "sobre", nullable = true, length = 100)
	private String sobre;
	//Atributo texo 'sobre mim profissional', opcional
	@Column(name = "sobre_profissional",length = 100, nullable = true) 
	private String sobreProfissional;
	//Atributo texo 'sobre mim profissional', opcional
	@Column(name = "sobre_Fatec",nullable = true, length = 100)
	private String sobreFatec;
	//Atributo usado para lidar com aprovação de cadastro aluno, se ainda não foi avaliado = false, se é aluno = true;
	@ColumnDefault("false")
	private Boolean situacaoCadastro = false;
	//Atributo usado para lidar com aprovação dos comentarios do aluno
	@ColumnDefault("false")
	private Boolean situacaoComentario = false;
	//NO banco de dados será salvo o caminho da foto, ex: "uploads/egresso/foto.jpg" 
	@Column(name = "foto", length = 200)
	private String foto;
	@Column(name = "situacao_foto")
	private Boolean situacaoFoto = false;
	
	//1 curso para N alunos
	@ManyToOne
	@JoinColumn(name="curso_id", nullable=false)
	private Curso curso;
	//1 Aluno para N Historicos , cascata do tipo All para quando for salvar um aluno salvar o histórico e o mesmo para remover
	@OneToMany(mappedBy = "egresso", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Historico> historico = new ArrayList<>();
	//Um aluno para 1 lista de redes, cascata do tipo All para quando for salvar um aluno salvar os links e o mesmo para remover
	@OneToOne(mappedBy = "egresso", cascade = CascadeType.ALL)
	private Links links;
	//Atributo para garantir que o usuário concorda com a divulgação de foto e comentarios expressos
	@Column(name = "consentimento_divulgacao", nullable = false)
	private Boolean consentimentoDivulgacao;
	
	public Egresso(DadosCadastroEgresso dados) {
		this.nome = dados.nome();
		this.email = dados.email();
		this.senha = dados.senha();
		this.comentarioFATEC = dados.comentarioFATEC();
		this.comentario = dados.comentario();
		this.ano = dados.ano();
		this.curso = dados.curso();
		this.sobre = dados.sobre();
		this.sobreFatec = dados.sobreFatec();
		this.sobreProfissional = dados.sobreProfissional();
		this.consentimentoDivulgacao = dados.consentimentoDivulgacao();
		//DTO do historico é adicionado no controller
		//Relações de historico e links são feitas no controller
	}
	
	public void atualizarInformacoes(DadosAtualizacaoEgresso dados) {
		if (dados.nome() != null) {
			this.nome = dados.nome();
		}
		if (dados.email() != null) {
			this.email = dados.email();
		}
		//Senha é criptografada e alterada no controller		
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
		if (dados.sobre() != null) {
			this.sobre = dados.sobre();
		}
		if (dados.sobreProfissional() != null) {
			this.sobreProfissional = dados.sobreProfissional();
		}
		if (dados.sobreFatec() != null) {
			this.sobreFatec = dados.sobreFatec();
		}
	}
}
