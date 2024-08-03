declare module "packages/com/tterrag/registrate/builders/$BlockEntityBuilder" {
import {$AbstractBuilder, $AbstractBuilder$Type} from "packages/com/tterrag/registrate/builders/$AbstractBuilder"
import {$BuilderCallback, $BuilderCallback$Type} from "packages/com/tterrag/registrate/builders/$BuilderCallback"
import {$BlockEntityBuilder$BlockEntityFactory, $BlockEntityBuilder$BlockEntityFactory$Type} from "packages/com/tterrag/registrate/builders/$BlockEntityBuilder$BlockEntityFactory"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$BlockEntityEntry, $BlockEntityEntry$Type} from "packages/com/tterrag/registrate/util/entry/$BlockEntityEntry"
import {$BlockEntityRendererProvider$Context, $BlockEntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRendererProvider$Context"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$NonNullFunction, $NonNullFunction$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullFunction"
import {$BlockEntityRenderer, $BlockEntityRenderer$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRenderer"

export class $BlockEntityBuilder<T extends $BlockEntity, P> extends $AbstractBuilder<($BlockEntityType<(any)>), ($BlockEntityType<(T)>), (P), ($BlockEntityBuilder<(T), (P)>)> {


public "register"(): $BlockEntityEntry<(T)>
public static "create"<T extends $BlockEntity, P>(arg0: $AbstractRegistrate$Type<(any)>, arg1: P, arg2: string, arg3: $BuilderCallback$Type, arg4: $BlockEntityBuilder$BlockEntityFactory$Type<(T)>): $BlockEntityBuilder<(T), (P)>
public "validBlocks"(...arg0: ($NonNullSupplier$Type<(any)>)[]): $BlockEntityBuilder<(T), (P)>
public "renderer"(arg0: $NonNullSupplier$Type<($NonNullFunction$Type<($BlockEntityRendererProvider$Context$Type), ($BlockEntityRenderer$Type<(any)>)>)>): $BlockEntityBuilder<(T), (P)>
public "validBlock"(arg0: $NonNullSupplier$Type<(any)>): $BlockEntityBuilder<(T), (P)>
public static "of"<T>(arg0: $Supplier$Type<(T)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<(T)>
public static "of"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
public static "lazy"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityBuilder$Type<T, P> = ($BlockEntityBuilder<(T), (P)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityBuilder_<T, P> = $BlockEntityBuilder$Type<(T), (P)>;
}}
declare module "packages/com/tterrag/registrate/providers/loot/$RegistrateLootTables" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$LootTable$Builder, $LootTable$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/$LootTable$Builder"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LootTable, $LootTable$Type} from "packages/net/minecraft/world/level/storage/loot/$LootTable"
import {$LootTableSubProvider, $LootTableSubProvider$Type} from "packages/net/minecraft/data/loot/$LootTableSubProvider"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $RegistrateLootTables extends $LootTableSubProvider {

 "validate"(arg0: $Map$Type<($ResourceLocation$Type), ($LootTable$Type)>, arg1: $ValidationContext$Type): void
 "generate"(arg0: $BiConsumer$Type<($ResourceLocation$Type), ($LootTable$Builder$Type)>): void

(arg0: $Map$Type<($ResourceLocation$Type), ($LootTable$Type)>, arg1: $ValidationContext$Type): void
}

export namespace $RegistrateLootTables {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrateLootTables$Type = ($RegistrateLootTables);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrateLootTables_ = $RegistrateLootTables$Type;
}}
declare module "packages/com/tterrag/registrate/builders/$FluidBuilder" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$NonNullConsumer, $NonNullConsumer$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullConsumer"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$LiquidBlock, $LiquidBlock$Type} from "packages/net/minecraft/world/level/block/$LiquidBlock"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$FluidType, $FluidType$Type} from "packages/net/minecraftforge/fluids/$FluidType"
import {$BlockBuilder, $BlockBuilder$Type} from "packages/com/tterrag/registrate/builders/$BlockBuilder"
import {$AbstractBuilder, $AbstractBuilder$Type} from "packages/com/tterrag/registrate/builders/$AbstractBuilder"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$FluidType$Properties, $FluidType$Properties$Type} from "packages/net/minecraftforge/fluids/$FluidType$Properties"
import {$BuilderCallback, $BuilderCallback$Type} from "packages/com/tterrag/registrate/builders/$BuilderCallback"
import {$FluidBuilder$FluidTypeFactory, $FluidBuilder$FluidTypeFactory$Type} from "packages/com/tterrag/registrate/builders/$FluidBuilder$FluidTypeFactory"
import {$ForgeFlowingFluid$Properties, $ForgeFlowingFluid$Properties$Type} from "packages/net/minecraftforge/fluids/$ForgeFlowingFluid$Properties"
import {$ItemBuilder, $ItemBuilder$Type} from "packages/com/tterrag/registrate/builders/$ItemBuilder"
import {$BucketItem, $BucketItem$Type} from "packages/net/minecraft/world/item/$BucketItem"
import {$FluidEntry, $FluidEntry$Type} from "packages/com/tterrag/registrate/util/entry/$FluidEntry"
import {$ForgeFlowingFluid$Flowing, $ForgeFlowingFluid$Flowing$Type} from "packages/net/minecraftforge/fluids/$ForgeFlowingFluid$Flowing"
import {$NonNullBiFunction, $NonNullBiFunction$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullBiFunction"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$ForgeFlowingFluid, $ForgeFlowingFluid$Type} from "packages/net/minecraftforge/fluids/$ForgeFlowingFluid"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$NonNullFunction, $NonNullFunction$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullFunction"

export class $FluidBuilder<T extends $ForgeFlowingFluid, P> extends $AbstractBuilder<($Fluid), (T), (P), ($FluidBuilder<(T), (P)>)> {

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: P, arg2: string, arg3: $BuilderCallback$Type, arg4: $ResourceLocation$Type, arg5: $ResourceLocation$Type, arg6: $NonNullSupplier$Type<($FluidType$Type)>, arg7: $NonNullFunction$Type<($ForgeFlowingFluid$Properties$Type), (T)>)
constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: P, arg2: string, arg3: $BuilderCallback$Type, arg4: $ResourceLocation$Type, arg5: $ResourceLocation$Type, arg6: $FluidBuilder$FluidTypeFactory$Type, arg7: $NonNullFunction$Type<($ForgeFlowingFluid$Properties$Type), (T)>)

public "register"(): $FluidEntry<(T)>
public "source"(arg0: $NonNullFunction$Type<($ForgeFlowingFluid$Properties$Type), (any)>): $FluidBuilder<(T), (P)>
public "properties"(arg0: $NonNullConsumer$Type<($FluidType$Properties$Type)>): $FluidBuilder<(T), (P)>
public static "create"<P>(arg0: $AbstractRegistrate$Type<(any)>, arg1: P, arg2: string, arg3: $BuilderCallback$Type, arg4: $ResourceLocation$Type, arg5: $ResourceLocation$Type, arg6: $FluidBuilder$FluidTypeFactory$Type): $FluidBuilder<($ForgeFlowingFluid$Flowing), (P)>
public static "create"<P>(arg0: $AbstractRegistrate$Type<(any)>, arg1: P, arg2: string, arg3: $BuilderCallback$Type, arg4: $ResourceLocation$Type, arg5: $ResourceLocation$Type): $FluidBuilder<($ForgeFlowingFluid$Flowing), (P)>
public static "create"<P>(arg0: $AbstractRegistrate$Type<(any)>, arg1: P, arg2: string, arg3: $BuilderCallback$Type, arg4: $ResourceLocation$Type, arg5: $ResourceLocation$Type, arg6: $NonNullSupplier$Type<($FluidType$Type)>): $FluidBuilder<($ForgeFlowingFluid$Flowing), (P)>
public static "create"<T extends $ForgeFlowingFluid, P>(arg0: $AbstractRegistrate$Type<(any)>, arg1: P, arg2: string, arg3: $BuilderCallback$Type, arg4: $ResourceLocation$Type, arg5: $ResourceLocation$Type, arg6: $NonNullSupplier$Type<($FluidType$Type)>, arg7: $NonNullFunction$Type<($ForgeFlowingFluid$Properties$Type), (T)>): $FluidBuilder<(T), (P)>
public static "create"<T extends $ForgeFlowingFluid, P>(arg0: $AbstractRegistrate$Type<(any)>, arg1: P, arg2: string, arg3: $BuilderCallback$Type, arg4: $ResourceLocation$Type, arg5: $ResourceLocation$Type, arg6: $FluidBuilder$FluidTypeFactory$Type, arg7: $NonNullFunction$Type<($ForgeFlowingFluid$Properties$Type), (T)>): $FluidBuilder<(T), (P)>
public static "create"<T extends $ForgeFlowingFluid, P>(arg0: $AbstractRegistrate$Type<(any)>, arg1: P, arg2: string, arg3: $BuilderCallback$Type, arg4: $ResourceLocation$Type, arg5: $ResourceLocation$Type, arg6: $NonNullFunction$Type<($ForgeFlowingFluid$Properties$Type), (T)>): $FluidBuilder<(T), (P)>
public "block"<B extends $LiquidBlock>(arg0: $NonNullBiFunction$Type<($NonNullSupplier$Type<(any)>), ($BlockBehaviour$Properties$Type), (any)>): $BlockBuilder<(B), ($FluidBuilder<(T), (P)>)>
public "block"(): $BlockBuilder<($LiquidBlock), ($FluidBuilder<(T), (P)>)>
public "lang"(arg0: string): $FluidBuilder<(T), (P)>
public "tag"(...arg0: ($TagKey$Type<($Fluid$Type)>)[]): $FluidBuilder<(T), (P)>
public "renderType"(arg0: $Supplier$Type<($RenderType$Type)>): $FluidBuilder<(T), (P)>
public "defaultLang"(): $FluidBuilder<(T), (P)>
public "defaultSource"(): $FluidBuilder<(T), (P)>
public "defaultBucket"(): $FluidBuilder<(T), (P)>
public "fluidProperties"(arg0: $NonNullConsumer$Type<($ForgeFlowingFluid$Properties$Type)>): $FluidBuilder<(T), (P)>
public "noBlock"(): $FluidBuilder<(T), (P)>
public "noBucket"(): $FluidBuilder<(T), (P)>
public "bucket"(): $ItemBuilder<($BucketItem), ($FluidBuilder<(T), (P)>)>
public "bucket"<I extends $BucketItem>(arg0: $NonNullBiFunction$Type<($Supplier$Type<(any)>), ($Item$Properties$Type), (any)>): $ItemBuilder<(I), ($FluidBuilder<(T), (P)>)>
public "defaultBlock"(): $FluidBuilder<(T), (P)>
public "removeTag"(...arg0: ($TagKey$Type<($Fluid$Type)>)[]): $FluidBuilder<(T), (P)>
public static "of"<T>(arg0: $Supplier$Type<(T)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<(T)>
public static "of"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
public static "lazy"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidBuilder$Type<T, P> = ($FluidBuilder<(T), (P)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidBuilder_<T, P> = $FluidBuilder$Type<(T), (P)>;
}}
declare module "packages/com/tterrag/registrate/builders/$MenuBuilder$MenuFactory" {
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export interface $MenuBuilder$MenuFactory<T extends $AbstractContainerMenu> {

 "create"(arg0: $MenuType$Type<(T)>, arg1: integer, arg2: $Inventory$Type): T

(arg0: $MenuType$Type<(T)>, arg1: integer, arg2: $Inventory$Type): T
}

export namespace $MenuBuilder$MenuFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuBuilder$MenuFactory$Type<T> = ($MenuBuilder$MenuFactory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuBuilder$MenuFactory_<T> = $MenuBuilder$MenuFactory$Type<(T)>;
}}
declare module "packages/com/tterrag/registrate/util/nullness/$NonNullUnaryOperator" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$NonNullFunction, $NonNullFunction$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullFunction"

export interface $NonNullUnaryOperator<T> extends $NonNullFunction<(T), (T)> {

 "andThen"<V>(arg0: $NonNullUnaryOperator$Type<(T)>): $NonNullUnaryOperator<(T)>
 "apply"(arg0: T): T
 "andThen"<V>(arg0: $NonNullFunction$Type<(any), (any)>): $NonNullFunction<(T), (V)>
 "compose"<V>(arg0: $Function$Type<(any), (any)>): $Function<(V), (T)>
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<(T), (V)>

(): $NonNullUnaryOperator<(T)>
}

export namespace $NonNullUnaryOperator {
function identity<T>(): $NonNullUnaryOperator<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NonNullUnaryOperator$Type<T> = ($NonNullUnaryOperator<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NonNullUnaryOperator_<T> = $NonNullUnaryOperator$Type<(T)>;
}}
declare module "packages/com/tterrag/registrate/$AbstractRegistrate" {
import {$CreativeModeTabModifier, $CreativeModeTabModifier$Type} from "packages/com/tterrag/registrate/util/$CreativeModeTabModifier"
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$NonNullConsumer, $NonNullConsumer$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullConsumer"
import {$RegistryBuilder, $RegistryBuilder$Type} from "packages/net/minecraftforge/registries/$RegistryBuilder"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$CreativeModeTab$Builder, $CreativeModeTab$Builder$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$Builder"
import {$RegistrateProvider, $RegistrateProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateProvider"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$EnchantmentBuilder$EnchantmentFactory, $EnchantmentBuilder$EnchantmentFactory$Type} from "packages/com/tterrag/registrate/builders/$EnchantmentBuilder$EnchantmentFactory"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$FluidType, $FluidType$Type} from "packages/net/minecraftforge/fluids/$FluidType"
import {$MenuAccess, $MenuAccess$Type} from "packages/net/minecraft/client/gui/screens/inventory/$MenuAccess"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$BlockBuilder, $BlockBuilder$Type} from "packages/com/tterrag/registrate/builders/$BlockBuilder"
import {$ProviderType, $ProviderType$Type} from "packages/com/tterrag/registrate/providers/$ProviderType"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$BuilderCallback, $BuilderCallback$Type} from "packages/com/tterrag/registrate/builders/$BuilderCallback"
import {$FluidBuilder$FluidTypeFactory, $FluidBuilder$FluidTypeFactory$Type} from "packages/com/tterrag/registrate/builders/$FluidBuilder$FluidTypeFactory"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$ForgeFlowingFluid$Properties, $ForgeFlowingFluid$Properties$Type} from "packages/net/minecraftforge/fluids/$ForgeFlowingFluid$Properties"
import {$EnchantmentCategory, $EnchantmentCategory$Type} from "packages/net/minecraft/world/item/enchantment/$EnchantmentCategory"
import {$ForgeFlowingFluid$Flowing, $ForgeFlowingFluid$Flowing$Type} from "packages/net/minecraftforge/fluids/$ForgeFlowingFluid$Flowing"
import {$NonNullBiFunction, $NonNullBiFunction$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullBiFunction"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$FluidBuilder, $FluidBuilder$Type} from "packages/com/tterrag/registrate/builders/$FluidBuilder"
import {$BlockEntityBuilder, $BlockEntityBuilder$Type} from "packages/com/tterrag/registrate/builders/$BlockEntityBuilder"
import {$NonNullUnaryOperator, $NonNullUnaryOperator$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullUnaryOperator"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$MenuBuilder$ScreenFactory, $MenuBuilder$ScreenFactory$Type} from "packages/com/tterrag/registrate/builders/$MenuBuilder$ScreenFactory"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$MenuBuilder$ForgeMenuFactory, $MenuBuilder$ForgeMenuFactory$Type} from "packages/com/tterrag/registrate/builders/$MenuBuilder$ForgeMenuFactory"
import {$MobCategory, $MobCategory$Type} from "packages/net/minecraft/world/entity/$MobCategory"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$Builder, $Builder$Type} from "packages/com/tterrag/registrate/builders/$Builder"
import {$EntityType$EntityFactory, $EntityType$EntityFactory$Type} from "packages/net/minecraft/world/entity/$EntityType$EntityFactory"
import {$NoConfigBuilder, $NoConfigBuilder$Type} from "packages/com/tterrag/registrate/builders/$NoConfigBuilder"
import {$EnchantmentBuilder, $EnchantmentBuilder$Type} from "packages/com/tterrag/registrate/builders/$EnchantmentBuilder"
import {$BlockEntityBuilder$BlockEntityFactory, $BlockEntityBuilder$BlockEntityFactory$Type} from "packages/com/tterrag/registrate/builders/$BlockEntityBuilder$BlockEntityFactory"
import {$ItemBuilder, $ItemBuilder$Type} from "packages/com/tterrag/registrate/builders/$ItemBuilder"
import {$EntityBuilder, $EntityBuilder$Type} from "packages/com/tterrag/registrate/builders/$EntityBuilder"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$MenuBuilder, $MenuBuilder$Type} from "packages/com/tterrag/registrate/builders/$MenuBuilder"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$ForgeFlowingFluid, $ForgeFlowingFluid$Type} from "packages/net/minecraftforge/fluids/$ForgeFlowingFluid"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/tterrag/registrate/util/entry/$RegistryEntry"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$MenuBuilder$MenuFactory, $MenuBuilder$MenuFactory$Type} from "packages/com/tterrag/registrate/builders/$MenuBuilder$MenuFactory"
import {$NonNullFunction, $NonNullFunction$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullFunction"

export class $AbstractRegistrate<S extends $AbstractRegistrate<(S)>> {


public "get"<R, T extends R>(arg0: $ResourceKey$Type<(any)>): $RegistryEntry<(T)>
public "get"<R, T extends R>(arg0: string, arg1: $ResourceKey$Type<(any)>): $RegistryEntry<(T)>
public "transform"<R, T extends R, P, S2 extends $Builder<(R), (T), (P), (S2)>>(arg0: $NonNullFunction$Type<(S), (S2)>): S2
public "transform"(arg0: $NonNullUnaryOperator$Type<(S)>): S
public "isRegistered"<R>(arg0: $ResourceKey$Type<(any)>): boolean
public "entry"<R, T extends R, P, S2 extends $Builder<(R), (T), (P), (S2)>>(arg0: string, arg1: $NonNullFunction$Type<($BuilderCallback$Type), (S2)>): S2
public "entry"<R, T extends R, P, S2 extends $Builder<(R), (T), (P), (S2)>>(arg0: $NonNullBiFunction$Type<(string), ($BuilderCallback$Type), (S2)>): S2
public "generic"<R, T extends R, P>(arg0: P, arg1: $ResourceKey$Type<($Registry$Type<(R)>)>, arg2: $NonNullSupplier$Type<(T)>): $NoConfigBuilder<(R), (T), (P)>
public "generic"<R, T extends R>(arg0: $ResourceKey$Type<($Registry$Type<(R)>)>, arg1: $NonNullSupplier$Type<(T)>): $NoConfigBuilder<(R), (T), (S)>
public "generic"<R, T extends R, P>(arg0: P, arg1: string, arg2: $ResourceKey$Type<($Registry$Type<(R)>)>, arg3: $NonNullSupplier$Type<(T)>): $NoConfigBuilder<(R), (T), (P)>
public "generic"<R, T extends R>(arg0: string, arg1: $ResourceKey$Type<($Registry$Type<(R)>)>, arg2: $NonNullSupplier$Type<(T)>): $NoConfigBuilder<(R), (T), (S)>
public "block"<T extends $Block>(arg0: $NonNullFunction$Type<($BlockBehaviour$Properties$Type), (T)>): $BlockBuilder<(T), (S)>
public "block"<T extends $Block, P>(arg0: P, arg1: $NonNullFunction$Type<($BlockBehaviour$Properties$Type), (T)>): $BlockBuilder<(T), (P)>
public "block"<T extends $Block, P>(arg0: P, arg1: string, arg2: $NonNullFunction$Type<($BlockBehaviour$Properties$Type), (T)>): $BlockBuilder<(T), (P)>
public "block"<T extends $Block>(arg0: string, arg1: $NonNullFunction$Type<($BlockBehaviour$Properties$Type), (T)>): $BlockBuilder<(T), (S)>
public "object"(arg0: string): S
public "getAll"<R>(arg0: $ResourceKey$Type<(any)>): $Collection<($RegistryEntry<(R)>)>
public "item"<T extends $Item>(arg0: $NonNullFunction$Type<($Item$Properties$Type), (T)>): $ItemBuilder<(T), (S)>
public "item"<T extends $Item, P>(arg0: P, arg1: string, arg2: $NonNullFunction$Type<($Item$Properties$Type), (T)>): $ItemBuilder<(T), (P)>
public "item"<T extends $Item, P>(arg0: P, arg1: $NonNullFunction$Type<($Item$Properties$Type), (T)>): $ItemBuilder<(T), (P)>
public "item"<T extends $Item>(arg0: string, arg1: $NonNullFunction$Type<($Item$Properties$Type), (T)>): $ItemBuilder<(T), (S)>
public "simple"<R, T extends R, P>(arg0: P, arg1: string, arg2: $ResourceKey$Type<($Registry$Type<(R)>)>, arg3: $NonNullSupplier$Type<(T)>): $RegistryEntry<(T)>
public "simple"<R, T extends R, P>(arg0: P, arg1: $ResourceKey$Type<($Registry$Type<(R)>)>, arg2: $NonNullSupplier$Type<(T)>): $RegistryEntry<(T)>
public "simple"<R, T extends R>(arg0: $ResourceKey$Type<($Registry$Type<(R)>)>, arg1: $NonNullSupplier$Type<(T)>): $RegistryEntry<(T)>
public "simple"<R, T extends R>(arg0: string, arg1: $ResourceKey$Type<($Registry$Type<(R)>)>, arg2: $NonNullSupplier$Type<(T)>): $RegistryEntry<(T)>
public "getOptional"<R, T extends R>(arg0: string, arg1: $ResourceKey$Type<(any)>): $RegistryEntry<(T)>
public "addDataGenerator"<T extends $RegistrateProvider>(arg0: $ProviderType$Type<(any)>, arg1: $NonNullConsumer$Type<(any)>): S
public "addRawLang"(arg0: string, arg1: string): $MutableComponent
public "getModid"(): string
public "enchantment"<T extends $Enchantment>(arg0: string, arg1: $EnchantmentCategory$Type, arg2: $EnchantmentBuilder$EnchantmentFactory$Type<(T)>): $EnchantmentBuilder<(T), (S)>
public "enchantment"<T extends $Enchantment, P>(arg0: P, arg1: string, arg2: $EnchantmentCategory$Type, arg3: $EnchantmentBuilder$EnchantmentFactory$Type<(T)>): $EnchantmentBuilder<(T), (P)>
public "enchantment"<T extends $Enchantment>(arg0: $EnchantmentCategory$Type, arg1: $EnchantmentBuilder$EnchantmentFactory$Type<(T)>): $EnchantmentBuilder<(T), (S)>
public "enchantment"<T extends $Enchantment, P>(arg0: P, arg1: $EnchantmentCategory$Type, arg2: $EnchantmentBuilder$EnchantmentFactory$Type<(T)>): $EnchantmentBuilder<(T), (P)>
public "menu"<T extends $AbstractContainerMenu, SC extends ($Screen) & ($MenuAccess<(T)>)>(arg0: $MenuBuilder$ForgeMenuFactory$Type<(T)>, arg1: $NonNullSupplier$Type<($MenuBuilder$ScreenFactory$Type<(T), (SC)>)>): $MenuBuilder<(T), (SC), (S)>
public "menu"<T extends $AbstractContainerMenu, SC extends ($Screen) & ($MenuAccess<(T)>)>(arg0: $MenuBuilder$MenuFactory$Type<(T)>, arg1: $NonNullSupplier$Type<($MenuBuilder$ScreenFactory$Type<(T), (SC)>)>): $MenuBuilder<(T), (SC), (S)>
public "menu"<T extends $AbstractContainerMenu, SC extends ($Screen) & ($MenuAccess<(T)>), P>(arg0: P, arg1: string, arg2: $MenuBuilder$ForgeMenuFactory$Type<(T)>, arg3: $NonNullSupplier$Type<($MenuBuilder$ScreenFactory$Type<(T), (SC)>)>): $MenuBuilder<(T), (SC), (P)>
public "menu"<T extends $AbstractContainerMenu, SC extends ($Screen) & ($MenuAccess<(T)>), P>(arg0: P, arg1: $MenuBuilder$ForgeMenuFactory$Type<(T)>, arg2: $NonNullSupplier$Type<($MenuBuilder$ScreenFactory$Type<(T), (SC)>)>): $MenuBuilder<(T), (SC), (P)>
public "menu"<T extends $AbstractContainerMenu, SC extends ($Screen) & ($MenuAccess<(T)>)>(arg0: string, arg1: $MenuBuilder$ForgeMenuFactory$Type<(T)>, arg2: $NonNullSupplier$Type<($MenuBuilder$ScreenFactory$Type<(T), (SC)>)>): $MenuBuilder<(T), (SC), (S)>
public "menu"<T extends $AbstractContainerMenu, SC extends ($Screen) & ($MenuAccess<(T)>), P>(arg0: P, arg1: $MenuBuilder$MenuFactory$Type<(T)>, arg2: $NonNullSupplier$Type<($MenuBuilder$ScreenFactory$Type<(T), (SC)>)>): $MenuBuilder<(T), (SC), (P)>
public "menu"<T extends $AbstractContainerMenu, SC extends ($Screen) & ($MenuAccess<(T)>), P>(arg0: P, arg1: string, arg2: $MenuBuilder$MenuFactory$Type<(T)>, arg3: $NonNullSupplier$Type<($MenuBuilder$ScreenFactory$Type<(T), (SC)>)>): $MenuBuilder<(T), (SC), (P)>
public "menu"<T extends $AbstractContainerMenu, SC extends ($Screen) & ($MenuAccess<(T)>)>(arg0: string, arg1: $MenuBuilder$MenuFactory$Type<(T)>, arg2: $NonNullSupplier$Type<($MenuBuilder$ScreenFactory$Type<(T), (SC)>)>): $MenuBuilder<(T), (SC), (S)>
public "addRegisterCallback"<R, T extends R>(arg0: string, arg1: $ResourceKey$Type<(any)>, arg2: $NonNullConsumer$Type<(any)>): S
public "addRegisterCallback"<R>(arg0: $ResourceKey$Type<(any)>, arg1: $Runnable$Type): S
public "modifyCreativeModeTab"(arg0: $ResourceKey$Type<($CreativeModeTab$Type)>, arg1: $Consumer$Type<($CreativeModeTabModifier$Type)>): S
public "makeDatapackRegistry"<R>(arg0: string, arg1: $Codec$Type<(R)>, arg2: $Codec$Type<(R)>): $ResourceKey<($Registry<(R)>)>
public "makeDatapackRegistry"<R>(arg0: string, arg1: $Codec$Type<(R)>): $ResourceKey<($Registry<(R)>)>
public "entity"<T extends $Entity, P>(arg0: P, arg1: string, arg2: $EntityType$EntityFactory$Type<(T)>, arg3: $MobCategory$Type): $EntityBuilder<(T), (P)>
public "entity"<T extends $Entity, P>(arg0: P, arg1: $EntityType$EntityFactory$Type<(T)>, arg2: $MobCategory$Type): $EntityBuilder<(T), (P)>
public "entity"<T extends $Entity>(arg0: $EntityType$EntityFactory$Type<(T)>, arg1: $MobCategory$Type): $EntityBuilder<(T), (S)>
public "entity"<T extends $Entity>(arg0: string, arg1: $EntityType$EntityFactory$Type<(T)>, arg2: $MobCategory$Type): $EntityBuilder<(T), (S)>
public "getModEventBus"(): $IEventBus
public "skipErrors"(arg0: boolean): S
public "getDataProvider"<P extends $RegistrateProvider>(arg0: $ProviderType$Type<(P)>): $Optional<(P)>
public "addLang"(arg0: string, arg1: $ResourceLocation$Type, arg2: string): $MutableComponent
public "addLang"(arg0: string, arg1: $ResourceLocation$Type, arg2: string, arg3: string): $MutableComponent
public "setDataGenerator"<P extends $RegistrateProvider, R>(arg0: $Builder$Type<(R), (any), (any), (any)>, arg1: $ProviderType$Type<(any)>, arg2: $NonNullConsumer$Type<(any)>): S
public "setDataGenerator"<P extends $RegistrateProvider, R>(arg0: string, arg1: $ResourceKey$Type<(any)>, arg2: $ProviderType$Type<(any)>, arg3: $NonNullConsumer$Type<(any)>): S
public "defaultCreativeTab"(arg0: string, arg1: $Consumer$Type<($CreativeModeTab$Builder$Type)>): $NoConfigBuilder<($CreativeModeTab), ($CreativeModeTab), (S)>
public "defaultCreativeTab"(): $NoConfigBuilder<($CreativeModeTab), ($CreativeModeTab), (S)>
public "defaultCreativeTab"<P>(arg0: P, arg1: string, arg2: $Consumer$Type<($CreativeModeTab$Builder$Type)>): $NoConfigBuilder<($CreativeModeTab), ($CreativeModeTab), (P)>
public "defaultCreativeTab"(arg0: $ResourceKey$Type<($CreativeModeTab$Type)>): S
public "defaultCreativeTab"(arg0: string): $NoConfigBuilder<($CreativeModeTab), ($CreativeModeTab), (S)>
public "defaultCreativeTab"<P>(arg0: P): $NoConfigBuilder<($CreativeModeTab), ($CreativeModeTab), (P)>
public "defaultCreativeTab"<P>(arg0: P, arg1: string): $NoConfigBuilder<($CreativeModeTab), ($CreativeModeTab), (P)>
public "defaultCreativeTab"(arg0: $Consumer$Type<($CreativeModeTab$Builder$Type)>): $NoConfigBuilder<($CreativeModeTab), ($CreativeModeTab), (S)>
public "defaultCreativeTab"<P>(arg0: P, arg1: $Consumer$Type<($CreativeModeTab$Builder$Type)>): $NoConfigBuilder<($CreativeModeTab), ($CreativeModeTab), (P)>
public "genData"<T extends $RegistrateProvider>(arg0: $ProviderType$Type<(any)>, arg1: T): void
public "blockEntity"<T extends $BlockEntity>(arg0: $BlockEntityBuilder$BlockEntityFactory$Type<(T)>): $BlockEntityBuilder<(T), (S)>
public "blockEntity"<T extends $BlockEntity, P>(arg0: P, arg1: string, arg2: $BlockEntityBuilder$BlockEntityFactory$Type<(T)>): $BlockEntityBuilder<(T), (P)>
public "blockEntity"<T extends $BlockEntity, P>(arg0: P, arg1: $BlockEntityBuilder$BlockEntityFactory$Type<(T)>): $BlockEntityBuilder<(T), (P)>
public "blockEntity"<T extends $BlockEntity>(arg0: string, arg1: $BlockEntityBuilder$BlockEntityFactory$Type<(T)>): $BlockEntityBuilder<(T), (S)>
public static "isDevEnvironment"(): boolean
public "fluid"(arg0: string, arg1: $FluidBuilder$FluidTypeFactory$Type): $FluidBuilder<($ForgeFlowingFluid$Flowing), (S)>
public "fluid"(arg0: string, arg1: $NonNullSupplier$Type<($FluidType$Type)>): $FluidBuilder<($ForgeFlowingFluid$Flowing), (S)>
public "fluid"<T extends $ForgeFlowingFluid>(arg0: string, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $NonNullSupplier$Type<($FluidType$Type)>, arg4: $NonNullFunction$Type<($ForgeFlowingFluid$Properties$Type), (T)>): $FluidBuilder<(T), (S)>
public "fluid"(arg0: string): $FluidBuilder<($ForgeFlowingFluid$Flowing), (S)>
public "fluid"<T extends $ForgeFlowingFluid>(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type, arg2: $NonNullSupplier$Type<($FluidType$Type)>, arg3: $NonNullFunction$Type<($ForgeFlowingFluid$Properties$Type), (T)>): $FluidBuilder<(T), (S)>
public "fluid"<T extends $ForgeFlowingFluid>(arg0: string, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $FluidBuilder$FluidTypeFactory$Type, arg4: $NonNullFunction$Type<($ForgeFlowingFluid$Properties$Type), (T)>): $FluidBuilder<(T), (S)>
public "fluid"<T extends $ForgeFlowingFluid>(arg0: string, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $NonNullFunction$Type<($ForgeFlowingFluid$Properties$Type), (T)>): $FluidBuilder<(T), (S)>
public "fluid"(arg0: string, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $NonNullSupplier$Type<($FluidType$Type)>): $FluidBuilder<($ForgeFlowingFluid$Flowing), (S)>
public "fluid"(arg0: string, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $FluidBuilder$FluidTypeFactory$Type): $FluidBuilder<($ForgeFlowingFluid$Flowing), (S)>
public "fluid"(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type): $FluidBuilder<($ForgeFlowingFluid$Flowing), (S)>
public "fluid"(arg0: $NonNullSupplier$Type<($FluidType$Type)>): $FluidBuilder<($ForgeFlowingFluid$Flowing), (S)>
public "fluid"(arg0: $FluidBuilder$FluidTypeFactory$Type): $FluidBuilder<($ForgeFlowingFluid$Flowing), (S)>
public "fluid"(): $FluidBuilder<($ForgeFlowingFluid$Flowing), (S)>
public "fluid"(arg0: string, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type): $FluidBuilder<($ForgeFlowingFluid$Flowing), (S)>
public "fluid"<T extends $ForgeFlowingFluid>(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type, arg2: $FluidBuilder$FluidTypeFactory$Type, arg3: $NonNullFunction$Type<($ForgeFlowingFluid$Properties$Type), (T)>): $FluidBuilder<(T), (S)>
public "fluid"<T extends $ForgeFlowingFluid>(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type, arg2: $NonNullFunction$Type<($ForgeFlowingFluid$Properties$Type), (T)>): $FluidBuilder<(T), (S)>
public "fluid"(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type, arg2: $NonNullSupplier$Type<($FluidType$Type)>): $FluidBuilder<($ForgeFlowingFluid$Flowing), (S)>
public "fluid"(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type, arg2: $FluidBuilder$FluidTypeFactory$Type): $FluidBuilder<($ForgeFlowingFluid$Flowing), (S)>
public "fluid"<P>(arg0: P, arg1: string, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type, arg4: $FluidBuilder$FluidTypeFactory$Type): $FluidBuilder<($ForgeFlowingFluid$Flowing), (P)>
public "fluid"<P>(arg0: P, arg1: string, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type): $FluidBuilder<($ForgeFlowingFluid$Flowing), (P)>
public "fluid"<P>(arg0: P, arg1: string, arg2: $NonNullSupplier$Type<($FluidType$Type)>): $FluidBuilder<($ForgeFlowingFluid$Flowing), (P)>
public "fluid"<P>(arg0: P, arg1: string, arg2: $FluidBuilder$FluidTypeFactory$Type): $FluidBuilder<($ForgeFlowingFluid$Flowing), (P)>
public "fluid"<P>(arg0: P, arg1: string): $FluidBuilder<($ForgeFlowingFluid$Flowing), (P)>
public "fluid"<T extends $ForgeFlowingFluid, P>(arg0: P, arg1: string, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type, arg4: $NonNullSupplier$Type<($FluidType$Type)>, arg5: $NonNullFunction$Type<($ForgeFlowingFluid$Properties$Type), (T)>): $FluidBuilder<(T), (P)>
public "fluid"<T extends $ForgeFlowingFluid, P>(arg0: P, arg1: string, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type, arg4: $FluidBuilder$FluidTypeFactory$Type, arg5: $NonNullFunction$Type<($ForgeFlowingFluid$Properties$Type), (T)>): $FluidBuilder<(T), (P)>
public "fluid"<T extends $ForgeFlowingFluid, P>(arg0: P, arg1: string, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type, arg4: $NonNullFunction$Type<($ForgeFlowingFluid$Properties$Type), (T)>): $FluidBuilder<(T), (P)>
public "fluid"<P>(arg0: P, arg1: string, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type, arg4: $NonNullSupplier$Type<($FluidType$Type)>): $FluidBuilder<($ForgeFlowingFluid$Flowing), (P)>
public "fluid"<P>(arg0: P, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $FluidBuilder$FluidTypeFactory$Type): $FluidBuilder<($ForgeFlowingFluid$Flowing), (P)>
public "fluid"<P>(arg0: P, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type): $FluidBuilder<($ForgeFlowingFluid$Flowing), (P)>
public "fluid"<P>(arg0: P, arg1: $NonNullSupplier$Type<($FluidType$Type)>): $FluidBuilder<($ForgeFlowingFluid$Flowing), (P)>
public "fluid"<P>(arg0: P, arg1: $FluidBuilder$FluidTypeFactory$Type): $FluidBuilder<($ForgeFlowingFluid$Flowing), (P)>
public "fluid"<P>(arg0: P): $FluidBuilder<($ForgeFlowingFluid$Flowing), (P)>
public "fluid"<T extends $ForgeFlowingFluid, P>(arg0: P, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $NonNullSupplier$Type<($FluidType$Type)>, arg4: $NonNullFunction$Type<($ForgeFlowingFluid$Properties$Type), (T)>): $FluidBuilder<(T), (P)>
public "fluid"<T extends $ForgeFlowingFluid, P>(arg0: P, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $FluidBuilder$FluidTypeFactory$Type, arg4: $NonNullFunction$Type<($ForgeFlowingFluid$Properties$Type), (T)>): $FluidBuilder<(T), (P)>
public "fluid"<T extends $ForgeFlowingFluid, P>(arg0: P, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $NonNullFunction$Type<($ForgeFlowingFluid$Properties$Type), (T)>): $FluidBuilder<(T), (P)>
public "fluid"<P>(arg0: P, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: $NonNullSupplier$Type<($FluidType$Type)>): $FluidBuilder<($ForgeFlowingFluid$Flowing), (P)>
public "makeRegistry"<R>(arg0: string, arg1: $Supplier$Type<($RegistryBuilder$Type<(R)>)>): $ResourceKey<($Registry<(R)>)>
get "modid"(): string
get "modEventBus"(): $IEventBus
get "devEnvironment"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractRegistrate$Type<S> = ($AbstractRegistrate<(S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractRegistrate_<S> = $AbstractRegistrate$Type<(S)>;
}}
declare module "packages/com/tterrag/registrate/providers/$DataGenContext" {
import {$Builder, $Builder$Type} from "packages/com/tterrag/registrate/builders/$Builder"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $DataGenContext<R, E extends R> implements $NonNullSupplier<(E)> {

constructor(arg0: $NonNullSupplier$Type<(E)>, arg1: string, arg2: $ResourceLocation$Type)

public "getName"(): string
public "get"(): E
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "from"<R, E extends R>(arg0: $Builder$Type<(R), (E), (any), (any)>): $DataGenContext<(R), (E)>
/**
 * 
 * @deprecated
 */
public static "from"<R, E extends R>(arg0: $Builder$Type<(R), (E), (any), (any)>, arg1: $ResourceKey$Type<(any)>): $DataGenContext<(R), (E)>
public "getId"(): $ResourceLocation
public "getEntry"(): E
public "lazy"(): $NonNullSupplier<(E)>
public static "of"<T>(arg0: $Supplier$Type<(E)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<(E)>
public static "of"<T>(arg0: $Supplier$Type<(E)>): $NonNullSupplier<(E)>
public static "lazy"<T>(arg0: $Supplier$Type<(E)>): $NonNullSupplier<(E)>
get "name"(): string
get "id"(): $ResourceLocation
get "entry"(): E
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataGenContext$Type<R, E> = ($DataGenContext<(R), (E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataGenContext_<R, E> = $DataGenContext$Type<(R), (E)>;
}}
declare module "packages/com/tterrag/registrate/util/$DataIngredient" {
import {$RegistrateRecipeProvider, $RegistrateRecipeProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateRecipeProvider"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Ingredient$Value, $Ingredient$Value$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient$Value"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$IIngredientSerializer, $IIngredientSerializer$Type} from "packages/net/minecraftforge/common/crafting/$IIngredientSerializer"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$InventoryChangeTrigger$TriggerInstance, $InventoryChangeTrigger$TriggerInstance$Type} from "packages/net/minecraft/advancements/critereon/$InventoryChangeTrigger$TriggerInstance"
import {$ItemPredicate, $ItemPredicate$Type} from "packages/net/minecraft/advancements/critereon/$ItemPredicate"
import {$IntList, $IntList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntList"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"

export class $DataIngredient extends $Ingredient {
static readonly "EMPTY": $Ingredient
 "values": ($Ingredient$Value)[]
 "itemStacks": ($ItemStack)[]
 "stackingIds": $IntList


public "test"(arg0: $ItemStack$Type): boolean
public "getId"(): $ResourceLocation
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($ItemStack)>
public "negate"(): $Predicate<($ItemStack)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($ItemStack)>
public static "tag"(arg0: $TagKey$Type<($Item$Type)>): $DataIngredient
public static "items"<T extends $ItemLike>(arg0: $NonNullSupplier$Type<(any)>, ...arg1: ($NonNullSupplier$Type<(any)>)[]): $DataIngredient
public static "items"<T extends $ItemLike>(arg0: T, ...arg1: (T)[]): $DataIngredient
public "isSimple"(): boolean
public "toJson"(): $JsonElement
public "getStackingIds"(): $IntList
public "isEmpty"(): boolean
public "getSerializer"(): $IIngredientSerializer<($DataIngredient)>
public static "stacks"(arg0: $ItemStack$Type, ...arg1: ($ItemStack$Type)[]): $DataIngredient
public "getCritereon"(arg0: $RegistrateRecipeProvider$Type): $InventoryChangeTrigger$TriggerInstance
public static "ingredient"(arg0: $Ingredient$Type, arg1: $ResourceLocation$Type, ...arg2: ($ItemPredicate$Type)[]): $DataIngredient
public static "ingredient"(arg0: $Ingredient$Type, arg1: $ItemLike$Type): $DataIngredient
public static "ingredient"(arg0: $Ingredient$Type, arg1: $TagKey$Type<($Item$Type)>): $DataIngredient
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<(T)>
public static "isEqual"<T>(arg0: any): $Predicate<(T)>
get "id"(): $ResourceLocation
get "simple"(): boolean
get "stackingIds"(): $IntList
get "empty"(): boolean
get "serializer"(): $IIngredientSerializer<($DataIngredient)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataIngredient$Type = ($DataIngredient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataIngredient_ = $DataIngredient$Type;
}}
declare module "packages/com/tterrag/registrate/util/entry/$BlockEntityEntry" {
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/tterrag/registrate/util/entry/$RegistryEntry"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $BlockEntityEntry<T extends $BlockEntity> extends $RegistryEntry<($BlockEntityType<(T)>)> {

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: $RegistryObject$Type<($BlockEntityType$Type<(T)>)>)

public "get"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type): $Optional<(T)>
public static "cast"<T extends $BlockEntity>(arg0: $RegistryEntry$Type<($BlockEntityType$Type<(T)>)>): $BlockEntityEntry<(T)>
public "create"(arg0: $BlockPos$Type, arg1: $BlockState$Type): T
public "is"(arg0: $BlockEntity$Type): boolean
public "getNullable"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type): T
public static "of"<T>(arg0: $Supplier$Type<(T)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<(T)>
public static "of"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
public static "lazy"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityEntry$Type<T> = ($BlockEntityEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityEntry_<T> = $BlockEntityEntry$Type<(T)>;
}}
declare module "packages/com/tterrag/registrate/util/nullness/$NonNullConsumer" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"

export interface $NonNullConsumer<T> extends $Consumer<(T)> {

 "accept"(arg0: T): void
 "andThen"(arg0: $NonNullConsumer$Type<(any)>): $NonNullConsumer<(T)>
 "andThen"(arg0: $Consumer$Type<(any)>): $Consumer<(T)>

(arg0: T): void
}

export namespace $NonNullConsumer {
function noop<T>(): $NonNullConsumer<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NonNullConsumer$Type<T> = ($NonNullConsumer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NonNullConsumer_<T> = $NonNullConsumer$Type<(T)>;
}}
declare module "packages/com/tterrag/registrate/providers/$RegistrateBlockstateProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$VariantBlockStateBuilder, $VariantBlockStateBuilder$Type} from "packages/net/minecraftforge/client/model/generators/$VariantBlockStateBuilder"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$WallSide, $WallSide$Type} from "packages/net/minecraft/world/level/block/state/properties/$WallSide"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$LogicalSide, $LogicalSide$Type} from "packages/net/minecraftforge/fml/$LogicalSide"
import {$BlockStateProvider, $BlockStateProvider$Type} from "packages/net/minecraftforge/client/model/generators/$BlockStateProvider"
import {$MultiPartBlockStateBuilder, $MultiPartBlockStateBuilder$Type} from "packages/net/minecraftforge/client/model/generators/$MultiPartBlockStateBuilder"
import {$RegistrateProvider, $RegistrateProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateProvider"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ImmutableMap, $ImmutableMap$Type} from "packages/com/google/common/collect/$ImmutableMap"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"

export class $RegistrateBlockstateProvider extends $BlockStateProvider implements $RegistrateProvider {
static readonly "WALL_PROPS": $ImmutableMap<($Direction), ($Property<($WallSide)>)>

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: $PackOutput$Type, arg2: $ExistingFileHelper$Type)

public "getSide"(): $LogicalSide
public "getExistingMultipartBuilder"(arg0: $Block$Type): $Optional<($MultiPartBlockStateBuilder)>
public "getExistingVariantBuilder"(arg0: $Block$Type): $Optional<($VariantBlockStateBuilder)>
public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "side"(): $LogicalSide
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrateBlockstateProvider$Type = ($RegistrateBlockstateProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrateBlockstateProvider_ = $RegistrateBlockstateProvider$Type;
}}
declare module "packages/com/tterrag/registrate/providers/$RegistrateRecipeProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$BlockFamily$Variant, $BlockFamily$Variant$Type} from "packages/net/minecraft/data/$BlockFamily$Variant"
import {$FeatureFlagSet, $FeatureFlagSet$Type} from "packages/net/minecraft/world/flag/$FeatureFlagSet"
import {$ShapedRecipeBuilder, $ShapedRecipeBuilder$Type} from "packages/net/minecraft/data/recipes/$ShapedRecipeBuilder"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Advancement$Builder, $Advancement$Builder$Type} from "packages/net/minecraft/advancements/$Advancement$Builder"
import {$InventoryChangeTrigger$TriggerInstance, $InventoryChangeTrigger$TriggerInstance$Type} from "packages/net/minecraft/advancements/critereon/$InventoryChangeTrigger$TriggerInstance"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ShapelessRecipeBuilder, $ShapelessRecipeBuilder$Type} from "packages/net/minecraft/data/recipes/$ShapelessRecipeBuilder"
import {$RegistrateProvider, $RegistrateProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateProvider"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$RecipeCategory, $RecipeCategory$Type} from "packages/net/minecraft/data/recipes/$RecipeCategory"
import {$FinishedRecipe, $FinishedRecipe$Type} from "packages/net/minecraft/data/recipes/$FinishedRecipe"
import {$RecipeBuilder, $RecipeBuilder$Type} from "packages/net/minecraft/data/recipes/$RecipeBuilder"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$BlockFamily, $BlockFamily$Type} from "packages/net/minecraft/data/$BlockFamily"
import {$MinMaxBounds$Ints, $MinMaxBounds$Ints$Type} from "packages/net/minecraft/advancements/critereon/$MinMaxBounds$Ints"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$LogicalSide, $LogicalSide$Type} from "packages/net/minecraftforge/fml/$LogicalSide"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$ItemPredicate, $ItemPredicate$Type} from "packages/net/minecraft/advancements/critereon/$ItemPredicate"
import {$DataIngredient, $DataIngredient$Type} from "packages/com/tterrag/registrate/util/$DataIngredient"
import {$RecipeProvider, $RecipeProvider$Type} from "packages/net/minecraft/data/recipes/$RecipeProvider"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$EnterBlockTrigger$TriggerInstance, $EnterBlockTrigger$TriggerInstance$Type} from "packages/net/minecraft/advancements/critereon/$EnterBlockTrigger$TriggerInstance"

export class $RegistrateRecipeProvider extends $RecipeProvider implements $RegistrateProvider, $Consumer<($FinishedRecipe)> {
static readonly "DEFAULT_SMELT_TIME": integer
static readonly "DEFAULT_BLAST_TIME": integer
static readonly "DEFAULT_SMOKE_TIME": integer
static readonly "DEFAULT_CAMPFIRE_TIME": integer

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: $PackOutput$Type)

public "accept"(arg0: $FinishedRecipe$Type): void
public "fence"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: string): void
public "square"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: boolean): void
public static "has"(arg0: $MinMaxBounds$Ints$Type, arg1: $ItemLike$Type): $InventoryChangeTrigger$TriggerInstance
public static "has"(arg0: $ItemLike$Type): $InventoryChangeTrigger$TriggerInstance
public static "has"(arg0: $TagKey$Type<($Item$Type)>): $InventoryChangeTrigger$TriggerInstance
public "wall"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>): void
public static "wall"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $RecipeCategory$Type, arg2: $ItemLike$Type, arg3: $ItemLike$Type): void
public "m_253240_"(arg0: $CachedOutput$Type, arg1: $ResourceLocation$Type, arg2: $Advancement$Builder$Type): $CompletableFuture<(any)>
public "m_247051_"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $FeatureFlagSet$Type): void
public "saveAdvancement"(arg0: $CachedOutput$Type, arg1: $FinishedRecipe$Type, arg2: $JsonObject$Type): $CompletableFuture<(any)>
public static "carpet"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ItemLike$Type, arg2: $ItemLike$Type): void
public "getSide"(): $LogicalSide
public static "banner"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ItemLike$Type, arg2: $ItemLike$Type): void
public "campfire"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: float, arg4: integer): void
public "campfire"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: float): void
public "smelting"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: float): void
public "smelting"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: float, arg4: integer): void
public "smoking"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: float): void
public "smoking"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: float, arg4: integer): void
public "stonecutting"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: integer): void
public "stonecutting"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>): void
public "blasting"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: float): void
public "blasting"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: float, arg4: integer): void
public "food"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: float): void
public "safeId"(arg0: $DataIngredient$Type): $ResourceLocation
public "safeId"(arg0: $ResourceLocation$Type): $ResourceLocation
public "safeId"(arg0: $ItemLike$Type): $ResourceLocation
public "safeName"(arg0: $ItemLike$Type): string
public "safeName"(arg0: $DataIngredient$Type): string
public "safeName"(arg0: $ResourceLocation$Type): string
public "storage"<T extends $ItemLike>(arg0: $NonNullSupplier$Type<(any)>, arg1: $RecipeCategory$Type, arg2: $NonNullSupplier$Type<(any)>): void
public "storage"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $NonNullSupplier$Type<(any)>, arg3: $DataIngredient$Type, arg4: $NonNullSupplier$Type<(any)>): void
/**
 * 
 * @deprecated
 */
public "storage"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $NonNullSupplier$Type<(any)>): void
public "singleItemUnfinished"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: integer, arg4: integer): $ShapelessRecipeBuilder
public static "smeltingResultFromBase"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ItemLike$Type, arg2: $ItemLike$Type): void
public static "stainedGlassFromGlassAndDye"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ItemLike$Type, arg2: $ItemLike$Type): void
public static "getConversionRecipeName"(arg0: $ItemLike$Type, arg1: $ItemLike$Type): string
public static "copySmithingTemplate"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ItemLike$Type, arg2: $TagKey$Type<($Item$Type)>): void
public static "copySmithingTemplate"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ItemLike$Type, arg2: $ItemLike$Type): void
public static "getSmeltingRecipeName"(arg0: $ItemLike$Type): string
public static "stainedGlassPaneFromStainedGlass"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ItemLike$Type, arg2: $ItemLike$Type): void
public static "stonecutterResultFromBase"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $RecipeCategory$Type, arg2: $ItemLike$Type, arg3: $ItemLike$Type): void
public static "stonecutterResultFromBase"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $RecipeCategory$Type, arg2: $ItemLike$Type, arg3: $ItemLike$Type, arg4: integer): void
public "smeltingAndBlasting"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: float): void
public static "pressurePlateBuilder"(arg0: $RecipeCategory$Type, arg1: $ItemLike$Type, arg2: $Ingredient$Type): $RecipeBuilder
public static "nineBlockStorageRecipes"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $RecipeCategory$Type, arg2: $ItemLike$Type, arg3: $RecipeCategory$Type, arg4: $ItemLike$Type): void
public static "simpleCookingRecipe"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: string, arg2: $RecipeSerializer$Type<(any)>, arg3: integer, arg4: $ItemLike$Type, arg5: $ItemLike$Type, arg6: float): void
public static "getBlastingRecipeName"(arg0: $ItemLike$Type): string
public static "bedFromPlanksAndWool"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ItemLike$Type, arg2: $ItemLike$Type): void
public static "getSimpleRecipeName"(arg0: $ItemLike$Type): string
public "planks"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>): void
public "stairs"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: string, arg4: boolean): void
public static "candle"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ItemLike$Type, arg2: $ItemLike$Type): void
public static "trimSmithing"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $Item$Type, arg2: $ResourceLocation$Type): void
public "fenceGate"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: string): void
public static "oreBlasting"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $List$Type<($ItemLike$Type)>, arg2: $RecipeCategory$Type, arg3: $ItemLike$Type, arg4: float, arg5: integer, arg6: string): void
public static "netheriteSmithing"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $Item$Type, arg2: $RecipeCategory$Type, arg3: $Item$Type): void
public static "twoByTwoPacker"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $RecipeCategory$Type, arg2: $ItemLike$Type, arg3: $ItemLike$Type): void
public static "oreSmelting"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $List$Type<($ItemLike$Type)>, arg2: $RecipeCategory$Type, arg3: $ItemLike$Type, arg4: float, arg5: integer, arg6: string): void
public static "threeByThreePacker"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $RecipeCategory$Type, arg2: $ItemLike$Type, arg3: $ItemLike$Type, arg4: string): void
public static "threeByThreePacker"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $RecipeCategory$Type, arg2: $ItemLike$Type, arg3: $ItemLike$Type): void
public "singleItem"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: integer, arg4: integer): void
public "door"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: string): void
public static "oreCooking"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $RecipeSerializer$Type<(any)>, arg2: $List$Type<($ItemLike$Type)>, arg3: $RecipeCategory$Type, arg4: $ItemLike$Type, arg5: float, arg6: integer, arg7: string, arg8: string): void
public "slab"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: string, arg4: boolean): void
public static "slab"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $RecipeCategory$Type, arg2: $ItemLike$Type, arg3: $ItemLike$Type): void
public "trapDoor"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: string): void
public static "polishedBuilder"(arg0: $RecipeCategory$Type, arg1: $ItemLike$Type, arg2: $Ingredient$Type): $RecipeBuilder
public static "colorBlockWithDye"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $List$Type<($Item$Type)>, arg2: $List$Type<($Item$Type)>, arg3: string): void
public static "hangingSign"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ItemLike$Type, arg2: $ItemLike$Type): void
public static "cookRecipes"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: string, arg2: $RecipeSerializer$Type<(any)>, arg3: integer): void
public static "signBuilder"(arg0: $ItemLike$Type, arg1: $Ingredient$Type): $RecipeBuilder
public static "chiseled"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $RecipeCategory$Type, arg2: $ItemLike$Type, arg3: $ItemLike$Type): void
public static "planksFromLog"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ItemLike$Type, arg2: $TagKey$Type<($Item$Type)>, arg3: integer): void
public static "planksFromLogs"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ItemLike$Type, arg2: $TagKey$Type<($Item$Type)>, arg3: integer): void
public static "slabBuilder"(arg0: $RecipeCategory$Type, arg1: $ItemLike$Type, arg2: $Ingredient$Type): $RecipeBuilder
public static "buttonBuilder"(arg0: $ItemLike$Type, arg1: $Ingredient$Type): $RecipeBuilder
public static "trapdoorBuilder"(arg0: $ItemLike$Type, arg1: $Ingredient$Type): $RecipeBuilder
public static "wallBuilder"(arg0: $RecipeCategory$Type, arg1: $ItemLike$Type, arg2: $Ingredient$Type): $RecipeBuilder
public static "woodenBoat"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ItemLike$Type, arg2: $ItemLike$Type): void
public static "pressurePlate"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ItemLike$Type, arg2: $ItemLike$Type): void
public static "woodFromLogs"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ItemLike$Type, arg2: $ItemLike$Type): void
public static "stairBuilder"(arg0: $ItemLike$Type, arg1: $Ingredient$Type): $RecipeBuilder
public static "fenceBuilder"(arg0: $ItemLike$Type, arg1: $Ingredient$Type): $RecipeBuilder
public static "polished"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $RecipeCategory$Type, arg2: $ItemLike$Type, arg3: $ItemLike$Type): void
public static "chestBoat"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ItemLike$Type, arg2: $ItemLike$Type): void
public static "cutBuilder"(arg0: $RecipeCategory$Type, arg1: $ItemLike$Type, arg2: $Ingredient$Type): $ShapedRecipeBuilder
public static "concretePowder"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ItemLike$Type, arg2: $ItemLike$Type): void
public static "mosaicBuilder"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $RecipeCategory$Type, arg2: $ItemLike$Type, arg3: $ItemLike$Type): void
public static "chiseledBuilder"(arg0: $RecipeCategory$Type, arg1: $ItemLike$Type, arg2: $Ingredient$Type): $ShapedRecipeBuilder
public static "doorBuilder"(arg0: $ItemLike$Type, arg1: $Ingredient$Type): $RecipeBuilder
public static "fenceGateBuilder"(arg0: $ItemLike$Type, arg1: $Ingredient$Type): $RecipeBuilder
public static "generateRecipes"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $BlockFamily$Type): void
public static "getItemName"(arg0: $ItemLike$Type): string
public static "inventoryTrigger"(...arg0: ($ItemPredicate$Type)[]): $InventoryChangeTrigger$TriggerInstance
public static "getBaseBlock"(arg0: $BlockFamily$Type, arg1: $BlockFamily$Variant$Type): $Block
public static "getHasName"(arg0: $ItemLike$Type): string
public static "waxRecipes"(arg0: $Consumer$Type<($FinishedRecipe$Type)>): void
public static "insideOf"(arg0: $Block$Type): $EnterBlockTrigger$TriggerInstance
public static "stainedGlassPaneFromGlassPaneAndDye"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ItemLike$Type, arg2: $ItemLike$Type): void
public static "nineBlockStorageRecipesRecipesWithCustomUnpacking"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $RecipeCategory$Type, arg2: $ItemLike$Type, arg3: $RecipeCategory$Type, arg4: $ItemLike$Type, arg5: string, arg6: string): void
public static "nineBlockStorageRecipesWithCustomPacking"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $RecipeCategory$Type, arg2: $ItemLike$Type, arg3: $RecipeCategory$Type, arg4: $ItemLike$Type, arg5: string, arg6: string): void
public static "coloredTerracottaFromTerracottaAndDye"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $ItemLike$Type, arg2: $ItemLike$Type): void
public "cooking"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: float, arg4: integer, arg5: $RecipeSerializer$Type<(any)>): void
public "cooking"<T extends $ItemLike>(arg0: $DataIngredient$Type, arg1: $RecipeCategory$Type, arg2: $Supplier$Type<(any)>, arg3: float, arg4: integer, arg5: string, arg6: $RecipeSerializer$Type<(any)>): void
public static "cut"(arg0: $Consumer$Type<($FinishedRecipe$Type)>, arg1: $RecipeCategory$Type, arg2: $ItemLike$Type, arg3: $ItemLike$Type): void
public "andThen"(arg0: $Consumer$Type<(any)>): $Consumer<($FinishedRecipe)>
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "side"(): $LogicalSide
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrateRecipeProvider$Type = ($RegistrateRecipeProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrateRecipeProvider_ = $RegistrateRecipeProvider$Type;
}}
declare module "packages/com/tterrag/registrate/builders/$BlockBuilder" {
import {$RegistrateRecipeProvider, $RegistrateRecipeProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateRecipeProvider"
import {$BlockEntityBuilder, $BlockEntityBuilder$Type} from "packages/com/tterrag/registrate/builders/$BlockEntityBuilder"
import {$NonNullUnaryOperator, $NonNullUnaryOperator$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullUnaryOperator"
import {$BlockColor, $BlockColor$Type} from "packages/net/minecraft/client/color/block/$BlockColor"
import {$RegistrateBlockstateProvider, $RegistrateBlockstateProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateBlockstateProvider"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$DataGenContext, $DataGenContext$Type} from "packages/com/tterrag/registrate/providers/$DataGenContext"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractBuilder, $AbstractBuilder$Type} from "packages/com/tterrag/registrate/builders/$AbstractBuilder"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$BuilderCallback, $BuilderCallback$Type} from "packages/com/tterrag/registrate/builders/$BuilderCallback"
import {$BlockEntityBuilder$BlockEntityFactory, $BlockEntityBuilder$BlockEntityFactory$Type} from "packages/com/tterrag/registrate/builders/$BlockEntityBuilder$BlockEntityFactory"
import {$ItemBuilder, $ItemBuilder$Type} from "packages/com/tterrag/registrate/builders/$ItemBuilder"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$NonNullBiFunction, $NonNullBiFunction$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullBiFunction"
import {$BlockItem, $BlockItem$Type} from "packages/net/minecraft/world/item/$BlockItem"
import {$NonNullBiConsumer, $NonNullBiConsumer$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullBiConsumer"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$RegistrateBlockLootTables, $RegistrateBlockLootTables$Type} from "packages/com/tterrag/registrate/providers/loot/$RegistrateBlockLootTables"
import {$NonNullFunction, $NonNullFunction$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullFunction"

export class $BlockBuilder<T extends $Block, P> extends $AbstractBuilder<($Block), (T), (P), ($BlockBuilder<(T), (P)>)> {


public "properties"(arg0: $NonNullUnaryOperator$Type<($BlockBehaviour$Properties$Type)>): $BlockBuilder<(T), (P)>
public static "create"<T extends $Block, P>(arg0: $AbstractRegistrate$Type<(any)>, arg1: P, arg2: string, arg3: $BuilderCallback$Type, arg4: $NonNullFunction$Type<($BlockBehaviour$Properties$Type), (T)>): $BlockBuilder<(T), (P)>
public "color"(arg0: $NonNullSupplier$Type<($Supplier$Type<($BlockColor$Type)>)>): $BlockBuilder<(T), (P)>
public "lang"(arg0: string): $BlockBuilder<(T), (P)>
public "recipe"(arg0: $NonNullBiConsumer$Type<($DataGenContext$Type<($Block$Type), (T)>), ($RegistrateRecipeProvider$Type)>): $BlockBuilder<(T), (P)>
public "tag"(...arg0: ($TagKey$Type<($Block$Type)>)[]): $BlockBuilder<(T), (P)>
public "item"<I extends $Item>(arg0: $NonNullBiFunction$Type<(any), ($Item$Properties$Type), (any)>): $ItemBuilder<(I), ($BlockBuilder<(T), (P)>)>
public "item"(): $ItemBuilder<($BlockItem), ($BlockBuilder<(T), (P)>)>
public "initialProperties"(arg0: $NonNullSupplier$Type<(any)>): $BlockBuilder<(T), (P)>
/**
 * 
 * @deprecated
 */
public "addLayer"(arg0: $Supplier$Type<($Supplier$Type<($RenderType$Type)>)>): $BlockBuilder<(T), (P)>
public "simpleBlockEntity"<BE extends $BlockEntity>(arg0: $BlockEntityBuilder$BlockEntityFactory$Type<(BE)>): $BlockBuilder<(T), (P)>
public "blockstate"(arg0: $NonNullBiConsumer$Type<($DataGenContext$Type<($Block$Type), (T)>), ($RegistrateBlockstateProvider$Type)>): $BlockBuilder<(T), (P)>
public "defaultBlockstate"(): $BlockBuilder<(T), (P)>
public "defaultLang"(): $BlockBuilder<(T), (P)>
public "defaultLoot"(): $BlockBuilder<(T), (P)>
public "simpleItem"(): $BlockBuilder<(T), (P)>
public "loot"(arg0: $NonNullBiConsumer$Type<($RegistrateBlockLootTables$Type), (T)>): $BlockBuilder<(T), (P)>
public "blockEntity"<BE extends $BlockEntity>(arg0: $BlockEntityBuilder$BlockEntityFactory$Type<(BE)>): $BlockEntityBuilder<(BE), ($BlockBuilder<(T), (P)>)>
public static "of"<T>(arg0: $Supplier$Type<(T)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<(T)>
public static "of"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
public static "lazy"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockBuilder$Type<T, P> = ($BlockBuilder<(T), (P)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockBuilder_<T, P> = $BlockBuilder$Type<(T), (P)>;
}}
declare module "packages/com/tterrag/registrate/providers/$RegistrateGenericProvider$GeneratorData" {
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"

export class $RegistrateGenericProvider$GeneratorData extends $Record {

constructor(output: $PackOutput$Type, registries: $CompletableFuture$Type<($HolderLookup$Provider$Type)>, existingFileHelper: $ExistingFileHelper$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "output"(): $PackOutput
public "existingFileHelper"(): $ExistingFileHelper
public "registries"(): $CompletableFuture<($HolderLookup$Provider)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrateGenericProvider$GeneratorData$Type = ($RegistrateGenericProvider$GeneratorData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrateGenericProvider$GeneratorData_ = $RegistrateGenericProvider$GeneratorData$Type;
}}
declare module "packages/com/tterrag/registrate/providers/$ProviderType" {
import {$RegistrateRecipeProvider, $RegistrateRecipeProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateRecipeProvider"
import {$RegistrateAdvancementProvider, $RegistrateAdvancementProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateAdvancementProvider"
import {$RegistrateTagsProvider$IntrinsicImpl, $RegistrateTagsProvider$IntrinsicImpl$Type} from "packages/com/tterrag/registrate/providers/$RegistrateTagsProvider$IntrinsicImpl"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$NonNullUnaryOperator, $NonNullUnaryOperator$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullUnaryOperator"
import {$RegistrateLangProvider, $RegistrateLangProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateLangProvider"
import {$RegistrateBlockstateProvider, $RegistrateBlockstateProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateBlockstateProvider"
import {$GatherDataEvent, $GatherDataEvent$Type} from "packages/net/minecraftforge/data/event/$GatherDataEvent"
import {$RegistrateLootTableProvider, $RegistrateLootTableProvider$Type} from "packages/com/tterrag/registrate/providers/loot/$RegistrateLootTableProvider"
import {$RegistrateItemModelProvider, $RegistrateItemModelProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateItemModelProvider"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$RegistrateItemTagsProvider, $RegistrateItemTagsProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateItemTagsProvider"
import {$NonNullBiFunction, $NonNullBiFunction$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullBiFunction"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$RegistrateProvider, $RegistrateProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateProvider"
import {$RegistrateGenericProvider, $RegistrateGenericProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateGenericProvider"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$NonNullFunction, $NonNullFunction$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullFunction"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $ProviderType<T extends $RegistrateProvider> {

 "create"(arg0: $AbstractRegistrate$Type<(any)>, arg1: $GatherDataEvent$Type, arg2: $Map$Type<($ProviderType$Type<(any)>), ($RegistrateProvider$Type)>): T

(arg0: string, arg1: $ProviderType$Type<(T)>): $ProviderType<(T)>
}

export namespace $ProviderType {
const RECIPE: $ProviderType<($RegistrateRecipeProvider)>
const ADVANCEMENT: $ProviderType<($RegistrateAdvancementProvider)>
const LOOT: $ProviderType<($RegistrateLootTableProvider)>
const BLOCK_TAGS: $ProviderType<($RegistrateTagsProvider$IntrinsicImpl<($Block)>)>
const ITEM_TAGS: $ProviderType<($RegistrateItemTagsProvider)>
const FLUID_TAGS: $ProviderType<($RegistrateTagsProvider$IntrinsicImpl<($Fluid)>)>
const ENTITY_TAGS: $ProviderType<($RegistrateTagsProvider$IntrinsicImpl<($EntityType<(any)>)>)>
const GENERIC_SERVER: $ProviderType<($RegistrateGenericProvider)>
const BLOCKSTATE: $ProviderType<($RegistrateBlockstateProvider)>
const ITEM_MODEL: $ProviderType<($RegistrateItemModelProvider)>
const LANG: $ProviderType<($RegistrateLangProvider)>
const GENERIC_CLIENT: $ProviderType<($RegistrateGenericProvider)>
function register<T>(arg0: string, arg1: $ProviderType$Type<(T)>): $ProviderType<(T)>
function register<T>(arg0: string, arg1: $NonNullBiFunction$Type<($AbstractRegistrate$Type<(any)>), ($GatherDataEvent$Type), (T)>): $ProviderType<(T)>
function register<T>(arg0: string, arg1: $NonNullFunction$Type<($ProviderType$Type<(T)>), ($NonNullBiFunction$Type<($AbstractRegistrate$Type<(any)>), ($GatherDataEvent$Type), (T)>)>): $ProviderType<(T)>
function registerDelegate<T>(arg0: string, arg1: $NonNullUnaryOperator$Type<($ProviderType$Type<(T)>)>): $ProviderType<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProviderType$Type<T> = ($ProviderType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProviderType_<T> = $ProviderType$Type<(T)>;
}}
declare module "packages/com/tterrag/registrate/providers/$RegistrateItemModelProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ItemModelBuilder, $ItemModelBuilder$Type} from "packages/net/minecraftforge/client/model/generators/$ItemModelBuilder"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$ItemModelProvider, $ItemModelProvider$Type} from "packages/net/minecraftforge/client/model/generators/$ItemModelProvider"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LogicalSide, $LogicalSide$Type} from "packages/net/minecraftforge/fml/$LogicalSide"
import {$RegistrateProvider, $RegistrateProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateProvider"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $RegistrateItemModelProvider extends $ItemModelProvider implements $RegistrateProvider {
static readonly "BLOCK_FOLDER": string
static readonly "ITEM_FOLDER": string
readonly "generatedModels": $Map<($ResourceLocation), (T)>
readonly "existingFileHelper": $ExistingFileHelper

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: $PackOutput$Type, arg2: $ExistingFileHelper$Type)

public "name"(arg0: $NonNullSupplier$Type<(any)>): string
public "generated"(arg0: $NonNullSupplier$Type<(any)>, ...arg1: ($ResourceLocation$Type)[]): $ItemModelBuilder
public "generated"(arg0: $NonNullSupplier$Type<(any)>): $ItemModelBuilder
public "getSide"(): $LogicalSide
public "handheld"(arg0: $NonNullSupplier$Type<(any)>, arg1: $ResourceLocation$Type): $ItemModelBuilder
public "handheld"(arg0: $NonNullSupplier$Type<(any)>): $ItemModelBuilder
public "modid"(arg0: $NonNullSupplier$Type<(any)>): string
public "blockSprite"(arg0: $NonNullSupplier$Type<(any)>, arg1: $ResourceLocation$Type): $ItemModelBuilder
public "blockSprite"(arg0: $NonNullSupplier$Type<(any)>): $ItemModelBuilder
public "itemTexture"(arg0: $NonNullSupplier$Type<(any)>): $ResourceLocation
public "getName"(): string
public "blockItem"(arg0: $NonNullSupplier$Type<(any)>): $ItemModelBuilder
public "blockItem"(arg0: $NonNullSupplier$Type<(any)>, arg1: string): $ItemModelBuilder
public "blockWithInventoryModel"(arg0: $NonNullSupplier$Type<(any)>): $ItemModelBuilder
public "run"(arg0: $CachedOutput$Type): $CompletableFuture<(any)>
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "side"(): $LogicalSide
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrateItemModelProvider$Type = ($RegistrateItemModelProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrateItemModelProvider_ = $RegistrateItemModelProvider$Type;
}}
declare module "packages/com/tterrag/registrate/providers/loot/$RegistrateEntityLootTables" {
import {$RegistrateLootTables, $RegistrateLootTables$Type} from "packages/com/tterrag/registrate/providers/loot/$RegistrateLootTables"
import {$LootTable$Builder, $LootTable$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/$LootTable$Builder"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LootItemCondition$Builder, $LootItemCondition$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemCondition$Builder"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$VanillaEntityLoot, $VanillaEntityLoot$Type} from "packages/net/minecraft/data/loot/packs/$VanillaEntityLoot"
import {$FrogVariant, $FrogVariant$Type} from "packages/net/minecraft/world/entity/animal/$FrogVariant"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$LootTable, $LootTable$Type} from "packages/net/minecraft/world/level/storage/loot/$LootTable"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $RegistrateEntityLootTables extends $VanillaEntityLoot implements $RegistrateLootTables {

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: $Consumer$Type<($RegistrateEntityLootTables$Type)>)

public static "createSheepTable"(arg0: $ItemLike$Type): $LootTable$Builder
public "generate"(): void
public "m_247253_"(arg0: $FrogVariant$Type): $LootItemCondition$Builder
public "m_247520_"(arg0: $EntityType$Type<(any)>, arg1: $ResourceLocation$Type, arg2: $LootTable$Builder$Type): void
public "m_245309_"(arg0: $EntityType$Type<(any)>, arg1: $LootTable$Builder$Type): void
public "m_245552_"(arg0: $EntityType$Type<(any)>): boolean
public "validate"(arg0: $Map$Type<($ResourceLocation$Type), ($LootTable$Type)>, arg1: $ValidationContext$Type): void
public "generate"(arg0: $BiConsumer$Type<($ResourceLocation$Type), ($LootTable$Builder$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrateEntityLootTables$Type = ($RegistrateEntityLootTables);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrateEntityLootTables_ = $RegistrateEntityLootTables$Type;
}}
declare module "packages/com/tterrag/registrate/util/nullness/$NonNullBiConsumer" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"

export interface $NonNullBiConsumer<T, U> extends $BiConsumer<(T), (U)> {

 "accept"(arg0: T, arg1: U): void
 "andThen"(arg0: $BiConsumer$Type<(any), (any)>): $BiConsumer<(T), (U)>

(arg0: T, arg1: U): void
}

export namespace $NonNullBiConsumer {
function noop<T, U>(): $NonNullBiConsumer<(T), (U)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NonNullBiConsumer$Type<T, U> = ($NonNullBiConsumer<(T), (U)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NonNullBiConsumer_<T, U> = $NonNullBiConsumer$Type<(T), (U)>;
}}
declare module "packages/com/tterrag/registrate/util/entry/$ItemProviderEntry" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/tterrag/registrate/util/entry/$RegistryEntry"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $ItemProviderEntry<T extends $ItemLike> extends $RegistryEntry<(T)> implements $ItemLike {

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: $RegistryObject$Type<(T)>)

public "is"(arg0: $Item$Type): boolean
public "asItem"(): $Item
public "asStack"(arg0: integer): $ItemStack
public "asStack"(): $ItemStack
public "isIn"(arg0: $ItemStack$Type): boolean
public static "of"<T>(arg0: $Supplier$Type<(T)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<(T)>
public static "of"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
public static "lazy"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemProviderEntry$Type<T> = ($ItemProviderEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemProviderEntry_<T> = $ItemProviderEntry$Type<(T)>;
}}
declare module "packages/com/tterrag/registrate/providers/loot/$RegistrateLootTableProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$RegistrateLootTables, $RegistrateLootTables$Type} from "packages/com/tterrag/registrate/providers/loot/$RegistrateLootTables"
import {$NonNullConsumer, $NonNullConsumer$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullConsumer"
import {$LootTableProvider$SubProviderEntry, $LootTableProvider$SubProviderEntry$Type} from "packages/net/minecraft/data/loot/$LootTableProvider$SubProviderEntry"
import {$LootTable$Builder, $LootTable$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/$LootTable$Builder"
import {$LootTableProvider, $LootTableProvider$Type} from "packages/net/minecraft/data/loot/$LootTableProvider"
import {$LootContextParamSet, $LootContextParamSet$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParamSet"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LogicalSide, $LogicalSide$Type} from "packages/net/minecraftforge/fml/$LogicalSide"
import {$RegistrateLootTableProvider$LootType, $RegistrateLootTableProvider$LootType$Type} from "packages/com/tterrag/registrate/providers/loot/$RegistrateLootTableProvider$LootType"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$RegistrateProvider, $RegistrateProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateProvider"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"

export class $RegistrateLootTableProvider extends $LootTableProvider implements $RegistrateProvider {

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: $PackOutput$Type)

public "addLootAction"(arg0: $LootContextParamSet$Type, arg1: $Consumer$Type<($BiConsumer$Type<($ResourceLocation$Type), ($LootTable$Builder$Type)>)>): void
public "addLootAction"<T extends $RegistrateLootTables>(arg0: $RegistrateLootTableProvider$LootType$Type<(T)>, arg1: $NonNullConsumer$Type<(T)>): void
public "getSide"(): $LogicalSide
public "getTables"(): $List<($LootTableProvider$SubProviderEntry)>
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "side"(): $LogicalSide
get "tables"(): $List<($LootTableProvider$SubProviderEntry)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrateLootTableProvider$Type = ($RegistrateLootTableProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrateLootTableProvider_ = $RegistrateLootTableProvider$Type;
}}
declare module "packages/com/tterrag/registrate/providers/$RegistrateGenericProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$RegistrateGenericProvider$Generator, $RegistrateGenericProvider$Generator$Type} from "packages/com/tterrag/registrate/providers/$RegistrateGenericProvider$Generator"
import {$RegistrateProvider, $RegistrateProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateProvider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$LogicalSide, $LogicalSide$Type} from "packages/net/minecraftforge/fml/$LogicalSide"

export class $RegistrateGenericProvider implements $RegistrateProvider {


public "add"(arg0: $RegistrateGenericProvider$Generator$Type): $RegistrateGenericProvider
public "getSide"(): $LogicalSide
public "run"(arg0: $CachedOutput$Type): $CompletableFuture<(any)>
public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "side"(): $LogicalSide
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrateGenericProvider$Type = ($RegistrateGenericProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrateGenericProvider_ = $RegistrateGenericProvider$Type;
}}
declare module "packages/com/tterrag/registrate/builders/$AbstractBuilder" {
import {$ProviderType, $ProviderType$Type} from "packages/com/tterrag/registrate/providers/$ProviderType"
import {$NonNullFunction, $NonNullFunction$Type} from "packages/net/minecraftforge/common/util/$NonNullFunction"
import {$Builder, $Builder$Type} from "packages/com/tterrag/registrate/builders/$Builder"
import {$NonNullConsumer, $NonNullConsumer$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullConsumer"
import {$BuilderCallback, $BuilderCallback$Type} from "packages/com/tterrag/registrate/builders/$BuilderCallback"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$RegistrateTagsProvider, $RegistrateTagsProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateTagsProvider"
import {$DataGenContext, $DataGenContext$Type} from "packages/com/tterrag/registrate/providers/$DataGenContext"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$RegistrateProvider, $RegistrateProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateProvider"
import {$TagsProvider, $TagsProvider$Type} from "packages/net/minecraft/data/tags/$TagsProvider"
import {$NonNullBiConsumer, $NonNullBiConsumer$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullBiConsumer"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/tterrag/registrate/util/entry/$RegistryEntry"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$NonNullFunction as $NonNullFunction$0, $NonNullFunction$Type as $NonNullFunction$0$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullFunction"

export class $AbstractBuilder<R, T extends R, P, S extends $AbstractBuilder<(R), (T), (P), (S)>> implements $Builder<(R), (T), (P), (S)> {

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: P, arg2: string, arg3: $BuilderCallback$Type, arg4: $ResourceKey$Type<($Registry$Type<(R)>)>)

public "getName"(): string
public "register"(): $RegistryEntry<(T)>
public "getParent"(): P
public "lang"(arg0: $NonNullFunction$Type<(T), (string)>, arg1: string): S
public "lang"(arg0: $NonNullFunction$Type<(T), (string)>): S
public "getOwner"(): $AbstractRegistrate<(any)>
public "tag"<TP extends ($TagsProvider<(R)>) & ($RegistrateTagsProvider<(R)>)>(arg0: $ProviderType$Type<(any)>, ...arg1: ($TagKey$Type<(R)>)[]): S
public "asSupplier"(): $NonNullSupplier<(T)>
public "removeTag"<TP extends ($TagsProvider<(R)>) & ($RegistrateTagsProvider<(R)>)>(arg0: $ProviderType$Type<(TP)>, ...arg1: ($TagKey$Type<(R)>)[]): S
public "getRegistryKey"(): $ResourceKey<($Registry<(R)>)>
public "transform"<R2, T2 extends R2, P2, S2 extends $Builder<(R2), (T2), (P2), (S2)>>(arg0: $NonNullFunction$0$Type<(S), (S2)>): S2
public "build"(): P
public "getEntry"(): T
public "setData"<D extends $RegistrateProvider>(arg0: $ProviderType$Type<(any)>, arg1: $NonNullBiConsumer$Type<($DataGenContext$Type<(R), (T)>), (D)>): S
public "onRegister"(arg0: $NonNullConsumer$Type<(any)>): S
public "addMiscData"<D extends $RegistrateProvider>(arg0: $ProviderType$Type<(any)>, arg1: $NonNullConsumer$Type<(any)>): S
public "onRegisterAfter"<OR>(arg0: $ResourceKey$Type<(any)>, arg1: $NonNullConsumer$Type<(any)>): S
public static "of"<T>(arg0: $Supplier$Type<($RegistryEntry$Type<(T)>)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<($RegistryEntry<(T)>)>
public static "of"<T>(arg0: $Supplier$Type<($RegistryEntry$Type<(T)>)>): $NonNullSupplier<($RegistryEntry<(T)>)>
public static "lazy"<T>(arg0: $Supplier$Type<($RegistryEntry$Type<(T)>)>): $NonNullSupplier<($RegistryEntry<(T)>)>
public "lazy"(): $NonNullSupplier<($RegistryEntry<(T)>)>
get "name"(): string
get "parent"(): P
get "owner"(): $AbstractRegistrate<(any)>
get "registryKey"(): $ResourceKey<($Registry<(R)>)>
get "entry"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractBuilder$Type<R, T, P, S> = ($AbstractBuilder<(R), (T), (P), (S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractBuilder_<R, T, P, S> = $AbstractBuilder$Type<(R), (T), (P), (S)>;
}}
declare module "packages/com/tterrag/registrate/providers/$RegistrateGenericProvider$Generator" {
import {$DataProvider, $DataProvider$Type} from "packages/net/minecraft/data/$DataProvider"
import {$RegistrateGenericProvider$GeneratorData, $RegistrateGenericProvider$GeneratorData$Type} from "packages/com/tterrag/registrate/providers/$RegistrateGenericProvider$GeneratorData"

export interface $RegistrateGenericProvider$Generator {

 "generate"(arg0: $RegistrateGenericProvider$GeneratorData$Type): $DataProvider

(arg0: $RegistrateGenericProvider$GeneratorData$Type): $DataProvider
}

export namespace $RegistrateGenericProvider$Generator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrateGenericProvider$Generator$Type = ($RegistrateGenericProvider$Generator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrateGenericProvider$Generator_ = $RegistrateGenericProvider$Generator$Type;
}}
declare module "packages/com/tterrag/registrate/providers/$RegistrateAdvancementProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$Advancement, $Advancement$Type} from "packages/net/minecraft/advancements/$Advancement"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$LogicalSide, $LogicalSide$Type} from "packages/net/minecraftforge/fml/$LogicalSide"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$RegistrateProvider, $RegistrateProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateProvider"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"

export class $RegistrateAdvancementProvider implements $RegistrateProvider, $Consumer<($Advancement)> {

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: $PackOutput$Type, arg2: $CompletableFuture$Type<($HolderLookup$Provider$Type)>)

public "accept"(arg0: $Advancement$Type): void
public "desc"(arg0: string, arg1: string, arg2: string): $MutableComponent
public "getSide"(): $LogicalSide
public "title"(arg0: string, arg1: string, arg2: string): $MutableComponent
public "run"(arg0: $CachedOutput$Type): $CompletableFuture<(any)>
public "getName"(): string
public "andThen"(arg0: $Consumer$Type<(any)>): $Consumer<($Advancement)>
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "side"(): $LogicalSide
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrateAdvancementProvider$Type = ($RegistrateAdvancementProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrateAdvancementProvider_ = $RegistrateAdvancementProvider$Type;
}}
declare module "packages/com/tterrag/registrate/builders/$Builder" {
import {$ProviderType, $ProviderType$Type} from "packages/com/tterrag/registrate/providers/$ProviderType"
import {$RegistrateProvider, $RegistrateProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateProvider"
import {$NonNullBiConsumer, $NonNullBiConsumer$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullBiConsumer"
import {$NonNullConsumer, $NonNullConsumer$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullConsumer"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/tterrag/registrate/util/entry/$RegistryEntry"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$DataGenContext, $DataGenContext$Type} from "packages/com/tterrag/registrate/providers/$DataGenContext"
import {$NonNullFunction, $NonNullFunction$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullFunction"

export interface $Builder<R, T extends R, P, S extends $Builder<(R), (T), (P), (S)>> extends $NonNullSupplier<($RegistryEntry<(T)>)> {

 "getName"(): string
 "transform"<R2, T2 extends R2, P2, S2 extends $Builder<(R2), (T2), (P2), (S2)>>(arg0: $NonNullFunction$Type<(S), (S2)>): S2
 "register"(): $RegistryEntry<(T)>
 "getParent"(): P
 "build"(): P
 "getOwner"(): $AbstractRegistrate<(any)>
 "getEntry"(): T
 "setData"<D extends $RegistrateProvider>(arg0: $ProviderType$Type<(any)>, arg1: $NonNullBiConsumer$Type<($DataGenContext$Type<(R), (T)>), (D)>): S
 "onRegister"(arg0: $NonNullConsumer$Type<(any)>): S
 "asSupplier"(): $NonNullSupplier<(T)>
 "addMiscData"<D extends $RegistrateProvider>(arg0: $ProviderType$Type<(any)>, arg1: $NonNullConsumer$Type<(any)>): S
 "onRegisterAfter"<OR>(arg0: $ResourceKey$Type<(any)>, arg1: $NonNullConsumer$Type<(any)>): S
 "getRegistryKey"(): $ResourceKey<(any)>
 "lazy"(): $NonNullSupplier<($RegistryEntry<(T)>)>
}

export namespace $Builder {
function of<T>(arg0: $Supplier$Type<($RegistryEntry$Type<(T)>)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<($RegistryEntry<(T)>)>
function of<T>(arg0: $Supplier$Type<($RegistryEntry$Type<(T)>)>): $NonNullSupplier<($RegistryEntry<(T)>)>
function lazy<T>(arg0: $Supplier$Type<($RegistryEntry$Type<(T)>)>): $NonNullSupplier<($RegistryEntry<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Builder$Type<R, T, P, S> = ($Builder<(R), (T), (P), (S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Builder_<R, T, P, S> = $Builder$Type<(R), (T), (P), (S)>;
}}
declare module "packages/com/tterrag/registrate/util/entry/$RegistryEntry" {
import {$IForgeRegistry, $IForgeRegistry$Type} from "packages/net/minecraftforge/registries/$IForgeRegistry"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$RegisterEvent, $RegisterEvent$Type} from "packages/net/minecraftforge/registries/$RegisterEvent"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $RegistryEntry<T> implements $NonNullSupplier<(T)> {

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: $RegistryObject$Type<(T)>)

public "get"(): T
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "map"<U>(arg0: $Function$Type<(any), (any)>): $Optional<(U)>
public "stream"(): $Stream<(T)>
public "filter"(arg0: $Predicate$Type<(any)>): $RegistryEntry<(T)>
public "getKey"(): $ResourceKey<(T)>
public static "empty"<T>(): $RegistryEntry<(T)>
public "flatMap"<U>(arg0: $Function$Type<(any), ($Optional$Type<(U)>)>): $Optional<(U)>
public "isPresent"(): boolean
public "getId"(): $ResourceLocation
public "orElse"(arg0: T): T
public "orElseThrow"<X extends $Throwable>(arg0: $Supplier$Type<(any)>): T
public "is"<R>(arg0: R): boolean
public "ifPresent"(arg0: $Consumer$Type<(any)>): void
public "orElseGet"(arg0: $Supplier$Type<(any)>): T
public "getUnchecked"(): T
public "getSibling"<R, E extends R>(arg0: $IForgeRegistry$Type<(R)>): $RegistryEntry<(E)>
public "getSibling"<R, E extends R>(arg0: $ResourceKey$Type<(any)>): $RegistryEntry<(E)>
/**
 * 
 * @deprecated
 */
public "updateReference"(arg0: $IForgeRegistry$Type<(any)>): void
public "updateReference"(arg0: $RegisterEvent$Type): void
public "lazyMap"<U>(arg0: $Function$Type<(any), (any)>): $Supplier<(U)>
public "getHolder"(): $Optional<($Holder<(T)>)>
public static "of"<T>(arg0: $Supplier$Type<(T)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<(T)>
public static "of"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
public static "lazy"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
public "lazy"(): $NonNullSupplier<(T)>
get "key"(): $ResourceKey<(T)>
get "present"(): boolean
get "id"(): $ResourceLocation
get "unchecked"(): T
get "holder"(): $Optional<($Holder<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryEntry$Type<T> = ($RegistryEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryEntry_<T> = $RegistryEntry$Type<(T)>;
}}
declare module "packages/com/tterrag/registrate/providers/$RegistrateProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$DataProvider, $DataProvider$Type} from "packages/net/minecraft/data/$DataProvider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$LogicalSide, $LogicalSide$Type} from "packages/net/minecraftforge/fml/$LogicalSide"

export interface $RegistrateProvider extends $DataProvider {

 "getSide"(): $LogicalSide
 "run"(arg0: $CachedOutput$Type): $CompletableFuture<(any)>
 "getName"(): string
}

export namespace $RegistrateProvider {
function saveStable(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrateProvider$Type = ($RegistrateProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrateProvider_ = $RegistrateProvider$Type;
}}
declare module "packages/com/tterrag/registrate/builders/$FluidBuilder$FluidTypeFactory" {
import {$FluidType$Properties, $FluidType$Properties$Type} from "packages/net/minecraftforge/fluids/$FluidType$Properties"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$FluidType, $FluidType$Type} from "packages/net/minecraftforge/fluids/$FluidType"

export interface $FluidBuilder$FluidTypeFactory {

 "create"(arg0: $FluidType$Properties$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type): $FluidType

(arg0: $FluidType$Properties$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type): $FluidType
}

export namespace $FluidBuilder$FluidTypeFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidBuilder$FluidTypeFactory$Type = ($FluidBuilder$FluidTypeFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidBuilder$FluidTypeFactory_ = $FluidBuilder$FluidTypeFactory$Type;
}}
declare module "packages/com/tterrag/registrate/util/entry/$EntityEntry" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/tterrag/registrate/util/entry/$RegistryEntry"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EntityEntry<T extends $Entity> extends $RegistryEntry<($EntityType<(T)>)> {

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: $RegistryObject$Type<($EntityType$Type<(T)>)>)

public static "cast"<T extends $Entity>(arg0: $RegistryEntry$Type<($EntityType$Type<(T)>)>): $EntityEntry<(T)>
public "create"(arg0: $Level$Type): T
public "is"(arg0: $Entity$Type): boolean
public static "of"<T>(arg0: $Supplier$Type<(T)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<(T)>
public static "of"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
public static "lazy"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityEntry$Type<T> = ($EntityEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityEntry_<T> = $EntityEntry$Type<(T)>;
}}
declare module "packages/com/tterrag/registrate/builders/$MenuBuilder$ScreenFactory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"
import {$MenuAccess, $MenuAccess$Type} from "packages/net/minecraft/client/gui/screens/inventory/$MenuAccess"

export interface $MenuBuilder$ScreenFactory<M extends $AbstractContainerMenu, T extends ($Screen) & ($MenuAccess<(M)>)> {

 "create"(arg0: M, arg1: $Inventory$Type, arg2: $Component$Type): T

(arg0: M, arg1: $Inventory$Type, arg2: $Component$Type): T
}

export namespace $MenuBuilder$ScreenFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuBuilder$ScreenFactory$Type<M, T> = ($MenuBuilder$ScreenFactory<(M), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuBuilder$ScreenFactory_<M, T> = $MenuBuilder$ScreenFactory$Type<(M), (T)>;
}}
declare module "packages/com/tterrag/registrate/builders/$BlockEntityBuilder$BlockEntityFactory" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $BlockEntityBuilder$BlockEntityFactory<T extends $BlockEntity> {

 "create"(arg0: $BlockEntityType$Type<(T)>, arg1: $BlockPos$Type, arg2: $BlockState$Type): T

(arg0: $BlockEntityType$Type<(T)>, arg1: $BlockPos$Type, arg2: $BlockState$Type): T
}

export namespace $BlockEntityBuilder$BlockEntityFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityBuilder$BlockEntityFactory$Type<T> = ($BlockEntityBuilder$BlockEntityFactory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityBuilder$BlockEntityFactory_<T> = $BlockEntityBuilder$BlockEntityFactory$Type<(T)>;
}}
declare module "packages/com/tterrag/registrate/util/$CreativeModeTabModifier" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$BooleanSupplier, $BooleanSupplier$Type} from "packages/java/util/function/$BooleanSupplier"
import {$CreativeModeTab$TabVisibility, $CreativeModeTab$TabVisibility$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$TabVisibility"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$FeatureFlagSet, $FeatureFlagSet$Type} from "packages/net/minecraft/world/flag/$FeatureFlagSet"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$CreativeModeTab$Output, $CreativeModeTab$Output$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$Output"

export class $CreativeModeTabModifier implements $CreativeModeTab$Output {

constructor(arg0: $Supplier$Type<($FeatureFlagSet$Type)>, arg1: $BooleanSupplier$Type, arg2: $BiConsumer$Type<($ItemStack$Type), ($CreativeModeTab$TabVisibility$Type)>)

public "accept"(arg0: $Supplier$Type<(any)>, arg1: $CreativeModeTab$TabVisibility$Type): void
public "accept"(arg0: $Supplier$Type<(any)>): void
public "getFlags"(): $FeatureFlagSet
public "hasPermissions"(): boolean
public "accept"(arg0: $ItemStack$Type, arg1: $CreativeModeTab$TabVisibility$Type): void
public "accept"(arg0: $ItemLike$Type, arg1: $CreativeModeTab$TabVisibility$Type): void
public "acceptAll"(arg0: $Collection$Type<($ItemStack$Type)>, arg1: $CreativeModeTab$TabVisibility$Type): void
public "accept"(arg0: $ItemLike$Type): void
public "accept"(arg0: $ItemStack$Type): void
public "acceptAll"(arg0: $Collection$Type<($ItemStack$Type)>): void
get "flags"(): $FeatureFlagSet
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CreativeModeTabModifier$Type = ($CreativeModeTabModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CreativeModeTabModifier_ = $CreativeModeTabModifier$Type;
}}
declare module "packages/com/tterrag/registrate/util/entry/$FluidEntry" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ForgeFlowingFluid, $ForgeFlowingFluid$Type} from "packages/net/minecraftforge/fluids/$ForgeFlowingFluid"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/tterrag/registrate/util/entry/$RegistryEntry"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$FluidType, $FluidType$Type} from "packages/net/minecraftforge/fluids/$FluidType"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $FluidEntry<T extends $ForgeFlowingFluid> extends $RegistryEntry<(T)> {

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: $RegistryObject$Type<(T)>)

public "getType"(): $FluidType
public "is"<R>(arg0: R): boolean
public "getSource"<S extends $ForgeFlowingFluid>(): S
public "getBlock"<B extends $Block>(): $Optional<(B)>
public "getBucket"<I extends $Item>(): $Optional<(I)>
public static "of"<T>(arg0: $Supplier$Type<(T)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<(T)>
public static "of"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
public static "lazy"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
get "type"(): $FluidType
get "source"(): S
get "block"(): $Optional<(B)>
get "bucket"(): $Optional<(I)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidEntry$Type<T> = ($FluidEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidEntry_<T> = $FluidEntry$Type<(T)>;
}}
declare module "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export interface $NonNullSupplier<T> extends $Supplier<(T)> {

 "get"(): T
 "lazy"(): $NonNullSupplier<(T)>

(): T
}

export namespace $NonNullSupplier {
function of<T>(arg0: $Supplier$Type<(T)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<(T)>
function of<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
function lazy<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NonNullSupplier$Type<T> = ($NonNullSupplier<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NonNullSupplier_<T> = $NonNullSupplier$Type<(T)>;
}}
declare module "packages/com/tterrag/registrate/builders/$MenuBuilder" {
import {$AbstractBuilder, $AbstractBuilder$Type} from "packages/com/tterrag/registrate/builders/$AbstractBuilder"
import {$BuilderCallback, $BuilderCallback$Type} from "packages/com/tterrag/registrate/builders/$BuilderCallback"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$MenuBuilder$ScreenFactory, $MenuBuilder$ScreenFactory$Type} from "packages/com/tterrag/registrate/builders/$MenuBuilder$ScreenFactory"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$MenuBuilder$MenuFactory, $MenuBuilder$MenuFactory$Type} from "packages/com/tterrag/registrate/builders/$MenuBuilder$MenuFactory"
import {$MenuBuilder$ForgeMenuFactory, $MenuBuilder$ForgeMenuFactory$Type} from "packages/com/tterrag/registrate/builders/$MenuBuilder$ForgeMenuFactory"
import {$MenuAccess, $MenuAccess$Type} from "packages/net/minecraft/client/gui/screens/inventory/$MenuAccess"

export class $MenuBuilder<T extends $AbstractContainerMenu, S extends ($Screen) & ($MenuAccess<(T)>), P> extends $AbstractBuilder<($MenuType<(any)>), ($MenuType<(T)>), (P), ($MenuBuilder<(T), (S), (P)>)> {

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: P, arg2: string, arg3: $BuilderCallback$Type, arg4: $MenuBuilder$MenuFactory$Type<(T)>, arg5: $NonNullSupplier$Type<($MenuBuilder$ScreenFactory$Type<(T), (S)>)>)
constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: P, arg2: string, arg3: $BuilderCallback$Type, arg4: $MenuBuilder$ForgeMenuFactory$Type<(T)>, arg5: $NonNullSupplier$Type<($MenuBuilder$ScreenFactory$Type<(T), (S)>)>)

public static "of"<T>(arg0: $Supplier$Type<(T)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<(T)>
public static "of"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
public static "lazy"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuBuilder$Type<T, S, P> = ($MenuBuilder<(T), (S), (P)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuBuilder_<T, S, P> = $MenuBuilder$Type<(T), (S), (P)>;
}}
declare module "packages/com/tterrag/registrate/providers/loot/$RegistrateBlockLootTables" {
import {$LootPoolEntryContainer$Builder, $LootPoolEntryContainer$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/entries/$LootPoolEntryContainer$Builder"
import {$RegistrateLootTables, $RegistrateLootTables$Type} from "packages/com/tterrag/registrate/providers/loot/$RegistrateLootTables"
import {$LootTable$Builder, $LootTable$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/$LootTable$Builder"
import {$FunctionUserBuilder, $FunctionUserBuilder$Type} from "packages/net/minecraft/world/level/storage/loot/functions/$FunctionUserBuilder"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$NumberProvider, $NumberProvider$Type} from "packages/net/minecraft/world/level/storage/loot/providers/number/$NumberProvider"
import {$LootItemCondition$Builder, $LootItemCondition$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemCondition$Builder"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$VanillaBlockLoot, $VanillaBlockLoot$Type} from "packages/net/minecraft/data/loot/packs/$VanillaBlockLoot"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ValidationContext, $ValidationContext$Type} from "packages/net/minecraft/world/level/storage/loot/$ValidationContext"
import {$ConditionUserBuilder, $ConditionUserBuilder$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$ConditionUserBuilder"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$LootTable, $LootTable$Type} from "packages/net/minecraft/world/level/storage/loot/$LootTable"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $RegistrateBlockLootTables extends $VanillaBlockLoot implements $RegistrateLootTables {

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: $Consumer$Type<($RegistrateBlockLootTables$Type)>)

public "m_247577_"(arg0: $Block$Type, arg1: $LootTable$Builder$Type): void
public "m_271693_"(arg0: $Block$Type): $LootTable$Builder
public "m_245765_"(arg0: $ItemLike$Type, arg1: $NumberProvider$Type): $LootTable$Builder
public "m_245644_"(arg0: $Block$Type): void
public "m_246535_"(arg0: $Block$Type): void
public "m_247733_"<T extends $ConditionUserBuilder<(T)>>(arg0: $ItemLike$Type, arg1: $ConditionUserBuilder$Type<(T)>): T
public "m_245602_"(arg0: $ItemLike$Type): $LootTable$Builder
public static "createBeeHiveDrop"(arg0: $Block$Type): $LootTable$Builder
public static "createBeeNestDrop"(arg0: $Block$Type): $LootTable$Builder
public "m_245724_"(arg0: $Block$Type): void
public static "createSilkTouchOrShearsDispatchTable"(arg0: $Block$Type, arg1: $LootPoolEntryContainer$Builder$Type<(any)>): $LootTable$Builder
public "m_245142_"(arg0: $Block$Type, arg1: $ItemLike$Type, arg2: $NumberProvider$Type): $LootTable$Builder
public "m_245514_"(arg0: $Block$Type, arg1: $ItemLike$Type): $LootTable$Builder
public "m_245238_"(arg0: $Block$Type, arg1: $Item$Type, arg2: $Item$Type, arg3: $LootItemCondition$Builder$Type): $LootTable$Builder
public "m_246108_"<T extends $FunctionUserBuilder<(T)>>(arg0: $ItemLike$Type, arg1: $FunctionUserBuilder$Type<(T)>): T
public "m_245854_"(arg0: $Block$Type, arg1: $Block$Type): void
public "m_245693_"(arg0: $Block$Type, arg1: $Block$Type): void
public "m_245349_"(arg0: $Block$Type): $LootTable$Builder
public "m_245671_"(arg0: $Block$Type): $LootTable$Builder
public "m_246224_"(arg0: $Block$Type, arg1: $Block$Type): $LootTable$Builder
public "m_245170_"(arg0: $Block$Type): $LootTable$Builder
public "m_246235_"(arg0: $Block$Type, arg1: $LootItemCondition$Builder$Type): $LootTable$Builder
public "m_246218_"(arg0: $Block$Type): $LootTable$Builder
public "m_246109_"(arg0: $Block$Type, arg1: $Item$Type): $LootTable$Builder
public "m_246142_"(arg0: $Block$Type, arg1: $Block$Type, ...arg2: (float)[]): $LootTable$Builder
public "m_246167_"(arg0: $Block$Type): $LootTable$Builder
public "m_245079_"(arg0: $Block$Type, arg1: $ItemLike$Type): $LootTable$Builder
public "m_246047_"(arg0: $Block$Type, arg1: $Block$Type, ...arg2: (float)[]): $LootTable$Builder
public "m_247458_"(arg0: $Block$Type): $LootTable$Builder
public "m_247334_"(arg0: $Block$Type): $LootTable$Builder
public "m_246180_"(arg0: $Block$Type): $LootTable$Builder
public "m_245895_"(arg0: $Block$Type): $LootTable$Builder
public "m_247398_"(arg0: $Block$Type): $LootTable$Builder
public "m_247233_"(arg0: $Block$Type): $LootTable$Builder
public "m_246125_"(arg0: $Block$Type, arg1: $ItemLike$Type): void
public static "createSilkTouchDispatchTable"(arg0: $Block$Type, arg1: $LootPoolEntryContainer$Builder$Type<(any)>): $LootTable$Builder
public static "createSelfDropDispatchTable"(arg0: $Block$Type, arg1: $LootItemCondition$Builder$Type, arg2: $LootPoolEntryContainer$Builder$Type<(any)>): $LootTable$Builder
public static "createCaveVinesDrop"(arg0: $Block$Type): $LootTable$Builder
public static "createShearsOnlyDrop"(arg0: $ItemLike$Type): $LootTable$Builder
public static "createSilkTouchOnlyTable"(arg0: $ItemLike$Type): $LootTable$Builder
public static "createDoublePlantShearsDrop"(arg0: $Block$Type): $LootTable$Builder
public static "createShearsDispatchTable"(arg0: $Block$Type, arg1: $LootPoolEntryContainer$Builder$Type<(any)>): $LootTable$Builder
public static "createCandleCakeDrops"(arg0: $Block$Type): $LootTable$Builder
public "validate"(arg0: $Map$Type<($ResourceLocation$Type), ($LootTable$Type)>, arg1: $ValidationContext$Type): void
public "generate"(arg0: $BiConsumer$Type<($ResourceLocation$Type), ($LootTable$Builder$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrateBlockLootTables$Type = ($RegistrateBlockLootTables);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrateBlockLootTables_ = $RegistrateBlockLootTables$Type;
}}
declare module "packages/com/tterrag/registrate/providers/$RegistrateItemTagsProvider" {
import {$ProviderType, $ProviderType$Type} from "packages/com/tterrag/registrate/providers/$ProviderType"
import {$RegistrateTagsProvider$IntrinsicImpl, $RegistrateTagsProvider$IntrinsicImpl$Type} from "packages/com/tterrag/registrate/providers/$RegistrateTagsProvider$IntrinsicImpl"
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$TagsProvider$TagLookup, $TagsProvider$TagLookup$Type} from "packages/net/minecraft/data/tags/$TagsProvider$TagLookup"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"

export class $RegistrateItemTagsProvider extends $RegistrateTagsProvider$IntrinsicImpl<($Item)> {

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: $ProviderType$Type<($RegistrateItemTagsProvider$Type)>, arg2: string, arg3: $PackOutput$Type, arg4: $CompletableFuture$Type<($HolderLookup$Provider$Type)>, arg5: $CompletableFuture$Type<($TagsProvider$TagLookup$Type<($Block$Type)>)>, arg6: $ExistingFileHelper$Type)

public "copy"(arg0: $TagKey$Type<($Block$Type)>, arg1: $TagKey$Type<($Item$Type)>): void
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrateItemTagsProvider$Type = ($RegistrateItemTagsProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrateItemTagsProvider_ = $RegistrateItemTagsProvider$Type;
}}
declare module "packages/com/tterrag/registrate/providers/$RegistrateTagsProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$RegistrateProvider, $RegistrateProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateProvider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$TagsProvider$TagAppender, $TagsProvider$TagAppender$Type} from "packages/net/minecraft/data/tags/$TagsProvider$TagAppender"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$LogicalSide, $LogicalSide$Type} from "packages/net/minecraftforge/fml/$LogicalSide"

export interface $RegistrateTagsProvider<T> extends $RegistrateProvider {

 "addTag"(arg0: $TagKey$Type<(T)>): $TagsProvider$TagAppender<(T)>
 "getSide"(): $LogicalSide
 "run"(arg0: $CachedOutput$Type): $CompletableFuture<(any)>
 "getName"(): string
}

export namespace $RegistrateTagsProvider {
function saveStable(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrateTagsProvider$Type<T> = ($RegistrateTagsProvider<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrateTagsProvider_<T> = $RegistrateTagsProvider$Type<(T)>;
}}
declare module "packages/com/tterrag/registrate/providers/loot/$RegistrateLootTableProvider$LootType" {
import {$NonNullBiFunction, $NonNullBiFunction$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullBiFunction"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$RegistrateLootTables, $RegistrateLootTables$Type} from "packages/com/tterrag/registrate/providers/loot/$RegistrateLootTables"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$RegistrateBlockLootTables, $RegistrateBlockLootTables$Type} from "packages/com/tterrag/registrate/providers/loot/$RegistrateBlockLootTables"
import {$LootContextParamSet, $LootContextParamSet$Type} from "packages/net/minecraft/world/level/storage/loot/parameters/$LootContextParamSet"
import {$RegistrateEntityLootTables, $RegistrateEntityLootTables$Type} from "packages/com/tterrag/registrate/providers/loot/$RegistrateEntityLootTables"

export interface $RegistrateLootTableProvider$LootType<T extends $RegistrateLootTables> {

 "getLootSet"(): $LootContextParamSet
 "getLootCreator"(arg0: $AbstractRegistrate$Type<(any)>, arg1: $Consumer$Type<(T)>): T
}

export namespace $RegistrateLootTableProvider$LootType {
const BLOCK: $RegistrateLootTableProvider$LootType<($RegistrateBlockLootTables)>
const ENTITY: $RegistrateLootTableProvider$LootType<($RegistrateEntityLootTables)>
function register<T>(arg0: string, arg1: $LootContextParamSet$Type, arg2: $NonNullBiFunction$Type<($AbstractRegistrate$Type), ($Consumer$Type<(T)>), (T)>): $RegistrateLootTableProvider$LootType<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrateLootTableProvider$LootType$Type<T> = ($RegistrateLootTableProvider$LootType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrateLootTableProvider$LootType_<T> = $RegistrateLootTableProvider$LootType$Type<(T)>;
}}
declare module "packages/com/tterrag/registrate/builders/$NoConfigBuilder" {
import {$AbstractBuilder, $AbstractBuilder$Type} from "packages/com/tterrag/registrate/builders/$AbstractBuilder"
import {$BuilderCallback, $BuilderCallback$Type} from "packages/com/tterrag/registrate/builders/$BuilderCallback"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $NoConfigBuilder<R, T extends R, P> extends $AbstractBuilder<(R), (T), (P), ($NoConfigBuilder<(R), (T), (P)>)> {

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: P, arg2: string, arg3: $BuilderCallback$Type, arg4: $ResourceKey$Type<($Registry$Type<(R)>)>, arg5: $NonNullSupplier$Type<(T)>)

public static "of"<T>(arg0: $Supplier$Type<(T)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<(T)>
public static "of"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
public static "lazy"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NoConfigBuilder$Type<R, T, P> = ($NoConfigBuilder<(R), (T), (P)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NoConfigBuilder_<R, T, P> = $NoConfigBuilder$Type<(R), (T), (P)>;
}}
declare module "packages/com/tterrag/registrate/util/entry/$MenuEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$MenuConstructor, $MenuConstructor$Type} from "packages/net/minecraft/world/inventory/$MenuConstructor"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/tterrag/registrate/util/entry/$RegistryEntry"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export class $MenuEntry<T extends $AbstractContainerMenu> extends $RegistryEntry<($MenuType<(T)>)> {

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: $RegistryObject$Type<($MenuType$Type<(T)>)>)

public "open"(arg0: $ServerPlayer$Type, arg1: $Component$Type, arg2: $Consumer$Type<($FriendlyByteBuf$Type)>): void
public "open"(arg0: $ServerPlayer$Type, arg1: $Component$Type, arg2: $MenuConstructor$Type): void
public "open"(arg0: $ServerPlayer$Type, arg1: $Component$Type, arg2: $MenuConstructor$Type, arg3: $Consumer$Type<($FriendlyByteBuf$Type)>): void
public "open"(arg0: $ServerPlayer$Type, arg1: $Component$Type): void
public "create"(arg0: integer, arg1: $Inventory$Type): T
public "asProvider"(): $MenuConstructor
public static "of"<T>(arg0: $Supplier$Type<(T)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<(T)>
public static "of"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
public static "lazy"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuEntry$Type<T> = ($MenuEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuEntry_<T> = $MenuEntry$Type<(T)>;
}}
declare module "packages/com/tterrag/registrate/builders/$MenuBuilder$ForgeMenuFactory" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export interface $MenuBuilder$ForgeMenuFactory<T extends $AbstractContainerMenu> {

 "create"(arg0: $MenuType$Type<(T)>, arg1: integer, arg2: $Inventory$Type, arg3: $FriendlyByteBuf$Type): T

(arg0: $MenuType$Type<(T)>, arg1: integer, arg2: $Inventory$Type, arg3: $FriendlyByteBuf$Type): T
}

export namespace $MenuBuilder$ForgeMenuFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuBuilder$ForgeMenuFactory$Type<T> = ($MenuBuilder$ForgeMenuFactory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuBuilder$ForgeMenuFactory_<T> = $MenuBuilder$ForgeMenuFactory$Type<(T)>;
}}
declare module "packages/com/tterrag/registrate/providers/$RegistrateTagsProvider$IntrinsicImpl" {
import {$ProviderType, $ProviderType$Type} from "packages/com/tterrag/registrate/providers/$ProviderType"
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$IntrinsicHolderTagsProvider, $IntrinsicHolderTagsProvider$Type} from "packages/net/minecraft/data/tags/$IntrinsicHolderTagsProvider"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$HolderLookup$Provider, $HolderLookup$Provider$Type} from "packages/net/minecraft/core/$HolderLookup$Provider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$RegistrateTagsProvider, $RegistrateTagsProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateTagsProvider"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$LogicalSide, $LogicalSide$Type} from "packages/net/minecraftforge/fml/$LogicalSide"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$IntrinsicHolderTagsProvider$IntrinsicTagAppender, $IntrinsicHolderTagsProvider$IntrinsicTagAppender$Type} from "packages/net/minecraft/data/tags/$IntrinsicHolderTagsProvider$IntrinsicTagAppender"

export class $RegistrateTagsProvider$IntrinsicImpl<T> extends $IntrinsicHolderTagsProvider<(T)> implements $RegistrateTagsProvider<(T)> {

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: $ProviderType$Type<(any)>, arg2: string, arg3: $PackOutput$Type, arg4: $ResourceKey$Type<(any)>, arg5: $CompletableFuture$Type<($HolderLookup$Provider$Type)>, arg6: $Function$Type<(T), ($ResourceKey$Type<(T)>)>, arg7: $ExistingFileHelper$Type)

public "getSide"(): $LogicalSide
public "addTag"(arg0: $TagKey$Type<(T)>): $IntrinsicHolderTagsProvider$IntrinsicTagAppender<(T)>
public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "side"(): $LogicalSide
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrateTagsProvider$IntrinsicImpl$Type<T> = ($RegistrateTagsProvider$IntrinsicImpl<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrateTagsProvider$IntrinsicImpl_<T> = $RegistrateTagsProvider$IntrinsicImpl$Type<(T)>;
}}
declare module "packages/com/tterrag/registrate/util/nullness/$NonNullFunction" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"

export interface $NonNullFunction<T, R> extends $Function<(T), (R)> {

 "apply"(arg0: T): R
 "andThen"<V>(arg0: $NonNullFunction$Type<(any), (any)>): $NonNullFunction<(T), (V)>
 "compose"<V>(arg0: $Function$Type<(any), (any)>): $Function<(V), (R)>
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $Function<(T), (V)>

(arg0: T): R
}

export namespace $NonNullFunction {
function identity<T>(): $Function<(T), (T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NonNullFunction$Type<T, R> = ($NonNullFunction<(T), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NonNullFunction_<T, R> = $NonNullFunction$Type<(T), (R)>;
}}
declare module "packages/com/tterrag/registrate/util/entry/$BlockEntry" {
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/tterrag/registrate/util/entry/$RegistryEntry"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemProviderEntry, $ItemProviderEntry$Type} from "packages/com/tterrag/registrate/util/entry/$ItemProviderEntry"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $BlockEntry<T extends $Block> extends $ItemProviderEntry<(T)> {

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: $RegistryObject$Type<(T)>)

public static "cast"<T extends $Block>(arg0: $RegistryEntry$Type<(T)>): $BlockEntry<(T)>
public "has"(arg0: $BlockState$Type): boolean
public "getDefaultState"(): $BlockState
public static "of"<T>(arg0: $Supplier$Type<(T)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<(T)>
public static "of"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
public static "lazy"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
get "defaultState"(): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntry$Type<T> = ($BlockEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntry_<T> = $BlockEntry$Type<(T)>;
}}
declare module "packages/com/tterrag/registrate/builders/$BuilderCallback" {
import {$Builder, $Builder$Type} from "packages/com/tterrag/registrate/builders/$Builder"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/tterrag/registrate/util/entry/$RegistryEntry"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$NonNullFunction, $NonNullFunction$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullFunction"

export interface $BuilderCallback {

 "accept"<R, T extends R>(arg0: string, arg1: $ResourceKey$Type<(any)>, arg2: $Builder$Type<(R), (T), (any), (any)>, arg3: $NonNullSupplier$Type<(any)>, arg4: $NonNullFunction$Type<($RegistryObject$Type<(T)>), (any)>): $RegistryEntry<(T)>
 "accept"<R, T extends R>(arg0: string, arg1: $ResourceKey$Type<(any)>, arg2: $Builder$Type<(R), (T), (any), (any)>, arg3: $NonNullSupplier$Type<(any)>): $RegistryEntry<(T)>

(arg0: string, arg1: $ResourceKey$Type<(any)>, arg2: $Builder$Type<(R), (T), (any), (any)>, arg3: $NonNullSupplier$Type<(any)>, arg4: $NonNullFunction$Type<($RegistryObject$Type<(T)>), (any)>): $RegistryEntry<(T)>
}

export namespace $BuilderCallback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuilderCallback$Type = ($BuilderCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuilderCallback_ = $BuilderCallback$Type;
}}
declare module "packages/com/tterrag/registrate/builders/$EntityBuilder" {
import {$SpawnPlacements$Type, $SpawnPlacements$Type$Type} from "packages/net/minecraft/world/entity/$SpawnPlacements$Type"
import {$AbstractBuilder, $AbstractBuilder$Type} from "packages/com/tterrag/registrate/builders/$AbstractBuilder"
import {$NonNullConsumer, $NonNullConsumer$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullConsumer"
import {$EntityType$EntityFactory, $EntityType$EntityFactory$Type} from "packages/net/minecraft/world/entity/$EntityType$EntityFactory"
import {$BuilderCallback, $BuilderCallback$Type} from "packages/com/tterrag/registrate/builders/$BuilderCallback"
import {$Heightmap$Types, $Heightmap$Types$Type} from "packages/net/minecraft/world/level/levelgen/$Heightmap$Types"
import {$ItemBuilder, $ItemBuilder$Type} from "packages/com/tterrag/registrate/builders/$ItemBuilder"
import {$EntityEntry, $EntityEntry$Type} from "packages/com/tterrag/registrate/util/entry/$EntityEntry"
import {$AttributeSupplier$Builder, $AttributeSupplier$Builder$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeSupplier$Builder"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$EntityRendererProvider$Context, $EntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider$Context"
import {$NonNullBiConsumer, $NonNullBiConsumer$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullBiConsumer"
import {$SpawnPlacements$SpawnPredicate, $SpawnPlacements$SpawnPredicate$Type} from "packages/net/minecraft/world/entity/$SpawnPlacements$SpawnPredicate"
import {$EntityRenderer, $EntityRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRenderer"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$EntityType$Builder, $EntityType$Builder$Type} from "packages/net/minecraft/world/entity/$EntityType$Builder"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$MobCategory, $MobCategory$Type} from "packages/net/minecraft/world/entity/$MobCategory"
import {$RegistrateEntityLootTables, $RegistrateEntityLootTables$Type} from "packages/com/tterrag/registrate/providers/loot/$RegistrateEntityLootTables"
import {$NonNullFunction, $NonNullFunction$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullFunction"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EntityBuilder<T extends $Entity, P> extends $AbstractBuilder<($EntityType<(any)>), ($EntityType<(T)>), (P), ($EntityBuilder<(T), (P)>)> {


public "register"(): $EntityEntry<(T)>
public "properties"(arg0: $NonNullConsumer$Type<($EntityType$Builder$Type<(T)>)>): $EntityBuilder<(T), (P)>
public static "create"<T extends $Entity, P>(arg0: $AbstractRegistrate$Type<(any)>, arg1: P, arg2: string, arg3: $BuilderCallback$Type, arg4: $EntityType$EntityFactory$Type<(T)>, arg5: $MobCategory$Type): $EntityBuilder<(T), (P)>
public "lang"(arg0: string): $EntityBuilder<(T), (P)>
public "attributes"(arg0: $Supplier$Type<($AttributeSupplier$Builder$Type)>): $EntityBuilder<(T), (P)>
public "tag"(...arg0: ($TagKey$Type<($EntityType$Type<(any)>)>)[]): $EntityBuilder<(T), (P)>
public "renderer"(arg0: $NonNullSupplier$Type<($NonNullFunction$Type<($EntityRendererProvider$Context$Type), ($EntityRenderer$Type<(any)>)>)>): $EntityBuilder<(T), (P)>
public "defaultLang"(): $EntityBuilder<(T), (P)>
/**
 * 
 * @deprecated
 */
public "spawnEgg"(arg0: integer, arg1: integer): $ItemBuilder<(any), ($EntityBuilder<(T), (P)>)>
/**
 * 
 * @deprecated
 */
public "defaultSpawnEgg"(arg0: integer, arg1: integer): $EntityBuilder<(T), (P)>
public "loot"(arg0: $NonNullBiConsumer$Type<($RegistrateEntityLootTables$Type), ($EntityType$Type<(T)>)>): $EntityBuilder<(T), (P)>
public "spawnPlacement"(arg0: $SpawnPlacements$Type$Type, arg1: $Heightmap$Types$Type, arg2: $SpawnPlacements$SpawnPredicate$Type<(T)>): $EntityBuilder<(T), (P)>
public static "of"<T>(arg0: $Supplier$Type<(T)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<(T)>
public static "of"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
public static "lazy"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityBuilder$Type<T, P> = ($EntityBuilder<(T), (P)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityBuilder_<T, P> = $EntityBuilder$Type<(T), (P)>;
}}
declare module "packages/com/tterrag/registrate/builders/$EnchantmentBuilder$EnchantmentFactory" {
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$Enchantment$Rarity, $Enchantment$Rarity$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment$Rarity"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$EnchantmentCategory, $EnchantmentCategory$Type} from "packages/net/minecraft/world/item/enchantment/$EnchantmentCategory"

export interface $EnchantmentBuilder$EnchantmentFactory<T extends $Enchantment> {

 "create"(arg0: $Enchantment$Rarity$Type, arg1: $EnchantmentCategory$Type, ...arg2: ($EquipmentSlot$Type)[]): T

(arg0: $Enchantment$Rarity$Type, arg1: $EnchantmentCategory$Type, ...arg2: ($EquipmentSlot$Type)[]): T
}

export namespace $EnchantmentBuilder$EnchantmentFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnchantmentBuilder$EnchantmentFactory$Type<T> = ($EnchantmentBuilder$EnchantmentFactory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnchantmentBuilder$EnchantmentFactory_<T> = $EnchantmentBuilder$EnchantmentFactory$Type<(T)>;
}}
declare module "packages/com/tterrag/registrate/builders/$ItemBuilder" {
import {$CreativeModeTabModifier, $CreativeModeTabModifier$Type} from "packages/com/tterrag/registrate/util/$CreativeModeTabModifier"
import {$RegistrateRecipeProvider, $RegistrateRecipeProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateRecipeProvider"
import {$AbstractBuilder, $AbstractBuilder$Type} from "packages/com/tterrag/registrate/builders/$AbstractBuilder"
import {$NonNullUnaryOperator, $NonNullUnaryOperator$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullUnaryOperator"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$BuilderCallback, $BuilderCallback$Type} from "packages/com/tterrag/registrate/builders/$BuilderCallback"
import {$RegistrateItemModelProvider, $RegistrateItemModelProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateItemModelProvider"
import {$DataGenContext, $DataGenContext$Type} from "packages/com/tterrag/registrate/providers/$DataGenContext"
import {$ItemColor, $ItemColor$Type} from "packages/net/minecraft/client/color/item/$ItemColor"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$NonNullBiConsumer, $NonNullBiConsumer$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullBiConsumer"
import {$ItemEntry, $ItemEntry$Type} from "packages/com/tterrag/registrate/util/entry/$ItemEntry"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$NonNullFunction, $NonNullFunction$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullFunction"

export class $ItemBuilder<T extends $Item, P> extends $AbstractBuilder<($Item), (T), (P), ($ItemBuilder<(T), (P)>)> {


public "register"(): $ItemEntry<(T)>
public "properties"(arg0: $NonNullUnaryOperator$Type<($Item$Properties$Type)>): $ItemBuilder<(T), (P)>
public "tab"(arg0: $ResourceKey$Type<($CreativeModeTab$Type)>): $ItemBuilder<(T), (P)>
/**
 * 
 * @deprecated
 */
public "tab"(arg0: $ResourceKey$Type<($CreativeModeTab$Type)>, arg1: $Consumer$Type<($CreativeModeTabModifier$Type)>): $ItemBuilder<(T), (P)>
public "tab"(arg0: $ResourceKey$Type<($CreativeModeTab$Type)>, arg1: $NonNullBiConsumer$Type<($DataGenContext$Type<($Item$Type), (T)>), ($CreativeModeTabModifier$Type)>): $ItemBuilder<(T), (P)>
public static "create"<T extends $Item, P>(arg0: $AbstractRegistrate$Type<(any)>, arg1: P, arg2: string, arg3: $BuilderCallback$Type, arg4: $NonNullFunction$Type<($Item$Properties$Type), (T)>): $ItemBuilder<(T), (P)>
public "color"(arg0: $NonNullSupplier$Type<($Supplier$Type<($ItemColor$Type)>)>): $ItemBuilder<(T), (P)>
public "lang"(arg0: string): $ItemBuilder<(T), (P)>
public "recipe"(arg0: $NonNullBiConsumer$Type<($DataGenContext$Type<($Item$Type), (T)>), ($RegistrateRecipeProvider$Type)>): $ItemBuilder<(T), (P)>
public "tag"(...arg0: ($TagKey$Type<($Item$Type)>)[]): $ItemBuilder<(T), (P)>
public "initialProperties"(arg0: $NonNullSupplier$Type<($Item$Properties$Type)>): $ItemBuilder<(T), (P)>
public "defaultLang"(): $ItemBuilder<(T), (P)>
public "defaultModel"(): $ItemBuilder<(T), (P)>
public "removeTab"(arg0: $ResourceKey$Type<($CreativeModeTab$Type)>): $ItemBuilder<(T), (P)>
public "model"(arg0: $NonNullBiConsumer$Type<($DataGenContext$Type<($Item$Type), (T)>), ($RegistrateItemModelProvider$Type)>): $ItemBuilder<(T), (P)>
public static "of"<T>(arg0: $Supplier$Type<(T)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<(T)>
public static "of"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
public static "lazy"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemBuilder$Type<T, P> = ($ItemBuilder<(T), (P)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemBuilder_<T, P> = $ItemBuilder$Type<(T), (P)>;
}}
declare module "packages/com/tterrag/registrate/builders/$EnchantmentBuilder" {
import {$AbstractBuilder, $AbstractBuilder$Type} from "packages/com/tterrag/registrate/builders/$AbstractBuilder"
import {$EnchantmentBuilder$EnchantmentFactory, $EnchantmentBuilder$EnchantmentFactory$Type} from "packages/com/tterrag/registrate/builders/$EnchantmentBuilder$EnchantmentFactory"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$BuilderCallback, $BuilderCallback$Type} from "packages/com/tterrag/registrate/builders/$BuilderCallback"
import {$Enchantment$Rarity, $Enchantment$Rarity$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment$Rarity"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$EnchantmentCategory, $EnchantmentCategory$Type} from "packages/net/minecraft/world/item/enchantment/$EnchantmentCategory"

export class $EnchantmentBuilder<T extends $Enchantment, P> extends $AbstractBuilder<($Enchantment), (T), (P), ($EnchantmentBuilder<(T), (P)>)> {


public static "create"<T extends $Enchantment, P>(arg0: $AbstractRegistrate$Type<(any)>, arg1: P, arg2: string, arg3: $BuilderCallback$Type, arg4: $EnchantmentCategory$Type, arg5: $EnchantmentBuilder$EnchantmentFactory$Type<(T)>): $EnchantmentBuilder<(T), (P)>
public "lang"(arg0: string): $EnchantmentBuilder<(T), (P)>
public "addArmorSlots"(): $EnchantmentBuilder<(T), (P)>
public "defaultLang"(): $EnchantmentBuilder<(T), (P)>
public "addSlots"(...arg0: ($EquipmentSlot$Type)[]): $EnchantmentBuilder<(T), (P)>
public "rarity"(arg0: $Enchantment$Rarity$Type): $EnchantmentBuilder<(T), (P)>
public static "of"<T>(arg0: $Supplier$Type<(T)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<(T)>
public static "of"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
public static "lazy"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnchantmentBuilder$Type<T, P> = ($EnchantmentBuilder<(T), (P)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnchantmentBuilder_<T, P> = $EnchantmentBuilder$Type<(T), (P)>;
}}
declare module "packages/com/tterrag/registrate/providers/$RegistrateLangProvider" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$LanguageProvider, $LanguageProvider$Type} from "packages/net/minecraftforge/common/data/$LanguageProvider"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$LogicalSide, $LogicalSide$Type} from "packages/net/minecraftforge/fml/$LogicalSide"
import {$RegistrateProvider, $RegistrateProvider$Type} from "packages/com/tterrag/registrate/providers/$RegistrateProvider"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $RegistrateLangProvider extends $LanguageProvider implements $RegistrateProvider {

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: $PackOutput$Type)

public "add"(arg0: $CreativeModeTab$Type, arg1: string): void
public "add"(arg0: string, arg1: string): void
public "getSide"(): $LogicalSide
public "addBlockWithTooltip"(arg0: $NonNullSupplier$Type<(any)>, arg1: string, arg2: string): void
public "addBlockWithTooltip"(arg0: $NonNullSupplier$Type<(any)>, arg1: string): void
public "addItem"(arg0: $NonNullSupplier$Type<(any)>): void
public "addEntityType"(arg0: $NonNullSupplier$Type<(any)>): void
public "addBlock"(arg0: $NonNullSupplier$Type<(any)>): void
public static "toEnglishName"(arg0: string): string
public "getAutomaticName"<T>(arg0: $NonNullSupplier$Type<(any)>, arg1: $ResourceKey$Type<($Registry$Type<(T)>)>): string
public "run"(arg0: $CachedOutput$Type): $CompletableFuture<(any)>
public "getName"(): string
public "addTooltip"(arg0: $NonNullSupplier$Type<(any)>, arg1: string): void
public "addTooltip"(arg0: $NonNullSupplier$Type<(any)>, arg1: $List$Type<(string)>): void
public "addItemWithTooltip"(arg0: $NonNullSupplier$Type<(any)>, arg1: string, arg2: $List$Type<(string)>): void
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "side"(): $LogicalSide
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistrateLangProvider$Type = ($RegistrateLangProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistrateLangProvider_ = $RegistrateLangProvider$Type;
}}
declare module "packages/com/tterrag/registrate/util/nullness/$NonNullBiFunction" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"

export interface $NonNullBiFunction<T, U, R> extends $BiFunction<(T), (U), (R)> {

 "apply"(arg0: T, arg1: U): R
 "andThen"<V>(arg0: $Function$Type<(any), (any)>): $BiFunction<(T), (U), (V)>

(arg0: T, arg1: U): R
}

export namespace $NonNullBiFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NonNullBiFunction$Type<T, U, R> = ($NonNullBiFunction<(T), (U), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NonNullBiFunction_<T, U, R> = $NonNullBiFunction$Type<(T), (U), (R)>;
}}
declare module "packages/com/tterrag/registrate/util/entry/$ItemEntry" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$NonNullSupplier, $NonNullSupplier$Type} from "packages/com/tterrag/registrate/util/nullness/$NonNullSupplier"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/tterrag/registrate/util/entry/$RegistryEntry"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemProviderEntry, $ItemProviderEntry$Type} from "packages/com/tterrag/registrate/util/entry/$ItemProviderEntry"
import {$AbstractRegistrate, $AbstractRegistrate$Type} from "packages/com/tterrag/registrate/$AbstractRegistrate"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $ItemEntry<T extends $Item> extends $ItemProviderEntry<(T)> {

constructor(arg0: $AbstractRegistrate$Type<(any)>, arg1: $RegistryObject$Type<(T)>)

public static "cast"<T extends $Item>(arg0: $RegistryEntry$Type<(T)>): $ItemEntry<(T)>
public static "of"<T>(arg0: $Supplier$Type<(T)>, arg1: $NonNullSupplier$Type<(string)>): $NonNullSupplier<(T)>
public static "of"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
public static "lazy"<T>(arg0: $Supplier$Type<(T)>): $NonNullSupplier<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemEntry$Type<T> = ($ItemEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemEntry_<T> = $ItemEntry$Type<(T)>;
}}
