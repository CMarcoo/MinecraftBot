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
            int size = DataUtils.readVarInt(dis);
            int id = DataUtils.readVarInt(dis);
            if (id == -1) {
                throw new IOException("Premature end of stream");
            } else if (id != 0x00) {
                throw new IOException("Invalid packet ID");
            }

            int length = DataUtils.readVarInt(dis);

            if (length == -1) {
                throw new IOException("Premature end of stream.");
            }

            if (length == 0) {
                throw new IOException("Invalid string length.");
            }

            final byte[] in = new byte[length];
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
