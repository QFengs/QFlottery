package github.qfeng.qflottery.Listener;

import github.qfeng.qflottery.Inv;
import github.qfeng.qflottery.LotteryData;
import github.qfeng.qflottery.LotteryDataManager;
import github.qfeng.qflottery.PlayerData;
import github.qfeng.qflottery.util.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {
    @EventHandler
    public void playerClickEvent(InventoryClickEvent e){

        if(e.getRawSlot()>e.getInventory().getSize()&&e.getInventory().getHolder() instanceof Inv){
            e.setCancelled(true);
            return;
        }
        if(e.isShiftClick()&&e.getInventory().getHolder() instanceof Inv){
            e.setCancelled(true);
            return;
        }
        if((e.getRawSlot()<44||e.getRawSlot()>53)&&e.getInventory().getHolder() instanceof Inv){
            e.setCancelled(true);
        }
        Player p = (Player) e.getWhoClicked();
        //创建gui对象和玩家对象
        String playerName = p.getName();
        PlayerData playerData = new PlayerData(playerName);
        if(e.getInventory().getHolder() instanceof Inv){
            e.setCancelled(true);
            if(checkNullClickItemName(e)&&checkNullClickItemLore(e)){

                //领取奖品的检测
                String lottery = ((Inv)e.getInventory().getHolder()).getLottery();
                LotteryData lotteryData = LotteryDataManager.getLotteryData(lottery);
                String check = "§a点击领取"+lottery+"的奖品";
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§b点击领取所有可领取的奖品")&&e.getCurrentItem().getItemMeta().getLore().contains(check)){
                    playerData.giveAllPrize(lottery);
                    e.getWhoClicked().closeInventory();
                    return;
                }

                //重新十连抽的检测
                String draw = "§a点击重新进行"+lottery+"的十连抽";
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§d点击重新进行十连抽")&&e.getCurrentItem().getItemMeta().getLore().contains(draw)){
                    e.getWhoClicked().closeInventory();
                    lotteryData.getDrawTenPrize(playerName);
                    return;
                }

                //opengui的单抽
                String openone = "§a点击进行一次"+lottery+"的抽奖";
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§d点击进行抽奖")&&e.getCurrentItem().getItemMeta().getLore().contains(openone)){
                    e.getWhoClicked().closeInventory();
                    lotteryData.drawPrize(playerName);
                    return;
                }
                //opengui的十连
                String openten = "§a点击进行十次"+lottery+"的抽奖";
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§d点击进行十连抽")&&e.getCurrentItem().getItemMeta().getLore().contains(openten)){
                    e.getWhoClicked().closeInventory();
                    lotteryData.getDrawTenPrize(playerName);
                    return;
                }



            }
        }
    }
    public boolean checkNullClickItemName(InventoryClickEvent e){
        if (e.getCurrentItem()!=null){
            if(e.getCurrentItem().getItemMeta()!=null){
                return e.getCurrentItem().getItemMeta().getDisplayName() != null;
            }
        }
        return false;
    }
    public boolean checkNullClickItemLore(InventoryClickEvent e){
        if (e.getCurrentItem()!=null){
            if(e.getCurrentItem().getItemMeta()!=null){
                return e.getCurrentItem().getItemMeta().getLore() != null;
            }
        }
        return false;
    }
}
