package com.furnaghan.home.component.torrent.deluge.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Torrent {

    @JsonProperty
    private final int maxDownloadSpeed;

    @JsonProperty
    private final int uploadPayloadRate;

    @JsonProperty
    private final int downloadPayloadRate;

    @JsonProperty
    private final int numPeers;

    @JsonProperty
    private final double ratio;

    @JsonProperty
    private final int totalPeers;

    @JsonProperty
    private final long totalSize;

    @JsonProperty
    private final int maxUploadSpeed;

    @JsonProperty
    private final String savePath;

    @JsonProperty
    private final State state;

    @JsonProperty
    private final String name;

    @JsonCreator
    public Torrent(
            @JsonProperty("max_download_speed") int maxDownloadSpeed,
            @JsonProperty("upload_payload_rate") int uploadPayloadRate,
            @JsonProperty("download_payload_rate") int downloadPayloadRate,
            @JsonProperty("num_peers") int numPeers,
            @JsonProperty("ratio") double ratio,
            @JsonProperty("total_peers") int totalPeers,
            @JsonProperty("total_size") long totalSize,
            @JsonProperty("max_upload_speed") int maxUploadSpeed,
            @JsonProperty("state") State state,
            @JsonProperty("save_path") String savePath,
            @JsonProperty("name") String name) {
        this.maxDownloadSpeed = maxDownloadSpeed;
        this.uploadPayloadRate = uploadPayloadRate;
        this.downloadPayloadRate = downloadPayloadRate;
        this.numPeers = numPeers;
        this.ratio = ratio;
        this.totalPeers = totalPeers;
        this.totalSize = totalSize;
        this.maxUploadSpeed = maxUploadSpeed;
        this.savePath = savePath;
        this.state = state;
        this.name = name;
    }

    public int getMaxDownloadSpeed() {
        return maxDownloadSpeed;
    }

    public int getUploadPayloadRate() {
        return uploadPayloadRate;
    }

    public int getDownloadPayloadRate() {
        return downloadPayloadRate;
    }

    public int getNumPeers() {
        return numPeers;
    }

    public double getRatio() {
        return ratio;
    }

    public int getTotalPeers() {
        return totalPeers;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public int getMaxUploadSpeed() {
        return maxUploadSpeed;
    }

    public String getSavePath() {
        return savePath;
    }

    public State getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Torrent that = (Torrent) o;

        if (downloadPayloadRate != that.downloadPayloadRate) return false;
        if (maxDownloadSpeed != that.maxDownloadSpeed) return false;
        if (maxUploadSpeed != that.maxUploadSpeed) return false;
        if (numPeers != that.numPeers) return false;
        if (Double.compare(that.ratio, ratio) != 0) return false;
        if (totalPeers != that.totalPeers) return false;
        if (totalSize != that.totalSize) return false;
        if (uploadPayloadRate != that.uploadPayloadRate) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (savePath != null ? !savePath.equals(that.savePath) : that.savePath != null) return false;
        if (state != that.state) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = maxDownloadSpeed;
        result = 31 * result + uploadPayloadRate;
        result = 31 * result + downloadPayloadRate;
        result = 31 * result + numPeers;
        temp = Double.doubleToLongBits(ratio);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + totalPeers;
        result = 31 * result + (int) (totalSize ^ (totalSize >>> 32));
        result = 31 * result + maxUploadSpeed;
        result = 31 * result + (savePath != null ? savePath.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("maxDownloadSpeed", maxDownloadSpeed)
                .add("uploadPayloadRate", uploadPayloadRate)
                .add("downloadPayloadRate", downloadPayloadRate)
                .add("numPeers", numPeers)
                .add("ratio", ratio)
                .add("totalPeers", totalPeers)
                .add("totalSize", totalSize)
                .add("maxUploadSpeed", maxUploadSpeed)
                .add("savePath", savePath)
                .add("state", state)
                .add("name", name)
                .toString();
    }
}
