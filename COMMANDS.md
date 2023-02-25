# RunForMoney2 - 命令说明书

命令结构树如下:
```text
/rfmgame
|
+--- start (启动游戏)
     stop  (强制终止游戏)
     pause (暂停游戏)
     resume (继续游戏)
     control (修改游戏)
     |
     +--- money (硬币相关操作)
     |    |
     |    +--- add (为某人增加硬币)
     |         set (设置某人的硬币数量)
     |         reset (重置所有人的硬币数量)
     |         get (获取所有人的硬币数量)
     |
     +--- reverse (使游戏时间倒流)
     +--- forceout (强制淘汰指定玩家)
     +--- respawn (使某个玩家重新回到游戏)

/rfmteam
|
+--- join (加入指定队伍)
+--- leave (离开所在队伍)

/rfmitem (获取逃走中道具)
/slowitem (将手中物品标记为有缓慢效果)
/rfmtimer (在游戏时显示剩余时间)
```

除了 `/rfmteam` 命令，其他命令都需要管理员才可以执行。
* `/rfmteam join` 在批量操作时也需要管理员！

**/rfmgame 中除了 start 与 stop 以外的子命令都需要逃走中游戏运行时才可以使用。**

命令的作用已在上文列出。

若下文没有额外介绍上文列出的命令，则意味着它们不需要额外参数。

下文提及的参数是有序的。

## /rfmgame start

完整语法: `/rfmgame start [true|false] [releaseTime: int]`

第一个可选参数为是否强制启动游戏。

第二个为游戏开始倒计时的时间，若无则使用配置文件提供的值。

## /rfmgame control money

`add` 和 `set` 选项需要两个参数: 玩家名 和 数量。

`get` 选项有一个可选参数: 玩家名。若提供了，则仅查询对应玩家的硬币。

## /rfmgame control forceout & /rfmgame control respawn

允许指定一些玩家以批量操作。

## /rfmteam join

命令格式: `/rfmteam join <队伍名> [玩家...]`

玩家可以是多个，以批量操作。

## /slowitem

命令格式: `/slowitem [等级]`

等级为将要应用的缓慢效果的等级。

插件的匹配逻辑: 尝试匹配等级最大的。

比如你的背包里有两个缓慢物品，一个等级 3，一个等级 10，插件会施加 10 级缓慢。
