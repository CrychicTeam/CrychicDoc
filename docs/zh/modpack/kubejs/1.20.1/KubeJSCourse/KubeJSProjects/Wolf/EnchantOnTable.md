---
authors:
  - Gu-meng
  - LitterWolf-fufu
editor: Gu-meng
---
# 工作台物品附魔

```js
ServerEvents.recipes((event) => {
    // 使铁剑可以在工作台上附魔
    enchantCrafting('iron_sword', event)
})
/**  
 * 附魔书附魔物品配方注册
 * @param {$ItemStack_} enchanted_item - 被附魔物品的ItemStack
 * @param {$RecipesEventJS_} event - 配方事件
 */  
const enchantCrafting = (enchanted_item, event) => { 
    event.shapeless(enchanted_item, [enchanted_item, 'minecraft:enchanted_book'])  
        .modifyResult((/**@type {$ModifyRecipeCraftingGrid_}*/grid, /**@type {$ItemStack_} */item) => {
            item = grid.find(enchanted_item)    

            let addition = grid.find('minecraft:enchanted_book').enchantments
            if (addition.isEmpty()) { // 附魔书附魔为空，返回原物品
                return item.withCount(1)
            }

            let origin = item.enchantments
            if (origin.isEmpty()) {  // 原物品附魔为空，返回带有附魔书所有附魔的同物品
                return item.enchant(addition).withCount(1)
            }

            // 二者都不为空
            let combinedOrgin = origin   // 拷贝原物品的附魔作为模板 
            item.enchantments.clear()
            addition.forEach((id, lvl) => { // 遍历附魔书的附魔
                // 检查附魔书和原物品是否有相同的附魔ID
                if (origin.containsKey(id)) {
                    if (lvl > origin.get(id)) {
                        // 附魔书的附魔等级高，附魔书的附魔替换原有附魔
                        combinedOrgin.replace(id, lvl)
                    }
                } else {
                    // 被附魔物品不存在相同ID附魔，附魔书的附魔合并到被附魔物品的附魔中
                    combinedOrgin.put(id, lvl)
                }
            })
            item.nbt.remove('Enchantments')
            return item.enchant(combinedOrgin).withCount(1)
        })
}
```
