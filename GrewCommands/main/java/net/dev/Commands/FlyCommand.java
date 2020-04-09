package net.dev.Commands;

import net.dev.Database.*;
import net.dev.*;
import net.dev.Utils.DatabaseUtils.*;
import net.dev.Utils.LogUtils.*;
import net.dev.Utils.StringUtils.*;
import net.dev.Utils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class FlyCommand implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(GrewEssentials.getInstance().Message.getBoolean("Fly.Enable")){
            if (command.getName().equalsIgnoreCase("Fly")) {
                    if (args.length >= 1) {
                        if (sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Fly.Other")) || sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))){
                            Player t =  Bukkit.getPlayer(args[0]);
                            if (t != null) {
                                setFly(t,sender);
                            }else{
                                Utils.notOnline(sender,args[0]);
                                return true;
                            }
                        }else {
                            sender.sendMessage(StringUtils.DoNotHavePerMission);
                            return true;
                        }
                    } else {
                        if (sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Fly.Self")) || sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))){
                            if (!(sender instanceof Player)) {
                                sender.sendMessage(StringUtils.OnlyPlayer);
                                return true;
                            } else {
                                Player that = (Player) sender;
                                setFly(that,that);
                            }
                            return true;
                        }else {
                            sender.sendMessage(StringUtils.DoNotHavePerMission);
                            return true;
                        }
                    }
            }
        }else {
            sender.sendMessage(StringUtils.DoNotHavePerMission);
            return true;
        }
        return true;
    }
    public static void setFly(Player t, CommandSender s){
        String fly= LoadDatabase.db.dbSelectFirst("CommandsEnable","fly",new KeyValue(){{ this.add("uuid",t.getUniqueId().toString()); }});
        String vanish= LoadDatabase.db.dbSelectFirst("CommandsEnable","vanish",new KeyValue(){{ this.add("uuid",t.getUniqueId().toString()); }});
        if(vanish == null)
            vanish = "0";
        String pn,puuid,pip;
        if(s instanceof Player){
            pn = GrewEssentials.getInstance().log.getString("Console");
            puuid = "无";
            pip = "无";
        } else{
            pn = s.getName();
            puuid = ((Player) s).getUniqueId().toString();
            pip = ((Player) s).getAddress().getHostName();
        }
        if (t.getAllowFlight()) {
            if (t.getGameMode() == GameMode.CREATIVE) {
                s.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Fly.Gamemode_Creative")).replace("$playername",t.getName()).replace("$prefix", StringUtils.Prefix));
            } else if (t.getGameMode() == GameMode.SPECTATOR) {
                s.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Fly.Gamemode_Spectator")).replace("$playername",t.getName()).replace("$prefix", StringUtils.Prefix));
            }else {
                t.setAllowFlight(false);
                if(fly==null) {
                    String finalVanish = vanish;
                    LoadDatabase.db.dbInsert("CommandsEnable",new KeyValue(){{
                        this.add("uuid",t.getUniqueId().toString());
                        this.add("fly","0");
                        this.add("vanish", finalVanish);
                    }});
                }
                else
                    LoadDatabase.db.dbUpdate("CommandsEnable",new KeyValue(){{ this.add("fly","0"); }},new KeyValue(){{ this.add("uuid",t.getUniqueId().toString()); }});
                t.setFlying(false);
                s.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Fly.Disable_Message")).replace("$playername",t.getName()).replace("$prefix", StringUtils.Prefix));
                LogUtils.writeLog(GrewEssentials.getInstance().log.getString("DisbleFly").replace("$player",pn).replace("$flyplayer",t.getName()).replace("$playeruuid",puuid).replace("$playerip",pip).replace("$playerisop",String.valueOf(s.isOp())).replace("$flyplayeruuid",t.getUniqueId().toString()).replace("$flyplayerip",t.getAddress().getHostName()).replace("$flyplayerisop",String.valueOf(t.isOp())));
            }
        } else {
            t.setAllowFlight(true);
            if(fly==null) {
                String finalVanish1 = vanish;
                LoadDatabase.db.dbInsert("CommandsEnable",new KeyValue(){{
                    this.add("uuid",t.getUniqueId().toString());
                    this.add("fly","1");
                    this.add("vanish", finalVanish1);
                }});
            }
            else
                LoadDatabase.db.dbUpdate("CommandsEnable",new KeyValue(){{
                    this.add("fly","1");
                }},new KeyValue(){{
                    this.add("uuid",t.getUniqueId().toString());
                }});
            s.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Fly.Enable_Message")).replace("$playername",t.getName()).replace("$prefix", StringUtils.Prefix));
            LogUtils.writeLog(GrewEssentials.getInstance().log.getString("EnableFly").replace("$player",pn).replace("$flyplayer",t.getName()).replace("$playeruuid",puuid).replace("$playerip",pip).replace("$playerisop",String.valueOf(s.isOp())).replace("$flyplayeruuid",t.getUniqueId().toString()).replace("$flyplayerip",t.getAddress().getHostName()).replace("$flyplayerisop",String.valueOf(t.isOp())));
        }
    }
}
