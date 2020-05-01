package net.dev.Commands;

import net.dev.*;
import net.dev.Utils.CommandUtils.*;
import net.dev.Utils.LogUtils.*;
import net.dev.Utils.PlayerUtils.*;
import net.dev.Utils.StringUtils.*;
import net.dev.Utils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.util.*;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.*;

import static net.dev.ReflectionWrapper.*;
import static net.dev.Utils.StringUtils.TabListType.*;

public class LightaCommand implements CommandWithCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(GrewEssentials.getInstance().Message.getBoolean("Light.Enable")){
            if(GrewEssentials.getInstance().Message.getBoolean("Light.All.Enable")){
                if(sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))||sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Light.All"))){
                    for(Player onlineplayer : Bukkit.getServer().getOnlinePlayers()) {
                        onlineplayer.getWorld().strikeLightning(onlineplayer.getLocation());
                        if(GrewEssentials.getInstance().Message.getBoolean("Light.Fly.Enable")){
                            if(args.length == 0){
                                if(GrewEssentials.getInstance().Message.getBoolean("Light.Fly.Default")){
                                    onlineplayer.setVelocity(new Vector(0, 10, 0));
                                }
                            }else if(args.length == 1){
                                if(args[0].equalsIgnoreCase("true")){
                                    onlineplayer.setVelocity(new Vector(0, 10, 0));
                                }
                            }
                        }
                        try{
                            Utils.createPlayerHelix(onlineplayer, Enum.valueOf((Class)getNMSClass("EnumParticle"),"SMOKE_LARGE"), 10, 2);
                            Utils.createPlayerHelix(onlineplayer, Enum.valueOf((Class)getNMSClass("EnumParticle"),"ENCHANTMENT_TABLE"), 5, 1);
                        }catch (Throwable e){
                            e.printStackTrace();
                        }
                    }
                    if(sender instanceof Player)
                        LogUtils.writeLog(StringUtils.removeColorCodes(GrewEssentials.getInstance().log.getString("Lighta").replace("$player",sender.getName()).replace("$playeruuid",((Player) sender).getUniqueId().toString()).replace("$playerip", ((Player) sender).getAddress().getHostName()).replace("$playerisop",String.valueOf(sender.isOp())).replace("$size",String.valueOf(Bukkit.getOnlinePlayers().size())).replace("$size",String.valueOf(Bukkit.getOnlinePlayers().size()-1))));
                    PlayerUtil.sendMessage(sender,GrewEssentials.getInstance().Message.getString("Light.All.Message"));
                    return true;
                }else{
                    sender.sendMessage(StringUtils.DoNotHavePerMission);
                }
            }
        }else{
            sender.sendMessage(StringUtils.DoNotHavePerMission);
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length==1)
            return ListUtil.toList("true","false").parallelStream().filter(i->i.toLowerCase(Locale.ENGLISH).startsWith(args[0].toLowerCase(Locale.ENGLISH))).collect(Collectors.toList());
        else return ListUtil.toList();
    }
}
