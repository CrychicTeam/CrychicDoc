---
authors: ['Gu-meng']
---
# 获取所有配方json并保存为文件
本章主要涉及内容：获取所有配方、JsonIO，本章所有代码部分都在`server_scripts`里

涉及模组及版本:
1. jei-1.20.1-forge-15.3.0.4
2. rhino-forge-2001.2.2-build.18
3. architectury-9.2.14-forge
4. kubejs-forge-2001.6.5-build.14
5. probejs-6.0.1-forge

## 完整代码
这里得提前在对应的游戏文件路径下创建`recipes`文件夹，不然会报错找不到路径
```js
ServerEvents.recipes(event=>{
    event.forEachRecipe({},recipe=>{
        JsonIO.write("./recipes/" + String(recipe.getId()).replace(/:|\//g, '_') + ".json",recipe.json)
    })
})
```