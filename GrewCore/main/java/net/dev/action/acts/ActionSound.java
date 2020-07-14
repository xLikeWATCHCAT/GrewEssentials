package net.dev.action.acts;

import net.dev.utils.sound.*;
import org.bukkit.entity.*;

public class ActionSound extends AbstractAction {

    @Override
    public String getName() {
        return "sound|playsound";
    }

    @Override
    public void onExecute(Player player) {
        String sound = getContent();
        if ("stop".equalsIgnoreCase(sound)) {
            CommandSoundsPreview.stopSounds(player);
        } else {
            new SoundPack(sound).play(player);
        }
    }

    @Override
    public void setContent(String content) {
        String[] split = content.split("-");
        if (split.length == 1) {
            content += "-1-1";
        }
        super.setContent(content);
    }
}
