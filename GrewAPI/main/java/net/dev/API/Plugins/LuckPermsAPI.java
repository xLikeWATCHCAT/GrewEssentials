package net.dev.API.Plugins;

import me.lucko.luckperms.common.bulkupdate.comparison.*;
import me.lucko.luckperms.common.model.*;
import me.lucko.luckperms.common.node.types.*;
import me.lucko.luckperms.common.plugin.*;

import net.luckperms.api.*;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.*;
import net.luckperms.api.query.*;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.permissions.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class LuckPermsAPI {
    public static String getPlayerPrefix(Player p){
        UUID u = p.getUniqueId();
        LuckPerms api=LuckPermsProvider.get();
        QueryOptions queryOptions = Objects.requireNonNull(api).getContextManager().getQueryOptions(p);
        return Objects.requireNonNull(LuckPermsProvider.get().getUserManager().getUser(u)).getCachedData().getMetaData(queryOptions).getPrefix();
    }
    public static String getPlayerSuffix(Player p){
        UUID u = p.getUniqueId();
        LuckPerms api=LuckPermsProvider.get();
        QueryOptions queryOptions = Objects.requireNonNull(api).getContextManager().getQueryOptions(p);
        return Objects.requireNonNull(LuckPermsProvider.get().getUserManager().getUser(u)).getCachedData().getMetaData(queryOptions).getSuffix();
    }
    public static void setPlayerPrefix(UUID playerUUID,String prefix,int priority,long durationTime,TimeUnit timeUnit){
        LuckPerms api = LuckPermsProvider.get();
        net.luckperms.api.model.user.User user = api.getUserManager().getUser(playerUUID);
        NodeBuilderRegistry nodeFactory = api.getNodeBuilderRegistry();
        Node prefixNode = nodeFactory.forPrefix().priority(priority).prefix(prefix).expiry(durationTime, timeUnit).build();
        Objects.requireNonNull(user).transientData().add(prefixNode);
        api.getUserManager().saveUser(user);
    }
    public static void setPlayerSuffix(UUID playerUUID,String suffix,int priority,long durationTime,TimeUnit timeUnit){
        LuckPerms api = LuckPermsProvider.get();
        net.luckperms.api.model.user.User user = api.getUserManager().getUser(playerUUID);
        NodeBuilderRegistry nodeFactory = api.getNodeBuilderRegistry();
        Node suffixNode = nodeFactory.forSuffix().priority(priority).suffix(suffix).expiry(durationTime, timeUnit).build();
        Objects.requireNonNull(user).transientData().add(suffixNode);
        api.getUserManager().saveUser(user);
    }
    public static boolean isInGroup(Permissible p, String group) {
        return p.hasPermission("group." + group);
    }
    public static Vector<String> getUserGroupList(Permissible p, Collection<String> possibleGroups) {
        Vector<String> ret=new Vector<>();
        for (String group : possibleGroups) {
            if (p.hasPermission("group." + group))
                ret.add(group);

        }
        return ret;
    }
    public static List<? extends Group> getAllGroupList()
    {
        LuckPermsPlugin plugin=(LuckPermsPlugin)Bukkit.getPluginManager().getPlugin("LuckPerms");
        try {
            plugin.getStorage().loadAllGroups().get();
        } catch (Exception var6) {
            throw new RuntimeException(var6);
        }
        return plugin.getGroupManager().getAll().values().stream().sorted((o1, o2) -> {
            int i = Integer.compare(o2.getWeight().orElse(0), o1.getWeight().orElse(0));
            return i != 0 ? i : o1.getName().compareToIgnoreCase(o2.getName());
        }).collect(Collectors.toList());
    }
    public static List<HeldNode<UUID>> getGroupMembers(Group group)
    {
        LuckPermsPlugin plugin=(LuckPermsPlugin)Bukkit.getPluginManager().getPlugin("LuckPerms");
        Constraint constraint = Constraint.of(StandardComparison.EQUAL, Inheritance.key(group.getName()));
        List<HeldNode<UUID>> matchedUsers = plugin.getStorage().getUsersWithPermission(constraint).join().stream().filter(n -> n.getNode().getValue()).collect(Collectors.toList());
        return matchedUsers;
    }
}
