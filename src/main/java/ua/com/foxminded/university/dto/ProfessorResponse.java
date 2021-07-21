package ua.com.foxminded.university.dto;

import java.util.List;
import java.util.Objects;

public class ProfessorResponse extends UserResponse {

    private DepartmentResponse departmentResponse;
    private ScienceDegreeResponse scienceDegreeResponse;
    private List<CourseResponse> coursesResponse;

    public DepartmentResponse getDepartmentResponse() {
        return departmentResponse;
    }

    public void setDepartmentResponse(DepartmentResponse departmentResponse) {
        this.departmentResponse = departmentResponse;
    }

    public ScienceDegreeResponse getScienceDegreeResponse() {
        return scienceDegreeResponse;
    }

    public void setScienceDegreeResponse(ScienceDegreeResponse scienceDegreeResponse) {
        this.scienceDegreeResponse = scienceDegreeResponse;
    }

    public List<CourseResponse> getCoursesResponse() {
        return coursesResponse;
    }

    public void setCoursesResponse(List<CourseResponse> coursesResponse) {
        this.coursesResponse = coursesResponse;
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
        ProfessorResponse professorResponse = (ProfessorResponse) o;
        return Objects.equals(departmentResponse, professorResponse.departmentResponse) &&
                Objects.equals(scienceDegreeResponse, professorResponse.scienceDegreeResponse) &&
                Objects.equals(coursesResponse, professorResponse.coursesResponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), departmentResponse, scienceDegreeResponse, coursesResponse);
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + this.getId() +
                ", firstName='" + this.getFirstName() + '\'' +
                ", lastName='" + this.getLastName() + '\'' +
                ", email='" + this.getEmail() + '\'' +
                ", department=" + departmentResponse +
                ", scienceDegree=" + scienceDegreeResponse +
                ", disciplines=" + coursesResponse +
                '}';
    }

}
