package net.dev.File.Utils;

import net.dev.*;
import net.dev.Utils.DatabaseUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.command.*;

import java.io.*;

import static net.dev.Manager.GrewPluginManager.*;

public class ReloadUtils {
    public static void reloadConfig(){
        try {
            GrewEssentials.getInstance().Config.reload();
        } catch (IOException e) { }
    }

    public static void reloadMessage(){
        StringUtils.init();
    }

    public static void reloadLog(){
        try {
            GrewEssentials.getInstance().log.reload();
        } catch (IOException e) { }
    }

    public static void reloadBossBar(){
        try {
            GrewEssentials.getInstance().bossbar.reload();
        } catch (IOException e) { }
    }

    public static void reloadData(){
        try {
            GrewEssentials.getInstance().data.loadConfig();
        } catch (IOException e) { }
    }

    public static void reloadDatabase(){
        try {
        LoadDatabase.db.close();
        } catch (Throwable e) { }
        try {
        LoadDatabase.loadDatabase();
        } catch (Throwable e) { }
    }
    public static void reloadPluginForJavaScript()
    {
        try{
            reloadBossBar();
            reloadConfig();
            reloadDatabase();
            reloadLog();
            reloadMessage();
        }catch (Throwable e){}
        try {
            GrewEssentials.getInstance().tryInvokeFunction("onReload");
        }catch(Throwable e){}
        try {
            reloadPlugin(GrewEssentials.getInstance());
        }catch(Throwable e){}
    }
    public static void reloadAll(){
        try{
        reloadBossBar();
        reloadConfig();
        reloadDatabase();
        reloadLog();
        reloadMessage();
        }catch (Throwable e){}
        if(GrewEssentials.getInstance().Message.getBoolean("Reload.ReloadPlugin",false)){
            try {
                GrewEssentials.getInstance().tryInvokeFunction("onReload");
            }catch(Throwable e){}
            try {
                reloadPlugin(GrewEssentials.getInstance());
            }catch(Throwable e){}
        }
    }
        /*try {
            GrewEssentials.getInstance().tryInvokeFunction("onReload");
        }catch(Throwable e){e.printStackTrace();}
        reloadPlugin(GrewEssentials.getInstance());*/
    public static void chooseReload(CommandSender sender, String file){
        try{
            if(file.equalsIgnoreCase("config")){
                reloadConfig();
                sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Reload.File").replace("$prefix", StringUtils.Prefix)).replace("$file",file).replace("$pluginname",StringUtils.PluginName));
            }else if(file.equalsIgnoreCase("message")){
                reloadMessage();
                sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Reload.File").replace("$prefix", StringUtils.Prefix)).replace("$file",file).replace("$pluginname",StringUtils.PluginName));
            }else if(file.equalsIgnoreCase("bossbar")){
                reloadBossBar();
                sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Reload.File").replace("$prefix", StringUtils.Prefix)).replace("$file",file).replace("$pluginname",StringUtils.PluginName));
            }else if(file.equalsIgnoreCase("database")){
                reloadConfig();
                reloadDatabase();
                sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Reload.File").replace("$prefix", StringUtils.Prefix)).replace("$file",file).replace("$pluginname",StringUtils.PluginName));
            }else if(file.equalsIgnoreCase("javascript")||file.equalsIgnoreCase("js")){
                String mess=StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Reload.File").replace("$prefix", StringUtils.Prefix)).replace("$file",file).replace("$pluginname",StringUtils.PluginName);
                reloadPluginForJavaScript();
                sender.sendMessage(mess);
            }else if(file.equalsIgnoreCase("logMessage")||file.equalsIgnoreCase("log")){
                reloadLog();
                sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Reload.File").replace("$prefix", StringUtils.Prefix)).replace("$file","logMessage").replace("$pluginname",StringUtils.PluginName));
            }else if(file.equalsIgnoreCase("data")){
                reloadData();
                sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Reload.File").replace("$prefix", StringUtils.Prefix)).replace("$file",file).replace("$pluginname",StringUtils.PluginName));
            }else{
                String mess=StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Reload.Message").replace("$prefix", StringUtils.Prefix).replace("$pluginname",StringUtils.PluginName));
                try{
                    reloadPluginForJavaScript();
                }catch (Throwable e){}
                sender.sendMessage(mess);
            }
        }catch (Throwable e){
            String mess=StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Reload.Message").replace("$prefix", StringUtils.Prefix).replace("$pluginname",StringUtils.PluginName));
            try{
                reloadAll();
            }catch (Throwable a){}
            sender.sendMessage(mess);
        }
    }
}
