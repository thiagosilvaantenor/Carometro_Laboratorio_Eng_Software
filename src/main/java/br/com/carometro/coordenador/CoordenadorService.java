package br.com.carometro.coordenador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
	
	
	public void salvar(Coordenador cordenador) throws Exception {
		try {
			if (repository.findbyEmail(cordenador.getEmail()) != null) {
				throw new Exception("Este email já está cadastrado: " + cordenador.getEmail());
			}

			cordenador.setSenha(Criptografia.md5(cordenador.getSenha()));

		} catch (Exception e) {
			throw new Exception("Erro na criptografia da senha");
		}
		repository.save(cordenador);
	}

	public Coordenador login(String email, String senha) {
		return repository.buscaLogin(email,senha);
	}
}
