package net.dev.Commands.GrewEssentials;

import net.dev.Commands.GrewEssentials.SubCommand.*;
import net.dev.*;
import net.dev.Utils.CommandUtils.*;
import net.dev.Utils.PlayerUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

import static net.dev.Utils.CommandUtils.CommandUtil.ArgumentUtil.*;
import static net.dev.Utils.LogUtils.LogUtils.*;
import static net.dev.Utils.StringUtils.StringUtils.*;
import static net.dev.Utils.StringUtils.TabListType.*;

public class GrewEssentialsCommand implements CommandWithCompleter {
    private static volatile Vector<IChildCommand> GrewEssentialsCmds=new Vector<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int helpPage=0;
        if(args.length>0) {
            if(isInteger(args[0])) {
                helpPage=Integer.valueOf(args[0])-1;
            }else {
                IChildCommand c=CommandUtil.getCommand(GrewEssentialsCmds,args[0]);
                if(c == null){
                    sender.sendMessage(getCommandInfo("grewessentials"));
                    return true;
                }
                if(!Objects.requireNonNull(c).getPermission().trim().equals("") && !sender.hasPermission(c.getPermission())||!sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All")))
                {
                    sender.sendMessage(StringUtils.DoNotHavePerMission);
                    return true;
                }
                if(!c.onCommand(sender, command, label, args))
                {
                    sender.sendMessage(getCommandInfo("grewessentials"));
                }
                return true;
            }
        }
        if (sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Help"))||sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))) {
            if(helpPage+1>(long)Math.ceil(RegisterCommands.cmds.size()/(double)7) || helpPage<0)
            {
                sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Help.Cant_Find")).replace("$prefix",StringUtils.Prefix));
                return true;
            }
        sender.sendMessage(getPage(RegisterCommands.cmds,helpPage).toString());
        if(sender instanceof Player && helpPage>0)
        {
            String Previous = "{\"text\":\""+StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Help.Previous")).replace("\\","\\\\").replace("\"","\\\"")+"\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/grewessentials "+helpPage+"\"}}";
            PlayerUtil.sendTextComponent((Player)sender,Previous.replace("$prefix",StringUtils.Prefix));
        }
        if(sender instanceof Player && helpPage+1<(long)Math.ceil(RegisterCommands.cmds.size()/(double)7))
        {
            String Next_Page = "{\"text\":\""+StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Help.Next_Page")).replace("\\","\\\\").replace("\"","\\\"")+"\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/grewessentials "+(helpPage+2)+"\"}}";
            PlayerUtil.sendTextComponent((Player)sender,Next_Page.replace("$prefix",StringUtils.Prefix));
        }
        if(sender instanceof Player){
            writeLog("玩家: "+ sender.getName()+" 查看了插件的帮助。 UUID: "+((Player) sender).getUniqueId()+" 是否为OP: "+ sender.isOp());
        }
        }else{
            sender.sendMessage(StringUtils.DoNotHavePerMission);
        }
        return true;
    }

    public static StringBuilder getPage(Vector<Command> commands, int page ){
        StringBuilder ret=new StringBuilder();
        ret = ret.append("\n"+StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Help.Help_First_Message")).replace("$page_now",String.valueOf(page+1)).replace("$pluginname", StringUtils.PluginName).replace("$page_all",String.valueOf((long)Math.ceil(commands.size()/(double)7))).replace("$prefix",StringUtils.Prefix)+"\n");
        //ret=ret.append("§e§l◎§b§lGrewEntials§r------第§e§l"+(page+1)+"§r页--------------\n");
        for(int i=7*page;i<7*page+7&&i<commands.size();i++) {
            ret = ret.append(StringUtils.translateColorCodes("&r&a" + commands.get(i).getUsage() + " "+StringUtils.HelpMid+" &7" + commands.get(i).getDescription()).replace("$prefix",StringUtils.Prefix)+"\n");
        }
        //ret = ret.append(StringUtils.Prefix.replace("$pluginname", StringUtils.PluginName)+StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Help.Help_End_Message"))+"\n");
        // ret=ret.append("§r----------§b"+GrewEntails.getInstance().getName()+"§r----------------\n");
        return ret;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length>0)
            return TabUtil.betterGetStartsWithList(realOnTabComplete(sender,command,alias,args),args[args.length-1]);
        else
            return realOnTabComplete(sender,command,alias,args);
    }

    public void addChildCmd(IChildCommand c) { GrewEssentialsCmds.add(c); }

    public void initChildCommands()
    {
        addChildCmd(new Reload());
        addChildCmd(new Version());
    }
    public List<String> realOnTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length<=1)
            return VectorUtil.toArrayList(CommandUtil.commandListToCommandNameList(VectorUtil.toVector(GrewEssentialsCmds.parallelStream().filter(i->sender.hasPermission(i.getPermission())||sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))).toArray(IChildCommand[]::new))));
        IChildCommand c=CommandUtil.getCommand(GrewEssentialsCmds, args[0]);
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
                }else if(desc.equalsIgnoreCase("filetype")){
                    return getFileType();
                }else{
                    return ListUtil.toList(desc);
                }
            }
        }
        return new ArrayList<>();
    }
}
