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
      - title: Getting Started
        path: Introduction
        collapsed: true
        children:
          - title: Table of Contents
            path: /
            file: Catalogue
          - title: Item and Ingredient Representation
            path: /
            file: ItemAndIngredient
          - title: Recipe Filter Representation
            path: /
            file: RecipeFilter
          - title: Modifying Recipes
            path: /
            file: ModifyRecipe
          - title: Deleting Recipes
            path: /
            file: DeleteRecipe
          - title: Adding Vanilla Recipes
            path: /
            file: Vanilla
          - title: Potion Effects
            path: PotionEffect
            children:
              - title: Table of Contents
                path: /
                file: Catalogue
            collapsed: true
          - title: Entities
            path: Entity
            collapsed: true
            children:
              - title: Adding Potion Effects to Entities
                path: /
                file: AddPotionEffects
      - title: Advanced
        path: Upgrade
        collapsed: true
        children:
          - title: Placeholders
            path: /
            file: placeholder
      - title: Expert
        path: Advanced
        collapsed: true
        children:
          - title: Placeholders
            path: /
            file: placeholder     
      # - title: kubejs教程-1.20.1(孤梦版)
      #   noScan: true
      #   path: KubeJSCourse
      #   file: README
prev: false
next: false
---
