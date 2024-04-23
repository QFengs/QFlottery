package github.qfeng.qflottery;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class LotteryDataManager {
    private static HashMap<String, LotteryData> map = new HashMap();

    public LotteryDataManager() {
    }

    public static void loadLotteryData() {
        map.clear();
    }

    public static LotteryData getLotteryData(String LotteryName) {
        if (map.containsKey(LotteryName)) {
            return (LotteryData) map.get(LotteryName);
        } else {
            LotteryData lotteryData = new LotteryData(LotteryName);
            map.put(LotteryName, lotteryData);
            return lotteryData;
        }
    }


    public static void autoSave() {
        (new BukkitRunnable() {
            public void run() {
                LotteryDataManager.map.values().forEach(LotteryData::load);
            }
        }).runTaskTimerAsynchronously(QFlottery.getPlugin(), 2400L, 2400L);
    }

    public static HashMap<String, LotteryData> getMap() {
        return map;
    }
}
