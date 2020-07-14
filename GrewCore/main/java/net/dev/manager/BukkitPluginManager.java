package net.dev.manager;

import net.dev.*;
import org.apache.commons.lang.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.command.defaults.*;
import org.bukkit.craftbukkit.libs.joptsimple.*;
import org.bukkit.event.*;
import org.bukkit.permissions.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.*;
import org.bukkit.scheduler.*;
import org.spigotmc.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.util.regex.*;

import static net.dev.utils.string.StringUtils.*;

public class BukkitPluginManager {
    public static <K,V> void removeWhereValueEquals(Map<K,V> m, V value)
    {
        Vector<K> keysToBeRemoved=new Vector<>();
        for(Map.Entry<K,V> e : m.entrySet())
            if(Objects.equals(e.getValue(),value))
                keysToBeRemoved.add(e.getKey());
        for(K k : keysToBeRemoved)
            m.remove(k);
    }
    public static <T> void removeWhereEquals(Collection<T> c,T v)
    {
        c.removeIf(i->Objects.equals(i,v));
    }

    public static org.bukkit.plugin.Plugin reloadPlugin(org.bukkit.plugin.Plugin p)
    {
        String name=p.getName();
        List<String> sft=p.getDescription().getSoftDepend();
        List<String> hrd=p.getDescription().getDepend();
        unloadPlugin(p);
        return loadAndEnablePlugin(name,toArrayList(sft),toArrayList(hrd));
    }
    public static org.bukkit.plugin.Plugin[] loadPluginsByManager(File directory, String targetPname, ArrayList<String> softds, ArrayList<String> hardds) throws InvalidDescriptionException
    {
        Validate.notNull(directory, "Directory cannot be null");
        Validate.isTrue(directory.isDirectory(), "Directory must be a directory");
        Map<String,Object> dpcls=new HashMap<>();
        if(softds==null)
            softds=new ArrayList<>();
        if(hardds==null)
            hardds=new ArrayList<>();
        String[] tmp=softds.parallelStream().toArray(String[]::new);
        softds.clear();
        for(String i : tmp)
            softds.add(i.replace(' ', '_'));
        tmp=hardds.parallelStream().toArray(String[]::new);
        hardds.clear();
        for(String i : tmp)
            hardds.add(i.replace(' ', '_'));
        List<org.bukkit.plugin.Plugin> result = new ArrayList();
        Map<Pattern,PluginLoader> fileAssociations=ReflectionWrapper.getFieldValue(ReflectionWrapper.getField(Bukkit.getServer().getPluginManager().getClass(),"fileAssociations"),Bukkit.getServer().getPluginManager());
        Set<Pattern> filters = fileAssociations.keySet();
        if (!Bukkit.getServer().getUpdateFolder().equals("")) {
            ReflectionWrapper.setFieldValue(ReflectionWrapper.getField(SimplePluginManager.class,"updateDirectory"),Bukkit.getServer().getPluginManager(),new File(directory, Bukkit.getServer().getUpdateFolder()));
        }

        Map<String, File> plugins = new HashMap();
        Set<String> loadedPlugins = new HashSet();
        Map<String, Collection<String>> dependencies = new HashMap();
        Map<String, Collection<String>> softDependencies = new HashMap();
        File[] var8;
        int var9 = (var8 = directory.listFiles()).length;

        for(int var10 = 0; var10 < var9; ++var10) {
            File file = var8[var10];
            PluginLoader loader = null;
            Iterator var13 = filters.iterator();

            Pattern filter;
            while(var13.hasNext()) {
                filter = (Pattern)var13.next();
                Matcher match = filter.matcher(file.getName());
                if (match.find()) {
                    loader = fileAssociations.get(filter);
                }
            }

            if (loader != null) {
                filter = null;

                PluginDescriptionFile description;
                try {
                    description = loader.getPluginDescription(file);
                    String name = description.getName();
                    if (name.equalsIgnoreCase("bukkit") || name.equalsIgnoreCase("minecraft") || name.equalsIgnoreCase("mojang")) {
                        Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "': Restricted Name");
                        continue;
                    }
                    if (((String)ReflectionWrapper.getFieldValue(ReflectionWrapper.getField(description.getClass(),"rawName"),description)).indexOf(32) != -1) {
                        Bukkit.getServer().getLogger().warning(String.format("BukkitPluginManager `%s' uses the space-character (0x20) in its name `%s' - this is discouraged", description.getFullName(), ReflectionWrapper.getFieldValue(ReflectionWrapper.getField(description.getClass(),"rawName"),description)));
                    }
                } catch (InvalidDescriptionException var23) {
                    Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "'", var23);
                    continue;
                }

                File replacedFile = plugins.put(description.getName(), file);
                if (replacedFile != null) {
                    Bukkit.getServer().getLogger().severe(String.format("Ambiguous plugin name `%s' for files `%s' and `%s' in `%s'", description.getName(), file.getPath(), replacedFile.getPath(), directory.getPath()));
                }

                Collection<String> softDependencySet = description.getSoftDepend();
                if (softDependencySet != null && !softDependencySet.isEmpty()) {
                    if (softDependencies.containsKey(description.getName())) {
                        softDependencies.get(description.getName()).addAll(softDependencySet);
                    } else {
                        softDependencies.put(description.getName(), new LinkedList(softDependencySet));
                    }
                }

                Collection<String> dependencySet = description.getDepend();
                if (dependencySet != null && !dependencySet.isEmpty()) {
                    dependencies.put(description.getName(), new LinkedList(dependencySet));
                }

                Collection<String> loadBeforeSet = description.getLoadBefore();
                if (loadBeforeSet != null && !loadBeforeSet.isEmpty()) {
                    Iterator var18 = loadBeforeSet.iterator();

                    while(var18.hasNext()) {
                        String loadBeforeTarget = (String)var18.next();
                        if (softDependencies.containsKey(loadBeforeTarget)) {
                            softDependencies.get(loadBeforeTarget).add(description.getName());
                        } else {
                            Collection<String> shortSoftDependency = new LinkedList();
                            shortSoftDependency.add(description.getName());
                            softDependencies.put(loadBeforeTarget, shortSoftDependency);
                        }
                    }
                }
            }
        }

        while(true) {
            File file;
            boolean missingDependency;
            label157:
            do {
                String plugin;
                Iterator pluginIterator;
                do {
                    if (plugins.isEmpty()) {
                        TimingsCommand.timingStart = System.nanoTime();
                        return result.toArray(new org.bukkit.plugin.Plugin[result.size()]);
                    }

                    missingDependency = true;
                    pluginIterator = plugins.keySet().iterator();

                    while(pluginIterator.hasNext()) {
                        plugin = (String)pluginIterator.next();
                        Iterator softDependencyIterator;
                        String dependency;
                        if (dependencies.containsKey(plugin)) {
                            softDependencyIterator = ((Collection)dependencies.get(plugin)).iterator();

                            while(softDependencyIterator.hasNext()) {
                                dependency = (String)softDependencyIterator.next();
                                if (loadedPlugins.contains(dependency)) {
                                    softDependencyIterator.remove();
                                } else if (!plugins.containsKey(dependency)) {
                                    missingDependency = false;
                                    File file2 = plugins.get(plugin);
                                    pluginIterator.remove();
                                    softDependencies.remove(plugin);
                                    dependencies.remove(plugin);
                                    Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not load '" + file2.getPath() + "' in folder '" + directory.getPath() + "'", new UnknownDependencyException(dependency));
                                    break;
                                }
                            }

                            if (dependencies.containsKey(plugin) && dependencies.get(plugin).isEmpty()) {
                                dependencies.remove(plugin);
                            }
                        }

                        if (softDependencies.containsKey(plugin)) {
                            softDependencyIterator = ((Collection)softDependencies.get(plugin)).iterator();

                            while(softDependencyIterator.hasNext()) {
                                dependency = (String)softDependencyIterator.next();
                                if (!plugins.containsKey(dependency)) {
                                    softDependencyIterator.remove();
                                }
                            }

                            if (softDependencies.get(plugin).isEmpty()) {
                                softDependencies.remove(plugin);
                            }
                        }

                        if (!dependencies.containsKey(plugin) && !softDependencies.containsKey(plugin) && plugins.containsKey(plugin)) {
                            file = plugins.get(plugin);
                            pluginIterator.remove();
                            missingDependency = false;

                            try {
                                if(plugin.replace(' ', '_').equals(targetPname.replace(' ', '_')) || softds.contains(plugin.replace(' ', '_')) || hardds.contains(plugin.replace(' ', '_')))
                                {
                                    if(Bukkit.getServer().getPluginManager().getPlugin(plugin.replace(' ', '_'))==null)
                                    {
                                        for(Pattern i : fileAssociations.keySet())
                                        {
                                            if(i.matcher(file.getName()).find())
                                            {
                                                PluginLoader loader = fileAssociations.get(i);
                                                Map<String, Object> mpcl=ReflectionWrapper.getFieldValue(ReflectionWrapper.getField(JavaPluginLoader.class,"loaders"),loader);
                                                for(Map.Entry<String,Object> iawa : dpcls.entrySet())
                                                    mpcl.put(iawa.getKey(),iawa.getValue());
                                            }
                                        }
                                        result.add(Bukkit.getServer().getPluginManager().loadPlugin(file));
                                        loadedPlugins.add(plugin);
                                    }else{
                                        PluginLoader depllod=Bukkit.getServer().getPluginManager().getPlugin(plugin.replace(' ', '_')).getPluginLoader();
                                        Map<String, Object> mpcl=ReflectionWrapper.getFieldValue(ReflectionWrapper.getField(JavaPluginLoader.class,"loaders"),depllod);
                                        dpcls.put(plugin,mpcl.get(plugin));
                                        result.add(Bukkit.getServer().getPluginManager().getPlugin(plugin.replace(' ', '_')));
                                        loadedPlugins.add(plugin);
                                    }
                                }
                            } catch (InvalidPluginException var21) {
                                Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "'", var21);
                            }
                        }
                    }
                } while(!missingDependency);

                pluginIterator = plugins.keySet().iterator();

                while(true) {
                    do {
                        if (!pluginIterator.hasNext()) {
                            continue label157;
                        }

                        plugin = (String)pluginIterator.next();
                    } while(dependencies.containsKey(plugin));

                    softDependencies.remove(plugin);
                    missingDependency = false;
                    file = plugins.get(plugin);
                    pluginIterator.remove();

                    try {
                        if(plugin.replace(' ', '_').equals(targetPname.replace(' ', '_')) || softds.contains(plugin.replace(' ', '_')) || hardds.contains(plugin.replace(' ', '_')))
                        {
                            if(Bukkit.getServer().getPluginManager().getPlugin(plugin.replace(' ', '_'))==null)
                            {
                                for(Pattern i : fileAssociations.keySet())
                                {
                                    if(i.matcher(file.getName()).find())
                                    {
                                        PluginLoader loader = fileAssociations.get(i);
                                        Map<String, Object> mpcl=ReflectionWrapper.getFieldValue(ReflectionWrapper.getField(JavaPluginLoader.class,"loaders"),loader);
                                        for(Map.Entry<String,Object> iawa : dpcls.entrySet())
                                            mpcl.put(iawa.getKey(),iawa.getValue());
                                    }
                                }
                                result.add(Bukkit.getServer().getPluginManager().loadPlugin(file));
                                loadedPlugins.add(plugin);
                            }else{
                                PluginLoader depllod=Bukkit.getServer().getPluginManager().getPlugin(plugin.replace(' ', '_')).getPluginLoader();
                                Map<String, Object> mpcl=ReflectionWrapper.getFieldValue(ReflectionWrapper.getField(JavaPluginLoader.class,"loaders"),depllod);
                                dpcls.put(plugin,mpcl.get(plugin));
                                result.add(Bukkit.getServer().getPluginManager().getPlugin(plugin.replace(' ', '_')));
                                loadedPlugins.add(plugin);
                            }
                        }
                        break;
                    } catch (InvalidPluginException var22) {
                        Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "'", var22);
                    }
                }
            } while(!missingDependency);

            softDependencies.clear();
            dependencies.clear();
            Iterator failedPluginIterator = plugins.values().iterator();

            while(failedPluginIterator.hasNext()) {
                file = (File)failedPluginIterator.next();
                failedPluginIterator.remove();
                Bukkit.getServer().getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "': circular dependency detected");
            }
        }
    }
    public static org.bukkit.plugin.Plugin loadAndEnablePlugin(String name, ArrayList<String> sftdp, ArrayList<String> hrddp)
    {
        try {
            org.bukkit.plugin.Plugin plugintoret = null;
            Bukkit.getServer().getPluginManager().registerInterface(JavaPluginLoader.class);
            File pluginFolder = (File)((OptionSet)ReflectionWrapper.getFieldValue(ReflectionWrapper.getField(ReflectionWrapper.getNMSClass("MinecraftServer"),"options"),ReflectionWrapper.getFieldValue(ReflectionWrapper.getField(Bukkit.getServer().getClass(),"console"),Bukkit.getServer()))).valueOf("plugins");
            if (pluginFolder.exists()) {
                org.bukkit.plugin.Plugin[] plugins = loadPluginsByManager(pluginFolder,name,sftdp,hrddp);
                org.bukkit.plugin.Plugin[] var3 = plugins;
                int var4 = plugins.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    org.bukkit.plugin.Plugin plugin = var3[var5];
                    if(plugin.getName().replace(' ', '_').equals(name.replace(' ', '_')))
                        plugintoret=plugin;
                    if(!plugin.isEnabled())
                    {
                        try {
                            String message = String.format("Loading %s", plugin.getDescription().getFullName());
                            plugin.getLogger().info(message);
                            plugin.onLoad();
                        } catch (Throwable var8) {
                            Logger.getLogger(Bukkit.getServer().getClass().getName()).log(Level.SEVERE, var8.getMessage() + " initializing " + plugin.getDescription().getFullName() + " (Is it up to date?)", var8);
                        }
                    }
                }
            } else {
                pluginFolder.mkdir();
            }
            ReflectionWrapper.invokeMethod(ReflectionWrapper.getMethod(Bukkit.getServer().getClass(), "loadPlugin", org.bukkit.plugin.Plugin.class), Bukkit.getServer(), Objects.requireNonNull(plugintoret));
            return plugintoret;
        }catch(Throwable e){throw new RuntimeException(e);}
    }
    public static void unloadPlugin(org.bukkit.plugin.Plugin p)
    {
        String name=p.getName();
        List<Permission> permissions=p.getDescription().getPermissions();
        SimplePluginManager pm=(SimplePluginManager) Bukkit.getPluginManager();
        pm.disablePlugin(p);
        ((List<org.bukkit.plugin.Plugin>) ReflectionWrapper.getFieldValue(ReflectionWrapper.getField(pm.getClass(),"plugins"),pm)).remove(p);
        ((Map<String, org.bukkit.plugin.Plugin>)ReflectionWrapper.getFieldValue(ReflectionWrapper.getField(pm.getClass(),"lookupNames"),pm)).remove(name);
        HandlerList.unregisterAll(p);
        removeWhereValueEquals(ReflectionWrapper.getFieldValue(ReflectionWrapper.getField(pm.getClass(),"fileAssociations"),pm),p.getPluginLoader());
        Map<String,Permission> mp=ReflectionWrapper.getFieldValue(ReflectionWrapper.getField(pm.getClass(),"permissions"),pm);
        for(Permission pe : permissions)
            removeWhereValueEquals(mp,pe);
        Map<Boolean,Set<Permission>> defaultPerms=ReflectionWrapper.getFieldValue(ReflectionWrapper.getField(pm.getClass(),"defaultPerms"),pm);
        for(Permission pe : permissions)
            removeWhereEquals(defaultPerms.get(true),pe);
        for(Permission pe : permissions)
            removeWhereEquals(defaultPerms.get(false),pe);
        SimpleCommandMap scmap= ReflectionWrapper.getFieldValue(ReflectionWrapper.getField(Bukkit.getServer().getClass(),"commandMap"),Bukkit.getServer());
        Map<String, Command> cmds=ReflectionWrapper.getFieldValue(ReflectionWrapper.getField(scmap.getClass(),"knownCommands"),scmap);
        Command[] tmp=cmds.values().parallelStream().toArray(Command[]::new);
        for(Command i : tmp)
            if(i instanceof PluginCommand)
                if(Objects.equals(((PluginCommand)i).getPlugin(),p))
                {
                    i.unregister(scmap);
                    removeWhereValueEquals(cmds,i);
                }
        Bukkit.getServer().resetRecipes();
        Bukkit.getServer().getScheduler().cancelTasks(p);
        List<BukkitWorker> overdueWorkers = Bukkit.getServer().getScheduler().getActiveWorkers();

        org.bukkit.plugin.Plugin plugin;
        String author;
        for(Iterator var7 = overdueWorkers.iterator(); var7.hasNext(); ) {
            BukkitWorker worker = (BukkitWorker)var7.next();
            plugin = worker.getOwner();
            author = "<NoAuthorGiven>";
            if (plugin.getDescription().getAuthors().size() > 0) {
                author = plugin.getDescription().getAuthors().get(0);
            }
            if(Objects.equals(plugin,p))
            {
                if(!Objects.equals(worker.getThread(),GrewEssentials.mainThread))
                    if(worker.getThread()!=null && worker.getThread().isAlive())
                        try {
                            worker.getThread().stop();
                        }catch(Throwable e){}
                Bukkit.getServer().getLogger().log(Level.SEVERE, String.format("Nag author: '%s' of '%s' about the following: %s", author, plugin.getDescription().getName(), "This plugin is not properly shutting down its async tasks when it is being reloaded.  This may cause conflicts with the newly loaded version of the plugin"));
            }
        }

        try {
            CustomTimingsHandler.reload();
        }catch(Throwable e){}
    }
}
