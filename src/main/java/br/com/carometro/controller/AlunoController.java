package br.com.carometro.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.servlet.ModelAndView;

import br.com.carometro.adm.Administrador;
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
import br.com.carometro.unidfatec.UnidFatecService;
import jakarta.servlet.http.HttpSession;
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
	private CursoService cursoService;
	
	@Autowired
	private HistoricoRepository historicoRepository;
	
	
	@GetMapping("/formulario")
	public String carregaPaginaFormulario(Long id, Model model) {
		//Manda a lista de cursos do banco de dados
		model.addAttribute("cursos", cursoService.getAllCursos());
		Aluno aluno = null;
		//Caso seja uma edição(PUT)
		if (id != null) {
			aluno = repository.getReferenceById(id);
			//Verifica e garante que tanto historico como links não sejam nulos ao editar aluno
			if (aluno.getHistorico() == null) {
				List<Historico> historico = new ArrayList<>();
				historico.add(new Historico());
				
				aluno.setHistorico(historico);
				
			}
			if (aluno.getLinks() == null) {
				aluno.setLinks(new Links());
			}			
		}//Caso seja um cadastro (POST) 
		else {
			
			aluno = new Aluno();
			aluno.setHistorico(new ArrayList<>());
			aluno.setLinks(new Links());
		}
		//De toda maneira envia para a model a entidade aluno
		model.addAttribute("aluno", aluno);
		return "aluno/formulario";
	}
	
	@GetMapping
	public String carregaPaginaListagem(Model model) {
		//Lista de alunos com cadastro aprovado pelo coordenador
		List<Aluno> alunos = service.buscaAlunosPelaSituacaoCadastro(true);
		model.addAttribute("lista", alunos);
		//Filtros
		//Separa os anos semestres cadastrados e envia a lista para a model
		List<Integer> anosSemestres = new ArrayList<>();
		alunos.forEach( a -> {
			//Verifica se o ano semestre ja esta na lista, se não adiciona
			if(!anosSemestres.contains(a.getAno())) {
				anosSemestres.add(a.getAno());							
			}
		});
		model.addAttribute("anos", anosSemestres);
		model.addAttribute("cursos", cursoService.getAllCursos());
		return "aluno/listagem";
	}
	
	@GetMapping("/filtrar")
	public String filtrarPaginaListagem(@RequestParam(name ="cursoId", required=false) Long cursoId,
			@RequestParam(name= "ano", required= false) Integer ano,
			Model model) {
		//Lista de alunos filtrados que tiveram cadastro aprovado pelo coordenador
		List<Aluno> alunosFiltrados = service.filtrarAluno(ano, cursoId, true);
		//Lista de todos os alunos para pegar os anos semestres
		List<Aluno> alunos = service.buscaAlunosPelaSituacaoCadastro(true);
		List<Integer> anosSemestres = new ArrayList<>();
		alunos.forEach( a -> {
			//Verifica se o ano semestre ja esta na lista, se não adiciona
			if(!anosSemestres.contains(a.getAno())) {
				anosSemestres.add(a.getAno());							
			}
		});
		 	model.addAttribute("lista", alunosFiltrados);
		    model.addAttribute("anos", anosSemestres);
		    model.addAttribute("cursos", cursoService.getAllCursos());
		return "aluno/listagem";
	}
	
	@PostMapping
	@Transactional
	public String cadastrar(@Valid
			DadosCadastroAluno dados,
			@RequestParam("cursoId") Long cursoId) throws Exception {
		 // A lista de histórico já estará em dados.getHistorico()
	    List<DadosCadastroHistorico> dadosHistoricoSubmetidos = dados.historico();
		Curso curso = cursoService.getCursoById(cursoId);
		Aluno aluno = new Aluno(dados);
		// adiciona o aluno na lista de alunos
		curso.getAlunos().add(aluno);
		aluno.setCurso(curso);
		

		// Historico
		List<Historico> historico = new ArrayList<>();
		if (dadosHistoricoSubmetidos != null) {
			dadosHistoricoSubmetidos.forEach(item -> { 
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
					historicoRepository.save(novo);
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
			@RequestParam("cursoId") Long cursoId,
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
//		//Atualização do histórico
		
		if (dados.historico() != null) {
			List<DadosCadastroHistorico> dadosHistoricoSubmetidos = dados.historico();
			List<Historico> novoOuAtualizado = new ArrayList<>();
			
			for (DadosCadastroHistorico dto : dadosHistoricoSubmetidos) {
				// Ignora DTOs vazios que podem vir do formulário dinâmico
				if (dto.empresaTrabalho() != null && !dto.empresaTrabalho().trim().isEmpty()) {
					Historico entidadeHistorico;
					
					//Se tem id é atualização de um histórico já cadastrado
					if (dto.id() != null) {
						entidadeHistorico = historicoRepository.findById(dto.id()).orElse(new Historico());
						entidadeHistorico.setId(dto.id());
				} else {
					// Cria um novo histórico se o ID for nulo ou não encontrado nos existentes
					entidadeHistorico = new Historico();
				}
					
				entidadeHistorico.setEmpresaTrabalho(dto.empresaTrabalho());
				entidadeHistorico.setDescricaoTrabalho(dto.descricaoTrabalho());
				entidadeHistorico.setDtInicio(dto.dtInicio());
				entidadeHistorico.setDtFim(dto.dtFim());
				entidadeHistorico.setAluno(aluno); // Define a relação para o novo histórico	
				
				//Salva na lista
				novoOuAtualizado.add(entidadeHistorico);	
			}
		}
			// Adiciona a lista original sem trocar a referencia
			List<Historico> historicosAtuais = aluno.getHistorico();
			// Limpa a lista
			historicosAtuais.clear();
			historicosAtuais.addAll(novoOuAtualizado);
			
		}
		//Atualiza foto
		if (dados.foto() != null && !dados.foto().isEmpty()) {
	        aluno.setFoto(dados.foto().getBytes());
	    }
		
		//codifica a senha nova
		if (dados.senha() != null) {
			aluno.setSenha(Criptografia.md5(dados.senha()));
		}
		
		aluno.atualizarInformacoes(dados);
		return "redirect:aluno";
	}

	@DeleteMapping
	@Transactional
	public String removeAluno(Long id) {
		service.remover(id);
		return "redirect:aluno";
	}
	
	
	
	
	//Mapeamento da pagina inicio do administrador, para chegar lá é necessario ele se logar pelo LoginController
	@GetMapping("/index")
	public ModelAndView index(HttpSession session) {
		//Pega o administrador recebido do login
		Aluno alunoLogado = (Aluno) session.getAttribute("usuarioLogado");
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("aluno/index");
		modelAndView.addObject("aluno", alunoLogado);
		modelAndView.addObject("role", "aluno");
		return modelAndView;
	}
	
//	Mapeamento para exibir a foto
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
