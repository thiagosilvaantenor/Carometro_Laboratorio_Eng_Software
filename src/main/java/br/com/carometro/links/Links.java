package br.com.carometro.links;

import br.com.carometro.egresso.Egresso;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
	private Long id;
	private String lattesCNPQ;
	private String gitHub;
	private String linkedIn;
	
	@OneToOne
	@JoinColumn(name = "egresso_id", referencedColumnName = "egresso_id", nullable = false)
	private Egresso egresso;
}
