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
import me.thevipershow.minecraftbot.packets.handshake.RequestPacket;

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

    public static void main(final String[] args) {
        final ArgumentsHandler argsHandler = new ArgumentsHandler(args); // A simple object to parse the jvm arguments.
        final String address = argsHandler.getAddress();
        final int port = argsHandler.getPort();

        final int randomPort = 25999;

        try {

            final InetAddress dstAddress = InetAddress.getByName(address);
            final Socket socket = new Socket();
            socket.setSoTimeout(3500); // this sets a connection timeout, the connection will close if the server doesn't respond for more than 3.5 sec
            socket.setTcpNoDelay(true); // Enable Nagle's algorithm
            socket.setTrafficClass(18); // settings traffic class.
            final InetSocketAddress inetSocketAddress = new InetSocketAddress(dstAddress, port); // The server we are going to connect to
            socket.connect(inetSocketAddress); // connecting

            println(String.format("Connected to destination Socket: [%s, %d]", address, port));

            final DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            final DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());


            // Initializing the HandshakePacket (https://wiki.vg/Protocol#Handshake)
            // This packet is the first packet and will be sent to the server, telling him to start a login phase.
            final HandshakePacket handshake = new HandshakePacket(0x2F, address, port, HandshakePacket.HandshakeNextState.LOGIN);  // 0x2F is 47, the protocol for 1.8-1.8.9
            handshake.sendPacket(dataOutputStream); // sending the packet to the server

            // Initializing the RequestPacket (https://wiki.vg/Protocol#Request)
            // This is an empty packet with id 0x00 and should be sent immediately after the Handshake.
            final RequestPacket request = new RequestPacket();
            request.sendPacket(dataOutputStream); // sending the packet to the server

            // Initializing the LoginStartPacket (https://wiki.vg/Protocol#Login_Start)
            // This makes the server start the authentication process, we will skip encryption as we're targeting offline-mode.
            final LoginStartPacket loginStart = new LoginStartPacket("Skeppy");
            loginStart.sendPacket(dataOutputStream); // sending the packet to the server

            // We'll start reading packets , if everything has worked we should receive a LoginSuccessPacket (https://wiki.vg/Protocol#Login_Success)
            final int packetLength = DataUtils.readVarInt(dataInputStream); // we first read (Length of packet data + length of the packet ID)
            final int packetID = DataUtils.readVarInt(dataInputStream); // Then we read the ID of the packet (VarInt).

            println(packetLength + " " + packetID);




            dataOutputStream.close(); // closing all connections
            dataInputStream.close();
            socket.close();

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
