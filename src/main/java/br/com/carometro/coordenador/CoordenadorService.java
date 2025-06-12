package br.com.carometro.coordenador;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.carometro.egresso.Egresso;
import br.com.carometro.egresso.EgressoRepository;
import br.com.carometro.security.Criptografia;

@Service
public class CoordenadorService {
	@Autowired
	private CoordenadorRepository repository;
	


	public List<Coordenador> getAllCoordenador() {
		return repository.findAll(Sort.by("nome").ascending());
	}

	public Coordenador getCoordenadorById(Long id) {
		return repository.getReferenceById(id);
	}
	
	
	public void salvar(Coordenador coordenador) throws Exception {
		try {
			
			if (repository.findByEmail(coordenador.getEmail()).isPresent()) {
				throw new Exception("Este email já está cadastrado: " + coordenador.getEmail());
			}
			coordenador.setSenha(Criptografia.md5(coordenador.getSenha()));

		} catch (NoSuchAlgorithmException e) {
			 throw new RuntimeException("Erro ao criptografar senha", e.getCause());
		}
		repository.save(coordenador);
	}

	public Optional<Coordenador> login(String email, String senha) {
		return repository.buscaLogin(email,senha);
	}

	public Optional<Coordenador> findbyEmail(String email) {
		return repository.findByEmail(email);
	}
	

}
