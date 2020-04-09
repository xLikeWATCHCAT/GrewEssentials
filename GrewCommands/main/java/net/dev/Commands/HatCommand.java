package net.dev.Commands;

import net.dev.*;
import net.dev.Utils.LogUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

public class HatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(GrewEssentials.getInstance().Message.getBoolean("Hat.Enable",true)){
            if(sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Hat"))|| sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))){
                if (sender instanceof Player) {
                    Player p = (Player)sender;
                    PlayerInventory inv = p.getInventory();
                    ItemStack held = p.getItemInHand();
                    ItemStack helm = inv.getHelmet();
                    if (held.getAmount() != 1 && held.getType() != Material.AIR) {
                        sender.sendMessage(StringUtils.translateColorCodes(p, GrewEssentials.getInstance().Message.getString("Hat.None").replace("$prefix",StringUtils.Prefix)));
                    } else {
                        inv.setHelmet(held);
                        p.setItemInHand(helm);
                        p.updateInventory();
                        sender.sendMessage(StringUtils.translateColorCodes(p,GrewEssentials.getInstance().Message.getString("Hat.Success").replace("$prefix",StringUtils.Prefix)));
                        LogUtils.writeLog(GrewEssentials.getInstance().log.getString("Hat").replace("$player",p.getName()).replace("$playeruuid",p.getUniqueId().toString()).replace("$playerip",p.getAddress().getHostName()).replace("$playerisop",String.valueOf(p.isOp())));
                    }
                } else {
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
}
