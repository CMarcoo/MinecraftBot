package me.thevipershow.minecraftbot;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ThreadLocalRandom;
import me.thevipershow.minecraftbot.packets.AbstractPacket;
import me.thevipershow.minecraftbot.packets.handshake.HandshakePacket;
import me.thevipershow.minecraftbot.packets.handshake.PingPacket;
import me.thevipershow.minecraftbot.packets.handshake.RequestPacket;
import me.thevipershow.minecraftbot.packets.handshake.ResponsePacket;

/**
 * @author TheViperShow
 * @version 1.0.0-SNAPSHOT
 */
public final class Main {

    public static void println(final String s) {
        System.out.println(s);
    }

    public static void println(final String[] s) {
        for (final String s1 : s)
            println(s1);
    }

    private static final ThreadLocalRandom rand = ThreadLocalRandom.current();

    public static void main(final String[] args) {
        final ArgumentsHandler argsHandler = new ArgumentsHandler(args);

        final int randomPort = 25999;

        try {

            final InetAddress dstAddress = InetAddress.getByName(argsHandler.getAddress());
            final Socket socket = new Socket();
            socket.setSoTimeout(3500);
            socket.setTcpNoDelay(true);
            socket.setTrafficClass(18);
            final InetSocketAddress inetSocketAddress = new InetSocketAddress(dstAddress, argsHandler.getPort());
            socket.connect(inetSocketAddress);
            println(String.format("Connected to destination Socket: [%s, %d]", argsHandler.getAddress(), argsHandler.getPort()));

            final DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            final DataInputStream dis = new DataInputStream(socket.getInputStream());

            final HandshakePacket handshake = new HandshakePacket(47, argsHandler.getAddress(), argsHandler.getPort(), HandshakePacket.HandshakeNextState.STATUS);
            final PingPacket pingPacket = new PingPacket();

            //handshake.sendPacket(dos);
            //println("Sent handshake packet.");
            handshake.sendPacket(dos);
            pingPacket.sendPacket(dos);

            final ResponsePacket responsePacket = new ResponsePacket();

            println("Sent request and ping packet.");
            println("Waiting for response packet...");
            responsePacket.readData(dis);
            println("Received response: ");

            println(responsePacket.getResponse());

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
