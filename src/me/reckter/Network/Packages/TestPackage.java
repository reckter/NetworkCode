package me.reckter.Network.Packages;

import me.reckter.Network.Network;

import java.nio.ByteBuffer;

/**
 * Created by mediacenter on 09.01.14.
 */
public class TestPackage extends BasePackage {
    public TestPackage(Network network) {
        super(network);
        this.type = 3;
    }


    public void setTestData(ByteBuffer buffer){
        this.buffer = buffer;
    }

    public ByteBuffer getTestData(){
        return this.buffer;
    }
}
