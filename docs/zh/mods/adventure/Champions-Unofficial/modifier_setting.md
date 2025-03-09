# 属性修饰符配置 Modifier Setting

阅前须知：你需要知道Minecraft中的[数据包](https://zh.minecraft.wiki/w/%E6%95%B0%E6%8D%AE%E5%8C%85)，知晓部分游戏内容通过数据包加载到游戏中，知晓JSON文本及其语法。

**Champions**通过**数据包**控制一个或多个生物在作为冠军强敌生成时,附加的属性。它在数据包中处于以下位置`data/<namespace>/modifier_setting/<id>.json`，例如`data/champions/modifier_setting/block_reach.json`

以下是一个示例：

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

`attributeType`字段为属性名id。

`conditions`字段为该修饰符应用的条件，`affixes`字段是一个[词缀谓词](#词缀谓词-affixespredicate)，`entity`为生物id列表，`permission`控制entity列表是白名单或是黑名单。

`enable`字段控制该修饰符是否启用。

`modifier`属性修饰符，value为属性修饰符的值，operation为操作符，可选值：ADDITION | MULTIPLY_BASE | MULTIPLY_TOTAL。

>ADDITION 将value的值加到实体的属性值上。
>
>MULTIPLY_BASE 将value的值与实体属性基础值相乘。
>
>MULTIPLY_TOTAL 将value的与实体属性总值相乘。

## 词缀谓词 AffixesPredicate

存在于Modifier Setting中的conditions字段，一些字段是可选的，也就是当你不需要这个字段的功能时，你可以省略掉它。

`values` 一个词缀id列表。

`count` 可选的，表示被测试的实体至少应该具有的词缀数量。

`matches` 可选的，应该至少有多少个词缀通过测试。

::: details matches示例

```js
"matches":{
  "min":1,
  "max":2
}
```

:::

### 词缀谓词示例

```js
"conditions":{
  "values":[

  ],
  "matches":{
    "min":1,
    "max":2
  },
  "count":1
}
```
