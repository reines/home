package com.furnaghan.home.component.torrent.deluge.client.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jackson.JsonSnakeCase;

@JsonSnakeCase
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Options {

    public static final Options DEFAULT_OPTIONS = new Options(null, null, null, null, null, null, null, null, null, null);

    @JsonProperty
    private final Boolean addPaused;

    @JsonProperty
    private final Boolean compactAllocation;

    @JsonProperty
    private final String downloadLocation;

    @JsonProperty
    private final Integer maxConnections;

    @JsonProperty
    private final Integer maxDownloadSpeed;

    @JsonProperty
    private final Integer maxUploadSlots;

    @JsonProperty
    private final Integer maxUploadSpeed;

    @JsonProperty
    private final Boolean moveCompleted;

    @JsonProperty
    private final String moveCompletedPath;

    @JsonProperty
    private final Boolean prioritizeFirstLastPieces;

    public Options(Boolean addPaused, Boolean compactAllocation, String downloadLocation, Integer maxConnections, Integer maxDownloadSpeed, Integer maxUploadSlots, Integer maxUploadSpeed, Boolean moveCompleted, String moveCompletedPath, Boolean prioritizeFirstLastPieces) {
        this.addPaused = addPaused;
        this.compactAllocation = compactAllocation;
        this.downloadLocation = downloadLocation;
        this.maxConnections = maxConnections;
        this.maxDownloadSpeed = maxDownloadSpeed;
        this.maxUploadSlots = maxUploadSlots;
        this.maxUploadSpeed = maxUploadSpeed;
        this.moveCompleted = moveCompleted;
        this.moveCompletedPath = moveCompletedPath;
        this.prioritizeFirstLastPieces = prioritizeFirstLastPieces;
    }

    public Boolean getAddPaused() {
        return addPaused;
    }

    public Boolean getCompactAllocation() {
        return compactAllocation;
    }

    public String getDownloadLocation() {
        return downloadLocation;
    }

    public Integer getMaxConnections() {
        return maxConnections;
    }

    public Integer getMaxDownloadSpeed() {
        return maxDownloadSpeed;
    }

    public Integer getMaxUploadSlots() {
        return maxUploadSlots;
    }

    public Integer getMaxUploadSpeed() {
        return maxUploadSpeed;
    }

    public Boolean getMoveCompleted() {
        return moveCompleted;
    }

    public String getMoveCompletedPath() {
        return moveCompletedPath;
    }

    public Boolean getPrioritizeFirstLastPieces() {
        return prioritizeFirstLastPieces;
    }
}
