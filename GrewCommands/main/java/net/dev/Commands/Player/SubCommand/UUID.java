package net.dev.Commands.Player.SubCommand;

import net.dev.API.*;
import net.dev.*;
import net.dev.Utils.CommandUtils.*;
import net.dev.Utils.PlayerUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.command.*;

import java.util.*;

public class UUID implements IChildCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(args.length == 2){
            String uuid = args[1];
            try{
                PlayerUtil.sendMessage(sender,GrewEssentials.getInstance().Message.getString("PlayerInfo.UUIDMessageGui").replace("$uuid", uuid).replace("$realname",PlayerInfo.getPlayerName(uuid)).replace("$ip",PlayerInfo.getPlayerIPUUID(uuid)));
            }catch (Throwable e){
                PlayerUtil.sendMessage(sender,GrewEssentials.getInstance().Message.getString("PlayerInfo.Failed").replace("$playername",uuid).replace("$name",uuid).replace("$info",uuid));
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
        return VectorUtil.toVector("onlineplayeruuid");
    }
    @Override
    public String getPermission() { return GrewEssentials.getInstance().Config.getString("Permissions.PlayerInfo"); }
}
