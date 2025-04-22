package br.com.carometro.historico;

import java.util.List;

import br.com.carometro.aluno.Aluno;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity (name="historico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Historico {
	

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="historico_id")
	private long id;
	private String empresaTrabalho;
	private String descricaoTrabalho;
	private int tempoTrabalho;
	
	@OneToMany (mappedBy = "historico")
	private List<Aluno> alunos;
	
}
