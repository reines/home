package com.furnaghan.home.component.xbmc.client.methods.local;

import com.furnaghan.home.component.xbmc.client.types.PlayerNotificationsData;
import com.furnaghan.home.component.xbmc.client.types.PlayerStopData;

public interface Player {
    void OnPause(final String sender, final PlayerNotificationsData data);
    void OnPlay(final String sender, final PlayerNotificationsData data);
    void OnStop(final String sender, final PlayerStopData data);
}
