QFLottery——无限抽奖

前言：
基于抽奖给大众带来诸多愉悦以及防止服务器某些资源泛滥的情况下
我写出了 QFLottery——无限抽奖
目前没有内置物品库，只加载了sx的物品库MM的物品库和NI的物品库，如若两个插件都无，那就没法使用。这里推荐使用NI。检查顺序也是优先NI的
插件为bukkit插件，测试端为1.12.2其他版本自测
那么它能干什么呢？
● 无限抽奖，抽到自己想要的东西再领
● 特点：抽领分离，将抽奖和领奖的过程分开，您可以放心让玩家爽抽
● 可设置成纯福利抽奖，无需任何“奖券”便可领取奖品
● 可设置抽奖箱的领取次数，
● 可设置成纯抽，不让领
● 可设置成基础抽奖箱(不限制领取次数)
● 可储存最近十个奖项
● 可设置成需要“奖券”才能领取奖品
指令：


/ql give <player> <lottery> <number> 	给予玩家某个中奖箱的number个物品（直到达到领取上限）
/ql open <player> <lottery> 				为玩家打开某个宝箱
/ql addprizetoplayer <player> <lottery> <item>	为玩家的某个中奖箱增加奖品（奖品可以不在抽奖箱内，但一定要在物品库内）虽然也不会出现报错，但是物品会是插件内置的错误物品
/ql addprizetolottery <lottery> <item> <weight> 为某个抽奖箱增加新奖品，以及其对应的权重
(注意事项同上，权重不为0)
/ql showlottery <player>				展示玩家的所有中奖箱的名称
/ql show <player> <lottery> 			展示玩家某个中奖箱的十个物品（只能保存十个物品）
/ql draw <player> <lottery> <number>	让某个玩家在某个宝箱内进行number次抽奖
为防止卡服，已设置最高连抽为20连抽（但是中奖箱最多只能存10个奖品，所以二十连抽没有意义）
配置：
config
Lottery:    #这个不要改
  x1:
    number: 0 #只能领取一次
    key: null
  x2:
    number: 0 #无领取限制
    key: null
  x3:
    number: -1 #抽奖箱名称：次数
  x4:
    number: -1 #负数不予领取,未标明配置均为无限领取
Items:
  DangBan:  #这个名字也不要改！
    material: STAINED_GLASS_PANE   #材质，不用说
    name: "§d§l无限抽奖"
    lore:           #lore，不用说
      - "§d这个是玻璃挡板"
      - "§d§l无限抽奖"

PlayerData

Lottery
(现在也支持MM物品库)

Message
Plugin:
  Name: '&8「&d无限抽奖&8」'
Admin:
  NoPermissionCommand: '&&8「&d无限抽奖&8」 &c你没有权限执行此指令'
  NoCommand: '&8「&d无限抽奖&8」 &c未找到此子指令:{0}'
  NoFormat: '&8「&d无限抽奖&8」 &c格式错误!'
  NoOnline: '&8「&d无限抽奖&8」&c玩家不在线或玩家不存在!'
  NoConsole: '&8「&d无限抽奖&8」&c控制台不允许执行此指令!'
  PluginReload: '&8「&d无限抽奖&8」§c插件已重载'
Command:
  reload: 重新加载这个插件的配置
  give: 给予玩家中将箱的物品
  draw: 抽奖
  open: 打开某个抽奖箱
  show: 展示玩家某个中将箱的物品（10个）
  showlottery: 展示玩家的中将箱
  addprizetoplayer: 给玩家某个中奖箱里增加物品
  addprizetolottery: 给某个抽奖箱里增加物品
Message:
  Player:
    NoLottery: 该玩家暂无抽奖记录
    Give: '&a已将奖品&e{0}&a给予玩家&6{1}'
    NoKey: '&e玩家{0}&c缺少{1}的钥匙'
    NoEnoughKey: '&e玩家{0}&c没有足够的钥匙'
    GiveAll: '&a已将奖品箱&e{0}所有可给予的物品&a给予玩家&6{1}'
    NoEnoughPrize: 该玩家的中奖箱已空，或已达到该宝箱的领取上限
  Lottery:
    NoPrize: 该抽奖箱暂无奖品
    NoGiveForTimes: '''&c抽奖箱&a{0}&c已设置不让领取'''
Title:
  Player:
    Prize: '&e{0}在{1}的中奖箱'
  Lottery:
    Prize: '&e宝箱{0}'
语雀链接：
https://www.yuque.com/yuqueyonghu3fn5f9/kkzl8x?# 《QFLottery》

QQ群：254585408
