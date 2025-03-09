# 属性修饰符配置 Modifier Setting

阅前须知：你需要知道Minecraft中的[数据包](https://zh.minecraft.wiki/w/%E6%95%B0%E6%8D%AE%E5%8C%85)，知晓部分游戏内容通过数据包加载到游戏中，知晓JSON文本及其语法。

**Champions**通过**数据包**控制一个或多个生物在作为冠军强敌生成时,附加的属性。它在数据包中处于以下位置`data/<namespace>/modifier_setting/<id>.json`，例如`data/champions/modifier_setting/block_reach.json`

下面是一个示例：

```json
{
  "attributeType": "forge:block_reach",
  "conditions": {
    "affixes": {
      "values": []
    },
    "entity": [
      "minecraft:creeper"
    ],
    "permission": "blacklist"
  },
  "enable": false,
  "modifier": {
    "operation": "ADDITION",
    "value": 0.0
  }
}
```

- `attributeType`字段为属性名id

- `conditions`字段为该修饰符应用的条件，affixes字段具有一个词缀id列表，entity为生物id列表

- `enable`字段控制该修饰符是否启用

- `modifier`属性修饰符，value为属性修饰符的值，operation为操作符，可选值：ADDITION | MULTIPLY_BASE | MULTIPLY_TOTAL

>ADDITION 将value的值加到实体的属性值上。
>
>MULTIPLY_BASE 将value的值与实体属性基础值相乘。
>
>MULTIPLY_TOTAL 将value的与实体属性总值相乘。
