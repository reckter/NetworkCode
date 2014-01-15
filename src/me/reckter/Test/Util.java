package me.reckter.Test;

/**
 * Created by mediacenter on 10.01.14.
 */
public class Util {


    static public String printBooleanArray(boolean[] array){
        String ret = "";
        for(int i = 0; i < array.length; i++){
            if(array[i]){
                ret += "    " + array[i];
            } else {
                ret += "   " + array[i];
            }
        }
        return ret;
    }


    static public String printByteArray(byte[] array){
        String ret = "";
        for(int i = 0; i < array.length; i++){
            ret += makeRadable(array[i]);
        }
        return ret;
    }



    static public String makeRadable(byte data) {
        if(data >= 16 || data < 0){
            return "  " + Integer.toHexString(((int) (data) << 24) >>> 24);
        }
        return "  0" + Integer.toHexString(((int) (data) << 24) >>> 24);
    }
}
