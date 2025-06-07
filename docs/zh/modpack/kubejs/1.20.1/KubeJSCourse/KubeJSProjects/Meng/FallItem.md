---
authors:
  - Gu-meng
editor: Gu-meng
---
# 下落合成
本章涉及内容：配方检测、物品实体、实体生成事件、世界tick事件
涉及模组及版本:
1. rhino-forge-2001.2.2-build.18
2. architectury-9.2.14-forge
3. kubejs-forge-2001.6.5-build.14
4. probejs-6.0.1-forge

## 完整代码
下面是完整代码和部分注释解析,如需完整内容解析请在哔哩哔哩查看孤梦的视频
```js
// 记录下落物品方块的数据
let itemFallList = {};
// 记录配方
let fallItem = [
    {
        inputItem: "minecraft:cobblestone",
        outputItem: 'minecraft:gravel',
        spaceBetween: 10
    }
]

EntityEvents.spawned("item", event => {
    /**
     * @type {Internal.ItemEntity}
     */
    let itemEntity = event.getEntity();
    fallItem.forEach(value => {
        // 判断物品
        if (itemEntity.getItem().getId() != value.inputItem) return;
        // 设置物品可被捡起时间为32767主要用处为无法合并(副作用无法捡起)
        itemEntity.pickUpDelay = 32767;
        // 记录实体掉落物的数量
        let count = itemEntity.getNbt().get("Item").getInt("Count")
         // 传值
        itemFallList[itemEntity.getUuid()] = {
            dimension: event.getLevel().getDimension(),
            y: itemEntity.getY(),
            output: value.outputItem,
            count: count,
            spaceBetween: value.spaceBetween
        }
    })
})

LevelEvents.tick(event => {
    if (event.server.tickCount % 5 != 0) return
    if (Object.keys(itemFallList).length == 0) return
    for (let key in itemFallList) {
        let fallValue = itemFallList[key];
        if (fallValue.dimension == event.getLevel().getDimension()) {
            try {
                let entity = event.getLevel().getEntity(key)
                if (entity.onGround()) {
                    if (fallValue.y - entity.getY() >= fallValue.spaceBetween) {
                        entity.setItem(Item.of(fallValue.output, fallValue.count))
                    }
                    entity.pickUpDelay = 20;
                    delete itemFallList[key]
                }
            } catch (e) {
                console.warn(e);
                delete itemFallList[key]
            }
        }
    }
})
```

## 一些注意事项
1. 该项目的配方检测全是字符串，并不是ItemStack所以无法检测nbt，如需请自行更改
2. 该项目只是作为示例，很多地方并不是最优解，可自行进行解决
3. 如果对该项目代码部分不满可以将修改好的代码上传至[gitee项目仓库](https://gitee.com/gumengmengs/kubejs-course)
4. 代码复制过去可直接使用，如需添加配方只需要在数组fallItem里进行添加修改
5. 如果是类似水流向上冲走物品实体然后从下掉下来这种，可以在leveltick里实时记录下来物品的最高y轴
