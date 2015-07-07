package com.furnaghan.home.component.xbmc.client.methods.remote;

import com.furnaghan.home.component.xbmc.client.types.api.jsonrpc.Permission;
import com.furnaghan.home.component.xbmc.client.types.api.jsonrpc.Version;

import java.util.Map;

public interface JSONRPC {
    String Ping();
    Map<String, Version> Version();
    Permission Permission();
}
