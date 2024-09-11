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
      # - title: 高级
      #   path: Advanced
      #   collapsed: true
      #   children:
      #     - title: 占位符
      #       path: /
      #       file: placeholder     
      - title: kubejs教程-1.20.1(孤梦版)
        noScan: true
        path: KubejsCourse
        file: README
prev: false
next: false
---