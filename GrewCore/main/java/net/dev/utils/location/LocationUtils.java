package net.dev.utils.location;

import net.dev.*;
import net.dev.file.json.*;
import net.dev.file.json.struct.*;
import net.dev.utils.string.Random;
import net.dev.utils.string.*;
import org.bukkit.*;

import java.util.*;
import java.util.stream.*;

public class LocationUtils {
    public String getRandomTPJoinPointNameAll() {
        String str = String.valueOf(getTPJoinListAll().size() > 0 ? new ArrayList<>(getTPJoinListAll()).get(new Random().getRandomInt(0, getTPJoinListAll().size() - 1)) : null);
        return str;
    }
    public String getRandomTPJoinPointNameDesignated(){
        return String.valueOf(getTPJoinListDesignated().size() > 0 ? new ArrayList<>(getTPJoinListDesignated()).get(new Random().getRandomInt(0, getTPJoinListDesignated().size() - 1)) : null);
    }
    public Location getRandomAllTPJoinLocation(){
        String name = getRandomTPJoinPointNameAll();
        if(!GrewEssentials.getInstance().teleportjoin.getConfig().teleportjoinpoints.containsKey(new Text(name)))
        {
            return null;
        }
        TelePortJoinConfig.TelePortJoin telePortJoin=GrewEssentials.getInstance().teleportjoin.getConfig().teleportjoinpoints.get(new Text(name));
        return new Location(Bukkit.getWorld(telePortJoin.world),telePortJoin.x,telePortJoin.y,telePortJoin.z,telePortJoin.yaw,telePortJoin.pitch);
    }
    public Location getRandomDesignatedLocation(){
        String name = getRandomTPJoinPointNameDesignated();
        if(!GrewEssentials.getInstance().teleportjoin.getConfig().teleportjoinpoints.containsKey(new Text(name)))
        {
            return null;
        }
        TelePortJoinConfig.TelePortJoin telePortJoin=GrewEssentials.getInstance().teleportjoin.getConfig().teleportjoinpoints.get(new Text(name));
        return new Location(Bukkit.getWorld(telePortJoin.world),telePortJoin.x,telePortJoin.y,telePortJoin.z,telePortJoin.yaw,telePortJoin.pitch);
    }
    public Location getTPJoinLocation(){
        String name = GrewEssentials.getInstance().Config.getString("Teleport.TeleportJoin.Teleport");
        if(!GrewEssentials.getInstance().teleportjoin.getConfig().teleportjoinpoints.containsKey(new Text(name)))
        {
            return null;
        }
        TelePortJoinConfig.TelePortJoin telePortJoin=GrewEssentials.getInstance().teleportjoin.getConfig().teleportjoinpoints.get(new Text(name));
        return new Location(Bukkit.getWorld(telePortJoin.world),telePortJoin.x,telePortJoin.y,telePortJoin.z,telePortJoin.yaw,telePortJoin.pitch);
    }
    public Location getRandomAllTPJoinLocationSmart(){
        int bornBlocksX = GrewEssentials.getInstance().Config.getInt("Teleport.TeleportJoin.Smart.BornBlocks.X");
        int bornBlocksYUP = GrewEssentials.getInstance().Config.getInt("Teleport.TeleportJoin.Smart.BornBlocks.Y.UP");
        int bornBlocksYDOWN = GrewEssentials.getInstance().Config.getInt("Teleport.TeleportJoin.Smart.BornBlocks.Y.DOWN");
        int bornBlocksZ = GrewEssentials.getInstance().Config.getInt("Teleport.TeleportJoin.Smart.BornBlocks.Z");
        double randomx = Numbers.getRandomDouble(0-bornBlocksX,bornBlocksX);
        double randomY = Numbers.getRandomDouble(0-bornBlocksYDOWN,bornBlocksYUP);
        double randomz = Numbers.getRandomDouble(0-bornBlocksZ,bornBlocksZ);
        Location smartLocation = getRandomAllTPJoinLocation();
        smartLocation.setX(smartLocation.getX()-randomx);
        smartLocation.setY(smartLocation.getY()-randomY);
        smartLocation.setZ(smartLocation.getZ()-randomz);
        return smartLocation;
    }
    public Location getRandomDesignatedLocationSmart(){
        int bornBlocksX = GrewEssentials.getInstance().Config.getInt("Teleport.TeleportJoin.Smart.BornBlocks.X");
        int bornBlocksYUP = GrewEssentials.getInstance().Config.getInt("Teleport.TeleportJoin.Smart.BornBlocks.Y.UP");
        int bornBlocksYDOWN = GrewEssentials.getInstance().Config.getInt("Teleport.TeleportJoin.Smart.BornBlocks.Y.DOWN");
        int bornBlocksZ = GrewEssentials.getInstance().Config.getInt("Teleport.TeleportJoin.Smart.BornBlocks.Z");
        double randomx = Numbers.getRandomDouble(0-bornBlocksX,bornBlocksX);
        double randomY = Numbers.getRandomDouble(0-bornBlocksYDOWN,bornBlocksYUP);
        double randomz = Numbers.getRandomDouble(0-bornBlocksZ,bornBlocksZ);
        Location smartLocation = getRandomDesignatedLocation();
        smartLocation.setX(smartLocation.getX()-randomx);
        smartLocation.setY(smartLocation.getY()-randomY);
        smartLocation.setZ(smartLocation.getZ()-randomz);
        return smartLocation;
    }
    public Location getTPJoinLocationSmart() {
        int bornBlocksX = GrewEssentials.getInstance().Config.getInt("Teleport.TeleportJoin.Smart.BornBlocks.X");
        int bornBlocksYUP = GrewEssentials.getInstance().Config.getInt("Teleport.TeleportJoin.Smart.BornBlocks.Y.UP");
        int bornBlocksYDOWN = GrewEssentials.getInstance().Config.getInt("Teleport.TeleportJoin.Smart.BornBlocks.Y.DOWN");
        int bornBlocksZ = GrewEssentials.getInstance().Config.getInt("Teleport.TeleportJoin.Smart.BornBlocks.Z");
        double randomx = Numbers.getRandomDouble(0-bornBlocksX,bornBlocksX);
        double randomY = Numbers.getRandomDouble(0-bornBlocksYDOWN,bornBlocksYUP);
        double randomz = Numbers.getRandomDouble(0-bornBlocksZ,bornBlocksZ);
        Location smartLocation = getTPJoinLocation();
        smartLocation.setX(smartLocation.getX()-randomx);
        smartLocation.setY(smartLocation.getY()-randomY);
        smartLocation.setZ(smartLocation.getZ()-randomz);
        return smartLocation;
    }
    public ArrayList<String> getTPJoinListDesignated(){
        return (ArrayList<String>) GrewEssentials.getInstance().Config.getStringList("Teleport.TeleportJoin.RandomList.List");
    }
    public List<String> getTPJoinListAll(){
        return GrewEssentials.getInstance().teleportjoin.getConfig().teleportjoinpoints.keySet().stream().map(i->i.toString()).collect(Collectors.toList());
    }
}