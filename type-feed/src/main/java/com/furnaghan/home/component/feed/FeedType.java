package com.furnaghan.home.component.feed;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;

import java.util.Map;

public interface FeedType extends ComponentType {
    interface Listener extends Component.Listener {
        void newItem(final Map<String, Object> fields);
    }

    void refresh();
}
