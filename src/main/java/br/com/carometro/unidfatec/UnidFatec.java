package br.com.carometro.unidfatec;

import java.util.List;

import br.com.carometro.curso.Curso;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="unidFatec")
@Table(name="unid_Fatec")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UnidFatec {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "unidFatec_id")
	private Long id;
	private String nome;
	@OneToMany
	@JoinColumn(name = "curos_id")
	private List<Curso> cursos;
	
}
