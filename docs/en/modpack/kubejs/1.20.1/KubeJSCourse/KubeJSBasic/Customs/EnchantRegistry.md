---
authors: ['Gu-meng']
---
# 添加附魔
在kubejs中可以非常轻松的添加附魔，一定要注意，写在startup_scripts文件夹里
```js
StartupEvents.registry("enchantment",event =>{
    event.create("meng:my_enchantment")
})
```
上面的方法在执行后会在游戏内创建一本附魔书，但是很可惜，如果无法直接使该附魔属性在附魔台中附魔到，得借助其他模组的力量

我们接下来可以为附魔书添加一些属性，方法如下
# 属性添加
## 设置可被附魔位置
|    方法名     |   传入参数   |               方法用处                |
| :-----------: | :----------: | :-----------------------------------: |
|    armor()    |   无需传参   |            使盔甲可被附魔             |
|  armorHead()  |   无需传参   |            使头盔可被附魔             |
| armorChest()  |   无需传参   |            使胸甲可被附魔             |
|  armorFeet()  |   无需传参   |            使护腿可被附魔             |
|  armorLegs()  |   无需传参   |            使靴子可被附魔             |
| fishingRod()  |   无需传参   |            使钓鱼竿可附魔             |
|     bow()     |   无需传参   |             使弓可被附魔              |
|  crossbow()   |   无需传参   |             使弩可被附魔              |
|   trident()   |   无需传参   |            使三叉戟可附魔             |
|  breakable()  |   无需传参   |                ？？？                 |
| vanishable()  |   无需传参   |                ？？？                 |
|  wearable()   |   无需传参   |        使可被穿戴的物品可附魔         |
|   weapon()    |   无需传参   |            使武器可被附魔             |
| category(str) | 附魔物品类型 | [添加可附魔的物品类型](#附魔物品类型) |

如果前面调用了`armor()`方法后面紧跟着调用`bow()`，他并不会为盔甲和弓两个都使其可以附魔，它会根据最后一个调用的`bow()`方法来覆盖掉盔甲`armor()`方法，所以这里得注意一下，不要重复调用导致最后最后发现只生效了一个

我们发现直接调用的设置可被附魔的位置里并没有挖掘类，是因为如果你什么都不调用的话，默认就是挖掘类，在一般情况下，我们可以直接使用方法来设置可被附魔的位置，并不需要调用到`category(附魔物品类型)`该方法

### 附魔物品类型
|    参数     |    描述    |
| :---------: | :--------: |
| fishing_rod |   钓鱼竿   |
|  wearable   |  可穿戴的  |
|   trident   |   三叉戟   |
|  crossbow   |     弩     |
| armor_chest |    胸甲    |
| vanishable  |   ？？？   |
|     bow     |     弓     |
|   digger    | 挖掘类工具 |
|   weapon    |    武器    |
|    armor    |    盔甲    |
| armor_legs  |    鞋子    |
| armor_head  |    头盔    |
| armor_feet  |    护腿    |
|  breakable  |   ？？？   |

## 设置稀有度
[关于稀有度](../../Digression/Rarity)
|     方法名     | 传入参数 |                       方法用处                       |
| :------------: | :------: | :--------------------------------------------------: |
| rarity(rarity) |  稀有度  | [添加稀有度](../../Digression/Rarity#稀有度的等级) |
|   uncommon()   | 无需传参 |                   设置稀有度为罕见                   |
|     rare()     | 无需传参 |                   设置稀有度为稀有                   |
|   veryRare()   | 无需传参 |                   设置稀有度为史诗                   |

## 常用方法
|    方法名     |  传入参数  |               方法用处               |
| :-----------: | :--------: | :----------------------------------: |
| minLevel(int) | 整型(数字) |        设置附魔属性的最小等级        |
| maxLevel(int) | 整型(数字) |        设置附魔属性的最大等级        |
| untradeable() |  无需传参  |      设置该附魔书无法被村民售卖      |
|    curse()    |  无需传参  | 设置为负面附魔(作用为显示文本为红色) |

## 检查物品是否可以附魔(canEnchant)
在注册物品时候，如果想给类似于木棍这种不属于任何类型的物品附魔，又或者这个附魔可以给全物品都使用上，我们可以就可以使用到这个`canEnchant`方法，下面是使用示例
```js
StartupEvents.registry("enchantment",event =>{
    //这里直接return true 则代表可以全物品都可以附魔
    event.create("meng:all_item_enchantment").canEnchant((item)=>{return true})
    //如果需要检测某一个附魔
    event.create("meng:item_stick_enchantment")
            .canEnchant((item)=>{
                // 直接返回判断结果作为是否能够附魔的依据
                return item.id == "minecraft:stick"
            })
    //如果检测多个物品
    event.create("meng:more_item_enchantment")
            .canEnchant((item)=>{
                if (item.id == "minecraft:stick"){
                    return true
                }else if(item.id == "minecraft:diamond"){
                    return true
                }
                return false
            })
})
```
当然这里的物品我们也可以进行判断包含哪些附魔属性，来返回false，类似于锋利、亡灵杀手、截肢杀手这样的属性冲突效果，下面是简单的示例
```js
StartupEvents.registry("enchantment",event =>{
    event.create("meng:clash_test1")
            .canEnchant((item)=>{
                //获取物品的所有附魔
                let enchantments = item.getEnchantments()
                //判断附魔中是否有该附魔
                if (enchantments.get("meng:clash_test2") == null){
                    return true
                }else{
                    return false
                }
            })
        
        event.create("meng:clash_test2")
            .canEnchant((item)=>{
                //获取物品的所有附魔
                let enchantments = item.getEnchantments()
                //判断附魔中是否有该附魔
                if (enchantments.get("meng:clash_test1") == null){
                    return true
                }else{
                    return false
                }
            })
})
```
使其clash_test1附魔里不能有clash_test2，clash_test2附魔里不能有clash_test1

## 伤害加成和伤害减免
我们可以直接在附魔注册时候就写好，对生物的伤害加成又或者对穿戴者的伤害减免
### 伤害加成(damageBonus)
该方法实现了对亡灵系生物造成额外附魔等级*10点的伤害
```js
StartupEvents.registry("enchantment",event =>{
    event.create("meng:undead_killer_pro")
            .weapon()
            .damageBonus((level,entityType)=>{
                    if (entityType == "undead"){
                        return level * 10
                    }  
            })
})
```
`level`为附魔等级

`entityType`是生物类型

### 伤害减免(damageProtection)
类似于保护、火焰保护等的伤害减少机制

所以在写之前我们得明白[保护的机制](https://zh.minecraft.wiki/w/%E7%9B%94%E7%94%B2%E6%9C%BA%E5%88%B6#%E4%BF%9D%E6%8A%A4%E9%AD%94%E5%92%92%E6%9C%BA%E5%88%B6)是什么

简单来说当返回值等于25时玩家将绝对免疫这次伤害,但是因为mc的游戏机制问题，返回值最多只有20，超过20的部分也只有20

更加详细的可以查看[mcwiki的保护机制](https://zh.minecraft.wiki/w/%E7%9B%94%E7%94%B2%E6%9C%BA%E5%88%B6#%E4%BF%9D%E6%8A%A4%E9%AD%94%E5%92%92%E6%9C%BA%E5%88%B6)或者文档内的[附魔保护机制](../../Digression/ProtectionEnchantMechanism)

```js
StartupEvents.registry("enchantment",event =>{
    event.create("meng:undead_protect")
            .armor()
            .damageProtection((level,damageSource)=>{
                try{
                    /**
                    * @type {$LivingEntity}
                    */
                    let damageSourceLiving = damageSource.getActual() //获取导致造成伤害的生物
                    let LivingEntityType = damageSourceLiving.getEntityType() //获取造成伤害的实体类型
                    //判断造成伤害的生物是否为 监守者
                    if(LivingEntityType.toShortString() == "warden"){
                        return level * 5
                    }
                    let damageSourceEntity = damageSource.getImmediate() //获取造成受伤的实体
                }catch(e){
                    //如果不是实体导致的伤害，如燃烧、摔落、溺水、药水效果等导致的伤害
                    //会因为报错进入到这里，就可以在这里进行处理
                    console.log(damageSource.getType());
                    if (damageSource.getType() == "lava"){
                        return level * 2
                    }
                }
                
            })
})
```
这里我们的区别一下`getActual()`和`getImmediate()`的区别

举个例子，小白(骷髅)射出来的箭对玩家造成伤害了，那么骷髅为`getActual()`，骷髅射出来的箭则为`getImmediate()`

所以这里得进行一个判断，自己到底需要减少哪一部分的伤害

这里使用try-catch包裹起来是因为玩家受到伤害时，不一定是生物或者实体造成的，也有可能是玩家摔了一跤或者燃起来了，又或者被药水效果导致的扣血伤害，这些都不是实体造成伤害，所以我们直接使用`getActual()`和`getImmediate()`会导致代码报错，所以用[try-catch](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Reference/Statements/try...catch)包裹起来“处理报错”

既然不是实体造成的伤害，我们就需要去判断他的伤害类型，所以使用`damageSource.getType()`来获取到伤害类型，`damageSource.getType()`返回的是一个字符串类型，所以我们可以直接使用`if (damageSource.getType() == "lava")`来判断造成伤害的类型是否为岩浆

这里使用了`console.log(damageSource.getType())`在`logs\kubejs\startup.log`下输出造成伤害类型，我们魔改不确定一个是那种伤害类型，可以将该属性附魔在盔甲上，在游戏里受到你需要判断的属性的伤害就可以获取到了


### 生物类型列表
非常可惜的是，伤害加成或者减免无法对某一个生物进行干预，只能干预某一种生物类别，下面就是全部的生物类别
1. unknown -- 没有属性的生物
2. undead -- 亡灵生物
3. arthropod -- 截肢生物
4. illager -- 灾厄村民(劫掠兽并不属于)
5. water -- 海洋生物

## 受伤和攻击事件
在附魔的注册中，我们可以直接对附魔该属性的物品修改攻击和受伤的事件，就不用单独写一个事件去处理附魔属性的事件
### 受伤(postHurt)
这里的受伤指的是来自实体的攻击后受到的伤害

所以下面实现了穿戴此属性的盔甲受到攻击后，会将攻击者的生命窃取到自己身上
```js
StartupEvents.registry("enchantment",event =>{
    event.create("meng:health_theft")
            .armor()
            .postHurt((living,entity,level)=>{
                living.heal(level * 2)
                if (entity.isLiving()) {
                    entity.attack(level)
                }
            })
})
```
上面的代码中实现了，注册了一个生命窃取的附魔，并只能附给盔甲，注册后别忘了写[lang文件](../../Resources/Lang)
```json
{
    "enchantment.meng.health_theft": "生命窃取"
}
```

在`postHurt`中第一个参数`living`代表了受伤的生物 第二个参数`entity`代表了造成伤害的生物 第三个参数`level`代表了附魔的等级

在本代码中，使用`living.heal(level * 2)`来实现被攻击者生命恢复的效果 `entity.attack(level)`来实现攻击者收到伤害的效果，模拟是因为吸取导致的扣血
### 攻击事件(postAttack)
下面实现了使用此属性的武器攻击后，会窃取吸取到使用者身上
```js
StartupEvents.registry("enchantment",event =>{
    event.create("meng:health_steal")
            .weapon()
            .minLevel(1)
            .maxLevel(5)
            .postAttack((living,entity,level)=>{
                if (entity.isLiving()) {
                    living.health += level
                }
            })
})
```
上面的代码中实现了，注册了一个生命吸取的附魔，并只能附给武器，注册后别忘了写[lang文件](../../Resources/Lang)
```json
{
    "enchantment.meng.health_steal": "生命吸取"
}
```
使用`if (entity.isLiving())`判断被攻击的是否为一个活体生物,如果是则给使用者回复等同于附魔等级的血量