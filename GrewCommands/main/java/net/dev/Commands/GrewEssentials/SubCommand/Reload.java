package net.dev.Commands.GrewEssentials.SubCommand;

import net.dev.*;
import net.dev.Utils.CommandUtils.*;
import net.dev.Utils.LogUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

import static net.dev.File.Utils.ReloadUtils.*;

public class Reload implements IChildCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(args.length == 1){
            String mess = StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Reload.Message").replace("$prefix", StringUtils.Prefix).replace("$pluginname",StringUtils.PluginName));
            String a;
            if(sender instanceof Player){
                a = sender.getName();
            }else{
                if(GrewEssentials.getInstance()!=null){
                    a = GrewEssentials.getInstance().log.getString("Console");
                } else{
                    a="Console";
                }
            }
            if(GrewEssentials.getInstance()!=null){
                LogUtils.writeLog(StringUtils.removeColorCodes(GrewEssentials.getInstance().log.getString("Reload").replace("$player",a)));
                sender.sendMessage(mess);
            }
            try{
                reloadAll();
            }catch (Throwable e){}
            return true;
        }else{
            String file= args[1];
            String a;
            if(sender instanceof Player){
                a = sender.getName();
            }else{
                a = GrewEssentials.getInstance().log.getString("Console");
            }
            try{
                LogUtils.writeLog(StringUtils.removeColorCodes(GrewEssentials.getInstance().log.getString("Reload").replace("$player",a)));
            }catch (Throwable e){}
            chooseReload(sender,file);
            return true;
        }
    }
    @Override
    public Vector<Class<?>> getArgumentsTypes() { return VectorUtil.toVector(String.class); }
    @Override
    public Vector<String> getArgumentsDescriptions()
    {
        return VectorUtil.toVector("filetype");
    }
    @Override
    public String getPermission() { return GrewEssentials.getInstance().Config.getString("Permissions.Reload"); }
}
