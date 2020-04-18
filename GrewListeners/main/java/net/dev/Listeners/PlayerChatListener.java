package net.dev.Listeners;

import net.dev.*;
import net.dev.API.*;
import net.dev.Database.*;
import net.dev.Events.*;
import net.dev.Utils.DatabaseUtils.*;
import net.dev.Utils.StringUtils.*;
import net.dev.Utils.StringUtils.Random;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import java.util.*;

public class PlayerChatListener implements Listener {
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event){
        Player p = event.getPlayer();
        UUID u = p.getUniqueId();
        if(GrewEssentials.getInstance().Message.getBoolean("BlackMessage.Enable",true)){
            for(String message:GrewEssentials.getInstance().Config.getStringList("BlackMessageList")){
                String ChatMessage = PlayerChat.getPlayerChatMessage(event).toLowerCase();
                String BlackMessage = message.toLowerCase();
                if(ChatMessage.contains(BlackMessage)){
                    String times = LoadDatabase.db.dbSelectFirst("BlackMessageWarnTimes","times",new KeyValue(){{ this.add("uuid",u); }});
                    int warntimes = PlayerInfo.getPlayerWarnBlackMessageTimes(p);
                    int randomtimes=new Random().getRandomInt(GrewEssentials.getInstance().Message.getInt("BlackMessage.WarnMinTimes"),GrewEssentials.getInstance().Message.getInt("BlackMessage.WarnMaxTimes"));
                    PlayerChat.stopPlayerChat(event);
                    if(times == null){
                        LoadDatabase.db.dbInsert("BlackMessageWarnTimes",new KeyValue(){{
                            this.add("uuid",u.toString());
                            //侧搭
                            this.add("times",0);
                        }});
                    }else{
                        LoadDatabase.db.dbUpdate("BlackMessageWarnTimes",new KeyValue("times",(warntimes+1)), new KeyValue("uuid",u.toString()));
                    }
                    warntimes = PlayerInfo.getPlayerWarnBlackMessageTimes(p);
                    if(warntimes >= randomtimes){
                        for(String cmd:GrewEssentials.getInstance().Message.getStringList("BlackMessage.Commands"))
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                        LoadDatabase.db.dbUpdate("BlackMessageWarnTimes",new KeyValue("times",1), new KeyValue("uuid",u.toString()));
                    }
                    p.sendMessage(StringUtils.translateColorCodes(event.getPlayer(),GrewEssentials.getInstance().Message.getString("BlackMessage.Warn")).replace("$prefix",StringUtils.Prefix));
                }
            }
        }
    }
}
