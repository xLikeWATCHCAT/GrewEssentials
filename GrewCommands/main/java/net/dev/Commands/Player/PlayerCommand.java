package net.dev.Commands.Player;

import net.dev.Commands.Player.SubCommand.UUID;
import net.dev.Commands.Player.SubCommand.*;
import net.dev.*;
import net.dev.Utils.CommandUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.command.*;

import java.util.*;

import static net.dev.Utils.StringUtils.TabListType.*;

public class PlayerCommand implements CommandWithCompleter {
    private static volatile Vector<IChildCommand> PlayerCmds=new Vector<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))||sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.ServerManager"))){
            if(args.length>0)
            {
                IChildCommand c=CommandUtil.getCommand(PlayerCmds,args[0]);
                if(c==null)
                {
                    sender.sendMessage(StringUtils.getCommandInfo("player"));
                }else {
                    if(!c.getPermission().trim().equals("") && !sender.hasPermission(c.getPermission()))
                    {
                        sender.sendMessage(StringUtils.DoNotHavePerMission);
                        return true;
                    }
                    if(!c.onCommand(sender, command, label, args))
                    {
                        String usage=c.getUsage();
                        if(!usage.trim().equals(""))
                            sender.sendMessage(usage);
                    }
                }
                return true;
            }else{
                sender.sendMessage(StringUtils.getCommandInfo("player"));
            }
        }else{
            sender.sendMessage(StringUtils.DoNotHavePerMission);
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length>0)
            return TabUtil.betterGetStartsWithList(realOnTabComplete(sender,command,alias,args),args[args.length-1]);
        else
            return realOnTabComplete(sender,command,alias,args);
    }
    public void addChildCmd(IChildCommand c) { PlayerCmds.add(c); }

    public void initChildCommands()
    {
        addChildCmd(new Name());
        addChildCmd(new UUID());
    }
    public List<String> realOnTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length<=1)
            return VectorUtil.toArrayList(CommandUtil.commandListToCommandNameList(VectorUtil.toVector(PlayerCmds.parallelStream().filter(i->sender.hasPermission(i.getPermission())||sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))).toArray(IChildCommand[]::new))));
        IChildCommand c=CommandUtil.getCommand(PlayerCmds, args[0]);
        if(c!=null)
        {
            if(!sender.hasPermission(c.getPermission()))
                return ListUtil.toList();
            Vector<Class<?>> cats=c.getArgumentsTypes();
            if(cats.size()>args.length-2)
            {
                Class<?> argType=cats.get(args.length-2);
                Vector<String> des=c.getArgumentsDescriptions();
                String desc=null;
                if(des.size()>args.length-2)
                    desc=des.get(args.length-2);
                if(desc==null)
                {
                    return ListUtil.toList(argType.getSimpleName());
                }else if(desc.equalsIgnoreCase("onlineplayeruuid")){
                    return getOnlinePlayersNameUUIDList();
                }else if(desc.equalsIgnoreCase("onlineplayer")){
                    return getOnlinePlayersNameList();
                }else{
                    return ListUtil.toList(desc);
                }
            }
        }
        return new ArrayList<>();
    }
}
