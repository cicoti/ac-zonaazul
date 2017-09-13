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
public class Credito implements Serializable { 

	private static final long serialVersionUID = -3904771609468818161L;
	
	private Long idCredito;
	private Double vlCredito;
	private Date dtCriacaoCredito;
		
}
