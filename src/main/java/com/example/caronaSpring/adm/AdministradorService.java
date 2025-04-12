package com.example.caronaSpring.adm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AdministradorService {
	@Autowired
	private AdminstradorRepository repository;
	
	public List<Administrador> getAllAdministrador() {
		return repository.findAll(Sort.by("nome").ascending());
	}

	public Administrador getAdministradorById(Long id) {
		return repository.getReferenceById(id);
	}
}
