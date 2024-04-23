//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package github.qfeng.qflottery.util;

import github.qfeng.qflottery.QFlottery;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Message {
    public static final String PLUGIN_NAME = "Plugin.Name";
    public static final String ADMIN_NO_PER_CMD = "Admin.NoPermissionCommand";
    public static final String ADMIN_NO_CMD = "Admin.NoCommand";
    public static final String ADMIN_NO_FORMAT = "Admin.NoFormat";
    public static final String ADMIN_NO_ONLINE = "Admin.NoOnline";
    public static final String ADMIN_NO_CONSOLE = "Admin.NoConsole";
    public static final String ADMIN_PLUGIN_RELOAD = "Admin.PluginReload";
    public static final String COMMAND_RELOAD = "Command.reload";
    public static final String COMMAND_GIVE = "Command.give";
    public static final String COMMAND_SHOW = "Command.show";
    public static final String COMMAND_SHOWLOTTERY = "Command.showlottery";
    public static final String COMMAND_DRAW = "Command.draw";
    public static final String COMMAND_OPEN = "Command.open";
    public static final String COMMAND_ADD_PRIZE_TO_PLAYER = "Command.addprizetoplayer";
    public static final String COMMAND_ADD_PRIZE_TO_LOTTERY = "Command.addprizetolottery";
    public static final String MESSAGE_PLAYER_NOLOTTERY = "Message.Player.NoLottery";
    public static final String MESSAGE_PLAYER_GIVE = "Message.Player.Give";
    public static final String MESSAGE_PLAYER_GIVE_All = "Message.Player.GiveAll";
    public static final String MESSAGE_PLAYER_NOPRIZE = "Message.Player.NoEnoughPrize";
    public static final String MESSAGE_PLAYER_NOKEY = "Message.Player.NoKey";
    public static final String MESSAGE_PLAYER_NOENOUGHKEY = "Message.Player.NoEnoughKey";
    public static final String MESSAGE_LOTTERY_NOPRIZE = "Message.Lottery.NoPrize";
    public static final String MESSAGE_LOTTERY_NOGIVEFORTIMES = "Message.Lottery.NoGiveForTimes";
    public static final String TITILE_PLAYER_PRIZE = "Title.Player.prize";
    public static final String TITILE_LOTTERY_PRIZE = "Title.Lottery.prize";
    static final File messageFile;
    private static YamlConfiguration messages;

    static {
        messageFile = new File("plugins" + File.separator + QFlottery.getPlugin().getName() + File.separator + "Message.yml");
    }

    public Message() {
    }

    public static void createMessage() {
        Bukkit.getConsoleSender().sendMessage("[" + QFlottery.getPlugin().getName() + "] §cCreate Message.yml");
        messages = new YamlConfiguration();
        messages.set("Plugin.Name", "&8「&d无限抽奖&8」");
        messages.set("Admin.NoPermissionCommand", "&&8「&d无限抽奖&8」 &c你没有权限执行此指令");
        messages.set("Admin.NoCommand", "&8「&d无限抽奖&8」 &c未找到此子指令:{0}");
        messages.set("Admin.NoFormat", "&8「&d无限抽奖&8」 &c格式错误!");
        messages.set("Admin.NoOnline", "&8「&d无限抽奖&8」&c玩家不在线或玩家不存在!");
        messages.set("Admin.NoConsole", "&8「&d无限抽奖&8」&c控制台不允许执行此指令!");
        messages.set("Admin.PluginReload", "&8「&d无限抽奖&8」§c插件已重载");
        messages.set("Command.reload", "重新加载这个插件的配置");
        messages.set("Command.give", "给予玩家中将箱的物品");
        messages.set("Command.draw", "抽奖");
        messages.set("Command.open", "打开某个抽奖箱");
        messages.set("Command.show", "展示玩家某个中将箱的物品（10个）");
        messages.set("Command.showlottery", "展示玩家的中将箱");
        messages.set("Command.addprizetoplayer", "给玩家某个中奖箱里增加物品");
        messages.set("Command.addprizetolottery", "给某个抽奖箱里增加物品");
        messages.set("Message.Player.NoLottery", "该玩家暂无抽奖记录");
        messages.set("Message.Player.Give", "&a已将奖品&e{0}&a给予玩家&6{1}");
        messages.set("Message.Player.NoKey", "&e玩家{0}&c缺少{1}的钥匙");
        messages.set("Message.Player.NoEnoughKey", "&e玩家{0}&c没有足够的钥匙");
        messages.set("Message.Player.GiveAll", "&a已将奖品箱&e{0}所有可给予的物品&a给予玩家&6{1}");
        messages.set("Message.Player.NoEnoughPrize", "该玩家的中奖箱已空，或已达到该宝箱的领取上限");
        messages.set("Message.Lottery.NoPrize", "该抽奖箱暂无奖品");
        messages.set("Message.Lottery.NoGiveForTimes", "'&c抽奖箱&a{0}&c已设置不让领取'");
        messages.set("Title.Player.Prize", "&e{0}在{1}的中奖箱");
        messages.set("Title.Lottery.Prize", "&e宝箱{0}");
        try {
            messages.save(messageFile);
        } catch (IOException var1) {
            var1.printStackTrace();
        }

    }

    public static void loadMessage() {
        if (!messageFile.exists()) {
            createMessage();
        } else {
            Bukkit.getConsoleSender().sendMessage("[" + QFlottery.getPlugin().getName() + "] §aFind Message.yml");
        }

        messages = new YamlConfiguration();

        try {
            messages.load(messageFile);
        } catch (InvalidConfigurationException | IOException var1) {
            var1.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(Message.getMsg("Plugin.Name")+" §a读取message时发生错误");
        }

    }

    public static String getMsg(String loc, Object... args) {
        String raw = messages.getString(loc);
        if (raw != null && !raw.isEmpty()) {
            if (args == null) {
                return raw.replace("&", "§");
            } else {
                for (int i = 0; i < args.length; ++i) {
                    raw = raw.replace("{" + i + "}", args[i] == null ? "null" : args[i].toString());
                }

                return raw.replace("&", "§");
            }
        } else {
            return "Null Message: " + loc;
        }
    }

    public static List<String> getList(String loc, String... args) {
        List<String> list = messages.getStringList(loc);
        if (list != null && !list.isEmpty()) {
            for (int e = 0; e < list.size(); ++e) {
                String lore = ((String) list.get(e)).replace("&", "§");

                for (int i = 0; i < args.length; ++i) {
                    lore = lore.replace("{" + i + "}", args[i] == null ? "null" : args[i]);
                }

                list.set(e, lore);
            }

            return list;
        } else {
            list.add("Null Message: " + loc);
            return list;
        }
    }

    public static void send(LivingEntity entity, String loc, Object... args) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            String message = getMsg(loc, args);
            if (message.contains("[ACTIONBAR]")) {
                message = message.replace("[ACTIONBAR]", "");
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
            } else if (message.contains("[TITLE]")) {
                message = message.replace("[TITLE]", "");
                if (message.contains(":")) {
                    String title = message.split("\n")[0];
                    String subTitle = message.split("\n")[1];
                    player.sendTitle(title, subTitle, 5, 20, 5);
                } else {
                    player.sendTitle(message, (String) null, 5, 20, 5);
                }
            } else {
                player.sendMessage(message);
            }

        }
    }

    public static YamlConfiguration getMessages() {
        return messages;
    }
}
