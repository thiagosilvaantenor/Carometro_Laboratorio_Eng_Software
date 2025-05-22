package br.com.carometro.unidfatec;

import java.util.ArrayList;
import java.util.List;

import br.com.carometro.adm.Administrador;
import br.com.carometro.coordenador.Coordenador;
import br.com.carometro.curso.Curso;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="unidFatec")
@Table(name="unid_fatec")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class UnidFatec {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "unidFatec_id")
	private Long id;
	private String nome;
    @OneToMany(mappedBy = "unidFatec") // "unidFatec" deve ser o nome do campo ManyToOne na classe Curso
    private List<Curso> cursos = new ArrayList<>();
    @OneToOne(mappedBy = "unidFatec")
    private Coordenador coordenador;
	@OneToOne(mappedBy = "unidFatec")
	private Administrador administrador;
	
}
