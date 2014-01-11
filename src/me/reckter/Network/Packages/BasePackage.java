package me.reckter.Network.Packages;

import me.reckter.Log;
import me.reckter.Network.Network;
import me.reckter.Test.Util;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 *
 * SEE SORCE CODE FOR A READABLE VERSION!
 *
 *  header:
 *
 *  bytes | function
 *   0              protocol ID (int)
 *   1          =|=
 *   2          =|=
 *   3          =|=
 *   4              sequence (int)
 *   5          =|=
 *   6          =|=
 *   7          =|=
 *   8              Client ID (int)
 *   9          =|=
 *   10         =|=
 *   11         =|=
 *   12             ack (int)
 *   13         =|=
 *   14         =|=
 *   15         =|=
 *   16             ack bitfield
 *   17         =|=
 *   18         =|=
 *   19         =|=
 *   20             package type
 *   21         =|=
 *   22         =|=
 *   23         =|=
 *
 *
 *  protocol ID (int)
 *
 *      A unique number for the game and the version number which is made as follows:
 *
 *          12775 + releaseNumber
 *          (I choose this number via random.org)
 *
 *
 *
 *  sequence (int)
 *
 *      an integer that counts up with each package send, uniquiely for each machine, is used in the ack and ack bitfield statement
 *
 *
 *  Client ID (int)
 *
 *      an integer that identifies a client (0 is always the server)
 *
 *
 *  ack (int)
 *
 *      the last received package number (so the sequence number) from the receiver
 *
 *
 *  ack bitfield
 *
 *      each bit in this stands for one package that was send before the ack
 *      so if the 1st bit is set then the package before the ack was received sucsessfully
 *      if the 4th bit is set then the 4th packahe before the ack was received etc,
 *
 *
 *  package type (int)
 *
 *      tells the receiver what the package has to say, this needs to pe specified and checked!
 *
 *
 * @author reckter
 * @version 0.1
 */
public class BasePackage {

    protected final static int GAMEID = 122775;
    protected final static int VERSIONID = 0;

    protected Network network;

    protected InetAddress receiver;
    protected InetAddress sender;

    protected ByteBuffer buffer;
    protected ByteBuffer header;

    protected int type;

    public BasePackage(Network network) {
        this.network = network;
        this.buffer = ByteBuffer.allocate(1);
        this.header = ByteBuffer.allocate(24);
        this.type = 0;
    }

    public void createHeader(int sequenz, int ClientID, ArrayList<BasePackage> oldReceivedPackages){
        header.clear();
        header.putInt(GAMEID + VERSIONID);
        header.putInt(sequenz);
        header.putInt(ClientID);
        if(oldReceivedPackages.size() != 0){
        BasePackage newestPackage = oldReceivedPackages.get(0);
            for(BasePackage pack: oldReceivedPackages){
                if(network.isMoreRecent(pack.getSequenz(), newestPackage.getSequenz())){
                    newestPackage = pack;
                }
            }
            int newestSequenz = newestPackage.getSequenz();

            header.putInt(newestSequenz);
            String bitfield = "";
            for(int i = 0; i < 32; i++){
                bitfield += "0";
            }

            for(BasePackage pack: oldReceivedPackages){
                if(sequenz - pack.getSequenz() < 32){
                    int packSequenz = pack.getSequenz();
                    int diff = (newestSequenz - packSequenz) + 1;

                    if(diff <= 0 || diff >= 32){
                        continue;
                    }
                    String temp1 = bitfield.substring(0, diff);
                    String temp2 = bitfield.substring(diff + 1, bitfield.length());
                    bitfield = temp1 + "1" + temp2;

                }else if(pack.getSequenz() - sequenz > Integer.MAX_VALUE - 32){
                    int diff = sequenz + (Integer.MAX_VALUE - pack.getSequenz()) + 1;


                    if(diff <= 0 || diff >= 32){
                        continue;
                    }
                    String temp1 = bitfield.substring(0, diff);
                    String temp2 = bitfield.substring(diff + 1, bitfield.length());
                    bitfield = temp1 + "1" + temp2;
                }
            }
            header.put(Byte.parseByte(bitfield.substring(0,4),2));
            header.put(Byte.parseByte(bitfield.substring(4,8),2));
            header.put(Byte.parseByte(bitfield.substring(8,12),2));
            header.put(Byte.parseByte(bitfield.substring(12,16),2));

        } else {
            header.putInt(0);
        }
        header.putInt(type);

    }

    public void parsePackage(byte[] data){

        header.clear();
        for(int i = 0; i < 24; i++){
            header.put(data[i]);
        }

        buffer = ByteBuffer.allocate(data.length - 24);
        buffer.clear();
        for(int i = 24; i < data.length; i++){
            buffer.put(data[i]);
        }

    }

    public int getProtocolID(){
        return header.getInt(0);
    }

    public int getClientID(){
        return header.getInt(8);
    }

    public int getSequenz(){
        return buffer.getInt(4);
    }


    public int getPackageType(){
        return buffer.getInt(20);
    }

    public int getAck(){
        return header.getInt(12);
    }

    public boolean[] getAckBitfield(){
        byte[] bitfield = new byte[4];
        bitfield[0] = header.get(16);
        bitfield[1] = header.get(17);
        bitfield[2] = header.get(18);
        bitfield[3] = header.get(19);
        boolean[] ret = new boolean[bitfield.length * 8];
        for(int i = 0; i < bitfield.length; i++){
            for(int j = 7; j >= 0; j--){
                ret[i * 8 + 7 - j] = ((bitfield[i] >> j) & 1) == 1;
            }
        }
        return ret;
    }

    public ByteBuffer getBytes() {
        return buffer;
    }

    public void setBytes(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public InetAddress getReceiver() {
        return receiver;
    }

    public void setReceiver(InetAddress receiver) {
        this.receiver = receiver;
    }

    public InetAddress getSender() {
        return sender;
    }

    public void setSender(InetAddress sender) {
        this.sender = sender;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public ByteBuffer getHeader() {
        return header;
    }

    public void setHeader(ByteBuffer header) {
        this.header = header;
    }
}
