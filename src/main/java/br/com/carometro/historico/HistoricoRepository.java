package br.com.carometro.historico;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {

	List<Historico> findByAlunoId(Long id);

}
