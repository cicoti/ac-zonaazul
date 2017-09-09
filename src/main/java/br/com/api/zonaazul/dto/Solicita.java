package br.com.api.zonaazul.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties ( ignoreUnknown = true )
public class Solicita implements Serializable { 

	private static final long serialVersionUID = 6547887710164561219L;

	private Long id;
	private Usuario usuario;
	private Venda venda;
	private Vaga vaga;
	private Placa placa;
	private String inicio;
	private String fim;
	private int blExtensao;
	private int blNegado;
	private String dsMotivo;
	
}
