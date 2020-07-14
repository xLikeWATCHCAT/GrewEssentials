package net.dev.database;

import java.sql.*;

public abstract class DataBaseCore
{
    public abstract boolean createTables(String tableName, KeyValue fields, String conditions) throws SQLException;
    
    public boolean execute(String sql) throws SQLException { return getStatement().execute(sql); }
    
    public ResultSet executeQuery(String sql) throws SQLException { return getStatement().executeQuery(sql); }
    
    public int executeUpdate(String sql) throws SQLException { return getStatement().executeUpdate(sql); }
    
    public abstract Connection getConnection();
    
    private Statement getStatement() throws SQLException { return getConnection().createStatement(); }
}
