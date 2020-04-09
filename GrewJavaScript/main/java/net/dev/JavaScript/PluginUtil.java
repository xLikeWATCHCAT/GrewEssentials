package net.dev.JavaScript;

import net.dev.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.event.*;
import org.mozilla.javascript.*;

import java.util.*;

import static net.dev.Utils.CommandUtils.RegisterCommands.*;

public class PluginUtil extends ScriptableObject {
    @Override
    public String getClassName() {
        return "PluginUtil";
    }
    public GrewEssentials jsFunction_getPluginInstance()
    {
        return GrewEssentials.instance;
    }
    public static String[] nativeArrayToStringArray(NativeArray a)
    {
        Object[] tmp=a.parallelStream().toArray(Object[]::new);
        String[] ret=new String[tmp.length];
        for(int i=0;i<tmp.length;i++)
            ret[i]=String.valueOf(tmp[i]);
        return ret;
    }
    public Command jsFunction_registerCommand(Scriptable s)
    {
        if(s instanceof ScriptableObject)
        {
            ScriptableObject so=(ScriptableObject)s;
            if(so.has("name",so))
            {
                Command retc= setTabCom(setComUsa(setComPerM(setComPer(setComDesc(setComAlias(newPluginCommand(so.get("name",so).toString(),so.has("executor",so)?(commandSender, command, str, strings)->{
                    Function f=(Function)so.get("executor",so);
                    Object o=f.call(GrewEssentials.instance.context,f,so,new Object[]{commandSender,command,str,strings});
                    return (o!=null&&!Objects.equals(Undefined.instance,o))?(boolean)o:false;
                }:GrewEssentials.instance,GrewEssentials.instance),so.has("alias",so)?nativeArrayToStringArray((NativeArray) so.get("alias",so)):new String[0]),so.has("description",so)?so.get("description",so).toString():""),so.has("permission",so)?so.get("permission",so).toString():null),so.has("permission-message",so)?so.get("permission-message",so).toString():null),so.has("usage",so)?so.get("usage",so).toString():""),so.has("completer",so)?(commandSender,command,str,strings)->{
                    Function f=(Function)so.get("completer",so);
                    Object o=f.call(GrewEssentials.instance.context,f,so,new Object[]{commandSender,command,str,strings});
                    return (o!=null&&!Objects.equals(Undefined.instance,o))?(List<String>) o:new ArrayList<>();
                }:GrewEssentials.instance);
                regJsCom(so.has("prefix",so)?so.get("prefix",so).toString():GrewEssentials.instance.getName(),retc);
                return retc;
            }
        }
        return null;
    }
    public void jsFunction_registerEvent(Scriptable s)
    {
        if(s.has("type",s))
        {
            Bukkit.getPluginManager().registerEvent((Class<? extends Event>) ((NativeJavaClass) s.get("type", s)).unwrap(), new Listener() {},s.has("priority", s)?(EventPriority)((NativeJavaObject)s.get("priority",s)).unwrap():EventPriority.NORMAL,(l,e)->{
                Function f=(Function)s.get("onEvent",s);
                f.call(GrewEssentials.instance.context,f,s,new Object[]{e});
            },GrewEssentials.instance,s.has("ignoreCancelled", s)?(boolean)s.get("ignoreCancelled",s):false);
        }
    }
}
