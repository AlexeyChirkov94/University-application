package ua.com.foxminded.university;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ua.com.foxminded.university")
public class UniversityApplication {

    public static void main(String[] args) {
        try{
            SpringApplication.run(UniversityApplication.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
