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
  backLink: "../../"
  title: KubeJS
  collapsed: false
  children:
    - title: '1.21'
      path: '1.21'
      collapsed: true
    - title: '1.20.1'
      path: '1.20.1'
      collapsed: true
      children:
        - title: Kubejs教程-1.20.1(Crychic版)
          path: Crychic
          collapsed: true
          children:
            - title: 入门
              path: Introduction
              collapsed: true
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
                    - title: 目录
                      path: /
                      file: Catalogue
                  collapsed: true
                - title: 实体
                  path: Entity
                  collapsed: true
                  children:
                    - title: 为实体添加药水效果
                      path: /
                      file: AddPotionEffects
            - title: 进阶
              path: Upgrade
              collapsed: true
              children:
                - title: 占位符
                  path: /
                  file: placeholder
            - title: 高级
              path: Advanced
              collapsed: true
              children:
                - title: 占位符
                  path: /
                  file: placeholder     
        - title: kubejs教程-1.20.1(孤梦版)
          noScan: true
          path: KubejsCourse
          file: README
    - title: 入门-开箱急用
      path: Introduction
      collapsed: true
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
            - title: 目录
              path: /
              file: Catalogue
          collapsed: true
        - title: 实体
          path: Entity
          collapsed: true
          children:
            - title: 为实体添加药水效果
              path: /
              file: AddPotionEffects
    - title: 进阶-究其物理
      path: Upgrade
      children:
        - title: 占位符
          path: /
          file: placeholder
      collapsed: true
    - title: 配方
      path: Recipe
      collapsed: true
    - title: 物品注册
      path: ItemRegister
      collapsed: true
    - title: 物品交互
      path: ItemInteraction
      collapsed: true
    - title: 方块注册
      path: BlockRegister
      collapsed: true
    - title: 方块交互
      path: BlockInteraction
      collapsed: true
    - title: 实体交互
      path: EntityInteraction
      collapsed: true
    - title: 世界生成
      path: WorldGeneration
      collapsed: true
    - title: Network通信
      path: NetworkInteration
      collapsed: true
    - title: 全局范围
      path: GlobalScope
      collapsed: true
    - title: JavaScript相关概念
      path: JavaScript
      collapsed: true
    - title: kubejs教程-1.20.1(孤梦版)
      noScan: true
      path: KubejsCourse
      file: README
    - title: '1.19.2'
      path: '1.19.2'
      collapsed: true
    - title: '1.18.2'
      path: '1.19.2'
      collapsed: true
---