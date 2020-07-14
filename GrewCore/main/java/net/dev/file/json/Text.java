package net.dev.file.json;

import java.util.*;

public class Text implements Comparable<Object>{
    private volatile String text;
    public Text(){
        this.text="";
    }
    public Text(String t)
    {
        if(t==null)
            t="";
        this.text=t.toLowerCase(Locale.ENGLISH);
    }
    public void setText(String t)
    {
        if(t==null)
            t="";
        this.text=t.toLowerCase(Locale.ENGLISH);
    }
    public String getText()
    {
        if(this.text==null)
            this.text="";
        return this.text.toLowerCase(Locale.ENGLISH);
    }
    @Override
    public String toString()
    {
        return this.getText();
    }
    @Override
    public boolean equals(Object o)
    {
        if(o instanceof Text)
            return ((Text) o).getText().equalsIgnoreCase(this.getText());
        else if(o instanceof String)
            return ((String) o).equalsIgnoreCase(this.getText());
        else if(o instanceof StringBuilder)
            return o.toString().equalsIgnoreCase(this.getText());
        else if(o==null)  return false;
        else return o.toString().equalsIgnoreCase(this.getText());
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Text)
            return ((Text) o).getText().compareToIgnoreCase(this.getText());
        else if(o instanceof String)
            return ((String) o).compareToIgnoreCase(this.getText());
        else if(o instanceof StringBuilder)
            return o.toString().compareToIgnoreCase(this.getText());
        else if(o==null)  return 0;
        else return o.toString().compareToIgnoreCase(this.getText());
    }
}
