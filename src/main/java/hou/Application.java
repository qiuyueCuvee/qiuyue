package hou;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
@MapperScan(basePackages="hou.mapper")
public class Application {
	public static void main(String [] args) {
		SpringApplication.run(Application.class, args);
	}

}
