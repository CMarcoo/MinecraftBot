package me.thevipershow.minecraftbot;

public final class ArgumentsHandler {
    private final String address;
    private final int port;

    public ArgumentsHandler(final String[] baseArgs) {
        if (baseArgs.length == 0) {
            address = "127.0.0.1";
            port = 25565;
            return;
        }
        address = baseArgs[0];
        port = Integer.parseInt(baseArgs[1]);
    }

    public final String getAddress() {
        return address;
    }

    public final int getPort() {
        return port;
    }
}
