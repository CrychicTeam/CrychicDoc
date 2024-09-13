---
layout: doc
# back: ../
# sidebarfolder: true
# prev: false
# next: false
# sidebarorder:
#   Recipe: 1
#   ItemRegister: 2
#   ItemInteraction: 3
#   BlockRegister: 4
#   BlockInteraction: 5
#   EntityInteraction: 6
#   WorldGeneration: 7
#   NetworkInteraction: 8

noguide: true
root:
  backLink: "../"
  title: KubeJS
  collapsed: false
  children:
      - title: 入门
        path: Introduction
        collapsed: false
        children:
          - title: 目录
            path: /
            file: Catalogue
          - title: 配方
            path: Recipe
            collapsed: true
            children:
              - title: 物品与原料表示
                path: /
                file: ItemAndIngredient
              - title: 配方过滤器表示
                path: /
                file: RecipeFilter
              - title: 修改配方
                path: /
                file: ModifyRecipe
              - title: 删除配方
                path: /
                file: DeleteRecipe
              - title: 添加原版配方
                path: /
                file: Vanilla
          - title: 药水效果
            path: PotionEffect
            children:
              - title: 药水效果
                path: /
                file: PotionEffect
            collapsed: true
          - title: 实体
            path: Entity
            collapsed: true
            children:
              - title: 生成
                path: /
                file: Spawn
              - title: 药水效果
                path: /
                file: PotionEffects
              - title: 属性
                path: /
                file: Attribute
          - title: 标签
            path: Tag
            collapsed: true
            children:
              - title: 标签
                path: /
                file: Tag
              - title: 物品标签
                path: /
                file: Item
              - title: 方块标签
                path: /
                file: Block
              - title: 流体标签
                path: /
                file: Fluid
              - title: 生物群系标签
                path: /
                file: Biome
              - title: 结构标签
                path: /
                file: Structure
          - title: 战利品
            path: LootTable
            collapsed: true
            children:
              # - title: 战利品
              #   path: /
              #   file: LootTable
              # - title: 战利品表示
              #   path: /
              #   file: LootEntry
              # - title: 战利品修改
              #   path: /
              #   file: ModifyLootEntry
              # - title: 战利品添加
              #   path: /
              #   file: AddLootEntry
              - title: 方块
                path: /
                file: Block
              - title: 实体
                path: /
                file: Entity
              - title: 钓鱼
                path: /
                file: Fish
              - title: 礼物
                path: /
                file: Gift
              - title: 箱子
                path: /
                file: Chest
          - title: 事件
            path: Event
            collapsed: true
            children:
              - title: 服务器事件
                path: Server
                collapsed: true
                children:
                  - title: 服务器事件
                    path: /
                    file: Server
                  - title: 实体交互事件
                    path: /
                    file: EntityInteracted
              - title: 客户端侧事件
                path: Client
                collapsed: true
                children:
                  - title: 客户端事件
                    path: /
                    file: Client
              - title: 启动事件
                path: Startup
                collapsed: true
                children:
                  - title: 启动事件
                    path: /
                    file: Startup
          - title: 附属
            path: Addon
            collapsed: true
            children:
              - title: LootJs
                path: LootJs
                collapsed: true
                children:
                - title: LootJs
                  path: /
                  file: LootJs
          - title: 杂项知识
            path: MiscellaneousKnowledge
            collapsed: true
            children:
              - title: 杂项知识
                path: /
                file: MiscellaneousKnowledge
              - title: 数字提供器
                path: /
                file: NumberProvider
      - title: 进阶
        path: Upgrade
        collapsed: false
        children:
          - title: 全局范围
            path: GlobalScope
            collapsed: true
            children:
              - title: 类
                path: Classes
                collapsed: true
                children:
                  - title: 目录
                    path: /
                    file: Catalogue
                  - title: 轴对齐包围盒（AABB）
                    path: /
                    file: AABB
                  - title: 方块（Block）
                    path: /
                    file: Block
                  - title: 方块状态谓词（BlockStatePredicate）
                    path: /
                    file: BlockStatePredicate
                  - title: 颜色（Color）
                    path: /
                    file: Color
                  - title: 方向（Direction）
                    path: /
                    file: Direction
                  - title: 面（Facing）
                    path: /
                    file: Facing
                  - title: 流体（Fluid）
                    path: /
                    file: Fluid
                  - title: 原料（Ingredient）
                    path: /
                    file: Ingredient
                  - title: 原料助手（IngredientHelper）
                    path: /
                    file: IngredientHelper
                  - title: 物品（tem）
                    path: /
                    file: Item
                  - title: Json读写（JsonIO）
                    path: /
                    file: JsonIO
                  - title: NBT（NBT）
                    path: /
                    file: NBT
                  - title: NBT读写（NBTIO）
                    path: /
                    file: NBTIO
                  - title: 平台（Platform）
                    path: /
                    file: Platform
                  - title: 资源位置（ResourceLocation）
                    path: /
                    file: ResourceLocation
                  - title: 文本（Text）
                    path: /
                    file: Text
                  - title: 通用唯一标识符（Uuid）
                    path: /
                    file: Uuid
                  - title: 实用工具（Utils）
                    path: /
                    file: Utils
              - title: 常量与对象
                path: ConstantsAndObjects
                collapsed: true
                file: Catalogue
              - title: 包装类
                path: WrappedClasses
                collapsed: true
                file: Catalogue
      # - title: 高级
      #   path: Advanced
      #   collapsed: true
      #   children:
      #     - title: 占位符
      #       path: /
      #       file: placeholder     
      # - title: kubejs教程-1.20.1(孤梦版)
      #   noScan: true
      #   path: KubejsCourse
      #   file: README
prev: false
next: false
---