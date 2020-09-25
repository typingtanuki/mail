package com.github.typingtanuki.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Edict {
    private static final Object INIT_LOCK = new Object[0];

    private static boolean initialized = false;
    private static boolean loaded = false;
    private static final Set<String> keys = new HashSet<>();
    private static final Map<String, String> meanings = new HashMap<>();
    private static final Map<String, String> pronunciations = new HashMap<>();

    private Edict() {
        super();
    }

    public static void init() {
        synchronized (INIT_LOCK) {
            if (initialized) {
                return;
            }

            initialized = true;

            InputStream stream = Edict.class.getResourceAsStream("/edict");
            if (stream == null) {
                return;
            }

            loadDictionary(stream);
            loaded = true;
        }
    }

    private static void loadDictionary(InputStream stream) {
        try (InputStreamReader streamReader =
                     new InputStreamReader(stream, Charset.forName("EUC-JP"));
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                loadLine(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadLine(String line) {
        String[] splits = line.split(" ", 2);
        String key = splits[0];
        String rest = splits[1];
        loadKey(key);

        loadPronunciation(key, clearPronunciation(rest));
        loadMeaning(key, clearDefinition(rest));
    }

    private static String clearDefinition(String rest) {
        String[] definitions = rest.split("/", 3);
        String def = definitions[1];
        return def.replaceAll("\\(.*\\)", "").strip();
    }

    private static String clearPronunciation(String rest) {
        String[] pronunciation = rest.split("]", 2);
        if (pronunciation.length < 2) {
            return "";
        }
        String pron = pronunciation[0];
        return pron.replaceAll("\\[", "").strip();
    }

    private static void loadPronunciation(String key, String pronunciation) {
        if (pronunciation.isBlank()) {
            return;
        }
        pronunciations.put(key, pronunciation);
    }

    private static void loadMeaning(String key, String meaning) {
        if (meaning.isBlank()) {
            return;
        }
        meanings.put(key, meaning);
    }

    private static void loadKey(String key) {
        keys.add(key);
    }

    public static boolean isInDict(String chars) {
        if (!loaded) {
            return true;
        }
        return keys.contains(chars);
    }

    public static String meaning(String key) {
        return meanings.get(key);
    }

    public static String pronunciation(String key) {
        return pronunciations.get(key);
    }
}
