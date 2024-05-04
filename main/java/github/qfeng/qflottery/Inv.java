package github.qfeng.qflottery;

import github.qfeng.qflottery.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.beans.PersistenceDelegate;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Inv implements InventoryHolder {
    YamlConfiguration config = new YamlConfiguration();
    Inventory gui;

    public String getLottery() {
        return lottery;
    }

    public String lottery;
    public ItemStack item;

    public Inv() {
        File configfile = new File(Config.getDataFile(), "config.yml");
        try {
            config.load(configfile);
        } catch (IOException | InvalidConfigurationException var4) {
            var4.printStackTrace();
        }
        ConfigurationSection itemsfirst = config.getConfigurationSection("Items");
        ConfigurationSection items = itemsfirst.getConfigurationSection("DangBan");
        Material itemmaterial = Material.NETHER_STAR;
        ItemStack item = new ItemStack(Material.STAINED_GLASS);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Collections.singletonList("§7配置错误了"));
        meta.setDisplayName("§d§l无限抽奖");
        if(items != null){
            itemmaterial = Material.getMaterial(items.getString("material")) ;
            meta.setLore(items.getStringList("lore"));
            meta.setDisplayName(items.getString("name"));
        }
        if(itemmaterial==null){
            itemmaterial = Material.STAINED_GLASS_PANE;
        }
        item = new ItemStack(itemmaterial);
        item.setItemMeta(meta);
        this.item = item;
    }
    public void load(){

    }
    public Inventory getGui(String title,String lotteryName) {
        this.lottery =lotteryName;
        gui = Bukkit.createInventory(this,6*9,title);
        //接下来环绕一圈
        int sideJ = 6;
        for (int i = 0; i < 9; i++) {
            gui.setItem(i,item);

        }
        for (int i = 0; i < sideJ; i++) {
            gui.setItem(i*9,item);
        }
        for (int i = 0; i < sideJ; i++) {
            gui.setItem(i*9+8,item);
        }
        for (int i = 0; i < 9; i++) {
            gui.setItem((sideJ-1)*9+i,item);
        }
        return gui;
    }
    @Override
    public Inventory getInventory() {
        if (gui == null ){
            return Bukkit.createInventory(this,9,"配置错误");
        }
        return gui;
    }
}
