package net.dev.Commands;

import net.dev.*;
import net.dev.Utils.LogUtils.*;
import net.dev.Utils.StringUtils.*;
import net.dev.Utils.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class AnnouncementCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(GrewEssentials.getInstance().Message.getBoolean("Announcement.Enable")){
                if (sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Announcement")) || sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))){
                    StringBuilder note = new StringBuilder();
                    if (args.length > 0) {
                        for(int i = 0; i < args.length; i++){
                            note.append(args[i]);
                            if(i != args.length-1)
                                note.append(" ");
                            Utils.BroadCast(GrewEssentials.getInstance().Message.getString("Announcement.Prefix")+String.valueOf(note).replace("$prefix", StringUtils.Prefix));
                            if(sender instanceof Player)
                                LogUtils.writeLog(StringUtils.removeColorCodes(GrewEssentials.getInstance().log.getString("Announcement").replace("$player",sender.getName()).replace("$playeruuid",((Player) sender).getUniqueId().toString()).replace("$playerip", ((Player) sender).getAddress().getHostName()).replace("$playerisop",String.valueOf(sender.isOp()))));
                        }
                    } else {
                        sender.sendMessage(StringUtils.getCommandInfo("Announcement"));
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
