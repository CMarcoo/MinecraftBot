package me.thevipershow.minecraftbot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import me.thevipershow.minecraftbot.json.ServerResponse;
import me.thevipershow.minecraftbot.packets.auth.LoginStartPacket;
import me.thevipershow.minecraftbot.packets.auth.LoginSuccessPacket;
import me.thevipershow.minecraftbot.packets.auth.SetCompressionPacket;
import me.thevipershow.minecraftbot.packets.game.JoinGamePacket;
import me.thevipershow.minecraftbot.packets.game.PlayerKeepAlive;
import me.thevipershow.minecraftbot.packets.game.ServerKeepAlive;
import me.thevipershow.minecraftbot.packets.handshake.HandshakePacket;

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
            socket.setSoTimeout(3500); // this sets a connection timeout, the connection will close if the server doesn't respond for more than 3.5 sec
            socket.setTcpNoDelay(true); // Enable Nagle's algorithm
            socket.setTrafficClass(18); // no idea what this does

            println(String.format("Connected to destination Socket: [%s, %d]", address, port));

            final DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream()); // client -> server
            final DataInputStream dataInputStream = new DataInputStream(socket.getInputStream()); // server -> client

            final HandshakePacket handshake = new HandshakePacket(0x2F, address, port, HandshakePacket.HandshakeNextState.LOGIN);
            final LoginStartPacket loginStart = new LoginStartPacket("LegitPlayer2");
            final LoginSuccessPacket loginSuccessPacket = new LoginSuccessPacket();
            final JoinGamePacket joinGamePacket = new JoinGamePacket();

            handshake.sendPacket(dataOutputStream);
            loginStart.sendPacket(dataOutputStream);

            loginSuccessPacket.readData(dataInputStream);
            joinGamePacket.readData(dataInputStream);

            println(joinGamePacket.toString());

            dataOutputStream.close(); // closing all connections
            dataInputStream.close();
            socket.close();
            println("Closed all connections");

        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
