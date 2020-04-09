package net.dev.API.Minecraft;

import net.dev.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.lang.reflect.*;
import java.util.*;

import static net.dev.ReflectionWrapper.*;

public class AnvilGUI {
    private Player p;
    private AnvilClickEventHandler handler;
    private static Class<?> blockPosition;
    private static Class<?> containerAnvil;
    private static Class<?> entityHuman;
    private HashMap<AnvilSlot, ItemStack> items = new HashMap<AnvilSlot, ItemStack>();
    private Inventory inv;
    private Listener listener;

    private void loadClasses() {
        blockPosition = getNMSClass("BlockPosition");
        containerAnvil = getNMSClass("ContainerAnvil");
        entityHuman = getNMSClass("EntityHuman");
    }

    public AnvilGUI(final Player p, final AnvilClickEventHandler handler) {
        loadClasses();

        this.p = p;
        this.handler = handler;

        this.listener = new Listener() {

            @EventHandler
            public void onInventoryClick(InventoryClickEvent e) {
                if (e.getWhoClicked() instanceof Player) {
                    if (e.getInventory().equals(inv)) {
                        e.setCancelled(true);

                        ItemStack item = e.getCurrentItem();
                        int slot = e.getRawSlot();
                        String name = "";

                        if (item != null) {
                            if (item.hasItemMeta()) {
                                ItemMeta meta = item.getItemMeta();

                                if (meta.hasDisplayName())
                                    name = meta.getDisplayName();
                            }
                        }

                        AnvilClickEvent clickEvent = new AnvilClickEvent(AnvilSlot.bySlot(slot), name);

                        handler.onAnvilClick(clickEvent);

                        if (clickEvent.getWillClose())
                            e.getWhoClicked().closeInventory();

                        if (clickEvent.getWillDestroy())
                            destroy();
                    }
                }
            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent e) {
                if (e.getPlayer() instanceof Player) {
                    Inventory inv = e.getInventory();

                    p.setLevel(p.getLevel() - 1);

                    if (inv.equals(AnvilGUI.this.inv)) {
                        inv.clear();

                        destroy();
                    }
                }
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent e) {
                if (e.getPlayer().equals(getPlayer())) {
                    p.setLevel(p.getLevel() - 1);

                    destroy();
                }
            }

        };

        Bukkit.getPluginManager().registerEvents(listener, GrewEssentials.getInstance());
    }

    public Player getPlayer() {
        return p;
    }

    public void setSlot(AnvilSlot slot, ItemStack item) {
        items.put(slot, item);
    }

    public void open() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        p.setLevel(p.getLevel() + 1);

        try {
            Object player = p.getClass().getMethod("getHandle").invoke(p);
            Object container = containerAnvil.getConstructor(getNMSClass("PlayerInventory"), getNMSClass("World"), blockPosition, entityHuman).newInstance(getPlayerField(p, "inventory"), getPlayerField(p, "world"), blockPosition.getConstructor(int.class, int.class, int.class).newInstance(0, 0, 0), player);

            getField(getNMSClass("Container"), "checkReachable").set(container, false);

            Object bukkitView = invokeMethod("getBukkitView", container);
            inv = (Inventory) invokeMethod("getTopInventory", bukkitView);

            for (AnvilSlot slot : items.keySet())
                inv.setItem(slot.getSlot(), items.get(slot));

            int c = (int) invokeMethod("nextContainerCounter", player);
            Constructor<?> chatMessageConstructor = getNMSClass("ChatMessage").getConstructor(String.class, Object[].class);
            Object playerConnection = getPlayerField(p, "playerConnection");
            Object packet = getNMSClass("PacketPlayOutOpenWindow").getConstructor(int.class, String.class, getNMSClass("IChatBaseComponent"), int.class).newInstance(c, "minecraft:anvil", chatMessageConstructor.newInstance("Repairing", new Object[] {}), 0);

            Method sendPacket = getMethod("sendPacket", playerConnection.getClass(), getNMSClass("Packet"));
            sendPacket.invoke(playerConnection, packet);

            Field activeContainerField = getField(entityHuman, "activeContainer");

            if (activeContainerField != null) {
                activeContainerField.set(player, container);

                getField(getNMSClass("Container"), "windowId").set(activeContainerField.get(player), c);

                activeContainerField.get(player).getClass().getMethod("addSlotListener", getNMSClass("ICrafting")).invoke(activeContainerField.get(player), player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Method getMethod(String name, Class<?> clazz, Class<?>... args) {
        try {

            return clazz.getMethod(name, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Object invokeMethod(String name, Object clazz) {
        try {
            return clazz.getClass().getMethod(name).invoke(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Object getPlayerField(Player p, String name) {
        try {
            Object getHandle = p.getClass().getMethod("getHandle").invoke(p);

            return getHandle.getClass().getField(name).get(getHandle);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public void destroy() {
        p = null;
        handler = null;
        items = null;

        HandlerList.unregisterAll(listener);

        listener = null;
    }

    public enum AnvilSlot {

        INPUT_LEFT(0), INPUT_RIGHT(1), OUTPUT(2);

        private int slot;

        private AnvilSlot(int slot) {
            this.slot = slot;
        }

        public static AnvilSlot bySlot(int slot) {
            for (AnvilSlot anvilSlot : values()) {
                if (anvilSlot.getSlot() == slot)
                    return anvilSlot;
            }

            return null;
        }

        public int getSlot() {
            return slot;
        }

    }

    public interface AnvilClickEventHandler {

        void onAnvilClick(AnvilClickEvent event);

    }

    public class AnvilClickEvent {

        private AnvilSlot slot;
        private String name;
        private boolean close = true;
        private boolean destroy = true;

        public AnvilClickEvent(AnvilSlot slot, String name) {
            this.slot = slot;
            this.name = name;
        }

        public AnvilSlot getSlot() {
            return slot;
        }

        public String getName() {
            return name;
        }

        public boolean getWillClose() {
            return close;
        }

        public void setWillClose(boolean close) {
            this.close = close;
        }

        public boolean getWillDestroy() {
            return destroy;
        }

        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }

    }
}
