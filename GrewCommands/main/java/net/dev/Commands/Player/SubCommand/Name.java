package net.dev.Commands.Player.SubCommand;

import net.dev.API.*;
import net.dev.*;
import net.dev.Utils.CommandUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.command.*;

import java.util.*;

public class Name implements IChildCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(args.length == 2){
            String name = args[1];
            try{
                sender.sendMessage(StringUtils.translateColorCodes(
                        GrewEssentials.getInstance().Message.getString("PlayerInfo.NameMessageGui"))
                        .replace("$uuid", PlayerInfo.getPlayerUUID(name))
                        .replace("$playername",name)
                        .replace("$realname",PlayerInfo.getPlayerName(PlayerInfo.getPlayerUUID(name)))
                        .replace("$ip",PlayerInfo.getPlayerIP(name)));
            }catch (Throwable e){
                sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("PlayerInfo.Failed")).replace("$playername",name).replace("$name",name).replace("$info",name));
            }
            }else{
            sender.sendMessage(StringUtils.getCommandInfo("player"));
        }
        return true;
    }
    @Override
    public Vector<Class<?>> getArgumentsTypes() { return VectorUtil.toVector(String.class); }
    @Override
    public Vector<String> getArgumentsDescriptions()
    {
        return VectorUtil.toVector("player");
    }
    @Override
    public String getPermission() { return GrewEssentials.getInstance().Config.getString("Permissions.PlayerInfo"); }
}
