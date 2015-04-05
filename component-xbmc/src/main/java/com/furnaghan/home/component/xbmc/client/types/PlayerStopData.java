package com.furnaghan.home.component.xbmc.client.types;

import java.util.Map;

public class PlayerStopData {

    private boolean end;
    private Map<String, String> item;

    public boolean isEnd() {
        return end;
    }

    public Map<String, String> getItem() {
        return item;
    }
}
