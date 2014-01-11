package me.reckter.Test;

import me.reckter.Log;
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
        while(true){
            ArrayList<BasePackage> inputs = net.getInputs();
            if(inputs.size() > 0){
                Log.info("Received " + inputs.size() + " packages:");
                for(BasePackage pack: inputs){

                    byte[] header = pack.getHeader().array();
                    byte[] buffer = pack.getBuffer().array();

                    String out = Util.printByteArray(header) + "  |";
                    out += Util.printByteArray(buffer);
                    Log.info(pack.getSender() + ": " + out);

                    try {
                        pack.createHeader(++sequenz,0,net.getLastPackages());
                        net.send(pack, InetAddress.getLocalHost(), 16662);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
                Log.info("-----------------------------------------------------------------");
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
