package com.rafael.osworks.api.exceptionhandler;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.rafael.osworks.domain.exception.EntidadeNaoEncontradaException;
import com.rafael.osworks.domain.exception.NegocioException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	
	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<Object> handleEntidadeNaoEncontrada(NegocioException ex, WebRequest request) {
		var status = HttpStatus.NOT_FOUND;
		var error = new Errors();
		error.setStatus(status.value());
		error.setTitulo(ex.getMessage());
		error.setDataHora(OffsetDateTime.now());
		
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
	}
		
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<Object> handleNegocio(NegocioException ex, WebRequest request) {
		var status = HttpStatus.BAD_REQUEST;
		var error = new Errors();
		error.setStatus(status.value());
		error.setTitulo(ex.getMessage());
		error.setDataHora(OffsetDateTime.now());
		
		return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
	}
	
	//Tratamento de exception de todos os controladores
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		var errors = new Errors();
		var campos = new ArrayList<Errors.Campo>();
		
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			String nome = ((FieldError) error).getField();
			String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
			
			campos.add(new Errors.Campo(nome, mensagem));
		}
		
		errors.setStatus(status.value());
		errors.setTitulo("Um ou mais campos inválidos, faça o preenchimento correto e tente novamente");
		errors.setDataHora(OffsetDateTime.now());
		errors.setCampos(campos);
		
		return super.handleExceptionInternal(ex, errors, headers, status, request);
	}
	
}
