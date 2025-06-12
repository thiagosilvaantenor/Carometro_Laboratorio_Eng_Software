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
import br.com.carometro.aluno.Aluno;
import br.com.carometro.aluno.AlunoService;
import br.com.carometro.curso.Curso;
import br.com.carometro.curso.CursoService;
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
	private AlunoService alunoService;
	
	@Autowired
	private CursoService cursoService;
	
	@GetMapping("/formulario")
	public String carregaPaginaFormulario(Long id, Model model) {
		if (id != null) {
			var admin = repository.getReferenceById(id);
			model.addAttribute("admin", admin);
		} else {
			model.addAttribute("admin", new Administrador());

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
		return "redirect:admin"; 
	}
 
	@PutMapping
	@Transactional
	public String atualizar(DadosAtualizacaoAdministrador dados) throws NoSuchAlgorithmException {
		var admin = repository.getReferenceById(dados.id());
		if (dados.senha() != null) {
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
	
	//Mapeamento da pagina de posts para validaçao dos comentarios dos alunos
	@GetMapping("/validarPostagem")
	public ModelAndView paginaExibicaoPosts(HttpSession session) throws Exception {
		//Busca o admin logado
		Administrador adminLogado = (Administrador) session.getAttribute("usuarioLogado");
	    if (adminLogado != null) {
	    	//Busca os alunos da unidFatec do admin para exibir
	    	
	    	//Busca os cursos
	     	List<Curso> cursos = cursoService.getAllCursos();
	    	//Cria a lista de alunos que vai ser populada com os alunos de cada curso da unidade
	    	List<Aluno> alunos = new ArrayList<>();
	    	cursos.forEach(curso -> {
	    		 alunos.addAll( alunoService.
	    				filtrarAlunosPeloCursoESituacaoComentario(curso.getId(), false));
	    	});
	    	//Envia para a model a lista de alunos
	        ModelAndView modelAndView = new ModelAndView();
	        modelAndView.addObject("alunos", alunos);
	        modelAndView.setViewName("admin/validarPostagem");
	        return modelAndView; 
	    } else {
	        throw new Exception("Usuário não está logado.");
	    }
	}
	
	//Caso seja os comentarios estejam de acordo é aprovado e o estado de situacaoComentario muda
	 @PostMapping("/aprovarComentario")
	    public String aprovarComentario(@RequestParam("id") Long id, Model model, HttpSession session) {
	        try {
				alunoService.aprovarComentario(id);
				//Busca o admin logado
	    	    Administrador adminLogado = (Administrador) session.getAttribute("usuarioLogado");
	    	    //Busca os cursos
		     	List<Curso> cursos = cursoService.getAllCursos();
		    	//Cria a lista de alunos que vai ser populada com os alunos de cada curso da unidade
		     	List<Aluno> alunos = new ArrayList<>();
		    	cursos.forEach(curso -> {
		    		 alunos.addAll( alunoService.
		    				 filtrarAlunosPeloCursoESituacaoComentario(curso.getId(), false));
		    	});
		    	model.addAttribute("alunos", alunos);
			} catch (Exception e) {
				e.printStackTrace();
			}
	        return "redirect:/admin/validarPostagem";
	    }
	 
	//Caso pelo menos 1 dos comentarios não esteja de acordo
   @PutMapping("/reprovarComentario")
	    public String reprovarComentario(@RequestParam("id") Long id, 
	    		@RequestParam("tipoComentario") String tipoComentario, Model model, HttpSession session) {
	        try{
	        	alunoService.reprovarComentario(id, tipoComentario);
	        	//Busca o admin logado
	        	Administrador adminLogado = (Administrador) session.getAttribute("usuarioLogado");
	        	//Busca os cursos
		     	List<Curso> cursos = cursoService.getAllCursos();
		    	//Cria a lista de alunos que vai ser populada com os alunos de cada curso da unidade
		    	List<Aluno> alunos = new ArrayList<>();
		    	cursos.forEach(curso ->
		    		 alunos.addAll( alunoService.
		    				 filtrarAlunosPeloCursoESituacaoComentario(curso.getId(), false))
		    		 );		
	 	        model.addAttribute("alunos", alunos);
	        }catch(Exception e) {
	        	e.printStackTrace();
	        }
	        return "redirect:/admin/validarPostagem";
	    }

}
