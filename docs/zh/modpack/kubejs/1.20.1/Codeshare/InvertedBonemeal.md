# 反转骨粉

::: v-info
手持反转骨粉右键具有“minecraft:crops”标签的方块，获取age，并将方块替换为age = age - 1的作物方块

触发概率可调，默认80%（0.8）

物品注册代码及物品贴图请自行实现。

粒子效果方法和声音方法的参数请参阅[Wiki /particle指令](https://zh.minecraft.wiki/w/%E5%91%BD%E4%BB%A4/particle)和[Wiki /playsound](https://zh.minecraft.wiki/w/%E5%91%BD%E4%BB%A4/playsound)，并结合ProbeJS食用。
:::

```js
BlockEvents.rightClicked(event => {

    // 解构
    const { block, item, player, hand } = event // 解构
    
    // 如果方块不是作物，退出
    if (!block.hasTag('minecraft:crops')) return

    // 退化概率
    const degenerateChance = 0.8

    // 如果右键物品是反转骨粉
    if (item.id == 'halosense:inverted_bone_meal') {
        
        // 获取方块位置（坐标）
        const blockPos = block.pos
        // 获取作物生长阶段（字符串）
        const ageString = block.properties.get('age')
        // 字符串转数字
        let age = parseInt(ageString)

        // 消耗一个物品
        item.count --
        // 手部挥动
        player.swing(hand, true)
        // 播放粒子效果
        block.level.spawnParticles('minecraft:angry_villager', false, blockPos.x + 0.5, blockPos.y - 0.5, blockPos.z + 0.5, 0.25, 0.1, 0.25, 10, 0)
        // 播放声音
        block.level.playSound(null, blockPos, 'minecraft:item.bone_meal.use', 'players')

        // 随机数
        if (Math.random() < degenerateChance) {

            // 如果生长阶段为0，则破坏作物方块            
            if (age == 0) {
                block.level.destroyBlock(blockPos, true)
            }
            else {  // 否则作物方块生长阶段-1
                age --
                const newAgeString = age.toString()
                block.set(block.id, {'age': newAgeString})
            }
        }
        
    }
})
```