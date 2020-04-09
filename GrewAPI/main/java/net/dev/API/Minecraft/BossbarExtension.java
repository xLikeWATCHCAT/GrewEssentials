package net.dev.API.Minecraft;

import me.clip.placeholderapi.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.entity.*;

import java.util.concurrent.atomic.*;

public class BossbarExtension {
    public static AtomicInteger sendBossbarExtended(Player p, String s,float health,boolean colorTrans,boolean variable)
    {
        String s2=s;
        if(colorTrans)
            s2= StringUtils.translateColorCodes(s2);
        if(variable)
            s2=PlaceholderAPI.setPlaceholders(p,s2);
        return BossBar.sendBossbar(p,s2,health);
    }
}
