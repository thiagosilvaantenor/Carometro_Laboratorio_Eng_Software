package br.com.carona.curso;

import java.util.List;

import br.com.carona.aluno.Aluno;
import br.com.carona.coordenador.Coordenador;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity (name="curso")
@Table(name = "curso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Curso {
	public Curso(DadosCadastroCurso dados) {
		this.nome = dados.nome();
		this.descricao = dados.descricao();
		this.duracao = dados.duracao();
		this.modalidade = dados.modalidade();
		this.turno = dados.turno();
		this.areaAtuacao = dados.areaAtuacao();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="curso_id")
	private long id;
	private String nome;
	private String descricao;
	private int duracao;
	private String modalidade;
	private String turno;
	private String areaAtuacao;
	
	@OneToOne(mappedBy = "curso")
	private Coordenador coordenador;
	
	@OneToMany(mappedBy = "curso")
	private List<Aluno> alunos;

	public void atualizarInformacoes(DadosAtualizacaoCurso dados) {
		if (dados.nome() != null) {
			this.nome = dados.nome();
		}
		if (dados.descricao() != null) {
			this.descricao = dados.descricao();
		}
		if (dados.duracao() != 0) {
			this.duracao = dados.duracao();
		}
		if (dados.modalidade() != null) {
			this.modalidade = dados.modalidade();
		}
		if (dados.turno() != null) {
			this.turno = dados.turno();
		}
		if (dados.areaAtuacao() != null) {
			this.areaAtuacao = dados.areaAtuacao();
		}
		
	}
}
