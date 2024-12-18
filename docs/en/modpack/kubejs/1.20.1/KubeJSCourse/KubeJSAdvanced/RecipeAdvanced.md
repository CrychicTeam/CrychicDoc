# 配方合成进阶
本章节所有内容都为KubeJS提供的shapeless和shaped，不适用于其他任何配方，哪怕你能够调用，但是并不会有用

KubeJS只做了自己的适配，并没有适配其他任何合成方式，包括原版的`recipes.minecraft.xxx`

所以这个地方得注意调用的是`shapeless`和`shaped`，或者`recipes.kubejs.shaped`和`recipes.kubejs.shapeless`

关于为什么原版的水桶在合成表里为什么可以返回桶请看 --> [设置物品合成返回物品](./ItemRecipeReturnItem.md)
## 合成附带nbt modifyResult
下面代码是使用工作台里使用钻石剑+附魔书进行合成出带附魔效果的,如果有更高等级的附魔将直接覆盖低等级附魔

这段代码只是示范modifyResult该如何去使用提供的一个示例，具体使用场景可更改
```js
ServerEvents.recipes(event=>{
    event.recipes.kubejs.shaped('minecraft:soul_torch',[
        ['minecraft:torch','minecraft:torch','minecraft:torch'],
        ['minecraft:torch','minecraft:stone','minecraft:torch'],
        ['minecraft:torch','minecraft:torch','minecraft:torch']
    ]).modifyResult((inputItemGrid,outputItem)=>{
        let stone = inputItemGrid.find("stone")
        let items = inputItemGrid.findAll("torch");
        for (let i = 0; i < items.length; i++) {
            if (!items[i].hasEnchantment("looting",2)){
                return "air"
            }
        }
        if (Math.random() < 0.5){
           let ci =  items[0].copy();
           ci.count = 1;
           return ci
        } 
        return outputItem;
    })
})
```
`modifyResult`里的第一个参数**inputItemGrid**为合成台里的物品,第二个参数**outputItem**为输出物品，最后需要返回一个物品为输出物品(return ·ItemStack·)

`inputItemGrid.find(物品id)`是寻找合成台里的物品，返回为 **·ItemStack·**

`·ItemStack·.hasEnchantment(附魔id,等级)`获取物品的所有附魔，返回为 bool

`·ItemStack·.enchant(附魔类型id,等级)`给物品附魔,这里并不会直接改变物品属性所以需要 ***使用变量接收返回参数***

## 消耗物品耐久合成 damageIngredient
下面是使用所有斧子在工作台内和木头进行合成出8个模板的示例
```js
ServerEvents.recipes((event) => {
	event.shapeless(Item.of('minecraft:oak_planks', 8), [
		'minecraft:oak_log', "#minecraft:axes"
	]).damageIngredient({ tag: "#minecraft:axes" }, 5)
})
```
`damageIngredient`里的第一个参数是匹配器，第二个参数为消耗耐久的个数

如果匹配器里填写item代表匹配物品，如果填写tag则代表匹配标签

## 合成返回物品 replaceIngredient
下面的示例是使用瞬间治疗2的药水和龙息进行工作台内进行合成出滞留型治疗药水2并且返回玻璃瓶
```js
ServerEvents.recipes(event => {
    event.shapeless(
        Item.of('minecraft:lingering_potion', '{Potion:"minecraft:strong_healing"}').strongNBT(),
        ['minecraft:dragon_breath', Item.of('minecraft:potion', '{Potion:"minecraft:strong_healing"}').strongNBT()]
    ).replaceIngredient({ item: "minecraft:dragon_breath" }, Item.of('minecraft:glass_bottle'))
})
```

`replaceIngredient`里的第一个参数是匹配器用于匹配被替换的物品，第二个参数为被替换的物品

匹配器的规则同上

## 合成保存物品 keepIngredient
下面的示例中是使用木棍+蜂蜜合成烈焰棒不消耗蜂蜜
```js
ServerEvents.recipes(event => {
    event.shapeless(
        'minecraft:blaze_rod',
        ['minecraft:stick', 'minecraft:honeycomb']
    ).keepIngredient({ item: "minecraft:honeycomb" })
})
```

`keepIngredient`里的参数是匹配器，规则同上