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
public class Vaga implements Serializable { 

	private static final long serialVersionUID = -8824245654840232923L;
	
	private Long id;
	private String noZona;
	private String noVaga;
	private Long nrLongitude;
	private Long nrLatitude;
		
}
