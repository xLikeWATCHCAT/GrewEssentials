package net.dev.Commands.Server.SubCommand;

import net.dev.*;
import net.dev.Utils.*;
import net.dev.Utils.CommandUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.command.*;

public class Info implements IChildCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(args.length >= 2){

        }else{
            sender.sendMessage(StringUtils.getCommandInfo("server"));
        }
        return true;
    }

    @Override
    public String getPermission() { return GrewEssentials.getInstance().Config.getString("Permissions.ServerManager"); }
}
