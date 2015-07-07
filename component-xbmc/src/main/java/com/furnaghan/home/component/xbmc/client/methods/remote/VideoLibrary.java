package com.furnaghan.home.component.xbmc.client.methods.remote;

import com.furnaghan.home.component.xbmc.client.types.Mixed;
import com.furnaghan.home.component.xbmc.client.types.api.videolibrary.Episodes;
import com.furnaghan.home.component.xbmc.client.types.list.Limits;
import com.furnaghan.home.component.xbmc.client.types.list.Sort;
import com.furnaghan.home.component.xbmc.client.types.video.fields.Episode;

public interface VideoLibrary {
    Episodes GetEpisodes(final int tvshowid, final int season, final Episode properties, final Limits limits, final Sort sort, final Mixed filter);
}
