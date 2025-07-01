package br.com.carometro.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.carometro.aluno.Aluno;
import br.com.carometro.aluno.AlunoDadosAtualizacao;
import br.com.carometro.aluno.AlunoDadosCadastro;
import br.com.carometro.aluno.AlunoService;
import br.com.carometro.coordenador.Coordenador;
import br.com.carometro.curso.Curso;
import br.com.carometro.curso.CursoService;
import br.com.carometro.security.Criptografia;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/aluno")
public class AlunoController {
	@Autowired
	private AlunoService service;
	@Autowired
	private CursoService cursoService;
	
	
	@GetMapping("/formulario")
	public String carregaPaginaFormulario(Long id, Model model) {
		model.addAttribute("cursos", cursoService.getAllCursos());
		if(id != null && service.getById(id).isPresent()) {
	        var aluno = service.getById(id).get();
	        model.addAttribute("aluno", aluno);
	        model.addAttribute("editar", true);
	    } else {
	    	model.addAttribute("aluno", new Aluno());
	    	model.addAttribute("editar", false);
	    }
		return "aluno/formulario";

	}
	
	@GetMapping
	public String carregaPaginaListagem(Model model) {
		//Envia a lista de cursos da seleção de filtro
		List<Curso> cursos = cursoService.getAllCursos();
		//Envia todos os alunos
		model.addAttribute("cursos", cursos);
		model.addAttribute("lista", service.getAll());
		
		return "aluno/listagem";
	} 
	
	
	@PostMapping
	@Transactional
	public String cadastrar(@Valid AlunoDadosCadastro dados, Model model, 
			@RequestParam("cursoId") Long cursoId) {
			//busca o curso
			Curso curso = cursoService.getCursoById(cursoId);
			Aluno aluno = new Aluno(dados);
			aluno.setCurso(curso);
			service.salvar(aluno);
			return "redirect:login";		
	}
	
	@PutMapping
	@Transactional
	public String atualizar(@Valid AlunoDadosAtualizacao dados, Model model, 
			@RequestParam("cursoId") Long cursoId) throws NoSuchAlgorithmException {
		var aluno = service.getById(dados.id());
		if (aluno.isPresent()) {
			Aluno encontrado = aluno.get();
			//Atualizará a senha apenas se tiver conteudo no campo
			if (dados.senha() != null && !dados.senha().isBlank()) {
				encontrado.setSenha(Criptografia.md5(dados.senha()));
			}
			if (cursoId != null) {
				Curso curso = cursoService.getCursoById(cursoId);
				encontrado.setCurso(curso);
				
			}
			encontrado.atualizarInformacoes(dados);			
		}
		return "redirect:aluno";		
	}
	
	
	@DeleteMapping
	@Transactional
	public String removeAluno(Long id) {
		Optional<Aluno> aluno = service.getById(id);
		// Desfaz o vinculo entre curso e coordenador
		if (aluno.isPresent()) {
			service.remover(id);
		}
		return "redirect:aluno";
	}
	
	//Método para filtrar a listagem
	@GetMapping("/filtrar")
	public String filtrar(@RequestParam(name = "nome", required = false) String nome, 
				@RequestParam(name = "cursoId", required = false)  Long cursoId, Model model) {
		List<Aluno> alunos = null;
		//Envia a lista de cursos da seleção de filtro
		List<Curso> cursos = cursoService.getAllCursos();
		model.addAttribute("cursos", cursos);
		if (!nome.isBlank() && cursoId != null)
			alunos = service.filtarPorNomeECurso(nome, cursoId);
		else if (!nome.isBlank() && cursoId == null)
			alunos = service.filtarPorNome(nome);
		else if (cursoId != null && nome.isBlank())
			alunos = service.filtarPorCurso(cursoId);
		else
			alunos = service.getAll();
		
		model.addAttribute("lista", alunos);
		return "aluno/listagem";
			
	}
	
	
	//Mapeamento da pagina inicio do administrador, para chegar lá é necessario ele se logar pelo LoginController
	@GetMapping("/index")
	public ModelAndView index(HttpSession session) {
		//Pega o aluno recebido do login
		Aluno alunoLogado = (Aluno) session.getAttribute("usuarioLogado");
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("aluno/index");
		modelAndView.addObject("aluno", alunoLogado);
		modelAndView.addObject("role", "aluno");
		return modelAndView;
	}
	
	//Mapeamento da pagina de envio de arquivo para cadastrar aluno(futuro egresso)
	@GetMapping("/cadastroPorArquivo")
	public String paginaReceberArquivosAlunos(Model model){
		return "aluno/cadastroPorArquivo";
	}
	
	//Mapeamento da pagina de envio de arquivo para cadastrar aluno(futuro egresso)
	@PostMapping("/resultadoCadastro")
	public String paginaReceberArquivosAlunos(@RequestParam("file") MultipartFile file, Model model){
		//Percurso de excessão, arquivo vazio
		if (file.isEmpty()) {
			model.addAttribute("mensagem", "Por favor selecione um arquivo para upload.");
			return "aluno/cadastroPorArquivo";
		}
		
		//Percurso correto
		try {
			List<Aluno> alunosGerados = service.processarArquivoCsv(file);
			model.addAttribute("mensagem", "Arquivo processado com sucesso! " + alunosGerados.size() + " alunos adicionados/atualizados.");
			model.addAttribute("alunos", alunosGerados);//Envia a lista de alunos para a view
			return "aluno/resultadoCadastro";
		} 
		//Percursos de excessão
		catch (IOException e) {
			model.addAttribute("mensagem", "Erro ao processar o arquivo: " + e.getMessage());
            return "aluno/cadastroPorArquivo";
        } catch (Exception e) {
        	model.addAttribute("mensagem", "Ocorreu um erro inesperado: " + e.getMessage());
            return "aluno/cadastroPorArquivo";
        }
		
	}
	
	
	
}
