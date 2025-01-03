package com.wait.app;

import cn.hutool.extra.spring.SpringUtil;
import com.tangzc.autotable.springboot.EnableAutoTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author 天
 */
@EnableAutoTable
@SpringBootApplication
@Slf4j
public class PetApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetApiApplication.class, args);
		log.info("启动成功,接口文档路径: http://localhost:{}/doc.html#/home", SpringUtil.getProperty("server.port"));
	}

}
