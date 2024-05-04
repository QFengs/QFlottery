package github.qfeng.qflottery;

import java.util.HashMap;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDataManager {
    private static HashMap<String, PlayerData> map = new HashMap();

    public PlayerDataManager() {
    }

    public static void loadPlayerData() {
        map.clear();
    }

    public static PlayerData getPlayerData(String playerName) {
        if (map.containsKey(playerName)) {
            return (PlayerData)map.get(playerName);
        } else {
            PlayerData playerData = new PlayerData(playerName);
            map.put(playerName, playerData);
            return playerData;
        }
    }


    public static void autoSave() {
        (new BukkitRunnable() {
            public void run() {
                PlayerDataManager.map.values().forEach(PlayerData::load);
            }
        }).runTaskTimerAsynchronously(QFlottery.getPlugin(), 2400L, 2400L);
    }

    public static HashMap<String, PlayerData> getMap() {
        return map;
    }
}
