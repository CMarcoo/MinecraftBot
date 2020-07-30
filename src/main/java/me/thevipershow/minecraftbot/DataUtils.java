package me.thevipershow.minecraftbot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import me.thevipershow.minecraftbot.packets.AbstractPacket;
import me.thevipershow.minecraftbot.packets.handshake.HandshakePacket;
import me.thevipershow.minecraftbot.packets.handshake.PingPacket;
import me.thevipershow.minecraftbot.packets.handshake.RequestPacket;
import me.thevipershow.minecraftbot.packets.handshake.ResponsePacket;

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
            throw new IOException("Invalid packetID, received " + actualId);
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

    public static String readUnformattedString(final DataInputStream dis) throws IOException {
        final StringBuffer stringBuffer = new StringBuffer();
        int read;
        while ((read = dis.read()) != -1)
            stringBuffer.append((char) read);
        return stringBuffer.toString();
    }

    public static String serverLookup(final DataOutputStream dataOutputStream, final DataInputStream dataInputStream, final String address, final int port) throws IOException {
        // Initializing the HandshakePacket (https://wiki.vg/Protocol#Handshake)
        // This packet is the first packet and will be sent to the server, telling him to start a login phase.
        final HandshakePacket handshake = new HandshakePacket(0x2F, address, port, HandshakePacket.HandshakeNextState.STATUS);  // 0x2F is 47, the protocol for 1.8-1.8.9
        handshake.sendPacket(dataOutputStream); // sending the packet to the server

        // Initializing the RequestPacket (https://wiki.vg/Protocol#Request)
        // This is an empty packet with id 0x00 and should be sent immediately after the Handshake.
        final RequestPacket request = new RequestPacket();
        request.sendPacket(dataOutputStream); // sending the packet to the server

        // Initializing the PingPacket (https://wiki.vg/Protocol#Ping)
        // This packet is only used by "Notchian" servers and should be sent right after a request packet
        // In order for the server to send back a response.
        final PingPacket pingPacket = new PingPacket();
        pingPacket.sendPacket(dataOutputStream);

        // Initializing a response packet (https://wiki.vg/Protocol#Response)
        // We should receive this packet from the server which will contain server information.
        final ResponsePacket responsePacket = new ResponsePacket();
        responsePacket.readData(dataInputStream);

        return responsePacket.getResponse();
    }

    public static String formatJson(final String json) {
        final StringBuilder output = new StringBuilder();
        final char[] chars = json.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            final char currentChar = chars[i];
            if (i + 1 < chars.length) {
                final char nextChar = chars[i + 1];
                output.append(currentChar);
                if (currentChar == '{') {
                    output.append('\n');
                } else if (currentChar == '[' && nextChar == '{') {
                    output.append('\n');
                } else if (currentChar == '}' && nextChar == ',') {
                    output.append('\n');
                } else if (currentChar == '"' && (nextChar == '{' || nextChar == '}')) {
                    output.append('\n');
                } else if (currentChar == ']' && nextChar == '}') {
                    output.append('\n');
                } else if (currentChar == ',') {
                    output.append('\n');
                }
            } else {
                break;
            }
        }
        return output.toString();
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

    public static int readVarIntFromBytes(final byte[] bytes) {
        int i = 0;
        int j = 0;
        for (byte b : bytes) {
            i |= (b & 0x7F) << j++ * 7;
            if (j > 5) throw new RuntimeException("VarInt too big");
            if ((b & 0x80) != 128) break;
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
