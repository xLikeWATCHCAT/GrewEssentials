package net.dev.Commands.Teleport;

import net.dev.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class TPAllCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(GrewEssentials.getInstance().Message.getBoolean("TelePort.Enable")){
            if(GrewEssentials.getInstance().Message.getBoolean("TelePort.TPALL.Enable")){
                if (sender instanceof Player) {
                    if(sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.TelePort.TPALL"))|| sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))){
                        Player p;
                        Location location;
                        if(args.length >= 1){
                            try{
                                if(Bukkit.getPlayer(args[0])==null){
                                    p = (Player) sender;
                                }else{
                                    p = Bukkit.getPlayer(args[0]);
                                }
                            }catch (Throwable e){
                                p = (Player) sender;
                            }
                        }else{
                            p = (Player) sender;
                        }
                        location = p.getLocation();
                        for(Player onlineplayer : Bukkit.getServer().getOnlinePlayers()) {
                            if(onlineplayer != p)
                                onlineplayer.teleport(location);
                        }
                        sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("TelePort.TPALL.Success")).replace("$prefix",StringUtils.Prefix).replace("$players",String.valueOf(Integer.valueOf(Bukkit.getServer().getOnlinePlayers().size()-1))).replace("$player",p.getName()).replace("$playername",p.getName()));
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
