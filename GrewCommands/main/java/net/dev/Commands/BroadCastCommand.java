package net.dev.Commands;

import net.dev.*;
import net.dev.Utils.LogUtils.*;
import net.dev.Utils.StringUtils.*;
import net.dev.Utils.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class BroadCastCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(GrewEssentials.getInstance().Message.getBoolean("BroadCast.Enable")){
                if (sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.BroadCast")) || sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))){
                    StringBuilder bc = new StringBuilder();//使用StringBuilder处理String合并会更快
                    if (args.length > 0) {//如果有写
                        //理由可能出现空格，遍历所有
                        for(int i = 0; i < args.length; i++){
                            bc.append(args[i]);
                            if(i != args.length-1)//不是最后一行的话
                                bc.append(" ");//每遍历一遍加上空格
                            //AtomicInteger boosbarID = sendBossbar((Player) sender,bc.toString(),800);
                            //removeBossbar(boosbarID);
                            Utils.BroadCast("&E"+String.valueOf(bc).replace("$prefix", StringUtils.Prefix));
                            LogUtils.writeLog(StringUtils.removeColorCodes(GrewEssentials.getInstance().log.getString("BroadCast").replace("$player",sender.getName()).replace("$playeruuid",((Player) sender).getUniqueId().toString()).replace("$playerip", ((Player) sender).getAddress().getHostName()).replace("$playerisop",String.valueOf(sender.isOp()))));
                        }
                    } else {//如果没写
                        sender.sendMessage(StringUtils.getCommandInfo("broadcast"));
                        // GrewEssentials.writeLog("玩家: "+p.getName()+"想使用BroadCast，但是他输入的格式错误，访问无法进行。"+" UUID: "+p.getUniqueId()+" IP: "+p.getAddress()+" 是否为OP: "+p.isOp());
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
