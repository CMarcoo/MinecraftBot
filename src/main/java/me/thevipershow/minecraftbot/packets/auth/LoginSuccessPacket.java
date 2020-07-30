package me.thevipershow.minecraftbot.packets.auth;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;
import me.thevipershow.minecraftbot.DataUtils;
import me.thevipershow.minecraftbot.packets.AbstractPacket;
import me.thevipershow.minecraftbot.packets.PacketType;

public final class LoginSuccessPacket extends AbstractPacket {
    private UUID uuid = null;
    private String username = null;

    public LoginSuccessPacket() {
        super(0x02, PacketType.TO_CLIENT);
    }

    @Override
    public void readData(DataInputStream dis) {
        try {
            final int length = DataUtils.readVarInt(dis);
            final int id = DataUtils.readVarInt(dis);
            DataUtils.checkPacket(getId(), id, length, false);
            uuid = UUID.fromString( DataUtils.readString(dis));
            username = DataUtils.readString(dis);
            System.out.println(uuid.toString() + " " + username );
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }
}
