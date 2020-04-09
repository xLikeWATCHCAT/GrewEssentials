package net.dev.API.Minecraft;

import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

import static net.dev.ReflectionWrapper.*;
import static net.dev.Utils.Utils.*;

public class BossBar {
    private static final Vector<Object> fakeEntities = new Vector<>();
    private static final Vector<Integer> fakeIds = new Vector<>();
    private static final Vector<Player> sendTo = new Vector<>();
    private static final Object lock = new Object();
    private static final Vector<AtomicInteger> atoms = new Vector<>();
    @Deprecated
    public static class Unsafe{
        @Deprecated
        public static int getEntityIdWithoutLock(int index)
        {
            return fakeIds.get(index);
        }
        @Deprecated
        public static Object getEntityWithoutLock(int index)
        {
            return fakeEntities.get(index);
        }
        @Deprecated
        public static Player getSendToPlayerWithoutLock(int index)
        {
            return sendTo.get(index);
        }
    }
    public static void updateBossbar(AtomicInteger ind)
    {
        synchronized (lock)
        {
            int i=ind.get();
            sendPacket(sendTo.get(i), newInstance(getConstructor(getNMSClass("PacketPlayOutEntityDestroy"), int[].class), new int[]{fakeIds.get(i)}));
            Player p=getSendToPlayer(ind);
            setFieldValue(getField(getNMSClass("Entity"),"world"),getFakeEntity(ind),invokeMethod(getMethod(p.getWorld().getClass(), "getHandle"), p.getWorld()));
            Location tl=p.getEyeLocation().add(p.getEyeLocation().getDirection().normalize().multiply(25));
            if (tl.getY() < 1) tl.setY(1);
            invokeMethod(getMethod(getNMSClass("Entity"), "setLocation", double.class, double.class, double.class, float.class, float.class), getFakeEntity(ind), tl.getX(), tl.getY(), tl.getZ(), tl.getPitch(), tl.getYaw());
            sendPacket(p, newInstance(getConstructor(getNMSClass("PacketPlayOutSpawnEntityLiving"), getNMSClass("EntityLiving")), getFakeEntity(ind)));
        }
    }
    public static void updateBossbarWithName(AtomicInteger ind,String name)
    {
        synchronized (lock)
        {
            invokeMethod(getMethod(getNMSClass("Entity"), "setCustomName", String.class), getFakeEntity(ind),name);
            updateBossbar(ind);
        }
    }
    public static AtomicInteger sendBossbar(Player p, String title, float health) {
        synchronized (lock) {
            sendTo.add(p);
            Class<?> Entity=null;
            Class<?> EntityLiving=null;
            Class<?> EntityEnderDragon=null;
            Object Wither=null;
            try {
                Entity = getNMSClass("Entity");
                EntityLiving = getNMSClass("EntityLiving");
                EntityEnderDragon = getNMSClass("EntityWither");
                Wither = newInstance(getConstructor(EntityEnderDragon, getNMSClass("World")), new Object[]{invokeMethod(getMethod(p.getWorld().getClass(), "getHandle"), p.getWorld())});
            }catch(Throwable e){
                sendTo.remove(p);
                throw new RuntimeException(e);
            }
            fakeEntities.add(Wither);

            int id=-1;
            try {
                id = invokeMethod(getMethod(Entity, "getId"), Wither);
            }catch(Throwable e)
            {
                sendTo.remove(p);
                fakeEntities.remove(Wither);
                throw new RuntimeException(e);
            }
            fakeIds.add(id);
            try {
                Location tl=p.getEyeLocation().add(p.getEyeLocation().getDirection().normalize().multiply(25));
                if (tl.getY() < 1) tl.setY(1);
                invokeMethod(getMethod(Entity, "setLocation", double.class, double.class, double.class, float.class, float.class), Wither, tl.getX(), tl.getY(), tl.getZ(), tl.getPitch(), tl.getYaw());
                invokeMethod(getMethod(Entity, "setInvisible", boolean.class), Wither, true);
                invokeMethod(getMethod(Entity, "setCustomName", String.class), Wither,title);
                invokeMethod(getMethod(EntityLiving, "setHealth", float.class), Wither, health);
                setFieldValue(getField(Entity, "motX"), Wither, 0);
                setFieldValue(getField(Entity, "motY"), Wither, 0);
                setFieldValue(getField(Entity, "motZ"), Wither, 0);
                sendPacket(p, newInstance(getConstructor(getNMSClass("PacketPlayOutSpawnEntityLiving"), EntityLiving), Wither));
            }catch(Throwable e){
                sendTo.remove(p);
                fakeEntities.remove(Wither);
                fakeIds.remove((Integer) id);
                throw new RuntimeException(e);
            }
            AtomicInteger i = new AtomicInteger(fakeEntities.size() - 1);
            atoms.add(i);
            return i;
        }
    }

    public static void removeBossbar(AtomicInteger index) {
        synchronized (lock) {
            int i = index.get();
            sendPacket(sendTo.get(i), newInstance(getConstructor(getNMSClass("PacketPlayOutEntityDestroy"), int[].class), new int[]{fakeIds.get(i)}));
            sendTo.remove(i);
            fakeIds.remove(i);
            fakeEntities.remove(i);
            index.set(Integer.MIN_VALUE);
            atoms.remove(index);
            for (int i2 = 0; i2 < atoms.size(); i2++)
                if (atoms.get(i2).get() > i)
                    atoms.get(i2).decrementAndGet();
        }
    }

    public static Object getFakeEntity(AtomicInteger index) {
        synchronized (lock) {
            return fakeEntities.get(index.get());
        }
    }

    public static int getFakeEntityId(AtomicInteger index) {
        synchronized (lock) {
            return fakeIds.get(index.get());
        }
    }

    public static Player getSendToPlayer(AtomicInteger index) {
        synchronized (lock) {
            return sendTo.get(index.get());
        }
    }
    @Deprecated
    public static void getIndexesByPlayer(Player p, Consumer<Integer[]> c)
    {
        synchronized (lock)
        {
            Vector<Integer> inds=new Vector<>();
            for(int i=0;i<sendTo.size();i++)
                if(Objects.equals(p,sendTo.get(i)))
                    inds.add(i);
                c.accept(inds.parallelStream().toArray(Integer[]::new));
        }
    }
    private static AtomicInteger toAtomic(int i)
    {
        synchronized (lock)
        {
            for(int i2=0;i2<atoms.size();i2++)
            {
                if(atoms.get(i2).get()==i)
                    return atoms.get(i2);
            }
            throw new RuntimeException("AtomicInteger not found! index: "+i);
        }
    }
    public static void getIndexesAtomicByPlayer(Player p, Consumer<AtomicInteger[]> c)
    {
        synchronized (lock)
        {
            Vector<AtomicInteger> inds=new Vector<>();
            for(int i=0;i<sendTo.size();i++)
                if(Objects.equals(p,sendTo.get(i)))
                    inds.add(toAtomic(i));
            c.accept(inds.parallelStream().toArray(AtomicInteger[]::new));
        }
    }

    public static boolean isInSendToList(Player p)
    {
        synchronized (lock)
        {
            return sendTo.contains(p);
        }
    }
}
