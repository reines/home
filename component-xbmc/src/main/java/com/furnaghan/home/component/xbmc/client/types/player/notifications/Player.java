package com.furnaghan.home.component.xbmc.client.types.player.notifications;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Player {

    @JsonProperty("playerid")
    private final int id;

    @JsonProperty
    private final int speed;

    @JsonCreator
    public Player(
            @JsonProperty("playerid") final int id,
            @JsonProperty("speed") final int speed) {
        this.id = id;
        this.speed = speed;
    }

    public int getId() {
        return id;
    }

    public int getSpeed() {
        return speed;
    }
}
