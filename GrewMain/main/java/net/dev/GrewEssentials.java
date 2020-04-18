package net.dev;

import net.dev.API.Minecraft.*;
import net.dev.Commands.*;
import net.dev.Commands.Gamemode.*;
import net.dev.Commands.GrewEssentials.*;
import net.dev.Commands.Player.*;
import net.dev.Commands.Server.*;
import net.dev.Commands.Teleport.*;
import net.dev.Commands.Warp.*;
import net.dev.File.Json.*;
import net.dev.File.Yaml.*;
import net.dev.File.struct.*;
import net.dev.JavaScript.*;
import net.dev.Listeners.*;
import net.dev.Utils.StringUtils.*;
import net.dev.Utils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.*;
import org.mozilla.javascript.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import static net.dev.JavaScript.LoadJavaScript.*;
import static net.dev.ReflectionWrapper.*;
import static net.dev.Utils.CommandUtils.RegisterCommands.*;
import static net.dev.Utils.DatabaseUtils.LoadDatabase.*;
import static net.dev.Utils.LogUtils.LogUtils.*;

public class GrewEssentials extends JavaPlugin {
    public static GrewEssentials instance;
    public static GrewEssentials getInstance() { return instance; }
    public SimpleConfig<WarpConfig> data =new SimpleConfig(new File(this.getDataFolder(),"data.json").getAbsoluteFile().toString(),"UTF8",WarpConfig.class);
    public ConfigFile Config;
    public MessageFile Message;
    public logMessageFile log;
    public BossBarFile bossbar;

    public static String version = "XX_XX_RXX";
    public Context context;
    public Scriptable scope;
    public PluginUtil util;
    public static Thread mainThread;
    static{
        mainThread=Thread.currentThread();
    }
    {
        init();
    }

    public static Plugin getPlugin() {
        return instance;
    }

    public void init()
    {
        instance=this;
        try {
            context=Context.enter();
            context.setOptimizationLevel(-1);
            context.setLanguageVersion(Context.VERSION_ES6);
            scope=context.initStandardObjects();
            ScriptableObject.defineClass(scope, PluginUtil.class);
            util=(PluginUtil) context.newObject(scope,"PluginUtil");
            scope.put("PluginUtil",scope,util);
            loadJsFiles(context,scope,new File(this.getDataFolder().toString()+File.separator+"JavaScript"));
        }catch(Throwable e){throw new RuntimeException(e);}
    }
    public <T> T tryInvokeFunction(String name,Object... args)
    {
        Object f=scope.get(name,scope);
        if(f instanceof Function)
            return (T)invokeJsFunction((Function)f,context,scope,args);
        return null;
    }
    @Override
    public void onLoad()
    {
        tryInvokeFunction("onLoad");
    }
    {
        init();
    }
    @Override
    public void onEnable(){
        try{
            try{
                version = getVersion();
            }catch (Throwable e){}
            loadFile();
            loadDatabase();
            regCmds();
            regTabCms();
            regListeners();
            log();
            tryInvokeFunction("onEnable");
        }catch (Throwable e){}
    }
    @Override
    public void onDisable(){
        try{
            try{
                Bukkit.getOnlinePlayers().parallelStream().forEach(i->{
                    BossBar.getIndexesAtomicByPlayer(i,i2->{
                        for(AtomicInteger i3 : i2)
                        {
                            BossBar.removeBossbar(i3);
                        }
                    });
                });
            }catch (Throwable e){}
            try{
            Bukkit.getOnlinePlayers().forEach(i->{
                if(i.getOpenInventory() !=null && InvSeeCommand.ivs.containsKey(i.getOpenInventory()))
                    i.closeInventory();
            });
            }catch (Throwable e){}
            tryInvokeFunction("onDisable");
            try{
                db.close();
            }catch (Throwable e){
                Utils.sendConsole(getInstance().Message.getString("DataBase.DatabaseUnLoadError"));
            }
            Context.exit();
            instance = null;
        }catch (Throwable e){}
    }

    private void loadFile(){
        try {
            this.data.loadConfig();
        }catch (Throwable e){throw new RuntimeException(e);}
        Message = new MessageFile(this);
        Config = new ConfigFile(this);
        log = new logMessageFile(this);
        bossbar = new BossBarFile(this);
    }

    private void regListeners(){
        try{
            Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
            Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), this);
            Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
            Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), this);
            Bukkit.getPluginManager().registerEvents(new InventoryListener(),this);
        }catch (Throwable e){}
    }

    private void regCmds() {
        try{
            regComWithCompleter(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("gamemode",new GamemodeCommand(),this),"gm"), StringUtils.translateColorCodes(getInstance().Message.getString("Gamemode.Usage").replace("$prefix",""))),StringUtils.translateColorCodes(getInstance().Message.getString("Gamemode.Usage_Explanation").replace("$prefix",""))),this));
            regComWithCompleter(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("grewessentials",new GrewEssentialsCommand(),this),"ge","g","ghelp","grewessentialshelp"), StringUtils.translateColorCodes(getInstance().Message.getString("Help.Usage").replace("$prefix",""))),StringUtils.translateColorCodes(getInstance().Message.getString("Help.Usage_Explanation").replace("$prefix",""))),this));
            regComWithCompleter(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("grewreload",new ReloadCommand(),this),"greload"), StringUtils.translateColorCodes(getInstance().Message.getString("Reload.Usage").replace("$prefix",""))),StringUtils.translateColorCodes(getInstance().Message.getString("Reload.Usage_Explanation").replace("$prefix",""))),this));
            regComWithCompleter(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("server",new ServerCommand(),this),"服务器","servers","servermanager","sm","服务器管理","服务器性能"), StringUtils.translateColorCodes(getInstance().Message.getString("ServerManager.Usage").replace("$prefix",""))),StringUtils.translateColorCodes(getInstance().Message.getString("ServerManager.Usage_Explanation").replace("$prefix",""))),this));
            regComWithCompleter(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("warp",new WarpCommand(),this),"data","传送"), StringUtils.translateColorCodes(getInstance().Message.getString("Warp.Usage").replace("$prefix",""))),StringUtils.translateColorCodes(getInstance().Message.getString("Warp.Usage_Explanation").replace("$prefix",""))),this));
            regComWithCompleter(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("removewarp",new RemoveWarpCommand(),this),"remwarp","deletewarp"), StringUtils.translateColorCodes(getInstance().Message.getString("RemoveWarp.Usage").replace("$prefix",""))),StringUtils.translateColorCodes(getInstance().Message.getString("RemoveWarp.Usage_Explanation").replace("$prefix",""))),this));
            regComWithCompleter(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("player",new PlayerCommand(),this),"p","playerinfo","name","uuid"), StringUtils.translateColorCodes(getInstance().Message.getString("PlayerInfo.Usage").replace("$prefix",""))),StringUtils.translateColorCodes(getInstance().Message.getString("PlayerInfo.Usage_Explanation").replace("$prefix",""))),this));
            regComWithCompleter(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("speed",new SpeedCommand(),this),"speeds","速度"),StringUtils.translateColorCodes(getInstance().Message.getString("Speed.Usage")).replace("$prefix","")),StringUtils.translateColorCodes(getInstance().Message.getString("Speed.Usage_Explanation"))),this));

            regCom(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("sign",new SignCommand(),this),"changesign"), StringUtils.translateColorCodes(getInstance().Message.getString("ChangeSign.Usage").replace("$prefix",""))),StringUtils.translateColorCodes(getInstance().Message.getString("ChangeSign.Usage_Explanation").replace("$prefix",""))),this));
            regCom(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("setwarp",new SetWarpCommand(),this),"设置坐标"), StringUtils.translateColorCodes(getInstance().Message.getString("SetWarp.Usage").replace("$prefix",""))),StringUtils.translateColorCodes(getInstance().Message.getString("SetWarp.Usage_Explanation").replace("$prefix",""))),this));
            regCom(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("light",new LightCommand(),this),"shock"), StringUtils.translateColorCodes(getInstance().Message.getString("Light.Alone.Usage").replace("$prefix",""))),StringUtils.translateColorCodes(getInstance().Message.getString("Light.Alone.Usage_Explanation").replace("$prefix",""))),this));
            regCom(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("lighta",new LightaCommand(),this),"shocka"), StringUtils.translateColorCodes(getInstance().Message.getString("Light.All.Usage").replace("$prefix",""))),StringUtils.translateColorCodes(getInstance().Message.getString("Light.All.Usage_Explanation").replace("$prefix",""))),this));
            regCom(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("invsee",new InvSeeCommand(),this),"inv","inventory"), StringUtils.translateColorCodes(getInstance().Message.getString("InvSee.Usage").replace("$prefix",""))),StringUtils.translateColorCodes(getInstance().Message.getString("InvSee.Usage_Explanation").replace("$prefix",""))),this));
            regCom(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("sucide",new SucideCommand(),this),"自杀"), StringUtils.translateColorCodes(getInstance().Message.getString("Sucide.Usage").replace("$prefix",""))),StringUtils.translateColorCodes(getInstance().Message.getString("Sucide.Usage_Explanation").replace("$prefix",""))),this));
            regCom(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("broadcast",new BroadCastCommand(),this),"bc"), StringUtils.translateColorCodes(getInstance().Message.getString("BroadCast.Usage").replace("$prefix",""))),StringUtils.translateColorCodes(getInstance().Message.getString("BroadCast.Usage_Explanation").replace("$prefix",""))),this));
            regCom(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("announcement",new AnnouncementCommand(),this),"note"), StringUtils.translateColorCodes(getInstance().Message.getString("Announcement.Usage").replace("$prefix",""))),StringUtils.translateColorCodes(getInstance().Message.getString("Announcement.Usage_Explanation").replace("$prefix",""))),this));
            regCom(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("KickAll",new KickAllCommand(),this),"kicka"), StringUtils.translateColorCodes(getInstance().Message.getString("KickAll.Usage").replace("$prefix",""))),StringUtils.translateColorCodes(getInstance().Message.getString("KickAll.Usage_Explanation").replace("$prefix",""))),this));
            regCom(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("fly",new FlyCommand(),this),"flight","useflight","setflight","usefly","setFly","efly","飞行"),StringUtils.translateColorCodes(getInstance().Message.getString("Fly.Usage")).replace("$prefix","")),StringUtils.translateColorCodes(getInstance().Message.getString("Fly.Usage_Explanation"))),this));
            regCom(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("hat",new HatCommand(),this),"帽子"),StringUtils.translateColorCodes(getInstance().Message.getString("Hat.Usage")).replace("$prefix","")),StringUtils.translateColorCodes(getInstance().Message.getString("Hat.Usage_Explanation"))),this));
            regCom(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("vanish",new VanishCommand(),this),"v","隐身"),StringUtils.translateColorCodes(getInstance().Message.getString("Vanish.Usage")).replace("$prefix","")),StringUtils.translateColorCodes(getInstance().Message.getString("Vanish.Usage_Explanation"))),this));
            regCom(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("tpall",new TPAllCommand(),this),"tpalll"),StringUtils.translateColorCodes(getInstance().Message.getString("TelePort.TPALL.Usage")).replace("$prefix","")),StringUtils.translateColorCodes(getInstance().Message.getString("TelePort.TPALL.Usage_Explanation"))),this));
            regCom(this.getName(),setTabCom(setComDesc(setComUsa(setComAlias(newPluginCommand("tphere",new TPHereCommand(),this),"tph"),StringUtils.translateColorCodes(getInstance().Message.getString("TelePort.TPHERE.Usage")).replace("$prefix","")),StringUtils.translateColorCodes(getInstance().Message.getString("TelePort.TPHERE.Usage_Explanation"))),this));
        }catch (Throwable e){}
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Object b=tryInvokeFunction("onCommand",sender,command,label,args);
        return (b!=null&&!Objects.equals(Undefined.instance,b))?(boolean)b:super.onCommand(sender,command,label,args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Object b=tryInvokeFunction("onTabComplete",sender, command, alias, args);
        return (b!=null&&!Objects.equals(Undefined.instance,b))?(List<String>)b:super.onTabComplete(sender,command,alias,args);
    }

    private void regTabCms(){
        new GrewEssentialsCommand().initChildCommands();
        new PlayerCommand().initChildCommands();
        new ServerCommand().initChildCommands();
    }
}
