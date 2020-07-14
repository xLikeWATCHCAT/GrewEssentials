package net.dev.utils.command;

import java.util.*;

public class CommandUtil {

    public static Vector<String> commandListToCommandNameList(Vector<IChildCommand> v)
    {
        Vector<String> ret=new Vector<>();
        for(IChildCommand i : v)
        {
            ret.add(i.getClass().getSimpleName().toLowerCase());
        }
        return ret;
    }
    public static IChildCommand getCommand(Vector<IChildCommand> l,String cmd)
    {
        for(IChildCommand i : l)
        {
            if(i.getClass().getSimpleName().equalsIgnoreCase(cmd))
                return i;
        }
        return null;
    }
}
