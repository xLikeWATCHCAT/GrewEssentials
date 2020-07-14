package net.dev.database;

import net.dev.*;
import net.dev.utils.string.*;
import org.bukkit.configuration.*;

import java.sql.*;
import java.util.*;

import static net.dev.utils.Utils.*;

public class DataBase {
    private final DataBaseCore dataBaseCore;
    public DataBase(DataBaseCore core)
    {
        dataBaseCore = core;
    }
    public static DataBase create(ConfigurationSection dbConfig)
    {
        Type type = Type.valueOf(dbConfig.getString("type", "SQLITE").toUpperCase());
        switch (type)
        {
            case MYSQL:
                return new DataBase(new MySQLCore(dbConfig));
            default:
                return new DataBase(new SQLiteCore(GrewEssentials.getInstance(), dbConfig));
        }
    }
    public boolean close()
    {
        try
        {
            dataBaseCore.getConnection().close();
            return true;
        }
        catch (SQLException e)
        {
        }
        return false;
    }
    public boolean copyTo(DataBaseCore db)
    {
        try
        {
            String src = dataBaseCore.getConnection().getMetaData().getURL();
            String des = db.getConnection().getMetaData().getURL();
            ResultSet rs = dataBaseCore.getConnection().getMetaData().getTables(null,
                    null, "%", null);
            List<String> tables = new LinkedList<String>();
            while (rs.next())
            {
                tables.add(rs.getString("TABLE_NAME"));
            }
            rs.close();
            int s = 0;
            for (String table : tables)
            {
                sendConsole(GrewEssentials.getInstance().Message.getString("Database.CopyToOtherDatabase").replace("$table",table).replace("$pluginname", StringUtils.PluginName));
                if (!table.toLowerCase().startsWith("sqlite_autoindex_"))
                {
                    db.execute("DELETE FROM " + table);
                    rs = dataBaseCore.executeQuery("SELECT * FROM " + table);
                    int n = 0;
                    String query = "INSERT INTO " + table + " VALUES (";
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
                    {
                        query = query + "?, ";
                    }
                    query = query.substring(0, query.length() - 2) + ")";
                    PreparedStatement ps = db.getConnection().prepareStatement(query);
                    long time = System.currentTimeMillis();
                    while (rs.next())
                    {
                        n++;
                        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
                        {
                            ps.setObject(i, rs.getObject(i));
                        }
                        ps.addBatch();
                        if (n % 100 == 0)
                        {
                            ps.executeBatch();
                        }
                        if (System.currentTimeMillis() - time > 500L)
                        {
                            time = System.currentTimeMillis();
                        }
                    }
                    s += n;
                    ps.executeBatch();
                    rs.close();
                }
            }
            sendConsole(GrewEssentials.getInstance().Message.getString("Database.CopyToOtherDatabase").replace("$table",src).replace("$order",String.valueOf(s)).replace("$des",des).replace("$pluginname",StringUtils.PluginName));
            db.getConnection().close();
            dataBaseCore.getConnection().close();
            return true;
        }
        catch (SQLException e)
        {
        }
        return false;
    }
    public boolean createTables(String tableName, KeyValue fields, String Conditions)
    {
        try
        {
            dataBaseCore.createTables(tableName, fields, Conditions);
            return isTableExists(tableName);
        }
        catch (Exception e)
        {
            sqlerr(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Database.CreateDatabaseTableError").replace("$table",tableName).replace("$pluginname",StringUtils.PluginName)),e);
        }
        return false;
    }
    public int dbDelete(String tableName, KeyValue fields)
    {
        String sql;
        if(fields == null){
            sql = "DELETE FROM `" + tableName+"`";
        }else{
            sql = "DELETE FROM `" + tableName + "` WHERE " + fields.toWhereString();
        }
        try
        {
            return dataBaseCore.executeUpdate(sql);
        }
        catch (Exception e)
        {
            sqlerr(sql, e);
        }
        return 0;
    }
    public int dbDelete(String tableName)
    {
        String sql = "DELETE FROM `" + tableName+"`";
        try
        {
            return dataBaseCore.executeUpdate(sql);
        }
        catch (Exception e)
        {
            sqlerr(sql, e);
        }
        return 0;
    }
    public boolean dbExist(String tableName, KeyValue fields)
    {
        String sql = "SELECT * FROM " + tableName + " WHERE " + fields.toWhereString();
        try
        {
            return dataBaseCore.executeQuery(sql).next();
        }
        catch (Exception e)
        {
            sqlerr(sql, e);
        }
        return false;
    }
    public int dbInsert(String tabName, KeyValue fields)
    {
        String sql = "INSERT INTO `" + tabName + "` " + fields.toInsertString();
        try
        {
            return dataBaseCore.executeUpdate(sql);
        }
        catch (Exception e)
        {
            sqlerr(sql, e);
        }
        return 0;
    }
    public List<KeyValue> dbSelect(String tableName, KeyValue fields, KeyValue selCondition)
    {
        String sql = "SELECT " + fields.toKeys() + " FROM `" + tableName + "`" + (selCondition == null ? "" : new StringBuilder().append(" WHERE ").append(selCondition.toWhereString()).toString());
        List<KeyValue> kvlist = new ArrayList<KeyValue>();
        try
        {
            ResultSet dbresult = dataBaseCore.executeQuery(sql);
            while (dbresult.next())
            {
                KeyValue kv = new KeyValue();
                for (String col : fields.getKeys())
                {
                    kv.add(col, dbresult.getString(col));
                }
                kvlist.add(kv);
            }
        }
        catch (Exception e)
        {
            sqlerr(sql, e);
        }
        return kvlist;
    }
    public String dbSelectFirst(String tableName, String fields, KeyValue selConditions)
    {
        String sql = "SELECT " + fields + " FROM " + tableName + " WHERE " + selConditions.toWhereString() + " LIMIT 1";
        try
        {
            ResultSet dbresult = dataBaseCore.executeQuery(sql);
            if (dbresult.next())
            {
                return dbresult.getString(fields);
            }
        }
        catch (Exception e)
        {
            sqlerr(sql, e);
        }
        return null;
    }
    public int dbUpdate(String tabName, KeyValue fields, KeyValue upCondition)
    {
        String sql = "UPDATE `" + tabName + "` SET " + fields.toUpdateString() + " WHERE " + upCondition.toWhereString();
        try
        {
            return dataBaseCore.executeUpdate(sql);
        }
        catch (Exception e)
        {
            sqlerr(sql, e);
        }
        return 0;
    }
    public DataBaseCore getDataBaseCore()
    {
        return dataBaseCore;
    }
    public boolean isValueExists(String tableName, KeyValue fields, KeyValue selCondition)
    {
        String sql = "SELECT " + fields.toKeys() + " FROM `" + tableName + "`" + (selCondition == null ? "" : new StringBuilder().append(" WHERE ").append(selCondition.toWhereString()).toString());
        try
        {
            ResultSet dbresult = dataBaseCore.executeQuery(sql);
            return dbresult.next();
        }
        catch (Exception e)
        {
            sqlerr(sql, e);
        }
        return false;
    }
    public boolean isFieldExists(String tableName, KeyValue fields)
    {
        try
        {
            DatabaseMetaData dbm = dataBaseCore.getConnection().getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName, null);
            if (tables.next())
            {
                ResultSet f = dbm.getColumns(null, null, tableName, fields.getKeys()[0]);
                return f.next();
            }
        }
        catch (SQLException e)
        {
            sqlerr(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Database.CheckTableNameAndKeyError").replace("$pluginname",StringUtils.PluginName).replace("$table",tableName).replace("$key",fields.getKeys()[0])), e);
        }
        return false;
    }

    public boolean isTableExists(String tableName)
    {
        try
        {
            DatabaseMetaData dbm = dataBaseCore.getConnection().getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName, null);
            return tables.next();
        }
        catch (SQLException e)
        {
            sqlerr(StringUtils.translateColorCodes(GrewEssentials.getInstance().Message.getString("Database.CheckTableNameError").replace("$pluginname",StringUtils.PluginName).replace("$table",tableName)), e);
        }
        return false;
    }

    public void sqlerr(String sql, Exception e)
    {
        sendConsole(GrewEssentials.getInstance().Message.getString("Database.DatabaseError").replace("$pluginname",StringUtils.PluginName)+e.getMessage());
        sendConsole(GrewEssentials.getInstance().Message.getString("Database.SQLCheck").replace("$pluginname",StringUtils.PluginName)+sql);
    }

    enum Type
    {
        MYSQL, SQLITE
    }
}
