package net.dev.Commands;

import net.dev.*;
import net.dev.Utils.CommandUtils.*;
import net.dev.Utils.LogUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;
import java.util.stream.*;

import static net.dev.Utils.StringUtils.TabListType.*;

public class SpeedCommand implements CommandWithCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(GrewEssentials.getInstance().Message.getBoolean("Speed.Enable",true)){
            if(sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Speed"))|| sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))){
                if (sender instanceof Player) {
                    if(args.length>=1){
                        String type = args[0];
                        Player p;
                        Float speed = 1f;
                        if(args.length == 3){
                            try{
                                if(Bukkit.getPlayer(args[2])==null){
                                    p = (Player) sender;
                                }else{
                                    p = Bukkit.getPlayer(args[2]);
                                }
                            }catch (Throwable e){
                                p = (Player) sender;
                            }
                        }else{
                            p = (Player) sender;
                        }
                        if(args.length >= 2){
                            try{
                                speed = Float.valueOf(args[1]);
                            }catch (Throwable e){
                                sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Speed.SpeedInputError")).replace("$prefix",StringUtils.Prefix));
                                return true;
                            }
                        }
                        if(type.equalsIgnoreCase("fly")){
                            if(args.length != 2){
                                p.setFlySpeed(0.3f);
                                sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Speed.ColseSpeed")).replace("$prefix",StringUtils.Prefix).replace("$type",type));
                                LogUtils.writeLog(GrewEssentials.getInstance().log.getString("Speed").replace("$player", sender.getName()).replace("$useplayer",p.getName()).replace("$playeruuid",((Player) sender).getUniqueId().toString()).replace("$playerip",((Player) sender).getAddress().getHostName()).replace("$playerisop",String.valueOf(sender.isOp())).replace("$useplayeruuid",p.getUniqueId().toString()).replace("$useplayerip",p.getAddress().getHostName()).replace("$useplayerisop",String.valueOf(p.isOp())).replace("$speed",String.valueOf(1)));
                                return true;
                            }
                            p.setFlySpeed(speed);
                            sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Speed.Success")).replace("$prefix",StringUtils.Prefix).replace("$type",type).replace("$speed",args[1]).replace("$playername",p.getName()).replace("$player",p.getName()));
                            LogUtils.writeLog(GrewEssentials.getInstance().log.getString("Speed").replace("$player", sender.getName()).replace("$useplayer",p.getName()).replace("$playeruuid",((Player) sender).getUniqueId().toString()).replace("$playerip",((Player) sender).getAddress().getHostName()).replace("$playerisop",String.valueOf(sender.isOp())).replace("$useplayeruuid",p.getUniqueId().toString()).replace("$useplayerip",p.getAddress().getHostName()).replace("$useplayerisop",String.valueOf(p.isOp())).replace("$speed",String.valueOf(speed)));
                        }else if(type.equalsIgnoreCase("walk")){
                            if(args.length != 2){
                                p.setWalkSpeed(0.3f);
                                sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Speed.ColseSpeed")).replace("$prefix",StringUtils.Prefix).replace("$type",type));
                                LogUtils.writeLog(GrewEssentials.getInstance().log.getString("Speed").replace("$player", sender.getName()).replace("$useplayer",p.getName()).replace("$playeruuid",((Player) sender).getUniqueId().toString()).replace("$playerip",((Player) sender).getAddress().getHostName()).replace("$playerisop",String.valueOf(sender.isOp())).replace("$useplayeruuid",p.getUniqueId().toString()).replace("$useplayerip",p.getAddress().getHostName()).replace("$useplayerisop",String.valueOf(p.isOp())).replace("$speed",String.valueOf(1)));
                                return true;
                            }
                            p.setWalkSpeed(speed);
                            sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Speed.Success")).replace("$prefix",StringUtils.Prefix).replace("$type",type).replace("$speed",args[1]).replace("$playername",p.getName()).replace("$player",p.getName()));
                            LogUtils.writeLog(GrewEssentials.getInstance().log.getString("Speed").replace("$player", sender.getName()).replace("$useplayer",p.getName()).replace("$playeruuid",((Player) sender).getUniqueId().toString()).replace("$playerip",((Player) sender).getAddress().getHostName()).replace("$playerisop",String.valueOf(sender.isOp())).replace("$useplayeruuid",p.getUniqueId().toString()).replace("$useplayerip",p.getAddress().getHostName()).replace("$useplayerisop",String.valueOf(p.isOp())).replace("$speed",String.valueOf(speed)));
                        }else{
                            sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Speed.UnKnowType")).replace("$prefix",StringUtils.Prefix).replace("$type",type));
                            return true;
                        }
                    }else{
                        sender.sendMessage(StringUtils.getCommandInfo("speed"));
                    }
                }else{
                    sender.sendMessage(StringUtils.OnlyPlayer);
                }
            }else{
                sender.sendMessage(StringUtils.DoNotHavePerMission);
            }
        }else{
            sender.sendMessage(StringUtils.DoNotHavePerMission);
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length==1)
            return getSpeedType().parallelStream().filter(i->i.toLowerCase(Locale.ENGLISH).startsWith(args[0].toLowerCase(Locale.ENGLISH))).collect(Collectors.toList());
        else return ListUtil.toList();
    }
}
