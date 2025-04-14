package com.example.caronaSpring.adm;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroAdministrador(
		@NotBlank
		String nome,
		String cpf,
		String unidFatec,
		String email,
		String senha
		) {

}
