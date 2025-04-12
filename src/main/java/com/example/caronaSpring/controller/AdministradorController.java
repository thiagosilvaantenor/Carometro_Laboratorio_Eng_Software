package com.example.caronaSpring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.caronaSpring.adm.AdminstradorRepository;
import com.example.caronaSpring.coordenador.Coordenador;
import com.example.caronaSpring.coordenador.DadosAtualizacaoCoordenador;
import com.example.caronaSpring.coordenador.DadosCadastroCoordenador;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdministradorController {
	@Autowired
	private AdminstradorRepository repository;
	
	@GetMapping("/formulario")
	public String carregaPaginaFormulario(Long id, Model model) {
		System.out.println("id" + id);
		if (id != null) {
			var admin = repository.getReferenceById(id);
			model.addAttribute("admin", admin);
		}
		return "admin/formulario";
	}
	
	@GetMapping
	public String carregaPaginaListagem(Model model) {
		model.addAttribute("lista", repository.findAll(Sort.by("nome").ascending()));
		return "admin/listagem";
	}
//	
//	@PostMapping
//	@Transactional
//	public String cadastrar(@Valid
//			DadosCadastroCoordenador dados) {
//			repository.save(new Coordenador(dados));
//		return "redirect:coordenador";
//	}
//	
//	@PutMapping
//	@Transactional
//	public String atualizar(DadosAtualizacaoCoordenador dados) {
//		var coordenador = repository.getReferenceById(dados.matricula());
//		coordenador.atualizarInformacoes(dados);
//		return "redirect:coordenador";
//	}
//
//	@DeleteMapping
//	@Transactional
//	public String removeCurso(String matricula) {
//		repository.deleteById(matricula);
//		return "redirect:coordenador";
//	}
}
