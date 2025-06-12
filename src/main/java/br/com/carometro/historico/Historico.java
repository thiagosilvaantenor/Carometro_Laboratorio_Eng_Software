package br.com.carometro.historico;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.carometro.egresso.Egresso;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	private Long id;
	private String empresaTrabalho;
	private String descricaoTrabalho;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dtInicio;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dtFim;
	
	
	@ManyToOne
	@JoinColumn(name="egresso_id", nullable=false)
	private Egresso egresso;
	
}
