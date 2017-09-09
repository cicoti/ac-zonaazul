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
public class Venda implements Serializable { 

	private static final long serialVersionUID = 6547887710164561219L;

	private Long id;
	private Credito credito;
	private Usuario usuario;
	private Long qtCredito;
	private Date data;

}
