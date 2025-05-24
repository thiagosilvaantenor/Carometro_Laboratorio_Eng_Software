package br.com.carometro.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.carometro.aluno.Aluno;
import br.com.carometro.aluno.AlunoRepository;
import br.com.carometro.aluno.AlunoService;
import br.com.carometro.aluno.DadosAtualizacaoAluno;
import br.com.carometro.aluno.DadosCadastroAluno;
import br.com.carometro.curso.Curso;
import br.com.carometro.curso.CursoService;
import br.com.carometro.historico.DadosCadastroHistorico;
import br.com.carometro.historico.Historico;
import br.com.carometro.historico.HistoricoRepository;
import br.com.carometro.links.Links;
import br.com.carometro.links.LinksRepository;
import br.com.carometro.security.Criptografia;
import br.com.carometro.unidfatec.UnidFatec;
import br.com.carometro.unidfatec.UnidFatecService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/aluno")
public class AlunoController {
	@Autowired
	private AlunoRepository repository;
	@Autowired
	private AlunoService service;
	@Autowired
	private HistoricoRepository repositoryHistorico;
	@Autowired
	private LinksRepository repositoryLinks;
	@Autowired
	private CursoService cursoService;
	
	@Autowired
	private UnidFatecService unidFatecService;
	
	@GetMapping("/formulario")
	public String carregaPaginaFormulario(Long id, Model model) {
		//Busca as unidades Fatec
		model.addAttribute("unidades", unidFatecService.buscaTodas());
		model.addAttribute("cursos", cursoService.getAllCursos());
		Aluno aluno = null;
		//Caso seja uma edição(PUT)
		if (id != null) {
			aluno = repository.getReferenceById(id);
			//Verifica e garante que tanto historico como links não sejam nulos ao editar aluno
			if (aluno.getHistorico() == null) {
				Set<Historico> historico = new HashSet<>();
				historico.add(new Historico());
				
				aluno.setHistorico(historico);
				
			}
			if (aluno.getLinks() == null) {
				aluno.setLinks(new Links());
			}			
		}//Caso seja um cadastro (POST) 
		else {
			
			aluno = new Aluno();
			aluno.setHistorico(new HashSet<>());
			aluno.setLinks(new Links());
		}
		//De toda maneira envia para a model a entidade aluno
		model.addAttribute("aluno", aluno);
		return "aluno/formulario";
	}
	
	@GetMapping
	public String carregaPaginaListagem(Model model) {
		model.addAttribute("lista", repository.findAll(Sort.by("nome").ascending()));
		return "aluno/listagem";
	}
	
	@PostMapping
	@Transactional
	public String cadastrar(@Valid
			DadosCadastroAluno dados,
			@ModelAttribute("aluno.historico") List<DadosCadastroHistorico> dadosHistoricoSubmetidos,
			@RequestParam("cursoId") Long cursoId, @RequestParam("fatecId") Long fatecId) throws Exception {
		
		
		 // Converte a lista submetida (que pode ser um proxy) para uma ArrayList padrão.
		List<DadosCadastroHistorico> listaDTOHistorico = new ArrayList<>(dadosHistoricoSubmetidos);
		Curso curso = cursoService.getCursoById(cursoId);
		Aluno aluno = new Aluno(dados);
		// adiciona o aluno na lista de alunos
		curso.getAlunos().add(aluno);
		aluno.setCurso(curso);
		
		
		//UnidFatec
		//UnidFATEC
		Optional<UnidFatec> unidFatec = unidFatecService.getUnidFatecById(fatecId);
		
		if (unidFatec.isEmpty()) {
			throw new Exception("Unidade fatec não encontrada");
		}
		//Se encontrou
		UnidFatec unidFatecEncontrada = unidFatec.get();
		// adiciona a relação coordenador e unidFatec
		aluno.setUnidFatec(unidFatecEncontrada);
				

		// Historico
		Set<Historico> historico = new HashSet<>();
		if (listaDTOHistorico != null) {
			listaDTOHistorico.forEach(item -> { 
				if (item.empresaTrabalho() != null) {
						
					Historico novo = new Historico();
					novo.setEmpresaTrabalho(item.empresaTrabalho());
					novo.setDescricaoTrabalho(item.descricaoTrabalho());
					novo.setDtInicio(item.dtInicio());
					//Caso este seja o trabalho atual, não terá data final
					if (item.dtFim() != null) {
						novo.setDtFim(item.dtFim());						
					}
					novo.setAluno(aluno);
					historico.add(novo);
					//Não precisa salvar historico devido ao cascade.TypeAll em Aluno
					//repositoryHistorico.save(novo);
				}
			});
			aluno.setHistorico(historico);
		}
		
		//Links
		if (dados.gitHub() != null || dados.linkedIn() != null) {
			Links links = new Links();
			links.setId(null);
			links.setGitHub(dados.gitHub());
			links.setLinkedIn(dados.linkedIn());
			links.setLattesCNPQ(dados.lattesCNPQ());
			
			links.setAluno(aluno);
			aluno.setLinks(links);
			//Salva primeiro os links no banco de dados
			//repositoryLinks.save(links);
		}
		
		 // convertendo a foto de MultipartFile para byte[]
	    if (dados.foto() != null && !dados.foto().isEmpty()) {
	        aluno.setFoto(dados.foto().getBytes());
	    }
				

		service.salvar(aluno);
		
		return "redirect:aluno";
	}
	
	@PutMapping
	@Transactional
	public String atualizar(@Valid DadosAtualizacaoAluno dados, 
			@ModelAttribute("aluno.historico") List<DadosCadastroHistorico> dadosHistorico,
			@RequestParam("cursoId") Long cursoId,
			@RequestParam("nomeFatec") String nomeFatec,
			Model model) throws IOException, NoSuchAlgorithmException {
		
		//Busca o aluno existente
		var aluno = repository.getReferenceById(dados.id());
		//Atualiza curso
		if (dados.curso() != null) {
			//Remove o curso antigo
			aluno.getCurso().getAlunos().remove(aluno);
			//Adiciona o curso novo
			aluno.setCurso(dados.curso());
			dados.curso().getAlunos().add(aluno);
		}
		
		//Unid FATEC
		// Atualiza a unidade Fatec
		//Busca a fatec existente
				if (nomeFatec != null && (aluno.getUnidFatec() == null || 
						!aluno.getUnidFatec().getNome().equals(nomeFatec))) {					
					UnidFatec unidFatec = unidFatecService.buscarPorNome(nomeFatec);
					if (unidFatec != null) {
						  model.addAttribute("erro", "Unidade Fatec não encontrada ao atualizar.");
			                // Repopular o modelo
			                model.addAttribute("unidades", unidFatecService.buscaTodas());
			                model.addAttribute("cursos", cursoService.getAllCursos());
			                model.addAttribute("aluno", aluno);
			                 model.addAttribute("selectedCursoId", cursoId);
			                model.addAttribute("selectedUnidadeId", unidFatec.getId());
			                return "aluno/formulario";
					}
					aluno.setUnidFatec(unidFatec);
				}
				
				// Se Links já existe, atualiza. Se não, cria um novo.
				Links links = aluno.getLinks();
				if (links == null) {
					links = new Links();
					links.setAluno(aluno); // Define a relação
					aluno.setLinks(links); // Associa ao aluno
					// Não precisa salvar links explicitamente se cascade está configurado
					// repositoryLinks.save(links); // Remova se estiver usando cascade
				}
				links.setGitHub(dados.gitHub());
				links.setLinkedIn(dados.linkedIn());
				links.setLattesCNPQ(dados.lattesCNPQ());
		
		
		//Atualização do histórico
		// 1. Mapeia históricos existentes pelo ID
				Set<Historico> historicosSalvos = new HashSet<>(aluno.getHistorico()); // Copia para iterar sem problemas
				Map<Long, Historico> mapHistoricosSalvos = historicosSalvos.stream()
						.collect(Collectors.toMap(Historico::getId, Function.identity()));

				// 2. Limpa a coleção no aluno para reconstruir
				aluno.getHistorico().clear();

				// 3. Processa a lista de DTOs submetida
				Set<Historico> novoOuAtualizado = new HashSet<>();
				if (dadosHistorico != null) {
					for (DadosCadastroHistorico dto : dadosHistorico) {
						// Ignora DTOs vazios que podem vir do formulário dinâmico
						if (dto.empresaTrabalho() != null && !dto.empresaTrabalho().trim().isEmpty()) {
							Historico entidadeHistorico = null;

							// Tenta encontrar um histórico existente pelo ID do DTO
							if (dto.id() != null && mapHistoricosSalvos.containsKey(dto.id())) {
								entidadeHistorico = mapHistoricosSalvos.get(dto.id());
								// Atualiza a entidade existente com os dados do DTO
								entidadeHistorico.setEmpresaTrabalho(dto.empresaTrabalho());
								entidadeHistorico.setDescricaoTrabalho(dto.descricaoTrabalho());
								entidadeHistorico.setDtInicio(dto.dtInicio());
								entidadeHistorico.setDtFim(dto.dtFim());
								// Remove do mapa de existentes para saber quais foram removidos
								mapHistoricosSalvos.remove(dto.id());
							} else {
								// Cria um novo histórico se o ID for nulo ou não encontrado nos existentes
								entidadeHistorico = new Historico();
								entidadeHistorico.setEmpresaTrabalho(dto.empresaTrabalho());
								entidadeHistorico.setDescricaoTrabalho(dto.descricaoTrabalho());
								entidadeHistorico.setDtInicio(dto.dtInicio());
								entidadeHistorico.setDtFim(dto.dtFim());
								entidadeHistorico.setAluno(aluno); // Define a relação para o novo histórico
							}
							// Adiciona o histórico (atualizado ou novo) à nova coleção
							novoOuAtualizado.add(entidadeHistorico);
						}
					}
				}

				// 4. Adiciona a coleção atualizada/nova de volta ao aluno
				aluno.setHistorico(novoOuAtualizado);

				// 5. Remove históricos que estavam no banco mas não foram submetidos
				// Esses são os históricos que sobraram no mapHistoricosSalvos
				mapHistoricosSalvos.values().forEach(historico -> {
					// Remover a relação do aluno antes de deletar 
					historico.setAluno(null); 
					repositoryHistorico.delete(historico); // Deleta do banco
				});		
	
		
		//Atualiza foto
		if (dados.foto() != null && !dados.foto().isEmpty()) {
	        aluno.setFoto(dados.foto().getBytes());
	    }
		
		//codifica a senha nova
		if (dados.senha() != null) {
			aluno.setSenha(Criptografia.md5(aluno.getSenha()));
		}
		
		aluno.atualizarInformacoes(dados);
		return "redirect:aluno";
	}

	@DeleteMapping
	@Transactional
	public String removeAluno(Long id) {
		//Remove a relação de aluno e historico
		List<Historico> historicosDoAluno = repositoryHistorico.findByAlunoId(id); 
		historicosDoAluno.forEach(repositoryHistorico::delete);
		repository.deleteById(id);
		return "redirect:aluno";
	}
	
	//Mapeamento para exibir a foto
	@GetMapping("/{id}/foto")
	@ResponseBody
	public ResponseEntity<byte[]> getFoto(@PathVariable Long id) {
		Aluno aluno = repository.findById(id).orElse(null);
		if (aluno != null && aluno.getFoto() != null) {
			return ResponseEntity.ok()
					.contentType(MediaType.IMAGE_JPEG)
					.body(aluno.getFoto());
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
}
