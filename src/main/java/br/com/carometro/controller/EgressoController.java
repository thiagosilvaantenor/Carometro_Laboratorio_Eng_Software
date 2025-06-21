package br.com.carometro.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.carometro.curso.Curso;
import br.com.carometro.curso.CursoService;
import br.com.carometro.egresso.DadosAtualizacaoEgresso;
import br.com.carometro.egresso.DadosCadastroEgresso;
import br.com.carometro.egresso.Egresso;
import br.com.carometro.egresso.EgressoRepository;
import br.com.carometro.egresso.EgressoService;
import br.com.carometro.historico.DadosCadastroHistorico;
import br.com.carometro.historico.Historico;
import br.com.carometro.historico.HistoricoRepository;
import br.com.carometro.links.Links;
import br.com.carometro.security.Criptografia;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/egresso")
public class EgressoController {
	@Autowired
	private EgressoRepository repository;
	@Autowired
	private EgressoService service;

	@Autowired
	private CursoService cursoService;
	
	@Autowired
	private HistoricoRepository historicoRepository;
	
	
	@GetMapping("/formulario")
	public String carregaPaginaFormulario(Long id, Model model) {
		//Manda a lista de cursos do banco de dados
		model.addAttribute("cursos", cursoService.getAllCursos());
		Egresso egresso = null;
		//Caso seja uma edição(PUT)
		if (id != null) {
			egresso = repository.getReferenceById(id);
			//Verifica e garante que tanto historico como links não sejam nulos ao editar egresso
			if (egresso.getHistorico() == null) {
				List<Historico> historico = new ArrayList<>();
				historico.add(new Historico());
				
				egresso.setHistorico(historico);
				
			}
			if (egresso.getLinks() == null) {
				egresso.setLinks(new Links());
			}			
		}//Caso seja um cadastro (POST) 
		else {
			
			egresso = new Egresso();
			egresso.setHistorico(new ArrayList<>());
			egresso.setLinks(new Links());
		}
		//De toda maneira envia para a model a entidade egresso
		model.addAttribute("egresso", egresso);
		return "egresso/formulario";
	}
	
	@GetMapping
	public String carregaPaginaListagem(Model model) {
		//Lista de egressos com cadastro aprovado pelo coordenador
		List<Egresso> egresso = service.buscaEgressoPelaSituacaoCadastro(true);
		model.addAttribute("lista", egresso);
		//Filtros
		//Separa os anos semestres cadastrados e envia a lista para a model
		List<Integer> anos = new ArrayList<>();
		egresso.forEach( e -> {
			//Verifica se o ano semestre ja esta na lista, se não adiciona
			if(!anos.contains(e.getAno())) {
				anos.add(e.getAno());							
			}
		});
		model.addAttribute("anos", anos);
		model.addAttribute("cursos", cursoService.getAllCursos());
		return "egresso/listagem";
	}
	
	@GetMapping("/filtrar")
	public String filtrarPaginaListagem(@RequestParam(name ="cursoId", required=false) Long cursoId,
			@RequestParam(name= "ano", required= false) Integer ano,
			Model model) {
		//Lista de egressos filtrados que tiveram cadastro aprovado pelo coordenador
		List<Egresso> egressosFiltrados = service.filtrarEgresso(ano, cursoId, true);
		//Lista de todos os egresso para pegar os anos semestres
		List<Egresso> egresso = service.buscaEgressoPelaSituacaoCadastro(true);
		List<Integer> anos = new ArrayList<>();
		egresso.forEach( a -> {
			//Verifica se o ano semestre ja esta na lista, se não adiciona
			if(!anos.contains(a.getAno())) {
				anos.add(a.getAno());							
			}
		});
		 	model.addAttribute("lista", egressosFiltrados);
		    model.addAttribute("anos", anos);
		    model.addAttribute("cursos", cursoService.getAllCursos());
		return "egresso/listagem";
	}
	
	@PostMapping
	@Transactional
	public String cadastrar(@Valid
			DadosCadastroEgresso dados,
			@RequestParam("cursoId") Long cursoId) throws Exception {
		 // A lista de histórico já estará em dados.getHistorico()
	    List<DadosCadastroHistorico> dadosHistoricoSubmetidos = dados.historico();
		Curso curso = cursoService.getCursoById(cursoId);
		Egresso egresso = new Egresso(dados);
		// adiciona o egresso na lista de egressos
		curso.getEgressos().add(egresso);
		egresso.setCurso(curso);
		

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
					novo.setEgresso(egresso);
					historico.add(novo);
					
				}
			});
			egresso.setHistorico(historico);
			//Não precisa usar repository.save pois o CASCADE.ALL garante que ao salvar o egresso salva historicos
		}
		
		//Links
		if (dados.gitHub() != null || dados.linkedIn() != null) {
			Links links = new Links();
			links.setId(null);
			links.setGitHub(dados.gitHub());
			links.setLinkedIn(dados.linkedIn());
			links.setLattesCNPQ(dados.lattesCNPQ());
			
			links.setEgresso(egresso);
			egresso.setLinks(links);
			//Não precisa usar repository.save pois o CASCADE.ALL garante que ao salvar o egresso salva links
		}
		
		 // convertendo a foto de MultipartFile para byte[]
	    if (dados.foto() != null && !dados.foto().isEmpty()) {
	        egresso.setFoto(dados.foto().getBytes());
	    }
				

		service.salvar(egresso);
		
		return "redirect:egresso";
	}
	
	@PutMapping
	@Transactional
	public String atualizar(@Valid DadosAtualizacaoEgresso dados,
			@RequestParam("cursoId") Long cursoId,
			Model model) throws IOException, NoSuchAlgorithmException {
		
		//Busca o egresso existente
		var egresso = repository.getReferenceById(dados.id());
		//Atualiza curso
		if (dados.curso() != null) {
			System.out.println(dados.historico().toString());
			//Remove o curso antigo
			egresso.getCurso().getEgressos().remove(egresso);
			//Adiciona o curso novo
			egresso.setCurso(dados.curso());
			dados.curso().getEgressos().add(egresso);
		}
		// Se Links já existe, atualiza. Se não, cria um novo.
		Links links = egresso.getLinks();
		if (links == null) {
			links = new Links();
			links.setEgresso(egresso); // Define a relação
			egresso.setLinks(links); // Associa ao egresso
			// Não precisa salvar links explicitamente se cascade está configurado
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
				entidadeHistorico.setEgresso(egresso); // Define a relação para o novo histórico	
				
				//Salva na lista
				novoOuAtualizado.add(entidadeHistorico);	
			}
		}
			// Adiciona a lista original sem trocar a referencia
			List<Historico> historicosAtuais = egresso.getHistorico();
			// Limpa a lista
			historicosAtuais.clear();
			historicosAtuais.addAll(novoOuAtualizado);
			
		}
		//Atualiza foto
		if (dados.foto() != null && !dados.foto().isEmpty()) {
	        egresso.setFoto(dados.foto().getBytes());
	    }
		
		//codifica a senha nova
		if (dados.senha() != null) {
			egresso.setSenha(Criptografia.md5(dados.senha()));
		}
		
		egresso.atualizarInformacoes(dados);
		return "redirect:egresso";
	}

	@DeleteMapping
	@Transactional
	public String removeEgresso(Long id) {
		service.remover(id);
		return "redirect:egresso";
	}
	
	
	//Mapeamento da pagina inicio do administrador, para chegar lá é necessario ele se logar pelo LoginController
	@GetMapping("/index")
	public ModelAndView index(HttpSession session) {
		//Pega o administrador recebido do login
		Egresso egressoLogado = (Egresso) session.getAttribute("usuarioLogado");
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("egresso/index");
		modelAndView.addObject("egresso", egressoLogado);
		modelAndView.addObject("role", "egresso");
		return modelAndView;
	}
	
//	Mapeamento para exibir a foto
	@GetMapping("/{id}/foto")
	@ResponseBody
	public ResponseEntity<byte[]> getFoto(@PathVariable Long id) {
		Egresso egresso = repository.findById(id).orElse(null);
		if (egresso != null && egresso.getFoto() != null) {
			return ResponseEntity.ok()
					.contentType(MediaType.IMAGE_JPEG)
					.body(egresso.getFoto());
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("perfil/{id}")
	public String exibirTelaPerfil(@PathVariable Long id, Model model) {
		Egresso egresso = repository.findById(id).orElse(null);
		if (egresso != null) {
			model.addAttribute("egresso", egresso);
			return "egresso/perfil";
		}
		return "egresso/listagem";
		
	}
	
	
}
