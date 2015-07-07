package com.furnaghan.home.component.xbmc.client.types.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.xbmc.client.types.notifications.Item;

public class PlayerStopData {

    @JsonProperty
    private boolean end;

    @JsonProperty
    private Item item;

    public boolean isEnd() {
        return end;
    }

    public Item getItem() {
        return item;
    }
}
