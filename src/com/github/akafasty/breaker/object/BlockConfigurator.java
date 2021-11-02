package com.github.akafasty.breaker.object;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BlockConfigurator {

    private int id, durability, minutes;
    private boolean starts;

}
