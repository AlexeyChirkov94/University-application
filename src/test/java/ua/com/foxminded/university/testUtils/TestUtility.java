package ua.com.foxminded.university.testUtils;

import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.entity.FormOfEducation;
import ua.com.foxminded.university.entity.FormOfLesson;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Privilege;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.entity.Role;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.entity.User;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class TestUtility {

    public static void assertCourses(Course actual, Course expected) {
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getDepartment().getId()).isEqualTo(expected.getDepartment().getId());
        assertThat(actual.getDepartment().getName()).isEqualTo(expected.getDepartment().getName());
    }

    public static void assertCourses(List<Course> actual, List<Course> expected) {
        assertThat(actual.size()).isEqualTo(expected.size());
        for(int i = 0; i < actual.size(); i++){
            assertCourses(actual.get(i), expected.get(i));
        }
    }

    public static void assertDepartments(Department actual, Department expected) {
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

    public static void assertDepartments(List<Department> actual, List<Department> expected) {
        assertThat(actual.size()).isEqualTo(expected.size());
        for(int i = 0; i < actual.size(); i++){
            assertDepartments(actual.get(i), expected.get(i));
        }
    }

    public static void assertRoles(Role actual, Role expected) {
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

    public static void assertRoles(List<Role> actual, List<Role> expected) {
        assertThat(actual.size()).isEqualTo(expected.size());
        for(int i = 0; i < actual.size(); i++){
            assertRoles(actual.get(i), expected.get(i));
        }
    }

    public static void assertPrivileges(Privilege actual, Privilege expected) {
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

    public static void assertPrivileges(List<Privilege> actual, List<Privilege> expected) {
        assertThat(actual.size()).isEqualTo(expected.size());
        for(int i = 0; i < actual.size(); i++){
            assertPrivileges(actual.get(i), expected.get(i));
        }
    }

    public static void assertFormsOfEducation(FormOfEducation actual, FormOfEducation expected) {
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

    public static void assertFormsOfEducation(List<FormOfEducation> actual, List<FormOfEducation> expected) {
        assertThat(actual.size()).isEqualTo(expected.size());
        for(int i = 0; i < actual.size(); i++){
            assertFormsOfEducation(actual.get(i), expected.get(i));
        }
    }

    public static void assertFormsOfLesson(FormOfLesson actual, FormOfLesson expected) {
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getDuration()).isEqualTo(expected.getDuration());
    }

    public static void assertFormsOfLesson(List<FormOfLesson> actual, List<FormOfLesson> expected) {
        assertThat(actual.size()).isEqualTo(expected.size());
        for(int i = 0; i < actual.size(); i++){
            assertFormsOfLesson(actual.get(i), expected.get(i));
        }
    }

    public static void assertGroups(Group actual, Group expected) {
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getDepartment().getId()).isEqualTo(expected.getDepartment().getId());
        assertThat(actual.getDepartment().getName()).isEqualTo(expected.getDepartment().getName());
        assertThat(actual.getFormOfEducation().getId()).isEqualTo(expected.getFormOfEducation().getId());
        assertThat(actual.getFormOfEducation().getName()).isEqualTo(expected.getFormOfEducation().getName());
    }

    public static void assertGroups(List<Group> actual, List<Group> expected) {
        assertThat(actual.size()).isEqualTo(expected.size());
        for(int i = 0; i < actual.size(); i++){
            assertGroups(actual.get(i), expected.get(i));
        }
    }

    public static void assertLessons(Lesson actual, Lesson expected) {
        assertThat(actual.getCourse().getId()).isEqualTo(expected.getCourse().getId());
        assertThat(actual.getCourse().getName()).isEqualTo(expected.getCourse().getName());
        assertThat(actual.getTimeOfStartLesson()).isEqualTo(expected.getTimeOfStartLesson());
        assertThat(actual.getGroup().getId()).isEqualTo(expected.getGroup().getId());
        assertThat(actual.getGroup().getName()).isEqualTo(expected.getGroup().getName());
        assertThat(actual.getTeacher().getId()).isEqualTo(expected.getTeacher().getId());
        assertThat(actual.getTeacher().getFirstName()).isEqualTo(expected.getTeacher().getFirstName());
        assertThat(actual.getTeacher().getLastName()).isEqualTo(expected.getTeacher().getLastName());
        assertThat(actual.getFormOfLesson().getId()).isEqualTo(expected.getFormOfLesson().getId());
        assertThat(actual.getFormOfLesson().getName()).isEqualTo(expected.getFormOfLesson().getName());
    }

    public static void assertLessons(List<Lesson> actual, List<Lesson> expected) {
        assertThat(actual.size()).isEqualTo(expected.size());
        for(int i = 0; i < actual.size(); i++){
            assertLessons(actual.get(i), expected.get(i));
        }
    }


    public static void assertUsers(Professor actual, Professor expected) {
        assertGenericUsers (actual, expected);
        assertThat(actual.getDepartment().getId()).isEqualTo(expected.getDepartment().getId());
        assertThat(actual.getDepartment().getName()).isEqualTo(expected.getDepartment().getName());
        assertThat(actual.getScienceDegree()).isEqualTo(expected.getScienceDegree());
    }

    public static void assertUsersProfessors(List<Professor> actual, List<Professor> expected) {
        assertThat(actual.size()).isEqualTo(expected.size());
        for(int i = 0; i < actual.size(); i++){
            assertUsers(actual.get(i), expected.get(i));
        }
    }

    public static void assertUsers(Student actual, Student expected) {
        assertGenericUsers (actual, expected);
        assertThat(actual.getGroup().getId()).isEqualTo(expected.getGroup().getId());
        assertThat(actual.getGroup().getName()).isEqualTo(expected.getGroup().getName());
    }

    public static void assertUsersStudents(List<Student> actual, List<Student> expected) {
        assertThat(actual.size()).isEqualTo(expected.size());
        for(int i = 0; i < actual.size(); i++){
            assertUsers(actual.get(i), expected.get(i));
        }
    }

    private static <T extends User> void assertGenericUsers(T actual, T expected){
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
    }

}
