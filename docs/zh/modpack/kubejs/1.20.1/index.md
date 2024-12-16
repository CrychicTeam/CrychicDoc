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


root:
  backLink: "../"
  title: KubeJS
  collapsed: false
  children:
    - title: 基础
      path: introduction
      collapsed: false
      children:
        - title: 介绍
          path: /
          file: Description
        - title: 物品
          path: item
          collapsed: true
          children:
            # - title: 目录
            #   path: /
            #   file: Catalogue
            - title: 注册
              path: register
              collapsed: true
              children:
                - title: 注册物品
                  path: /
                  file: Register
                - title: 物品属性
                  path: /
                  file: ItemProperty
                # - title: 物品类型
                #   path: /
                #   file: ItemType
                - title: 资源文件
                  path: /
                  file: Resource
        - title: 方块
          path: block
          collapsed: true
          children:
            # - title: 目录
            #   path: /
            #   file: Catalogue
            - title: 注册
              path: register
              collapsed: true
              children:
                - title: 注册
                  path: /
                  file: Register
        - title: 实体
          path: entity
          collapsed: true
          children:
            # - title: 目录
            #   path: /
            #   file: Catalogue
            - title: 生成
              path: /
              file: Spawn
            - title: 状态效果
              path: /
              file: PotionEffects
            - title: 属性
              path: /
              file: Attribute
        - title: 状态效果
          path: potionEffect
          collapsed: true
          children:
            - title: 状态效果
              path: /
              file: PotionEffect
              collapsed: true
        - title: 配方
          path: recipe
          collapsed: true
          children:
            # - title: 目录
            #   path: /
            #   file: Catalogue
            - title: 修改配方
              path: /
              file: ModifyRecipe
            - title: 删除配方
              path: /
              file: DeleteRecipe
            - title: 添加配方
              path: addRecipe
              collapsed: true
              children:
                - title: 原版配方
                  path: Vanilla
                  collapsed: true
                  children:
                    - title: 工作台
                      path: /
                      file: CraftingTable
                    - title: 熔炉
                      path: /
                      file: Furnace
                    - title: 高炉
                      path: /
                      file: BlastFurnace
                    - title: 烟熏炉
                      path: /
                      file: Smoker
                    - title: 营火
                      path: /
                      file: Campfire
                    - title: 锻造台
                      path: /
                      file: SmithingTable
                    - title: 切石机
                      path: /
                      file: Stonecutter
        - title: 战利品表
          path: lootTable
          collapsed: true
          children:
            # - title: 目录
            #   path: /
            #   file: Catalogue
            - title: 介绍
              path: /
              file: Description
            - title: 基础知识
              path: basicKnowledge
              children:
                - title: 战利品表类型
                  path: /
                  file: LootType
                - title: 随机池
                  path: /
                  file: LootPool
                - title: 抽取项
                  path: /
                  file: LootEntry
                - title: 谓词
                  path: /
                  file: Predicate
                - title: 物品修饰器
                  path: /
                  file: ItemModifier
            - title: 原版战利品表类型
              path: vanilla
              children:
                - title: 方块战利品表
                  path: /
                  file: Block
                - title: 实体战利品表
                  path: /
                  file: Entity
                - title: 钓鱼战利品表
                  path: /
                  file: Fish
                - title: 礼物战利品表
                  path: /
                  file: Gift
                - title: 箱子战利品表
                  path: /
                  file: Chest
                - title: 通用战利品表
                  path: /
                  file: Generic
        - title: 标签
          path: tag
          collapsed: true
          children:
            # - title: 目录
            #   path: /
            #   file: Catalogue
            - title: 介绍
              path: /
              file: Description
            - title: 原版标签类型
              path: /
              children:
                - title: 物品标签
                  path: /
                  file: Item
                - title: 方块标签
                  path: /
                  file: Block
                - title: 生物类型标签
                  path: /
                  file: EntityType
                - title: 流体标签
                  path: /
                  file: Fluid
                - title: 生物群系标签
                  path: /
                  file: Biome
                - title: 结构标签
                  path: /
                  file: Structure
        - title: 事件
          path: event
          collapsed: true
          children:
            - title: 服务端脚本
              path: serverScript
              collapsed: true
              children:
                - title: 事件列表
                  path: /
                  file: EventList
                - title: 事件示例
                  path: example
                  collapsed: true
                  children:
                    - title: 命令注册事件
                      path: /
                      file: CommandRegistry
                    - title: 实体交互事件
                      path: /
                      file: EntityInteracted
            - title: 客户端脚本
              path: clientScript
              collapsed: true
              children:
                - title: 事件列表
                  path: /
                  file: EventList
            - title: 启动脚本
              path: startupScript
              collapsed: true
              children:
                - title: 事件列表
                  path: /
                  file: EventList
        - title: 附属
          path: addon
          collapsed: true
          children:
            - title: LootJs
              path: LootJs
              collapsed: true
              children:
                - title: LootJs
                  path: /
                  file: LootJs
                - title: Conditions
                  path: /
                  file: Conditions  
                - title: LootEntry
                  path: /
                  file: LootEntry
                - title: Event
                  path: /
                  file: Event
                - title: Functions
                  path: /
                  file: Functions
                - title: Type
                  path: /
                  file: Type
            - title: ProbeJS
              path: ProbeJS
              collapsed: true
              children:
                - title: 使用方法
                  path: /
                  file: ProbeJS
                - title: JSDoc
                  path: /
                  file: JSDoc
                - title: ProbeJS类型文件
                  path: /
                  file: ProbeJSClassFlie
        - title: 全局范围
          path: globalScope
          collapsed: true
          children:
            - title: 简述
              path: /
              file: Description
            - title: 实用工具
              path: /
              file: Utils
            - title: 类
              path: classes
              collapsed: true
              children:
                - title: ResourceLocation
                  path: /
                  file: resourceLocation
        - title: 网络通信
          path: network
          collapsed: true
          children:
            - title: 简述
              path: /
              file: Description
              collapsed: true
        - title: 杂项知识
          path: miscellaneous
          collapsed: true
          children:
            - title: 介绍
              path: /
              file:  Description
            - title: Collection
              path: /
              file:  Collection
            - title: CompletableFuture
              path: /
              file:  CompletableFuture
            - title: Components
              path: /
              file:  Components
            - title: CountingMap
              path: /
              file:  CountingMap
            - title: Ingredient
              path: /
              file:  Ingredient
            - title: IngredientActionFilter
              path: /
              file:  IngredientActionFilter
            - title: ItemStack
              path: /
              file:  ItemStack
            - title: Lazy
              path: /
              file:  Lazy
            - title: Map
              path: /
              file:  Map
            - title: NumberProvider
              path: /
              file:  NumberProvider
            - title: ParticleOptions
              path: /
              file:  ParticleOptions
            - title: Pattern
              path: /
              file:  Pattern
            - title: Providers
              path: /
              file:  Providers  
            - title: Random
              path: /
              file:  Random
            - title: RecipeFilter
              path: /
              file:  RecipeFilter
            - title: RegistryInfo
              path: /
              file:  RegistryInfo 
            - title: Runnable
              path: /
              file:  Runnable   
            - title: SoundEvent
              path: /
              file:  SoundEvent
            - title: Stat
              path: /
              file:  Stat  
            - title: Supplier
              path: /
              file:  Supplier
    # - title: 进阶
    #   path: Upgrade
    #   collapsed: false
    #   children:
    #     - title: 全局范围
    #       path: GlobalScope
    #       collapsed: true
    #       children:
    #         - title: 类
    #           path: Classes
    #           collapsed: true
    #           noScan: false
    - title: 代码分享
      path: codeshare
      collapsed: true
      children:
      - title: PainterAPI血条
        path: /
        file: Painter
      - title: 自定义方块打开工作台界面
        path: /
        file: CraftingTableGUI
      - title: 死亡扣除生命上限
        path: /
        file: LifeValue
      - title: 反向骨粉
        path: /
        file: InvertedBonemeal
      - title: 饱和修补
        path: /
        file: Dining
prev: false
next: false
---
