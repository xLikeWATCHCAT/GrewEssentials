package net.dev.Commands;

import net.dev.*;
import net.dev.Utils.LogUtils.*;
import net.dev.Utils.PlayerUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class SignCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(GrewEssentials.getInstance().Message.getBoolean("ChangeSign.Enable")){
            if(sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))||sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.ChangeSign"))){
                if (!(sender instanceof Player)) {
                    sender.sendMessage(StringUtils.OnlyPlayer);
                    return true;
                }else{
                    Player p = (Player) sender;
                    if(args.length >=2){
                        int line;
                        try {
                            line = Integer.parseInt(args[0]);
                        } catch (Exception var9) {
                            PlayerUtil.sendMessage(p,GrewEssentials.getInstance().Message.getString("ChangeSign.UnKnowLine").replace("$line",args[0]));
                            return true;
                        }
                        if (line >= 1 && line <= 4) {
                            Block block = p.getTargetBlock((Set)null, 5);
                            if (block == null || block.getType() != Material.SIGN_POST && block.getType() != Material.WALL_SIGN) {
                                PlayerUtil.sendMessage(p,GrewEssentials.getInstance().Message.getString("ChangeSign.NotASign"));
                            }else{
                                Sign sign;
                                try {
                                    sign = (Sign)block.getState();
                                } catch (Exception var8) {
                                    PlayerUtil.sendMessage(p,GrewEssentials.getInstance().Message.getString("ChangeSign.NotASign"));
                                    return true;
                                }
                                StringBuilder var6 = new StringBuilder();

                                for(int var7 = 1; var7 < args.length; ++var7) {
                                    var6.append(' ');
                                    var6.append(args[var7].replace('&', '§').replace("§§", "&"));
                                }

                                String var10 = var6.toString().substring(1, var6.length());
                                sign.setLine(line - 1, var10);
                                PlayerUtil.sendMessage(p,GrewEssentials.getInstance().Message.getString("ChangeSign.Success").replace("$line",args[0]).replace("$content",var10));
                                sign.update();
                                LogUtils.writeLog(GrewEssentials.getInstance().log.getString("Sign").replace("$player",p.getName()).replace("$playeruuid",p.getUniqueId().toString()).replace("$playerip",p.getAddress().getHostName()).replace("$playerisop",String.valueOf(p.isOp())).replace("$playerlocation",p.getLocation().toString()));
                            }
                        }else{
                            p.sendMessage(StringUtils.translateColorCodes(p,GrewEssentials.getInstance().Message.getString("ChangeSign.UnKnowLine")).replace("$line",args[0]).replace("$prefix",StringUtils.Prefix));
                            return true;
                        }
                    }else{
                        sender.sendMessage(StringUtils.getCommandInfo("sign"));
                    }
                }
                return true;
            }else{
                sender.sendMessage(StringUtils.DoNotHavePerMission);
            }
        }else{
            sender.sendMessage(StringUtils.DoNotHavePerMission);
        }
        return true;
    }
}
