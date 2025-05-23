package br.com.carometro.adm;

public record DadosAtualizacaoAdministrador(
		Long id,
		String nome,
		String cpf,
		String email,
		String senha
		) {

}
