package me.reckter.Network;

import me.reckter.Log;
import me.reckter.Network.Packages.BasePackage;
import me.reckter.Network.Packages.ClientIdPackage;
import me.reckter.Test.Util;

import java.io.IOException;
import java.net.*;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by mediacenter on 01.01.14.
 */
public class Network {
    protected int sequenz;

    protected Network net;

    protected HashMap<Integer, Connection> connections; //clientId's divide every connection
    protected int maxClients;
    protected boolean isClient = true;

    protected int clientId;

    protected DatagramSocket socket;
    protected int port;

    protected NetworkListener listener;
    protected NetworkKeepAliveThread keepAlive;



    public Network(int port) throws SocketException {
        this.net = this;
        this.sequenz = 0;
        this.socket = new DatagramSocket(port);
        this.port = port;

        this.connections = new HashMap<Integer, Connection>();
        this.maxClients = 0;

        this.listener = new NetworkListener();
        this.listener.start();

        this.keepAlive = new NetworkKeepAliveThread();
        this.keepAlive.start();

    }

    public void isServer(){
        isClient = false;
    }


    public void send(BasePackage pack, InetAddress address, int port){
        try {
            byte[] data = pack.getDataToSend();

            DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, port);
            socket.send(sendPacket);

            byte[] headerTmp = pack.getHeader().array();
            byte[] bufferTmp = pack.getBuffer().array();

            String out = Util.printByteArray(headerTmp) + "  |";
            out += Util.printByteArray(bufferTmp);
            Log.info("sending to " + address + ":" + port + ": " + out);

        } catch (IOException e) {
            Log.error("got an IOExepction sending a package (inputs: " + address.getHostAddress() + " ; " + port + "): " + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * checks if the package can be excepted (isn't a dublicate)
     * @param sequenzToCheck
     * @return
     */
    public boolean isMoreRecent(int sequenzToCheck, int currentSequenz){
        int max_sequence = Integer.MAX_VALUE;

        return ( sequenzToCheck > currentSequenz ) && ( sequenzToCheck - currentSequenz <= max_sequence/2 ) ||
               ( currentSequenz > sequenzToCheck ) && ( currentSequenz - sequenzToCheck > max_sequence/2  );

    }

    public void keepConnectionsAlive(){

        Collection<Connection> cons = connections.values();
        for(Connection con : cons){
            con.keepAlive();
            if(!con.isAlive()){
                connections.remove(con.getClientID());
                Log.info("Lost connection to (ID: " + con.clientID + " IP: " + con.getWith().getHostAddress() + ")[time out]");
            }
        }
    }

    class NetworkKeepAliveThread extends Thread{
        public void run(){

            while (true){
                keepConnectionsAlive();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected boolean packageIsAccepted(BasePackage pack) {
        boolean protocolIsRight = pack.getProtocolID() == pack.getCodedProtocolID();
        return protocolIsRight;
    }

    class NetworkListener extends Thread{
        public void run(){
            try {
                socket.setSoTimeout(20);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            while(true){
                try {
                    byte[] inData = new byte[128];
                    DatagramPacket receivePacket = new DatagramPacket(inData, inData.length);

                    socket.receive(receivePacket);
                    //socket.setReceiveBufferSize(128);

                    // TODO make this code breatier!
                    BasePackage pack = new BasePackage(net);
                    pack.parsePackage(receivePacket.getData());

                    pack.setSender(receivePacket.getAddress());
                    if(!packageIsAccepted(pack)){
                        continue;
                    }

                    //only here to print it out
                    byte[] header = pack.getHeader().array();
                    byte[] buffer = pack.getBuffer().array();

                    String out = Util.printByteArray(header) + "  |";
                    out += Util.printByteArray(buffer);
                    Log.info("Received " + pack.getSender() + ":" + receivePacket.getPort() + ": " + out);


                    if(pack.getPackageType() == 1) { //So this is either a client asking for a clientID or the server giving out a clientID
                        ClientIdPackage clientIdPackage = new ClientIdPackage(net);
                        clientIdPackage.parsePackage(receivePacket.getData());
                        clientIdPackage.setSender(receivePacket.getAddress());

                        if(clientIdPackage.getNewClientId() == -1){ //so this is a client asking for a clientID; so we send him a new clientId
                            connections.put(++maxClients, new Connection(net, receivePacket.getAddress(), clientIdPackage.getPort(), maxClients));

                            ClientIdPackage responds = new ClientIdPackage(net);
                            responds.setNewClientId(maxClients);
                            Log.info("new Client: ID: " + maxClients + " IP: " + receivePacket.getAddress() + ":" + clientIdPackage.getPort());



                            connections.get(maxClients).receivePackage(clientIdPackage);
                            connections.get(maxClients).send(responds);

                        } else if(isClient && clientIdPackage.getClientID() == 0){ //so this is a response from the server (and to make sure, that this isn't a client trying to hack the server we asked for isClient
                            clientId = clientIdPackage.getNewClientId();
                            Log.info("ID: " + clientId);

                            connections.get(0).receivePackage(clientIdPackage);
                        }
                    } else {
                        int tmp = pack.getClientID();
                        if(!connections.keySet().contains(tmp)){ //TODO bug here!
                            Log.error("received invalid clientID and it's not a connection opening Package! ignoring it!");
                            continue;
                        }

                        connections.get(pack.getClientID()).receivePackage(pack);
                    }



                } catch (SocketTimeoutException e){

                } catch (IOException e) {
                    Log.error("received IOExpetion while waiting for a Package (ignoring that Package): " + e.getMessage());
                }

            }
        }
    }

    public void connectToServer(InetAddress with){
        Connection connection = new Connection(this, with, 16661, 0); //TODO remove hardcoded server port
        connections.put(0, connection);
        connection.open(port);
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public HashMap<Integer, Connection> getConnections() {
        return connections;
    }

    public void setConnections(HashMap<Integer, Connection> connections) {
        this.connections = connections;
    }
}
