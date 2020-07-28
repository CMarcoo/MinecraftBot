package me.thevipershow.minecraftbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;
import me.thevipershow.minecraftbot.DataUtils;

public abstract class AbstractPacket implements Packet {
    private final int id;
    private final PacketType packetType;

    public AbstractPacket(final int id, final PacketType packetType) {
        this.id = id;
        this.packetType = packetType;
    }

    public int getId() {
        return id;
    }

    public PacketType getPacketType() {
        return packetType;
    }

    public void sendPacket(final DataOutputStream dos) throws IOException {
        DataUtils.writeVarInt(dos, id);
        writeData(dos);
    }
}
