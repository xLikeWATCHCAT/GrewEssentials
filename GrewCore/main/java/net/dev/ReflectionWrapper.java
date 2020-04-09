package net.dev;

import org.bukkit.*;

import java.lang.reflect.*;

public class ReflectionWrapper {
    public static <T> T newInstance(Constructor<T> con,Object... args) {
        try {
            con.setAccessible(true);
            return con.newInstance(args);
        } catch (Throwable e) {
            if (e instanceof RuntimeException)
                throw (RuntimeException) e;
            throw new RuntimeException(e);
        }
    }
    public static <T> Constructor<T> getInnerConstructor(Class<T> c,Class<?>... args){
        try {
            Class<?>[] rargs=new Class<?>[args.length+1];
            rargs[0]=c.getEnclosingClass();
            for(int i=1;i<rargs.length;i++)
                rargs[i]=args[i-1];
            Constructor<T> con=c.getDeclaredConstructor(rargs);
            con.setAccessible(true);
            return con;
        }catch(Throwable e){
            if(e instanceof RuntimeException)
                throw (RuntimeException)e;
            throw new RuntimeException(e);
        }
    }
    public static <T> Constructor<T> getConstructor(Class<T> c,Class<?>... args){
        try {
            Constructor<T> con=c.getDeclaredConstructor(args);
            con.setAccessible(true);
            return con;
        }catch(Throwable e){
            if(e instanceof RuntimeException)
                throw (RuntimeException)e;
            throw new RuntimeException(e);
        }
    }
    public static Class<?> getInnerClass(Class<?> parent,String name)
    {
        Class<?>[] classes=parent.getDeclaredClasses();
        for(Class<?> i : classes)
        {
            if(i.getSimpleName().equals(name))
                return i;
        }
        return null;
    }
    public static Method getMethod(Class<?> c,String n,Class<?>... t)
    {
        try {
            Method m = c.getDeclaredMethod(n, t);
            m.setAccessible(true);
            return m;
        }catch(Throwable e){
            if(e instanceof RuntimeException)
                throw (RuntimeException)e;
            throw new RuntimeException(e);
        }
    }
    public static <T> T invokeMethod(Method m,Object o,Object... args)
    {
        try {
            m.setAccessible(true);
            return (T)m.invoke(o,args);
        }catch(Throwable e){
            if(e instanceof RuntimeException)
                throw (RuntimeException)e;
            throw new RuntimeException(e);
        }
    }
    public static <T> T invokeStaticMethod(Method m,Object... args)
    {
        m.setAccessible(true);
        return invokeMethod(m,null,args);
    }
    public static Class<?> getNMSClass(String c)
    {
        try {
            return Class.forName(Bukkit.getServer().getClass().getPackage().getName().replace("org.bukkit.craftbukkit", "net.minecraft.server") + "." + c);
        }catch(Throwable e){
            if(e instanceof RuntimeException)
                throw (RuntimeException)e;
            throw new RuntimeException(e);
        }
    }
    public static Class<?> getCraftBukkitClass(String c)
    {
        try {
            return Class.forName(Bukkit.getServer().getClass().getPackage().getName() + "." + c);
        }catch(Throwable e){
            if(e instanceof RuntimeException)
                throw (RuntimeException)e;
            throw new RuntimeException(e);
        }
    }
    public static Field getField(Class<?> c,String n)
    {
        try {
            Field f = c.getDeclaredField(n);
            f.setAccessible(true);
            if(Modifier.isFinal(f.getModifiers()))
            {
                Field modifiers = f.getClass().getDeclaredField("modifiers");
                modifiers.setAccessible(true);
                modifiers.set(f, f.getModifiers() & ~Modifier.FINAL);
            }
            return f;
        }catch(Throwable e){
            if(e instanceof RuntimeException)
                throw (RuntimeException)e;
            throw new RuntimeException(e);
        }
    }
    public static <T> T getFieldValue(Field f,Object o)
    {
        try {
            f.setAccessible(true);
            return (T)f.get(o);
        }catch(Throwable e){
            if(e instanceof RuntimeException)
                throw (RuntimeException)e;
            throw new RuntimeException(e);
        }
    }
    public static <T> T getStaticFIeldValue(Field f)
    {
        return getFieldValue(f,null);
    }
    public static <T> T setFieldValue(Field f,Object o,T v)
    {
        try {
            f.setAccessible(true);
            if(Modifier.isFinal(f.getModifiers()))
            {
                Field modifiers = f.getClass().getDeclaredField("modifiers");
                modifiers.setAccessible(true);
                modifiers.set(f, f.getModifiers() & ~Modifier.FINAL);
            }
            f.set(o,v);
            return v;
        }catch(Throwable e){
            if(e instanceof RuntimeException)
                throw (RuntimeException)e;
            throw new RuntimeException(e);
        }
    }
    public static <T> T setStaticFieldValue(Field f,T v)
    {
        return setFieldValue(f,null,v);
    }
    public static Class<?> getCraftClass(String className) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
}
