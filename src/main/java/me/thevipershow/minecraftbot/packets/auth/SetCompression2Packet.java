package me.thevipershow.minecraftbot.packets.auth;

import java.io.DataInputStream;
import java.io.IOException;
import me.thevipershow.minecraftbot.DataUtils;
import me.thevipershow.minecraftbot.packets.AbstractPacket;
import me.thevipershow.minecraftbot.packets.PacketType;

public final class SetCompression2Packet extends AbstractPacket {
    public SetCompression2Packet() {
        super(0x46, PacketType.TO_CLIENT);
    }

    private int threshold;

    @Override
    public void readData(DataInputStream dis) {
        try {
            final int length = DataUtils.readVarInt(dis);
            final int packetID = DataUtils.readVarInt(dis);
            DataUtils.checkPacket(getId(), packetID, length, false);
            threshold = DataUtils.readVarInt(dis);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public int getThreshold() {
        return threshold;
    }
}
