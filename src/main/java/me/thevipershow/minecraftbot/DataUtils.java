package me.thevipershow.minecraftbot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import me.thevipershow.minecraftbot.packets.AbstractPacket;

public final class DataUtils {

    public static void writeString(final DataOutputStream dos, final String s) throws IOException {
        final byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        final int byteLength = bytes.length;
        if (byteLength > Short.MAX_VALUE)
            throw new IOException(String.format("Cannot write string is too big (%d) bytes, max (%d).", byteLength, Short.MAX_VALUE));
        else {
            writeVarInt(dos, byteLength);
            dos.write(bytes, 0, bytes.length);
        }
    }

    public static void checkPacket(final int expectedID, final int actualId, final int length, final boolean emptyPacket) throws IOException {
        if (actualId == -1) {
            throw new IOException("Premature end of stream.");
        }

        if (actualId != expectedID) { //we want a status response
            throw new IOException("Invalid packetID");
        }

        if (length == -1) {
            throw new IOException("Premature end of stream.");
        }

        if (emptyPacket)
            if (length == 0)
                throw new IOException("This packet should not be empty.");
    }

    public static String readString(final DataInputStream dis) throws IOException {
        final int length = readVarInt(dis);
        final byte[] bytes = dis.readNBytes(length);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static int readVarInt(final DataInputStream in) throws IOException {
        int i = 0;
        int j = 0;
        while (true) {
            int k = in.readByte();
            i |= (k & 0x7F) << j++ * 7;
            if (j > 5) throw new RuntimeException("VarInt too big");
            if ((k & 0x80) != 128) break;
        }
        return i;
    }

    public static void writeVarInt(final DataOutputStream out, int paramInt) throws IOException {
        while (true) {
            if ((paramInt & 0xFFFFFF80) == 0) { // 0xFFFFFF80 is 0x80 full
                out.writeByte(paramInt);
                return;
            }
            out.writeByte(paramInt & 0x7F | 0x80); // 0x7F is 127 - 0x80 is 128
            paramInt >>>= 7;
        }
    }
}
