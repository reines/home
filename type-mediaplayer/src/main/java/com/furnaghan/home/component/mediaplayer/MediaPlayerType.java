package com.furnaghan.home.component.mediaplayer;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;

public interface MediaPlayerType extends ComponentType {
    interface Listener extends Component.Listener {
        void onVolumeChanged(final int volume);
        void onPlay(final String path);
        void onPause(final String path);
        void onStop(final String path);
    }

    void setVolume(final int volume);
    void play(final String path);
    void stop();
}
