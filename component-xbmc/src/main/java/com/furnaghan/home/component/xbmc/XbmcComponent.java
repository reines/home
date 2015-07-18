package com.furnaghan.home.component.xbmc;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.medialibrary.MediaLibraryType;
import com.furnaghan.home.component.mediaplayer.MediaPlayerType;
import com.furnaghan.home.component.notifier.NotifierType;
import com.furnaghan.home.component.xbmc.client.XbmcClient;
import com.furnaghan.home.component.xbmc.client.methods.local.Application;
import com.furnaghan.home.component.xbmc.client.methods.local.Player;
import com.furnaghan.home.component.xbmc.client.methods.local.VideoLibrary;
import com.furnaghan.home.component.xbmc.client.types.api.PlayRequest;
import com.furnaghan.home.component.xbmc.client.types.player.notifications.Data;
import com.furnaghan.home.component.xbmc.client.types.api.PlayerStopData;
import com.furnaghan.home.component.xbmc.client.types.api.VolumeData;
import com.furnaghan.home.util.JsonUtil;
import com.google.common.collect.Range;

import java.io.IOException;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

public class XbmcComponent extends Component<XbmcComponent.Listener> implements MediaLibraryType, MediaPlayerType, NotifierType {
    public interface Listener extends MediaLibraryType.Listener, MediaPlayerType.Listener, NotifierType.Listener {}

    private static final Range<Integer> VOLUME_RANGE = Range.closed(0, 100);

    private static final int PLAYER_AUDIO = 0;
    private static final int PLAYER_VIDEO = 1;
    private static final int PLAYER_PICTURE = 2;

    public static void main(final String... args) throws IOException {
        final XbmcComponent xbmc = new XbmcComponent(JsonUtil.newObjectMapper().readValue("{\"address\":\"localhost:9090\"}", XbmcConfiguration.class));
        xbmc.send("title", "message");
    }

    private final XbmcClient client;

    public XbmcComponent(final XbmcConfiguration configuration) throws IOException {
        client = new XbmcClient(configuration.getAddress());

        client.register(new Application() {
            @Override
            public void OnVolumeChanged(final String sender, final VolumeData data) {
                trigger(l -> l.onVolumeChanged(data.getVolume()));
            }
        }, Application.class);

        client.register(new Player() {
            @Override
            public void OnPause(final String sender, final Data data) {
                final String path = (String) data.getItem().get("title");
                trigger(l -> l.onPause(path));
            }

            @Override
            public void OnPlay(final String sender, final Data data) {
                final String path = (String) data.getItem().get("title");
                trigger(l -> l.onPlay(path));
            }

            @Override
            public void OnStop(final String sender, final PlayerStopData data) {
                final String path = (String) data.getItem().get("title");
                trigger(l -> l.onStop(path));
            }
        }, Player.class);

        client.register(new VideoLibrary() {
            @Override
            public void OnRemove(final String sender, final Map<String, ?> data) {
                System.err.println("remove:");
                System.err.println(data);
            }

            @Override
            public void OnUpdate(final String sender, final Map<String, ?> data) {
                System.err.println("update:");
                System.err.println(data);

            }
        }, VideoLibrary.class);
    }

    @Override
    public void setVolume(final int volume) {
        checkArgument(VOLUME_RANGE.contains(volume), "Volume must within range " + VOLUME_RANGE);

        final int result = client.application().SetVolume(volume);
        checkState(result == volume, String.format(
                "Failed to set volume to requested level, requested %d but was %d", volume, result));
    }

    @Override
    public void play(final String path) {
        client.player().Open(new PlayRequest(path, true));
    }

    @Override
    public void stop() {
        client.player().Stop(PLAYER_VIDEO);
    }

    @Override
    public void send(final String title, final String message) {
        client.gui().ShowNotification(title, message);
    }
}
