package me.reckter.Network.Packages;

/*
import me.reckter.Level.BaseLevel;
import me.reckter.Network.Network;
import org.newdawn.slick.Input;
*/

import me.reckter.Network.Network;

import java.nio.ByteBuffer;

/**
 * Created by mediacenter on 01.01.14.
 */
public class InputPackage extends BasePackage{


    /**
     *  bytes | function
     *   0          clientID
     *   1          package number
     *   2          package ID (1 here)
     *   3          mouse x (float)
     *   4          =|=
     *   5          =|=
     *   6          =|=
     *   7          mouse y (float)
     *   8          =|=
     *   9          =|=
     *   10         =|=
     *   11         keys pressed (0 = accelerate, 1 = shot)
     *
     */


    public InputPackage(Network network) {
        super(network);
        this.buffer = ByteBuffer.allocate(9);
    }
    /*

    public void fillPackage(Input input, BaseLevel level){
        buffer.put((byte) 1);
        float x = level.getRealX(input.getMouseX());
        float y = level.getRealX(input.getMouseX());

        buffer.putFloat(x);
        buffer.putFloat(y);
        byte keys = 0;
        if(input.isKeyDown(Input.KEY_W) || input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
            keys += 128;
        }

        if(input.isKeyDown(Input.KEY_SPACE) || input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)){
            keys += 64;
        }
        buffer.put(keys);
    }

    public float getMouseX(){
        return buffer.getFloat(1);
    }

    public float getMouseY(){
        return buffer.getFloat(5);
    }

    public boolean isAccelerating(){
        return buffer.get(9) - 128 >= 0; //the highest bit of "keys pressed" is set
    }

    public boolean isShooting(){
        if(isAccelerating()){
            return buffer.get(9) - 128 - 64 >= 0;
        } else {
            return buffer.get(9) - 64 >= 0;
        }
    }
    */
}
