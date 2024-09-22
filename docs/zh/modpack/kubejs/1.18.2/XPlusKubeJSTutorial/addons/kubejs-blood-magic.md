---
authors: ['Wudji']
---

# 12.2 KubeJS Blood Magic

***

```
支持的格式：
- 祭坛
event.recipes.bloodmagic.altar(output, input)
event.recipes.bloodmagic.altar(output, input).upgradeLevel(int).altarSyphon(int).consumptionRate(int).drainRate(int)
- 炼金矩阵
event.recipes.bloodmagic.array(output, baseInput, addedInput)
event.recipes.bloodmagic.array(output, baseInput, addedInput).texture(string)
- 狱火熔炉
event.recipes.bloodmagic.soulforge(output, [input])
event.recipes.bloodmagic.soulforge(output, [input]).minimumDrain(double).drain(double)
- 奥术粉灰
event.recipes.bloodmagic.arc(output, input, tool)
event.recipes.bloodmagic.arc(output, input, tool, [addedOutput])
event.recipes.bloodmagic.arc(output, input, tool, [addedOutput]).consumeIngredient(boolean).outputFluid(fluid)
- 炼金术桌
event.recipes.bloodmagic.alchemytable(output, [input])
event.recipes.bloodmagic.alchemytable(output, [input]).syphon(int).ticks(int).upgradeLevel(int)


例：
onEvent('recipes', event => {
  const { altar, array, soulforge, arc, alchemytable } = event.recipes.bloodmagic
  altar('minecraft:carrot', 'minecraft:apple')
  array('minecraft:spruce_planks', 'minecraft:oak_planks', 'minecraft:birch_planks')
  soulforge('minecraft:stone', ['minecraft:gold_ore', 'minecraft:diamond_ore', 'minecraft:iron_ore']).drain(1.0)
  arc('minecraft:netherite_ingot', 'minecraft:iron_ingot', 'minecraft:iron_pickaxe', [Item.of('minecraft:cobblestone').chance(0.4)])
  alchemytable('minecraft:gold_ingot', ['minecraft:iron_ingot', 'minecraft:iron_ingot', 'minecraft:iron_ingot', 'minecraft:iron_ingot']).upgradeLevel(2)
})
```
