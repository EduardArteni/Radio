package src;

import java.io.IOException;
import java.net.*;

public class GameClient extends Thread {
    private InetAddress ipAddress;
    private DatagramSocket socket;

    public GameClient(String ipAddress) {
        try {
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);
        } catch (Exception e) {
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
            String message = new String(data).trim();
            int packetType = Integer.parseInt(message.substring(0, 2));
            switch (packetType) {
                case 01:
                    int frequency = Integer.parseInt(message.substring(2, 4));
                    System.out.println("CONNECTED ON FREQUENCY " + frequency);
                    Main.connected = true;
                    Main.radioFrequency = frequency;
                    break;
                default:
                    System.out.println("FROM [" + packet.getAddress().getHostAddress() + ":" + packet.getPort() + "]> " + new String(packet.getData()).trim());
                    break;
            }
        }
    }

    public void sendData(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
        try {
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void ping() {
        byte[] data = ("00" + Main.radioFrequency).getBytes();
        this.sendData(data);
    }

    public void connect() {
        byte[] data = ("01" + Main.radioFrequency).getBytes();
        this.sendData(data);
    }

    public void disconnect() {
        Main.connected = false;
        byte[] data = ("02" + Main.radioFrequency).getBytes();
        this.sendData(data);
    }
}