# 自定义方块打开工作台界面
::: v-info
通过加载原版Java类来进行方块右键打开工作台GUI的功能
:::

```js
let $SimpleMenuProvider = Java.loadClass('net.minecraft.world.SimpleMenuProvider')
let $CraftingMenu = Java.loadClass('net.minecraft.world.inventory.CraftingMenu')
let $Optional = Java.loadClass(`java.util.Optional`)

// 下方以草方块作为一个示例代码，具体需求可以自行更改
BlockEvents.rightClicked('grass_block', (event) => {
    const { player, level, block } = event

    if (level.clientSide) return

    player.openMenu(
        new $SimpleMenuProvider((i, inv, p) => {
            return new $CraftingMenu(i, inv, (func) => {
                func.apply(level, block.pos)
                return $Optional.empty()
            })
        }, '工作台')
    )
    //这串代码是显示玩家右键是否挥动手臂，可有可无
    player.swing()
})
```