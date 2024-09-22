---
authors: ['Gu-meng']
---
# 战利品上下文
## this
战利品生成的本身实体

## killer
间接导致实体死亡或受伤的实体，如： 玩家使用弓箭、三叉戟投掷或投掷瞬间伤害药水击杀了生物，那么玩家则为killer

## direct_killer
直接导致实体死亡或受伤的实体，如： 玩家使用剑或者三叉戟直接攻击生物，那么玩家会被判定为direct_killer,

如果是弓射出去的箭击杀的生物，那么箭就会判定为direct_killer

如果是投掷出去的三叉戟，那么三叉戟就会被判定为direct_killer

如果投掷瞬间伤害药水，那么瞬间伤害药水被判定为direct_killer

## killer_player
直接导致实体死亡或受伤的为玩家