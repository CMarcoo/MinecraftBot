package me.thevipershow.minecraftbot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;
import me.thevipershow.minecraftbot.packets.auth.LoginStartPacket;
import me.thevipershow.minecraftbot.packets.auth.LoginSuccessPacket;
import me.thevipershow.minecraftbot.packets.handshake.HandshakePacket;
import me.thevipershow.minecraftbot.packets.handshake.PingPacket;

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

            final HandshakePacket handshake = new HandshakePacket(47, argsHandler.getAddress(), argsHandler.getPort(), HandshakePacket.HandshakeNextState.LOGIN);
            final PingPacket pingPacket = new PingPacket();

            handshake.sendPacket(dos);

            pingPacket.sendPacket(dos);

            final LoginStartPacket loginStartPacket = new LoginStartPacket("CyberNoob");

            println("Sending login start packet:");
            loginStartPacket.sendPacket(dos);

            final int nextPacket = DataUtils.readVarInt(dis);
            if (nextPacket == 0x02) {

                println("Received LoginSuccessPacket.");
                final LoginSuccessPacket loginSuccessPacket = new LoginSuccessPacket();
                loginSuccessPacket.readData(dis);

                Thread.sleep(100);
            } else {
                println(String.format("Received unexpected packet with ID: %d", nextPacket));
            }

            dos.close();
            dis.close();
            socket.close();

        } catch (final IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
