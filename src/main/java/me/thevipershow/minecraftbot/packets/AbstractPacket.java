package me.thevipershow.minecraftbot.packets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import me.thevipershow.minecraftbot.DataUtils;

public abstract class AbstractPacket implements Packet {
    private final int id;
    private final PacketType packetType;
    protected final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    protected final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

    public AbstractPacket(final int id, final PacketType packetType) {
        this.id = id;
        this.packetType = packetType;
    }

    public void sendPacket(final DataOutputStream dos) throws IOException {
        final int packetSize = byteArrayOutputStream.size();
        System.out.println(packetSize);
        DataUtils.writeVarInt(dos, packetSize);
        dos.write(byteArrayOutputStream.toByteArray());
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }

    public int getId() {
        return id;
    }

    public PacketType getPacketType() {
        return packetType;
    }
}
