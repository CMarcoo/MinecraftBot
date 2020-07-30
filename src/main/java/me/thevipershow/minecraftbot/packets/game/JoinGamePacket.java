package me.thevipershow.minecraftbot.packets.game;

import java.io.DataInputStream;
import java.io.IOException;
import me.thevipershow.minecraftbot.DataUtils;
import me.thevipershow.minecraftbot.packets.AbstractPacket;
import me.thevipershow.minecraftbot.packets.PacketType;

public final class JoinGamePacket extends AbstractPacket {
    public JoinGamePacket() {
        super(0x01, PacketType.TO_CLIENT);
    }

    private int entityID;
    private byte gamemode;
    private byte dimension;
    private byte difficulty;
    private byte maxPlayers;
    private String levelType;
    private boolean reducedDebugInfo;

    @Override
    public void readData(DataInputStream dis) {
        try {
            final int length = DataUtils.readVarInt(dis);
            final int id = DataUtils.readVarInt(dis);
            DataUtils.checkPacket(getId(), id, length, false);
            entityID = dis.readInt();
            gamemode = dis.readByte();
            dimension = dis.readByte();
            difficulty = dis.readByte();
            maxPlayers = dis.readByte();
            levelType = DataUtils.readString(dis);
            reducedDebugInfo = dis.readBoolean();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public int getEntityID() {
        return entityID;
    }

    public byte getGamemode() {
        return gamemode;
    }

    public byte getDimension() {
        return dimension;
    }

    public byte getDifficulty() {
        return difficulty;
    }

    public byte getMaxPlayers() {
        return maxPlayers;
    }

    public String getLevelType() {
        return levelType;
    }

    public boolean isReducedDebugInfo() {
        return reducedDebugInfo;
    }

    @Override
    public String toString() {
        return "JoinGamePacket{" +
                "entityID=" + entityID +
                ", gamemode=" + gamemode +
                ", dimension=" + dimension +
                ", difficulty=" + difficulty +
                ", maxPlayers=" + maxPlayers +
                ", levelType='" + levelType + '\'' +
                ", reducedDebugInfo=" + reducedDebugInfo +
                '}';
    }
}
