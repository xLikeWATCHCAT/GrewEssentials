package net.dev.Action.acts;

import net.dev.API.Minecraft.*;
import net.dev.Utils.StringUtils.*;
import net.dev.Utils.*;
import org.apache.commons.lang.math.*;
import org.bukkit.entity.*;

public class ActionTitle extends AbstractAction {

    private String title;
    private String subtitle;
    private int fadein = -1;
    private int stay = -1;
    private int fadeout = -1;

    @Override
    public String getName() {
        return "title|subtitle";
    }

    @Override
    public void onExecute(Player player) {
        String title =  this.title;
        String subTitle =  this.subtitle;
        TitleAPI.sendTitle(player,fadein,stay,fadeout,title != null ? title : "",subTitle != null ? subTitle : "");
    }

    @Override
    public void setContent(String content) {
        super.setContent(content);

        if (getContent() == null) {
            return;
        }

        for (Variables.Variable variable : new Variables(getContent()).find().getVariableList()) {
            if (variable.isVariable()) {
                String[] x = variable.getText().split("=");
                if (x.length >= 2) {
                    String type = x[0].toLowerCase(), value = ArrayUtil.arrayJoin(x, 1);
                    switch (type) {
                        case "title":
                            this.title = value;
                            continue;
                        case "subtitle":
                            this.subtitle = value;
                            continue;
                        case "fadein":
                            this.fadein = NumberUtils.toInt(value, 20);
                            continue;
                        case "stay":
                            this.stay = NumberUtils.toInt(value, 40);
                            continue;
                        case "fadeout":
                            this.fadeout = NumberUtils.toInt(value, 20);
                            continue;
                        default:
                    }
                }
            }
        }
        this.fadein = fadein <= 0 ? 20 : fadein;
        this.fadeout = fadeout <= 0 ? 20 : fadeout;
        this.stay = stay <= 0 ? 40 : stay;
    }
}
