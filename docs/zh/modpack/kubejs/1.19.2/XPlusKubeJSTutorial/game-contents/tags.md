# 3、标签

## 一、标签事件

### 1、事件监听

你可以使用`ServerEvents.tags`事件来进行与标签有关的修改，需要注意的是你需要向其传入参数来设置其修改的类型，即：

```js
ServerEvents.tags("标签类型", event => {
    // 脚本
})
```

绝大多数情况下你只需要使用类型`item`、 `block` 和 `fluid`，但其还支持由其他模组注册的类型。

### 2、事件方法

以下为标签事件支持的方法：

| 方法                                            | 描述                                   | 返回值     |
| ----------------------------------------------- | -------------------------------------- | ---------- |
| add(ResourceLocation 标签, String... 对象ID)    | 向给定标签中添加对象                   | TagWrapper |
| remove(ResourceLocation 标签, String... 对象ID) | 从给定标签中移除对象                   | TagWrapper |
| removeAll(ResourceLocation 标签)                | 移除给定标签中的所有对象               | TagWrapper |
| removeAllTagsFrom(String... 对象ID)             | 移除包含给定对象的所有标签中的所有对象 | TagWrapper |
| get(ResourceLocation 标签)                      | 获取标签中所有对象                     | TagWrapper |

## 二、示例

```js
// 监听标签事件
ServerEvents.tags('item', event => {
  // 将 minecraft:diamond_ore 添加至 #forge:cobblestone
  event.add('forge:cobblestone', 'minecraft:diamond_ore')
  
  // 从 #forge:cobblestone 中移除 minecraft:mossy_cobblestone
  event.remove('forge:cobblestone', 'minecraft:mossy_cobblestone')
  
  // 移除 #forge:ingots/copper 中的所有对象
  event.removeAll('forge:ingots/copper')
  
  // 用于 FTB任务 检测物品NBT
  event.add('itemfilters:check_nbt', 'some_item:that_has_nbt_types')
  
  // 自定义标签（#forge:completely_new_tag）
  event.add('forge:completely_new_tag', 'minecraft:clay_ball')
  
  // 移除包含 minecraft:stick 的所有标签中的所有对象
  event.removeAllTagsFrom('minecraft:stick')
  
  // 将 #forge:stone 中所有非闪长岩对象添加到 #c:stone 中
  const stones = event.get('forge:stone').getObjectIds()
  const blacklist = Ingredient.of(/.*diorite.*/)
  stones.forEach(stone => {
    if (!blacklist.test(stone)) {
      event.add('c:stone', stone)
    }
  })
})
```

另请参阅：[TagWrapper (github.com)](https://github.com/KubeJS-Mods/KubeJS/blob/3a446ee3127181a2e6492cd3d78021f034c91ec6/common/src/main/java/dev/latvian/mods/kubejs/server/TagEventJS.java#L35)