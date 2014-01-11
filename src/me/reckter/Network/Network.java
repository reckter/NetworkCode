package me.reckter.Network;

import me.reckter.Log;
import me.reckter.Network.Packages.BasePackage;

import java.io.IOException;
import java.net.*;
import java.nio.BufferOverflowException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by mediacenter on 01.01.14.
 */
public class Network {
    protected byte sequenz;
    protected ArrayList<BasePackage> lastPackages;
    protected Network net;

    //protected BaseLevel level;
    protected DatagramSocket socket;

    protected ArrayList<BasePackage> inputs;
    protected Lock inputLock = new ReentrantLock();

    protected NetworkListener listener;

    protected PackageTimer timer;


    public Network(int port) throws SocketException {
      //  this.level = level;
        this.net = this;
        this.sequenz = 0;
        this.socket = new DatagramSocket(port);
        this.inputs = new ArrayList<BasePackage>();

        this.timer = new PackageTimer();
        this.lastPackages = new ArrayList<BasePackage>();
        this.listener = new NetworkListener();
        this.listener.start();

    }


    public void send(BasePackage pack, InetAddress address, int port){
        try {
            byte[] header = pack.getHeader().array();
            byte[] buffer = pack.getBuffer().array();
            byte[] data = new byte[header.length + buffer.length];
            for(int i = 0; i < header.length; i++){
                data[i] = header[i];
            }
            for(int i = 0; i < buffer.length; i++){
                data[header.length + i] = buffer[i];
            }

            DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, port);
            socket.send(sendPacket);
            timer.addPackage(pack.getSequenz());

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


    class NetworkListener extends Thread{
        public void run(){
            byte[] inData = new byte[128];
            try {
                socket.setSoTimeout(20);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            while(true){
                try {
                    DatagramPacket receivePacket = new DatagramPacket(inData, inData.length);

                    socket.receive(receivePacket);
                    //socket.setReceiveBufferSize(128);

                    //TODO implement package exepting logick here
                    inputLock.lock();
                    try{
                        BasePackage pack = new BasePackage(net);
                        pack.parsePackage(receivePacket.getData());

                        pack.setSender(receivePacket.getAddress());
                        timer.receivedAck(pack);

                        inputs.add(pack);
                        lastPackages.add(pack);

                    } catch(BufferOverflowException e){
                        Log.error("BufferOverflowException in the Listener: " + e.getLocalizedMessage());
                    } finally {
                        inputLock.unlock();
                    }

                } catch (SocketTimeoutException e){

                } catch (IOException e) {
                    Log.error("received IOExpetion while waiting for a Package (ignoring that Package): " + e.getMessage());
                }

            }
        }
    }



    public ArrayList<BasePackage> getInputs() {
        ArrayList<BasePackage> ret = new ArrayList<BasePackage>();
        inputLock.lock();
        try {
            ret = inputs;
            inputs = new ArrayList<BasePackage>();
        } finally {
            inputLock.unlock();
        }
        return ret;
    }

    public void setInputs(ArrayList<BasePackage> inputs) {
        inputLock.lock();
        try {
            this.inputs = inputs;
        } finally {
            inputLock.unlock();
        }
    }

    public ArrayList<BasePackage> getLastPackages() {
        return lastPackages;
    }

    public void setLastPackages(ArrayList<BasePackage> lastPackages) {
        this.lastPackages = lastPackages;
    }
}
