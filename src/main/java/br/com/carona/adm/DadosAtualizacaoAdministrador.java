package br.com.carona.adm;

public record DadosAtualizacaoAdministrador(
		Long id,
		String nome,
		String cpf,
		String unidFatec,
		String email,
		String senha
		) {

}
