package net.dev.Commands.Warp;

import net.dev.File.struct.*;
import net.dev.*;
import net.dev.Utils.CommandUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;
import java.util.stream.*;

public class WarpCommand implements CommandWithCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(GrewEssentials.getInstance().Message.getBoolean("Warp.Enable")){
            if(sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))||sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Warp.Use"))||sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Warp.Admin"))){
                if(sender instanceof Player){
                    if(args.length >= 1){
                        String warpName = args[0];
                        Player p = (Player) sender;
                         if(!GrewEssentials.getInstance().data.getConfig().warps.containsKey(new WarpConfig.Text(warpName)))
                        {
                            sender.sendMessage(StringUtils.translateColorCodes(p,GrewEssentials.getInstance().Message.getString("Warp.Failed")).replace("$prefix",StringUtils.Prefix).replace("$warpname",warpName));
                            return true;
                        }
                        /*if(!GrewEssentials.getInstance().data.getConfig().data.get(warpName).enabled)
                        {
                            sender.sendMessage(StringUtils.translateColorCodes(p,GrewEssentials.getInstance().Message.getString("Warp.Failed")).replace("$prefix",StringUtils.Prefix));
                            return true;
                        }*/
                        try{
                            WarpConfig.WarpPoint wp=GrewEssentials.getInstance().data.getConfig().warps.get(new WarpConfig.Text(warpName));
                            p.teleport(new Location(Bukkit.getWorld(wp.world),wp.x,wp.y,wp.z,wp.yaw,wp.pitch));
                            sender.sendMessage(StringUtils.translateColorCodes(p,GrewEssentials.getInstance().Message.getString("Warp.Success")).replace("$prefix",StringUtils.Prefix).replace("$warpname",warpName));
                        }catch (Throwable e){
                            sender.sendMessage(StringUtils.translateColorCodes(p,GrewEssentials.getInstance().Message.getString("Warp.Failed")).replace("$prefix",StringUtils.Prefix));
                        }
                    }else{
                        sender.sendMessage(StringUtils.getCommandInfo("warp"));
                    }
                }else{
                    sender.sendMessage(StringUtils.OnlyPlayer);
                }
            }else{
                sender.sendMessage(StringUtils.DoNotHavePerMission);
            }
        }else {
            sender.sendMessage(StringUtils.DoNotHavePerMission);
        }
        return true;
    }
    public static List<String> textListToStringList(List<WarpConfig.Text> l)
    {
        List<String> ret =new ArrayList<>();
        for(WarpConfig.Text i : l)
            ret.add(i.getText());
        return ret;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length==1)
            return textListToStringList(GrewEssentials.getInstance().data.getConfig().warps.keySet().parallelStream().filter(i->i.getText().toLowerCase(Locale.ENGLISH).startsWith(args[0].toLowerCase(Locale.ENGLISH))).collect(Collectors.toList()));
        else return ListUtil.toList();
    }
}
