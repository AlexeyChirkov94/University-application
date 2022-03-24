package ua.com.foxminded.university.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="lessons")
@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name="course_id")
    Course course;

    @Column(name="timeofstart")
    LocalDateTime timeOfStartLesson;

    @ManyToOne
    @JoinColumn(name="group_id")
    Group group;

    @ManyToOne
    @JoinColumn(name="professor_id")
    Professor teacher;

    @ManyToOne
    @JoinColumn(name="forms_of_lesson_id")
    FormOfLesson formOfLesson;

}
