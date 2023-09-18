package src;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Objects;

class Receiver {
    int frequency;
    InetAddress ipAddress;
    int port;

    public Receiver(int frequency, InetAddress ipAddress, int port) {
        this.frequency = frequency;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public String toString() {
        return "Receiver{" +
                "frequency=" + frequency +
                ", ipAddress=" + ipAddress +
                ", port=" + port +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receiver receiver = (Receiver) o;
        return Objects.equals(ipAddress, receiver.ipAddress);
    }

}

public class GameServer extends Thread {
    private DatagramSocket socket;
    private ArrayList<Receiver> receivers = new ArrayList<>();

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
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        int packetType = Integer.parseInt(message.substring(0, 2));
        switch (packetType) {
            case 01:
                int frequency = Integer.parseInt(message.substring(2, 4));
                System.out.println("CONNECT REQUEST FROM [" + address.getHostAddress() + ":" + port + "] ON FREQUENCY " + frequency);
                byte[] response = ("01" + frequency).getBytes();
                receivers.add(new Receiver(frequency, address, port));
                System.out.println(receivers);
                sendData(response, address, port);
                break;
            case 02:
                int disconnectFrequency = Integer.parseInt(message.substring(2, 4));
                System.out.println("DISCONNECT REQUEST FROM [" + address.getHostAddress() + ":" + port + "] FROM FREQUENCY " + disconnectFrequency);
                receivers.remove(new Receiver(disconnectFrequency, address, port));
                System.out.println(receivers);
                break;
            default:
                System.out.println("FROM [" + address.getHostAddress() + ":" + port + "]> " + message);
                break;
        }
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendDataToAllClients(byte[] data, int frequency) {
        for (Receiver receiver : receivers) {
            if (receiver.frequency == frequency) {
                sendData(data, receiver.ipAddress, receiver.port);
            }
        }
    }
}