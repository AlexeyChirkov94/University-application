package ua.com.foxminded.university.dto;

import java.util.Objects;

public class CourseResponse {

    private Long id;
    private String name;
    private DepartmentResponse departmentResponse;

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

    public DepartmentResponse getDepartmentResponse() {
        return departmentResponse;
    }

    public void setDepartmentResponse(DepartmentResponse departmentResponse) {
        this.departmentResponse = departmentResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CourseResponse that = (CourseResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(departmentResponse, that.departmentResponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, departmentResponse);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department=" + departmentResponse +
                '}';
    }


}
