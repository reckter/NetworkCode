package me.reckter.Test;

import me.reckter.Log;
import me.reckter.Network.Connection;
import me.reckter.Network.Network;
import me.reckter.Network.Packages.BasePackage;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * Created by mediacenter on 09.01.14.
 */
public class Client {

    static public void main(String[] args) throws SocketException, UnknownHostException {

        int port = 16663;
        Network net = new Network(port);
        Log.setConsoleLevel(Log.DEBUG);

        byte[] data = new byte[20];

        net.connectToServer(InetAddress.getLocalHost());

        BasePackage back = new BasePackage(net);
        back.setBytes(ByteBuffer.allocate(20).put(randomData(data)));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //net.getConnections().get(0).send(back);


        while(true){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(Connection con: net.getConnections().values()){
                for(BasePackage pack: con.getUnprocessedPackages()) {
                    if(pack.getPackageType() == 3) {
                        Log.info("prime: " + pack.getBytes().getInt(0));
                    }
                   // Log.info("RTT average: " + con.getTimer().getRtt() + " last: " + con.getTimer().getJitter()[0]);
                }
            }
        }
    }

    static public byte[] randomData(byte[] in){
        byte[] ret = new byte[in.length];
        for(int i = 0; i < ret.length; i++){
            ret[i] = (byte) (Math.random() * Byte.MAX_VALUE);

        }
        return ret;
    }


}
