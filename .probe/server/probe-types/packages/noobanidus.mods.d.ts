declare module "packages/noobanidus/mods/lootr/api/$LootFiller" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $LootFiller {

 "unpackLootTable"(arg0: $Player$Type, arg1: $Container$Type, arg2: $ResourceLocation$Type, arg3: long): void

(arg0: $Player$Type, arg1: $Container$Type, arg2: $ResourceLocation$Type, arg3: long): void
}

export namespace $LootFiller {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootFiller$Type = ($LootFiller);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootFiller_ = $LootFiller$Type;
}}
declare module "packages/noobanidus/mods/lootr/api/$MenuBuilder" {
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export interface $MenuBuilder {

 "build"(arg0: integer, arg1: $Inventory$Type, arg2: $Container$Type, arg3: integer): $AbstractContainerMenu

(arg0: integer, arg1: $Inventory$Type, arg2: $Container$Type, arg3: integer): $AbstractContainerMenu
}

export namespace $MenuBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuBuilder$Type = ($MenuBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuBuilder_ = $MenuBuilder$Type;
}}
declare module "packages/noobanidus/mods/lootr/block/entities/$LootrTrappedChestBlockEntity" {
import {$LithiumStackList, $LithiumStackList$Type} from "packages/me/jellysquid/mods/lithium/common/hopper/$LithiumStackList"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$LootrChestBlockEntity, $LootrChestBlockEntity$Type} from "packages/noobanidus/mods/lootr/block/entities/$LootrChestBlockEntity"
import {$InventoryChangeTracker, $InventoryChangeTracker$Type} from "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_change_tracking/$InventoryChangeTracker"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$InventoryChangeListener, $InventoryChangeListener$Type} from "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_change_tracking/$InventoryChangeListener"
import {$LockCode, $LockCode$Type} from "packages/net/minecraft/world/$LockCode"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $LootrTrappedChestBlockEntity extends $LootrChestBlockEntity {
 "openers": $Set<($UUID)>
 "items": $NonNullList<($ItemStack)>
static readonly "LOOT_TABLE_TAG": string
static readonly "LOOT_TABLE_SEED_TAG": string
 "lootTable": $ResourceLocation
 "lootTableSeed": long
 "lockKey": $LockCode
 "blockState": $BlockState

constructor(arg0: $BlockPos$Type, arg1: $BlockState$Type)

public "stopForwardingMajorInventoryChanges"(arg0: $InventoryChangeListener$Type): void
public "emitFirstComparatorAdded"(): void
public "forwardMajorInventoryChanges"(arg0: $InventoryChangeListener$Type): void
public "emitContentModified"(): void
public "emitStackListReplaced"(): void
public "forwardContentChangeOnce"(arg0: $InventoryChangeListener$Type, arg1: $LithiumStackList$Type, arg2: $InventoryChangeTracker$Type): void
public "emitRemoved"(): void
public "setChanged"(): void
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public static "tryClear"(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrTrappedChestBlockEntity$Type = ($LootrTrappedChestBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrTrappedChestBlockEntity_ = $LootrTrappedChestBlockEntity$Type;
}}
declare module "packages/noobanidus/mods/lootr/item/$LootrShulkerBlockItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$BlockItem, $BlockItem$Type} from "packages/net/minecraft/world/item/$BlockItem"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$IClientItemExtensions, $IClientItemExtensions$Type} from "packages/net/minecraftforge/client/extensions/common/$IClientItemExtensions"
import {$AdditionalItemPlacement, $AdditionalItemPlacement$Type} from "packages/net/mehvahdjukaar/moonlight/api/item/additional_placements/$AdditionalItemPlacement"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LootrShulkerBlockItem extends $BlockItem {
static readonly "BLOCK_ENTITY_TAG": string
static readonly "BLOCK_STATE_TAG": string
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Block$Type, arg1: $Item$Properties$Type)

public "initializeClient"(arg0: $Consumer$Type<($IClientItemExtensions$Type)>): void
public "moonlight$addAdditionalBehavior"(arg0: $AdditionalItemPlacement$Type): void
public "moonlight$getAdditionalBehavior"(): $AdditionalItemPlacement
public "moonlight$getClientAnimationExtension"(): any
public "moonlight$setClientAnimationExtension"(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrShulkerBlockItem$Type = ($LootrShulkerBlockItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrShulkerBlockItem_ = $LootrShulkerBlockItem$Type;
}}
declare module "packages/noobanidus/mods/lootr/advancement/$AdvancementPredicate" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$IGenericPredicate, $IGenericPredicate$Type} from "packages/noobanidus/mods/lootr/api/advancement/$IGenericPredicate"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $AdvancementPredicate implements $IGenericPredicate<($ResourceLocation)> {

constructor()
constructor(arg0: $ResourceLocation$Type)

public "test"(arg0: $ServerPlayer$Type, arg1: $ResourceLocation$Type): boolean
public "deserialize"(arg0: $JsonObject$Type): $IGenericPredicate<($ResourceLocation)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdvancementPredicate$Type = ($AdvancementPredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdvancementPredicate_ = $AdvancementPredicate$Type;
}}
declare module "packages/noobanidus/mods/lootr/block/entities/$LootrInventoryBlockEntity" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Connection, $Connection$Type} from "packages/net/minecraft/network/$Connection"
import {$LithiumStackList, $LithiumStackList$Type} from "packages/me/jellysquid/mods/lithium/common/hopper/$LithiumStackList"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$LootrChestBlockEntity, $LootrChestBlockEntity$Type} from "packages/noobanidus/mods/lootr/block/entities/$LootrChestBlockEntity"
import {$InventoryChangeTracker, $InventoryChangeTracker$Type} from "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_change_tracking/$InventoryChangeTracker"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ClientboundBlockEntityDataPacket, $ClientboundBlockEntityDataPacket$Type} from "packages/net/minecraft/network/protocol/game/$ClientboundBlockEntityDataPacket"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$InventoryChangeListener, $InventoryChangeListener$Type} from "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_change_tracking/$InventoryChangeListener"
import {$LockCode, $LockCode$Type} from "packages/net/minecraft/world/$LockCode"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $LootrInventoryBlockEntity extends $LootrChestBlockEntity {
 "openers": $Set<($UUID)>
 "items": $NonNullList<($ItemStack)>
static readonly "LOOT_TABLE_TAG": string
static readonly "LOOT_TABLE_SEED_TAG": string
 "lootTable": $ResourceLocation
 "lootTableSeed": long
 "lockKey": $LockCode
 "blockState": $BlockState

constructor(arg0: $BlockPos$Type, arg1: $BlockState$Type)

public "getCustomInventory"(): $NonNullList<($ItemStack)>
public "setCustomInventory"(arg0: $NonNullList$Type<($ItemStack$Type)>): void
public "load"(arg0: $CompoundTag$Type): void
public "onDataPacket"(arg0: $Connection$Type, arg1: $ClientboundBlockEntityDataPacket$Type): void
public "stopForwardingMajorInventoryChanges"(arg0: $InventoryChangeListener$Type): void
public "emitFirstComparatorAdded"(): void
public "forwardMajorInventoryChanges"(arg0: $InventoryChangeListener$Type): void
public "emitContentModified"(): void
public "emitStackListReplaced"(): void
public "forwardContentChangeOnce"(arg0: $InventoryChangeListener$Type, arg1: $LithiumStackList$Type, arg2: $InventoryChangeTracker$Type): void
public "emitRemoved"(): void
public "setChanged"(): void
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public static "tryClear"(arg0: any): void
get "customInventory"(): $NonNullList<($ItemStack)>
set "customInventory"(value: $NonNullList$Type<($ItemStack$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrInventoryBlockEntity$Type = ($LootrInventoryBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrInventoryBlockEntity_ = $LootrInventoryBlockEntity$Type;
}}
declare module "packages/noobanidus/mods/lootr/util/$Getter" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export class $Getter {

constructor()

public static "getPlayer"(): $Player
get "player"(): $Player
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Getter$Type = ($Getter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Getter_ = $Getter$Type;
}}
declare module "packages/noobanidus/mods/lootr/loot/conditions/$LootCount$Operation" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$LootCount$Operand, $LootCount$Operand$Type} from "packages/noobanidus/mods/lootr/loot/conditions/$LootCount$Operand"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"

export class $LootCount$Operation implements $Predicate<(integer)> {

constructor(arg0: $LootCount$Operand$Type, arg1: integer)

public "test"(arg0: integer): boolean
public static "deserialize"(arg0: $JsonObject$Type): $LootCount$Operation
public "getPrecedence"(): integer
public "serialize"(): $JsonObject
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<(integer)>
public "negate"(): $Predicate<(integer)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<(integer)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<(integer)>
public static "isEqual"<T>(arg0: any): $Predicate<(integer)>
get "precedence"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootCount$Operation$Type = ($LootCount$Operation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootCount$Operation_ = $LootCount$Operation$Type;
}}
declare module "packages/noobanidus/mods/lootr/api/advancement/$IGenericPredicate" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"

export interface $IGenericPredicate<T> {

 "test"(arg0: $ServerPlayer$Type, arg1: T): boolean
 "deserialize"(arg0: $JsonObject$Type): $IGenericPredicate<(T)>
}

export namespace $IGenericPredicate {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IGenericPredicate$Type<T> = ($IGenericPredicate<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IGenericPredicate_<T> = $IGenericPredicate$Type<(T)>;
}}
declare module "packages/noobanidus/mods/lootr/block/entities/$LootrBarrelBlockEntity" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Connection, $Connection$Type} from "packages/net/minecraft/network/$Connection"
import {$ModelData, $ModelData$Type} from "packages/net/minecraftforge/client/model/data/$ModelData"
import {$ILootBlockEntity, $ILootBlockEntity$Type} from "packages/noobanidus/mods/lootr/api/blockentity/$ILootBlockEntity"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ClientboundBlockEntityDataPacket, $ClientboundBlockEntityDataPacket$Type} from "packages/net/minecraft/network/protocol/game/$ClientboundBlockEntityDataPacket"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$LockCode, $LockCode$Type} from "packages/net/minecraft/world/$LockCode"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$RandomizableContainerBlockEntity, $RandomizableContainerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$RandomizableContainerBlockEntity"

export class $LootrBarrelBlockEntity extends $RandomizableContainerBlockEntity implements $ILootBlockEntity {
 "openers": $Set<($UUID)>
static readonly "LOOT_TABLE_TAG": string
static readonly "LOOT_TABLE_SEED_TAG": string
 "lootTable": $ResourceLocation
 "lootTableSeed": long
 "lockKey": $LockCode
 "blockState": $BlockState

constructor(arg0: $BlockPos$Type, arg1: $BlockState$Type)

public "getTable"(): $ResourceLocation
public "getSeed"(): long
public "getPosition"(): $BlockPos
public "unpackLootTable"(arg0: $Player$Type, arg1: $Container$Type, arg2: $ResourceLocation$Type, arg3: long): void
public "getTileId"(): $UUID
public "getContainerSize"(): integer
public "getUpdatePacket"(): $ClientboundBlockEntityDataPacket
public "getCapability"<T>(arg0: $Capability$Type<(T)>, arg1: $Direction$Type): $LazyOptional<(T)>
public "recheckOpen"(): void
public "load"(arg0: $CompoundTag$Type): void
public "startOpen"(arg0: $Player$Type): void
public "stopOpen"(arg0: $Player$Type): void
public "setLootTable"(arg0: $ResourceLocation$Type, arg1: long): void
public "updatePacketViaState"(): void
public "unpackLootTable"(arg0: $Player$Type): void
public "saveToItem"(arg0: $ItemStack$Type): void
public "getOpeners"(): $Set<($UUID)>
public "setOpened"(arg0: boolean): void
public "getUpdateTag"(): $CompoundTag
public "onDataPacket"(arg0: $Connection$Type, arg1: $ClientboundBlockEntityDataPacket$Type): void
public "getModelData"(): $ModelData
public "updatePacketViaForce"(arg0: $BlockEntity$Type): void
public "setChanged"(): void
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public static "tryClear"(arg0: any): void
get "table"(): $ResourceLocation
get "seed"(): long
get "position"(): $BlockPos
get "tileId"(): $UUID
get "containerSize"(): integer
get "updatePacket"(): $ClientboundBlockEntityDataPacket
get "openers"(): $Set<($UUID)>
set "opened"(value: boolean)
get "updateTag"(): $CompoundTag
get "modelData"(): $ModelData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrBarrelBlockEntity$Type = ($LootrBarrelBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrBarrelBlockEntity_ = $LootrBarrelBlockEntity$Type;
}}
declare module "packages/noobanidus/mods/lootr/block/entities/$LootrChestBlockEntity" {
import {$ChestBlockEntity, $ChestBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$ChestBlockEntity"
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Connection, $Connection$Type} from "packages/net/minecraft/network/$Connection"
import {$ILootBlockEntity, $ILootBlockEntity$Type} from "packages/noobanidus/mods/lootr/api/blockentity/$ILootBlockEntity"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ClientboundBlockEntityDataPacket, $ClientboundBlockEntityDataPacket$Type} from "packages/net/minecraft/network/protocol/game/$ClientboundBlockEntityDataPacket"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$InventoryChangeListener, $InventoryChangeListener$Type} from "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_change_tracking/$InventoryChangeListener"
import {$LockCode, $LockCode$Type} from "packages/net/minecraft/world/$LockCode"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$LithiumStackList, $LithiumStackList$Type} from "packages/me/jellysquid/mods/lithium/common/hopper/$LithiumStackList"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$InventoryChangeTracker, $InventoryChangeTracker$Type} from "packages/me/jellysquid/mods/lithium/common/block/entity/inventory_change_tracking/$InventoryChangeTracker"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"
import {$Set, $Set$Type} from "packages/java/util/$Set"

export class $LootrChestBlockEntity extends $ChestBlockEntity implements $ILootBlockEntity {
 "openers": $Set<($UUID)>
 "items": $NonNullList<($ItemStack)>
static readonly "LOOT_TABLE_TAG": string
static readonly "LOOT_TABLE_SEED_TAG": string
 "lootTable": $ResourceLocation
 "lootTableSeed": long
 "lockKey": $LockCode
 "blockState": $BlockState

constructor(arg0: $BlockPos$Type, arg1: $BlockState$Type)

public "getTable"(): $ResourceLocation
public "getSeed"(): long
public "getPosition"(): $BlockPos
public "unpackLootTable"(arg0: $Player$Type, arg1: $Container$Type, arg2: $ResourceLocation$Type, arg3: long): void
public "getTileId"(): $UUID
public "isOpened"(): boolean
public "getUpdatePacket"(): $ClientboundBlockEntityDataPacket
public "getCapability"<T>(arg0: $Capability$Type<(T)>, arg1: $Direction$Type): $LazyOptional<(T)>
public "load"(arg0: $CompoundTag$Type): void
public "recheckOpen"(): void
public static "lootrLidAnimateTick"<T extends $BlockEntity>(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: T): void
public "startOpen"(arg0: $Player$Type): void
public "stopOpen"(arg0: $Player$Type): void
public "setLootTable"(arg0: $ResourceLocation$Type, arg1: long): void
public static "getOpenCount"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type): integer
public "updatePacketViaState"(): void
public "unpackLootTable"(arg0: $Player$Type): void
public "saveToItem"(arg0: $ItemStack$Type): void
public "triggerEvent"(arg0: integer, arg1: integer): boolean
public "getOpenNess"(arg0: float): float
public "getOpeners"(): $Set<($UUID)>
public "setOpened"(arg0: boolean): void
public "getUpdateTag"(): $CompoundTag
public "onDataPacket"(arg0: $Connection$Type, arg1: $ClientboundBlockEntityDataPacket$Type): void
public "updatePacketViaForce"(arg0: $BlockEntity$Type): void
public "stopForwardingMajorInventoryChanges"(arg0: $InventoryChangeListener$Type): void
public "emitFirstComparatorAdded"(): void
public "forwardMajorInventoryChanges"(arg0: $InventoryChangeListener$Type): void
public "emitContentModified"(): void
public "emitStackListReplaced"(): void
public "forwardContentChangeOnce"(arg0: $InventoryChangeListener$Type, arg1: $LithiumStackList$Type, arg2: $InventoryChangeTracker$Type): void
public "emitRemoved"(): void
public "setChanged"(): void
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public static "tryClear"(arg0: any): void
get "table"(): $ResourceLocation
get "seed"(): long
get "position"(): $BlockPos
get "tileId"(): $UUID
get "opened"(): boolean
get "updatePacket"(): $ClientboundBlockEntityDataPacket
get "openers"(): $Set<($UUID)>
set "opened"(value: boolean)
get "updateTag"(): $CompoundTag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrChestBlockEntity$Type = ($LootrChestBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrChestBlockEntity_ = $LootrChestBlockEntity$Type;
}}
declare module "packages/noobanidus/mods/lootr/api/inventory/$ILootrInventory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockContainerJS, $BlockContainerJS$Type} from "packages/dev/latvian/mods/kubejs/level/$BlockContainerJS"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractMinecartContainer, $AbstractMinecartContainer$Type} from "packages/net/minecraft/world/entity/vehicle/$AbstractMinecartContainer"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"
import {$BaseContainerBlockEntity, $BaseContainerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BaseContainerBlockEntity"

export interface $ILootrInventory extends $Container, $MenuProvider {

 "getBlockEntity"(arg0: $Level$Type): $BaseContainerBlockEntity
 "getInventoryContents"(): $NonNullList<($ItemStack)>
/**
 * 
 * @deprecated
 */
 "getTile"(arg0: $Level$Type): $BaseContainerBlockEntity
 "getEntity"(arg0: $Level$Type): $AbstractMinecartContainer
 "getPos"(): $BlockPos
 "kjs$self"(): $Container
 "setChanged"(): void
 "getBlock"(level: $Level$Type): $BlockContainerJS
 "getItem"(arg0: integer): $ItemStack
 "getContainerSize"(): integer
 "removeItemNoUpdate"(arg0: integer): $ItemStack
 "removeItem"(arg0: integer, arg1: integer): $ItemStack
 "isEmpty"(): boolean
 "startOpen"(arg0: $Player$Type): void
 "getMaxStackSize"(): integer
 "stillValid"(arg0: $Player$Type): boolean
 "stopOpen"(arg0: $Player$Type): void
 "canPlaceItem"(arg0: integer, arg1: $ItemStack$Type): boolean
 "countItem"(arg0: $Item$Type): integer
 "canTakeItem"(arg0: $Container$Type, arg1: integer, arg2: $ItemStack$Type): boolean
 "hasAnyMatching"(arg0: $Predicate$Type<($ItemStack$Type)>): boolean
 "getSlots"(): integer
 "getStackInSlot"(slot: integer): $ItemStack
 "insertItem"(slot: integer, stack: $ItemStack$Type, simulate: boolean): $ItemStack
 "isMutable"(): boolean
 "hasAnyOf"(arg0: $Set$Type<($Item$Type)>): boolean
 "setChanged"(): void
 "asContainer"(): $Container
 "getHeight"(): integer
 "extractItem"(slot: integer, amount: integer, simulate: boolean): $ItemStack
 "isItemValid"(slot: integer, stack: $ItemStack$Type): boolean
 "getWidth"(): integer
 "setStackInSlot"(slot: integer, stack: $ItemStack$Type): void
 "getSlotLimit"(slot: integer): integer
 "clear"(): void
 "setItem"(arg0: integer, arg1: $ItemStack$Type): void
 "getDisplayName"(): $Component
 "clearContent"(): void
 "insertItem"(stack: $ItemStack$Type, simulate: boolean): $ItemStack
 "countNonEmpty"(ingredient: $Ingredient$Type): integer
 "countNonEmpty"(): integer
 "getAllItems"(): $List<($ItemStack)>
 "find"(ingredient: $Ingredient$Type): integer
 "find"(): integer
 "clear"(ingredient: $Ingredient$Type): void
 "count"(ingredient: $Ingredient$Type): integer
 "count"(): integer
 "isEmpty"(): boolean
 "createMenu"(arg0: integer, arg1: $Inventory$Type, arg2: $Player$Type): $AbstractContainerMenu
}

export namespace $ILootrInventory {
function stillValidBlockEntity(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
function stillValidBlockEntity(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
function tryClear(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ILootrInventory$Type = ($ILootrInventory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ILootrInventory_ = $ILootrInventory$Type;
}}
declare module "packages/noobanidus/mods/lootr/block/entities/$TileTicker" {
import {$TickEvent$ServerTickEvent, $TickEvent$ServerTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ServerTickEvent"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $TileTicker {

constructor()

public static "addEntry"(arg0: $Level$Type, arg1: $BlockPos$Type): void
public static "serverTick"(arg0: $TickEvent$ServerTickEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TileTicker$Type = ($TileTicker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TileTicker_ = $TileTicker$Type;
}}
declare module "packages/noobanidus/mods/lootr/gen/$LootrBlockTagProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$BlockTagsProvider, $BlockTagsProvider$Type} from "packages/net/minecraftforge/common/data/$BlockTagsProvider"

export class $LootrBlockTagProvider extends $BlockTagsProvider {

constructor(arg0: $PackOutput$Type, arg1: $CompletableFuture$Type<($HolderLookup$Provider$Type)>, arg2: $ExistingFileHelper$Type)

public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrBlockTagProvider$Type = ($LootrBlockTagProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrBlockTagProvider_ = $LootrBlockTagProvider$Type;
}}
declare module "packages/noobanidus/mods/lootr/init/$ModLoot" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $ModLoot {
static readonly "LOOT_COUNT": $RegistryObject<($LootItemConditionType)>

constructor()

public static "register"(arg0: $IEventBus$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModLoot$Type = ($ModLoot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModLoot_ = $ModLoot$Type;
}}
declare module "packages/noobanidus/mods/lootr/advancement/$ContainerPredicate" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$IGenericPredicate, $IGenericPredicate$Type} from "packages/noobanidus/mods/lootr/api/advancement/$IGenericPredicate"

export class $ContainerPredicate implements $IGenericPredicate<($UUID)> {

constructor()

public "test"(arg0: $ServerPlayer$Type, arg1: $UUID$Type): boolean
public "deserialize"(arg0: $JsonObject$Type): $IGenericPredicate<($UUID)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContainerPredicate$Type = ($ContainerPredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContainerPredicate_ = $ContainerPredicate$Type;
}}
declare module "packages/noobanidus/mods/lootr/block/$LootrShulkerBlock" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$ShulkerBoxBlock, $ShulkerBoxBlock$Type} from "packages/net/minecraft/world/level/block/$ShulkerBoxBlock"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$DyeColor, $DyeColor$Type} from "packages/net/minecraft/world/item/$DyeColor"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$EnumProperty, $EnumProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$EnumProperty"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"

export class $LootrShulkerBlock extends $ShulkerBoxBlock {
static readonly "FACING": $EnumProperty<($Direction)>
static readonly "CONTENTS": $ResourceLocation
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

constructor(arg0: $BlockBehaviour$Properties$Type)

public "getTicker"<T extends $BlockEntity>(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockEntityType$Type<(T)>): $BlockEntityTicker<(T)>
public "getExplosionResistance"(): float
public "playerWillDestroy"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $Player$Type): void
public "appendHoverText"(arg0: $ItemStack$Type, arg1: $BlockGetter$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void
public "use"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type, arg4: $InteractionHand$Type, arg5: $BlockHitResult$Type): $InteractionResult
public "hasAnalogOutputSignal"(arg0: $BlockState$Type): boolean
public "onRemove"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type, arg4: boolean): void
public "getAnalogOutputSignal"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): integer
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
public "getDestroyProgress"(arg0: $BlockState$Type, arg1: $Player$Type, arg2: $BlockGetter$Type, arg3: $BlockPos$Type): float
public "getColor"(): $DyeColor
public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
get "explosionResistance"(): float
get "color"(): $DyeColor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrShulkerBlock$Type = ($LootrShulkerBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrShulkerBlock_ = $LootrShulkerBlock$Type;
}}
declare module "packages/noobanidus/mods/lootr/event/$HandleCart" {
import {$EntityJoinLevelEvent, $EntityJoinLevelEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityJoinLevelEvent"

export class $HandleCart {

constructor()

public static "onEntityJoin"(arg0: $EntityJoinLevelEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HandleCart$Type = ($HandleCart);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HandleCart_ = $HandleCart$Type;
}}
declare module "packages/noobanidus/mods/lootr/init/$ModBlockEntities" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$LootrBarrelBlockEntity, $LootrBarrelBlockEntity$Type} from "packages/noobanidus/mods/lootr/block/entities/$LootrBarrelBlockEntity"
import {$LootrTrappedChestBlockEntity, $LootrTrappedChestBlockEntity$Type} from "packages/noobanidus/mods/lootr/block/entities/$LootrTrappedChestBlockEntity"
import {$LootrChestBlockEntity, $LootrChestBlockEntity$Type} from "packages/noobanidus/mods/lootr/block/entities/$LootrChestBlockEntity"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$LootrInventoryBlockEntity, $LootrInventoryBlockEntity$Type} from "packages/noobanidus/mods/lootr/block/entities/$LootrInventoryBlockEntity"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$LootrShulkerBlockEntity, $LootrShulkerBlockEntity$Type} from "packages/noobanidus/mods/lootr/block/entities/$LootrShulkerBlockEntity"

export class $ModBlockEntities {
static readonly "LOOTR_CHEST": $RegistryObject<($BlockEntityType<($LootrChestBlockEntity)>)>
static readonly "LOOTR_TRAPPED_CHEST": $RegistryObject<($BlockEntityType<($LootrTrappedChestBlockEntity)>)>
static readonly "LOOTR_BARREL": $RegistryObject<($BlockEntityType<($LootrBarrelBlockEntity)>)>
static readonly "LOOTR_INVENTORY": $RegistryObject<($BlockEntityType<($LootrInventoryBlockEntity)>)>
static readonly "LOOTR_SHULKER": $RegistryObject<($BlockEntityType<($LootrShulkerBlockEntity)>)>

constructor()

public static "register"(arg0: $IEventBus$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModBlockEntities$Type = ($ModBlockEntities);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModBlockEntities_ = $ModBlockEntities$Type;
}}
declare module "packages/noobanidus/mods/lootr/data/$DataStorage" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$DimensionDataStorage, $DimensionDataStorage$Type} from "packages/net/minecraft/world/level/storage/$DimensionDataStorage"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$IntSupplier, $IntSupplier$Type} from "packages/java/util/function/$IntSupplier"
import {$ChestData, $ChestData$Type} from "packages/noobanidus/mods/lootr/data/$ChestData"
import {$LootFiller, $LootFiller$Type} from "packages/noobanidus/mods/lootr/api/$LootFiller"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$SpecialChestInventory, $SpecialChestInventory$Type} from "packages/noobanidus/mods/lootr/data/$SpecialChestInventory"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LootrChestMinecartEntity, $LootrChestMinecartEntity$Type} from "packages/noobanidus/mods/lootr/entity/$LootrChestMinecartEntity"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$LongSupplier, $LongSupplier$Type} from "packages/java/util/function/$LongSupplier"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BaseContainerBlockEntity, $BaseContainerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BaseContainerBlockEntity"
import {$RandomizableContainerBlockEntity, $RandomizableContainerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$RandomizableContainerBlockEntity"

export class $DataStorage {
static readonly "ID_OLD": string
static readonly "SCORED_OLD": string
static readonly "DECAY_OLD": string
static readonly "REFRESH_OLD": string
static readonly "ID": string
static readonly "SCORED": string
static readonly "DECAY": string
static readonly "REFRESH": string

constructor()

/**
 * 
 * @deprecated
 */
public static "getInstance"(arg0: $ServerLevel$Type, arg1: $BlockPos$Type, arg2: $UUID$Type): $ChestData
public static "getInventory"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $BaseContainerBlockEntity$Type, arg5: $LootFiller$Type, arg6: $Supplier$Type<($ResourceLocation$Type)>, arg7: $LongSupplier$Type): $SpecialChestInventory
public static "getInventory"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $IntSupplier$Type, arg5: $Supplier$Type<($Component$Type)>, arg6: $LootFiller$Type, arg7: $Supplier$Type<($ResourceLocation$Type)>, arg8: $LongSupplier$Type): $SpecialChestInventory
public static "getInventory"(arg0: $Level$Type, arg1: $LootrChestMinecartEntity$Type, arg2: $ServerPlayer$Type, arg3: $LootFiller$Type): $SpecialChestInventory
public static "getInventory"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $NonNullList$Type<($ItemStack$Type)>, arg3: $ServerPlayer$Type, arg4: $BlockPos$Type, arg5: $RandomizableContainerBlockEntity$Type): $SpecialChestInventory
public static "getInventory"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $RandomizableContainerBlockEntity$Type, arg5: $LootFiller$Type): $SpecialChestInventory
public static "getContainerData"(arg0: $ServerLevel$Type, arg1: $BlockPos$Type, arg2: $UUID$Type): $ChestData
public static "getReferenceContainerData"(arg0: $ServerLevel$Type, arg1: $BlockPos$Type, arg2: $UUID$Type, arg3: $NonNullList$Type<($ItemStack$Type)>): $ChestData
/**
 * 
 * @deprecated
 */
public static "getInstanceInventory"(arg0: $ServerLevel$Type, arg1: $BlockPos$Type, arg2: $UUID$Type, arg3: $NonNullList$Type<($ItemStack$Type)>): $ChestData
public static "score"(arg0: $UUID$Type, arg1: $UUID$Type): void
public static "clearInventories"(arg0: $UUID$Type): boolean
public static "isAwarded"(arg0: $UUID$Type, arg1: $UUID$Type): boolean
public static "award"(arg0: $UUID$Type, arg1: $UUID$Type): void
public static "refreshInventory"(arg0: $Level$Type, arg1: $LootrChestMinecartEntity$Type, arg2: $ServerPlayer$Type): void
public static "refreshInventory"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $UUID$Type, arg3: $ServerPlayer$Type): void
public static "refreshInventory"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $UUID$Type, arg3: $NonNullList$Type<($ItemStack$Type)>, arg4: $ServerPlayer$Type): void
public static "doRefresh"(): void
public static "doDecay"(): void
public static "isScored"(arg0: $UUID$Type, arg1: $UUID$Type): boolean
public static "removeDecayed"(arg0: $UUID$Type): void
public static "removeRefreshed"(arg0: $UUID$Type): void
/**
 * 
 * @deprecated
 */
public static "getInstanceUuid"(arg0: $ServerLevel$Type, arg1: $BlockPos$Type, arg2: $UUID$Type): $ChestData
public static "isRefreshed"(arg0: $UUID$Type): boolean
public static "getDecayValue"(arg0: $UUID$Type): integer
public static "isDecayed"(arg0: $UUID$Type): boolean
public static "getRefreshValue"(arg0: $UUID$Type): integer
public static "getDataStorage"(): $DimensionDataStorage
public static "getEntityData"(arg0: $ServerLevel$Type, arg1: $BlockPos$Type, arg2: $UUID$Type): $ChestData
public static "setDecaying"(arg0: $UUID$Type, arg1: integer): void
public static "setRefreshing"(arg0: $UUID$Type, arg1: integer): void
get "dataStorage"(): $DimensionDataStorage
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataStorage$Type = ($DataStorage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataStorage_ = $DataStorage$Type;
}}
declare module "packages/noobanidus/mods/lootr/data/$AdvancementData" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$SavedData, $SavedData$Type} from "packages/net/minecraft/world/level/saveddata/$SavedData"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$File, $File$Type} from "packages/java/io/$File"
import {$AdvancementData$UUIDPair, $AdvancementData$UUIDPair$Type} from "packages/noobanidus/mods/lootr/data/$AdvancementData$UUIDPair"

export class $AdvancementData extends $SavedData {

constructor()

public "add"(arg0: $AdvancementData$UUIDPair$Type): void
public "add"(arg0: $UUID$Type, arg1: $UUID$Type): void
public static "load"(arg0: $CompoundTag$Type): $AdvancementData
public "contains"(arg0: $AdvancementData$UUIDPair$Type): boolean
public "contains"(arg0: $UUID$Type, arg1: $UUID$Type): boolean
public "save"(arg0: $CompoundTag$Type): $CompoundTag
public "save"(arg0: $File$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdvancementData$Type = ($AdvancementData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdvancementData_ = $AdvancementData$Type;
}}
declare module "packages/noobanidus/mods/lootr/data/$AdvancementData$UUIDPair" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$INBTSerializable, $INBTSerializable$Type} from "packages/net/minecraftforge/common/util/$INBTSerializable"

export class $AdvancementData$UUIDPair implements $INBTSerializable<($CompoundTag)> {

constructor(arg0: $UUID$Type, arg1: $UUID$Type)

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "getFirst"(): $UUID
public "getSecond"(): $UUID
public static "fromNBT"(arg0: $CompoundTag$Type): $AdvancementData$UUIDPair
public "deserializeNBT"(arg0: $CompoundTag$Type): void
get "first"(): $UUID
get "second"(): $UUID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdvancementData$UUIDPair$Type = ($AdvancementData$UUIDPair);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdvancementData$UUIDPair_ = $AdvancementData$UUIDPair$Type;
}}
declare module "packages/noobanidus/mods/lootr/client/item/$LootrChestItemRenderer" {
import {$BlockEntityWithoutLevelRenderer, $BlockEntityWithoutLevelRenderer$Type} from "packages/net/minecraft/client/renderer/$BlockEntityWithoutLevelRenderer"
import {$ItemDisplayContext, $ItemDisplayContext$Type} from "packages/net/minecraft/world/item/$ItemDisplayContext"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$EntityModelSet, $EntityModelSet$Type} from "packages/net/minecraft/client/model/geom/$EntityModelSet"
import {$BlockEntityRenderDispatcher, $BlockEntityRenderDispatcher$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRenderDispatcher"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LootrChestMinecartEntity, $LootrChestMinecartEntity$Type} from "packages/noobanidus/mods/lootr/entity/$LootrChestMinecartEntity"

export class $LootrChestItemRenderer extends $BlockEntityWithoutLevelRenderer {

constructor(arg0: $BlockEntityRenderDispatcher$Type, arg1: $EntityModelSet$Type)
constructor()

public static "getInstance"(): $LootrChestItemRenderer
public "renderByMinecart"(arg0: $LootrChestMinecartEntity$Type, arg1: $PoseStack$Type, arg2: $MultiBufferSource$Type, arg3: integer): void
public "renderByItem"(arg0: $ItemStack$Type, arg1: $ItemDisplayContext$Type, arg2: $PoseStack$Type, arg3: $MultiBufferSource$Type, arg4: integer, arg5: integer): void
get "instance"(): $LootrChestItemRenderer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrChestItemRenderer$Type = ($LootrChestItemRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrChestItemRenderer_ = $LootrChestItemRenderer$Type;
}}
declare module "packages/noobanidus/mods/lootr/client/entity/$LootrChestCartRenderer" {
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$ModelLayerLocation, $ModelLayerLocation$Type} from "packages/net/minecraft/client/model/geom/$ModelLayerLocation"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$LootrChestMinecartEntity, $LootrChestMinecartEntity$Type} from "packages/noobanidus/mods/lootr/entity/$LootrChestMinecartEntity"
import {$MinecartRenderer, $MinecartRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$MinecartRenderer"

export class $LootrChestCartRenderer<T extends $LootrChestMinecartEntity> extends $MinecartRenderer<(T)> {
 "shadowRadius": float

constructor(arg0: $EntityRendererProvider$Context$Type, arg1: $ModelLayerLocation$Type)

public "render"(arg0: T, arg1: float, arg2: float, arg3: $PoseStack$Type, arg4: $MultiBufferSource$Type, arg5: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrChestCartRenderer$Type<T> = ($LootrChestCartRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrChestCartRenderer_<T> = $LootrChestCartRenderer$Type<(T)>;
}}
declare module "packages/noobanidus/mods/lootr/$Lootr" {
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$RegisterCommandsEvent, $RegisterCommandsEvent$Type} from "packages/net/minecraftforge/event/$RegisterCommandsEvent"
import {$CommandLootr, $CommandLootr$Type} from "packages/noobanidus/mods/lootr/command/$CommandLootr"

export class $Lootr {
 "COMMAND_LOOTR": $CommandLootr
 "TAB": $CreativeModeTab

constructor()

public "onCommands"(arg0: $RegisterCommandsEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Lootr$Type = ($Lootr);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Lootr_ = $Lootr$Type;
}}
declare module "packages/noobanidus/mods/lootr/loot/conditions/$LootCount$Operand" {
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $LootCount$Operand extends $Enum<($LootCount$Operand)> implements $BiPredicate<(integer), (integer)> {
static readonly "EQUALS": $LootCount$Operand
static readonly "NOT_EQUALS": $LootCount$Operand
static readonly "LESS_THAN": $LootCount$Operand
static readonly "GREATER_THAN": $LootCount$Operand
static readonly "LESS_THAN_EQUALS": $LootCount$Operand
static readonly "GREATER_THAN_EQUALS": $LootCount$Operand


public static "values"(): ($LootCount$Operand)[]
public "test"(arg0: integer, arg1: integer): boolean
public static "valueOf"(arg0: string): $LootCount$Operand
public static "fromString"(arg0: string): $LootCount$Operand
public "getPrecedence"(): integer
public "or"(arg0: $BiPredicate$Type<(any), (any)>): $BiPredicate<(integer), (integer)>
public "negate"(): $BiPredicate<(integer), (integer)>
public "and"(arg0: $BiPredicate$Type<(any), (any)>): $BiPredicate<(integer), (integer)>
get "precedence"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootCount$Operand$Type = (("not_equals") | ("less_than_equals") | ("greater_than") | ("greater_than_equals") | ("equals") | ("less_than")) | ($LootCount$Operand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootCount$Operand_ = $LootCount$Operand$Type;
}}
declare module "packages/noobanidus/mods/lootr/api/$ILootrAPI" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$IntSupplier, $IntSupplier$Type} from "packages/java/util/function/$IntSupplier"
import {$LootFiller, $LootFiller$Type} from "packages/noobanidus/mods/lootr/api/$LootFiller"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$MenuBuilder, $MenuBuilder$Type} from "packages/noobanidus/mods/lootr/api/$MenuBuilder"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ILootrInventory, $ILootrInventory$Type} from "packages/noobanidus/mods/lootr/api/inventory/$ILootrInventory"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$LongSupplier, $LongSupplier$Type} from "packages/java/util/function/$LongSupplier"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BaseContainerBlockEntity, $BaseContainerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BaseContainerBlockEntity"

export interface $ILootrAPI {

 "getInventory"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $BaseContainerBlockEntity$Type, arg5: $LootFiller$Type, arg6: $Supplier$Type<($ResourceLocation$Type)>, arg7: $LongSupplier$Type, arg8: $MenuBuilder$Type): $ILootrInventory
 "getInventory"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $IntSupplier$Type, arg5: $Supplier$Type<($Component$Type)>, arg6: $LootFiller$Type, arg7: $Supplier$Type<($ResourceLocation$Type)>, arg8: $LongSupplier$Type): $ILootrInventory
 "getInventory"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $IntSupplier$Type, arg5: $Supplier$Type<($Component$Type)>, arg6: $LootFiller$Type, arg7: $Supplier$Type<($ResourceLocation$Type)>, arg8: $LongSupplier$Type, arg9: $MenuBuilder$Type): $ILootrInventory
 "getInventory"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $BaseContainerBlockEntity$Type, arg5: $LootFiller$Type, arg6: $Supplier$Type<($ResourceLocation$Type)>, arg7: $LongSupplier$Type): $ILootrInventory
 "getAnalogOutputSignal"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer): integer
 "isFakePlayer"(arg0: $Player$Type): boolean
 "clearPlayerLoot"(arg0: $ServerPlayer$Type): boolean
 "clearPlayerLoot"(arg0: $UUID$Type): boolean
/**
 * 
 * @deprecated
 */
 "getModdedMenu"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $IntSupplier$Type, arg5: $Supplier$Type<($Component$Type)>, arg6: $LootFiller$Type, arg7: $Supplier$Type<($ResourceLocation$Type)>, arg8: $LongSupplier$Type): $MenuProvider
/**
 * 
 * @deprecated
 */
 "getModdedMenu"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $IntSupplier$Type, arg5: $Supplier$Type<($Component$Type)>, arg6: $LootFiller$Type, arg7: $Supplier$Type<($ResourceLocation$Type)>, arg8: $LongSupplier$Type, arg9: $MenuBuilder$Type): $MenuProvider
/**
 * 
 * @deprecated
 */
 "getModdedMenu"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $BaseContainerBlockEntity$Type, arg5: $LootFiller$Type, arg6: $Supplier$Type<($ResourceLocation$Type)>, arg7: $LongSupplier$Type, arg8: $MenuBuilder$Type): $MenuProvider
/**
 * 
 * @deprecated
 */
 "getModdedMenu"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $BaseContainerBlockEntity$Type, arg5: $LootFiller$Type, arg6: $Supplier$Type<($ResourceLocation$Type)>, arg7: $LongSupplier$Type): $MenuProvider
 "isSavingStructure"(): boolean
 "getLootSeed"(arg0: long): long
 "hasCapacity"(arg0: string): boolean
 "shouldDiscard"(): boolean
 "getDestroyProgress"(arg0: $BlockState$Type, arg1: $Player$Type, arg2: $BlockGetter$Type, arg3: $BlockPos$Type, arg4: float): float
 "getExplosionResistance"(arg0: $Block$Type, arg1: float): float
}

export namespace $ILootrAPI {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ILootrAPI$Type = ($ILootrAPI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ILootrAPI_ = $ILootrAPI$Type;
}}
declare module "packages/noobanidus/mods/lootr/api/entity/$ILootCart" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IHasOpeners, $IHasOpeners$Type} from "packages/noobanidus/mods/lootr/api/$IHasOpeners"

export interface $ILootCart extends $IHasOpeners {

 "getOpeners"(): $Set<($UUID)>

(): $Set<($UUID)>
}

export namespace $ILootCart {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ILootCart$Type = ($ILootCart);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ILootCart_ = $ILootCart$Type;
}}
declare module "packages/noobanidus/mods/lootr/api/blockentity/$ILootBlockEntity" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IHasOpeners, $IHasOpeners$Type} from "packages/noobanidus/mods/lootr/api/$IHasOpeners"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $ILootBlockEntity extends $IHasOpeners {

 "getTable"(): $ResourceLocation
 "getSeed"(): long
 "getPosition"(): $BlockPos
 "unpackLootTable"(arg0: $Player$Type, arg1: $Container$Type, arg2: $ResourceLocation$Type, arg3: long): void
 "getTileId"(): $UUID
 "updatePacketViaState"(): void
 "updatePacketViaForce"(arg0: $BlockEntity$Type): void
 "setOpened"(arg0: boolean): void
 "getOpeners"(): $Set<($UUID)>
}

export namespace $ILootBlockEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ILootBlockEntity$Type = ($ILootBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ILootBlockEntity_ = $ILootBlockEntity$Type;
}}
declare module "packages/noobanidus/mods/lootr/network/$UpdateModelData" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $UpdateModelData {
 "pos": $BlockPos

constructor(arg0: $FriendlyByteBuf$Type)
constructor(arg0: $BlockPos$Type)

public "encode"(arg0: $FriendlyByteBuf$Type): void
public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UpdateModelData$Type = ($UpdateModelData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UpdateModelData_ = $UpdateModelData$Type;
}}
declare module "packages/noobanidus/mods/lootr/gen/$LootrDataGenerators" {
import {$GatherDataEvent, $GatherDataEvent$Type} from "packages/net/minecraftforge/data/event/$GatherDataEvent"

export class $LootrDataGenerators {

constructor()

public static "gatherData"(arg0: $GatherDataEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrDataGenerators$Type = ($LootrDataGenerators);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrDataGenerators_ = $LootrDataGenerators$Type;
}}
declare module "packages/noobanidus/mods/lootr/advancement/$LootedStatPredicate" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$IGenericPredicate, $IGenericPredicate$Type} from "packages/noobanidus/mods/lootr/api/advancement/$IGenericPredicate"

export class $LootedStatPredicate implements $IGenericPredicate<(void)> {

constructor()
constructor(arg0: integer)

public "test"(arg0: $ServerPlayer$Type, arg1: void): boolean
public "deserialize"(arg0: $JsonObject$Type): $IGenericPredicate<(void)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootedStatPredicate$Type = ($LootedStatPredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootedStatPredicate_ = $LootedStatPredicate$Type;
}}
declare module "packages/noobanidus/mods/lootr/block/$LootrTrappedChestBlock" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ChestType, $ChestType$Type} from "packages/net/minecraft/world/level/block/state/properties/$ChestType"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$ChestBlock, $ChestBlock$Type} from "packages/net/minecraft/world/level/block/$ChestBlock"
import {$EnumProperty, $EnumProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$EnumProperty"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"

export class $LootrTrappedChestBlock extends $ChestBlock {
static readonly "FACING": $DirectionProperty
static readonly "TYPE": $EnumProperty<($ChestType)>
static readonly "WATERLOGGED": $BooleanProperty
static readonly "EVENT_SET_OPEN_COUNT": integer
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

constructor(arg0: $BlockBehaviour$Properties$Type)

public "getTicker"<T extends $BlockEntity>(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockEntityType$Type<(T)>): $BlockEntityTicker<(T)>
public "getExplosionResistance"(): float
public "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
public "use"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type, arg4: $InteractionHand$Type, arg5: $BlockHitResult$Type): $InteractionResult
public "isSignalSource"(arg0: $BlockState$Type): boolean
public "getMenuProvider"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): $MenuProvider
public "getAnalogOutputSignal"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): integer
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
public "getDestroyProgress"(arg0: $BlockState$Type, arg1: $Player$Type, arg2: $BlockGetter$Type, arg3: $BlockPos$Type): float
public "getSignal"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Direction$Type): integer
public "getDirectSignal"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Direction$Type): integer
public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
get "explosionResistance"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrTrappedChestBlock$Type = ($LootrTrappedChestBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrTrappedChestBlock_ = $LootrTrappedChestBlock$Type;
}}
declare module "packages/noobanidus/mods/lootr/init/$ModEntities" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$LootrChestMinecartEntity, $LootrChestMinecartEntity$Type} from "packages/noobanidus/mods/lootr/entity/$LootrChestMinecartEntity"

export class $ModEntities {
static readonly "LOOTR_MINECART_ENTITY": $RegistryObject<($EntityType<($LootrChestMinecartEntity)>)>

constructor()

public static "register"(arg0: $IEventBus$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModEntities$Type = ($ModEntities);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModEntities_ = $ModEntities$Type;
}}
declare module "packages/noobanidus/mods/lootr/api/$LootrCapacities" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LootrCapacities {

}

export namespace $LootrCapacities {
const VERSION: integer
const CAPACITIES: string
const STRUCTURE_SAVING: string
const SHOULD_DISCARD: string
const EXPLOSION_RESISTANCE: string
const DESTRUCTION_PROGRESS: string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrCapacities$Type = ($LootrCapacities);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrCapacities_ = $LootrCapacities$Type;
}}
declare module "packages/noobanidus/mods/lootr/block/$LootrInventoryBlock" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ChestType, $ChestType$Type} from "packages/net/minecraft/world/level/block/state/properties/$ChestType"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$ChestBlock, $ChestBlock$Type} from "packages/net/minecraft/world/level/block/$ChestBlock"
import {$EnumProperty, $EnumProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$EnumProperty"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"

export class $LootrInventoryBlock extends $ChestBlock {
static readonly "FACING": $DirectionProperty
static readonly "TYPE": $EnumProperty<($ChestType)>
static readonly "WATERLOGGED": $BooleanProperty
static readonly "EVENT_SET_OPEN_COUNT": integer
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

constructor(arg0: $BlockBehaviour$Properties$Type)

public "getTicker"<T extends $BlockEntity>(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockEntityType$Type<(T)>): $BlockEntityTicker<(T)>
public "getExplosionResistance"(): float
public "getStateForPlacement"(arg0: $BlockPlaceContext$Type): $BlockState
public "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
public "use"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type, arg4: $InteractionHand$Type, arg5: $BlockHitResult$Type): $InteractionResult
public "isSignalSource"(arg0: $BlockState$Type): boolean
public "hasAnalogOutputSignal"(arg0: $BlockState$Type): boolean
public "getFluidState"(arg0: $BlockState$Type): $FluidState
public "getMenuProvider"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): $MenuProvider
public "getAnalogOutputSignal"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): integer
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
public "getDestroyProgress"(arg0: $BlockState$Type, arg1: $Player$Type, arg2: $BlockGetter$Type, arg3: $BlockPos$Type): float
public "tick"(arg0: $BlockState$Type, arg1: $ServerLevel$Type, arg2: $BlockPos$Type, arg3: $RandomSource$Type): void
public "getSignal"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Direction$Type): integer
public "getDirectSignal"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Direction$Type): integer
public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
get "explosionResistance"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrInventoryBlock$Type = ($LootrInventoryBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrInventoryBlock_ = $LootrInventoryBlock$Type;
}}
declare module "packages/noobanidus/mods/lootr/data/$SpecialChestInventory" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ChestData, $ChestData$Type} from "packages/noobanidus/mods/lootr/data/$ChestData"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockContainerJS, $BlockContainerJS$Type} from "packages/dev/latvian/mods/kubejs/level/$BlockContainerJS"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$MenuBuilder, $MenuBuilder$Type} from "packages/noobanidus/mods/lootr/api/$MenuBuilder"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ILootrInventory, $ILootrInventory$Type} from "packages/noobanidus/mods/lootr/api/inventory/$ILootrInventory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BaseContainerBlockEntity, $BaseContainerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BaseContainerBlockEntity"

export class $SpecialChestInventory implements $ILootrInventory {

constructor(arg0: $ChestData$Type, arg1: $NonNullList$Type<($ItemStack$Type)>, arg2: $Component$Type)
constructor(arg0: $ChestData$Type, arg1: $CompoundTag$Type, arg2: string)

public "getDisplayName"(): $Component
public "setChanged"(): void
public "getBlockEntity"(arg0: $Level$Type): $BaseContainerBlockEntity
public "setMenuBuilder"(arg0: $MenuBuilder$Type): void
public "getTileId"(): $UUID
public "getInventoryContents"(): $NonNullList<($ItemStack)>
public "getItem"(arg0: integer): $ItemStack
public "getContainerSize"(): integer
public "removeItemNoUpdate"(arg0: integer): $ItemStack
public "removeItem"(arg0: integer, arg1: integer): $ItemStack
public "clearContent"(): void
public "isEmpty"(): boolean
public "createMenu"(arg0: integer, arg1: $Inventory$Type, arg2: $Player$Type): $AbstractContainerMenu
public "startOpen"(arg0: $Player$Type): void
public "stillValid"(arg0: $Player$Type): boolean
public "stopOpen"(arg0: $Player$Type): void
public "getPos"(): $BlockPos
public "setItem"(arg0: integer, arg1: $ItemStack$Type): void
public "writeName"(): string
public "resizeInventory"(arg0: integer): void
public "writeItems"(): $CompoundTag
/**
 * 
 * @deprecated
 */
public "getTile"(arg0: $Level$Type): $BaseContainerBlockEntity
public "kjs$self"(): $Container
public "getBlock"(level: $Level$Type): $BlockContainerJS
public "getMaxStackSize"(): integer
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public "canPlaceItem"(arg0: integer, arg1: $ItemStack$Type): boolean
public "countItem"(arg0: $Item$Type): integer
public "canTakeItem"(arg0: $Container$Type, arg1: integer, arg2: $ItemStack$Type): boolean
public "hasAnyMatching"(arg0: $Predicate$Type<($ItemStack$Type)>): boolean
public "getSlots"(): integer
public "getStackInSlot"(slot: integer): $ItemStack
public "insertItem"(slot: integer, stack: $ItemStack$Type, simulate: boolean): $ItemStack
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public "isMutable"(): boolean
public "hasAnyOf"(arg0: $Set$Type<($Item$Type)>): boolean
public "setChanged"(): void
public "asContainer"(): $Container
public "getHeight"(): integer
public "extractItem"(slot: integer, amount: integer, simulate: boolean): $ItemStack
public "isItemValid"(slot: integer, stack: $ItemStack$Type): boolean
public "getWidth"(): integer
public "setStackInSlot"(slot: integer, stack: $ItemStack$Type): void
public "getSlotLimit"(slot: integer): integer
public "clear"(): void
public static "tryClear"(arg0: any): void
public "insertItem"(stack: $ItemStack$Type, simulate: boolean): $ItemStack
public "countNonEmpty"(ingredient: $Ingredient$Type): integer
public "countNonEmpty"(): integer
public "getAllItems"(): $List<($ItemStack)>
public "find"(ingredient: $Ingredient$Type): integer
public "find"(): integer
public "clear"(ingredient: $Ingredient$Type): void
public "count"(ingredient: $Ingredient$Type): integer
public "count"(): integer
public "isEmpty"(): boolean
get "displayName"(): $Component
set "menuBuilder"(value: $MenuBuilder$Type)
get "tileId"(): $UUID
get "inventoryContents"(): $NonNullList<($ItemStack)>
get "containerSize"(): integer
get "empty"(): boolean
get "pos"(): $BlockPos
get "maxStackSize"(): integer
get "slots"(): integer
get "mutable"(): boolean
get "height"(): integer
get "width"(): integer
get "allItems"(): $List<($ItemStack)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpecialChestInventory$Type = ($SpecialChestInventory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpecialChestInventory_ = $SpecialChestInventory$Type;
}}
declare module "packages/noobanidus/mods/lootr/event/$HandleChunk" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ServerAboutToStartEvent, $ServerAboutToStartEvent$Type} from "packages/net/minecraftforge/event/server/$ServerAboutToStartEvent"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ChunkEvent$Load, $ChunkEvent$Load$Type} from "packages/net/minecraftforge/event/level/$ChunkEvent$Load"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$ServerStoppedEvent, $ServerStoppedEvent$Type} from "packages/net/minecraftforge/event/server/$ServerStoppedEvent"

export class $HandleChunk {
static readonly "LOADED_CHUNKS": $Map<($ResourceKey<($Level)>), ($Set<($ChunkPos)>)>

constructor()

public static "onServerStarted"(arg0: $ServerAboutToStartEvent$Type): void
public static "onServerStopped"(arg0: $ServerStoppedEvent$Type): void
public static "onChunkLoad"(arg0: $ChunkEvent$Load$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HandleChunk$Type = ($HandleChunk);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HandleChunk_ = $HandleChunk$Type;
}}
declare module "packages/noobanidus/mods/lootr/impl/$LootrAPIImpl" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$IntSupplier, $IntSupplier$Type} from "packages/java/util/function/$IntSupplier"
import {$LootFiller, $LootFiller$Type} from "packages/noobanidus/mods/lootr/api/$LootFiller"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$ILootrAPI, $ILootrAPI$Type} from "packages/noobanidus/mods/lootr/api/$ILootrAPI"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$MenuBuilder, $MenuBuilder$Type} from "packages/noobanidus/mods/lootr/api/$MenuBuilder"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ILootrInventory, $ILootrInventory$Type} from "packages/noobanidus/mods/lootr/api/inventory/$ILootrInventory"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$LongSupplier, $LongSupplier$Type} from "packages/java/util/function/$LongSupplier"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BaseContainerBlockEntity, $BaseContainerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BaseContainerBlockEntity"

export class $LootrAPIImpl implements $ILootrAPI {

constructor()

public "getInventory"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $BaseContainerBlockEntity$Type, arg5: $LootFiller$Type, arg6: $Supplier$Type<($ResourceLocation$Type)>, arg7: $LongSupplier$Type, arg8: $MenuBuilder$Type): $ILootrInventory
public "getInventory"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $IntSupplier$Type, arg5: $Supplier$Type<($Component$Type)>, arg6: $LootFiller$Type, arg7: $Supplier$Type<($ResourceLocation$Type)>, arg8: $LongSupplier$Type): $ILootrInventory
public "getInventory"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $IntSupplier$Type, arg5: $Supplier$Type<($Component$Type)>, arg6: $LootFiller$Type, arg7: $Supplier$Type<($ResourceLocation$Type)>, arg8: $LongSupplier$Type, arg9: $MenuBuilder$Type): $ILootrInventory
public "getInventory"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $BaseContainerBlockEntity$Type, arg5: $LootFiller$Type, arg6: $Supplier$Type<($ResourceLocation$Type)>, arg7: $LongSupplier$Type): $ILootrInventory
public "getAnalogOutputSignal"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer): integer
public "isFakePlayer"(arg0: $Player$Type): boolean
public "clearPlayerLoot"(arg0: $UUID$Type): boolean
public "isSavingStructure"(): boolean
public "getLootSeed"(arg0: long): long
public "shouldDiscard"(): boolean
public "getDestroyProgress"(arg0: $BlockState$Type, arg1: $Player$Type, arg2: $BlockGetter$Type, arg3: $BlockPos$Type, arg4: float): float
public "getExplosionResistance"(arg0: $Block$Type, arg1: float): float
public "clearPlayerLoot"(arg0: $ServerPlayer$Type): boolean
/**
 * 
 * @deprecated
 */
public "getModdedMenu"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $IntSupplier$Type, arg5: $Supplier$Type<($Component$Type)>, arg6: $LootFiller$Type, arg7: $Supplier$Type<($ResourceLocation$Type)>, arg8: $LongSupplier$Type): $MenuProvider
/**
 * 
 * @deprecated
 */
public "getModdedMenu"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $IntSupplier$Type, arg5: $Supplier$Type<($Component$Type)>, arg6: $LootFiller$Type, arg7: $Supplier$Type<($ResourceLocation$Type)>, arg8: $LongSupplier$Type, arg9: $MenuBuilder$Type): $MenuProvider
/**
 * 
 * @deprecated
 */
public "getModdedMenu"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $BaseContainerBlockEntity$Type, arg5: $LootFiller$Type, arg6: $Supplier$Type<($ResourceLocation$Type)>, arg7: $LongSupplier$Type, arg8: $MenuBuilder$Type): $MenuProvider
/**
 * 
 * @deprecated
 */
public "getModdedMenu"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $BaseContainerBlockEntity$Type, arg5: $LootFiller$Type, arg6: $Supplier$Type<($ResourceLocation$Type)>, arg7: $LongSupplier$Type): $MenuProvider
public "hasCapacity"(arg0: string): boolean
get "savingStructure"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrAPIImpl$Type = ($LootrAPIImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrAPIImpl_ = $LootrAPIImpl$Type;
}}
declare module "packages/noobanidus/mods/lootr/network/$PacketHandler" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$PacketDistributor$PacketTarget, $PacketDistributor$PacketTarget$Type} from "packages/net/minecraftforge/network/$PacketDistributor$PacketTarget"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $PacketHandler {
static readonly "HANDLER": $SimpleChannel

constructor()

public static "registerMessages"(): void
public static "sendToServerInternal"(arg0: any): void
public static "sendInternal"<MSG>(arg0: $PacketDistributor$PacketTarget$Type, arg1: MSG): void
public static "registerMessage"<MSG>(arg0: $Class$Type<(MSG)>, arg1: $BiConsumer$Type<(MSG), ($FriendlyByteBuf$Type)>, arg2: $Function$Type<($FriendlyByteBuf$Type), (MSG)>, arg3: $BiConsumer$Type<(MSG), ($Supplier$Type<($NetworkEvent$Context$Type)>)>): void
public static "sendToInternal"(arg0: any, arg1: $ServerPlayer$Type): void
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
declare module "packages/noobanidus/mods/lootr/client/block/$LootrShulkerBlockRenderer" {
import {$Material, $Material$Type} from "packages/net/minecraft/client/resources/model/$Material"
import {$BlockEntityRendererProvider$Context, $BlockEntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRendererProvider$Context"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$LootrShulkerBlockEntity, $LootrShulkerBlockEntity$Type} from "packages/noobanidus/mods/lootr/block/entities/$LootrShulkerBlockEntity"
import {$BlockEntityRenderer, $BlockEntityRenderer$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRenderer"

export class $LootrShulkerBlockRenderer implements $BlockEntityRenderer<($LootrShulkerBlockEntity)> {
static readonly "MATERIAL": $Material
static readonly "MATERIAL2": $Material
static readonly "MATERIAL3": $Material
static readonly "MATERIAL4": $Material

constructor(arg0: $BlockEntityRendererProvider$Context$Type)

public "render"(arg0: $LootrShulkerBlockEntity$Type, arg1: float, arg2: $PoseStack$Type, arg3: $MultiBufferSource$Type, arg4: integer, arg5: integer): void
public "shouldRender"(arg0: $LootrShulkerBlockEntity$Type, arg1: $Vec3$Type): boolean
public "shouldRenderOffScreen"(arg0: $LootrShulkerBlockEntity$Type): boolean
public "getViewDistance"(): integer
get "viewDistance"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrShulkerBlockRenderer$Type = ($LootrShulkerBlockRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrShulkerBlockRenderer_ = $LootrShulkerBlockRenderer$Type;
}}
declare module "packages/noobanidus/mods/lootr/entity/$EntityTicker" {
import {$TickEvent$ServerTickEvent, $TickEvent$ServerTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ServerTickEvent"
import {$LootrChestMinecartEntity, $LootrChestMinecartEntity$Type} from "packages/noobanidus/mods/lootr/entity/$LootrChestMinecartEntity"

export class $EntityTicker {

constructor()

public static "addEntity"(arg0: $LootrChestMinecartEntity$Type): void
public static "onServerTick"(arg0: $TickEvent$ServerTickEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityTicker$Type = ($EntityTicker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityTicker_ = $EntityTicker$Type;
}}
declare module "packages/noobanidus/mods/lootr/loot/conditions/$LootCount" {
import {$LootContextParam, $LootContextParam$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParam"
import {$LootItemConditionType, $LootItemConditionType$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemConditionType"
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LootCount$Operation, $LootCount$Operation$Type} from "packages/noobanidus/mods/lootr/loot/conditions/$LootCount$Operation"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LootItemCondition, $LootItemCondition$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemCondition"

export class $LootCount implements $LootItemCondition {

constructor(arg0: $List$Type<($LootCount$Operation$Type)>)

public "test"(arg0: $LootContext$Type): boolean
public "getType"(): $LootItemConditionType
public "getReferencedContextParams"(): $Set<($LootContextParam<(any)>)>
public "validate"(arg0: $ValidationContext$Type): void
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public "negate"(): $Predicate<($LootContext)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($LootContext)>
public static "isEqual"<T>(arg0: any): $Predicate<($LootContext)>
public "applyLootHandler"(context: $LootContext$Type, loot: $List$Type<($ItemStack$Type)>): boolean
get "type"(): $LootItemConditionType
get "referencedContextParams"(): $Set<($LootContextParam<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootCount$Type = ($LootCount);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootCount_ = $LootCount$Type;
}}
declare module "packages/noobanidus/mods/lootr/client/item/$LootrShulkerItemRenderer" {
import {$BlockEntityWithoutLevelRenderer, $BlockEntityWithoutLevelRenderer$Type} from "packages/net/minecraft/client/renderer/$BlockEntityWithoutLevelRenderer"
import {$ItemDisplayContext, $ItemDisplayContext$Type} from "packages/net/minecraft/world/item/$ItemDisplayContext"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$EntityModelSet, $EntityModelSet$Type} from "packages/net/minecraft/client/model/geom/$EntityModelSet"
import {$BlockEntityRenderDispatcher, $BlockEntityRenderDispatcher$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRenderDispatcher"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"

export class $LootrShulkerItemRenderer extends $BlockEntityWithoutLevelRenderer {

constructor(arg0: $BlockEntityRenderDispatcher$Type, arg1: $EntityModelSet$Type)
constructor()

public static "getInstance"(): $LootrShulkerItemRenderer
public "renderByItem"(arg0: $ItemStack$Type, arg1: $ItemDisplayContext$Type, arg2: $PoseStack$Type, arg3: $MultiBufferSource$Type, arg4: integer, arg5: integer): void
get "instance"(): $LootrShulkerItemRenderer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrShulkerItemRenderer$Type = ($LootrShulkerItemRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrShulkerItemRenderer_ = $LootrShulkerItemRenderer$Type;
}}
declare module "packages/noobanidus/mods/lootr/setup/$CommonSetup" {
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"

export class $CommonSetup {

constructor()

public static "init"(arg0: $FMLCommonSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonSetup$Type = ($CommonSetup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonSetup_ = $CommonSetup$Type;
}}
declare module "packages/noobanidus/mods/lootr/gen/$LootrLootTableProvider" {
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$LootTableProvider, $LootTableProvider$Type} from "packages/net/minecraft/data/loot/$LootTableProvider"

export class $LootrLootTableProvider {

constructor()

public static "create"(arg0: $PackOutput$Type): $LootTableProvider
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrLootTableProvider$Type = ($LootrLootTableProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrLootTableProvider_ = $LootrLootTableProvider$Type;
}}
declare module "packages/noobanidus/mods/lootr/setup/$ClientSetup" {
import {$EntityRenderersEvent$RegisterRenderers, $EntityRenderersEvent$RegisterRenderers$Type} from "packages/net/minecraftforge/client/event/$EntityRenderersEvent$RegisterRenderers"
import {$ModelEvent$RegisterGeometryLoaders, $ModelEvent$RegisterGeometryLoaders$Type} from "packages/net/minecraftforge/client/event/$ModelEvent$RegisterGeometryLoaders"

export class $ClientSetup {

constructor()

public static "registerRenderers"(arg0: $EntityRenderersEvent$RegisterRenderers$Type): void
public static "modelRegister"(arg0: $ModelEvent$RegisterGeometryLoaders$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientSetup$Type = ($ClientSetup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientSetup_ = $ClientSetup$Type;
}}
declare module "packages/noobanidus/mods/lootr/init/$ModTabs" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $ModTabs {
static readonly "LOOTR": $RegistryObject<($CreativeModeTab)>

constructor()

public static "register"(arg0: $IEventBus$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModTabs$Type = ($ModTabs);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModTabs_ = $ModTabs$Type;
}}
declare module "packages/noobanidus/mods/lootr/event/$HandleBreak" {
import {$BlockEvent$BreakEvent, $BlockEvent$BreakEvent$Type} from "packages/net/minecraftforge/event/level/$BlockEvent$BreakEvent"
import {$Style, $Style$Type} from "packages/net/minecraft/network/chat/$Style"

export class $HandleBreak {

constructor()

public static "onBlockBreak"(arg0: $BlockEvent$BreakEvent$Type): void
public static "getChatStyle"(): $Style
get "chatStyle"(): $Style
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HandleBreak$Type = ($HandleBreak);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HandleBreak_ = $HandleBreak$Type;
}}
declare module "packages/noobanidus/mods/lootr/api/$IHasOpeners" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Set, $Set$Type} from "packages/java/util/$Set"

export interface $IHasOpeners {

 "getOpeners"(): $Set<($UUID)>

(): $Set<($UUID)>
}

export namespace $IHasOpeners {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IHasOpeners$Type = ($IHasOpeners);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IHasOpeners_ = $IHasOpeners$Type;
}}
declare module "packages/noobanidus/mods/lootr/network/$OpenCart" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $OpenCart {
 "entityId": integer

constructor(arg0: $FriendlyByteBuf$Type)
constructor(arg0: integer)

public "encode"(arg0: $FriendlyByteBuf$Type): void
public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OpenCart$Type = ($OpenCart);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OpenCart_ = $OpenCart$Type;
}}
declare module "packages/noobanidus/mods/lootr/client/impl/$ClientGetter" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export class $ClientGetter {

constructor()

public static "getPlayer"(): $Player
get "player"(): $Player
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientGetter$Type = ($ClientGetter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientGetter_ = $ClientGetter$Type;
}}
declare module "packages/noobanidus/mods/lootr/item/$LootrChestBlockItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$BlockItem, $BlockItem$Type} from "packages/net/minecraft/world/item/$BlockItem"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$IClientItemExtensions, $IClientItemExtensions$Type} from "packages/net/minecraftforge/client/extensions/common/$IClientItemExtensions"
import {$AdditionalItemPlacement, $AdditionalItemPlacement$Type} from "packages/net/mehvahdjukaar/moonlight/api/item/additional_placements/$AdditionalItemPlacement"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $LootrChestBlockItem extends $BlockItem {
static readonly "BLOCK_ENTITY_TAG": string
static readonly "BLOCK_STATE_TAG": string
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Block$Type, arg1: $Item$Properties$Type)

public "initializeClient"(arg0: $Consumer$Type<($IClientItemExtensions$Type)>): void
public "moonlight$addAdditionalBehavior"(arg0: $AdditionalItemPlacement$Type): void
public "moonlight$getAdditionalBehavior"(): $AdditionalItemPlacement
public "moonlight$getClientAnimationExtension"(): any
public "moonlight$setClientAnimationExtension"(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrChestBlockItem$Type = ($LootrChestBlockItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrChestBlockItem_ = $LootrChestBlockItem$Type;
}}
declare module "packages/noobanidus/mods/lootr/block/$LootrBarrelBlock" {
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BarrelBlock, $BarrelBlock$Type} from "packages/net/minecraft/world/level/block/$BarrelBlock"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ModelProperty, $ModelProperty$Type} from "packages/net/minecraftforge/client/model/data/$ModelProperty"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"

export class $LootrBarrelBlock extends $BarrelBlock {
static readonly "OPENED": $ModelProperty<(boolean)>
static readonly "FACING": $DirectionProperty
static readonly "OPEN": $BooleanProperty
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

constructor(arg0: $BlockBehaviour$Properties$Type)

public "getExplosionResistance"(): float
public "triggerEvent"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer, arg4: integer): boolean
public "use"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type, arg4: $InteractionHand$Type, arg5: $BlockHitResult$Type): $InteractionResult
public "hasAnalogOutputSignal"(arg0: $BlockState$Type): boolean
public "onRemove"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type, arg4: boolean): void
public "getAnalogOutputSignal"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): integer
public "getDestroyProgress"(arg0: $BlockState$Type, arg1: $Player$Type, arg2: $BlockGetter$Type, arg3: $BlockPos$Type): float
public "tick"(arg0: $BlockState$Type, arg1: $ServerLevel$Type, arg2: $BlockPos$Type, arg3: $RandomSource$Type): void
public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
get "explosionResistance"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrBarrelBlock$Type = ($LootrBarrelBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrBarrelBlock_ = $LootrBarrelBlock$Type;
}}
declare module "packages/noobanidus/mods/lootr/init/$ModItems" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$BlockItem, $BlockItem$Type} from "packages/net/minecraft/world/item/$BlockItem"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $ModItems {
static readonly "CHEST": $RegistryObject<($BlockItem)>
static readonly "TRAPPED_CHEST": $RegistryObject<($BlockItem)>
static readonly "BARREL": $RegistryObject<($BlockItem)>
static readonly "INVENTORY": $RegistryObject<($BlockItem)>
static readonly "SHULKER": $RegistryObject<($BlockItem)>
static readonly "TROPHY": $RegistryObject<($BlockItem)>

constructor()

public static "register"(arg0: $IEventBus$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModItems$Type = ($ModItems);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModItems_ = $ModItems$Type;
}}
declare module "packages/noobanidus/mods/lootr/data/$TickingData" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$SavedData, $SavedData$Type} from "packages/net/minecraft/world/level/saveddata/$SavedData"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$File, $File$Type} from "packages/java/io/$File"

export class $TickingData extends $SavedData {

constructor()

public "remove"(arg0: $UUID$Type): integer
public static "load"(arg0: $CompoundTag$Type): $TickingData
public "getValue"(arg0: $UUID$Type): integer
public "setValue"(arg0: $UUID$Type, arg1: integer): boolean
public "tick"(): boolean
public "isComplete"(arg0: $UUID$Type): boolean
public "save"(arg0: $CompoundTag$Type): $CompoundTag
public "save"(arg0: $File$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickingData$Type = ($TickingData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickingData_ = $TickingData$Type;
}}
declare module "packages/noobanidus/mods/lootr/client/block/$BarrelModel" {
import {$Material, $Material$Type} from "packages/net/minecraft/client/resources/model/$Material"
import {$ModelBaker, $ModelBaker$Type} from "packages/net/minecraft/client/resources/model/$ModelBaker"
import {$ModelState, $ModelState$Type} from "packages/net/minecraft/client/resources/model/$ModelState"
import {$IGeometryBakingContext, $IGeometryBakingContext$Type} from "packages/net/minecraftforge/client/model/geometry/$IGeometryBakingContext"
import {$IUnbakedGeometry, $IUnbakedGeometry$Type} from "packages/net/minecraftforge/client/model/geometry/$IUnbakedGeometry"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ItemOverrides, $ItemOverrides$Type} from "packages/net/minecraft/client/renderer/block/model/$ItemOverrides"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$UnbakedModel, $UnbakedModel$Type} from "packages/net/minecraft/client/resources/model/$UnbakedModel"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $BarrelModel implements $IUnbakedGeometry<($BarrelModel)> {

constructor(arg0: $UnbakedModel$Type, arg1: $UnbakedModel$Type, arg2: $UnbakedModel$Type, arg3: $UnbakedModel$Type, arg4: $UnbakedModel$Type)

public "resolveParents"(arg0: $Function$Type<($ResourceLocation$Type), ($UnbakedModel$Type)>, arg1: $IGeometryBakingContext$Type): void
public "bake"(arg0: $IGeometryBakingContext$Type, arg1: $ModelBaker$Type, arg2: $Function$Type<($Material$Type), ($TextureAtlasSprite$Type)>, arg3: $ModelState$Type, arg4: $ItemOverrides$Type, arg5: $ResourceLocation$Type): $BakedModel
public "getConfigurableComponentNames"(): $Set<(string)>
get "configurableComponentNames"(): $Set<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BarrelModel$Type = ($BarrelModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BarrelModel_ = $BarrelModel$Type;
}}
declare module "packages/noobanidus/mods/lootr/block/$TrophyBlock" {
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$Mirror, $Mirror$Type} from "packages/net/minecraft/world/level/block/$Mirror"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"

export class $TrophyBlock extends $Block {
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

constructor(arg0: $BlockBehaviour$Properties$Type)

public "getStateForPlacement"(arg0: $BlockPlaceContext$Type): $BlockState
public "rotate"(arg0: $BlockState$Type, arg1: $Rotation$Type): $BlockState
public "mirror"(arg0: $BlockState$Type, arg1: $Mirror$Type): $BlockState
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TrophyBlock$Type = ($TrophyBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TrophyBlock_ = $TrophyBlock$Type;
}}
declare module "packages/noobanidus/mods/lootr/util/$ChestUtil" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Style, $Style$Type} from "packages/net/minecraft/network/chat/$Style"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$LootrChestMinecartEntity, $LootrChestMinecartEntity$Type} from "packages/noobanidus/mods/lootr/entity/$LootrChestMinecartEntity"

export class $ChestUtil {

constructor()

public static "handleLootInventory"(arg0: $Block$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type): void
public static "handleLootSneak"(arg0: $Block$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type): void
public static "handleLootChest"(arg0: $Block$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type): void
public static "handleLootCartSneak"(arg0: $Level$Type, arg1: $LootrChestMinecartEntity$Type, arg2: $Player$Type): void
public static "getInvalidTable"(arg0: $ResourceLocation$Type): $Component
public static "handleLootCart"(arg0: $Level$Type, arg1: $LootrChestMinecartEntity$Type, arg2: $Player$Type): void
public static "copyItemList"(arg0: $NonNullList$Type<($ItemStack$Type)>): $NonNullList<($ItemStack)>
public static "getInvalidStyle"(): $Style
public static "getRefreshStyle"(): $Style
public static "getDecayStyle"(): $Style
get "invalidStyle"(): $Style
get "refreshStyle"(): $Style
get "decayStyle"(): $Style
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChestUtil$Type = ($ChestUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChestUtil_ = $ChestUtil$Type;
}}
declare module "packages/noobanidus/mods/lootr/block/entities/$LootrShulkerBlockEntity" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Connection, $Connection$Type} from "packages/net/minecraft/network/$Connection"
import {$ILootBlockEntity, $ILootBlockEntity$Type} from "packages/noobanidus/mods/lootr/api/blockentity/$ILootBlockEntity"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ClientboundBlockEntityDataPacket, $ClientboundBlockEntityDataPacket$Type} from "packages/net/minecraft/network/protocol/game/$ClientboundBlockEntityDataPacket"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ShulkerBoxBlockEntity$AnimationStatus, $ShulkerBoxBlockEntity$AnimationStatus$Type} from "packages/net/minecraft/world/level/block/entity/$ShulkerBoxBlockEntity$AnimationStatus"
import {$LockCode, $LockCode$Type} from "packages/net/minecraft/world/$LockCode"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$RandomizableContainerBlockEntity, $RandomizableContainerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$RandomizableContainerBlockEntity"

export class $LootrShulkerBlockEntity extends $RandomizableContainerBlockEntity implements $ILootBlockEntity {
 "openers": $Set<($UUID)>
static readonly "LOOT_TABLE_TAG": string
static readonly "LOOT_TABLE_SEED_TAG": string
 "lootTable": $ResourceLocation
 "lootTableSeed": long
 "lockKey": $LockCode
 "blockState": $BlockState

constructor(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockPos$Type, arg2: $BlockState$Type)
constructor(arg0: $BlockPos$Type, arg1: $BlockState$Type)

public "getTable"(): $ResourceLocation
public static "tick"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $LootrShulkerBlockEntity$Type): void
public "getSeed"(): long
public "getPosition"(): $BlockPos
public "isClosed"(): boolean
public "getProgress"(arg0: float): float
public "getBoundingBox"(arg0: $BlockState$Type): $AABB
public "getAnimationStatus"(): $ShulkerBoxBlockEntity$AnimationStatus
public "unpackLootTable"(arg0: $Player$Type, arg1: $Container$Type, arg2: $ResourceLocation$Type, arg3: long): void
public "getTileId"(): $UUID
public "getContainerSize"(): integer
public "getUpdatePacket"(): $ClientboundBlockEntityDataPacket
public "getCapability"<T>(arg0: $Capability$Type<(T)>, arg1: $Direction$Type): $LazyOptional<(T)>
public "load"(arg0: $CompoundTag$Type): void
public "startOpen"(arg0: $Player$Type): void
public "stopOpen"(arg0: $Player$Type): void
public "setLootTable"(arg0: $ResourceLocation$Type, arg1: long): void
public "updatePacketViaState"(): void
public "unpackLootTable"(arg0: $Player$Type): void
public "saveToItem"(arg0: $ItemStack$Type): void
public "triggerEvent"(arg0: integer, arg1: integer): boolean
public "getOpeners"(): $Set<($UUID)>
public "setOpened"(arg0: boolean): void
public "getUpdateTag"(): $CompoundTag
public "onDataPacket"(arg0: $Connection$Type, arg1: $ClientboundBlockEntityDataPacket$Type): void
public "updatePacketViaForce"(arg0: $BlockEntity$Type): void
public "setChanged"(): void
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public static "tryClear"(arg0: any): void
get "table"(): $ResourceLocation
get "seed"(): long
get "position"(): $BlockPos
get "closed"(): boolean
get "animationStatus"(): $ShulkerBoxBlockEntity$AnimationStatus
get "tileId"(): $UUID
get "containerSize"(): integer
get "updatePacket"(): $ClientboundBlockEntityDataPacket
get "openers"(): $Set<($UUID)>
set "opened"(value: boolean)
get "updateTag"(): $CompoundTag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrShulkerBlockEntity$Type = ($LootrShulkerBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrShulkerBlockEntity_ = $LootrShulkerBlockEntity$Type;
}}
declare module "packages/noobanidus/mods/lootr/network/client/$ClientHandlers" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$OpenCart, $OpenCart$Type} from "packages/noobanidus/mods/lootr/network/$OpenCart"
import {$CloseCart, $CloseCart$Type} from "packages/noobanidus/mods/lootr/network/$CloseCart"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$UpdateModelData, $UpdateModelData$Type} from "packages/noobanidus/mods/lootr/network/$UpdateModelData"

export class $ClientHandlers {

constructor()

public static "handleCloseCart"(arg0: $CloseCart$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public static "handleUpdateModel"(arg0: $UpdateModelData$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public static "handleOpenCart"(arg0: $OpenCart$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientHandlers$Type = ($ClientHandlers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientHandlers_ = $ClientHandlers$Type;
}}
declare module "packages/noobanidus/mods/lootr/data/$ChestData" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$SavedData, $SavedData$Type} from "packages/net/minecraft/world/level/saveddata/$SavedData"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$IntSupplier, $IntSupplier$Type} from "packages/java/util/function/$IntSupplier"
import {$LootFiller, $LootFiller$Type} from "packages/noobanidus/mods/lootr/api/$LootFiller"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$SpecialChestInventory, $SpecialChestInventory$Type} from "packages/noobanidus/mods/lootr/data/$SpecialChestInventory"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$LongSupplier, $LongSupplier$Type} from "packages/java/util/function/$LongSupplier"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BaseContainerBlockEntity, $BaseContainerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BaseContainerBlockEntity"
import {$RandomizableContainerBlockEntity, $RandomizableContainerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$RandomizableContainerBlockEntity"

export class $ChestData extends $SavedData {


public static "load"(arg0: $CompoundTag$Type): $ChestData
public "clear"(): void
public "getKey"(): string
public static "id"(arg0: $ResourceKey$Type<($Level$Type)>, arg1: $BlockPos$Type, arg2: $UUID$Type): $Supplier<($ChestData)>
public "getSize"(): integer
public static "unwrap"(arg0: $ChestData$Type, arg1: $UUID$Type, arg2: $ResourceKey$Type<($Level$Type)>, arg3: $BlockPos$Type, arg4: integer): $ChestData
public static "ID"(arg0: $UUID$Type): string
public "getInventory"(arg0: $ServerPlayer$Type): $SpecialChestInventory
public "getDimension"(): $ResourceKey<($Level)>
public "getTileId"(): $UUID
public "customInventory"(): $LootFiller
public static "entity"(arg0: $ResourceKey$Type<($Level$Type)>, arg1: $BlockPos$Type, arg2: $UUID$Type): $Supplier<($ChestData)>
public "clearInventory"(arg0: $UUID$Type): boolean
public static "ref_id"(arg0: $ResourceKey$Type<($Level$Type)>, arg1: $BlockPos$Type, arg2: $UUID$Type, arg3: $NonNullList$Type<($ItemStack$Type)>): $Supplier<($ChestData)>
public static "loadWrapper"(arg0: $UUID$Type, arg1: $ResourceKey$Type<($Level$Type)>, arg2: $BlockPos$Type): $Function<($CompoundTag), ($ChestData)>
public "getPos"(): $BlockPos
public "getEntityId"(): $UUID
public "save"(arg0: $CompoundTag$Type): $CompoundTag
public "save"(arg0: $File$Type): void
public "createInventory"(arg0: $ServerPlayer$Type, arg1: $LootFiller$Type, arg2: $BaseContainerBlockEntity$Type, arg3: $Supplier$Type<($ResourceLocation$Type)>, arg4: $LongSupplier$Type): $SpecialChestInventory
public "createInventory"(arg0: $ServerPlayer$Type, arg1: $LootFiller$Type, arg2: $RandomizableContainerBlockEntity$Type): $SpecialChestInventory
public "createInventory"(arg0: $ServerPlayer$Type, arg1: $LootFiller$Type, arg2: $IntSupplier$Type, arg3: $Supplier$Type<($Component$Type)>, arg4: $Supplier$Type<($ResourceLocation$Type)>, arg5: $LongSupplier$Type): $SpecialChestInventory
public "isEntity"(): boolean
get "key"(): string
get "size"(): integer
get "dimension"(): $ResourceKey<($Level)>
get "tileId"(): $UUID
get "pos"(): $BlockPos
get "entityId"(): $UUID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChestData$Type = ($ChestData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChestData_ = $ChestData$Type;
}}
declare module "packages/noobanidus/mods/lootr/init/$ModBlocks" {
import {$LootrTrappedChestBlock, $LootrTrappedChestBlock$Type} from "packages/noobanidus/mods/lootr/block/$LootrTrappedChestBlock"
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$LootrShulkerBlock, $LootrShulkerBlock$Type} from "packages/noobanidus/mods/lootr/block/$LootrShulkerBlock"
import {$LootrBarrelBlock, $LootrBarrelBlock$Type} from "packages/noobanidus/mods/lootr/block/$LootrBarrelBlock"
import {$LootrChestBlock, $LootrChestBlock$Type} from "packages/noobanidus/mods/lootr/block/$LootrChestBlock"
import {$LootrInventoryBlock, $LootrInventoryBlock$Type} from "packages/noobanidus/mods/lootr/block/$LootrInventoryBlock"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $ModBlocks {
static readonly "BARREL": $RegistryObject<($LootrBarrelBlock)>
static readonly "CHEST": $RegistryObject<($LootrChestBlock)>
static readonly "TRAPPED_CHEST": $RegistryObject<($LootrTrappedChestBlock)>
static readonly "INVENTORY": $RegistryObject<($LootrInventoryBlock)>
static readonly "TROPHY": $RegistryObject<($Block)>
static readonly "SHULKER": $RegistryObject<($LootrShulkerBlock)>

constructor()

public static "register"(arg0: $IEventBus$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModBlocks$Type = ($ModBlocks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModBlocks_ = $ModBlocks$Type;
}}
declare module "packages/noobanidus/mods/lootr/advancement/$GenericTrigger$Instance" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$AbstractCriterionTriggerInstance, $AbstractCriterionTriggerInstance$Type} from "packages/net/minecraft/advancements/critereon/$AbstractCriterionTriggerInstance"

export class $GenericTrigger$Instance<T> extends $AbstractCriterionTriggerInstance {


public "test"(arg0: $ServerPlayer$Type, arg1: T): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenericTrigger$Instance$Type<T> = ($GenericTrigger$Instance<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenericTrigger$Instance_<T> = $GenericTrigger$Instance$Type<(T)>;
}}
declare module "packages/noobanidus/mods/lootr/config/$ConfigManager" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ForgeConfigSpec$IntValue, $ForgeConfigSpec$IntValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$IntValue"
import {$ILootBlockEntity, $ILootBlockEntity$Type} from "packages/noobanidus/mods/lootr/api/blockentity/$ILootBlockEntity"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LootrChestMinecartEntity, $LootrChestMinecartEntity$Type} from "packages/noobanidus/mods/lootr/entity/$LootrChestMinecartEntity"
import {$ForgeConfigSpec$BooleanValue, $ForgeConfigSpec$BooleanValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$BooleanValue"
import {$ModConfigEvent, $ModConfigEvent$Type} from "packages/net/minecraftforge/fml/event/config/$ModConfigEvent"
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $ConfigManager {
static readonly "REPORT_UNRESOLVED_TABLES": $ForgeConfigSpec$BooleanValue
static readonly "RANDOMISE_SEED": $ForgeConfigSpec$BooleanValue
static readonly "DISABLE": $ForgeConfigSpec$BooleanValue
static readonly "OLD_MAX_AGE": integer
static readonly "MAXIMUM_AGE": $ForgeConfigSpec$IntValue
static readonly "CONVERT_MINESHAFTS": $ForgeConfigSpec$BooleanValue
static readonly "CONVERT_ELYTRAS": $ForgeConfigSpec$BooleanValue
static readonly "CONVERT_WOODEN_CHESTS": $ForgeConfigSpec$BooleanValue
static readonly "CONVERT_TRAPPED_CHESTS": $ForgeConfigSpec$BooleanValue
static readonly "ADDITIONAL_CHESTS": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "ADDITIONAL_TRAPPED_CHESTS": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "DISABLE_BREAK": $ForgeConfigSpec$BooleanValue
static readonly "ENABLE_BREAK": $ForgeConfigSpec$BooleanValue
static readonly "ENABLE_FAKE_PLAYER_BREAK": $ForgeConfigSpec$BooleanValue
static readonly "CHECK_WORLD_BORDER": $ForgeConfigSpec$BooleanValue
static readonly "MODID_DIMENSION_WHITELIST": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "DIMENSION_WHITELIST": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "MODID_DIMENSION_BLACKLIST": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "DIMENSION_BLACKLIST": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "LOOT_TABLE_BLACKLIST": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "LOOT_MODID_BLACKLIST": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "DECAY_VALUE": $ForgeConfigSpec$IntValue
static readonly "DECAY_ALL": $ForgeConfigSpec$BooleanValue
static readonly "DECAY_MODIDS": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "DECAY_LOOT_TABLES": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "DECAY_DIMENSIONS": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "DECAY_STRUCTURES": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "REFRESH_VALUE": $ForgeConfigSpec$IntValue
static readonly "REFRESH_ALL": $ForgeConfigSpec$BooleanValue
static readonly "REFRESH_MODIDS": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "REFRESH_LOOT_TABLES": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "REFRESH_DIMENSIONS": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "REFRESH_STRUCTURES": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "POWER_COMPARATORS": $ForgeConfigSpec$BooleanValue
static readonly "BLAST_RESISTANT": $ForgeConfigSpec$BooleanValue
static readonly "BLAST_IMMUNE": $ForgeConfigSpec$BooleanValue
static readonly "NOTIFICATION_DELAY": $ForgeConfigSpec$IntValue
static readonly "DISABLE_NOTIFICATIONS": $ForgeConfigSpec$BooleanValue
static readonly "DISABLE_MESSAGE_STYLES": $ForgeConfigSpec$BooleanValue
static readonly "TRAPPED_CUSTOM": $ForgeConfigSpec$BooleanValue
static readonly "VANILLA_TEXTURES": $ForgeConfigSpec$BooleanValue
static readonly "OLD_TEXTURES": $ForgeConfigSpec$BooleanValue
static "COMMON_CONFIG": $ForgeConfigSpec
static "CLIENT_CONFIG": $ForgeConfigSpec

constructor()

public static "replacement"(arg0: $BlockState$Type): $BlockState
public static "isDimensionDecaying"(arg0: $ResourceKey$Type<($Level$Type)>): boolean
public static "getRefreshStructures"(): $Set<($ResourceLocation)>
public static "getDimensionBlacklist"(): $Set<($ResourceKey<($Level)>)>
public static "getDimensionModidBlacklist"(): $Set<(string)>
public static "getAdditionalChests"(): $Set<($ResourceLocation)>
public static "getAdditionalTrappedChests"(): $Set<($ResourceLocation)>
public static "isDimensionRefreshing"(arg0: $ResourceKey$Type<($Level$Type)>): boolean
public static "getDimensionWhitelist"(): $Set<($ResourceKey<($Level)>)>
public static "getDimensionModidWhitelist"(): $Set<(string)>
public static "getRefreshDimensions"(): $Set<($ResourceKey<($Level)>)>
public static "getRefreshingTables"(): $Set<($ResourceLocation)>
public static "reloadConfig"(arg0: $ModConfigEvent$Type): void
public static "isVanillaTextures"(): boolean
public static "isDimensionBlocked"(arg0: $ResourceKey$Type<($Level$Type)>): boolean
public static "isRefreshing"(arg0: $ServerLevel$Type, arg1: $ILootBlockEntity$Type): boolean
public static "isRefreshing"(arg0: $ServerLevel$Type, arg1: $LootrChestMinecartEntity$Type): boolean
public static "getDecayStructures"(): $Set<($ResourceLocation)>
public static "getRefreshMods"(): $Set<(string)>
public static "isOldTextures"(): boolean
public static "getDecayingTables"(): $Set<($ResourceLocation)>
public static "getLootBlacklist"(): $Set<($ResourceLocation)>
public static "getDecayDimensions"(): $Set<($ResourceKey<($Level)>)>
public static "isDecaying"(arg0: $ServerLevel$Type, arg1: $ILootBlockEntity$Type): boolean
public static "isDecaying"(arg0: $ServerLevel$Type, arg1: $LootrChestMinecartEntity$Type): boolean
public static "getLootModids"(): $Set<(string)>
public static "getDecayMods"(): $Set<(string)>
public static "isBlacklisted"(arg0: $ResourceLocation$Type): boolean
public static "shouldNotify"(arg0: integer): boolean
public static "loadConfig"(arg0: $ForgeConfigSpec$Type, arg1: $Path$Type): void
get "refreshStructures"(): $Set<($ResourceLocation)>
get "dimensionBlacklist"(): $Set<($ResourceKey<($Level)>)>
get "dimensionModidBlacklist"(): $Set<(string)>
get "additionalChests"(): $Set<($ResourceLocation)>
get "additionalTrappedChests"(): $Set<($ResourceLocation)>
get "dimensionWhitelist"(): $Set<($ResourceKey<($Level)>)>
get "dimensionModidWhitelist"(): $Set<(string)>
get "refreshDimensions"(): $Set<($ResourceKey<($Level)>)>
get "refreshingTables"(): $Set<($ResourceLocation)>
get "vanillaTextures"(): boolean
get "decayStructures"(): $Set<($ResourceLocation)>
get "refreshMods"(): $Set<(string)>
get "oldTextures"(): boolean
get "decayingTables"(): $Set<($ResourceLocation)>
get "lootBlacklist"(): $Set<($ResourceLocation)>
get "decayDimensions"(): $Set<($ResourceKey<($Level)>)>
get "lootModids"(): $Set<(string)>
get "decayMods"(): $Set<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigManager$Type = ($ConfigManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigManager_ = $ConfigManager$Type;
}}
declare module "packages/noobanidus/mods/lootr/network/$CloseCart" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $CloseCart {
 "entityId": integer

constructor(arg0: $FriendlyByteBuf$Type)
constructor(arg0: integer)

public "encode"(arg0: $FriendlyByteBuf$Type): void
public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CloseCart$Type = ($CloseCart);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CloseCart_ = $CloseCart$Type;
}}
declare module "packages/noobanidus/mods/toofast/$TooFast" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $TooFast {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TooFast$Type = ($TooFast);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TooFast_ = $TooFast$Type;
}}
declare module "packages/noobanidus/mods/lootr/init/$ModAdvancements" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$GenericTrigger, $GenericTrigger$Type} from "packages/noobanidus/mods/lootr/advancement/$GenericTrigger"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ModAdvancements {
static readonly "CHEST_LOCATION": $ResourceLocation
static readonly "BARREL_LOCATION": $ResourceLocation
static readonly "CART_LOCATION": $ResourceLocation
static readonly "SHULKER_LOCATION": $ResourceLocation
static readonly "ADVANCEMENT_LOCATION": $ResourceLocation
static readonly "SCORE_LOCATION": $ResourceLocation
static "CHEST_PREDICATE": $GenericTrigger<($UUID)>
static "BARREL_PREDICATE": $GenericTrigger<($UUID)>
static "CART_PREDICATE": $GenericTrigger<($UUID)>
static "SHULKER_PREDICATE": $GenericTrigger<($UUID)>
static "SCORE_PREDICATE": $GenericTrigger<(void)>
static "ADVANCEMENT_PREDICATE": $GenericTrigger<($ResourceLocation)>

constructor()

public static "load"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModAdvancements$Type = ($ModAdvancements);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModAdvancements_ = $ModAdvancements$Type;
}}
declare module "packages/noobanidus/mods/lootr/advancement/$GenericTrigger" {
import {$CriterionTrigger$Listener, $CriterionTrigger$Listener$Type} from "packages/net/minecraft/advancements/$CriterionTrigger$Listener"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$PlayerAdvancements, $PlayerAdvancements$Type} from "packages/net/minecraft/server/$PlayerAdvancements"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$IGenericPredicate, $IGenericPredicate$Type} from "packages/noobanidus/mods/lootr/api/advancement/$IGenericPredicate"
import {$CriterionTrigger, $CriterionTrigger$Type} from "packages/net/minecraft/advancements/$CriterionTrigger"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GenericTrigger$Instance, $GenericTrigger$Instance$Type} from "packages/noobanidus/mods/lootr/advancement/$GenericTrigger$Instance"
import {$DeserializationContext, $DeserializationContext$Type} from "packages/net/minecraft/advancements/critereon/$DeserializationContext"
import {$ContextAwarePredicate, $ContextAwarePredicate$Type} from "packages/net/minecraft/advancements/critereon/$ContextAwarePredicate"

export class $GenericTrigger<T> implements $CriterionTrigger<($GenericTrigger$Instance<(T)>)> {

constructor(arg0: string, arg1: $IGenericPredicate$Type<(T)>)
constructor(arg0: $ResourceLocation$Type, arg1: $IGenericPredicate$Type<(T)>)

public "trigger"(arg0: $ServerPlayer$Type, arg1: T): void
public "createInstance"(arg0: $JsonObject$Type, arg1: $DeserializationContext$Type): $GenericTrigger$Instance<(T)>
public "createInstance"(arg0: $JsonObject$Type, arg1: $ContextAwarePredicate$Type, arg2: $DeserializationContext$Type): $GenericTrigger$Instance<(T)>
public "removePlayerListener"(arg0: $PlayerAdvancements$Type, arg1: $CriterionTrigger$Listener$Type<($GenericTrigger$Instance$Type<(T)>)>): void
public "removePlayerListeners"(arg0: $PlayerAdvancements$Type): void
public "getId"(): $ResourceLocation
public "addPlayerListener"(arg0: $PlayerAdvancements$Type, arg1: $CriterionTrigger$Listener$Type<($GenericTrigger$Instance$Type<(T)>)>): void
get "id"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenericTrigger$Type<T> = ($GenericTrigger<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenericTrigger_<T> = $GenericTrigger$Type<(T)>;
}}
declare module "packages/noobanidus/mods/lootr/impl/$ServerGetter" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export class $ServerGetter {

constructor()

public static "getPlayer"(): $Player
get "player"(): $Player
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerGetter$Type = ($ServerGetter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerGetter_ = $ServerGetter$Type;
}}
declare module "packages/noobanidus/mods/lootr/block/$LootrChestBlock" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ChestType, $ChestType$Type} from "packages/net/minecraft/world/level/block/state/properties/$ChestType"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$ChestBlock, $ChestBlock$Type} from "packages/net/minecraft/world/level/block/$ChestBlock"
import {$EnumProperty, $EnumProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$EnumProperty"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"

export class $LootrChestBlock extends $ChestBlock {
static readonly "FACING": $DirectionProperty
static readonly "TYPE": $EnumProperty<($ChestType)>
static readonly "WATERLOGGED": $BooleanProperty
static readonly "EVENT_SET_OPEN_COUNT": integer
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

constructor(arg0: $BlockBehaviour$Properties$Type)

public "getTicker"<T extends $BlockEntity>(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockEntityType$Type<(T)>): $BlockEntityTicker<(T)>
public "getExplosionResistance"(): float
public "getStateForPlacement"(arg0: $BlockPlaceContext$Type): $BlockState
public "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
public "use"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type, arg4: $InteractionHand$Type, arg5: $BlockHitResult$Type): $InteractionResult
public "hasAnalogOutputSignal"(arg0: $BlockState$Type): boolean
public "getFluidState"(arg0: $BlockState$Type): $FluidState
public "getMenuProvider"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): $MenuProvider
public "getAnalogOutputSignal"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type): integer
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
public "getDestroyProgress"(arg0: $BlockState$Type, arg1: $Player$Type, arg2: $BlockGetter$Type, arg3: $BlockPos$Type): float
public "tick"(arg0: $BlockState$Type, arg1: $ServerLevel$Type, arg2: $BlockPos$Type, arg3: $RandomSource$Type): void
public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
get "explosionResistance"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrChestBlock$Type = ($LootrChestBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrChestBlock_ = $LootrChestBlock$Type;
}}
declare module "packages/noobanidus/mods/lootr/gen/$LootrAtlasGenerator" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$SpriteSourceProvider, $SpriteSourceProvider$Type} from "packages/net/minecraftforge/common/data/$SpriteSourceProvider"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"

export class $LootrAtlasGenerator extends $SpriteSourceProvider {

constructor(arg0: $PackOutput$Type, arg1: $ExistingFileHelper$Type)

public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrAtlasGenerator$Type = ($LootrAtlasGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrAtlasGenerator_ = $LootrAtlasGenerator$Type;
}}
declare module "packages/noobanidus/mods/lootr/$LootrTags" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $LootrTags {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrTags$Type = ($LootrTags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrTags_ = $LootrTags$Type;
}}
declare module "packages/noobanidus/mods/lootr/api/$LootrAPI" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$IntSupplier, $IntSupplier$Type} from "packages/java/util/function/$IntSupplier"
import {$LootFiller, $LootFiller$Type} from "packages/noobanidus/mods/lootr/api/$LootFiller"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$ILootrAPI, $ILootrAPI$Type} from "packages/noobanidus/mods/lootr/api/$ILootrAPI"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$MenuBuilder, $MenuBuilder$Type} from "packages/noobanidus/mods/lootr/api/$MenuBuilder"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ILootrInventory, $ILootrInventory$Type} from "packages/noobanidus/mods/lootr/api/inventory/$ILootrInventory"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$LongSupplier, $LongSupplier$Type} from "packages/java/util/function/$LongSupplier"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$RandomizableContainerBlockEntity, $RandomizableContainerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$RandomizableContainerBlockEntity"

export class $LootrAPI {
static readonly "LOG": $Logger
static readonly "MODID": string
static readonly "ELYTRA_CHEST": $ResourceLocation
static "INSTANCE": $ILootrAPI
static "shouldDiscardIdAndOpeners": boolean

constructor()

public static "getInventory"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $IntSupplier$Type, arg5: $Supplier$Type<($Component$Type)>, arg6: $LootFiller$Type, arg7: $Supplier$Type<($ResourceLocation$Type)>, arg8: $LongSupplier$Type): $ILootrInventory
public static "getInventory"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $RandomizableContainerBlockEntity$Type, arg5: $LootFiller$Type, arg6: $Supplier$Type<($ResourceLocation$Type)>, arg7: $LongSupplier$Type, arg8: $MenuBuilder$Type): $ILootrInventory
public static "getInventory"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $RandomizableContainerBlockEntity$Type, arg5: $LootFiller$Type, arg6: $Supplier$Type<($ResourceLocation$Type)>, arg7: $LongSupplier$Type): $ILootrInventory
public static "getInventory"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $IntSupplier$Type, arg5: $Supplier$Type<($Component$Type)>, arg6: $LootFiller$Type, arg7: $Supplier$Type<($ResourceLocation$Type)>, arg8: $LongSupplier$Type, arg9: $MenuBuilder$Type): $ILootrInventory
public static "getAnalogOutputSignal"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: integer): integer
public static "isFakePlayer"(arg0: $Player$Type): boolean
public static "clearPlayerLoot"(arg0: $UUID$Type): boolean
public static "clearPlayerLoot"(arg0: $ServerPlayer$Type): boolean
/**
 * 
 * @deprecated
 */
public static "getModdedMenu"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $IntSupplier$Type, arg5: $Supplier$Type<($Component$Type)>, arg6: $LootFiller$Type, arg7: $Supplier$Type<($ResourceLocation$Type)>, arg8: $LongSupplier$Type, arg9: $MenuBuilder$Type): $MenuProvider
/**
 * 
 * @deprecated
 */
public static "getModdedMenu"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $IntSupplier$Type, arg5: $Supplier$Type<($Component$Type)>, arg6: $LootFiller$Type, arg7: $Supplier$Type<($ResourceLocation$Type)>, arg8: $LongSupplier$Type): $MenuProvider
/**
 * 
 * @deprecated
 */
public static "getModdedMenu"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $RandomizableContainerBlockEntity$Type, arg5: $LootFiller$Type, arg6: $Supplier$Type<($ResourceLocation$Type)>, arg7: $LongSupplier$Type, arg8: $MenuBuilder$Type): $MenuProvider
/**
 * 
 * @deprecated
 */
public static "getModdedMenu"(arg0: $Level$Type, arg1: $UUID$Type, arg2: $BlockPos$Type, arg3: $ServerPlayer$Type, arg4: $RandomizableContainerBlockEntity$Type, arg5: $LootFiller$Type, arg6: $Supplier$Type<($ResourceLocation$Type)>, arg7: $LongSupplier$Type): $MenuProvider
public static "isSavingStructure"(): boolean
public static "getLootSeed"(arg0: long): long
public static "hasCapacity"(arg0: string): boolean
public static "shouldDiscard"(): boolean
public static "getDestroyProgress"(arg0: $BlockState$Type, arg1: $Player$Type, arg2: $BlockGetter$Type, arg3: $BlockPos$Type, arg4: float): float
public static "getExplosionResistance"(arg0: $Block$Type, arg1: float): float
get "savingStructure"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrAPI$Type = ($LootrAPI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrAPI_ = $LootrAPI$Type;
}}
declare module "packages/noobanidus/mods/lootr/command/$CommandLootr" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $CommandLootr {

constructor(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>)

public "builder"(arg0: $LiteralArgumentBuilder$Type<($CommandSourceStack$Type)>): $LiteralArgumentBuilder<($CommandSourceStack)>
public "register"(): $CommandLootr
public static "createBlock"(arg0: $CommandSourceStack$Type, arg1: $Block$Type, arg2: $ResourceLocation$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommandLootr$Type = ($CommandLootr);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommandLootr_ = $CommandLootr$Type;
}}
declare module "packages/noobanidus/mods/lootr/init/$ModStats" {
import {$Stat, $Stat$Type} from "packages/net/minecraft/stats/$Stat"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ModStats {
static "LOOTED_LOCATION": $ResourceLocation
static "LOOTED_STAT": $Stat<($ResourceLocation)>

constructor()

public static "load"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModStats$Type = ($ModStats);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModStats_ = $ModStats$Type;
}}
declare module "packages/noobanidus/mods/lootr/entity/$LootrChestMinecartEntity" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$ILootCart, $ILootCart$Type} from "packages/noobanidus/mods/lootr/api/entity/$ILootCart"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ClientGamePacketListener, $ClientGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ClientGamePacketListener"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Entity$RemovalReason, $Entity$RemovalReason$Type} from "packages/net/minecraft/world/entity/$Entity$RemovalReason"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$EntityInLevelCallback, $EntityInLevelCallback$Type} from "packages/net/minecraft/world/level/entity/$EntityInLevelCallback"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"
import {$LevelRenderer, $LevelRenderer$Type} from "packages/net/minecraft/client/renderer/$LevelRenderer"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$AbstractMinecart$Type, $AbstractMinecart$Type$Type} from "packages/net/minecraft/world/entity/vehicle/$AbstractMinecart$Type"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$EntityDimensions, $EntityDimensions$Type} from "packages/net/minecraft/world/entity/$EntityDimensions"
import {$AbstractMinecartContainer, $AbstractMinecartContainer$Type} from "packages/net/minecraft/world/entity/vehicle/$AbstractMinecartContainer"

export class $LootrChestMinecartEntity extends $AbstractMinecartContainer implements $ILootCart {
 "lootTable": $ResourceLocation
 "lootTableSeed": long
static readonly "ID_TAG": string
static readonly "PASSENGERS_TAG": string
static readonly "BOARDING_COOLDOWN": integer
static readonly "TOTAL_AIR_SUPPLY": integer
static readonly "MAX_ENTITY_TAG_COUNT": integer
static readonly "DELTA_AFFECTED_BY_BLOCKS_BELOW_0_2": float
static readonly "DELTA_AFFECTED_BY_BLOCKS_BELOW_0_5": double
static readonly "DELTA_AFFECTED_BY_BLOCKS_BELOW_1_0": double
static readonly "BREATHING_DISTANCE_BELOW_EYES": float
static readonly "BASE_TICKS_REQUIRED_TO_FREEZE": integer
static readonly "FREEZE_HURT_FREQUENCY": integer
static readonly "UUID_TAG": string
 "blocksBuilding": boolean
 "level": $Level
 "xo": double
 "yo": double
 "zo": double
 "blockPosition": $BlockPos
 "yRot": float
 "xRot": float
 "yRotO": float
 "xRotO": float
 "horizontalCollision": boolean
 "verticalCollision": boolean
 "verticalCollisionBelow": boolean
 "minorHorizontalCollision": boolean
 "hurtMarked": boolean
 "removalReason": $Entity$RemovalReason
static readonly "DEFAULT_BB_WIDTH": float
static readonly "DEFAULT_BB_HEIGHT": float
 "walkDistO": float
 "walkDist": float
 "moveDist": float
 "flyDist": float
 "fallDistance": float
 "xOld": double
 "yOld": double
 "zOld": double
 "noPhysics": boolean
 "age": integer
 "wasTouchingWater": boolean
 "wasEyeInWater": boolean
 "invulnerableTime": integer
 "levelCallback": $EntityInLevelCallback
 "noCulling": boolean
 "hasImpulse": boolean
 "portalCooldown": integer
 "isInsidePortal": boolean
 "dimensions": $EntityDimensions
 "eyeHeight": float
 "isInPowderSnow": boolean
 "wasInPowderSnow": boolean
 "wasOnFire": boolean
 "mainSupportingBlockPos": $Optional<($BlockPos)>

constructor(arg0: $EntityType$Type<($LootrChestMinecartEntity$Type)>, arg1: $Level$Type)
constructor(arg0: $Level$Type, arg1: double, arg2: double, arg3: double)

public "addOpener"(arg0: $Player$Type): void
public "setClosed"(): void
public "getContainerSize"(): integer
public "isInvulnerableTo"(arg0: $DamageSource$Type): boolean
public "interact"(arg0: $Player$Type, arg1: $InteractionHand$Type): $InteractionResult
public "remove"(arg0: $Entity$RemovalReason$Type): void
public "isOpened"(): boolean
public "getDropItem"(): $Item
public "destroy"(arg0: $DamageSource$Type): void
public "getMinecartType"(): $AbstractMinecart$Type
public "getDefaultDisplayOffset"(): integer
public "getDefaultDisplayBlockState"(): $BlockState
public "startSeenByPlayer"(arg0: $ServerPlayer$Type): void
public "getAddEntityPacket"(): $Packet<($ClientGamePacketListener)>
public "startOpen"(arg0: $Player$Type): void
public "stopOpen"(arg0: $Player$Type): void
public "unpackChestVehicleLootTable"(arg0: $Player$Type): void
public "m_7402_"(arg0: integer, arg1: $Inventory$Type): $AbstractContainerMenu
public "getOpeners"(): $Set<($UUID)>
public "setOpened"(): void
public "addLoot"(arg0: $Player$Type, arg1: $Container$Type, arg2: $ResourceLocation$Type, arg3: long): void
public "isRemoved"(): boolean
public "position"(): $Vec3
public "self"(): $Container
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public "getDisplayName"(): $Component
public "tdv$getDynamicLightX"(): double
public "tdv$getDynamicLightZ"(): double
public "tdv$getDynamicLightWorld"(): $Level
public "tdv$shouldUpdateDynamicLight"(): boolean
public "tdv$resetDynamicLight"(): void
public "tdv$getDynamicLightY"(): double
public "tdv$lambdynlights$scheduleTrackedChunksRebuild"(arg0: $LevelRenderer$Type): void
public "tdv$lambdynlights$updateDynamicLight"(arg0: $LevelRenderer$Type): boolean
public static "tryClear"(arg0: any): void
get "containerSize"(): integer
get "opened"(): boolean
get "dropItem"(): $Item
get "minecartType"(): $AbstractMinecart$Type
get "defaultDisplayOffset"(): integer
get "defaultDisplayBlockState"(): $BlockState
get "addEntityPacket"(): $Packet<($ClientGamePacketListener)>
get "openers"(): $Set<($UUID)>
get "removed"(): boolean
get "displayName"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrChestMinecartEntity$Type = ($LootrChestMinecartEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrChestMinecartEntity_ = $LootrChestMinecartEntity$Type;
}}
declare module "packages/noobanidus/mods/lootr/gen/$LootrItemTagsProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$TagsProvider$TagLookup, $TagsProvider$TagLookup$Type} from "packages/net/minecraft/data/tags/$TagsProvider$TagLookup"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$ItemTagsProvider, $ItemTagsProvider$Type} from "packages/net/minecraft/data/tags/$ItemTagsProvider"

export class $LootrItemTagsProvider extends $ItemTagsProvider {

constructor(arg0: $PackOutput$Type, arg1: $CompletableFuture$Type<($HolderLookup$Provider$Type)>, arg2: $CompletableFuture$Type<($TagsProvider$TagLookup$Type<($Block$Type)>)>, arg3: $ExistingFileHelper$Type)

public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrItemTagsProvider$Type = ($LootrItemTagsProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrItemTagsProvider_ = $LootrItemTagsProvider$Type;
}}
declare module "packages/noobanidus/mods/lootr/event/$HandleAdvancement" {
import {$AdvancementEvent, $AdvancementEvent$Type} from "packages/net/minecraftforge/event/entity/player/$AdvancementEvent"

export class $HandleAdvancement {

constructor()

public static "onAdvancement"(arg0: $AdvancementEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HandleAdvancement$Type = ($HandleAdvancement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HandleAdvancement_ = $HandleAdvancement$Type;
}}
declare module "packages/noobanidus/mods/lootr/client/block/$LootrChestBlockRenderer" {
import {$Material, $Material$Type} from "packages/net/minecraft/client/resources/model/$Material"
import {$BlockEntityRendererProvider$Context, $BlockEntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRendererProvider$Context"
import {$ILootBlockEntity, $ILootBlockEntity$Type} from "packages/noobanidus/mods/lootr/api/blockentity/$ILootBlockEntity"
import {$LootrChestBlockEntity, $LootrChestBlockEntity$Type} from "packages/noobanidus/mods/lootr/block/entities/$LootrChestBlockEntity"
import {$ChestRenderer, $ChestRenderer$Type} from "packages/net/minecraft/client/renderer/blockentity/$ChestRenderer"

export class $LootrChestBlockRenderer<T extends ($LootrChestBlockEntity) & ($ILootBlockEntity)> extends $ChestRenderer<(T)> {
static readonly "MATERIAL": $Material
static readonly "MATERIAL2": $Material
static readonly "MATERIAL3": $Material
static readonly "MATERIAL4": $Material
static readonly "OLD_MATERIAL": $Material
static readonly "OLD_MATERIAL2": $Material

constructor(arg0: $BlockEntityRendererProvider$Context$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootrChestBlockRenderer$Type<T> = ($LootrChestBlockRenderer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootrChestBlockRenderer_<T> = $LootrChestBlockRenderer$Type<(T)>;
}}
