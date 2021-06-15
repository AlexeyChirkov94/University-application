package ua.com.foxminded.university.dto;

import java.util.Objects;

public class StudentResponse extends UserResponse {

    private GroupResponse groupResponse;

    public GroupResponse getGroupResponse() {
        return groupResponse;
    }

    public void setGroupResponse(GroupResponse groupResponse) {
        this.groupResponse = groupResponse;
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
        StudentResponse student = (StudentResponse) o;
        return Objects.equals(groupResponse, student.groupResponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), groupResponse);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + this.getId() +
                ", firstName='" + this.getFirstName() + '\'' +
                ", lastName='" + this.getLastName() + '\'' +
                ", email='" + this.getEmail() + '\'' +
                ", group=" + groupResponse +
                '}';
    }

}
