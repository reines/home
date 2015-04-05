package com.furnaghan.home.component.xbmc.client.methods.local;

public interface System {
    void OnLowBattery(final String sender);
    void OnQuit(final String sender);
    void OnRestart(final String sender);
    void OnSleep(final String sender);
    void OnWake(final String sender);
}
