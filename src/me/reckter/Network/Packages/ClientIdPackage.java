package me.reckter.Network.Packages;

import me.reckter.Network.Network;

import java.nio.ByteBuffer;

/**
 * Created by reckter on 1/13/14.
 */
public class ClientIdPackage extends BasePackage {
    /**
     * header:
     * type = 1
     *
     * buffer:
     *  bytes | function
     *   0               new ClientID (int)
     *   1          =|=
     *   2          =|=
     *   3          =|=
     *   4               port (int)
     *   5          =|=
     *   6          =|=
     *   7          =|=
     *
     *
     */
    public ClientIdPackage(Network network) {
        super(network);
        this.type = 1;
        this.buffer = ByteBuffer.allocate(8);
    }

    public void setNewClientId(int id){
        buffer.putInt(0, id);
    }

    public int getNewClientId(){
        return buffer.getInt(0);
    }

    public void setPort(int port){
        buffer.putInt(4, port);
    }

    public int getPort(){
        return buffer.getInt(4);
    }


}
