package github.qfeng.qflottery.util;


import github.qfeng.qflottery.QFlottery;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {
    public static final String DATA_FILE = "DataFile";
    static final File configFile;

    public static List<String> getI() {
        return i;
    }

    private static List<String> i = new ArrayList<>();
    private static YamlConfiguration config;
    private static File dataFile;
    private static final JavaPlugin plugin = QFlottery.getPlugin();

    public Config() {

    }

    public static void createConfig() {
        Bukkit.getConsoleSender().sendMessage("[" + QFlottery.getPlugin().getName() + "] §cCreate Config.yml");

        try {
            config.save(configFile);
        } catch (IOException var1) {
            var1.printStackTrace();
        }
        i = config.getStringList("ItemPacks");
    }

    public static void loadConfig() {
        if (!configFile.exists()) {
            createConfig();
        } else {
            Bukkit.getConsoleSender().sendMessage(Message.getMsg("Plugin.Name")+" §aFind config.yml");
            config = new YamlConfiguration();

            try {
                config.load(configFile);
            } catch (InvalidConfigurationException | IOException var1) {
                var1.printStackTrace();
                Bukkit.getConsoleSender().sendMessage("[" + QFlottery.getPlugin().getName() + "] §c读取config时发生错误");
            }
            i = config.getStringList("ItemPacks");
        }
    }
    public static YamlConfiguration getConfig() {
        return config;
    }
    public static void registerConfig(){
        plugin.saveDefaultConfig();
    }
    public static File getDataFile() {
        return dataFile;
    }

    static {
        configFile = new File("plugins" + File.separator + QFlottery.getPlugin().getName() + File.separator + "config.yml");
        dataFile = QFlottery.getPlugin().getDataFolder();
    }
}
