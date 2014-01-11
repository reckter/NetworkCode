package me.reckter.Network.Packages;
/*
import me.reckter.Entities.BaseEntity;
import me.reckter.Entities.Enemies.*;
import me.reckter.Entities.Player;
import me.reckter.Entities.PowerUp.BasePowerUp;
import me.reckter.Entities.PowerUp.HealPowerUp;
import me.reckter.Entities.Projectiles.*;
import me.reckter.Level.BaseLevel;
import me.reckter.Log;
import me.reckter.Network.Network;
import org.newdawn.slick.geom.Vector2f;
*/

import me.reckter.Network.Network;

import java.nio.ByteBuffer;

/**
 * Created by mediacenter on 01.01.14.
 */
public class EntityPackage extends BasePackage {

    public EntityPackage(Network network) {
        super(network);
        this.buffer = ByteBuffer.allocate(64);
    }

    /**
     *  bytes | function
     *   0          clientID
     *   1          package number
     *   2          package ID (2 here)
     *   3          type
     *   4          entity ID
     *   5          =|=
     *   6          =|=
     *   7          =|=
     *   8          x (float)
     *   9          =|=
     *   10         =|=
     *   11         =|=
     *   12         y (float)
     *   13         =|=
     *   14         =|=
     *   15         =|=
     *   16         movement x (float)
     *   17         =|=
     *   18         =|=
     *   19         =|=
     *   20         movement y (float)
     *   21         =|=
     *   22         =|=
     *   23         =|=
     *   24         angle (float)
     *   25         =|=
     *   26         =|=
     *   27         =|=
     *   28         misc (0 = isAccelerating, 1 = isShooting , 2 = isDead)
     *
     */

/*
    public void fillPackage(BaseEntity entity){

        buffer.put((byte) 2);
        buffer.put(entity.type);
        buffer.putInt(entity.getId());
        buffer.putFloat(entity.getX());
        buffer.putFloat(entity.getY());

        try{
        buffer.putFloat(entity.getMovement().getX());
        buffer.putFloat(entity.getMovement().getY());
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        buffer.putFloat(entity.getAngle());
        byte misc = 0;

        if(entity.isAccelerating()){
            misc += 128;
        }
        if(entity.isShooting()){
            misc += 64;
        }
        if(entity.isDead()){
            misc += 32;
        }

        buffer.put(misc);
    }

    public byte getType(){
        return buffer.get(3);
    }

    public int getEntityID(){
        return buffer.getInt(4);
    }

    public float getX(){
        return buffer.getInt(8);
    }

    public float getY(){
        return buffer.getInt(12);
    }

    public float getMovementX(){
        return buffer.getInt(16);
    }

    public float getMovementY(){
        return buffer.getInt(20);
    }

    public float getAngle(){
        return buffer.get(24);
    }

    public byte getMiscByte(){
        return buffer.get(28);
    }

    public boolean isAccelerating(){
        return getMiscByte() - 128 >= 0;
    }

    public boolean isShooting(){
        byte misc = getMiscByte();
        if(isAccelerating()){
            misc -= 128;
        }
        return misc - 64 >= 0;
    }
    public boolean isDead(){
        byte misc = getMiscByte();
        if(isAccelerating()){
            misc -= 128;
        }
        if(isShooting()){
            misc -= 64;
        }
        return misc - 32 >= 0;
    }


    public void updateEntity(BaseEntity entity){
        entity.setX(getX());
        entity.setY(getY());
        entity.setMovement(new Vector2f(getMovementX(), getMovementY()));
        entity.setAngle(getAngle());
        entity.setShooting(isShooting());
        entity.setAccelerating(isAccelerating());
        entity.setDead(isDead());
    }

    public BaseEntity createEntity(BaseLevel level){
        BaseEntity entity;
        BaseEntity ent;
        switch (getType()){
            case 0: ent = new BaseEntity(level);
                break;
            case 1: ent = new Player(level);
                break;
            case 2: ent = new BaseEnemy(level);
                break;
            case 3: ent = new BasePowerUp(level);
                break;
            case 4: ent = new BaseProjectile(level);
                break;
            case 5: ent = new Fighter(level);
                break;
            case 6: ent = new HugeRock(level);
                break;
            case 7: ent = new MiddleRock(level);
                break;
            case 8: ent = new Rock(level);
                break;
            case 9: ent = new Shooter(level);
                break;
            case 10: ent = new SmallRock(level);
                break;
            case 11: ent = new HealPowerUp(level);
                break;
            case 12: ent = new BombProjectile(level);
                break;
            case 13: ent = new GrenadeProjectile(level);
                break;
            case 14: ent = new HomingMissileProjectile(level);
                break;
            case 15: ent = new LaserProjectile(level);
                break;
            default:
                Log.error("Got an Invalid Entity Type in a Package... Ignoring that package");
                return null;
        }
        updateEntity(ent);
        return ent;
    }

*/

}
