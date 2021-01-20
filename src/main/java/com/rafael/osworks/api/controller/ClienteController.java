package com.rafael.osworks.api.controller;

import java.util.List;
import java.util.Optional;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.osworks.domain.model.Cliente;
import com.rafael.osworks.domain.repository.ClienteRepository;
import com.rafael.osworks.domain.service.ClienteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

//Receber requisições externas e responder

@RestController
@RequestMapping("/clientes")
@Api(value = "API Rest Clientes")
@CrossOrigin(origins = "*")
public class ClienteController {
	
	@Autowired // Dizendo que quero uma instância da Interface nesse ponto.
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@GetMapping
	@ApiOperation(value = "Retorna todos os Clientes")
	public List<Cliente> listar() {
		return clienteRepository.findAll();
		// Buscar por nome p exemplo = Criar metódos na Interface com pesquisas específicas
		// return clienteRepository.findByNome("Rafael Araujo");
	}

	@GetMapping("/{clienteId}")
	@ApiOperation(value = "Retorna 1 cliente especifico")
	public ResponseEntity<Cliente> buscar(@PathVariable Long clienteId) {
		Optional<Cliente> cliente = clienteRepository.findById(clienteId);
		
		// Validação de código http
		if (cliente.isPresent()) {
			return ResponseEntity.ok(cliente.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	/*Quando recebe a requisição do corpo ele transforma em um Cliente*/
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Salva Cliente")
	public Cliente adicionar(@Valid @RequestBody Cliente cliente) {
		return clienteService.salvar(cliente);
	}
	
	@PutMapping("/{clienteId}")
	@ApiOperation(value = "Edita Cliente")
	public ResponseEntity<Cliente> atualizar (@Valid @PathVariable Long clienteId, 
			@RequestBody Cliente cliente){
		
		if(!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}
		
		cliente.setId(clienteId);
		cliente = clienteService.salvar(cliente);
		
		return ResponseEntity.ok(cliente);
	}
	
	@DeleteMapping("/{clienteId}")
	@ApiOperation(value = "Deleta Cliente")
	public ResponseEntity<Void> excluir (@PathVariable Long clienteId){
		if(!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}
		
		clienteService.excluir(clienteId);
		
		return ResponseEntity.noContent().build();	
	}
}
