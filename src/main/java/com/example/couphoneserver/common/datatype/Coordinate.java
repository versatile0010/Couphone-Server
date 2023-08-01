package com.example.couphoneserver.common.datatype;

import lombok.Data;

@Data
public class Coordinate {
    private double x;
    private double y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
