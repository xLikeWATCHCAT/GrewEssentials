package net.dev.Commands;

import net.dev.API.Minecraft.*;
import net.dev.Database.*;
import net.dev.*;
import net.dev.Utils.DatabaseUtils.*;
import net.dev.Utils.PlayerUtils.*;
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
    public static final Method getHandle=getMethod(getCraftClass("entity.CraftPlayer"),"getHandle");
    public static void setVanished(Player p, boolean set,boolean mess,boolean hideTabPlayer) {
        new Thread(()->{
            Object NMSPlayer=invokeMethod(getHandle,p);
            Class<?> action=getInnerClass(getNMSClass("PacketPlayOutPlayerInfo"),"EnumPlayerInfoAction");
            Object ps= Array.newInstance(NMSPlayer.getClass(),1);
            Array.set(ps,0,NMSPlayer);
            if (set) {
                if(hideTabPlayer){
                    new Thread(()->{
                        Object pack=newInstance(getConstructor(getNMSClass("PacketPlayOutPlayerInfo"),action, ps.getClass()),Enum.valueOf((Class)action,"REMOVE_PLAYER"),ps);
                        sendPacketToAllPlayers(pack);
                    }).start();
                }
                for (Player user : Bukkit.getOnlinePlayers()) {
                    if (!user.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Vanish.See"))) {
                        user.hidePlayer(p);
                    }
                }
            }else{
                if(hideTabPlayer){
                    new Thread(()->{
                        sendPacketToAllPlayers(newInstance(getConstructor(getNMSClass("PacketPlayOutPlayerInfo"),action, ps.getClass()),Enum.valueOf((Class)action,"ADD_PLAYER"),ps));
                    }).start();
                }
                for (Player p2 : Bukkit.getOnlinePlayers()) {
                    p2.showPlayer(p);
                }
            }
        }).start();
        String fly= LoadDatabase.db.dbSelectFirst("CommandsEnable","fly",new KeyValue(){{ this.add("uuid",p.getUniqueId().toString()); }});
        String vanish= LoadDatabase.db.dbSelectFirst("CommandsEnable","vanish",new KeyValue(){{ this.add("uuid",p.getUniqueId().toString()); }});
        if(set){
            VanishPlayerInThisServerList.add(p);
            if(vanish==null){
                LoadDatabase.db.dbInsert("CommandsEnable",new KeyValue(){{
                    this.add("uuid",p.getUniqueId().toString());
                    this.add("fly",fly);
                    this.add("vanish", "1");
                }});
            }else{
                LoadDatabase.db.dbUpdate("CommandsEnable",new KeyValue(){{ this.add("vanish","1"); }},new KeyValue(){{ this.add("uuid",p.getUniqueId().toString()); }});
            }
            if(isVanished(p.getUniqueId())) {
                if(mess){
                    PlayerUtil.sendMessage(p,GrewEssentials.getInstance().Message.getString("Vanish.Message_Enable").replace("$playername", p.getName()));
                    if(GrewEssentials.getInstance().Message.getBoolean("Vanish.BroadCast"))
                        Utils.BroadCast(GrewEssentials.getInstance().Message.getString("Vanish.LeftMessage").replace("$playername",p.getName()).replace("$prefix",StringUtils.Prefix));
                }
            }
            try{
                if (p.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Vanish.Effect"))|| p.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false));
                }
            }catch (Throwable e){ }
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
        }else{
            VanishPlayerInThisServerList.remove(p);
            LoadDatabase.db.dbUpdate("CommandsEnable",new KeyValue(){{ this.add("vanish","0"); }},new KeyValue(){{ this.add("uuid",p.getUniqueId().toString()); }});
            try{
                if (p.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Vanish.Effect"))|| p.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))) {
                    p.removePotionEffect(PotionEffectType.INVISIBILITY);
                }
            }catch (Throwable e){}
            if(!isVanished(p.getUniqueId())) {
                if(mess){
                    PlayerUtil.sendMessage(p,GrewEssentials.getInstance().Message.getString("Vanish.Message_Disable").replace("$playername", p.getName()));
                    if(GrewEssentials.getInstance().Message.getBoolean("Vanish.BroadCast"))
                        Utils.BroadCast(GrewEssentials.getInstance().Message.getString("Vanish.JoinMessage").replace("$playername",p.getName()));
                }
            }
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(GrewEssentials.getInstance().Message.getBoolean("Vanish.Enable")){
            if (args.length > 0) {
                if(sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Vanish.Other"))|| sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))){
                    Player p=Bukkit.getPlayer(args[0]);
                    UUID u = p.getUniqueId();
                    setVanished(p,!isVanished(u),true,true);
                }else{
                    sender.sendMessage(StringUtils.DoNotHavePerMission);
                }
            }else{
                if(sender instanceof Player){
                    if(sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.Vanish.Self"))|| sender.hasPermission(GrewEssentials.getInstance().Config.getString("Permissions.All"))){
                        Player p = (Player) sender;
                        UUID u = p.getUniqueId();
                        setVanished((Player)sender,!isVanished(u),true,true);
                    }else{
                        sender.sendMessage(StringUtils.DoNotHavePerMission);
                    }
                }else{
                    sender.sendMessage(StringUtils.OnlyPlayer);
                }
            }
        }else{
            sender.sendMessage(StringUtils.DoNotHavePerMission);
        }
        return true;
    }
}
