package me.vrekt.server;

/**
 * Created by Vrekt on 8/7/2017.
 */
public class ServerInitializer {

    public static void main(String[] args) {
        // improve this later like asking for ports, server limits etc.

        Server server = new Server(1569);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.stopServer()));

        server.startServer();

    }

}
