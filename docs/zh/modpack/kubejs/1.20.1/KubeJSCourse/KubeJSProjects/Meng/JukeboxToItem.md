---
authors: ['Gu-meng']
---
# 唱片转化为物品
本章涉及内容：唱片方块实体、方块实体数据、方块的弹出物品方法
涉及模组及版本:
1. rhino-forge-2001.2.2-build.18
2. architectury-9.2.14-forge
3. kubejs-forge-2001.6.5-build.14
4. probejs-6.0.1-forge

## 项目代码
```js
BlockEvents.rightClicked("jukebox", event => {
    let block = event.getBlock()
    // 判断右键唱片机后唱片机是否在播放
    if (block.entityData.getBoolean("IsPlating")){
        // 设置定时任务在3秒后进行判断
        event.server.scheduleInTicks(20 * 3, () => {
            let newBlock = event.level.getBlock(block.pos)
            if (newBlock.id == 'minecraft:air') return
            let blockEntity = newBlock.getEntity()
            let blockEntityData = newBlock.getEntityData()
            // 判断物品是否为空气来进行判断是否在播放 
            // 也可以使用blockEntityData.getBoolean("IsPlating") 来判断
            if (!blockEntity.getFirstItem().is("air")) {
                // 判断播放时长是否大于2.8 （尽可能的接近但是最好不要等于，怕出现其他意外）
                if ((blockEntityData.getInt("TickCount") - blockEntityData.getInt("RecordStartTick")) / 20 >= 2.8) {
                    // 清除唱片机里面的物品
                    blockEntity.clearContent()
                    // 在方块上面弹出一个新的物品
                    newBlock.popItemFromFace("stone", "up")
                }
            }
        })
    }
})
```

## 一些注意事项
1. 该项目只是作为示例，很多地方并不是最优解，可自行进行解决
2. 如果对该项目代码部分不满可以将修改好的代码上传至[gitee项目仓库](https://gitee.com/gumengmengs/kubejs-course)
3. 代码多出为可变动参数可以根据需求自行修改