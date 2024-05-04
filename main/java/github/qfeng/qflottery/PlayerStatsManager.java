package github.qfeng.qflottery;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class PlayerStatsManager {
    private static HashMap<String, Stats> map = new HashMap();

    public PlayerStatsManager() {
    }

    public static void loadPlayerStats() {
        map.clear();
    }

    public static Stats getPlayerStats(String playerName) {
        if (map.containsKey(playerName)) {
            return (Stats)map.get(playerName);
        } else {
            Stats stats = new Stats(playerName);
            map.put(playerName, stats);
            return stats;
        }
    }


    public static void autoSave() {
        (new BukkitRunnable() {
            public void run() {
                PlayerStatsManager.map.values().forEach(Stats::load);
            }
        }).runTaskTimerAsynchronously(QFlottery.getPlugin(), 2400L, 2400L);
    }

    public static HashMap<String, Stats> getMap() {
        return map;
    }
}
