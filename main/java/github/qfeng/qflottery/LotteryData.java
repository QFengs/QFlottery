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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LotteryData {
    private String lottery;
    private Map<String, Integer> prizeWeight = new HashMap<>();
    private Integer lotteryTime = 0;
    private List<String> prize = new ArrayList<>();
    private Set<String> timesKeys;
    private String keyitem;
    private YamlConfiguration config = new YamlConfiguration();


    public LotteryData(String lottery) {
        this.lottery = lottery;
        YamlConfiguration yaml = new YamlConfiguration();
        File file = new File(Config.getDataFile(), "Lottery" + File.separator + this.lottery + ".yml");
        File configfile = new File(Config.getDataFile(), "config.yml");
        if (!file.exists() || file.isDirectory()) {
            this.save();
            return;
        }
        try {
            yaml.load(file);
            config.load(configfile);
        } catch (InvalidConfigurationException | IOException var7) {
            var7.printStackTrace();
        }
        load();

    }

    public void load(){
        YamlConfiguration yaml = new YamlConfiguration();
        File file = new File(Config.getDataFile(), "Lottery" + File.separator + this.lottery + ".yml");
        File configfile = new File(Config.getDataFile(), "config.yml");
        if (!file.exists() || file.isDirectory()) {
            this.save();
            return;
        }
        try {
            yaml.load(file);
            config.load(configfile);
        } catch (InvalidConfigurationException | IOException var7) {
            var7.printStackTrace();
        }
        ConfigurationSection lotteryName = yaml.getConfigurationSection(lottery);
        if(lotteryName != null){
            Set<String> keys = lotteryName.getKeys(false);
            for (String key : keys) {
                this.prize.add(key);
                this.prizeWeight.put(key,lotteryName.getInt(key));
            }

        }
        ConfigurationSection lotterytimes = config.getConfigurationSection(lottery);
        if (lotterytimes!=null){
            timesKeys = lotterytimes.getKeys(false);
            if (timesKeys.contains(lottery)) {
                lotteryTime = lotterytimes.getInt(lottery);
            }
        }
        ConfigurationSection key = config.getConfigurationSection("Lottery");
        if (!key.contains(this.lottery)){
            this.keyitem =null;
        } else {
            ConfigurationSection keyTwo = key.getConfigurationSection(this.lottery);
            this.keyitem = keyTwo.getString("key");
        }

    }
    public void save() {
        YamlConfiguration yaml = new YamlConfiguration();

        if (!prizeWeight.isEmpty()){
            for (String s : prize) {
                yaml.set(lottery+"."+s,prizeWeight.get(s));
            }
        }
        File file = new File(Config.getDataFile(), "Lottery" + File.separator + this.lottery + ".yml");
        try {
            yaml.save(file);
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }
    public void configsave(){
        if (!timesKeys.contains(lottery)){
            config.set("Lottery."+lottery,lotteryTime);
        }
        File configfile = new File(Config.getDataFile(), "config.yml");
        try {
            config.save(configfile);
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }
    public void addPrize(String item,Integer w) {
        if(!this.prize.contains(item)){
            this.prize.add(item);
        }
        if (!prizeWeight.containsKey(item)) {
            prizeWeight.put(item, w);
        }
        this.save();
    }


    public void drawPrize(String player){
        List<String> list = new ArrayList<>();
        Player pl = Bukkit.getPlayerExact(player);
        if (!prize.isEmpty()) {
            int totalWeight = 0;
            int sum = 0;
            for (String s : prize) {
                sum+=prizeWeight.get(s);
            }
            int randomValue = new Random().nextInt(sum); // 生成一个0到allweight之间的随机数
            PlayerData playerData = PlayerDataManager.getPlayerData(player);
            for (String p : prize)  {
                totalWeight += prizeWeight.get(p);
                if (randomValue <= totalWeight) {
                    // 根据权重随机抽取奖品
                    playerData.addPrize(lottery, p);
                    list.add(p);
                    break;
                }
            }
        }
        Inventory gui = new Inv().getGui("§a您抽到了如下奖励",lottery);
        for (String s : list) {
            int i=10;
            for (; i < 53; i++) {
                if (gui.getItem(i)==null){
                    gui.setItem(i, ItemManager.getItem(s));
                    break;
                }
            }
        }
        pl.openInventory(gui);
    }
    public void getDrawTenPrize(String player){
        List<String> list = new ArrayList<>();
        Player pl = Bukkit.getPlayerExact(player);
        for (int j = 0; j < 10; j++) {
            if (!prize.isEmpty()) {
                int totalWeight = 0;
                int sum = 0;
                for (String s : prize) {
                    sum+=prizeWeight.get(s);
                }
                int randomValue = new Random().nextInt(sum); // 生成一个0到allweight之间的随机数
                PlayerData playerData = PlayerDataManager.getPlayerData(player);
                for (String p : prize)  {
                    totalWeight += prizeWeight.get(p);
                    if (randomValue <= totalWeight) {
                        // 根据权重随机抽取奖品
                        playerData.addPrize(lottery, p);
                        list.add(p);
                        break;
                    }
                }

            }
        }
        Inventory gui = new Inv().getGui("§a您抽到了如下奖励",lottery);
        for (String s : list) {
            int i=10;
            for (; i < 53; i++) {
                if (gui.getItem(i)==null){
                    gui.setItem(i, ItemManager.getItem(s));
                    break;
                }
            }
        }
        gui.setItem(45,ItemManager.getCheckGiveItem(lottery));
        gui.setItem(53,ItemManager.getReDrawItem(lottery));
        pl.openInventory(gui);
    }
    public void getDrawNumberPrize(String player,Integer number){
        List<String> list = new ArrayList<>();
        Player pl = Bukkit.getPlayerExact(player);
        if (number>20){
            pl.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.RED+"单次最高进行二十连抽");
            number = 20;
        }
        for (int j = 0; j < number; j++) {
            if (!prize.isEmpty()) {
                int totalWeight = 0;
                int sum = 0;
                for (String s : prize) {
                    sum+=prizeWeight.get(s);
                }
                int randomValue = new Random().nextInt(sum); // 生成一个0到allweight之间的随机数
                PlayerData playerData = PlayerDataManager.getPlayerData(player);
                for (String p : prize)  {
                    totalWeight += prizeWeight.get(p);
                    if (randomValue <= totalWeight) {
                        // 根据权重随机抽取奖品
                        playerData.addPrize(lottery, p);
                        list.add(p);
                        break;
                    }
                }

            }
        }
        Inventory gui = new Inv().getGui("§a您抽到了如下奖励",lottery);
        for (String s : list) {
            int i=10;
            for (; i < 53; i++) {
                if (gui.getItem(i)==null){
                    gui.setItem(i, ItemManager.getItem(s));
                    break;
                }
            }
        }
        if (number>=10){
            gui.setItem(45,ItemManager.getCheckGiveItem(lottery));
        }
        gui.setItem(53,ItemManager.getReDrawItem(lottery));
        pl.openInventory(gui);
    }

    public Integer getPrizeWeight(String key){
        if (prize.contains(key)){
            return prizeWeight.get(key);
        }
        else {
            return 0;
        }
    }
    public Inventory getPrizeWeightGui(){
        Inv inv = new Inv();
        Inventory gui = inv.getGui(Message.getMsg("Title.Lottery.Prize",lottery),lottery);
        int i =10;
        for (String s : prize) {
            for (; i < 53; i++) {
                if(gui.getItem(i)==null){
                    gui.setItem(i,ItemManager.getItemWithWeight(lottery,s));
                    break;
                }
            }
        }
        return gui;
    }
    public Integer getTimes(){
        ConfigurationSection key = config.getConfigurationSection("Lottery");
        if (!key.contains(lottery)){
            return 0;
        }
        ConfigurationSection keyTwo = key.getConfigurationSection(lottery);
        return keyTwo.getInt("number");
    }
    public boolean isPrizeEmpty(){
        return prize.isEmpty();
    }
    public String getLotteryKeyName(){
        return keyitem;
    }
    public ItemStack getKeyItem(){
        return ItemManager.getKeyItem(keyitem);
    }
}