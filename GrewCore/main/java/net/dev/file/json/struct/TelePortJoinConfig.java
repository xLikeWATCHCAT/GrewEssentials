package net.dev.file.json.struct;

import net.dev.file.json.*;
import org.bukkit.*;

import java.util.*;

public class TelePortJoinConfig {
    public TreeMap<Text, TelePortJoin> teleportjoinpoints=new TreeMap<>();
    public static class TelePortJoin {
        public String world;
        public double x;
        public double y;
        public double z;
        public float pitch;
        public float yaw;
        public TelePortJoin(){}
        public TelePortJoin(String world, double x, double y, double z, float pitch, float yaw)
        {
            this.world=world;
            this.x=x;
            this.y=y;
            this.z=z;
            this.pitch=pitch;
            this.yaw=yaw;
        }
        public static TelePortJoin fromLocation(Location l)
        {
            return new TelePortJoin(l.getWorld().getName(),l.getX(),l.getY(),l.getZ(),l.getPitch(),l.getYaw());
        }
    }
}
