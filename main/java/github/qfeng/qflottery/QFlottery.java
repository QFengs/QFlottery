package github.qfeng.qflottery;

import github.qfeng.qflottery.Listener.InventoryListener;
import github.qfeng.qflottery.util.Config;
import github.qfeng.qflottery.util.LotteryCommand;
import github.qfeng.qflottery.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class QFlottery extends JavaPlugin implements CommandExecutor {
    private static JavaPlugin plugin = null;

    public static QFlottery getInstance() {
        return instance;
    }

    private static QFlottery instance;
    private Logger logger;

    @Override
    public void onEnable() {
        Long oldTimes = System.currentTimeMillis();
        logger = getLogger();
        plugin = this;
        instance = this;
        Message.loadMessage();
        Config.registerConfig();
        // 使用Logger记录日志
        logger.info("QFlottery 插件已启用");
        // 注册命令
        getCommand("qflottery").setExecutor(new LotteryCommand());
        getServer().getPluginManager().registerEvents(new InventoryListener(),this);
        if (Bukkit.getPluginManager().isPluginEnabled("NeigeItems")) {
            Bukkit.getConsoleSender().sendMessage("[" + this.getName() + "]Find NeigeItems");
        } else if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            Bukkit.getConsoleSender().sendMessage("[" + this.getName() + "]Find MythicMobs!");
        } else if (Bukkit.getPluginManager().isPluginEnabled("SX-Attribute")) {
            Bukkit.getConsoleSender().sendMessage("[" + this.getName() + "]Find SX-Attribute!");
        } else {
            Bukkit.getConsoleSender().sendMessage("[" + this.getName() + "]Please load NeigeItems or MythicMobs or SX-Attribute!");
            this.setEnabled(false);
        }
        Bukkit.getConsoleSender().sendMessage("[" + this.getName() + "] §a加载用时:" + (System.currentTimeMillis() - oldTimes) + "毫秒");
        Bukkit.getConsoleSender().sendMessage("[" + this.getName() + "] §a加载成功! ");
        Config.loadConfig();
        // 其他初始化代码...
    }
    @Override
    public void onDisable() {
        // 使用Logger记录日志
        logger.info("QFlottery 插件卸载");
        logger = null;
        // 注册命令
        // 其他初始化代码...
    }
    public static JavaPlugin getPlugin() {
        return plugin;
    }


}