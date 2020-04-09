package net.dev.JavaScript;

import org.mozilla.javascript.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class LoadJavaScript {
    public static void loadJsFiles(Context ctx, Scriptable scope, File dir) throws IOException
    {
        if(!dir.isDirectory())
            dir.getAbsoluteFile().mkdirs();
        for(File i : dir.listFiles())
        {
            if(!i.isFile())
            {
                if(i.isDirectory())
                    loadJsFiles(ctx,scope,i);
            } else{
                if(i.getName().toLowerCase(Locale.ENGLISH).endsWith(".js".toLowerCase(Locale.ENGLISH)))
                    loadJsFile(ctx,scope,i);
            }
        }
    }
    public static void loadJsFile(Context ctx,Scriptable scope,File f) throws IOException
    {
        if(f.isFile())
        {
            ctx.evaluateString(scope,readTextFile(f),f.getAbsoluteFile().toString(),1,null);
        }
    }
    public static byte[] readFile(File f) throws IOException
    {
        try(FileInputStream i=new FileInputStream(f))
        {
            byte[] buf=new byte[i.available()];
            i.read(buf);
            return buf;
        }
    }
    public static String readTextFile(File f) throws IOException
    {
        return new String(readFile(f), StandardCharsets.UTF_8);
    }

    public static Object invokeJsFunction(Function f,Context ctx,Scriptable scope,Object... args)
    {
        return f.call(ctx,f,scope,args);
    }
}
