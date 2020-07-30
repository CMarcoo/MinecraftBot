package me.thevipershow.minecraftbot.packets.auth;

import java.io.DataInputStream;
import java.io.IOException;
import me.thevipershow.minecraftbot.DataUtils;
import me.thevipershow.minecraftbot.packets.AbstractPacket;
import me.thevipershow.minecraftbot.packets.PacketType;

public final class SetCompressionPacket extends AbstractPacket {
    private int threshold;

    public SetCompressionPacket() {
        super(0x03, PacketType.TO_CLIENT);
    }

    @Override
    public void readData(DataInputStream dis) {
        try {
            final int length = DataUtils.readVarInt(dis);
            final int packetID = DataUtils.readVarInt(dis);
            DataUtils.checkPacket(getId(), packetID, length, false);
            this.threshold = DataUtils.readVarInt(dis);
            System.out.println("Read compression threshold: " + threshold);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
