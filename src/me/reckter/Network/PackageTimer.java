package me.reckter.Network;

import me.reckter.Network.Packages.BasePackage;

import java.util.ArrayList;

/**
 * Created by mediacenter on 03.01.14.
 */
public class PackageTimer { //TODO implement the function that calculates the wait time between to packages from the rtt and the jitter

    protected ArrayList<Long[]> packages;
    protected float rtt;
    protected int[] jitter;


    public PackageTimer() {
        this.packages = new ArrayList<Long[]>();
        this.rtt = 20;
        this.jitter = new int[32];
    }

    public void addPackage(int packageID){
        Long[] tmp = new Long[2];
        tmp[0] = (long) packageID;
        tmp[1] = System.currentTimeMillis();
        packages.add(tmp);
        ArrayList<Long[]> packagesToRemove = new ArrayList<Long[]>();

        for(Long[] pack: packages){
            if(pack[0] < packageID - 32 || pack[0] > packageID + Integer.MAX_VALUE / 2 ){
                packagesToRemove.add(pack);
            }
        }
        packages.removeAll(packagesToRemove);
    }

    public void receivedAckForPackage(int packageID){
        ArrayList<Long[]> packagesToRemove = new ArrayList<Long[]>();
        for(Long[] pack: packages){
            if(pack[0] == packageID){
                packagesToRemove.add(pack);
                pushNewRtt((int) (System.currentTimeMillis() - pack[1]));
            }
        }
    }

    public void receivedAck(BasePackage pack){
        int ack = pack.getAck();
        boolean[] bitfield = pack.getAckBitfield();
        receivedAckForPackage(ack);
        for(int i = 0; i < bitfield.length; i++){
            if(bitfield[i]){
                receivedAckForPackage(ack - (i + 1));
            }
        }
    }

    public void pushNewRtt(int newRtt){
        rtt  += (newRtt - rtt) * 0.1f; //adjusting the rtt 10% in the direction of the most recent rtt
        for(int i = 1; i < jitter.length; i++){
            jitter[i] = jitter[i - 1];
        }
        jitter[0] = newRtt;
    }

    public float getRtt(){
        return rtt;
    }


    /**
     * 20 packets per seconds is a speed of 50ms
     *
     *
     * @return
     */
    public int getSpeed(){
        return 20;
    }

}
