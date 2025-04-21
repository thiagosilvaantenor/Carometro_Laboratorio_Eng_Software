package br.com.carona.controller;

import java.util.ArrayList;

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
import org.springframework.web.multipart.MultipartFile;

import br.com.carona.aluno.Aluno;
import br.com.carona.aluno.AlunoRepository;
import br.com.carona.aluno.AlunoService;
import br.com.carona.aluno.DadosAtualizacaoAluno;
import br.com.carona.aluno.DadosCadastroAluno;
import br.com.carona.curso.Curso;
import br.com.carona.curso.CursoService;
import br.com.carona.historico.Historico;
import br.com.carona.historico.HistoricoRepository;
import br.com.carona.links.Links;
import br.com.carona.links.LinksRepository;
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
			aluno.setHistorico(new Historico());
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
			@RequestParam("cursoId") Long cursoId
			//TO FIX: Quando tenta atualizar ou cadastrar da erro por conta da foto ,@RequestParam("foto") MultipartFile foto
			) throws Exception {
		
		Aluno aluno = new Aluno(dados);
		Curso curso = cursoService.getCursoById(cursoId);
		// adiciona o aluno na lista de alunos
		curso.getAlunos().add(aluno);
		aluno.setCurso(curso);
		
		// Historico
		Historico historico = new Historico();
		historico.setEmpresaTrabalho(dados.empresaTrabalho());
		historico.setDescricaoTrabalho(dados.descricaoTrabalho());
		historico.setTempoTrabalho(dados.tempoTrabalho());
		
		historico.setAlunos(new ArrayList<>());
		historico.getAlunos().add(aluno);
		aluno.setHistorico(historico);
		
		//Links
		Links links = new Links();
		links.setGitHub(dados.gitHub());
		links.setLinkedIn(dados.linkedIn());
		links.setLattesCNPQ(dados.lattesCNPQ());
		
		links.setAlunos(new ArrayList<>());
		links.getAlunos().add(aluno);
		aluno.setLinks(links);
		
		/*TO FIX: Foto não está sendo salva no banco de dados
		//Foto
		if (!foto.isEmpty()) {
			try {
				aluno.setFoto(foto.getBytes());
			} catch (Exception e) {
				throw new Exception("Erro ao salvar foto");
			}
		}
		*/
		
		//Salva primeiro o histórico e links no banco de dados
		repositoryHistorico.save(historico);
		repositoryLinks.save(links);
		
		service.salvar(aluno);
		
		return "redirect:aluno";
	}
	
	@PutMapping
	@Transactional
	public String atualizar(DadosAtualizacaoAluno dados) {
		var aluno = repository.getReferenceById(dados.id());
		if (dados.curso() != null) {
			//Remove o curso antigo
			aluno.getCurso().getAlunos().remove(aluno);
			//Adiciona o curso novo
			aluno.setCurso(dados.curso());
			dados.curso().getAlunos().add(aluno);
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
