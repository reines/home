package com.furnaghan.home.component.xbmc.client.methods.local;

import com.furnaghan.home.component.xbmc.client.types.player.notifications.Data;
import com.furnaghan.home.component.xbmc.client.types.api.PlayerStopData;

public interface Player {
    void OnPause(final String sender, final Data data);
    void OnPlay(final String sender, final Data data);
    void OnStop(final String sender, final PlayerStopData data);
}
