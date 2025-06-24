package br.com.carometro.egresso;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.carometro.historico.Historico;
import br.com.carometro.historico.HistoricoRepository;
import br.com.carometro.security.Criptografia;

@Service
public class EgressoService {
	@Autowired
	private EgressoRepository repository;
	
	@Autowired
	private HistoricoRepository repositoryHistorico;

	@Value("${upload.dir}")
	private String uploadDir;
	
	
	public List<Egresso> getAllEgresso() {
		return repository.findAll(Sort.by("nome").ascending());
	}

	public Egresso getEgressoById(Long id) {
		return repository.getReferenceById(id);
	}

	//Método para salvar a foto, vai salvar a foto localmente em /src/main/resources/static/upload/egressos/ e no banco de dados vai salvar o caminho
	public String salvarFoto(MultipartFile foto, Egresso egresso) throws IOException {
	    if (foto != null && !foto.isEmpty()) {
	        String nomeArquivo = UUID.randomUUID() + "_" + foto.getOriginalFilename();
	        //Pega o caminho do diretorio /uploads
	        Path diretorio = Paths.get(uploadDir);
	        //Cria os diretórios
	        Files.createDirectories(diretorio);
	        //Cria o caminho da foto
	        Path caminhoFoto = diretorio.resolve(nomeArquivo);
	        //Salva localmente a foto
	        Files.write(caminhoFoto, foto.getBytes());
	        //Coloca o caminho do diretorio da foto como foto no egresso
	        egresso.setFoto("/uploads/" + nomeArquivo);
	    }
	    return egresso.getFoto();
	}
	
	
	
	public void salvar(Egresso egresso) throws Exception {
		try {
			if (repository.findByEmail(egresso.getEmail()) != null) {
				throw new Exception("Este email já está cadastrado: " + egresso.getEmail());
			}
	
			egresso.setSenha(Criptografia.md5(egresso.getSenha()));

		} catch (Exception e) {
			throw new Exception("Erro na criptografia da senha");
		}
		repository.save(egresso);
	}

	//Verifica o login(email e senha) do aluno
	public Optional<Egresso> login(String email, String senha) {
		return repository.findByEmailAndSenha(email,senha);
	}

	public Egresso findByEmail(String email) {
		return repository.findByEmail(email);
	}
	
	public List<Egresso> filtraEgressoPeloCurso(Long cursoId){
		return repository.findByCursoId(cursoId);
	}
	
	public List<Egresso> filtraEgressoPeloCursoESituacaoCadastor(Long cursoId, Boolean situacaoCadastro){
		return repository.findByCursoIdAndSituacaoCadastro(cursoId, situacaoCadastro);
	}
	
	public List<Egresso> filtrarEgressoPeloCursoESituacaoComentario(Long cursoId, Boolean situacaoComentario){
		return repository.findByCursoIdAndSituacaoComentario(cursoId, situacaoComentario);
	}
	
	public List<Egresso> filtraEgressoPeloAnoSemestre(Integer ano){
		return repository.findByAno(ano);		
	}
	
	public List<Egresso> buscaEgressoPelaSituacaoCadastro(Boolean situacao){
		if (situacao != null)
			return repository.findBySituacaoCadastro(situacao);
		throw new IllegalArgumentException();
	}
		
	//Filtra os alunos
	public List<Egresso> filtrarEgresso(Integer ano, Long cursoId, Boolean situacao){
			//Informou tudo
		 	if (cursoId != null && ano != null && ano > 0 && situacao != null) {
		        return repository.findByCursoIdAndAno(cursoId, ano);
		    } else if (cursoId != null && situacao != null) { // Informou curso e situacao
		        return repository.findByCursoIdAndSituacaoCadastro(cursoId, situacao);
		    } else if (cursoId != null ) { // Informou curso
		        return repository.findByCursoId(cursoId);
		    } else if (ano != null && ano > 0 && situacao != null) { // informou ano e situacao
		        return repository.findByAnoAndSituacaoCadastro(ano, situacao);
		    }  else if (ano != null && ano > 0) { // informou ano
		        return repository.findByAno(ano);
		    }  else if (situacao != null){ // informou apenas situacao
		        return repository.findBySituacaoCadastro(situacao);
		    } else {// Não informou nada
		    	return repository.findAll();
		    }
	}
	
	//Caso o aluno cadastrado for realmente ex-aluno, muda o estado de Situação cadastro e passa a listar ele na postagem
	public void aprovarEgresso(Long id) throws IllegalArgumentException {
		Optional<Egresso> egressoBuscado = repository.findById(id);
		if (egressoBuscado.isPresent()) {
			Egresso egresso = egressoBuscado.get();
			egresso.setSituacaoCadastro(true);
			repository.save(egresso);
		} else {
			throw new IllegalArgumentException("Erro ao tentar buscar e aprovar egresso");
		}
		
	}
	//Caso o aluno cadastrado não for ex-aluno, não tem motivo deixar ele no banco de dados, então deleta ele
	public void reprovarEgresso(Long id) throws IllegalArgumentException {
		Optional<Egresso> egressoBuscado = repository.findById(id);
		if (egressoBuscado.isPresent()) {
			remover(id);
		} else {
			throw new IllegalArgumentException("Erro ao tentar buscar e aprovar egresso");
		}
		
	}
	
	
	//Caso os comentarios forem aprovados, muda estado de situacaoComentario
		public void aprovarComentario(Long id) throws IllegalArgumentException {
			Optional<Egresso> egressoBuscado = repository.findById(id);
			if (egressoBuscado.isPresent()) {
				Egresso egresso = egressoBuscado.get();
				egresso.setSituacaoComentario(true);
				repository.save(egresso);
			} else {
				throw new IllegalArgumentException("Erro ao tentar buscar e aprovar comentario do egresso");
			}
			
		}
		//Caso pelo menos 1 dos comentarios não foi reprovado, apaga os comentarios reprovados
		public void reprovarComentario(Long id, String tipoComentario) throws IllegalArgumentException {
			Optional<Egresso> egressoBuscado = repository.findById(id);
			if (egressoBuscado.isPresent() && (tipoComentario.equalsIgnoreCase("geral") 
					|| tipoComentario.equalsIgnoreCase("fatec") || tipoComentario.equalsIgnoreCase("comentarios") )) {
				
				Egresso egresso = egressoBuscado.get();
				egresso.setSituacaoComentario(false);
				//verifica qual comentario foi reprovado e atualiza o egresso
				if (tipoComentario.equalsIgnoreCase("geral"))
					egresso.setComentario(null);
				else if (tipoComentario.equalsIgnoreCase("fatec"))
					egresso.setComentarioFATEC(null);
				else {
					egresso.setComentarioFATEC(null);
					egresso.setComentario(null);
				}
				
				repository.save(egresso);
			} else {
				throw new IllegalArgumentException("Erro ao tentar buscar e aprovar egresso");
			}
			
		}

	public void remover(Long id) {
		Optional<Egresso> egressoBuscado = repository.findById(id);
		if (egressoBuscado.isPresent()) {			
			//Remove a relação de aluno e historico
			List<Historico> historicosDoEgresso = repositoryHistorico.findByEgressoId(id); 
			historicosDoEgresso.forEach(repositoryHistorico::delete);
			repository.deleteById(id);
		}
		
	}

	public void reprovarFoto(Long id) {
		Optional<Egresso> egressoBuscado = repository.findById(id);
		if (egressoBuscado.isPresent() ) {
			Egresso egresso = egressoBuscado.get();
			egresso.setFoto(null);
			//Garante que a foto não vai ser exibida na postagem
			egresso.setSituacaoFoto(false);
			repository.save(egresso);
		}
			
	}

	public void aprovarFoto(Long id) {
		//Busca egresso
		Optional<Egresso> egressoBuscado = repository.findById(id);
		//Se achou muda o estado de situacaoFoto
		if (egressoBuscado.isPresent() ) {
			Egresso egresso = egressoBuscado.get();
			egresso.setSituacaoFoto(true);
			repository.save(egresso);
		}
		
	}

	public List<Egresso> filtraEgressoPelaSituacaoCadastro(boolean situacao) {
		return repository.findBySituacaoCadastro(situacao);
	}
	
	public List<Egresso> filtraEgressoAindaNaoAprovadosEmAlgumaSituacao() {
		return repository.findBySituacaoCadastroFalseOrSituacaoComentarioFalseOrSituacaoFotoFalse();
	}
	
}
