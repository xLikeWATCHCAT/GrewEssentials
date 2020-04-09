package net.dev.Commands.Server.SubCommand;

import com.google.gson.*;
import net.dev.*;
import net.dev.Utils.CommandUtils.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.plugin.*;
import org.spigotmc.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class Timings implements IChildCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if(args.length >= 2){
            if(args[1].equalsIgnoreCase("start")){
                ((SimplePluginManager) Bukkit.getPluginManager()).useTimings(true);
                CustomTimingsHandler.reload();
                sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Timings.Start")).replace("$prefix",StringUtils.Prefix));
            }else if(args[1].equalsIgnoreCase("paste")){
                if (!Bukkit.getServer().getPluginManager().useTimings()) {
                    sender.sendMessage(StringUtils.getCommandInfo("server"));
                    return true;
                }
                sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Timings.Paste")).replace("$prefix",StringUtils.Prefix));
                long timingsstarttime = System.nanoTime() - org.bukkit.command.defaults.TimingsCommand.timingStart;
                int inta = 0;
                File timings = new File("timings");
                timings.mkdirs();
                File timingsfile = new File(timings, "timings.txt");

                StringBuilder stringbuilder;
                ByteArrayOutputStream bytearrayoutputstream;
                for(bytearrayoutputstream = new ByteArrayOutputStream(); timingsfile.exists(); timingsfile = new File(timings, stringbuilder.append(inta).append(".txt").toString())) {
                    stringbuilder = (new StringBuilder()).append("timings");
                    ++inta;
                }

                PrintStream printstream = new PrintStream(bytearrayoutputstream);
                CustomTimingsHandler.printTimings(printstream);
                printstream.println("Sample time " + timingsstarttime + " (" + (double)timingsstarttime / 1.0E9D + "s)");
                printstream.println("<spigotConfig>");
                printstream.println(Bukkit.spigot().getConfig().saveToString());
                printstream.println("</spigotConfig>");
                (new PasteThread(sender, bytearrayoutputstream)).start();
            }else if(args[1].equalsIgnoreCase("stop")){
                ((SimplePluginManager) Bukkit.getPluginManager()).useTimings(false);
                sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Timings.Stop")).replace("$prefix",StringUtils.Prefix));
            }else{
                sender.sendMessage(StringUtils.getCommandInfo("server"));
            }
        }else{
            sender.sendMessage(StringUtils.getCommandInfo("server"));
        }
        return true;
    }
    private static class PasteThread extends Thread {
        private final CommandSender sender;
        private final ByteArrayOutputStream bout;

        PasteThread(CommandSender sender, ByteArrayOutputStream bytearrayoutputstream) {
            this.sender = sender;
            this.bout = bytearrayoutputstream;
        }

        public synchronized void start() {
            if (this.sender instanceof RemoteConsoleCommandSender) {
                this.run();
            } else {
                super.start();
            }
        }

        public void run() {
            try {
                HttpURLConnection url = (HttpURLConnection)(new URL("https://timings.spigotmc.org/paste")).openConnection();
                url.setDoOutput(true);
                url.setRequestMethod("POST");
                url.setInstanceFollowRedirects(false);
                OutputStream outputstream = url.getOutputStream();
                Throwable throwable = null;

                try {
                    outputstream.write(this.bout.toByteArray());
                } catch (Throwable e) {
                    throwable = e;
                    throw e;
                } finally {
                    if (outputstream != null) {
                        if (throwable != null) {
                            try {
                                outputstream.close();
                            } catch (Throwable e) {
                                throwable.addSuppressed(e);
                            }
                        } else {
                            outputstream.close();
                        }
                    }

                }
                JsonObject json = (new Gson()).fromJson(new InputStreamReader(url.getInputStream()), JsonObject.class);
                url.getInputStream().close();
                String key = json.get("key").getAsString();
                this.sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Timings.PasteSuccess")).replace("$prefix",StringUtils.Prefix).replace("$url","https://www.spigotmc.org/go/timings?url=" + key));
            } catch (IOException e) {
                e.printStackTrace();
                this.sender.sendMessage(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Timings.PasteFailed")).replace("$prefix",StringUtils.Prefix));
            }
        }
    }
    @Override
    public Vector<Class<?>> getArgumentsTypes() { return VectorUtil.toVector(String.class); }
    @Override
    public Vector<String> getArgumentsDescriptions()
    {
        return VectorUtil.toVector("timings");
    }
    @Override
    public String getPermission() { return GrewEssentials.getInstance().Config.getString("Permissions.ServerManager"); }
}
