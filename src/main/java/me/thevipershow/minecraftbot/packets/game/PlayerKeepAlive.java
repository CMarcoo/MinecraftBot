package me.thevipershow.minecraftbot.packets.game;

import java.io.DataInputStream;
import java.io.IOException;
import me.thevipershow.minecraftbot.DataUtils;
import me.thevipershow.minecraftbot.packets.AbstractPacket;
import me.thevipershow.minecraftbot.packets.PacketType;

public final class PlayerKeepAlive extends AbstractPacket {
    private int keepAliveID;

    public PlayerKeepAlive() {
        super(0x00, PacketType.TO_SERVER);
    }

    @Override
    public void readData(DataInputStream dis) {
        try {
            final int length = DataUtils.readVarInt(dis);
            final int packetID = DataUtils.readVarInt(dis);
            DataUtils.checkPacket(getId(), packetID, length, false);
            keepAliveID = DataUtils.readVarInt(dis);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public int getKeepAliveID() {
        return keepAliveID;
    }
}
