# 启动脚本事件列表

## 前言

- 放在startup_script文件夹下生效。

## 事件列表

### StartupEvents 启动事件

|   事件名称    |   用途    |   示例    |
|:------------:|:---------:|:---------:|
|   init   |   初始化时触发。   |   -   |
|   modifyCreativeTab   |   修改创造物品栏时触发。   |   -   |
|   postInit   |   初始化完成后触发。   |   -   |
|   recipeSchemaRegistry   |   配方类型注册。   |   -   |
|   registry   |   注册游戏内各种事物，如物品，方块。   |   -   |

### BlockEvents 方块事件

|   事件名称    |   用途    |   示例    |
|:------------:|:---------:|:---------:|
|   modification   |   修改块属性。   |   -   |

### ItemEvents 物品事件

|   事件名称    |   用途    |   示例    |
|:------------:|:---------:|:---------:|
|   modelProperties    |   模型属性。   |   -   |
|   modification    |   修改物品属性。   |   -   |
|   armorTierRegistry   |   盔甲等级注册。   |   -   |
|   toolTierRegistry    |   工具等级注册。   |   -   |

### ClientEvents 客户端事件

|   事件名称    |   用途    |   示例    |
|:------------:|:---------:|:---------:|
|   init   |   初始化时触发。   |   -   |
