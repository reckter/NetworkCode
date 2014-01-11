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



    static public String makeRadable(byte data){
        if(data <= -100){
            return " " + Byte.toString(data);
        }
        if(data >= 100 || data <= -10){
            return "  " + Byte.toString(data);
        }
        if(data >= 10 || data <= -1){
            return "   " + Byte.toString(data);
        }
        return "    " + Byte.toString(data);
    }
}
