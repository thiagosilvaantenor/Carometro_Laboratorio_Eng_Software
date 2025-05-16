package br.com.carometro.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
	
	@GetMapping("/formulario")
	public String carregaPaginaFormulario(Long id, Model model) {
		model.addAttribute("cursos", cursoService.getAllCursos());
		
		if (id != null) {
			var aluno = repository.getReferenceById(id);
			model.addAttribute("aluno", aluno);
			
		} else {
			Aluno aluno = new Aluno();
			aluno.setHistorico(new HashSet<>());
			aluno.setLinks(new Links());
			model.addAttribute("aluno", aluno);
		}
		
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
			List<DadosCadastroHistorico> dadosHistorico,
			@RequestParam("cursoId") Long cursoId
			) throws Exception {
		
		Aluno aluno = new Aluno(dados);
		Curso curso = cursoService.getCursoById(cursoId);
		// adiciona o aluno na lista de alunos
		curso.getAlunos().add(aluno);
		aluno.setCurso(curso);
		
		// Historico
		Set<Historico> historico = new HashSet<>();
		dadosHistorico.forEach(item -> {
			Historico novo = new Historico();
			novo.setEmpresaTrabalho(item.empresaTrabalho());
			novo.setDescricaoTrabalho(item.descricaoTrabalho());
			novo.setTempoTrabalho(item.tempoTrabalho());
			novo.setAluno(aluno);
			historico.add(novo);
			//Salva primeiro o histórico no banco de dados
			repositoryHistorico.save(novo);
		});
		aluno.setHistorico(historico);
		
		//Links
		Links links = new Links();
		links.setGitHub(dados.gitHub());
		links.setLinkedIn(dados.linkedIn());
		links.setLattesCNPQ(dados.lattesCNPQ());
		
		links.setAlunos(new ArrayList<>());
		links.getAlunos().add(aluno);
		aluno.setLinks(links);
		
		 // convertendo a foto de MultipartFile para byte[]
	    if (dados.foto() != null && !dados.foto().isEmpty()) {
	        aluno.setFoto(dados.foto().getBytes());
	    }
				
		//Salva primeiro os links no banco de dados
		repositoryLinks.save(links);
		service.salvar(aluno);
		
		return "redirect:aluno";
	}
	
	@PutMapping
	@Transactional
	public String atualizar(DadosAtualizacaoAluno dados, DadosCadastroHistorico dadosHistorico) throws IOException, NoSuchAlgorithmException {
		var aluno = repository.getReferenceById(dados.id());
		//Atualiza curso
		if (dados.curso() != null) {
			//Remove o curso antigo
			aluno.getCurso().getAlunos().remove(aluno);
			//Adiciona o curso novo
			aluno.setCurso(dados.curso());
			dados.curso().getAlunos().add(aluno);
		}
		
		//TODO: atualização do histórico
		
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
