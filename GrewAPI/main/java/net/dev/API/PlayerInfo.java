package net.dev.API;

import net.dev.Database.*;
import net.dev.Utils.DatabaseUtils.*;
import org.bukkit.entity.*;

import java.util.*;

public class PlayerInfo {
    public static String getPlayerName(UUID u)
    {
        return LoadDatabase.db.dbSelectFirst("PlayerInfo","name",new KeyValue(){{ this.add("uuid",u.toString()); }});
    }
    public static String getPlayerName(String uuid)
    {
        return LoadDatabase.db.dbSelectFirst("PlayerInfo","name",new KeyValue(){{ this.add("uuid",uuid); }});
    }
    public static String getPlayerUUID(Player p)
    {
        return LoadDatabase.db.dbSelectFirst("PlayerInfo","uuid",new KeyValue(){{ this.add("name",p.getName()); }});
    }
    public static String getPlayerUUID(String playername)
    {
        return LoadDatabase.db.dbSelectFirst("PlayerInfo","uuid",new KeyValue(){{ this.add("name",playername); }});
    }
    public static Boolean havePlayer(UUID u)
    {
        String name = LoadDatabase.db.dbSelectFirst("PlayerInfo","name",new KeyValue(){{ this.add("uuid",u.toString()); }});
        return name != null;
    }
    public static Boolean havePlayer(Player p)
    {
        String uuid = LoadDatabase.db.dbSelectFirst("PlayerInfo","uuid",new KeyValue(){{ this.add("name",p.getName()); }});
        return uuid != null;
    }
    public static Boolean havePlayer(String playername)
    {
        String uuid = LoadDatabase.db.dbSelectFirst("PlayerInfo","uuid",new KeyValue(){{ this.add("name",playername); }});
        return uuid != null;
    }
    public static String getPlayerIP(Player p)
    {
        return LoadDatabase.db.dbSelectFirst("PlayerInfo","ip",new KeyValue(){{ this.add("name",p.getName()); }});
    }
    public static String getPlayerIP(String playername)
    {
        return LoadDatabase.db.dbSelectFirst("PlayerInfo","ip",new KeyValue(){{ this.add("name",playername); }});
    }
    public static String getPlayerIP(UUID u)
    {
        return LoadDatabase.db.dbSelectFirst("PlayerInfo","ip",new KeyValue(){{ this.add("uuid",u); }});
    }
    public static String getPlayerIPUUID(String u)
    {
        return LoadDatabase.db.dbSelectFirst("PlayerInfo","ip",new KeyValue(){{ this.add("uuid",u); }});
    }
}
