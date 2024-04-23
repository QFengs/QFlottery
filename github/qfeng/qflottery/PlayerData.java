package github.qfeng.qflottery;

import github.qfeng.qflottery.Item.ItemManager;
import github.qfeng.qflottery.util.Config;
import github.qfeng.qflottery.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerData implements Listener {
    private String playerName;
    private Player player;
    private Map<String, List<String>> prizeName = new HashMap<>();

    public List<String> getLotteryName() {
        return lotteryName;
    }

    private List<String> lotteryName = new ArrayList<>();

    public PlayerData(String playerName) {
        this.playerName = playerName;
        this.player = Bukkit.getPlayer(playerName);
        YamlConfiguration yaml = new YamlConfiguration();
        File file = new File(Config.getDataFile(), "PlayerData" + File.separator + this.playerName + ".yml");
        if (!file.exists() || file.isDirectory()) {
            this.save();
            return;
        }
        try {
            yaml.load(file);
        } catch (InvalidConfigurationException | IOException var7) {
            var7.printStackTrace();
        }
        ConfigurationSection player = yaml.getConfigurationSection(playerName);
        if(player != null){
            Set<String> keys = player.getKeys(false);
            for (String key : keys) {
                this.lotteryName.add(key);
                this.prizeName.put(key,player.getStringList(key));
            }
        }
    }

    public void load(){
        YamlConfiguration yaml = new YamlConfiguration();
        File file = new File(Config.getDataFile(), "PlayerData" + File.separator + this.playerName + ".yml");
        if (!file.exists() || file.isDirectory()) {
            this.save();
            return;
        }
        try {
            yaml.load(file);
        } catch (InvalidConfigurationException | IOException var7) {
            var7.printStackTrace();
        }
        ConfigurationSection player = yaml.getConfigurationSection(playerName);
        if(player != null){
            Set<String> keys = player.getKeys(false);
            for (String key : keys) {
                this.lotteryName.add(key);
                this.prizeName.put(key,player.getStringList(key));
            }
        }
    }
    public void save() {
        YamlConfiguration yaml = new YamlConfiguration();
        if (!prizeName.isEmpty()){
            for (String s : lotteryName) {
                yaml.set(playerName+"."+s,prizeName.get(s));
            }
        }
        File file = new File(Config.getDataFile(), "PlayerData" + File.separator + this.playerName + ".yml");
        try {
            yaml.save(file);
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }
    public void addPrize(String lotteryName, String item) {
        if(!this.lotteryName.contains(lotteryName)){
            this.lotteryName.add(lotteryName);
        }
        if (!prizeName.containsKey(lotteryName)) {
            prizeName.put(lotteryName, new ArrayList<>());
        }
        prizeName.get(lotteryName).add(item);
        // 确保最近十个奖品的缓存
        if (prizeName.get(lotteryName).size() > 10) {
            prizeName.get(lotteryName).remove(0);
        }
        this.save();
    }
    public boolean givePrize(String lottery,String item){
        Stats stats = PlayerStatsManager.getPlayerStats(playerName);
        if (!this.checkKeyItem(lottery)){
            player.sendMessage(Message.getMsg("Plugin.Name")+Message.getMsg("Message.Player.NoKey",playerName,lottery));
            return false;
        }
        if (!stats.CheckForStats(lottery)){
            return false;
        }
        this.removeKeyItem(lottery);
        ItemStack itemStacks = ItemManager.getItem(item);
        //这里获取物品库数据
        player.getInventory().addItem(itemStacks);
        removePrize(lottery);
        return true;
    }
    public void giveAllPrize(String lottery){
        Stats stats = PlayerStatsManager.getPlayerStats(playerName);
        if (!stats.JustCheckForStats(lottery)||getRecentPrizes(lottery)==null){
            player.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.YELLOW+"玩家"+playerName+ChatColor.RED+"没有奖品可领取!");
            return;
        }
        List<String> items = getRecentPrizes(lottery);
        int i =0;
        for (String item : items) {
            if(!this.checkKeyItem(lottery)){
                player.sendMessage(Message.getMsg("Plugin.Name")+Message.getMsg("Message.Player.NoEnoughKey",playerName,lottery));
                if (i!=0){
                    player.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.GREEN+"已给予"+ChatColor.YELLOW+"玩家"+playerName+ChatColor.GREEN+i+"个物品!");
                }
                return;
            }
            if(stats.CheckForStats(lottery)){
                this.removeKeyItem(lottery);
                ItemStack itemStacks = ItemManager.getItem(item);
                player.getInventory().addItem(itemStacks);
                removePrize(lottery);
                i++;
            }else {
                break;
            }
        }
        player.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.GREEN+"已给予"+ChatColor.YELLOW+"玩家"+playerName+ChatColor.GREEN+i+"个物品!");
    }
    public void removePrize(String lotteryName){
        prizeName.get(lotteryName).remove(0);
        this.save();
    }

    public List<String> getRecentPrizes(String lotteryName, int numberOfPrizes) {
        if (prizeName.isEmpty()) {
            return new ArrayList<>();
        }
        if (prizeName.containsKey(lotteryName)) {
            return new ArrayList<>(prizeName.get(lotteryName).subList(0, Math.min(numberOfPrizes, prizeName.get(lotteryName).size())));
        }
        return new ArrayList<>();
    }
    public List<String> getRecentPrizes(String lotteryName) {
        if (prizeName.isEmpty()) {
            return new ArrayList<>();
        }
        if (prizeName.containsKey(lotteryName)) {
            return new ArrayList<>(prizeName.get(lotteryName));
        }
        return new ArrayList<>();
    }
    public void getShowPrizeGui(String lottery,String pl){
        load();
        Player sender = Bukkit.getPlayer(pl);
        Stats playerstats = new Stats(playerName);
        if(getRecentPrizes(lottery).isEmpty()){
            sender.sendMessage(Message.getMsg("Plugin.Name")+ ChatColor.RED + "没有找到该玩家的抽奖记录。");
            return;
        }
        if(!playerstats.JustCheckForStats(lottery)){
            sender.sendMessage(Message.getMsg("Plugin.Name")+ ChatColor.RED + "该玩家宝箱领取次数已达上限");
            return;
        }
        Inv inv = new Inv();
        Inventory gui = inv.getGui(Message.getMsg("Title.Player.Prize",playerName,lottery), lottery);
        List<String> prize = getRecentPrizes(lottery);
        List<ItemStack> prizeItem = new ArrayList<>();
        for (String s : prize) {
            prizeItem.add(ItemManager.getItem(s));
        }
        int i = 0;
        for (ItemStack stack : prizeItem) {
            for (; i < 5*9; i++) {
                if(gui.getItem(i)==null){
                    gui.setItem(i,stack);
                    break;
                }
            }
        }

        gui.setItem(49,ItemManager.getCheckGiveItem(lottery));
        sender.openInventory(gui);
    }

    //check KEYITEM
    public boolean checkKeyItem(String lottery){
        LotteryData lotteryData = LotteryDataManager.getLotteryData(lottery);
        ItemStack key = lotteryData.getKeyItem();
        if (key==null){
            return true;
        }
        else return player.getInventory().containsAtLeast(key,1);
    }
    public void removeKeyItem(String lottery){
        LotteryData lotteryData = LotteryDataManager.getLotteryData(lottery);
        ItemStack key = lotteryData.getKeyItem();
        if (key==null){
            return;
        }
        player.getInventory().removeItem(key) ;
    }

}