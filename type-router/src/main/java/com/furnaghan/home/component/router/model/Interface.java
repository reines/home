package com.furnaghan.home.component.router.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Interface {
    public static class Stats {
        private final long bytes;
        private final long packets;
        private final long errors;
        private final long dropped;

        public Stats(long bytes, long packets, long errors, long dropped) {
            this.bytes = bytes;
            this.packets = packets;
            this.errors = errors;
            this.dropped = dropped;
        }

        public long getBytes() {
            return bytes;
        }

        public long getPackets() {
            return packets;
        }

        public long getErrors() {
            return errors;
        }

        public long getDropped() {
            return dropped;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("bytes", bytes)
                    .add("packets", packets)
                    .add("errors", errors)
                    .add("dropped", dropped)
                    .toString();
        }
    }

    private final Stats receive;
    private final Stats transmit;

    public Interface(Stats receive, Stats transmit) {
        this.receive = receive;
        this.transmit = transmit;
    }

    public Stats getReceive() {
        return receive;
    }

    public Stats getTransmit() {
        return transmit;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("receive", receive)
                .add("transmit", transmit)
                .toString();
    }
}
