package me.thevipershow.minecraftbot.packets.handshake;

import me.thevipershow.minecraftbot.packets.AbstractPacket;
import me.thevipershow.minecraftbot.packets.PacketType;

public final class PingPacket extends AbstractPacket {
    public PingPacket() {
        super(0x01, PacketType.TO_SERVER);
    }
}
