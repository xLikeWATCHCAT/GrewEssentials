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
                    StringBuilder note = new StringBuilder();//使用StringBuilder处理String合并会更快
                    if (args.length > 0) {//如果有写
                        //理由可能出现空格，遍历所有
                        for(int i = 0; i < args.length; i++){
                            note.append(args[i]);
                            if(i != args.length-1)//不是最后一行的话
                                note.append(" ");//每遍历一遍加上空格
                            Utils.BroadCast(GrewEssentials.getInstance().Message.getString("Announcement.Prefix")+String.valueOf(note).replace("$prefix", StringUtils.Prefix));
                            if(sender instanceof Player)
                                LogUtils.writeLog(StringUtils.removeColorCodes(GrewEssentials.getInstance().log.getString("Announcement").replace("$player",sender.getName()).replace("$playeruuid",((Player) sender).getUniqueId().toString()).replace("$playerip", ((Player) sender).getAddress().getHostName()).replace("$playerisop",String.valueOf(sender.isOp()))));
                        }
                    } else {//如果没写
                        sender.sendMessage(StringUtils.getCommandInfo("Announcement"));
                        // GrewEssentials.writeLog("玩家: "+p.getName()+"想使用Announcement，但是他输入的格式错误，访问无法进行。"+" UUID: "+p.getUniqueId()+" IP: "+p.getAddress()+" 是否为OP: "+p.isOp());
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
