package net.dev.Utils.CommandUtils;

import org.bukkit.command.*;

import java.util.*;

public interface IChildCommand extends CommandExecutor {
    default String getUsage() {return "";}
    default Vector<Class<?>> getArgumentsTypes(){return new Vector<>();}
    default Vector<String> getArgumentsDescriptions(){return new Vector<>();}
    default String getPermission() {return "";}

}
