package com.frontline.frontline_tech;

import com.frontline.frontline_tech.model.Membro;
import com.frontline.frontline_tech.repository.MembroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FrontlineTechApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontlineTechApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDatabase(MembroRepository repository) {
		return args -> {
			System.out.println(">>> Verificando banco de dados...");
			if (repository.count() == 0) {
				Membro admin = new Membro();
				admin.setNome("Admin");
				admin.setSenha("123");
				admin.setCargo("LIDER");
				admin.setFuncao("Diretor");
				admin.setWhatsapp("24999999999");
				repository.save(admin);
				System.out.println(">>> USUÁRIO MESTRE CRIADO AGORA NO MYSQL!");
			} else {
				System.out.println(">>> O banco já tem " + repository.count() + " membros.");
			}
		};
	}
}