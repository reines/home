package com.furnaghan.home.component.xbmc.client.methods.remote;

import com.furnaghan.home.component.xbmc.client.types.PlayRequest;

public interface Player {
    String Open(final PlayRequest item);
    String Stop(final int playerid);
}
