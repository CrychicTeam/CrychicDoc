# 饱和修补

::: v-info
新建名为“饱和修补”的附魔。

在进食后，玩家每有1饱和度，执行一次以下操作：

筛选物品栏中有此附魔的耐久不满物品，随机选中一个。

修复此物品15耐久，并扣除玩家1饱和度。
:::

```js
// 在startup_scripts中新建“饱和修补”附魔。
StartupEvents.registry("enchantment", event => {
  event.create("dining")
    .breakable() // 可作用于任何有耐久度的物品。
    .undiscoverable() // 宝藏附魔。
    .maxLevel(1)
    .displayName("饱和修补");
});

// 在server_scripts中监听进食事件。
ItemEvents.foodEaten(event => {
    // 进食后，玩家每剩余1饱和度，执行一次以下事件。
    for (let i = 0; i < Math.floor(event.player.saturation); i++) {
        // 读取进食玩家的物品栏。
        let targets = event.player.inventory.allItems.toArray()
            //筛选物品栏中有“饱和修补”附魔的耐久不满物品。
            .filter(item => item.hasEnchantment("kubejs:dining", 1) && item.damaged);

        let target = targets[Math.floor(targets.length * Math.random())];

        // 如果存在符合条件的物品。
        if (target) {
            // 修复此物品15耐久。
            target.setDamageValue(target.damageValue - 15);
            // 扣除玩家1饱和度。
            event.player.setSaturation(event.player.saturation - 1);
        }
    }
});

// 附魔描述。
{
    "enchantment.kubejs.dining.desc": "用饱和度来修补工具和护甲的耐久值"
}
```