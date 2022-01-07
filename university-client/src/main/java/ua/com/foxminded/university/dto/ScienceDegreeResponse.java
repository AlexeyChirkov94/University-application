package ua.com.foxminded.university.dto;

import java.util.Arrays;

public enum ScienceDegreeResponse {

    GRADUATE(1),
    MASTER(2),
    PH_D_CANDIDATE(3),
    PH_D(4);

    private int id;

    ScienceDegreeResponse(int id) {
        this.id = id;
    }

    public static ScienceDegreeResponse getById(int id) {
        return Arrays.stream(values())
                .filter(legNo -> legNo.id == id)
                .findFirst().orElse(null);
    }

    public int getId(){
        return id;
    }

}