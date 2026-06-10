package br.edu.utfpr.pb.pw44s.server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {


		SpringApplication.run(ServerApplication.class, args);
	}



	/*@Bean
	public  static BeanFactoryPostProcessor dependsOnPostProcessor() {
		return bf -> {


			String[] flyway = bf.getBeanNamesForType(Flyway.class);
			Stream.of(flyway)
					.map(bf::getBeanDefinition)
					.forEach(it -> it.setDependsOn("databaseStartupValidator"));
			String[] jpa = bf.getBeanNamesForType(EntityManagerFactory.class);
			Stream.of(jpa)
					.map(bf::getBeanDefinition)
					.forEach(it ->  it.setDependsOn("databaseStartupValidator"));
		};
	}

	@Bean
	public DatabaseStartupValidator databaseStartupValidator(DataSource dataSource) {
		var  dsv = new  DatabaseStartupValidator();
		dsv.setDataSource(dataSource);
		dsv.setTimeout(120);
		dsv.setInterval(7);
		// dsv.setValidationQuery(DatabaseDriver.POSTGRESQL.getValidationQuery());
		dsv.afterPropertiesSet();
		return dsv;
	}*/

}
