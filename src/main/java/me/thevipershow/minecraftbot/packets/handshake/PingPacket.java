package me.thevipershow.minecraftbot.packets.handshake;

import java.io.DataOutputStream;
import java.io.IOException;
import me.thevipershow.minecraftbot.packets.AbstractPacket;
import me.thevipershow.minecraftbot.packets.PacketType;

public final class PingPacket extends AbstractPacket {
    public PingPacket() {
        super(0x01, PacketType.TO_SERVER);
    }

    @Override
    public void writeData() {
        try {
            dataOutputStream.writeLong(420L);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
