package br.com.api.candidato;

import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import br.com.api.candidato.config.ServletConfiguration;

@Configuration
@SpringBootApplication
@PropertySource("file:./application.yml")
@EnableAutoConfiguration(exclude={ServletConfiguration.class,MybatisAutoConfiguration.class})
public class Application {

	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(Application.class);
	    ConfigurableApplicationContext ctx= app.run(args);
	    Runtime.getRuntime().addShutdownHook(new Thread() {
	        public void run(){
	           if (ctx instanceof ConfigurableApplicationContext) {
	               ((ConfigurableApplicationContext)ctx).close();
	           }
	        }
	     });
	}
}
