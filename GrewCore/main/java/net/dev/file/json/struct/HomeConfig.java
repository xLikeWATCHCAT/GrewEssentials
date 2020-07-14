package net.dev.file.json.struct;

import net.dev.file.json.*;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;

public class HomeConfig {
    public TreeMap<Text, HomePoint> homes=new TreeMap<>();
    public static class HomePoint {
        public String player;
        public String world;
        public double x;
        public double y;
        public double z;
        public float pitch;
        public float yaw;
        public HomePoint(){}
        public HomePoint(String player,String world, double x, double y, double z, float pitch, float yaw)
        {
            this.player=player;
            this.world=world;
            this.x=x;
            this.y=y;
            this.z=z;
            this.pitch=pitch;
            this.yaw=yaw;
        }
        public static HomePoint fromLocation(Player p)
        {
            Location l = p.getLocation();
            return new HomePoint(p.getName(),l.getWorld().getName(),l.getX(),l.getY(),l.getZ(),l.getPitch(),l.getYaw());
        }
    }
}
