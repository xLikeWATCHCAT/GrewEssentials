package net.dev.JavaScript;

import jdk.nashorn.api.scripting.*;

import javax.script.*;
import java.util.*;

public class Scripts {
    private static ScriptEngine scriptEngine;
    private static ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

    static void init() {
        try {
            NashornScriptEngineFactory factory = (NashornScriptEngineFactory) scriptEngineManager.getEngineFactories().stream().filter(factories -> "Oracle Nashorn".equalsIgnoreCase(factories.getEngineName())).findFirst().orElse(null);
            scriptEngine = Objects.requireNonNull(factory).getScriptEngine("-doe", "--global-per-engine");
        } catch (Exception ignored) {
            scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
        }
    }

    public static CompiledScript compile(String script) {
        try {
            return ((Compilable) scriptEngine).compile(script);
        } catch (Exception e) {
            return null;
        }
    }

    // *********************************
    //
    //        Getter and Setter
    //
    // *********************************

    public static ScriptEngine getScriptEngine() {
        return scriptEngine;
    }

    public static ScriptEngineManager getScriptEngineManager() {
        return scriptEngineManager;
    }
}
