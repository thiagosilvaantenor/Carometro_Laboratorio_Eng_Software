package br.com.carometro.controller;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.carometro.adm.Administrador;
import br.com.carometro.adm.AdministradorService;
import br.com.carometro.adm.AdministradorRepository;
import br.com.carometro.adm.DadosAtualizacaoAdministrador;
import br.com.carometro.adm.DadosCadastroAdministrador;
import br.com.carometro.aluno.Aluno;
import br.com.carometro.aluno.AlunoService;
import br.com.carometro.coordenador.Coordenador;
import br.com.carometro.curso.Curso;
import br.com.carometro.curso.CursoService;
import br.com.carometro.security.Criptografia;
import br.com.carometro.unidfatec.UnidFatec;
import br.com.carometro.unidfatec.UnidFatecService;
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
	private UnidFatecService unidFatecService;
	
	@Autowired
	private AlunoService alunoService;
	
	@Autowired
	private CursoService cursoService;
	
	@GetMapping("/formulario")
	public String carregaPaginaFormulario(Long id, Model model) {
		model.addAttribute("unidades", unidFatecService.buscaTodas());
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
	public String cadastrar(@Valid DadosCadastroAdministrador dados, @RequestParam("nomeFatec") String nomeFatec) throws Exception {
		if (dados == null || dados.email().isBlank() || dados.nome().isBlank() || dados.senha().isBlank()) {
			throw new Exception("Dados invalidos, cadastro não realizado");
		}
		UnidFatec unidade = unidFatecService.buscarPorNome(nomeFatec);
		//Caso o nome da unidade não foi encontrado então cria essa unidadeFatec
		if (unidade == null) {
			    unidade = new UnidFatec();
			    unidade.setNome(nomeFatec);
			    unidFatecService.salvar(unidade);
		}
		Administrador admin = new Administrador(dados);
		admin.setUnidFatec(unidade);
		service.salvarAdmin(admin);
		return "redirect:admin"; 
	}
 
	@PutMapping
	@Transactional
	public String atualizar(DadosAtualizacaoAdministrador dados) throws NoSuchAlgorithmException {
		var admin = repository.getReferenceById(dados.id());
		if (dados.senha() != null) {
			admin.setSenha(Criptografia.md5(admin.getSenha()));
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
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/index");
		modelAndView.addObject("admin", adminLogado);
		return modelAndView;
	}
	
	//Mapeamento da pagina de posts para validaçao dos alunos 
	@GetMapping("/validarPostagem")
	public ModelAndView paginaExibicaoPosts(HttpSession session) throws Exception {
		//Busca o admin logado
		Administrador adminLogado = (Administrador) session.getAttribute("usuarioLogado");
	    if (adminLogado != null) {
	    	//Busca os alunos da unidFatec do admin para exibir
	    	
	    	//Busca os cursos da unidFatec do admin
	    	List<Curso> cursos = cursoService.findByUnidFatecId(adminLogado.getUnidFatec().getId());
	    	//Cria a lista de alunos que vai ser populada com os alunos de cada curso da unidade
	    	List<Aluno> alunos = new ArrayList<>();
	    	cursos.forEach(curso -> {
	    		 alunos.addAll( alunoService.
	    				filtraAlunosPeloCurso(curso.getId()) );
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
	
	

//    @PostMapping("/logout")
//    public ModelAndView logout(HttpSession session) {
//        session.invalidate();
//        return login();
//    }
}
