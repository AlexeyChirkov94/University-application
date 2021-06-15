package ua.com.foxminded.university.dto;

import java.util.Objects;

public class GroupResponse {

    private Long id;
    private String name;
    private DepartmentResponse departmentResponse;
    private FormOfEducationResponse formOfEducationResponse;

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

    public FormOfEducationResponse getFormOfEducationResponse() {
        return formOfEducationResponse;
    }

    public void setFormOfEducationResponse(FormOfEducationResponse formOfEducationResponse) {
        this.formOfEducationResponse = formOfEducationResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupResponse groupResponse = (GroupResponse) o;
        return Objects.equals(id, groupResponse.id) &&
                Objects.equals(name, groupResponse.name) &&
                Objects.equals(departmentResponse, groupResponse.departmentResponse) &&
                Objects.equals(formOfEducationResponse, groupResponse.formOfEducationResponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, departmentResponse, formOfEducationResponse);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department=" + departmentResponse +
                ", formOfEducation=" + formOfEducationResponse +
                '}';
    }

}
