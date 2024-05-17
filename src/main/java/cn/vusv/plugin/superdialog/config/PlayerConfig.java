package cn.vusv.plugin.superdialog.config;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.vusv.plugin.superdialog.SuperDialogPlugin;

import java.io.File;
import java.util.LinkedHashMap;

public class PlayerConfig {
    public static LinkedHashMap<String, Config> PlayersMap;

    public static void init() {
        PlayersMap = new LinkedHashMap<>();
        File[] files = new File(SuperDialogPlugin.getInstance().getDataFolder() + File.separator + "players").listFiles();

        for (File file : files) {
            if (!file.isFile()) continue;
            String fileName = file.getName().replace(".yml", "");
            PlayersMap.put(fileName, new Config(file, Config.YAML));
        }
    }

    public static Config create(String playerName) {
        Config config = PlayersMap.getOrDefault(playerName, new Config(
                new File(SuperDialogPlugin.getInstance().getDataFolder() + File.separator + "players", playerName + ".yml"),
                Config.YAML,
                //Default values (not necessary)
                new ConfigSection()
        ));
        if (!PlayersMap.containsKey(playerName)) PlayersMap.put(playerName, config);
        return PlayersMap.get(playerName);
    }

    public static void reload() {
        for (String key : PlayersMap.keySet()) {
            PlayersMap.get(key).reload();
        }
    }

    public static void save() {
        for (String key : PlayersMap.keySet()) {
            PlayersMap.get(key).save();
        }
    }
}
