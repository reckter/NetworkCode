package me.reckter.Test;

import me.reckter.Log;
import me.reckter.Network.Connection;
import me.reckter.Network.Network;
import me.reckter.Network.Packages.BasePackage;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by mediacenter on 09.01.14.
 */
public class Server {

    static public void main(String[] args) throws SocketException {
        int sequenz = 0;

        Log.setConsoleLevel(Log.DEBUG);
        Network net = new Network(16661);
        net.isServer();
        while(true){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(Connection con: net.getConnections().values()){
                for(BasePackage pack: con.getUnprocessedPackages()){

                    BasePackage back = new BasePackage(net);

                    back.setBytes(pack.getBytes());

                    con.send(back);
                    Log.info("RTT average: " + con.getTimer().getRtt() + " last: " + con.getTimer().getJitter()[0]);
                }
            }
        }
    }

}
