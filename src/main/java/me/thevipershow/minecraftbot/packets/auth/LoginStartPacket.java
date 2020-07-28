package me.thevipershow.minecraftbot.packets.auth;

import java.io.DataOutputStream;
import java.io.IOException;
import me.thevipershow.minecraftbot.DataUtils;
import me.thevipershow.minecraftbot.packets.AbstractPacket;
import me.thevipershow.minecraftbot.packets.PacketType;

public final class LoginStartPacket extends AbstractPacket {
    private final String username;

    public LoginStartPacket(final String username) {
        super(0x00, PacketType.TO_SERVER);
        this.username = username;
    }

    @Override
    public void writeData(final DataOutputStream dos) {
        try {
            DataUtils.writeString(dos, username);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPacket(final DataOutputStream dos) throws IOException {
        DataUtils.writeVarInt(dos, getId());
        writeData(dos);
    }

    public String getUsername() {
        return username;
    }
}
