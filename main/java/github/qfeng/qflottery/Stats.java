package github.qfeng.qflottery;

import github.qfeng.qflottery.util.Config;
import github.qfeng.qflottery.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Stats {
    private String playerName;


    private List<String> lotteryOfPlayer = new ArrayList<>();
    private Map<String,Integer> playerStats = new HashMap<>();
    public Stats(String playerName) {
        this.playerName = playerName;
        YamlConfiguration yaml = new YamlConfiguration();
        File file = new File(Config.getDataFile(), "PlayerStats" + File.separator + this.playerName + ".yml");
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
                this.lotteryOfPlayer.add(key);
                this.playerStats.put(key, Integer.parseInt(player.getString(key)));
            }

        }
    }

    public void load(){
        YamlConfiguration yaml = new YamlConfiguration();
        File file = new File(Config.getDataFile(), "PlayerStats" + File.separator + this.playerName + ".yml");
        try {
            yaml.load(file);
        } catch (InvalidConfigurationException | IOException var7) {
            var7.printStackTrace();
        }
        try {
            yaml.save(file);
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }
    public void save() {
        YamlConfiguration yaml = new YamlConfiguration();
        if(!this.lotteryOfPlayer.contains("lotteryName")) {
            yaml.set(playerName + ".lotteryName", 0);
        }
        if (!playerStats.isEmpty()){
            for (String s : lotteryOfPlayer) {
                yaml.set(playerName+"."+s,playerStats.get(s));
            }
        }
        File file = new File(Config.getDataFile(), "PlayerStats" + File.separator + this.playerName + ".yml");
        try {
            yaml.save(file);
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }
    public void createStats(String lottery){
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set(playerName+"."+lottery,0);
        playerStats.put(lottery,0);
        File file = new File(Config.getDataFile(), "PlayerStats" + File.separator + this.playerName + ".yml");
        try {
            yaml.save(file);
        } catch (IOException var4) {
            var4.printStackTrace();
        }
        this.save();
    }
    public Integer getStatsOfLottery(String lottery){
        if (lotteryOfPlayer.contains(lottery)){
           return playerStats.get(lottery);
        } else{
            createStats(lottery);
            return playerStats.get(lottery);
        }
    }
    public Boolean CheckForStats(String lottery){
        LotteryData lotteryData = LotteryDataManager.getLotteryData(lottery);
        if(lotteryData.getTimes()==0){
            addTimes(lottery);
            return true;
        }
        if (getStatsOfLottery(lottery) < lotteryData.getTimes()){
            addTimes(lottery);
            return true;
        }
        Player player = Bukkit.getPlayer(playerName);
        int var1 = lotteryData.getTimes();
        if(var1>0){
            player.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.RED+"宝箱"+lottery +"只能领取"+var1+"次");
        }
        else {
            player.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.RED+Message.getMsg("Message.Lottery.NoGiveForTimes",lottery));
        }
        return false;
    }
    public Boolean JustCheckForStats(String lottery){
        LotteryData lotteryData = LotteryDataManager.getLotteryData(lottery);
        if(lotteryData.getTimes()==0){
            return true;
        }
        return getStatsOfLottery(lottery) < lotteryData.getTimes();
    }
    public void addTimes(String lottery){
        YamlConfiguration yaml = new YamlConfiguration();
        if(lotteryOfPlayer.contains(lottery)){
            int var1=playerStats.get(lottery)+1;
            yaml.set(playerName+"."+lottery,var1);
            playerStats.remove(lottery);
            playerStats.put(lottery,var1);
        } else {
            yaml.set(playerName+"."+lottery,1);
            lotteryOfPlayer.add(lottery);
            playerStats.put(lottery,1);
        }
        this.save();
    }
    public void setTimes(String lottery,Integer var1){
        YamlConfiguration yaml = new YamlConfiguration();
        if(lotteryOfPlayer.contains(lottery)){
            yaml.set(playerName+"."+lottery,var1);
            playerStats.remove(lottery);
            playerStats.put(lottery,var1);
        } else {
            yaml.set(playerName+"."+lottery,var1);
            lotteryOfPlayer.add(lottery);
            playerStats.put(lottery,var1);
        }
        this.save();
    }

}
