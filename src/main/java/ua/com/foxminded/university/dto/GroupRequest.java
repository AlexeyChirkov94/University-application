package ua.com.foxminded.university.dto;

import java.util.Objects;

public class GroupRequest {

    private Long id;
    private String name;
    private DepartmentRequest departmentRequest;
    private FormOfEducationRequest formOfEducationRequest;

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

    public FormOfEducationRequest getFormOfEducationRequest() {
        return formOfEducationRequest;
    }

    public void setFormOfEducationRequest(FormOfEducationRequest formOfEducationRequest) {
        this.formOfEducationRequest = formOfEducationRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupRequest groupRequest = (GroupRequest) o;
        return Objects.equals(id, groupRequest.id) &&
                Objects.equals(name, groupRequest.name) &&
                Objects.equals(departmentRequest, groupRequest.departmentRequest) &&
                Objects.equals(formOfEducationRequest, groupRequest.formOfEducationRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, departmentRequest, formOfEducationRequest);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department=" + departmentRequest +
                ", formOfEducation=" + formOfEducationRequest +
                '}';
    }

}
