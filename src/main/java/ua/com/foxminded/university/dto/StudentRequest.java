package ua.com.foxminded.university.dto;

import java.util.Objects;

public class StudentRequest extends UserRequest {

    private GroupRequest groupRequest;

    public GroupRequest getGroupRequest() {
        return groupRequest;
    }

    public void setGroupRequest(GroupRequest groupRequest) {
        this.groupRequest = groupRequest;
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
        StudentRequest student = (StudentRequest) o;
        return Objects.equals(groupRequest, student.groupRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), groupRequest);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + this.getId() +
                ", firstName='" + this.getFirstName() + '\'' +
                ", lastName='" + this.getLastName() + '\'' +
                ", email='" + this.getEmail() + '\'' +
                ", group=" + groupRequest +
                '}';
    }

}
