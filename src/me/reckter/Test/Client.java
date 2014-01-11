package me.reckter.Test;

import me.reckter.Log;
import me.reckter.Network.Network;
import me.reckter.Network.Packages.BasePackage;
import me.reckter.Network.Packages.InputPackage;
import me.reckter.Network.Packages.TestPackage;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by mediacenter on 09.01.14.
 */
public class Client {

    static public void main(String[] args) throws SocketException, UnknownHostException {

        Network net = new Network(16662);
        Log.setConsoleLevel(Log.DEBUG);

        byte[] data = new byte[20];
        int sequenz = 0;
        while(true){
            sequenz++;
            data = randomData(data);

            TestPackage test = new TestPackage(net);
            ArrayList<BasePackage> tmp = new ArrayList<BasePackage>();

            test.createHeader(sequenz,1, net.getLastPackages());

            test.setTestData(ByteBuffer.allocate(data.length).put(data));
            test.setReceiver(InetAddress.getLocalHost());

            byte[] header = test.getHeader().array();
            byte[] buffer = test.getBuffer().array();
            String out = Util.printByteArray(header) + "  |";
            out += Util.printByteArray(buffer);

            Log.info("sending data: " + out);


            net.send(test, InetAddress.getLocalHost(),16661);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
