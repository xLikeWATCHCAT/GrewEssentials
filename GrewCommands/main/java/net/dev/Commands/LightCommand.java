package net.dev.Commands;

import net.dev.*;
import net.dev.Utils.LogUtils.*;
import net.dev.Utils.StringUtils.*;
import net.dev.Utils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.util.*;

import static net.dev.ReflectionWrapper.*;

public class LightCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(GrewEssentials.getInstance().Message.getBoolean("Light.Enable")){
            if(GrewEssentials.getInstance().Message.getBoolean("Light.Alone.Enable")){
                if(sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))||sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Light.Alone"))){
                    if (args.length > 0) {
                        if (args[0] != null) {
                            Player t = Bukkit.getPlayer(args[0]);
                            if (t == null) {
                                Utils.notOnline(sender,args[0]);
                                return true;
                            }
                            t.getWorld().strikeLightning(Bukkit.getPlayer(args[0]).getLocation());
                            if(GrewEssentials.getInstance().Message.getBoolean("Light.Fly.Enable")){
                                if(args.length == 1){
                                    if(GrewEssentials.getInstance().Message.getBoolean("Light.Knock.Default")){
                                        t.setVelocity(new Vector(0, 10, 0));
                                    }
                                }else if(args.length == 2){
                                    if(args[1].equalsIgnoreCase("true")){
                                        t.setVelocity(new Vector(0, 10, 0));
                                    }
                                }
                            }
                            try{
                                Utils.createPlayerHelix(Bukkit.getPlayer(args[0]), Enum.valueOf((Class)getNMSClass("EnumParticle"),"SMOKE_LARGE"), 10, 2);
                                Utils.createPlayerHelix(Bukkit.getPlayer(args[0]), Enum.valueOf((Class)getNMSClass("EnumParticle"),"ENCHANTMENT_TABLE"), 5, 1);
                            }catch (Throwable e){
                                e.printStackTrace();
                            }
                            sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Light.Alone.Message")).replace("$prefix",StringUtils.Prefix).replace("$playername",t.getName()));
                            if(sender instanceof Player)
                                LogUtils.writeLog(StringUtils.removeColorCodes(GrewEssentials.getInstance().log.getString("Light").replace("$player",sender.getName()).replace("$playeruuid",((Player) sender).getUniqueId().toString()).replace("$playerip", ((Player) sender).getAddress().getHostName()).replace("$playerisop",String.valueOf(sender.isOp())).replace("$fightplayer",t.getName()).replace("$fightplayeruuid",t.getUniqueId().toString()).replace("$fightplayerip",t.getAddress().getHostName()).replace("$fightplayerisop",String.valueOf(t.isOp()))));
                            return true;
                        }
                    }else{
                        sender.sendMessage(StringUtils.getCommandInfo("light"));
                    }
                }else{
                    sender.sendMessage(StringUtils.DoNotHavePerMission);
                }
            }
        }else{
            sender.sendMessage(StringUtils.DoNotHavePerMission);
        }
        return true;
    }
}
