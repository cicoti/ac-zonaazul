package br.com.api.zonaazul.dto;

import java.io.Serializable;

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
public class Usuario implements Serializable { 

	private static final long serialVersionUID = -3164863848203812524L;
	
	private Long idUsuario;
	private String email;
	private String senha;
	private String tipo;
	
}
