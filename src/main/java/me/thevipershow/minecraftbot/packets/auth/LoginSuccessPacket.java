package me.thevipershow.minecraftbot.packets.auth;

import java.io.DataInputStream;
import java.util.UUID;
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

    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }
}
