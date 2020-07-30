package me.thevipershow.minecraftbot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import me.thevipershow.minecraftbot.json.ServerResponse;
import me.thevipershow.minecraftbot.packets.handshake.HandshakePacket;
import me.thevipershow.minecraftbot.packets.handshake.PingPacket;
import me.thevipershow.minecraftbot.packets.handshake.RequestPacket;
import me.thevipershow.minecraftbot.packets.handshake.ResponsePacket;

/**
 * @author TheViperShow
 * @version 1.0.0-SNAPSHOT
 */
public final class Main {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(ServerResponse.class, ServerResponse.responseDeserializer).create();

    public static void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException e) {
        }
    }

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
            final Socket socket = new Socket(dstAddress, port);
            socket.setSoTimeout(5000); // this sets a connection timeout, the connection will close if the server doesn't respond for more than 3.5 sec
            socket.setTcpNoDelay(true); // Enable Nagle's algorithm
            socket.setTrafficClass(18); // no idea what this does

            println(String.format("Connected to destination Socket: [%s, %d]", address, port));

            final DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream()); // client -> server
            final DataInputStream dataInputStream = new DataInputStream(socket.getInputStream()); // server -> client

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

            println(DataUtils.formatJson(responsePacket.getResponse())); // printing the result

            dataOutputStream.close(); // closing all connections
            dataInputStream.close();
            socket.close();

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
