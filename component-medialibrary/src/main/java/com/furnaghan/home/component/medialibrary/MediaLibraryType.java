package com.furnaghan.home.component.medialibrary;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;

public interface MediaLibraryType extends ComponentType {
    interface Listener extends Component.Listener {
        void onAdded(final String path);
        void onRemoved(final String path);
        void onMarkedWatched(final String path);
    }
}
