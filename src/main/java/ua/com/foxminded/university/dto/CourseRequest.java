package ua.com.foxminded.university.dto;

import java.util.Objects;

public class CourseRequest {

    private Long id;
    private String name;
    private DepartmentRequest departmentRequest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DepartmentRequest getDepartmentRequest() {
        return departmentRequest;
    }

    public void setDepartmentRequest(DepartmentRequest departmentRequest) {
        this.departmentRequest = departmentRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CourseRequest that = (CourseRequest) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(departmentRequest, that.departmentRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, departmentRequest);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department=" + departmentRequest +
                '}';
    }

}
