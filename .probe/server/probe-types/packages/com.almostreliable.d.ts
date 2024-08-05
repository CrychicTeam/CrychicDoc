declare module "packages/com/almostreliable/ponderjs/$PonderItemTagEventJS" {
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$PonderTag, $PonderTag$Type} from "packages/com/simibubi/create/foundation/ponder/$PonderTag"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $PonderItemTagEventJS extends $EventJS {

constructor()

public "add"(arg0: $PonderTag$Type, arg1: $Ingredient$Type): void
public "remove"(arg0: $PonderTag$Type, arg1: $Ingredient$Type): void
public "createTag"(arg0: string, arg1: $ItemStack$Type, arg2: string, arg3: string): void
public "createTag"(arg0: string, arg1: $ItemStack$Type, arg2: string, arg3: string, arg4: $Ingredient$Type): void
public "removeTag"(...arg0: ($PonderTag$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PonderItemTagEventJS$Type = ($PonderItemTagEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PonderItemTagEventJS_ = $PonderItemTagEventJS$Type;
}}
declare module "packages/com/almostreliable/morejs/mixin/potion/$PotionBrewingAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $PotionBrewingAccessor {

}

export namespace $PotionBrewingAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PotionBrewingAccessor$Type = ($PotionBrewingAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PotionBrewingAccessor_ = $PotionBrewingAccessor$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$AnyDimension" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$IExtendedLootCondition, $IExtendedLootCondition$Type} from "packages/com/almostreliable/lootjs/loot/condition/$IExtendedLootCondition"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $AnyDimension implements $IExtendedLootCondition {

constructor(dimensions: ($ResourceLocation$Type)[])

public "test"(context: $LootContext$Type): boolean
public "getType"(): $LootItemConditionType
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnyDimension$Type = ($AnyDimension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnyDimension_ = $AnyDimension$Type;
}}
declare module "packages/com/almostreliable/ponderjs/$PonderStoriesManager" {
import {$PonderStoryBoardEntry, $PonderStoryBoardEntry$Type} from "packages/com/simibubi/create/foundation/ponder/$PonderStoryBoardEntry"

export class $PonderStoriesManager {

constructor()

public "add"(arg0: $PonderStoryBoardEntry$Type): void
public "clear"(): void
public "compileLang"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PonderStoriesManager$Type = ($PonderStoriesManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PonderStoriesManager_ = $PonderStoriesManager$Type;
}}
declare module "packages/com/almostreliable/morejs/$Plugin" {
import {$KubeJSPlugin, $KubeJSPlugin$Type} from "packages/dev/latvian/mods/kubejs/$KubeJSPlugin"
import {$BindingsEvent, $BindingsEvent$Type} from "packages/dev/latvian/mods/kubejs/script/$BindingsEvent"
import {$ClassFilter, $ClassFilter$Type} from "packages/dev/latvian/mods/kubejs/util/$ClassFilter"
import {$ScriptType, $ScriptType$Type} from "packages/dev/latvian/mods/kubejs/script/$ScriptType"
import {$TypeWrappers, $TypeWrappers$Type} from "packages/dev/latvian/mods/rhino/util/wrap/$TypeWrappers"

export class $Plugin extends $KubeJSPlugin {

constructor()

public "registerBindings"(arg0: $BindingsEvent$Type): void
public "registerEvents"(): void
public "registerClasses"(arg0: $ScriptType$Type, arg1: $ClassFilter$Type): void
public "registerTypeWrappers"(arg0: $ScriptType$Type, arg1: $TypeWrappers$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Plugin$Type = ($Plugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Plugin_ = $Plugin$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/$Constants" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Constants {
static readonly "ACTIVE": string
static readonly "ALTAR": string
static readonly "BLOCK": string
static readonly "BLOCK_BELOW": string
static readonly "CATALYST": string
static readonly "COUNT": string
static readonly "DATA": string
static readonly "DAY_TIME": string
static readonly "INDESTRUCTIBLE_ALTAR": string
static readonly "INGREDIENT": string
static readonly "INPUTS": string
static readonly "INSERT_ORDER": string
static readonly "INVALID": string
static readonly "INVENTORY": string
static readonly "ITEM": string
static readonly "ITEMS": string
static readonly "RECIPE_VIEWER": string
static readonly "LABEL": string
static readonly "MOB": string
static readonly "MOBS": string
static readonly "NBT": string
static readonly "OFFSET": string
static readonly "OUTPUTS": string
static readonly "PROGRESS": string
static readonly "PROPERTIES": string
static readonly "RECIPE_TIME": string
static readonly "REGION": string
static readonly "SACRIFICES": string
static readonly "SLOT": string
static readonly "SPREAD": string
static readonly "TOOLTIP": string
static readonly "WEATHER": string


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Constants$Type = ($Constants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Constants_ = $Constants$Type;
}}
declare module "packages/com/almostreliable/morejs/core/$ReloadListener" {
import {$PreparableReloadListener, $PreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PreparableReloadListener$PreparationBarrier, $PreparableReloadListener$PreparationBarrier$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener$PreparationBarrier"
import {$Executor, $Executor$Type} from "packages/java/util/concurrent/$Executor"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$ProfilerFiller, $ProfilerFiller$Type} from "packages/net/minecraft/util/profiling/$ProfilerFiller"

export class $ReloadListener implements $PreparableReloadListener {

constructor()

public "reload"(arg0: $PreparableReloadListener$PreparationBarrier$Type, arg1: $ResourceManager$Type, arg2: $ProfilerFiller$Type, arg3: $ProfilerFiller$Type, arg4: $Executor$Type, arg5: $Executor$Type): $CompletableFuture<(void)>
public "getName"(): string
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReloadListener$Type = ($ReloadListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReloadListener_ = $ReloadListener$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$LootItemConditionWrapper" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$IExtendedLootCondition, $IExtendedLootCondition$Type} from "packages/com/almostreliable/lootjs/loot/condition/$IExtendedLootCondition"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LootItemCondition, $LootItemCondition$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemCondition"

export class $LootItemConditionWrapper implements $IExtendedLootCondition {

constructor(condition: $LootItemCondition$Type)

public "test"(context: $LootContext$Type): boolean
public "getCondition"(): $LootItemCondition
public "getType"(): $LootItemConditionType
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
get "condition"(): $LootItemCondition
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootItemConditionWrapper$Type = ($LootItemConditionWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootItemConditionWrapper_ = $LootItemConditionWrapper$Type;
}}
declare module "packages/com/almostreliable/morejs/$MoreJSPlatformForge" {
import {$TradingManager, $TradingManager$Type} from "packages/com/almostreliable/morejs/features/villager/$TradingManager"
import {$MoreJSPlatform, $MoreJSPlatform$Type} from "packages/com/almostreliable/morejs/$MoreJSPlatform"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Platform, $Platform$Type} from "packages/com/almostreliable/morejs/$Platform"

export class $MoreJSPlatformForge implements $MoreJSPlatform {

constructor()

public "isDevelopmentEnvironment"(): boolean
public "getPlatform"(): $Platform
public "getEnchantmentCost"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: integer, arg3: integer, arg4: $ItemStack$Type, arg5: integer): integer
public "isModLoaded"(arg0: string): boolean
public "getTradingManager"(): $TradingManager
public "getEnchantmentPower"(arg0: $Level$Type, arg1: $BlockPos$Type): float
get "developmentEnvironment"(): boolean
get "platform"(): $Platform
get "tradingManager"(): $TradingManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MoreJSPlatformForge$Type = ($MoreJSPlatformForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MoreJSPlatformForge_ = $MoreJSPlatformForge$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/events/$WandererTradingEventJS" {
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$TransformableTrade$Transformer, $TransformableTrade$Transformer$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$TransformableTrade$Transformer"
import {$TradeItem, $TradeItem$Type} from "packages/com/almostreliable/morejs/features/villager/$TradeItem"
import {$List, $List$Type} from "packages/java/util/$List"
import {$VillagerTrades$ItemListing, $VillagerTrades$ItemListing$Type} from "packages/net/minecraft/world/entity/npc/$VillagerTrades$ItemListing"
import {$TradeFilter, $TradeFilter$Type} from "packages/com/almostreliable/morejs/features/villager/$TradeFilter"
import {$Int2ObjectMap, $Int2ObjectMap$Type} from "packages/it/unimi/dsi/fastutil/ints/$Int2ObjectMap"
import {$SimpleTrade, $SimpleTrade$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$SimpleTrade"

export class $WandererTradingEventJS extends $EventJS {

constructor(arg0: $Int2ObjectMap$Type<($List$Type<($VillagerTrades$ItemListing$Type)>)>)

public "removeTrades"(arg0: $TradeFilter$Type): void
public "getTrades"(arg0: integer): $List<($VillagerTrades$ItemListing)>
public "removeModdedTrades"(arg0: integer): void
public "addCustomTrade"(arg0: integer, arg1: $TransformableTrade$Transformer$Type): void
public "addTrade"(arg0: integer, arg1: ($TradeItem$Type)[], arg2: $TradeItem$Type): $SimpleTrade
public "addTrade"<T extends $VillagerTrades$ItemListing>(arg0: integer, arg1: T): T
public "removeVanillaTrades"(arg0: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WandererTradingEventJS$Type = ($WandererTradingEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WandererTradingEventJS_ = $WandererTradingEventJS$Type;
}}
declare module "packages/com/almostreliable/lootjs/core/$LootContextParamSetsMapping" {
import {$LootContextType, $LootContextType$Type} from "packages/com/almostreliable/lootjs/core/$LootContextType"
import {$LootContextParamSet, $LootContextParamSet$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParamSet"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LootContextParamSetsMapping {
static readonly "PSETS_TO_TYPE": $Map<($LootContextParamSet), ($LootContextType)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootContextParamSetsMapping$Type = ($LootContextParamSetsMapping);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootContextParamSetsMapping_ = $LootContextParamSetsMapping$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/viewer/jei/ingredient/block/$JEIBlockReferenceRenderer" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$BlockReferenceRenderer, $BlockReferenceRenderer$Type} from "packages/com/almostreliable/summoningrituals/compat/viewer/common/$BlockReferenceRenderer"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$BlockReference, $BlockReference$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$BlockReference"

export class $JEIBlockReferenceRenderer extends $BlockReferenceRenderer implements $IIngredientRenderer<($BlockReference)> {

constructor(size: integer)

public "getWidth"(): integer
public "getHeight"(): integer
public "getFontRenderer"(arg0: $Minecraft$Type, arg1: $BlockReference$Type): $Font
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JEIBlockReferenceRenderer$Type = ($JEIBlockReferenceRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEIBlockReferenceRenderer_ = $JEIBlockReferenceRenderer$Type;
}}
declare module "packages/com/almostreliable/morejs/features/enchantment/$FilterAvailableEnchantmentsEventJS" {
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EnchantmentInstance, $EnchantmentInstance$Type} from "packages/net/minecraft/world/item/enchantment/$EnchantmentInstance"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $FilterAvailableEnchantmentsEventJS extends $EventJS {

constructor(arg0: $List$Type<($EnchantmentInstance$Type)>, arg1: integer, arg2: $ItemStack$Type)

public "add"(...arg0: ($Enchantment$Type)[]): void
public "remove"(...arg0: ($Enchantment$Type)[]): void
public "getItem"(): $ItemStack
public "getPowerLevel"(): integer
public "addWithLevel"(arg0: $Enchantment$Type, arg1: integer): void
public "getEnchantmentInstances"(): $List<($EnchantmentInstance)>
public "printEnchantmentInstances"(): void
get "item"(): $ItemStack
get "powerLevel"(): integer
get "enchantmentInstances"(): $List<($EnchantmentInstance)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FilterAvailableEnchantmentsEventJS$Type = ($FilterAvailableEnchantmentsEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FilterAvailableEnchantmentsEventJS_ = $FilterAvailableEnchantmentsEventJS$Type;
}}
declare module "packages/com/almostreliable/lootjs/$BuildConfig" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $BuildConfig {
static readonly "MOD_ID": string
static readonly "MOD_NAME": string
static readonly "MOD_VERSION": string


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuildConfig$Type = ($BuildConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuildConfig_ = $BuildConfig$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/viewer/common/$MobIngredient" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$SpawnEggItem, $SpawnEggItem$Type} from "packages/net/minecraft/world/item/$SpawnEggItem"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $MobIngredient {

constructor(mob: $EntityType$Type<(any)>, count: integer, tag: $CompoundTag$Type)
constructor(mob: $EntityType$Type<(any)>, count: integer)

public "getDisplayName"(): $Component
public "getCount"(): integer
public "getTag"(): $CompoundTag
public "getEntityType"(): $EntityType<(any)>
public "getEntity"(): $Entity
public "getEgg"(): $SpawnEggItem
public "getRegistryName"(): $MutableComponent
get "displayName"(): $Component
get "count"(): integer
get "tag"(): $CompoundTag
get "entityType"(): $EntityType<(any)>
get "entity"(): $Entity
get "egg"(): $SpawnEggItem
get "registryName"(): $MutableComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobIngredient$Type = ($MobIngredient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobIngredient_ = $MobIngredient$Type;
}}
declare module "packages/com/almostreliable/ponderjs/particles/$ParticleDataBuilder" {
import {$ParticleTransformation, $ParticleTransformation$Type} from "packages/com/almostreliable/ponderjs/particles/$ParticleTransformation"
import {$Color, $Color$Type} from "packages/dev/latvian/mods/rhino/mod/util/color/$Color"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ParticleTransformation$Simple, $ParticleTransformation$Simple$Type} from "packages/com/almostreliable/ponderjs/particles/$ParticleTransformation$Simple"
import {$ParticleOptions, $ParticleOptions$Type} from "packages/net/minecraft/core/particles/$ParticleOptions"

export class $ParticleDataBuilder<O extends $ParticleDataBuilder<(O), (PO)>, PO extends $ParticleOptions> {

constructor()

public "physics"(arg0: boolean): O
public "transformMotion"(arg0: $ParticleTransformation$Simple$Type): O
public "scale"(arg0: float): O
public "transform"(arg0: $ParticleTransformation$Type): O
public "delta"(arg0: $Vec3$Type): O
public "color"(arg0: $Color$Type): O
public "roll"(arg0: float): O
public "lifetime"(arg0: integer): O
public "withinBlockSpace"(): O
public "transformPosition"(arg0: $ParticleTransformation$Simple$Type): O
public "area"(arg0: $Vec3$Type): O
public "motion"(arg0: $Vec3$Type): O
public "collision"(arg0: boolean): O
public "density"(arg0: integer): O
public "gravity"(arg0: float): O
public "friction"(arg0: float): O
public "speed"(arg0: $Vec3$Type): O
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleDataBuilder$Type<O, PO> = ($ParticleDataBuilder<(O), (PO)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleDataBuilder_<O, PO> = $ParticleDataBuilder$Type<(O), (PO)>;
}}
declare module "packages/com/almostreliable/morejs/features/villager/trades/$MapPosInfo" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $MapPosInfo extends $Record {

constructor(pos: $BlockPos$Type, name: $Component$Type)

public "name"(): $Component
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "pos"(): $BlockPos
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapPosInfo$Type = ($MapPosInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapPosInfo_ = $MapPosInfo$Type;
}}
declare module "packages/com/almostreliable/lootjs/$LootContextData" {
import {$LootContextType, $LootContextType$Type} from "packages/com/almostreliable/lootjs/core/$LootContextType"
import {$ILootContextData, $ILootContextData$Type} from "packages/com/almostreliable/lootjs/core/$ILootContextData"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LootContextData implements $ILootContextData {

constructor(pType: $LootContextType$Type)

public "reset"(): void
public "setCanceled"(flag: boolean): void
public "isCanceled"(): boolean
public "getCustomData"(): $Map<(string), (any)>
public "getLootContextType"(): $LootContextType
public "getGeneratedLoot"(): $List<($ItemStack)>
public "setGeneratedLoot"(loot: $List$Type<($ItemStack$Type)>): void
set "canceled"(value: boolean)
get "canceled"(): boolean
get "customData"(): $Map<(string), (any)>
get "lootContextType"(): $LootContextType
get "generatedLoot"(): $List<($ItemStack)>
set "generatedLoot"(value: $List$Type<($ItemStack$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootContextData$Type = ($LootContextData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootContextData_ = $LootContextData$Type;
}}
declare module "packages/com/almostreliable/morejs/features/structure/$StructureLoadEventJS" {
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$StructureTemplateAccess, $StructureTemplateAccess$Type} from "packages/com/almostreliable/morejs/features/structure/$StructureTemplateAccess"
import {$PaletteWrapper, $PaletteWrapper$Type} from "packages/com/almostreliable/morejs/features/structure/$PaletteWrapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$EntityInfoWrapper, $EntityInfoWrapper$Type} from "packages/com/almostreliable/morejs/features/structure/$EntityInfoWrapper"
import {$StructureTemplate, $StructureTemplate$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate"

export class $StructureLoadEventJS extends $EventJS {

constructor(arg0: $StructureTemplateAccess$Type, arg1: $ResourceLocation$Type)

public "getEntitiesSize"(): integer
public "getPalette"(arg0: integer): $PaletteWrapper
public "getStructureSize"(): $Vec3i
public "getPalettesSize"(): integer
public "forEachPalettes"(arg0: $Consumer$Type<($PaletteWrapper$Type)>): void
public "removePalette"(arg0: integer): void
public static "invoke"(arg0: $StructureTemplate$Type, arg1: $ResourceLocation$Type): void
public "getId"(): string
public "getEntities"(): $EntityInfoWrapper
get "entitiesSize"(): integer
get "structureSize"(): $Vec3i
get "palettesSize"(): integer
get "id"(): string
get "entities"(): $EntityInfoWrapper
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureLoadEventJS$Type = ($StructureLoadEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureLoadEventJS_ = $StructureLoadEventJS$Type;
}}
declare module "packages/com/almostreliable/morejs/$MoreJSBinding" {
import {$TradeItem, $TradeItem$Type} from "packages/com/almostreliable/morejs/features/villager/$TradeItem"
import {$IntRange, $IntRange$Type} from "packages/com/almostreliable/morejs/features/villager/$IntRange"
import {$TradeFilter, $TradeFilter$Type} from "packages/com/almostreliable/morejs/features/villager/$TradeFilter"
import {$WeightedList$Builder, $WeightedList$Builder$Type} from "packages/com/almostreliable/morejs/util/$WeightedList$Builder"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$WeightedList, $WeightedList$Type} from "packages/com/almostreliable/morejs/util/$WeightedList"

export class $MoreJSBinding {

constructor()

public static "range"(arg0: any): $IntRange
public static "weightedList"(): $WeightedList$Builder<(any)>
public static "findStructure"(arg0: $BlockPos$Type, arg1: $ServerLevel$Type, arg2: string, arg3: integer): $BlockPos
public static "findBiome"(arg0: $BlockPos$Type, arg1: $ServerLevel$Type, arg2: string, arg3: integer): $BlockPos
public static "ofTradeFilter"(arg0: any): $TradeFilter
public static "ofWeightedList"(arg0: any): $WeightedList<(any)>
public static "ofTradeItem"(arg0: any): $TradeItem
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MoreJSBinding$Type = ($MoreJSBinding);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MoreJSBinding_ = $MoreJSBinding$Type;
}}
declare module "packages/com/almostreliable/ponderjs/api/$ExtendedSceneBuilder$ExtendedWorldInstructions" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$BlockStateFunction, $BlockStateFunction$Type} from "packages/com/almostreliable/ponderjs/util/$BlockStateFunction"
import {$Selection, $Selection$Type} from "packages/com/simibubi/create/foundation/ponder/$Selection"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ExtendedSceneBuilder, $ExtendedSceneBuilder$Type} from "packages/com/almostreliable/ponderjs/api/$ExtendedSceneBuilder"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$SceneBuilder$WorldInstructions, $SceneBuilder$WorldInstructions$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuilder$WorldInstructions"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ElementLink, $ElementLink$Type} from "packages/com/simibubi/create/foundation/ponder/$ElementLink"
import {$EntityElement, $EntityElement$Type} from "packages/com/simibubi/create/foundation/ponder/element/$EntityElement"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ExtendedSceneBuilder$ExtendedWorldInstructions extends $SceneBuilder$WorldInstructions {

constructor(arg0: $ExtendedSceneBuilder$Type)

public "modifyBlock"(arg0: $BlockPos$Type, arg1: $BlockStateFunction$Type, arg2: boolean): void
public "removeEntity"(arg0: $ElementLink$Type<($EntityElement$Type)>): void
public "createEntity"(arg0: $EntityType$Type<(any)>, arg1: $Vec3$Type, arg2: $Consumer$Type<($Entity$Type)>): $ElementLink<($EntityElement)>
public "createEntity"(arg0: $EntityType$Type<(any)>, arg1: $Vec3$Type): $ElementLink<($EntityElement)>
public "modifyBlockEntityNBT"(arg0: $Selection$Type, arg1: $Consumer$Type<($CompoundTag$Type)>): void
public "modifyBlockEntityNBT"(arg0: $Selection$Type, arg1: boolean, arg2: $Consumer$Type<($CompoundTag$Type)>): void
public "setBlocks"(arg0: $Selection$Type, arg1: boolean, arg2: $BlockState$Type): void
public "setBlocks"(arg0: $Selection$Type, arg1: $BlockState$Type): void
public "modifyBlocks"(arg0: $Selection$Type, arg1: $BlockStateFunction$Type): void
public "modifyBlocks"(arg0: $Selection$Type, arg1: boolean, arg2: $BlockStateFunction$Type): void
public "modifyBlocks"(arg0: $Selection$Type, arg1: $BlockStateFunction$Type, arg2: boolean): void
/**
 * 
 * @deprecated
 */
public "modifyTileNBT"(arg0: $Selection$Type, arg1: $Consumer$Type<($CompoundTag$Type)>): void
/**
 * 
 * @deprecated
 */
public "modifyTileNBT"(arg0: $Selection$Type, arg1: $Consumer$Type<($CompoundTag$Type)>, arg2: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtendedSceneBuilder$ExtendedWorldInstructions$Type = ($ExtendedSceneBuilder$ExtendedWorldInstructions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtendedSceneBuilder$ExtendedWorldInstructions_ = $ExtendedSceneBuilder$ExtendedWorldInstructions$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/action/$LootItemFunctionWrapperAction" {
import {$LootItemFunction, $LootItemFunction$Type} from "packages/net/minecraft/world/level/storage/loot/functions/$LootItemFunction"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ILootAction, $ILootAction$Type} from "packages/com/almostreliable/lootjs/core/$ILootAction"

export class $LootItemFunctionWrapperAction implements $ILootAction {

constructor(lootItemFunction: $LootItemFunction$Type)

public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
public "getLootItemFunction"(): $LootItemFunction
get "lootItemFunction"(): $LootItemFunction
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootItemFunctionWrapperAction$Type = ($LootItemFunctionWrapperAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootItemFunctionWrapperAction_ = $LootItemFunctionWrapperAction$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/recipe/$AltarRecipeSerializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$AltarRecipe, $AltarRecipe$Type} from "packages/com/almostreliable/summoningrituals/recipe/$AltarRecipe"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $AltarRecipeSerializer implements $RecipeSerializer<($AltarRecipe)> {
static "MAX_INPUTS": integer

constructor()

public "fromJson"(id: $ResourceLocation$Type, json: $JsonObject$Type): $AltarRecipe
public "fromNetwork"(id: $ResourceLocation$Type, buffer: $FriendlyByteBuf$Type): $AltarRecipe
public "toNetwork"(buffer: $FriendlyByteBuf$Type, recipe: $AltarRecipe$Type): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type, arg2: $ICondition$IContext$Type): $AltarRecipe
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AltarRecipeSerializer$Type = ($AltarRecipeSerializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AltarRecipeSerializer_ = $AltarRecipeSerializer$Type;
}}
declare module "packages/com/almostreliable/ponderjs/$PonderJSPlugin" {
import {$KubeJSPlugin, $KubeJSPlugin$Type} from "packages/dev/latvian/mods/kubejs/$KubeJSPlugin"
import {$BindingsEvent, $BindingsEvent$Type} from "packages/dev/latvian/mods/kubejs/script/$BindingsEvent"
import {$ScriptType, $ScriptType$Type} from "packages/dev/latvian/mods/kubejs/script/$ScriptType"
import {$TypeWrappers, $TypeWrappers$Type} from "packages/dev/latvian/mods/rhino/util/wrap/$TypeWrappers"

export class $PonderJSPlugin extends $KubeJSPlugin {

constructor()

public "registerBindings"(arg0: $BindingsEvent$Type): void
public "registerEvents"(): void
public "registerTypeWrappers"(arg0: $ScriptType$Type, arg1: $TypeWrappers$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PonderJSPlugin$Type = ($PonderJSPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PonderJSPlugin_ = $PonderJSPlugin$Type;
}}
declare module "packages/com/almostreliable/morejs/$MoreJSPlatform" {
import {$TradingManager, $TradingManager$Type} from "packages/com/almostreliable/morejs/features/villager/$TradingManager"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Platform, $Platform$Type} from "packages/com/almostreliable/morejs/$Platform"

export interface $MoreJSPlatform {

 "isDevelopmentEnvironment"(): boolean
 "getPlatform"(): $Platform
 "getEnchantmentCost"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: integer, arg3: integer, arg4: $ItemStack$Type, arg5: integer): integer
 "isModLoaded"(arg0: string): boolean
 "getTradingManager"(): $TradingManager
 "getEnchantmentPower"(arg0: $Level$Type, arg1: $BlockPos$Type): float
}

export namespace $MoreJSPlatform {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MoreJSPlatform$Type = ($MoreJSPlatform);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MoreJSPlatform_ = $MoreJSPlatform$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/results/$LootContextInfo" {
import {$LootInfoCollector, $LootInfoCollector$Type} from "packages/com/almostreliable/lootjs/loot/results/$LootInfoCollector"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $LootContextInfo {


public static "create"(context: $LootContext$Type): $LootContextInfo
public "updateLoot"(loot: $Collection$Type<($ItemStack$Type)>): void
public "getCollector"(): $LootInfoCollector
get "collector"(): $LootInfoCollector
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootContextInfo$Type = ($LootContextInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootContextInfo_ = $LootContextInfo$Type;
}}
declare module "packages/com/almostreliable/lootjs/core/$LootEntry$Generator" {
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $LootEntry$Generator {

 "create"(arg0: $LootContext$Type): $ItemStack

(arg0: $LootContext$Type): $ItemStack
}

export namespace $LootEntry$Generator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootEntry$Generator$Type = ($LootEntry$Generator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootEntry$Generator_ = $LootEntry$Generator$Type;
}}
declare module "packages/com/almostreliable/ponderjs/particles/$ParticleTransformation$Data" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $ParticleTransformation$Data extends $Record {

constructor(position: $Vec3$Type, motion: $Vec3$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "position"(): $Vec3
public static "of"(arg0: any): $ParticleTransformation$Data
public "motion"(): $Vec3
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleTransformation$Data$Type = ($ParticleTransformation$Data);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleTransformation$Data_ = $ParticleTransformation$Data$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/$AddAttributesFunction$Modifier" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$NumberProvider, $NumberProvider$Type} from "packages/net/minecraft/world/level/storage/loot/providers/number/$NumberProvider"

export class $AddAttributesFunction$Modifier {

constructor(probability: float, attribute: $Attribute$Type, operation: $AttributeModifier$Operation$Type, amount: $NumberProvider$Type, name: string, slots: $Function$Type<($ItemStack$Type), (($EquipmentSlot$Type)[])>, uuid: $UUID$Type)

public "createAttributeModifier"(context: $LootContext$Type): $AttributeModifier
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AddAttributesFunction$Modifier$Type = ($AddAttributesFunction$Modifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AddAttributesFunction$Modifier_ = $AddAttributesFunction$Modifier$Type;
}}
declare module "packages/com/almostreliable/ponderjs/api/$AbstractPonderBuilder" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$PonderTag, $PonderTag$Type} from "packages/com/simibubi/create/foundation/ponder/$PonderTag"
import {$Set, $Set$Type} from "packages/java/util/$Set"

export class $AbstractPonderBuilder<S extends $AbstractPonderBuilder<(S)>> {

constructor(arg0: $Set$Type<($Item$Type)>)

public "tag"(...arg0: ($PonderTag$Type)[]): S
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractPonderBuilder$Type<S> = ($AbstractPonderBuilder<(S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractPonderBuilder_<S> = $AbstractPonderBuilder$Type<(S)>;
}}
declare module "packages/com/almostreliable/morejs/features/villager/trades/$EnchantedItemTrade" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$MerchantOffer, $MerchantOffer$Type} from "packages/net/minecraft/world/item/trading/$MerchantOffer"
import {$TradeItem, $TradeItem$Type} from "packages/com/almostreliable/morejs/features/villager/$TradeItem"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$TransformableTrade, $TransformableTrade$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$TransformableTrade"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EnchantedItemTrade extends $TransformableTrade<($EnchantedItemTrade)> {

constructor(arg0: ($TradeItem$Type)[], arg1: $Item$Type)

public "amount"(arg0: integer): $EnchantedItemTrade
public "amount"(arg0: integer, arg1: integer): $EnchantedItemTrade
public "createOffer"(arg0: $Entity$Type, arg1: $RandomSource$Type): $MerchantOffer
public "enchantments"(...arg0: ($Enchantment$Type)[]): $EnchantedItemTrade
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnchantedItemTrade$Type = ($EnchantedItemTrade);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnchantedItemTrade_ = $EnchantedItemTrade$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$PlayerParamPredicate" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$IExtendedLootCondition, $IExtendedLootCondition$Type} from "packages/com/almostreliable/lootjs/loot/condition/$IExtendedLootCondition"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $PlayerParamPredicate implements $IExtendedLootCondition {

constructor(predicate: $Predicate$Type<($ServerPlayer$Type)>)

public "test"(lootContext: $LootContext$Type): boolean
public "getType"(): $LootItemConditionType
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerParamPredicate$Type = ($PlayerParamPredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerParamPredicate_ = $PlayerParamPredicate$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/$LootActionsContainer" {
import {$ModifyLootAction$Callback, $ModifyLootAction$Callback$Type} from "packages/com/almostreliable/lootjs/loot/action/$ModifyLootAction$Callback"
import {$ItemFilter, $ItemFilter$Type} from "packages/com/almostreliable/lootjs/filters/$ItemFilter"
import {$Explosion$BlockInteraction, $Explosion$BlockInteraction$Type} from "packages/net/minecraft/world/level/$Explosion$BlockInteraction"
import {$LootEntry, $LootEntry$Type} from "packages/com/almostreliable/lootjs/core/$LootEntry"
import {$ILootAction, $ILootAction$Type} from "packages/com/almostreliable/lootjs/core/$ILootAction"
import {$NumberProvider, $NumberProvider$Type} from "packages/net/minecraft/world/level/storage/loot/providers/number/$NumberProvider"

export interface $LootActionsContainer<A extends $LootActionsContainer<(any)>> {

 "replaceLoot"(filter: $ItemFilter$Type, lootEntry: $LootEntry$Type): A
 "replaceLoot"(filter: $ItemFilter$Type, lootEntry: $LootEntry$Type, preserveCount: boolean): A
 "addSequenceLoot"(...entries: ($LootEntry$Type)[]): A
 "removeLoot"(filter: $ItemFilter$Type): A
 "addWeightedLoot"(poolEntries: ($LootEntry$Type)[]): A
 "addWeightedLoot"(numberProvider: $NumberProvider$Type, poolEntries: ($LootEntry$Type)[]): A
 "addWeightedLoot"(numberProvider: $NumberProvider$Type, allowDuplicateLoot: boolean, poolEntries: ($LootEntry$Type)[]): A
 "triggerExplosion"(radius: float, destroy: boolean, fire: boolean): A
 "triggerExplosion"(radius: float, mode: $Explosion$BlockInteraction$Type, fire: boolean): A
 "dropExperience"(amount: integer): A
 "modifyLoot"(filter: $ItemFilter$Type, callback: $ModifyLootAction$Callback$Type): A
 "addLoot"(...entries: ($LootEntry$Type)[]): A
 "addAlternativesLoot"(...entries: ($LootEntry$Type)[]): A
 "triggerLightningStrike"(shouldDamage: boolean): A
 "addAction"(arg0: $ILootAction$Type): A

(filter: $ItemFilter$Type, lootEntry: $LootEntry$Type): A
}

export namespace $LootActionsContainer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootActionsContainer$Type<A> = ($LootActionsContainer<(A)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootActionsContainer_<A> = $LootActionsContainer$Type<(A)>;
}}
declare module "packages/com/almostreliable/ponderjs/$BuildConfig" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $BuildConfig {
static readonly "MOD_ID": string
static readonly "MOD_VERSION": string
static readonly "MOD_NAME": string


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuildConfig$Type = ($BuildConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuildConfig_ = $BuildConfig$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/viewer/jei/$AlmostJEI" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IGuiHandlerRegistration, $IGuiHandlerRegistration$Type} from "packages/mezz/jei/api/registration/$IGuiHandlerRegistration"
import {$IJeiConfigManager, $IJeiConfigManager$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigManager"
import {$IAdvancedRegistration, $IAdvancedRegistration$Type} from "packages/mezz/jei/api/registration/$IAdvancedRegistration"
import {$IVanillaCategoryExtensionRegistration, $IVanillaCategoryExtensionRegistration$Type} from "packages/mezz/jei/api/registration/$IVanillaCategoryExtensionRegistration"
import {$IRecipeRegistration, $IRecipeRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeRegistration"
import {$IRecipeTransferRegistration, $IRecipeTransferRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeTransferRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IJeiRuntime, $IJeiRuntime$Type} from "packages/mezz/jei/api/runtime/$IJeiRuntime"
import {$BlockReference, $BlockReference$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$BlockReference"
import {$IRecipeCatalystRegistration, $IRecipeCatalystRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCatalystRegistration"
import {$IModPlugin, $IModPlugin$Type} from "packages/mezz/jei/api/$IModPlugin"
import {$IRuntimeRegistration, $IRuntimeRegistration$Type} from "packages/mezz/jei/api/registration/$IRuntimeRegistration"
import {$MobIngredient, $MobIngredient$Type} from "packages/com/almostreliable/summoningrituals/compat/viewer/common/$MobIngredient"
import {$IRecipeCategoryRegistration, $IRecipeCategoryRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCategoryRegistration"
import {$IModIngredientRegistration, $IModIngredientRegistration$Type} from "packages/mezz/jei/api/registration/$IModIngredientRegistration"
import {$ISubtypeRegistration, $ISubtypeRegistration$Type} from "packages/mezz/jei/api/registration/$ISubtypeRegistration"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"

export class $AlmostJEI implements $IModPlugin {
static readonly "MOB": $IIngredientType<($MobIngredient)>
static readonly "BLOCK_REFERENCE": $IIngredientType<($BlockReference)>

constructor()

public "registerIngredients"(r: $IModIngredientRegistration$Type): void
public "registerRecipeCatalysts"(r: $IRecipeCatalystRegistration$Type): void
public "getPluginUid"(): $ResourceLocation
public "registerRecipes"(r: $IRecipeRegistration$Type): void
public "registerCategories"(r: $IRecipeCategoryRegistration$Type): void
public "registerItemSubtypes"(arg0: $ISubtypeRegistration$Type): void
public "registerVanillaCategoryExtensions"(arg0: $IVanillaCategoryExtensionRegistration$Type): void
public "registerFluidSubtypes"<T>(arg0: $ISubtypeRegistration$Type, arg1: $IPlatformFluidHelper$Type<(T)>): void
public "onConfigManagerAvailable"(arg0: $IJeiConfigManager$Type): void
public "registerGuiHandlers"(arg0: $IGuiHandlerRegistration$Type): void
public "onRuntimeUnavailable"(): void
public "registerRecipeTransferHandlers"(arg0: $IRecipeTransferRegistration$Type): void
public "registerAdvanced"(arg0: $IAdvancedRegistration$Type): void
public "onRuntimeAvailable"(arg0: $IJeiRuntime$Type): void
public "registerRuntime"(arg0: $IRuntimeRegistration$Type): void
get "pluginUid"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AlmostJEI$Type = ($AlmostJEI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AlmostJEI_ = $AlmostJEI$Type;
}}
declare module "packages/com/almostreliable/ponderjs/$PonderRegistryEventJS" {
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$PonderBuilderJS, $PonderBuilderJS$Type} from "packages/com/almostreliable/ponderjs/$PonderBuilderJS"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"

export class $PonderRegistryEventJS extends $EventJS {

constructor()

public "create"(arg0: $Ingredient$Type): $PonderBuilderJS
public "printParticleNames"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PonderRegistryEventJS$Type = ($PonderRegistryEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PonderRegistryEventJS_ = $PonderRegistryEventJS$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/inventory/$VanillaWrapper" {
import {$RecipeWrapper, $RecipeWrapper$Type} from "packages/net/minecraftforge/items/wrapper/$RecipeWrapper"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $VanillaWrapper extends $RecipeWrapper {


public "getCatalyst"(): $ItemStack
public "getItems"(): $List<($ItemStack)>
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public static "tryClear"(arg0: any): void
get "catalyst"(): $ItemStack
get "items"(): $List<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VanillaWrapper$Type = ($VanillaWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VanillaWrapper_ = $VanillaWrapper$Type;
}}
declare module "packages/com/almostreliable/lootjs/core/$ILootHandler" {
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $ILootHandler {

 "applyLootHandler"(arg0: $LootContext$Type, arg1: $List$Type<($ItemStack$Type)>): boolean

(arg0: $LootContext$Type, arg1: $List$Type<($ItemStack$Type)>): boolean
}

export namespace $ILootHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ILootHandler$Type = ($ILootHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ILootHandler_ = $ILootHandler$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/viewer/jei/ingredient/mob/$JEIMobRenderer" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$MobIngredient, $MobIngredient$Type} from "packages/com/almostreliable/summoningrituals/compat/viewer/common/$MobIngredient"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$MobRenderer, $MobRenderer$Type} from "packages/com/almostreliable/summoningrituals/compat/viewer/common/$MobRenderer"

export class $JEIMobRenderer extends $MobRenderer implements $IIngredientRenderer<($MobIngredient)> {

constructor(size: integer)

public "getWidth"(): integer
public "getHeight"(): integer
public "getFontRenderer"(arg0: $Minecraft$Type, arg1: $MobIngredient$Type): $Font
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JEIMobRenderer$Type = ($JEIMobRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEIMobRenderer_ = $JEIMobRenderer$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/gametest/conditions/$ContainsLootConditionTest" {
import {$GameTestHelper, $GameTestHelper$Type} from "packages/net/minecraft/gametest/framework/$GameTestHelper"

export class $ContainsLootConditionTest {

constructor()

public "simpleCheck_fail"(helper: $GameTestHelper$Type): void
public "exactCheck"(helper: $GameTestHelper$Type): void
public "exactCheck_fail"(helper: $GameTestHelper$Type): void
public "nonExactCheck"(helper: $GameTestHelper$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContainsLootConditionTest$Type = ($ContainsLootConditionTest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContainsLootConditionTest_ = $ContainsLootConditionTest$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/kubejs/$AlmostKube" {
import {$KubeJSPlugin, $KubeJSPlugin$Type} from "packages/dev/latvian/mods/kubejs/$KubeJSPlugin"
import {$BindingsEvent, $BindingsEvent$Type} from "packages/dev/latvian/mods/kubejs/script/$BindingsEvent"
import {$ScriptType, $ScriptType$Type} from "packages/dev/latvian/mods/kubejs/script/$ScriptType"
import {$TypeWrappers, $TypeWrappers$Type} from "packages/dev/latvian/mods/rhino/util/wrap/$TypeWrappers"
import {$RegisterRecipeSchemasEvent, $RegisterRecipeSchemasEvent$Type} from "packages/dev/latvian/mods/kubejs/recipe/schema/$RegisterRecipeSchemasEvent"

export class $AlmostKube extends $KubeJSPlugin {

constructor()

public "init"(): void
public "registerBindings"(event: $BindingsEvent$Type): void
public "registerEvents"(): void
public "registerTypeWrappers"(type: $ScriptType$Type, typeWrappers: $TypeWrappers$Type): void
public "registerRecipeSchemas"(event: $RegisterRecipeSchemasEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AlmostKube$Type = ($AlmostKube);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AlmostKube_ = $AlmostKube$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/gametest/$GameTestUtilsTests" {
import {$GameTestHelper, $GameTestHelper$Type} from "packages/net/minecraft/gametest/framework/$GameTestHelper"

export class $GameTestUtilsTests {

constructor()

public "fillExampleLoot"(helper: $GameTestHelper$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GameTestUtilsTests$Type = ($GameTestUtilsTests);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GameTestUtilsTests_ = $GameTestUtilsTests$Type;
}}
declare module "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentTableIsEnchantableEventJS" {
import {$EnchantmentTableServerEventJS, $EnchantmentTableServerEventJS$Type} from "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentTableServerEventJS"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$EnchantmentMenuProcess, $EnchantmentMenuProcess$Type} from "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentMenuProcess"

export class $EnchantmentTableIsEnchantableEventJS extends $EnchantmentTableServerEventJS {

constructor(arg0: $ItemStack$Type, arg1: $ItemStack$Type, arg2: $Level$Type, arg3: $BlockPos$Type, arg4: $EnchantmentMenuProcess$Type)

public "getIsEnchantable"(): boolean
public "setIsEnchantable"(arg0: boolean): void
get "isEnchantable"(): boolean
set "isEnchantable"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnchantmentTableIsEnchantableEventJS$Type = ($EnchantmentTableIsEnchantableEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnchantmentTableIsEnchantableEventJS_ = $EnchantmentTableIsEnchantableEventJS$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/recipe/$AltarRecipe" {
import {$InputReplacement, $InputReplacement$Type} from "packages/dev/latvian/mods/kubejs/recipe/$InputReplacement"
import {$AltarRecipe$DAY_TIME, $AltarRecipe$DAY_TIME$Type} from "packages/com/almostreliable/summoningrituals/recipe/$AltarRecipe$DAY_TIME"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$RecipeSchema, $RecipeSchema$Type} from "packages/dev/latvian/mods/kubejs/recipe/schema/$RecipeSchema"
import {$AltarRecipe$WEATHER, $AltarRecipe$WEATHER$Type} from "packages/com/almostreliable/summoningrituals/recipe/$AltarRecipe$WEATHER"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$RecipeSacrifices, $RecipeSacrifices$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeSacrifices"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockReference, $BlockReference$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$BlockReference"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$RecipeOutputs, $RecipeOutputs$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs"
import {$IngredientStack, $IngredientStack$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$IngredientStack"
import {$ReplacementMatch, $ReplacementMatch$Type} from "packages/dev/latvian/mods/kubejs/recipe/$ReplacementMatch"
import {$VanillaWrapper, $VanillaWrapper$Type} from "packages/com/almostreliable/summoningrituals/inventory/$VanillaWrapper"
import {$OutputReplacement, $OutputReplacement$Type} from "packages/dev/latvian/mods/kubejs/recipe/$OutputReplacement"

export class $AltarRecipe implements $Recipe<($VanillaWrapper)> {
static readonly "CATALYST_CACHE": $Set<($Ingredient)>


public "matches"(inv: $VanillaWrapper$Type, level: $Level$Type): boolean
public "getResultItem"(registryAccess: $RegistryAccess$Type): $ItemStack
public "getSacrifices"(): $RecipeSacrifices
public "getDayTime"(): $AltarRecipe$DAY_TIME
public "getCatalyst"(): $Ingredient
public "getBlockBelow"(): $BlockReference
public "getOutputs"(): $RecipeOutputs
public "getWeather"(): $AltarRecipe$WEATHER
public "getRecipeTime"(): integer
public "canCraftInDimensions"(width: integer, height: integer): boolean
public "getSerializer"(): $RecipeSerializer<(any)>
public "assemble"(inv: $VanillaWrapper$Type, registryAccess: $RegistryAccess$Type): $ItemStack
public "getInputs"(): $NonNullList<($IngredientStack)>
public "getId"(): $ResourceLocation
public "isSpecial"(): boolean
public "getRemainingItems"(arg0: $VanillaWrapper$Type): $NonNullList<($ItemStack)>
public "getIngredients"(): $NonNullList<($Ingredient)>
public "getToastSymbol"(): $ItemStack
public "isIncomplete"(): boolean
public "showNotification"(): boolean
public "getType"(): $ResourceLocation
public "replaceOutput"(match: $ReplacementMatch$Type, arg1: $OutputReplacement$Type): boolean
public "setGroup"(group: string): void
public "hasInput"(match: $ReplacementMatch$Type): boolean
public "getOrCreateId"(): $ResourceLocation
public "getSchema"(): $RecipeSchema
public "replaceInput"(match: $ReplacementMatch$Type, arg1: $InputReplacement$Type): boolean
public "hasOutput"(match: $ReplacementMatch$Type): boolean
public "getGroup"(): string
public "getMod"(): string
get "sacrifices"(): $RecipeSacrifices
get "dayTime"(): $AltarRecipe$DAY_TIME
get "catalyst"(): $Ingredient
get "blockBelow"(): $BlockReference
get "outputs"(): $RecipeOutputs
get "weather"(): $AltarRecipe$WEATHER
get "recipeTime"(): integer
get "serializer"(): $RecipeSerializer<(any)>
get "inputs"(): $NonNullList<($IngredientStack)>
get "id"(): $ResourceLocation
get "special"(): boolean
get "ingredients"(): $NonNullList<($Ingredient)>
get "toastSymbol"(): $ItemStack
get "incomplete"(): boolean
get "type"(): $ResourceLocation
set "group"(value: string)
get "orCreateId"(): $ResourceLocation
get "schema"(): $RecipeSchema
get "group"(): string
get "mod"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AltarRecipe$Type = ($AltarRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AltarRecipe_ = $AltarRecipe$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/$OfferExtension" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $OfferExtension {

 "getSecondInput"(): $ItemStack
 "setSecondInput"(arg0: $ItemStack$Type): void
 "setVillagerExperience"(arg0: integer): void
 "setRewardExp"(arg0: boolean): void
 "setPriceMultiplier"(arg0: float): void
 "isRewardingExp"(): boolean
 "replaceEmeralds"(arg0: $Item$Type): void
 "replaceItems"(arg0: $Ingredient$Type, arg1: $ItemStack$Type): void
 "getFirstInput"(): $ItemStack
 "setFirstInput"(arg0: $ItemStack$Type): void
 "isDisabled"(): boolean
 "setMaxUses"(arg0: integer): void
 "getOutput"(): $ItemStack
 "setDisabled"(arg0: boolean): void
 "setOutput"(arg0: $ItemStack$Type): void
 "setDemand"(arg0: integer): void
}

export namespace $OfferExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OfferExtension$Type = ($OfferExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OfferExtension_ = $OfferExtension$Type;
}}
declare module "packages/com/almostreliable/lootjs/core/$LootModificationByType" {
import {$AbstractLootModification, $AbstractLootModification$Type} from "packages/com/almostreliable/lootjs/core/$AbstractLootModification"
import {$ILootHandler, $ILootHandler$Type} from "packages/com/almostreliable/lootjs/core/$ILootHandler"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$LootContextType, $LootContextType$Type} from "packages/com/almostreliable/lootjs/core/$LootContextType"
import {$List, $List$Type} from "packages/java/util/$List"

export class $LootModificationByType extends $AbstractLootModification {

constructor(name: string, types: $List$Type<($LootContextType$Type)>, handlers: $List$Type<($ILootHandler$Type)>)

public "shouldExecute"(context: $LootContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootModificationByType$Type = ($LootModificationByType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootModificationByType_ = $LootModificationByType$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$RecipeOutput" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"

export class $RecipeOutputs$RecipeOutput<T> {


public "getCount"(): integer
public "getData"(): $CompoundTag
public "getOutput"(): T
get "count"(): integer
get "data"(): $CompoundTag
get "output"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeOutputs$RecipeOutput$Type<T> = ($RecipeOutputs$RecipeOutput<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeOutputs$RecipeOutput_<T> = $RecipeOutputs$RecipeOutput$Type<(T)>;
}}
declare module "packages/com/almostreliable/morejs/util/$WeightedList$Builder" {
import {$WeightedList, $WeightedList$Type} from "packages/com/almostreliable/morejs/util/$WeightedList"

export class $WeightedList$Builder<T> {

constructor()

public "add"(arg0: integer, arg1: T): $WeightedList$Builder<(T)>
public "build"(): $WeightedList<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WeightedList$Builder$Type<T> = ($WeightedList$Builder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WeightedList$Builder_<T> = $WeightedList$Builder$Type<(T)>;
}}
declare module "packages/com/almostreliable/morejs/$MoreJSForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MoreJSForge {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MoreJSForge$Type = ($MoreJSForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MoreJSForge_ = $MoreJSForge$Type;
}}
declare module "packages/com/almostreliable/lootjs/kube/$LootConditionsContainer" {
import {$EntityPredicateBuilderJS, $EntityPredicateBuilderJS$Type} from "packages/com/almostreliable/lootjs/kube/builder/$EntityPredicateBuilderJS"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$DamageSourcePredicateBuilderJS, $DamageSourcePredicateBuilderJS$Type} from "packages/com/almostreliable/lootjs/kube/builder/$DamageSourcePredicateBuilderJS"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$ILootCondition, $ILootCondition$Type} from "packages/com/almostreliable/lootjs/core/$ILootCondition"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$DistancePredicateBuilder, $DistancePredicateBuilder$Type} from "packages/com/almostreliable/lootjs/loot/condition/builder/$DistancePredicateBuilder"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$MinMaxBounds$Doubles, $MinMaxBounds$Doubles$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Doubles"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$LootItemCondition$Builder, $LootItemCondition$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemCondition$Builder"
import {$Resolver, $Resolver$Type} from "packages/com/almostreliable/lootjs/filters/$Resolver"
import {$ItemFilter, $ItemFilter$Type} from "packages/com/almostreliable/lootjs/filters/$ItemFilter"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $LootConditionsContainer<B extends $LootConditionsContainer<(any)>> {

 "or"(action: $Consumer$Type<($LootConditionsContainer$Type<(B)>)>): B
 "and"(action: $Consumer$Type<($LootConditionsContainer$Type<(B)>)>): B
 "not"(action: $Consumer$Type<($LootConditionsContainer$Type<(B)>)>): B
 "survivesExplosion"(): B
 "addCondition"(arg0: $ILootCondition$Type): B
 "addCondition"(builder: $LootItemCondition$Builder$Type): B
 "randomChance"(value: float): B
 "randomChanceWithLooting"(value: float, looting: float): B
 "killedByPlayer"(): B
 "lightLevel"(min: integer, max: integer): B
 "entityPredicate"(predicate: $Predicate$Type<($Entity$Type)>): B
 "matchLoot"(filter: $ItemFilter$Type): B
 "matchLoot"(filter: $ItemFilter$Type, exact: boolean): B
 "matchEquip"(slot: $EquipmentSlot$Type, filter: $ItemFilter$Type): B
 "matchMainHand"(filter: $ItemFilter$Type): B
 "matchOffHand"(filter: $ItemFilter$Type): B
 "timeCheck"(min: integer, max: integer): B
 "timeCheck"(period: long, min: integer, max: integer): B
 "anyBiome"(...resolvers: ($Resolver$Type)[]): B
 "anyDimension"(...dimensions: ($ResourceLocation$Type)[]): B
 "randomTableBonus"(enchantment: $Enchantment$Type, chances: (float)[]): B
 "anyStructure"(idOrTags: (string)[], exact: boolean): B
 "weatherCheck"(map: $Map$Type<(string), (boolean)>): B
 "killerPredicate"(predicate: $Predicate$Type<($Entity$Type)>): B
 "matchPlayer"(action: $Consumer$Type<($EntityPredicateBuilderJS$Type)>): B
 "hasAnyStage"(...stages: (string)[]): B
 "distanceToKiller"(bounds: $MinMaxBounds$Doubles$Type): B
 "matchDirectKiller"(action: $Consumer$Type<($EntityPredicateBuilderJS$Type)>): B
 "matchBlockState"(block: $Block$Type, propertyMap: $Map$Type<(string), (string)>): B
 "matchDamageSource"(action: $Consumer$Type<($DamageSourcePredicateBuilderJS$Type)>): B
 "matchFluid"(resolver: $Resolver$Type): B
 "playerPredicate"(predicate: $Predicate$Type<($ServerPlayer$Type)>): B
 "matchEntity"(action: $Consumer$Type<($EntityPredicateBuilderJS$Type)>): B
 "matchKiller"(action: $Consumer$Type<($EntityPredicateBuilderJS$Type)>): B
 "createConditions"(action: $Consumer$Type<($LootConditionsContainer$Type<(B)>)>): $List<($ILootCondition)>
 "customCondition"(json: $JsonObject$Type): B
 "biome"(...resolvers: ($Resolver$Type)[]): B
 "randomChanceWithEnchantment"(enchantment: $Enchantment$Type, chances: (float)[]): B
 "blockEntityPredicate"(predicate: $Predicate$Type<($BlockEntity$Type)>): B
 "customDistanceToPlayer"(action: $Consumer$Type<($DistancePredicateBuilder$Type)>): B
 "directKillerPredicate"(predicate: $Predicate$Type<($Entity$Type)>): B

(action: $Consumer$Type<($LootConditionsContainer$Type<(B)>)>): B
}

export namespace $LootConditionsContainer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootConditionsContainer$Type<B> = ($LootConditionsContainer<(B)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootConditionsContainer_<B> = $LootConditionsContainer$Type<(B)>;
}}
declare module "packages/com/almostreliable/lootjs/core/$ILootAction" {
import {$ILootHandler, $ILootHandler$Type} from "packages/com/almostreliable/lootjs/core/$ILootHandler"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $ILootAction extends $ILootHandler {

 "applyLootHandler"(arg0: $LootContext$Type, arg1: $List$Type<($ItemStack$Type)>): boolean

(arg0: $LootContext$Type, arg1: $List$Type<($ItemStack$Type)>): boolean
}

export namespace $ILootAction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ILootAction$Type = ($ILootAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ILootAction_ = $ILootAction$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/altar/$AltarBlock" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$EntityBlock, $EntityBlock$Type} from "packages/net/minecraft/world/level/block/$EntityBlock"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$SimpleWaterloggedBlock, $SimpleWaterloggedBlock$Type} from "packages/net/minecraft/world/level/block/$SimpleWaterloggedBlock"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"
import {$GameEventListener, $GameEventListener$Type} from "packages/net/minecraft/world/level/gameevent/$GameEventListener"

export class $AltarBlock extends $Block implements $SimpleWaterloggedBlock, $EntityBlock {
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(properties: $BlockBehaviour$Properties$Type)

public "getTicker"<T extends $BlockEntity>(level: $Level$Type, state: $BlockState$Type, type: $BlockEntityType$Type<(T)>): $BlockEntityTicker<(T)>
public "getStateForPlacement"(context: $BlockPlaceContext$Type): $BlockState
public "playerWillDestroy"(level: $Level$Type, pos: $BlockPos$Type, state: $BlockState$Type, player: $Player$Type): void
public "updateShape"(state: $BlockState$Type, direction: $Direction$Type, nState: $BlockState$Type, level: $LevelAccessor$Type, pos: $BlockPos$Type, nPos: $BlockPos$Type): $BlockState
public "use"(state: $BlockState$Type, level: $Level$Type, pos: $BlockPos$Type, player: $Player$Type, hand: $InteractionHand$Type, hit: $BlockHitResult$Type): $InteractionResult
public "getFluidState"(state: $BlockState$Type): $FluidState
public "getShape"(state: $BlockState$Type, level: $BlockGetter$Type, pos: $BlockPos$Type, context: $CollisionContext$Type): $VoxelShape
public "animateTick"(state: $BlockState$Type, level: $Level$Type, pos: $BlockPos$Type, random: $RandomSource$Type): void
public "newBlockEntity"(pos: $BlockPos$Type, state: $BlockState$Type): $BlockEntity
public "getPickupSound"(): $Optional<($SoundEvent)>
public "canPlaceLiquid"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $Fluid$Type): boolean
public "placeLiquid"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $FluidState$Type): boolean
public "pickupBlock"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): $ItemStack
public "getListener"<T extends $BlockEntity>(arg0: $ServerLevel$Type, arg1: T): $GameEventListener
public "getPickupSound"(arg0: $BlockState$Type): $Optional<($SoundEvent)>
get "pickupSound"(): $Optional<($SoundEvent)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AltarBlock$Type = ($AltarBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AltarBlock_ = $AltarBlock$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/action/$ModifyLootAction" {
import {$ModifyLootAction$Callback, $ModifyLootAction$Callback$Type} from "packages/com/almostreliable/lootjs/loot/action/$ModifyLootAction$Callback"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ILootAction, $ILootAction$Type} from "packages/com/almostreliable/lootjs/core/$ILootAction"

export class $ModifyLootAction implements $ILootAction {

constructor(predicate: $Predicate$Type<($ItemStack$Type)>, callback: $ModifyLootAction$Callback$Type)

public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModifyLootAction$Type = ($ModifyLootAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModifyLootAction_ = $ModifyLootAction$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/kubejs/$SummoningComponents" {
import {$RecipeOutputs, $RecipeOutputs$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs"
import {$RecipeComponent, $RecipeComponent$Type} from "packages/dev/latvian/mods/kubejs/recipe/component/$RecipeComponent"
import {$RecipeSacrifices, $RecipeSacrifices$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeSacrifices"
import {$BlockReference, $BlockReference$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$BlockReference"

export interface $SummoningComponents {

}

export namespace $SummoningComponents {
const OUTPUTS: $RecipeComponent<($RecipeOutputs)>
const SACRIFICES: $RecipeComponent<($RecipeSacrifices)>
const BLOCK_REFERENCE: $RecipeComponent<($BlockReference)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SummoningComponents$Type = ($SummoningComponents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SummoningComponents_ = $SummoningComponents$Type;
}}
declare module "packages/com/almostreliable/lootjs/core/$LootContextType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $LootContextType extends $Enum<($LootContextType)> {
static readonly "UNKNOWN": $LootContextType
static readonly "BLOCK": $LootContextType
static readonly "ENTITY": $LootContextType
static readonly "CHEST": $LootContextType
static readonly "FISHING": $LootContextType
static readonly "GIFT": $LootContextType
static readonly "PIGLIN_BARTER": $LootContextType
static readonly "ADVANCEMENT_ENTITY": $LootContextType
static readonly "ADVANCEMENT_REWARD": $LootContextType


public static "values"(): ($LootContextType)[]
public static "valueOf"(name: string): $LootContextType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootContextType$Type = (("gift") | ("chest") | ("piglin_barter") | ("advancement_entity") | ("advancement_reward") | ("fishing") | ("block") | ("entity") | ("unknown")) | ($LootContextType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootContextType_ = $LootContextType$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/$ForgeTradingManager" {
import {$TradingManager, $TradingManager$Type} from "packages/com/almostreliable/morejs/features/villager/$TradingManager"

export class $ForgeTradingManager extends $TradingManager {
static readonly "INSTANCE": $ForgeTradingManager

constructor()

public "start"(): void
public "reset"(): void
public "reload"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeTradingManager$Type = ($ForgeTradingManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeTradingManager_ = $ForgeTradingManager$Type;
}}
declare module "packages/com/almostreliable/lootjs/core/$LootEntry" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$LootItemFunction$Builder, $LootItemFunction$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/functions/$LootItemFunction$Builder"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$LootFunctionsContainer, $LootFunctionsContainer$Type} from "packages/com/almostreliable/lootjs/loot/$LootFunctionsContainer"
import {$LootConditionsContainer, $LootConditionsContainer$Type} from "packages/com/almostreliable/lootjs/kube/$LootConditionsContainer"
import {$AddAttributesFunction$Builder, $AddAttributesFunction$Builder$Type} from "packages/com/almostreliable/lootjs/loot/$AddAttributesFunction$Builder"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$NumberProvider, $NumberProvider$Type} from "packages/net/minecraft/world/level/storage/loot/providers/number/$NumberProvider"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$ItemFilter, $ItemFilter$Type} from "packages/com/almostreliable/lootjs/filters/$ItemFilter"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$LootEntry$Generator, $LootEntry$Generator$Type} from "packages/com/almostreliable/lootjs/core/$LootEntry$Generator"

export class $LootEntry implements $LootFunctionsContainer<($LootEntry)> {

constructor(generator: $LootEntry$Generator$Type)
constructor(itemStack: $ItemStack$Type)
constructor(item: $Item$Type)

public "when"(action: $Consumer$Type<($LootConditionsContainer$Type<(any)>)>): $LootEntry
public "matchesConditions"(context: $LootContext$Type): boolean
public "withWeight"(weight: integer): $LootEntry
public "hasWeight"(): boolean
public "getWeight"(): integer
public "withChance"(chance: integer): $LootEntry
public "createItem"(context: $LootContext$Type): $ItemStack
public "setName"(component: $Component$Type): $LootEntry
public "functions"(filter: $ItemFilter$Type, action: $Consumer$Type<($LootFunctionsContainer$Type<($LootEntry$Type)>)>): $LootEntry
public "addAttributes"(action: $Consumer$Type<($AddAttributesFunction$Builder$Type)>): $LootEntry
public "addLore"(...components: ($Component$Type)[]): $LootEntry
public "addFunction"(builder: $LootItemFunction$Builder$Type): $LootEntry
public "damage"(numberProvider: $NumberProvider$Type): $LootEntry
public "simulateExplosionDecay"(): $LootEntry
public "applyBinomialDistributionBonus"(enchantment: $Enchantment$Type, probability: float, n: integer): $LootEntry
public "enchantWithLevels"(numberProvider: $NumberProvider$Type, allowTreasure: boolean): $LootEntry
public "enchantWithLevels"(numberProvider: $NumberProvider$Type): $LootEntry
public "applyLootingBonus"(numberProvider: $NumberProvider$Type): $LootEntry
public "applyOreBonus"(enchantment: $Enchantment$Type): $LootEntry
public "enchantRandomly"(): $LootEntry
public "enchantRandomly"(enchantments: ($Enchantment$Type)[]): $LootEntry
public "smeltLoot"(): $LootEntry
public "addPotion"(potion: $Potion$Type): $LootEntry
public "limitCount"(numberProviderMin: $NumberProvider$Type, numberProviderMax: $NumberProvider$Type): $LootEntry
public "limitCount"(numberProvider: $NumberProvider$Type): $LootEntry
public "replaceLore"(...components: ($Component$Type)[]): $LootEntry
public "addNbt"(tag: $CompoundTag$Type): $LootEntry
public "addNBT"(tag: $CompoundTag$Type): $LootEntry
public "applyBonus"(enchantment: $Enchantment$Type, multiplier: integer): $LootEntry
public "customFunction"(json: $JsonObject$Type): $LootEntry
get "weight"(): integer
set "name"(value: $Component$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootEntry$Type = ($LootEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootEntry_ = $LootEntry$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/gametest/conditions/$OrConditionTest" {
import {$GameTestHelper, $GameTestHelper$Type} from "packages/net/minecraft/gametest/framework/$GameTestHelper"

export class $OrConditionTest {

constructor()

public "orConditionFails"(helper: $GameTestHelper$Type): void
public "orConditionSucceed"(helper: $GameTestHelper$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OrConditionTest$Type = ($OrConditionTest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OrConditionTest_ = $OrConditionTest$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/gametest/$ConditionsContainer" {
import {$GameTestHelper, $GameTestHelper$Type} from "packages/net/minecraft/gametest/framework/$GameTestHelper"

export class $ConditionsContainer {

constructor()

public "entityTarget_Killer"(helper: $GameTestHelper$Type): void
public "entityTarget_Entity"(helper: $GameTestHelper$Type): void
public "entityTarget_DirectKiller"(helper: $GameTestHelper$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConditionsContainer$Type = ($ConditionsContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConditionsContainer_ = $ConditionsContainer$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/viewer/jei/ingredient/item/$JEICatalystRenderer" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$CatalystRenderer, $CatalystRenderer$Type} from "packages/com/almostreliable/summoningrituals/compat/viewer/common/$CatalystRenderer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"

export class $JEICatalystRenderer extends $CatalystRenderer implements $IIngredientRenderer<($ItemStack)> {

constructor(size: integer)

public "getWidth"(): integer
public "getHeight"(): integer
public "getFontRenderer"(arg0: $Minecraft$Type, arg1: $ItemStack$Type): $Font
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JEICatalystRenderer$Type = ($JEICatalystRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEICatalystRenderer_ = $JEICatalystRenderer$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/$TradeItem" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IntRange, $IntRange$Type} from "packages/com/almostreliable/morejs/features/villager/$IntRange"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $TradeItem {
static readonly "EMPTY": $TradeItem

constructor(arg0: $ItemStack$Type, arg1: $IntRange$Type)

public "isEmpty"(): boolean
public static "of"(arg0: $ItemStack$Type): $TradeItem
public static "of"(arg0: $ItemStack$Type, arg1: integer, arg2: integer, arg3: $CompoundTag$Type): $TradeItem
public static "of"(arg0: $ItemStack$Type, arg1: integer): $TradeItem
public static "of"(arg0: $ItemStack$Type, arg1: integer, arg2: integer): $TradeItem
public static "of"(arg0: $ItemStack$Type, arg1: integer, arg2: $CompoundTag$Type): $TradeItem
public "createItemStack"(arg0: $RandomSource$Type): $ItemStack
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TradeItem$Type = ($TradeItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TradeItem_ = $TradeItem$Type;
}}
declare module "packages/com/almostreliable/lootjs/filters/$Resolver" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Resolver {


public static "of"(value: string): $Resolver
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Resolver$Type = ($Resolver);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Resolver_ = $Resolver$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$WrappedDamageSourceCondition" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$IExtendedLootCondition, $IExtendedLootCondition$Type} from "packages/com/almostreliable/lootjs/loot/condition/$IExtendedLootCondition"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$DamageSourcePredicate, $DamageSourcePredicate$Type} from "packages/net/minecraft/advancements/critereon/$DamageSourcePredicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $WrappedDamageSourceCondition implements $IExtendedLootCondition {

constructor(predicate: $DamageSourcePredicate$Type, sourceNames: (string)[])

public "test"(lootContext: $LootContext$Type): boolean
public "serializeToJson"(): $JsonObject
public "getType"(): $LootItemConditionType
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WrappedDamageSourceCondition$Type = ($WrappedDamageSourceCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WrappedDamageSourceCondition_ = $WrappedDamageSourceCondition$Type;
}}
declare module "packages/com/almostreliable/morejs/features/teleport/$TeleportType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $TeleportType extends $Enum<($TeleportType)> {
static readonly "CHORUS_FRUIT": $TeleportType
static readonly "ENDER_PEARL": $TeleportType


public static "values"(): ($TeleportType)[]
public static "valueOf"(arg0: string): $TeleportType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TeleportType$Type = (("ender_pearl") | ("chorus_fruit")) | ($TeleportType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TeleportType_ = $TeleportType$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/network/$SacrificeParticlePacket" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ServerToClientPacket, $ServerToClientPacket$Type} from "packages/com/almostreliable/summoningrituals/network/$ServerToClientPacket"

export class $SacrificeParticlePacket extends $ServerToClientPacket<($SacrificeParticlePacket)> {

constructor(positions: $List$Type<($BlockPos$Type)>)

public "decode"(buffer: $FriendlyByteBuf$Type): $SacrificeParticlePacket
public "encode"(packet: $SacrificeParticlePacket$Type, buffer: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SacrificeParticlePacket$Type = ($SacrificeParticlePacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SacrificeParticlePacket_ = $SacrificeParticlePacket$Type;
}}
declare module "packages/com/almostreliable/morejs/util/$Utils" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"

export class $Utils {

constructor()

public static "cast"<T>(arg0: any): T
public static "cast"<T>(arg0: any, arg1: $Class$Type<(T)>): $Optional<(T)>
public static "format"(arg0: string): string
public static "asList"(arg0: any): $List<(any)>
public static "matchesIngredient"(arg0: $Ingredient$Type, arg1: $Ingredient$Type): boolean
public static "nullableCast"<T>(arg0: any): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Utils$Type = ($Utils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Utils_ = $Utils$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/$Registration$RecipeEntry" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $Registration$RecipeEntry<T extends $Recipe<(any)>> extends $Record {

constructor(type: $RegistryObject$Type<($RecipeType$Type<(T)>)>, serializer: $RegistryObject$Type<(any)>)

public "type"(): $RegistryObject<($RecipeType<(T)>)>
public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "serializer"(): $RegistryObject<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Registration$RecipeEntry$Type<T> = ($Registration$RecipeEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Registration$RecipeEntry_<T> = $Registration$RecipeEntry$Type<(T)>;
}}
declare module "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentState" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $EnchantmentState extends $Enum<($EnchantmentState)> {
static readonly "IDLE": $EnchantmentState
static readonly "STORE_ENCHANTMENTS": $EnchantmentState
static readonly "USE_STORED_ENCHANTMENTS": $EnchantmentState


public static "values"(): ($EnchantmentState)[]
public static "valueOf"(arg0: string): $EnchantmentState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnchantmentState$Type = (("idle") | ("use_stored_enchantments") | ("store_enchantments")) | ($EnchantmentState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnchantmentState_ = $EnchantmentState$Type;
}}
declare module "packages/com/almostreliable/lootjs/kube/$LootModificationEventJS" {
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$LootContextType, $LootContextType$Type} from "packages/com/almostreliable/lootjs/core/$LootContextType"
import {$LootActionsBuilderJS, $LootActionsBuilderJS$Type} from "packages/com/almostreliable/lootjs/kube/builder/$LootActionsBuilderJS"
import {$ResourceLocationFilter, $ResourceLocationFilter$Type} from "packages/com/almostreliable/lootjs/filters/$ResourceLocationFilter"

export class $LootModificationEventJS extends $EventJS {

constructor()

public "disableWitherStarDrop"(): void
public "addEntityLootModifier"(...entities: ($EntityType$Type<(any)>)[]): $LootActionsBuilderJS
public "disableSkeletonHeadDrop"(): void
public "addLootTypeModifier"(...types: ($LootContextType$Type)[]): $LootActionsBuilderJS
public "addBlockLootModifier"(o: any): $LootActionsBuilderJS
public "disableZombieHeadDrop"(): void
public "addLootTableModifier"(...filters: ($ResourceLocationFilter$Type)[]): $LootActionsBuilderJS
public "disableCreeperHeadDrop"(): void
public "disableLootModification"(...filters: ($ResourceLocationFilter$Type)[]): void
public "enableLogging"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootModificationEventJS$Type = ($LootModificationEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootModificationEventJS_ = $LootModificationEventJS$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/kubejs/$AltarRecipeSchema" {
import {$RecipeKey, $RecipeKey$Type} from "packages/dev/latvian/mods/kubejs/recipe/$RecipeKey"
import {$InputItem, $InputItem$Type} from "packages/dev/latvian/mods/kubejs/item/$InputItem"
import {$AltarRecipe$DAY_TIME, $AltarRecipe$DAY_TIME$Type} from "packages/com/almostreliable/summoningrituals/recipe/$AltarRecipe$DAY_TIME"
import {$RecipeSchema, $RecipeSchema$Type} from "packages/dev/latvian/mods/kubejs/recipe/schema/$RecipeSchema"
import {$RecipeOutputs, $RecipeOutputs$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs"
import {$AltarRecipe$WEATHER, $AltarRecipe$WEATHER$Type} from "packages/com/almostreliable/summoningrituals/recipe/$AltarRecipe$WEATHER"
import {$RecipeSacrifices, $RecipeSacrifices$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeSacrifices"
import {$BlockReference, $BlockReference$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$BlockReference"

export interface $AltarRecipeSchema {

}

export namespace $AltarRecipeSchema {
const CATALYST: $RecipeKey<($InputItem)>
const OUTPUTS: $RecipeKey<($RecipeOutputs)>
const INPUTS: $RecipeKey<(($InputItem)[])>
const SACRIFICES: $RecipeKey<($RecipeSacrifices)>
const RECIPE_TIME: $RecipeKey<(integer)>
const BLOCK_BELOW: $RecipeKey<($BlockReference)>
const DAY_TIME: $RecipeKey<($AltarRecipe$DAY_TIME)>
const WEATHER: $RecipeKey<($AltarRecipe$WEATHER)>
const SCHEMA: $RecipeSchema
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AltarRecipeSchema$Type = ($AltarRecipeSchema);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AltarRecipeSchema_ = $AltarRecipeSchema$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/viewer/common/$SizedItemRenderer" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $SizedItemRenderer {


public "render"(guiGraphics: $GuiGraphics$Type, item: $ItemStack$Type): void
public "getTooltip"(stack: $ItemStack$Type, tooltipFlag: $TooltipFlag$Type): $List<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SizedItemRenderer$Type = ($SizedItemRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SizedItemRenderer_ = $SizedItemRenderer$Type;
}}
declare module "packages/com/almostreliable/ponderjs/api/$ExtendedSceneBuilder" {
import {$SceneBuilder$OverlayInstructions, $SceneBuilder$OverlayInstructions$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuilder$OverlayInstructions"
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$SceneBuilder$DebugInstructions, $SceneBuilder$DebugInstructions$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuilder$DebugInstructions"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ExtendedSceneBuilder$ExtendedWorldInstructions, $ExtendedSceneBuilder$ExtendedWorldInstructions$Type} from "packages/com/almostreliable/ponderjs/api/$ExtendedSceneBuilder$ExtendedWorldInstructions"
import {$SoundSource, $SoundSource$Type} from "packages/net/minecraft/sounds/$SoundSource"
import {$SceneBuilder$EffectInstructions, $SceneBuilder$EffectInstructions$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuilder$EffectInstructions"
import {$PonderScene, $PonderScene$Type} from "packages/com/simibubi/create/foundation/ponder/$PonderScene"
import {$SceneBuilder$SpecialInstructions, $SceneBuilder$SpecialInstructions$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuilder$SpecialInstructions"
import {$TextWindowElement$Builder, $TextWindowElement$Builder$Type} from "packages/com/simibubi/create/foundation/ponder/element/$TextWindowElement$Builder"
import {$ParticleInstructions, $ParticleInstructions$Type} from "packages/com/almostreliable/ponderjs/particles/$ParticleInstructions"
import {$SceneBuilder$WorldInstructions, $SceneBuilder$WorldInstructions$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuilder$WorldInstructions"
import {$SceneBuilder, $SceneBuilder$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuilder"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$InputWindowElement, $InputWindowElement$Type} from "packages/com/simibubi/create/foundation/ponder/element/$InputWindowElement"
import {$Pointing, $Pointing$Type} from "packages/com/simibubi/create/foundation/utility/$Pointing"

export class $ExtendedSceneBuilder extends $SceneBuilder {
readonly "overlay": $SceneBuilder$OverlayInstructions
 "world": $SceneBuilder$WorldInstructions
readonly "debug": $SceneBuilder$DebugInstructions
readonly "effects": $SceneBuilder$EffectInstructions
 "special": $SceneBuilder$SpecialInstructions

constructor(arg0: $PonderScene$Type)

public "getDebug"(): $SceneBuilder$DebugInstructions
public "text"(arg0: integer, arg1: string): $TextWindowElement$Builder
public "text"(arg0: integer, arg1: string, arg2: $Vec3$Type): $TextWindowElement$Builder
public "getLevel"(): $ExtendedSceneBuilder$ExtendedWorldInstructions
public "showControls"(arg0: integer, arg1: $Vec3$Type, arg2: $Pointing$Type): $InputWindowElement
public "getWorld"(): $ExtendedSceneBuilder$ExtendedWorldInstructions
public "getOverlay"(): $SceneBuilder$OverlayInstructions
public "getEffects"(): $SceneBuilder$EffectInstructions
public "playSound"(arg0: $SoundEvent$Type): void
public "playSound"(arg0: $SoundEvent$Type, arg1: float): void
public "playSound"(arg0: $SoundEvent$Type, arg1: $SoundSource$Type, arg2: float, arg3: float): void
public "getParticles"(): $ParticleInstructions
public "showStructure"(): void
public "showStructure"(arg0: integer): void
public "getSpecial"(): $SceneBuilder$SpecialInstructions
public "encapsulateBounds"(arg0: $BlockPos$Type): void
public "sharedText"(arg0: integer, arg1: $ResourceLocation$Type, arg2: $Vec3$Type): $TextWindowElement$Builder
public "sharedText"(arg0: integer, arg1: $ResourceLocation$Type): $TextWindowElement$Builder
get "debug"(): $SceneBuilder$DebugInstructions
get "level"(): $ExtendedSceneBuilder$ExtendedWorldInstructions
get "world"(): $ExtendedSceneBuilder$ExtendedWorldInstructions
get "overlay"(): $SceneBuilder$OverlayInstructions
get "effects"(): $SceneBuilder$EffectInstructions
get "particles"(): $ParticleInstructions
get "special"(): $SceneBuilder$SpecialInstructions
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtendedSceneBuilder$Type = ($ExtendedSceneBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtendedSceneBuilder_ = $ExtendedSceneBuilder$Type;
}}
declare module "packages/com/almostreliable/morejs/$ForgeEventLoaders" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"

export class $ForgeEventLoaders {

constructor()

public static "load"(arg0: $IEventBus$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeEventLoaders$Type = ($ForgeEventLoaders);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeEventLoaders_ = $ForgeEventLoaders$Type;
}}
declare module "packages/com/almostreliable/lootjs/predicate/$ExtendedEntityFlagsPredicate$Builder" {
import {$ExtendedEntityFlagsPredicate$IBuilder, $ExtendedEntityFlagsPredicate$IBuilder$Type} from "packages/com/almostreliable/lootjs/predicate/$ExtendedEntityFlagsPredicate$IBuilder"
import {$ExtendedEntityFlagsPredicate, $ExtendedEntityFlagsPredicate$Type} from "packages/com/almostreliable/lootjs/predicate/$ExtendedEntityFlagsPredicate"
import {$EntityFlagsPredicate$Builder, $EntityFlagsPredicate$Builder$Type} from "packages/net/minecraft/advancements/critereon/$EntityFlagsPredicate$Builder"

export class $ExtendedEntityFlagsPredicate$Builder extends $EntityFlagsPredicate$Builder implements $ExtendedEntityFlagsPredicate$IBuilder<($ExtendedEntityFlagsPredicate)> {
 "isOnFire": boolean
 "isCrouching": boolean
 "isSprinting": boolean
 "isSwimming": boolean
 "isBaby": boolean

constructor()

public "isSprinting"(flag: boolean): $ExtendedEntityFlagsPredicate$Builder
public "isBaby"(flag: boolean): $ExtendedEntityFlagsPredicate$Builder
public "isUnderWater"(flag: boolean): $ExtendedEntityFlagsPredicate$Builder
public "isOnGround"(flag: boolean): $ExtendedEntityFlagsPredicate$Builder
public "isSwimming"(flag: boolean): $ExtendedEntityFlagsPredicate$Builder
public "isCrouching"(flag: boolean): $ExtendedEntityFlagsPredicate$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtendedEntityFlagsPredicate$Builder$Type = ($ExtendedEntityFlagsPredicate$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtendedEntityFlagsPredicate$Builder_ = $ExtendedEntityFlagsPredicate$Builder$Type;
}}
declare module "packages/com/almostreliable/ponderjs/$PonderBuilderJS" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ExtendedPonderStoryBoard, $ExtendedPonderStoryBoard$Type} from "packages/com/almostreliable/ponderjs/api/$ExtendedPonderStoryBoard"
import {$AbstractPonderBuilder, $AbstractPonderBuilder$Type} from "packages/com/almostreliable/ponderjs/api/$AbstractPonderBuilder"

export class $PonderBuilderJS extends $AbstractPonderBuilder<($PonderBuilderJS)> {
static readonly "BASIC_STRUCTURE": string

constructor(arg0: $Set$Type<($Item$Type)>)

public "scene"(arg0: string, arg1: string, arg2: string, arg3: $ExtendedPonderStoryBoard$Type): $PonderBuilderJS
public "scene"(arg0: string, arg1: string, arg2: $ExtendedPonderStoryBoard$Type): $PonderBuilderJS
public "getSelf"(): $PonderBuilderJS
get "self"(): $PonderBuilderJS
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PonderBuilderJS$Type = ($PonderBuilderJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PonderBuilderJS_ = $PonderBuilderJS$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/$TradeTypes" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $TradeTypes extends $Enum<($TradeTypes)> {
static readonly "DyedArmorForEmeralds": $TradeTypes
static readonly "EnchantBookForEmeralds": $TradeTypes
static readonly "EnchantedItemForEmeralds": $TradeTypes
static readonly "ItemsForEmeralds": $TradeTypes
static readonly "ItemsAndEmeraldsToItems": $TradeTypes
static readonly "EmeraldForItems": $TradeTypes
static readonly "TippedArrowForItemsAndEmeralds": $TradeTypes
static readonly "SuspiciousStewForEmeralds": $TradeTypes
static readonly "TreasureMapForEmeralds": $TradeTypes
static readonly "EmeraldsForVillagerTypeItem": $TradeTypes
static readonly "ForgeBasic": $TradeTypes


public static "values"(): ($TradeTypes)[]
public static "valueOf"(arg0: string): $TradeTypes
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TradeTypes$Type = (("treasuremapforemeralds") | ("dyedarmorforemeralds") | ("enchanteditemforemeralds") | ("tippedarrowforitemsandemeralds") | ("emeraldsforvillagertypeitem") | ("forgebasic") | ("emeraldforitems") | ("itemsandemeraldstoitems") | ("suspiciousstewforemeralds") | ("itemsforemeralds") | ("enchantbookforemeralds")) | ($TradeTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TradeTypes_ = $TradeTypes$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/action/$DropExperienceAction" {
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ILootAction, $ILootAction$Type} from "packages/com/almostreliable/lootjs/core/$ILootAction"

export class $DropExperienceAction implements $ILootAction {

constructor(amount: integer)

public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropExperienceAction$Type = ($DropExperienceAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropExperienceAction_ = $DropExperienceAction$Type;
}}
declare module "packages/com/almostreliable/ponderjs/particles/$ParticleTransformation$Simple" {
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export interface $ParticleTransformation$Simple {

 "apply"(arg0: float, arg1: $Vec3$Type): $Vec3

(arg0: float, arg1: $Vec3$Type): $Vec3
}

export namespace $ParticleTransformation$Simple {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleTransformation$Simple$Type = ($ParticleTransformation$Simple);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleTransformation$Simple_ = $ParticleTransformation$Simple$Type;
}}
declare module "packages/com/almostreliable/ponderjs/mixin/$ParticleAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ParticleAccessor {

 "ponderjs$setRoll"(arg0: float): void
 "ponderjs$setAlpha"(arg0: float): void
 "ponderjs$setGravity"(arg0: float): void
 "ponderjs$setHasPhysics"(arg0: boolean): void
 "ponderjs$setFriction"(arg0: float): void
 "ponderjs$setStoppedByCollision"(arg0: boolean): void
 "ponderjs$setLifetime"(arg0: integer): void
}

export namespace $ParticleAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleAccessor$Type = ($ParticleAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleAccessor_ = $ParticleAccessor$Type;
}}
declare module "packages/com/almostreliable/morejs/util/$WeightedList" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"

export class $WeightedList<T> {


public "map"<T2>(arg0: $Function$Type<(T), (T2)>): $WeightedList<(T2)>
public "roll"(): T
public "roll"(arg0: $RandomSource$Type): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WeightedList$Type<T> = ($WeightedList<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WeightedList_<T> = $WeightedList$Type<(T)>;
}}
declare module "packages/com/almostreliable/morejs/features/structure/$EntityInfoWrapper" {
import {$StructureTemplate$StructureEntityInfo, $StructureTemplate$StructureEntityInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureEntityInfo"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"

export class $EntityInfoWrapper {

constructor(arg0: $List$Type<($StructureTemplate$StructureEntityInfo$Type)>, arg1: $Vec3i$Type)

public "add"(arg0: $CompoundTag$Type): void
public "forEach"(arg0: $Consumer$Type<($StructureTemplate$StructureEntityInfo$Type)>): void
public "removeIf"(arg0: $Predicate$Type<($StructureTemplate$StructureEntityInfo$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityInfoWrapper$Type = ($EntityInfoWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityInfoWrapper_ = $EntityInfoWrapper$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$ItemOutput" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$RecipeOutputs$RecipeOutput, $RecipeOutputs$RecipeOutput$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$RecipeOutput"

export class $RecipeOutputs$ItemOutput extends $RecipeOutputs$RecipeOutput<($ItemStack)> {


public "getCount"(): integer
get "count"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeOutputs$ItemOutput$Type = ($RecipeOutputs$ItemOutput);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeOutputs$ItemOutput_ = $RecipeOutputs$ItemOutput$Type;
}}
declare module "packages/com/almostreliable/morejs/features/misc/$PiglinPlayerBehaviorEventJS$PiglinBehavior" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $PiglinPlayerBehaviorEventJS$PiglinBehavior extends $Enum<($PiglinPlayerBehaviorEventJS$PiglinBehavior)> {
static readonly "ATTACK": $PiglinPlayerBehaviorEventJS$PiglinBehavior
static readonly "IGNORE": $PiglinPlayerBehaviorEventJS$PiglinBehavior
static readonly "KEEP": $PiglinPlayerBehaviorEventJS$PiglinBehavior


public static "values"(): ($PiglinPlayerBehaviorEventJS$PiglinBehavior)[]
public static "valueOf"(arg0: string): $PiglinPlayerBehaviorEventJS$PiglinBehavior
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PiglinPlayerBehaviorEventJS$PiglinBehavior$Type = (("attack") | ("keep") | ("ignore")) | ($PiglinPlayerBehaviorEventJS$PiglinBehavior);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PiglinPlayerBehaviorEventJS$PiglinBehavior_ = $PiglinPlayerBehaviorEventJS$PiglinBehavior$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$IsLightLevel" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$IExtendedLootCondition, $IExtendedLootCondition$Type} from "packages/com/almostreliable/lootjs/loot/condition/$IExtendedLootCondition"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $IsLightLevel implements $IExtendedLootCondition {

constructor(min: integer, max: integer)

public "test"(context: $LootContext$Type): boolean
public "getType"(): $LootItemConditionType
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IsLightLevel$Type = ($IsLightLevel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IsLightLevel_ = $IsLightLevel$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/viewer/common/$MobRenderer" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$MobIngredient, $MobIngredient$Type} from "packages/com/almostreliable/summoningrituals/compat/viewer/common/$MobIngredient"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $MobRenderer {


public "render"(guiGraphics: $GuiGraphics$Type, mob: $MobIngredient$Type): void
public "getTooltip"(mob: $MobIngredient$Type, tooltipFlag: $TooltipFlag$Type): $List<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobRenderer$Type = ($MobRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobRenderer_ = $MobRenderer$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeSacrifices$Sacrifice" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $RecipeSacrifices$Sacrifice extends $Record implements $Predicate<($Entity)> {

constructor(mob: $EntityType$Type<(any)>, count: integer)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "test"(entity: $Entity$Type): boolean
public "count"(): integer
public "mob"(): $EntityType<(any)>
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($Entity)>
public "negate"(): $Predicate<($Entity)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($Entity)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($Entity)>
public static "isEqual"<T>(arg0: any): $Predicate<($Entity)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeSacrifices$Sacrifice$Type = ($RecipeSacrifices$Sacrifice);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeSacrifices$Sacrifice_ = $RecipeSacrifices$Sacrifice$Type;
}}
declare module "packages/com/almostreliable/morejs/features/misc/$ExperiencePlayerEventJS" {
import {$PlayerEventJS, $PlayerEventJS$Type} from "packages/dev/latvian/mods/kubejs/player/$PlayerEventJS"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export class $ExperiencePlayerEventJS extends $PlayerEventJS {

constructor(arg0: $Player$Type, arg1: integer)

public "setAmount"(arg0: integer): void
public "getAmount"(): integer
public "getTotalExperience"(): integer
public "setTotalExperience"(arg0: integer): void
public "willLevelUp"(): boolean
public "setExperienceProgress"(arg0: float): void
public "getXpNeededForNextLevel"(): integer
public "getRemainingExperience"(): integer
public "getExperienceProgress"(): float
public "getExperienceLevel"(): integer
public "setExperienceLevel"(arg0: integer): void
set "amount"(value: integer)
get "amount"(): integer
get "totalExperience"(): integer
set "totalExperience"(value: integer)
set "experienceProgress"(value: float)
get "xpNeededForNextLevel"(): integer
get "remainingExperience"(): integer
get "experienceProgress"(): float
get "experienceLevel"(): integer
set "experienceLevel"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExperiencePlayerEventJS$Type = ($ExperiencePlayerEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExperiencePlayerEventJS_ = $ExperiencePlayerEventJS$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/events/$UpdateVillagerOffersEventJS" {
import {$Villager, $Villager$Type} from "packages/net/minecraft/world/entity/npc/$Villager"
import {$MerchantOffer, $MerchantOffer$Type} from "packages/net/minecraft/world/item/trading/$MerchantOffer"
import {$VillagerData, $VillagerData$Type} from "packages/net/minecraft/world/entity/npc/$VillagerData"
import {$List, $List$Type} from "packages/java/util/$List"
import {$VillagerTrades$ItemListing, $VillagerTrades$ItemListing$Type} from "packages/net/minecraft/world/entity/npc/$VillagerTrades$ItemListing"
import {$UpdateAbstractVillagerOffersEventJS, $UpdateAbstractVillagerOffersEventJS$Type} from "packages/com/almostreliable/morejs/features/villager/events/$UpdateAbstractVillagerOffersEventJS"
import {$MerchantOffers, $MerchantOffers$Type} from "packages/net/minecraft/world/item/trading/$MerchantOffers"
import {$VillagerProfession, $VillagerProfession$Type} from "packages/net/minecraft/world/entity/npc/$VillagerProfession"

export class $UpdateVillagerOffersEventJS extends $UpdateAbstractVillagerOffersEventJS {

constructor(arg0: $Villager$Type, arg1: $MerchantOffers$Type, arg2: ($VillagerTrades$ItemListing$Type)[], arg3: $List$Type<($MerchantOffer$Type)>)

public "getProfession"(): $VillagerProfession
public "getVillagerData"(): $VillagerData
public "getVillagerLevel"(): integer
public "isProfession"(arg0: $VillagerProfession$Type): boolean
get "profession"(): $VillagerProfession
get "villagerData"(): $VillagerData
get "villagerLevel"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UpdateVillagerOffersEventJS$Type = ($UpdateVillagerOffersEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UpdateVillagerOffersEventJS_ = $UpdateVillagerOffersEventJS$Type;
}}
declare module "packages/com/almostreliable/morejs/core/$Events" {
import {$EventHandler, $EventHandler$Type} from "packages/dev/latvian/mods/kubejs/event/$EventHandler"
import {$EventGroup, $EventGroup$Type} from "packages/dev/latvian/mods/kubejs/event/$EventGroup"

export interface $Events {

}

export namespace $Events {
const GROUP: $EventGroup
const VILLAGER_TRADING: $EventHandler
const WANDERING_TRADING: $EventHandler
const PLAYER_START_TRADING: $EventHandler
const FILTER_AVAILABLE_ENCHANTMENTS: $EventHandler
const FILTER_ENCHANTED_BOOK_TRADE: $EventHandler
const UPDATE_ABSTRACT_VILLAGER_OFFERS: $EventHandler
const UPDATE_VILLAGER_OFFERS: $EventHandler
const UPDATE_WANDERER_OFFERS: $EventHandler
const ENCHANTMENT_TABLE_IS_ENCHANTABLE: $EventHandler
const ENCHANTMENT_TABLE_CHANGED: $EventHandler
const ENCHANTMENT_TABLE_ENCHANT: $EventHandler
const ENCHANTMENT_TABLE_TOOLTIP: $EventHandler
const TELEPORT: $EventHandler
const STRUCTURE_LOAD: $EventHandler
const STRUCTURE_AFTER_PLACE: $EventHandler
const XP_CHANGE: $EventHandler
const PIGLIN_PLAYER_BEHAVIOR: $EventHandler
const POTION_BREWING_REGISTER: $EventHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Events$Type = ($Events);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Events_ = $Events$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/filters/$ForgeItemFilter" {
import {$ItemFilter, $ItemFilter$Type} from "packages/com/almostreliable/lootjs/filters/$ItemFilter"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ResourceLocationFilter, $ResourceLocationFilter$Type} from "packages/com/almostreliable/lootjs/filters/$ResourceLocationFilter"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $ForgeItemFilter extends $ItemFilter {

 "test"(arg0: $ItemStack$Type): boolean
 "or"(other: $ItemFilter$Type): $ItemFilter
 "negate"(): $ItemFilter
 "and"(other: $ItemFilter$Type): $ItemFilter
 "or"(arg0: $Predicate$Type<(any)>): $Predicate<($ItemStack)>
 "and"(arg0: $Predicate$Type<(any)>): $Predicate<($ItemStack)>

(...actions: (string)[]): $ItemFilter
}

export namespace $ForgeItemFilter {
function canPerformAnyAction(...actions: (string)[]): $ItemFilter
function canPerformAction(...actions: (string)[]): $ItemFilter
function or(...itemFilters: ($ItemFilter$Type)[]): $ItemFilter
function and(...itemFilters: ($ItemFilter$Type)[]): $ItemFilter
function not(itemFilter: $ItemFilter$Type): $ItemFilter
function hasEnchantment(filter: $ResourceLocationFilter$Type): $ItemFilter
function hasEnchantment(filter: $ResourceLocationFilter$Type, min: integer, max: integer): $ItemFilter
function custom(predicate: $Predicate$Type<($ItemStack$Type)>): $ItemFilter
function equipmentSlot(slot: $EquipmentSlot$Type): $ItemFilter
function not<T>(arg0: $Predicate$Type<(any)>): $Predicate<($ItemStack)>
function isEqual<T>(arg0: any): $Predicate<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeItemFilter$Type = ($ForgeItemFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeItemFilter_ = $ForgeItemFilter$Type;
}}
declare module "packages/com/almostreliable/ponderjs/mixin/$PonderTagRegistryAccessor" {
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$PonderTag, $PonderTag$Type} from "packages/com/simibubi/create/foundation/ponder/$PonderTag"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $PonderTagRegistryAccessor {

 "getTags"(): $Multimap<($ResourceLocation), ($PonderTag)>

(): $Multimap<($ResourceLocation), ($PonderTag)>
}

export namespace $PonderTagRegistryAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PonderTagRegistryAccessor$Type = ($PonderTagRegistryAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PonderTagRegistryAccessor_ = $PonderTagRegistryAccessor$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/action/$GroupedLootAction" {
import {$ILootHandler, $ILootHandler$Type} from "packages/com/almostreliable/lootjs/core/$ILootHandler"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$CompositeLootAction, $CompositeLootAction$Type} from "packages/com/almostreliable/lootjs/loot/action/$CompositeLootAction"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$NumberProvider, $NumberProvider$Type} from "packages/net/minecraft/world/level/storage/loot/providers/number/$NumberProvider"

export class $GroupedLootAction extends $CompositeLootAction {

constructor(numberProvider: $NumberProvider$Type, handlers: $Collection$Type<($ILootHandler$Type)>)

public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GroupedLootAction$Type = ($GroupedLootAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GroupedLootAction_ = $GroupedLootAction$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/$SummoningRitualsClient" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SummoningRitualsClient {

constructor()

public "onInitializeClient"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SummoningRitualsClient$Type = ($SummoningRitualsClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SummoningRitualsClient_ = $SummoningRitualsClient$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$MobOutputBuilder" {
import {$RecipeOutputs$MobOutput, $RecipeOutputs$MobOutput$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$MobOutput"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$RecipeOutputs$RecipeOutputBuilder, $RecipeOutputs$RecipeOutputBuilder$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$RecipeOutputBuilder"

export class $RecipeOutputs$MobOutputBuilder extends $RecipeOutputs$RecipeOutputBuilder {

constructor(mob: $EntityType$Type<(any)>)

public "count"(count: integer): $RecipeOutputs$MobOutputBuilder
public "build"(): $RecipeOutputs$MobOutput
public "mob"(mob: $EntityType$Type<(any)>): $RecipeOutputs$MobOutputBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeOutputs$MobOutputBuilder$Type = ($RecipeOutputs$MobOutputBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeOutputs$MobOutputBuilder_ = $RecipeOutputs$MobOutputBuilder$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/gametest/conditions/$ExtendedEntityFlagsPredicateTest" {
import {$GameTestHelper, $GameTestHelper$Type} from "packages/net/minecraft/gametest/framework/$GameTestHelper"

export class $ExtendedEntityFlagsPredicateTest {

constructor()

public "onGroundTest"(helper: $GameTestHelper$Type): void
public "isUndeadTest"(helper: $GameTestHelper$Type): void
public "isCreatureTest"(helper: $GameTestHelper$Type): void
public "isArthropodTest"(helper: $GameTestHelper$Type): void
public "isWaterMobTest"(helper: $GameTestHelper$Type): void
public "inWaterTest"(helper: $GameTestHelper$Type): void
public "isIllegarTest"(helper: $GameTestHelper$Type): void
public "isMonsterTest"(helper: $GameTestHelper$Type): void
public "inUnderWaterTest"(helper: $GameTestHelper$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtendedEntityFlagsPredicateTest$Type = ($ExtendedEntityFlagsPredicateTest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtendedEntityFlagsPredicateTest_ = $ExtendedEntityFlagsPredicateTest$Type;
}}
declare module "packages/com/almostreliable/morejs/$Debug" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Debug {
static readonly "ENCHANTMENT": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Debug$Type = ($Debug);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Debug_ = $Debug$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/viewer/common/$CatalystRenderer" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$SizedItemRenderer, $SizedItemRenderer$Type} from "packages/com/almostreliable/summoningrituals/compat/viewer/common/$SizedItemRenderer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $CatalystRenderer extends $SizedItemRenderer {

constructor(size: integer)

public "getTooltip"(stack: $ItemStack$Type, tooltipFlag: $TooltipFlag$Type): $List<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CatalystRenderer$Type = ($CatalystRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CatalystRenderer_ = $CatalystRenderer$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/trades/$StewTrade" {
import {$MerchantOffer, $MerchantOffer$Type} from "packages/net/minecraft/world/item/trading/$MerchantOffer"
import {$TradeItem, $TradeItem$Type} from "packages/com/almostreliable/morejs/features/villager/$TradeItem"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$TransformableTrade, $TransformableTrade$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$TransformableTrade"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $StewTrade extends $TransformableTrade<($StewTrade)> {

constructor(arg0: ($TradeItem$Type)[], arg1: ($MobEffect$Type)[], arg2: integer)

public "createOffer"(arg0: $Entity$Type, arg1: $RandomSource$Type): $MerchantOffer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StewTrade$Type = ($StewTrade);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StewTrade_ = $StewTrade$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/viewer/jei/$AltarCategoryJEI" {
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$AltarRecipe, $AltarRecipe$Type} from "packages/com/almostreliable/summoningrituals/recipe/$AltarRecipe"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$AltarCategory, $AltarCategory$Type} from "packages/com/almostreliable/summoningrituals/compat/viewer/common/$AltarCategory"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $AltarCategoryJEI extends $AltarCategory<($IDrawable), ($IIngredientRenderer<($ItemStack)>)> implements $IRecipeCategory<($AltarRecipe)> {


public "getRecipeType"(): $RecipeType<($AltarRecipe)>
public "draw"(recipe: $AltarRecipe$Type, recipeSlotsView: $IRecipeSlotsView$Type, guiGraphics: $GuiGraphics$Type, mouseX: double, mouseY: double): void
public "setRecipe"(builder: $IRecipeLayoutBuilder$Type, recipe: $AltarRecipe$Type, focuses: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "getTooltipStrings"(recipe: $AltarRecipe$Type, slotsView: $IRecipeSlotsView$Type, mX: double, mY: double): $List<($Component)>
public "getWidth"(): integer
public "getHeight"(): integer
public "isHandled"(arg0: $AltarRecipe$Type): boolean
public "getTitle"(): $Component
public "handleInput"(arg0: $AltarRecipe$Type, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "getRegistryName"(arg0: $AltarRecipe$Type): $ResourceLocation
get "recipeType"(): $RecipeType<($AltarRecipe)>
get "background"(): $IDrawable
get "width"(): integer
get "height"(): integer
get "title"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AltarCategoryJEI$Type = ($AltarCategoryJEI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AltarCategoryJEI_ = $AltarCategoryJEI$Type;
}}
declare module "packages/com/almostreliable/lootjs/kube/builder/$DamageSourcePredicateBuilderJS" {
import {$EntityPredicateBuilderJS, $EntityPredicateBuilderJS$Type} from "packages/com/almostreliable/lootjs/kube/builder/$EntityPredicateBuilderJS"
import {$AllOfCondition$Builder, $AllOfCondition$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$AllOfCondition$Builder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$AnyOfCondition$Builder, $AnyOfCondition$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$AnyOfCondition$Builder"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LootItemCondition$Builder, $LootItemCondition$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemCondition$Builder"

export class $DamageSourcePredicateBuilderJS implements $LootItemCondition$Builder {

constructor()

public "is"(tag: $ResourceLocation$Type): $DamageSourcePredicateBuilderJS
public "matchDirectEntity"(action: $Consumer$Type<($EntityPredicateBuilderJS$Type)>): $DamageSourcePredicateBuilderJS
public "matchSourceEntity"(action: $Consumer$Type<($EntityPredicateBuilderJS$Type)>): $DamageSourcePredicateBuilderJS
public "anyType"(...names: (string)[]): $DamageSourcePredicateBuilderJS
public "isNot"(tag: $ResourceLocation$Type): $DamageSourcePredicateBuilderJS
public "and"(arg0: $LootItemCondition$Builder$Type): $AllOfCondition$Builder
public "invert"(): $LootItemCondition$Builder
public "or"(arg0: $LootItemCondition$Builder$Type): $AnyOfCondition$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DamageSourcePredicateBuilderJS$Type = ($DamageSourcePredicateBuilderJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DamageSourcePredicateBuilderJS_ = $DamageSourcePredicateBuilderJS$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/$AddAttributesFunction$Modifier$Builder" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$AddAttributesFunction$Modifier, $AddAttributesFunction$Modifier$Type} from "packages/com/almostreliable/lootjs/loot/$AddAttributesFunction$Modifier"
import {$NumberProvider, $NumberProvider$Type} from "packages/net/minecraft/world/level/storage/loot/providers/number/$NumberProvider"

export class $AddAttributesFunction$Modifier$Builder {

constructor(attribute: $Attribute$Type, amount: $NumberProvider$Type)

public "setName"(name: string): void
public "build"(): $AddAttributesFunction$Modifier
public "setSlots"(slots: ($EquipmentSlot$Type)[]): void
public "setOperation"(operation: $AttributeModifier$Operation$Type): void
public "setUuid"(uuid: $UUID$Type): void
public "setProbability"(probability: float): void
set "name"(value: string)
set "slots"(value: ($EquipmentSlot$Type)[])
set "operation"(value: $AttributeModifier$Operation$Type)
set "uuid"(value: $UUID$Type)
set "probability"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AddAttributesFunction$Modifier$Builder$Type = ($AddAttributesFunction$Modifier$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AddAttributesFunction$Modifier$Builder_ = $AddAttributesFunction$Modifier$Builder$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/gametest/$GameTestTemplates" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $GameTestTemplates {
static readonly "EMPTY": string

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GameTestTemplates$Type = ($GameTestTemplates);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GameTestTemplates_ = $GameTestTemplates$Type;
}}
declare module "packages/com/almostreliable/ponderjs/util/$DyeColorWrapper" {
import {$DyeColor, $DyeColor$Type} from "packages/net/minecraft/world/item/$DyeColor"

export class $DyeColorWrapper {
readonly "mcColor": $DyeColor

constructor(arg0: $DyeColor$Type)

public "getName"(): string
public static "get"(arg0: string): $DyeColorWrapper
public "getId"(): integer
public "getSerializedName"(): string
public "getColorValue"(): integer
public static "byId"(arg0: integer): $DyeColorWrapper
get "name"(): string
get "id"(): integer
get "serializedName"(): string
get "colorValue"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DyeColorWrapper$Type = ($DyeColorWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DyeColorWrapper_ = $DyeColorWrapper$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$MatchKillerDistance" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$IExtendedLootCondition, $IExtendedLootCondition$Type} from "packages/com/almostreliable/lootjs/loot/condition/$IExtendedLootCondition"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$DistancePredicate, $DistancePredicate$Type} from "packages/net/minecraft/advancements/critereon/$DistancePredicate"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $MatchKillerDistance implements $IExtendedLootCondition {

constructor(predicate: $DistancePredicate$Type)

public "test"(context: $LootContext$Type): boolean
public "getType"(): $LootItemConditionType
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MatchKillerDistance$Type = ($MatchKillerDistance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MatchKillerDistance_ = $MatchKillerDistance$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/events/$FilterEnchantedTradeEventJS" {
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$LivingEntityEventJS, $LivingEntityEventJS$Type} from "packages/dev/latvian/mods/kubejs/entity/$LivingEntityEventJS"
import {$AbstractVillager, $AbstractVillager$Type} from "packages/net/minecraft/world/entity/npc/$AbstractVillager"

export class $FilterEnchantedTradeEventJS extends $LivingEntityEventJS {

constructor(arg0: $AbstractVillager$Type, arg1: $RandomSource$Type, arg2: $List$Type<($Enchantment$Type)>)

public "printEnchantments"(): void
public "isWanderer"(): boolean
public "add"(...arg0: ($Enchantment$Type)[]): void
public "remove"(...arg0: ($Enchantment$Type)[]): void
public "getEnchantments"(): $List<($Enchantment)>
public "isVillager"(): boolean
public "getRandom"(): $RandomSource
get "wanderer"(): boolean
get "enchantments"(): $List<($Enchantment)>
get "villager"(): boolean
get "random"(): $RandomSource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FilterEnchantedTradeEventJS$Type = ($FilterEnchantedTradeEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FilterEnchantedTradeEventJS_ = $FilterEnchantedTradeEventJS$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/action/$AddLootAction$AddType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $AddLootAction$AddType extends $Enum<($AddLootAction$AddType)> {
static readonly "DEFAULT": $AddLootAction$AddType
static readonly "SEQUENCE": $AddLootAction$AddType
static readonly "ALTERNATIVES": $AddLootAction$AddType


public static "values"(): ($AddLootAction$AddType)[]
public static "valueOf"(name: string): $AddLootAction$AddType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AddLootAction$AddType$Type = (("sequence") | ("default") | ("alternatives")) | ($AddLootAction$AddType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AddLootAction$AddType_ = $AddLootAction$AddType$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/gametest/$ItemFilterTests" {
import {$GameTestHelper, $GameTestHelper$Type} from "packages/net/minecraft/gametest/framework/$GameTestHelper"

export class $ItemFilterTests {

constructor()

public "enchantmentRegexOrLocation"(helper: $GameTestHelper$Type): void
public "enchantmentFilterWithAndFails"(helper: $GameTestHelper$Type): void
public "enchantmentFilterWithAnd"(helper: $GameTestHelper$Type): void
public "enchantmentMaxInclusive"(helper: $GameTestHelper$Type): void
public "enchantmentMinInclusive"(helper: $GameTestHelper$Type): void
public "enchantmentFilterWithOr"(helper: $GameTestHelper$Type): void
public "simpleTest"(helper: $GameTestHelper$Type): void
public "enchantmentRegex"(helper: $GameTestHelper$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemFilterTests$Type = ($ItemFilterTests);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemFilterTests_ = $ItemFilterTests$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$RecipeOutputBuilder" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$RecipeOutputs$RecipeOutput, $RecipeOutputs$RecipeOutput$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$RecipeOutput"

export class $RecipeOutputs$RecipeOutputBuilder {


public "offset"(x: integer, y: integer, z: integer): $RecipeOutputs$RecipeOutputBuilder
public "data"(data: $CompoundTag$Type): $RecipeOutputs$RecipeOutputBuilder
public "spread"(x: integer, y: integer, z: integer): $RecipeOutputs$RecipeOutputBuilder
public "build"(): $RecipeOutputs$RecipeOutput<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeOutputs$RecipeOutputBuilder$Type = ($RecipeOutputs$RecipeOutputBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeOutputs$RecipeOutputBuilder_ = $RecipeOutputs$RecipeOutputBuilder$Type;
}}
declare module "packages/com/almostreliable/lootjs/filters/$ResourceLocationFilter" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $ResourceLocationFilter extends $Predicate<($ResourceLocation)> {

 "test"(arg0: $ResourceLocation$Type): boolean
 "or"(arg0: $Predicate$Type<(any)>): $Predicate<($ResourceLocation)>
 "negate"(): $Predicate<($ResourceLocation)>
 "and"(arg0: $Predicate$Type<(any)>): $Predicate<($ResourceLocation)>

(arg0: $ResourceLocation$Type): boolean
}

export namespace $ResourceLocationFilter {
function not<T>(arg0: $Predicate$Type<(any)>): $Predicate<($ResourceLocation)>
function isEqual<T>(arg0: any): $Predicate<($ResourceLocation)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResourceLocationFilter$Type = ($ResourceLocationFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResourceLocationFilter_ = $ResourceLocationFilter$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/gametest/conditions/$DimensionCheckTest" {
import {$GameTestHelper, $GameTestHelper$Type} from "packages/net/minecraft/gametest/framework/$GameTestHelper"

export class $DimensionCheckTest {

constructor()

public "AnyDimension_match"(helper: $GameTestHelper$Type): void
public "AnyDimension_fail"(helper: $GameTestHelper$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DimensionCheckTest$Type = ($DimensionCheckTest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DimensionCheckTest_ = $DimensionCheckTest$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/trades/$TransformableTrade$Transformer" {
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$OfferModification, $OfferModification$Type} from "packages/com/almostreliable/morejs/features/villager/$OfferModification"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $TransformableTrade$Transformer {

 "accept"(arg0: $OfferModification$Type, arg1: $Entity$Type, arg2: $RandomSource$Type): void

(arg0: $OfferModification$Type, arg1: $Entity$Type, arg2: $RandomSource$Type): void
}

export namespace $TransformableTrade$Transformer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TransformableTrade$Transformer$Type = ($TransformableTrade$Transformer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TransformableTrade$Transformer_ = $TransformableTrade$Transformer$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/events/$UpdateAbstractVillagerOffersEventJS" {
import {$MerchantOffer, $MerchantOffer$Type} from "packages/net/minecraft/world/item/trading/$MerchantOffer"
import {$VillagerData, $VillagerData$Type} from "packages/net/minecraft/world/entity/npc/$VillagerData"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$VillagerTrades$ItemListing, $VillagerTrades$ItemListing$Type} from "packages/net/minecraft/world/entity/npc/$VillagerTrades$ItemListing"
import {$LivingEntityEventJS, $LivingEntityEventJS$Type} from "packages/dev/latvian/mods/kubejs/entity/$LivingEntityEventJS"
import {$AbstractVillager, $AbstractVillager$Type} from "packages/net/minecraft/world/entity/npc/$AbstractVillager"
import {$MerchantOffers, $MerchantOffers$Type} from "packages/net/minecraft/world/item/trading/$MerchantOffers"
import {$VillagerProfession, $VillagerProfession$Type} from "packages/net/minecraft/world/entity/npc/$VillagerProfession"

export class $UpdateAbstractVillagerOffersEventJS extends $LivingEntityEventJS {

constructor(arg0: $AbstractVillager$Type, arg1: $MerchantOffers$Type, arg2: ($VillagerTrades$ItemListing$Type)[], arg3: $List$Type<($MerchantOffer$Type)>)

public "isWanderer"(): boolean
public "getOffers"(): $MerchantOffers
public "getVillagerTrades"(arg0: $VillagerProfession$Type): $List<($VillagerTrades$ItemListing)>
public "getVillagerTrades"(arg0: $VillagerProfession$Type, arg1: integer): $List<($VillagerTrades$ItemListing)>
public "getWandererTrades"(arg0: integer): $List<($VillagerTrades$ItemListing)>
public "getWandererTrades"(): $List<($VillagerTrades$ItemListing)>
public static "invokeEvent"(arg0: $AbstractVillager$Type, arg1: $MerchantOffers$Type, arg2: ($VillagerTrades$ItemListing$Type)[], arg3: $List$Type<($MerchantOffer$Type)>): void
public "getVillagerData"(): $VillagerData
public "deleteAddedOffers"(): void
public "getAddedOffers"(): $Collection<($MerchantOffer)>
public "addRandomOffer"(): $MerchantOffer
public "addRandomOffer"(arg0: $List$Type<($VillagerTrades$ItemListing$Type)>): $MerchantOffer
public "getUsedTrades"(): $List<($VillagerTrades$ItemListing)>
public "isVillager"(): boolean
get "wanderer"(): boolean
get "offers"(): $MerchantOffers
get "wandererTrades"(): $List<($VillagerTrades$ItemListing)>
get "villagerData"(): $VillagerData
get "addedOffers"(): $Collection<($MerchantOffer)>
get "usedTrades"(): $List<($VillagerTrades$ItemListing)>
get "villager"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UpdateAbstractVillagerOffersEventJS$Type = ($UpdateAbstractVillagerOffersEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UpdateAbstractVillagerOffersEventJS_ = $UpdateAbstractVillagerOffersEventJS$Type;
}}
declare module "packages/com/almostreliable/lootjs/core/$LootModificationByBlock" {
import {$AbstractLootModification, $AbstractLootModification$Type} from "packages/com/almostreliable/lootjs/core/$AbstractLootModification"
import {$ILootHandler, $ILootHandler$Type} from "packages/com/almostreliable/lootjs/core/$ILootHandler"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export class $LootModificationByBlock extends $AbstractLootModification {

constructor(name: string, predicate: $Predicate$Type<($BlockState$Type)>, handlers: $List$Type<($ILootHandler$Type)>)

public "shouldExecute"(context: $LootContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootModificationByBlock$Type = ($LootModificationByBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootModificationByBlock_ = $LootModificationByBlock$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/recipe/$AltarRecipe$WEATHER" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export class $AltarRecipe$WEATHER extends $Enum<($AltarRecipe$WEATHER)> {
static readonly "ANY": $AltarRecipe$WEATHER
static readonly "CLEAR": $AltarRecipe$WEATHER
static readonly "RAIN": $AltarRecipe$WEATHER
static readonly "THUNDER": $AltarRecipe$WEATHER


public static "values"(): ($AltarRecipe$WEATHER)[]
public static "valueOf"(name: string): $AltarRecipe$WEATHER
public "check"(level: $Level$Type, player: $ServerPlayer$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AltarRecipe$WEATHER$Type = (("rain") | ("clear") | ("thunder") | ("any")) | ($AltarRecipe$WEATHER);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AltarRecipe$WEATHER_ = $AltarRecipe$WEATHER$Type;
}}
declare module "packages/com/almostreliable/morejs/features/structure/$StructureTemplateAccess" {
import {$StructureTemplate$StructureEntityInfo, $StructureTemplate$StructureEntityInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureEntityInfo"
import {$StructureTemplate$Palette, $StructureTemplate$Palette$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$Palette"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$List, $List$Type} from "packages/java/util/$List"

export interface $StructureTemplateAccess {

 "getEntities"(): $List<($StructureTemplate$StructureEntityInfo)>
 "getPalettes"(): $List<($StructureTemplate$Palette)>
 "getBorderSize"(): $Vec3i
}

export namespace $StructureTemplateAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureTemplateAccess$Type = ($StructureTemplateAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureTemplateAccess_ = $StructureTemplateAccess$Type;
}}
declare module "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentTableChangedJS" {
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$EnchantmentTableServerEventJS, $EnchantmentTableServerEventJS$Type} from "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentTableServerEventJS"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$EnchantmentMenuProcess, $EnchantmentMenuProcess$Type} from "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentMenuProcess"

export class $EnchantmentTableChangedJS extends $EnchantmentTableServerEventJS {

constructor(arg0: $ItemStack$Type, arg1: $ItemStack$Type, arg2: $Level$Type, arg3: $BlockPos$Type, arg4: $EnchantmentMenuProcess$Type, arg5: $RandomSource$Type)

public "getSize"(): integer
get "size"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnchantmentTableChangedJS$Type = ($EnchantmentTableChangedJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnchantmentTableChangedJS_ = $EnchantmentTableChangedJS$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/$IntRange" {
import {$IntPredicate, $IntPredicate$Type} from "packages/java/util/function/$IntPredicate"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"

export class $IntRange implements $IntPredicate {

constructor(arg0: integer, arg1: integer)
constructor(arg0: integer)

public "test"(arg0: integer): boolean
public static "all"(): $IntRange
public "getMin"(): integer
public "getMax"(): integer
public "getRandom"(arg0: $RandomSource$Type): integer
public "or"(arg0: $IntPredicate$Type): $IntPredicate
public "negate"(): $IntPredicate
public "and"(arg0: $IntPredicate$Type): $IntPredicate
get "min"(): integer
get "max"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntRange$Type = ($IntRange);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntRange_ = $IntRange$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/events/$VillagerTradingEventJS" {
import {$TransformableTrade$Transformer, $TransformableTrade$Transformer$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$TransformableTrade$Transformer"
import {$TradeItem, $TradeItem$Type} from "packages/com/almostreliable/morejs/features/villager/$TradeItem"
import {$IntRange, $IntRange$Type} from "packages/com/almostreliable/morejs/features/villager/$IntRange"
import {$VillagerTradingEventJS$ForEachCallback, $VillagerTradingEventJS$ForEachCallback$Type} from "packages/com/almostreliable/morejs/features/villager/events/$VillagerTradingEventJS$ForEachCallback"
import {$VillagerTrades$ItemListing, $VillagerTrades$ItemListing$Type} from "packages/net/minecraft/world/entity/npc/$VillagerTrades$ItemListing"
import {$TradeFilter, $TradeFilter$Type} from "packages/com/almostreliable/morejs/features/villager/$TradeFilter"
import {$Int2ObjectMap, $Int2ObjectMap$Type} from "packages/it/unimi/dsi/fastutil/ints/$Int2ObjectMap"
import {$VillagerProfession, $VillagerProfession$Type} from "packages/net/minecraft/world/entity/npc/$VillagerProfession"
import {$SimpleTrade, $SimpleTrade$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$SimpleTrade"
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $VillagerTradingEventJS extends $EventJS {

constructor(arg0: $Map$Type<($VillagerProfession$Type), ($Int2ObjectMap$Type<($List$Type<($VillagerTrades$ItemListing$Type)>)>)>)

public "removeTrades"(arg0: $TradeFilter$Type): void
public "getTrades"(arg0: $VillagerProfession$Type, arg1: integer): $List<($VillagerTrades$ItemListing)>
public "removeModdedTrades"(): void
public "removeModdedTrades"(arg0: ($VillagerProfession$Type)[], arg1: $IntRange$Type): void
public "forEachTrades"(arg0: ($VillagerProfession$Type)[], arg1: $IntRange$Type, arg2: $Consumer$Type<($List$Type<($VillagerTrades$ItemListing$Type)>)>): void
public "forEachTrades"(arg0: $VillagerTradingEventJS$ForEachCallback$Type): void
public "addCustomTrade"(arg0: $VillagerProfession$Type, arg1: integer, arg2: $TransformableTrade$Transformer$Type): void
public "addTrade"(arg0: $VillagerProfession$Type, arg1: integer, arg2: ($TradeItem$Type)[], arg3: $TradeItem$Type): $SimpleTrade
public "addTrade"<T extends $VillagerTrades$ItemListing>(arg0: $VillagerProfession$Type, arg1: integer, arg2: T): T
public "removeVanillaTrades"(arg0: ($VillagerProfession$Type)[], arg1: $IntRange$Type): void
public "removeVanillaTrades"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VillagerTradingEventJS$Type = ($VillagerTradingEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VillagerTradingEventJS_ = $VillagerTradingEventJS$Type;
}}
declare module "packages/com/almostreliable/morejs/util/$TriConsumer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $TriConsumer<T1, T2, T3> {

 "accept"(arg0: T1, arg1: T2, arg2: T3): void

(arg0: T1, arg1: T2, arg2: T3): void
}

export namespace $TriConsumer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TriConsumer$Type<T1, T2, T3> = ($TriConsumer<(T1), (T2), (T3)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TriConsumer_<T1, T2, T3> = $TriConsumer$Type<(T1), (T2), (T3)>;
}}
declare module "packages/com/almostreliable/morejs/features/villager/trades/$TransformableTrade" {
import {$MerchantOffer, $MerchantOffer$Type} from "packages/net/minecraft/world/item/trading/$MerchantOffer"
import {$TransformableTrade$Transformer, $TransformableTrade$Transformer$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$TransformableTrade$Transformer"
import {$TradeItem, $TradeItem$Type} from "packages/com/almostreliable/morejs/features/villager/$TradeItem"
import {$VillagerTrades$ItemListing, $VillagerTrades$ItemListing$Type} from "packages/net/minecraft/world/entity/npc/$VillagerTrades$ItemListing"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $TransformableTrade<T extends $VillagerTrades$ItemListing> implements $VillagerTrades$ItemListing {

constructor(arg0: ($TradeItem$Type)[])

public "transform"(arg0: $TransformableTrade$Transformer$Type): T
public "createOffer"(arg0: $Entity$Type, arg1: $RandomSource$Type): $MerchantOffer
public "getOffer"(arg0: $Entity$Type, arg1: $RandomSource$Type): $MerchantOffer
public "maxUses"(arg0: integer): T
public "priceMultiplier"(arg0: float): T
public "villagerExperience"(arg0: integer): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TransformableTrade$Type<T> = ($TransformableTrade<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TransformableTrade_<T> = $TransformableTrade$Type<(T)>;
}}
declare module "packages/com/almostreliable/summoningrituals/util/$GameUtils$ANCHOR" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $GameUtils$ANCHOR extends $Enum<($GameUtils$ANCHOR)> {
static readonly "TOP_LEFT": $GameUtils$ANCHOR
static readonly "TOP_RIGHT": $GameUtils$ANCHOR
static readonly "BOTTOM_LEFT": $GameUtils$ANCHOR
static readonly "BOTTOM_RIGHT": $GameUtils$ANCHOR


public static "values"(): ($GameUtils$ANCHOR)[]
public static "valueOf"(name: string): $GameUtils$ANCHOR
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GameUtils$ANCHOR$Type = (("top_right") | ("top_left") | ("bottom_right") | ("bottom_left")) | ($GameUtils$ANCHOR);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GameUtils$ANCHOR_ = $GameUtils$ANCHOR$Type;
}}
declare module "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentTableTooltipEventJS" {
import {$EnchantmentTableEventJS, $EnchantmentTableEventJS$Type} from "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentTableEventJS"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$EnchantmentMenu, $EnchantmentMenu$Type} from "packages/net/minecraft/world/inventory/$EnchantmentMenu"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EnchantmentInstance, $EnchantmentInstance$Type} from "packages/net/minecraft/world/item/enchantment/$EnchantmentInstance"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $EnchantmentTableTooltipEventJS extends $EnchantmentTableEventJS {

constructor(arg0: $ItemStack$Type, arg1: $ItemStack$Type, arg2: $Level$Type, arg3: $Player$Type, arg4: $EnchantmentMenu$Type, arg5: integer, arg6: $List$Type<(any)>)

public "getSlot"(): integer
public "getLines"(): $List<(any)>
public "getClue"(): $EnchantmentInstance
public "getRequiredLevel"(): integer
public "getClueId"(): $ResourceLocation
get "slot"(): integer
get "lines"(): $List<(any)>
get "clue"(): $EnchantmentInstance
get "requiredLevel"(): integer
get "clueId"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnchantmentTableTooltipEventJS$Type = ($EnchantmentTableTooltipEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnchantmentTableTooltipEventJS_ = $EnchantmentTableTooltipEventJS$Type;
}}
declare module "packages/com/almostreliable/morejs/features/potion/$PotionBrewingRegisterEventForge" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$PotionBrewingRegisterEvent, $PotionBrewingRegisterEvent$Type} from "packages/com/almostreliable/morejs/features/potion/$PotionBrewingRegisterEvent"
import {$IBrewingRecipe, $IBrewingRecipe$Type} from "packages/net/minecraftforge/common/brewing/$IBrewingRecipe"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"

export class $PotionBrewingRegisterEventForge extends $PotionBrewingRegisterEvent {

constructor()

public "addCustomBrewing"(arg0: $Ingredient$Type, arg1: $Ingredient$Type, arg2: $ItemStack$Type): void
public "addPotionBrewing"(arg0: $Ingredient$Type, arg1: $Potion$Type, arg2: $Potion$Type): void
public "removeByCustom"(arg0: $Ingredient$Type, arg1: $Ingredient$Type, arg2: $Ingredient$Type): void
public "removeByCustom"(arg0: $Predicate$Type<($IBrewingRecipe$Type)>): void
public "addContainerRecipe"(arg0: $Item$Type, arg1: $Ingredient$Type, arg2: $Item$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PotionBrewingRegisterEventForge$Type = ($PotionBrewingRegisterEventForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PotionBrewingRegisterEventForge_ = $PotionBrewingRegisterEventForge$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$OrCondition" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$IExtendedLootCondition, $IExtendedLootCondition$Type} from "packages/com/almostreliable/lootjs/loot/condition/$IExtendedLootCondition"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$ILootCondition, $ILootCondition$Type} from "packages/com/almostreliable/lootjs/core/$ILootCondition"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $OrCondition implements $IExtendedLootCondition {

constructor(...conditions: ($ILootCondition$Type)[])

public "test"(context: $LootContext$Type): boolean
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
public "getType"(): $LootItemConditionType
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OrCondition$Type = ($OrCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OrCondition_ = $OrCondition$Type;
}}
declare module "packages/com/almostreliable/lootjs/util/$Utils" {
import {$StatePropertiesPredicate$Builder, $StatePropertiesPredicate$Builder$Type} from "packages/net/minecraft/advancements/critereon/$StatePropertiesPredicate$Builder"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $Utils {

constructor()

public static "createProperties"(block: $Block$Type, propertyMap: $Map$Type<(string), (string)>): $StatePropertiesPredicate$Builder
public static "quote"(rl: $ResourceLocation$Type): string
public static "quote"(s: string): string
public static "quote"(prefix: string, objects: $Collection$Type<(any)>): string
public static "formatItemStack"(itemStack: $ItemStack$Type): string
public static "formatEntity"(entity: $Entity$Type): string
public static "formatPosition"(position: $Vec3$Type): string
public static "getClassNameEnding"<T>(t: T): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Utils$Type = ($Utils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Utils_ = $Utils$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/action/$ModifyLootAction$Callback" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $ModifyLootAction$Callback {

 "modify"(arg0: $ItemStack$Type): $ItemStack

(arg0: $ItemStack$Type): $ItemStack
}

export namespace $ModifyLootAction$Callback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModifyLootAction$Callback$Type = ($ModifyLootAction$Callback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModifyLootAction$Callback_ = $ModifyLootAction$Callback$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/$LootJSPlatformForge" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$BindingsEvent, $BindingsEvent$Type} from "packages/dev/latvian/mods/kubejs/script/$BindingsEvent"
import {$LootJSPlatform, $LootJSPlatform$Type} from "packages/com/almostreliable/lootjs/$LootJSPlatform"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $LootJSPlatformForge implements $LootJSPlatform {

constructor()

public "registerBindings"(event: $BindingsEvent$Type): void
public "isDevelopmentEnvironment"(): boolean
public "setQueriedLootTableId"(context: $LootContext$Type, id: $ResourceLocation$Type): void
public "getQueriedLootTableId"(context: $LootContext$Type): $ResourceLocation
public "getPlatformName"(): string
public "isModLoaded"(modId: string): boolean
public "getRegistryName"(block: $Block$Type): $ResourceLocation
public "getRegistryName"(entityType: $EntityType$Type<(any)>): $ResourceLocation
get "developmentEnvironment"(): boolean
get "platformName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootJSPlatformForge$Type = ($LootJSPlatformForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootJSPlatformForge_ = $LootJSPlatformForge$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/viewer/common/$AltarCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"

export class $AltarCategory<I, R> {


public "getIcon"(): I
public "getTitle"(): $Component
get "icon"(): I
get "title"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AltarCategory$Type<I, R> = ($AltarCategory<(I), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AltarCategory_<I, R> = $AltarCategory$Type<(I), (R)>;
}}
declare module "packages/com/almostreliable/lootjs/core/$LootConditionTypes" {
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"

export class $LootConditionTypes {
static readonly "UNUSED": $LootItemConditionType

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootConditionTypes$Type = ($LootConditionTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootConditionTypes_ = $LootConditionTypes$Type;
}}
declare module "packages/com/almostreliable/ponderjs/util/$PonderPlatform" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$FluidParticleData, $FluidParticleData$Type} from "packages/com/simibubi/create/content/fluids/particle/$FluidParticleData"
import {$ParticleType, $ParticleType$Type} from "packages/net/minecraft/core/particles/$ParticleType"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$FluidStackJS, $FluidStackJS$Type} from "packages/dev/latvian/mods/kubejs/fluid/$FluidStackJS"

export class $PonderPlatform {

constructor()

public static "getParticleTypeName"(arg0: $ParticleType$Type<(any)>): $ResourceLocation
public static "getEntityTypeName"(arg0: $EntityType$Type<(any)>): $ResourceLocation
public static "getParticleTypes"(): $Stream<($ParticleType<(any)>)>
public static "getBlockName"(arg0: $Block$Type): $ResourceLocation
public static "getItemName"(arg0: $Item$Type): $ResourceLocation
public static "createFluidParticleData"(arg0: $FluidStackJS$Type, arg1: $ParticleType$Type<(any)>): $FluidParticleData
get "particleTypes"(): $Stream<($ParticleType<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PonderPlatform$Type = ($PonderPlatform);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PonderPlatform_ = $PonderPlatform$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/$Registration" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$AltarBlock, $AltarBlock$Type} from "packages/com/almostreliable/summoningrituals/altar/$AltarBlock"
import {$AltarBlockEntity, $AltarBlockEntity$Type} from "packages/com/almostreliable/summoningrituals/altar/$AltarBlockEntity"
import {$AltarRecipe, $AltarRecipe$Type} from "packages/com/almostreliable/summoningrituals/recipe/$AltarRecipe"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Registration$RecipeEntry, $Registration$RecipeEntry$Type} from "packages/com/almostreliable/summoningrituals/$Registration$RecipeEntry"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $Registration {
static readonly "ALTAR_BLOCK": $RegistryObject<($AltarBlock)>
static readonly "INDESTRUCTIBLE_ALTAR_BLOCK": $RegistryObject<($AltarBlock)>
static readonly "ALTAR_ITEM": $RegistryObject<($Item)>
static readonly "INDESTRUCTIBLE_ALTAR_ITEM": $RegistryObject<($Item)>
static readonly "ALTAR_ENTITY": $RegistryObject<($BlockEntityType<($AltarBlockEntity)>)>
static readonly "ALTAR_RECIPE": $Registration$RecipeEntry<($AltarRecipe)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Registration$Type = ($Registration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Registration_ = $Registration$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/altar/$AltarObservable$Observer" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$AltarRecipe, $AltarRecipe$Type} from "packages/com/almostreliable/summoningrituals/recipe/$AltarRecipe"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $AltarObservable$Observer {

 "run"(arg0: $ServerLevel$Type, arg1: $BlockPos$Type, arg2: $AltarRecipe$Type, arg3: $ServerPlayer$Type): boolean

(arg0: $ServerLevel$Type, arg1: $BlockPos$Type, arg2: $AltarRecipe$Type, arg3: $ServerPlayer$Type): boolean
}

export namespace $AltarObservable$Observer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AltarObservable$Observer$Type = ($AltarObservable$Observer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AltarObservable$Observer_ = $AltarObservable$Observer$Type;
}}
declare module "packages/com/almostreliable/ponderjs/particles/$ParticleInstructions" {
import {$ParticleType, $ParticleType$Type} from "packages/net/minecraft/core/particles/$ParticleType"
import {$Color, $Color$Type} from "packages/dev/latvian/mods/rhino/mod/util/color/$Color"
import {$Direction$Axis, $Direction$Axis$Type} from "packages/net/minecraft/core/$Direction$Axis"
import {$ParticleDataBuilder$DustParticleDataBuilder, $ParticleDataBuilder$DustParticleDataBuilder$Type} from "packages/com/almostreliable/ponderjs/particles/$ParticleDataBuilder$DustParticleDataBuilder"
import {$SceneBuilder, $SceneBuilder$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuilder"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ParticleDataBuilder, $ParticleDataBuilder$Type} from "packages/com/almostreliable/ponderjs/particles/$ParticleDataBuilder"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$FluidStackJS, $FluidStackJS$Type} from "packages/dev/latvian/mods/kubejs/fluid/$FluidStackJS"
import {$ParticleDataBuilder$Static, $ParticleDataBuilder$Static$Type} from "packages/com/almostreliable/ponderjs/particles/$ParticleDataBuilder$Static"

export class $ParticleInstructions {

constructor(arg0: $SceneBuilder$Type)

public "rotationIndicator"(arg0: integer, arg1: $Vec3$Type, arg2: float, arg3: float, arg4: $Direction$Axis$Type): $ParticleDataBuilder<(any), (any)>
public "block"(arg0: integer, arg1: $BlockState$Type, arg2: $Vec3$Type): $ParticleDataBuilder$Static
public "item"(arg0: integer, arg1: $ItemStack$Type, arg2: $Vec3$Type): $ParticleDataBuilder$Static
public "simple"(arg0: integer, arg1: $ParticleType$Type<(any)>, arg2: $Vec3$Type): $ParticleDataBuilder<(any), (any)>
public "basin"(arg0: integer, arg1: $FluidStackJS$Type, arg2: $Vec3$Type): $ParticleDataBuilder<(any), (any)>
public "drip"(arg0: integer, arg1: $FluidStackJS$Type, arg2: $Vec3$Type): $ParticleDataBuilder<(any), (any)>
public "dust"(arg0: integer, arg1: $Color$Type, arg2: $Vec3$Type): $ParticleDataBuilder$DustParticleDataBuilder
public "dust"(arg0: integer, arg1: $Color$Type, arg2: $Color$Type, arg3: $Vec3$Type): $ParticleDataBuilder$DustParticleDataBuilder
public "fluid"(arg0: integer, arg1: $FluidStackJS$Type, arg2: $Vec3$Type): $ParticleDataBuilder<(any), (any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleInstructions$Type = ($ParticleInstructions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleInstructions_ = $ParticleInstructions$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$MatchEquipmentSlot" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$IExtendedLootCondition, $IExtendedLootCondition$Type} from "packages/com/almostreliable/lootjs/loot/condition/$IExtendedLootCondition"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $MatchEquipmentSlot implements $IExtendedLootCondition {

constructor(slot: $EquipmentSlot$Type, predicate: $Predicate$Type<($ItemStack$Type)>)

public "test"(context: $LootContext$Type): boolean
public "getType"(): $LootItemConditionType
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MatchEquipmentSlot$Type = ($MatchEquipmentSlot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MatchEquipmentSlot_ = $MatchEquipmentSlot$Type;
}}
declare module "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentTableEventJS" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$EnchantmentMenu, $EnchantmentMenu$Type} from "packages/net/minecraft/world/inventory/$EnchantmentMenu"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LevelEventJS, $LevelEventJS$Type} from "packages/dev/latvian/mods/kubejs/level/$LevelEventJS"

export class $EnchantmentTableEventJS extends $LevelEventJS {

constructor(arg0: $ItemStack$Type, arg1: $ItemStack$Type, arg2: $Level$Type, arg3: $Player$Type, arg4: $EnchantmentMenu$Type)

public "getLevel"(): $Level
public "getItem"(): $ItemStack
public "getPlayer"(): $Player
public "getSecondItem"(): $ItemStack
get "level"(): $Level
get "item"(): $ItemStack
get "player"(): $Player
get "secondItem"(): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnchantmentTableEventJS$Type = ($EnchantmentTableEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnchantmentTableEventJS_ = $EnchantmentTableEventJS$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/util/$MathUtils" {
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $MathUtils {


public static "modifier"(current: float, max: float, fallback: float): float
public static "isWithinBounds"(mX: double, mY: double, x: integer, y: integer, width: integer, height: integer): boolean
public static "getHorizontalVectors"(...north: ($Vector3f$Type)[]): (($Vector3f)[])[]
public static "shiftToCenter"(pos: $Vec3$Type): $Vec3
public static "shiftToCenter"(pos: $Vec3i$Type): $Vec3
public static "singleRotation"(degree: number): float
public static "vectorFromPos"(pos: $Vec3i$Type): $Vec3
public static "flipCircle"(degree: number): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MathUtils$Type = ($MathUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MathUtils_ = $MathUtils$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$AnyBiomeCheck" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$BiomeCheck, $BiomeCheck$Type} from "packages/com/almostreliable/lootjs/loot/condition/$BiomeCheck"

export class $AnyBiomeCheck extends $BiomeCheck {

constructor(biomes: $List$Type<($ResourceKey$Type<($Biome$Type)>)>, tags: $List$Type<($TagKey$Type<($Biome$Type)>)>)

public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<(T)>
public static "isEqual"<T>(arg0: any): $Predicate<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnyBiomeCheck$Type = ($AnyBiomeCheck);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnyBiomeCheck_ = $AnyBiomeCheck$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/inventory/$ItemHandler" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$TagSerializable, $TagSerializable$Type} from "packages/com/almostreliable/summoningrituals/platform/$TagSerializable"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockContainerJS, $BlockContainerJS$Type} from "packages/dev/latvian/mods/kubejs/level/$BlockContainerJS"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$IItemHandlerModifiable, $IItemHandlerModifiable$Type} from "packages/net/minecraftforge/items/$IItemHandlerModifiable"
import {$IItemHandler, $IItemHandler$Type} from "packages/net/minecraftforge/items/$IItemHandler"

export interface $ItemHandler extends $IItemHandlerModifiable, $TagSerializable<($CompoundTag)> {

 "getNoneEmptyItems"(): $List<($ItemStack)>
 "getCatalyst"(): $ItemStack
 "setStackInSlot"(arg0: integer, arg1: $ItemStack$Type): void
 "deserialize"(arg0: $CompoundTag$Type): void
 "serialize"(): $CompoundTag
 "kjs$self"(): $IItemHandler
 "getBlock"(level: $Level$Type): $BlockContainerJS
 "getSlots"(): integer
 "getStackInSlot"(arg0: integer): $ItemStack
 "insertItem"(arg0: integer, arg1: $ItemStack$Type, arg2: boolean): $ItemStack
 "getSlotLimit"(arg0: integer): integer
 "extractItem"(arg0: integer, arg1: integer, arg2: boolean): $ItemStack
 "isItemValid"(arg0: integer, arg1: $ItemStack$Type): boolean
 "getSlots"(): integer
 "getStackInSlot"(i: integer): $ItemStack
 "insertItem"(i: integer, itemStack: $ItemStack$Type, b: boolean): $ItemStack
 "isMutable"(): boolean
 "extractItem"(i: integer, i1: integer, b: boolean): $ItemStack
 "isItemValid"(i: integer, itemStack: $ItemStack$Type): boolean
 "setStackInSlot"(slot: integer, stack: $ItemStack$Type): void
 "getSlotLimit"(i: integer): integer
 "insertItem"(stack: $ItemStack$Type, simulate: boolean): $ItemStack
 "setChanged"(): void
 "asContainer"(): $Container
 "countNonEmpty"(ingredient: $Ingredient$Type): integer
 "countNonEmpty"(): integer
 "getAllItems"(): $List<($ItemStack)>
 "getHeight"(): integer
 "find"(ingredient: $Ingredient$Type): integer
 "find"(): integer
 "getWidth"(): integer
 "clear"(): void
 "clear"(ingredient: $Ingredient$Type): void
 "count"(ingredient: $Ingredient$Type): integer
 "count"(): integer
 "isEmpty"(): boolean
}

export namespace $ItemHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemHandler$Type = ($ItemHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemHandler_ = $ItemHandler$Type;
}}
declare module "packages/com/almostreliable/morejs/$BuildConfig" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $BuildConfig {
static readonly "MOD_ID": string
static readonly "MOD_VERSION": string
static readonly "MOD_NAME": string
static readonly "MOD_GROUP": string


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuildConfig$Type = ($BuildConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuildConfig_ = $BuildConfig$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/action/$ExplodeAction" {
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$Explosion$BlockInteraction, $Explosion$BlockInteraction$Type} from "packages/net/minecraft/world/level/$Explosion$BlockInteraction"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ILootAction, $ILootAction$Type} from "packages/com/almostreliable/lootjs/core/$ILootAction"

export class $ExplodeAction implements $ILootAction {

constructor(radius: float, mode: $Explosion$BlockInteraction$Type, fire: boolean)

public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExplodeAction$Type = ($ExplodeAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExplodeAction_ = $ExplodeAction$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/builder/$DistancePredicateBuilder" {
import {$DistancePredicate, $DistancePredicate$Type} from "packages/net/minecraft/advancements/critereon/$DistancePredicate"
import {$MinMaxBounds$Doubles, $MinMaxBounds$Doubles$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Doubles"

export class $DistancePredicateBuilder {

constructor()

public "x"(bounds: $MinMaxBounds$Doubles$Type): $DistancePredicateBuilder
public "z"(bounds: $MinMaxBounds$Doubles$Type): $DistancePredicateBuilder
public "y"(bounds: $MinMaxBounds$Doubles$Type): $DistancePredicateBuilder
public "build"(): $DistancePredicate
public "absolute"(bounds: $MinMaxBounds$Doubles$Type): $DistancePredicateBuilder
public "horizontal"(bounds: $MinMaxBounds$Doubles$Type): $DistancePredicateBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DistancePredicateBuilder$Type = ($DistancePredicateBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DistancePredicateBuilder_ = $DistancePredicateBuilder$Type;
}}
declare module "packages/com/almostreliable/morejs/features/structure/$PaletteWrapper" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$StructureTemplate$Palette, $StructureTemplate$Palette$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$Palette"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $PaletteWrapper {

constructor(arg0: $StructureTemplate$Palette$Type, arg1: $Vec3i$Type)

public "add"(arg0: $BlockPos$Type, arg1: $BlockState$Type, arg2: $CompoundTag$Type): void
public "add"(arg0: $BlockPos$Type, arg1: $BlockState$Type): void
public "get"(arg0: $BlockPos$Type): $StructureTemplate$StructureBlockInfo
public "clear"(): void
public "forEach"(arg0: $Consumer$Type<($StructureTemplate$StructureBlockInfo$Type)>): void
public "removeIf"(arg0: $Predicate$Type<($StructureTemplate$StructureBlockInfo$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PaletteWrapper$Type = ($PaletteWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PaletteWrapper_ = $PaletteWrapper$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/viewer/jei/ingredient/mob/$MobHelper" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$MobIngredient, $MobIngredient$Type} from "packages/com/almostreliable/summoningrituals/compat/viewer/common/$MobIngredient"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$UidContext, $UidContext$Type} from "packages/mezz/jei/api/ingredients/subtypes/$UidContext"

export class $MobHelper implements $IIngredientHelper<($MobIngredient)> {

constructor()

public "getDisplayName"(mob: $MobIngredient$Type): string
public "copyIngredient"(mob: $MobIngredient$Type): $MobIngredient
public "getErrorInfo"(mob: $MobIngredient$Type): string
public "getIngredientType"(): $IIngredientType<($MobIngredient)>
public "getUniqueId"(mob: $MobIngredient$Type, context: $UidContext$Type): string
public "getResourceLocation"(mob: $MobIngredient$Type): $ResourceLocation
public "getWildcardId"(arg0: $MobIngredient$Type): string
public "getDisplayModId"(arg0: $MobIngredient$Type): string
public "getTagEquivalent"(arg0: $Collection$Type<($MobIngredient$Type)>): $Optional<($ResourceLocation)>
public "getTagStream"(arg0: $MobIngredient$Type): $Stream<($ResourceLocation)>
public "getCheatItemStack"(arg0: $MobIngredient$Type): $ItemStack
public "isValidIngredient"(arg0: $MobIngredient$Type): boolean
public "normalizeIngredient"(arg0: $MobIngredient$Type): $MobIngredient
public "isIngredientOnServer"(arg0: $MobIngredient$Type): boolean
public "getColors"(arg0: $MobIngredient$Type): $Iterable<(integer)>
get "ingredientType"(): $IIngredientType<($MobIngredient)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobHelper$Type = ($MobHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobHelper_ = $MobHelper$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/gametest/conditions/$IsLightLevelTest" {
import {$GameTestHelper, $GameTestHelper$Type} from "packages/net/minecraft/gametest/framework/$GameTestHelper"

export class $IsLightLevelTest {

constructor()

public "matchLight"(helper: $GameTestHelper$Type): void
public "failMatchLight"(helper: $GameTestHelper$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IsLightLevelTest$Type = ($IsLightLevelTest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IsLightLevelTest_ = $IsLightLevelTest$Type;
}}
declare module "packages/com/almostreliable/morejs/features/enchantment/$PlayerEnchantEventJS" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EnchantmentInstance, $EnchantmentInstance$Type} from "packages/net/minecraft/world/item/enchantment/$EnchantmentInstance"
import {$EnchantmentTableServerEventJS, $EnchantmentTableServerEventJS$Type} from "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentTableServerEventJS"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$EnchantmentMenuProcess, $EnchantmentMenuProcess$Type} from "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentMenuProcess"

export class $PlayerEnchantEventJS extends $EnchantmentTableServerEventJS {

constructor(arg0: integer, arg1: $ItemStack$Type, arg2: $ItemStack$Type, arg3: $Level$Type, arg4: $BlockPos$Type, arg5: $Player$Type, arg6: $EnchantmentMenuProcess$Type)

public "getEnchantments"(): $List<($EnchantmentInstance)>
public "getClickedButton"(): integer
public "getCosts"(): integer
public "getEnchantmentIds"(): $List<($ResourceLocation)>
get "enchantments"(): $List<($EnchantmentInstance)>
get "clickedButton"(): integer
get "costs"(): integer
get "enchantmentIds"(): $List<($ResourceLocation)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEnchantEventJS$Type = ($PlayerEnchantEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEnchantEventJS_ = $PlayerEnchantEventJS$Type;
}}
declare module "packages/com/almostreliable/morejs/features/potion/$PotionBrewingRegisterEvent" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"

export class $PotionBrewingRegisterEvent extends $EventJS {

constructor()

public "addCustomBrewing"(arg0: $Ingredient$Type, arg1: $Ingredient$Type, arg2: $ItemStack$Type): void
public "addPotionBrewing"(arg0: $Ingredient$Type, arg1: $Potion$Type): void
public "addPotionBrewing"(arg0: $Ingredient$Type, arg1: $Potion$Type, arg2: $Potion$Type): void
public "removeByPotion"(arg0: $Potion$Type, arg1: $Ingredient$Type, arg2: $Potion$Type): void
public "validateContainer"(arg0: $Item$Type, arg1: $Ingredient$Type, arg2: $Item$Type): void
public "removeContainer"(arg0: $Ingredient$Type): void
public "addContainerRecipe"(arg0: $Item$Type, arg1: $Ingredient$Type, arg2: $Item$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PotionBrewingRegisterEvent$Type = ($PotionBrewingRegisterEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PotionBrewingRegisterEvent_ = $PotionBrewingRegisterEvent$Type;
}}
declare module "packages/com/almostreliable/lootjs/predicate/$MultiEntityTypePredicate" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EntityTypePredicate, $EntityTypePredicate$Type} from "packages/net/minecraft/advancements/critereon/$EntityTypePredicate"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"

export class $MultiEntityTypePredicate extends $EntityTypePredicate {
static readonly "ANY": $EntityTypePredicate

constructor(tags: $List$Type<($TagKey$Type<($EntityType$Type<(any)>)>)>, types: $List$Type<($EntityType$Type<(any)>)>)

public "matches"(typeToCheck: $EntityType$Type<(any)>): boolean
public "serializeToJson"(): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultiEntityTypePredicate$Type = ($MultiEntityTypePredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultiEntityTypePredicate_ = $MultiEntityTypePredicate$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/results/$Info" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Info {

 "transform"(): string

(): string
}

export namespace $Info {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Info$Type = ($Info);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Info_ = $Info$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/network/$ClientAltarUpdatePacket" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ServerToClientPacket, $ServerToClientPacket$Type} from "packages/com/almostreliable/summoningrituals/network/$ServerToClientPacket"

export class $ClientAltarUpdatePacket extends $ServerToClientPacket<($ClientAltarUpdatePacket)> {


public "decode"(buffer: $FriendlyByteBuf$Type): $ClientAltarUpdatePacket
public "encode"(packet: $ClientAltarUpdatePacket$Type, buffer: $FriendlyByteBuf$Type): void
public static "processTimeUpdate"(pos: $BlockPos$Type, processTime: integer): $ClientAltarUpdatePacket
public static "progressUpdate"(pos: $BlockPos$Type, progress: integer): $ClientAltarUpdatePacket
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientAltarUpdatePacket$Type = ($ClientAltarUpdatePacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientAltarUpdatePacket_ = $ClientAltarUpdatePacket$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/gametest/$BuilderTests" {
import {$GameTestHelper, $GameTestHelper$Type} from "packages/net/minecraft/gametest/framework/$GameTestHelper"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$DamageSourcePredicateBuilderJS, $DamageSourcePredicateBuilderJS$Type} from "packages/com/almostreliable/lootjs/kube/builder/$DamageSourcePredicateBuilderJS"
import {$ExtendedEntityFlagsPredicate$Builder, $ExtendedEntityFlagsPredicate$Builder$Type} from "packages/com/almostreliable/lootjs/predicate/$ExtendedEntityFlagsPredicate$Builder"

export class $BuilderTests {

constructor()

public "damageSourcePredicateBuilderJS_Empty"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isMonster_false"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isIllegarMob_false"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isCrouching_false"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isCrouching"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isMonster"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isUndeadMob"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isSwimming"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isInWater"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isOnGround"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isWaterMob"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isSprinting_false"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isBaby_false"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isOnFire"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isInWater_false"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isOnGround_false"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicateFieldTest"(helper: $GameTestHelper$Type, setter: $BiConsumer$Type<($ExtendedEntityFlagsPredicate$Builder$Type), (boolean)>, key: string, value: boolean): void
public "extendedEntityFlagsPredicate_isSprinting"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isUnderWater"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isWaterMob_false"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isSwimming_false"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isOnFire_false"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isIllegarMob"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isBaby"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isArthropodMob"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isCreature_false"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isUndeadMob_false"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isCreature"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isArthropodMob_false"(helper: $GameTestHelper$Type): void
public "extendedEntityFlagsPredicate_isUnderWater_false"(helper: $GameTestHelper$Type): void
public "damageSourcePredicateFieldTest"(helper: $GameTestHelper$Type, setter: $BiConsumer$Type<($DamageSourcePredicateBuilderJS$Type), (boolean)>, key: string, value: boolean): void
public "extendedEntityFlagsPredicate"(helper: $GameTestHelper$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuilderTests$Type = ($BuilderTests);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuilderTests_ = $BuilderTests$Type;
}}
declare module "packages/com/almostreliable/ponderjs/$PonderEvents" {
import {$EventHandler, $EventHandler$Type} from "packages/dev/latvian/mods/kubejs/event/$EventHandler"
import {$EventGroup, $EventGroup$Type} from "packages/dev/latvian/mods/kubejs/event/$EventGroup"

export interface $PonderEvents {

}

export namespace $PonderEvents {
const GROUP: $EventGroup
const REGISTRY: $EventHandler
const TAGS: $EventHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PonderEvents$Type = ($PonderEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PonderEvents_ = $PonderEvents$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/action/$ReplaceLootAction" {
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LootEntry, $LootEntry$Type} from "packages/com/almostreliable/lootjs/core/$LootEntry"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ILootAction, $ILootAction$Type} from "packages/com/almostreliable/lootjs/core/$ILootAction"

export class $ReplaceLootAction implements $ILootAction {

constructor(predicate: $Predicate$Type<($ItemStack$Type)>, lootEntry: $LootEntry$Type, preserveCount: boolean)

public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReplaceLootAction$Type = ($ReplaceLootAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReplaceLootAction_ = $ReplaceLootAction$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/util/$GameUtils" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$GameUtils$ANCHOR, $GameUtils$ANCHOR$Type} from "packages/com/almostreliable/summoningrituals/util/$GameUtils$ANCHOR"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"

export class $GameUtils {


public static "renderText"(guiGraphics: $GuiGraphics$Type, text: string, anchor: $GameUtils$ANCHOR$Type, x: integer, y: integer, scale: float, color: integer): void
public static "renderCount"(guiGraphics: $GuiGraphics$Type, text: string, x: integer, y: integer): void
public static "sendPlayerMessage"(player: $Player$Type, translationKey: string, color: $ChatFormatting$Type, ...args: (any)[]): void
public static "playSound"(level: $Level$Type, pos: $BlockPos$Type, sound: $SoundEvent$Type): void
public static "dropItem"(level: $Level$Type, pos: $BlockPos$Type, stack: $ItemStack$Type, offset: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GameUtils$Type = ($GameUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GameUtils_ = $GameUtils$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/recipe/component/$BlockReference" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export class $BlockReference implements $Predicate<($BlockState)> {


public "test"(blockState: $BlockState$Type): boolean
public static "fromJson"(json: $JsonObject$Type): $BlockReference
public "toJson"(): $JsonObject
public static "fromNetwork"(buffer: $FriendlyByteBuf$Type): $BlockReference
public "getDisplayState"(): $BlockState
public "toNetwork"(buffer: $FriendlyByteBuf$Type): void
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($BlockState)>
public "negate"(): $Predicate<($BlockState)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($BlockState)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($BlockState)>
public static "isEqual"<T>(arg0: any): $Predicate<($BlockState)>
get "displayState"(): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockReference$Type = ($BlockReference);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockReference_ = $BlockReference$Type;
}}
declare module "packages/com/almostreliable/lootjs/kube/$LootJSPlugin" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$KubeJSPlugin, $KubeJSPlugin$Type} from "packages/dev/latvian/mods/kubejs/$KubeJSPlugin"
import {$BindingsEvent, $BindingsEvent$Type} from "packages/dev/latvian/mods/kubejs/script/$BindingsEvent"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ScriptType, $ScriptType$Type} from "packages/dev/latvian/mods/kubejs/script/$ScriptType"
import {$TypeWrappers, $TypeWrappers$Type} from "packages/dev/latvian/mods/rhino/util/wrap/$TypeWrappers"

export class $LootJSPlugin extends $KubeJSPlugin {

constructor()

public static "valueOf"<T extends $Enum<(T)>>(clazz: $Class$Type<(T)>, o: any): T
public "registerBindings"(event: $BindingsEvent$Type): void
public "initStartup"(): void
public "registerEvents"(): void
public static "eventsAreDisabled"(): boolean
public "registerTypeWrappers"(type: $ScriptType$Type, typeWrappers: $TypeWrappers$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootJSPlugin$Type = ($LootJSPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootJSPlugin_ = $LootJSPlugin$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/$TradeFilter" {
import {$TriConsumer, $TriConsumer$Type} from "packages/com/almostreliable/morejs/util/$TriConsumer"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IntRange, $IntRange$Type} from "packages/com/almostreliable/morejs/features/villager/$IntRange"
import {$TradeTypes, $TradeTypes$Type} from "packages/com/almostreliable/morejs/features/villager/$TradeTypes"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$VillagerProfession, $VillagerProfession$Type} from "packages/net/minecraft/world/entity/npc/$VillagerProfession"

export class $TradeFilter {

constructor(arg0: $Ingredient$Type, arg1: $Ingredient$Type, arg2: $Ingredient$Type)

public "match"(arg0: $ItemStack$Type, arg1: $ItemStack$Type, arg2: $ItemStack$Type, arg3: $TradeTypes$Type): boolean
public "match"(arg0: $ItemStack$Type, arg1: $ItemStack$Type, arg2: $TradeTypes$Type): boolean
public "matchType"(arg0: $TradeTypes$Type): boolean
public "setFirstCountMatcher"(arg0: $IntRange$Type): void
public "setSecondCountMatcher"(arg0: $IntRange$Type): void
public "setOutputCountMatcher"(arg0: $IntRange$Type): void
public "setMerchantLevelMatcher"(arg0: $IntRange$Type): void
public "setTradeTypes"(arg0: $Set$Type<($TradeTypes$Type)>): void
public "setProfessions"(arg0: $Set$Type<($VillagerProfession$Type)>): void
public "matchMerchantLevel"(arg0: integer): boolean
public "matchProfession"(arg0: $VillagerProfession$Type): boolean
public "onMatch"(arg0: $TriConsumer$Type<($ItemStack$Type), ($ItemStack$Type), ($ItemStack$Type)>): void
set "firstCountMatcher"(value: $IntRange$Type)
set "secondCountMatcher"(value: $IntRange$Type)
set "outputCountMatcher"(value: $IntRange$Type)
set "merchantLevelMatcher"(value: $IntRange$Type)
set "tradeTypes"(value: $Set$Type<($TradeTypes$Type)>)
set "professions"(value: $Set$Type<($VillagerProfession$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TradeFilter$Type = ($TradeFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TradeFilter_ = $TradeFilter$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/kubejs/$AlmostKube$OutputWrapper" {
import {$RecipeOutputs$MobOutputBuilder, $RecipeOutputs$MobOutputBuilder$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$MobOutputBuilder"
import {$RecipeOutputs$ItemOutputBuilder, $RecipeOutputs$ItemOutputBuilder$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$ItemOutputBuilder"

export class $AlmostKube$OutputWrapper {


public static "item"(o: any): $RecipeOutputs$ItemOutputBuilder
public static "mob"(o: any): $RecipeOutputs$MobOutputBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AlmostKube$OutputWrapper$Type = ($AlmostKube$OutputWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AlmostKube$OutputWrapper_ = $AlmostKube$OutputWrapper$Type;
}}
declare module "packages/com/almostreliable/lootjs/kube/$LootJSEvent" {
import {$EventHandler, $EventHandler$Type} from "packages/dev/latvian/mods/kubejs/event/$EventHandler"
import {$EventGroup, $EventGroup$Type} from "packages/dev/latvian/mods/kubejs/event/$EventGroup"

export interface $LootJSEvent {

}

export namespace $LootJSEvent {
const GROUP: $EventGroup
const MODIFIERS: $EventHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootJSEvent$Type = ($LootJSEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootJSEvent_ = $LootJSEvent$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/inventory/$AltarInventory" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$AltarRecipe, $AltarRecipe$Type} from "packages/com/almostreliable/summoningrituals/recipe/$AltarRecipe"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockContainerJS, $BlockContainerJS$Type} from "packages/dev/latvian/mods/kubejs/level/$BlockContainerJS"
import {$IItemHandler, $IItemHandler$Type} from "packages/net/minecraftforge/items/$IItemHandler"
import {$ItemHandler, $ItemHandler$Type} from "packages/com/almostreliable/summoningrituals/inventory/$ItemHandler"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PlatformBlockEntity, $PlatformBlockEntity$Type} from "packages/com/almostreliable/summoningrituals/platform/$PlatformBlockEntity"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$VanillaWrapper, $VanillaWrapper$Type} from "packages/com/almostreliable/summoningrituals/inventory/$VanillaWrapper"

export class $AltarInventory implements $ItemHandler {
static readonly "SIZE": integer

constructor(parent: $PlatformBlockEntity$Type)

public "deserialize"(tag: $CompoundTag$Type): void
public "handleRecipe"(recipe: $AltarRecipe$Type): boolean
public "popLastInserted"(): void
public "setCatalyst"(catalyst: $ItemStack$Type): void
public "getNoneEmptyItems"(): $List<($ItemStack)>
public "getVanillaInv"(): $VanillaWrapper
public "dropContents"(): void
public "getCatalyst"(): $ItemStack
public "getSlots"(): integer
public "getStackInSlot"(slot: integer): $ItemStack
public "insertItem"(slot: integer, stack: $ItemStack$Type, simulate: boolean): $ItemStack
public "setStackInSlot"(slot: integer, stack: $ItemStack$Type): void
public "getSlotLimit"(slot: integer): integer
public "extractItem"(slot: integer, amount: integer, simulate: boolean): $ItemStack
public "isItemValid"(slot: integer, stack: $ItemStack$Type): boolean
public "handleInsertion"(stack: $ItemStack$Type): $ItemStack
public "kjs$self"(): $IItemHandler
public "getBlock"(level: $Level$Type): $BlockContainerJS
public "getSlots"(): integer
public "getStackInSlot"(i: integer): $ItemStack
public "insertItem"(i: integer, itemStack: $ItemStack$Type, b: boolean): $ItemStack
public "isMutable"(): boolean
public "extractItem"(i: integer, i1: integer, b: boolean): $ItemStack
public "isItemValid"(i: integer, itemStack: $ItemStack$Type): boolean
public "setStackInSlot"(slot: integer, stack: $ItemStack$Type): void
public "getSlotLimit"(i: integer): integer
public "insertItem"(stack: $ItemStack$Type, simulate: boolean): $ItemStack
public "setChanged"(): void
public "asContainer"(): $Container
public "countNonEmpty"(ingredient: $Ingredient$Type): integer
public "countNonEmpty"(): integer
public "getAllItems"(): $List<($ItemStack)>
public "getHeight"(): integer
public "find"(ingredient: $Ingredient$Type): integer
public "find"(): integer
public "getWidth"(): integer
public "clear"(): void
public "clear"(ingredient: $Ingredient$Type): void
public "count"(ingredient: $Ingredient$Type): integer
public "count"(): integer
public "isEmpty"(): boolean
set "catalyst"(value: $ItemStack$Type)
get "noneEmptyItems"(): $List<($ItemStack)>
get "vanillaInv"(): $VanillaWrapper
get "catalyst"(): $ItemStack
get "slots"(): integer
get "slots"(): integer
get "mutable"(): boolean
get "allItems"(): $List<($ItemStack)>
get "height"(): integer
get "width"(): integer
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AltarInventory$Type = ($AltarInventory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AltarInventory_ = $AltarInventory$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$ItemOutputBuilder" {
import {$RecipeOutputs$RecipeOutputBuilder, $RecipeOutputs$RecipeOutputBuilder$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$RecipeOutputBuilder"
import {$RecipeOutputs$ItemOutput, $RecipeOutputs$ItemOutput$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$ItemOutput"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $RecipeOutputs$ItemOutputBuilder extends $RecipeOutputs$RecipeOutputBuilder {

constructor(stack: $ItemStack$Type)

public "build"(): $RecipeOutputs$ItemOutput
public "item"(item: $ItemStack$Type): $RecipeOutputs$ItemOutputBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeOutputs$ItemOutputBuilder$Type = ($RecipeOutputs$ItemOutputBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeOutputs$ItemOutputBuilder_ = $RecipeOutputs$ItemOutputBuilder$Type;
}}
declare module "packages/com/almostreliable/ponderjs/api/$SceneBuildingUtilDelegate" {
import {$SceneBuildingUtil, $SceneBuildingUtil$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuildingUtil"
import {$SceneBuildingUtil$SelectionUtil, $SceneBuildingUtil$SelectionUtil$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuildingUtil$SelectionUtil"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$SceneBuildingUtil$PositionUtil, $SceneBuildingUtil$PositionUtil$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuildingUtil$PositionUtil"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$SceneBuildingUtil$VectorUtil, $SceneBuildingUtil$VectorUtil$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuildingUtil$VectorUtil"

export class $SceneBuildingUtilDelegate {

constructor(arg0: $SceneBuildingUtil$Type)

public "getSelect"(): $SceneBuildingUtil$SelectionUtil
public "getGrid"(): $SceneBuildingUtil$PositionUtil
public "getDefaultState"(arg0: $Block$Type): $BlockState
public "getVector"(): $SceneBuildingUtil$VectorUtil
get "select"(): $SceneBuildingUtil$SelectionUtil
get "grid"(): $SceneBuildingUtil$PositionUtil
get "vector"(): $SceneBuildingUtil$VectorUtil
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SceneBuildingUtilDelegate$Type = ($SceneBuildingUtilDelegate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SceneBuildingUtilDelegate_ = $SceneBuildingUtilDelegate$Type;
}}
declare module "packages/com/almostreliable/lootjs/predicate/$ExtendedEntityFlagsPredicate" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$EntityFlagsPredicate, $EntityFlagsPredicate$Type} from "packages/net/minecraft/advancements/critereon/$EntityFlagsPredicate"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ExtendedEntityFlagsPredicate extends $EntityFlagsPredicate {
static readonly "ANY": $EntityFlagsPredicate

constructor(isOnFire: boolean, isCrouching: boolean, isSprinting: boolean, isSwimming: boolean, isBaby: boolean, isInWater: boolean, isUnderWater: boolean, isMonster: boolean, isCreature: boolean, isOnGround: boolean, isUndeadMob: boolean, isArthropodMob: boolean, isIllegarMob: boolean, isWaterMob: boolean)

public "matches"(entity: $Entity$Type): boolean
public "serializeToJson"(): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtendedEntityFlagsPredicate$Type = ($ExtendedEntityFlagsPredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtendedEntityFlagsPredicate_ = $ExtendedEntityFlagsPredicate$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/viewer/jei/ingredient/block/$BlockReferenceHelper" {
import {$IIngredientType, $IIngredientType$Type} from "packages/mezz/jei/api/ingredients/$IIngredientType"
import {$IIngredientHelper, $IIngredientHelper$Type} from "packages/mezz/jei/api/ingredients/$IIngredientHelper"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$BlockReference, $BlockReference$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$BlockReference"
import {$UidContext, $UidContext$Type} from "packages/mezz/jei/api/ingredients/subtypes/$UidContext"

export class $BlockReferenceHelper implements $IIngredientHelper<($BlockReference)> {

constructor()

public "getDisplayName"(blockReference: $BlockReference$Type): string
public "copyIngredient"(blockReference: $BlockReference$Type): $BlockReference
public "getErrorInfo"(blockReference: $BlockReference$Type): string
public "getIngredientType"(): $IIngredientType<($BlockReference)>
public "getUniqueId"(blockReference: $BlockReference$Type, context: $UidContext$Type): string
public "getResourceLocation"(blockReference: $BlockReference$Type): $ResourceLocation
public "getWildcardId"(arg0: $BlockReference$Type): string
public "getDisplayModId"(arg0: $BlockReference$Type): string
public "getTagEquivalent"(arg0: $Collection$Type<($BlockReference$Type)>): $Optional<($ResourceLocation)>
public "getTagStream"(arg0: $BlockReference$Type): $Stream<($ResourceLocation)>
public "getCheatItemStack"(arg0: $BlockReference$Type): $ItemStack
public "isValidIngredient"(arg0: $BlockReference$Type): boolean
public "normalizeIngredient"(arg0: $BlockReference$Type): $BlockReference
public "isIngredientOnServer"(arg0: $BlockReference$Type): boolean
public "getColors"(arg0: $BlockReference$Type): $Iterable<(integer)>
get "ingredientType"(): $IIngredientType<($BlockReference)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockReferenceHelper$Type = ($BlockReferenceHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockReferenceHelper_ = $BlockReferenceHelper$Type;
}}
declare module "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentMenuProcess" {
import {$EnchantmentState, $EnchantmentState$Type} from "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentState"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$EnchantmentMenu, $EnchantmentMenu$Type} from "packages/net/minecraft/world/inventory/$EnchantmentMenu"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EnchantmentInstance, $EnchantmentInstance$Type} from "packages/net/minecraft/world/item/enchantment/$EnchantmentInstance"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $EnchantmentMenuProcess {

constructor(arg0: $EnchantmentMenu$Type)

public "getState"(): $EnchantmentState
public "setState"(arg0: $EnchantmentState$Type): void
public "getPlayer"(): $Player
public "setEnchantments"(arg0: integer, arg1: $List$Type<($EnchantmentInstance$Type)>): void
public "getEnchantments"(arg0: integer): $List<($EnchantmentInstance)>
public "clearEnchantments"(): void
public "abortEvent"(arg0: $ItemStack$Type): void
public "isFreezeBroadcast"(): boolean
public "setPlayer"(arg0: $Player$Type): void
public "isItemEnchantable"(arg0: $ItemStack$Type): boolean
public "getCurrentItem"(): $ItemStack
public "matchesCurrentItem"(arg0: $ItemStack$Type): boolean
public "setFreezeBroadcast"(arg0: boolean): void
public "prepareEvent"(arg0: $ItemStack$Type): void
public "setCurrentItem"(arg0: $ItemStack$Type): void
public "getMenu"(): $EnchantmentMenu
public "storeItemIsEnchantable"(arg0: boolean, arg1: $ItemStack$Type): boolean
get "state"(): $EnchantmentState
set "state"(value: $EnchantmentState$Type)
get "player"(): $Player
get "freezeBroadcast"(): boolean
set "player"(value: $Player$Type)
get "currentItem"(): $ItemStack
set "freezeBroadcast"(value: boolean)
set "currentItem"(value: $ItemStack$Type)
get "menu"(): $EnchantmentMenu
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnchantmentMenuProcess$Type = ($EnchantmentMenuProcess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnchantmentMenuProcess_ = $EnchantmentMenuProcess$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/$AddAttributesFunction$Builder" {
import {$LootItemFunction$Builder, $LootItemFunction$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/functions/$LootItemFunction$Builder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$AddAttributesFunction$Modifier$Builder, $AddAttributesFunction$Modifier$Builder$Type} from "packages/com/almostreliable/lootjs/loot/$AddAttributesFunction$Modifier$Builder"
import {$AddAttributesFunction, $AddAttributesFunction$Type} from "packages/com/almostreliable/lootjs/loot/$AddAttributesFunction"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$AddAttributesFunction$Modifier, $AddAttributesFunction$Modifier$Type} from "packages/com/almostreliable/lootjs/loot/$AddAttributesFunction$Modifier"
import {$NumberProvider, $NumberProvider$Type} from "packages/net/minecraft/world/level/storage/loot/providers/number/$NumberProvider"

export class $AddAttributesFunction$Builder implements $LootItemFunction$Builder {

constructor()

public "add"(modifier: $AddAttributesFunction$Modifier$Type): $AddAttributesFunction$Builder
public "add"(attribute: $Attribute$Type, amount: $NumberProvider$Type, action: $Consumer$Type<($AddAttributesFunction$Modifier$Builder$Type)>): $AddAttributesFunction$Builder
public "build"(): $AddAttributesFunction
public "simple"(probability: float, attribute: $Attribute$Type, amount: $NumberProvider$Type): $AddAttributesFunction$Builder
public "simple"(attribute: $Attribute$Type, amount: $NumberProvider$Type): $AddAttributesFunction$Builder
public "preserveDefaults"(flag: boolean): $AddAttributesFunction$Builder
public "forSlots"(probability: float, attribute: $Attribute$Type, amount: $NumberProvider$Type, slots: ($EquipmentSlot$Type)[]): $AddAttributesFunction$Builder
public "forSlots"(attribute: $Attribute$Type, amount: $NumberProvider$Type, slots: ($EquipmentSlot$Type)[]): $AddAttributesFunction$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AddAttributesFunction$Builder$Type = ($AddAttributesFunction$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AddAttributesFunction$Builder_ = $AddAttributesFunction$Builder$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/util/$TextUtils" {
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"

export class $TextUtils {


public static "f"(input: string, ...args: (any)[]): string
public static "colorize"(input: string, color: $ChatFormatting$Type): $MutableComponent
public static "translateAsString"(type: string, key: string): string
public static "translate"(type: string, key: string, ...color: ($ChatFormatting$Type)[]): $MutableComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextUtils$Type = ($TextUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextUtils_ = $TextUtils$Type;
}}
declare module "packages/com/almostreliable/lootjs/core/$ILootContextData" {
import {$LootContextType, $LootContextType$Type} from "packages/com/almostreliable/lootjs/core/$LootContextType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $ILootContextData {

 "reset"(): void
 "setCanceled"(arg0: boolean): void
 "isCanceled"(): boolean
 "getCustomData"(): $Map<(string), (any)>
 "getLootContextType"(): $LootContextType
 "getGeneratedLoot"(): $List<($ItemStack)>
 "setGeneratedLoot"(arg0: $List$Type<($ItemStack$Type)>): void
}

export namespace $ILootContextData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ILootContextData$Type = ($ILootContextData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ILootContextData_ = $ILootContextData$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/kubejs/$SummoningEventJS" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$AltarRecipe, $AltarRecipe$Type} from "packages/com/almostreliable/summoningrituals/recipe/$AltarRecipe"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LevelEventJS, $LevelEventJS$Type} from "packages/dev/latvian/mods/kubejs/level/$LevelEventJS"

export class $SummoningEventJS extends $LevelEventJS {


public "getPlayer"(): $ServerPlayer
public "getPos"(): $BlockPos
public "getRecipe"(): $AltarRecipe
get "player"(): $ServerPlayer
get "pos"(): $BlockPos
get "recipe"(): $AltarRecipe
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SummoningEventJS$Type = ($SummoningEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SummoningEventJS_ = $SummoningEventJS$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$IExtendedLootCondition" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ILootCondition, $ILootCondition$Type} from "packages/com/almostreliable/lootjs/core/$ILootCondition"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LootItemCondition, $LootItemCondition$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemCondition"

export interface $IExtendedLootCondition extends $ILootCondition, $LootItemCondition {

 "getType"(): $LootItemConditionType
 "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
 "test"(arg0: $LootContext$Type): boolean
 "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
 "negate"(): $Predicate<($LootContext)>
 "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
 "validate"(arg0: $ValidationContext$Type): void
 "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>

(): $LootItemConditionType
}

export namespace $IExtendedLootCondition {
function not<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
function isEqual<T>(arg0: any): $Predicate<($LootContext)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IExtendedLootCondition$Type = ($IExtendedLootCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IExtendedLootCondition_ = $IExtendedLootCondition$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/gametest/conditions/$MatchKillerDistanceTest" {
import {$GameTestHelper, $GameTestHelper$Type} from "packages/net/minecraft/gametest/framework/$GameTestHelper"

export class $MatchKillerDistanceTest {

constructor()

public "failDistance"(helper: $GameTestHelper$Type): void
public "matchDistance"(helper: $GameTestHelper$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MatchKillerDistanceTest$Type = ($MatchKillerDistanceTest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MatchKillerDistanceTest_ = $MatchKillerDistanceTest$Type;
}}
declare module "packages/com/almostreliable/lootjs/$PlatformLoader" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PlatformLoader {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformLoader$Type = ($PlatformLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformLoader_ = $PlatformLoader$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/action/$RemoveLootAction" {
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ILootAction, $ILootAction$Type} from "packages/com/almostreliable/lootjs/core/$ILootAction"

export class $RemoveLootAction implements $ILootAction {

constructor(predicate: $Predicate$Type<($ItemStack$Type)>)

public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RemoveLootAction$Type = ($RemoveLootAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RemoveLootAction_ = $RemoveLootAction$Type;
}}
declare module "packages/com/almostreliable/lootjs/$LootModificationsAPI" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocationFilter, $ResourceLocationFilter$Type} from "packages/com/almostreliable/lootjs/filters/$ResourceLocationFilter"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ILootAction, $ILootAction$Type} from "packages/com/almostreliable/lootjs/core/$ILootAction"

export class $LootModificationsAPI {
static readonly "FILTERS": $List<($ResourceLocationFilter)>
static "DEBUG_ACTION": $Consumer<(string)>
static "LOOT_MODIFICATION_LOGGING": boolean
static "DISABLE_WITHER_DROPPING_NETHER_STAR": boolean
static "DISABLE_ZOMBIE_DROPPING_HEAD": boolean
static "DISABLE_SKELETON_DROPPING_HEAD": boolean
static "DISABLE_CREEPER_DROPPING_HEAD": boolean


public static "reload"(): void
public static "invokeActions"(loot: $List$Type<($ItemStack$Type)>, context: $LootContext$Type): void
public static "addModification"(action: $ILootAction$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootModificationsAPI$Type = ($LootModificationsAPI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootModificationsAPI_ = $LootModificationsAPI$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeSacrifices" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$RecipeSacrifices$Sacrifice, $RecipeSacrifices$Sacrifice$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeSacrifices$Sacrifice"

export class $RecipeSacrifices {

constructor()

public "add"(mob: $EntityType$Type<(any)>, count: integer): void
public "get"(index: integer): $RecipeSacrifices$Sacrifice
public "test"(predicate: $Predicate$Type<(any)>): boolean
public "isEmpty"(): boolean
public "size"(): integer
public "getRegion"(pos: $BlockPos$Type): $AABB
public "setRegion"(region: $Vec3i$Type): void
public static "fromJson"(json: $JsonObject$Type): $RecipeSacrifices
public "toJson"(): $JsonElement
public static "fromNetwork"(buffer: $FriendlyByteBuf$Type): $RecipeSacrifices
public "toNetwork"(buffer: $FriendlyByteBuf$Type): void
public "getDisplayRegion"(): string
get "empty"(): boolean
set "region"(value: $Vec3i$Type)
get "displayRegion"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeSacrifices$Type = ($RecipeSacrifices);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeSacrifices_ = $RecipeSacrifices$Type;
}}
declare module "packages/com/almostreliable/ponderjs/mixin/$PonderWorldAccessor" {
import {$Particle, $Particle$Type} from "packages/net/minecraft/client/particle/$Particle"
import {$ParticleOptions, $ParticleOptions$Type} from "packages/net/minecraft/core/particles/$ParticleOptions"

export interface $PonderWorldAccessor {

 "ponderjs$makeParticle"<T extends $ParticleOptions>(arg0: T, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double): $Particle

(arg0: T, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double): $Particle
}

export namespace $PonderWorldAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PonderWorldAccessor$Type = ($PonderWorldAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PonderWorldAccessor_ = $PonderWorldAccessor$Type;
}}
declare module "packages/com/almostreliable/ponderjs/$PonderLang" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"

export class $PonderLang {
static readonly "PATH": string

constructor()

public "generate"(arg0: string): boolean
public "createFromLocalization"(): $JsonObject
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PonderLang$Type = ($PonderLang);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PonderLang_ = $PonderLang$Type;
}}
declare module "packages/com/almostreliable/lootjs/kube/builder/$LootActionsBuilderJS" {
import {$EntityPredicateBuilderJS, $EntityPredicateBuilderJS$Type} from "packages/com/almostreliable/lootjs/kube/builder/$EntityPredicateBuilderJS"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$LootItemFunction, $LootItemFunction$Type} from "packages/net/minecraft/world/level/storage/loot/functions/$LootItemFunction"
import {$LootConditionsContainer, $LootConditionsContainer$Type} from "packages/com/almostreliable/lootjs/kube/$LootConditionsContainer"
import {$AddAttributesFunction$Builder, $AddAttributesFunction$Builder$Type} from "packages/com/almostreliable/lootjs/loot/$AddAttributesFunction$Builder"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ILootAction, $ILootAction$Type} from "packages/com/almostreliable/lootjs/core/$ILootAction"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$NumberProvider, $NumberProvider$Type} from "packages/net/minecraft/world/level/storage/loot/providers/number/$NumberProvider"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"
import {$Resolver, $Resolver$Type} from "packages/com/almostreliable/lootjs/filters/$Resolver"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LootActionsContainer, $LootActionsContainer$Type} from "packages/com/almostreliable/lootjs/loot/$LootActionsContainer"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$ModifyLootAction$Callback, $ModifyLootAction$Callback$Type} from "packages/com/almostreliable/lootjs/loot/action/$ModifyLootAction$Callback"
import {$LootItemFunction$Builder, $LootItemFunction$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/functions/$LootItemFunction$Builder"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$DamageSourcePredicateBuilderJS, $DamageSourcePredicateBuilderJS$Type} from "packages/com/almostreliable/lootjs/kube/builder/$DamageSourcePredicateBuilderJS"
import {$LootFunctionsContainer, $LootFunctionsContainer$Type} from "packages/com/almostreliable/lootjs/loot/$LootFunctionsContainer"
import {$ILootCondition, $ILootCondition$Type} from "packages/com/almostreliable/lootjs/core/$ILootCondition"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$LootEntry, $LootEntry$Type} from "packages/com/almostreliable/lootjs/core/$LootEntry"
import {$DistancePredicateBuilder, $DistancePredicateBuilder$Type} from "packages/com/almostreliable/lootjs/loot/condition/builder/$DistancePredicateBuilder"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$MinMaxBounds$Doubles, $MinMaxBounds$Doubles$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Doubles"
import {$LootItemCondition$Builder, $LootItemCondition$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemCondition$Builder"
import {$ItemFilter, $ItemFilter$Type} from "packages/com/almostreliable/lootjs/filters/$ItemFilter"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$Explosion$BlockInteraction, $Explosion$BlockInteraction$Type} from "packages/net/minecraft/world/level/$Explosion$BlockInteraction"
import {$LootContextJS, $LootContextJS$Type} from "packages/com/almostreliable/lootjs/kube/$LootContextJS"
import {$GroupedLootBuilder, $GroupedLootBuilder$Type} from "packages/com/almostreliable/lootjs/loot/$GroupedLootBuilder"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LootActionsBuilderJS implements $LootConditionsContainer<($LootActionsBuilderJS)>, $LootFunctionsContainer<($LootActionsBuilderJS)>, $LootActionsContainer<($LootActionsBuilderJS)> {
static readonly "DEPRECATED_MSG": string

constructor()

public "group"(callback: $Consumer$Type<($GroupedLootBuilder$Type)>): $LootActionsBuilderJS
public "apply"(action: $Consumer$Type<($LootContextJS$Type)>): $LootActionsBuilderJS
public "pool"(callback: $Consumer$Type<($GroupedLootBuilder$Type)>): $LootActionsBuilderJS
public "addFunction"(lootItemFunction: $LootItemFunction$Type): $LootActionsBuilderJS
public "playerAction"(action: $Consumer$Type<($ServerPlayer$Type)>): $LootActionsBuilderJS
public "logName"(logName: string): $LootActionsBuilderJS
public "getLogName"(alternative: string): string
public "addAction"(action: $ILootAction$Type): $LootActionsBuilderJS
public "or"(action: $Consumer$Type<($LootConditionsContainer$Type<($LootActionsBuilderJS$Type)>)>): $LootActionsBuilderJS
public "and"(action: $Consumer$Type<($LootConditionsContainer$Type<($LootActionsBuilderJS$Type)>)>): $LootActionsBuilderJS
public "not"(action: $Consumer$Type<($LootConditionsContainer$Type<($LootActionsBuilderJS$Type)>)>): $LootActionsBuilderJS
public "survivesExplosion"(): $LootActionsBuilderJS
public "addCondition"(builder: $LootItemCondition$Builder$Type): $LootActionsBuilderJS
public "randomChance"(value: float): $LootActionsBuilderJS
public "randomChanceWithLooting"(value: float, looting: float): $LootActionsBuilderJS
public "killedByPlayer"(): $LootActionsBuilderJS
public "lightLevel"(min: integer, max: integer): $LootActionsBuilderJS
public "entityPredicate"(predicate: $Predicate$Type<($Entity$Type)>): $LootActionsBuilderJS
public "matchLoot"(filter: $ItemFilter$Type): $LootActionsBuilderJS
public "matchLoot"(filter: $ItemFilter$Type, exact: boolean): $LootActionsBuilderJS
public "matchEquip"(slot: $EquipmentSlot$Type, filter: $ItemFilter$Type): $LootActionsBuilderJS
public "matchMainHand"(filter: $ItemFilter$Type): $LootActionsBuilderJS
public "matchOffHand"(filter: $ItemFilter$Type): $LootActionsBuilderJS
public "timeCheck"(min: integer, max: integer): $LootActionsBuilderJS
public "timeCheck"(period: long, min: integer, max: integer): $LootActionsBuilderJS
public "anyBiome"(...resolvers: ($Resolver$Type)[]): $LootActionsBuilderJS
public "anyDimension"(...dimensions: ($ResourceLocation$Type)[]): $LootActionsBuilderJS
public "randomTableBonus"(enchantment: $Enchantment$Type, chances: (float)[]): $LootActionsBuilderJS
public "anyStructure"(idOrTags: (string)[], exact: boolean): $LootActionsBuilderJS
public "weatherCheck"(map: $Map$Type<(string), (boolean)>): $LootActionsBuilderJS
public "killerPredicate"(predicate: $Predicate$Type<($Entity$Type)>): $LootActionsBuilderJS
public "matchPlayer"(action: $Consumer$Type<($EntityPredicateBuilderJS$Type)>): $LootActionsBuilderJS
public "hasAnyStage"(...stages: (string)[]): $LootActionsBuilderJS
public "distanceToKiller"(bounds: $MinMaxBounds$Doubles$Type): $LootActionsBuilderJS
public "matchDirectKiller"(action: $Consumer$Type<($EntityPredicateBuilderJS$Type)>): $LootActionsBuilderJS
public "matchBlockState"(block: $Block$Type, propertyMap: $Map$Type<(string), (string)>): $LootActionsBuilderJS
public "matchDamageSource"(action: $Consumer$Type<($DamageSourcePredicateBuilderJS$Type)>): $LootActionsBuilderJS
public "matchFluid"(resolver: $Resolver$Type): $LootActionsBuilderJS
public "playerPredicate"(predicate: $Predicate$Type<($ServerPlayer$Type)>): $LootActionsBuilderJS
public "matchEntity"(action: $Consumer$Type<($EntityPredicateBuilderJS$Type)>): $LootActionsBuilderJS
public "matchKiller"(action: $Consumer$Type<($EntityPredicateBuilderJS$Type)>): $LootActionsBuilderJS
public "createConditions"(action: $Consumer$Type<($LootConditionsContainer$Type<($LootActionsBuilderJS$Type)>)>): $List<($ILootCondition)>
public "customCondition"(json: $JsonObject$Type): $LootActionsBuilderJS
public "biome"(...resolvers: ($Resolver$Type)[]): $LootActionsBuilderJS
public "randomChanceWithEnchantment"(enchantment: $Enchantment$Type, chances: (float)[]): $LootActionsBuilderJS
public "blockEntityPredicate"(predicate: $Predicate$Type<($BlockEntity$Type)>): $LootActionsBuilderJS
public "customDistanceToPlayer"(action: $Consumer$Type<($DistancePredicateBuilder$Type)>): $LootActionsBuilderJS
public "directKillerPredicate"(predicate: $Predicate$Type<($Entity$Type)>): $LootActionsBuilderJS
public "setName"(component: $Component$Type): $LootActionsBuilderJS
public "functions"(filter: $ItemFilter$Type, action: $Consumer$Type<($LootFunctionsContainer$Type<($LootActionsBuilderJS$Type)>)>): $LootActionsBuilderJS
public "addAttributes"(action: $Consumer$Type<($AddAttributesFunction$Builder$Type)>): $LootActionsBuilderJS
public "addLore"(...components: ($Component$Type)[]): $LootActionsBuilderJS
public "addFunction"(builder: $LootItemFunction$Builder$Type): $LootActionsBuilderJS
public "damage"(numberProvider: $NumberProvider$Type): $LootActionsBuilderJS
public "simulateExplosionDecay"(): $LootActionsBuilderJS
public "applyBinomialDistributionBonus"(enchantment: $Enchantment$Type, probability: float, n: integer): $LootActionsBuilderJS
public "enchantWithLevels"(numberProvider: $NumberProvider$Type, allowTreasure: boolean): $LootActionsBuilderJS
public "enchantWithLevels"(numberProvider: $NumberProvider$Type): $LootActionsBuilderJS
public "applyLootingBonus"(numberProvider: $NumberProvider$Type): $LootActionsBuilderJS
public "applyOreBonus"(enchantment: $Enchantment$Type): $LootActionsBuilderJS
public "enchantRandomly"(): $LootActionsBuilderJS
public "enchantRandomly"(enchantments: ($Enchantment$Type)[]): $LootActionsBuilderJS
public "smeltLoot"(): $LootActionsBuilderJS
public "addPotion"(potion: $Potion$Type): $LootActionsBuilderJS
public "limitCount"(numberProviderMin: $NumberProvider$Type, numberProviderMax: $NumberProvider$Type): $LootActionsBuilderJS
public "limitCount"(numberProvider: $NumberProvider$Type): $LootActionsBuilderJS
public "replaceLore"(...components: ($Component$Type)[]): $LootActionsBuilderJS
public "addNbt"(tag: $CompoundTag$Type): $LootActionsBuilderJS
public "addNBT"(tag: $CompoundTag$Type): $LootActionsBuilderJS
public "applyBonus"(enchantment: $Enchantment$Type, multiplier: integer): $LootActionsBuilderJS
public "customFunction"(json: $JsonObject$Type): $LootActionsBuilderJS
public "replaceLoot"(filter: $ItemFilter$Type, lootEntry: $LootEntry$Type): $LootActionsBuilderJS
public "replaceLoot"(filter: $ItemFilter$Type, lootEntry: $LootEntry$Type, preserveCount: boolean): $LootActionsBuilderJS
public "addSequenceLoot"(...entries: ($LootEntry$Type)[]): $LootActionsBuilderJS
public "removeLoot"(filter: $ItemFilter$Type): $LootActionsBuilderJS
public "addWeightedLoot"(poolEntries: ($LootEntry$Type)[]): $LootActionsBuilderJS
public "addWeightedLoot"(numberProvider: $NumberProvider$Type, poolEntries: ($LootEntry$Type)[]): $LootActionsBuilderJS
public "addWeightedLoot"(numberProvider: $NumberProvider$Type, allowDuplicateLoot: boolean, poolEntries: ($LootEntry$Type)[]): $LootActionsBuilderJS
public "triggerExplosion"(radius: float, destroy: boolean, fire: boolean): $LootActionsBuilderJS
public "triggerExplosion"(radius: float, mode: $Explosion$BlockInteraction$Type, fire: boolean): $LootActionsBuilderJS
public "dropExperience"(amount: integer): $LootActionsBuilderJS
public "modifyLoot"(filter: $ItemFilter$Type, callback: $ModifyLootAction$Callback$Type): $LootActionsBuilderJS
public "addLoot"(...entries: ($LootEntry$Type)[]): $LootActionsBuilderJS
public "addAlternativesLoot"(...entries: ($LootEntry$Type)[]): $LootActionsBuilderJS
public "triggerLightningStrike"(shouldDamage: boolean): $LootActionsBuilderJS
set "name"(value: $Component$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootActionsBuilderJS$Type = ($LootActionsBuilderJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootActionsBuilderJS_ = $LootActionsBuilderJS$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/$LootJSForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $LootJSForge {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootJSForge$Type = ($LootJSForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootJSForge_ = $LootJSForge$Type;
}}
declare module "packages/com/almostreliable/morejs/features/structure/$StructureAfterPlaceEventJS" {
import {$BoundingBox, $BoundingBox$Type} from "packages/net/minecraft/world/level/levelgen/structure/$BoundingBox"
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$ChunkGenerator, $ChunkGenerator$Type} from "packages/net/minecraft/world/level/chunk/$ChunkGenerator"
import {$StructurePieceType, $StructurePieceType$Type} from "packages/net/minecraft/world/level/levelgen/structure/pieces/$StructurePieceType"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$StructurePiece, $StructurePiece$Type} from "packages/net/minecraft/world/level/levelgen/structure/$StructurePiece"
import {$WorldGenLevel, $WorldGenLevel$Type} from "packages/net/minecraft/world/level/$WorldGenLevel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LevelEventJS, $LevelEventJS$Type} from "packages/dev/latvian/mods/kubejs/level/$LevelEventJS"
import {$Structure, $Structure$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$StructureManager, $StructureManager$Type} from "packages/net/minecraft/world/level/$StructureManager"
import {$PiecesContainer, $PiecesContainer$Type} from "packages/net/minecraft/world/level/levelgen/structure/pieces/$PiecesContainer"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $StructureAfterPlaceEventJS extends $LevelEventJS {

constructor(arg0: $Structure$Type, arg1: $WorldGenLevel$Type, arg2: $StructureManager$Type, arg3: $ChunkGenerator$Type, arg4: $RandomSource$Type, arg5: $BoundingBox$Type, arg6: $ChunkPos$Type, arg7: $PiecesContainer$Type)

public "getIntersectionMap"(): $Map<($StructurePiece), ($BoundingBox)>
public "getWorldGenLevel"(): $WorldGenLevel
public "getPiecesContainer"(): $PiecesContainer
public "getChunkGenerator"(): $ChunkGenerator
public "getGenStep"(): string
public "getPieceType"(arg0: $StructurePieceType$Type): $ResourceLocation
public "getId"(): $ResourceLocation
public "getType"(): $ResourceLocation
public "getStructureManager"(): $StructureManager
public "getChunkBoundingBox"(): $BoundingBox
public "getStructureBoundingBox"(): $BoundingBox
public "getIntersectionBoxes"(): $Collection<($BoundingBox)>
public "getIntersectionPieces"(): $Collection<($StructurePiece)>
public "getRandomSource"(): $RandomSource
public "getStructure"(): $Structure
public "getChunkPos"(): $ChunkPos
get "intersectionMap"(): $Map<($StructurePiece), ($BoundingBox)>
get "worldGenLevel"(): $WorldGenLevel
get "piecesContainer"(): $PiecesContainer
get "chunkGenerator"(): $ChunkGenerator
get "genStep"(): string
get "id"(): $ResourceLocation
get "type"(): $ResourceLocation
get "structureManager"(): $StructureManager
get "chunkBoundingBox"(): $BoundingBox
get "structureBoundingBox"(): $BoundingBox
get "intersectionBoxes"(): $Collection<($BoundingBox)>
get "intersectionPieces"(): $Collection<($StructurePiece)>
get "randomSource"(): $RandomSource
get "structure"(): $Structure
get "chunkPos"(): $ChunkPos
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureAfterPlaceEventJS$Type = ($StructureAfterPlaceEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureAfterPlaceEventJS_ = $StructureAfterPlaceEventJS$Type;
}}
declare module "packages/com/almostreliable/morejs/features/structure/$StructureBlockInfoModification" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $StructureBlockInfoModification {

 "getProperties"(): $Map<(string), (any)>
 "getId"(): string
 "getPosition"(): $BlockPos
 "getBlock"(): $Block
 "setBlock"(arg0: $ResourceLocation$Type): void
 "setBlock"(arg0: $ResourceLocation$Type, arg1: $Map$Type<(string), (any)>): void
 "setNbt"(arg0: $CompoundTag$Type): void
 "getNbt"(): $CompoundTag
 "hasNbt"(): boolean
 "setVanillaBlockState"(arg0: $BlockState$Type): void
}

export namespace $StructureBlockInfoModification {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureBlockInfoModification$Type = ($StructureBlockInfoModification);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureBlockInfoModification_ = $StructureBlockInfoModification$Type;
}}
declare module "packages/com/almostreliable/lootjs/filters/$ItemFilter" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ResourceLocationFilter, $ResourceLocationFilter$Type} from "packages/com/almostreliable/lootjs/filters/$ResourceLocationFilter"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $ItemFilter extends $Predicate<($ItemStack)> {

 "test"(arg0: $ItemStack$Type): boolean
 "or"(other: $ItemFilter$Type): $ItemFilter
 "negate"(): $ItemFilter
 "and"(other: $ItemFilter$Type): $ItemFilter
 "or"(arg0: $Predicate$Type<(any)>): $Predicate<($ItemStack)>
 "and"(arg0: $Predicate$Type<(any)>): $Predicate<($ItemStack)>

(arg0: $ItemStack$Type): boolean
}

export namespace $ItemFilter {
const ALWAYS_FALSE: $ItemFilter
const ALWAYS_TRUE: $ItemFilter
const SWORD: $ItemFilter
const PICKAXE: $ItemFilter
const AXE: $ItemFilter
const SHOVEL: $ItemFilter
const HOE: $ItemFilter
const TOOL: $ItemFilter
const POTION: $ItemFilter
const HAS_TIER: $ItemFilter
const PROJECTILE_WEAPON: $ItemFilter
const ARMOR: $ItemFilter
const WEAPON: $ItemFilter
const HEAD_ARMOR: $ItemFilter
const CHEST_ARMOR: $ItemFilter
const LEGS_ARMOR: $ItemFilter
const FEET_ARMOR: $ItemFilter
const FOOD: $ItemFilter
const DAMAGEABLE: $ItemFilter
const DAMAGED: $ItemFilter
const ENCHANTABLE: $ItemFilter
const ENCHANTED: $ItemFilter
const BLOCK: $ItemFilter
function or(...itemFilters: ($ItemFilter$Type)[]): $ItemFilter
function and(...itemFilters: ($ItemFilter$Type)[]): $ItemFilter
function not(itemFilter: $ItemFilter$Type): $ItemFilter
function hasEnchantment(filter: $ResourceLocationFilter$Type): $ItemFilter
function hasEnchantment(filter: $ResourceLocationFilter$Type, min: integer, max: integer): $ItemFilter
function custom(predicate: $Predicate$Type<($ItemStack$Type)>): $ItemFilter
function equipmentSlot(slot: $EquipmentSlot$Type): $ItemFilter
function not<T>(arg0: $Predicate$Type<(any)>): $Predicate<($ItemStack)>
function isEqual<T>(arg0: any): $Predicate<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemFilter$Type = ($ItemFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemFilter_ = $ItemFilter$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/recipe/$AltarRecipe$DAY_TIME" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export class $AltarRecipe$DAY_TIME extends $Enum<($AltarRecipe$DAY_TIME)> {
static readonly "ANY": $AltarRecipe$DAY_TIME
static readonly "DAY": $AltarRecipe$DAY_TIME
static readonly "NIGHT": $AltarRecipe$DAY_TIME


public static "values"(): ($AltarRecipe$DAY_TIME)[]
public static "valueOf"(name: string): $AltarRecipe$DAY_TIME
public "check"(level: $Level$Type, player: $ServerPlayer$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AltarRecipe$DAY_TIME$Type = (("night") | ("any") | ("day")) | ($AltarRecipe$DAY_TIME);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AltarRecipe$DAY_TIME_ = $AltarRecipe$DAY_TIME$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/events/$StartTradingEventJS" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$PlayerEventJS, $PlayerEventJS$Type} from "packages/dev/latvian/mods/kubejs/player/$PlayerEventJS"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Merchant, $Merchant$Type} from "packages/net/minecraft/world/item/trading/$Merchant"
import {$OfferExtension, $OfferExtension$Type} from "packages/com/almostreliable/morejs/features/villager/$OfferExtension"

export class $StartTradingEventJS extends $PlayerEventJS {

constructor(arg0: $Player$Type, arg1: $Merchant$Type)

public "forEachOffers"(arg0: $BiConsumer$Type<($OfferExtension$Type), (integer)>): void
public "getMerchant"(): $Merchant
get "merchant"(): $Merchant
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StartTradingEventJS$Type = ($StartTradingEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StartTradingEventJS_ = $StartTradingEventJS$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$MatchFluid" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$IExtendedLootCondition, $IExtendedLootCondition$Type} from "packages/com/almostreliable/lootjs/loot/condition/$IExtendedLootCondition"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$FluidPredicate, $FluidPredicate$Type} from "packages/net/minecraft/advancements/critereon/$FluidPredicate"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $MatchFluid implements $IExtendedLootCondition {

constructor(predicate: $FluidPredicate$Type)

public "test"(context: $LootContext$Type): boolean
public "getType"(): $LootItemConditionType
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MatchFluid$Type = ($MatchFluid);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MatchFluid_ = $MatchFluid$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$OutputType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $RecipeOutputs$OutputType extends $Enum<($RecipeOutputs$OutputType)> {
static readonly "ITEM": $RecipeOutputs$OutputType
static readonly "MOB": $RecipeOutputs$OutputType


public static "values"(): ($RecipeOutputs$OutputType)[]
public static "valueOf"(name: string): $RecipeOutputs$OutputType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeOutputs$OutputType$Type = (("mob") | ("item")) | ($RecipeOutputs$OutputType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeOutputs$OutputType_ = $RecipeOutputs$OutputType$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/$OfferModification" {
import {$MerchantOffer, $MerchantOffer$Type} from "packages/net/minecraft/world/item/trading/$MerchantOffer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $OfferModification {

constructor(arg0: $MerchantOffer$Type)

public "setOutput"(arg0: $ItemStack$Type): void
public "getMerchantOffer"(): $MerchantOffer
public "getOutput"(): $ItemStack
public "setMaxUses"(arg0: integer): void
public "setVillagerExperience"(arg0: integer): void
public "getVillagerExperience"(): integer
public "getPriceMultiplier"(): float
public "setRewardExp"(arg0: boolean): void
public "getMaxUses"(): integer
public "setDemand"(arg0: integer): void
public "isRewardingExp"(): boolean
public "getFirstInput"(): $ItemStack
public "getSecondInput"(): $ItemStack
public "setPriceMultiplier"(arg0: float): void
public "setFirstInput"(arg0: $ItemStack$Type): void
public "getDemand"(): integer
public "setSecondInput"(arg0: $ItemStack$Type): void
set "output"(value: $ItemStack$Type)
get "merchantOffer"(): $MerchantOffer
get "output"(): $ItemStack
set "maxUses"(value: integer)
set "villagerExperience"(value: integer)
get "villagerExperience"(): integer
get "priceMultiplier"(): float
set "rewardExp"(value: boolean)
get "maxUses"(): integer
set "demand"(value: integer)
get "rewardingExp"(): boolean
get "firstInput"(): $ItemStack
get "secondInput"(): $ItemStack
set "priceMultiplier"(value: float)
set "firstInput"(value: $ItemStack$Type)
get "demand"(): integer
set "secondInput"(value: $ItemStack$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OfferModification$Type = ($OfferModification);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OfferModification_ = $OfferModification$Type;
}}
declare module "packages/com/almostreliable/ponderjs/util/$BlockStateFunction" {
import {$Context, $Context$Type} from "packages/dev/latvian/mods/rhino/$Context"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$BlockIDPredicate, $BlockIDPredicate$Type} from "packages/dev/latvian/mods/kubejs/block/predicate/$BlockIDPredicate"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export interface $BlockStateFunction extends $Function<($BlockIDPredicate), ($BlockState)> {

 "apply"(arg0: $BlockIDPredicate$Type): $BlockState
 "compose"<V>(arg0: $Function$Type<(any), (any)>): $Function<(V), ($BlockState)>
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<($BlockIDPredicate), (V)>

(arg0: $Context$Type, arg1: any): $BlockStateFunction
}

export namespace $BlockStateFunction {
function of(arg0: $Context$Type, arg1: any): $BlockStateFunction
function from(arg0: $BlockStateFunction$Type): $UnaryOperator<($BlockState)>
function identity<T>(): $Function<($BlockIDPredicate), ($BlockIDPredicate)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStateFunction$Type = ($BlockStateFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStateFunction_ = $BlockStateFunction$Type;
}}
declare module "packages/com/almostreliable/ponderjs/particles/$ParticleDataBuilder$DustParticleDataBuilder" {
import {$Color, $Color$Type} from "packages/dev/latvian/mods/rhino/mod/util/color/$Color"
import {$DustParticleOptionsBase, $DustParticleOptionsBase$Type} from "packages/net/minecraft/core/particles/$DustParticleOptionsBase"
import {$ParticleDataBuilder, $ParticleDataBuilder$Type} from "packages/com/almostreliable/ponderjs/particles/$ParticleDataBuilder"

export class $ParticleDataBuilder$DustParticleDataBuilder extends $ParticleDataBuilder<($ParticleDataBuilder$DustParticleDataBuilder), ($DustParticleOptionsBase)> {

constructor(arg0: $Color$Type, arg1: $Color$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleDataBuilder$DustParticleDataBuilder$Type = ($ParticleDataBuilder$DustParticleDataBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleDataBuilder$DustParticleDataBuilder_ = $ParticleDataBuilder$DustParticleDataBuilder$Type;
}}
declare module "packages/com/almostreliable/morejs/$Platform" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Platform extends $Enum<($Platform)> {
static readonly "Forge": $Platform
static readonly "Fabric": $Platform


public static "values"(): ($Platform)[]
public static "valueOf"(arg0: string): $Platform
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Platform$Type = (("forge") | ("fabric")) | ($Platform);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Platform_ = $Platform$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/platform/$PlatformBlockEntity" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$AltarInventory, $AltarInventory$Type} from "packages/com/almostreliable/summoningrituals/inventory/$AltarInventory"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"

export class $PlatformBlockEntity extends $BlockEntity {
 "blockState": $BlockState


public "getInventory"(): $AltarInventory
public "getProgress"(): integer
public "invalidateCaps"(): void
public "getCapability"<T>(cap: $Capability$Type<(T)>, side: $Direction$Type): $LazyOptional<(T)>
public "handleInteraction"(arg0: $ServerPlayer$Type, arg1: $ItemStack$Type): $ItemStack
public "setProgress"(progress: integer): void
get "inventory"(): $AltarInventory
get "progress"(): integer
set "progress"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformBlockEntity$Type = ($PlatformBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformBlockEntity_ = $PlatformBlockEntity$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/results/$Icon" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Icon extends $Enum<($Icon)> {
static readonly "SUCCEED": $Icon
static readonly "FAILED": $Icon
static readonly "ACTION": $Icon
static readonly "WRENCH": $Icon
static readonly "BOLT": $Icon
static readonly "DICE": $Icon


public "toString"(): string
public static "values"(): ($Icon)[]
public static "valueOf"(name: string): $Icon
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Icon$Type = (("succeed") | ("dice") | ("action") | ("failed") | ("bolt") | ("wrench")) | ($Icon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Icon_ = $Icon$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/$TradingManager" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$VillagerTrades$ItemListing, $VillagerTrades$ItemListing$Type} from "packages/net/minecraft/world/entity/npc/$VillagerTrades$ItemListing"
import {$Int2ObjectMap, $Int2ObjectMap$Type} from "packages/it/unimi/dsi/fastutil/ints/$Int2ObjectMap"
import {$VillagerProfession, $VillagerProfession$Type} from "packages/net/minecraft/world/entity/npc/$VillagerProfession"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $TradingManager {

constructor()

public "reload"(): void
public "getTradesBackup"(): $Map<($VillagerProfession), ($Int2ObjectMap<($List<($VillagerTrades$ItemListing)>)>)>
public "invokeVillagerTradeEvent"(arg0: $Map$Type<($VillagerProfession$Type), ($Int2ObjectMap$Type<($List$Type<($VillagerTrades$ItemListing$Type)>)>)>): void
public "invokeWanderingTradeEvent"(arg0: $Int2ObjectMap$Type<($List$Type<($VillagerTrades$ItemListing$Type)>)>): void
public "getWandererTradesBackup"(): $Int2ObjectMap<($List<($VillagerTrades$ItemListing)>)>
get "tradesBackup"(): $Map<($VillagerProfession), ($Int2ObjectMap<($List<($VillagerTrades$ItemListing)>)>)>
get "wandererTradesBackup"(): $Int2ObjectMap<($List<($VillagerTrades$ItemListing)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TradingManager$Type = ($TradingManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TradingManager_ = $TradingManager$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/viewer/common/$BlockReferenceRenderer" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$BlockReference, $BlockReference$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$BlockReference"

export class $BlockReferenceRenderer {


public "render"(guiGraphics: $GuiGraphics$Type, blockReference: $BlockReference$Type): void
public "getTooltip"(blockReference: $BlockReference$Type, tooltipFlag: $TooltipFlag$Type): $List<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockReferenceRenderer$Type = ($BlockReferenceRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockReferenceRenderer_ = $BlockReferenceRenderer$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$AndCondition" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$IExtendedLootCondition, $IExtendedLootCondition$Type} from "packages/com/almostreliable/lootjs/loot/condition/$IExtendedLootCondition"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$ILootCondition, $ILootCondition$Type} from "packages/com/almostreliable/lootjs/core/$ILootCondition"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $AndCondition implements $IExtendedLootCondition {

constructor(...conditions: ($ILootCondition$Type)[])

public "test"(context: $LootContext$Type): boolean
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
public "getType"(): $LootItemConditionType
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AndCondition$Type = ($AndCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AndCondition_ = $AndCondition$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$ContainsLootCondition" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$IExtendedLootCondition, $IExtendedLootCondition$Type} from "packages/com/almostreliable/lootjs/loot/condition/$IExtendedLootCondition"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $ContainsLootCondition implements $IExtendedLootCondition {

constructor(predicate: $Predicate$Type<($ItemStack$Type)>, exact: boolean)

public "test"(context: $LootContext$Type): boolean
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
public "getType"(): $LootItemConditionType
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContainsLootCondition$Type = ($ContainsLootCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContainsLootCondition_ = $ContainsLootCondition$Type;
}}
declare module "packages/com/almostreliable/lootjs/mixin/$LootItemConditionMixin" {
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ILootCondition, $ILootCondition$Type} from "packages/com/almostreliable/lootjs/core/$ILootCondition"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $LootItemConditionMixin extends $ILootCondition {

 "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
 "test"(arg0: $LootContext$Type): boolean
 "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
 "negate"(): $Predicate<($LootContext)>
 "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>

(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
}

export namespace $LootItemConditionMixin {
function not<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
function isEqual<T>(arg0: any): $Predicate<($LootContext)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootItemConditionMixin$Type = ($LootItemConditionMixin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootItemConditionMixin_ = $LootItemConditionMixin$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/$VillagerUtils" {
import {$TransformableTrade$Transformer, $TransformableTrade$Transformer$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$TransformableTrade$Transformer"
import {$TradeItem, $TradeItem$Type} from "packages/com/almostreliable/morejs/features/villager/$TradeItem"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$VillagerTrades$ItemListing, $VillagerTrades$ItemListing$Type} from "packages/net/minecraft/world/entity/npc/$VillagerTrades$ItemListing"
import {$TreasureMapTrade, $TreasureMapTrade$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$TreasureMapTrade"
import {$EnchantedItemTrade, $EnchantedItemTrade$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$EnchantedItemTrade"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$VillagerProfession, $VillagerProfession$Type} from "packages/net/minecraft/world/entity/npc/$VillagerProfession"
import {$SimpleTrade, $SimpleTrade$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$SimpleTrade"
import {$MapPosInfo$Provider, $MapPosInfo$Provider$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$MapPosInfo$Provider"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$CustomTrade, $CustomTrade$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$CustomTrade"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$WeightedList, $WeightedList$Type} from "packages/com/almostreliable/morejs/util/$WeightedList"
import {$StewTrade, $StewTrade$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$StewTrade"
import {$PotionTrade, $PotionTrade$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$PotionTrade"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $VillagerUtils {
static readonly "CACHED_PROFESSION_TRADES": $Map<($VillagerProfession), ($List<($VillagerTrades$ItemListing)>)>
static readonly "VANILLA_TRADE_TYPES": $Set<($Class<(any)>)>

constructor()

public static "getRandomWandererTrade"(arg0: integer): $VillagerTrades$ItemListing
public static "getRandomVillagerTrade"(arg0: $VillagerProfession$Type): $VillagerTrades$ItemListing
public static "getRandomVillagerTrade"(arg0: $VillagerProfession$Type, arg1: integer): $VillagerTrades$ItemListing
public static "createStructureMapTrade"(arg0: ($TradeItem$Type)[], arg1: $WeightedList$Type<(any)>): $TreasureMapTrade
public static "createBiomeMapTrade"(arg0: ($TradeItem$Type)[], arg1: $WeightedList$Type<(any)>): $TreasureMapTrade
public static "createEnchantedItemTrade"(arg0: ($TradeItem$Type)[], arg1: $Item$Type): $EnchantedItemTrade
public static "createCustomMapTrade"(arg0: ($TradeItem$Type)[], arg1: $MapPosInfo$Provider$Type): $TreasureMapTrade
public static "setAbstractTrades"(arg0: $Map$Type<(integer), (($VillagerTrades$ItemListing$Type)[])>, arg1: integer, arg2: $List$Type<($VillagerTrades$ItemListing$Type)>): void
public static "getAbstractTrades"(arg0: $Map$Type<(integer), (($VillagerTrades$ItemListing$Type)[])>, arg1: integer): $List<($VillagerTrades$ItemListing)>
public static "isVanillaTrade"(arg0: $VillagerTrades$ItemListing$Type): boolean
public static "getProfessions"(): $Collection<($VillagerProfession)>
public static "createCustomTrade"(arg0: $TransformableTrade$Transformer$Type): $CustomTrade
public static "isModdedTrade"(arg0: $VillagerTrades$ItemListing$Type): boolean
public static "isMoreJSTrade"(arg0: $VillagerTrades$ItemListing$Type): boolean
public static "createSimpleTrade"(arg0: ($TradeItem$Type)[], arg1: $TradeItem$Type): $SimpleTrade
public static "createStewTrade"(arg0: ($TradeItem$Type)[], arg1: ($MobEffect$Type)[], arg2: integer): $StewTrade
public static "createPotionTrade"(arg0: ($TradeItem$Type)[]): $PotionTrade
public static "getVillagerTrades"(arg0: $VillagerProfession$Type, arg1: integer): $List<($VillagerTrades$ItemListing)>
public static "getVillagerTrades"(arg0: $VillagerProfession$Type): $List<($VillagerTrades$ItemListing)>
public static "getWandererTrades"(arg0: integer): $List<($VillagerTrades$ItemListing)>
public static "getProfession"(arg0: $ResourceLocation$Type): $VillagerProfession
get "professions"(): $Collection<($VillagerProfession)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VillagerUtils$Type = ($VillagerUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VillagerUtils_ = $VillagerUtils$Type;
}}
declare module "packages/com/almostreliable/lootjs/kube/builder/$EntityPredicateBuilderJS" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Resolver, $Resolver$Type} from "packages/com/almostreliable/lootjs/filters/$Resolver"
import {$ItemFilter, $ItemFilter$Type} from "packages/com/almostreliable/lootjs/filters/$ItemFilter"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$EntityPredicate, $EntityPredicate$Type} from "packages/net/minecraft/advancements/critereon/$EntityPredicate"
import {$ExtendedEntityFlagsPredicate$IBuilder, $ExtendedEntityFlagsPredicate$IBuilder$Type} from "packages/com/almostreliable/lootjs/predicate/$ExtendedEntityFlagsPredicate$IBuilder"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $EntityPredicateBuilderJS implements $ExtendedEntityFlagsPredicate$IBuilder<($EntityPredicate)> {

constructor()

public "isSprinting"(flag: boolean): $EntityPredicateBuilderJS
public "nbt"(nbt: $CompoundTag$Type): $EntityPredicateBuilderJS
public "hasEffect"(effect: $MobEffect$Type, amplifier: integer): $EntityPredicateBuilderJS
public "hasEffect"(effect: $MobEffect$Type): $EntityPredicateBuilderJS
public "matchFluid"(resolver: $Resolver$Type): $EntityPredicateBuilderJS
public "anyType"(...resolvers: ($Resolver$Type)[]): $EntityPredicateBuilderJS
public "isInWater"(flag: boolean): $EntityPredicateBuilderJS
public "isMonster"(flag: boolean): $EntityPredicateBuilderJS
public "isCreature"(flag: boolean): $EntityPredicateBuilderJS
public "isOnFire"(flag: boolean): $EntityPredicateBuilderJS
public "isUndeadMob"(flag: boolean): $EntityPredicateBuilderJS
public "matchBlock"(resolver: $Resolver$Type, propertyMap: $Map$Type<(string), (string)>): $EntityPredicateBuilderJS
public "matchBlock"(resolver: $Resolver$Type): $EntityPredicateBuilderJS
public "matchMount"(action: $Consumer$Type<($EntityPredicateBuilderJS$Type)>): $EntityPredicateBuilderJS
public "matchSlot"(slot: $EquipmentSlot$Type, itemFilter: $ItemFilter$Type): $EntityPredicateBuilderJS
public "matchTargetedEntity"(action: $Consumer$Type<($EntityPredicateBuilderJS$Type)>): $EntityPredicateBuilderJS
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityPredicateBuilderJS$Type = ($EntityPredicateBuilderJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityPredicateBuilderJS_ = $EntityPredicateBuilderJS$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/$GroupedLootBuilder" {
import {$EntityPredicateBuilderJS, $EntityPredicateBuilderJS$Type} from "packages/com/almostreliable/lootjs/kube/builder/$EntityPredicateBuilderJS"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GroupedLootAction, $GroupedLootAction$Type} from "packages/com/almostreliable/lootjs/loot/action/$GroupedLootAction"
import {$LootConditionsContainer, $LootConditionsContainer$Type} from "packages/com/almostreliable/lootjs/kube/$LootConditionsContainer"
import {$AddAttributesFunction$Builder, $AddAttributesFunction$Builder$Type} from "packages/com/almostreliable/lootjs/loot/$AddAttributesFunction$Builder"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ILootAction, $ILootAction$Type} from "packages/com/almostreliable/lootjs/core/$ILootAction"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$NumberProvider, $NumberProvider$Type} from "packages/net/minecraft/world/level/storage/loot/providers/number/$NumberProvider"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"
import {$Resolver, $Resolver$Type} from "packages/com/almostreliable/lootjs/filters/$Resolver"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LootActionsContainer, $LootActionsContainer$Type} from "packages/com/almostreliable/lootjs/loot/$LootActionsContainer"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$ModifyLootAction$Callback, $ModifyLootAction$Callback$Type} from "packages/com/almostreliable/lootjs/loot/action/$ModifyLootAction$Callback"
import {$LootItemFunction$Builder, $LootItemFunction$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/functions/$LootItemFunction$Builder"
import {$DamageSourcePredicateBuilderJS, $DamageSourcePredicateBuilderJS$Type} from "packages/com/almostreliable/lootjs/kube/builder/$DamageSourcePredicateBuilderJS"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$LootFunctionsContainer, $LootFunctionsContainer$Type} from "packages/com/almostreliable/lootjs/loot/$LootFunctionsContainer"
import {$ILootCondition, $ILootCondition$Type} from "packages/com/almostreliable/lootjs/core/$ILootCondition"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$LootEntry, $LootEntry$Type} from "packages/com/almostreliable/lootjs/core/$LootEntry"
import {$DistancePredicateBuilder, $DistancePredicateBuilder$Type} from "packages/com/almostreliable/lootjs/loot/condition/builder/$DistancePredicateBuilder"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$MinMaxBounds$Doubles, $MinMaxBounds$Doubles$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Doubles"
import {$LootItemCondition$Builder, $LootItemCondition$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemCondition$Builder"
import {$ItemFilter, $ItemFilter$Type} from "packages/com/almostreliable/lootjs/filters/$ItemFilter"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$Explosion$BlockInteraction, $Explosion$BlockInteraction$Type} from "packages/net/minecraft/world/level/$Explosion$BlockInteraction"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $GroupedLootBuilder implements $LootConditionsContainer<($GroupedLootBuilder)>, $LootFunctionsContainer<($GroupedLootBuilder)>, $LootActionsContainer<($GroupedLootBuilder)> {

constructor()

public "build"(): $GroupedLootAction
public "rolls"(numberProvider: $NumberProvider$Type): $GroupedLootBuilder
public "addAction"(action: $ILootAction$Type): $GroupedLootBuilder
public "or"(action: $Consumer$Type<($LootConditionsContainer$Type<($GroupedLootBuilder$Type)>)>): $GroupedLootBuilder
public "and"(action: $Consumer$Type<($LootConditionsContainer$Type<($GroupedLootBuilder$Type)>)>): $GroupedLootBuilder
public "not"(action: $Consumer$Type<($LootConditionsContainer$Type<($GroupedLootBuilder$Type)>)>): $GroupedLootBuilder
public "survivesExplosion"(): $GroupedLootBuilder
public "addCondition"(builder: $LootItemCondition$Builder$Type): $GroupedLootBuilder
public "randomChance"(value: float): $GroupedLootBuilder
public "randomChanceWithLooting"(value: float, looting: float): $GroupedLootBuilder
public "killedByPlayer"(): $GroupedLootBuilder
public "lightLevel"(min: integer, max: integer): $GroupedLootBuilder
public "entityPredicate"(predicate: $Predicate$Type<($Entity$Type)>): $GroupedLootBuilder
public "matchLoot"(filter: $ItemFilter$Type): $GroupedLootBuilder
public "matchLoot"(filter: $ItemFilter$Type, exact: boolean): $GroupedLootBuilder
public "matchEquip"(slot: $EquipmentSlot$Type, filter: $ItemFilter$Type): $GroupedLootBuilder
public "matchMainHand"(filter: $ItemFilter$Type): $GroupedLootBuilder
public "matchOffHand"(filter: $ItemFilter$Type): $GroupedLootBuilder
public "timeCheck"(min: integer, max: integer): $GroupedLootBuilder
public "timeCheck"(period: long, min: integer, max: integer): $GroupedLootBuilder
public "anyBiome"(...resolvers: ($Resolver$Type)[]): $GroupedLootBuilder
public "anyDimension"(...dimensions: ($ResourceLocation$Type)[]): $GroupedLootBuilder
public "randomTableBonus"(enchantment: $Enchantment$Type, chances: (float)[]): $GroupedLootBuilder
public "anyStructure"(idOrTags: (string)[], exact: boolean): $GroupedLootBuilder
public "weatherCheck"(map: $Map$Type<(string), (boolean)>): $GroupedLootBuilder
public "killerPredicate"(predicate: $Predicate$Type<($Entity$Type)>): $GroupedLootBuilder
public "matchPlayer"(action: $Consumer$Type<($EntityPredicateBuilderJS$Type)>): $GroupedLootBuilder
public "hasAnyStage"(...stages: (string)[]): $GroupedLootBuilder
public "distanceToKiller"(bounds: $MinMaxBounds$Doubles$Type): $GroupedLootBuilder
public "matchDirectKiller"(action: $Consumer$Type<($EntityPredicateBuilderJS$Type)>): $GroupedLootBuilder
public "matchBlockState"(block: $Block$Type, propertyMap: $Map$Type<(string), (string)>): $GroupedLootBuilder
public "matchDamageSource"(action: $Consumer$Type<($DamageSourcePredicateBuilderJS$Type)>): $GroupedLootBuilder
public "matchFluid"(resolver: $Resolver$Type): $GroupedLootBuilder
public "playerPredicate"(predicate: $Predicate$Type<($ServerPlayer$Type)>): $GroupedLootBuilder
public "matchEntity"(action: $Consumer$Type<($EntityPredicateBuilderJS$Type)>): $GroupedLootBuilder
public "matchKiller"(action: $Consumer$Type<($EntityPredicateBuilderJS$Type)>): $GroupedLootBuilder
public "createConditions"(action: $Consumer$Type<($LootConditionsContainer$Type<($GroupedLootBuilder$Type)>)>): $List<($ILootCondition)>
public "customCondition"(json: $JsonObject$Type): $GroupedLootBuilder
public "biome"(...resolvers: ($Resolver$Type)[]): $GroupedLootBuilder
public "randomChanceWithEnchantment"(enchantment: $Enchantment$Type, chances: (float)[]): $GroupedLootBuilder
public "blockEntityPredicate"(predicate: $Predicate$Type<($BlockEntity$Type)>): $GroupedLootBuilder
public "customDistanceToPlayer"(action: $Consumer$Type<($DistancePredicateBuilder$Type)>): $GroupedLootBuilder
public "directKillerPredicate"(predicate: $Predicate$Type<($Entity$Type)>): $GroupedLootBuilder
public "setName"(component: $Component$Type): $GroupedLootBuilder
public "functions"(filter: $ItemFilter$Type, action: $Consumer$Type<($LootFunctionsContainer$Type<($GroupedLootBuilder$Type)>)>): $GroupedLootBuilder
public "addAttributes"(action: $Consumer$Type<($AddAttributesFunction$Builder$Type)>): $GroupedLootBuilder
public "addLore"(...components: ($Component$Type)[]): $GroupedLootBuilder
public "addFunction"(builder: $LootItemFunction$Builder$Type): $GroupedLootBuilder
public "damage"(numberProvider: $NumberProvider$Type): $GroupedLootBuilder
public "simulateExplosionDecay"(): $GroupedLootBuilder
public "applyBinomialDistributionBonus"(enchantment: $Enchantment$Type, probability: float, n: integer): $GroupedLootBuilder
public "enchantWithLevels"(numberProvider: $NumberProvider$Type, allowTreasure: boolean): $GroupedLootBuilder
public "enchantWithLevels"(numberProvider: $NumberProvider$Type): $GroupedLootBuilder
public "applyLootingBonus"(numberProvider: $NumberProvider$Type): $GroupedLootBuilder
public "applyOreBonus"(enchantment: $Enchantment$Type): $GroupedLootBuilder
public "enchantRandomly"(): $GroupedLootBuilder
public "enchantRandomly"(enchantments: ($Enchantment$Type)[]): $GroupedLootBuilder
public "smeltLoot"(): $GroupedLootBuilder
public "addPotion"(potion: $Potion$Type): $GroupedLootBuilder
public "limitCount"(numberProviderMin: $NumberProvider$Type, numberProviderMax: $NumberProvider$Type): $GroupedLootBuilder
public "limitCount"(numberProvider: $NumberProvider$Type): $GroupedLootBuilder
public "replaceLore"(...components: ($Component$Type)[]): $GroupedLootBuilder
public "addNbt"(tag: $CompoundTag$Type): $GroupedLootBuilder
public "addNBT"(tag: $CompoundTag$Type): $GroupedLootBuilder
public "applyBonus"(enchantment: $Enchantment$Type, multiplier: integer): $GroupedLootBuilder
public "customFunction"(json: $JsonObject$Type): $GroupedLootBuilder
public "replaceLoot"(filter: $ItemFilter$Type, lootEntry: $LootEntry$Type): $GroupedLootBuilder
public "replaceLoot"(filter: $ItemFilter$Type, lootEntry: $LootEntry$Type, preserveCount: boolean): $GroupedLootBuilder
public "addSequenceLoot"(...entries: ($LootEntry$Type)[]): $GroupedLootBuilder
public "removeLoot"(filter: $ItemFilter$Type): $GroupedLootBuilder
public "addWeightedLoot"(poolEntries: ($LootEntry$Type)[]): $GroupedLootBuilder
public "addWeightedLoot"(numberProvider: $NumberProvider$Type, poolEntries: ($LootEntry$Type)[]): $GroupedLootBuilder
public "addWeightedLoot"(numberProvider: $NumberProvider$Type, allowDuplicateLoot: boolean, poolEntries: ($LootEntry$Type)[]): $GroupedLootBuilder
public "triggerExplosion"(radius: float, destroy: boolean, fire: boolean): $GroupedLootBuilder
public "triggerExplosion"(radius: float, mode: $Explosion$BlockInteraction$Type, fire: boolean): $GroupedLootBuilder
public "dropExperience"(amount: integer): $GroupedLootBuilder
public "modifyLoot"(filter: $ItemFilter$Type, callback: $ModifyLootAction$Callback$Type): $GroupedLootBuilder
public "addLoot"(...entries: ($LootEntry$Type)[]): $GroupedLootBuilder
public "addAlternativesLoot"(...entries: ($LootEntry$Type)[]): $GroupedLootBuilder
public "triggerLightningStrike"(shouldDamage: boolean): $GroupedLootBuilder
set "name"(value: $Component$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GroupedLootBuilder$Type = ($GroupedLootBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GroupedLootBuilder_ = $GroupedLootBuilder$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/trades/$SimpleTrade" {
import {$MerchantOffer, $MerchantOffer$Type} from "packages/net/minecraft/world/item/trading/$MerchantOffer"
import {$TradeItem, $TradeItem$Type} from "packages/com/almostreliable/morejs/features/villager/$TradeItem"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$TransformableTrade, $TransformableTrade$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$TransformableTrade"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $SimpleTrade extends $TransformableTrade<($SimpleTrade)> {

constructor(arg0: ($TradeItem$Type)[], arg1: $TradeItem$Type)

public "createOffer"(arg0: $Entity$Type, arg1: $RandomSource$Type): $MerchantOffer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleTrade$Type = ($SimpleTrade);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleTrade_ = $SimpleTrade$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/events/$VillagerTradingEventJS$ForEachCallback" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$VillagerTrades$ItemListing, $VillagerTrades$ItemListing$Type} from "packages/net/minecraft/world/entity/npc/$VillagerTrades$ItemListing"
import {$VillagerProfession, $VillagerProfession$Type} from "packages/net/minecraft/world/entity/npc/$VillagerProfession"

export interface $VillagerTradingEventJS$ForEachCallback {

 "accept"(arg0: $List$Type<($VillagerTrades$ItemListing$Type)>, arg1: integer, arg2: $VillagerProfession$Type): void

(arg0: $List$Type<($VillagerTrades$ItemListing$Type)>, arg1: integer, arg2: $VillagerProfession$Type): void
}

export namespace $VillagerTradingEventJS$ForEachCallback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VillagerTradingEventJS$ForEachCallback$Type = ($VillagerTradingEventJS$ForEachCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VillagerTradingEventJS$ForEachCallback_ = $VillagerTradingEventJS$ForEachCallback$Type;
}}
declare module "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentTableServerEventJS$Data" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$IntRange, $IntRange$Type} from "packages/com/almostreliable/morejs/features/villager/$IntRange"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $EnchantmentTableServerEventJS$Data {


public "hasEnchantment"(arg0: $ResourceLocation$Type): boolean
public "hasEnchantment"(arg0: $ResourceLocation$Type, arg1: $IntRange$Type): boolean
public "clearEnchantments"(): void
public "getEnchantmentIds"(): $List<($ResourceLocation)>
public "getRequiredLevel"(): integer
public "forEachEnchantments"(arg0: $BiConsumer$Type<($Enchantment$Type), (integer)>): void
public "getEnchantmentCount"(): integer
get "enchantmentIds"(): $List<($ResourceLocation)>
get "requiredLevel"(): integer
get "enchantmentCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnchantmentTableServerEventJS$Data$Type = ($EnchantmentTableServerEventJS$Data);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnchantmentTableServerEventJS$Data_ = $EnchantmentTableServerEventJS$Data$Type;
}}
declare module "packages/com/almostreliable/lootjs/predicate/$ExtendedEntityFlagsPredicate$IBuilder" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ExtendedEntityFlagsPredicate$IBuilder<T> {

 "isSprinting"(arg0: boolean): $ExtendedEntityFlagsPredicate$IBuilder<(T)>
 "build"(): T
 "isBaby"(arg0: boolean): $ExtendedEntityFlagsPredicate$IBuilder<(T)>
 "isInWater"(arg0: boolean): $ExtendedEntityFlagsPredicate$IBuilder<(T)>
 "isUnderWater"(arg0: boolean): $ExtendedEntityFlagsPredicate$IBuilder<(T)>
 "isMonster"(arg0: boolean): $ExtendedEntityFlagsPredicate$IBuilder<(T)>
 "isCreature"(arg0: boolean): $ExtendedEntityFlagsPredicate$IBuilder<(T)>
 "isOnFire"(arg0: boolean): $ExtendedEntityFlagsPredicate$IBuilder<(T)>
 "isOnGround"(arg0: boolean): $ExtendedEntityFlagsPredicate$IBuilder<(T)>
 "isUndeadMob"(arg0: boolean): $ExtendedEntityFlagsPredicate$IBuilder<(T)>
 "isArthropodMob"(arg0: boolean): $ExtendedEntityFlagsPredicate$IBuilder<(T)>
 "isWaterMob"(arg0: boolean): $ExtendedEntityFlagsPredicate$IBuilder<(T)>
 "isIllegarMob"(arg0: boolean): $ExtendedEntityFlagsPredicate$IBuilder<(T)>
 "isSwimming"(arg0: boolean): $ExtendedEntityFlagsPredicate$IBuilder<(T)>
 "isCrouching"(arg0: boolean): $ExtendedEntityFlagsPredicate$IBuilder<(T)>
}

export namespace $ExtendedEntityFlagsPredicate$IBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtendedEntityFlagsPredicate$IBuilder$Type<T> = ($ExtendedEntityFlagsPredicate$IBuilder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtendedEntityFlagsPredicate$IBuilder_<T> = $ExtendedEntityFlagsPredicate$IBuilder$Type<(T)>;
}}
declare module "packages/com/almostreliable/ponderjs/util/$Util" {
import {$PonderTag, $PonderTag$Type} from "packages/com/simibubi/create/foundation/ponder/$PonderTag"
import {$AllIcons, $AllIcons$Type} from "packages/com/simibubi/create/foundation/gui/$AllIcons"
import {$Selection, $Selection$Type} from "packages/com/simibubi/create/foundation/ponder/$Selection"
import {$BlockIDPredicate, $BlockIDPredicate$Type} from "packages/dev/latvian/mods/kubejs/block/predicate/$BlockIDPredicate"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export class $Util {

constructor()

public static "blockStateOf"(arg0: any): $BlockState
public static "allIconsOf"(arg0: any): $AllIcons
public static "selectionOf"(arg0: any): $Selection
public static "ponderTagOf"(arg0: any): $PonderTag
public static "createBlockID"(arg0: $BlockState$Type): $BlockIDPredicate
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Util$Type = ($Util);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Util_ = $Util$Type;
}}
declare module "packages/com/almostreliable/lootjs/core/$AbstractLootModification" {
import {$ILootHandler, $ILootHandler$Type} from "packages/com/almostreliable/lootjs/core/$ILootHandler"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$CompositeLootAction, $CompositeLootAction$Type} from "packages/com/almostreliable/lootjs/loot/action/$CompositeLootAction"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $AbstractLootModification extends $CompositeLootAction {

constructor(name: string, handlers: $Collection$Type<($ILootHandler$Type)>)

public "shouldExecute"(arg0: $LootContext$Type): boolean
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractLootModification$Type = ($AbstractLootModification);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractLootModification_ = $AbstractLootModification$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/action/$AddLootAction" {
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$AddLootAction$AddType, $AddLootAction$AddType$Type} from "packages/com/almostreliable/lootjs/loot/action/$AddLootAction$AddType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LootEntry, $LootEntry$Type} from "packages/com/almostreliable/lootjs/core/$LootEntry"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ILootAction, $ILootAction$Type} from "packages/com/almostreliable/lootjs/core/$ILootAction"

export class $AddLootAction implements $ILootAction {

constructor(entries: ($LootEntry$Type)[])
constructor(entries: ($LootEntry$Type)[], type: $AddLootAction$AddType$Type)

public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AddLootAction$Type = ($AddLootAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AddLootAction_ = $AddLootAction$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/altar/$AltarObservable" {
import {$AltarObservable$Observer, $AltarObservable$Observer$Type} from "packages/com/almostreliable/summoningrituals/altar/$AltarObservable$Observer"

export class $AltarObservable {

constructor()

public "register"(observer: $AltarObservable$Observer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AltarObservable$Type = ($AltarObservable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AltarObservable_ = $AltarObservable$Type;
}}
declare module "packages/com/almostreliable/lootjs/util/$LootContextUtils" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"

export class $LootContextUtils {

constructor()

public static "getPlayerOrNull"(context: $LootContext$Type): $ServerPlayer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootContextUtils$Type = ($LootContextUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootContextUtils_ = $LootContextUtils$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/altar/$AltarRenderer" {
import {$BlockEntityRendererProvider$Context, $BlockEntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRendererProvider$Context"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$AltarBlockEntity, $AltarBlockEntity$Type} from "packages/com/almostreliable/summoningrituals/altar/$AltarBlockEntity"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$BlockEntityRenderer, $BlockEntityRenderer$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRenderer"

export class $AltarRenderer implements $BlockEntityRenderer<($AltarBlockEntity)> {

constructor(ignoredContext: $BlockEntityRendererProvider$Context$Type)

public "render"(entity: $AltarBlockEntity$Type, partial: float, stack: $PoseStack$Type, buffer: $MultiBufferSource$Type, light: integer, overlay: integer): void
public "shouldRender"(arg0: $AltarBlockEntity$Type, arg1: $Vec3$Type): boolean
public "shouldRenderOffScreen"(arg0: $AltarBlockEntity$Type): boolean
public "getViewDistance"(): integer
get "viewDistance"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AltarRenderer$Type = ($AltarRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AltarRenderer_ = $AltarRenderer$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/kubejs/$AltarRecipeJS" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$ModifyRecipeResultCallback, $ModifyRecipeResultCallback$Type} from "packages/dev/latvian/mods/kubejs/recipe/$ModifyRecipeResultCallback"
import {$InputItem, $InputItem$Type} from "packages/dev/latvian/mods/kubejs/item/$InputItem"
import {$RecipeOutputs$MobOutputBuilder, $RecipeOutputs$MobOutputBuilder$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$MobOutputBuilder"
import {$RecipeJS, $RecipeJS$Type} from "packages/dev/latvian/mods/kubejs/recipe/$RecipeJS"
import {$RecipeOutputs$ItemOutputBuilder, $RecipeOutputs$ItemOutputBuilder$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$ItemOutputBuilder"
import {$RecipeTypeFunction, $RecipeTypeFunction$Type} from "packages/dev/latvian/mods/kubejs/recipe/$RecipeTypeFunction"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $AltarRecipeJS extends $RecipeJS {
static "itemErrors": boolean
 "id": $ResourceLocation
 "type": $RecipeTypeFunction
 "newRecipe": boolean
 "removed": boolean
 "modifyResult": $ModifyRecipeResultCallback
 "originalJson": $JsonObject
 "json": $JsonObject
 "changed": boolean

constructor()

public "input"(...ingredients: ($InputItem$Type)[]): $AltarRecipeJS
public "sacrifice"(id: $ResourceLocation$Type, count: integer): $AltarRecipeJS
public "sacrifice"(id: $ResourceLocation$Type): $AltarRecipeJS
public "writeInputItem"(value: $InputItem$Type): $JsonElement
public "readInputItem"(from: any): $InputItem
public "blockBelow"(id: $ResourceLocation$Type, properties: $JsonObject$Type): $AltarRecipeJS
public "blockBelow"(id: $ResourceLocation$Type): $AltarRecipeJS
public "mobOutput"(entityOutput: $RecipeOutputs$MobOutputBuilder$Type): $AltarRecipeJS
public "itemOutput"(itemOutput: $RecipeOutputs$ItemOutputBuilder$Type): $AltarRecipeJS
public "sacrificeRegion"(width: integer, height: integer): $AltarRecipeJS
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AltarRecipeJS$Type = ($AltarRecipeJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AltarRecipeJS_ = $AltarRecipeJS$Type;
}}
declare module "packages/com/almostreliable/ponderjs/particles/$ParticleDataBuilder$Static" {
import {$ParticleDataBuilder, $ParticleDataBuilder$Type} from "packages/com/almostreliable/ponderjs/particles/$ParticleDataBuilder"
import {$ParticleOptions, $ParticleOptions$Type} from "packages/net/minecraft/core/particles/$ParticleOptions"

export class $ParticleDataBuilder$Static extends $ParticleDataBuilder<($ParticleDataBuilder$Static), ($ParticleOptions)> {

constructor(arg0: $ParticleOptions$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleDataBuilder$Static$Type = ($ParticleDataBuilder$Static);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleDataBuilder$Static_ = $ParticleDataBuilder$Static$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/util/$SerializeUtils" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$IForgeRegistry, $IForgeRegistry$Type} from "packages/net/minecraftforge/registries/$IForgeRegistry"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $SerializeUtils {


public static "getFromRegistry"<T>(registry: $IForgeRegistry$Type<(T)>, id: $ResourceLocation$Type): T
public static "vec3FromJson"(json: $JsonObject$Type): $Vec3i
public static "vec3ToJson"(vec: $Vec3i$Type): $JsonObject
public static "vec3FromNetwork"(buffer: $FriendlyByteBuf$Type): $Vec3i
public static "vec3ToNetwork"(buffer: $FriendlyByteBuf$Type, vec: $Vec3i$Type): void
public static "nbtFromString"(nbtString: string): $CompoundTag
public static "stackToJson"(stack: $ItemStack$Type): $JsonObject
public static "mobFromNetwork"(buffer: $FriendlyByteBuf$Type): $EntityType<(any)>
public static "mapToJson"(map: $Map$Type<(string), (string)>): $JsonObject
public static "blockFromId"(id: $ResourceLocation$Type): $Block
public static "mapToNetwork"(buffer: $FriendlyByteBuf$Type, map: $Map$Type<(string), (string)>): void
public static "mapFromJson"(json: $JsonObject$Type): $Map<(string), (string)>
public static "mapFromNetwork"(buffer: $FriendlyByteBuf$Type): $Map<(string), (string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SerializeUtils$Type = ($SerializeUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SerializeUtils_ = $SerializeUtils$Type;
}}
declare module "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentTableServerEventJS" {
import {$EnchantmentTableEventJS, $EnchantmentTableEventJS$Type} from "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentTableEventJS"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$EnchantmentTableServerEventJS$Data, $EnchantmentTableServerEventJS$Data$Type} from "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentTableServerEventJS$Data"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$EnchantmentMenuProcess, $EnchantmentMenuProcess$Type} from "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentMenuProcess"

export class $EnchantmentTableServerEventJS extends $EnchantmentTableEventJS {

constructor(arg0: $ItemStack$Type, arg1: $ItemStack$Type, arg2: $Level$Type, arg3: $BlockPos$Type, arg4: $Player$Type, arg5: $EnchantmentMenuProcess$Type)

public "get"(arg0: integer): $EnchantmentTableServerEventJS$Data
public "getSize"(): integer
public "getPosition"(): $BlockPos
public "itemWasChanged"(): boolean
public "setItem"(arg0: $ItemStack$Type): void
get "size"(): integer
get "position"(): $BlockPos
set "item"(value: $ItemStack$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnchantmentTableServerEventJS$Type = ($EnchantmentTableServerEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnchantmentTableServerEventJS_ = $EnchantmentTableServerEventJS$Type;
}}
declare module "packages/com/almostreliable/ponderjs/mixin/$SceneBuilderAccessor" {
import {$SceneBuilder$SpecialInstructions, $SceneBuilder$SpecialInstructions$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuilder$SpecialInstructions"
import {$SceneBuilder$WorldInstructions, $SceneBuilder$WorldInstructions$Type} from "packages/com/simibubi/create/foundation/ponder/$SceneBuilder$WorldInstructions"
import {$PonderScene, $PonderScene$Type} from "packages/com/simibubi/create/foundation/ponder/$PonderScene"

export interface $SceneBuilderAccessor {

 "ponderjs$getPonderScene"(): $PonderScene
 "ponderjs$setWorldInstructions"(arg0: $SceneBuilder$WorldInstructions$Type): void
 "ponderjs$setSpecialInstructions"(arg0: $SceneBuilder$SpecialInstructions$Type): void
}

export namespace $SceneBuilderAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SceneBuilderAccessor$Type = ($SceneBuilderAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SceneBuilderAccessor_ = $SceneBuilderAccessor$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/platform/$Platform" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$BlockRenderDispatcher, $BlockRenderDispatcher$Type} from "packages/net/minecraft/client/renderer/block/$BlockRenderDispatcher"
import {$MultiBufferSource$BufferSource, $MultiBufferSource$BufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource$BufferSource"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$BlockReference, $BlockReference$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$BlockReference"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$EntityRenderersEvent$RegisterRenderers, $EntityRenderersEvent$RegisterRenderers$Type} from "packages/net/minecraftforge/client/event/$EntityRenderersEvent$RegisterRenderers"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $Platform {


public static "getId"(item: $Item$Type): $ResourceLocation
public static "getId"(block: $Block$Type): $ResourceLocation
public static "getId"(entityType: $EntityType$Type<(any)>): $ResourceLocation
public static "serializeEntity"(entity: $Entity$Type): $CompoundTag
public static "serializeItemStack"(stack: $ItemStack$Type): $CompoundTag
public static "renderSingleBlock"(blockRenderer: $BlockRenderDispatcher$Type, blockReference: $BlockReference$Type, stack: $PoseStack$Type, bufferSource: $MultiBufferSource$BufferSource$Type): void
public static "sendProgressUpdate"(level: $Level$Type, pos: $BlockPos$Type, progress: integer): void
public static "sendParticleEmit"(level: $Level$Type, positions: $List$Type<($BlockPos$Type)>): void
public static "mobFromJson"(json: $JsonObject$Type): $EntityType<(any)>
public static "mobFromId"(id: $ResourceLocation$Type): $EntityType<(any)>
public static "itemStackFromJson"(json: $JsonObject$Type): $ItemStack
public static "getTagsFor"(entityType: $EntityType$Type<(any)>): $Stream<(any)>
public static "getTagsFor"(block: $Block$Type): $Stream<(any)>
public static "registerBlockEntityRenderer"(event: $EntityRenderersEvent$RegisterRenderers$Type): void
public static "sendProcessTimeUpdate"(level: $Level$Type, pos: $BlockPos$Type, processTime: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Platform$Type = ($Platform);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Platform_ = $Platform$Type;
}}
declare module "packages/com/almostreliable/lootjs/core/$ILootCondition" {
import {$ILootHandler, $ILootHandler$Type} from "packages/com/almostreliable/lootjs/core/$ILootHandler"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $ILootCondition extends $ILootHandler, $Predicate<($LootContext)> {

 "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
 "test"(arg0: $LootContext$Type): boolean
 "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
 "negate"(): $Predicate<($LootContext)>
 "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>

(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
}

export namespace $ILootCondition {
function not<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
function isEqual<T>(arg0: any): $Predicate<($LootContext)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ILootCondition$Type = ($ILootCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ILootCondition_ = $ILootCondition$Type;
}}
declare module "packages/com/almostreliable/ponderjs/util/$PonderErrorHelper" {
import {$Exception, $Exception$Type} from "packages/java/lang/$Exception"

export class $PonderErrorHelper {

constructor()

public static "yeet"(arg0: $Exception$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PonderErrorHelper$Type = ($PonderErrorHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PonderErrorHelper_ = $PonderErrorHelper$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/viewer/common/$AltarRenderer" {
import {$SizedItemRenderer, $SizedItemRenderer$Type} from "packages/com/almostreliable/summoningrituals/compat/viewer/common/$SizedItemRenderer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $AltarRenderer extends $SizedItemRenderer {

constructor(size: integer)

public "render"(guiGraphics: $GuiGraphics$Type, item: $ItemStack$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AltarRenderer$Type = ($AltarRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AltarRenderer_ = $AltarRenderer$Type;
}}
declare module "packages/com/almostreliable/ponderjs/api/$ExtendedPonderStoryBoard" {
import {$ExtendedSceneBuilder, $ExtendedSceneBuilder$Type} from "packages/com/almostreliable/ponderjs/api/$ExtendedSceneBuilder"
import {$SceneBuildingUtilDelegate, $SceneBuildingUtilDelegate$Type} from "packages/com/almostreliable/ponderjs/api/$SceneBuildingUtilDelegate"

export interface $ExtendedPonderStoryBoard {

 "program"(arg0: $ExtendedSceneBuilder$Type, arg1: $SceneBuildingUtilDelegate$Type): void

(arg0: $ExtendedSceneBuilder$Type, arg1: $SceneBuildingUtilDelegate$Type): void
}

export namespace $ExtendedPonderStoryBoard {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtendedPonderStoryBoard$Type = ($ExtendedPonderStoryBoard);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtendedPonderStoryBoard_ = $ExtendedPonderStoryBoard$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/$SummoningRitualsConstants" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SummoningRitualsConstants {
static readonly "MOD_ID": string
static readonly "MOD_NAME": string
static readonly "MOD_VERSION": string


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SummoningRitualsConstants$Type = ($SummoningRitualsConstants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SummoningRitualsConstants_ = $SummoningRitualsConstants$Type;
}}
declare module "packages/com/almostreliable/lootjs/core/$FilterResult" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$List, $List$Type} from "packages/java/util/$List"

export class $FilterResult<F, T> {


public "yeetIfUnresolvedFilters"(): void
public static "create"<F, T>(filters: $List$Type<(F)>, converter: $Function$Type<(F), (T)>): $FilterResult<(F), (T)>
public "getFoundValues"(): $List<(T)>
public "getNotFoundFilters"(): $List<(F)>
get "foundValues"(): $List<(T)>
get "notFoundFilters"(): $List<(F)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FilterResult$Type<F, T> = ($FilterResult<(F), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FilterResult_<F, T> = $FilterResult$Type<(F), (T)>;
}}
declare module "packages/com/almostreliable/lootjs/forge/gametest/conditions/$MatchEquipmentSlotTest" {
import {$GameTestHelper, $GameTestHelper$Type} from "packages/net/minecraft/gametest/framework/$GameTestHelper"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"

export class $MatchEquipmentSlotTest {

constructor()

public "matchBoots"(helper: $GameTestHelper$Type): void
public "hasStickInOffHand"(helper: $GameTestHelper$Type): void
public "basicSetup"(helper: $GameTestHelper$Type): $LootContext
public "failMatchMainHand"(helper: $GameTestHelper$Type): void
public "hasDiamondInHand"(helper: $GameTestHelper$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MatchEquipmentSlotTest$Type = ($MatchEquipmentSlotTest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MatchEquipmentSlotTest_ = $MatchEquipmentSlotTest$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$MatchPlayer" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$IExtendedLootCondition, $IExtendedLootCondition$Type} from "packages/com/almostreliable/lootjs/loot/condition/$IExtendedLootCondition"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$EntityPredicate, $EntityPredicate$Type} from "packages/net/minecraft/advancements/critereon/$EntityPredicate"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $MatchPlayer implements $IExtendedLootCondition {

constructor(predicate: $EntityPredicate$Type)

public "test"(context: $LootContext$Type): boolean
public "getType"(): $LootItemConditionType
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MatchPlayer$Type = ($MatchPlayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MatchPlayer_ = $MatchPlayer$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/results/$Info$Composite" {
import {$Icon, $Icon$Type} from "packages/com/almostreliable/lootjs/loot/results/$Icon"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Info, $Info$Type} from "packages/com/almostreliable/lootjs/loot/results/$Info"

export class $Info$Composite implements $Info {

constructor(icon: $Icon$Type, title: string)
constructor(title: string)
constructor(base: $Info$Type)

public "transform"(): string
public "getBase"(): $Info
public "getChildren"(): $Collection<($Info)>
public "addChildren"(info: $Info$Type): void
get "base"(): $Info
get "children"(): $Collection<($Info)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Info$Composite$Type = ($Info$Composite);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Info$Composite_ = $Info$Composite$Type;
}}
declare module "packages/com/almostreliable/lootjs/core/$LootModificationByEntity" {
import {$AbstractLootModification, $AbstractLootModification$Type} from "packages/com/almostreliable/lootjs/core/$AbstractLootModification"
import {$HashSet, $HashSet$Type} from "packages/java/util/$HashSet"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$ILootHandler, $ILootHandler$Type} from "packages/com/almostreliable/lootjs/core/$ILootHandler"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$List, $List$Type} from "packages/java/util/$List"

export class $LootModificationByEntity extends $AbstractLootModification {
readonly "entities": $HashSet<($EntityType<(any)>)>

constructor(name: string, entities: $HashSet$Type<($EntityType$Type<(any)>)>, handlers: $List$Type<($ILootHandler$Type)>)

public "shouldExecute"(context: $LootContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootModificationByEntity$Type = ($LootModificationByEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootModificationByEntity_ = $LootModificationByEntity$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/trades/$PotionTrade" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$MerchantOffer, $MerchantOffer$Type} from "packages/net/minecraft/world/item/trading/$MerchantOffer"
import {$TradeItem, $TradeItem$Type} from "packages/com/almostreliable/morejs/features/villager/$TradeItem"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$TransformableTrade, $TransformableTrade$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$TransformableTrade"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"

export class $PotionTrade extends $TransformableTrade<($PotionTrade)> {

constructor(arg0: ($TradeItem$Type)[])

public "item"(arg0: $Item$Type): $PotionTrade
public "createOffer"(arg0: $Entity$Type, arg1: $RandomSource$Type): $MerchantOffer
public "noBrewablePotion"(): $PotionTrade
public "onlyBrewablePotion"(): $PotionTrade
public "potions"(...arg0: ($Potion$Type)[]): $PotionTrade
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PotionTrade$Type = ($PotionTrade);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PotionTrade_ = $PotionTrade$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/network/$Packet" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export interface $Packet<T> {

 "decode"(arg0: $FriendlyByteBuf$Type): T
 "encode"(arg0: T, arg1: $FriendlyByteBuf$Type): void
 "handle"(arg0: T, arg1: $Supplier$Type<(any)>): void
}

export namespace $Packet {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Packet$Type<T> = ($Packet<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Packet_<T> = $Packet$Type<(T)>;
}}
declare module "packages/com/almostreliable/lootjs/loot/action/$CustomPlayerAction" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ILootAction, $ILootAction$Type} from "packages/com/almostreliable/lootjs/core/$ILootAction"

export class $CustomPlayerAction implements $ILootAction {

constructor(action: $Consumer$Type<($ServerPlayer$Type)>)

public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomPlayerAction$Type = ($CustomPlayerAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomPlayerAction_ = $CustomPlayerAction$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/kube/$LootModificationForgeEventJS" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$LootModificationEventJS, $LootModificationEventJS$Type} from "packages/com/almostreliable/lootjs/kube/$LootModificationEventJS"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IGlobalLootModifier, $IGlobalLootModifier$Type} from "packages/net/minecraftforge/common/loot/$IGlobalLootModifier"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LootModificationForgeEventJS extends $LootModificationEventJS {

constructor(modifiers: $Map$Type<($ResourceLocation$Type), ($IGlobalLootModifier$Type)>)

public "removeGlobalModifier"(...locationOrModIds: (string)[]): void
public "getGlobalModifiers"(): $List<(string)>
get "globalModifiers"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootModificationForgeEventJS$Type = ($LootModificationForgeEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootModificationForgeEventJS_ = $LootModificationForgeEventJS$Type;
}}
declare module "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentMenuExtension" {
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$EnchantmentMenuProcess, $EnchantmentMenuProcess$Type} from "packages/com/almostreliable/morejs/features/enchantment/$EnchantmentMenuProcess"

export interface $EnchantmentMenuExtension {

 "getMoreJSProcess"(): $EnchantmentMenuProcess
 "getMoreJsEnchantSlots"(): $Container
}

export namespace $EnchantmentMenuExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnchantmentMenuExtension$Type = ($EnchantmentMenuExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnchantmentMenuExtension_ = $EnchantmentMenuExtension$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/action/$CompositeLootAction" {
import {$ILootHandler, $ILootHandler$Type} from "packages/com/almostreliable/lootjs/core/$ILootHandler"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ILootAction, $ILootAction$Type} from "packages/com/almostreliable/lootjs/core/$ILootAction"

export class $CompositeLootAction implements $ILootAction {

constructor(handlers: $Collection$Type<($ILootHandler$Type)>)

public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompositeLootAction$Type = ($CompositeLootAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompositeLootAction_ = $CompositeLootAction$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/$SummoningRituals" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SummoningRituals {

constructor()

public "onInitialize"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SummoningRituals$Type = ($SummoningRituals);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SummoningRituals_ = $SummoningRituals$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/action/$LightningStrikeAction" {
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ILootAction, $ILootAction$Type} from "packages/com/almostreliable/lootjs/core/$ILootAction"

export class $LightningStrikeAction implements $ILootAction {

constructor(shouldDamageEntity: boolean)

public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightningStrikeAction$Type = ($LightningStrikeAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightningStrikeAction_ = $LightningStrikeAction$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$MainHandTableBonus" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$IExtendedLootCondition, $IExtendedLootCondition$Type} from "packages/com/almostreliable/lootjs/loot/condition/$IExtendedLootCondition"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $MainHandTableBonus implements $IExtendedLootCondition {

constructor(enchantment: $Enchantment$Type, values: (float)[])

public "test"(context: $LootContext$Type): boolean
public "getType"(): $LootItemConditionType
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MainHandTableBonus$Type = ($MainHandTableBonus);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MainHandTableBonus_ = $MainHandTableBonus$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/network/$PacketHandler" {
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $PacketHandler {
static readonly "CHANNEL": $SimpleChannel


public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketHandler$Type = ($PacketHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketHandler_ = $PacketHandler$Type;
}}
declare module "packages/com/almostreliable/lootjs/kube/$LootEntryWrapper" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LootEntry, $LootEntry$Type} from "packages/com/almostreliable/lootjs/core/$LootEntry"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $LootEntryWrapper {

constructor()

public static "of"(arg0: $ItemStack$Type, count: integer, nbt: $CompoundTag$Type): $LootEntry
public static "of"(arg0: $ItemStack$Type, nbt: $CompoundTag$Type): $LootEntry
public static "of"(arg0: $ItemStack$Type, count: integer): $LootEntry
public static "of"(o: any): $LootEntry
public static "ofJson"(json: $JsonObject$Type): $LootEntry
public static "withChance"(o: any, chance: integer): $LootEntry
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootEntryWrapper$Type = ($LootEntryWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootEntryWrapper_ = $LootEntryWrapper$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/$TradeFilter$Filterable" {
import {$TradeFilter, $TradeFilter$Type} from "packages/com/almostreliable/morejs/features/villager/$TradeFilter"

export interface $TradeFilter$Filterable {

 "matchesTradeFilter"(arg0: $TradeFilter$Type): boolean
}

export namespace $TradeFilter$Filterable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TradeFilter$Filterable$Type = ($TradeFilter$Filterable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TradeFilter$Filterable_ = $TradeFilter$Filterable$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/gametest/conditions/$AndConditionTest" {
import {$GameTestHelper, $GameTestHelper$Type} from "packages/net/minecraft/gametest/framework/$GameTestHelper"

export class $AndConditionTest {

constructor()

public "andConditionFails"(helper: $GameTestHelper$Type): void
public "andConditionSucceed"(helper: $GameTestHelper$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AndConditionTest$Type = ($AndConditionTest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AndConditionTest_ = $AndConditionTest$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/gametest/conditions/$NotConditionTest" {
import {$GameTestHelper, $GameTestHelper$Type} from "packages/net/minecraft/gametest/framework/$GameTestHelper"

export class $NotConditionTest {

constructor()

public "notConditionSucceed"(helper: $GameTestHelper$Type): void
public "notConditionFails"(helper: $GameTestHelper$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NotConditionTest$Type = ($NotConditionTest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NotConditionTest_ = $NotConditionTest$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$AnyStructure$StructureLocator" {
import {$Structure, $Structure$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $AnyStructure$StructureLocator {

 "getStructure"(arg0: $Registry$Type<($Structure$Type)>, arg1: $ServerLevel$Type, arg2: $BlockPos$Type): $Structure

(arg0: $Registry$Type<($Structure$Type)>, arg1: $ServerLevel$Type, arg2: $BlockPos$Type): $Structure
}

export namespace $AnyStructure$StructureLocator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnyStructure$StructureLocator$Type = ($AnyStructure$StructureLocator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnyStructure$StructureLocator_ = $AnyStructure$StructureLocator$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$MobOutput" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$RecipeOutputs$RecipeOutput, $RecipeOutputs$RecipeOutput$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$RecipeOutput"

export class $RecipeOutputs$MobOutput extends $RecipeOutputs$RecipeOutput<($EntityType<(any)>)> {


public "getCount"(): integer
get "count"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeOutputs$MobOutput$Type = ($RecipeOutputs$MobOutput);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeOutputs$MobOutput_ = $RecipeOutputs$MobOutput$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$NotCondition" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$IExtendedLootCondition, $IExtendedLootCondition$Type} from "packages/com/almostreliable/lootjs/loot/condition/$IExtendedLootCondition"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$ILootCondition, $ILootCondition$Type} from "packages/com/almostreliable/lootjs/core/$ILootCondition"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $NotCondition implements $IExtendedLootCondition {

constructor(condition: $ILootCondition$Type)

public "test"(context: $LootContext$Type): boolean
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
public "getType"(): $LootItemConditionType
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NotCondition$Type = ($NotCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NotCondition_ = $NotCondition$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/compat/viewer/jei/ingredient/item/$JEIAltarRenderer" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$AltarRenderer, $AltarRenderer$Type} from "packages/com/almostreliable/summoningrituals/compat/viewer/common/$AltarRenderer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IIngredientRenderer, $IIngredientRenderer$Type} from "packages/mezz/jei/api/ingredients/$IIngredientRenderer"

export class $JEIAltarRenderer extends $AltarRenderer implements $IIngredientRenderer<($ItemStack)> {

constructor(size: integer)

public "getWidth"(): integer
public "getHeight"(): integer
public "getFontRenderer"(arg0: $Minecraft$Type, arg1: $ItemStack$Type): $Font
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JEIAltarRenderer$Type = ($JEIAltarRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEIAltarRenderer_ = $JEIAltarRenderer$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/trades/$TreasureMapTrade" {
import {$MerchantOffer, $MerchantOffer$Type} from "packages/net/minecraft/world/item/trading/$MerchantOffer"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TradeItem, $TradeItem$Type} from "packages/com/almostreliable/morejs/features/villager/$TradeItem"
import {$MapDecoration$Type, $MapDecoration$Type$Type} from "packages/net/minecraft/world/level/saveddata/maps/$MapDecoration$Type"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$TransformableTrade, $TransformableTrade$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$TransformableTrade"
import {$WeightedList, $WeightedList$Type} from "packages/com/almostreliable/morejs/util/$WeightedList"
import {$MapPosInfo$Provider, $MapPosInfo$Provider$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$MapPosInfo$Provider"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $TreasureMapTrade extends $TransformableTrade<($TreasureMapTrade)> {

constructor(arg0: ($TradeItem$Type)[], arg1: $MapPosInfo$Provider$Type)

public "scale"(arg0: byte): $TreasureMapTrade
public "displayName"(arg0: $Component$Type): $TreasureMapTrade
public "createOffer"(arg0: $Entity$Type, arg1: $RandomSource$Type): $MerchantOffer
public "noPreview"(): $TreasureMapTrade
public "marker"(arg0: $MapDecoration$Type$Type): $TreasureMapTrade
public static "forStructure"(arg0: ($TradeItem$Type)[], arg1: $WeightedList$Type<(any)>): $TreasureMapTrade
public static "forBiome"(arg0: ($TradeItem$Type)[], arg1: $WeightedList$Type<(any)>): $TreasureMapTrade
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TreasureMapTrade$Type = ($TreasureMapTrade);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TreasureMapTrade_ = $TreasureMapTrade$Type;
}}
declare module "packages/com/almostreliable/lootjs/predicate/$CustomItemPredicate" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$ItemPredicate, $ItemPredicate$Type} from "packages/net/minecraft/advancements/critereon/$ItemPredicate"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $CustomItemPredicate extends $ItemPredicate {
static readonly "ANY": $ItemPredicate
 "items": $Set<($Item)>

constructor(predicate: $Predicate$Type<($ItemStack$Type)>)

public "serializeToJson"(): $JsonElement
public "matches"(item: $ItemStack$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomItemPredicate$Type = ($CustomItemPredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomItemPredicate_ = $CustomItemPredicate$Type;
}}
declare module "packages/com/almostreliable/ponderjs/$PonderJS" {
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$PonderTag, $PonderTag$Type} from "packages/com/simibubi/create/foundation/ponder/$PonderTag"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$AllIcons, $AllIcons$Type} from "packages/com/simibubi/create/foundation/gui/$AllIcons"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$PonderStoriesManager, $PonderStoriesManager$Type} from "packages/com/almostreliable/ponderjs/$PonderStoriesManager"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $PonderJS {
static readonly "LOGGER": $Logger
static readonly "TAG_EVENT": string
static readonly "REGISTRY_EVENT": string
static readonly "NAMESPACES": $Set<(string)>
static readonly "CACHED_ICONS": $HashMap<(string), ($AllIcons)>
static readonly "STORIES_MANAGER": $PonderStoriesManager

constructor()

public static "init"(): void
public static "reload"(): void
public static "getTagByName"(arg0: string): $Optional<($PonderTag)>
public static "getTagByName"(arg0: $ResourceLocation$Type): $Optional<($PonderTag)>
public static "appendCreateToId"(arg0: string): $ResourceLocation
public static "getIconByName"(arg0: string): $AllIcons
public static "appendKubeToId"(arg0: string): $ResourceLocation
public static "isInitialized"(): boolean
get "initialized"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PonderJS$Type = ($PonderJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PonderJS_ = $PonderJS$Type;
}}
declare module "packages/com/almostreliable/morejs/features/teleport/$EntityTeleportsEventJS" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$TeleportType, $TeleportType$Type} from "packages/com/almostreliable/morejs/features/teleport/$TeleportType"
import {$EntityEventJS, $EntityEventJS$Type} from "packages/dev/latvian/mods/kubejs/entity/$EntityEventJS"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EntityTeleportsEventJS extends $EntityEventJS {

constructor(arg0: $Entity$Type, arg1: double, arg2: double, arg3: double, arg4: $TeleportType$Type)
constructor(arg0: $Entity$Type, arg1: double, arg2: double, arg3: double, arg4: $Level$Type, arg5: $TeleportType$Type)

public "getType"(): $TeleportType
public "getY"(): double
public "setX"(arg0: double): void
public "getZ"(): double
public "setY"(arg0: double): void
public "setZ"(arg0: double): void
public "getX"(): double
public "getEntity"(): $Entity
get "type"(): $TeleportType
get "y"(): double
set "x"(value: double)
get "z"(): double
set "y"(value: double)
set "z"(value: double)
get "x"(): double
get "entity"(): $Entity
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityTeleportsEventJS$Type = ($EntityTeleportsEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityTeleportsEventJS_ = $EntityTeleportsEventJS$Type;
}}
declare module "packages/com/almostreliable/lootjs/$LootJS" {
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $LootJS {
static readonly "LOG": $Logger
static readonly "CONDITION_GSON": $Gson
static readonly "FUNCTION_GSON": $Gson

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootJS$Type = ($LootJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootJS_ = $LootJS$Type;
}}
declare module "packages/com/almostreliable/morejs/features/misc/$PiglinPlayerBehaviorEventJS" {
import {$PiglinPlayerBehaviorEventJS$PiglinBehavior, $PiglinPlayerBehaviorEventJS$PiglinBehavior$Type} from "packages/com/almostreliable/morejs/features/misc/$PiglinPlayerBehaviorEventJS$PiglinBehavior"
import {$PlayerEventJS, $PlayerEventJS$Type} from "packages/dev/latvian/mods/kubejs/player/$PlayerEventJS"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Piglin, $Piglin$Type} from "packages/net/minecraft/world/entity/monster/piglin/$Piglin"

export class $PiglinPlayerBehaviorEventJS extends $PlayerEventJS {

constructor(arg0: $Piglin$Type, arg1: $Player$Type, arg2: $Optional$Type<($Player$Type)>)

public "isIgnoreHoldingCheck"(): boolean
public "getPreviousTargetPlayer"(): $Player
public "isAggressiveAlready"(): boolean
public "ignoreHoldingCheck"(): void
public "getBehavior"(): $PiglinPlayerBehaviorEventJS$PiglinBehavior
public "getPiglin"(): $Piglin
public "setBehavior"(arg0: $PiglinPlayerBehaviorEventJS$PiglinBehavior$Type): void
get "previousTargetPlayer"(): $Player
get "aggressiveAlready"(): boolean
get "behavior"(): $PiglinPlayerBehaviorEventJS$PiglinBehavior
get "piglin"(): $Piglin
set "behavior"(value: $PiglinPlayerBehaviorEventJS$PiglinBehavior$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PiglinPlayerBehaviorEventJS$Type = ($PiglinPlayerBehaviorEventJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PiglinPlayerBehaviorEventJS_ = $PiglinPlayerBehaviorEventJS$Type;
}}
declare module "packages/com/almostreliable/ponderjs/commands/$GenerateKubeJSLangCommand" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$Command, $Command$Type} from "packages/com/mojang/brigadier/$Command"
import {$CommandContext, $CommandContext$Type} from "packages/com/mojang/brigadier/context/$CommandContext"

export class $GenerateKubeJSLangCommand implements $Command<($CommandSourceStack)> {

constructor()

public "run"(arg0: $CommandContext$Type<($CommandSourceStack$Type)>): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenerateKubeJSLangCommand$Type = ($GenerateKubeJSLangCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenerateKubeJSLangCommand_ = $GenerateKubeJSLangCommand$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/$LootFunctionsContainer" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ItemFilter, $ItemFilter$Type} from "packages/com/almostreliable/lootjs/filters/$ItemFilter"
import {$LootItemFunction$Builder, $LootItemFunction$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/functions/$LootItemFunction$Builder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$LootItemFunction, $LootItemFunction$Type} from "packages/net/minecraft/world/level/storage/loot/functions/$LootItemFunction"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$AddAttributesFunction$Builder, $AddAttributesFunction$Builder$Type} from "packages/com/almostreliable/lootjs/loot/$AddAttributesFunction$Builder"
import {$NumberProvider, $NumberProvider$Type} from "packages/net/minecraft/world/level/storage/loot/providers/number/$NumberProvider"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"

export interface $LootFunctionsContainer<F extends $LootFunctionsContainer<(any)>> {

 "setName"(component: $Component$Type): F
 "functions"(filter: $ItemFilter$Type, action: $Consumer$Type<($LootFunctionsContainer$Type<(F)>)>): F
 "addAttributes"(action: $Consumer$Type<($AddAttributesFunction$Builder$Type)>): F
 "addLore"(...components: ($Component$Type)[]): F
 "addFunction"(builder: $LootItemFunction$Builder$Type): F
 "addFunction"(arg0: $LootItemFunction$Type): F
 "damage"(numberProvider: $NumberProvider$Type): F
 "simulateExplosionDecay"(): F
 "applyBinomialDistributionBonus"(enchantment: $Enchantment$Type, probability: float, n: integer): F
 "enchantWithLevels"(numberProvider: $NumberProvider$Type, allowTreasure: boolean): F
 "enchantWithLevels"(numberProvider: $NumberProvider$Type): F
 "applyLootingBonus"(numberProvider: $NumberProvider$Type): F
 "applyOreBonus"(enchantment: $Enchantment$Type): F
 "enchantRandomly"(): F
 "enchantRandomly"(enchantments: ($Enchantment$Type)[]): F
 "smeltLoot"(): F
 "addPotion"(potion: $Potion$Type): F
 "limitCount"(numberProviderMin: $NumberProvider$Type, numberProviderMax: $NumberProvider$Type): F
 "limitCount"(numberProvider: $NumberProvider$Type): F
 "replaceLore"(...components: ($Component$Type)[]): F
 "addNbt"(tag: $CompoundTag$Type): F
 "addNBT"(tag: $CompoundTag$Type): F
 "applyBonus"(enchantment: $Enchantment$Type, multiplier: integer): F
 "customFunction"(json: $JsonObject$Type): F

(component: $Component$Type): F
}

export namespace $LootFunctionsContainer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootFunctionsContainer$Type<F> = ($LootFunctionsContainer<(F)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootFunctionsContainer_<F> = $LootFunctionsContainer$Type<(F)>;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$BiomeCheck" {
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$IExtendedLootCondition, $IExtendedLootCondition$Type} from "packages/com/almostreliable/lootjs/loot/condition/$IExtendedLootCondition"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $BiomeCheck implements $IExtendedLootCondition {

constructor(biomes: $List$Type<($ResourceKey$Type<($Biome$Type)>)>, tags: $List$Type<($TagKey$Type<($Biome$Type)>)>)

public "test"(context: $LootContext$Type): boolean
public "getBiomes"(): $List<($ResourceKey<($Biome)>)>
public "getTags"(): $List<($TagKey<($Biome)>)>
public "getType"(): $LootItemConditionType
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
get "biomes"(): $List<($ResourceKey<($Biome)>)>
get "tags"(): $List<($TagKey<($Biome)>)>
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeCheck$Type = ($BiomeCheck);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeCheck_ = $BiomeCheck$Type;
}}
declare module "packages/com/almostreliable/morejs/$MoreJS" {
import {$MoreJSPlatform, $MoreJSPlatform$Type} from "packages/com/almostreliable/morejs/$MoreJSPlatform"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $MoreJS {
static readonly "LOG": $Logger
static readonly "PLATFORM": $MoreJSPlatform
static readonly "DISABLED_TAG": string

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MoreJS$Type = ($MoreJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MoreJS_ = $MoreJS$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/results/$LootInfoCollector" {
import {$ILootHandler, $ILootHandler$Type} from "packages/com/almostreliable/lootjs/core/$ILootHandler"
import {$LootItemFunction, $LootItemFunction$Type} from "packages/net/minecraft/world/level/storage/loot/functions/$LootItemFunction"
import {$Info$Composite, $Info$Composite$Type} from "packages/com/almostreliable/lootjs/loot/results/$Info$Composite"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$StringBuilder, $StringBuilder$Type} from "packages/java/lang/$StringBuilder"
import {$Info, $Info$Type} from "packages/com/almostreliable/lootjs/loot/results/$Info"

export class $LootInfoCollector {
static readonly "COMPOSITES": ($Class<(any)>)[]

constructor()

public "add"(info: $Info$Type): void
public static "append"(info: $Info$Type, indentDepth: integer, sb: $StringBuilder$Type): void
public "append"(stringBuilder: $StringBuilder$Type, indentDepth: integer): void
public static "create"(collector: $LootInfoCollector$Type, lootHandler: $ILootHandler$Type): $Info
public "pop"(): $Info$Composite
public static "finalizeInfo"(collector: $LootInfoCollector$Type, info: $Info$Type, result: boolean): void
public static "finalizeInfo"(collector: $LootInfoCollector$Type, info: $Info$Type): void
public static "createInfo"(collector: $LootInfoCollector$Type, info: $Info$Type): $Info
public "getFirstLayer"(): $Collection<($Info)>
public "addOrPush"(info: $Info$Type): void
public static "createFunctionInfo"(collector: $LootInfoCollector$Type, arg1: $LootItemFunction$Type): void
get "firstLayer"(): $Collection<($Info)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootInfoCollector$Type = ($LootInfoCollector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootInfoCollector_ = $LootInfoCollector$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/altar/$AltarBlockEntity" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ClientGamePacketListener, $ClientGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ClientGamePacketListener"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$AltarObservable, $AltarObservable$Type} from "packages/com/almostreliable/summoningrituals/altar/$AltarObservable"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$PlatformBlockEntity, $PlatformBlockEntity$Type} from "packages/com/almostreliable/summoningrituals/platform/$PlatformBlockEntity"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $AltarBlockEntity extends $PlatformBlockEntity {
static readonly "SUMMONING_START": $AltarObservable
static readonly "SUMMONING_COMPLETE": $AltarObservable
 "blockState": $BlockState

constructor(pos: $BlockPos$Type, state: $BlockState$Type)

public "getProcessTime"(): integer
public "load"(tag: $CompoundTag$Type): void
public "setProcessTime"(processTime: integer): void
public "handleInteraction"(player: $ServerPlayer$Type, stack: $ItemStack$Type): $ItemStack
public "getUpdateTag"(): $CompoundTag
public "getUpdatePacket"(): $Packet<($ClientGamePacketListener)>
get "processTime"(): integer
set "processTime"(value: integer)
get "updateTag"(): $CompoundTag
get "updatePacket"(): $Packet<($ClientGamePacketListener)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AltarBlockEntity$Type = ($AltarBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AltarBlockEntity_ = $AltarBlockEntity$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$CustomParamPredicate" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$IExtendedLootCondition, $IExtendedLootCondition$Type} from "packages/com/almostreliable/lootjs/loot/condition/$IExtendedLootCondition"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $CustomParamPredicate<T> implements $IExtendedLootCondition {

constructor(param: $LootContextParam$Type<(T)>, predicate: $Predicate$Type<(T)>)

public "test"(lootContext: $LootContext$Type): boolean
public "getType"(): $LootItemConditionType
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomParamPredicate$Type<T> = ($CustomParamPredicate<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomParamPredicate_<T> = $CustomParamPredicate$Type<(T)>;
}}
declare module "packages/com/almostreliable/lootjs/kube/wrapper/$IntervalJS" {
import {$MinMaxBounds$Ints, $MinMaxBounds$Ints$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Ints"
import {$MinMaxBounds$Doubles, $MinMaxBounds$Doubles$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Doubles"

export class $IntervalJS {

constructor()

public "toString"(): string
public "min"(min: double): $IntervalJS
public "max"(max: double): $IntervalJS
public "matches"(value: double): boolean
public "between"(min: double, max: double): $IntervalJS
public "getVanillaDoubles"(): $MinMaxBounds$Doubles
public "getVanillaInt"(): $MinMaxBounds$Ints
public "matchesSqr"(value: double): boolean
public static "ofInt"(o: any): $MinMaxBounds$Ints
public static "ofDoubles"(o: any): $MinMaxBounds$Doubles
get "vanillaDoubles"(): $MinMaxBounds$Doubles
get "vanillaInt"(): $MinMaxBounds$Ints
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntervalJS$Type = ($IntervalJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntervalJS_ = $IntervalJS$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/util/$Utils" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $Utils {


public static "cast"<T>(o: any): T
public static "getRL"(key: string): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Utils$Type = ($Utils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Utils_ = $Utils$Type;
}}
declare module "packages/com/almostreliable/ponderjs/$PonderJSMod" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PonderJSMod {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PonderJSMod$Type = ($PonderJSMod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PonderJSMod_ = $PonderJSMod$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/trades/$CustomTrade" {
import {$MerchantOffer, $MerchantOffer$Type} from "packages/net/minecraft/world/item/trading/$MerchantOffer"
import {$TransformableTrade$Transformer, $TransformableTrade$Transformer$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$TransformableTrade$Transformer"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$VillagerTrades$ItemListing, $VillagerTrades$ItemListing$Type} from "packages/net/minecraft/world/entity/npc/$VillagerTrades$ItemListing"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $CustomTrade implements $VillagerTrades$ItemListing {

constructor(arg0: $TransformableTrade$Transformer$Type)

public "getOffer"(arg0: $Entity$Type, arg1: $RandomSource$Type): $MerchantOffer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomTrade$Type = ($CustomTrade);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomTrade_ = $CustomTrade$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/recipe/component/$IngredientStack" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"

export class $IngredientStack extends $Record {

constructor(ingredient: $Ingredient$Type, count: integer)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "count"(): integer
public static "fromJson"(json: $JsonElement$Type): $IngredientStack
public static "fromNetwork"(buffer: $FriendlyByteBuf$Type): $IngredientStack
public "toNetwork"(buffer: $FriendlyByteBuf$Type): void
public "ingredient"(): $Ingredient
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientStack$Type = ($IngredientStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientStack_ = $IngredientStack$Type;
}}
declare module "packages/com/almostreliable/morejs/util/$LevelUtils" {
import {$Structure, $Structure$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ResourceOrTag, $ResourceOrTag$Type} from "packages/com/almostreliable/morejs/util/$ResourceOrTag"

export class $LevelUtils {

constructor()

public static "findStructure"(arg0: $BlockPos$Type, arg1: $ServerLevel$Type, arg2: $ResourceOrTag$Type<($Structure$Type)>, arg3: integer): $BlockPos
public static "findBiome"(arg0: $BlockPos$Type, arg1: $ServerLevel$Type, arg2: $ResourceOrTag$Type<($Biome$Type)>, arg3: integer): $BlockPos
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LevelUtils$Type = ($LevelUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LevelUtils_ = $LevelUtils$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/action/$WeightedAddLootAction" {
import {$SimpleWeightedRandomList, $SimpleWeightedRandomList$Type} from "packages/net/minecraft/util/random/$SimpleWeightedRandomList"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LootEntry, $LootEntry$Type} from "packages/com/almostreliable/lootjs/core/$LootEntry"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ILootAction, $ILootAction$Type} from "packages/com/almostreliable/lootjs/core/$ILootAction"
import {$NumberProvider, $NumberProvider$Type} from "packages/net/minecraft/world/level/storage/loot/providers/number/$NumberProvider"

export class $WeightedAddLootAction implements $ILootAction {

constructor(numberProvider: $NumberProvider$Type, weightedRandomList: $SimpleWeightedRandomList$Type<($LootEntry$Type)>, allowDuplicateLoot: boolean)

public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WeightedAddLootAction$Type = ($WeightedAddLootAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WeightedAddLootAction_ = $WeightedAddLootAction$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/gametest/$GameTestUtils" {
import {$GameTestHelper, $GameTestHelper$Type} from "packages/net/minecraft/gametest/framework/$GameTestHelper"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ILootContextData, $ILootContextData$Type} from "packages/com/almostreliable/lootjs/core/$ILootContextData"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $GameTestUtils {

constructor()

public static "simpleEntity"<E extends $Entity>(entityType: $EntityType$Type<(E)>, level: $ServerLevel$Type, pos: $BlockPos$Type): E
public static "assertTrue"(helper: $GameTestHelper$Type, condition: boolean): void
public static "assertTrue"(helper: $GameTestHelper$Type, condition: boolean, message: string): void
public static "assertNotNull"(helper: $GameTestHelper$Type, actual: any): void
public static "fillExampleLoot"(context: $LootContext$Type): $ILootContextData
public static "fillExampleLoot"(context: $LootContext$Type, ...items: ($Item$Type)[]): $ILootContextData
public static "fillExampleLoot"(context: $LootContext$Type, ...items: ($ItemStack$Type)[]): $ILootContextData
public static "chestContext"(level: $ServerLevel$Type, origin: $Vec3$Type, player: $Player$Type): $LootContext
public static "unknownContext"(level: $ServerLevel$Type, origin: $Vec3$Type): $LootContext
public static "assertFalse"(helper: $GameTestHelper$Type, condition: boolean, message: string): void
public static "assertFalse"(helper: $GameTestHelper$Type, condition: boolean): void
public static "assertEquals"(helper: $GameTestHelper$Type, expected: any, actual: any): void
public static "assertNotEquals"(helper: $GameTestHelper$Type, expected: any, actual: any): void
public static "assertNull"(helper: $GameTestHelper$Type, actual: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GameTestUtils$Type = ($GameTestUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GameTestUtils_ = $GameTestUtils$Type;
}}
declare module "packages/com/almostreliable/lootjs/core/$LootJSParamSets" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootInfoCollector, $LootInfoCollector$Type} from "packages/com/almostreliable/lootjs/loot/results/$LootInfoCollector"
import {$ILootContextData, $ILootContextData$Type} from "packages/com/almostreliable/lootjs/core/$ILootContextData"

export class $LootJSParamSets {
static readonly "DATA": $LootContextParam<($ILootContextData)>
static readonly "RESULT_COLLECTOR": $LootContextParam<($LootInfoCollector)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootJSParamSets$Type = ($LootJSParamSets);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootJSParamSets_ = $LootJSParamSets$Type;
}}
declare module "packages/com/almostreliable/ponderjs/particles/$ParticleTransformation" {
import {$ParticleTransformation$Data, $ParticleTransformation$Data$Type} from "packages/com/almostreliable/ponderjs/particles/$ParticleTransformation$Data"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ParticleTransformation$Simple, $ParticleTransformation$Simple$Type} from "packages/com/almostreliable/ponderjs/particles/$ParticleTransformation$Simple"

export interface $ParticleTransformation {

 "apply"(arg0: float, arg1: $Vec3$Type, arg2: $Vec3$Type): $ParticleTransformation$Data

(arg0: float, arg1: $Vec3$Type, arg2: $Vec3$Type): $ParticleTransformation$Data
}

export namespace $ParticleTransformation {
function onlyMotion(arg0: $ParticleTransformation$Simple$Type): $ParticleTransformation
function onlyPosition(arg0: $ParticleTransformation$Simple$Type): $ParticleTransformation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleTransformation$Type = ($ParticleTransformation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleTransformation_ = $ParticleTransformation$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/platform/$TagSerializable" {
import {$Tag, $Tag$Type} from "packages/net/minecraft/nbt/$Tag"

export interface $TagSerializable<T extends $Tag> {

 "deserialize"(arg0: T): void
 "serialize"(): T
}

export namespace $TagSerializable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TagSerializable$Type<T> = ($TagSerializable<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TagSerializable_<T> = $TagSerializable$Type<(T)>;
}}
declare module "packages/com/almostreliable/lootjs/kube/action/$CustomJSAction" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$LootContextJS, $LootContextJS$Type} from "packages/com/almostreliable/lootjs/kube/$LootContextJS"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ILootAction, $ILootAction$Type} from "packages/com/almostreliable/lootjs/core/$ILootAction"

export class $CustomJSAction implements $ILootAction {

constructor(pAction: $Consumer$Type<($LootContextJS$Type)>)

public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomJSAction$Type = ($CustomJSAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomJSAction_ = $CustomJSAction$Type;
}}
declare module "packages/com/almostreliable/lootjs/$LootJSPlatform" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$BindingsEvent, $BindingsEvent$Type} from "packages/dev/latvian/mods/kubejs/script/$BindingsEvent"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export interface $LootJSPlatform {

 "registerBindings"(arg0: $BindingsEvent$Type): void
 "isDevelopmentEnvironment"(): boolean
 "setQueriedLootTableId"(arg0: $LootContext$Type, arg1: $ResourceLocation$Type): void
 "getQueriedLootTableId"(arg0: $LootContext$Type): $ResourceLocation
 "getPlatformName"(): string
 "isModLoaded"(arg0: string): boolean
 "getRegistryName"(arg0: $Block$Type): $ResourceLocation
 "getRegistryName"(arg0: $EntityType$Type<(any)>): $ResourceLocation
}

export namespace $LootJSPlatform {
const INSTANCE: $LootJSPlatform
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootJSPlatform$Type = ($LootJSPlatform);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootJSPlatform_ = $LootJSPlatform$Type;
}}
declare module "packages/com/almostreliable/morejs/features/villager/trades/$MapPosInfo$Provider" {
import {$MapPosInfo, $MapPosInfo$Type} from "packages/com/almostreliable/morejs/features/villager/trades/$MapPosInfo"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $MapPosInfo$Provider {

 "apply"(arg0: $ServerLevel$Type, arg1: $Entity$Type): $MapPosInfo

(arg0: $ServerLevel$Type, arg1: $Entity$Type): $MapPosInfo
}

export namespace $MapPosInfo$Provider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapPosInfo$Provider$Type = ($MapPosInfo$Provider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapPosInfo$Provider_ = $MapPosInfo$Provider$Type;
}}
declare module "packages/com/almostreliable/lootjs/core/$LootModificationByTable" {
import {$AbstractLootModification, $AbstractLootModification$Type} from "packages/com/almostreliable/lootjs/core/$AbstractLootModification"
import {$ILootHandler, $ILootHandler$Type} from "packages/com/almostreliable/lootjs/core/$ILootHandler"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocationFilter, $ResourceLocationFilter$Type} from "packages/com/almostreliable/lootjs/filters/$ResourceLocationFilter"

export class $LootModificationByTable extends $AbstractLootModification {
readonly "filters": ($ResourceLocationFilter)[]

constructor(name: string, filters: ($ResourceLocationFilter$Type)[], handlers: $List$Type<($ILootHandler$Type)>)

public "shouldExecute"(context: $LootContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootModificationByTable$Type = ($LootModificationByTable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootModificationByTable_ = $LootModificationByTable$Type;
}}
declare module "packages/com/almostreliable/lootjs/kube/$LootContextJS" {
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$LootEntry, $LootEntry$Type} from "packages/com/almostreliable/lootjs/core/$LootEntry"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockContainerJS, $BlockContainerJS$Type} from "packages/dev/latvian/mods/kubejs/level/$BlockContainerJS"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ItemFilter, $ItemFilter$Type} from "packages/com/almostreliable/lootjs/filters/$ItemFilter"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$LootContextType, $LootContextType$Type} from "packages/com/almostreliable/lootjs/core/$LootContextType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LootContextJS {

constructor(context: $LootContext$Type)

public "forEachLoot"(action: $Consumer$Type<($ItemStack$Type)>): void
public "getLuck"(): float
public "hasLoot"(ingredient: $ItemFilter$Type): boolean
public "getVanillaContext"(): $LootContext
public "getDestroyedBlock"(): $BlockContainerJS
public "getLooting"(): integer
public "findLoot"(itemFilter: $ItemFilter$Type): $List<($ItemStack)>
public "getKillerEntity"(): $Entity
public "isExploded"(): boolean
public "getType"(): $LootContextType
public "cancel"(): void
public "getLevel"(): $ServerLevel
public "getPosition"(): $Vec3
public "getExplosionRadius"(): float
public "getLoot"(): $List<($ItemStack)>
public "isCanceled"(): boolean
public "getDamageSource"(): $DamageSource
public "getServer"(): $MinecraftServer
public "getPlayer"(): $ServerPlayer
public "getEntity"(): $Entity
public "removeLoot"(itemFilter: $ItemFilter$Type): void
public "lootSize"(): integer
public "getBlockPos"(): $BlockPos
public "getLootTableId"(): $ResourceLocation
public "getTool"(): $ItemStack
public "getCustomData"(): $Map<(string), (any)>
public "getRandom"(): $RandomSource
public "addLoot"(lootEntry: $LootEntry$Type): void
get "luck"(): float
get "vanillaContext"(): $LootContext
get "destroyedBlock"(): $BlockContainerJS
get "looting"(): integer
get "killerEntity"(): $Entity
get "exploded"(): boolean
get "type"(): $LootContextType
get "level"(): $ServerLevel
get "position"(): $Vec3
get "explosionRadius"(): float
get "loot"(): $List<($ItemStack)>
get "canceled"(): boolean
get "damageSource"(): $DamageSource
get "server"(): $MinecraftServer
get "player"(): $ServerPlayer
get "entity"(): $Entity
get "blockPos"(): $BlockPos
get "lootTableId"(): $ResourceLocation
get "tool"(): $ItemStack
get "customData"(): $Map<(string), (any)>
get "random"(): $RandomSource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootContextJS$Type = ($LootContextJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootContextJS_ = $LootContextJS$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/$AddAttributesFunction" {
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$LootItemFunction, $LootItemFunction$Type} from "packages/net/minecraft/world/level/storage/loot/functions/$LootItemFunction"
import {$LootItemFunctionType, $LootItemFunctionType$Type} from "packages/net/minecraft/world/level/storage/loot/functions/$LootItemFunctionType"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AddAttributesFunction$Modifier, $AddAttributesFunction$Modifier$Type} from "packages/com/almostreliable/lootjs/loot/$AddAttributesFunction$Modifier"

export class $AddAttributesFunction implements $LootItemFunction {

constructor(preserveDefaultModifier: boolean, modifiers: $List$Type<($AddAttributesFunction$Modifier$Type)>)

public "apply"(itemStack: $ItemStack$Type, context: $LootContext$Type): $ItemStack
public "preserveDefaultAttributes"(itemStack: $ItemStack$Type, slot: $EquipmentSlot$Type): void
public "getType"(): $LootItemFunctionType
public static "decorate"(arg0: $BiFunction$Type<($ItemStack$Type), ($LootContext$Type), ($ItemStack$Type)>, arg1: $Consumer$Type<($ItemStack$Type)>, arg2: $LootContext$Type): $Consumer<($ItemStack)>
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
public "andThen"<V>(arg0: $Function$Type<(any), (any)>): $BiFunction<($ItemStack), ($LootContext), (V)>
get "type"(): $LootItemFunctionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AddAttributesFunction$Type = ($AddAttributesFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AddAttributesFunction_ = $AddAttributesFunction$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/gametest/conditions/$BiomeCheckTest" {
import {$GameTestHelper, $GameTestHelper$Type} from "packages/net/minecraft/gametest/framework/$GameTestHelper"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $BiomeCheckTest {

constructor()

public "BiomeCheck_fail"(helper: $GameTestHelper$Type): void
public "AnyBiomeCheck_fail"(helper: $GameTestHelper$Type): void
public "BiomeCheck_match"(helper: $GameTestHelper$Type): void
public "biome"(biome: $ResourceLocation$Type): $ResourceKey<($Biome)>
public "AnyBiomeCheck_matchTags"(helper: $GameTestHelper$Type): void
public "BiomeCheck_matchAllTags"(helper: $GameTestHelper$Type): void
public "BiomeCheck_failAllTags"(helper: $GameTestHelper$Type): void
public "AnyBiomeCheck_failAllTags"(helper: $GameTestHelper$Type): void
public "AnyBiomeCheck_match"(helper: $GameTestHelper$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeCheckTest$Type = ($BiomeCheckTest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeCheckTest_ = $BiomeCheckTest$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$TriConsumer, $TriConsumer$Type} from "packages/org/apache/logging/log4j/util/$TriConsumer"
import {$JsonArray, $JsonArray$Type} from "packages/com/google/gson/$JsonArray"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$RecipeOutputs$OutputType, $RecipeOutputs$OutputType$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$OutputType"
import {$RecipeOutputs$RecipeOutput, $RecipeOutputs$RecipeOutput$Type} from "packages/com/almostreliable/summoningrituals/recipe/component/$RecipeOutputs$RecipeOutput"

export class $RecipeOutputs {

constructor()

public "add"(output: $RecipeOutputs$RecipeOutput$Type<(any)>): void
public "size"(): integer
public "forEach"(consumer: $TriConsumer$Type<($RecipeOutputs$OutputType$Type), ($RecipeOutputs$RecipeOutput$Type<(any)>), (integer)>): void
public static "fromJson"(json: $JsonArray$Type): $RecipeOutputs
public "toJson"(): $JsonArray
public static "fromNetwork"(buffer: $FriendlyByteBuf$Type): $RecipeOutputs
public "handleRecipe"(level: $ServerLevel$Type, origin: $BlockPos$Type): void
public "toNetwork"(buffer: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeOutputs$Type = ($RecipeOutputs);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeOutputs_ = $RecipeOutputs$Type;
}}
declare module "packages/com/almostreliable/lootjs/forge/gametest/conditions/$ParamSetTest" {
import {$GameTestHelper, $GameTestHelper$Type} from "packages/net/minecraft/gametest/framework/$GameTestHelper"

export class $ParamSetTest {

constructor()

public "block"(helper: $GameTestHelper$Type): void
public "chest"(helper: $GameTestHelper$Type): void
public "entity"(helper: $GameTestHelper$Type): void
public "fishing"(helper: $GameTestHelper$Type): void
public "unknown"(helper: $GameTestHelper$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParamSetTest$Type = ($ParamSetTest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParamSetTest_ = $ParamSetTest$Type;
}}
declare module "packages/com/almostreliable/morejs/util/$ResourceOrTag" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $ResourceOrTag<T> {


public "getName"(): $Component
public static "get"<E>(arg0: string, arg1: $ResourceKey$Type<($Registry$Type<(E)>)>): $ResourceOrTag<(E)>
public "asHolderSet"(arg0: $Registry$Type<(T)>): $Optional<(any)>
public "asHolderPredicate"(): $Predicate<($Holder<(T)>)>
get "name"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResourceOrTag$Type<T> = ($ResourceOrTag<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResourceOrTag_<T> = $ResourceOrTag$Type<(T)>;
}}
declare module "packages/com/almostreliable/lootjs/forge/gametest/$LootActionGameTests" {
import {$GameTestHelper, $GameTestHelper$Type} from "packages/net/minecraft/gametest/framework/$GameTestHelper"

export class $LootActionGameTests {

constructor()

public "addLootAction"(helper: $GameTestHelper$Type): void
public "replaceLootAction"(helper: $GameTestHelper$Type): void
public "removeLootAction"(helper: $GameTestHelper$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootActionGameTests$Type = ($LootActionGameTests);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootActionGameTests_ = $LootActionGameTests$Type;
}}
declare module "packages/com/almostreliable/morejs/mixin/$BrewingRecipeRegistryAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $BrewingRecipeRegistryAccessor {

}

export namespace $BrewingRecipeRegistryAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BrewingRecipeRegistryAccessor$Type = ($BrewingRecipeRegistryAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BrewingRecipeRegistryAccessor_ = $BrewingRecipeRegistryAccessor$Type;
}}
declare module "packages/com/almostreliable/lootjs/loot/condition/$AnyStructure" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$IExtendedLootCondition, $IExtendedLootCondition$Type} from "packages/com/almostreliable/lootjs/loot/condition/$IExtendedLootCondition"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$AnyStructure$StructureLocator, $AnyStructure$StructureLocator$Type} from "packages/com/almostreliable/lootjs/loot/condition/$AnyStructure$StructureLocator"

export class $AnyStructure implements $IExtendedLootCondition {

constructor(structureLocators: $List$Type<($AnyStructure$StructureLocator$Type)>, exact: boolean)

public "test"(context: $LootContext$Type): boolean
public "isExact"(): boolean
public "getType"(): $LootItemConditionType
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "validate"(arg0: $ValidationContext$Type): void
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
get "exact"(): boolean
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnyStructure$Type = ($AnyStructure);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnyStructure_ = $AnyStructure$Type;
}}
declare module "packages/com/almostreliable/summoningrituals/network/$ServerToClientPacket" {
import {$Packet, $Packet$Type} from "packages/com/almostreliable/summoningrituals/network/$Packet"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $ServerToClientPacket<T> implements $Packet<(T)> {

constructor()

public "handle"(packet: T, context: $Supplier$Type<(any)>): void
public "decode"(arg0: $FriendlyByteBuf$Type): T
public "encode"(arg0: T, arg1: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerToClientPacket$Type<T> = ($ServerToClientPacket<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerToClientPacket_<T> = $ServerToClientPacket$Type<(T)>;
}}
