---
Layout: doc
title: 配置
backPath: ../
prev:
    text: Wiki
    link: /zh/mods/adventure/Champions-Unofficial/wiki
next:
    text: 介绍
    link: /zh/mods/adventure/Champions-Unofficial/index
authors:
    - M1hono
---

# Champions 模组配置指南

Champions 模组通过增加精英怪物来增强 Minecraft 的挑战性。以下是 `config/` 文件夹下的配置文件的详细说明，帮助您正确配置游戏。

## HUD 显示设置

```toml
[hud]
# HUD的X轴偏移量
# 范围：-1000 ~ 1000
hudXOffset = 0

# HUD的Y轴偏移量
# 范围：-1000 ~ 1000
hudYOffset = 0

# 可以看到champion HUD的距离（方块）
# 范围：0 ~ 1000
hudRange = 50

# 如果设置为true，将WAILA覆盖层移动到champion HUD下方
enableWailaIntegration = true
```

这些设置用于调整游戏中的HUD（头上显示）的位置和可见性。

## 通用配置

```toml
[general]
# 激活的信标生效的范围（设置为0则禁用此功能）
# 控制信标周围不生成champions的范围
# 范围：0 ~ 1000
beaconProtectionRange = 64

# 设置为true以允许从刷怪笼生成champions
championSpawners = false

# 设置显示击杀champions的死亡播报的最低等级（设置为0则禁用此功能）
# 范围：> 0
deathMessageTier = 0

# 黑名单/白名单中的维度名称列表
dimensionList = []

# 设置维度列表是黑名单还是白名单
# 允许的值：BLACKLIST, WHITELIST
dimensionPermission = "BLACKLIST"

# 黑名单/白名单中的实体名称列表
entitiesList = []

# 设置实体列表是黑名单还是白名单
# 允许的值：BLACKLIST, WHITELIST
entitiesPermission = "BLACKLIST"

# 设置为true以显示champions的HUD，包括健康、词缀和等级
showHud = true

# 设置实体ID（例如，['minecraft:end_dragon', 'minecraft:creeper']）以隐藏champions的HUD显示，包括健康、词缀和等级
bossBarBlackList = []

# 设置为true以使champions生成表示其等级的彩色粒子效果
showParticles = true

# 设置为true以在The One Probe覆盖中显示champion等级和词缀
enableTOPIntegration = true
```

这些通用配置选项允许您自定义游戏中的精英怪物的行为和显示方式，以适应不同玩家的需求。

通过正确配置这些选项，您可以使游戏体验更加丰富和个性化。
