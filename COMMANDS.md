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
```

**/rfmgame 中除了 start 与 stop 以外的子命令都需要逃走中游戏运行时才可以使用。**

命令的作用已在上文列出。

若下文没有额外介绍上文列出的命令，则意味着它们不需要额外参数。

下文提及的参数是有序的。

## /rfmgame control money

`add` 和 `set` 选项需要两个参数: 玩家名 和 数量。

## /rfmgame forceout & /rfmgame respawn

这两个命令都只需要一个 玩家名 作为参数。
