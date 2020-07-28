package me.thevipershow.minecraftbot.packets;

import me.thevipershow.minecraftbot.packets.handshake.HandshakePacket;

public enum PacketType {
    TO_SERVER(HandshakePacket.class),
    TO_CLIENT;

    private final Class<? extends AbstractPacket>[] packetsOfType;

    PacketType(final Class<? extends AbstractPacket>... packetsOfType) {
        this.packetsOfType = packetsOfType;
    }

    public Class<? extends AbstractPacket>[] getPacketsOfType() {
        return packetsOfType;
    }
}
