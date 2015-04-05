package com.furnaghan.home.component.xbmc.client.methods.remote;

import com.furnaghan.home.component.xbmc.client.types.Permissions;
import com.furnaghan.home.component.xbmc.client.types.Version;

import java.util.Map;

public interface JSONRPC {
    String Ping();
    Map<String, Version> Version();
    Permissions Permission();
}
