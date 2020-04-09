package net.dev.Listeners;

import net.dev.*;
import net.dev.Commands.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

import java.util.*;

public class InventoryListener implements Listener {
    public static <K,V> K getKeyByValue(Map<K,V> m,V value)
    {
        for(Map.Entry<K,V> i : m.entrySet())
            if(Objects.equals(value,i.getValue()))
                return i.getKey();
            return null;
    }
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onItemPick(PlayerPickupItemEvent e)
    {
        Inventory inv=getKeyByValue(InvSeeCommand.ivs,e.getPlayer());
        if(inv!=null)
        {
            Bukkit.getScheduler().runTask(GrewEssentials.getInstance(),()->{
                for(int i=0;i<4;i++)
                    inv.setItem(i,e.getPlayer().getInventory().getArmorContents()[i]);
                for(int i=9;i<45;i++)
                    inv.setItem(i,e.getPlayer().getInventory().getContents()[i-9]);
            });
        }
    }
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onItemDrop(PlayerDropItemEvent e)
    {
        Inventory inv=getKeyByValue(InvSeeCommand.ivs,e.getPlayer());
        if(inv!=null)
        {
            Bukkit.getScheduler().runTask(GrewEssentials.getInstance(),()->{
                for(int i=0;i<4;i++)
                    inv.setItem(i,e.getPlayer().getInventory().getArmorContents()[i]);
                for(int i=9;i<45;i++)
                    inv.setItem(i,e.getPlayer().getInventory().getContents()[i-9]);
            });
        }
    }
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e)
    {
        if(e.getClickedInventory()==null)
            return;
        if(Objects.equals(e.getClickedInventory().getType(),InventoryType.PLAYER))
        {
            Inventory inv=getKeyByValue(InvSeeCommand.ivs,(Player)e.getClickedInventory().getHolder());
            if(inv!=null)
            {
                //System.out.println("test");
                Bukkit.getScheduler().runTask(GrewEssentials.getInstance(),()->{
                    for(int i=0;i<4;i++)
                        inv.setItem(i,((PlayerInventory)e.getClickedInventory()).getArmorContents()[i]);
                    for(int i=9;i<45;i++)
                        inv.setItem(i,((PlayerInventory)e.getClickedInventory()).getContents()[i-9]);
                });
            }
        }
        if(InvSeeCommand.ivs.containsKey(e.getClickedInventory()))
        {
            int s=e.getRawSlot();
            if(s>=4 && (s<9 || s>44))
            {
                e.setCancelled(true);
                return;
            }
            Bukkit.getScheduler().runTask(GrewEssentials.getInstance(),()->{
                ItemStack[] iss=new ItemStack[]{e.getClickedInventory().getContents()[0],e.getClickedInventory().getContents()[1],e.getClickedInventory().getContents()[2],e.getClickedInventory().getContents()[3]};
                InvSeeCommand.ivs.get(e.getClickedInventory()).getInventory().setArmorContents(iss);
                iss=new ItemStack[]{e.getClickedInventory().getContents()[9],e.getClickedInventory().getContents()[10],e.getClickedInventory().getContents()[11],e.getClickedInventory().getContents()[12],e.getClickedInventory().getContents()[13],e.getClickedInventory().getContents()[14],e.getClickedInventory().getContents()[15],e.getClickedInventory().getContents()[16],e.getClickedInventory().getContents()[17],e.getClickedInventory().getContents()[18],e.getClickedInventory().getContents()[19],e.getClickedInventory().getContents()[20],e.getClickedInventory().getContents()[21],e.getClickedInventory().getContents()[22],e.getClickedInventory().getContents()[23],e.getClickedInventory().getContents()[24],e.getClickedInventory().getContents()[25],e.getClickedInventory().getContents()[26],e.getClickedInventory().getContents()[27],e.getClickedInventory().getContents()[28],e.getClickedInventory().getContents()[29],e.getClickedInventory().getContents()[30],e.getClickedInventory().getContents()[31],e.getClickedInventory().getContents()[32],e.getClickedInventory().getContents()[33],e.getClickedInventory().getContents()[34],e.getClickedInventory().getContents()[35],e.getClickedInventory().getContents()[36],e.getClickedInventory().getContents()[37],e.getClickedInventory().getContents()[38],e.getClickedInventory().getContents()[39],e.getClickedInventory().getContents()[40],e.getClickedInventory().getContents()[41],e.getClickedInventory().getContents()[42],e.getClickedInventory().getContents()[43],e.getClickedInventory().getContents()[44]};
                InvSeeCommand.ivs.get(e.getClickedInventory()).getInventory().setContents(iss);
                InvSeeCommand.ivs.get(e.getClickedInventory()).updateInventory();
            });
            /*if(e.getClick().isLeftClick() || e.getClick().isShiftClick())
            {
                if(s<4)
                {
                    ItemStack[] iss=InvSeeCommand.ivs.get(e.getClickedInventory()).getInventory().getArmorContents();
                    iss[s]=null;
                    InvSeeCommand.ivs.get(e.getClickedInventory()).getInventory().setArmorContents(iss);
                }
                else if(s>=9 && s<=44)
                {
                    ItemStack[] iss= InvSeeCommand.ivs.get(e.getClickedInventory()).getInventory().getContents();
                    iss[s-9]=null;
                    InvSeeCommand.ivs.get(e.getClickedInventory()).getInventory().setContents(iss);
                }else e.setCancelled(true);
            }else if(e.getClick().isRightClick())
            {
                if(s<4)
                {
                    ItemStack[] iss= InvSeeCommand.ivs.get(e.getClickedInventory()).getInventory().getArmorContents();
                    iss[s].setAmount(iss[s].getAmount()/2);
                    InvSeeCommand.ivs.get(e.getClickedInventory()).getInventory().setArmorContents(iss);
                }
                else if(s>=9 && s<=44)
                {
                    ItemStack[] iss=InvSeeCommand.ivs.get(e.getClickedInventory()).getInventory().getContents();
                    iss[s-9].setAmount(iss[s-9].getAmount()/2);
                    InvSeeCommand.ivs.get(e.getClickedInventory()).getInventory().setContents(iss);
                }else e.setCancelled(true);
            }else if(e.getClick().isKeyboardClick())
            {
                if(s<4) {
                    ItemStack[] iss = InvSeeCommand.ivs.get(e.getClickedInventory()).getInventory().getArmorContents();
                    iss[s] = e.getWhoClicked().getInventory().getContents()[e.getHotbarButton()];
                    InvSeeCommand.ivs.get(e.getClickedInventory()).getInventory().setArmorContents(iss);
                } else if(s>=9 && s<=44) {
                    ItemStack[] iss = InvSeeCommand.ivs.get(e.getClickedInventory()).getInventory().getContents();
                    iss[s - 9] = e.getWhoClicked().getInventory().getContents()[e.getHotbarButton()];
                    InvSeeCommand.ivs.get(e.getClickedInventory()).getInventory().setContents(iss);
                }else e.setCancelled(true);
            }else if(Objects.equals(e.getClick(),ClickType.MIDDLE) || e.getClick().isCreativeAction())
            {
                e.setCancelled(true);
            }else e.setCancelled(true);
            InvSeeCommand.ivs.get(e.getClickedInventory()).updateInventory();*/
        }
    }
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onInvenoryClose(InventoryCloseEvent e)
    {
        if(InvSeeCommand.ivs.containsKey(e.getInventory()))
            InvSeeCommand.ivs.remove(e.getInventory());
    }
}
