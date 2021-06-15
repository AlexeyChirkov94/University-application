package ua.com.foxminded.university.entity;

import java.util.Arrays;

public enum ScienceDegree {


    GRADUATE(1),
    MASTER(2),
    PH_D_CANDIDATE(3),
    PH_D(4);

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
