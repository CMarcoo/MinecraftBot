package me.thevipershow.minecraftbot.packets.handshake;

import java.io.DataOutputStream;
import me.thevipershow.minecraftbot.packets.AbstractPacket;
import me.thevipershow.minecraftbot.packets.PacketType;

public final class RequestPacket extends AbstractPacket {
    public RequestPacket() {
        super(0x00, PacketType.TO_SERVER);
    }

    @Override
    public void writeData(final DataOutputStream dos) { // do nothing. Avoid UnsupportedOperationException
    }
}
