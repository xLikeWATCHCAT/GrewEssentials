package net.dev.Commands;

import net.dev.*;
import net.dev.Utils.LogUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class KickAllCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(GrewEssentials.getInstance().Message.getBoolean("KickAll.Enable")){
            if (command.getName().equalsIgnoreCase("kickall")) {
                if (sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.KickAll")) || sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))){
                    //如果除了执行者没有其他玩家就没必要运行下面的代码了

                    if((sender instanceof Player && Bukkit.getServer().getOnlinePlayers().size() == 1)//由玩家执行的情况
                            || Bukkit.getServer().getOnlinePlayers().size() == 0){//由后台或命令方块执行的情况
                        sender.sendMessage(StringUtils.NoHavePlayer);
                        return true;
                    }

                    StringBuilder reason = new StringBuilder();//使用StringBuilder处理String合并会更快
                    if (args.length > 0) {//如果有写理由
                        //理由可能出现空格，遍历所有理由
                        for(int i = 0; i < args.length; i++){
                            reason.append(args[i]);
                            if(i != args.length-1)//不是最后一行的话
                                reason.append(" ");//每遍历一遍加上空格
                        }
                    } else {//如果没写理由
                        reason.append(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Kick.Default")));
                    }
                    for(Player onlineplayer : Bukkit.getServer().getOnlinePlayers()) {//遍历所有在线玩家
                        //由于Player和ConsoleSender都实现了CommandSender接口，因此可直接判断目标玩家是否为输入者
                        //若输入者是后台，因为后台不是在线玩家，因此会踢出所有玩家
                        if(sender instanceof Player)
                            LogUtils.writeLog(StringUtils.removeColorCodes(GrewEssentials.getInstance().log.getString("KickAll").replace("$player",sender.getName()).replace("$playeruuid",((Player) sender).getUniqueId().toString()).replace("$playerip", ((Player) sender).getAddress().getHostName()).replace("$playerisop",String.valueOf(sender.isOp())).replace("$size",String.valueOf(Bukkit.getOnlinePlayers().size())).replace("$size",String.valueOf(Bukkit.getOnlinePlayers().size()-1))));
                        if(GrewEssentials.getInstance().Message.getBoolean("KickAll.KickExceptSelf")){
                            onlineplayer.kickPlayer(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Kick.Message_Gui"))
                                    .replace("$prefix", StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Prefix")))
                                    .replace("$reason", StringUtils.translateColorCodes(reason.toString()))
                                    .replace("\\n", "\n"));
                        }else{
                            if (onlineplayer != sender) {
                                onlineplayer.kickPlayer(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Kick.Message_Gui"))
                                        .replace("$prefix", StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Prefix")))
                                        .replace("$reason", StringUtils.translateColorCodes(reason.toString()))
                                        .replace("\\n", "\n"));
                            }
                        }
                    }
                    sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("KickAll.All")).replace("$prefix",StringUtils.Prefix));//成功踢出
                    return true;
                }else {
                    sender.sendMessage(StringUtils.DoNotHavePerMission);
                    return true;
                }
            }
        }else{
            sender.sendMessage(StringUtils.DoNotHavePerMission);
        }
        return true;
    }
}
