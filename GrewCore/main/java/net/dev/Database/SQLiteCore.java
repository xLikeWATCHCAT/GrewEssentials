package net.dev.Database;


import net.dev.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.configuration.*;
import org.bukkit.plugin.*;

import java.io.*;
import java.sql.*;
import java.util.logging.*;

import static net.dev.Utils.Utils.*;

public class SQLiteCore extends DataBaseCore
{
    private static String driverName = "org.sqlite.JDBC";
    private static Logger logger = GrewEssentials.getInstance().getLogger();
    private Connection connection;
    private File dbFile;

    public SQLiteCore(File dbFile)
    {
        this.dbFile = dbFile;
        if (this.dbFile.exists())
        {
            try
            {
                this.dbFile.createNewFile();
            }
            catch (IOException e)
            {
                sendConsole(GrewEssentials.getInstance().Message.getString("DataBase.DatabaseCreateFailed").replace("$table",dbFile.getAbsolutePath()).replace("$pluginname", StringUtils.PluginName));
            }
        }
        try
        {
            Class.forName(driverName).newInstance();
        }
        catch (Exception e)
        {
            sendConsole(GrewEssentials.getInstance().Message.getString("DataBase.DatabaseLoadError").replace("$driver",driverName));
        }
    }

    public SQLiteCore(Plugin plugin, ConfigurationSection cfg)
    {
        this(plugin, cfg.getString("database"));
    }

    public SQLiteCore(Plugin plugin, String filename)
    {
        dbFile = new File(plugin.getDataFolder(), filename + ".db");
        if (dbFile.exists())
        {
            try
            {
                dbFile.createNewFile();
            }
            catch (IOException e)
            {
                sendConsole(GrewEssentials.getInstance().Message.getString("DataBase.DatabaseCreateFailed").replace("$pluginname",StringUtils.PluginName).replace("$table",dbFile.getAbsolutePath()));
            }
        }
        try
        {
            Class.forName(driverName).newInstance();
        }
        catch (Exception e)
        {
            sendConsole(GrewEssentials.getInstance().Message.getString("DataBase.DatabaseLoadError").replace("$pluginname",StringUtils.PluginName).replace("$driver",driverName));
        }
    }

    public SQLiteCore(String filepath)
    {
        this(new File(filepath));
    }

    @Override
    public boolean createTables(String tableName, KeyValue fields, String Conditions)
            throws SQLException
    {
        String sql = "CREATE TABLE IF NOT EXISTS `%s` ( %s )";
        return execute(String.format(sql, tableName,
                fields.toCreateString().replace("AUTO_INCREMENT", "AUTOINCREMENT")));
    }

    public String getAUTO_INCREMENT()
    {
        return "AUTOINCREMENT";
    }

    @Override public Connection getConnection()
    {
        try
        {
            if ((connection != null) && (!connection.isClosed()))
            {
                return connection;
            }
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
            return connection;
        }
        catch (SQLException e)
        {
            sendConsole(GrewEssentials.getInstance().Message.getString("DataBase.DatabaseError")+e.getMessage());
            sendConsole(GrewEssentials.getInstance().Message.getString("DataBase.DatabaseFile").replace("$pluginname",StringUtils.PluginName).replace("$file",dbFile.getAbsolutePath()));
        }
        return null;
    }
}
