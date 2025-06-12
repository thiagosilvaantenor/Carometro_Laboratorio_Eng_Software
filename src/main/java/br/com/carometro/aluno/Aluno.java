package br.com.carometro.aluno;

import br.com.carometro.curso.Curso;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Aluno {
	//Entidade para lidar com os alunos que ainda não se formara, mas vão se cadastrar com antecedencia na plataforma

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "egresso_id")
	private Long id;
	private String nome;
	@Email
	private String email;
	//TODO: VALIDAÇÃO DE SENHA 
	private String senha;
	private String telefone;
	//1 curso para N alunos
	@ManyToOne
	@JoinColumn(name="curso_id", nullable=false)
	private Curso curso;

}
