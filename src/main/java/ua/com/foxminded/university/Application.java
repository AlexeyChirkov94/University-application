package ua.com.foxminded.university;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.ApplicationContext;

public class Application {

    public static void main(String[] args){

        ApplicationContext context = new AnnotationConfigApplicationContext(ContextConfiguration.class);

        FrontController frontController = context.getBean(FrontControllerImpl.class);
        frontController.startMenu();
    }

}
