package me.reckter.Network;

import me.reckter.Log;
import me.reckter.Network.Packages.BasePackage;
import me.reckter.Network.Packages.ClientIdPackage;
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

    protected int sequenz;

    protected PackageTimer timer;

    protected ArrayList<BasePackage> receivedPackage;
    protected ArrayList<BasePackage> unprocessedPackages;


    public Connection(Network network, InetAddress with,int port) {
        this.network = network;
        this.with = with;
        this.port = port;

        receivedPackage = new ArrayList<BasePackage>();
        unprocessedPackages = new ArrayList<BasePackage>();
        sequenz = 0;
        timer = new PackageTimer();
    }

    public void send(BasePackage pack){
        pack.createHeader(++sequenz, network.getClientId(), receivedPackage);
        timer.addPackage(pack.getSequenz());
        network.send(pack, with, port);
    }

    public void receivePackage(BasePackage pack){
        timer.receivedAck(pack);
        receivedPackage.add(pack);
        if(!(pack instanceof ClientIdPackage)){
            unprocessedPackages.add(pack);
        }
    }

    public void open(int clientPort){
        ClientIdPackage pack = new ClientIdPackage(network);
        pack.setNewClientId(-1);
        pack.setPort(clientPort);
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

    public PackageTimer getTimer() {
        return timer;
    }

}
