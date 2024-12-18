# 锻造台

## 前言

- 锻造台相关的配方类型。

- 需在ServerEvents.recipes事件中使用。

## 配方类型

### 升级

- 例如使钻石装备升级为下界合金装备。

- 语句：event.recipes.minecraft.smithing_transform（输出物品栈, 锻造物品栈, 原料）。

- 例子：下界合金升级锻造模板（默认就有的） + 生铁 + 木炭 -> 铁锭。

```js
ServerEvents.recipes(event => {
    event.recipes.minecraft.smithing_transform(
        // 输出铁锭
        'minecraft:iron_ingot',
        // 输入生铁
        'minecraft:raw_iron',
        // 添加木炭
        'minecraft:charcoal'
    )
})
```

### 纹饰

- 给盔甲附着纹饰。

- 语句：event.recipes.minecraft.smithing_trim(锻造模板, 盔甲物品栈, 原料)。

- 例子：暂无示例，正在研究。

```js
ServerEvents.recipes(event => {
    // event.recipes.minecraft.smithing_trim();
})

```

## 配方修饰

### 替换升级模板

- 升级，但更改默认的下界合金锻造模板为其他物品栈。

- 语句：event.recipes.minecraft.smithing_transform(输出物品栈, 输入物品栈, 添加物品栈).template(替换模板的物品栈)

- 例子：铁锭（占据模板的位置） + 生铁 + 木炭 => 铁锭。

```js
// 换行为了是避免太长看的难受，可以不换行
ServerEvents.recipes(event => {
    event.recipes.minecraft.smithing_transform(
        'minecraft:iron_ingot',
        'minecraft:raw_iron',
        'minecraft:charcoal'
    ).template('minecraft:iron_ingot');
    // 如果替换为'minecraft:air'会在JEI中显示不出输出物品，其他物品管理器模组未测试，但配方正常。
})
```
