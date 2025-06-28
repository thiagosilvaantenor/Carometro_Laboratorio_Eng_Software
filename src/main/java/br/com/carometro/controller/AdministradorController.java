package br.com.carometro.controller;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.carometro.adm.Administrador;
import br.com.carometro.adm.AdministradorRepository;
import br.com.carometro.adm.AdministradorService;
import br.com.carometro.adm.DadosAtualizacaoAdministrador;
import br.com.carometro.adm.DadosCadastroAdministrador;
import br.com.carometro.coordenador.Coordenador;
import br.com.carometro.curso.Curso;
import br.com.carometro.curso.CursoService;
import br.com.carometro.egresso.Egresso;
import br.com.carometro.egresso.EgressoService;
import br.com.carometro.security.Criptografia;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdministradorController {

	@Autowired
	private AdministradorRepository repository;
	
	@Autowired
	private AdministradorService service;

	
	@Autowired
	private EgressoService egressoService;
	
	@Autowired
	private CursoService cursoService;
	
	@GetMapping("/formulario")
	public String carregaPaginaFormulario(Long id, Model model) {
		if (id != null) {
			var admin = repository.getReferenceById(id);
			model.addAttribute("admin", admin);
			model.addAttribute("editar", true);
		} else {
			model.addAttribute("admin", new Administrador());
			model.addAttribute("editar", false);
		}
		return "admin/formulario";
	}
  
	@GetMapping
	public String carregaPaginaListagem(Model model) {
		model.addAttribute("lista", repository.findAll(Sort.by("nome").ascending()));
		return "admin/listagem"; 
	}

	@PostMapping
	@Transactional
	public String cadastrar(@Valid DadosCadastroAdministrador dados) throws Exception {
		if (dados == null || dados.email().isBlank() || dados.nome().isBlank() || dados.senha().isBlank()) {
			throw new Exception("Dados invalidos, cadastro não realizado");
		}
		Administrador admin = new Administrador(dados);
		service.salvarAdmin(admin);
		return "redirect:login"; 
	}
 
	@PutMapping
	@Transactional
	public String atualizar(DadosAtualizacaoAdministrador dados) throws NoSuchAlgorithmException {
		var admin = repository.getReferenceById(dados.id());
		
		//Atualizará a senha apenas se tiver conteudo no campo
		if (dados.senha() != null && !dados.senha().isBlank()) {
			admin.setSenha(Criptografia.md5(dados.senha()));
		}
		admin.atualizarInformacoes(dados);
		return "redirect:admin";
	}

	@DeleteMapping
	@Transactional
	public String removeAdmin(Long id) throws Exception {
		service.remover(id);
		return "redirect:admin";
	}
	
	
	//Mapeamento da pagina inicio do administrador, para chegar lá é necessario ele se logar pelo LoginController
	@GetMapping("/index")
	public ModelAndView index(HttpSession session) {
		//Pega o administrador recebido do login
		Administrador adminLogado = (Administrador) session.getAttribute("usuarioLogado");
		session.setAttribute("usuarioLogado", adminLogado);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/index");
		modelAndView.addObject("admin", adminLogado);
		modelAndView.addObject("role", "admin");
		return modelAndView;
	}
	
	//Mapeamento da pagina de posts para validaçao dos comentarios dos egressos
	@GetMapping("/validarPostagem")
	public ModelAndView paginaExibicaoPosts(HttpSession session) throws Exception {
		//Busca o admin logado
		Administrador adminLogado = (Administrador) session.getAttribute("usuarioLogado");
	    if (adminLogado != null) {
	    	//Busca os egressos da unidFatec do admin para exibir
	    	
	    	//Busca os cursos
	     	List<Curso> cursos = cursoService.getAllCursos();
	    	//Cria a lista de egressos que vai ser populada com os egressos que ainda não foram validados
	    	List<Egresso> egressos = new ArrayList<>();
	    	egressos = egressoService.filtraEgressoAindaNaoAprovadosEmAlgumaSituacao();
	    	//Envia para a model a lista de egressos
	        ModelAndView modelAndView = new ModelAndView();
	        modelAndView.addObject("egressos", egressos);
	        modelAndView.addObject("cursos", cursoService.getAllCursos());
	        modelAndView.setViewName("admin/validarPostagem");
	        return modelAndView; 
	    } else {
	        throw new Exception("Usuário não está logado.");
	    }
	}
	
	//Caso seja os comentarios estejam de acordo é aprovado e o estado de situacaoComentario muda
	 @PostMapping("/aprovarComentario")
	    public String aprovarComentariosOuFoto(@RequestParam("id") Long id, @RequestParam("tipo") String tipo, 
	    		Model model, HttpSession session) {
	        try {
	        	switch(tipo) {
	        		//Caso seja aprovação de comentario, verifical qual ou quais
	        		case "fatec","geral","comentarios":
	        			egressoService.aprovarComentario(id);
	        		break;
	        		//caso seja reprovação de foto
	        		case "foto":
	        			egressoService.aprovarFoto(id);
	        		break;
	        	}
	    	    //Busca os cursos
		     	List<Curso> cursos = cursoService.getAllCursos();
		    	//Cria a lista de egressos que vai ser populada com os egressos de cada curso da unidade
		     	List<Egresso> egressos = new ArrayList<>();
		    	cursos.forEach(curso -> {
		    		 egressos.addAll( egressoService.
		    				 filtrarEgressoPeloCursoESituacaoComentario(curso.getId(), false));
		    	});
		    	model.addAttribute("egressos", egressos);
		    	model.addAttribute("cursos", cursos);
			} catch (Exception e) {
				e.printStackTrace();
			}
	        return "redirect:/admin/validarPostagem";
	    }
	 
	//Reprovação dos comentarios ou foto
   @PutMapping("/reprovarComentario")
	    public String reprovarComentarioOuFoto(@RequestParam("id") Long id, 
	    		@RequestParam("tipo") String tipo, Model model, HttpSession session) {
	        try{
	        	switch(tipo) {
	        		//Caso seja reprovação de comentario, verifical qual ou quais
	        		case "fatec","geral","comentarios":
	        			egressoService.reprovarComentario(id, tipo);
	        		break;
	        		//caso seja reprovação de foto
	        		case "foto":
	        			egressoService.reprovarFoto(id);
	        		break;
	        	}
	        	//Busca os cursos
		     	List<Curso> cursos = cursoService.getAllCursos();
		    	//Cria a lista de egressos que vai ser populada com os egressos de cada curso da unidade
		    	List<Egresso> egressos = new ArrayList<>();
		    	cursos.forEach(curso ->
		    		 egressos.addAll( egressoService.
		    				 filtrarEgressoPeloCursoESituacaoComentario(curso.getId(), false))
		    		 );		
	 	        model.addAttribute("egressos", egressos);
	 	        model.addAttribute("cursos", cursos);
	        }catch(Exception e) {
	        	e.printStackTrace();
	        }
	        return "redirect:/admin/validarPostagem";
	    }
   
	//Caso seja ex-aluno é aprovado, estado de situaçãoCadastro é mudado para TRUE
	 @PostMapping("/aprovarEgresso")
	    public String aprovarEgresso(@RequestParam("id") Long id,  Model model, HttpSession session) {
		 
	        try {
				egressoService.aprovarEgresso(id);
				//Busca os egressos ainda não avaliados
	    	    List<Egresso> egressos = egressoService.filtraEgressoPelaSituacaoCadastro(false);
	    	    model.addAttribute("egressos", egressos);
	    	    model.addAttribute("cursos", cursoService.getAllCursos());
			} catch (Exception e) {
				e.printStackTrace();
			}
	        return "redirect:/admin/validarPostagem";
	    }
	 
	//Caso não seja ex-aluno é reprovado e deletado do banco de dados 
  @DeleteMapping("/reprovarEgresso")
	    public String reprovarEgresso(@RequestParam("id") Long id, Model model, HttpSession session) {
	        try{
	        	egressoService.reprovarEgresso(id);
	        	//Busca os egressos ainda não avaliados
	    	    List<Egresso> egressos = egressoService.filtraEgressoPelaSituacaoCadastro(false);
	    	    model.addAttribute("egressos", egressos);
	    	    model.addAttribute("cursos", cursoService.getAllCursos());
	        }catch(Exception e) {
	        	e.printStackTrace();
	        }
	        return "redirect:/admin/validarPostagem";
	    }

  
  @GetMapping("/filtrar")
	public String filtrarPaginaListagem(@RequestParam(name ="cursoId", required=false) Long cursoId,
			@RequestParam(name= "ano", required= false) Integer ano,
			Model model) {
		//Lista de egressos filtrados que tiveram cadastro aprovado pelo coordenador
		List<Egresso> egressosFiltrados = egressoService.filtrarEgresso(ano, cursoId, false);
		//Lista de todos os egresso para pegar os anos semestres
		List<Egresso> egresso = egressoService.buscaEgressoPelaSituacaoCadastro(false);
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
  
  
}
