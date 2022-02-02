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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="groups")
@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class Group {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name="name")
    String name;

    @ManyToOne
    @JoinColumn(name="department_id")
    Department department;

    @ManyToOne
    @JoinColumn(name="formofeducation_id")
    FormOfEducation formOfEducation;

}
