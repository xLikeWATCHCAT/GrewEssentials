package net.dev.Utils;

import org.bukkit.entity.*;
import org.bukkit.event.*;

import java.util.*;

public class Catchers implements Listener {
    private static HashMap<String, LinkedList<Catcher>> playerdata = new HashMap<>();

    public static HashMap<String, LinkedList<Catcher>> getPlayerdata() {
        return playerdata;
    }

    public static boolean contains(Player player) {
        return playerdata.containsKey(player.getName()) && playerdata.get(player.getName()).size() > 0;
    }

    public static void call(Player player, Catcher catcher) {
        if (!playerdata.containsKey(player.getName())) {
            playerdata.put(player.getName(), new LinkedList<>());
        }
        playerdata.get(player.getName()).add(catcher.before());
    }


    public interface Catcher {

        default String quit() {
            return "(?i)quit|cancel|exit";
        }

        default Catcher before() {
            return this;
        }

        boolean after(String message);

        default void cancel() {
        }

    }
}
