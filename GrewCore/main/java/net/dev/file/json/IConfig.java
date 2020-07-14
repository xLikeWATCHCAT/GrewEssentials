package net.dev.file.json;

import java.io.*;

public interface IConfig<T> {
    public void autoCreateNewFile() throws IOException;
    public void loadConfig() throws IOException;
    public void saveConfig() throws IOException;
    public T getConfig();
}
