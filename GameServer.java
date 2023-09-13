import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class GameServer extends Thread {
    private DatagramSocket socket;
    public GameServer() {
        try {
            this.socket = new DatagramSocket(1331);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
//            String message = new String(packet.getData()).trim();
//            System.out.println("CLIENT [" + packet.getAddress().getHostAddress() + ":" + packet.getPort() + "]> " + message);
//            if (message.equalsIgnoreCase("ping")) {
//                sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
//            }
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        System.out.println("FROM [" + address.getHostAddress() + ":" + port + "]> " + message);
        if(message.equals("PING")){
            byte[] pong = "PONG".getBytes();
            sendData(pong,address,port);
        }
//        Packet.PacketTypes type = Packet.lookupPacket(Integer.parseInt(message.substring(0, 2)));
//        switch (type) {
//            case INVALID:
//                break;
//            case LOGIN:
//                Packet00Login packet = new Packet00Login(data);
//                System.out.println("[" + address.getHostAddress() + ":" + port + "]" + packet.getUsername() + " has connected...");
//                PlayerMP player = new PlayerMP(Main.gamePanel, Main.gamePanel.keyHandler, address, port);
//                this.connectedPlayers.add(player);
//                break;
//            case DISCONNECT:
//                break;
//            default:
//                break;
//        }
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendDataToAllClients(byte[] data) {
    }
}