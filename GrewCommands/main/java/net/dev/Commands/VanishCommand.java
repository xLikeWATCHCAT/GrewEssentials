package net.dev.Commands;

import net.dev.API.Minecraft.*;
import net.dev.Database.*;
import net.dev.*;
import net.dev.Utils.DatabaseUtils.*;
import net.dev.Utils.StringUtils.*;
import net.dev.Utils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;

import java.lang.reflect.*;
import java.util.*;

import static net.dev.ReflectionWrapper.*;
import static net.dev.Utils.PlayerUtils.VanishUtils.VanishUtils.*;
import static net.dev.Utils.Utils.*;

public class VanishCommand implements CommandExecutor {
    public static void setVanished(Player p, boolean set) {
        Object NMSPlayer=invokeMethod(getMethod(p.getClass(),"getHandle"),p);
        Class<?> action=getInnerClass(getNMSClass("PacketPlayOutPlayerInfo"),"EnumPlayerInfoAction");
        Object ps= Array.newInstance(NMSPlayer.getClass(),1);
        Array.set(ps,0,NMSPlayer);
        String fly= LoadDatabase.db.dbSelectFirst("CommandsEnable","fly",new KeyValue(){{ this.add("uuid",p.getUniqueId().toString()); }});
        String vanish= LoadDatabase.db.dbSelectFirst("CommandsEnable","vanish",new KeyValue(){{ this.add("uuid",p.getUniqueId().toString()); }});
        if (set) {
            for (Player user : Bukkit.getOnlinePlayers()) {
                if (!user.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Vanish.See"))) {
                    user.hidePlayer(p);
                }
                if (p.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Vanish.Effect"))|| p.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false));
                }else {
                    p.sendMessage(StringUtils.DoNotHavePerMission);
                }
                Object pack=newInstance(getConstructor(getNMSClass("PacketPlayOutPlayerInfo"),action, ps.getClass()),Enum.valueOf((Class)action,"REMOVE_PLAYER"),ps);
                sendPacketToAllPlayers(pack);
                if(!isVanished(p.getUniqueId())) {
                    p.sendMessage(StringUtils.translateColorCodes(p,GrewEssentials.getInstance().Message.getString("Vanish.Message_Enable")).replace("$playername", p.getName()).replace("$prefix",StringUtils.Prefix));
                    if(GrewEssentials.getInstance().Message.getBoolean("Vanish.BroadCast"))
                        Utils.BroadCast(GrewEssentials.getInstance().Message.getString("Vanish.LeftMessage").replace("$playername",p.getName()).replace("$prefix",StringUtils.Prefix));
                }
                if(vanish==null){
                    LoadDatabase.db.dbInsert("CommandsEnable",new KeyValue(){{
                        this.add("uuid",p.getUniqueId().toString());
                        this.add("fly",fly);
                        this.add("vanish", "1");
                    }});
                }else{
                    LoadDatabase.db.dbUpdate("CommandsEnable",new KeyValue(){{ this.add("vanish","1"); }},new KeyValue(){{ this.add("uuid",p.getUniqueId().toString()); }});
                }
                if(GrewEssentials.getInstance().Message.getBoolean("Vanish.SendActionBar",true)){
                    new Thread(()-> {
                        while (isVanished(p.getUniqueId())) {
                            ActionBar.sendActionBar(p, StringUtils.translateColorCodes(p,GrewEssentials.getInstance().Message.getString("Vanish.ActionBar")).replace("$prefix",StringUtils.Prefix));
                            try{
                                Thread.sleep(1);
                            }catch(Throwable e){if(e instanceof RuntimeException)  throw (RuntimeException)e;throw new RuntimeException(e);}
                        }
                        ActionBar.sendActionBar(p, "");
                    }).start();
                }
            }
        }else{
            for (Player p2 : Bukkit.getOnlinePlayers()) {
                p2.showPlayer(p);
            }
            if (p.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Vanish.Effect"))|| p.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))) {
                p.removePotionEffect(PotionEffectType.INVISIBILITY);
            }else {
                p.sendMessage(StringUtils.DoNotHavePerMission);
            }
            sendPacketToAllPlayers(newInstance(getConstructor(getNMSClass("PacketPlayOutPlayerInfo"),action, ps.getClass()),Enum.valueOf((Class)action,"ADD_PLAYER"),ps));
            if(isVanished(p.getUniqueId())) {
                p.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Vanish.Message_Disable")).replace("$playername", p.getName()).replace("$prefix",StringUtils.Prefix));
                if(GrewEssentials.getInstance().Message.getBoolean("Vanish.BroadCast"))
                    Utils.BroadCast(GrewEssentials.getInstance().Message.getString("Vanish.JoinMessage").replace("$playername",p.getName()));
            }
            LoadDatabase.db.dbUpdate("CommandsEnable",new KeyValue(){{ this.add("vanish","0"); }},new KeyValue(){{ this.add("uuid",p.getUniqueId().toString()); }});
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(GrewEssentials.getInstance().Message.getBoolean("Vanish.Enable")){
            if (args.length > 0) {
                if(sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Vanish.Other"))|| sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))){
                    Player p=Bukkit.getPlayer(args[0]);
                    UUID u = p.getUniqueId();
                    if(p==null) {
                        Utils.notOnline(sender,args[0]);
                        return true;
                    }
                    setVanished(p,!isVanished(u));
                }else{
                    sender.sendMessage(StringUtils.DoNotHavePerMission);
                }
            }else{
                if(sender instanceof Player){
                    if(sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Vanish.Self"))|| sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))){
                        Player p = (Player) sender;
                        UUID u = p.getUniqueId();
                        setVanished((Player)sender,!isVanished(u));
                    }else{
                        sender.sendMessage(StringUtils.DoNotHavePerMission);
                    }
                }else{
                    sender.sendMessage(StringUtils.OnlyPlayer);
                }
            }
        }
        return true;
    }
}
