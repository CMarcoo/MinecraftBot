package me.thevipershow.minecraftbot.packets.handshake;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import me.thevipershow.minecraftbot.DataUtils;
import me.thevipershow.minecraftbot.packets.AbstractPacket;
import me.thevipershow.minecraftbot.packets.PacketType;

public final class ResponsePacket extends AbstractPacket {
    private String response;

    public ResponsePacket() {
        super(0x00, PacketType.TO_CLIENT);
    }

    @Override
    public void readData(final DataInputStream dis) {
        try {
            int length = DataUtils.readVarInt(dis);
            int id = DataUtils.readVarInt(dis);
            DataUtils.checkPacket(getId(), id, length, false);
            final int stringLength = DataUtils.readVarInt(dis);
            final byte[] in = new byte[stringLength];
            dis.readFully(in);
            final String json = new String(in, StandardCharsets.UTF_8);
            response = json;
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public String getResponse() {
        return response;
    }
}
