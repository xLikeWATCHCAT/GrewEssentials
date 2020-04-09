package net.dev.Commands;

import net.dev.*;
import net.dev.Utils.CommandUtils.*;
import net.dev.Utils.LogUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;
import java.util.stream.*;

import static net.dev.File.Utils.ReloadUtils.*;
import static net.dev.Utils.StringUtils.TabListType.*;

public class ReloadCommand implements CommandWithCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Reload"))|| sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))){
            if(args.length < 1){
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
                String file= args[0],a;
                if(sender instanceof Player){
                    a = sender.getName();
                }else{
                    a = GrewEssentials.getInstance().log.getString("Console");
                }
                LogUtils.writeLog(StringUtils.removeColorCodes(GrewEssentials.getInstance().log.getString("Reload").replace("$player",a)));
                chooseReload(sender,file);
                return true;
            }
        }else{
            sender.sendMessage(StringUtils.DoNotHavePerMission);
            return true;
        }
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length==1)
            return getFileType().parallelStream().filter(i->i.toLowerCase(Locale.ENGLISH).startsWith(args[0].toLowerCase(Locale.ENGLISH))).collect(Collectors.toList());
        else return ListUtil.toList();
    }
}
