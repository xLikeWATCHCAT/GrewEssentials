package net.dev.Commands.Server.SubCommand;

import net.dev.*;
import net.dev.Utils.CommandUtils.*;
import net.dev.Utils.PlayerUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.*;
import org.bukkit.command.*;

import java.text.*;
import java.util.*;

public class Info implements IChildCommand {
    private static SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd kk:mm Z");
    private Date date = new Date();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(args.length >= 1){
            try{
                Runtime runtime = Runtime.getRuntime();
                String generationTime = dateFmt.format(this.date);
                String pluginVersion = GrewEssentials.getInstance().getDescription().getVersion();
                String onlinePlayers = String.valueOf(Bukkit.getServer().getOnlinePlayers().size());
                String javaInfo =  System.getProperty("java.version");
                String operatingSystem = System.getProperty("os.name");
                String numberOfProcessors = String.valueOf(runtime.availableProcessors());
                String freeMemory = runtime.freeMemory() / 1024L / 1024L + " MB";
                String maximumMemory = runtime.maxMemory() / 1024L / 1024L + " MB";
                String totalMemory = runtime.totalMemory() / 1024L / 1024L + " MB";
                String server = Bukkit.getServer().getServerId();
                String serverName = Bukkit.getServer().getServerName();
                String serverVersion = GrewEssentials.version;

                String configGenerationTime = GrewEssentials.getInstance().Message.getString("ServerManager.Info.GenerationTime");
                String configPluginVersion = GrewEssentials.getInstance().Message.getString("ServerManager.Info.PluginVersion");
                String configJavaInfo = GrewEssentials.getInstance().Message.getString("ServerManager.Info.JavaInfo");
                String configOperatingSystem = GrewEssentials.getInstance().Message.getString("ServerManager.Info.OperatingSystem");
                String configNumberOfProcessors = GrewEssentials.getInstance().Message.getString("ServerManager.Info.NumberOfProcessors");
                String configFreeMemory = GrewEssentials.getInstance().Message.getString("ServerManager.Info.FreeMemory");
                String configMaximumMemory = GrewEssentials.getInstance().Message.getString("ServerManager.Info.MaximumMemory");
                String configTotalMemory = GrewEssentials.getInstance().Message.getString("ServerManager.Info.TotalMemory");
                String configServer = GrewEssentials.getInstance().Message.getString("ServerManager.Info.Server");
                String configServerName = GrewEssentials.getInstance().Message.getString("ServerManager.Info.ServerName");
                String configServerVersion = GrewEssentials.getInstance().Message.getString("ServerManager.Info.ServerVersion");
                String configOnlinePlayers = GrewEssentials.getInstance().Message.getString("ServerManager.Info.OnlinePlayers");
                String configEnd = GrewEssentials.getInstance().Message.getString("ServerManager.Info.End");

                PlayerUtil.sendMessage(sender,GrewEssentials.getInstance().Message.getString("ServerManager.Info.InfoGui")
                        .replace("$generationtime",configGenerationTime)
                        .replace("$pluginversion",configPluginVersion)
                        .replace("$onlineplayers",configOnlinePlayers)
                        .replace("$javainfo",configJavaInfo)
                        .replace("$operatingsystem",configOperatingSystem)
                        .replace("$numberofprocessors",configNumberOfProcessors)
                        .replace("$freememory",configFreeMemory)
                        .replace("$maximummemory",configMaximumMemory)
                        .replace("$totalmemory",configTotalMemory)
                        .replace("$Server",configServer)
                        .replace("$servername",configServerName)
                        .replace("$serverversion",configServerVersion)
                        .replace("$end",configEnd)
                        .replace("$reportgenerationtime",generationTime)
                        .replace("$reportpluginversion",pluginVersion)
                        .replace("$reportonlineplayers",onlinePlayers)
                        .replace("$reportjavainfo",javaInfo)
                        .replace("$reportoperatingsystem",operatingSystem)
                        .replace("$reportnumberofprocessors",numberOfProcessors)
                        .replace("$reportfreememory",freeMemory)
                        .replace("$reportmaximummemory",maximumMemory)
                        .replace("$reporttotalmemory",totalMemory)
                        .replace("$reportServer",server)
                        .replace("$reportservername",serverName)
                        .replace("$reportserverversion",serverVersion)
                );
            }catch (Throwable e){
                PlayerUtil.sendMessage(sender,GrewEssentials.getInstance().Message.getString("ServerManager.Info.Error"));
            }
        }else{
            sender.sendMessage(StringUtils.getCommandInfo("server"));
        }
        return true;
    }

    @Override
    public String getPermission() { return GrewEssentials.getInstance().Config.getString("Permissions.ServerManager"); }
}
