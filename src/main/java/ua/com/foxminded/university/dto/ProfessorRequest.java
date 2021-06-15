package ua.com.foxminded.university.dto;

import java.util.List;
import java.util.Objects;

public class ProfessorRequest extends UserRequest {

    private DepartmentRequest departmentRequest;
    private ScienceDegreeRequest scienceDegreeRequest;
    private List<CourseRequest> coursesRequest;

    public DepartmentRequest getDepartmentRequest() {
        return departmentRequest;
    }

    public void setDepartmentRequest(DepartmentRequest departmentRequest) {
        this.departmentRequest = departmentRequest;
    }

    public ScienceDegreeRequest getScienceDegreeRequest() {
        return scienceDegreeRequest;
    }

    public void setScienceDegreeRequest(ScienceDegreeRequest scienceDegreeRequest) {
        this.scienceDegreeRequest = scienceDegreeRequest;
    }

    public List<CourseRequest> getCoursesRequest() {
        return coursesRequest;
    }

    public void setCoursesRequest(List<CourseRequest> coursesRequest) {
        this.coursesRequest = coursesRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ProfessorRequest professorRequest = (ProfessorRequest) o;
        return Objects.equals(departmentRequest, professorRequest.departmentRequest) &&
                Objects.equals(scienceDegreeRequest, professorRequest.scienceDegreeRequest) &&
                Objects.equals(coursesRequest, professorRequest.coursesRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), departmentRequest, scienceDegreeRequest, coursesRequest);
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + this.getId() +
                ", firstName='" + this.getFirstName() + '\'' +
                ", lastName='" + this.getLastName() + '\'' +
                ", email='" + this.getEmail() + '\'' +
                ", department=" + departmentRequest +
                ", scienceDegree=" + scienceDegreeRequest +
                ", disciplines=" + coursesRequest +
                '}';
    }

}
