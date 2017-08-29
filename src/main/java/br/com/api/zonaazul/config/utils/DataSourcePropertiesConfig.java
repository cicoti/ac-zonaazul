package br.com.api.zonaazul.config.utils;

/*
 * Propriedades de configuração do Datasource.
 * Classe usada para armazenar parametros de configuração da conexão com banco de dados.
*/

public class DataSourcePropertiesConfig {

	private String jndiName;
	private String url;
	private String username;
	private String password;
	private String driverClassName;
	
	
	public String getJndiName() {
		return jndiName;
	}
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
}
