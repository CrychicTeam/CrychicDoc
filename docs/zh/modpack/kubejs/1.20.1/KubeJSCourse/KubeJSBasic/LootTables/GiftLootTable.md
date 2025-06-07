---
authors:
  - Gu-meng
editor: Gu-meng
---
# 村民礼物战利品
这里的村民礼物是在玩家获得村庄英雄时，[不同村民](../../Digression/LootTableId.md/#村民礼物战利品)会赠送礼物给玩家
## 覆盖村民的礼物赠送
```js
ServerEvents.giftLootTables(e=>{
    e.addGift("armorer_gift",l=>{
        l.addPool(p=>{
            p.addItem("diamond")
        })
    })
})
```
将盔甲匠赠送的礼物该为钻石
## 添加村民的礼物赠送
```js
ServerEvents.giftLootTables(e=>{
    e.modify("armorer_gift",l=>{
        l.addPool(p=>{
            p.addItem("diamond")
        })
    })
})
```
盔甲匠每次赠送礼物会额外赠送一颗钻石
