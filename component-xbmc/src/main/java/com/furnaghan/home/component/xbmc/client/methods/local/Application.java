package com.furnaghan.home.component.xbmc.client.methods.local;

import com.furnaghan.home.component.xbmc.client.types.VolumeData;

public interface Application {
    void OnVolumeChanged(final String sender, final VolumeData data);
}
