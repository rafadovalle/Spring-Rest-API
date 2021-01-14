package com.rafael.osworks.domain.service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rafael.osworks.domain.exception.NegocioException;
import com.rafael.osworks.domain.model.Cliente;
import com.rafael.osworks.domain.model.OrdemServico;
import com.rafael.osworks.domain.model.StatusOrdemServico;
import com.rafael.osworks.domain.repository.ClienteRepository;
import com.rafael.osworks.domain.repository.OrdemServicoRepository;

@Service
public class GestaoOrdemServicoService {
	
	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	public OrdemServico criar(OrdemServico ordemServico) {
		
		//Buscando cliente no repositório/db 
		Cliente cliente = clienteRepository.findById(ordemServico.getCliente().getId()).orElseThrow(() 
				-> new NegocioException("Cliente não encontrado"));
		
		
		ordemServico.setCliente(cliente);
		
		ordemServico.setStatus(StatusOrdemServico.ABERTA);
		ordemServico.setDataAbertura(OffsetDateTime.now());
		
		return ordemServicoRepository.save(ordemServico);
	}
	
	
}
