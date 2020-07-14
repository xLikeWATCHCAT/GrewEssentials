package net.dev.database;

import java.util.*;

public class KeyValue
{
    private Map<Object, Object> keyvalues = new HashMap<Object, Object>();
    
    public KeyValue()
    {
    }
    
    public KeyValue(String key, Object value)
    {
        add(key, value);
    }
    
    public KeyValue add(String key, Object value)
    {
        keyvalues.put(key, value);
        return this;
    }
    
    public String[] getKeys()
    {
        return keyvalues.keySet().toArray(new String[0]);
    }
    
    public String getString(String key)
    {
        Object obj = keyvalues.get(key);
        return obj == null ? "" : obj.toString();
    }
    
    public Object[] getValues()
    {
        List<Object> keys = new ArrayList<Object>();
        for (Map.Entry<Object, Object> next : keyvalues.entrySet())
        {
            keys.add(next.getValue());
        }
        return keys.toArray(new Object[0]);
    }
    
    public boolean isEmpty()
    {
        return keyvalues.isEmpty();
    }
    
    public String toCreateString()
    {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Object, Object> next : keyvalues.entrySet())
        {
            sb.append("`");
            sb.append(next.getKey());
            sb.append("` ");
            sb.append(next.getValue());
            sb.append(", ");
        }
        return sb.toString().substring(0, sb.length() - 2);
    }
    
    public String toInsertString()
    {
        String ks = "";
        String vs = "";
        for (Map.Entry<Object, Object> next : keyvalues.entrySet())
        {
            ks = ks + "`" + next.getKey() + "`, ";
            vs = vs + "'" + next.getValue() + "', ";
        }
        return "(" + ks.substring(0, ks.length() - 2) + ") VALUES ("
                + vs.substring(0, vs.length() - 2) + ")";
    }
    
    public String toKeys()
    {
        StringBuilder sb = new StringBuilder();
        for (Object next : keyvalues.keySet())
        {
            sb.append("`");
            sb.append(next);
            sb.append("`, ");
        }
        return sb.toString().substring(0, sb.length() - 2);
    }
    
    @Override
    public String toString()
    {
        return keyvalues.toString();
    }
    
    public String toUpdateString()
    {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Object, Object> next : keyvalues.entrySet())
        {
            sb.append("`");
            sb.append(next.getKey());
            sb.append("`='");
            sb.append(next.getValue());
            sb.append("' ,");
        }
        return sb.substring(0, sb.length() - 2);
    }
    
    public String toWhereString()
    {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Object, Object> next : keyvalues.entrySet())
        {
            sb.append("`");
            sb.append(next.getKey());
            sb.append("`='");
            sb.append(next.getValue());
            sb.append("' and ");
        }
        return sb.substring(0, sb.length() - 5);
    }
}
