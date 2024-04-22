package org.example;

import org.example.console.ConsoleGame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.util.Properties;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws Exception {
        String propertiesPath = "env.properties";
        Properties properties = new Properties();
        properties.load(new FileInputStream(propertiesPath));
        String method = (String) properties.get("METHOD");
        if (method.equals("CLI")) {
            ConsoleGame.start();
        } else if (method.equals("BACKEND")) {
            SpringApplication.run(Main.class, args);
        }
    }
}