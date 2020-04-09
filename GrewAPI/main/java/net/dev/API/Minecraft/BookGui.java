package net.dev.API.Minecraft;

import io.netty.buffer.*;
import net.dev.*;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.chat.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;

import static net.dev.ReflectionWrapper.*;

public class BookGui {
    private static ItemStack create(String title, TextComponent... texts) {
        ItemStack is = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta m = (BookMeta) is.getItemMeta();
        m.setTitle(title);
        m.setAuthor("");

        try {
            List<Object> list = (List<Object>) getField(getCraftClass("inventory.CraftMetaBook"), "pages").get(m);

            TextComponent text = new TextComponent("");

            for (TextComponent tc : texts)
                text.addExtra(tc);

            list.add(toIChatBaseComponent(text));
        } catch (Exception e) {
            e.printStackTrace();
        }

        is.setItemMeta(m);

        return is;
    }

    private static Object toIChatBaseComponent(TextComponent tc) {
        try {
            Class<?> chatSerializer = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0];

            return chatSerializer.getMethod("a", String.class).invoke(chatSerializer, ComponentSerializer.toString(tc));
        } catch (Exception e) {
            return null;
        }
    }
    public static void open(Player p, ItemStack book) {
        ItemStack hand = p.getItemInHand();

        try {
            Object entityPlayer = p.getClass().getMethod("getHandle").invoke(p);
            Class<?> craftItemStackClass = getCraftClass("inventory.CraftItemStack");
            Object nmsItemStack = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class).invoke(craftItemStackClass, book);

            p.setItemInHand(book);

            if(!(GrewEssentials.version.startsWith("1_8"))) {
                Class<?> enumHand = getNMSClass("EnumHand");
                Object mainHand = getField(enumHand, "MAIN_HAND").get(enumHand);

                if(Bukkit.getVersion().contains("1.14.4") || GrewEssentials.version.startsWith("1_15")) {
                    Class<?> itemWrittenBook = getNMSClass("ItemWrittenBook");

                    if ((boolean) itemWrittenBook.getMethod("a", getNMSClass("ItemStack"), getNMSClass("CommandListenerWrapper"), getNMSClass("EntityHuman")).invoke(itemWrittenBook, nmsItemStack, entityPlayer.getClass().getMethod("getCommandListener").invoke(entityPlayer), entityPlayer)) {
                        Object activeContainer = entityPlayer.getClass().getField("activeContainer").get(entityPlayer);

                        activeContainer.getClass().getMethod("c").invoke(activeContainer);
                    }

                    Object packet = getNMSClass("PacketPlayOutOpenBook").getConstructor(enumHand).newInstance(mainHand);
                    Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
                    playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
                } else
                    entityPlayer.getClass().getMethod("a", getNMSClass("ItemStack"), enumHand).invoke(entityPlayer, nmsItemStack, mainHand);
            } else {
                Object packet = getNMSClass("PacketPlayOutCustomPayload").getConstructor(String.class, getNMSClass("PacketDataSerializer")).newInstance("MC|BOpen", getNMSClass("PacketDataSerializer").getConstructor(ByteBuf.class).newInstance(Unpooled.buffer()));
                Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
                playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            p.setItemInHand(hand);
        }
    }
}
