package net.dev.Commands;

import net.dev.*;
import net.dev.Utils.LogUtils.*;
import net.dev.Utils.PlayerUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

import java.util.concurrent.*;

public class InvSeeCommand implements CommandExecutor{
    public static final ConcurrentHashMap<Inventory,Player> ivs=new ConcurrentHashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(GrewEssentials.getInstance().Message.getBoolean("InvSee.Enable")){
            if(sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))||sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.InvSee"))){
                Player p;
                if(sender instanceof  Player){
                    p = (Player)sender;
                }else{
                    sender.sendMessage(StringUtils.OnlyPlayer);
                    return true;
                }
                if(args.length == 1){
                    Player target = Bukkit.getPlayer(args[0]);
                    if(target == null){
                        sender.sendMessage(StringUtils.getCommandInfo("invsee"));
                        return true;
                    }
                    Inventory inv;
                    inv = Bukkit.createInventory(null,45);
                    for(int i=0;i<4;i++)
                        inv.setItem(i,target.getInventory().getArmorContents()[i]);
                    for(int i=9;i<45;i++)
                        inv.setItem(i,target.getInventory().getContents()[i-9]);
                    ivs.put(inv,target);
                    p.openInventory(inv);
                    PlayerUtil.sendMessage(p,GrewEssentials.getInstance().Message.getString("InvSee.Message").replace("$playername",target.getName()));
                    LogUtils.writeLog(StringUtils.removeColorCodes(GrewEssentials.getInstance().log.getString("InvSee").replace("$player",sender.getName()).replace("$playeruuid",((Player) sender).getUniqueId().toString()).replace("$playerip", ((Player) sender).getAddress().getHostName()).replace("$playerisop",String.valueOf(sender.isOp())).replace("$lookplayer",target.getName()).replace("$lookplayeruuid",target.getUniqueId().toString()).replace("$lookplayerip", target.getAddress().getHostName()).replace("$lookplayerisop",String.valueOf(target.isOp()))));
                    return true;
                }else{
                    sender.sendMessage(StringUtils.getCommandInfo("invsee"));
                }
            }else{
                sender.sendMessage(StringUtils.DoNotHavePerMission);
            }
        }else{
            sender.sendMessage(StringUtils.DoNotHavePerMission);
        }
        return true;
    }
}
