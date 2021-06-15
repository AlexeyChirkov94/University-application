package ua.com.foxminded.university;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.ApplicationContext;
import ua.com.foxminded.university.dto.ProfessorRequest;
import ua.com.foxminded.university.service.impl.LessonServiceImpl;
import ua.com.foxminded.university.service.impl.ProfessorServiceImpl;
import ua.com.foxminded.university.service.interfaces.LessonService;
import ua.com.foxminded.university.service.interfaces.ProfessorService;

public class Application {

    public static void main(String[] args){

        ApplicationContext context = new AnnotationConfigApplicationContext(ContextConfiguration.class);

        FrontController frontController = context.getBean(FrontControllerImpl.class);
        frontController.startMenu();
    }

}
