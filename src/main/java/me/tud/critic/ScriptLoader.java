package me.tud.critic;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public final class ScriptLoader {

    private static final String SUFFIX = ".cr";

    private static final HashMap<String, Script> loadedScripts = new HashMap<>();

    private ScriptLoader() {
        throw new UnsupportedOperationException();
    }

    public static Script loadScript(String fileName) throws IOException {
        if (!fileName.endsWith(SUFFIX))
            fileName = fileName + SUFFIX;

        if (loadedScripts.containsKey(fileName))
            return null;

        try (InputStream inputStream = Main.CLASS_LOADER.getResourceAsStream(fileName)) {
            if (inputStream == null)
                return null;
            Script script = new Script(fileName.substring(fileName.length() - SUFFIX.length()), inputStream);
            if (script.init()) {
                loadedScripts.put(fileName, script);
                return script;
            }
            return null;
        }
    }

}
