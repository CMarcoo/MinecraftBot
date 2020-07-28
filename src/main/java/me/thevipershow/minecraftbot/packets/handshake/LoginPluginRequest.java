package me.thevipershow.minecraftbot.packets.handshake;

import java.io.DataInputStream;
import java.io.IOException;
import me.thevipershow.minecraftbot.DataUtils;
import me.thevipershow.minecraftbot.packets.AbstractPacket;
import me.thevipershow.minecraftbot.packets.PacketType;

public final class LoginPluginRequest extends AbstractPacket {
    public LoginPluginRequest() {
        super(0x04, PacketType.TO_CLIENT);
    }

    private int messageID;
    private String identifier;
    private byte[] data;

    @Override
    public void readData(DataInputStream dis) {
        try {
            final int length = DataUtils.readVarInt(dis);
            final int packetID = DataUtils.readVarInt(dis);
            if (packetID != getId()) throw new IOException("Received wrong packet.");
            messageID = DataUtils.readVarInt(dis);
            identifier = DataUtils.readString(dis);
            data = dis.readAllBytes();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
