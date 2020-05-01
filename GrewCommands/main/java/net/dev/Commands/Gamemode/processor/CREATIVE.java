package net.dev.Commands.Gamemode.processor;

import net.dev.*;
import net.dev.Utils.LogUtils.*;
import net.dev.Utils.PlayerUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class CREATIVE {
    public static String getPermission() { return GrewEssentials.getInstance().Config.getString("Permissions.Gamemode.CREATIVE"); }

    public static void setPlayerGamemodeToCREATIVE(Player p,CommandSender b){
        if (p.getGameMode() == GameMode.CREATIVE) {
            PlayerUtil.sendMessage(b, GrewEssentials.getInstance().Message.getString("Gamemode.Presence_Message").replace("$type",GrewEssentials.getInstance().Message.getString("Gamemode.Creative.Type")).replace("$playername",p.getName()).replace("$player",p.getName()));
            return;
        }
        p.setGameMode(GameMode.CREATIVE);
        PlayerUtil.sendMessage(b,GrewEssentials.getInstance().Message.getString("Gamemode.Message").replace("$type",GrewEssentials.getInstance().Message.getString("Gamemode.Creative.Type")).replace("$playername",p.getName()).replace("$player",p.getName()));
        if(b instanceof Player){
            Player a = (Player) b;
            LogUtils.writeLog(StringUtils.removeColorCodes(GrewEssentials.getInstance().log.getString("Gamemode").replace("$player",p.getName()).replace("$type",StringUtils.removeColorCodes(GrewEssentials.getInstance().Message.getString("Gamemode.Creative.Type"))).replace("$doplayer",a.getName()).replace("$playeruuid",p.getUniqueId().toString()).replace("$playerip",p.getAddress().getHostName()).replace("$playerisop",String.valueOf(p.isOp())).replace("$doplayeruuid",a.getUniqueId().toString()).replace("$doplayerip",a.getAddress().getHostName()).replace("$doplayerisop",String.valueOf(a.isOp()))));
        }else{
            String Console = GrewEssentials.getInstance().log.getString("Console");
            LogUtils.writeLog(StringUtils.removeColorCodes(GrewEssentials.getInstance().log.getString("Gamemode").replace("$player",p.getName()).replace("$type",StringUtils.removeColorCodes(GrewEssentials.getInstance().Message.getString("Gamemode.Creative.Type"))).replace("$doplayer",Console).replace("$playeruuid",p.getUniqueId().toString()).replace("$playerip",p.getAddress().getHostName()).replace("$playerisop",String.valueOf(p.isOp())).replace("$doplayeruuid",Console).replace("$doplayerip",Console).replace("$doplayerisop","true")));
        }
    }

}
