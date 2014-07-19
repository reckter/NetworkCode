package me.reckter.Network;

import me.reckter.Log;
import me.reckter.Network.Packages.BasePackage;
import me.reckter.Network.Packages.ClientIdPackage;
import me.reckter.Network.Packages.KeepAlivePackage;
import me.reckter.Test.Util;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by reckter on 1/13/14.
 */
public class Connection {
    protected Network network;

    protected InetAddress with;
    protected int port;
    protected int clientID;

    protected int sequenz;

    protected long lastPackageSend;
    protected long lastPackageReceived;

    protected boolean isAlive;

    protected ArrayList<BasePackage> receivedPackage;
    protected ArrayList<BasePackage> unprocessedPackages;


    public Connection(Network network, InetAddress with,int port, int clientID) {
        this.network = network;
        this.with = with;
        this.port = port;
        this.clientID = clientID;

        receivedPackage = new ArrayList<BasePackage>();
        unprocessedPackages = new ArrayList<BasePackage>();
        sequenz = 0;
        isAlive = true;
    }

    public void send(BasePackage pack){
        ArrayList<BasePackage> receivedPackageTmp = new ArrayList<BasePackage>();
        receivedPackageTmp.addAll(receivedPackage);
        pack.createHeader(++sequenz, network.getClientId(), receivedPackageTmp);
        lastPackageSend = System.currentTimeMillis();
        network.send(pack, with, port);
    }

    public void receivePackage(BasePackage pack){
        receivedPackage.add(pack);
        if(!(pack instanceof ClientIdPackage)){
            unprocessedPackages.add(pack);
        }
        lastPackageReceived = System.currentTimeMillis();

    }

    public void open(int clientPort){
        ClientIdPackage pack = new ClientIdPackage(network);
        pack.setNewClientId(-1);
        pack.setPort(clientPort);

        lastPackageReceived = System.currentTimeMillis();

        send(pack);

        byte[] header = pack.getHeader().array();
        byte[] buffer = pack.getBuffer().array();

        String out = Util.printByteArray(header) + "  |";
        out += Util.printByteArray(buffer);
        Log.info("sending opening package: " + out);
    }

    public  ArrayList<BasePackage> getUnprocessedPackages(){
        ArrayList<BasePackage> tmp = unprocessedPackages;
        unprocessedPackages = new ArrayList<BasePackage>();
        return tmp;
    }

    public void keepAlive(){
        if(System.currentTimeMillis() - lastPackageSend > 100){
            KeepAlivePackage pack = new KeepAlivePackage(network);
            send(pack);
        }
        if(System.currentTimeMillis() - lastPackageReceived > 1000) {
            isAlive = false;
        }
    }



    public InetAddress getWith() {
        return with;
    }

    public void setWith(InetAddress with) {
        this.with = with;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSequenz() {
        return sequenz;
    }

    public void setSequenz(int sequenz) {
        this.sequenz = sequenz;
    }

    public ArrayList<BasePackage> getReceivedPackage() {
        return receivedPackage;
    }

    public void setReceivedPackage(ArrayList<BasePackage> receivedPackage) {
        this.receivedPackage = receivedPackage;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }
}
