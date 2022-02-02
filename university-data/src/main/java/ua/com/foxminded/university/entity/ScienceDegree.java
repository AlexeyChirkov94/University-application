package ua.com.foxminded.university.entity;

import java.util.Arrays;

public enum ScienceDegree {

    GRADUATE(0),
    MASTER(1),
    PH_D_CANDIDATE(2),
    PH_D(3);

    private int id;

    ScienceDegree(int id) {
        this.id = id;
    }

    public static ScienceDegree getById(int id) {
        return Arrays.stream(values())
                .filter(legNo -> legNo.id == id)
                .findFirst().orElse(null);
    }

    public int getId(){
        return id;
    }

}
