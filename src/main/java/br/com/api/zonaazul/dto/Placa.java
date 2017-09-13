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
public class Placa implements Serializable { 

	private static final long serialVersionUID = 8012266416055769671L;
	
	private Long idPlaca;
	private Long idUsuario;
	private String nrPlaca;
	private Date dtCadastroPlaca;
		
}
