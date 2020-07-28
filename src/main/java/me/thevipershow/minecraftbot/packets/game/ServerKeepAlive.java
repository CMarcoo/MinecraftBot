package me.thevipershow.minecraftbot.packets.game;

import java.io.DataOutputStream;
import java.io.IOException;
import me.thevipershow.minecraftbot.DataUtils;
import me.thevipershow.minecraftbot.packets.AbstractPacket;
import me.thevipershow.minecraftbot.packets.PacketType;

public class ServerKeepAlive extends AbstractPacket {
    private final int keepAliveID;

    public ServerKeepAlive(final int keepAliveID) {
        super(0x00, PacketType.TO_CLIENT);
        this.keepAliveID = keepAliveID;
    }

    public ServerKeepAlive(final PlayerKeepAlive playerKeepAlive) {
        this(playerKeepAlive.getKeepAliveID());
    }

    public long getKeepAliveID() {
        return keepAliveID;
    }

    @Override
    public void writeData() {
        try {
            DataUtils.writeVarInt(dataOutputStream, keepAliveID);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
