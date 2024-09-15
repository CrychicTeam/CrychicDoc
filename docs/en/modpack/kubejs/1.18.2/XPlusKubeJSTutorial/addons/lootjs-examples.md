# 12.8 LootJS战利品表修改实例

1、雨天实体掉落物翻倍

```
onEvent("lootjs", (event) => {
    event
        .addLootTypeModifier(LootType.ENTITY) // 修改实体战利品表
        .weatherCheck({
            raining: true,// 检测当前天气为雨天
        })
        .thenModify(Ingredient.getAll(), (itemStack) => {
            // 掉落物数量翻倍
            return itemStack.withCount(itemStack.getCount() * 2);
        });
});
```

2、检测玩家手持物品，当手持下界合金斧破坏带#forge:ores标签的方块时时额外掉落砂砾

```
onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("#forge:ores") // 使用方块tag匹配战利品表
        .matchEquip(EquipmentSlot.MAINHAND, Item.of("minecraft:netherite_pickaxe").ignoreNBT())
        .thenAdd("minecraft:gravel");
});
```

3、修改宝箱战利品表，概率掉落下界合金剑

```
onEvent("lootjs", (event) => {
    event
        .addLootTypeModifier(LootType.CHEST)
        .randomChance(0.05)
        .thenAdd("minecraft:netherite_sword");
});
```

4、修改宝箱战利品表，将所有钻石替换为下界合金

```
onEvent("lootjs", (event) => {
    event
        .addLootTypeModifier(LootType.CHEST)
        .thenReplace("minecraft:diamond","minecraft:netherite");
});
```

5、修改铁傀儡战利品表，使其只在村庄中被杀死时掉落铁锭

```
onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .not((callback)=>{
        	callback.anyStructure(["minecraft:village"], false)
        }) 
        .thenRemove("minecraft:iron_ingot");// 反转条件修改掉落物
});
```
