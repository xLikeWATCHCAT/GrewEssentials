package net.dev.Utils;

import net.dev.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;

import java.io.*;

import static org.bukkit.Bukkit.*;

public class Bungees {
    public static void init() {
        Plugin plugin = GrewEssentials.getPlugin();
        if (!getMessenger().isOutgoingChannelRegistered(plugin, "BungeeCord")) {
            getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        }
    }

    public static void connect(Player player, String server) {
        sendBungeeData(player, "Connect", server);
    }

    public static void sendBungeeData(Player player, String... args) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArray);
        for (String arg : args) {
            try {
                out.writeUTF(arg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        player.sendPluginMessage(GrewEssentials.getPlugin(), "BungeeCord", byteArray.toByteArray());
    }
}
