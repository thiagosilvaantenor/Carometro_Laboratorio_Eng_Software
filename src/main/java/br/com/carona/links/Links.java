package br.com.carona.links;

import java.util.List;

import br.com.carona.aluno.Aluno;
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
@Entity (name="links")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Links {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="links_id")
	private long id;
	private String lattesCNPQ;
	private String gitHub;
	private String linkedIn;
	
	@OneToMany(mappedBy = "links")
	private List<Aluno> alunos;
}
