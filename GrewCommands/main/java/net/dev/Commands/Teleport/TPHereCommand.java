package net.dev.Commands.Teleport;

import net.dev.*;
import net.dev.Utils.PlayerUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class TPHereCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(GrewEssentials.getInstance().Message.getBoolean("TelePort.Enable")){
            if(GrewEssentials.getInstance().Message.getBoolean("TelePort.TPHERE.Enable")){
                if (sender instanceof Player) {
                    if(sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.TelePort.TPHERE"))|| sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))){
                        if(args.length >=1){
                            Player p,teleportplayer;
                            try{
                                if(args.length >= 2)
                                    p = Bukkit.getPlayer(args[1]);
                                else
                                    p = (Player) sender;
                            }catch (Throwable e){
                                sender.sendMessage(StringUtils.NoHavePlayer);
                                return true;
                            }
                            try{
                                teleportplayer = Bukkit.getPlayer(args[0]);
                            }catch (Throwable e){
                                sender.sendMessage(StringUtils.NoHavePlayer);
                                return true;
                            }
                            teleportplayer.teleport(p.getLocation());
                            PlayerUtil.sendMessage(sender,GrewEssentials.getInstance().Message.getString("TelePort.TPHERE.Success").replace("$player",p.getName()).replace("$playername",p.getName()).replace("$teleportplayer",teleportplayer.getName()).replace("$teleportplayername",teleportplayer.getName()));
                        }else{
                            sender.sendMessage(StringUtils.getCommandInfo("tphere"));
                        }
                    }else {
                        sender.sendMessage(StringUtils.DoNotHavePerMission);
                        return true;
                    }
                }else{
                    sender.sendMessage(StringUtils.OnlyPlayer);
                }
            }else {
                sender.sendMessage(StringUtils.DoNotHavePerMission);
                return true;
            }
        }else {
            sender.sendMessage(StringUtils.DoNotHavePerMission);
            return true;
        }
        return true;
    }
}
