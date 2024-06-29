package org.MyProject.Jpa;

import org.MyProject.Jpa.Entity.Customers;
import org.MyProject.Jpa.Entity.Role;
import org.MyProject.Jpa.Service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("org.MyProject.Jpa.Repository")
@EntityScan("org.MyProject.Jpa.Entity")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
