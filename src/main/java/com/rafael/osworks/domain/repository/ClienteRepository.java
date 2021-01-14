package com.rafael.osworks.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rafael.osworks.domain.model.Cliente;

@Repository //Define que é um componente do Spring (Gerenciado pelo próprio framework) - Spring Data JPA
public interface ClienteRepository extends JpaRepository<Cliente, Long>{

	List<Cliente> findByNome(String nome);
	List<Cliente> findByNomeContaining(String nome);
	
	Cliente findByEmail(String email);
	
}
