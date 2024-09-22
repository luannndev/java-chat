package dev.luan.chat;

import dev.luan.chat.client.Client;
import dev.luan.chat.server.Server;
import dev.luan.chat.utility.GameType;

import java.util.Scanner;

public final class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Would you like to run the server (S) or the client (C)?");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();

        GameType type = line.equalsIgnoreCase("s") ? GameType.SERVER : GameType.CLIENT;

        int port;
        if (type == GameType.SERVER) {
            System.out.println("Please tell us the port you would like your server to bind to?");
            line = scanner.nextLine();
            port = Integer.parseInt(line);
            new Server().start(port);
        }
        else {
            System.out.println("What IP and port would you like to connect to? (separate the IP and the port using a ':' symbol)");
            line = scanner.nextLine();
            if (!line.contains(":")) {
                System.err.println("The IP and the port were not separated using a ':' symbol!");
                return;
            }
            String[] parameters = line.split(":");
            port = Integer.parseInt(parameters[1]);

            System.out.println("What is your username?");
            String username = scanner.nextLine();

            new Client(username).connect(parameters[0], port);
        }
    }

}