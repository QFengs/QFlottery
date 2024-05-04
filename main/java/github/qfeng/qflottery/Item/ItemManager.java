package github.qfeng.qflottery.Item;

import github.qfeng.qflottery.LotteryData;
import github.qfeng.qflottery.LotteryDataManager;
import github.qfeng.qflottery.util.Config;
import github.saukiya.sxattribute.SXAttribute;
import io.lumine.xikage.mythicmobs.MythicMobs;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    public static ItemStack getItem (String key){
        if(key==null){
            return null;
        }
        ItemStack stack = null;
        List<String> i = Config.getI();
        if(i.contains("NI")) {
            stack = pers.neige.neigeitems.manager.ItemManager.INSTANCE.getItemStack(key);
        }
        if (stack==null&&i.contains("MM")){
           stack = MythicMobs.inst().getItemManager().getItemStack(key);
        }
        if (stack==null&&i.contains("SX")){
            stack = SXAttribute.getApi().getItem(key,null);
        }
        if (stack==null){
            ItemStack netherStar = new ItemStack(Material.NETHER_STAR);
            ItemMeta m = netherStar.getItemMeta();
            m.setDisplayName("§r§l物品库不存在该物品");
            List<String> lore = new ArrayList<>();
            lore.add("§7§l当你看到该物品，请联系管理员进行修改配置");
            m.setLore(lore);
            netherStar.setItemMeta(m);
            return netherStar;
        }
        return stack;
    }
    public static ItemStack getKeyItem(String key){
        if(key==null){
            return null;
        }
        List<String> i = Config.getI();
        ItemStack stack = null;
        if (i.contains("NI")) {
            stack = pers.neige.neigeitems.manager.ItemManager.INSTANCE.getItemStack(key);
        }
        if (stack==null&&i.contains("MM")){
            stack = MythicMobs.inst().getItemManager().getItemStack(key);
        }
        if (stack==null&&i.contains("SX")){
            stack = SXAttribute.getApi().getItem(key,null);
        }
        return stack;
    }
    public static ItemStack getItemWithWeight(String lottery,String key){
        LotteryData lotteryData = LotteryDataManager.getLotteryData(lottery);
        ItemStack itemStack = getItem(key);
        ItemMeta m;
        m = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (m != null) {
            if(!(m.getLore() == null)){
                lore = m.getLore();
            }
        }
        lore.add("§7§m===========================");
        lore.add("§e权重:"+lotteryData.getPrizeWeight(key));
        if (m != null) {
            m.setLore(lore);
        }
        itemStack.setItemMeta(m);
        return itemStack;
    }
    public static ItemStack getCheckGiveItem(String lottery){
        LotteryData lotteryData = LotteryDataManager.getLotteryData(lottery);
        ItemStack give = new ItemStack(Material.DIAMOND);
        ItemMeta m = give.getItemMeta();
        m.setDisplayName("§b点击领取所有可领取的奖品");
        List<String> lore = new ArrayList<>();
        lore.add("§a点击领取"+lottery+"的奖品");
        if(lotteryData.getLotteryKeyName()!=null){
            lore.add("§7§m===========================");
            lore.add("§a需求：§d"+lotteryData.getKeyItem().getItemMeta().getDisplayName());
        }
        m.setLore(lore);
        give.setItemMeta(m);
        return give;
    }
    public static ItemStack getReDrawItem(String lottery){
        ItemStack give = new ItemStack(Material.CHEST);
        ItemMeta m = give.getItemMeta();
        m.setDisplayName("§d点击重新进行十连抽");
        List<String> lore = new ArrayList<>();
        lore.add("§a点击重新进行"+lottery+"的十连抽");
        m.setLore(lore);
        give.setItemMeta(m);
        return give;
    }
    public static ItemStack getOneDrawItem(String lottery){
        ItemStack give = new ItemStack(Material.CHEST);
        ItemMeta m = give.getItemMeta();
        m.setDisplayName("§d点击进行抽奖");
        List<String> lore = new ArrayList<>();
        lore.add("§a点击进行一次"+lottery+"的抽奖");
        m.setLore(lore);
        give.setItemMeta(m);
        return give;
    }
    public static ItemStack getTenDrawItem(String lottery){
        ItemStack give = new ItemStack(Material.CHEST);
        ItemMeta m = give.getItemMeta();
        m.setDisplayName("§d点击进行十连抽");
        List<String> lore = new ArrayList<>();
        lore.add("§a点击进行十次"+lottery+"的抽奖");
        m.setLore(lore);
        give.setItemMeta(m);
        return give;
    }
}
