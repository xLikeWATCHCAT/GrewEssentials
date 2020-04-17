package net.dev.Action;

import net.dev.Action.acts.*;
import net.dev.*;
import org.bukkit.*;
import org.bukkit.entity.*;

import java.util.*;
import java.util.regex.*;

public class Action {
    private static List<net.dev.Action.acts.AbstractAction> actions =
            Arrays.asList(
            new ActionUnknow(),
            new ActionCommand(),
            new ActionActionbar(),
            new ActionBreak(),
            new ActionCleanCatchers(),
            new ActionClose(),
            new ActionCommandConsole(),
            new ActionCommandOp(),
            new ActionConnect(),
            new ActionDelay(),
            new ActionGiveMoney(),
            new ActionJs(),
            new ActionSetArgs(),
            new ActionSound(),
            new ActionTakeItem(),
            new ActionTakeMoney(),
            new ActionTell(),
            new ActionTitle()
    );

    public static void runActions(List<AbstractAction> actions, Player player) {
        long delay = 0;
        for (AbstractAction action : actions) {
            if (delay > 0) {
                Bukkit.getScheduler().runTaskLater(GrewEssentials.getPlugin(), () -> action.run(player), delay);
            } else {
                action.run(player);
            }
        }
    }

    /**
     * 同行并列多个动作, 动作选项共享
     * 分割符号 “_||_”
     *
     * @param lines 行
     * @return 动作
     */
    public static List<AbstractAction> readActions(List<String> lines) {
        List<AbstractAction> actions = new ArrayList<>();
        lines.forEach(line -> actions.addAll(readActions(line)));
        return actions;
    }

    public static List<AbstractAction> readActions(String line) {
        List<AbstractAction> actions = new ArrayList<>();
        HashMap<EnumOption, String> options = new HashMap<>();
        for (String s : line.split("_\\|\\|_")) {
            AbstractAction read = readSingleAction(s);
            if (read != null) {
                actions.add(read);
                if (read.getOptions() != null) {
                    options.putAll(read.getOptions());
                }
            }
        }
        if (actions.size() > 1) {
            actions.forEach(act -> act.setOptions(options));
        }
        return actions;
    }

    /**
     * 读单个动作
     *
     * @param line 行
     * @return 动作
     */
    private static AbstractAction readSingleAction(String line) {
        String tag = line.replaceAll("<([^<>].+)>", "").split(":", 2)[0];
        AbstractAction action = actions.stream().filter(act -> tag.matches("(?i)" + act.getName())).findFirst().orElse(actions.get(0));
        HashMap<EnumOption, String> options = new HashMap<>();

        if (action == null) {
            return null;
        } else {
            action = action.create();
            line = line.replaceFirst(tag + "( )?:( )?", "");
        }

        for (EnumOption option : EnumOption.values()) {
            Matcher matcher = option.matcher(line);
            while (matcher.find()) {
                String find = matcher.group();
                String[] opts = find.split(":", 2);
                String value = opts.length >= 2 ? opts[1].substring(0, opts[1].length() - 1) : null;
                options.put(option, value);
                line = line.replace(find, "");
            }
        }

        action.setContent(line);
        action.setOptions(options);
        return action;
    }

}
