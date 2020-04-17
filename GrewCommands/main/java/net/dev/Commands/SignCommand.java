package net.dev.Commands;

import net.dev.*;
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
                            p.sendMessage(StringUtils.translateColorCodes(p,GrewEssentials.getInstance().Message.getString("ChangeSign.UnKnowLine")).replace("$line",args[0]).replace("$prefix",StringUtils.Prefix));
                            return true;
                        }
                        if (line >= 1 && line <= 4) {
                            Block block = p.getTargetBlock((Set)null, 5);
                            if (block == null || block.getType() != Material.SIGN_POST && block.getType() != Material.WALL_SIGN) {
                                p.sendMessage(StringUtils.translateColorCodes(p,GrewEssentials.getInstance().Message.getString("ChangeSign.NotASign")).replace("$prefix",StringUtils.Prefix));
                            }else{
                                Sign sign;
                                try {
                                    sign = (Sign)block.getState();
                                } catch (Exception var8) {
                                    p.sendMessage(StringUtils.translateColorCodes(p,GrewEssentials.getInstance().Message.getString("ChangeSign.NotASign")).replace("$prefix",StringUtils.Prefix));
                                    return true;
                                }
                                if (args.length == 1) {
                                    sign.setLine(line - 1, "");
                                } else {
                                    StringBuilder var6 = new StringBuilder();

                                    for(int var7 = 1; var7 < args.length; ++var7) {
                                        var6.append(' ');
                                        var6.append(args[var7].replace('&', '§').replace("§§", "&"));
                                    }

                                    String var10 = var6.toString().substring(1, var6.length());
                                    sign.setLine(line - 1, var10);
                                }

                                sign.update();
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
