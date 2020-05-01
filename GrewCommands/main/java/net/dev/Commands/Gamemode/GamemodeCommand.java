package net.dev.Commands.Gamemode;

import net.dev.Commands.Gamemode.processor.*;
import net.dev.*;
import net.dev.Utils.CommandUtils.*;
import net.dev.Utils.StringUtils.*;
import net.dev.Utils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;
import java.util.stream.*;

import static net.dev.Utils.StringUtils.StringUtils.*;
import static net.dev.Utils.StringUtils.TabListType.*;

public class GamemodeCommand implements CommandWithCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(GrewEssentials.getInstance().Message.getBoolean("Gamemode.Enable")){
            if(sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))||sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Gamemode.All"))){
                if (args.length >= 2) {
                    Player n = Bukkit.getPlayer(args[1]);
                    if(n == null){
                        Utils.notOnline(sender,args[1]);
                        return true;
                    }
                    gamemodeChange(n,sender,args);
                }else{
                    if(args.length == 1){
                        if (!(sender instanceof Player)) {
                            sender.sendMessage(StringUtils.OnlyPlayer);
                            return true;
                        }
                        Player p = (Player) sender;
                        gamemodeChange(p,sender,args);
                    }else{
                        sender.sendMessage(getCommandInfo("gamemode"));
                    }
                }
            }
        }else{
            sender.sendMessage(StringUtils.DoNotHavePerMission);
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length==1)
            return ListUtil.toList("adventure","creative","spectator","survival","0","1","2","3").parallelStream().filter(i->i.toLowerCase(Locale.ENGLISH).startsWith(args[0].toLowerCase(Locale.ENGLISH))).collect(Collectors.toList());
        else if(args.length==2)
            return getOnlinePlayersNameList();
        else return ListUtil.toList();
    }
    private static void gamemodeChange(Player dop, CommandSender sender, String[] args){
        if(args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("ADVENTURE") || args[0].equalsIgnoreCase("冒险") || args[0].equalsIgnoreCase("a")){
            if(sender.hasPermission(ADVENTURE.getPermission())){
                ADVENTURE.setPlayerGamemodeToADVENTURE(dop,sender);
            }else{
                sender.sendMessage(StringUtils.DoNotHavePerMission);
            }
        }else if(args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("CREATIVE") || args[0].equalsIgnoreCase("创造") || args[0].equalsIgnoreCase("c")){
            if(sender.hasPermission(CREATIVE.getPermission())){
                CREATIVE.setPlayerGamemodeToCREATIVE(dop,sender);
            }else{
                sender.sendMessage(StringUtils.DoNotHavePerMission);
            }
        }else if(args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("SPECTATOR") || args[0].equalsIgnoreCase("观察者") || args[0].equalsIgnoreCase("sp")){
            if(sender.hasPermission(SPECTATOR.getPermission())){
                SPECTATOR.setPlayerGamemodeToSPECTATOR(dop,sender);
            }else{
                sender.sendMessage(StringUtils.DoNotHavePerMission);
            }
        }else if(args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("SURVIVAL") || args[0].equalsIgnoreCase("生存") || args[0].equalsIgnoreCase("s")){
            if(sender.hasPermission(SURVIVAL.getPermission())){
                SURVIVAL.setPlayerGamemodeToSURVIVAL(dop,sender);
            }else{
                sender.sendMessage(StringUtils.DoNotHavePerMission);
            }
        }else{
            sender.sendMessage(getCommandInfo("gamemode"));
        }
    }
}
