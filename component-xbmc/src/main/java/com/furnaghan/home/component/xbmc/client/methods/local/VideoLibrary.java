package com.furnaghan.home.component.xbmc.client.methods.local;

import java.util.Map;

public interface VideoLibrary {
    void OnRemove(final String sender, final Map<String, ?> data);
    void OnUpdate(final String sender, final Map<String, ?> data);
}
