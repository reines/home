package com.furnaghan.home.component.xbmc.client.types.video.details;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.xbmc.client.types.video.Resume;
import com.furnaghan.home.component.xbmc.client.types.video.Streams;

import java.util.List;

public class File extends Item {

    @JsonProperty("streamdetails")
    private Streams streamDetails;

    @JsonProperty("director")
    private List<String> directors;

    @JsonProperty
    private Resume resume;

    @JsonProperty
    private int runtime;

    public Streams getStreamDetails() {
        return streamDetails;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public Resume getResume() {
        return resume;
    }

    public int getRuntime() {
        return runtime;
    }
}
