LootJS.modifiers(event=>{
    event.addBlockLootModifier("minecraft:grass").addLoot(Item.of("minecraft:dirt").setCount(1))
})
ItemEvents.rightClicked("acacia_button",event=>{
    event.player.sendMessage("You right clicked an acacia button")
})