package org.cyberagent;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.File;
import java.lang.instrument.Instrumentation;

public class ArdentAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        printLogo();

        Config config = loadConfig();
        if (config == null) {
            System.err.println("[!] config.json not loaded properly! Launching the game usual way");
            return;
        }

        System.out.println("Cheats are on!");
        System.out.println("Found the server: " + config.serverVersion);

        //syncing json file with editor
        inst.addTransformer(new BytecodeEditor(config));
    }

    private static Config loadConfig() {
        try {
            File configFile = new File("config.json");
            if (!configFile.exists()) {
                System.out.println("[!] config.json missing in the main folder!");
                return null;
            }
            Gson gson = new Gson();
            return gson.fromJson(new FileReader(configFile), Config.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void printLogo() {
        System.out.println("\n    _    ____  ____  _____ _   _ _____ ");
        System.out.println("   / \\  |  _ \\|  _ \\| ____| \\ | |_   _|");
        System.out.println("  / _ \\ | |_) | | | |  _| |  \\| | | |  ");
        System.out.println(" / ___ \\|  _ <| |_| | |___| |\\  | | |  ");
        System.out.println("/_/   \\_\\_| \\_\\____/|_____|_| \\_| |_|  ");
        System.out.println("   [ Project Edge Dynamic Modder ]\n");
    }
}