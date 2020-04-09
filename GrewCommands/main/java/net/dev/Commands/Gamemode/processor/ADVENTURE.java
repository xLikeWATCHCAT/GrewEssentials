package net.dev.Commands.Gamemode.processor;

import net.dev.*;
import net.dev.Utils.LogUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import static net.dev.Utils.StringUtils.StringUtils.*;

public class ADVENTURE{
    public static String getPermission() { return GrewEssentials.getInstance().Config.getString("Permissions.Gamemode.ADVENTURE"); }

    public static void setPlayerGamemodeToADVENTURE(Player p,CommandSender b){
        if (p.getGameMode() == GameMode.ADVENTURE) {
            b.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Gamemode.Presence_Message"))
                    .replace("$playername", p.getName())
                    .replace("$prefix", Prefix)
                    .replace("$type",StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Gamemode.Adventure.Type"))));
            return;
        }
        p.setGameMode(GameMode.ADVENTURE);
        b.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Gamemode.Message")).replace("$prefix", StringUtils.Prefix).replace("$playername",p.getName()).replace("$type",StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Gamemode.Adventure.Type"))));
        if(b instanceof Player){
            Player a = (Player) b;
            LogUtils.writeLog(StringUtils.removeColorCodes(GrewEssentials.getInstance().log.getString("Gamemode").replace("$player",p.getName()).replace("$type",StringUtils.removeColorCodes(GrewEssentials.getInstance().Message.getString("Gamemode.Adventure.Type"))).replace("$doplayer",a.getName()).replace("$playeruuid",p.getUniqueId().toString()).replace("$playerip",p.getAddress().getHostName()).replace("$playerisop",String.valueOf(p.isOp())).replace("$doplayeruuid",a.getUniqueId().toString()).replace("$doplayerip",a.getAddress().getHostName()).replace("$doplayerisop",String.valueOf(a.isOp()))));
        }else{
            String Console = GrewEssentials.getInstance().log.getString("Console");
            LogUtils.writeLog(StringUtils.removeColorCodes(GrewEssentials.getInstance().log.getString("Gamemode").replace("$player",p.getName()).replace("$type",StringUtils.removeColorCodes(GrewEssentials.getInstance().Message.getString("Gamemode.Adventure.Type"))).replace("$doplayer",Console).replace("$playeruuid",p.getUniqueId().toString()).replace("$playerip",p.getAddress().getHostName()).replace("$playerisop",String.valueOf(p.isOp())).replace("$doplayeruuid",Console).replace("$doplayerip",Console).replace("$doplayerisop","true")));
        }
    }

}
