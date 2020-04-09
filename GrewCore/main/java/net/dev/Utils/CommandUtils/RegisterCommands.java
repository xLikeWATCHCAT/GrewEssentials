package net.dev.Utils.CommandUtils;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.plugin.*;

import java.lang.reflect.*;
import java.util.*;

public class RegisterCommands {
    public static Vector<Command> cmds=new Vector<>();
    public static PluginCommand setTabCom(PluginCommand pc,TabCompleter tc)
    {
        pc.setTabCompleter(tc);
        return pc;
    }
    public static PluginCommand setComDesc(PluginCommand pc,String desc)
    {
        pc.setDescription(desc);
        return pc;
    }
    public static PluginCommand setComPerM(PluginCommand pc, String m)
    {
        pc.setPermissionMessage(m);
        return pc;
    }

    public static PluginCommand setComUsa(PluginCommand pc,String u)
    {
        pc.setUsage(u);
        return pc;
    }

    public static PluginCommand setComPer(PluginCommand pc,String p)
    {
        pc.setPermission(p);
        return pc;
    }

    public static PluginCommand setComAlias(PluginCommand pc,String... alias)
    {
        List<String> a=new ArrayList<>();
        for(String i : alias)
            a.add(i);
        pc.setAliases(a);
        return pc;
    }
    public static void regJsCom(String prefix, Command c)
    {
        try {
            Field cmap= Bukkit.getServer().getClass().getDeclaredField("commandMap");
            cmap.setAccessible(true);
            SimpleCommandMap scmap=(SimpleCommandMap) cmap.get(Bukkit.getServer());
            scmap.register(prefix,c);
        }catch(Throwable e){throw new RuntimeException(e);}
    }
    public static void regCom(String prefix,Command c)
    {
        try {
            Field cmap= Bukkit.getServer().getClass().getDeclaredField("commandMap");
            cmap.setAccessible(true);
            SimpleCommandMap scmap=(SimpleCommandMap) cmap.get(Bukkit.getServer());
            scmap.register(prefix,c);
            cmds.add(c);
        }catch(Throwable e){throw new RuntimeException(e);}
    }
    public static void regComWithCompleter(String prefix,PluginCommand c)
    {
            regCom(prefix,setTabCom(c,(TabCompleter)c.getExecutor()));
    }

    public static PluginCommand newPluginCommand(String name, CommandExecutor ce, Plugin plugin)
    {
        try {
            PluginCommand pc=newPluginCommand(name,plugin);
            pc.setExecutor(ce);
            return pc;
        }catch(Throwable e){throw new RuntimeException(e);}
    }

    public static PluginCommand newPluginCommand(String name, Plugin plugin)
    {
        try {
            Constructor<PluginCommand> c=PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);
            return c.newInstance(name,plugin);
        }catch(Throwable e){throw new RuntimeException(e);}
    }
}
