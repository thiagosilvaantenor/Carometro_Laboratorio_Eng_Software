package br.com.carometro.aluno;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.carometro.curso.Curso;
import br.com.carometro.curso.CursoService;
import br.com.carometro.security.Criptografia;

@Service
public class AlunoService {

	@Autowired
	private AlunoRepository repository;
	
	@Autowired
	private CursoService cursoService;
	
	public List<Aluno> getAll(){
		return repository.findAll();
	}
	
	public void salvar(Aluno aluno) {
		try {
			if (repository.findByEmail(aluno.getEmail()) != null && aluno.getId() == null) { // Adicionado check para ID nulo para permitir atualização
				throw new IllegalStateException("Este email já está cadastrado: " + aluno.getEmail());
			}
			//Como senha pode ser null, criptografa ela apenas se existir e não for vazia
			if (aluno.getSenha() != null && !aluno.getSenha().isEmpty()) {
				aluno.setSenha(Criptografia.md5(aluno.getSenha()));				
			}

		} catch (Exception e) {
			new RuntimeException("Erro na criptografia da senha " + e.getMessage());
		}
		repository.save(aluno);
	}

	public Optional<Aluno> getById(Long id) {
		return repository.findById(id);
	}

	public List<Aluno> filtarPorNomeECurso(String nome, Long cursoId) {
		return repository.findByNomeAndCursoId(nome, cursoId);
	}

	public List<Aluno> filtarPorNome(String nome) {
		return repository.findByNome(nome);
	}

	public List<Aluno> filtarPorCurso(Long cursoId) {
		return repository.findByCursoId(cursoId);
	}
	
	//Método para lidar com cadastro através de envio de arquivo csv
	public List<Aluno> processarArquivoCsv(MultipartFile file) throws IOException {
		List<Aluno> alunos = new ArrayList<>();
		try (BufferedReader br = new BufferedReader
				(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
			String linha;
			//Inteiros para armazenar o indice da coluna de cada atributo
			int nomeI = -1;
			int telefoneI = -1;
			int emailI = -1;
			int cursoI = -1;
			boolean primeiraLinha = true;
			while ((linha = br.readLine()) != null) {
				String[] dados = linha.split(",");
				//Caso seja a linha do cabeçalho, entra no if para verificar qual os indices das colunas de cada atributo
				if (primeiraLinha) {
					primeiraLinha = false;
					for(int i = 0; i < dados.length; i++) {
						//Padroniza a coluna a ser comparada
						String cabecalhoColuna = dados[i].trim().toLowerCase();
						if(cabecalhoColuna.equalsIgnoreCase("nome")) {
							nomeI = i;
						}
						else if(cabecalhoColuna.equalsIgnoreCase("email")) {
							emailI = i;
						}
						else if(cabecalhoColuna.equalsIgnoreCase("telefone")) {
							telefoneI = i;
							
						}
						else if(cabecalhoColuna.equalsIgnoreCase("curso")) {
							cursoI = i;
						}
					}
					// Se o cabeçalho não contiver todas as colunas esperadas, você pode lançar uma exceção
					if (nomeI == -1 || emailI == -1 || telefoneI == -1 || cursoI == -1) {
						throw new IllegalArgumentException("CSV sem cabeçalho ou colunas obrigatórias faltando (Nome, Email, Telefone, Curso)");
					}
					continue; // Pula para a próxima linha depois de processar o cabeçalho
				}
				//Assumindo que o separador do arquivo seja virgula
				if(dados.length >= 4) {//O minimo tem que ser 4 dados 
					String nome = dados[nomeI].trim();
					String email = dados[emailI].trim();
					String telefone = dados[telefoneI].trim();
					String curso = dados[cursoI].trim();
					
					
					Aluno aluno = new Aluno(nome, email, telefone);
					// Gera senha padrão para o aluno depois trocar
					aluno.setSenha("senhaPadrao123");
					Optional<Curso> cursoEncontrado = cursoService.getByNome(curso);
					if (cursoEncontrado.isPresent()) {
						aluno.setCurso(cursoEncontrado.get());
					}
					
					// Verifica se o aluno já existe pelo email antes de salvar
					Aluno alunoExistente = repository.findByEmail(aluno.getEmail());
					if (alunoExistente != null) {
						// Atualiza o aluno existente
						alunoExistente.setNome(aluno.getNome());
						alunoExistente.setTelefone(aluno.getTelefone());
						alunoExistente.setCurso(aluno.getCurso());
						// Não atualiza a senha aqui
						alunos.add(alunoExistente); // Adiciona o aluno atualizado à lista
					} else {
						// Salva novo aluno
						alunos.add(aluno);
					}
				} else {
					// Opcional: logar linhas que não têm dados suficientes
					System.err.println("Linha ignorada por ter dados insuficientes: " + linha);
				}
					
					
				}
			}
			return repository.saveAll(alunos);
		}
	
	
}
