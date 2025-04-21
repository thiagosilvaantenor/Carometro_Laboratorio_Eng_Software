package br.com.carona.aluno;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import br.com.carona.curso.Curso;

public record DadosAtualizacaoAluno(Long id, 
		String nome, 
		String matricula, 
		String cpf, 
		String email, 
		String senha, 
		LocalDate dtNascimento,
		String unidFATEC, 
		String comentarioFATEC, 
		String comentario,
		Integer ano, 
		Curso curso,
		String empresaTrabalho,
		String descricaoTrabalho,
		int tempoTrabalho,
		String gitHub,
		String linkedIn,
		String lattesCNPQ,
		MultipartFile foto) {

}
