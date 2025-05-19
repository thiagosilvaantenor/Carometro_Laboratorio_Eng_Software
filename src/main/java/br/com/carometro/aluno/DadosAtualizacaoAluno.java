package br.com.carometro.aluno;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import br.com.carometro.curso.Curso;
import br.com.carometro.unidfatec.UnidFatec;

public record DadosAtualizacaoAluno(Long id, 
		String nome, 
		String email, 
		String senha, 
		LocalDate dtNascimento,
		UnidFatec unidFATEC, 
		String comentarioFATEC, 
		String comentario,
		Integer ano, 
		Curso curso,
		String gitHub,
		String linkedIn,
		String lattesCNPQ,
		MultipartFile foto) {

}
