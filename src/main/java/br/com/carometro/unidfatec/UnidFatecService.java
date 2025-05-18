package br.com.carometro.unidfatec;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UnidFatecService {
	
	@Autowired
	private UnidFatecRepository repository;
	
	public List<UnidFatec> buscaTodas(){
		return repository.findAll();
	}

	public Optional<UnidFatec> getUnidFatecById(Long unidFatecId) {
		return repository.findById(unidFatecId);
	}

	public UnidFatec buscarPorNome(String nomeFatec) {
		return repository.findByNome(nomeFatec);
	}
	
	public UnidFatec salvar(UnidFatec unidFatec) throws Exception {
		if (unidFatec == null) {
			throw new Exception("Erro ao tentar salvar unidade Fatec");
		}
		return repository.save(unidFatec);
	}
}
