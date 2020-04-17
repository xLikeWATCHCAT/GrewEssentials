package net.dev.Utils;

import net.dev.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.*;
import org.bukkit.plugin.*;

import java.io.*;
import java.lang.reflect.*;
import java.text.*;
import java.util.*;

public class ReportWriter {
    private static SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd kk:mm Z");
    private Date date = new Date();
    public StringBuilder output = new StringBuilder();
    private String flags = "";
    Plugin plugin;
    Method customInfo;
    Object methodinstance;

    public ReportWriter(Plugin var1, Method var2, Object var3) {
        this.plugin = var1;
        this.customInfo = var2;
        this.methodinstance = var3;
    }

    public void generate() {
        this.appendReportHeader(this.plugin);
        this.appendServerInformation(this.plugin.getServer());
        this.appendPluginInformation(this.plugin.getServer().getPluginManager().getPlugins());

        try {
            this.customInfo.invoke(this.methodinstance, (Object[])null);
        } catch (Exception var6) {
            this.appendln(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Info.Error")));
            this.appendln(var6.getMessage());
            StackTraceElement[] var2 = var6.getStackTrace();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                StackTraceElement var5 = var2[var4];
                this.appendln(var5.toString());
            }
        }

        this.appendln("-------------");
        this.appendln(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Info.End")));
        this.appendln();
    }

    protected static String repeat(String var0, int var1) {
        if (var0 == null) {
            return null;
        } else {
            StringBuilder var2 = new StringBuilder();

            for(int var3 = 0; var3 < var1; ++var3) {
                var2.append(var0);
            }

            return var2.toString();
        }
    }

    public void append(LogListBlock var1) {
        this.output.append(var1.toString());
    }

    public void appendln(String var1) {
        this.output.append(var1);
        this.output.append("\r\n");
    }

    public void appendln(String var1, Object... var2) {
        this.output.append(String.format(var1, var2));
        this.output.append("\r\n");
    }

    public void appendln() {
        this.output.append("\r\n");
    }

    public void appendHeader(String var1) {
        String var2 = repeat("-", var1.length());
        this.output.append(var2);
        this.output.append("\r\n");
        this.appendln(var1);
        this.output.append(var2);
        this.output.append("\r\n");
        this.appendln();
    }

    private void appendReportHeader(Plugin var1) {
        this.appendln(var1.getName());
        this.appendln(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Info.GenerationTime")) + dateFmt.format(this.date));
        this.appendln();
        this.appendln(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Info.PluginVersion")) + var1.getDescription().getVersion());
        this.appendln();
    }

    private void appendServerInformation(Server var1) {
        this.appendHeader(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Info.ServerInfo")));
        LogListBlock var2 = new LogListBlock();
        Runtime var3 = Runtime.getRuntime();
        var2.put(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Info.JavaInfo")), "%s %s (%s)", System.getProperty("java.vendor"), System.getProperty("java.version"), System.getProperty("java.vendor.url"));
        var2.put(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Info.OperatingSystem")), "%s %s (%s)", System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"));
        var2.put(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Info.NumberOfProcessors")), var3.availableProcessors());
        var2.put(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Info.FreeMemory")), var3.freeMemory() / 1024L / 1024L + " MB");
        var2.put(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Info.MaximumMemory")), var3.maxMemory() / 1024L / 1024L + " MB");
        var2.put(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Info.TotalMemory")), var3.totalMemory() / 1024L / 1024L + " MB");
        var2.put(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Info.Server")), var1.getServerId());
        var2.put(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Info.ServerName")), var1.getServerName());
        var2.put(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Info.ServerVersion")), var1.getVersion());
        var2.put(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Info.OnlinePlayers")), "%d/%d", var1.getOnlinePlayers().size(), var1.getMaxPlayers());
        this.append(var2);
        this.appendln();
    }

    private void appendPluginInformation(Plugin[] var1) {
        this.appendHeader(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("ServerManager.Info.PluginList"))+" (" + var1.length + ")");
        LogListBlock var2 = new LogListBlock();
        Plugin[] var3 = var1;
        int var4 = var1.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Plugin var6 = var3[var5];
            var2.put(var6.getDescription().getName(), var6.getDescription().getVersion());
        }

        this.append(var2);
        this.appendln();
    }

    public void write(File var1) throws IOException {
        OutputStreamWriter var2 = null;

        try {
            var2 = new OutputStreamWriter(new FileOutputStream(var1), "UTF-8");
            BufferedWriter var3 = new BufferedWriter(var2);
            var3.write(this.output.toString());
            var3.close();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        if (var2 != null) {
            try {
                var2.close();
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

    }

    public String toString() {
        return this.output.toString();
    }

    public String getFlags() {
        return this.flags;
    }

    public void appendFlags(String var1) {
        this.flags = this.flags + var1;
    }

    public class LogListBlock {
        private LinkedHashMap<String, Object> items = new LinkedHashMap();
        private int maxKeyLength = 0;

        public LogListBlock() {
        }

        private void updateKey(String var1) {
            if (var1.length() > this.maxKeyLength) {
                this.maxKeyLength = var1.length();
            }

        }

        public LogListBlock put(String var1, String var2) {
            this.updateKey(var1);
            this.items.put(var1, String.valueOf(var2));
            return this;
        }

        public LogListBlock put(String var1, LogListBlock var2) {
            this.updateKey(var1);
            this.items.put(var1, var2);
            return this;
        }

        public LogListBlock put(String var1, Object var2) {
            this.put(var1, String.valueOf(var2));
            return this;
        }

        public LogListBlock put(String var1, String var2, Object... var3) {
            this.put(var1, String.format(var2, var3));
            return this;
        }

        public LogListBlock put(String var1, int var2) {
            this.put(var1, String.valueOf(var2));
            return this;
        }

        public LogListBlock put(String var1, byte var2) {
            this.put(var1, String.valueOf(var2));
            return this;
        }

        public LogListBlock put(String var1, double var2) {
            this.put(var1, String.valueOf(var2));
            return this;
        }

        public LogListBlock put(String var1, float var2) {
            this.put(var1, String.valueOf(var2));
            return this;
        }

        public LogListBlock put(String var1, short var2) {
            this.put(var1, String.valueOf(var2));
            return this;
        }

        public LogListBlock put(String var1, long var2) {
            this.put(var1, String.valueOf(var2));
            return this;
        }

        public LogListBlock put(String var1, boolean var2) {
            this.put(var1, String.valueOf(var2));
            return this;
        }

        public LogListBlock putChild(String var1) {
            this.updateKey(var1);
            LogListBlock var2 = ReportWriter.this.new LogListBlock();
            this.items.put(var1, var2);
            return var2;
        }

        private String padKey(String var1, int var2) {
            return String.format("%-" + var2 + "s", var1);
        }

        protected String getOutput(String var1) {
            StringBuilder var2 = new StringBuilder();
            Iterator var3 = this.items.entrySet().iterator();

            while(var3.hasNext()) {
                Map.Entry var4 = (Map.Entry)var3.next();
                Object var5 = var4.getValue();
                if (var5 instanceof LogListBlock) {
                    var2.append(var1);
                    var2.append(this.padKey((String)var4.getKey(), this.maxKeyLength));
                    var2.append(":\r\n");
                    var2.append(((LogListBlock)var5).getOutput(var1 + "    "));
                } else {
                    var2.append(var1);
                    var2.append(this.padKey((String)var4.getKey(), this.maxKeyLength));
                    var2.append(": ");
                    var2.append(var5.toString());
                    var2.append("\r\n");
                }
            }

            return var2.toString();
        }

        public String toString() {
            return this.getOutput("");
        }
    }
}
