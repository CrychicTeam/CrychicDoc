declare module "packages/dev/architectury/event/events/client/$ClientTooltipEvent$RenderModifyPosition" {
import {$ClientTooltipEvent$PositionContext, $ClientTooltipEvent$PositionContext$Type} from "packages/dev/architectury/event/events/client/$ClientTooltipEvent$PositionContext"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $ClientTooltipEvent$RenderModifyPosition {

 "renderTooltip"(arg0: $GuiGraphics$Type, arg1: $ClientTooltipEvent$PositionContext$Type): void

(arg0: $GuiGraphics$Type, arg1: $ClientTooltipEvent$PositionContext$Type): void
}

export namespace $ClientTooltipEvent$RenderModifyPosition {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTooltipEvent$RenderModifyPosition$Type = ($ClientTooltipEvent$RenderModifyPosition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTooltipEvent$RenderModifyPosition_ = $ClientTooltipEvent$RenderModifyPosition$Type;
}}
declare module "packages/dev/architectury/event/events/common/$EntityEvent$LivingCheckSpawn" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$MobSpawnType, $MobSpawnType$Type} from "packages/net/minecraft/world/entity/$MobSpawnType"
import {$BaseSpawner, $BaseSpawner$Type} from "packages/net/minecraft/world/level/$BaseSpawner"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $EntityEvent$LivingCheckSpawn {

 "canSpawn"(arg0: $LivingEntity$Type, arg1: $LevelAccessor$Type, arg2: double, arg3: double, arg4: double, arg5: $MobSpawnType$Type, arg6: $BaseSpawner$Type): $EventResult

(arg0: $LivingEntity$Type, arg1: $LevelAccessor$Type, arg2: double, arg3: double, arg4: double, arg5: $MobSpawnType$Type, arg6: $BaseSpawner$Type): $EventResult
}

export namespace $EntityEvent$LivingCheckSpawn {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityEvent$LivingCheckSpawn$Type = ($EntityEvent$LivingCheckSpawn);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityEvent$LivingCheckSpawn_ = $EntityEvent$LivingCheckSpawn$Type;
}}
declare module "packages/dev/architectury/event/events/common/$EntityEvent$Add" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $EntityEvent$Add {

 "add"(arg0: $Entity$Type, arg1: $Level$Type): $EventResult

(arg0: $Entity$Type, arg1: $Level$Type): $EventResult
}

export namespace $EntityEvent$Add {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityEvent$Add$Type = ($EntityEvent$Add);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityEvent$Add_ = $EntityEvent$Add$Type;
}}
declare module "packages/dev/architectury/registry/fuel/forge/$FuelRegistryImpl" {
import {$FurnaceFuelBurnTimeEvent, $FurnaceFuelBurnTimeEvent$Type} from "packages/net/minecraftforge/event/furnace/$FurnaceFuelBurnTimeEvent"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"

export class $FuelRegistryImpl {

constructor()

public static "get"(stack: $ItemStack$Type): integer
public static "register"(time: integer, ...items: ($ItemLike$Type)[]): void
public static "event"(event: $FurnaceFuelBurnTimeEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FuelRegistryImpl$Type = ($FuelRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FuelRegistryImpl_ = $FuelRegistryImpl$Type;
}}
declare module "packages/dev/architectury/registry/client/particle/$ParticleProviderRegistry$ExtendedSpriteSet" {
import {$TextureAtlas, $TextureAtlas$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlas"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$SpriteSet, $SpriteSet$Type} from "packages/net/minecraft/client/particle/$SpriteSet"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export interface $ParticleProviderRegistry$ExtendedSpriteSet extends $SpriteSet {

 "getAtlas"(): $TextureAtlas
 "getSprites"(): $List<($TextureAtlasSprite)>
 "get"(arg0: $RandomSource$Type): $TextureAtlasSprite
 "get"(arg0: integer, arg1: integer): $TextureAtlasSprite
}

export namespace $ParticleProviderRegistry$ExtendedSpriteSet {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleProviderRegistry$ExtendedSpriteSet$Type = ($ParticleProviderRegistry$ExtendedSpriteSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleProviderRegistry$ExtendedSpriteSet_ = $ParticleProviderRegistry$ExtendedSpriteSet$Type;
}}
declare module "packages/dev/architectury/fluid/forge/$FluidStackImpl" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$FluidStack, $FluidStack$Type} from "packages/net/minecraftforge/fluids/$FluidStack"
import {$FluidStack$FluidStackAdapter, $FluidStack$FluidStackAdapter$Type} from "packages/dev/architectury/fluid/$FluidStack$FluidStackAdapter"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$FluidStack as $FluidStack$0, $FluidStack$Type as $FluidStack$0$Type} from "packages/dev/architectury/fluid/$FluidStack"

export class $FluidStackImpl extends $Enum<($FluidStackImpl)> implements $FluidStack$FluidStackAdapter<($FluidStack)> {
static readonly "INSTANCE": $FluidStackImpl
static "toValue": $Function<($FluidStack$0), (any)>
static "fromValue": $Function<(any), ($FluidStack$0)>


public static "values"(): ($FluidStackImpl)[]
public "hashCode"(value: $FluidStack$Type): integer
public static "valueOf"(name: string): $FluidStackImpl
public "copy"(value: $FluidStack$Type): $FluidStack
public "create"(fluid: $Supplier$Type<($Fluid$Type)>, amount: long, tag: $CompoundTag$Type): $FluidStack
public static "adapt"(toValue: $Function$Type<($FluidStack$0$Type), (any)>, fromValue: $Function$Type<(any), ($FluidStack$0$Type)>): $FluidStack$FluidStackAdapter<(any)>
public "getTag"(value: $FluidStack$Type): $CompoundTag
public "getRawFluidSupplier"(object: $FluidStack$Type): $Supplier<($Fluid)>
public "setAmount"(object: $FluidStack$Type, amount: long): void
public "getAmount"(object: $FluidStack$Type): long
public "setTag"(value: $FluidStack$Type, tag: $CompoundTag$Type): void
public "getFluid"(object: $FluidStack$Type): $Fluid
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidStackImpl$Type = (("instance")) | ($FluidStackImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidStackImpl_ = $FluidStackImpl$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientScreenInputEvent$KeyTyped" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"

export interface $ClientScreenInputEvent$KeyTyped {

 "charTyped"(arg0: $Minecraft$Type, arg1: $Screen$Type, arg2: character, arg3: integer): $EventResult

(arg0: $Minecraft$Type, arg1: $Screen$Type, arg2: character, arg3: integer): $EventResult
}

export namespace $ClientScreenInputEvent$KeyTyped {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientScreenInputEvent$KeyTyped$Type = ($ClientScreenInputEvent$KeyTyped);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientScreenInputEvent$KeyTyped_ = $ClientScreenInputEvent$KeyTyped$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientTooltipEvent$AdditionalContexts" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $ClientTooltipEvent$AdditionalContexts {

 "getItem"(): $ItemStack
 "setItem"(arg0: $ItemStack$Type): void
}

export namespace $ClientTooltipEvent$AdditionalContexts {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTooltipEvent$AdditionalContexts$Type = ($ClientTooltipEvent$AdditionalContexts);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTooltipEvent$AdditionalContexts_ = $ClientTooltipEvent$AdditionalContexts$Type;
}}
declare module "packages/dev/architectury/networking/transformers/$SplitPacketTransformer" {
import {$PacketTransformer, $PacketTransformer$Type} from "packages/dev/architectury/networking/transformers/$PacketTransformer"
import {$NetworkManager$Side, $NetworkManager$Side$Type} from "packages/dev/architectury/networking/$NetworkManager$Side"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$NetworkManager$PacketContext, $NetworkManager$PacketContext$Type} from "packages/dev/architectury/networking/$NetworkManager$PacketContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$PacketTransformer$TransformationSink, $PacketTransformer$TransformationSink$Type} from "packages/dev/architectury/networking/transformers/$PacketTransformer$TransformationSink"

export class $SplitPacketTransformer implements $PacketTransformer {

constructor()

public "outbound"(side: $NetworkManager$Side$Type, id: $ResourceLocation$Type, buf: $FriendlyByteBuf$Type, sink: $PacketTransformer$TransformationSink$Type): void
public "inbound"(side: $NetworkManager$Side$Type, id: $ResourceLocation$Type, buf: $FriendlyByteBuf$Type, context: $NetworkManager$PacketContext$Type, sink: $PacketTransformer$TransformationSink$Type): void
public static "concat"(transformers: $Iterable$Type<(any)>): $PacketTransformer
public static "none"(): $PacketTransformer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SplitPacketTransformer$Type = ($SplitPacketTransformer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SplitPacketTransformer_ = $SplitPacketTransformer$Type;
}}
declare module "packages/dev/architectury/hooks/level/entity/forge/$ItemEntityHooksImpl" {
import {$IntValue, $IntValue$Type} from "packages/dev/architectury/utils/value/$IntValue"
import {$ItemEntity, $ItemEntity$Type} from "packages/net/minecraft/world/entity/item/$ItemEntity"

export class $ItemEntityHooksImpl {

constructor()

public static "lifespan"(entity: $ItemEntity$Type): $IntValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemEntityHooksImpl$Type = ($ItemEntityHooksImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemEntityHooksImpl_ = $ItemEntityHooksImpl$Type;
}}
declare module "packages/dev/architectury/event/events/common/$LootEvent$ModifyLootTable" {
import {$LootEvent$LootTableModificationContext, $LootEvent$LootTableModificationContext$Type} from "packages/dev/architectury/event/events/common/$LootEvent$LootTableModificationContext"
import {$LootDataManager, $LootDataManager$Type} from "packages/net/minecraft/world/level/storage/loot/$LootDataManager"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $LootEvent$ModifyLootTable {

 "modifyLootTable"(arg0: $LootDataManager$Type, arg1: $ResourceLocation$Type, arg2: $LootEvent$LootTableModificationContext$Type, arg3: boolean): void

(arg0: $LootDataManager$Type, arg1: $ResourceLocation$Type, arg2: $LootEvent$LootTableModificationContext$Type, arg3: boolean): void
}

export namespace $LootEvent$ModifyLootTable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootEvent$ModifyLootTable$Type = ($LootEvent$ModifyLootTable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootEvent$ModifyLootTable_ = $LootEvent$ModifyLootTable$Type;
}}
declare module "packages/dev/architectury/hooks/item/tool/$AxeItemHooks" {
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $AxeItemHooks {


public static "addStrippable"(input: $Block$Type, result: $Block$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AxeItemHooks$Type = ($AxeItemHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AxeItemHooks_ = $AxeItemHooks$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientTooltipEvent$ColorContext" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ClientTooltipEvent$ColorContext {

 "getBackgroundColor"(): integer
 "setBackgroundColor"(arg0: integer): void
 "setOutlineGradientTopColor"(arg0: integer): void
 "setOutlineGradientBottomColor"(arg0: integer): void
 "getOutlineGradientBottomColor"(): integer
 "getOutlineGradientTopColor"(): integer
}

export namespace $ClientTooltipEvent$ColorContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTooltipEvent$ColorContext$Type = ($ClientTooltipEvent$ColorContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTooltipEvent$ColorContext_ = $ClientTooltipEvent$ColorContext$Type;
}}
declare module "packages/dev/architectury/event/events/common/$TickEvent$Server" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$TickEvent, $TickEvent$Type} from "packages/dev/architectury/event/events/common/$TickEvent"

export interface $TickEvent$Server extends $TickEvent<($MinecraftServer)> {

 "tick"(arg0: $MinecraftServer$Type): void

(arg0: $MinecraftServer$Type): void
}

export namespace $TickEvent$Server {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickEvent$Server$Type = ($TickEvent$Server);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickEvent$Server_ = $TickEvent$Server$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientScreenInputEvent$MouseScrolled" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"

export interface $ClientScreenInputEvent$MouseScrolled {

 "mouseScrolled"(arg0: $Minecraft$Type, arg1: $Screen$Type, arg2: double, arg3: double, arg4: double): $EventResult

(arg0: $Minecraft$Type, arg1: $Screen$Type, arg2: double, arg3: double, arg4: double): $EventResult
}

export namespace $ClientScreenInputEvent$MouseScrolled {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientScreenInputEvent$MouseScrolled$Type = ($ClientScreenInputEvent$MouseScrolled);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientScreenInputEvent$MouseScrolled_ = $ClientScreenInputEvent$MouseScrolled$Type;
}}
declare module "packages/dev/architectury/registry/menu/$MenuRegistry$SimpleMenuTypeFactory" {
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

/**
 * 
 * @deprecated
 */
export interface $MenuRegistry$SimpleMenuTypeFactory<T extends $AbstractContainerMenu> {

 "create"(arg0: integer, arg1: $Inventory$Type): T

(arg0: integer, arg1: $Inventory$Type): T
}

export namespace $MenuRegistry$SimpleMenuTypeFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuRegistry$SimpleMenuTypeFactory$Type<T> = ($MenuRegistry$SimpleMenuTypeFactory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuRegistry$SimpleMenuTypeFactory_<T> = $MenuRegistry$SimpleMenuTypeFactory$Type<(T)>;
}}
declare module "packages/dev/architectury/hooks/level/entity/forge/$PlayerHooksImpl" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export class $PlayerHooksImpl {

constructor()

public static "isFake"(playerEntity: $Player$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerHooksImpl$Type = ($PlayerHooksImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerHooksImpl_ = $PlayerHooksImpl$Type;
}}
declare module "packages/dev/architectury/registry/level/entity/forge/$SpawnPlacementsRegistryImpl" {
import {$SpawnPlacements$Type, $SpawnPlacements$Type$Type} from "packages/net/minecraft/world/entity/$SpawnPlacements$Type"
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"
import {$SpawnPlacements$SpawnPredicate, $SpawnPlacements$SpawnPredicate$Type} from "packages/net/minecraft/world/entity/$SpawnPlacements$SpawnPredicate"
import {$Heightmap$Types, $Heightmap$Types$Type} from "packages/net/minecraft/world/level/levelgen/$Heightmap$Types"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $SpawnPlacementsRegistryImpl {

constructor()

public static "register"<T extends $Mob>(type: $Supplier$Type<(any)>, spawnPlacement: $SpawnPlacements$Type$Type, heightmapType: $Heightmap$Types$Type, spawnPredicate: $SpawnPlacements$SpawnPredicate$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpawnPlacementsRegistryImpl$Type = ($SpawnPlacementsRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpawnPlacementsRegistryImpl_ = $SpawnPlacementsRegistryImpl$Type;
}}
declare module "packages/dev/architectury/fluid/$FluidStack" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $FluidStack {


public "getName"(): $Component
public "equals"(o: any): boolean
public "hashCode"(): integer
public "isEmpty"(): boolean
public static "empty"(): $FluidStack
public static "init"(): void
public "write"(tag: $CompoundTag$Type): $CompoundTag
public "write"(buf: $FriendlyByteBuf$Type): void
public static "read"(tag: $CompoundTag$Type): $FluidStack
public static "read"(buf: $FriendlyByteBuf$Type): $FluidStack
public "copy"(): $FluidStack
public static "create"(fluid: $Fluid$Type, amount: long, tag: $CompoundTag$Type): $FluidStack
public static "create"(stack: $FluidStack$Type, amount: long): $FluidStack
public static "create"(fluid: $Supplier$Type<($Fluid$Type)>, amount: long): $FluidStack
public static "create"(fluid: $Supplier$Type<($Fluid$Type)>, amount: long, tag: $CompoundTag$Type): $FluidStack
public static "create"(fluid: $Fluid$Type, amount: long): $FluidStack
public "grow"(amount: long): void
public "getTag"(): $CompoundTag
public static "bucketAmount"(): long
public "getRawFluidSupplier"(): $Supplier<($Fluid)>
public "copyWithAmount"(amount: long): $FluidStack
public "isTagEqual"(other: $FluidStack$Type): boolean
public "isFluidStackEqual"(other: $FluidStack$Type): boolean
public "setAmount"(amount: long): void
public "getAmount"(): long
public "hasTag"(): boolean
public "getTranslationKey"(): string
public "shrink"(amount: long): void
public "setTag"(tag: $CompoundTag$Type): void
public "getFluid"(): $Fluid
public "getOrCreateChildTag"(childName: string): $CompoundTag
public "getChildTag"(childName: string): $CompoundTag
public "isFluidEqual"(other: $FluidStack$Type): boolean
public "getOrCreateTag"(): $CompoundTag
public "getRawFluid"(): $Fluid
public "removeChildTag"(childName: string): void
get "name"(): $Component
get "tag"(): $CompoundTag
get "rawFluidSupplier"(): $Supplier<($Fluid)>
set "amount"(value: long)
get "amount"(): long
get "translationKey"(): string
set "tag"(value: $CompoundTag$Type)
get "fluid"(): $Fluid
get "orCreateTag"(): $CompoundTag
get "rawFluid"(): $Fluid
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidStack$Type = ($FluidStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidStack_ = $FluidStack$Type;
}}
declare module "packages/dev/architectury/event/$CompoundEventResult" {
import {$InteractionResultHolder, $InteractionResultHolder$Type} from "packages/net/minecraft/world/$InteractionResultHolder"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"

export class $CompoundEventResult<T> {


public "value"(): boolean
public "isEmpty"(): boolean
public "result"(): $EventResult
public "isPresent"(): boolean
public static "interrupt"<T>(value: boolean, object: T): $CompoundEventResult<(T)>
public "object"(): T
public static "pass"<T>(): $CompoundEventResult<(T)>
public "isTrue"(): boolean
public static "interruptTrue"<T>(object: T): $CompoundEventResult<(T)>
public static "interruptDefault"<T>(object: T): $CompoundEventResult<(T)>
public "asMinecraft"(): $InteractionResultHolder<(T)>
public static "interruptFalse"<T>(object: T): $CompoundEventResult<(T)>
public "interruptsFurtherEvaluation"(): boolean
public "isFalse"(): boolean
get "empty"(): boolean
get "present"(): boolean
get "true"(): boolean
get "false"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompoundEventResult$Type<T> = ($CompoundEventResult<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompoundEventResult_<T> = $CompoundEventResult$Type<(T)>;
}}
declare module "packages/dev/architectury/registry/registries/$RegistrarManager$RegistryProvider" {
import {$RegistrarBuilder, $RegistrarBuilder$Type} from "packages/dev/architectury/registry/registries/$RegistrarBuilder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Registrar, $Registrar$Type} from "packages/dev/architectury/registry/registries/$Registrar"

export interface $RegistrarManager$RegistryProvider {

 "get"<T>(arg0: $ResourceKey$Type<($Registry$Type<(T)>)>): $Registrar<(T)>
/**
 * 
 * @deprecated
 */
 "get"<T>(arg0: $Registry$Type<(T)>): $Registrar<(T)>
 "builder"<T>(arg0: $Class$Type<(T)>, arg1: $ResourceLocation$Type): $RegistrarBuilder<(T)>
 "forRegistry"<T>(arg0: $ResourceKey$Type<($Registry$Type<(T)>)>, arg1: $Consumer$Type<($Registrar$Type<(T)>)>): void
}

export namespace $RegistrarManager$RegistryProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrarManager$RegistryProvider$Type = ($RegistrarManager$RegistryProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrarManager$RegistryProvider_ = $RegistrarManager$RegistryProvider$Type;
}}
declare module "packages/dev/architectury/registry/menu/$MenuRegistry$ScreenFactory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"
import {$MenuAccess, $MenuAccess$Type} from "packages/net/minecraft/client/gui/screens/inventory/$MenuAccess"

export interface $MenuRegistry$ScreenFactory<H extends $AbstractContainerMenu, S extends ($Screen) & ($MenuAccess<(H)>)> {

 "create"(arg0: H, arg1: $Inventory$Type, arg2: $Component$Type): S

(arg0: H, arg1: $Inventory$Type, arg2: $Component$Type): S
}

export namespace $MenuRegistry$ScreenFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuRegistry$ScreenFactory$Type<H, S> = ($MenuRegistry$ScreenFactory<(H), (S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuRegistry$ScreenFactory_<H, S> = $MenuRegistry$ScreenFactory$Type<(H), (S)>;
}}
declare module "packages/dev/architectury/networking/forge/$ClientNetworkingManager" {
import {$ClientPlayerNetworkEvent$LoggingOut, $ClientPlayerNetworkEvent$LoggingOut$Type} from "packages/net/minecraftforge/client/event/$ClientPlayerNetworkEvent$LoggingOut"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export class $ClientNetworkingManager {

constructor()

public static "loggedOut"(event: $ClientPlayerNetworkEvent$LoggingOut$Type): void
public static "initClient"(): void
public static "getClientPlayer"(): $Player
get "clientPlayer"(): $Player
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientNetworkingManager$Type = ($ClientNetworkingManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientNetworkingManager_ = $ClientNetworkingManager$Type;
}}
declare module "packages/dev/architectury/event/events/common/$PlayerEvent$CraftItem" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export interface $PlayerEvent$CraftItem {

 "craft"(arg0: $Player$Type, arg1: $ItemStack$Type, arg2: $Container$Type): void

(arg0: $Player$Type, arg1: $ItemStack$Type, arg2: $Container$Type): void
}

export namespace $PlayerEvent$CraftItem {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvent$CraftItem$Type = ($PlayerEvent$CraftItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvent$CraftItem_ = $PlayerEvent$CraftItem$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientSystemMessageEvent" {
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$ClientSystemMessageEvent$Received, $ClientSystemMessageEvent$Received$Type} from "packages/dev/architectury/event/events/client/$ClientSystemMessageEvent$Received"

export interface $ClientSystemMessageEvent {

}

export namespace $ClientSystemMessageEvent {
const RECEIVED: $Event<($ClientSystemMessageEvent$Received)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientSystemMessageEvent$Type = ($ClientSystemMessageEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientSystemMessageEvent_ = $ClientSystemMessageEvent$Type;
}}
declare module "packages/dev/architectury/networking/simple/$BaseC2SMessage" {
import {$Message, $Message$Type} from "packages/dev/architectury/networking/simple/$Message"

export class $BaseC2SMessage extends $Message {

constructor()

public "sendToServer"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseC2SMessage$Type = ($BaseC2SMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseC2SMessage_ = $BaseC2SMessage$Type;
}}
declare module "packages/dev/architectury/registry/client/rendering/$RenderTypeRegistry" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $RenderTypeRegistry {


public static "register"(type: $RenderType$Type, ...blocks: ($Block$Type)[]): void
public static "register"(type: $RenderType$Type, ...fluids: ($Fluid$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderTypeRegistry$Type = ($RenderTypeRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderTypeRegistry_ = $RenderTypeRegistry$Type;
}}
declare module "packages/dev/architectury/event/events/common/$ChatEvent" {
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$ChatEvent$Decorate, $ChatEvent$Decorate$Type} from "packages/dev/architectury/event/events/common/$ChatEvent$Decorate"
import {$ChatEvent$Received, $ChatEvent$Received$Type} from "packages/dev/architectury/event/events/common/$ChatEvent$Received"

export interface $ChatEvent {

}

export namespace $ChatEvent {
const DECORATE: $Event<($ChatEvent$Decorate)>
const RECEIVED: $Event<($ChatEvent$Received)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChatEvent$Type = ($ChatEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChatEvent_ = $ChatEvent$Type;
}}
declare module "packages/dev/architectury/event/events/common/$PlayerEvent$PlayerJoin" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"

export interface $PlayerEvent$PlayerJoin {

 "join"(arg0: $ServerPlayer$Type): void

(arg0: $ServerPlayer$Type): void
}

export namespace $PlayerEvent$PlayerJoin {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvent$PlayerJoin$Type = ($PlayerEvent$PlayerJoin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvent$PlayerJoin_ = $PlayerEvent$PlayerJoin$Type;
}}
declare module "packages/dev/architectury/event/events/common/$PlayerEvent$DropItem" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"
import {$ItemEntity, $ItemEntity$Type} from "packages/net/minecraft/world/entity/item/$ItemEntity"

export interface $PlayerEvent$DropItem {

 "drop"(arg0: $Player$Type, arg1: $ItemEntity$Type): $EventResult

(arg0: $Player$Type, arg1: $ItemEntity$Type): $EventResult
}

export namespace $PlayerEvent$DropItem {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvent$DropItem$Type = ($PlayerEvent$DropItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvent$DropItem_ = $PlayerEvent$DropItem$Type;
}}
declare module "packages/dev/architectury/extensions/injected/$InjectedEntityTypeExtension" {
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$InjectedRegistryEntryExtension, $InjectedRegistryEntryExtension$Type} from "packages/dev/architectury/extensions/injected/$InjectedRegistryEntryExtension"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $InjectedEntityTypeExtension extends $InjectedRegistryEntryExtension<($EntityType<(any)>)> {

 "arch$holder"(): $Holder<($EntityType<(any)>)>
 "arch$registryName"(): $ResourceLocation
}

export namespace $InjectedEntityTypeExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectedEntityTypeExtension$Type = ($InjectedEntityTypeExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectedEntityTypeExtension_ = $InjectedEntityTypeExtension$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientGuiEvent$ScreenRenderPre" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $ClientGuiEvent$ScreenRenderPre {

 "render"(arg0: $Screen$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: float): $EventResult

(arg0: $Screen$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: float): $EventResult
}

export namespace $ClientGuiEvent$ScreenRenderPre {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientGuiEvent$ScreenRenderPre$Type = ($ClientGuiEvent$ScreenRenderPre);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientGuiEvent$ScreenRenderPre_ = $ClientGuiEvent$ScreenRenderPre$Type;
}}
declare module "packages/dev/architectury/utils/forge/$GameInstanceImpl" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export class $GameInstanceImpl {

constructor()

public static "getServer"(): $MinecraftServer
get "server"(): $MinecraftServer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GameInstanceImpl$Type = ($GameInstanceImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GameInstanceImpl_ = $GameInstanceImpl$Type;
}}
declare module "packages/dev/architectury/fluid/$FluidStack$FluidStackAdapter" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export interface $FluidStack$FluidStackAdapter<T> {

 "hashCode"(arg0: T): integer
 "copy"(arg0: T): T
 "create"(arg0: $Supplier$Type<($Fluid$Type)>, arg1: long, arg2: $CompoundTag$Type): T
 "getTag"(arg0: T): $CompoundTag
 "getRawFluidSupplier"(arg0: T): $Supplier<($Fluid)>
 "setAmount"(arg0: T, arg1: long): void
 "getAmount"(arg0: T): long
 "setTag"(arg0: T, arg1: $CompoundTag$Type): void
 "getFluid"(arg0: T): $Fluid
}

export namespace $FluidStack$FluidStackAdapter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidStack$FluidStackAdapter$Type<T> = ($FluidStack$FluidStackAdapter<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidStack$FluidStackAdapter_<T> = $FluidStack$FluidStackAdapter$Type<(T)>;
}}
declare module "packages/dev/architectury/event/events/common/$ChunkEvent$LoadData" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ChunkAccess, $ChunkAccess$Type} from "packages/net/minecraft/world/level/chunk/$ChunkAccess"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export interface $ChunkEvent$LoadData {

 "load"(arg0: $ChunkAccess$Type, arg1: $ServerLevel$Type, arg2: $CompoundTag$Type): void

(arg0: $ChunkAccess$Type, arg1: $ServerLevel$Type, arg2: $CompoundTag$Type): void
}

export namespace $ChunkEvent$LoadData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkEvent$LoadData$Type = ($ChunkEvent$LoadData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkEvent$LoadData_ = $ChunkEvent$LoadData$Type;
}}
declare module "packages/dev/architectury/registry/client/rendering/forge/$BlockEntityRendererRegistryImpl" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockEntityRendererProvider, $BlockEntityRendererProvider$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRendererProvider"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"

export class $BlockEntityRendererRegistryImpl {

constructor()

public static "register"<T extends $BlockEntity>(type: $BlockEntityType$Type<(T)>, provider: $BlockEntityRendererProvider$Type<(any)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityRendererRegistryImpl$Type = ($BlockEntityRendererRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityRendererRegistryImpl_ = $BlockEntityRendererRegistryImpl$Type;
}}
declare module "packages/dev/architectury/event/events/common/$ChunkEvent$SaveData" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ChunkAccess, $ChunkAccess$Type} from "packages/net/minecraft/world/level/chunk/$ChunkAccess"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export interface $ChunkEvent$SaveData {

 "save"(arg0: $ChunkAccess$Type, arg1: $ServerLevel$Type, arg2: $CompoundTag$Type): void

(arg0: $ChunkAccess$Type, arg1: $ServerLevel$Type, arg2: $CompoundTag$Type): void
}

export namespace $ChunkEvent$SaveData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkEvent$SaveData$Type = ($ChunkEvent$SaveData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkEvent$SaveData_ = $ChunkEvent$SaveData$Type;
}}
declare module "packages/dev/architectury/event/events/common/$PlayerEvent$CloseMenu" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"

export interface $PlayerEvent$CloseMenu {

 "close"(arg0: $Player$Type, arg1: $AbstractContainerMenu$Type): void

(arg0: $Player$Type, arg1: $AbstractContainerMenu$Type): void
}

export namespace $PlayerEvent$CloseMenu {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvent$CloseMenu$Type = ($PlayerEvent$CloseMenu);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvent$CloseMenu_ = $PlayerEvent$CloseMenu$Type;
}}
declare module "packages/dev/architectury/hooks/level/entity/$ItemEntityHooks" {
import {$IntValue, $IntValue$Type} from "packages/dev/architectury/utils/value/$IntValue"
import {$ItemEntity, $ItemEntity$Type} from "packages/net/minecraft/world/entity/item/$ItemEntity"

export class $ItemEntityHooks {


public static "lifespan"(entity: $ItemEntity$Type): $IntValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemEntityHooks$Type = ($ItemEntityHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemEntityHooks_ = $ItemEntityHooks$Type;
}}
declare module "packages/dev/architectury/event/events/common/$TickEvent$LevelTick" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$TickEvent, $TickEvent$Type} from "packages/dev/architectury/event/events/common/$TickEvent"

export interface $TickEvent$LevelTick<T extends $Level> extends $TickEvent<(T)> {

 "tick"(arg0: T): void

(arg0: T): void
}

export namespace $TickEvent$LevelTick {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickEvent$LevelTick$Type<T> = ($TickEvent$LevelTick<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickEvent$LevelTick_<T> = $TickEvent$LevelTick$Type<(T)>;
}}
declare module "packages/dev/architectury/hooks/fluid/forge/$LiquidBlockHooksImpl" {
import {$FlowingFluid, $FlowingFluid$Type} from "packages/net/minecraft/world/level/material/$FlowingFluid"
import {$LiquidBlock, $LiquidBlock$Type} from "packages/net/minecraft/world/level/block/$LiquidBlock"

export class $LiquidBlockHooksImpl {

constructor()

public static "getFluid"(block: $LiquidBlock$Type): $FlowingFluid
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LiquidBlockHooksImpl$Type = ($LiquidBlockHooksImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LiquidBlockHooksImpl_ = $LiquidBlockHooksImpl$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientChatEvent$Received" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$CompoundEventResult, $CompoundEventResult$Type} from "packages/dev/architectury/event/$CompoundEventResult"
import {$ChatType$Bound, $ChatType$Bound$Type} from "packages/net/minecraft/network/chat/$ChatType$Bound"

export interface $ClientChatEvent$Received {

 "process"(arg0: $ChatType$Bound$Type, arg1: $Component$Type): $CompoundEventResult<($Component)>

(arg0: $ChatType$Bound$Type, arg1: $Component$Type): $CompoundEventResult<($Component)>
}

export namespace $ClientChatEvent$Received {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientChatEvent$Received$Type = ($ClientChatEvent$Received);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientChatEvent$Received_ = $ClientChatEvent$Received$Type;
}}
declare module "packages/dev/architectury/event/forge/$LootTableModificationContextImpl" {
import {$LootEvent$LootTableModificationContext, $LootEvent$LootTableModificationContext$Type} from "packages/dev/architectury/event/events/common/$LootEvent$LootTableModificationContext"
import {$LootPool$Builder, $LootPool$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/$LootPool$Builder"
import {$LootPool, $LootPool$Type} from "packages/net/minecraft/world/level/storage/loot/$LootPool"

export class $LootTableModificationContextImpl implements $LootEvent$LootTableModificationContext {


public "addPool"(pool: $LootPool$Type): void
public "addPool"(pool: $LootPool$Builder$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootTableModificationContextImpl$Type = ($LootTableModificationContextImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootTableModificationContextImpl_ = $LootTableModificationContextImpl$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientRecipeUpdateEvent" {
import {$RecipeManager, $RecipeManager$Type} from "packages/net/minecraft/world/item/crafting/$RecipeManager"
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"

export interface $ClientRecipeUpdateEvent {

 "update"(arg0: $RecipeManager$Type): void

(arg0: $RecipeManager$Type): void
}

export namespace $ClientRecipeUpdateEvent {
const EVENT: $Event<($ClientRecipeUpdateEvent)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientRecipeUpdateEvent$Type = ($ClientRecipeUpdateEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientRecipeUpdateEvent_ = $ClientRecipeUpdateEvent$Type;
}}
declare module "packages/dev/architectury/hooks/level/biome/$BiomeProperties$Mutable" {
import {$SpawnProperties$Mutable, $SpawnProperties$Mutable$Type} from "packages/dev/architectury/hooks/level/biome/$SpawnProperties$Mutable"
import {$EffectsProperties$Mutable, $EffectsProperties$Mutable$Type} from "packages/dev/architectury/hooks/level/biome/$EffectsProperties$Mutable"
import {$BiomeProperties, $BiomeProperties$Type} from "packages/dev/architectury/hooks/level/biome/$BiomeProperties"

export interface $BiomeProperties$Mutable extends $BiomeProperties {

 "getSpawnProperties"(): $SpawnProperties$Mutable
 "getEffectsProperties"(): $EffectsProperties$Mutable
}

export namespace $BiomeProperties$Mutable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeProperties$Mutable$Type = ($BiomeProperties$Mutable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeProperties$Mutable_ = $BiomeProperties$Mutable$Type;
}}
declare module "packages/dev/architectury/utils/$PlatformExpectedError" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Error, $Error$Type} from "packages/java/lang/$Error"

export class $PlatformExpectedError extends $Error {

constructor(message: string, cause: $Throwable$Type, enableSuppression: boolean, writableStackTrace: boolean)
constructor(cause: $Throwable$Type)
constructor(message: string, cause: $Throwable$Type)
constructor(message: string)
constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformExpectedError$Type = ($PlatformExpectedError);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformExpectedError_ = $PlatformExpectedError$Type;
}}
declare module "packages/dev/architectury/event/events/common/$ChatEvent$ChatComponent" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"

export interface $ChatEvent$ChatComponent {

 "get"(): $Component
 "set"(arg0: $Component$Type): void
}

export namespace $ChatEvent$ChatComponent {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChatEvent$ChatComponent$Type = ($ChatEvent$ChatComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChatEvent$ChatComponent_ = $ChatEvent$ChatComponent$Type;
}}
declare module "packages/dev/architectury/registry/level/entity/trade/forge/$TradeRegistryImpl" {
import {$VillagerTradesEvent, $VillagerTradesEvent$Type} from "packages/net/minecraftforge/event/village/$VillagerTradesEvent"
import {$WandererTradesEvent, $WandererTradesEvent$Type} from "packages/net/minecraftforge/event/village/$WandererTradesEvent"
import {$VillagerTrades$ItemListing, $VillagerTrades$ItemListing$Type} from "packages/net/minecraft/world/entity/npc/$VillagerTrades$ItemListing"
import {$VillagerProfession, $VillagerProfession$Type} from "packages/net/minecraft/world/entity/npc/$VillagerProfession"

export class $TradeRegistryImpl {

constructor()

public static "onTradeRegistering"(event: $VillagerTradesEvent$Type): void
public static "registerTradeForWanderingTrader"(rare: boolean, ...trades: ($VillagerTrades$ItemListing$Type)[]): void
public static "registerVillagerTrade0"(profession: $VillagerProfession$Type, level: integer, ...trades: ($VillagerTrades$ItemListing$Type)[]): void
public static "onWanderingTradeRegistering"(event: $WandererTradesEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TradeRegistryImpl$Type = ($TradeRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TradeRegistryImpl_ = $TradeRegistryImpl$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientTooltipEvent$PositionContext" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ClientTooltipEvent$PositionContext {

 "getTooltipY"(): integer
 "getTooltipX"(): integer
 "setTooltipY"(arg0: integer): void
 "setTooltipX"(arg0: integer): void
}

export namespace $ClientTooltipEvent$PositionContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTooltipEvent$PositionContext$Type = ($ClientTooltipEvent$PositionContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTooltipEvent$PositionContext_ = $ClientTooltipEvent$PositionContext$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientGuiEvent" {
import {$ClientGuiEvent$DebugText, $ClientGuiEvent$DebugText$Type} from "packages/dev/architectury/event/events/client/$ClientGuiEvent$DebugText"
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$ClientGuiEvent$ScreenRenderPre, $ClientGuiEvent$ScreenRenderPre$Type} from "packages/dev/architectury/event/events/client/$ClientGuiEvent$ScreenRenderPre"
import {$ClientGuiEvent$ContainerScreenRenderBackground, $ClientGuiEvent$ContainerScreenRenderBackground$Type} from "packages/dev/architectury/event/events/client/$ClientGuiEvent$ContainerScreenRenderBackground"
import {$ClientGuiEvent$SetScreen, $ClientGuiEvent$SetScreen$Type} from "packages/dev/architectury/event/events/client/$ClientGuiEvent$SetScreen"
import {$ClientGuiEvent$RenderHud, $ClientGuiEvent$RenderHud$Type} from "packages/dev/architectury/event/events/client/$ClientGuiEvent$RenderHud"
import {$ClientGuiEvent$ScreenInitPost, $ClientGuiEvent$ScreenInitPost$Type} from "packages/dev/architectury/event/events/client/$ClientGuiEvent$ScreenInitPost"
import {$ClientGuiEvent$ContainerScreenRenderForeground, $ClientGuiEvent$ContainerScreenRenderForeground$Type} from "packages/dev/architectury/event/events/client/$ClientGuiEvent$ContainerScreenRenderForeground"
import {$ClientGuiEvent$ScreenRenderPost, $ClientGuiEvent$ScreenRenderPost$Type} from "packages/dev/architectury/event/events/client/$ClientGuiEvent$ScreenRenderPost"
import {$ClientGuiEvent$ScreenInitPre, $ClientGuiEvent$ScreenInitPre$Type} from "packages/dev/architectury/event/events/client/$ClientGuiEvent$ScreenInitPre"

export interface $ClientGuiEvent {

}

export namespace $ClientGuiEvent {
const RENDER_HUD: $Event<($ClientGuiEvent$RenderHud)>
const DEBUG_TEXT_LEFT: $Event<($ClientGuiEvent$DebugText)>
const DEBUG_TEXT_RIGHT: $Event<($ClientGuiEvent$DebugText)>
const INIT_PRE: $Event<($ClientGuiEvent$ScreenInitPre)>
const INIT_POST: $Event<($ClientGuiEvent$ScreenInitPost)>
const RENDER_PRE: $Event<($ClientGuiEvent$ScreenRenderPre)>
const RENDER_POST: $Event<($ClientGuiEvent$ScreenRenderPost)>
const RENDER_CONTAINER_BACKGROUND: $Event<($ClientGuiEvent$ContainerScreenRenderBackground)>
const RENDER_CONTAINER_FOREGROUND: $Event<($ClientGuiEvent$ContainerScreenRenderForeground)>
const SET_SCREEN: $Event<($ClientGuiEvent$SetScreen)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientGuiEvent$Type = ($ClientGuiEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientGuiEvent_ = $ClientGuiEvent$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientGuiEvent$ContainerScreenRenderForeground" {
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export interface $ClientGuiEvent$ContainerScreenRenderForeground {

 "render"(arg0: $AbstractContainerScreen$Type<(any)>, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: float): void

(arg0: $AbstractContainerScreen$Type<(any)>, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: float): void
}

export namespace $ClientGuiEvent$ContainerScreenRenderForeground {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientGuiEvent$ContainerScreenRenderForeground$Type = ($ClientGuiEvent$ContainerScreenRenderForeground);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientGuiEvent$ContainerScreenRenderForeground_ = $ClientGuiEvent$ContainerScreenRenderForeground$Type;
}}
declare module "packages/dev/architectury/networking/transformers/$PacketSink" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export interface $PacketSink {

 "accept"(arg0: $Packet$Type<(any)>): void

(arg0: $Packet$Type<(any)>): void
}

export namespace $PacketSink {
function client(): $PacketSink
function ofPlayer(player: $ServerPlayer$Type): $PacketSink
function ofPlayers(players: $Iterable$Type<(any)>): $PacketSink
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketSink$Type = ($PacketSink);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketSink_ = $PacketSink$Type;
}}
declare module "packages/dev/architectury/hooks/item/$ItemStackHooks" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $ItemStackHooks {


public static "hasCraftingRemainingItem"(stack: $ItemStack$Type): boolean
public static "getCraftingRemainingItem"(stack: $ItemStack$Type): $ItemStack
public static "copyWithCount"(stack: $ItemStack$Type, count: integer): $ItemStack
public static "giveItem"(player: $ServerPlayer$Type, stack: $ItemStack$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemStackHooks$Type = ($ItemStackHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemStackHooks_ = $ItemStackHooks$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientTickEvent$ClientLevel" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$ClientTickEvent, $ClientTickEvent$Type} from "packages/dev/architectury/event/events/client/$ClientTickEvent"

export interface $ClientTickEvent$ClientLevel extends $ClientTickEvent<($ClientLevel)> {

 "tick"(arg0: $ClientLevel$Type): void

(arg0: $ClientLevel$Type): void
}

export namespace $ClientTickEvent$ClientLevel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTickEvent$ClientLevel$Type = ($ClientTickEvent$ClientLevel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTickEvent$ClientLevel_ = $ClientTickEvent$ClientLevel$Type;
}}
declare module "packages/dev/architectury/registry/level/biome/$BiomeModifications$BiomeContext" {
import {$BiomeProperties, $BiomeProperties$Type} from "packages/dev/architectury/hooks/level/biome/$BiomeProperties"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"

export interface $BiomeModifications$BiomeContext {

 "getKey"(): $Optional<($ResourceLocation)>
 "getProperties"(): $BiomeProperties
 "hasTag"(arg0: $TagKey$Type<($Biome$Type)>): boolean
}

export namespace $BiomeModifications$BiomeContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeModifications$BiomeContext$Type = ($BiomeModifications$BiomeContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeModifications$BiomeContext_ = $BiomeModifications$BiomeContext$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientTooltipEvent$Item" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $ClientTooltipEvent$Item {

 "append"(arg0: $ItemStack$Type, arg1: $List$Type<($Component$Type)>, arg2: $TooltipFlag$Type): void

(arg0: $ItemStack$Type, arg1: $List$Type<($Component$Type)>, arg2: $TooltipFlag$Type): void
}

export namespace $ClientTooltipEvent$Item {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTooltipEvent$Item$Type = ($ClientTooltipEvent$Item);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTooltipEvent$Item_ = $ClientTooltipEvent$Item$Type;
}}
declare module "packages/dev/architectury/registry/client/rendering/forge/$ColorHandlerRegistryImpl" {
import {$BlockColor, $BlockColor$Type} from "packages/net/minecraft/client/color/block/$BlockColor"
import {$RegisterColorHandlersEvent$Item, $RegisterColorHandlersEvent$Item$Type} from "packages/net/minecraftforge/client/event/$RegisterColorHandlersEvent$Item"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemColor, $ItemColor$Type} from "packages/net/minecraft/client/color/item/$ItemColor"
import {$RegisterColorHandlersEvent$Block, $RegisterColorHandlersEvent$Block$Type} from "packages/net/minecraftforge/client/event/$RegisterColorHandlersEvent$Block"

export class $ColorHandlerRegistryImpl {

constructor()

public static "registerItemColors"(itemColor: $ItemColor$Type, ...items: ($Supplier$Type<(any)>)[]): void
public static "onItemColorEvent"(event: $RegisterColorHandlersEvent$Item$Type): void
public static "onBlockColorEvent"(event: $RegisterColorHandlersEvent$Block$Type): void
public static "registerBlockColors"(blockColor: $BlockColor$Type, ...blocks: ($Supplier$Type<(any)>)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorHandlerRegistryImpl$Type = ($ColorHandlerRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorHandlerRegistryImpl_ = $ColorHandlerRegistryImpl$Type;
}}
declare module "packages/dev/architectury/platform/$Platform" {
import {$Env, $Env$Type} from "packages/dev/architectury/utils/$Env"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Mod, $Mod$Type} from "packages/dev/architectury/platform/$Mod"
import {$Dist, $Dist$Type} from "packages/net/minecraftforge/api/distmarker/$Dist"

export class $Platform {


public static "getModIds"(): $Collection<(string)>
public static "getGameFolder"(): $Path
public static "getModsFolder"(): $Path
public static "getOptionalMod"(id: string): $Optional<($Mod)>
public static "isDevelopmentEnvironment"(): boolean
public static "isFabric"(): boolean
public static "getEnvironment"(): $Env
public static "getMods"(): $Collection<($Mod)>
public static "getEnv"(): $Dist
public static "getMod"(id: string): $Mod
public static "getMinecraftVersion"(): string
public static "isForge"(): boolean
public static "getConfigFolder"(): $Path
public static "isModLoaded"(id: string): boolean
get "modIds"(): $Collection<(string)>
get "gameFolder"(): $Path
get "modsFolder"(): $Path
get "developmentEnvironment"(): boolean
get "fabric"(): boolean
get "environment"(): $Env
get "mods"(): $Collection<($Mod)>
get "env"(): $Dist
get "minecraftVersion"(): string
get "forge"(): boolean
get "configFolder"(): $Path
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
declare module "packages/dev/architectury/platform/forge/$PlatformImpl" {
import {$Env, $Env$Type} from "packages/dev/architectury/utils/$Env"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Mod, $Mod$Type} from "packages/dev/architectury/platform/$Mod"
import {$Dist, $Dist$Type} from "packages/net/minecraftforge/api/distmarker/$Dist"

export class $PlatformImpl {

constructor()

public static "getModIds"(): $Collection<(string)>
public static "getGameFolder"(): $Path
public static "getModsFolder"(): $Path
public static "isDevelopmentEnvironment"(): boolean
public static "getEnvironment"(): $Env
public static "getMods"(): $Collection<($Mod)>
public static "getEnv"(): $Dist
public static "getMod"(id: string): $Mod
public static "getConfigFolder"(): $Path
public static "isModLoaded"(id: string): boolean
get "modIds"(): $Collection<(string)>
get "gameFolder"(): $Path
get "modsFolder"(): $Path
get "developmentEnvironment"(): boolean
get "environment"(): $Env
get "mods"(): $Collection<($Mod)>
get "env"(): $Dist
get "configFolder"(): $Path
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformImpl$Type = ($PlatformImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformImpl_ = $PlatformImpl$Type;
}}
declare module "packages/dev/architectury/core/block/$ArchitecturyLiquidBlock" {
import {$IntegerProperty, $IntegerProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$IntegerProperty"
import {$ImmutableList, $ImmutableList$Type} from "packages/com/google/common/collect/$ImmutableList"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$LiquidBlock, $LiquidBlock$Type} from "packages/net/minecraft/world/level/block/$LiquidBlock"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ArchitecturyLiquidBlock extends $LiquidBlock {
static readonly "LEVEL": $IntegerProperty
static readonly "STABLE_SHAPE": $VoxelShape
static readonly "POSSIBLE_FLOW_DIRECTIONS": $ImmutableList<($Direction)>
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

constructor(fluid: $Supplier$Type<(any)>, properties: $BlockBehaviour$Properties$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArchitecturyLiquidBlock$Type = ($ArchitecturyLiquidBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArchitecturyLiquidBlock_ = $ArchitecturyLiquidBlock$Type;
}}
declare module "packages/dev/architectury/event/events/common/$LootEvent$LootTableModificationContext" {
import {$LootPool$Builder, $LootPool$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/$LootPool$Builder"
import {$LootPool, $LootPool$Type} from "packages/net/minecraft/world/level/storage/loot/$LootPool"

export interface $LootEvent$LootTableModificationContext {

 "addPool"(arg0: $LootPool$Type): void
 "addPool"(pool: $LootPool$Builder$Type): void

(arg0: $LootPool$Type): void
}

export namespace $LootEvent$LootTableModificationContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootEvent$LootTableModificationContext$Type = ($LootEvent$LootTableModificationContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootEvent$LootTableModificationContext_ = $LootEvent$LootTableModificationContext$Type;
}}
declare module "packages/dev/architectury/event/events/common/$TickEvent$ServerLevelTick" {
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$TickEvent$LevelTick, $TickEvent$LevelTick$Type} from "packages/dev/architectury/event/events/common/$TickEvent$LevelTick"

export interface $TickEvent$ServerLevelTick extends $TickEvent$LevelTick<($ServerLevel)> {

 "tick"(arg0: $ServerLevel$Type): void

(arg0: $ServerLevel$Type): void
}

export namespace $TickEvent$ServerLevelTick {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickEvent$ServerLevelTick$Type = ($TickEvent$ServerLevelTick);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickEvent$ServerLevelTick_ = $TickEvent$ServerLevelTick$Type;
}}
declare module "packages/dev/architectury/registry/client/gui/forge/$ClientTooltipComponentRegistryImpl" {
import {$TooltipComponent, $TooltipComponent$Type} from "packages/net/minecraft/world/inventory/tooltip/$TooltipComponent"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $ClientTooltipComponentRegistryImpl {

constructor()

public static "register"<T extends $TooltipComponent>(clazz: $Class$Type<(T)>, factory: $Function$Type<(any), (any)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTooltipComponentRegistryImpl$Type = ($ClientTooltipComponentRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTooltipComponentRegistryImpl_ = $ClientTooltipComponentRegistryImpl$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientGuiEvent$DebugText" {
import {$List, $List$Type} from "packages/java/util/$List"

export interface $ClientGuiEvent$DebugText {

 "gatherText"(arg0: $List$Type<(string)>): void

(arg0: $List$Type<(string)>): void
}

export namespace $ClientGuiEvent$DebugText {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientGuiEvent$DebugText$Type = ($ClientGuiEvent$DebugText);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientGuiEvent$DebugText_ = $ClientGuiEvent$DebugText$Type;
}}
declare module "packages/dev/architectury/hooks/fluid/forge/$FluidBucketHooksImpl" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$BucketItem, $BucketItem$Type} from "packages/net/minecraft/world/item/$BucketItem"

export class $FluidBucketHooksImpl {

constructor()

public static "getFluid"(item: $BucketItem$Type): $Fluid
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidBucketHooksImpl$Type = ($FluidBucketHooksImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidBucketHooksImpl_ = $FluidBucketHooksImpl$Type;
}}
declare module "packages/dev/architectury/hooks/fluid/forge/$FluidStackHooksForge" {
import {$FluidStack, $FluidStack$Type} from "packages/net/minecraftforge/fluids/$FluidStack"
import {$FluidStack as $FluidStack$0, $FluidStack$Type as $FluidStack$0$Type} from "packages/dev/architectury/fluid/$FluidStack"

export class $FluidStackHooksForge {


public static "toForge"(stack: $FluidStack$0$Type): $FluidStack
public static "fromForge"(stack: $FluidStack$Type): $FluidStack$0
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidStackHooksForge$Type = ($FluidStackHooksForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidStackHooksForge_ = $FluidStackHooksForge$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientChatEvent$Send" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"

export interface $ClientChatEvent$Send {

 "send"(arg0: string, arg1: $Component$Type): $EventResult

(arg0: string, arg1: $Component$Type): $EventResult
}

export namespace $ClientChatEvent$Send {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientChatEvent$Send$Type = ($ClientChatEvent$Send);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientChatEvent$Send_ = $ClientChatEvent$Send$Type;
}}
declare module "packages/dev/architectury/event/events/common/$LifecycleEvent$ServerLevelState" {
import {$LifecycleEvent$LevelState, $LifecycleEvent$LevelState$Type} from "packages/dev/architectury/event/events/common/$LifecycleEvent$LevelState"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export interface $LifecycleEvent$ServerLevelState extends $LifecycleEvent$LevelState<($ServerLevel)> {

 "act"(arg0: $ServerLevel$Type): void

(arg0: $ServerLevel$Type): void
}

export namespace $LifecycleEvent$ServerLevelState {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LifecycleEvent$ServerLevelState$Type = ($LifecycleEvent$ServerLevelState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LifecycleEvent$ServerLevelState_ = $LifecycleEvent$ServerLevelState$Type;
}}
declare module "packages/dev/architectury/registry/client/particle/$ParticleProviderRegistry" {
import {$ParticleProviderRegistry$DeferredParticleProvider, $ParticleProviderRegistry$DeferredParticleProvider$Type} from "packages/dev/architectury/registry/client/particle/$ParticleProviderRegistry$DeferredParticleProvider"
import {$ParticleProvider, $ParticleProvider$Type} from "packages/net/minecraft/client/particle/$ParticleProvider"
import {$RegistrySupplier, $RegistrySupplier$Type} from "packages/dev/architectury/registry/registries/$RegistrySupplier"
import {$ParticleType, $ParticleType$Type} from "packages/net/minecraft/core/particles/$ParticleType"
import {$ParticleOptions, $ParticleOptions$Type} from "packages/net/minecraft/core/particles/$ParticleOptions"

export class $ParticleProviderRegistry {

constructor()

public static "register"<T extends $ParticleOptions>(type: $ParticleType$Type<(T)>, provider: $ParticleProviderRegistry$DeferredParticleProvider$Type<(T)>): void
public static "register"<T extends $ParticleOptions>(type: $ParticleType$Type<(T)>, provider: $ParticleProvider$Type<(T)>): void
public static "register"<T extends $ParticleOptions>(supplier: $RegistrySupplier$Type<(any)>, provider: $ParticleProviderRegistry$DeferredParticleProvider$Type<(T)>): void
public static "register"<T extends $ParticleOptions>(supplier: $RegistrySupplier$Type<(any)>, provider: $ParticleProvider$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleProviderRegistry$Type = ($ParticleProviderRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleProviderRegistry_ = $ParticleProviderRegistry$Type;
}}
declare module "packages/dev/architectury/registry/$CreativeTabRegistry$ModifyTabCallback" {
import {$CreativeTabOutput, $CreativeTabOutput$Type} from "packages/dev/architectury/registry/$CreativeTabOutput"
import {$FeatureFlagSet, $FeatureFlagSet$Type} from "packages/net/minecraft/world/flag/$FeatureFlagSet"

export interface $CreativeTabRegistry$ModifyTabCallback {

 "accept"(arg0: $FeatureFlagSet$Type, arg1: $CreativeTabOutput$Type, arg2: boolean): void

(arg0: $FeatureFlagSet$Type, arg1: $CreativeTabOutput$Type, arg2: boolean): void
}

export namespace $CreativeTabRegistry$ModifyTabCallback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CreativeTabRegistry$ModifyTabCallback$Type = ($CreativeTabRegistry$ModifyTabCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CreativeTabRegistry$ModifyTabCallback_ = $CreativeTabRegistry$ModifyTabCallback$Type;
}}
declare module "packages/dev/architectury/registry/registries/$RegistrarManager" {
import {$RegistrarBuilder, $RegistrarBuilder$Type} from "packages/dev/architectury/registry/registries/$RegistrarBuilder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Registrar, $Registrar$Type} from "packages/dev/architectury/registry/registries/$Registrar"

export class $RegistrarManager {


/**
 * 
 * @deprecated
 */
public "get"<T>(registry: $Registry$Type<(T)>): $Registrar<(T)>
public static "get"(modId: string): $RegistrarManager
public "get"<T>(key: $ResourceKey$Type<($Registry$Type<(T)>)>): $Registrar<(T)>
public "builder"<T>(registryId: $ResourceLocation$Type, ...typeGetter: (T)[]): $RegistrarBuilder<(T)>
/**
 * 
 * @deprecated
 */
public static "getId"<T>(object: T, fallback: $Registry$Type<(T)>): $ResourceLocation
public static "getId"<T>(object: T, fallback: $ResourceKey$Type<($Registry$Type<(T)>)>): $ResourceLocation
public "forRegistry"<T>(key: $ResourceKey$Type<($Registry$Type<(T)>)>, callback: $Consumer$Type<($Registrar$Type<(T)>)>): void
public "getModId"(): string
get "modId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrarManager$Type = ($RegistrarManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrarManager_ = $RegistrarManager$Type;
}}
declare module "packages/dev/architectury/event/events/common/$PlayerEvent" {
import {$PlayerEvent$CraftItem, $PlayerEvent$CraftItem$Type} from "packages/dev/architectury/event/events/common/$PlayerEvent$CraftItem"
import {$PlayerEvent$DropItem, $PlayerEvent$DropItem$Type} from "packages/dev/architectury/event/events/common/$PlayerEvent$DropItem"
import {$PlayerEvent$PlayerAdvancement, $PlayerEvent$PlayerAdvancement$Type} from "packages/dev/architectury/event/events/common/$PlayerEvent$PlayerAdvancement"
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$PlayerEvent$ChangeDimension, $PlayerEvent$ChangeDimension$Type} from "packages/dev/architectury/event/events/common/$PlayerEvent$ChangeDimension"
import {$PlayerEvent$PlayerRespawn, $PlayerEvent$PlayerRespawn$Type} from "packages/dev/architectury/event/events/common/$PlayerEvent$PlayerRespawn"
import {$PlayerEvent$OpenMenu, $PlayerEvent$OpenMenu$Type} from "packages/dev/architectury/event/events/common/$PlayerEvent$OpenMenu"
import {$PlayerEvent$PlayerJoin, $PlayerEvent$PlayerJoin$Type} from "packages/dev/architectury/event/events/common/$PlayerEvent$PlayerJoin"
import {$PlayerEvent$PlayerClone, $PlayerEvent$PlayerClone$Type} from "packages/dev/architectury/event/events/common/$PlayerEvent$PlayerClone"
import {$PlayerEvent$PlayerQuit, $PlayerEvent$PlayerQuit$Type} from "packages/dev/architectury/event/events/common/$PlayerEvent$PlayerQuit"
import {$PlayerEvent$CloseMenu, $PlayerEvent$CloseMenu$Type} from "packages/dev/architectury/event/events/common/$PlayerEvent$CloseMenu"
import {$PlayerEvent$PickupItem, $PlayerEvent$PickupItem$Type} from "packages/dev/architectury/event/events/common/$PlayerEvent$PickupItem"
import {$PlayerEvent$AttackEntity, $PlayerEvent$AttackEntity$Type} from "packages/dev/architectury/event/events/common/$PlayerEvent$AttackEntity"
import {$PlayerEvent$SmeltItem, $PlayerEvent$SmeltItem$Type} from "packages/dev/architectury/event/events/common/$PlayerEvent$SmeltItem"
import {$PlayerEvent$PickupItemPredicate, $PlayerEvent$PickupItemPredicate$Type} from "packages/dev/architectury/event/events/common/$PlayerEvent$PickupItemPredicate"
import {$PlayerEvent$FillBucket, $PlayerEvent$FillBucket$Type} from "packages/dev/architectury/event/events/common/$PlayerEvent$FillBucket"

export interface $PlayerEvent {

}

export namespace $PlayerEvent {
const PLAYER_JOIN: $Event<($PlayerEvent$PlayerJoin)>
const PLAYER_QUIT: $Event<($PlayerEvent$PlayerQuit)>
const PLAYER_RESPAWN: $Event<($PlayerEvent$PlayerRespawn)>
const PLAYER_ADVANCEMENT: $Event<($PlayerEvent$PlayerAdvancement)>
const PLAYER_CLONE: $Event<($PlayerEvent$PlayerClone)>
const CRAFT_ITEM: $Event<($PlayerEvent$CraftItem)>
const SMELT_ITEM: $Event<($PlayerEvent$SmeltItem)>
const PICKUP_ITEM_PRE: $Event<($PlayerEvent$PickupItemPredicate)>
const PICKUP_ITEM_POST: $Event<($PlayerEvent$PickupItem)>
const CHANGE_DIMENSION: $Event<($PlayerEvent$ChangeDimension)>
const DROP_ITEM: $Event<($PlayerEvent$DropItem)>
const OPEN_MENU: $Event<($PlayerEvent$OpenMenu)>
const CLOSE_MENU: $Event<($PlayerEvent$CloseMenu)>
const FILL_BUCKET: $Event<($PlayerEvent$FillBucket)>
const ATTACK_ENTITY: $Event<($PlayerEvent$AttackEntity)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvent$Type = ($PlayerEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvent_ = $PlayerEvent$Type;
}}
declare module "packages/dev/architectury/networking/$NetworkManager" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$PacketSink, $PacketSink$Type} from "packages/dev/architectury/networking/transformers/$PacketSink"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$PacketTransformer, $PacketTransformer$Type} from "packages/dev/architectury/networking/transformers/$PacketTransformer"
import {$ClientGamePacketListener, $ClientGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ClientGamePacketListener"
import {$NetworkManager$Side, $NetworkManager$Side$Type} from "packages/dev/architectury/networking/$NetworkManager$Side"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NetworkManager$NetworkReceiver, $NetworkManager$NetworkReceiver$Type} from "packages/dev/architectury/networking/$NetworkManager$NetworkReceiver"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $NetworkManager {

constructor()

public static "c2s"(): $NetworkManager$Side
public static "s2c"(): $NetworkManager$Side
public static "sendToPlayer"(player: $ServerPlayer$Type, id: $ResourceLocation$Type, buf: $FriendlyByteBuf$Type): void
public static "createAddEntityPacket"(entity: $Entity$Type): $Packet<($ClientGamePacketListener)>
/**
 * 
 * @deprecated
 */
public static "toPacket"(side: $NetworkManager$Side$Type, id: $ResourceLocation$Type, buf: $FriendlyByteBuf$Type): $Packet<(any)>
public static "registerReceiver"(side: $NetworkManager$Side$Type, id: $ResourceLocation$Type, packetTransformers: $List$Type<($PacketTransformer$Type)>, receiver: $NetworkManager$NetworkReceiver$Type): void
public static "registerReceiver"(side: $NetworkManager$Side$Type, id: $ResourceLocation$Type, receiver: $NetworkManager$NetworkReceiver$Type): void
public static "clientToServer"(): $NetworkManager$Side
public static "collectPackets"(sink: $PacketSink$Type, side: $NetworkManager$Side$Type, id: $ResourceLocation$Type, buf: $FriendlyByteBuf$Type): void
public static "serverToClient"(): $NetworkManager$Side
public static "canPlayerReceive"(player: $ServerPlayer$Type, id: $ResourceLocation$Type): boolean
public static "sendToPlayers"(players: $Iterable$Type<($ServerPlayer$Type)>, id: $ResourceLocation$Type, buf: $FriendlyByteBuf$Type): void
/**
 * 
 * @deprecated
 */
public static "toPackets"(side: $NetworkManager$Side$Type, id: $ResourceLocation$Type, buf: $FriendlyByteBuf$Type): $List<($Packet<(any)>)>
public static "canServerReceive"(id: $ResourceLocation$Type): boolean
public static "sendToServer"(id: $ResourceLocation$Type, buf: $FriendlyByteBuf$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkManager$Type = ($NetworkManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkManager_ = $NetworkManager$Type;
}}
declare module "packages/dev/architectury/platform/$Mod$ConfigurationScreenProvider" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export interface $Mod$ConfigurationScreenProvider {

 "provide"(arg0: $Screen$Type): $Screen

(arg0: $Screen$Type): $Screen
}

export namespace $Mod$ConfigurationScreenProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Mod$ConfigurationScreenProvider$Type = ($Mod$ConfigurationScreenProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Mod$ConfigurationScreenProvider_ = $Mod$ConfigurationScreenProvider$Type;
}}
declare module "packages/dev/architectury/networking/simple/$MessageDecoder" {
import {$Message, $Message$Type} from "packages/dev/architectury/networking/simple/$Message"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$NetworkManager$NetworkReceiver, $NetworkManager$NetworkReceiver$Type} from "packages/dev/architectury/networking/$NetworkManager$NetworkReceiver"

export interface $MessageDecoder<T extends $Message> {

 "decode"(arg0: $FriendlyByteBuf$Type): T
 "createReceiver"(): $NetworkManager$NetworkReceiver

(arg0: $FriendlyByteBuf$Type): T
}

export namespace $MessageDecoder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageDecoder$Type<T> = ($MessageDecoder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageDecoder_<T> = $MessageDecoder$Type<(T)>;
}}
declare module "packages/dev/architectury/hooks/client/screen/$ScreenAccess" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export interface $ScreenAccess {

 "addWidget"<T extends ($GuiEventListener) & ($NarratableEntry)>(arg0: T): T
 "getRenderables"(): $List<($Renderable)>
 "getNarratables"(): $List<($NarratableEntry)>
 "getScreen"(): $Screen
 "addRenderableWidget"<T extends ($AbstractWidget) & ($Renderable) & ($NarratableEntry)>(arg0: T): T
 "addRenderableOnly"<T extends $Renderable>(arg0: T): T
}

export namespace $ScreenAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenAccess$Type = ($ScreenAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenAccess_ = $ScreenAccess$Type;
}}
declare module "packages/dev/architectury/utils/value/$DoubleValue" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$DoubleConsumer, $DoubleConsumer$Type} from "packages/it/unimi/dsi/fastutil/doubles/$DoubleConsumer"
import {$Value, $Value$Type} from "packages/dev/architectury/utils/value/$Value"
import {$DoubleSupplier, $DoubleSupplier$Type} from "packages/java/util/function/$DoubleSupplier"

export interface $DoubleValue extends $Value<(double)>, $DoubleSupplier, $DoubleConsumer {

 "get"(): double
 "getAsDouble"(): double
/**
 * 
 * @deprecated
 */
 "accept"(arg0: double): void
/**
 * 
 * @deprecated
 */
 "andThen"(arg0: $Consumer$Type<(any)>): $Consumer<(double)>
 "andThen"(arg0: $DoubleConsumer$Type): $DoubleConsumer
 "accept"(arg0: double): void
}

export namespace $DoubleValue {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleValue$Type = ($DoubleValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleValue_ = $DoubleValue$Type;
}}
declare module "packages/dev/architectury/core/fluid/$ArchitecturyFluidAttributesForge" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$SoundAction, $SoundAction$Type} from "packages/net/minecraftforge/common/$SoundAction"
import {$ArchitecturyFluidAttributes, $ArchitecturyFluidAttributes$Type} from "packages/dev/architectury/core/fluid/$ArchitecturyFluidAttributes"
import {$FluidType$Properties, $FluidType$Properties$Type} from "packages/net/minecraftforge/fluids/$FluidType$Properties"
import {$FluidStack, $FluidStack$Type} from "packages/net/minecraftforge/fluids/$FluidStack"
import {$IClientFluidTypeExtensions, $IClientFluidTypeExtensions$Type} from "packages/net/minecraftforge/client/extensions/common/$IClientFluidTypeExtensions"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$FluidStack as $FluidStack$0, $FluidStack$Type as $FluidStack$0$Type} from "packages/dev/architectury/fluid/$FluidStack"
import {$Rarity, $Rarity$Type} from "packages/net/minecraft/world/item/$Rarity"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"
import {$Lazy, $Lazy$Type} from "packages/net/minecraftforge/common/util/$Lazy"
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$FluidType, $FluidType$Type} from "packages/net/minecraftforge/fluids/$FluidType"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"

export class $ArchitecturyFluidAttributesForge extends $FluidType {
static readonly "BUCKET_VOLUME": integer
static readonly "SIZE": $Lazy<(integer)>

constructor(builder: $FluidType$Properties$Type, fluid: $Fluid$Type, attributes: $ArchitecturyFluidAttributes$Type)

public "convertSafe"(stack: $FluidStack$Type): $FluidStack$0
public "convertSafe"(state: $FluidState$Type): $FluidStack$0
public "getDescription"(): $Component
public "getDescription"(stack: $FluidStack$Type): $Component
public "initializeClient"(consumer: $Consumer$Type<($IClientFluidTypeExtensions$Type)>): void
public "getTemperature"(state: $FluidState$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): integer
public "getTemperature"(stack: $FluidStack$Type): integer
public "getBucket"(stack: $FluidStack$Type): $ItemStack
public "getViscosity"(state: $FluidState$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): integer
public "getViscosity"(stack: $FluidStack$Type): integer
public "getLightLevel"(stack: $FluidStack$Type): integer
public "getLightLevel"(state: $FluidState$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): integer
public "getDensity"(stack: $FluidStack$Type): integer
public "getDensity"(state: $FluidState$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): integer
public "getDescriptionId"(stack: $FluidStack$Type): string
public "getDescriptionId"(): string
public "getRarity"(): $Rarity
public "getRarity"(stack: $FluidStack$Type): $Rarity
public "canConvertToSource"(state: $FluidState$Type, reader: $LevelReader$Type, pos: $BlockPos$Type): boolean
public "canConvertToSource"(stack: $FluidStack$Type): boolean
public "getSound"(stack: $FluidStack$Type, action: $SoundAction$Type): $SoundEvent
public "getSound"(action: $SoundAction$Type): $SoundEvent
public "getSound"(player: $Player$Type, getter: $BlockGetter$Type, pos: $BlockPos$Type, action: $SoundAction$Type): $SoundEvent
get "description"(): $Component
get "descriptionId"(): string
get "rarity"(): $Rarity
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArchitecturyFluidAttributesForge$Type = ($ArchitecturyFluidAttributesForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArchitecturyFluidAttributesForge_ = $ArchitecturyFluidAttributesForge$Type;
}}
declare module "packages/dev/architectury/event/events/common/$LifecycleEvent$ServerState" {
import {$LifecycleEvent$InstanceState, $LifecycleEvent$InstanceState$Type} from "packages/dev/architectury/event/events/common/$LifecycleEvent$InstanceState"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export interface $LifecycleEvent$ServerState extends $LifecycleEvent$InstanceState<($MinecraftServer)> {

 "stateChanged"(arg0: $MinecraftServer$Type): void

(arg0: $MinecraftServer$Type): void
}

export namespace $LifecycleEvent$ServerState {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LifecycleEvent$ServerState$Type = ($LifecycleEvent$ServerState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LifecycleEvent$ServerState_ = $LifecycleEvent$ServerState$Type;
}}
declare module "packages/dev/architectury/hooks/client/screen/forge/$ScreenHooksImpl" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export class $ScreenHooksImpl {

constructor()

public static "addWidget"<T extends ($GuiEventListener) & ($NarratableEntry)>(screen: $Screen$Type, listener: T): T
public static "getRenderables"(screen: $Screen$Type): $List<($Renderable)>
public static "getNarratables"(screen: $Screen$Type): $List<($NarratableEntry)>
public static "addRenderableWidget"<T extends ($AbstractWidget) & ($Renderable) & ($NarratableEntry)>(screen: $Screen$Type, widget: T): T
public static "addRenderableOnly"<T extends $Renderable>(screen: $Screen$Type, listener: T): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenHooksImpl$Type = ($ScreenHooksImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenHooksImpl_ = $ScreenHooksImpl$Type;
}}
declare module "packages/dev/architectury/registry/menu/$ExtendedMenuProvider" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export interface $ExtendedMenuProvider extends $MenuProvider {

 "saveExtraData"(arg0: $FriendlyByteBuf$Type): void
 "getDisplayName"(): $Component
 "createMenu"(arg0: integer, arg1: $Inventory$Type, arg2: $Player$Type): $AbstractContainerMenu
}

export namespace $ExtendedMenuProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtendedMenuProvider$Type = ($ExtendedMenuProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtendedMenuProvider_ = $ExtendedMenuProvider$Type;
}}
declare module "packages/dev/architectury/hooks/level/biome/$SpawnProperties$Mutable" {
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$MobSpawnSettings$SpawnerData, $MobSpawnSettings$SpawnerData$Type} from "packages/net/minecraft/world/level/biome/$MobSpawnSettings$SpawnerData"
import {$SpawnProperties, $SpawnProperties$Type} from "packages/dev/architectury/hooks/level/biome/$SpawnProperties"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MobSpawnSettings$MobSpawnCost, $MobSpawnSettings$MobSpawnCost$Type} from "packages/net/minecraft/world/level/biome/$MobSpawnSettings$MobSpawnCost"
import {$MobCategory, $MobCategory$Type} from "packages/net/minecraft/world/entity/$MobCategory"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $SpawnProperties$Mutable extends $SpawnProperties {

 "addSpawn"(arg0: $MobCategory$Type, arg1: $MobSpawnSettings$SpawnerData$Type): $SpawnProperties$Mutable
 "setCreatureProbability"(arg0: float): $SpawnProperties$Mutable
 "removeSpawns"(arg0: $BiPredicate$Type<($MobCategory$Type), ($MobSpawnSettings$SpawnerData$Type)>): boolean
 "clearSpawnCost"(arg0: $EntityType$Type<(any)>): $SpawnProperties$Mutable
 "setSpawnCost"(arg0: $EntityType$Type<(any)>, arg1: double, arg2: double): $SpawnProperties$Mutable
 "setSpawnCost"(arg0: $EntityType$Type<(any)>, arg1: $MobSpawnSettings$MobSpawnCost$Type): $SpawnProperties$Mutable
 "getCreatureProbability"(): float
 "getSpawners"(): $Map<($MobCategory), ($List<($MobSpawnSettings$SpawnerData)>)>
 "getMobSpawnCosts"(): $Map<($EntityType<(any)>), ($MobSpawnSettings$MobSpawnCost)>
}

export namespace $SpawnProperties$Mutable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpawnProperties$Mutable$Type = ($SpawnProperties$Mutable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpawnProperties$Mutable_ = $SpawnProperties$Mutable$Type;
}}
declare module "packages/dev/architectury/registry/client/level/entity/forge/$EntityRendererRegistryImpl" {
import {$EntityRendererProvider, $EntityRendererProvider$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider"
import {$EntityRenderersEvent$RegisterRenderers, $EntityRenderersEvent$RegisterRenderers$Type} from "packages/net/minecraftforge/client/event/$EntityRenderersEvent$RegisterRenderers"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EntityRendererRegistryImpl {

constructor()

public static "register"<T extends $Entity>(type: $Supplier$Type<(any)>, factory: $EntityRendererProvider$Type<(T)>): void
public static "event"(event: $EntityRenderersEvent$RegisterRenderers$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityRendererRegistryImpl$Type = ($EntityRendererRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityRendererRegistryImpl_ = $EntityRendererRegistryImpl$Type;
}}
declare module "packages/dev/architectury/core/fluid/$SimpleArchitecturyFluidAttributes" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ArchitecturyFluidAttributes, $ArchitecturyFluidAttributes$Type} from "packages/dev/architectury/core/fluid/$ArchitecturyFluidAttributes"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$FluidStack, $FluidStack$Type} from "packages/dev/architectury/fluid/$FluidStack"
import {$Rarity, $Rarity$Type} from "packages/net/minecraft/world/item/$Rarity"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$RegistrySupplier, $RegistrySupplier$Type} from "packages/dev/architectury/registry/registries/$RegistrySupplier"
import {$LiquidBlock, $LiquidBlock$Type} from "packages/net/minecraft/world/level/block/$LiquidBlock"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"

export class $SimpleArchitecturyFluidAttributes implements $ArchitecturyFluidAttributes {


public static "of"(flowingFluid: $Supplier$Type<(any)>, sourceFluid: $Supplier$Type<(any)>): $SimpleArchitecturyFluidAttributes
public "block"(block: $Supplier$Type<(any)>): $SimpleArchitecturyFluidAttributes
public "block"(block: $RegistrySupplier$Type<(any)>): $SimpleArchitecturyFluidAttributes
public "color"(color: integer): $SimpleArchitecturyFluidAttributes
public "getBlock"(): $LiquidBlock
public "flowingTexture"(flowingTexture: $ResourceLocation$Type): $SimpleArchitecturyFluidAttributes
public "fillSound"(fillSound: $SoundEvent$Type): $SimpleArchitecturyFluidAttributes
public "emptySound"(emptySound: $SoundEvent$Type): $SimpleArchitecturyFluidAttributes
public "getFlowingTexture"(stack: $FluidStack$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): $ResourceLocation
public "getOverlayTexture"(state: $FluidState$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): $ResourceLocation
public "overlayTexture"(overlayTexture: $ResourceLocation$Type): $SimpleArchitecturyFluidAttributes
public "lighterThanAir"(lighterThanAir: boolean): $SimpleArchitecturyFluidAttributes
public "bucketItem"(bucketItem: $RegistrySupplier$Type<($Item$Type)>): $SimpleArchitecturyFluidAttributes
public "bucketItem"(bucketItem: $Supplier$Type<(any)>): $SimpleArchitecturyFluidAttributes
public "sourceTexture"(sourceTexture: $ResourceLocation$Type): $SimpleArchitecturyFluidAttributes
public "getTickDelay"(level: $LevelReader$Type): integer
public "getBucketItem"(): $Item
public "getDropOff"(level: $LevelReader$Type): integer
public "getSourceFluid"(): $Fluid
public "getFillSound"(stack: $FluidStack$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): $SoundEvent
public "getFlowingFluid"(): $Fluid
public "getSourceTexture"(stack: $FluidStack$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): $ResourceLocation
public "getLuminosity"(stack: $FluidStack$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): integer
public "viscosity"(viscosity: integer): $SimpleArchitecturyFluidAttributes
public "getExplosionResistance"(): float
public "explosionResistance"(explosionResistance: float): $SimpleArchitecturyFluidAttributes
public "getTemperature"(stack: $FluidStack$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): integer
public "tickDelay"(tickDelay: integer): $SimpleArchitecturyFluidAttributes
public "rarity"(rarity: $Rarity$Type): $SimpleArchitecturyFluidAttributes
public "getColor"(stack: $FluidStack$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): integer
public "density"(density: integer): $SimpleArchitecturyFluidAttributes
public "luminosity"(luminosity: integer): $SimpleArchitecturyFluidAttributes
public "getViscosity"(stack: $FluidStack$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): integer
public "getDensity"(stack: $FluidStack$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): integer
public "isLighterThanAir"(stack: $FluidStack$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): boolean
public "slopeFindDistance"(slopeFindDistance: integer): $SimpleArchitecturyFluidAttributes
public "blockSupplier"(block: $Supplier$Type<($RegistrySupplier$Type<(any)>)>): $SimpleArchitecturyFluidAttributes
public "getTranslationKey"(stack: $FluidStack$Type): string
public "temperature"(temperature: integer): $SimpleArchitecturyFluidAttributes
public "getRarity"(stack: $FluidStack$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): $Rarity
public "getEmptySound"(stack: $FluidStack$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): $SoundEvent
public static "ofSupplier"(flowingFluid: $Supplier$Type<(any)>, sourceFluid: $Supplier$Type<(any)>): $SimpleArchitecturyFluidAttributes
public "convertToSource"(canConvertToSource: boolean): $SimpleArchitecturyFluidAttributes
public "dropOff"(dropOff: integer): $SimpleArchitecturyFluidAttributes
public "bucketItemSupplier"(bucketItem: $Supplier$Type<($RegistrySupplier$Type<($Item$Type)>)>): $SimpleArchitecturyFluidAttributes
public "canConvertToSource"(): boolean
public "getSlopeFindDistance"(level: $LevelReader$Type): integer
public "getName"(): $Component
public "getName"(stack: $FluidStack$Type): $Component
public "getFlowingTexture"(stack: $FluidStack$Type): $ResourceLocation
public "getFlowingTexture"(state: $FluidState$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): $ResourceLocation
public "getFlowingTexture"(): $ResourceLocation
public "getOverlayTexture"(): $ResourceLocation
public "getOverlayTexture"(stack: $FluidStack$Type): $ResourceLocation
public "getTickDelay"(): integer
public "getDropOff"(): integer
public "getFillSound"(): $SoundEvent
public "getFillSound"(stack: $FluidStack$Type): $SoundEvent
public "getSourceTexture"(state: $FluidState$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): $ResourceLocation
public "getSourceTexture"(): $ResourceLocation
public "getSourceTexture"(stack: $FluidStack$Type): $ResourceLocation
public "getLuminosity"(): integer
public "getLuminosity"(stack: $FluidStack$Type): integer
public "getTemperature"(stack: $FluidStack$Type): integer
public "getTemperature"(): integer
public "getColor"(state: $FluidState$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): integer
public "getColor"(stack: $FluidStack$Type): integer
public "getColor"(): integer
public "getViscosity"(): integer
public "getViscosity"(stack: $FluidStack$Type): integer
public "getDensity"(): integer
public "getDensity"(stack: $FluidStack$Type): integer
public "isLighterThanAir"(): boolean
public "isLighterThanAir"(stack: $FluidStack$Type): boolean
public "getTranslationKey"(): string
public "getRarity"(stack: $FluidStack$Type): $Rarity
public "getRarity"(): $Rarity
public "getEmptySound"(stack: $FluidStack$Type): $SoundEvent
public "getEmptySound"(): $SoundEvent
public "getSlopeFindDistance"(): integer
get "sourceFluid"(): $Fluid
get "flowingFluid"(): $Fluid
get "name"(): $Component
get "translationKey"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleArchitecturyFluidAttributes$Type = ($SimpleArchitecturyFluidAttributes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleArchitecturyFluidAttributes_ = $SimpleArchitecturyFluidAttributes$Type;
}}
declare module "packages/dev/architectury/event/events/common/$EntityEvent$EnterSection" {
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $EntityEvent$EnterSection {

 "enterSection"(arg0: $Entity$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer): void

(arg0: $Entity$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer): void
}

export namespace $EntityEvent$EnterSection {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityEvent$EnterSection$Type = ($EntityEvent$EnterSection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityEvent$EnterSection_ = $EntityEvent$EnterSection$Type;
}}
declare module "packages/dev/architectury/event/events/common/$ExplosionEvent" {
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$ExplosionEvent$Pre, $ExplosionEvent$Pre$Type} from "packages/dev/architectury/event/events/common/$ExplosionEvent$Pre"
import {$ExplosionEvent$Detonate, $ExplosionEvent$Detonate$Type} from "packages/dev/architectury/event/events/common/$ExplosionEvent$Detonate"

export interface $ExplosionEvent {

}

export namespace $ExplosionEvent {
const PRE: $Event<($ExplosionEvent$Pre)>
const DETONATE: $Event<($ExplosionEvent$Detonate)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExplosionEvent$Type = ($ExplosionEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExplosionEvent_ = $ExplosionEvent$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientCommandRegistrationEvent$ClientCommandSourceStack" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Suggestions, $Suggestions$Type} from "packages/com/mojang/brigadier/suggestion/$Suggestions"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$SharedSuggestionProvider$TextCoordinates, $SharedSuggestionProvider$TextCoordinates$Type} from "packages/net/minecraft/commands/$SharedSuggestionProvider$TextCoordinates"
import {$FeatureFlagSet, $FeatureFlagSet$Type} from "packages/net/minecraft/world/flag/$FeatureFlagSet"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$SuggestionsBuilder, $SuggestionsBuilder$Type} from "packages/com/mojang/brigadier/suggestion/$SuggestionsBuilder"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$SharedSuggestionProvider$ElementSuggestionType, $SharedSuggestionProvider$ElementSuggestionType$Type} from "packages/net/minecraft/commands/$SharedSuggestionProvider$ElementSuggestionType"
import {$CommandContext, $CommandContext$Type} from "packages/com/mojang/brigadier/context/$CommandContext"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Vec2, $Vec2$Type} from "packages/net/minecraft/world/phys/$Vec2"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"
import {$Message, $Message$Type} from "packages/com/mojang/brigadier/$Message"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$SharedSuggestionProvider, $SharedSuggestionProvider$Type} from "packages/net/minecraft/commands/$SharedSuggestionProvider"

export interface $ClientCommandRegistrationEvent$ClientCommandSourceStack extends $SharedSuggestionProvider {

 "arch$sendSuccess"(arg0: $Supplier$Type<($Component$Type)>, arg1: boolean): void
 "arch$getPlayer"(): $LocalPlayer
 "arch$getLevel"(): $ClientLevel
 "arch$getRotation"(): $Vec2
 "arch$sendFailure"(arg0: $Component$Type): void
 "arch$getPosition"(): $Vec3
 "hasPermission"(arg0: integer): boolean
 "customSuggestion"(arg0: $CommandContext$Type<(any)>): $CompletableFuture<($Suggestions)>
 "getSelectedEntities"(): $Collection<(string)>
 "getOnlinePlayerNames"(): $Collection<(string)>
 "getRelevantCoordinates"(): $Collection<($SharedSuggestionProvider$TextCoordinates)>
 "getAbsoluteCoordinates"(): $Collection<($SharedSuggestionProvider$TextCoordinates)>
 "getAllTeams"(): $Collection<(string)>
 "levels"(): $Set<($ResourceKey<($Level)>)>
 "suggestRegistryElements"(arg0: $ResourceKey$Type<(any)>, arg1: $SharedSuggestionProvider$ElementSuggestionType$Type, arg2: $SuggestionsBuilder$Type, arg3: $CommandContext$Type<(any)>): $CompletableFuture<($Suggestions)>
 "enabledFeatures"(): $FeatureFlagSet
 "registryAccess"(): $RegistryAccess
 "getRecipeNames"(): $Stream<($ResourceLocation)>
 "suggestRegistryElements"(arg0: $Registry$Type<(any)>, arg1: $SharedSuggestionProvider$ElementSuggestionType$Type, arg2: $SuggestionsBuilder$Type): void
 "getAvailableSounds"(): $Stream<($ResourceLocation)>
 "getCustomTabSugggestions"(): $Collection<(string)>
}

export namespace $ClientCommandRegistrationEvent$ClientCommandSourceStack {
function suggestResource(arg0: $Iterable$Type<($ResourceLocation$Type)>, arg1: $SuggestionsBuilder$Type): $CompletableFuture<($Suggestions)>
function suggestResource(arg0: $Iterable$Type<($ResourceLocation$Type)>, arg1: $SuggestionsBuilder$Type, arg2: string): $CompletableFuture<($Suggestions)>
function suggestResource(arg0: $Stream$Type<($ResourceLocation$Type)>, arg1: $SuggestionsBuilder$Type, arg2: string): $CompletableFuture<($Suggestions)>
function suggest(arg0: $Iterable$Type<(string)>, arg1: $SuggestionsBuilder$Type): $CompletableFuture<($Suggestions)>
function suggestCoordinates(arg0: string, arg1: $Collection$Type<($SharedSuggestionProvider$TextCoordinates$Type)>, arg2: $SuggestionsBuilder$Type, arg3: $Predicate$Type<(string)>): $CompletableFuture<($Suggestions)>
function suggest2DCoordinates(arg0: string, arg1: $Collection$Type<($SharedSuggestionProvider$TextCoordinates$Type)>, arg2: $SuggestionsBuilder$Type, arg3: $Predicate$Type<(string)>): $CompletableFuture<($Suggestions)>
function suggestResource<T>(arg0: $Stream$Type<(T)>, arg1: $SuggestionsBuilder$Type, arg2: $Function$Type<(T), ($ResourceLocation$Type)>, arg3: $Function$Type<(T), ($Message$Type)>): $CompletableFuture<($Suggestions)>
function filterResources<T>(arg0: $Iterable$Type<(T)>, arg1: string, arg2: $Function$Type<(T), ($ResourceLocation$Type)>, arg3: $Consumer$Type<(T)>): void
function matchesSubStr(arg0: string, arg1: string): boolean
function filterResources<T>(arg0: $Iterable$Type<(T)>, arg1: string, arg2: string, arg3: $Function$Type<(T), ($ResourceLocation$Type)>, arg4: $Consumer$Type<(T)>): void
function suggestResource<T>(arg0: $Iterable$Type<(T)>, arg1: $SuggestionsBuilder$Type, arg2: $Function$Type<(T), ($ResourceLocation$Type)>, arg3: $Function$Type<(T), ($Message$Type)>): $CompletableFuture<($Suggestions)>
function suggest(arg0: (string)[], arg1: $SuggestionsBuilder$Type): $CompletableFuture<($Suggestions)>
function suggestResource(arg0: $Stream$Type<($ResourceLocation$Type)>, arg1: $SuggestionsBuilder$Type): $CompletableFuture<($Suggestions)>
function suggest(arg0: $Stream$Type<(string)>, arg1: $SuggestionsBuilder$Type): $CompletableFuture<($Suggestions)>
function suggest<T>(arg0: $Iterable$Type<(T)>, arg1: $SuggestionsBuilder$Type, arg2: $Function$Type<(T), (string)>, arg3: $Function$Type<(T), ($Message$Type)>): $CompletableFuture<($Suggestions)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientCommandRegistrationEvent$ClientCommandSourceStack$Type = ($ClientCommandRegistrationEvent$ClientCommandSourceStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientCommandRegistrationEvent$ClientCommandSourceStack_ = $ClientCommandRegistrationEvent$ClientCommandSourceStack$Type;
}}
declare module "packages/dev/architectury/registry/client/rendering/$BlockEntityRendererRegistry" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockEntityRendererProvider, $BlockEntityRendererProvider$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRendererProvider"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"

export class $BlockEntityRendererRegistry {


public static "register"<T extends $BlockEntity>(type: $BlockEntityType$Type<(T)>, provider: $BlockEntityRendererProvider$Type<(any)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityRendererRegistry$Type = ($BlockEntityRendererRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityRendererRegistry_ = $BlockEntityRendererRegistry$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientTickEvent" {
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$ClientTickEvent$Client, $ClientTickEvent$Client$Type} from "packages/dev/architectury/event/events/client/$ClientTickEvent$Client"
import {$ClientTickEvent$ClientLevel, $ClientTickEvent$ClientLevel$Type} from "packages/dev/architectury/event/events/client/$ClientTickEvent$ClientLevel"

export interface $ClientTickEvent<T> {

 "tick"(arg0: T): void

(arg0: T): void
}

export namespace $ClientTickEvent {
const CLIENT_PRE: $Event<($ClientTickEvent$Client)>
const CLIENT_POST: $Event<($ClientTickEvent$Client)>
const CLIENT_LEVEL_PRE: $Event<($ClientTickEvent$ClientLevel)>
const CLIENT_LEVEL_POST: $Event<($ClientTickEvent$ClientLevel)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTickEvent$Type<T> = ($ClientTickEvent<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTickEvent_<T> = $ClientTickEvent$Type<(T)>;
}}
declare module "packages/dev/architectury/event/events/client/$ClientChatEvent" {
import {$ClientChatEvent$Send, $ClientChatEvent$Send$Type} from "packages/dev/architectury/event/events/client/$ClientChatEvent$Send"
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$ClientChatEvent$Received, $ClientChatEvent$Received$Type} from "packages/dev/architectury/event/events/client/$ClientChatEvent$Received"

export interface $ClientChatEvent {

}

export namespace $ClientChatEvent {
const SEND: $Event<($ClientChatEvent$Send)>
const RECEIVED: $Event<($ClientChatEvent$Received)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientChatEvent$Type = ($ClientChatEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientChatEvent_ = $ClientChatEvent$Type;
}}
declare module "packages/dev/architectury/extensions/injected/$InjectedGameEventExtension" {
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$GameEvent, $GameEvent$Type} from "packages/net/minecraft/world/level/gameevent/$GameEvent"
import {$InjectedRegistryEntryExtension, $InjectedRegistryEntryExtension$Type} from "packages/dev/architectury/extensions/injected/$InjectedRegistryEntryExtension"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $InjectedGameEventExtension extends $InjectedRegistryEntryExtension<($GameEvent)> {

 "arch$holder"(): $Holder<($GameEvent)>
 "arch$registryName"(): $ResourceLocation
}

export namespace $InjectedGameEventExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectedGameEventExtension$Type = ($InjectedGameEventExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectedGameEventExtension_ = $InjectedGameEventExtension$Type;
}}
declare module "packages/dev/architectury/impl/$ScreenAccessImpl" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ScreenAccess, $ScreenAccess$Type} from "packages/dev/architectury/hooks/client/screen/$ScreenAccess"

export class $ScreenAccessImpl implements $ScreenAccess {

constructor(screen: $Screen$Type)

public "setScreen"(screen: $Screen$Type): void
public "addWidget"<T extends ($GuiEventListener) & ($NarratableEntry)>(listener: T): T
public "getRenderables"(): $List<($Renderable)>
public "getNarratables"(): $List<($NarratableEntry)>
public "getScreen"(): $Screen
public "addRenderableWidget"<T extends ($AbstractWidget) & ($Renderable) & ($NarratableEntry)>(widget: T): T
public "addRenderableOnly"<T extends $Renderable>(listener: T): T
set "screen"(value: $Screen$Type)
get "renderables"(): $List<($Renderable)>
get "narratables"(): $List<($NarratableEntry)>
get "screen"(): $Screen
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenAccessImpl$Type = ($ScreenAccessImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenAccessImpl_ = $ScreenAccessImpl$Type;
}}
declare module "packages/dev/architectury/registry/client/level/entity/$EntityModelLayerRegistry" {
import {$ModelLayerLocation, $ModelLayerLocation$Type} from "packages/net/minecraft/client/model/geom/$ModelLayerLocation"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$LayerDefinition, $LayerDefinition$Type} from "packages/net/minecraft/client/model/geom/builders/$LayerDefinition"

export class $EntityModelLayerRegistry {

constructor()

public static "register"(location: $ModelLayerLocation$Type, definition: $Supplier$Type<($LayerDefinition$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityModelLayerRegistry$Type = ($EntityModelLayerRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityModelLayerRegistry_ = $EntityModelLayerRegistry$Type;
}}
declare module "packages/dev/architectury/event/events/common/$ChatEvent$Decorate" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$ChatEvent$ChatComponent, $ChatEvent$ChatComponent$Type} from "packages/dev/architectury/event/events/common/$ChatEvent$ChatComponent"

export interface $ChatEvent$Decorate {

 "decorate"(arg0: $ServerPlayer$Type, arg1: $ChatEvent$ChatComponent$Type): void

(arg0: $ServerPlayer$Type, arg1: $ChatEvent$ChatComponent$Type): void
}

export namespace $ChatEvent$Decorate {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChatEvent$Decorate$Type = ($ChatEvent$Decorate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChatEvent$Decorate_ = $ChatEvent$Decorate$Type;
}}
declare module "packages/dev/architectury/event/events/common/$InteractionEvent$RightClickItem" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$CompoundEventResult, $CompoundEventResult$Type} from "packages/dev/architectury/event/$CompoundEventResult"

export interface $InteractionEvent$RightClickItem {

 "click"(arg0: $Player$Type, arg1: $InteractionHand$Type): $CompoundEventResult<($ItemStack)>

(arg0: $Player$Type, arg1: $InteractionHand$Type): $CompoundEventResult<($ItemStack)>
}

export namespace $InteractionEvent$RightClickItem {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InteractionEvent$RightClickItem$Type = ($InteractionEvent$RightClickItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InteractionEvent$RightClickItem_ = $InteractionEvent$RightClickItem$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientGuiEvent$RenderHud" {
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $ClientGuiEvent$RenderHud {

 "renderHud"(arg0: $GuiGraphics$Type, arg1: float): void

(arg0: $GuiGraphics$Type, arg1: float): void
}

export namespace $ClientGuiEvent$RenderHud {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientGuiEvent$RenderHud$Type = ($ClientGuiEvent$RenderHud);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientGuiEvent$RenderHud_ = $ClientGuiEvent$RenderHud$Type;
}}
declare module "packages/dev/architectury/event/events/common/$InteractionEvent$RightClickBlock" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $InteractionEvent$RightClickBlock {

 "click"(arg0: $Player$Type, arg1: $InteractionHand$Type, arg2: $BlockPos$Type, arg3: $Direction$Type): $EventResult

(arg0: $Player$Type, arg1: $InteractionHand$Type, arg2: $BlockPos$Type, arg3: $Direction$Type): $EventResult
}

export namespace $InteractionEvent$RightClickBlock {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InteractionEvent$RightClickBlock$Type = ($InteractionEvent$RightClickBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InteractionEvent$RightClickBlock_ = $InteractionEvent$RightClickBlock$Type;
}}
declare module "packages/dev/architectury/event/events/common/$PlayerEvent$PickupItemPredicate" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ItemEntity, $ItemEntity$Type} from "packages/net/minecraft/world/entity/item/$ItemEntity"

export interface $PlayerEvent$PickupItemPredicate {

 "canPickup"(arg0: $Player$Type, arg1: $ItemEntity$Type, arg2: $ItemStack$Type): $EventResult

(arg0: $Player$Type, arg1: $ItemEntity$Type, arg2: $ItemStack$Type): $EventResult
}

export namespace $PlayerEvent$PickupItemPredicate {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvent$PickupItemPredicate$Type = ($PlayerEvent$PickupItemPredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvent$PickupItemPredicate_ = $PlayerEvent$PickupItemPredicate$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientScreenInputEvent$KeyReleased" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"

export interface $ClientScreenInputEvent$KeyReleased {

 "keyReleased"(arg0: $Minecraft$Type, arg1: $Screen$Type, arg2: integer, arg3: integer, arg4: integer): $EventResult

(arg0: $Minecraft$Type, arg1: $Screen$Type, arg2: integer, arg3: integer, arg4: integer): $EventResult
}

export namespace $ClientScreenInputEvent$KeyReleased {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientScreenInputEvent$KeyReleased$Type = ($ClientScreenInputEvent$KeyReleased);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientScreenInputEvent$KeyReleased_ = $ClientScreenInputEvent$KeyReleased$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientScreenInputEvent$MouseReleased" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"

export interface $ClientScreenInputEvent$MouseReleased {

 "mouseReleased"(arg0: $Minecraft$Type, arg1: $Screen$Type, arg2: double, arg3: double, arg4: integer): $EventResult

(arg0: $Minecraft$Type, arg1: $Screen$Type, arg2: double, arg3: double, arg4: integer): $EventResult
}

export namespace $ClientScreenInputEvent$MouseReleased {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientScreenInputEvent$MouseReleased$Type = ($ClientScreenInputEvent$MouseReleased);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientScreenInputEvent$MouseReleased_ = $ClientScreenInputEvent$MouseReleased$Type;
}}
declare module "packages/dev/architectury/core/fluid/$ArchitecturyFlowingFluid" {
import {$IntegerProperty, $IntegerProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$IntegerProperty"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ForgeFlowingFluid, $ForgeFlowingFluid$Type} from "packages/net/minecraftforge/fluids/$ForgeFlowingFluid"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"

export class $ArchitecturyFlowingFluid extends $ForgeFlowingFluid {
static readonly "FALLING": $BooleanProperty
static readonly "LEVEL": $IntegerProperty
static readonly "FLUID_STATE_REGISTRY": $IdMapper<($FluidState)>


public "getTickDelay"(level: $LevelReader$Type): integer
public "getBucket"(): $Item
public "getPickupSound"(): $Optional<($SoundEvent)>
public "isSame"(fluid: $Fluid$Type): boolean
public "getFlowing"(): $Fluid
public "getSource"(): $Fluid
get "bucket"(): $Item
get "pickupSound"(): $Optional<($SoundEvent)>
get "flowing"(): $Fluid
get "source"(): $Fluid
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArchitecturyFlowingFluid$Type = ($ArchitecturyFlowingFluid);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArchitecturyFlowingFluid_ = $ArchitecturyFlowingFluid$Type;
}}
declare module "packages/dev/architectury/event/events/common/$InteractionEvent$InteractEntity" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $InteractionEvent$InteractEntity {

 "interact"(arg0: $Player$Type, arg1: $Entity$Type, arg2: $InteractionHand$Type): $EventResult

(arg0: $Player$Type, arg1: $Entity$Type, arg2: $InteractionHand$Type): $EventResult
}

export namespace $InteractionEvent$InteractEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InteractionEvent$InteractEntity$Type = ($InteractionEvent$InteractEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InteractionEvent$InteractEntity_ = $InteractionEvent$InteractEntity$Type;
}}
declare module "packages/dev/architectury/event/events/common/$PlayerEvent$ChangeDimension" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export interface $PlayerEvent$ChangeDimension {

 "change"(arg0: $ServerPlayer$Type, arg1: $ResourceKey$Type<($Level$Type)>, arg2: $ResourceKey$Type<($Level$Type)>): void

(arg0: $ServerPlayer$Type, arg1: $ResourceKey$Type<($Level$Type)>, arg2: $ResourceKey$Type<($Level$Type)>): void
}

export namespace $PlayerEvent$ChangeDimension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvent$ChangeDimension$Type = ($PlayerEvent$ChangeDimension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvent$ChangeDimension_ = $PlayerEvent$ChangeDimension$Type;
}}
declare module "packages/dev/architectury/event/events/common/$BlockEvent$Place" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $BlockEvent$Place {

 "placeBlock"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $Entity$Type): $EventResult

(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $Entity$Type): $EventResult
}

export namespace $BlockEvent$Place {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEvent$Place$Type = ($BlockEvent$Place);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEvent$Place_ = $BlockEvent$Place$Type;
}}
declare module "packages/dev/architectury/hooks/$DyeColorHooks" {
import {$DyeColor, $DyeColor$Type} from "packages/net/minecraft/world/item/$DyeColor"

export class $DyeColorHooks {


public static "getColorValue"(color: $DyeColor$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DyeColorHooks$Type = ($DyeColorHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DyeColorHooks_ = $DyeColorHooks$Type;
}}
declare module "packages/dev/architectury/hooks/level/biome/$BiomeHooks" {
import {$BiomeProperties, $BiomeProperties$Type} from "packages/dev/architectury/hooks/level/biome/$BiomeProperties"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"

export class $BiomeHooks {

constructor()

public static "getBiomeProperties"(biome: $Biome$Type): $BiomeProperties
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeHooks$Type = ($BiomeHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeHooks_ = $BiomeHooks$Type;
}}
declare module "packages/dev/architectury/hooks/level/biome/$EffectsProperties$Mutable" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Music, $Music$Type} from "packages/net/minecraft/sounds/$Music"
import {$AmbientMoodSettings, $AmbientMoodSettings$Type} from "packages/net/minecraft/world/level/biome/$AmbientMoodSettings"
import {$AmbientParticleSettings, $AmbientParticleSettings$Type} from "packages/net/minecraft/world/level/biome/$AmbientParticleSettings"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AmbientAdditionsSettings, $AmbientAdditionsSettings$Type} from "packages/net/minecraft/world/level/biome/$AmbientAdditionsSettings"
import {$OptionalInt, $OptionalInt$Type} from "packages/java/util/$OptionalInt"
import {$EffectsProperties, $EffectsProperties$Type} from "packages/dev/architectury/hooks/level/biome/$EffectsProperties"
import {$BiomeSpecialEffects$GrassColorModifier, $BiomeSpecialEffects$GrassColorModifier$Type} from "packages/net/minecraft/world/level/biome/$BiomeSpecialEffects$GrassColorModifier"

export interface $EffectsProperties$Mutable extends $EffectsProperties {

 "setAmbientMoodSound"(arg0: $AmbientMoodSettings$Type): $EffectsProperties$Mutable
 "setAmbientLoopSound"(arg0: $Holder$Type<($SoundEvent$Type)>): $EffectsProperties$Mutable
 "setGrassColorOverride"(arg0: integer): $EffectsProperties$Mutable
 "setGrassColorModifier"(arg0: $BiomeSpecialEffects$GrassColorModifier$Type): $EffectsProperties$Mutable
 "setFoliageColorOverride"(arg0: integer): $EffectsProperties$Mutable
 "setAmbientAdditionsSound"(arg0: $AmbientAdditionsSettings$Type): $EffectsProperties$Mutable
 "setWaterFogColor"(arg0: integer): $EffectsProperties$Mutable
 "setSkyColor"(arg0: integer): $EffectsProperties$Mutable
 "setWaterColor"(arg0: integer): $EffectsProperties$Mutable
 "setFogColor"(arg0: integer): $EffectsProperties$Mutable
 "setBackgroundMusic"(arg0: $Music$Type): $EffectsProperties$Mutable
 "setAmbientParticle"(arg0: $AmbientParticleSettings$Type): $EffectsProperties$Mutable
 "getGrassColorOverride"(): $OptionalInt
 "getAmbientMoodSound"(): $Optional<($AmbientMoodSettings)>
 "getFoliageColorOverride"(): $OptionalInt
 "getAmbientAdditionsSound"(): $Optional<($AmbientAdditionsSettings)>
 "getAmbientLoopSound"(): $Optional<($Holder<($SoundEvent)>)>
 "getGrassColorModifier"(): $BiomeSpecialEffects$GrassColorModifier
 "getSkyColor"(): integer
 "getAmbientParticle"(): $Optional<($AmbientParticleSettings)>
 "getWaterFogColor"(): integer
 "getBackgroundMusic"(): $Optional<($Music)>
 "getWaterColor"(): integer
 "getFogColor"(): integer
}

export namespace $EffectsProperties$Mutable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EffectsProperties$Mutable$Type = ($EffectsProperties$Mutable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EffectsProperties$Mutable_ = $EffectsProperties$Mutable$Type;
}}
declare module "packages/dev/architectury/platform/$Mod" {
import {$Mod$ConfigurationScreenProvider, $Mod$ConfigurationScreenProvider$Type} from "packages/dev/architectury/platform/$Mod$ConfigurationScreenProvider"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export interface $Mod {

 "getName"(): string
 "findResource"(...arg0: (string)[]): $Optional<($Path)>
 "getVersion"(): string
 "getDescription"(): string
/**
 * 
 * @deprecated
 */
 "getFilePath"(): $Path
 "getHomepage"(): $Optional<(string)>
 "getIssueTracker"(): $Optional<(string)>
 "getFilePaths"(): $List<($Path)>
 "getLicense"(): $Collection<(string)>
 "registerConfigurationScreen"(arg0: $Mod$ConfigurationScreenProvider$Type): void
 "getSources"(): $Optional<(string)>
 "getAuthors"(): $Collection<(string)>
 "getModId"(): string
 "getLogoFile"(arg0: integer): $Optional<(string)>
}

export namespace $Mod {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Mod$Type = ($Mod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Mod_ = $Mod$Type;
}}
declare module "packages/dev/architectury/registry/client/keymappings/$KeyMappingRegistry" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"

export class $KeyMappingRegistry {


public static "register"(mapping: $KeyMapping$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyMappingRegistry$Type = ($KeyMappingRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyMappingRegistry_ = $KeyMappingRegistry$Type;
}}
declare module "packages/dev/architectury/hooks/item/food/$FoodPropertiesHooks" {
import {$FoodProperties$Builder, $FoodProperties$Builder$Type} from "packages/net/minecraft/world/food/$FoodProperties$Builder"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $FoodPropertiesHooks {


public static "effect"(builder: $FoodProperties$Builder$Type, effectSupplier: $Supplier$Type<(any)>, chance: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoodPropertiesHooks$Type = ($FoodPropertiesHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoodPropertiesHooks_ = $FoodPropertiesHooks$Type;
}}
declare module "packages/dev/architectury/registry/forge/$CreativeTabRegistryImpl" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$DeferredSupplier, $DeferredSupplier$Type} from "packages/dev/architectury/registry/registries/$DeferredSupplier"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CreativeTabRegistry$ModifyTabCallback, $CreativeTabRegistry$ModifyTabCallback$Type} from "packages/dev/architectury/registry/$CreativeTabRegistry$ModifyTabCallback"
import {$BuildCreativeModeTabContentsEvent, $BuildCreativeModeTabContentsEvent$Type} from "packages/net/minecraftforge/event/$BuildCreativeModeTabContentsEvent"
import {$CreativeModeTab$Builder, $CreativeModeTab$Builder$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$Builder"

export class $CreativeTabRegistryImpl {

constructor()

public static "event"(event: $BuildCreativeModeTabContentsEvent$Type): void
public static "create"(callback: $Consumer$Type<($CreativeModeTab$Builder$Type)>): $CreativeModeTab
public static "defer"(name: $ResourceLocation$Type): $DeferredSupplier<($CreativeModeTab)>
public static "modify"(tab: $DeferredSupplier$Type<($CreativeModeTab$Type)>, filler: $CreativeTabRegistry$ModifyTabCallback$Type): void
public static "ofBuiltin"(tab: $CreativeModeTab$Type): $DeferredSupplier<($CreativeModeTab)>
public static "appendStack"(tab: $DeferredSupplier$Type<($CreativeModeTab$Type)>, item: $Supplier$Type<($ItemStack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CreativeTabRegistryImpl$Type = ($CreativeTabRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CreativeTabRegistryImpl_ = $CreativeTabRegistryImpl$Type;
}}
declare module "packages/dev/architectury/hooks/level/biome/$GenerationProperties$Mutable" {
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$GenerationStep$Decoration, $GenerationStep$Decoration$Type} from "packages/net/minecraft/world/level/levelgen/$GenerationStep$Decoration"
import {$PlacedFeature, $PlacedFeature$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacedFeature"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$GenerationStep$Carving, $GenerationStep$Carving$Type} from "packages/net/minecraft/world/level/levelgen/$GenerationStep$Carving"
import {$ConfiguredWorldCarver, $ConfiguredWorldCarver$Type} from "packages/net/minecraft/world/level/levelgen/carver/$ConfiguredWorldCarver"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$GenerationProperties, $GenerationProperties$Type} from "packages/dev/architectury/hooks/level/biome/$GenerationProperties"

export interface $GenerationProperties$Mutable extends $GenerationProperties {

 "removeFeature"(arg0: $GenerationStep$Decoration$Type, arg1: $ResourceKey$Type<($PlacedFeature$Type)>): $GenerationProperties$Mutable
 "addFeature"(arg0: $GenerationStep$Decoration$Type, arg1: $Holder$Type<($PlacedFeature$Type)>): $GenerationProperties$Mutable
 "addFeature"(arg0: $GenerationStep$Decoration$Type, arg1: $ResourceKey$Type<($PlacedFeature$Type)>): $GenerationProperties$Mutable
 "addCarver"(arg0: $GenerationStep$Carving$Type, arg1: $ResourceKey$Type<($ConfiguredWorldCarver$Type<(any)>)>): $GenerationProperties$Mutable
 "addCarver"(arg0: $GenerationStep$Carving$Type, arg1: $Holder$Type<($ConfiguredWorldCarver$Type<(any)>)>): $GenerationProperties$Mutable
 "removeCarver"(arg0: $GenerationStep$Carving$Type, arg1: $ResourceKey$Type<($ConfiguredWorldCarver$Type<(any)>)>): $GenerationProperties$Mutable
 "getFeatures"(arg0: $GenerationStep$Decoration$Type): $Iterable<($Holder<($PlacedFeature)>)>
 "getFeatures"(): $List<($Iterable<($Holder<($PlacedFeature)>)>)>
 "getCarvers"(arg0: $GenerationStep$Carving$Type): $Iterable<($Holder<($ConfiguredWorldCarver<(any)>)>)>
}

export namespace $GenerationProperties$Mutable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenerationProperties$Mutable$Type = ($GenerationProperties$Mutable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenerationProperties$Mutable_ = $GenerationProperties$Mutable$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientCommandRegistrationEvent" {
import {$ClientCommandRegistrationEvent$ClientCommandSourceStack, $ClientCommandRegistrationEvent$ClientCommandSourceStack$Type} from "packages/dev/architectury/event/events/client/$ClientCommandRegistrationEvent$ClientCommandSourceStack"
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$CommandBuildContext, $CommandBuildContext$Type} from "packages/net/minecraft/commands/$CommandBuildContext"
import {$ArgumentType, $ArgumentType$Type} from "packages/com/mojang/brigadier/arguments/$ArgumentType"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"
import {$RequiredArgumentBuilder, $RequiredArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$RequiredArgumentBuilder"

export interface $ClientCommandRegistrationEvent {

 "register"(arg0: $CommandDispatcher$Type<($ClientCommandRegistrationEvent$ClientCommandSourceStack$Type)>, arg1: $CommandBuildContext$Type): void

(arg0: $CommandDispatcher$Type<($ClientCommandRegistrationEvent$ClientCommandSourceStack$Type)>, arg1: $CommandBuildContext$Type): void
}

export namespace $ClientCommandRegistrationEvent {
const EVENT: $Event<($ClientCommandRegistrationEvent)>
function argument<T>(name: string, type: $ArgumentType$Type<(T)>): $RequiredArgumentBuilder<($ClientCommandRegistrationEvent$ClientCommandSourceStack), (T)>
function literal(name: string): $LiteralArgumentBuilder<($ClientCommandRegistrationEvent$ClientCommandSourceStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientCommandRegistrationEvent$Type = ($ClientCommandRegistrationEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientCommandRegistrationEvent_ = $ClientCommandRegistrationEvent$Type;
}}
declare module "packages/dev/architectury/impl/$ItemPropertiesExtensionImpl" {
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$DeferredSupplier, $DeferredSupplier$Type} from "packages/dev/architectury/registry/registries/$DeferredSupplier"

export interface $ItemPropertiesExtensionImpl {

 "arch$getTabSupplier"(): $DeferredSupplier<($CreativeModeTab)>
 "arch$getTab"(): $CreativeModeTab
}

export namespace $ItemPropertiesExtensionImpl {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemPropertiesExtensionImpl$Type = ($ItemPropertiesExtensionImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemPropertiesExtensionImpl_ = $ItemPropertiesExtensionImpl$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientTooltipEvent$Render" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $ClientTooltipEvent$Render {

 "renderTooltip"(arg0: $GuiGraphics$Type, arg1: $List$Type<(any)>, arg2: integer, arg3: integer): $EventResult

(arg0: $GuiGraphics$Type, arg1: $List$Type<(any)>, arg2: integer, arg3: integer): $EventResult
}

export namespace $ClientTooltipEvent$Render {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTooltipEvent$Render$Type = ($ClientTooltipEvent$Render);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTooltipEvent$Render_ = $ClientTooltipEvent$Render$Type;
}}
declare module "packages/dev/architectury/event/events/common/$PlayerEvent$PlayerRespawn" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"

export interface $PlayerEvent$PlayerRespawn {

 "respawn"(arg0: $ServerPlayer$Type, arg1: boolean): void

(arg0: $ServerPlayer$Type, arg1: boolean): void
}

export namespace $PlayerEvent$PlayerRespawn {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvent$PlayerRespawn$Type = ($PlayerEvent$PlayerRespawn);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvent$PlayerRespawn_ = $PlayerEvent$PlayerRespawn$Type;
}}
declare module "packages/dev/architectury/hooks/level/biome/$BiomeProperties" {
import {$SpawnProperties, $SpawnProperties$Type} from "packages/dev/architectury/hooks/level/biome/$SpawnProperties"
import {$ClimateProperties, $ClimateProperties$Type} from "packages/dev/architectury/hooks/level/biome/$ClimateProperties"
import {$EffectsProperties, $EffectsProperties$Type} from "packages/dev/architectury/hooks/level/biome/$EffectsProperties"
import {$GenerationProperties, $GenerationProperties$Type} from "packages/dev/architectury/hooks/level/biome/$GenerationProperties"

export interface $BiomeProperties {

 "getSpawnProperties"(): $SpawnProperties
 "getClimateProperties"(): $ClimateProperties
 "getEffectsProperties"(): $EffectsProperties
 "getGenerationProperties"(): $GenerationProperties
}

export namespace $BiomeProperties {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeProperties$Type = ($BiomeProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeProperties_ = $BiomeProperties$Type;
}}
declare module "packages/dev/architectury/extensions/injected/$InjectedBlockExtension" {
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$InjectedRegistryEntryExtension, $InjectedRegistryEntryExtension$Type} from "packages/dev/architectury/extensions/injected/$InjectedRegistryEntryExtension"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export interface $InjectedBlockExtension extends $InjectedRegistryEntryExtension<($Block)> {

 "arch$holder"(): $Holder<($Block)>
 "arch$registryName"(): $ResourceLocation
}

export namespace $InjectedBlockExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectedBlockExtension$Type = ($InjectedBlockExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectedBlockExtension_ = $InjectedBlockExtension$Type;
}}
declare module "packages/dev/architectury/event/events/common/$EntityEvent$LivingHurt" {
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $EntityEvent$LivingHurt {

 "hurt"(arg0: $LivingEntity$Type, arg1: $DamageSource$Type, arg2: float): $EventResult

(arg0: $LivingEntity$Type, arg1: $DamageSource$Type, arg2: float): $EventResult
}

export namespace $EntityEvent$LivingHurt {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityEvent$LivingHurt$Type = ($EntityEvent$LivingHurt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityEvent$LivingHurt_ = $EntityEvent$LivingHurt$Type;
}}
declare module "packages/dev/architectury/event/events/common/$LootEvent" {
import {$LootEvent$ModifyLootTable, $LootEvent$ModifyLootTable$Type} from "packages/dev/architectury/event/events/common/$LootEvent$ModifyLootTable"
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"

export interface $LootEvent {

}

export namespace $LootEvent {
const MODIFY_LOOT_TABLE: $Event<($LootEvent$ModifyLootTable)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootEvent$Type = ($LootEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootEvent_ = $LootEvent$Type;
}}
declare module "packages/dev/architectury/registry/level/biome/forge/$BiomeModificationsImpl" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$BiomeProperties$Mutable, $BiomeProperties$Mutable$Type} from "packages/dev/architectury/hooks/level/biome/$BiomeProperties$Mutable"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BiomeModifications$BiomeContext, $BiomeModifications$BiomeContext$Type} from "packages/dev/architectury/registry/level/biome/$BiomeModifications$BiomeContext"

export class $BiomeModificationsImpl {

constructor()

public static "init"(): void
public static "addProperties"(predicate: $Predicate$Type<($BiomeModifications$BiomeContext$Type)>, modifier: $BiConsumer$Type<($BiomeModifications$BiomeContext$Type), ($BiomeProperties$Mutable$Type)>): void
public static "removeProperties"(predicate: $Predicate$Type<($BiomeModifications$BiomeContext$Type)>, modifier: $BiConsumer$Type<($BiomeModifications$BiomeContext$Type), ($BiomeProperties$Mutable$Type)>): void
public static "replaceProperties"(predicate: $Predicate$Type<($BiomeModifications$BiomeContext$Type)>, modifier: $BiConsumer$Type<($BiomeModifications$BiomeContext$Type), ($BiomeProperties$Mutable$Type)>): void
public static "postProcessProperties"(predicate: $Predicate$Type<($BiomeModifications$BiomeContext$Type)>, modifier: $BiConsumer$Type<($BiomeModifications$BiomeContext$Type), ($BiomeProperties$Mutable$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeModificationsImpl$Type = ($BiomeModificationsImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeModificationsImpl_ = $BiomeModificationsImpl$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientGuiEvent$ScreenInitPre" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"
import {$ScreenAccess, $ScreenAccess$Type} from "packages/dev/architectury/hooks/client/screen/$ScreenAccess"

export interface $ClientGuiEvent$ScreenInitPre {

 "init"(arg0: $Screen$Type, arg1: $ScreenAccess$Type): $EventResult

(arg0: $Screen$Type, arg1: $ScreenAccess$Type): $EventResult
}

export namespace $ClientGuiEvent$ScreenInitPre {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientGuiEvent$ScreenInitPre$Type = ($ClientGuiEvent$ScreenInitPre);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientGuiEvent$ScreenInitPre_ = $ClientGuiEvent$ScreenInitPre$Type;
}}
declare module "packages/dev/architectury/utils/value/$FloatValue" {
import {$FloatConsumer, $FloatConsumer$Type} from "packages/it/unimi/dsi/fastutil/floats/$FloatConsumer"
import {$FloatSupplier, $FloatSupplier$Type} from "packages/dev/architectury/utils/value/$FloatSupplier"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Value, $Value$Type} from "packages/dev/architectury/utils/value/$Value"

export interface $FloatValue extends $Value<(float)>, $FloatSupplier, $FloatConsumer {

 "get"(): float
 "getAsFloat"(): float
/**
 * 
 * @deprecated
 */
 "accept"(arg0: float): void
/**
 * 
 * @deprecated
 */
 "accept"(arg0: double): void
 "accept"(arg0: float): void
/**
 * 
 * @deprecated
 */
 "andThen"(arg0: $Consumer$Type<(any)>): $Consumer<(float)>
 "andThen"(arg0: $FloatConsumer$Type): $FloatConsumer
}

export namespace $FloatValue {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FloatValue$Type = ($FloatValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FloatValue_ = $FloatValue$Type;
}}
declare module "packages/dev/architectury/extensions/network/$EntitySpawnExtension" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$IEntityAdditionalSpawnData, $IEntityAdditionalSpawnData$Type} from "packages/net/minecraftforge/entity/$IEntityAdditionalSpawnData"

export interface $EntitySpawnExtension extends $IEntityAdditionalSpawnData {

 "writeSpawnData"(buf: $FriendlyByteBuf$Type): void
 "readSpawnData"(buf: $FriendlyByteBuf$Type): void
 "saveAdditionalSpawnData"(arg0: $FriendlyByteBuf$Type): void
 "loadAdditionalSpawnData"(arg0: $FriendlyByteBuf$Type): void
}

export namespace $EntitySpawnExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntitySpawnExtension$Type = ($EntitySpawnExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntitySpawnExtension_ = $EntitySpawnExtension$Type;
}}
declare module "packages/dev/architectury/extensions/injected/$InjectedFoodPropertiesBuilderExtension" {
import {$FoodProperties$Builder, $FoodProperties$Builder$Type} from "packages/net/minecraft/world/food/$FoodProperties$Builder"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export interface $InjectedFoodPropertiesBuilderExtension {

 "arch$effect"(effectSupplier: $Supplier$Type<(any)>, chance: float): $FoodProperties$Builder
}

export namespace $InjectedFoodPropertiesBuilderExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectedFoodPropertiesBuilderExtension$Type = ($InjectedFoodPropertiesBuilderExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectedFoodPropertiesBuilderExtension_ = $InjectedFoodPropertiesBuilderExtension$Type;
}}
declare module "packages/dev/architectury/event/events/common/$InteractionEvent$FarmlandTrample" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $InteractionEvent$FarmlandTrample {

 "trample"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: float, arg4: $Entity$Type): $EventResult

(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: float, arg4: $Entity$Type): $EventResult
}

export namespace $InteractionEvent$FarmlandTrample {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InteractionEvent$FarmlandTrample$Type = ($InteractionEvent$FarmlandTrample);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InteractionEvent$FarmlandTrample_ = $InteractionEvent$FarmlandTrample$Type;
}}
declare module "packages/dev/architectury/event/forge/$EventFactoryImpl" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$EventActor, $EventActor$Type} from "packages/dev/architectury/event/$EventActor"

export class $EventFactoryImpl {

constructor()

public static "attachToForge"<T>(event: $Event$Type<($Consumer$Type<(T)>)>): $Event<($Consumer<(T)>)>
public static "attachToForgeEventActor"<T>(event: $Event$Type<($EventActor$Type<(T)>)>): $Event<($EventActor<(T)>)>
public static "attachToForgeEventActorCancellable"<T>(event: $Event$Type<($EventActor$Type<(T)>)>): $Event<($EventActor<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventFactoryImpl$Type = ($EventFactoryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventFactoryImpl_ = $EventFactoryImpl$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientLifecycleEvent$ClientState" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$LifecycleEvent$InstanceState, $LifecycleEvent$InstanceState$Type} from "packages/dev/architectury/event/events/common/$LifecycleEvent$InstanceState"

export interface $ClientLifecycleEvent$ClientState extends $LifecycleEvent$InstanceState<($Minecraft)> {

 "stateChanged"(arg0: $Minecraft$Type): void

(arg0: $Minecraft$Type): void
}

export namespace $ClientLifecycleEvent$ClientState {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientLifecycleEvent$ClientState$Type = ($ClientLifecycleEvent$ClientState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientLifecycleEvent$ClientState_ = $ClientLifecycleEvent$ClientState$Type;
}}
declare module "packages/dev/architectury/networking/simple/$SimpleNetworkManager" {
import {$MessageDecoder, $MessageDecoder$Type} from "packages/dev/architectury/networking/simple/$MessageDecoder"
import {$PacketTransformer, $PacketTransformer$Type} from "packages/dev/architectury/networking/transformers/$PacketTransformer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BaseS2CMessage, $BaseS2CMessage$Type} from "packages/dev/architectury/networking/simple/$BaseS2CMessage"
import {$BaseC2SMessage, $BaseC2SMessage$Type} from "packages/dev/architectury/networking/simple/$BaseC2SMessage"
import {$MessageType, $MessageType$Type} from "packages/dev/architectury/networking/simple/$MessageType"

export class $SimpleNetworkManager {
readonly "namespace": string


public static "create"(namespace: string): $SimpleNetworkManager
public "registerC2S"(id: string, decoder: $MessageDecoder$Type<($BaseC2SMessage$Type)>, transformers: $List$Type<($PacketTransformer$Type)>): $MessageType
public "registerC2S"(id: string, decoder: $MessageDecoder$Type<($BaseC2SMessage$Type)>): $MessageType
public "registerS2C"(id: string, decoder: $MessageDecoder$Type<($BaseS2CMessage$Type)>): $MessageType
public "registerS2C"(id: string, decoder: $MessageDecoder$Type<($BaseS2CMessage$Type)>, transformers: $List$Type<($PacketTransformer$Type)>): $MessageType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleNetworkManager$Type = ($SimpleNetworkManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleNetworkManager_ = $SimpleNetworkManager$Type;
}}
declare module "packages/dev/architectury/event/events/common/$ChunkEvent" {
import {$ChunkEvent$LoadData, $ChunkEvent$LoadData$Type} from "packages/dev/architectury/event/events/common/$ChunkEvent$LoadData"
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$ChunkEvent$SaveData, $ChunkEvent$SaveData$Type} from "packages/dev/architectury/event/events/common/$ChunkEvent$SaveData"

export interface $ChunkEvent {

}

export namespace $ChunkEvent {
const SAVE_DATA: $Event<($ChunkEvent$SaveData)>
const LOAD_DATA: $Event<($ChunkEvent$LoadData)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkEvent$Type = ($ChunkEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkEvent_ = $ChunkEvent$Type;
}}
declare module "packages/dev/architectury/networking/transformers/$SinglePacketCollector" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$PacketSink, $PacketSink$Type} from "packages/dev/architectury/networking/transformers/$PacketSink"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export class $SinglePacketCollector implements $PacketSink {

constructor(consumer: $Consumer$Type<($Packet$Type<(any)>)>)

public "accept"(packet: $Packet$Type<(any)>): void
public "getPacket"(): $Packet<(any)>
public static "client"(): $PacketSink
public static "ofPlayer"(player: $ServerPlayer$Type): $PacketSink
public static "ofPlayers"(players: $Iterable$Type<(any)>): $PacketSink
get "packet"(): $Packet<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SinglePacketCollector$Type = ($SinglePacketCollector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SinglePacketCollector_ = $SinglePacketCollector$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientLifecycleEvent$ClientLevelState" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"
import {$LifecycleEvent$LevelState, $LifecycleEvent$LevelState$Type} from "packages/dev/architectury/event/events/common/$LifecycleEvent$LevelState"

export interface $ClientLifecycleEvent$ClientLevelState extends $LifecycleEvent$LevelState<($ClientLevel)> {

 "act"(arg0: $ClientLevel$Type): void

(arg0: $ClientLevel$Type): void
}

export namespace $ClientLifecycleEvent$ClientLevelState {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientLifecycleEvent$ClientLevelState$Type = ($ClientLifecycleEvent$ClientLevelState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientLifecycleEvent$ClientLevelState_ = $ClientLifecycleEvent$ClientLevelState$Type;
}}
declare module "packages/dev/architectury/core/item/$ArchitecturyRecordItem" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$RegistrySupplier, $RegistrySupplier$Type} from "packages/dev/architectury/registry/registries/$RegistrySupplier"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$RecordItem, $RecordItem$Type} from "packages/net/minecraft/world/item/$RecordItem"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ArchitecturyRecordItem extends $RecordItem {
/**
 * 
 * @deprecated
 */
static readonly "BY_NAME": $Map<($SoundEvent), ($RecordItem)>
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(analogOutput: integer, sound: $RegistrySupplier$Type<($SoundEvent$Type)>, properties: $Item$Properties$Type, lengthInSeconds: integer)

public "getSound"(): $SoundEvent
get "sound"(): $SoundEvent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArchitecturyRecordItem$Type = ($ArchitecturyRecordItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArchitecturyRecordItem_ = $ArchitecturyRecordItem$Type;
}}
declare module "packages/dev/architectury/networking/$NetworkManager$NetworkReceiver" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$NetworkManager$PacketContext, $NetworkManager$PacketContext$Type} from "packages/dev/architectury/networking/$NetworkManager$PacketContext"

export interface $NetworkManager$NetworkReceiver {

 "receive"(arg0: $FriendlyByteBuf$Type, arg1: $NetworkManager$PacketContext$Type): void

(arg0: $FriendlyByteBuf$Type, arg1: $NetworkManager$PacketContext$Type): void
}

export namespace $NetworkManager$NetworkReceiver {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkManager$NetworkReceiver$Type = ($NetworkManager$NetworkReceiver);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkManager$NetworkReceiver_ = $NetworkManager$NetworkReceiver$Type;
}}
declare module "packages/dev/architectury/platform/forge/$EventBuses" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export class $EventBuses {


public static "getModEventBus"(modId: string): $Optional<($IEventBus)>
public static "onRegistered"(modId: string, busConsumer: $Consumer$Type<($IEventBus$Type)>): void
public static "registerModEventBus"(modId: string, bus: $IEventBus$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventBuses$Type = ($EventBuses);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventBuses_ = $EventBuses$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientScreenInputEvent" {
import {$ClientScreenInputEvent$MouseScrolled, $ClientScreenInputEvent$MouseScrolled$Type} from "packages/dev/architectury/event/events/client/$ClientScreenInputEvent$MouseScrolled"
import {$ClientScreenInputEvent$MouseClicked, $ClientScreenInputEvent$MouseClicked$Type} from "packages/dev/architectury/event/events/client/$ClientScreenInputEvent$MouseClicked"
import {$ClientScreenInputEvent$KeyPressed, $ClientScreenInputEvent$KeyPressed$Type} from "packages/dev/architectury/event/events/client/$ClientScreenInputEvent$KeyPressed"
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$ClientScreenInputEvent$MouseReleased, $ClientScreenInputEvent$MouseReleased$Type} from "packages/dev/architectury/event/events/client/$ClientScreenInputEvent$MouseReleased"
import {$ClientScreenInputEvent$MouseDragged, $ClientScreenInputEvent$MouseDragged$Type} from "packages/dev/architectury/event/events/client/$ClientScreenInputEvent$MouseDragged"
import {$ClientScreenInputEvent$KeyTyped, $ClientScreenInputEvent$KeyTyped$Type} from "packages/dev/architectury/event/events/client/$ClientScreenInputEvent$KeyTyped"
import {$ClientScreenInputEvent$KeyReleased, $ClientScreenInputEvent$KeyReleased$Type} from "packages/dev/architectury/event/events/client/$ClientScreenInputEvent$KeyReleased"

export interface $ClientScreenInputEvent {

}

export namespace $ClientScreenInputEvent {
const MOUSE_SCROLLED_PRE: $Event<($ClientScreenInputEvent$MouseScrolled)>
const MOUSE_SCROLLED_POST: $Event<($ClientScreenInputEvent$MouseScrolled)>
const MOUSE_CLICKED_PRE: $Event<($ClientScreenInputEvent$MouseClicked)>
const MOUSE_CLICKED_POST: $Event<($ClientScreenInputEvent$MouseClicked)>
const MOUSE_RELEASED_PRE: $Event<($ClientScreenInputEvent$MouseReleased)>
const MOUSE_RELEASED_POST: $Event<($ClientScreenInputEvent$MouseReleased)>
const MOUSE_DRAGGED_PRE: $Event<($ClientScreenInputEvent$MouseDragged)>
const MOUSE_DRAGGED_POST: $Event<($ClientScreenInputEvent$MouseDragged)>
const CHAR_TYPED_PRE: $Event<($ClientScreenInputEvent$KeyTyped)>
const CHAR_TYPED_POST: $Event<($ClientScreenInputEvent$KeyTyped)>
const KEY_PRESSED_PRE: $Event<($ClientScreenInputEvent$KeyPressed)>
const KEY_PRESSED_POST: $Event<($ClientScreenInputEvent$KeyPressed)>
const KEY_RELEASED_PRE: $Event<($ClientScreenInputEvent$KeyReleased)>
const KEY_RELEASED_POST: $Event<($ClientScreenInputEvent$KeyReleased)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientScreenInputEvent$Type = ($ClientScreenInputEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientScreenInputEvent_ = $ClientScreenInputEvent$Type;
}}
declare module "packages/dev/architectury/hooks/item/forge/$ItemStackHooksImpl" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $ItemStackHooksImpl {

constructor()

public static "hasCraftingRemainingItem"(stack: $ItemStack$Type): boolean
public static "getCraftingRemainingItem"(stack: $ItemStack$Type): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemStackHooksImpl$Type = ($ItemStackHooksImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemStackHooksImpl_ = $ItemStackHooksImpl$Type;
}}
declare module "packages/dev/architectury/event/events/common/$InteractionEvent" {
import {$InteractionEvent$ClientRightClickAir, $InteractionEvent$ClientRightClickAir$Type} from "packages/dev/architectury/event/events/common/$InteractionEvent$ClientRightClickAir"
import {$InteractionEvent$LeftClickBlock, $InteractionEvent$LeftClickBlock$Type} from "packages/dev/architectury/event/events/common/$InteractionEvent$LeftClickBlock"
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$InteractionEvent$RightClickBlock, $InteractionEvent$RightClickBlock$Type} from "packages/dev/architectury/event/events/common/$InteractionEvent$RightClickBlock"
import {$InteractionEvent$RightClickItem, $InteractionEvent$RightClickItem$Type} from "packages/dev/architectury/event/events/common/$InteractionEvent$RightClickItem"
import {$InteractionEvent$InteractEntity, $InteractionEvent$InteractEntity$Type} from "packages/dev/architectury/event/events/common/$InteractionEvent$InteractEntity"
import {$InteractionEvent$ClientLeftClickAir, $InteractionEvent$ClientLeftClickAir$Type} from "packages/dev/architectury/event/events/common/$InteractionEvent$ClientLeftClickAir"
import {$InteractionEvent$FarmlandTrample, $InteractionEvent$FarmlandTrample$Type} from "packages/dev/architectury/event/events/common/$InteractionEvent$FarmlandTrample"

export interface $InteractionEvent {

}

export namespace $InteractionEvent {
const LEFT_CLICK_BLOCK: $Event<($InteractionEvent$LeftClickBlock)>
const RIGHT_CLICK_BLOCK: $Event<($InteractionEvent$RightClickBlock)>
const RIGHT_CLICK_ITEM: $Event<($InteractionEvent$RightClickItem)>
const CLIENT_LEFT_CLICK_AIR: $Event<($InteractionEvent$ClientLeftClickAir)>
const CLIENT_RIGHT_CLICK_AIR: $Event<($InteractionEvent$ClientRightClickAir)>
const INTERACT_ENTITY: $Event<($InteractionEvent$InteractEntity)>
const FARMLAND_TRAMPLE: $Event<($InteractionEvent$FarmlandTrample)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InteractionEvent$Type = ($InteractionEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InteractionEvent_ = $InteractionEvent$Type;
}}
declare module "packages/dev/architectury/registry/level/entity/trade/$SimpleTrade" {
import {$MerchantOffer, $MerchantOffer$Type} from "packages/net/minecraft/world/item/trading/$MerchantOffer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$VillagerTrades$ItemListing, $VillagerTrades$ItemListing$Type} from "packages/net/minecraft/world/entity/npc/$VillagerTrades$ItemListing"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $SimpleTrade extends $Record implements $VillagerTrades$ItemListing {

constructor(primaryPrice: $ItemStack$Type, secondaryPrice: $ItemStack$Type, sale: $ItemStack$Type, maxTrades: integer, experiencePoints: integer, priceMultiplier: float)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "maxTrades"(): integer
public "getOffer"(entity: $Entity$Type, random: $RandomSource$Type): $MerchantOffer
public "priceMultiplier"(): float
public "sale"(): $ItemStack
public "secondaryPrice"(): $ItemStack
public "primaryPrice"(): $ItemStack
public "experiencePoints"(): integer
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
declare module "packages/dev/architectury/utils/$EnvExecutor" {
import {$Env, $Env$Type} from "packages/dev/architectury/utils/$Env"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$Dist, $Dist$Type} from "packages/net/minecraftforge/api/distmarker/$Dist"

export class $EnvExecutor {


public static "getEnvSpecific"<T>(client: $Supplier$Type<($Supplier$Type<(T)>)>, server: $Supplier$Type<($Supplier$Type<(T)>)>): T
public static "runInEnv"(type: $Dist$Type, runnableSupplier: $Supplier$Type<($Runnable$Type)>): void
public static "runInEnv"(type: $Env$Type, runnableSupplier: $Supplier$Type<($Runnable$Type)>): void
public static "getInEnv"<T>(type: $Dist$Type, runnableSupplier: $Supplier$Type<($Supplier$Type<(T)>)>): $Optional<(T)>
public static "getInEnv"<T>(type: $Env$Type, runnableSupplier: $Supplier$Type<($Supplier$Type<(T)>)>): $Optional<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnvExecutor$Type = ($EnvExecutor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnvExecutor_ = $EnvExecutor$Type;
}}
declare module "packages/dev/architectury/registry/menu/forge/$MenuRegistryImpl" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$MenuRegistry$ExtendedMenuTypeFactory, $MenuRegistry$ExtendedMenuTypeFactory$Type} from "packages/dev/architectury/registry/menu/$MenuRegistry$ExtendedMenuTypeFactory"
import {$MenuRegistry$ScreenFactory, $MenuRegistry$ScreenFactory$Type} from "packages/dev/architectury/registry/menu/$MenuRegistry$ScreenFactory"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$MenuRegistry$SimpleMenuTypeFactory, $MenuRegistry$SimpleMenuTypeFactory$Type} from "packages/dev/architectury/registry/menu/$MenuRegistry$SimpleMenuTypeFactory"
import {$MenuAccess, $MenuAccess$Type} from "packages/net/minecraft/client/gui/screens/inventory/$MenuAccess"
import {$ExtendedMenuProvider, $ExtendedMenuProvider$Type} from "packages/dev/architectury/registry/menu/$ExtendedMenuProvider"

export class $MenuRegistryImpl {

constructor()

public static "of"<T extends $AbstractContainerMenu>(factory: $MenuRegistry$SimpleMenuTypeFactory$Type<(T)>): $MenuType<(T)>
public static "ofExtended"<T extends $AbstractContainerMenu>(factory: $MenuRegistry$ExtendedMenuTypeFactory$Type<(T)>): $MenuType<(T)>
public static "openExtendedMenu"(player: $ServerPlayer$Type, provider: $ExtendedMenuProvider$Type): void
public static "registerScreenFactory"<H extends $AbstractContainerMenu, S extends ($Screen) & ($MenuAccess<(H)>)>(type: $MenuType$Type<(any)>, factory: $MenuRegistry$ScreenFactory$Type<(H), (S)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuRegistryImpl$Type = ($MenuRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuRegistryImpl_ = $MenuRegistryImpl$Type;
}}
declare module "packages/dev/architectury/hooks/fluid/$FluidBucketHooks" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$BucketItem, $BucketItem$Type} from "packages/net/minecraft/world/item/$BucketItem"

export class $FluidBucketHooks {

constructor()

public static "getFluid"(item: $BucketItem$Type): $Fluid
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidBucketHooks$Type = ($FluidBucketHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidBucketHooks_ = $FluidBucketHooks$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientPlayerEvent" {
import {$ClientPlayerEvent$ClientPlayerJoin, $ClientPlayerEvent$ClientPlayerJoin$Type} from "packages/dev/architectury/event/events/client/$ClientPlayerEvent$ClientPlayerJoin"
import {$ClientPlayerEvent$ClientPlayerQuit, $ClientPlayerEvent$ClientPlayerQuit$Type} from "packages/dev/architectury/event/events/client/$ClientPlayerEvent$ClientPlayerQuit"
import {$ClientPlayerEvent$ClientPlayerRespawn, $ClientPlayerEvent$ClientPlayerRespawn$Type} from "packages/dev/architectury/event/events/client/$ClientPlayerEvent$ClientPlayerRespawn"
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"

export interface $ClientPlayerEvent {

}

export namespace $ClientPlayerEvent {
const CLIENT_PLAYER_JOIN: $Event<($ClientPlayerEvent$ClientPlayerJoin)>
const CLIENT_PLAYER_QUIT: $Event<($ClientPlayerEvent$ClientPlayerQuit)>
const CLIENT_PLAYER_RESPAWN: $Event<($ClientPlayerEvent$ClientPlayerRespawn)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientPlayerEvent$Type = ($ClientPlayerEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientPlayerEvent_ = $ClientPlayerEvent$Type;
}}
declare module "packages/dev/architectury/registry/menu/$MenuRegistry" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$MenuRegistry$ExtendedMenuTypeFactory, $MenuRegistry$ExtendedMenuTypeFactory$Type} from "packages/dev/architectury/registry/menu/$MenuRegistry$ExtendedMenuTypeFactory"
import {$MenuRegistry$ScreenFactory, $MenuRegistry$ScreenFactory$Type} from "packages/dev/architectury/registry/menu/$MenuRegistry$ScreenFactory"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$MenuRegistry$SimpleMenuTypeFactory, $MenuRegistry$SimpleMenuTypeFactory$Type} from "packages/dev/architectury/registry/menu/$MenuRegistry$SimpleMenuTypeFactory"
import {$MenuAccess, $MenuAccess$Type} from "packages/net/minecraft/client/gui/screens/inventory/$MenuAccess"
import {$ExtendedMenuProvider, $ExtendedMenuProvider$Type} from "packages/dev/architectury/registry/menu/$ExtendedMenuProvider"

export class $MenuRegistry {


/**
 * 
 * @deprecated
 */
public static "of"<T extends $AbstractContainerMenu>(factory: $MenuRegistry$SimpleMenuTypeFactory$Type<(T)>): $MenuType<(T)>
public static "openMenu"(player: $ServerPlayer$Type, provider: $MenuProvider$Type): void
public static "ofExtended"<T extends $AbstractContainerMenu>(factory: $MenuRegistry$ExtendedMenuTypeFactory$Type<(T)>): $MenuType<(T)>
public static "openExtendedMenu"(player: $ServerPlayer$Type, provider: $ExtendedMenuProvider$Type): void
public static "openExtendedMenu"(player: $ServerPlayer$Type, provider: $MenuProvider$Type, bufWriter: $Consumer$Type<($FriendlyByteBuf$Type)>): void
public static "registerScreenFactory"<H extends $AbstractContainerMenu, S extends ($Screen) & ($MenuAccess<(H)>)>(type: $MenuType$Type<(any)>, factory: $MenuRegistry$ScreenFactory$Type<(H), (S)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuRegistry$Type = ($MenuRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuRegistry_ = $MenuRegistry$Type;
}}
declare module "packages/dev/architectury/event/events/common/$TickEvent$Player" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$TickEvent, $TickEvent$Type} from "packages/dev/architectury/event/events/common/$TickEvent"

export interface $TickEvent$Player extends $TickEvent<($Player)> {

 "tick"(arg0: $Player$Type): void

(arg0: $Player$Type): void
}

export namespace $TickEvent$Player {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickEvent$Player$Type = ($TickEvent$Player);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickEvent$Player_ = $TickEvent$Player$Type;
}}
declare module "packages/dev/architectury/registry/client/rendering/forge/$RenderTypeRegistryImpl" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export class $RenderTypeRegistryImpl {

constructor()

public static "register"(type: $RenderType$Type, ...blocks: ($Block$Type)[]): void
public static "register"(type: $RenderType$Type, ...fluids: ($Fluid$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderTypeRegistryImpl$Type = ($RenderTypeRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderTypeRegistryImpl_ = $RenderTypeRegistryImpl$Type;
}}
declare module "packages/dev/architectury/event/events/common/$ExplosionEvent$Detonate" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Explosion, $Explosion$Type} from "packages/net/minecraft/world/level/$Explosion"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $ExplosionEvent$Detonate {

 "explode"(arg0: $Level$Type, arg1: $Explosion$Type, arg2: $List$Type<($Entity$Type)>): void

(arg0: $Level$Type, arg1: $Explosion$Type, arg2: $List$Type<($Entity$Type)>): void
}

export namespace $ExplosionEvent$Detonate {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExplosionEvent$Detonate$Type = ($ExplosionEvent$Detonate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExplosionEvent$Detonate_ = $ExplosionEvent$Detonate$Type;
}}
declare module "packages/dev/architectury/networking/simple/$BaseS2CMessage" {
import {$LevelChunk, $LevelChunk$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunk"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Message, $Message$Type} from "packages/dev/architectury/networking/simple/$Message"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export class $BaseS2CMessage extends $Message {

constructor()

public "sendToLevel"(level: $ServerLevel$Type): void
public "sendToChunkListeners"(chunk: $LevelChunk$Type): void
public "sendToAll"(server: $MinecraftServer$Type): void
public "sendTo"(players: $Iterable$Type<($ServerPlayer$Type)>): void
public "sendTo"(player: $ServerPlayer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseS2CMessage$Type = ($BaseS2CMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseS2CMessage_ = $BaseS2CMessage$Type;
}}
declare module "packages/dev/architectury/event/events/common/$LifecycleEvent$InstanceState" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LifecycleEvent$InstanceState<T> {

 "stateChanged"(arg0: T): void

(arg0: T): void
}

export namespace $LifecycleEvent$InstanceState {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LifecycleEvent$InstanceState$Type<T> = ($LifecycleEvent$InstanceState<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LifecycleEvent$InstanceState_<T> = $LifecycleEvent$InstanceState$Type<(T)>;
}}
declare module "packages/dev/architectury/event/events/common/$InteractionEvent$ClientLeftClickAir" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"

export interface $InteractionEvent$ClientLeftClickAir {

 "click"(arg0: $Player$Type, arg1: $InteractionHand$Type): void

(arg0: $Player$Type, arg1: $InteractionHand$Type): void
}

export namespace $InteractionEvent$ClientLeftClickAir {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InteractionEvent$ClientLeftClickAir$Type = ($InteractionEvent$ClientLeftClickAir);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InteractionEvent$ClientLeftClickAir_ = $InteractionEvent$ClientLeftClickAir$Type;
}}
declare module "packages/dev/architectury/event/events/common/$InteractionEvent$ClientRightClickAir" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"

export interface $InteractionEvent$ClientRightClickAir {

 "click"(arg0: $Player$Type, arg1: $InteractionHand$Type): void

(arg0: $Player$Type, arg1: $InteractionHand$Type): void
}

export namespace $InteractionEvent$ClientRightClickAir {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InteractionEvent$ClientRightClickAir$Type = ($InteractionEvent$ClientRightClickAir);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InteractionEvent$ClientRightClickAir_ = $InteractionEvent$ClientRightClickAir$Type;
}}
declare module "packages/dev/architectury/networking/$NetworkManager$Side" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $NetworkManager$Side extends $Enum<($NetworkManager$Side)> {
static readonly "S2C": $NetworkManager$Side
static readonly "C2S": $NetworkManager$Side


public static "values"(): ($NetworkManager$Side)[]
public static "valueOf"(name: string): $NetworkManager$Side
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkManager$Side$Type = (("s2c") | ("c2s")) | ($NetworkManager$Side);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkManager$Side_ = $NetworkManager$Side$Type;
}}
declare module "packages/dev/architectury/hooks/fluid/forge/$FluidStackHooksImpl" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$FluidStack, $FluidStack$Type} from "packages/dev/architectury/fluid/$FluidStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"

export class $FluidStackHooksImpl {

constructor()

public static "getName"(stack: $FluidStack$Type): $Component
public static "write"(stack: $FluidStack$Type, buf: $FriendlyByteBuf$Type): void
public static "write"(stack: $FluidStack$Type, tag: $CompoundTag$Type): $CompoundTag
public static "read"(buf: $FriendlyByteBuf$Type): $FluidStack
public static "read"(tag: $CompoundTag$Type): $FluidStack
public static "getFlowingTexture"(stack: $FluidStack$Type): $TextureAtlasSprite
public static "getFlowingTexture"(level: $BlockAndTintGetter$Type, pos: $BlockPos$Type, state: $FluidState$Type): $TextureAtlasSprite
public static "getFlowingTexture"(fluid: $Fluid$Type): $TextureAtlasSprite
public static "bucketAmount"(): long
public static "getLuminosity"(fluid: $FluidStack$Type, level: $Level$Type, pos: $BlockPos$Type): integer
/**
 * 
 * @deprecated
 */
public static "getLuminosity"(fluid: $Fluid$Type, level: $Level$Type, pos: $BlockPos$Type): integer
public static "getStillTexture"(stack: $FluidStack$Type): $TextureAtlasSprite
public static "getStillTexture"(fluid: $Fluid$Type): $TextureAtlasSprite
public static "getStillTexture"(level: $BlockAndTintGetter$Type, pos: $BlockPos$Type, state: $FluidState$Type): $TextureAtlasSprite
public static "getTemperature"(fluid: $FluidStack$Type, level: $Level$Type, pos: $BlockPos$Type): integer
public static "getTemperature"(fluid: $Fluid$Type, level: $Level$Type, pos: $BlockPos$Type): integer
public static "getColor"(level: $BlockAndTintGetter$Type, pos: $BlockPos$Type, state: $FluidState$Type): integer
public static "getColor"(stack: $FluidStack$Type): integer
public static "getColor"(fluid: $Fluid$Type): integer
public static "getViscosity"(fluid: $FluidStack$Type, level: $Level$Type, pos: $BlockPos$Type): integer
public static "getViscosity"(fluid: $Fluid$Type, level: $Level$Type, pos: $BlockPos$Type): integer
public static "getTranslationKey"(stack: $FluidStack$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidStackHooksImpl$Type = ($FluidStackHooksImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidStackHooksImpl_ = $FluidStackHooksImpl$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientPlayerEvent$ClientPlayerQuit" {
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"

export interface $ClientPlayerEvent$ClientPlayerQuit {

 "quit"(arg0: $LocalPlayer$Type): void

(arg0: $LocalPlayer$Type): void
}

export namespace $ClientPlayerEvent$ClientPlayerQuit {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientPlayerEvent$ClientPlayerQuit$Type = ($ClientPlayerEvent$ClientPlayerQuit);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientPlayerEvent$ClientPlayerQuit_ = $ClientPlayerEvent$ClientPlayerQuit$Type;
}}
declare module "packages/dev/architectury/utils/value/$BooleanValue" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$BooleanSupplier, $BooleanSupplier$Type} from "packages/java/util/function/$BooleanSupplier"
import {$Value, $Value$Type} from "packages/dev/architectury/utils/value/$Value"
import {$BooleanConsumer, $BooleanConsumer$Type} from "packages/it/unimi/dsi/fastutil/booleans/$BooleanConsumer"

export interface $BooleanValue extends $Value<(boolean)>, $BooleanSupplier, $BooleanConsumer {

 "get"(): boolean
 "getAsBoolean"(): boolean
/**
 * 
 * @deprecated
 */
 "accept"(arg0: boolean): void
 "accept"(arg0: boolean): void
/**
 * 
 * @deprecated
 */
 "andThen"(arg0: $Consumer$Type<(any)>): $Consumer<(boolean)>
 "andThen"(arg0: $BooleanConsumer$Type): $BooleanConsumer
}

export namespace $BooleanValue {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BooleanValue$Type = ($BooleanValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BooleanValue_ = $BooleanValue$Type;
}}
declare module "packages/dev/architectury/event/events/common/$PlayerEvent$PickupItem" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ItemEntity, $ItemEntity$Type} from "packages/net/minecraft/world/entity/item/$ItemEntity"

export interface $PlayerEvent$PickupItem {

 "pickup"(arg0: $Player$Type, arg1: $ItemEntity$Type, arg2: $ItemStack$Type): void

(arg0: $Player$Type, arg1: $ItemEntity$Type, arg2: $ItemStack$Type): void
}

export namespace $PlayerEvent$PickupItem {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvent$PickupItem$Type = ($PlayerEvent$PickupItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvent$PickupItem_ = $PlayerEvent$PickupItem$Type;
}}
declare module "packages/dev/architectury/networking/transformers/$PacketTransformer$TransformationSink" {
import {$NetworkManager$Side, $NetworkManager$Side$Type} from "packages/dev/architectury/networking/$NetworkManager$Side"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $PacketTransformer$TransformationSink {

 "accept"(arg0: $NetworkManager$Side$Type, arg1: $ResourceLocation$Type, arg2: $FriendlyByteBuf$Type): void

(arg0: $NetworkManager$Side$Type, arg1: $ResourceLocation$Type, arg2: $FriendlyByteBuf$Type): void
}

export namespace $PacketTransformer$TransformationSink {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketTransformer$TransformationSink$Type = ($PacketTransformer$TransformationSink);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketTransformer$TransformationSink_ = $PacketTransformer$TransformationSink$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientScreenInputEvent$MouseDragged" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"

export interface $ClientScreenInputEvent$MouseDragged {

 "mouseDragged"(arg0: $Minecraft$Type, arg1: $Screen$Type, arg2: double, arg3: double, arg4: integer, arg5: double, arg6: double): $EventResult

(arg0: $Minecraft$Type, arg1: $Screen$Type, arg2: double, arg3: double, arg4: integer, arg5: double, arg6: double): $EventResult
}

export namespace $ClientScreenInputEvent$MouseDragged {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientScreenInputEvent$MouseDragged$Type = ($ClientScreenInputEvent$MouseDragged);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientScreenInputEvent$MouseDragged_ = $ClientScreenInputEvent$MouseDragged$Type;
}}
declare module "packages/dev/architectury/hooks/level/forge/$ExplosionHooksImpl" {
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Explosion, $Explosion$Type} from "packages/net/minecraft/world/level/$Explosion"

export class $ExplosionHooksImpl {

constructor()

public static "getPosition"(explosion: $Explosion$Type): $Vec3
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExplosionHooksImpl$Type = ($ExplosionHooksImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExplosionHooksImpl_ = $ExplosionHooksImpl$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientRawInputEvent$MouseClicked" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"

export interface $ClientRawInputEvent$MouseClicked {

 "mouseClicked"(arg0: $Minecraft$Type, arg1: integer, arg2: integer, arg3: integer): $EventResult

(arg0: $Minecraft$Type, arg1: integer, arg2: integer, arg3: integer): $EventResult
}

export namespace $ClientRawInputEvent$MouseClicked {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientRawInputEvent$MouseClicked$Type = ($ClientRawInputEvent$MouseClicked);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientRawInputEvent$MouseClicked_ = $ClientRawInputEvent$MouseClicked$Type;
}}
declare module "packages/dev/architectury/registry/registries/$DeferredSupplier" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$OptionalSupplier, $OptionalSupplier$Type} from "packages/dev/architectury/utils/$OptionalSupplier"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export interface $DeferredSupplier<T> extends $OptionalSupplier<(T)> {

 "getKey"(): $ResourceKey<(T)>
 "getId"(): $ResourceLocation
 "getRegistryId"(): $ResourceLocation
 "getRegistryKey"(): $ResourceKey<($Registry<(T)>)>
 "stream"(): $Stream<(T)>
 "isPresent"(): boolean
 "orElse"(other: T): T
 "ifPresent"(action: $Consumer$Type<(any)>): void
 "ifPresentOrElse"(action: $Consumer$Type<(any)>, emptyAction: $Runnable$Type): void
 "orElseGet"(supplier: $Supplier$Type<(any)>): T
 "toOptional"(): $Optional<(T)>
 "getOrNull"(): T
 "get"(): T
}

export namespace $DeferredSupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeferredSupplier$Type<T> = ($DeferredSupplier<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeferredSupplier_<T> = $DeferredSupplier$Type<(T)>;
}}
declare module "packages/dev/architectury/event/$EventHandler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $EventHandler {


public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventHandler$Type = ($EventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventHandler_ = $EventHandler$Type;
}}
declare module "packages/dev/architectury/registry/registries/forge/$RegistrarManagerImpl" {
import {$RegistrarManager$RegistryProvider, $RegistrarManager$RegistryProvider$Type} from "packages/dev/architectury/registry/registries/$RegistrarManager$RegistryProvider"

export class $RegistrarManagerImpl {

constructor()

public static "_get"(modId: string): $RegistrarManager$RegistryProvider
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrarManagerImpl$Type = ($RegistrarManagerImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrarManagerImpl_ = $RegistrarManagerImpl$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientGuiEvent$ScreenRenderPost" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $ClientGuiEvent$ScreenRenderPost {

 "render"(arg0: $Screen$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: float): void

(arg0: $Screen$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: float): void
}

export namespace $ClientGuiEvent$ScreenRenderPost {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientGuiEvent$ScreenRenderPost$Type = ($ClientGuiEvent$ScreenRenderPost);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientGuiEvent$ScreenRenderPost_ = $ClientGuiEvent$ScreenRenderPost$Type;
}}
declare module "packages/dev/architectury/event/events/common/$PlayerEvent$AttackEntity" {
import {$EntityHitResult, $EntityHitResult$Type} from "packages/net/minecraft/world/phys/$EntityHitResult"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $PlayerEvent$AttackEntity {

 "attack"(arg0: $Player$Type, arg1: $Level$Type, arg2: $Entity$Type, arg3: $InteractionHand$Type, arg4: $EntityHitResult$Type): $EventResult

(arg0: $Player$Type, arg1: $Level$Type, arg2: $Entity$Type, arg3: $InteractionHand$Type, arg4: $EntityHitResult$Type): $EventResult
}

export namespace $PlayerEvent$AttackEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvent$AttackEntity$Type = ($PlayerEvent$AttackEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvent$AttackEntity_ = $PlayerEvent$AttackEntity$Type;
}}
declare module "packages/dev/architectury/extensions/injected/$InjectedLiquidBlockExtension" {
import {$FlowingFluid, $FlowingFluid$Type} from "packages/net/minecraft/world/level/material/$FlowingFluid"

export interface $InjectedLiquidBlockExtension {

 "arch$getFluid"(): $FlowingFluid
}

export namespace $InjectedLiquidBlockExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectedLiquidBlockExtension$Type = ($InjectedLiquidBlockExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectedLiquidBlockExtension_ = $InjectedLiquidBlockExtension$Type;
}}
declare module "packages/dev/architectury/event/events/common/$ExplosionEvent$Pre" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"
import {$Explosion, $Explosion$Type} from "packages/net/minecraft/world/level/$Explosion"

export interface $ExplosionEvent$Pre {

 "explode"(arg0: $Level$Type, arg1: $Explosion$Type): $EventResult

(arg0: $Level$Type, arg1: $Explosion$Type): $EventResult
}

export namespace $ExplosionEvent$Pre {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExplosionEvent$Pre$Type = ($ExplosionEvent$Pre);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExplosionEvent$Pre_ = $ExplosionEvent$Pre$Type;
}}
declare module "packages/dev/architectury/event/events/common/$BlockEvent$FallingLand" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$FallingBlockEntity, $FallingBlockEntity$Type} from "packages/net/minecraft/world/entity/item/$FallingBlockEntity"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $BlockEvent$FallingLand {

 "onLand"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $BlockState$Type, arg4: $FallingBlockEntity$Type): void

(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $BlockState$Type, arg4: $FallingBlockEntity$Type): void
}

export namespace $BlockEvent$FallingLand {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEvent$FallingLand$Type = ($BlockEvent$FallingLand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEvent$FallingLand_ = $BlockEvent$FallingLand$Type;
}}
declare module "packages/dev/architectury/plugin/forge/$ArchitecturyMixinPlugin" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ArchitecturyMixinPlugin implements $IMixinConfigPlugin {

constructor()

public "onLoad"(mixinPackage: string): void
public "postApply"(targetClassName: string, targetClass: $ClassNode$Type, mixinClassName: string, mixinInfo: $IMixinInfo$Type): void
public "getMixins"(): $List<(string)>
public "getRefMapperConfig"(): string
public "shouldApplyMixin"(targetClassName: string, mixinClassName: string): boolean
public "preApply"(targetClassName: string, targetClass: $ClassNode$Type, mixinClassName: string, mixinInfo: $IMixinInfo$Type): void
public "acceptTargets"(myTargets: $Set$Type<(string)>, otherTargets: $Set$Type<(string)>): void
get "mixins"(): $List<(string)>
get "refMapperConfig"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArchitecturyMixinPlugin$Type = ($ArchitecturyMixinPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArchitecturyMixinPlugin_ = $ArchitecturyMixinPlugin$Type;
}}
declare module "packages/dev/architectury/networking/$NetworkManager$PacketContext" {
import {$Env, $Env$Type} from "packages/dev/architectury/utils/$Env"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$Dist, $Dist$Type} from "packages/net/minecraftforge/api/distmarker/$Dist"

export interface $NetworkManager$PacketContext {

 "queue"(arg0: $Runnable$Type): void
 "getEnvironment"(): $Env
 "getPlayer"(): $Player
 "getEnv"(): $Dist
}

export namespace $NetworkManager$PacketContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkManager$PacketContext$Type = ($NetworkManager$PacketContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkManager$PacketContext_ = $NetworkManager$PacketContext$Type;
}}
declare module "packages/dev/architectury/hooks/item/food/forge/$FoodPropertiesHooksImpl" {
import {$FoodProperties$Builder, $FoodProperties$Builder$Type} from "packages/net/minecraft/world/food/$FoodProperties$Builder"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $FoodPropertiesHooksImpl {

constructor()

public static "effect"(builder: $FoodProperties$Builder$Type, effectSupplier: $Supplier$Type<(any)>, chance: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FoodPropertiesHooksImpl$Type = ($FoodPropertiesHooksImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FoodPropertiesHooksImpl_ = $FoodPropertiesHooksImpl$Type;
}}
declare module "packages/dev/architectury/core/item/$ArchitecturySpawnEggItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$SpawnEggItem, $SpawnEggItem$Type} from "packages/net/minecraft/world/item/$SpawnEggItem"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$RegistrySupplier, $RegistrySupplier$Type} from "packages/dev/architectury/registry/registries/$RegistrySupplier"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$DispenseItemBehavior, $DispenseItemBehavior$Type} from "packages/net/minecraft/core/dispenser/$DispenseItemBehavior"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ArchitecturySpawnEggItem extends $SpawnEggItem {
static readonly "BY_ID": $Map<($EntityType<(any)>), ($SpawnEggItem)>
 "defaultType": $EntityType<(any)>
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(entityType: $RegistrySupplier$Type<(any)>, backgroundColor: integer, highlightColor: integer, properties: $Item$Properties$Type, dispenseItemBehavior: $DispenseItemBehavior$Type)
constructor(entityType: $RegistrySupplier$Type<(any)>, backgroundColor: integer, highlightColor: integer, properties: $Item$Properties$Type)

public "getType"(compoundTag: $CompoundTag$Type): $EntityType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArchitecturySpawnEggItem$Type = ($ArchitecturySpawnEggItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArchitecturySpawnEggItem_ = $ArchitecturySpawnEggItem$Type;
}}
declare module "packages/dev/architectury/registry/client/level/entity/forge/$EntityModelLayerRegistryImpl" {
import {$ModelLayerLocation, $ModelLayerLocation$Type} from "packages/net/minecraft/client/model/geom/$ModelLayerLocation"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$EntityRenderersEvent$RegisterLayerDefinitions, $EntityRenderersEvent$RegisterLayerDefinitions$Type} from "packages/net/minecraftforge/client/event/$EntityRenderersEvent$RegisterLayerDefinitions"
import {$LayerDefinition, $LayerDefinition$Type} from "packages/net/minecraft/client/model/geom/builders/$LayerDefinition"

export class $EntityModelLayerRegistryImpl {

constructor()

public static "register"(location: $ModelLayerLocation$Type, definition: $Supplier$Type<($LayerDefinition$Type)>): void
public static "event"(event: $EntityRenderersEvent$RegisterLayerDefinitions$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityModelLayerRegistryImpl$Type = ($EntityModelLayerRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityModelLayerRegistryImpl_ = $EntityModelLayerRegistryImpl$Type;
}}
declare module "packages/dev/architectury/annotations/$ForgeEventCancellable" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $ForgeEventCancellable extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $ForgeEventCancellable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeEventCancellable$Type = ($ForgeEventCancellable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeEventCancellable_ = $ForgeEventCancellable$Type;
}}
declare module "packages/dev/architectury/hooks/client/screen/$ScreenHooks" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export class $ScreenHooks {


public static "addWidget"<T extends ($GuiEventListener) & ($NarratableEntry)>(screen: $Screen$Type, listener: T): T
public static "getRenderables"(screen: $Screen$Type): $List<($Renderable)>
public static "getNarratables"(screen: $Screen$Type): $List<($NarratableEntry)>
public static "addRenderableWidget"<T extends ($AbstractWidget) & ($Renderable) & ($NarratableEntry)>(screen: $Screen$Type, widget: T): T
public static "addRenderableOnly"<T extends $Renderable>(screen: $Screen$Type, listener: T): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenHooks$Type = ($ScreenHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenHooks_ = $ScreenHooks$Type;
}}
declare module "packages/dev/architectury/event/events/common/$LightningEvent$Strike" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$LightningBolt, $LightningBolt$Type} from "packages/net/minecraft/world/entity/$LightningBolt"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $LightningEvent$Strike {

 "onStrike"(arg0: $LightningBolt$Type, arg1: $Level$Type, arg2: $Vec3$Type, arg3: $List$Type<($Entity$Type)>): void

(arg0: $LightningBolt$Type, arg1: $Level$Type, arg2: $Vec3$Type, arg3: $List$Type<($Entity$Type)>): void
}

export namespace $LightningEvent$Strike {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightningEvent$Strike$Type = ($LightningEvent$Strike);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightningEvent$Strike_ = $LightningEvent$Strike$Type;
}}
declare module "packages/dev/architectury/registry/forge/$ReloadListenerRegistryImpl" {
import {$PreparableReloadListener, $PreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener"
import {$PackType, $PackType$Type} from "packages/net/minecraft/server/packs/$PackType"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$AddReloadListenerEvent, $AddReloadListenerEvent$Type} from "packages/net/minecraftforge/event/$AddReloadListenerEvent"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ReloadListenerRegistryImpl {

constructor()

public static "register"(type: $PackType$Type, listener: $PreparableReloadListener$Type, listenerId: $ResourceLocation$Type, dependencies: $Collection$Type<($ResourceLocation$Type)>): void
public static "addReloadListeners"(event: $AddReloadListenerEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReloadListenerRegistryImpl$Type = ($ReloadListenerRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReloadListenerRegistryImpl_ = $ReloadListenerRegistryImpl$Type;
}}
declare module "packages/dev/architectury/event/events/common/$CommandRegistrationEvent" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$CommandBuildContext, $CommandBuildContext$Type} from "packages/net/minecraft/commands/$CommandBuildContext"
import {$Commands$CommandSelection, $Commands$CommandSelection$Type} from "packages/net/minecraft/commands/$Commands$CommandSelection"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export interface $CommandRegistrationEvent {

 "register"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>, arg1: $CommandBuildContext$Type, arg2: $Commands$CommandSelection$Type): void

(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>, arg1: $CommandBuildContext$Type, arg2: $Commands$CommandSelection$Type): void
}

export namespace $CommandRegistrationEvent {
const EVENT: $Event<($CommandRegistrationEvent)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommandRegistrationEvent$Type = ($CommandRegistrationEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommandRegistrationEvent_ = $CommandRegistrationEvent$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientRawInputEvent" {
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$ClientRawInputEvent$MouseScrolled, $ClientRawInputEvent$MouseScrolled$Type} from "packages/dev/architectury/event/events/client/$ClientRawInputEvent$MouseScrolled"
import {$ClientRawInputEvent$MouseClicked, $ClientRawInputEvent$MouseClicked$Type} from "packages/dev/architectury/event/events/client/$ClientRawInputEvent$MouseClicked"
import {$ClientRawInputEvent$KeyPressed, $ClientRawInputEvent$KeyPressed$Type} from "packages/dev/architectury/event/events/client/$ClientRawInputEvent$KeyPressed"

export interface $ClientRawInputEvent {

}

export namespace $ClientRawInputEvent {
const MOUSE_SCROLLED: $Event<($ClientRawInputEvent$MouseScrolled)>
const MOUSE_CLICKED_PRE: $Event<($ClientRawInputEvent$MouseClicked)>
const MOUSE_CLICKED_POST: $Event<($ClientRawInputEvent$MouseClicked)>
const KEY_PRESSED: $Event<($ClientRawInputEvent$KeyPressed)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientRawInputEvent$Type = ($ClientRawInputEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientRawInputEvent_ = $ClientRawInputEvent$Type;
}}
declare module "packages/dev/architectury/event/events/common/$EntityEvent" {
import {$EntityEvent$Add, $EntityEvent$Add$Type} from "packages/dev/architectury/event/events/common/$EntityEvent$Add"
import {$EntityEvent$LivingCheckSpawn, $EntityEvent$LivingCheckSpawn$Type} from "packages/dev/architectury/event/events/common/$EntityEvent$LivingCheckSpawn"
import {$EntityEvent$EnterSection, $EntityEvent$EnterSection$Type} from "packages/dev/architectury/event/events/common/$EntityEvent$EnterSection"
import {$EntityEvent$LivingHurt, $EntityEvent$LivingHurt$Type} from "packages/dev/architectury/event/events/common/$EntityEvent$LivingHurt"
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$EntityEvent$LivingDeath, $EntityEvent$LivingDeath$Type} from "packages/dev/architectury/event/events/common/$EntityEvent$LivingDeath"
import {$EntityEvent$AnimalTame, $EntityEvent$AnimalTame$Type} from "packages/dev/architectury/event/events/common/$EntityEvent$AnimalTame"

export interface $EntityEvent {

}

export namespace $EntityEvent {
const LIVING_DEATH: $Event<($EntityEvent$LivingDeath)>
const LIVING_HURT: $Event<($EntityEvent$LivingHurt)>
const LIVING_CHECK_SPAWN: $Event<($EntityEvent$LivingCheckSpawn)>
const ADD: $Event<($EntityEvent$Add)>
const ENTER_SECTION: $Event<($EntityEvent$EnterSection)>
const ANIMAL_TAME: $Event<($EntityEvent$AnimalTame)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityEvent$Type = ($EntityEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityEvent_ = $EntityEvent$Type;
}}
declare module "packages/dev/architectury/event/events/common/$PlayerEvent$PlayerClone" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"

export interface $PlayerEvent$PlayerClone {

 "clone"(arg0: $ServerPlayer$Type, arg1: $ServerPlayer$Type, arg2: boolean): void

(arg0: $ServerPlayer$Type, arg1: $ServerPlayer$Type, arg2: boolean): void
}

export namespace $PlayerEvent$PlayerClone {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvent$PlayerClone$Type = ($PlayerEvent$PlayerClone);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvent$PlayerClone_ = $PlayerEvent$PlayerClone$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientTooltipEvent$RenderModifyColor" {
import {$ClientTooltipEvent$ColorContext, $ClientTooltipEvent$ColorContext$Type} from "packages/dev/architectury/event/events/client/$ClientTooltipEvent$ColorContext"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $ClientTooltipEvent$RenderModifyColor {

 "renderTooltip"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $ClientTooltipEvent$ColorContext$Type): void

(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $ClientTooltipEvent$ColorContext$Type): void
}

export namespace $ClientTooltipEvent$RenderModifyColor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTooltipEvent$RenderModifyColor$Type = ($ClientTooltipEvent$RenderModifyColor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTooltipEvent$RenderModifyColor_ = $ClientTooltipEvent$RenderModifyColor$Type;
}}
declare module "packages/dev/architectury/event/$EventResult" {
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"

export class $EventResult {


public "value"(): boolean
public "isEmpty"(): boolean
public "isPresent"(): boolean
public static "interrupt"(value: boolean): $EventResult
public static "pass"(): $EventResult
public "isTrue"(): boolean
public static "interruptTrue"(): $EventResult
public static "interruptDefault"(): $EventResult
public "asMinecraft"(): $InteractionResult
public static "interruptFalse"(): $EventResult
public "interruptsFurtherEvaluation"(): boolean
public "isFalse"(): boolean
get "empty"(): boolean
get "present"(): boolean
get "true"(): boolean
get "false"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventResult$Type = ($EventResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventResult_ = $EventResult$Type;
}}
declare module "packages/dev/architectury/event/$Event" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Event<T> {

 "isRegistered"(arg0: T): boolean
 "register"(arg0: T): void
 "invoker"(): T
 "unregister"(arg0: T): void
 "clearListeners"(): void
}

export namespace $Event {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Event$Type<T> = ($Event<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Event_<T> = $Event$Type<(T)>;
}}
declare module "packages/dev/architectury/event/events/client/$ClientReloadShadersEvent$ShadersSink" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ShaderInstance, $ShaderInstance$Type} from "packages/net/minecraft/client/renderer/$ShaderInstance"

export interface $ClientReloadShadersEvent$ShadersSink {

 "registerShader"(arg0: $ShaderInstance$Type, arg1: $Consumer$Type<($ShaderInstance$Type)>): void

(arg0: $ShaderInstance$Type, arg1: $Consumer$Type<($ShaderInstance$Type)>): void
}

export namespace $ClientReloadShadersEvent$ShadersSink {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientReloadShadersEvent$ShadersSink$Type = ($ClientReloadShadersEvent$ShadersSink);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientReloadShadersEvent$ShadersSink_ = $ClientReloadShadersEvent$ShadersSink$Type;
}}
declare module "packages/dev/architectury/hooks/forge/$DyeColorHooksImpl" {
import {$DyeColor, $DyeColor$Type} from "packages/net/minecraft/world/item/$DyeColor"

export class $DyeColorHooksImpl {

constructor()

public static "getColorValue"(dyeColor: $DyeColor$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DyeColorHooksImpl$Type = ($DyeColorHooksImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DyeColorHooksImpl_ = $DyeColorHooksImpl$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientGuiEvent$ScreenInitPost" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ScreenAccess, $ScreenAccess$Type} from "packages/dev/architectury/hooks/client/screen/$ScreenAccess"

export interface $ClientGuiEvent$ScreenInitPost {

 "init"(arg0: $Screen$Type, arg1: $ScreenAccess$Type): void

(arg0: $Screen$Type, arg1: $ScreenAccess$Type): void
}

export namespace $ClientGuiEvent$ScreenInitPost {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientGuiEvent$ScreenInitPost$Type = ($ClientGuiEvent$ScreenInitPost);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientGuiEvent$ScreenInitPost_ = $ClientGuiEvent$ScreenInitPost$Type;
}}
declare module "packages/dev/architectury/utils/$Amount" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Amount {

constructor()

public static "toInt"(amount: long): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Amount$Type = ($Amount);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Amount_ = $Amount$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientPlayerEvent$ClientPlayerRespawn" {
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"

export interface $ClientPlayerEvent$ClientPlayerRespawn {

 "respawn"(arg0: $LocalPlayer$Type, arg1: $LocalPlayer$Type): void

(arg0: $LocalPlayer$Type, arg1: $LocalPlayer$Type): void
}

export namespace $ClientPlayerEvent$ClientPlayerRespawn {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientPlayerEvent$ClientPlayerRespawn$Type = ($ClientPlayerEvent$ClientPlayerRespawn);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientPlayerEvent$ClientPlayerRespawn_ = $ClientPlayerEvent$ClientPlayerRespawn$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientTooltipEvent" {
import {$ClientTooltipEvent$AdditionalContexts, $ClientTooltipEvent$AdditionalContexts$Type} from "packages/dev/architectury/event/events/client/$ClientTooltipEvent$AdditionalContexts"
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$ClientTooltipEvent$RenderModifyPosition, $ClientTooltipEvent$RenderModifyPosition$Type} from "packages/dev/architectury/event/events/client/$ClientTooltipEvent$RenderModifyPosition"
import {$ClientTooltipEvent$Item, $ClientTooltipEvent$Item$Type} from "packages/dev/architectury/event/events/client/$ClientTooltipEvent$Item"
import {$ClientTooltipEvent$Render, $ClientTooltipEvent$Render$Type} from "packages/dev/architectury/event/events/client/$ClientTooltipEvent$Render"
import {$ClientTooltipEvent$RenderModifyColor, $ClientTooltipEvent$RenderModifyColor$Type} from "packages/dev/architectury/event/events/client/$ClientTooltipEvent$RenderModifyColor"

export interface $ClientTooltipEvent {

}

export namespace $ClientTooltipEvent {
const ITEM: $Event<($ClientTooltipEvent$Item)>
const RENDER_PRE: $Event<($ClientTooltipEvent$Render)>
const RENDER_MODIFY_POSITION: $Event<($ClientTooltipEvent$RenderModifyPosition)>
const RENDER_MODIFY_COLOR: $Event<($ClientTooltipEvent$RenderModifyColor)>
function additionalContexts(): $ClientTooltipEvent$AdditionalContexts
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTooltipEvent$Type = ($ClientTooltipEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTooltipEvent_ = $ClientTooltipEvent$Type;
}}
declare module "packages/dev/architectury/event/events/common/$PlayerEvent$OpenMenu" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"

export interface $PlayerEvent$OpenMenu {

 "open"(arg0: $Player$Type, arg1: $AbstractContainerMenu$Type): void

(arg0: $Player$Type, arg1: $AbstractContainerMenu$Type): void
}

export namespace $PlayerEvent$OpenMenu {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvent$OpenMenu$Type = ($PlayerEvent$OpenMenu);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvent$OpenMenu_ = $PlayerEvent$OpenMenu$Type;
}}
declare module "packages/dev/architectury/utils/value/$IntValue" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$IntConsumer, $IntConsumer$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntConsumer"
import {$Value, $Value$Type} from "packages/dev/architectury/utils/value/$Value"
import {$IntSupplier, $IntSupplier$Type} from "packages/java/util/function/$IntSupplier"

export interface $IntValue extends $Value<(integer)>, $IntSupplier, $IntConsumer {

 "get"(): integer
 "getAsInt"(): integer
/**
 * 
 * @deprecated
 */
 "accept"(arg0: integer): void
/**
 * 
 * @deprecated
 */
 "andThen"(arg0: $Consumer$Type<(any)>): $Consumer<(integer)>
 "andThen"(arg0: $IntConsumer$Type): $IntConsumer
 "accept"(arg0: integer): void
}

export namespace $IntValue {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntValue$Type = ($IntValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntValue_ = $IntValue$Type;
}}
declare module "packages/dev/architectury/core/fluid/$ArchitecturyFluidAttributes" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$FluidStack, $FluidStack$Type} from "packages/dev/architectury/fluid/$FluidStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Rarity, $Rarity$Type} from "packages/net/minecraft/world/item/$Rarity"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$LiquidBlock, $LiquidBlock$Type} from "packages/net/minecraft/world/level/block/$LiquidBlock"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"

export interface $ArchitecturyFluidAttributes {

 "getName"(): $Component
 "getName"(stack: $FluidStack$Type): $Component
 "getBlock"(): $LiquidBlock
 "getFlowingTexture"(stack: $FluidStack$Type): $ResourceLocation
/**
 * 
 * @deprecated
 */
 "getFlowingTexture"(arg0: $FluidStack$Type, arg1: $BlockAndTintGetter$Type, arg2: $BlockPos$Type): $ResourceLocation
 "getFlowingTexture"(state: $FluidState$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): $ResourceLocation
 "getFlowingTexture"(): $ResourceLocation
 "getOverlayTexture"(state: $FluidState$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): $ResourceLocation
 "getOverlayTexture"(): $ResourceLocation
 "getOverlayTexture"(stack: $FluidStack$Type): $ResourceLocation
 "getTickDelay"(arg0: $LevelReader$Type): integer
 "getTickDelay"(): integer
 "getBucketItem"(): $Item
 "getDropOff"(arg0: $LevelReader$Type): integer
 "getDropOff"(): integer
 "getSourceFluid"(): $Fluid
 "getFillSound"(arg0: $FluidStack$Type, arg1: $BlockAndTintGetter$Type, arg2: $BlockPos$Type): $SoundEvent
 "getFillSound"(): $SoundEvent
 "getFillSound"(stack: $FluidStack$Type): $SoundEvent
 "getFlowingFluid"(): $Fluid
/**
 * 
 * @deprecated
 */
 "getSourceTexture"(arg0: $FluidStack$Type, arg1: $BlockAndTintGetter$Type, arg2: $BlockPos$Type): $ResourceLocation
 "getSourceTexture"(state: $FluidState$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): $ResourceLocation
 "getSourceTexture"(): $ResourceLocation
 "getSourceTexture"(stack: $FluidStack$Type): $ResourceLocation
 "getLuminosity"(): integer
 "getLuminosity"(stack: $FluidStack$Type): integer
 "getLuminosity"(arg0: $FluidStack$Type, arg1: $BlockAndTintGetter$Type, arg2: $BlockPos$Type): integer
 "getExplosionResistance"(): float
 "getTemperature"(arg0: $FluidStack$Type, arg1: $BlockAndTintGetter$Type, arg2: $BlockPos$Type): integer
 "getTemperature"(stack: $FluidStack$Type): integer
 "getTemperature"(): integer
/**
 * 
 * @deprecated
 */
 "getColor"(arg0: $FluidStack$Type, arg1: $BlockAndTintGetter$Type, arg2: $BlockPos$Type): integer
 "getColor"(state: $FluidState$Type, level: $BlockAndTintGetter$Type, pos: $BlockPos$Type): integer
 "getColor"(stack: $FluidStack$Type): integer
 "getColor"(): integer
 "getViscosity"(arg0: $FluidStack$Type, arg1: $BlockAndTintGetter$Type, arg2: $BlockPos$Type): integer
 "getViscosity"(): integer
 "getViscosity"(stack: $FluidStack$Type): integer
 "getDensity"(arg0: $FluidStack$Type, arg1: $BlockAndTintGetter$Type, arg2: $BlockPos$Type): integer
 "getDensity"(): integer
 "getDensity"(stack: $FluidStack$Type): integer
 "isLighterThanAir"(arg0: $FluidStack$Type, arg1: $BlockAndTintGetter$Type, arg2: $BlockPos$Type): boolean
 "isLighterThanAir"(): boolean
 "isLighterThanAir"(stack: $FluidStack$Type): boolean
 "getTranslationKey"(): string
 "getTranslationKey"(arg0: $FluidStack$Type): string
 "getRarity"(stack: $FluidStack$Type): $Rarity
 "getRarity"(arg0: $FluidStack$Type, arg1: $BlockAndTintGetter$Type, arg2: $BlockPos$Type): $Rarity
 "getRarity"(): $Rarity
 "getEmptySound"(stack: $FluidStack$Type): $SoundEvent
 "getEmptySound"(arg0: $FluidStack$Type, arg1: $BlockAndTintGetter$Type, arg2: $BlockPos$Type): $SoundEvent
 "getEmptySound"(): $SoundEvent
 "canConvertToSource"(): boolean
 "getSlopeFindDistance"(arg0: $LevelReader$Type): integer
 "getSlopeFindDistance"(): integer
}

export namespace $ArchitecturyFluidAttributes {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArchitecturyFluidAttributes$Type = ($ArchitecturyFluidAttributes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArchitecturyFluidAttributes_ = $ArchitecturyFluidAttributes$Type;
}}
declare module "packages/dev/architectury/registry/registries/$Registrar" {
import {$RegistrySupplier, $RegistrySupplier$Type} from "packages/dev/architectury/registry/registries/$RegistrySupplier"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export interface $Registrar<T> extends $Iterable<(T)> {

 "get"(arg0: $ResourceLocation$Type): T
 "wrap"<R extends T>(obj: R): $RegistrySupplier<(R)>
 "contains"(arg0: $ResourceLocation$Type): boolean
 "entrySet"(): $Set<($Map$Entry<($ResourceKey<(T)>), (T)>)>
 "getKey"(arg0: T): $Optional<($ResourceKey<(T)>)>
 "register"<E extends T>(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<(E)>): $RegistrySupplier<(E)>
 "key"(): $ResourceKey<(any)>
 "listen"(arg0: $ResourceLocation$Type, arg1: $Consumer$Type<(T)>): void
 "listen"<R extends T>(supplier: $RegistrySupplier$Type<(R)>, callback: $Consumer$Type<(R)>): void
 "getId"(arg0: T): $ResourceLocation
 "containsValue"(arg0: T): boolean
 "delegate"(arg0: $ResourceLocation$Type): $RegistrySupplier<(T)>
 "byRawId"(arg0: integer): T
 "getRawId"(arg0: T): integer
 "getIds"(): $Set<($ResourceLocation)>
 "iterator"(): $Iterator<(T)>
 "spliterator"(): $Spliterator<(T)>
 "forEach"(arg0: $Consumer$Type<(any)>): void
}

export namespace $Registrar {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Registrar$Type<T> = ($Registrar<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Registrar_<T> = $Registrar$Type<(T)>;
}}
declare module "packages/dev/architectury/event/events/common/$PlayerEvent$PlayerAdvancement" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Advancement, $Advancement$Type} from "packages/net/minecraft/advancements/$Advancement"

export interface $PlayerEvent$PlayerAdvancement {

 "award"(arg0: $ServerPlayer$Type, arg1: $Advancement$Type): void

(arg0: $ServerPlayer$Type, arg1: $Advancement$Type): void
}

export namespace $PlayerEvent$PlayerAdvancement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvent$PlayerAdvancement$Type = ($PlayerEvent$PlayerAdvancement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvent$PlayerAdvancement_ = $PlayerEvent$PlayerAdvancement$Type;
}}
declare module "packages/dev/architectury/extensions/injected/$InjectedFluidExtension" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$InjectedRegistryEntryExtension, $InjectedRegistryEntryExtension$Type} from "packages/dev/architectury/extensions/injected/$InjectedRegistryEntryExtension"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $InjectedFluidExtension extends $InjectedRegistryEntryExtension<($Fluid)> {

 "arch$holder"(): $Holder<($Fluid)>
 "arch$registryName"(): $ResourceLocation
}

export namespace $InjectedFluidExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectedFluidExtension$Type = ($InjectedFluidExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectedFluidExtension_ = $InjectedFluidExtension$Type;
}}
declare module "packages/dev/architectury/extensions/$ItemExtension" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IForgeItem, $IForgeItem$Type} from "packages/net/minecraftforge/common/extensions/$IForgeItem"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$FoodProperties, $FoodProperties$Type} from "packages/net/minecraft/world/food/$FoodProperties"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$ToolAction, $ToolAction$Type} from "packages/net/minecraftforge/common/$ToolAction"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$EnderMan, $EnderMan$Type} from "packages/net/minecraft/world/entity/monster/$EnderMan"
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$ICapabilityProvider, $ICapabilityProvider$Type} from "packages/net/minecraftforge/common/capabilities/$ICapabilityProvider"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$ItemEntity, $ItemEntity$Type} from "packages/net/minecraft/world/entity/item/$ItemEntity"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $ItemExtension extends $IForgeItem {

 "getEquipmentSlot"(stack: $ItemStack$Type): $EquipmentSlot
 "onArmorTick"(stack: $ItemStack$Type, world: $Level$Type, player: $Player$Type): void
 "tickArmor"(stack: $ItemStack$Type, player: $Player$Type): void
 "getCustomEquipmentSlot"(stack: $ItemStack$Type): $EquipmentSlot
 "getHighlightTip"(arg0: $ItemStack$Type, arg1: $Component$Type): $Component
 "getEnchantmentLevel"(arg0: $ItemStack$Type, arg1: $Enchantment$Type): integer
 "canDisableShield"(arg0: $ItemStack$Type, arg1: $ItemStack$Type, arg2: $LivingEntity$Type, arg3: $LivingEntity$Type): boolean
 "canPerformAction"(arg0: $ItemStack$Type, arg1: $ToolAction$Type): boolean
 "getSweepHitBox"(arg0: $ItemStack$Type, arg1: $Player$Type, arg2: $Entity$Type): $AABB
 "canElytraFly"(arg0: $ItemStack$Type, arg1: $LivingEntity$Type): boolean
 "isCorrectToolForDrops"(arg0: $ItemStack$Type, arg1: $BlockState$Type): boolean
 "shouldCauseReequipAnimation"(arg0: $ItemStack$Type, arg1: $ItemStack$Type, arg2: boolean): boolean
 "makesPiglinsNeutral"(arg0: $ItemStack$Type, arg1: $LivingEntity$Type): boolean
 "canWalkOnPowderedSnow"(arg0: $ItemStack$Type, arg1: $LivingEntity$Type): boolean
 "hasCraftingRemainingItem"(arg0: $ItemStack$Type): boolean
 "getCraftingRemainingItem"(arg0: $ItemStack$Type): $ItemStack
 "canGrindstoneRepair"(arg0: $ItemStack$Type): boolean
 "getEnchantmentValue"(arg0: $ItemStack$Type): integer
 "getAttributeModifiers"(arg0: $EquipmentSlot$Type, arg1: $ItemStack$Type): $Multimap<($Attribute), ($AttributeModifier)>
 "getDefaultTooltipHideFlags"(arg0: $ItemStack$Type): integer
 "canApplyAtEnchantingTable"(arg0: $ItemStack$Type, arg1: $Enchantment$Type): boolean
 "isNotReplaceableByPickAction"(arg0: $ItemStack$Type, arg1: $Player$Type, arg2: integer): boolean
 "shouldCauseBlockBreakReset"(arg0: $ItemStack$Type, arg1: $ItemStack$Type): boolean
 "onEntitySwing"(arg0: $ItemStack$Type, arg1: $LivingEntity$Type): boolean
 "elytraFlightTick"(arg0: $ItemStack$Type, arg1: $LivingEntity$Type, arg2: integer): boolean
 "getFoodProperties"(arg0: $ItemStack$Type, arg1: $LivingEntity$Type): $FoodProperties
 "canContinueUsing"(arg0: $ItemStack$Type, arg1: $ItemStack$Type): boolean
 "onStopUsing"(arg0: $ItemStack$Type, arg1: $LivingEntity$Type, arg2: integer): void
 "createEntity"(arg0: $Level$Type, arg1: $Entity$Type, arg2: $ItemStack$Type): $Entity
 "isRepairable"(arg0: $ItemStack$Type): boolean
 "isDamageable"(arg0: $ItemStack$Type): boolean
 "getMaxDamage"(arg0: $ItemStack$Type): integer
 "onDroppedByPlayer"(arg0: $ItemStack$Type, arg1: $Player$Type): boolean
 "onItemUseFirst"(arg0: $ItemStack$Type, arg1: $UseOnContext$Type): $InteractionResult
 "isPiglinCurrency"(arg0: $ItemStack$Type): boolean
 "isBookEnchantable"(arg0: $ItemStack$Type, arg1: $ItemStack$Type): boolean
 "hasCustomEntity"(arg0: $ItemStack$Type): boolean
 "getArmorTexture"(arg0: $ItemStack$Type, arg1: $Entity$Type, arg2: $EquipmentSlot$Type, arg3: string): string
 "readShareTag"(arg0: $ItemStack$Type, arg1: $CompoundTag$Type): void
 "getXpRepairRatio"(arg0: $ItemStack$Type): float
 "isDamaged"(arg0: $ItemStack$Type): boolean
 "setDamage"(arg0: $ItemStack$Type, arg1: integer): void
 "getShareTag"(arg0: $ItemStack$Type): $CompoundTag
 "onLeftClickEntity"(arg0: $ItemStack$Type, arg1: $Player$Type, arg2: $Entity$Type): boolean
 "doesSneakBypassUse"(arg0: $ItemStack$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type, arg3: $Player$Type): boolean
 "onInventoryTick"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $Player$Type, arg3: integer, arg4: integer): void
 "canEquip"(arg0: $ItemStack$Type, arg1: $EquipmentSlot$Type, arg2: $Entity$Type): boolean
 "onBlockStartBreak"(arg0: $ItemStack$Type, arg1: $BlockPos$Type, arg2: $Player$Type): boolean
 "damageItem"<T extends $LivingEntity>(arg0: $ItemStack$Type, arg1: integer, arg2: T, arg3: $Consumer$Type<(T)>): integer
 "getBurnTime"(arg0: $ItemStack$Type, arg1: $RecipeType$Type<(any)>): integer
 "onHorseArmorTick"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $Mob$Type): void
 "isEnderMask"(arg0: $ItemStack$Type, arg1: $Player$Type, arg2: $EnderMan$Type): boolean
 "getCreatorModId"(arg0: $ItemStack$Type): string
 "getDamage"(arg0: $ItemStack$Type): integer
 "initCapabilities"(arg0: $ItemStack$Type, arg1: $CompoundTag$Type): $ICapabilityProvider
 "getAllEnchantments"(arg0: $ItemStack$Type): $Map<($Enchantment), (integer)>
 "getEntityLifespan"(arg0: $ItemStack$Type, arg1: $Level$Type): integer
 "onEntityItemUpdate"(arg0: $ItemStack$Type, arg1: $ItemEntity$Type): boolean
 "onDestroyed"(arg0: $ItemEntity$Type, arg1: $DamageSource$Type): void
 "getMaxStackSize"(arg0: $ItemStack$Type): integer

(stack: $ItemStack$Type): $EquipmentSlot
}

export namespace $ItemExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemExtension$Type = ($ItemExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemExtension_ = $ItemExtension$Type;
}}
declare module "packages/dev/architectury/event/forge/$EventHandlerImplCommon$LevelEventAttachment" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"

export interface $EventHandlerImplCommon$LevelEventAttachment {

 "architectury$getAttachedLevel"(): $LevelAccessor
 "architectury$attachLevel"(arg0: $LevelAccessor$Type): void
}

export namespace $EventHandlerImplCommon$LevelEventAttachment {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventHandlerImplCommon$LevelEventAttachment$Type = ($EventHandlerImplCommon$LevelEventAttachment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventHandlerImplCommon$LevelEventAttachment_ = $EventHandlerImplCommon$LevelEventAttachment$Type;
}}
declare module "packages/dev/architectury/registry/$ReloadListenerRegistry" {
import {$PreparableReloadListener, $PreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener"
import {$PackType, $PackType$Type} from "packages/net/minecraft/server/packs/$PackType"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ReloadListenerRegistry {


public static "register"(type: $PackType$Type, listener: $PreparableReloadListener$Type, listenerId: $ResourceLocation$Type, dependencies: $Collection$Type<($ResourceLocation$Type)>): void
public static "register"(type: $PackType$Type, listener: $PreparableReloadListener$Type, listenerId: $ResourceLocation$Type): void
public static "register"(type: $PackType$Type, listener: $PreparableReloadListener$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReloadListenerRegistry$Type = ($ReloadListenerRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReloadListenerRegistry_ = $ReloadListenerRegistry$Type;
}}
declare module "packages/dev/architectury/registry/registries/options/$RegistrarOption" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RegistrarOption {

}

export namespace $RegistrarOption {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrarOption$Type = ($RegistrarOption);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrarOption_ = $RegistrarOption$Type;
}}
declare module "packages/dev/architectury/registry/level/entity/forge/$EntityAttributeRegistryImpl" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$EntityAttributeCreationEvent, $EntityAttributeCreationEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityAttributeCreationEvent"
import {$AttributeSupplier$Builder, $AttributeSupplier$Builder$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeSupplier$Builder"

export class $EntityAttributeRegistryImpl {

constructor()

public static "register"(type: $Supplier$Type<(any)>, attribute: $Supplier$Type<($AttributeSupplier$Builder$Type)>): void
public static "event"(event: $EntityAttributeCreationEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityAttributeRegistryImpl$Type = ($EntityAttributeRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityAttributeRegistryImpl_ = $EntityAttributeRegistryImpl$Type;
}}
declare module "packages/dev/architectury/networking/transformers/$PacketTransformer" {
import {$NetworkManager$Side, $NetworkManager$Side$Type} from "packages/dev/architectury/networking/$NetworkManager$Side"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$NetworkManager$PacketContext, $NetworkManager$PacketContext$Type} from "packages/dev/architectury/networking/$NetworkManager$PacketContext"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$PacketTransformer$TransformationSink, $PacketTransformer$TransformationSink$Type} from "packages/dev/architectury/networking/transformers/$PacketTransformer$TransformationSink"

export interface $PacketTransformer {

 "outbound"(arg0: $NetworkManager$Side$Type, arg1: $ResourceLocation$Type, arg2: $FriendlyByteBuf$Type, arg3: $PacketTransformer$TransformationSink$Type): void
 "inbound"(arg0: $NetworkManager$Side$Type, arg1: $ResourceLocation$Type, arg2: $FriendlyByteBuf$Type, arg3: $NetworkManager$PacketContext$Type, arg4: $PacketTransformer$TransformationSink$Type): void
}

export namespace $PacketTransformer {
function concat(transformers: $Iterable$Type<(any)>): $PacketTransformer
function none(): $PacketTransformer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketTransformer$Type = ($PacketTransformer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketTransformer_ = $PacketTransformer$Type;
}}
declare module "packages/dev/architectury/hooks/level/entity/$EntityHooks" {
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EntityHooks {


public static "fromCollision"(ctx: $CollisionContext$Type): $Entity
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityHooks$Type = ($EntityHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityHooks_ = $EntityHooks$Type;
}}
declare module "packages/dev/architectury/event/events/common/$LifecycleEvent$LevelState" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export interface $LifecycleEvent$LevelState<T extends $Level> {

 "act"(arg0: T): void

(arg0: T): void
}

export namespace $LifecycleEvent$LevelState {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LifecycleEvent$LevelState$Type<T> = ($LifecycleEvent$LevelState<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LifecycleEvent$LevelState_<T> = $LifecycleEvent$LevelState$Type<(T)>;
}}
declare module "packages/dev/architectury/registry/registries/options/$StandardRegistrarOption" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$RegistrarOption, $RegistrarOption$Type} from "packages/dev/architectury/registry/registries/options/$RegistrarOption"

export class $StandardRegistrarOption extends $Enum<($StandardRegistrarOption)> implements $RegistrarOption {
static readonly "SAVE_TO_DISC": $StandardRegistrarOption
static readonly "SYNC_TO_CLIENTS": $StandardRegistrarOption


public static "values"(): ($StandardRegistrarOption)[]
public static "valueOf"(name: string): $StandardRegistrarOption
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StandardRegistrarOption$Type = (("save_to_disc") | ("sync_to_clients")) | ($StandardRegistrarOption);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StandardRegistrarOption_ = $StandardRegistrarOption$Type;
}}
declare module "packages/dev/architectury/registry/level/entity/$SpawnPlacementsRegistry" {
import {$SpawnPlacements$Type, $SpawnPlacements$Type$Type} from "packages/net/minecraft/world/entity/$SpawnPlacements$Type"
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"
import {$SpawnPlacements$SpawnPredicate, $SpawnPlacements$SpawnPredicate$Type} from "packages/net/minecraft/world/entity/$SpawnPlacements$SpawnPredicate"
import {$Heightmap$Types, $Heightmap$Types$Type} from "packages/net/minecraft/world/level/levelgen/$Heightmap$Types"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $SpawnPlacementsRegistry {

constructor()

public static "register"<T extends $Mob>(type: $Supplier$Type<(any)>, spawnPlacement: $SpawnPlacements$Type$Type, heightmapType: $Heightmap$Types$Type, spawnPredicate: $SpawnPlacements$SpawnPredicate$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpawnPlacementsRegistry$Type = ($SpawnPlacementsRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpawnPlacementsRegistry_ = $SpawnPlacementsRegistry$Type;
}}
declare module "packages/dev/architectury/event/$EventFactory" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$EventActor, $EventActor$Type} from "packages/dev/architectury/event/$EventActor"

export class $EventFactory {


public static "of"<T>(arg0: $Function$Type<($List$Type<(T)>), (T)>): $Event<(T)>
public static "createConsumerLoop"<T>(...typeGetter: (T)[]): $Event<($Consumer<(T)>)>
public static "createConsumerLoop"<T>(clazz: $Class$Type<(T)>): $Event<($Consumer<(T)>)>
public static "createLoop"<T>(clazz: $Class$Type<(T)>): $Event<(T)>
public static "createLoop"<T>(...typeGetter: (T)[]): $Event<(T)>
public static "createEventResult"<T>(clazz: $Class$Type<(T)>): $Event<(T)>
public static "createEventResult"<T>(...typeGetter: (T)[]): $Event<(T)>
public static "attachToForge"<T>(event: $Event$Type<($Consumer$Type<(T)>)>): $Event<($Consumer<(T)>)>
public static "createCompoundEventResult"<T>(...typeGetter: (T)[]): $Event<(T)>
public static "createCompoundEventResult"<T>(clazz: $Class$Type<(T)>): $Event<(T)>
public static "attachToForgeEventActor"<T>(event: $Event$Type<($EventActor$Type<(T)>)>): $Event<($EventActor<(T)>)>
public static "createEventActorLoop"<T>(clazz: $Class$Type<(T)>): $Event<($EventActor<(T)>)>
public static "createEventActorLoop"<T>(...typeGetter: (T)[]): $Event<($EventActor<(T)>)>
public static "attachToForgeEventActorCancellable"<T>(event: $Event$Type<($EventActor$Type<(T)>)>): $Event<($EventActor<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventFactory$Type = ($EventFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventFactory_ = $EventFactory$Type;
}}
declare module "packages/dev/architectury/impl/$TooltipAdditionalContextsImpl" {
import {$ClientTooltipEvent$AdditionalContexts, $ClientTooltipEvent$AdditionalContexts$Type} from "packages/dev/architectury/event/events/client/$ClientTooltipEvent$AdditionalContexts"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $TooltipAdditionalContextsImpl implements $ClientTooltipEvent$AdditionalContexts {

constructor()

public static "get"(): $ClientTooltipEvent$AdditionalContexts
public "getItem"(): $ItemStack
public "setItem"(item: $ItemStack$Type): void
get "item"(): $ItemStack
set "item"(value: $ItemStack$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TooltipAdditionalContextsImpl$Type = ($TooltipAdditionalContextsImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TooltipAdditionalContextsImpl_ = $TooltipAdditionalContextsImpl$Type;
}}
declare module "packages/dev/architectury/impl/$TooltipEventPositionContextImpl" {
import {$ClientTooltipEvent$PositionContext, $ClientTooltipEvent$PositionContext$Type} from "packages/dev/architectury/event/events/client/$ClientTooltipEvent$PositionContext"

export class $TooltipEventPositionContextImpl implements $ClientTooltipEvent$PositionContext {

constructor()

public "reset"(tooltipX: integer, tooltipY: integer): $TooltipEventPositionContextImpl
public "getTooltipY"(): integer
public "getTooltipX"(): integer
public "setTooltipY"(y: integer): void
public "setTooltipX"(x: integer): void
get "tooltipY"(): integer
get "tooltipX"(): integer
set "tooltipY"(value: integer)
set "tooltipX"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TooltipEventPositionContextImpl$Type = ($TooltipEventPositionContextImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TooltipEventPositionContextImpl_ = $TooltipEventPositionContextImpl$Type;
}}
declare module "packages/dev/architectury/event/forge/$EventHandlerImplCommon" {
import {$PlayerEvent$ItemCraftedEvent, $PlayerEvent$ItemCraftedEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$ItemCraftedEvent"
import {$RegisterCommandsEvent, $RegisterCommandsEvent$Type} from "packages/net/minecraftforge/event/$RegisterCommandsEvent"
import {$ChunkDataEvent$Save, $ChunkDataEvent$Save$Type} from "packages/net/minecraftforge/event/level/$ChunkDataEvent$Save"
import {$AttackEntityEvent, $AttackEntityEvent$Type} from "packages/net/minecraftforge/event/entity/player/$AttackEntityEvent"
import {$PlayerEvent$PlayerLoggedOutEvent, $PlayerEvent$PlayerLoggedOutEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerLoggedOutEvent"
import {$PlayerInteractEvent$RightClickItem, $PlayerInteractEvent$RightClickItem$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$RightClickItem"
import {$ServerStartingEvent, $ServerStartingEvent$Type} from "packages/net/minecraftforge/event/server/$ServerStartingEvent"
import {$ChunkDataEvent$Load, $ChunkDataEvent$Load$Type} from "packages/net/minecraftforge/event/level/$ChunkDataEvent$Load"
import {$BlockEvent$BreakEvent, $BlockEvent$BreakEvent$Type} from "packages/net/minecraftforge/event/level/$BlockEvent$BreakEvent"
import {$LivingAttackEvent, $LivingAttackEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingAttackEvent"
import {$BlockEvent$EntityPlaceEvent, $BlockEvent$EntityPlaceEvent$Type} from "packages/net/minecraftforge/event/level/$BlockEvent$EntityPlaceEvent"
import {$PlayerEvent$PlayerLoggedInEvent, $PlayerEvent$PlayerLoggedInEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerLoggedInEvent"
import {$ServerStoppingEvent, $ServerStoppingEvent$Type} from "packages/net/minecraftforge/event/server/$ServerStoppingEvent"
import {$LevelEvent$Load, $LevelEvent$Load$Type} from "packages/net/minecraftforge/event/level/$LevelEvent$Load"
import {$ExplosionEvent$Detonate, $ExplosionEvent$Detonate$Type} from "packages/net/minecraftforge/event/level/$ExplosionEvent$Detonate"
import {$ServerStoppedEvent, $ServerStoppedEvent$Type} from "packages/net/minecraftforge/event/server/$ServerStoppedEvent"
import {$TickEvent$PlayerTickEvent, $TickEvent$PlayerTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$PlayerTickEvent"
import {$EntityItemPickupEvent, $EntityItemPickupEvent$Type} from "packages/net/minecraftforge/event/entity/player/$EntityItemPickupEvent"
import {$PlayerInteractEvent$EntityInteract, $PlayerInteractEvent$EntityInteract$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$EntityInteract"
import {$MobSpawnEvent$FinalizeSpawn, $MobSpawnEvent$FinalizeSpawn$Type} from "packages/net/minecraftforge/event/entity/living/$MobSpawnEvent$FinalizeSpawn"
import {$TickEvent$ServerTickEvent, $TickEvent$ServerTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ServerTickEvent"
import {$LootTableLoadEvent, $LootTableLoadEvent$Type} from "packages/net/minecraftforge/event/$LootTableLoadEvent"
import {$EntityJoinLevelEvent, $EntityJoinLevelEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityJoinLevelEvent"
import {$PlayerInteractEvent$LeftClickBlock, $PlayerInteractEvent$LeftClickBlock$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$LeftClickBlock"
import {$LevelEvent$Save, $LevelEvent$Save$Type} from "packages/net/minecraftforge/event/level/$LevelEvent$Save"
import {$LevelEvent$Unload, $LevelEvent$Unload$Type} from "packages/net/minecraftforge/event/level/$LevelEvent$Unload"
import {$ItemTossEvent, $ItemTossEvent$Type} from "packages/net/minecraftforge/event/entity/item/$ItemTossEvent"
import {$LivingDeathEvent, $LivingDeathEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingDeathEvent"
import {$CommandEvent, $CommandEvent$Type} from "packages/net/minecraftforge/event/$CommandEvent"
import {$PlayerContainerEvent$Open, $PlayerContainerEvent$Open$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerContainerEvent$Open"
import {$PlayerEvent$Clone, $PlayerEvent$Clone$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$Clone"
import {$AdvancementEvent$AdvancementEarnEvent, $AdvancementEvent$AdvancementEarnEvent$Type} from "packages/net/minecraftforge/event/entity/player/$AdvancementEvent$AdvancementEarnEvent"
import {$ServerChatEvent, $ServerChatEvent$Type} from "packages/net/minecraftforge/event/$ServerChatEvent"
import {$AnimalTameEvent, $AnimalTameEvent$Type} from "packages/net/minecraftforge/event/entity/living/$AnimalTameEvent"
import {$FillBucketEvent, $FillBucketEvent$Type} from "packages/net/minecraftforge/event/entity/player/$FillBucketEvent"
import {$BlockEvent$FarmlandTrampleEvent, $BlockEvent$FarmlandTrampleEvent$Type} from "packages/net/minecraftforge/event/level/$BlockEvent$FarmlandTrampleEvent"
import {$PlayerEvent$ItemSmeltedEvent, $PlayerEvent$ItemSmeltedEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$ItemSmeltedEvent"
import {$PlayerEvent$ItemPickupEvent, $PlayerEvent$ItemPickupEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$ItemPickupEvent"
import {$WeakReference, $WeakReference$Type} from "packages/java/lang/ref/$WeakReference"
import {$PlayerContainerEvent$Close, $PlayerContainerEvent$Close$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerContainerEvent$Close"
import {$PlayerInteractEvent$RightClickBlock, $PlayerInteractEvent$RightClickBlock$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$RightClickBlock"
import {$PlayerEvent$PlayerChangedDimensionEvent, $PlayerEvent$PlayerChangedDimensionEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerChangedDimensionEvent"
import {$ServerAboutToStartEvent, $ServerAboutToStartEvent$Type} from "packages/net/minecraftforge/event/server/$ServerAboutToStartEvent"
import {$ServerStartedEvent, $ServerStartedEvent$Type} from "packages/net/minecraftforge/event/server/$ServerStartedEvent"
import {$ExplosionEvent$Start, $ExplosionEvent$Start$Type} from "packages/net/minecraftforge/event/level/$ExplosionEvent$Start"
import {$LootDataManager, $LootDataManager$Type} from "packages/net/minecraft/world/level/storage/loot/$LootDataManager"
import {$PlayerEvent$PlayerRespawnEvent, $PlayerEvent$PlayerRespawnEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerRespawnEvent"
import {$TickEvent$LevelTickEvent, $TickEvent$LevelTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$LevelTickEvent"

export class $EventHandlerImplCommon {
static "lootDataManagerRef": $WeakReference<($LootDataManager)>

constructor()

public static "event"(event: $FillBucketEvent$Type): void
public static "event"(event: $AnimalTameEvent$Type): void
public static "event"(event: $PlayerEvent$ItemCraftedEvent$Type): void
public static "event"(event: $PlayerEvent$ItemSmeltedEvent$Type): void
public static "event"(event: $EntityItemPickupEvent$Type): void
public static "event"(event: $AdvancementEvent$AdvancementEarnEvent$Type): void
public static "event"(event: $LivingAttackEvent$Type): void
public static "event"(event: $TickEvent$ServerTickEvent$Type): void
public static "event"(event: $BlockEvent$FarmlandTrampleEvent$Type): void
public static "event"(event: $ServerAboutToStartEvent$Type): void
public static "event"(event: $PlayerEvent$PlayerChangedDimensionEvent$Type): void
public static "event"(event: $LootTableLoadEvent$Type): void
public static "event"(event: $AttackEntityEvent$Type): void
public static "event"(event: $EntityJoinLevelEvent$Type): void
public static "event"(event: $PlayerEvent$ItemPickupEvent$Type): void
public static "event"(event: $ItemTossEvent$Type): void
public static "event"(event: $BlockEvent$BreakEvent$Type): void
public static "event"(event: $BlockEvent$EntityPlaceEvent$Type): void
public static "event"(event: $ServerChatEvent$Type): void
public static "event"(event: $PlayerEvent$PlayerLoggedInEvent$Type): void
public static "event"(event: $PlayerEvent$PlayerLoggedOutEvent$Type): void
public static "event"(event: $RegisterCommandsEvent$Type): void
public static "event"(event: $TickEvent$PlayerTickEvent$Type): void
public static "event"(event: $CommandEvent$Type): void
public static "event"(event: $PlayerEvent$PlayerRespawnEvent$Type): void
public static "event"(event: $TickEvent$LevelTickEvent$Type): void
public static "event"(event: $ServerStartingEvent$Type): void
public static "event"(event: $ServerStartedEvent$Type): void
public static "event"(event: $LivingDeathEvent$Type): void
public static "event"(event: $ServerStoppingEvent$Type): void
public static "event"(event: $ServerStoppedEvent$Type): void
public static "eventLivingSpawnEvent"(event: $MobSpawnEvent$FinalizeSpawn$Type): void
public static "eventPlayerContainerEvent"(event: $PlayerContainerEvent$Close$Type): void
public static "eventPlayerContainerEvent"(event: $PlayerContainerEvent$Open$Type): void
public static "eventChunkDataEvent"(event: $ChunkDataEvent$Save$Type): void
public static "eventChunkDataEvent"(event: $ChunkDataEvent$Load$Type): void
public static "eventExplosionEvent"(event: $ExplosionEvent$Detonate$Type): void
public static "eventExplosionEvent"(event: $ExplosionEvent$Start$Type): void
public static "eventPlayerEvent"(event: $PlayerEvent$Clone$Type): void
public static "eventAfter"(event: $ServerChatEvent$Type): void
public static "eventWorldEvent"(event: $LevelEvent$Unload$Type): void
public static "eventWorldEvent"(event: $LevelEvent$Save$Type): void
public static "eventWorldEvent"(event: $LevelEvent$Load$Type): void
public static "eventPlayerInteractEvent"(event: $PlayerInteractEvent$EntityInteract$Type): void
public static "eventPlayerInteractEvent"(event: $PlayerInteractEvent$RightClickItem$Type): void
public static "eventPlayerInteractEvent"(event: $PlayerInteractEvent$RightClickBlock$Type): void
public static "eventPlayerInteractEvent"(event: $PlayerInteractEvent$LeftClickBlock$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventHandlerImplCommon$Type = ($EventHandlerImplCommon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventHandlerImplCommon_ = $EventHandlerImplCommon$Type;
}}
declare module "packages/dev/architectury/event/events/common/$LightningEvent" {
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$LightningEvent$Strike, $LightningEvent$Strike$Type} from "packages/dev/architectury/event/events/common/$LightningEvent$Strike"

export interface $LightningEvent {

}

export namespace $LightningEvent {
const STRIKE: $Event<($LightningEvent$Strike)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightningEvent$Type = ($LightningEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightningEvent_ = $LightningEvent$Type;
}}
declare module "packages/dev/architectury/registry/fuel/$FuelRegistry" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"

export class $FuelRegistry {


public static "get"(stack: $ItemStack$Type): integer
public static "register"(time: integer, ...items: ($ItemLike$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FuelRegistry$Type = ($FuelRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FuelRegistry_ = $FuelRegistry$Type;
}}
declare module "packages/dev/architectury/registry/client/keymappings/forge/$KeyMappingRegistryImpl" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$RegisterKeyMappingsEvent, $RegisterKeyMappingsEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterKeyMappingsEvent"

export class $KeyMappingRegistryImpl {

constructor()

public static "register"(mapping: $KeyMapping$Type): void
public static "event"(event: $RegisterKeyMappingsEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyMappingRegistryImpl$Type = ($KeyMappingRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyMappingRegistryImpl_ = $KeyMappingRegistryImpl$Type;
}}
declare module "packages/dev/architectury/extensions/injected/$InjectedItemPropertiesExtension" {
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$DeferredSupplier, $DeferredSupplier$Type} from "packages/dev/architectury/registry/registries/$DeferredSupplier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export interface $InjectedItemPropertiesExtension {

 "arch$tab"(tab: $CreativeModeTab$Type): $Item$Properties
 "arch$tab"(tab: $DeferredSupplier$Type<($CreativeModeTab$Type)>): $Item$Properties
 "arch$tab"(tab: $ResourceKey$Type<($CreativeModeTab$Type)>): $Item$Properties
}

export namespace $InjectedItemPropertiesExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectedItemPropertiesExtension$Type = ($InjectedItemPropertiesExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectedItemPropertiesExtension_ = $InjectedItemPropertiesExtension$Type;
}}
declare module "packages/dev/architectury/hooks/level/$ExplosionHooks" {
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Explosion, $Explosion$Type} from "packages/net/minecraft/world/level/$Explosion"

export class $ExplosionHooks {


public static "getPosition"(explosion: $Explosion$Type): $Vec3
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExplosionHooks$Type = ($ExplosionHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExplosionHooks_ = $ExplosionHooks$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientScreenInputEvent$MouseClicked" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"

export interface $ClientScreenInputEvent$MouseClicked {

 "mouseClicked"(arg0: $Minecraft$Type, arg1: $Screen$Type, arg2: double, arg3: double, arg4: integer): $EventResult

(arg0: $Minecraft$Type, arg1: $Screen$Type, arg2: double, arg3: double, arg4: integer): $EventResult
}

export namespace $ClientScreenInputEvent$MouseClicked {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientScreenInputEvent$MouseClicked$Type = ($ClientScreenInputEvent$MouseClicked);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientScreenInputEvent$MouseClicked_ = $ClientScreenInputEvent$MouseClicked$Type;
}}
declare module "packages/dev/architectury/registry/level/entity/trade/$TradeRegistry" {
import {$VillagerTrades$ItemListing, $VillagerTrades$ItemListing$Type} from "packages/net/minecraft/world/entity/npc/$VillagerTrades$ItemListing"
import {$VillagerProfession, $VillagerProfession$Type} from "packages/net/minecraft/world/entity/npc/$VillagerProfession"

export class $TradeRegistry {


public static "registerTradeForWanderingTrader"(rare: boolean, ...trades: ($VillagerTrades$ItemListing$Type)[]): void
public static "registerVillagerTrade"(profession: $VillagerProfession$Type, level: integer, ...trades: ($VillagerTrades$ItemListing$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TradeRegistry$Type = ($TradeRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TradeRegistry_ = $TradeRegistry$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientRawInputEvent$MouseScrolled" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"

export interface $ClientRawInputEvent$MouseScrolled {

 "mouseScrolled"(arg0: $Minecraft$Type, arg1: double): $EventResult

(arg0: $Minecraft$Type, arg1: double): $EventResult
}

export namespace $ClientRawInputEvent$MouseScrolled {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientRawInputEvent$MouseScrolled$Type = ($ClientRawInputEvent$MouseScrolled);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientRawInputEvent$MouseScrolled_ = $ClientRawInputEvent$MouseScrolled$Type;
}}
declare module "packages/dev/architectury/networking/simple/$Message" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$NetworkManager$PacketContext, $NetworkManager$PacketContext$Type} from "packages/dev/architectury/networking/$NetworkManager$PacketContext"
import {$MessageType, $MessageType$Type} from "packages/dev/architectury/networking/simple/$MessageType"

export class $Message {


public "write"(arg0: $FriendlyByteBuf$Type): void
public "getType"(): $MessageType
public "handle"(arg0: $NetworkManager$PacketContext$Type): void
public "toPacket"(): $Packet<(any)>
get "type"(): $MessageType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Message$Type = ($Message);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Message_ = $Message$Type;
}}
declare module "packages/dev/architectury/registry/registries/$RegistrySupplier" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$RegistrarManager, $RegistrarManager$Type} from "packages/dev/architectury/registry/registries/$RegistrarManager"
import {$DeferredSupplier, $DeferredSupplier$Type} from "packages/dev/architectury/registry/registries/$DeferredSupplier"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$Registrar, $Registrar$Type} from "packages/dev/architectury/registry/registries/$Registrar"

export interface $RegistrySupplier<T> extends $DeferredSupplier<(T)> {

 "listen"(callback: $Consumer$Type<(T)>): void
 "getRegistrar"(): $Registrar<(T)>
 "getRegistrarManager"(): $RegistrarManager
 "getKey"(): $ResourceKey<(T)>
 "getId"(): $ResourceLocation
 "getRegistryId"(): $ResourceLocation
 "getRegistryKey"(): $ResourceKey<($Registry<(T)>)>
 "stream"(): $Stream<(T)>
 "isPresent"(): boolean
 "orElse"(other: T): T
 "ifPresent"(action: $Consumer$Type<(any)>): void
 "ifPresentOrElse"(action: $Consumer$Type<(any)>, emptyAction: $Runnable$Type): void
 "orElseGet"(supplier: $Supplier$Type<(any)>): T
 "toOptional"(): $Optional<(T)>
 "getOrNull"(): T
 "get"(): T
}

export namespace $RegistrySupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrySupplier$Type<T> = ($RegistrySupplier<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrySupplier_<T> = $RegistrySupplier$Type<(T)>;
}}
declare module "packages/dev/architectury/event/events/common/$TickEvent" {
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$TickEvent$Player, $TickEvent$Player$Type} from "packages/dev/architectury/event/events/common/$TickEvent$Player"
import {$TickEvent$ServerLevelTick, $TickEvent$ServerLevelTick$Type} from "packages/dev/architectury/event/events/common/$TickEvent$ServerLevelTick"
import {$TickEvent$Server, $TickEvent$Server$Type} from "packages/dev/architectury/event/events/common/$TickEvent$Server"

export interface $TickEvent<T> {

 "tick"(arg0: T): void

(arg0: T): void
}

export namespace $TickEvent {
const SERVER_PRE: $Event<($TickEvent$Server)>
const SERVER_POST: $Event<($TickEvent$Server)>
const SERVER_LEVEL_PRE: $Event<($TickEvent$ServerLevelTick)>
const SERVER_LEVEL_POST: $Event<($TickEvent$ServerLevelTick)>
const PLAYER_PRE: $Event<($TickEvent$Player)>
const PLAYER_POST: $Event<($TickEvent$Player)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickEvent$Type<T> = ($TickEvent<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickEvent_<T> = $TickEvent$Type<(T)>;
}}
declare module "packages/dev/architectury/networking/$NetworkChannel" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$NetworkManager$Side, $NetworkManager$Side$Type} from "packages/dev/architectury/networking/$NetworkManager$Side"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$NetworkManager$PacketContext, $NetworkManager$PacketContext$Type} from "packages/dev/architectury/networking/$NetworkManager$PacketContext"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export class $NetworkChannel {


public "register"<T>(type: $Class$Type<(T)>, encoder: $BiConsumer$Type<(T), ($FriendlyByteBuf$Type)>, decoder: $Function$Type<($FriendlyByteBuf$Type), (T)>, messageConsumer: $BiConsumer$Type<(T), ($Supplier$Type<($NetworkManager$PacketContext$Type)>)>): void
public static "create"(id: $ResourceLocation$Type): $NetworkChannel
public "sendToPlayer"<T>(player: $ServerPlayer$Type, message: T): void
public "toPacket"<T>(side: $NetworkManager$Side$Type, message: T): $Packet<(any)>
public "canPlayerReceive"<T>(player: $ServerPlayer$Type, type: $Class$Type<(T)>): boolean
public "sendToPlayers"<T>(players: $Iterable$Type<($ServerPlayer$Type)>, message: T): void
public "canServerReceive"<T>(type: $Class$Type<(T)>): boolean
public static "hashCodeString"(str: string): long
public "sendToServer"<T>(message: T): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkChannel$Type = ($NetworkChannel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkChannel_ = $NetworkChannel$Type;
}}
declare module "packages/dev/architectury/registry/level/entity/$EntityAttributeRegistry" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AttributeSupplier$Builder, $AttributeSupplier$Builder$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeSupplier$Builder"

export class $EntityAttributeRegistry {


public static "register"(type: $Supplier$Type<(any)>, attribute: $Supplier$Type<($AttributeSupplier$Builder$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityAttributeRegistry$Type = ($EntityAttributeRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityAttributeRegistry_ = $EntityAttributeRegistry$Type;
}}
declare module "packages/dev/architectury/event/events/common/$PlayerEvent$PlayerQuit" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"

export interface $PlayerEvent$PlayerQuit {

 "quit"(arg0: $ServerPlayer$Type): void

(arg0: $ServerPlayer$Type): void
}

export namespace $PlayerEvent$PlayerQuit {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvent$PlayerQuit$Type = ($PlayerEvent$PlayerQuit);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvent$PlayerQuit_ = $PlayerEvent$PlayerQuit$Type;
}}
declare module "packages/dev/architectury/hooks/item/tool/$ShovelItemHooks" {
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $ShovelItemHooks {


public static "addFlattenable"(input: $Block$Type, result: $BlockState$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShovelItemHooks$Type = ($ShovelItemHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShovelItemHooks_ = $ShovelItemHooks$Type;
}}
declare module "packages/dev/architectury/registry/menu/$MenuRegistry$ExtendedMenuTypeFactory" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export interface $MenuRegistry$ExtendedMenuTypeFactory<T extends $AbstractContainerMenu> {

 "create"(arg0: integer, arg1: $Inventory$Type, arg2: $FriendlyByteBuf$Type): T

(arg0: integer, arg1: $Inventory$Type, arg2: $FriendlyByteBuf$Type): T
}

export namespace $MenuRegistry$ExtendedMenuTypeFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuRegistry$ExtendedMenuTypeFactory$Type<T> = ($MenuRegistry$ExtendedMenuTypeFactory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuRegistry$ExtendedMenuTypeFactory_<T> = $MenuRegistry$ExtendedMenuTypeFactory$Type<(T)>;
}}
declare module "packages/dev/architectury/utils/value/$FloatSupplier" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $FloatSupplier {

 "getAsFloat"(): float

(): float
}

export namespace $FloatSupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FloatSupplier$Type = ($FloatSupplier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FloatSupplier_ = $FloatSupplier$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientPlayerEvent$ClientPlayerJoin" {
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"

export interface $ClientPlayerEvent$ClientPlayerJoin {

 "join"(arg0: $LocalPlayer$Type): void

(arg0: $LocalPlayer$Type): void
}

export namespace $ClientPlayerEvent$ClientPlayerJoin {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientPlayerEvent$ClientPlayerJoin$Type = ($ClientPlayerEvent$ClientPlayerJoin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientPlayerEvent$ClientPlayerJoin_ = $ClientPlayerEvent$ClientPlayerJoin$Type;
}}
declare module "packages/dev/architectury/utils/$OptionalSupplier" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export interface $OptionalSupplier<T> extends $Supplier<(T)> {

 "stream"(): $Stream<(T)>
 "isPresent"(): boolean
 "orElse"(other: T): T
 "ifPresent"(action: $Consumer$Type<(any)>): void
 "ifPresentOrElse"(action: $Consumer$Type<(any)>, emptyAction: $Runnable$Type): void
 "orElseGet"(supplier: $Supplier$Type<(any)>): T
 "toOptional"(): $Optional<(T)>
 "getOrNull"(): T
 "get"(): T
}

export namespace $OptionalSupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionalSupplier$Type<T> = ($OptionalSupplier<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionalSupplier_<T> = $OptionalSupplier$Type<(T)>;
}}
declare module "packages/dev/architectury/registry/registries/$RegistrarBuilder" {
import {$RegistrarOption, $RegistrarOption$Type} from "packages/dev/architectury/registry/registries/options/$RegistrarOption"
import {$Registrar, $Registrar$Type} from "packages/dev/architectury/registry/registries/$Registrar"

export interface $RegistrarBuilder<T> {

 "option"(arg0: $RegistrarOption$Type): $RegistrarBuilder<(T)>
 "build"(): $Registrar<(T)>
 "syncToClients"(): $RegistrarBuilder<(T)>
 "saveToDisc"(): $RegistrarBuilder<(T)>
}

export namespace $RegistrarBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrarBuilder$Type<T> = ($RegistrarBuilder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrarBuilder_<T> = $RegistrarBuilder$Type<(T)>;
}}
declare module "packages/dev/architectury/event/events/common/$ChatEvent$Received" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"

export interface $ChatEvent$Received {

 "received"(arg0: $ServerPlayer$Type, arg1: $Component$Type): $EventResult

(arg0: $ServerPlayer$Type, arg1: $Component$Type): $EventResult
}

export namespace $ChatEvent$Received {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChatEvent$Received$Type = ($ChatEvent$Received);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChatEvent$Received_ = $ChatEvent$Received$Type;
}}
declare module "packages/dev/architectury/hooks/level/entity/$PlayerHooks" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export class $PlayerHooks {


public static "isFake"(player: $Player$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerHooks$Type = ($PlayerHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerHooks_ = $PlayerHooks$Type;
}}
declare module "packages/dev/architectury/hooks/item/tool/$HoeItemHooks" {
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $HoeItemHooks {


public static "addTillable"(input: $Block$Type, predicate: $Predicate$Type<($UseOnContext$Type)>, action: $Consumer$Type<($UseOnContext$Type)>, newState: $Function$Type<($UseOnContext$Type), ($BlockState$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HoeItemHooks$Type = ($HoeItemHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HoeItemHooks_ = $HoeItemHooks$Type;
}}
declare module "packages/dev/architectury/registry/client/rendering/$ColorHandlerRegistry" {
import {$BlockColor, $BlockColor$Type} from "packages/net/minecraft/client/color/block/$BlockColor"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$ItemColor, $ItemColor$Type} from "packages/net/minecraft/client/color/item/$ItemColor"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $ColorHandlerRegistry {


public static "registerItemColors"(color: $ItemColor$Type, ...items: ($Supplier$Type<(any)>)[]): void
public static "registerItemColors"(color: $ItemColor$Type, ...items: ($ItemLike$Type)[]): void
public static "registerBlockColors"(color: $BlockColor$Type, ...blocks: ($Supplier$Type<(any)>)[]): void
public static "registerBlockColors"(color: $BlockColor$Type, ...blocks: ($Block$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorHandlerRegistry$Type = ($ColorHandlerRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorHandlerRegistry_ = $ColorHandlerRegistry$Type;
}}
declare module "packages/dev/architectury/registry/$CreativeTabRegistry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$DeferredSupplier, $DeferredSupplier$Type} from "packages/dev/architectury/registry/registries/$DeferredSupplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$CreativeTabRegistry$ModifyTabCallback, $CreativeTabRegistry$ModifyTabCallback$Type} from "packages/dev/architectury/registry/$CreativeTabRegistry$ModifyTabCallback"
import {$CreativeModeTab$Builder, $CreativeModeTab$Builder$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$Builder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $CreativeTabRegistry {


public static "append"<I extends $ItemLike, T extends $Supplier<(I)>>(tab: $ResourceKey$Type<($CreativeModeTab$Type)>, ...items: (T)[]): void
public static "append"(tab: $DeferredSupplier$Type<($CreativeModeTab$Type)>, ...items: ($ItemLike$Type)[]): void
public static "append"<I extends $ItemLike, T extends $Supplier<(I)>>(tab: $DeferredSupplier$Type<($CreativeModeTab$Type)>, ...items: (T)[]): void
public static "append"(tab: $ResourceKey$Type<($CreativeModeTab$Type)>, ...items: ($ItemLike$Type)[]): void
public static "create"(callback: $Consumer$Type<($CreativeModeTab$Builder$Type)>): $CreativeModeTab
public static "create"(title: $Component$Type, icon: $Supplier$Type<($ItemStack$Type)>): $CreativeModeTab
public static "defer"(name: $ResourceKey$Type<($CreativeModeTab$Type)>): $DeferredSupplier<($CreativeModeTab)>
public static "defer"(name: $ResourceLocation$Type): $DeferredSupplier<($CreativeModeTab)>
public static "modify"(tab: $DeferredSupplier$Type<($CreativeModeTab$Type)>, filler: $CreativeTabRegistry$ModifyTabCallback$Type): void
public static "appendBuiltin"<I extends $ItemLike, T extends $Supplier<(I)>>(tab: $CreativeModeTab$Type, ...items: (T)[]): void
public static "appendBuiltin"(tab: $CreativeModeTab$Type, ...items: ($ItemLike$Type)[]): void
public static "modifyBuiltin"(tab: $CreativeModeTab$Type, filler: $CreativeTabRegistry$ModifyTabCallback$Type): void
public static "appendBuiltinStack"(tab: $CreativeModeTab$Type, ...items: ($ItemStack$Type)[]): void
public static "appendBuiltinStack"(tab: $CreativeModeTab$Type, ...items: ($Supplier$Type<($ItemStack$Type)>)[]): void
public static "ofBuiltin"(tab: $CreativeModeTab$Type): $DeferredSupplier<($CreativeModeTab)>
public static "appendStack"(tab: $ResourceKey$Type<($CreativeModeTab$Type)>, ...items: ($ItemStack$Type)[]): void
public static "appendStack"(tab: $ResourceKey$Type<($CreativeModeTab$Type)>, items: $Stream$Type<($Supplier$Type<($ItemStack$Type)>)>): void
public static "appendStack"(tab: $ResourceKey$Type<($CreativeModeTab$Type)>, ...items: ($Supplier$Type<($ItemStack$Type)>)[]): void
public static "appendStack"(tab: $ResourceKey$Type<($CreativeModeTab$Type)>, item: $Supplier$Type<($ItemStack$Type)>): void
public static "appendStack"(tab: $DeferredSupplier$Type<($CreativeModeTab$Type)>, items: $Stream$Type<($Supplier$Type<($ItemStack$Type)>)>): void
public static "appendStack"(tab: $DeferredSupplier$Type<($CreativeModeTab$Type)>, ...items: ($Supplier$Type<($ItemStack$Type)>)[]): void
public static "appendStack"(tab: $DeferredSupplier$Type<($CreativeModeTab$Type)>, item: $Supplier$Type<($ItemStack$Type)>): void
public static "appendStack"(tab: $DeferredSupplier$Type<($CreativeModeTab$Type)>, ...items: ($ItemStack$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CreativeTabRegistry$Type = ($CreativeTabRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CreativeTabRegistry_ = $CreativeTabRegistry$Type;
}}
declare module "packages/dev/architectury/hooks/block/$BlockEntityHooks" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"

export class $BlockEntityHooks {


public static "syncData"(entity: $BlockEntity$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityHooks$Type = ($BlockEntityHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityHooks_ = $BlockEntityHooks$Type;
}}
declare module "packages/dev/architectury/annotations/$ForgeEvent" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $ForgeEvent extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $ForgeEvent {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeEvent$Type = ($ForgeEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeEvent_ = $ForgeEvent$Type;
}}
declare module "packages/dev/architectury/networking/forge/$NetworkManagerImpl" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$PlayerEvent$PlayerLoggedInEvent, $PlayerEvent$PlayerLoggedInEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerLoggedInEvent"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$PacketSink, $PacketSink$Type} from "packages/dev/architectury/networking/transformers/$PacketSink"
import {$PacketTransformer, $PacketTransformer$Type} from "packages/dev/architectury/networking/transformers/$PacketTransformer"
import {$ClientGamePacketListener, $ClientGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ClientGamePacketListener"
import {$NetworkManager$Side, $NetworkManager$Side$Type} from "packages/dev/architectury/networking/$NetworkManager$Side"
import {$PlayerEvent$PlayerLoggedOutEvent, $PlayerEvent$PlayerLoggedOutEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerLoggedOutEvent"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NetworkManager$NetworkReceiver, $NetworkManager$NetworkReceiver$Type} from "packages/dev/architectury/networking/$NetworkManager$NetworkReceiver"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $NetworkManagerImpl {

constructor()

public static "loggedOut"(event: $PlayerEvent$PlayerLoggedOutEvent$Type): void
public static "loggedIn"(event: $PlayerEvent$PlayerLoggedInEvent$Type): void
public static "createAddEntityPacket"(entity: $Entity$Type): $Packet<($ClientGamePacketListener)>
public static "toPacket"(side: $NetworkManager$Side$Type, id: $ResourceLocation$Type, buffer: $FriendlyByteBuf$Type): $Packet<(any)>
public static "registerC2SReceiver"(id: $ResourceLocation$Type, packetTransformers: $List$Type<($PacketTransformer$Type)>, receiver: $NetworkManager$NetworkReceiver$Type): void
public static "registerS2CReceiver"(id: $ResourceLocation$Type, packetTransformers: $List$Type<($PacketTransformer$Type)>, receiver: $NetworkManager$NetworkReceiver$Type): void
public static "registerReceiver"(side: $NetworkManager$Side$Type, id: $ResourceLocation$Type, packetTransformers: $List$Type<($PacketTransformer$Type)>, receiver: $NetworkManager$NetworkReceiver$Type): void
public static "collectPackets"(sink: $PacketSink$Type, side: $NetworkManager$Side$Type, id: $ResourceLocation$Type, buf: $FriendlyByteBuf$Type): void
public static "canPlayerReceive"(player: $ServerPlayer$Type, id: $ResourceLocation$Type): boolean
public static "canServerReceive"(id: $ResourceLocation$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkManagerImpl$Type = ($NetworkManagerImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkManagerImpl_ = $NetworkManagerImpl$Type;
}}
declare module "packages/dev/architectury/event/events/common/$CommandPerformEvent" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$EventActor, $EventActor$Type} from "packages/dev/architectury/event/$EventActor"
import {$ParseResults, $ParseResults$Type} from "packages/com/mojang/brigadier/$ParseResults"

export class $CommandPerformEvent {
static readonly "EVENT": $Event<($EventActor<($CommandPerformEvent)>)>

constructor(results: $ParseResults$Type<($CommandSourceStack$Type)>, throwable: $Throwable$Type)

public "getThrowable"(): $Throwable
public "getResults"(): $ParseResults<($CommandSourceStack)>
public "setResults"(results: $ParseResults$Type<($CommandSourceStack$Type)>): void
public "setThrowable"(throwable: $Throwable$Type): void
get "throwable"(): $Throwable
get "results"(): $ParseResults<($CommandSourceStack)>
set "results"(value: $ParseResults$Type<($CommandSourceStack$Type)>)
set "throwable"(value: $Throwable$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommandPerformEvent$Type = ($CommandPerformEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommandPerformEvent_ = $CommandPerformEvent$Type;
}}
declare module "packages/dev/architectury/event/events/common/$EntityEvent$LivingDeath" {
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $EntityEvent$LivingDeath {

 "die"(arg0: $LivingEntity$Type, arg1: $DamageSource$Type): $EventResult

(arg0: $LivingEntity$Type, arg1: $DamageSource$Type): $EventResult
}

export namespace $EntityEvent$LivingDeath {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityEvent$LivingDeath$Type = ($EntityEvent$LivingDeath);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityEvent$LivingDeath_ = $EntityEvent$LivingDeath$Type;
}}
declare module "packages/dev/architectury/registry/client/gui/$ClientTooltipComponentRegistry" {
import {$TooltipComponent, $TooltipComponent$Type} from "packages/net/minecraft/world/inventory/tooltip/$TooltipComponent"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $ClientTooltipComponentRegistry {


public static "register"<T extends $TooltipComponent>(clazz: $Class$Type<(T)>, factory: $Function$Type<(any), (any)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTooltipComponentRegistry$Type = ($ClientTooltipComponentRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTooltipComponentRegistry_ = $ClientTooltipComponentRegistry$Type;
}}
declare module "packages/dev/architectury/event/events/common/$LifecycleEvent" {
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$LifecycleEvent$ServerLevelState, $LifecycleEvent$ServerLevelState$Type} from "packages/dev/architectury/event/events/common/$LifecycleEvent$ServerLevelState"
import {$LifecycleEvent$ServerState, $LifecycleEvent$ServerState$Type} from "packages/dev/architectury/event/events/common/$LifecycleEvent$ServerState"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export interface $LifecycleEvent {

}

export namespace $LifecycleEvent {
const SERVER_BEFORE_START: $Event<($LifecycleEvent$ServerState)>
const SERVER_STARTING: $Event<($LifecycleEvent$ServerState)>
const SERVER_STARTED: $Event<($LifecycleEvent$ServerState)>
const SERVER_STOPPING: $Event<($LifecycleEvent$ServerState)>
const SERVER_STOPPED: $Event<($LifecycleEvent$ServerState)>
const SERVER_LEVEL_LOAD: $Event<($LifecycleEvent$ServerLevelState)>
const SERVER_LEVEL_UNLOAD: $Event<($LifecycleEvent$ServerLevelState)>
const SERVER_LEVEL_SAVE: $Event<($LifecycleEvent$ServerLevelState)>
const SETUP: $Event<($Runnable)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LifecycleEvent$Type = ($LifecycleEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LifecycleEvent_ = $LifecycleEvent$Type;
}}
declare module "packages/dev/architectury/hooks/level/biome/$SpawnProperties" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$MobSpawnSettings$SpawnerData, $MobSpawnSettings$SpawnerData$Type} from "packages/net/minecraft/world/level/biome/$MobSpawnSettings$SpawnerData"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MobSpawnSettings$MobSpawnCost, $MobSpawnSettings$MobSpawnCost$Type} from "packages/net/minecraft/world/level/biome/$MobSpawnSettings$MobSpawnCost"
import {$MobCategory, $MobCategory$Type} from "packages/net/minecraft/world/entity/$MobCategory"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $SpawnProperties {

 "getCreatureProbability"(): float
 "getSpawners"(): $Map<($MobCategory), ($List<($MobSpawnSettings$SpawnerData)>)>
 "getMobSpawnCosts"(): $Map<($EntityType<(any)>), ($MobSpawnSettings$MobSpawnCost)>
}

export namespace $SpawnProperties {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpawnProperties$Type = ($SpawnProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpawnProperties_ = $SpawnProperties$Type;
}}
declare module "packages/dev/architectury/forge/$ArchitecturyForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ArchitecturyForge {
static readonly "MOD_ID": string

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArchitecturyForge$Type = ($ArchitecturyForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArchitecturyForge_ = $ArchitecturyForge$Type;
}}
declare module "packages/dev/architectury/utils/value/$LongValue" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Value, $Value$Type} from "packages/dev/architectury/utils/value/$Value"
import {$LongConsumer, $LongConsumer$Type} from "packages/it/unimi/dsi/fastutil/longs/$LongConsumer"
import {$LongSupplier, $LongSupplier$Type} from "packages/java/util/function/$LongSupplier"

export interface $LongValue extends $Value<(long)>, $LongSupplier, $LongConsumer {

 "get"(): long
 "getAsLong"(): long
/**
 * 
 * @deprecated
 */
 "accept"(arg0: long): void
/**
 * 
 * @deprecated
 */
 "andThen"(arg0: $Consumer$Type<(any)>): $Consumer<(long)>
 "andThen"(arg0: $LongConsumer$Type): $LongConsumer
 "accept"(arg0: long): void
}

export namespace $LongValue {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongValue$Type = ($LongValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongValue_ = $LongValue$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientGuiEvent$ContainerScreenRenderBackground" {
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export interface $ClientGuiEvent$ContainerScreenRenderBackground {

 "render"(arg0: $AbstractContainerScreen$Type<(any)>, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: float): void

(arg0: $AbstractContainerScreen$Type<(any)>, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: float): void
}

export namespace $ClientGuiEvent$ContainerScreenRenderBackground {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientGuiEvent$ContainerScreenRenderBackground$Type = ($ClientGuiEvent$ContainerScreenRenderBackground);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientGuiEvent$ContainerScreenRenderBackground_ = $ClientGuiEvent$ContainerScreenRenderBackground$Type;
}}
declare module "packages/dev/architectury/event/forge/$EventHandlerImpl" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $EventHandlerImpl {

constructor()

public static "registerCommon"(): void
public static "registerClient"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventHandlerImpl$Type = ($EventHandlerImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventHandlerImpl_ = $EventHandlerImpl$Type;
}}
declare module "packages/dev/architectury/registry/level/biome/$BiomeModifications" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$BiomeProperties$Mutable, $BiomeProperties$Mutable$Type} from "packages/dev/architectury/hooks/level/biome/$BiomeProperties$Mutable"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BiomeModifications$BiomeContext, $BiomeModifications$BiomeContext$Type} from "packages/dev/architectury/registry/level/biome/$BiomeModifications$BiomeContext"

export class $BiomeModifications {

constructor()

public static "addProperties"(predicate: $Predicate$Type<($BiomeModifications$BiomeContext$Type)>, modifier: $BiConsumer$Type<($BiomeModifications$BiomeContext$Type), ($BiomeProperties$Mutable$Type)>): void
public static "addProperties"(modifier: $BiConsumer$Type<($BiomeModifications$BiomeContext$Type), ($BiomeProperties$Mutable$Type)>): void
public static "removeProperties"(predicate: $Predicate$Type<($BiomeModifications$BiomeContext$Type)>, modifier: $BiConsumer$Type<($BiomeModifications$BiomeContext$Type), ($BiomeProperties$Mutable$Type)>): void
public static "removeProperties"(modifier: $BiConsumer$Type<($BiomeModifications$BiomeContext$Type), ($BiomeProperties$Mutable$Type)>): void
public static "replaceProperties"(modifier: $BiConsumer$Type<($BiomeModifications$BiomeContext$Type), ($BiomeProperties$Mutable$Type)>): void
public static "replaceProperties"(predicate: $Predicate$Type<($BiomeModifications$BiomeContext$Type)>, modifier: $BiConsumer$Type<($BiomeModifications$BiomeContext$Type), ($BiomeProperties$Mutable$Type)>): void
public static "postProcessProperties"(modifier: $BiConsumer$Type<($BiomeModifications$BiomeContext$Type), ($BiomeProperties$Mutable$Type)>): void
public static "postProcessProperties"(predicate: $Predicate$Type<($BiomeModifications$BiomeContext$Type)>, modifier: $BiConsumer$Type<($BiomeModifications$BiomeContext$Type), ($BiomeProperties$Mutable$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeModifications$Type = ($BiomeModifications);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeModifications_ = $BiomeModifications$Type;
}}
declare module "packages/dev/architectury/registry/item/forge/$ItemPropertiesRegistryImpl" {
import {$ClampedItemPropertyFunction, $ClampedItemPropertyFunction$Type} from "packages/net/minecraft/client/renderer/item/$ClampedItemPropertyFunction"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ItemPropertiesRegistryImpl {

constructor()

public static "register"(item: $ItemLike$Type, propertyId: $ResourceLocation$Type, arg2: $ClampedItemPropertyFunction$Type): $ClampedItemPropertyFunction
public static "registerGeneric"(propertyId: $ResourceLocation$Type, arg1: $ClampedItemPropertyFunction$Type): $ClampedItemPropertyFunction
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemPropertiesRegistryImpl$Type = ($ItemPropertiesRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemPropertiesRegistryImpl_ = $ItemPropertiesRegistryImpl$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientReloadShadersEvent" {
import {$ResourceProvider, $ResourceProvider$Type} from "packages/net/minecraft/server/packs/resources/$ResourceProvider"
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$ClientReloadShadersEvent$ShadersSink, $ClientReloadShadersEvent$ShadersSink$Type} from "packages/dev/architectury/event/events/client/$ClientReloadShadersEvent$ShadersSink"

export interface $ClientReloadShadersEvent {

 "reload"(arg0: $ResourceProvider$Type, arg1: $ClientReloadShadersEvent$ShadersSink$Type): void

(arg0: $ResourceProvider$Type, arg1: $ClientReloadShadersEvent$ShadersSink$Type): void
}

export namespace $ClientReloadShadersEvent {
const EVENT: $Event<($ClientReloadShadersEvent)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientReloadShadersEvent$Type = ($ClientReloadShadersEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientReloadShadersEvent_ = $ClientReloadShadersEvent$Type;
}}
declare module "packages/dev/architectury/utils/$GameInstance" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export class $GameInstance {

constructor()

public static "getClient"(): $Minecraft
public static "getServer"(): $MinecraftServer
get "client"(): $Minecraft
get "server"(): $MinecraftServer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GameInstance$Type = ($GameInstance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GameInstance_ = $GameInstance$Type;
}}
declare module "packages/dev/architectury/hooks/level/biome/$GenerationProperties" {
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$PlacedFeature, $PlacedFeature$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacedFeature"
import {$GenerationStep$Decoration, $GenerationStep$Decoration$Type} from "packages/net/minecraft/world/level/levelgen/$GenerationStep$Decoration"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ConfiguredWorldCarver, $ConfiguredWorldCarver$Type} from "packages/net/minecraft/world/level/levelgen/carver/$ConfiguredWorldCarver"
import {$GenerationStep$Carving, $GenerationStep$Carving$Type} from "packages/net/minecraft/world/level/levelgen/$GenerationStep$Carving"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export interface $GenerationProperties {

 "getFeatures"(arg0: $GenerationStep$Decoration$Type): $Iterable<($Holder<($PlacedFeature)>)>
 "getFeatures"(): $List<($Iterable<($Holder<($PlacedFeature)>)>)>
 "getCarvers"(arg0: $GenerationStep$Carving$Type): $Iterable<($Holder<($ConfiguredWorldCarver<(any)>)>)>
}

export namespace $GenerationProperties {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenerationProperties$Type = ($GenerationProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenerationProperties_ = $GenerationProperties$Type;
}}
declare module "packages/dev/architectury/event/events/common/$BlockEvent" {
import {$BlockEvent$Break, $BlockEvent$Break$Type} from "packages/dev/architectury/event/events/common/$BlockEvent$Break"
import {$BlockEvent$FallingLand, $BlockEvent$FallingLand$Type} from "packages/dev/architectury/event/events/common/$BlockEvent$FallingLand"
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$BlockEvent$Place, $BlockEvent$Place$Type} from "packages/dev/architectury/event/events/common/$BlockEvent$Place"

export interface $BlockEvent {

}

export namespace $BlockEvent {
const BREAK: $Event<($BlockEvent$Break)>
const PLACE: $Event<($BlockEvent$Place)>
const FALLING_LAND: $Event<($BlockEvent$FallingLand)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEvent$Type = ($BlockEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEvent_ = $BlockEvent$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientLifecycleEvent" {
import {$ClientLifecycleEvent$ClientState, $ClientLifecycleEvent$ClientState$Type} from "packages/dev/architectury/event/events/client/$ClientLifecycleEvent$ClientState"
import {$Event, $Event$Type} from "packages/dev/architectury/event/$Event"
import {$ClientLifecycleEvent$ClientLevelState, $ClientLifecycleEvent$ClientLevelState$Type} from "packages/dev/architectury/event/events/client/$ClientLifecycleEvent$ClientLevelState"

export interface $ClientLifecycleEvent {

}

export namespace $ClientLifecycleEvent {
const CLIENT_STARTED: $Event<($ClientLifecycleEvent$ClientState)>
const CLIENT_STOPPING: $Event<($ClientLifecycleEvent$ClientState)>
const CLIENT_LEVEL_LOAD: $Event<($ClientLifecycleEvent$ClientLevelState)>
const CLIENT_SETUP: $Event<($ClientLifecycleEvent$ClientState)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientLifecycleEvent$Type = ($ClientLifecycleEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientLifecycleEvent_ = $ClientLifecycleEvent$Type;
}}
declare module "packages/dev/architectury/event/events/common/$EntityEvent$AnimalTame" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Animal, $Animal$Type} from "packages/net/minecraft/world/entity/animal/$Animal"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"

export interface $EntityEvent$AnimalTame {

 "tame"(arg0: $Animal$Type, arg1: $Player$Type): $EventResult

(arg0: $Animal$Type, arg1: $Player$Type): $EventResult
}

export namespace $EntityEvent$AnimalTame {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityEvent$AnimalTame$Type = ($EntityEvent$AnimalTame);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityEvent$AnimalTame_ = $EntityEvent$AnimalTame$Type;
}}
declare module "packages/dev/architectury/hooks/level/biome/forge/$BiomeHooksImpl" {
import {$Biome$ClimateSettings, $Biome$ClimateSettings$Type} from "packages/net/minecraft/world/level/biome/$Biome$ClimateSettings"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"

export class $BiomeHooksImpl {

constructor()

public static "extractClimateSettings"(biome: $Biome$Type): $Biome$ClimateSettings
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeHooksImpl$Type = ($BiomeHooksImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeHooksImpl_ = $BiomeHooksImpl$Type;
}}
declare module "packages/dev/architectury/registry/client/level/entity/$EntityRendererRegistry" {
import {$EntityRendererProvider, $EntityRendererProvider$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EntityRendererRegistry {


public static "register"<T extends $Entity>(type: $Supplier$Type<(any)>, provider: $EntityRendererProvider$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityRendererRegistry$Type = ($EntityRendererRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityRendererRegistry_ = $EntityRendererRegistry$Type;
}}
declare module "packages/dev/architectury/event/events/common/$BlockEvent$Break" {
import {$IntValue, $IntValue$Type} from "packages/dev/architectury/utils/value/$IntValue"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $BlockEvent$Break {

 "breakBlock"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $ServerPlayer$Type, arg4: $IntValue$Type): $EventResult

(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $ServerPlayer$Type, arg4: $IntValue$Type): $EventResult
}

export namespace $BlockEvent$Break {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEvent$Break$Type = ($BlockEvent$Break);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEvent$Break_ = $BlockEvent$Break$Type;
}}
declare module "packages/dev/architectury/impl/$TooltipEventColorContextImpl" {
import {$ClientTooltipEvent$ColorContext, $ClientTooltipEvent$ColorContext$Type} from "packages/dev/architectury/event/events/client/$ClientTooltipEvent$ColorContext"
import {$ThreadLocal, $ThreadLocal$Type} from "packages/java/lang/$ThreadLocal"

export class $TooltipEventColorContextImpl implements $ClientTooltipEvent$ColorContext {
static readonly "CONTEXT": $ThreadLocal<($TooltipEventColorContextImpl)>

constructor()

public "reset"(): $TooltipEventColorContextImpl
public "getBackgroundColor"(): integer
public "setBackgroundColor"(color: integer): void
public "setOutlineGradientTopColor"(color: integer): void
public "setOutlineGradientBottomColor"(color: integer): void
public "getOutlineGradientBottomColor"(): integer
public "getOutlineGradientTopColor"(): integer
get "backgroundColor"(): integer
set "backgroundColor"(value: integer)
set "outlineGradientTopColor"(value: integer)
set "outlineGradientBottomColor"(value: integer)
get "outlineGradientBottomColor"(): integer
get "outlineGradientTopColor"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TooltipEventColorContextImpl$Type = ($TooltipEventColorContextImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TooltipEventColorContextImpl_ = $TooltipEventColorContextImpl$Type;
}}
declare module "packages/dev/architectury/utils/$Env" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Dist, $Dist$Type} from "packages/net/minecraftforge/api/distmarker/$Dist"

export class $Env extends $Enum<($Env)> {
static readonly "CLIENT": $Env
static readonly "SERVER": $Env


public static "values"(): ($Env)[]
public static "valueOf"(name: string): $Env
public static "fromPlatform"(type: any): $Env
public "toPlatform"(): $Dist
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Env$Type = (("server") | ("client")) | ($Env);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Env_ = $Env$Type;
}}
declare module "packages/dev/architectury/event/events/common/$PlayerEvent$FillBucket" {
import {$HitResult, $HitResult$Type} from "packages/net/minecraft/world/phys/$HitResult"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$CompoundEventResult, $CompoundEventResult$Type} from "packages/dev/architectury/event/$CompoundEventResult"

export interface $PlayerEvent$FillBucket {

 "fill"(arg0: $Player$Type, arg1: $Level$Type, arg2: $ItemStack$Type, arg3: $HitResult$Type): $CompoundEventResult<($ItemStack)>

(arg0: $Player$Type, arg1: $Level$Type, arg2: $ItemStack$Type, arg3: $HitResult$Type): $CompoundEventResult<($ItemStack)>
}

export namespace $PlayerEvent$FillBucket {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvent$FillBucket$Type = ($PlayerEvent$FillBucket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvent$FillBucket_ = $PlayerEvent$FillBucket$Type;
}}
declare module "packages/dev/architectury/core/item/$ArchitecturyMobBucketItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$MobBucketItem, $MobBucketItem$Type} from "packages/net/minecraft/world/item/$MobBucketItem"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ArchitecturyMobBucketItem extends $MobBucketItem {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(entity: $Supplier$Type<(any)>, fluid: $Supplier$Type<(any)>, sound: $Supplier$Type<(any)>, properties: $Item$Properties$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArchitecturyMobBucketItem$Type = ($ArchitecturyMobBucketItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArchitecturyMobBucketItem_ = $ArchitecturyMobBucketItem$Type;
}}
declare module "packages/dev/architectury/registry/client/particle/$ParticleProviderRegistry$DeferredParticleProvider" {
import {$ParticleProvider, $ParticleProvider$Type} from "packages/net/minecraft/client/particle/$ParticleProvider"
import {$ParticleProviderRegistry$ExtendedSpriteSet, $ParticleProviderRegistry$ExtendedSpriteSet$Type} from "packages/dev/architectury/registry/client/particle/$ParticleProviderRegistry$ExtendedSpriteSet"
import {$ParticleOptions, $ParticleOptions$Type} from "packages/net/minecraft/core/particles/$ParticleOptions"

export interface $ParticleProviderRegistry$DeferredParticleProvider<T extends $ParticleOptions> {

 "create"(arg0: $ParticleProviderRegistry$ExtendedSpriteSet$Type): $ParticleProvider<(T)>

(arg0: $ParticleProviderRegistry$ExtendedSpriteSet$Type): $ParticleProvider<(T)>
}

export namespace $ParticleProviderRegistry$DeferredParticleProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleProviderRegistry$DeferredParticleProvider$Type<T> = ($ParticleProviderRegistry$DeferredParticleProvider<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleProviderRegistry$DeferredParticleProvider_<T> = $ParticleProviderRegistry$DeferredParticleProvider$Type<(T)>;
}}
declare module "packages/dev/architectury/event/$EventActor" {
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"

export interface $EventActor<T> {

 "act"(arg0: T): $EventResult

(arg0: T): $EventResult
}

export namespace $EventActor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventActor$Type<T> = ($EventActor<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventActor_<T> = $EventActor$Type<(T)>;
}}
declare module "packages/dev/architectury/event/events/common/$InteractionEvent$LeftClickBlock" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $InteractionEvent$LeftClickBlock {

 "click"(arg0: $Player$Type, arg1: $InteractionHand$Type, arg2: $BlockPos$Type, arg3: $Direction$Type): $EventResult

(arg0: $Player$Type, arg1: $InteractionHand$Type, arg2: $BlockPos$Type, arg3: $Direction$Type): $EventResult
}

export namespace $InteractionEvent$LeftClickBlock {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InteractionEvent$LeftClickBlock$Type = ($InteractionEvent$LeftClickBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InteractionEvent$LeftClickBlock_ = $InteractionEvent$LeftClickBlock$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientTickEvent$Client" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$ClientTickEvent, $ClientTickEvent$Type} from "packages/dev/architectury/event/events/client/$ClientTickEvent"

export interface $ClientTickEvent$Client extends $ClientTickEvent<($Minecraft)> {

 "tick"(arg0: $Minecraft$Type): void

(arg0: $Minecraft$Type): void
}

export namespace $ClientTickEvent$Client {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTickEvent$Client$Type = ($ClientTickEvent$Client);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTickEvent$Client_ = $ClientTickEvent$Client$Type;
}}
declare module "packages/dev/architectury/hooks/level/biome/$ClimateProperties" {
import {$Biome$TemperatureModifier, $Biome$TemperatureModifier$Type} from "packages/net/minecraft/world/level/biome/$Biome$TemperatureModifier"

export interface $ClimateProperties {

 "getDownfall"(): float
 "getTemperatureModifier"(): $Biome$TemperatureModifier
 "getTemperature"(): float
 "hasPrecipitation"(): boolean
}

export namespace $ClimateProperties {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClimateProperties$Type = ($ClimateProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClimateProperties_ = $ClimateProperties$Type;
}}
declare module "packages/dev/architectury/hooks/fluid/$FluidStackHooks" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$FluidStack, $FluidStack$Type} from "packages/dev/architectury/fluid/$FluidStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"

export class $FluidStackHooks {


public static "getName"(stack: $FluidStack$Type): $Component
public static "write"(stack: $FluidStack$Type, buf: $FriendlyByteBuf$Type): void
public static "write"(stack: $FluidStack$Type, tag: $CompoundTag$Type): $CompoundTag
public static "read"(buf: $FriendlyByteBuf$Type): $FluidStack
public static "read"(tag: $CompoundTag$Type): $FluidStack
public static "getFlowingTexture"(stack: $FluidStack$Type): $TextureAtlasSprite
public static "getFlowingTexture"(level: $BlockAndTintGetter$Type, pos: $BlockPos$Type, state: $FluidState$Type): $TextureAtlasSprite
public static "getFlowingTexture"(fluid: $Fluid$Type): $TextureAtlasSprite
public static "bucketAmount"(): long
public static "getLuminosity"(fluid: $FluidStack$Type, level: $Level$Type, pos: $BlockPos$Type): integer
public static "getLuminosity"(fluid: $Fluid$Type, level: $Level$Type, pos: $BlockPos$Type): integer
public static "getStillTexture"(stack: $FluidStack$Type): $TextureAtlasSprite
public static "getStillTexture"(fluid: $Fluid$Type): $TextureAtlasSprite
public static "getStillTexture"(level: $BlockAndTintGetter$Type, pos: $BlockPos$Type, state: $FluidState$Type): $TextureAtlasSprite
public static "getTemperature"(fluid: $FluidStack$Type, level: $Level$Type, pos: $BlockPos$Type): integer
public static "getTemperature"(fluid: $Fluid$Type, level: $Level$Type, pos: $BlockPos$Type): integer
public static "getColor"(level: $BlockAndTintGetter$Type, pos: $BlockPos$Type, state: $FluidState$Type): integer
public static "getColor"(stack: $FluidStack$Type): integer
public static "getColor"(fluid: $Fluid$Type): integer
public static "getViscosity"(fluid: $FluidStack$Type, level: $Level$Type, pos: $BlockPos$Type): integer
public static "getViscosity"(fluid: $Fluid$Type, level: $Level$Type, pos: $BlockPos$Type): integer
public static "getTranslationKey"(stack: $FluidStack$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidStackHooks$Type = ($FluidStackHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidStackHooks_ = $FluidStackHooks$Type;
}}
declare module "packages/dev/architectury/hooks/item/tool/forge/$HoeItemHooksImpl" {
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $HoeItemHooksImpl {

constructor()

public static "addTillable"(input: $Block$Type, predicate: $Predicate$Type<($UseOnContext$Type)>, action: $Consumer$Type<($UseOnContext$Type)>, arg3: $Function$Type<($UseOnContext$Type), ($BlockState$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HoeItemHooksImpl$Type = ($HoeItemHooksImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HoeItemHooksImpl_ = $HoeItemHooksImpl$Type;
}}
declare module "packages/dev/architectury/utils/value/$Value" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export interface $Value<T> extends $Supplier<(T)>, $Consumer<(T)> {

 "get"(): T
 "accept"(arg0: T): void
 "andThen"(arg0: $Consumer$Type<(any)>): $Consumer<(T)>
}

export namespace $Value {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Value$Type<T> = ($Value<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Value_<T> = $Value$Type<(T)>;
}}
declare module "packages/dev/architectury/registry/registries/$DeferredRegister" {
import {$RegistrySupplier, $RegistrySupplier$Type} from "packages/dev/architectury/registry/registries/$RegistrySupplier"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$RegistrarManager, $RegistrarManager$Type} from "packages/dev/architectury/registry/registries/$RegistrarManager"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$Registrar, $Registrar$Type} from "packages/dev/architectury/registry/registries/$Registrar"

export class $DeferredRegister<T> implements $Iterable<($RegistrySupplier<(T)>)> {


public "iterator"(): $Iterator<($RegistrySupplier<(T)>)>
public "register"<R extends T>(id: $ResourceLocation$Type, supplier: $Supplier$Type<(any)>): $RegistrySupplier<(R)>
public "register"(): void
public "register"<R extends T>(id: string, supplier: $Supplier$Type<(any)>): $RegistrySupplier<(R)>
public static "create"<T>(modId: string, key: $ResourceKey$Type<($Registry$Type<(T)>)>): $DeferredRegister<(T)>
public "getRegistrar"(): $Registrar<(T)>
public "getRegistrarManager"(): $RegistrarManager
public "spliterator"(): $Spliterator<($RegistrySupplier<(T)>)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
[Symbol.iterator](): IterableIterator<$RegistrySupplier<(T)>>;
get "registrar"(): $Registrar<(T)>
get "registrarManager"(): $RegistrarManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeferredRegister$Type<T> = ($DeferredRegister<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeferredRegister_<T> = $DeferredRegister$Type<(T)>;
}}
declare module "packages/dev/architectury/extensions/injected/$InjectedItemExtension" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$InjectedRegistryEntryExtension, $InjectedRegistryEntryExtension$Type} from "packages/dev/architectury/extensions/injected/$InjectedRegistryEntryExtension"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $InjectedItemExtension extends $InjectedRegistryEntryExtension<($Item)> {

 "arch$holder"(): $Holder<($Item)>
 "arch$registryName"(): $ResourceLocation
}

export namespace $InjectedItemExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectedItemExtension$Type = ($InjectedItemExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectedItemExtension_ = $InjectedItemExtension$Type;
}}
declare module "packages/dev/architectury/hooks/forge/$PackRepositoryHooksImpl" {
import {$PackRepository, $PackRepository$Type} from "packages/net/minecraft/server/packs/repository/$PackRepository"
import {$RepositorySource, $RepositorySource$Type} from "packages/net/minecraft/server/packs/repository/$RepositorySource"

export class $PackRepositoryHooksImpl {

constructor()

public static "addSource"(repository: $PackRepository$Type, source: $RepositorySource$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PackRepositoryHooksImpl$Type = ($PackRepositoryHooksImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PackRepositoryHooksImpl_ = $PackRepositoryHooksImpl$Type;
}}
declare module "packages/dev/architectury/registry/client/particle/forge/$ParticleProviderRegistryImpl" {
import {$ParticleProviderRegistry$DeferredParticleProvider, $ParticleProviderRegistry$DeferredParticleProvider$Type} from "packages/dev/architectury/registry/client/particle/$ParticleProviderRegistry$DeferredParticleProvider"
import {$ParticleProvider, $ParticleProvider$Type} from "packages/net/minecraft/client/particle/$ParticleProvider"
import {$ParticleType, $ParticleType$Type} from "packages/net/minecraft/core/particles/$ParticleType"
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"
import {$RegisterParticleProvidersEvent, $RegisterParticleProvidersEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterParticleProvidersEvent"
import {$ParticleOptions, $ParticleOptions$Type} from "packages/net/minecraft/core/particles/$ParticleOptions"

export class $ParticleProviderRegistryImpl {
static readonly "LOGGER": $Logger

constructor()

public static "register"<T extends $ParticleOptions>(type: $ParticleType$Type<(T)>, provider: $ParticleProviderRegistry$DeferredParticleProvider$Type<(T)>): void
public static "register"<T extends $ParticleOptions>(type: $ParticleType$Type<(T)>, provider: $ParticleProvider$Type<(T)>): void
public static "onParticleFactoryRegister"(event: $RegisterParticleProvidersEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParticleProviderRegistryImpl$Type = ($ParticleProviderRegistryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParticleProviderRegistryImpl_ = $ParticleProviderRegistryImpl$Type;
}}
declare module "packages/dev/architectury/event/events/common/$PlayerEvent$SmeltItem" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $PlayerEvent$SmeltItem {

 "smelt"(arg0: $Player$Type, arg1: $ItemStack$Type): void

(arg0: $Player$Type, arg1: $ItemStack$Type): void
}

export namespace $PlayerEvent$SmeltItem {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerEvent$SmeltItem$Type = ($PlayerEvent$SmeltItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerEvent$SmeltItem_ = $PlayerEvent$SmeltItem$Type;
}}
declare module "packages/dev/architectury/hooks/fluid/$LiquidBlockHooks" {
import {$FlowingFluid, $FlowingFluid$Type} from "packages/net/minecraft/world/level/material/$FlowingFluid"
import {$LiquidBlock, $LiquidBlock$Type} from "packages/net/minecraft/world/level/block/$LiquidBlock"

export class $LiquidBlockHooks {

constructor()

public static "getFluid"(block: $LiquidBlock$Type): $FlowingFluid
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LiquidBlockHooks$Type = ($LiquidBlockHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LiquidBlockHooks_ = $LiquidBlockHooks$Type;
}}
declare module "packages/dev/architectury/networking/simple/$MessageType" {
import {$NetworkManager$Side, $NetworkManager$Side$Type} from "packages/dev/architectury/networking/$NetworkManager$Side"
import {$SimpleNetworkManager, $SimpleNetworkManager$Type} from "packages/dev/architectury/networking/simple/$SimpleNetworkManager"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $MessageType {


public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getId"(): $ResourceLocation
public "getSide"(): $NetworkManager$Side
public "getManager"(): $SimpleNetworkManager
get "id"(): $ResourceLocation
get "side"(): $NetworkManager$Side
get "manager"(): $SimpleNetworkManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageType$Type = ($MessageType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageType_ = $MessageType$Type;
}}
declare module "packages/dev/architectury/extensions/injected/$InjectedBucketItemExtension" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"

export interface $InjectedBucketItemExtension {

 "arch$getFluid"(): $Fluid
}

export namespace $InjectedBucketItemExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectedBucketItemExtension$Type = ($InjectedBucketItemExtension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectedBucketItemExtension_ = $InjectedBucketItemExtension$Type;
}}
declare module "packages/dev/architectury/registry/registries/options/$DefaultIdRegistrarOption" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$RegistrarOption, $RegistrarOption$Type} from "packages/dev/architectury/registry/registries/options/$RegistrarOption"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $DefaultIdRegistrarOption extends $Record implements $RegistrarOption {

constructor(defaultId: $ResourceLocation$Type)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "defaultId"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultIdRegistrarOption$Type = ($DefaultIdRegistrarOption);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultIdRegistrarOption_ = $DefaultIdRegistrarOption$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientSystemMessageEvent$Received" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$CompoundEventResult, $CompoundEventResult$Type} from "packages/dev/architectury/event/$CompoundEventResult"

export interface $ClientSystemMessageEvent$Received {

 "process"(arg0: $Component$Type): $CompoundEventResult<($Component)>

(arg0: $Component$Type): $CompoundEventResult<($Component)>
}

export namespace $ClientSystemMessageEvent$Received {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientSystemMessageEvent$Received$Type = ($ClientSystemMessageEvent$Received);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientSystemMessageEvent$Received_ = $ClientSystemMessageEvent$Received$Type;
}}
declare module "packages/dev/architectury/registry/item/$ItemPropertiesRegistry" {
import {$ClampedItemPropertyFunction, $ClampedItemPropertyFunction$Type} from "packages/net/minecraft/client/renderer/item/$ClampedItemPropertyFunction"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ItemPropertiesRegistry {


public static "register"(item: $ItemLike$Type, propertyId: $ResourceLocation$Type, arg2: $ClampedItemPropertyFunction$Type): $ClampedItemPropertyFunction
public static "registerGeneric"(propertyId: $ResourceLocation$Type, arg1: $ClampedItemPropertyFunction$Type): $ClampedItemPropertyFunction
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemPropertiesRegistry$Type = ($ItemPropertiesRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemPropertiesRegistry_ = $ItemPropertiesRegistry$Type;
}}
declare module "packages/dev/architectury/registry/$CreativeTabOutput" {
import {$CreativeModeTab$TabVisibility, $CreativeModeTab$TabVisibility$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$TabVisibility"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$CreativeModeTab$Output, $CreativeModeTab$Output$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$Output"

export interface $CreativeTabOutput extends $CreativeModeTab$Output {

 "accept"(stack: $ItemStack$Type, visibility: $CreativeModeTab$TabVisibility$Type): void
 "acceptBefore"(before: $ItemStack$Type, item: $ItemLike$Type): void
 "acceptBefore"(before: $ItemLike$Type, stack: $ItemStack$Type): void
 "acceptBefore"(before: $ItemLike$Type, item: $ItemLike$Type, visibility: $CreativeModeTab$TabVisibility$Type): void
 "acceptBefore"(before: $ItemStack$Type, item: $ItemLike$Type, visibility: $CreativeModeTab$TabVisibility$Type): void
 "acceptBefore"(before: $ItemStack$Type, stack: $ItemStack$Type): void
 "acceptBefore"(arg0: $ItemStack$Type, arg1: $ItemStack$Type, arg2: $CreativeModeTab$TabVisibility$Type): void
 "acceptBefore"(before: $ItemLike$Type, item: $ItemLike$Type): void
 "acceptAllBefore"(before: $ItemLike$Type, stacks: $Collection$Type<($ItemStack$Type)>, visibility: $CreativeModeTab$TabVisibility$Type): void
 "acceptAllBefore"(before: $ItemLike$Type, stacks: $Collection$Type<($ItemStack$Type)>): void
 "acceptAllBefore"(before: $ItemStack$Type, stacks: $Collection$Type<($ItemStack$Type)>): void
 "acceptAllBefore"(before: $ItemStack$Type, stacks: $Collection$Type<($ItemStack$Type)>, visibility: $CreativeModeTab$TabVisibility$Type): void
 "acceptAfter"(after: $ItemLike$Type, stack: $ItemStack$Type): void
 "acceptAfter"(after: $ItemStack$Type, stack: $ItemStack$Type): void
 "acceptAfter"(after: $ItemStack$Type, item: $ItemLike$Type, visibility: $CreativeModeTab$TabVisibility$Type): void
 "acceptAfter"(after: $ItemStack$Type, item: $ItemLike$Type): void
 "acceptAfter"(arg0: $ItemStack$Type, arg1: $ItemStack$Type, arg2: $CreativeModeTab$TabVisibility$Type): void
 "acceptAfter"(after: $ItemLike$Type, item: $ItemLike$Type): void
 "acceptAfter"(after: $ItemLike$Type, item: $ItemLike$Type, visibility: $CreativeModeTab$TabVisibility$Type): void
 "acceptAllAfter"(after: $ItemLike$Type, stacks: $Collection$Type<($ItemStack$Type)>): void
 "acceptAllAfter"(after: $ItemStack$Type, stacks: $Collection$Type<($ItemStack$Type)>, visibility: $CreativeModeTab$TabVisibility$Type): void
 "acceptAllAfter"(after: $ItemStack$Type, stacks: $Collection$Type<($ItemStack$Type)>): void
 "acceptAllAfter"(after: $ItemLike$Type, stacks: $Collection$Type<($ItemStack$Type)>, visibility: $CreativeModeTab$TabVisibility$Type): void
 "accept"(arg0: $ItemLike$Type, arg1: $CreativeModeTab$TabVisibility$Type): void
 "acceptAll"(arg0: $Collection$Type<($ItemStack$Type)>, arg1: $CreativeModeTab$TabVisibility$Type): void
 "accept"(arg0: $ItemLike$Type): void
 "accept"(arg0: $ItemStack$Type): void
 "acceptAll"(arg0: $Collection$Type<($ItemStack$Type)>): void
}

export namespace $CreativeTabOutput {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CreativeTabOutput$Type = ($CreativeTabOutput);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CreativeTabOutput_ = $CreativeTabOutput$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientRawInputEvent$KeyPressed" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"

export interface $ClientRawInputEvent$KeyPressed {

 "keyPressed"(arg0: $Minecraft$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer): $EventResult

(arg0: $Minecraft$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer): $EventResult
}

export namespace $ClientRawInputEvent$KeyPressed {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientRawInputEvent$KeyPressed$Type = ($ClientRawInputEvent$KeyPressed);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientRawInputEvent$KeyPressed_ = $ClientRawInputEvent$KeyPressed$Type;
}}
declare module "packages/dev/architectury/extensions/injected/$InjectedRegistryEntryExtension" {
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $InjectedRegistryEntryExtension<T> {

 "arch$holder"(): $Holder<(T)>
 "arch$registryName"(): $ResourceLocation

(): $Holder<(T)>
}

export namespace $InjectedRegistryEntryExtension {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InjectedRegistryEntryExtension$Type<T> = ($InjectedRegistryEntryExtension<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InjectedRegistryEntryExtension_<T> = $InjectedRegistryEntryExtension$Type<(T)>;
}}
declare module "packages/dev/architectury/hooks/$PackRepositoryHooks" {
import {$PackRepository, $PackRepository$Type} from "packages/net/minecraft/server/packs/repository/$PackRepository"
import {$RepositorySource, $RepositorySource$Type} from "packages/net/minecraft/server/packs/repository/$RepositorySource"

export class $PackRepositoryHooks {


public static "addSource"(repository: $PackRepository$Type, source: $RepositorySource$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PackRepositoryHooks$Type = ($PackRepositoryHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PackRepositoryHooks_ = $PackRepositoryHooks$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientScreenInputEvent$KeyPressed" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$EventResult, $EventResult$Type} from "packages/dev/architectury/event/$EventResult"

export interface $ClientScreenInputEvent$KeyPressed {

 "keyPressed"(arg0: $Minecraft$Type, arg1: $Screen$Type, arg2: integer, arg3: integer, arg4: integer): $EventResult

(arg0: $Minecraft$Type, arg1: $Screen$Type, arg2: integer, arg3: integer, arg4: integer): $EventResult
}

export namespace $ClientScreenInputEvent$KeyPressed {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientScreenInputEvent$KeyPressed$Type = ($ClientScreenInputEvent$KeyPressed);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientScreenInputEvent$KeyPressed_ = $ClientScreenInputEvent$KeyPressed$Type;
}}
declare module "packages/dev/architectury/core/item/$ArchitecturyBucketItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$ICapabilityProvider, $ICapabilityProvider$Type} from "packages/net/minecraftforge/common/capabilities/$ICapabilityProvider"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BucketItem, $BucketItem$Type} from "packages/net/minecraft/world/item/$BucketItem"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ArchitecturyBucketItem extends $BucketItem {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(fluid: $Supplier$Type<(any)>, properties: $Item$Properties$Type)

public "getContainedFluid"(): $Fluid
public "initCapabilities"(stack: $ItemStack$Type, nbt: $CompoundTag$Type): $ICapabilityProvider
get "containedFluid"(): $Fluid
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArchitecturyBucketItem$Type = ($ArchitecturyBucketItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArchitecturyBucketItem_ = $ArchitecturyBucketItem$Type;
}}
declare module "packages/dev/architectury/hooks/level/biome/$EffectsProperties" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Music, $Music$Type} from "packages/net/minecraft/sounds/$Music"
import {$AmbientMoodSettings, $AmbientMoodSettings$Type} from "packages/net/minecraft/world/level/biome/$AmbientMoodSettings"
import {$AmbientParticleSettings, $AmbientParticleSettings$Type} from "packages/net/minecraft/world/level/biome/$AmbientParticleSettings"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AmbientAdditionsSettings, $AmbientAdditionsSettings$Type} from "packages/net/minecraft/world/level/biome/$AmbientAdditionsSettings"
import {$OptionalInt, $OptionalInt$Type} from "packages/java/util/$OptionalInt"
import {$BiomeSpecialEffects$GrassColorModifier, $BiomeSpecialEffects$GrassColorModifier$Type} from "packages/net/minecraft/world/level/biome/$BiomeSpecialEffects$GrassColorModifier"

export interface $EffectsProperties {

 "getGrassColorOverride"(): $OptionalInt
 "getAmbientMoodSound"(): $Optional<($AmbientMoodSettings)>
 "getFoliageColorOverride"(): $OptionalInt
 "getAmbientAdditionsSound"(): $Optional<($AmbientAdditionsSettings)>
 "getAmbientLoopSound"(): $Optional<($Holder<($SoundEvent)>)>
 "getGrassColorModifier"(): $BiomeSpecialEffects$GrassColorModifier
 "getSkyColor"(): integer
 "getAmbientParticle"(): $Optional<($AmbientParticleSettings)>
 "getWaterFogColor"(): integer
 "getBackgroundMusic"(): $Optional<($Music)>
 "getWaterColor"(): integer
 "getFogColor"(): integer
}

export namespace $EffectsProperties {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EffectsProperties$Type = ($EffectsProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EffectsProperties_ = $EffectsProperties$Type;
}}
declare module "packages/dev/architectury/networking/transformers/$PacketCollector" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PacketSink, $PacketSink$Type} from "packages/dev/architectury/networking/transformers/$PacketSink"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export class $PacketCollector implements $PacketSink {

constructor(consumer: $Consumer$Type<($Packet$Type<(any)>)>)

public "collect"(): $List<($Packet<(any)>)>
public "accept"(packet: $Packet$Type<(any)>): void
public static "client"(): $PacketSink
public static "ofPlayer"(player: $ServerPlayer$Type): $PacketSink
public static "ofPlayers"(players: $Iterable$Type<(any)>): $PacketSink
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketCollector$Type = ($PacketCollector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketCollector_ = $PacketCollector$Type;
}}
declare module "packages/dev/architectury/event/forge/$EventHandlerImplClient" {
import {$ScreenEvent$Init$Pre, $ScreenEvent$Init$Pre$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$Init$Pre"
import {$ScreenEvent$CharacterTyped$Pre, $ScreenEvent$CharacterTyped$Pre$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$CharacterTyped$Pre"
import {$ClientPlayerNetworkEvent$LoggingOut, $ClientPlayerNetworkEvent$LoggingOut$Type} from "packages/net/minecraftforge/client/event/$ClientPlayerNetworkEvent$LoggingOut"
import {$ScreenEvent$KeyReleased$Pre, $ScreenEvent$KeyReleased$Pre$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$KeyReleased$Pre"
import {$ScreenEvent$MouseButtonReleased$Pre, $ScreenEvent$MouseButtonReleased$Pre$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$MouseButtonReleased$Pre"
import {$InputEvent$MouseButton$Pre, $InputEvent$MouseButton$Pre$Type} from "packages/net/minecraftforge/client/event/$InputEvent$MouseButton$Pre"
import {$InputEvent$MouseButton$Post, $InputEvent$MouseButton$Post$Type} from "packages/net/minecraftforge/client/event/$InputEvent$MouseButton$Post"
import {$ScreenEvent$CharacterTyped$Post, $ScreenEvent$CharacterTyped$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$CharacterTyped$Post"
import {$ClientChatReceivedEvent, $ClientChatReceivedEvent$Type} from "packages/net/minecraftforge/client/event/$ClientChatReceivedEvent"
import {$ContainerScreenEvent$Render$Foreground, $ContainerScreenEvent$Render$Foreground$Type} from "packages/net/minecraftforge/client/event/$ContainerScreenEvent$Render$Foreground"
import {$PlayerInteractEvent$LeftClickEmpty, $PlayerInteractEvent$LeftClickEmpty$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$LeftClickEmpty"
import {$ContainerScreenEvent$Render$Background, $ContainerScreenEvent$Render$Background$Type} from "packages/net/minecraftforge/client/event/$ContainerScreenEvent$Render$Background"
import {$PlayerInteractEvent$RightClickEmpty, $PlayerInteractEvent$RightClickEmpty$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerInteractEvent$RightClickEmpty"
import {$ScreenEvent$MouseScrolled$Pre, $ScreenEvent$MouseScrolled$Pre$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$MouseScrolled$Pre"
import {$ScreenEvent$MouseScrolled$Post, $ScreenEvent$MouseScrolled$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$MouseScrolled$Post"
import {$RenderTooltipEvent$Color, $RenderTooltipEvent$Color$Type} from "packages/net/minecraftforge/client/event/$RenderTooltipEvent$Color"
import {$InputEvent$MouseScrollingEvent, $InputEvent$MouseScrollingEvent$Type} from "packages/net/minecraftforge/client/event/$InputEvent$MouseScrollingEvent"
import {$CustomizeGuiOverlayEvent$DebugText, $CustomizeGuiOverlayEvent$DebugText$Type} from "packages/net/minecraftforge/client/event/$CustomizeGuiOverlayEvent$DebugText"
import {$ScreenEvent$KeyReleased$Post, $ScreenEvent$KeyReleased$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$KeyReleased$Post"
import {$ScreenEvent$Opening, $ScreenEvent$Opening$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$Opening"
import {$RegisterClientCommandsEvent, $RegisterClientCommandsEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterClientCommandsEvent"
import {$ScreenEvent$MouseButtonPressed$Pre, $ScreenEvent$MouseButtonPressed$Pre$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$MouseButtonPressed$Pre"
import {$InputEvent$Key, $InputEvent$Key$Type} from "packages/net/minecraftforge/client/event/$InputEvent$Key"
import {$ClientPlayerNetworkEvent$Clone, $ClientPlayerNetworkEvent$Clone$Type} from "packages/net/minecraftforge/client/event/$ClientPlayerNetworkEvent$Clone"
import {$ScreenEvent$MouseButtonReleased$Post, $ScreenEvent$MouseButtonReleased$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$MouseButtonReleased$Post"
import {$ScreenEvent$KeyPressed$Post, $ScreenEvent$KeyPressed$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$KeyPressed$Post"
import {$LevelEvent$Load, $LevelEvent$Load$Type} from "packages/net/minecraftforge/event/level/$LevelEvent$Load"
import {$RenderTooltipEvent$Pre, $RenderTooltipEvent$Pre$Type} from "packages/net/minecraftforge/client/event/$RenderTooltipEvent$Pre"
import {$ScreenEvent$MouseButtonPressed$Post, $ScreenEvent$MouseButtonPressed$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$MouseButtonPressed$Post"
import {$ScreenEvent$MouseDragged$Post, $ScreenEvent$MouseDragged$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$MouseDragged$Post"
import {$ClientPlayerNetworkEvent$LoggingIn, $ClientPlayerNetworkEvent$LoggingIn$Type} from "packages/net/minecraftforge/client/event/$ClientPlayerNetworkEvent$LoggingIn"
import {$RenderGuiEvent$Post, $RenderGuiEvent$Post$Type} from "packages/net/minecraftforge/client/event/$RenderGuiEvent$Post"
import {$ItemTooltipEvent, $ItemTooltipEvent$Type} from "packages/net/minecraftforge/event/entity/player/$ItemTooltipEvent"
import {$ScreenEvent$Init$Post, $ScreenEvent$Init$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$Init$Post"
import {$ScreenEvent$Render$Post, $ScreenEvent$Render$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$Render$Post"
import {$ScreenEvent$KeyPressed$Pre, $ScreenEvent$KeyPressed$Pre$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$KeyPressed$Pre"
import {$ClientChatEvent, $ClientChatEvent$Type} from "packages/net/minecraftforge/client/event/$ClientChatEvent"
import {$ScreenEvent$MouseDragged$Pre, $ScreenEvent$MouseDragged$Pre$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$MouseDragged$Pre"
import {$RecipesUpdatedEvent, $RecipesUpdatedEvent$Type} from "packages/net/minecraftforge/client/event/$RecipesUpdatedEvent"
import {$ScreenEvent$Render$Pre, $ScreenEvent$Render$Pre$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$Render$Pre"
import {$TickEvent$ClientTickEvent, $TickEvent$ClientTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ClientTickEvent"

export class $EventHandlerImplClient {

constructor()

public static "event"(event: $ItemTooltipEvent$Type): void
public static "event"(event: $ClientChatEvent$Type): void
public static "event"(event: $ClientChatReceivedEvent$Type): void
public static "event"(event: $ScreenEvent$Opening$Type): void
public static "event"(event: $RecipesUpdatedEvent$Type): void
public static "event"(event: $RegisterClientCommandsEvent$Type): void
public static "event"(event: $ClientPlayerNetworkEvent$LoggingOut$Type): void
public static "event"(event: $ClientPlayerNetworkEvent$Clone$Type): void
public static "event"(event: $ClientPlayerNetworkEvent$LoggingIn$Type): void
public static "event"(event: $TickEvent$ClientTickEvent$Type): void
public static "eventWorldEvent"(event: $LevelEvent$Load$Type): void
public static "eventInputEvent"(event: $InputEvent$MouseScrollingEvent$Type): void
public static "eventInputEvent"(event: $InputEvent$MouseButton$Pre$Type): void
public static "eventInputEvent"(event: $InputEvent$Key$Type): void
public static "eventInputEvent"(event: $InputEvent$MouseButton$Post$Type): void
public static "eventRenderGameOverlayEvent"(event: $CustomizeGuiOverlayEvent$DebugText$Type): void
public static "eventRenderGameOverlayEvent"(event: $RenderGuiEvent$Post$Type): void
public static "eventInitScreenEvent"(event: $ScreenEvent$Init$Post$Type): void
public static "eventInitScreenEvent"(event: $ScreenEvent$Init$Pre$Type): void
public static "eventDrawScreenEvent"(event: $ScreenEvent$Render$Pre$Type): void
public static "eventDrawScreenEvent"(event: $ScreenEvent$Render$Post$Type): void
public static "eventContainerScreenEvent"(event: $ContainerScreenEvent$Render$Background$Type): void
public static "eventContainerScreenEvent"(event: $ContainerScreenEvent$Render$Foreground$Type): void
public static "eventKeyboardKeyReleasedEvent"(event: $ScreenEvent$KeyReleased$Post$Type): void
public static "eventKeyboardKeyReleasedEvent"(event: $ScreenEvent$KeyReleased$Pre$Type): void
public static "eventMouseScrollEvent"(event: $ScreenEvent$MouseScrolled$Pre$Type): void
public static "eventMouseScrollEvent"(event: $ScreenEvent$MouseScrolled$Post$Type): void
public static "eventMouseClickedEvent"(event: $ScreenEvent$MouseButtonPressed$Pre$Type): void
public static "eventMouseClickedEvent"(event: $ScreenEvent$MouseButtonPressed$Post$Type): void
public static "eventMouseReleasedEvent"(event: $ScreenEvent$MouseButtonReleased$Post$Type): void
public static "eventMouseReleasedEvent"(event: $ScreenEvent$MouseButtonReleased$Pre$Type): void
public static "eventRenderTooltipEvent"(event: $RenderTooltipEvent$Pre$Type): void
public static "eventRenderTooltipEvent"(event: $RenderTooltipEvent$Color$Type): void
public static "eventKeyboardCharTypedEvent"(event: $ScreenEvent$CharacterTyped$Pre$Type): void
public static "eventKeyboardCharTypedEvent"(event: $ScreenEvent$CharacterTyped$Post$Type): void
public static "eventMouseDragEvent"(event: $ScreenEvent$MouseDragged$Pre$Type): void
public static "eventMouseDragEvent"(event: $ScreenEvent$MouseDragged$Post$Type): void
public static "eventPlayerInteractEvent"(event: $PlayerInteractEvent$LeftClickEmpty$Type): void
public static "eventPlayerInteractEvent"(event: $PlayerInteractEvent$RightClickEmpty$Type): void
public static "eventKeyboardKeyPressedEvent"(event: $ScreenEvent$KeyPressed$Pre$Type): void
public static "eventKeyboardKeyPressedEvent"(event: $ScreenEvent$KeyPressed$Post$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventHandlerImplClient$Type = ($EventHandlerImplClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventHandlerImplClient_ = $EventHandlerImplClient$Type;
}}
declare module "packages/dev/architectury/event/events/client/$ClientGuiEvent$SetScreen" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$CompoundEventResult, $CompoundEventResult$Type} from "packages/dev/architectury/event/$CompoundEventResult"

export interface $ClientGuiEvent$SetScreen {

 "modifyScreen"(arg0: $Screen$Type): $CompoundEventResult<($Screen)>

(arg0: $Screen$Type): $CompoundEventResult<($Screen)>
}

export namespace $ClientGuiEvent$SetScreen {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientGuiEvent$SetScreen$Type = ($ClientGuiEvent$SetScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientGuiEvent$SetScreen_ = $ClientGuiEvent$SetScreen$Type;
}}
