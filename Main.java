public class Main {
    public static void main(String[] args) {
        GameServer server = new GameServer();
        server.start();
        String serverAddress = "localhost";
        GameClient client = new GameClient(serverAddress);
        client.start();
        client.ping();
    }
}