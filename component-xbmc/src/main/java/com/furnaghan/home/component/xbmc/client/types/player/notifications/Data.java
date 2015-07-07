package com.furnaghan.home.component.xbmc.client.types.player.notifications;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.xbmc.client.types.notifications.Item;

public class Data {

    @JsonProperty
    private Player player;

    @JsonProperty
    private Item item;

    public Player getPlayer() {
        return player;
    }

    public Item getItem() {
        return item;
    }
}
