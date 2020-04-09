package net.dev.Database;

import net.dev.*;
import net.dev.Utils.StringUtils.*;
import org.bukkit.configuration.*;

import java.sql.*;

import static net.dev.Utils.Utils.*;

public class MySQLCore extends DataBaseCore
{
    private static String driverName = "com.mysql.jdbc.Driver";
    private String username;
    private String password;
    private Connection connection;
    private String url;
    
    public MySQLCore(ConfigurationSection cfg)
    {
        this(cfg.getString("ip"), cfg.getInt("port"), cfg.getString("database"),
                cfg.getString("username"), cfg.getString("password"));
    }
    
    public MySQLCore(String host, int port, String dbname, String username,
            String password)
    {
        url = ("jdbc:mysql://" + host + ":" + port + "/" + dbname);
        this.username = username;
        this.password = password;
        try
        {
            Class.forName(driverName).newInstance();
        }
        catch (Exception e)
        {
            sendConsole(GrewEssentials.getInstance().Message.getString("DataBase.DatabaseLoadError").replace("$driver",driverName).replace("$pluginname", StringUtils.PluginName));
        }
    }
    
    @Override
    public boolean createTables(String tableName, KeyValue fields, String conditions)
            throws SQLException
    {
        String sql = "CREATE TABLE IF NOT EXISTS `" + tableName + "` ( "
                + fields.toCreateString()
                + (conditions == null ? ""
                        : new StringBuilder(" , ").append(conditions).toString())
                + " ) ENGINE = MyISAM DEFAULT CHARSET=UTF8;";
        return execute(sql);
    }
    
    @Override
    public Connection getConnection()
    {
        try
        {
            if ((connection != null) && (!connection.isClosed()))
            {
                return connection;
            }
            connection = DriverManager.getConnection(url, username, password);
            return connection;
        }
        catch (SQLException e)
        {
            sendConsole(GrewEssentials.getInstance().Message.getString("DataBase.DatabaseLoginError").replace("$pluginname",StringUtils.PluginName).replace("$reason",e.getMessage()).replace("$url",url).replace("$username",username).replace("$password",password));
        }
        return null;
    }
}
