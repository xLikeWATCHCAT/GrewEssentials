package net.dev.file.json.struct;

import net.dev.file.json.*;
import org.bukkit.*;

import java.util.*;

public class WarpConfig {
    public TreeMap<Text, WarpPoint> warps=new TreeMap<>();
    public static class WarpPoint {
        public String world;
        public double x;
        public double y;
        public double z;
        public float pitch;
        public float yaw;
        public WarpPoint(){}
        public WarpPoint(String world, double x, double y, double z, float pitch, float yaw)
        {
            this.world=world;
            this.x=x;
            this.y=y;
            this.z=z;
            this.pitch=pitch;
            this.yaw=yaw;
        }
        public static WarpPoint fromLocation(Location l)
        {
            return new WarpPoint(l.getWorld().getName(),l.getX(),l.getY(),l.getZ(),l.getPitch(),l.getYaw());
        }
    }
}
