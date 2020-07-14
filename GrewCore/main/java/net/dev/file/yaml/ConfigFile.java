package net.dev.file.yaml;

import com.google.common.base.*;
import com.google.common.io.*;
import net.dev.*;
import net.dev.utils.string.StringUtils;
import org.apache.commons.lang.*;
import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.configuration.file.*;
import org.bukkit.plugin.*;
import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.representer.*;

import java.io.*;
import java.util.logging.*;

public class ConfigFile extends YamlConfiguration {
    private File file;
    private Logger loger;
    private Plugin plugin;
    private final DumperOptions yamlOptions = new DumperOptions();
    private final Representer yamlRepresenter = new YamlRepresenter();
    private final Yaml yaml = new Yaml(new YamlConstructor(), yamlRepresenter, yamlOptions);

    private ConfigFile(File file)
    {
        Validate.notNull(file, "file cannot be null");
        this.file = file;
        loger = Bukkit.getLogger();
        init(file);
    }

    private ConfigFile(InputStream stream)
    {
        loger = Bukkit.getLogger();
        init(stream);
    }

    public ConfigFile(Plugin plugin) { this(plugin, "config.yml"); }

    private ConfigFile(Plugin plugin, File file)
    {
        Validate.notNull(file, "file cannot be null");
        Validate.notNull(plugin, "BukkitPluginManager cannot be null");
        this.plugin = plugin;
        this.file = file;
        loger = plugin.getLogger();
        check(file);
        init(file);
    }

    private ConfigFile(Plugin plugin, String filename) { this(plugin, new File(plugin.getDataFolder(), filename)); }

    private void check(File file)
    {
        String filename = file.getName();
        InputStream stream = plugin.getResource(filename);
        try
        {
            if (!file.exists())
            {
                if (stream == null)
                {
                    file.createNewFile();
                   // loger.info("配置文件 " + filename + " 创建失败...");
                } else { plugin.saveResource(filename, true); }
            }
            else
            {
                ConfigFile newcfg = new ConfigFile(stream);
                ConfigFile oldcfg = new ConfigFile(file);
                String newver = newcfg.getString("version");
                String oldver = oldcfg.getString("version");
                if ((newver != null) && !(newver.equalsIgnoreCase(oldver)))
                {
                    //loger.warning("配置文件: " + filename + " 版本 " + oldver + " 过低 正在升级到 " + newver + " ...");
                    try {
                        String b = StringUtils.getNanon();
                        oldcfg.save(new File(file.getParent(), "Backup"+File.separator +"config"+File.separator+"backup."+filename +"."+b));
                      //  loger.warning("配置文件: " + filename + " 已备份为 backup."+filename+"."+b);
                    } catch (IOException e) {
                       // loger.warning("配置文件: " + filename + " 备份失败!");
                    }
                    plugin.saveResource(filename, true);
                   // loger.info("配置文件: " + filename + " 升级成功!");
                }
            }
        }
        catch (IOException e) { /*loger.info("配置文件 " + filename + " 创建失败...");*/ }
    }
    private void init(File file)
    {
        Validate.notNull(file, "file cannot be null");
        FileInputStream stream;
        try {
            stream = new FileInputStream(file);
            init(stream);
        }
        catch (FileNotFoundException e) {
            plugin.saveResource(file.getName(), true);
        }
    }

    private void init(InputStream stream)
    {
        Validate.notNull(stream, "Stream cannot be null");
        try { this.load(new InputStreamReader(stream, Charsets.UTF_8)); }
        catch (IOException ex) {/* loger.info("配置文件 " + file.getName() + " 读取错误...");*/ }
        catch (InvalidConfigurationException ex) {/* loger.info("配置文件 " + file.getName() + " 格式错误..."); */}
    }

    @Override
    public void load(File file)
            throws IOException, InvalidConfigurationException
    {
        Validate.notNull(file, "file cannot be null");
        final FileInputStream stream = new FileInputStream(file);
        load(new InputStreamReader(stream, Charsets.UTF_8));
    }

    @Override
    public void load(Reader reader) throws IOException, InvalidConfigurationException
    {
        BufferedReader input = (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
        StringBuilder builder = new StringBuilder();
        try {
            String line;
            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n'); }
        } finally { input.close(); }
        loadFromString(builder.toString());
    }

    public void reload() throws IOException {
        backup();
        init(file);
    }

    public void backup() throws IOException {
        if(GrewEssentials.getInstance().Config.getBoolean("AutoBackUp",true)){
            check(file);
            String filename = file.getName(),time = StringUtils.getNanon();
            ConfigFile oldcfg = new ConfigFile(file);
            oldcfg.save(new File(file.getParent(), "Backup"+File.separator +"config"+File.separator+"backup."+filename+"."+time));
        }
    }

    public void save()
    {
        if (file == null)
        {
            //loger.info("未定义配置文件路径 保存失败!");
        }
        try
        {
            this.save(file);
        }
        catch (IOException e)
        {
           // loger.info("配置文件 " + file.getName() + " 保存错误...");
            e.printStackTrace();
        }
    }

    @Override
    public void save(File file) throws IOException
    {
        Validate.notNull(file, "file cannot be null");
        Files.createParentDirs(file);
        String data = saveToString();
        Writer writer = new OutputStreamWriter(new FileOutputStream(file),
                Charsets.UTF_8);
        try { writer.write(data); }
        finally { writer.close(); }
    }

    @Override
    public String saveToString()
    {
        yamlOptions.setIndent(options().indent());
        yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        String header = buildHeader();
        String dump = yaml.dump(getValues(false));
        if (dump.equals(BLANK_CONFIG)) { dump = ""; }
        return header + dump;
    }
}
