# 词缀配置 Affix Setting

阅前须知：你需要知道Minecraft中的[数据包](https://zh.minecraft.wiki/w/%E6%95%B0%E6%8D%AE%E5%8C%85)，知晓部分游戏内容通过数据包加载到游戏中，知晓JSON文本及其语法。

**Champions**通过**数据包**控制各个词缀允许拥有词缀的生物，词缀等级范围。它在数据包中处于以下位置`data/<namespace>/affix_setting/<id>.json`，例如`data/champions/affix_setting/adaptable.json`

下面是一个示例：

```json
{
  "type": "champions:adaptable",
  "category": "defence",
  "enable": true,
  "hasSub": false,
  "mobList": [
    "minecraft:pig",
    "minecraft:creeper"
  ],
  "mobPermission": "blacklist",
  "tier": {
    "max": 100,
    "min": 1
  }
}
```

- `enable`字段控制该词缀是否启用，true启用，false关闭。

- `mobList`字段控制与该词缀相关的生物列表，包含一系列生物id。

- `mobPermission`字段控制**mobList**的列表是黑名单或是白名单，当处于黑名单(blacklist)，mobList列表中的生物不会被附加该词缀；当处于白名单(whitelist)，只有mobList列表中的生物可附加该词缀。

- `tier`字段控制该词缀出现时的最大最小等级，max为最大，min为最小。
