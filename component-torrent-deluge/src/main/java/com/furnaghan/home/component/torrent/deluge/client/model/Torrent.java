package com.furnaghan.home.component.torrent.deluge.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.torrent.api.State;
import io.dropwizard.util.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Torrent extends com.furnaghan.home.component.torrent.api.Torrent {

    private final int maxDownloadSpeed;
    private final int uploadPayloadRate;
    private final int downloadPayloadRate;
    private final int numPeers;
    private final int totalPeers;
    private final int maxUploadSpeed;

    @JsonCreator
    public Torrent(
            @JsonProperty("max_download_speed") int maxDownloadSpeed,
            @JsonProperty("upload_payload_rate") int uploadPayloadRate,
            @JsonProperty("download_payload_rate") int downloadPayloadRate,
            @JsonProperty("num_peers") int numPeers,
            @JsonProperty("ratio") double ratio,
            @JsonProperty("total_peers") int totalPeers,
            @JsonProperty("total_size") long size,
            @JsonProperty("max_upload_speed") int maxUploadSpeed,
            @JsonProperty("state") State state,
            @JsonProperty("save_path") String path,
            @JsonProperty("name") String name) {
        super (name, state, Size.bytes(size), ratio, path);

        this.maxDownloadSpeed = maxDownloadSpeed;
        this.uploadPayloadRate = uploadPayloadRate;
        this.downloadPayloadRate = downloadPayloadRate;
        this.numPeers = numPeers;
        this.totalPeers = totalPeers;
        this.maxUploadSpeed = maxUploadSpeed;
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

    public int getTotalPeers() {
        return totalPeers;
    }

    public int getMaxUploadSpeed() {
        return maxUploadSpeed;
    }
}
