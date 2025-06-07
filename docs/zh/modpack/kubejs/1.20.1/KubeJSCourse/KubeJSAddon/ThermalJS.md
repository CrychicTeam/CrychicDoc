---
authors:
  - Gu-meng
editor: Gu-meng
---
## 热力膨胀和KubeJs 
在本章中将介绍如何使用KubeJS去修改热力膨胀模组的配方

### 配方类型表格(不代表全部,仅仅是常用的,带有`[]`说明可以写入多个输入或输出或是数组)
**这个Mod的开发多多少少带点神经病**
**那么多配方类型游戏里面是没几个能看到的**
**有的还甚至没写完,有的类型有`ingredients`没`results`**

大部分配方后带`.energy(int)`方法可以配置配方所需的能量消耗(默认`2000`)
|                           配方格式                            |   配方类型   | 补充说明 |
| :-----------------------------------------------------------: | :----------: | :------: |
|               `thermal.press(output[], input)`                |  多驱冲压机  |    -     |
|            `thermal.bottler(output, hive(block))`             |  流体罐装机  |    -     |
|         `thermal.hive_extractor(output, hive(block))`         |   蜂箱漏斗   | 蜂箱漏斗 |
| `thermal.tree_extractor(output, trunk(block), leaves(block))` |  树汁提取器  | 贴着树干 |
|               `thermal.furnace(output, input)`                |    红石炉    |    -     |
|          `thermal.smelter_recycle(output, input[])`           |    感应炉    |    -     |
|        `thermal.pulverizer_catalyst(output[], input)`         |    磨粉机    |    -     |
|            `thermal.crystallizer(output, input[])`            |    结晶器    |    -     |
|               `thermal.crucible(output, input)`               |    熔岩炉    |    -     |
|              `thermal.chiller(output, input[])`               |  极速冷冻机  |    -     |
|            `thermal.insolator(output[], input[])`             |  有机灌注机  |    -     |
|               `thermal.brewer(output, input[])`               |  药水酿造机  |    -     |
|              `thermal.refinery(output, input[])`              |  流体精炼机  |    -     |
|             `thermal.centrifuge(output[], input)`             |  离心分离机  |    -     |
|              `thermal.sawmill(output[], input)`               |    锯木机    |    -     |
|   `thermal.rock_gen(output, below(block), adjacent(block))`   |    造石机    |    -     |
|           `thermal.numismatic_fuel(input, energy)`            |  通货能源炉  |  发电机  |
|            `thermal.gourmand_fuel(input, energy)`             |  饕餮能源炉  |  发电机  |
|            `thermal.magmatic_fuel(input, energy)`             |  热力能源炉  |  发电机  |
|            `thermal.lapidary_fuel(input, energy)`             |  珠宝能源炉  |  发电机  |
|            `thermal.stirling_fuel(input, energy)`             | 斯特林能源炉 |  发电机  |
|         `thermal.disenchantment_fuel(input, energy)`          |  祛魔能源炉  |  发电机  |
|           `thermal.compression_fuel(input, energy)`           |  压缩能源炉  |  发电机  |
