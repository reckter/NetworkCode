package me.reckter.Test;

import me.reckter.Log;
import me.reckter.Network.Connection;
import me.reckter.Network.Network;
import me.reckter.Network.Packages.BasePackage;
import me.reckter.Network.Packages.TestPackage;

import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by mediacenter on 09.01.14.
 */
public class Server {
    static Network net;

    static public void main(String[] args) throws SocketException {
        int sequenz = 0;

        Log.setConsoleLevel(Log.DEBUG);
        net = new Network(16661);
        net.isServer();
        Prime prime = new Prime();
        prime.start();

        while(true){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(Connection con: net.getConnections().values()){
                for(BasePackage pack: con.getUnprocessedPackages()){
                   //Log.info("RTT average: " + con.getTimer().getRtt() + " last: " + con.getTimer().getJitter()[0]);
                }
            }
        }
    }

    static class Prime extends Thread {

        ArrayList<Integer> primes;
        public void run() {
            primes = new ArrayList<Integer>();
            primes.add(2);
            int i = 3;
            while(true) {
                boolean isPrime = true;
                for(Integer prime: primes) {
                    if (i % prime == 0) {
                        isPrime = false;
                        break;
                    }
                }
                if(isPrime) {
                    primes.add(i);
                    for(Connection con: net.getConnections().values()) {
                        TestPackage testPackage = new TestPackage(net);
                        testPackage.setTestData(ByteBuffer.allocate(4).putInt(i));

                        con.send(testPackage);
                    }
                }
                i += 2;
            }
        }
    }

}
