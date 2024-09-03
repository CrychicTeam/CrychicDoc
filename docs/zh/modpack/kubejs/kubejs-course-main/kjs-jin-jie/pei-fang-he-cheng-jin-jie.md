# 配方合成进阶
## 合成附带nbt modifyResult
下面代码是使用工作台里使用钻石剑+附魔书进行合成出带附魔效果的,如果有更高等级的附魔将直接覆盖低等级附魔
```js
ServerEvents.recipes((event) => {
	event.shapeless(Item.of('minecraft:diamond_sword'), [
		Item.of('minecraft:enchanted_book'),
		Item.of('minecraft:diamond_sword')
	]).modifyResult((/**@type {$ModifyRecipeCraftingGrid_}*/grid,/**@type {$ItemStack_} */item) => {
		let itemBook = grid.find("minecraft:enchanted_book")
		let diamond_sword = grid.find("minecraft:diamond_sword")
		let dsEns = diamond_sword.getEnchantments()
		if (dsEns.size() == 0) {
			return diamond_sword.enchant(itemBook.getEnchantments())
		}
		let ibEns = itemBook.getEnchantments()
		let i = 0;
		ibEns.forEach((ikey, ivalue) => {
			dsEns.forEach((key, value) => {
				if (key == ikey) {
					if (value < ivalue) {
						/**@type {$ListTag_} */
						let dimEns = diamond_sword.getNbt().get("Enchantments")
						dimEns.forEach(value => {
							if (value["id"] == key) {
								value["lvl"] = ivalue
							}
						})
					}
				} else {
					i++
				}
				if (i == dsEns.size()) {
					i = 0
					diamond_sword = diamond_sword.enchant(ikey, ivalue)
				}
			})

		})
		return diamond_sword
	})
})
```
`modifyResult`里的第一个参数**grid**为合成台里的物品,第二个参数**item**为输出物品，最后需要返回一个物品为输出物品(return ·ItemStack·)

`grid.find(物品id)`是寻找合成台里的物品，返回为 **·ItemStack·**

`·ItemStack·.getEnchantments()`获取物品的所有附魔，返回为 **·Map\<(string), (integer)\>·**

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
        Item.of('minecraft:lingering_potion', '{Potion:"minecraft:strong_healing"}'),
        ['minecraft:dragon_breath', Item.of('minecraft:potion', '{Potion:"minecraft:strong_healing"}')]
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