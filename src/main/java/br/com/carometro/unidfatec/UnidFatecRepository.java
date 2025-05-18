package br.com.carometro.unidfatec;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UnidFatecRepository extends JpaRepository<UnidFatec, Long> {

	UnidFatec findByNome(String nomeFatec);

}
