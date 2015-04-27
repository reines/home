package com.furnaghan.home.component.torrent.deluge.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Stats {

    public static final Stats DEFAULT = new Stats(0, 0, 0, 0, false, 0, 0, 0, 0, 0, 0);

    private final int dhtNodes;
    private final int downloadProtocolRate;
    private final int downloadRate;
    private final long freeSpace;
    private final boolean hasIncomingConnections;
    private final int maxDownload;
    private final int maxNumConnections;
    private final int maxUpload;
    private final int numConnections;
    private final int uploadProtocolRate;
    private final int uploadRate;

    @JsonCreator
    public Stats(
            @JsonProperty("dht_nodes") int dhtNodes,
            @JsonProperty("download_protocol_rate") int downloadProtocolRate,
            @JsonProperty("download_rate") int downloadRate,
            @JsonProperty("free_space") long freeSpace,
            @JsonProperty("has_incoming_connections") boolean hasIncomingConnections,
            @JsonProperty("max_download") int maxDownload,
            @JsonProperty("max_num_connections") int maxNumConnections,
            @JsonProperty("max_upload") int maxUpload,
            @JsonProperty("num_connections") int numConnections,
            @JsonProperty("upload_protocol_rate") int uploadProtocolRate,
            @JsonProperty("upload_rate") int uploadRate) {
        this.dhtNodes = dhtNodes;
        this.downloadProtocolRate = downloadProtocolRate;
        this.downloadRate = downloadRate;
        this.freeSpace = freeSpace;
        this.hasIncomingConnections = hasIncomingConnections;
        this.maxDownload = maxDownload;
        this.maxNumConnections = maxNumConnections;
        this.maxUpload = maxUpload;
        this.numConnections = numConnections;
        this.uploadProtocolRate = uploadProtocolRate;
        this.uploadRate = uploadRate;
    }

    public int getDhtNodes() {
        return dhtNodes;
    }

    public int getDownloadProtocolRate() {
        return downloadProtocolRate;
    }

    public int getDownloadRate() {
        return downloadRate;
    }

    public long getFreeSpace() {
        return freeSpace;
    }

    public boolean isHasIncomingConnections() {
        return hasIncomingConnections;
    }

    public int getMaxDownload() {
        return maxDownload;
    }

    public int getMaxNumConnections() {
        return maxNumConnections;
    }

    public int getMaxUpload() {
        return maxUpload;
    }

    public int getNumConnections() {
        return numConnections;
    }

    public int getUploadProtocolRate() {
        return uploadProtocolRate;
    }

    public int getUploadRate() {
        return uploadRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stats stats = (Stats) o;

        if (dhtNodes != stats.dhtNodes) return false;
        if (downloadProtocolRate != stats.downloadProtocolRate) return false;
        if (downloadRate != stats.downloadRate) return false;
        if (freeSpace != stats.freeSpace) return false;
        if (hasIncomingConnections != stats.hasIncomingConnections) return false;
        if (maxDownload != stats.maxDownload) return false;
        if (maxNumConnections != stats.maxNumConnections) return false;
        if (maxUpload != stats.maxUpload) return false;
        if (numConnections != stats.numConnections) return false;
        if (uploadProtocolRate != stats.uploadProtocolRate) return false;
        if (uploadRate != stats.uploadRate) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dhtNodes;
        result = 31 * result + downloadProtocolRate;
        result = 31 * result + downloadRate;
        result = 31 * result + (int) (freeSpace ^ (freeSpace >>> 32));
        result = 31 * result + (hasIncomingConnections ? 1 : 0);
        result = 31 * result + maxDownload;
        result = 31 * result + maxNumConnections;
        result = 31 * result + maxUpload;
        result = 31 * result + numConnections;
        result = 31 * result + uploadProtocolRate;
        result = 31 * result + uploadRate;
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("dhtNodes", dhtNodes)
                .add("downloadProtocolRate", downloadProtocolRate)
                .add("downloadRate", downloadRate)
                .add("freeSpace", freeSpace)
                .add("hasIncomingConnections", hasIncomingConnections)
                .add("maxDownload", maxDownload)
                .add("maxNumConnections", maxNumConnections)
                .add("maxUpload", maxUpload)
                .add("numConnections", numConnections)
                .add("uploadProtocolRate", uploadProtocolRate)
                .add("uploadRate", uploadRate)
                .toString();
    }
}
