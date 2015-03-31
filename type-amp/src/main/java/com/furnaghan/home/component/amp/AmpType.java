package com.furnaghan.home.component.amp;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;
import com.furnaghan.home.component.amp.model.Source;

public interface AmpType extends ComponentType {
    interface Listener extends Component.Listener {
        void turnedOn();
        void turnedOff();
        void muteChanged(final boolean mute);
        void volumeChanged(final int volume);
    }

    void turnOn();
    void turnOff();
    void setMute(final boolean mute);
    void setVolume(final int volume);
    void setSource(final Source source);
}
