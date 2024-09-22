---
authors: ['Gu-meng']
---
# 台阶合成木板
本章主要涉及内容：配方事件里的findRecipes，本章所有代码部分都在`server_scripts`里

## 完整代码
```js
ServerEvents.recipes(event => {
    // 遍历了所有包含`minecraft:slabs`tag的物品
    Ingredient.of('#minecraft:slabs').getItemIds().forEach(slab => {
        // 寻找物品配方类型为工作台并且输出为台阶的配方并使用forEach遍历出来
        event.findRecipes({ type: "minecraft:crafting_shaped", output: slab }).forEach(value => {
            // 判断配方输入物品个数是否不等于3（一般的台阶合成只有3个物品，避免刷物品）
            if (value.getOriginalRecipe().getIngredients().size() != 3) return;
            // 获取到第一个物品存入变量
            let item = value.getOriginalRecipe().getIngredients().get(0).getFirst();
            // 遍历配方输入物品
            value.getOriginalRecipe().getIngredients().forEach(inputItem=>{
                // 判断item遍历是否为null
                if (item == null) return;
                // 判断配方内的物品是否相同 如果不同则给item设置为null
                if (item != inputItem) item = null;
            })
            // 如果循环出来item为null那么就直接结束这次遍历，进入下次遍历
            if (item == null) return;
            event.shaped(item, [slab,slab]);
        })
    })
})
```
注：小示例，代码解释都在注释里，这样看起来也比较方便

## 注意事项
1. 该项目只是作为示例，很多地方并不是最优解，可自行进行解决
2. 代码可能存在的问题：item变量有可能是一个tag,未测试是否有问题请注意
3. 如果对该项目代码部分不满可以将修改好的代码上传至[gitee项目仓库](https://gitee.com/gumengmengs/kubejs-course)
