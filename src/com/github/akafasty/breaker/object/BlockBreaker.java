package com.github.akafasty.breaker.object;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BlockBreaker {

    private int durability;
    private double x, y, z;
    private long time;

}
