package net.dev.file.json;

import net.dev.gson.*;

import java.io.*;
import java.lang.reflect.*;

public class SimpleConfig<T> implements IConfig<T> {
    public File conf=null;
    public T con=null;
    public Gson g=new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().setLenient().create();
    public String encode=null;
    public Class<T> clazz=null;
    public Constructor<T> cons=null;
    public SimpleConfig(String loc,String encode,Class<T> clazz)
    {
        this.conf=new File(loc);
        this.encode=encode;
        this.clazz=clazz;
        try {
            this.cons=this.clazz.getDeclaredConstructor(new Class<?>[0]);
            this.cons.setAccessible(true);
        }catch(Throwable e) {throw new RuntimeException(e);}
    }
    @Override
    public void autoCreateNewFile() throws IOException
    {
        if(!this.conf.isFile())
        {
            this.conf.getParentFile().mkdirs();
            this.conf.createNewFile();
            initConfig();
        }
    }
    public void initConfig() throws IOException
    {
        try {
            this.con=getDefaultConfig();
        }catch(Throwable e) {throw new RuntimeException(e);}
        this.saveConfig(false);
    }
    @Override
    public void loadConfig() throws IOException {
        autoCreateNewFile();
        try(FileInputStream i=new FileInputStream(conf))
        {
            byte[] buf=new byte[i.available()];
            i.read(buf);
            this.con=this.g.fromJson(new String(buf,this.encode), this.clazz);
            if(this.con==null)
                initConfig();
        }
    }
    public T getDefaultConfig()
    {
        try {
            return this.cons.newInstance(new Object[0]);
        }catch(Throwable e) {throw new RuntimeException(e);}
    }
    @Override
    public void saveConfig() throws IOException {
        this.saveConfig(true);
    }
    private void saveConfig(boolean ac) throws IOException {
        if(ac)
            autoCreateNewFile();
        try(FileOutputStream i=new FileOutputStream(conf))
        {
            i.write(this.g.toJson(this.con, this.clazz).getBytes(this.encode));
        }
    }

    @Override
    public T getConfig() {
        return con;
    }
}
