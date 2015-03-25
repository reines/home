package com.furnaghan.home.component;

import com.furnaghan.home.util.Listenable;

public abstract class Component<T extends Component.Listener> extends Listenable<T> {

    public static interface Listener {
    }
}
