package net.dev.utils.database;

import net.dev.database.*;
import net.dev.*;
import org.bukkit.configuration.*;

public class LoadDatabase {
    public static DataBase db;
    public static void loadDatabase(){
        db= DataBase.create(new MemoryConfiguration(){{
            this.set("type", GrewEssentials.getInstance().Config.getString("Database.type"));
            this.set("database",GrewEssentials.getInstance().Config.getString("Database.database"));
            this.set("ip",GrewEssentials.getInstance().Config.getString("Database.hostname"));
            this.set("port",GrewEssentials.getInstance().Config.getInt("Database.port"));
            this.set("username",GrewEssentials.getInstance().Config.getString("Database.username"));
            this.set("password",GrewEssentials.getInstance().Config.getString("Database.password"));
        }});
        db.createTables("CommandsEnable",new KeyValue(){{
            this.add("uuid","TEXT");
            this.add("fly","TINYINT");
            this.add("vanish","TINYINT");
        }},null);
        db.createTables("PlayerInfo",new KeyValue(){{
            this.add("uuid","TEXT");
            this.add("name","TEXT");
            this.add("ip","TEXT");
        }},null);
        db.createTables("BlackMessageWarnTimes",new KeyValue(){{
            this.add("uuid","TEXT");
            this.add("times","INT");
        }},null);
    }
}
