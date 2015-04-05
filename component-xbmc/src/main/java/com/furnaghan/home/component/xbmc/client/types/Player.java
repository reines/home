package com.furnaghan.home.component.xbmc.client.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Player {

    private final int id;
    private final int speed;

    @JsonCreator
    public Player(
            @JsonProperty("playerid") final int id,
            @JsonProperty("speed") final int speed) {
        this.id = id;
        this.speed = speed;
    }

    @JsonProperty("playerid")
    public int getId() {
        return id;
    }

    public int getSpeed() {
        return speed;
    }
}
