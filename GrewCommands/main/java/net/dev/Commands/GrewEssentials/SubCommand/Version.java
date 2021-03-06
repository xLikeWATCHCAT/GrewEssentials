package net.dev.Commands.GrewEssentials.SubCommand;

import net.dev.*;
import net.dev.Utils.CommandUtils.*;
import net.dev.Utils.LogUtils.*;
import net.dev.Utils.PlayerUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class Version implements IChildCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(args.length>=1){
            PlayerUtil.sendMessage(sender,GrewEssentials.getInstance().Message.getString("Version.Message"));
            String a;
            if(sender instanceof Player){
                a = sender.getName();
            }else{
                a = GrewEssentials.getInstance().log.getString("Console");
            }
            LogUtils.writeLog(StringUtils.removeColorCodes(GrewEssentials.getInstance().log.getString("Version").replace("$player",a)));
        }
        return true;
    }
    @Override
    public String getPermission() { return GrewEssentials.getInstance().Config.getString("Permissions.Version"); }
}
