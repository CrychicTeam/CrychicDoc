---
Layout: doc
title: Wiki
order: 1
authors:
  - M1hono
prev:
  text: 介绍
  link: /zh/mods/adventure/Champions-Unofficial/index
next:
  text: 配置
  link: /zh/mods/adventure/Champions-Unofficial/config
---

## 冠军再续介绍

**Champions** 是一为提升游玩难度而设计的模组，为游戏添加了不同稀有度的精英怪，每个敌对生物在诞生时都有概率被赋予等级并因而成为精英怪（冠军）。这些精英怪不仅具有更强的属性，还拥有额外的能力，并且会掉落符合其实力的战利品。

该模组的配置文件提供了丰富的条件与参数，玩家可以通过配置文件轻松调整模组的难度和多样性，从而根据自己的游戏风格进行个性化设置。

> **冠军再续** 基本继承了该模组的所有设定，并计划进行更深入的优化与增强。

<ClientOnly>
<DamageChart 
  :incomingDamage="50"
  :armorToughness="0"
  :minDamage="0"
  :maxDamage="60"
  :maxArmorPoints="100"
  :isJavaEdition="true"
/>
</ClientOnly>

由于敌对生物的数值在冒险包中是较为重要的要素，因此在此提供一个伤害随护甲值与护甲韧性的动态变化图表，以供魔改者参考。

你可以随意更改上述的数值来观察不同程度的攻击在面对不同护甲值的敌人时所能造成的伤害。

## 计算公式

为了帮助魔改者计算大致的数值并使用上面的图表进行计算，这里提供冠军模组数值的计算公式。

以下公式适用于生命值、攻击力、护甲值、护甲韧性和防击退属性的增长：

$$
\text{属性} = \text{原始属性} \times (1 + \text{growthFactor} \times \text{growth.health})
$$

### 示例

假设原始生命值为 `100`，`growthFactor`[^1] 为 `15`，`growth.health`[^2] 默认为 `2`，则计算后的生命值为：

$$
\text{生命值} = 100 \times (1 + 15 \times 2) = 100 \times 31 = 3100
$$

### 可应用的五种属性

- **生命值 (Max Health)**
- **攻击力 (Attack Damage)**
- **护甲值 (Armor)**
- **护甲韧性 (Armor Toughness)**
- **防击退属性 (Knockback Resistance)**

## 词条列表

1. **适应（Adaptable）** - 受到相同伤害类型的连续攻击造成的伤害越来越少；

2. **极寒（Arctic）** - 持续发射追踪冰弹，使被击中的人减速；

3. **灰烬（Enkindling）** - 持续发射追踪火球，造成爆炸与火焰效果；

4. **抑制（Dampening）** - 减少间接攻击的伤害；（例如箭矢）

5. **亵渎（Desecrating）** - 在目标下方生成伤害云持续造成伤害；

6. **急速（Hasty）** - 大幅提高移动速度；

7. **感染（Infested）** - 受到攻击会生成蠹虫攻击目标；

8. **瘫痪（Paralyzing）** - 攻击时有概率使目标在几秒钟内无法移动；

9. **爆震（Knocking）** - 攻击击退能力增加；

10. **活力（Lively）** - 每秒恢复 1 点生命值。无攻击或追击目标时，每秒恢复 5 点生命值；（恢复量可配置）

11. **熔融（Molten）** - 增强攻击力和护甲穿透力；

12. **瘟疫（Plagued）** - 感染附近生物，增加凋零效果，使他们来凋零你；（药水效果可配置）

13. **反射（Reflective）** - 将一小部分伤害反射；

14. **创伤（Wounding）** - 攻击将有一定机率造成致残效果，这会降低目标的治疗效果并增加其受到的伤害；

15. **防护（Shielding）** - 每隔一段时间获得伤害免疫效果；

16. **磁力（Magnetic）** - 每隔一段时间将目标拉向自己；

17. **监禁（Jailer）** - 每次攻击将会有几率监禁目标。

> **此处文字引用自** [MC百科](https://www.mcmod.cn/class/2130.html)

[^1]: 由 `champions-ranks.toml` 定义的属性，用于表示等级影响的增长因子。

[^2]: 由 `champions-server.toml` 定义的属性，默认值为 2，代表对应属性的增长系数。
