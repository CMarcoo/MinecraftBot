package me.thevipershow.minecraftbot.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public interface Packet {
    default void writeData(final DataOutputStream dos) {
        throw new UnsupportedOperationException(String.format("Packet of type %s does not support write operations.", getClass().getSimpleName()));
    }

    default void readData(final DataInputStream dis) {
        throw new UnsupportedOperationException(String.format("Packet of type %s does not support read operations.", getClass().getSimpleName()));
    }
}
