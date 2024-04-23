package github.qfeng.qflottery.util;


import github.qfeng.qflottery.*;
import github.qfeng.qflottery.Item.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.IntStream;

public class LotteryCommand implements CommandExecutor, TabExecutor {

    public JavaPlugin plugin = QFlottery.getPlugin();

    public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args) {
        CommandType type = CommandType.CONSOLE;
        if (sender instanceof Player) {
            if (!sender.hasPermission(plugin.getName() + ".use")) {
                sender.sendMessage(Message.getMsg("Admin.NoPermissionCommand", new Object[0]));
                return true;
            }

            type = CommandType.PLAYER;
        }

        int var8;
        if (args.length == 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&l----------"+Message.getMsg("Plugin.Name")+" &e&l指令详细"+"&7&l----------") );
            String color = "&7";
            Method[] var14 = this.getClass().getDeclaredMethods();
            var8 = var14.length;

            for (int var15 = 0; var15 < var8; ++var15) {
                Method method = var14[var15];
                if (method.isAnnotationPresent(PlayerCommand.class)) {
                    PlayerCommand sub = (PlayerCommand) method.getAnnotation(PlayerCommand.class);
                    if (this.contains(sub.type(), type) && sender.hasPermission(plugin.getName() + "." + sub.cmd())) {
                        color = color.equals("&7") ? "" : "&7";
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageFormat.format(color + "/{0} {1}{2}&7 -&c {3}", label, sub.cmd(), sub.arg(), Message.getMsg("Command." + sub.cmd(), new Object[0]))));
                    }
                }
            }

            return true;
        } else {
            Method[] var6 = this.getClass().getDeclaredMethods();
            int var7 = var6.length;

            for (var8 = 0; var8 < var7; ++var8) {
                Method method = var6[var8];
                if (method.isAnnotationPresent(PlayerCommand.class)) {
                    PlayerCommand sub = (PlayerCommand) method.getAnnotation(PlayerCommand.class);
                    if (sub.cmd().equalsIgnoreCase(args[0])) {
                        if (this.contains(sub.type(), type) && sender.hasPermission(plugin.getName() + "." + args[0])) {
                            try {
                                method.invoke(this, sender, args);
                            } catch (InvocationTargetException | IllegalAccessException var12) {
                                var12.printStackTrace();
                            }

                            return true;
                        }

                        sender.sendMessage(Message.getMsg("Admin.NoPermissionCommand", new Object[0]));
                        return true;
                    }
                }
            }

            sender.sendMessage(Message.getMsg("Admin.NoCommand", new Object[]{args[0]}));
            return true;
        }
    }

    private boolean contains(CommandType[] type1, CommandType type2) {
        return IntStream.range(0, type1.length).anyMatch((i) -> {
            return type1[i].equals(CommandType.ALL) || type1[i].equals(type2);
        });
    }
    private void showHelp(CommandSender sender) {
        // 显示帮助信息
        sender.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.GREEN + "输入'/ql'查看详细指令");

    }

    @PlayerCommand(
            cmd = "give",
            arg = " <player> <lottery> <numbers>"
    )
    public void onGiveCommand(CommandSender sender, String[] args) {
        if (args.length > 3) {
            Player player = Bukkit.getPlayerExact(args[1]);
            if (player == null) {
                sender.sendMessage(Message.getMsg("Admin.NoOnline", new Object[0]));
                return;
            }
            PlayerData playerData = PlayerDataManager.getPlayerData(args[1]);
            String lotteryName = args[2];
            int numberOfPrizes = Integer.parseInt(args[3]);
            List<String> prizes = playerData.getRecentPrizes(lotteryName, numberOfPrizes);
            if(prizes.isEmpty()){
                sender.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.RED + "没有找到该玩家的抽奖记录。");
                return;
            }
            numberOfPrizes = Math.min(numberOfPrizes,prizes.size());
            for (int i = 0; i < numberOfPrizes; i++) {
                if (!prizes.isEmpty()&&playerData.givePrize(lotteryName,prizes.get(i))) {
                    sender.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.GREEN +Message.getMsg("Message.Player.Give",prizes.get(i),player.getName()));
                } else{
                    sender.sendMessage(Message.getMsg("Plugin.Name")+Message.getMsg("Message.Player.NoEnoughPrize"));
                    if (i !=0){
                        sender.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.GREEN + "已给予玩家" + i + "个物品");
                    }
                    break;
                }
                if(i==numberOfPrizes-1){
                    sender.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.GREEN + "已给予玩家" + numberOfPrizes + "个物品");
                }
            }

        } else {
            showHelp(sender);
        }
    }


    @PlayerCommand(
            cmd = "show",
            arg = " <player> <lottery>"
    )
    public void onShowCommand(CommandSender sender, String[] args) {
        if (args.length > 2) {
            Player player = Bukkit.getPlayerExact(args[1]);
            if (player == null) {
                sender.sendMessage(Message.getMsg("Admin.NoOnline", new Object[0]));
                return;
            }
            PlayerData playerData = PlayerDataManager.getPlayerData(args[1]);
            String lotteryName = args[2];
            playerData.getShowPrizeGui(lotteryName,sender.getName());
            /*List<String> prizes = playerData.getRecentPrizes(lotteryName);
            if(prizes.isEmpty()){
                sender.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.RED + "没有找到该玩家的抽奖记录。");
                return;
            }
            sender.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.GREEN +player.getName()+ "的"+lotteryName+"中奖记录为：");
            for (String prize : prizes) {
                sender.sendMessage(ChatColor.GRAY+prize);
            }*/
        } else {
            showHelp(sender);
        }
    }


    @PlayerCommand(
            cmd = "showlottery",
            arg = " <player>"
    )
    public void onShowlotteryCommand(CommandSender sender, String[] args) {
        if (args.length > 1) {
            Player player = Bukkit.getPlayerExact(args[1]);
            if (player == null) {
                sender.sendMessage(Message.getMsg("Admin.NoOnline", new Object[0]));
                return;
            }
            PlayerData playerData = PlayerDataManager.getPlayerData(args[1]);
            List<String> lotteryName = playerData.getLotteryName();
            if(lotteryName.isEmpty()){
                sender.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.RED + "没有找到该玩家的抽奖记录。");
                return;
            }
            sender.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.GREEN +player.getName()+ "的中奖记录为：");
            Boolean f = true;
            for (String s : lotteryName) {
                if (!playerData.getRecentPrizes(s).isEmpty()){
                    sender.sendMessage(ChatColor.GRAY+"宝箱名："+s);
                    if(f){
                        f = false;
                    }
                }
            }
            if(f){
                sender.sendMessage(ChatColor.GRAY+Message.getMsg("Message.Player.NoLottery"));
            }
        } else {
            showHelp(sender);
        }
    }

    @PlayerCommand(
            cmd = "addprizetoplayer",
            arg = " <player> <lottery> <item>"
    )
    public void onAddPlayerPrizeCommand(CommandSender sender, String[] args) {
        if (args.length > 3) {
            Player player = Bukkit.getPlayerExact(args[1]);
            if (player == null) {
                sender.sendMessage(Message.getMsg("Admin.NoOnline", new Object[0]));
                return;
            }
            PlayerData playerData = PlayerDataManager.getPlayerData(args[1]);
            String lotteryName = args[2];
            String itemName = args[3];
            playerData.addPrize(lotteryName,itemName);
            playerData.save();
            sender.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.GREEN + "已添加");
        } else {
            showHelp(sender);
        }
    }

    @PlayerCommand(
            cmd = "addprizetolottery",
            arg = " <lottery> <item> <weight>"
    )
    public void onAddLotteryPrizeCommand(CommandSender sender, String[] args) {
        if (args.length > 3) {
            LotteryData lotteryData = LotteryDataManager.getLotteryData(args[1]);
            String itemName = args[2];
            Integer w = Integer.valueOf(args[3].replaceAll("[^0-9]", ""));
            lotteryData.addPrize(itemName,w);
            lotteryData.save();
            sender.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.GOLD + "已添加");
        } else {
            showHelp(sender);
        }
    }
    @PlayerCommand(
            cmd = "draw",
            arg = " <player> <lottery> <number>"
    )
    public void onDrawCommand(CommandSender sender, String[] args) {
        if (args.length > 2) {
            Player player = Bukkit.getPlayerExact(args[1]);
            if (player == null) {
                sender.sendMessage(Message.getMsg("Admin.NoOnline", new Object[0]));
                return;
            }
            int var1 ;
            if(args.length == 3){
                var1 =1;
            } else {
                var1 = Integer.valueOf(args[3].replaceAll("[^0-9]",""));
            }
            PlayerData playerData = PlayerDataManager.getPlayerData((args[1]));
            LotteryData lotteryData = LotteryDataManager.getLotteryData(args[2]);
            if(lotteryData.isPrizeEmpty()){
                sender.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.RED + "该抽奖箱不存在");
                return;
            }
            lotteryData.getDrawNumberPrize(args[1],var1);
            playerData.save();
            var1 = Math.min(var1,20);
            if (var1!=0){
                sender.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.GOLD + "已为您完成"+var1+"次抽奖");
            }
        } else {
            showHelp(sender);
        }
    }
    @PlayerCommand(
            cmd = "open",
            arg = " <player> <lottery>"
    )
    public void onOpenCommand(CommandSender sender, String[] args) {
        if (args.length > 2) {
            Player player = Bukkit.getPlayerExact(args[1]);
            if (player == null) {
                sender.sendMessage(Message.getMsg("Admin.NoOnline", new Object[0]));
                return;
            }
            LotteryData lotteryData = LotteryDataManager.getLotteryData(args[2]);
            Stats playerstats = PlayerStatsManager.getPlayerStats(args[1]);
            if(lotteryData.isPrizeEmpty()){
                sender.sendMessage(Message.getMsg("Plugin.Name")+ChatColor.RED + "该抽奖箱不存在");
                return;
            }
            Inventory gui = lotteryData.getPrizeWeightGui();
            gui.setItem(48,ItemManager.getOneDrawItem(args[2]));
            gui.setItem(50,ItemManager.getTenDrawItem(args[2]));
            player.openInventory(gui);
        } else {
            showHelp(sender);
        }
    }

    @PlayerCommand(
            cmd = "reload"
    )
    public void onReloadCommand(CommandSender sender, String[] args) {

        Message.loadMessage();
        Config.loadConfig();
        (new Thread(() -> {
            PlayerDataManager.getMap().values().forEach(PlayerData::load);
            PlayerDataManager.loadPlayerData();
            LotteryDataManager.getMap().values().forEach(LotteryData::load);
            LotteryDataManager.loadLotteryData();
            PlayerStatsManager.getMap().values().forEach(Stats::load);
            PlayerStatsManager.loadPlayerStats();

        })).start();
        sender.sendMessage(Message.getMsg("Admin.PluginReload", new Object[0]));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1&&args[0].contains("a")){
            List<String> list = new ArrayList<>();
            list.add("addprizetolottery");
            list.add("addprizetoplayer");
            return list;
        }
        if (args.length == 1&&args[0].contains("s")){
            List<String> list = new ArrayList<>();
            list.add("showprize");
            list.add("showlottery");
            list.add("show");
            return list;
        }
        if (args.length == 1&&args[0].contains("d")){
            List<String> list = new ArrayList<>();
            list.add("draw");
            return list;
        }
        if (args.length == 1&&args[0].contains("g")){
            List<String> list = new ArrayList<>();
            list.add("give");
            return list;
        }
        if (args.length == 1&&args[0].contains("o")){
            List<String> list = new ArrayList<>();
            list.add("open");
            return list;
        }
        if(args.length == 1){
            List<String> list = new ArrayList<>();
            list.add("addprizetolottery");
            list.add("addprizetoplayer");
            list.add("showprize");
            list.add("showlottery");
            list.add("show");
            list.add("give");
            list.add("draw");
            list.add("open");
            return list;
        }
        return null;
    }
}
