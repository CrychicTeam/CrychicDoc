declare module "packages/dev/shadowsoffire/placebo/registry/$DeferredHelper" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$PaintingVariant, $PaintingVariant$Type} from "packages/net/minecraft/world/entity/decoration/$PaintingVariant"
import {$StatType, $StatType$Type} from "packages/net/minecraft/stats/$StatType"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$ParticleOptions, $ParticleOptions$Type} from "packages/net/minecraft/core/particles/$ParticleOptions"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$ParticleType, $ParticleType$Type} from "packages/net/minecraft/core/particles/$ParticleType"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$RegisterEvent, $RegisterEvent$Type} from "packages/net/minecraftforge/registries/$RegisterEvent"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$FeatureConfiguration, $FeatureConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$FeatureConfiguration"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$StatFormatter, $StatFormatter$Type} from "packages/net/minecraft/stats/$StatFormatter"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$Feature, $Feature$Type} from "packages/net/minecraft/world/level/levelgen/feature/$Feature"

export class $DeferredHelper {


public "register"(arg0: $RegisterEvent$Type): void
public "tab"<T extends $CreativeModeTab>(arg0: string, arg1: $Supplier$Type<(T)>): $RegistryObject<(T)>
public static "create"(arg0: string): $DeferredHelper
public "block"<T extends $Block>(arg0: string, arg1: $Supplier$Type<(T)>): $RegistryObject<(T)>
public "recipe"<C extends $Container, U extends $Recipe<(C)>, T extends $RecipeType<(U)>>(arg0: string, arg1: $Supplier$Type<(T)>): $RegistryObject<(T)>
public "feature"<U extends $FeatureConfiguration, T extends $Feature<(U)>>(arg0: string, arg1: $Supplier$Type<(T)>): $RegistryObject<(T)>
public "attribute"<T extends $Attribute>(arg0: string, arg1: $Supplier$Type<(T)>): $RegistryObject<(T)>
public "stat"<S, U extends $StatType<(S)>, T extends $StatType<(U)>>(arg0: string, arg1: $Supplier$Type<(T)>): $RegistryObject<(T)>
public "item"<T extends $Item>(arg0: string, arg1: $Supplier$Type<(T)>): $RegistryObject<(T)>
public "potion"<T extends $Potion>(arg0: string, arg1: $Supplier$Type<(T)>): $RegistryObject<(T)>
public "menu"<U extends $AbstractContainerMenu, T extends $MenuType<(U)>>(arg0: string, arg1: $Supplier$Type<(T)>): $RegistryObject<(T)>
public "sound"(arg0: string): $RegistryObject<($SoundEvent)>
public "sound"<T extends $SoundEvent>(arg0: string, arg1: $Supplier$Type<(T)>): $RegistryObject<(T)>
public "custom"<P, T extends P>(arg0: string, arg1: $ResourceKey$Type<($Registry$Type<(P)>)>, arg2: $Supplier$Type<(T)>): $RegistryObject<(T)>
public "effect"<T extends $MobEffect>(arg0: string, arg1: $Supplier$Type<(T)>): $RegistryObject<(T)>
public "entity"<U extends $Entity, T extends $EntityType<(U)>>(arg0: string, arg1: $Supplier$Type<(T)>): $RegistryObject<(T)>
public "enchant"<T extends $Enchantment>(arg0: string, arg1: $Supplier$Type<(T)>): $RegistryObject<(T)>
public "recipeSerializer"<C extends $Container, U extends $Recipe<(C)>, T extends $RecipeSerializer<(U)>>(arg0: string, arg1: $Supplier$Type<(T)>): $RegistryObject<(T)>
public "customStat"(arg0: string, arg1: $StatFormatter$Type): $RegistryObject<($ResourceLocation)>
public "blockEntity"<U extends $BlockEntity, T extends $BlockEntityType<(U)>>(arg0: string, arg1: $Supplier$Type<(T)>): $RegistryObject<(T)>
public "particle"<U extends $ParticleOptions, T extends $ParticleType<(U)>>(arg0: string, arg1: $Supplier$Type<(T)>): $RegistryObject<(T)>
public "fluid"<T extends $Fluid>(arg0: string, arg1: $Supplier$Type<(T)>): $RegistryObject<(T)>
public "painting"<T extends $PaintingVariant>(arg0: string, arg1: $Supplier$Type<(T)>): $RegistryObject<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeferredHelper$Type = ($DeferredHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeferredHelper_ = $DeferredHelper$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/compat/$CuriosCompat" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CuriosCompat {

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosCompat$Type = ($CuriosCompat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosCompat_ = $CuriosCompat$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/menu/$SlotUpdateListener" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $SlotUpdateListener {

 "slotUpdated"(arg0: integer, arg1: $ItemStack$Type): void

(arg0: integer, arg1: $ItemStack$Type): void
}

export namespace $SlotUpdateListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotUpdateListener$Type = ($SlotUpdateListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotUpdateListener_ = $SlotUpdateListener$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/packets/$PatreonDisableMessage" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"

export class $PatreonDisableMessage {

constructor(arg0: integer)
constructor(arg0: integer, arg1: $UUID$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PatreonDisableMessage$Type = ($PatreonDisableMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PatreonDisableMessage_ = $PatreonDisableMessage$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/client/$AttributesLibClient" {
import {$ClientPlayerNetworkEvent$Clone, $ClientPlayerNetworkEvent$Clone$Type} from "packages/net/minecraftforge/client/event/$ClientPlayerNetworkEvent$Clone"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$RegisterParticleProvidersEvent, $RegisterParticleProvidersEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterParticleProvidersEvent"
import {$ItemTooltipEvent, $ItemTooltipEvent$Type} from "packages/net/minecraftforge/event/entity/player/$ItemTooltipEvent"
import {$RegisterClientReloadListenersEvent, $RegisterClientReloadListenersEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterClientReloadListenersEvent"
import {$ScreenEvent$Init$Post, $ScreenEvent$Init$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$Init$Post"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$GatherEffectScreenTooltipsEvent, $GatherEffectScreenTooltipsEvent$Type} from "packages/dev/shadowsoffire/attributeslib/api/client/$GatherEffectScreenTooltipsEvent"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"

export class $AttributesLibClient {

constructor()

public static "clientSetup"(arg0: $FMLClientSetupEvent$Type): void
public "tooltips"(arg0: $ItemTooltipEvent$Type): void
public "updateClientFlyStateOnRespawn"(arg0: $ClientPlayerNetworkEvent$Clone$Type): void
public static "clientReload"(arg0: $RegisterClientReloadListenersEvent$Type): void
public static "particleFactories"(arg0: $RegisterParticleProvidersEvent$Type): void
public "effectGuiTooltips"(arg0: $GatherEffectScreenTooltipsEvent$Type): void
public "addAttribComponent"(arg0: $ScreenEvent$Init$Post$Type): void
public static "getSortedModifiers"(arg0: $ItemStack$Type, arg1: $EquipmentSlot$Type): $Multimap<($Attribute), ($AttributeModifier)>
public static "apothCrit"(arg0: integer): void
public "potionTooltips"(arg0: $ItemTooltipEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributesLibClient$Type = ($AttributesLibClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributesLibClient_ = $AttributesLibClient$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/block_entity/$TickingBlockEntity" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $TickingBlockEntity {

 "clientTick"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): void
 "serverTick"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): void
}

export namespace $TickingBlockEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickingBlockEntity$Type = ($TickingBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickingBlockEntity_ = $TickingBlockEntity$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/mobfx/$KnowledgeEffect" {
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"

export class $KnowledgeEffect extends $MobEffect {

constructor()

public "getAttributeModifierValue"(arg0: integer, arg1: $AttributeModifier$Type): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KnowledgeEffect$Type = ($KnowledgeEffect);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KnowledgeEffect_ = $KnowledgeEffect$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/menu/$MenuUtil" {
import {$MenuType$MenuSupplier, $MenuType$MenuSupplier$Type} from "packages/net/minecraft/world/inventory/$MenuType$MenuSupplier"
import {$MenuUtil$PosFactory, $MenuUtil$PosFactory$Type} from "packages/dev/shadowsoffire/placebo/menu/$MenuUtil$PosFactory"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$IContainerFactory, $IContainerFactory$Type} from "packages/net/minecraftforge/network/$IContainerFactory"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $MenuUtil {

constructor()

public static "type"<T extends $AbstractContainerMenu>(arg0: $MenuType$MenuSupplier$Type<(T)>): $MenuType<(T)>
public static "split"(arg0: integer, arg1: boolean): integer
public static "merge"(arg0: integer, arg1: integer, arg2: boolean): integer
public static "factory"<T extends $AbstractContainerMenu>(arg0: $MenuUtil$PosFactory$Type<(T)>): $IContainerFactory<(T)>
public static "openGui"<M extends $AbstractContainerMenu>(arg0: $Player$Type, arg1: $BlockPos$Type, arg2: $MenuUtil$PosFactory$Type<(M)>): $InteractionResult
public static "posType"<T extends $AbstractContainerMenu>(arg0: $MenuUtil$PosFactory$Type<(T)>): $MenuType<(T)>
public static "bufType"<T extends $AbstractContainerMenu>(arg0: $IContainerFactory$Type<(T)>): $MenuType<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuUtil$Type = ($MenuUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuUtil_ = $MenuUtil$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/collections/$BlockedMap" {
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

/**
 * 
 * @deprecated
 */
export class $BlockedMap<K, V> extends $HashMap<(K), (V)> {

constructor()

public "put"(arg0: K, arg1: V): V
public "putAll"(arg0: $Map$Type<(any), (any)>): void
public "putIfAbsent"(arg0: K, arg1: V): V
public "isBlocked"(arg0: K, arg1: V): boolean
public "isBlocked"(arg0: $Map$Type<(any), (any)>): boolean
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V): $Map<(K), (V)>
public static "of"<K, V>(): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V, arg18: K, arg19: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V, arg16: K, arg17: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V, arg14: K, arg15: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V, arg12: K, arg13: V): $Map<(K), (V)>
public static "of"<K, V>(arg0: K, arg1: V, arg2: K, arg3: V, arg4: K, arg5: V, arg6: K, arg7: V, arg8: K, arg9: V, arg10: K, arg11: V): $Map<(K), (V)>
public static "entry"<K, V>(arg0: K, arg1: V): $Map$Entry<(K), (V)>
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(K), (V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockedMap$Type<K, V> = ($BlockedMap<(K), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockedMap_<K, V> = $BlockedMap$Type<(K), (V)>;
}}
declare module "packages/dev/shadowsoffire/placebo/util/$EnchantmentUtils" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export class $EnchantmentUtils {

constructor()

public static "getLevelForExperience"(arg0: integer): integer
public static "getExperienceForLevel"(arg0: integer): integer
public static "chargeExperience"(arg0: $Player$Type, arg1: integer): boolean
public static "getExperience"(arg0: $Player$Type): integer
public static "getTotalExperienceForLevel"(arg0: integer): integer
public static "getExperienceDifference"(arg0: integer, arg1: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnchantmentUtils$Type = ($EnchantmentUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnchantmentUtils_ = $EnchantmentUtils$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/patreon/$PatreonPreview" {
import {$TickEvent$PlayerTickEvent, $TickEvent$PlayerTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$PlayerTickEvent"

export class $PatreonPreview {
static readonly "PARTICLES": boolean
static readonly "WINGS": boolean

constructor()

public static "tick"(arg0: $TickEvent$PlayerTickEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PatreonPreview$Type = ($PatreonPreview);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PatreonPreview_ = $PatreonPreview$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/collections/$BlockedList" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"

/**
 * 
 * @deprecated
 */
export class $BlockedList<T> extends $ArrayList<(T)> {

constructor()

public "add"(arg0: T): boolean
public "add"(arg0: integer, arg1: T): void
public "addAll"(arg0: integer, arg1: $Collection$Type<(any)>): boolean
public "addAll"(arg0: $Collection$Type<(any)>): boolean
public "isBlocked"(arg0: T): boolean
public "isBlocked"(arg0: $Collection$Type<(any)>): boolean
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E): $List<(E)>
public static "of"<E>(arg0: E): $List<(E)>
public static "of"<E>(): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $List<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $List<(E)>
public static "of"<E>(...arg0: (E)[]): $List<(E)>
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockedList$Type<T> = ($BlockedList<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockedList_<T> = $BlockedList$Type<(T)>;
}}
declare module "packages/dev/shadowsoffire/placebo/network/$MessageProvider" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$NetworkDirection, $NetworkDirection$Type} from "packages/net/minecraftforge/network/$NetworkDirection"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export interface $MessageProvider<T> {

 "write"(arg0: T, arg1: $FriendlyByteBuf$Type): void
 "read"(arg0: $FriendlyByteBuf$Type): T
 "handle"(arg0: T, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
 "getMsgClass"(): $Class<(any)>
 "getNetworkDirection"(): $Optional<($NetworkDirection)>
}

export namespace $MessageProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageProvider$Type<T> = ($MessageProvider<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageProvider_<T> = $MessageProvider$Type<(T)>;
}}
declare module "packages/dev/shadowsoffire/attributeslib/client/$AttributeModifierComponent" {
import {$TooltipComponent, $TooltipComponent$Type} from "packages/net/minecraft/world/inventory/tooltip/$TooltipComponent"
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$MultiBufferSource$BufferSource, $MultiBufferSource$BufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource$BufferSource"
import {$ModifierSource, $ModifierSource$Type} from "packages/dev/shadowsoffire/attributeslib/client/$ModifierSource"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ClientTooltipComponent, $ClientTooltipComponent$Type} from "packages/net/minecraft/client/gui/screens/inventory/tooltip/$ClientTooltipComponent"

export class $AttributeModifierComponent implements $ClientTooltipComponent {
static readonly "TEXTURE": $ResourceLocation

constructor(arg0: $ModifierSource$Type<(any)>, arg1: $FormattedText$Type, arg2: $Font$Type, arg3: integer)

public "getWidth"(arg0: $Font$Type): integer
public "getHeight"(): integer
public "renderText"(arg0: $Font$Type, arg1: integer, arg2: integer, arg3: $Matrix4f$Type, arg4: $MultiBufferSource$BufferSource$Type): void
public "renderImage"(arg0: $Font$Type, arg1: integer, arg2: integer, arg3: $GuiGraphics$Type): void
public static "create"(arg0: $TooltipComponent$Type): $ClientTooltipComponent
public static "create"(arg0: $FormattedCharSequence$Type): $ClientTooltipComponent
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributeModifierComponent$Type = ($AttributeModifierComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributeModifierComponent_ = $AttributeModifierComponent$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/client/$AttributesGui" {
import {$InventoryScreen, $InventoryScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$InventoryScreen"
import {$AttributeInstance, $AttributeInstance$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeInstance"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$ComponentPath, $ComponentPath$Type} from "packages/net/minecraft/client/gui/$ComponentPath"
import {$FocusNavigationEvent, $FocusNavigationEvent$Type} from "packages/net/minecraft/client/gui/navigation/$FocusNavigationEvent"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$ScreenRectangle, $ScreenRectangle$Type} from "packages/net/minecraft/client/gui/navigation/$ScreenRectangle"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $AttributesGui implements $Renderable, $GuiEventListener {
static readonly "TEXTURES": $ResourceLocation
static readonly "ENTRY_HEIGHT": integer
static readonly "MAX_ENTRIES": integer
static readonly "WIDTH": integer
static "wasOpen": boolean

constructor(arg0: $InventoryScreen$Type)

public static "format"(arg0: integer): string
public "isMouseOver"(arg0: double, arg1: double): boolean
public "setFocused"(arg0: boolean): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "isFocused"(): boolean
public "refreshData"(): void
public "getHoveredSlot"(arg0: integer, arg1: integer): $AttributeInstance
public "toggleVisibility"(): void
public "getCurrentFocusPath"(): $ComponentPath
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "nextFocusPath"(arg0: $FocusNavigationEvent$Type): $ComponentPath
public "getRectangle"(): $ScreenRectangle
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "keyReleased"(arg0: integer, arg1: integer, arg2: integer): boolean
public "mouseMoved"(arg0: double, arg1: double): void
public "getTabOrderGroup"(): integer
set "focused"(value: boolean)
get "focused"(): boolean
get "currentFocusPath"(): $ComponentPath
get "rectangle"(): $ScreenRectangle
get "tabOrderGroup"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributesGui$Type = ($AttributesGui);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributesGui_ = $AttributesGui$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/events/$ItemUseEvent" {
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$PlayerEvent, $PlayerEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $ItemUseEvent extends $PlayerEvent {

constructor(arg0: $UseOnContext$Type)
constructor()

public "getContext"(): $UseOnContext
public "getLevel"(): $Level
public "isCancelable"(): boolean
public "getFace"(): $Direction
public "getHand"(): $InteractionHand
public "getItemStack"(): $ItemStack
public "getPos"(): $BlockPos
public "getCancellationResult"(): $InteractionResult
public "setCancellationResult"(arg0: $InteractionResult$Type): void
public "getListenerList"(): $ListenerList
get "context"(): $UseOnContext
get "level"(): $Level
get "cancelable"(): boolean
get "face"(): $Direction
get "hand"(): $InteractionHand
get "itemStack"(): $ItemStack
get "pos"(): $BlockPos
get "cancellationResult"(): $InteractionResult
set "cancellationResult"(value: $InteractionResult$Type)
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemUseEvent$Type = ($ItemUseEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemUseEvent_ = $ItemUseEvent$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/menu/$BlockEntityMenu" {
import {$PlaceboContainerMenu, $PlaceboContainerMenu$Type} from "packages/dev/shadowsoffire/placebo/menu/$PlaceboContainerMenu"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $BlockEntityMenu<T extends $BlockEntity> extends $PlaceboContainerMenu {
static readonly "SLOT_CLICKED_OUTSIDE": integer
static readonly "QUICKCRAFT_TYPE_CHARITABLE": integer
static readonly "QUICKCRAFT_TYPE_GREEDY": integer
static readonly "QUICKCRAFT_TYPE_CLONE": integer
static readonly "QUICKCRAFT_HEADER_START": integer
static readonly "QUICKCRAFT_HEADER_CONTINUE": integer
static readonly "QUICKCRAFT_HEADER_END": integer
static readonly "CARRIED_SLOT_SIZE": integer
readonly "lastSlots": $NonNullList<($ItemStack)>
readonly "slots": $NonNullList<($Slot)>
 "remoteSlots": $NonNullList<($ItemStack)>
 "containerId": integer


public "stillValid"(arg0: $Player$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityMenu$Type<T> = ($BlockEntityMenu<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityMenu_<T> = $BlockEntityMenu$Type<(T)>;
}}
declare module "packages/dev/shadowsoffire/placebo/patreon/$PatreonUtils$WingType" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IWingModel, $IWingModel$Type} from "packages/dev/shadowsoffire/placebo/patreon/wings/$IWingModel"

export class $PatreonUtils$WingType extends $Enum<($PatreonUtils$WingType)> {
static readonly "ANGEL": $PatreonUtils$WingType
static readonly "ARMORED": $PatreonUtils$WingType
static readonly "BAT": $PatreonUtils$WingType
static readonly "BLAZE": $PatreonUtils$WingType
static readonly "BONE": $PatreonUtils$WingType
static readonly "CLOUD": $PatreonUtils$WingType
static readonly "DEMON": $PatreonUtils$WingType
static readonly "FAIRY": $PatreonUtils$WingType
static readonly "FLY": $PatreonUtils$WingType
static readonly "MECHANICAL": $PatreonUtils$WingType
static readonly "MONARCH": $PatreonUtils$WingType
static readonly "PIXIE": $PatreonUtils$WingType
static readonly "SPOOKY": $PatreonUtils$WingType
readonly "model": $Supplier<($IWingModel)>
readonly "textureGetter": $Function<($Player), ($ResourceLocation)>
readonly "yOffset": double
readonly "flapSpeed": double


public static "values"(): ($PatreonUtils$WingType)[]
public static "valueOf"(arg0: string): $PatreonUtils$WingType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PatreonUtils$WingType$Type = (("pixie") | ("armored") | ("mechanical") | ("bone") | ("angel") | ("monarch") | ("cloud") | ("spooky") | ("fly") | ("bat") | ("blaze") | ("demon") | ("fairy")) | ($PatreonUtils$WingType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PatreonUtils$WingType_ = $PatreonUtils$WingType$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/registry/$RegistryEvent$Register" {
import {$IForgeRegistry, $IForgeRegistry$Type} from "packages/net/minecraftforge/registries/$IForgeRegistry"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$RegistryEvent, $RegistryEvent$Type} from "packages/dev/shadowsoffire/placebo/registry/$RegistryEvent"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$RegistryEvent$RegistryWrapper, $RegistryEvent$RegistryWrapper$Type} from "packages/dev/shadowsoffire/placebo/registry/$RegistryEvent$RegistryWrapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $RegistryEvent$Register<T> extends $RegistryEvent<(T)> {

constructor()
constructor(arg0: $Class$Type<(T)>, arg1: $IForgeRegistry$Type<(T)>)

public "getName"(): $ResourceLocation
public "toString"(): string
public "getListenerList"(): $ListenerList
public "getRegistry"(): $RegistryEvent$RegistryWrapper<(T)>
public "getForgeRegistry"(): $IForgeRegistry<(T)>
get "name"(): $ResourceLocation
get "listenerList"(): $ListenerList
get "registry"(): $RegistryEvent$RegistryWrapper<(T)>
get "forgeRegistry"(): $IForgeRegistry<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryEvent$Register$Type<T> = ($RegistryEvent$Register<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryEvent$Register_<T> = $RegistryEvent$Register$Type<(T)>;
}}
declare module "packages/dev/shadowsoffire/placebo/codec/$PlaceboCodecs" {
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$BiMap, $BiMap$Type} from "packages/com/google/common/collect/$BiMap"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$CodecProvider, $CodecProvider$Type} from "packages/dev/shadowsoffire/placebo/codec/$CodecProvider"

export class $PlaceboCodecs {

constructor()

public static "nullableField"<A>(arg0: $Codec$Type<(A)>, arg1: string): $MapCodec<($Optional<(A)>)>
public static "nullableField"<A>(arg0: $Codec$Type<(A)>, arg1: string, arg2: A): $MapCodec<(A)>
public static "enumCodec"<E extends $Enum<(E)>>(arg0: $Class$Type<(E)>): $Codec<(E)>
public static "mapBacked"<T extends $CodecProvider<(any)>>(arg0: string, arg1: $BiMap$Type<($ResourceLocation$Type), ($Codec$Type<(any)>)>): $Codec<(T)>
public static "mapBackedDefaulted"<T extends $CodecProvider<(T)>>(arg0: string, arg1: $BiMap$Type<($ResourceLocation$Type), ($Codec$Type<(any)>)>, arg2: $Codec$Type<(any)>): $Codec<(T)>
public static "setOf"<T>(arg0: $Codec$Type<(T)>): $Codec<($Set<(T)>)>
public static "setFromList"<T>(arg0: $Codec$Type<($List$Type<(T)>)>): $Codec<($Set<(T)>)>
public static "stringResolver"<T extends $StringRepresentable>(arg0: $Function$Type<(string), (T)>): $Codec<(T)>
set "of"(value: $Codec$Type<(T)>)
set "fromList"(value: $Codec$Type<($List$Type<(T)>)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceboCodecs$Type = ($PlaceboCodecs);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceboCodecs_ = $PlaceboCodecs$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/commands/$SerializeLootTableCommand" {
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$File, $File$Type} from "packages/java/io/$File"
import {$LootTable, $LootTable$Type} from "packages/net/minecraft/world/level/storage/loot/$LootTable"
import {$DynamicCommandExceptionType, $DynamicCommandExceptionType$Type} from "packages/com/mojang/brigadier/exceptions/$DynamicCommandExceptionType"

export class $SerializeLootTableCommand {
static readonly "GSON": $Gson
static readonly "NOT_FOUND": $DynamicCommandExceptionType

constructor()

public static "register"(arg0: $LiteralArgumentBuilder$Type<($CommandSourceStack$Type)>): void
public static "attemptSerialize"(arg0: $LootTable$Type, arg1: $File$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SerializeLootTableCommand$Type = ($SerializeLootTableCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SerializeLootTableCommand_ = $SerializeLootTableCommand$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/util/$RunnableReloader" {
import {$SimplePreparableReloadListener, $SimplePreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimplePreparableReloadListener"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $RunnableReloader extends $SimplePreparableReloadListener<(any)> {

constructor(arg0: $Runnable$Type)

public static "of"(arg0: $Runnable$Type): $RunnableReloader
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RunnableReloader$Type = ($RunnableReloader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RunnableReloader_ = $RunnableReloader$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/menu/$IDataUpdateListener" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IDataUpdateListener {

 "dataUpdated"(arg0: integer, arg1: integer): void

(arg0: integer, arg1: integer): void
}

export namespace $IDataUpdateListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IDataUpdateListener$Type = ($IDataUpdateListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IDataUpdateListener_ = $IDataUpdateListener$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/util/$AttributesUtil" {
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"

export class $AttributesUtil {

constructor()

public static "isPhysicalDamage"(arg0: $DamageSource$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributesUtil$Type = ($AttributesUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributesUtil_ = $AttributesUtil$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/util/$IEntityOwned" {
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export interface $IEntityOwned {

 "getOwner"(): $LivingEntity
 "setOwner"(arg0: $LivingEntity$Type): void
}

export namespace $IEntityOwned {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IEntityOwned$Type = ($IEntityOwned);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IEntityOwned_ = $IEntityOwned$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/asm/$ALHooks" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$EffectRenderingInventoryScreen, $EffectRenderingInventoryScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$EffectRenderingInventoryScreen"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"

export class $ALHooks {

constructor()

public static "getEffectTooltip"(arg0: $EffectRenderingInventoryScreen$Type<(any)>, arg1: $MobEffectInstance$Type, arg2: $List$Type<($Component$Type)>): $List<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ALHooks$Type = ($ALHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ALHooks_ = $ALHooks$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/util/$CachedObject$CachedObjectSource" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $CachedObject$CachedObjectSource {

 "getOrCreate"<T>(arg0: $ResourceLocation$Type, arg1: $Function$Type<($ItemStack$Type), (T)>): T
 "getOrCreate"<T>(arg0: $ResourceLocation$Type, arg1: $Function$Type<($ItemStack$Type), (T)>, arg2: $ToIntFunction$Type<($ItemStack$Type)>): T

(arg0: $ItemStack$Type, arg1: $ResourceLocation$Type, arg2: $Function$Type<($ItemStack$Type), (T)>): T
}

export namespace $CachedObject$CachedObjectSource {
function getOrCreate<T>(arg0: $ItemStack$Type, arg1: $ResourceLocation$Type, arg2: $Function$Type<($ItemStack$Type), (T)>): T
function getOrCreate<T>(arg0: $ItemStack$Type, arg1: $ResourceLocation$Type, arg2: $Function$Type<($ItemStack$Type), (T)>, arg3: $ToIntFunction$Type<($ItemStack$Type)>): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CachedObject$CachedObjectSource$Type = ($CachedObject$CachedObjectSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CachedObject$CachedObjectSource_ = $CachedObject$CachedObjectSource$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/codec/$CodecProvider" {
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export interface $CodecProvider<T> {

 "getCodec"(): $Codec<(any)>

(): $Codec<(any)>
}

export namespace $CodecProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CodecProvider$Type<T> = ($CodecProvider<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CodecProvider_<T> = $CodecProvider$Type<(T)>;
}}
declare module "packages/dev/shadowsoffire/attributeslib/client/$ModifierSource" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$ModifierSourceType, $ModifierSourceType$Type} from "packages/dev/shadowsoffire/attributeslib/client/$ModifierSourceType"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ModifierSource<T> implements $Comparable<($ModifierSource<(T)>)> {

constructor(arg0: $ModifierSourceType$Type<(T)>, arg1: $Comparator$Type<(T)>, arg2: T)

public "compareTo"(arg0: $ModifierSource$Type<(T)>): integer
public "getType"(): $ModifierSourceType<(T)>
public "getData"(): T
public "render"(arg0: $GuiGraphics$Type, arg1: $Font$Type, arg2: integer, arg3: integer): void
get "type"(): $ModifierSourceType<(T)>
get "data"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModifierSource$Type<T> = ($ModifierSource<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModifierSource_<T> = $ModifierSource$Type<(T)>;
}}
declare module "packages/dev/shadowsoffire/placebo/registry/$RegistryEvent$RegistryWrapper" {
import {$IForgeRegistry, $IForgeRegistry$Type} from "packages/net/minecraftforge/registries/$IForgeRegistry"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $RegistryEvent$RegistryWrapper<T> {

constructor(arg0: $IForgeRegistry$Type<(T)>)

public "register"(arg0: T, arg1: $ResourceLocation$Type): void
public "register"(arg0: T, arg1: string): void
public "registerAll"(...arg0: (any)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryEvent$RegistryWrapper$Type<T> = ($RegistryEvent$RegistryWrapper<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryEvent$RegistryWrapper_<T> = $RegistryEvent$RegistryWrapper$Type<(T)>;
}}
declare module "packages/dev/shadowsoffire/placebo/codec/$NullableFieldCodec" {
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$RecordBuilder, $RecordBuilder$Type} from "packages/com/mojang/serialization/$RecordBuilder"
import {$KeyCompressor, $KeyCompressor$Type} from "packages/com/mojang/serialization/$KeyCompressor"
import {$DataResult, $DataResult$Type} from "packages/com/mojang/serialization/$DataResult"
import {$DynamicOps, $DynamicOps$Type} from "packages/com/mojang/serialization/$DynamicOps"
import {$MapLike, $MapLike$Type} from "packages/com/mojang/serialization/$MapLike"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$OptionalFieldCodec, $OptionalFieldCodec$Type} from "packages/com/mojang/serialization/codecs/$OptionalFieldCodec"

export class $NullableFieldCodec<A> extends $OptionalFieldCodec<(A)> {


public "decode"<T>(arg0: $DynamicOps$Type<(T)>, arg1: $MapLike$Type<(T)>): $DataResult<($Optional<(A)>)>
public "compressor"<T>(arg0: $DynamicOps$Type<(T)>): $KeyCompressor<(T)>
public static "makeCompressedBuilder"<T>(arg0: $DynamicOps$Type<(T)>, arg1: $KeyCompressor$Type<(T)>): $RecordBuilder<(T)>
public static "forStrings"(arg0: $Supplier$Type<($Stream$Type<(string)>)>): $Keyable
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NullableFieldCodec$Type<A> = ($NullableFieldCodec<(A)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NullableFieldCodec_<A> = $NullableFieldCodec$Type<(A)>;
}}
declare module "packages/dev/shadowsoffire/placebo/screen/$TickableText" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $TickableText {

constructor(arg0: string, arg1: integer, arg2: boolean, arg3: float)
constructor(arg0: string, arg1: integer)

public "reset"(): void
public "tick"(): void
public "isDone"(): boolean
public "setTicks"(arg0: integer): $TickableText
public "render"(arg0: $Font$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer): void
public "getWidth"(arg0: $Font$Type): integer
public static "tickList"(arg0: $List$Type<($TickableText$Type)>): void
public "getMaxUsefulTicks"(): integer
public "causesNewLine"(): boolean
get "done"(): boolean
set "ticks"(value: integer)
get "maxUsefulTicks"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickableText$Type = ($TickableText);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickableText_ = $TickableText$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/api/$ALObjects" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ALObjects {

constructor()

public static "bootstrap"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ALObjects$Type = ($ALObjects);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ALObjects_ = $ALObjects$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/patreon/wings/$WingLayer" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$RenderLayer, $RenderLayer$Type} from "packages/net/minecraft/client/renderer/entity/layers/$RenderLayer"
import {$RenderLayerParent, $RenderLayerParent$Type} from "packages/net/minecraft/client/renderer/entity/$RenderLayerParent"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$PlayerModel, $PlayerModel$Type} from "packages/net/minecraft/client/model/$PlayerModel"

export class $WingLayer extends $RenderLayer<($AbstractClientPlayer), ($PlayerModel<($AbstractClientPlayer)>)> {

constructor(arg0: $RenderLayerParent$Type<($AbstractClientPlayer$Type), ($PlayerModel$Type<($AbstractClientPlayer$Type)>)>)

public "render"(arg0: $PoseStack$Type, arg1: $MultiBufferSource$Type, arg2: integer, arg3: $AbstractClientPlayer$Type, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WingLayer$Type = ($WingLayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WingLayer_ = $WingLayer$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/reload/$ReloadListenerPacket" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ReloadListenerPacket<T extends $ReloadListenerPacket<(T)>> {

constructor(arg0: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReloadListenerPacket$Type<T> = ($ReloadListenerPacket<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReloadListenerPacket_<T> = $ReloadListenerPacket$Type<(T)>;
}}
declare module "packages/dev/shadowsoffire/placebo/loot/$PoolBuilder" {
import {$LootItemFunction, $LootItemFunction$Type} from "packages/net/minecraft/world/level/storage/loot/functions/$LootItemFunction"
import {$LootPool$Builder, $LootPool$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/$LootPool$Builder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$LootPoolEntryContainer, $LootPoolEntryContainer$Type} from "packages/net/minecraft/world/level/storage/loot/entries/$LootPoolEntryContainer"
import {$LootItemCondition, $LootItemCondition$Type} from "packages/net/minecraft/world/level/storage/loot/predicates/$LootItemCondition"

export class $PoolBuilder extends $LootPool$Builder {
readonly "entries": $List<($LootPoolEntryContainer)>
readonly "conditions": $List<($LootItemCondition)>
readonly "functions": $List<($LootItemFunction)>

constructor(arg0: integer, arg1: integer)

public "addFunc"(...arg0: ($LootItemFunction$Type)[]): $PoolBuilder
public "addCondition"(...arg0: ($LootItemCondition$Type)[]): $PoolBuilder
public "addEntries"(...arg0: ($LootPoolEntryContainer$Type)[]): $PoolBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PoolBuilder$Type = ($PoolBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PoolBuilder_ = $PoolBuilder$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/packets/$ButtonClickMessage" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ButtonClickMessage {

constructor(arg0: integer)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ButtonClickMessage$Type = ($ButtonClickMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ButtonClickMessage_ = $ButtonClickMessage$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/tabs/$TabFillingRegistry" {
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$ITabFiller, $ITabFiller$Type} from "packages/dev/shadowsoffire/placebo/tabs/$ITabFiller"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$BuildCreativeModeTabContentsEvent, $BuildCreativeModeTabContentsEvent$Type} from "packages/net/minecraftforge/event/$BuildCreativeModeTabContentsEvent"

export class $TabFillingRegistry {

constructor()

public static "register"(arg0: $ResourceKey$Type<($CreativeModeTab$Type)>, ...arg1: ($Supplier$Type<(any)>)[]): void
public static "register"(arg0: $ITabFiller$Type, ...arg1: ($ResourceKey$Type<($CreativeModeTab$Type)>)[]): void
public static "register"(arg0: $Supplier$Type<(any)>, ...arg1: ($ResourceKey$Type<($CreativeModeTab$Type)>)[]): void
public static "register"(arg0: $ResourceKey$Type<($CreativeModeTab$Type)>, ...arg1: ($ITabFiller$Type)[]): void
public static "fillTabs"(arg0: $BuildCreativeModeTabContentsEvent$Type): void
public static "registerSimple"(arg0: $ResourceKey$Type<($CreativeModeTab$Type)>, ...arg1: ($ItemLike$Type)[]): void
public static "registerSimple"(arg0: $ItemLike$Type, ...arg1: ($ResourceKey$Type<($CreativeModeTab$Type)>)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TabFillingRegistry$Type = ($TabFillingRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TabFillingRegistry_ = $TabFillingRegistry$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/network/$MessageHelper" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$MessageProvider, $MessageProvider$Type} from "packages/dev/shadowsoffire/placebo/network/$MessageProvider"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $MessageHelper {

constructor()

public static "registerMessage"<T>(arg0: $SimpleChannel$Type, arg1: integer, arg2: $MessageProvider$Type<(T)>): void
public static "registerMessage"<MSG>(arg0: $SimpleChannel$Type, arg1: integer, arg2: $Class$Type<(MSG)>, arg3: $BiConsumer$Type<(MSG), ($FriendlyByteBuf$Type)>, arg4: $Function$Type<($FriendlyByteBuf$Type), (MSG)>, arg5: $BiConsumer$Type<(MSG), ($Supplier$Type<($NetworkEvent$Context$Type)>)>): void
public static "handlePacket"(arg0: $Runnable$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageHelper$Type = ($MessageHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageHelper_ = $MessageHelper$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/config/$ConfigElement" {
import {$ConfigGuiType, $ConfigGuiType$Type} from "packages/dev/shadowsoffire/placebo/config/$ConfigGuiType"
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$ConfigCategory, $ConfigCategory$Type} from "packages/dev/shadowsoffire/placebo/config/$ConfigCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Property, $Property$Type} from "packages/dev/shadowsoffire/placebo/config/$Property"
import {$IConfigElement, $IConfigElement$Type} from "packages/dev/shadowsoffire/placebo/config/$IConfigElement"

export class $ConfigElement implements $IConfigElement {

constructor(arg0: $ConfigCategory$Type)
constructor(arg0: $Property$Type)

public "getValidValuesDisplay"(): (string)[]
public "listCategoriesFirst"(arg0: boolean): $ConfigElement
public "getName"(): string
public "get"(): any
public "getDefault"(): any
public "set"(arg0: any): void
public "set"(arg0: (any)[]): void
public "getType"(): $ConfigGuiType
public static "getType"(arg0: $Property$Type): $ConfigGuiType
public "isDefault"(): boolean
public "getComment"(): string
public "getList"(): (any)[]
public "getQualifiedName"(): string
public "isProperty"(): boolean
public "hasSlidingControl"(): boolean
public "getChildElements"(): $List<($IConfigElement)>
public "isListLengthFixed"(): boolean
public "requiresMcRestart"(): boolean
public "getMinValue"(): any
public "getMaxValue"(): any
public "getDefaults"(): (any)[]
public "showInGui"(): boolean
public "isList"(): boolean
public "getMaxListLength"(): integer
public "getLanguageKey"(): string
public "getValidValues"(): (string)[]
public "requiresWorldRestart"(): boolean
public "setToDefault"(): void
public "getValidationPattern"(): $Pattern
get "validValuesDisplay"(): (string)[]
get "name"(): string
get "default"(): any
get "type"(): $ConfigGuiType
get "default"(): boolean
get "comment"(): string
get "list"(): (any)[]
get "qualifiedName"(): string
get "property"(): boolean
get "childElements"(): $List<($IConfigElement)>
get "listLengthFixed"(): boolean
get "minValue"(): any
get "maxValue"(): any
get "defaults"(): (any)[]
get "list"(): boolean
get "maxListLength"(): integer
get "languageKey"(): string
get "validValues"(): (string)[]
get "validationPattern"(): $Pattern
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigElement$Type = ($ConfigElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigElement_ = $ConfigElement$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/json/$GearSetRegistry" {
import {$GearSet$SetPredicate, $GearSet$SetPredicate$Type} from "packages/dev/shadowsoffire/placebo/json/$GearSet$SetPredicate"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$WeightedDynamicRegistry, $WeightedDynamicRegistry$Type} from "packages/dev/shadowsoffire/placebo/reload/$WeightedDynamicRegistry"
import {$GearSet, $GearSet$Type} from "packages/dev/shadowsoffire/placebo/json/$GearSet"

export class $GearSetRegistry extends $WeightedDynamicRegistry<($GearSet)> {
static readonly "INSTANCE": $GearSetRegistry

constructor()

public "getRandomSet"<T extends $Predicate<($GearSet)>>(arg0: $RandomSource$Type, arg1: float, arg2: $List$Type<($GearSet$SetPredicate$Type)>): $GearSet
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GearSetRegistry$Type = ($GearSetRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GearSetRegistry_ = $GearSetRegistry$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/config/$Configuration" {
import {$Property$Type, $Property$Type$Type} from "packages/dev/shadowsoffire/placebo/config/$Property$Type"
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$CharMatcher, $CharMatcher$Type} from "packages/com/google/common/base/$CharMatcher"
import {$ConfigCategory, $ConfigCategory$Type} from "packages/dev/shadowsoffire/placebo/config/$ConfigCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Property, $Property$Type} from "packages/dev/shadowsoffire/placebo/config/$Property"
import {$BufferedWriter, $BufferedWriter$Type} from "packages/java/io/$BufferedWriter"

export class $Configuration {
static readonly "CATEGORY_GENERAL": string
static readonly "CATEGORY_CLIENT": string
static readonly "ALLOWED_CHARS": string
static readonly "DEFAULT_ENCODING": string
static readonly "CATEGORY_SPLITTER": string
static readonly "allowedProperties": $CharMatcher
 "defaultEncoding": string
 "isChild": boolean

constructor(arg0: $File$Type)
constructor(arg0: string)

public "get"(arg0: string, arg1: string, arg2: (double)[], arg3: string, arg4: double, arg5: double): $Property
public "get"(arg0: string, arg1: string, arg2: (double)[], arg3: string, arg4: double, arg5: double, arg6: boolean, arg7: integer): $Property
public "get"(arg0: string, arg1: string, arg2: string): $Property
public "get"(arg0: string, arg1: string, arg2: string, arg3: string, arg4: $Pattern$Type): $Property
public "get"(arg0: string, arg1: string, arg2: double, arg3: string): $Property
public "get"(arg0: string, arg1: string, arg2: double, arg3: string, arg4: double, arg5: double): $Property
public "get"(arg0: string, arg1: string, arg2: (double)[]): $Property
public "get"(arg0: string, arg1: string, arg2: (double)[], arg3: string): $Property
public "get"(arg0: string, arg1: string, arg2: (string)[], arg3: string, arg4: $Pattern$Type): $Property
public "get"(arg0: string, arg1: string, arg2: (string)[], arg3: string, arg4: boolean, arg5: integer, arg6: $Pattern$Type): $Property
public "get"(arg0: string, arg1: string, arg2: string, arg3: string, arg4: $Property$Type$Type): $Property
public "get"(arg0: string, arg1: string, arg2: (string)[], arg3: string, arg4: $Property$Type$Type): $Property
public "get"(arg0: string, arg1: string, arg2: (string)[], arg3: string): $Property
public "get"(arg0: string, arg1: string, arg2: (string)[]): $Property
public "get"(arg0: string, arg1: string, arg2: string, arg3: string, arg4: (string)[]): $Property
public "get"(arg0: string, arg1: string, arg2: (boolean)[]): $Property
public "get"(arg0: string, arg1: string, arg2: (boolean)[], arg3: string): $Property
public "get"(arg0: string, arg1: string, arg2: (boolean)[], arg3: string, arg4: boolean, arg5: integer): $Property
public "get"(arg0: string, arg1: string, arg2: integer): $Property
public "get"(arg0: string, arg1: string, arg2: boolean, arg3: string): $Property
public "get"(arg0: string, arg1: string, arg2: boolean): $Property
public "get"(arg0: string, arg1: string, arg2: string, arg3: string): $Property
public "get"(arg0: string, arg1: string, arg2: (integer)[]): $Property
public "get"(arg0: string, arg1: string, arg2: double): $Property
public "get"(arg0: string, arg1: string, arg2: (integer)[], arg3: string, arg4: integer, arg5: integer): $Property
public "get"(arg0: string, arg1: string, arg2: (integer)[], arg3: string, arg4: integer, arg5: integer, arg6: boolean, arg7: integer): $Property
public "get"(arg0: string, arg1: string, arg2: (integer)[], arg3: string): $Property
public "get"(arg0: string, arg1: string, arg2: integer, arg3: string): $Property
public "get"(arg0: string, arg1: string, arg2: integer, arg3: string, arg4: integer, arg5: integer): $Property
public "toString"(): string
public "getBoolean"(arg0: string, arg1: string, arg2: boolean, arg3: string, arg4: string): boolean
public "getBoolean"(arg0: string, arg1: string, arg2: boolean, arg3: string): boolean
public "getInt"(arg0: string, arg1: string, arg2: integer, arg3: integer, arg4: integer, arg5: string): integer
public "getInt"(arg0: string, arg1: string, arg2: integer, arg3: integer, arg4: integer, arg5: string, arg6: string): integer
public "getFloat"(arg0: string, arg1: string, arg2: float, arg3: float, arg4: float, arg5: string): float
public "getFloat"(arg0: string, arg1: string, arg2: float, arg3: float, arg4: float, arg5: string, arg6: string): float
public "load"(): void
public "save"(): void
public "getString"(arg0: string, arg1: string, arg2: string, arg3: string, arg4: string): string
public "getString"(arg0: string, arg1: string, arg2: string, arg3: string, arg4: $Pattern$Type): string
public "getString"(arg0: string, arg1: string, arg2: string, arg3: string): string
public "getString"(arg0: string, arg1: string, arg2: string, arg3: string, arg4: string, arg5: $Pattern$Type): string
public "getString"(arg0: string, arg1: string, arg2: string, arg3: string, arg4: (string)[]): string
public "getString"(arg0: string, arg1: string, arg2: string, arg3: string, arg4: (string)[], arg5: string): string
public "setComment"(arg0: string): void
public "getConfigFile"(): $File
public static "writeComment"(arg0: $BufferedWriter$Type, arg1: string): void
public "getCategory"(arg0: string): $ConfigCategory
public "setTitle"(arg0: string): void
public "getCategoryNames"(): $Set<(string)>
public "hasChanged"(): boolean
public "getStringList"(arg0: string, arg1: string, arg2: (string)[], arg3: string, arg4: (string)[]): (string)[]
public "getStringList"(arg0: string, arg1: string, arg2: (string)[], arg3: string, arg4: (string)[], arg5: string): (string)[]
public "getStringList"(arg0: string, arg1: string, arg2: (string)[], arg3: string): (string)[]
public "hasCategory"(arg0: string): boolean
public "setCategoryComment"(arg0: string, arg1: string): $Configuration
public "copyCategoryProps"(arg0: $Configuration$Type, arg1: (string)[]): void
public "moveProperty"(arg0: string, arg1: string, arg2: string): boolean
public "renameProperty"(arg0: string, arg1: string, arg2: string): boolean
public "removeCategory"(arg0: $ConfigCategory$Type): void
public "setCategoryPropertyOrder"(arg0: string, arg1: $List$Type<(string)>): $Configuration
public "setCategoryLanguageKey"(arg0: string, arg1: string): $Configuration
public "setCategoryRequiresWorldRestart"(arg0: string, arg1: boolean): $Configuration
public "setCategoryRequiresMcRestart"(arg0: string, arg1: boolean): $Configuration
public "hasKey"(arg0: string, arg1: string): boolean
set "comment"(value: string)
get "configFile"(): $File
set "title"(value: string)
get "categoryNames"(): $Set<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Configuration$Type = ($Configuration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Configuration_ = $Configuration$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/reload/$WeightedDynamicRegistry" {
import {$WeightedDynamicRegistry$ILuckyWeighted, $WeightedDynamicRegistry$ILuckyWeighted$Type} from "packages/dev/shadowsoffire/placebo/reload/$WeightedDynamicRegistry$ILuckyWeighted"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$CodecProvider, $CodecProvider$Type} from "packages/dev/shadowsoffire/placebo/codec/$CodecProvider"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$DynamicRegistry, $DynamicRegistry$Type} from "packages/dev/shadowsoffire/placebo/reload/$DynamicRegistry"

export class $WeightedDynamicRegistry<V extends ($CodecProvider<(any)>) & ($WeightedDynamicRegistry$ILuckyWeighted)> extends $DynamicRegistry<(V)> {

constructor(arg0: $Logger$Type, arg1: string, arg2: boolean, arg3: boolean)

public "getRandomItem"(arg0: $RandomSource$Type): V
public "getRandomItem"(arg0: $RandomSource$Type, arg1: float): V
public "getRandomItem"(arg0: $RandomSource$Type, arg1: float, ...arg2: ($Predicate$Type<(V)>)[]): V
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WeightedDynamicRegistry$Type<V> = ($WeightedDynamicRegistry<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WeightedDynamicRegistry_<V> = $WeightedDynamicRegistry$Type<(V)>;
}}
declare module "packages/dev/shadowsoffire/placebo/recipe/$VanillaPacketDispatcher" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $VanillaPacketDispatcher {

constructor()

public static "dispatchTEToNearbyPlayers"(arg0: $BlockEntity$Type): void
public static "dispatchTEToNearbyPlayers"(arg0: $Level$Type, arg1: $BlockPos$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VanillaPacketDispatcher$Type = ($VanillaPacketDispatcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VanillaPacketDispatcher_ = $VanillaPacketDispatcher$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/compat/$TOPCompat$Provider" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $TOPCompat$Provider {

}

export namespace $TOPCompat$Provider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TOPCompat$Provider$Type = ($TOPCompat$Provider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TOPCompat$Provider_ = $TOPCompat$Provider$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/util/$ChestBuilder" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $ChestBuilder {


public "fill"(arg0: $ResourceLocation$Type): void
public static "place"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type, arg2: $ResourceLocation$Type): void
public static "placeTrapped"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type, arg2: $ResourceLocation$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChestBuilder$Type = ($ChestBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChestBuilder_ = $ChestBuilder$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/util/$PlaceboUtil" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$IReplacementBlock, $IReplacementBlock$Type} from "packages/dev/shadowsoffire/placebo/util/$IReplacementBlock"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$TextColor, $TextColor$Type} from "packages/net/minecraft/network/chat/$TextColor"

export class $PlaceboUtil {

constructor()

public static "asList"<T>(...arg0: (T)[]): $List<(T)>
public static "addLore"(arg0: $ItemStack$Type, arg1: $Component$Type): void
/**
 * 
 * @deprecated
 */
public static "registerOverride"<B extends ($Block) & ($IReplacementBlock)>(arg0: $Block$Type, arg1: B, arg2: string): void
/**
 * 
 * @deprecated
 */
public static "registerTypes"(): void
public static "getStackNBT"(arg0: $ItemStack$Type): $CompoundTag
public static "toStackArray"(...arg0: (any)[]): ($ItemStack)[]
/**
 * 
 * @deprecated
 */
public static "overrideStates"<B extends ($Block) & ($IReplacementBlock)>(arg0: $Block$Type, arg1: B): void
public static "toMutable"<T>(arg0: $List$Type<(T)>): $List<(T)>
public static "tryHarvestBlock"(arg0: $ServerPlayer$Type, arg1: $BlockPos$Type): boolean
/**
 * 
 * @deprecated
 */
public static "makeRecipeType"<T extends $Recipe<(any)>>(arg0: string): $RecipeType<(T)>
public static "registerCustomColor"<T extends $TextColor>(arg0: T): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceboUtil$Type = ($PlaceboUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceboUtil_ = $PlaceboUtil$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/impl/$PercentBasedAttribute" {
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$RangedAttribute, $RangedAttribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$RangedAttribute"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$IFormattableAttribute, $IFormattableAttribute$Type} from "packages/dev/shadowsoffire/attributeslib/api/$IFormattableAttribute"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export class $PercentBasedAttribute extends $RangedAttribute implements $IFormattableAttribute {
static readonly "MAX_NAME_LENGTH": integer

constructor(arg0: string, arg1: double, arg2: double, arg3: double)

public "toValueComponent"(arg0: $AttributeModifier$Operation$Type, arg1: double, arg2: $TooltipFlag$Type): $MutableComponent
public static "isNullOrAddition"(arg0: $AttributeModifier$Operation$Type): boolean
public static "toValueComponent"(arg0: $Attribute$Type, arg1: $AttributeModifier$Operation$Type, arg2: double, arg3: $TooltipFlag$Type): $MutableComponent
public static "toComponent"(arg0: $Attribute$Type, arg1: $AttributeModifier$Type, arg2: $TooltipFlag$Type): $MutableComponent
public static "toBaseComponent"(arg0: $Attribute$Type, arg1: double, arg2: double, arg3: boolean, arg4: $TooltipFlag$Type): $MutableComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PercentBasedAttribute$Type = ($PercentBasedAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PercentBasedAttribute_ = $PercentBasedAttribute$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/menu/$SimplerMenuProvider" {
import {$MenuUtil$PosFactory, $MenuUtil$PosFactory$Type} from "packages/dev/shadowsoffire/placebo/menu/$MenuUtil$PosFactory"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export class $SimplerMenuProvider<M extends $AbstractContainerMenu> implements $MenuProvider {

constructor(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $MenuUtil$PosFactory$Type<(M)>)

public "getDisplayName"(): $Component
public "createMenu"(arg0: integer, arg1: $Inventory$Type, arg2: $Player$Type): $AbstractContainerMenu
get "displayName"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimplerMenuProvider$Type<M> = ($SimplerMenuProvider<(M)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimplerMenuProvider_<M> = $SimplerMenuProvider$Type<(M)>;
}}
declare module "packages/dev/shadowsoffire/placebo/events/$GetEnchantmentLevelEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $GetEnchantmentLevelEvent extends $Event {

constructor(arg0: $ItemStack$Type, arg1: $Map$Type<($Enchantment$Type), (integer)>)
constructor()

public "getStack"(): $ItemStack
public "isCancelable"(): boolean
public "getEnchantments"(): $Map<($Enchantment), (integer)>
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "stack"(): $ItemStack
get "cancelable"(): boolean
get "enchantments"(): $Map<($Enchantment), (integer)>
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GetEnchantmentLevelEvent$Type = ($GetEnchantmentLevelEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GetEnchantmentLevelEvent_ = $GetEnchantmentLevelEvent$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/commands/$StringToObjCommand" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandBuildContext, $CommandBuildContext$Type} from "packages/net/minecraft/commands/$CommandBuildContext"
import {$DynamicCommandExceptionType, $DynamicCommandExceptionType$Type} from "packages/com/mojang/brigadier/exceptions/$DynamicCommandExceptionType"

export class $StringToObjCommand {
static readonly "GSON": $Gson
static readonly "NOT_FOUND": $DynamicCommandExceptionType

constructor()

public static "register"(arg0: $LiteralArgumentBuilder$Type<($CommandSourceStack$Type)>, arg1: $CommandBuildContext$Type): void
public static "toJsonStr"(arg0: $CompoundTag$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringToObjCommand$Type = ($StringToObjCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringToObjCommand_ = $StringToObjCommand$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/config/$ConfigFlags" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ConfigFlags {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigFlags$Type = ($ConfigFlags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigFlags_ = $ConfigFlags$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/color/$GradientColor" {
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$TextColor, $TextColor$Type} from "packages/net/minecraft/network/chat/$TextColor"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $GradientColor extends $TextColor {
static readonly "RAINBOW_GRADIENT": (integer)[]
static readonly "RAINBOW": $GradientColor
static readonly "CODEC": $Codec<($TextColor)>
static "NAMED_COLORS": $Map<(string), ($TextColor)>

constructor(arg0: (integer)[], arg1: string, arg2: float)
constructor(arg0: (integer)[], arg1: string)

public "getValue"(): integer
public static "checkSpecialEquality"(o: any, o1: any, shallow: boolean): boolean
get "value"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GradientColor$Type = ($GradientColor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GradientColor_ = $GradientColor$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/json/$JsonUtil" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"
import {$JsonSerializer, $JsonSerializer$Type} from "packages/com/google/gson/$JsonSerializer"
import {$IForgeRegistry, $IForgeRegistry$Type} from "packages/net/minecraftforge/registries/$IForgeRegistry"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $JsonUtil {

constructor()

public static "getRegistryObject"<T>(arg0: $JsonObject$Type, arg1: string, arg2: $IForgeRegistry$Type<(T)>): T
public static "checkAndLogEmpty"(arg0: $JsonElement$Type, arg1: $ResourceLocation$Type, arg2: string, arg3: $Logger$Type): boolean
public static "checkConditions"(arg0: $JsonElement$Type, arg1: $ResourceLocation$Type, arg2: string, arg3: $Logger$Type, arg4: $ICondition$IContext$Type): boolean
/**
 * 
 * @deprecated
 */
public static "makeSerializer"<T>(arg0: $JsonDeserializer$Type<(T)>, arg1: $JsonSerializer$Type<(T)>): any
/**
 * 
 * @deprecated
 */
public static "makeSerializer"<T>(arg0: $IForgeRegistry$Type<(T)>): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonUtil$Type = ($JsonUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonUtil_ = $JsonUtil$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/events/$AnvilLandEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$FallingBlockEntity, $FallingBlockEntity$Type} from "packages/net/minecraft/world/entity/item/$FallingBlockEntity"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $AnvilLandEvent extends $Event {

constructor()
constructor(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $BlockState$Type, arg4: $FallingBlockEntity$Type)

public "getLevel"(): $Level
public "isCancelable"(): boolean
public "getEntity"(): $FallingBlockEntity
public "getNewState"(): $BlockState
public "getPos"(): $BlockPos
public "getOldState"(): $BlockState
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "level"(): $Level
get "cancelable"(): boolean
get "entity"(): $FallingBlockEntity
get "newState"(): $BlockState
get "pos"(): $BlockPos
get "oldState"(): $BlockState
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnvilLandEvent$Type = ($AnvilLandEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnvilLandEvent_ = $AnvilLandEvent$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/util/$Comparators" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"

export class $Comparators {

constructor()

public static "chained"<T>(...arg0: ($Comparator$Type<(T)>)[]): $Comparator<(T)>
public static "idComparator"<T>(arg0: $Registry$Type<(T)>): $Comparator<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Comparators$Type = ($Comparators);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Comparators_ = $Comparators$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/util/$StepFunction" {
import {$Long2FloatFunction, $Long2FloatFunction$Type} from "packages/it/unimi/dsi/fastutil/longs/$Long2FloatFunction"
import {$Float2DoubleFunction, $Float2DoubleFunction$Type} from "packages/it/unimi/dsi/fastutil/floats/$Float2DoubleFunction"
import {$Float2IntFunction, $Float2IntFunction$Type} from "packages/it/unimi/dsi/fastutil/floats/$Float2IntFunction"
import {$Float2CharFunction, $Float2CharFunction$Type} from "packages/it/unimi/dsi/fastutil/floats/$Float2CharFunction"
import {$Float2ShortFunction, $Float2ShortFunction$Type} from "packages/it/unimi/dsi/fastutil/floats/$Float2ShortFunction"
import {$Float2FloatFunction, $Float2FloatFunction$Type} from "packages/it/unimi/dsi/fastutil/floats/$Float2FloatFunction"
import {$DoubleUnaryOperator, $DoubleUnaryOperator$Type} from "packages/java/util/function/$DoubleUnaryOperator"
import {$Float2ReferenceFunction, $Float2ReferenceFunction$Type} from "packages/it/unimi/dsi/fastutil/floats/$Float2ReferenceFunction"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Char2FloatFunction, $Char2FloatFunction$Type} from "packages/it/unimi/dsi/fastutil/chars/$Char2FloatFunction"
import {$Object2FloatFunction, $Object2FloatFunction$Type} from "packages/it/unimi/dsi/fastutil/objects/$Object2FloatFunction"
import {$Float2ObjectFunction, $Float2ObjectFunction$Type} from "packages/it/unimi/dsi/fastutil/floats/$Float2ObjectFunction"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Int2FloatFunction, $Int2FloatFunction$Type} from "packages/it/unimi/dsi/fastutil/ints/$Int2FloatFunction"
import {$Double2FloatFunction, $Double2FloatFunction$Type} from "packages/it/unimi/dsi/fastutil/doubles/$Double2FloatFunction"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Float2LongFunction, $Float2LongFunction$Type} from "packages/it/unimi/dsi/fastutil/floats/$Float2LongFunction"
import {$Reference2FloatFunction, $Reference2FloatFunction$Type} from "packages/it/unimi/dsi/fastutil/objects/$Reference2FloatFunction"
import {$Byte2FloatFunction, $Byte2FloatFunction$Type} from "packages/it/unimi/dsi/fastutil/bytes/$Byte2FloatFunction"
import {$Float2ByteFunction, $Float2ByteFunction$Type} from "packages/it/unimi/dsi/fastutil/floats/$Float2ByteFunction"
import {$Short2FloatFunction, $Short2FloatFunction$Type} from "packages/it/unimi/dsi/fastutil/shorts/$Short2FloatFunction"

export class $StepFunction extends $Record implements $Float2FloatFunction {
static readonly "STRICT_CODEC": $Codec<($StepFunction)>
static readonly "CONSTANT_CODEC": $Codec<($StepFunction)>
static readonly "CODEC": $Codec<($StepFunction)>

constructor(min: float, steps: integer, step: float)

public "get"(arg0: float): float
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "min"(): float
public "max"(): float
public "getInt"(arg0: float): integer
public static "constant"(arg0: float): $StepFunction
public "write"(arg0: $FriendlyByteBuf$Type): void
public static "read"(arg0: $FriendlyByteBuf$Type): $StepFunction
public "step"(): float
public "steps"(): integer
public "getStep"(arg0: float): integer
public "getIntForStep"(arg0: integer): float
public "getForStep"(arg0: integer): float
public "remove"(arg0: float): float
/**
 * 
 * @deprecated
 */
public "remove"(arg0: any): float
public "put"(arg0: float, arg1: float): float
/**
 * 
 * @deprecated
 */
public "put"(arg0: float, arg1: float): float
public static "identity"(): $Float2FloatFunction
/**
 * 
 * @deprecated
 */
public "containsKey"(arg0: any): boolean
public "containsKey"(arg0: float): boolean
/**
 * 
 * @deprecated
 */
public "getOrDefault"(arg0: any, arg1: float): float
public "getOrDefault"(arg0: float, arg1: float): float
/**
 * 
 * @deprecated
 */
public "applyAsDouble"(arg0: double): double
/**
 * 
 * @deprecated
 */
public "compose"<T>(arg0: $Function$Type<(any), (any)>): $Function<(T), (float)>
/**
 * 
 * @deprecated
 */
public "andThen"<T>(arg0: $Function$Type<(any), (any)>): $Function<(float), (T)>
public "defaultReturnValue"(arg0: float): void
public "defaultReturnValue"(): float
public "composeInt"(arg0: $Int2FloatFunction$Type): $Int2FloatFunction
public "composeByte"(arg0: $Byte2FloatFunction$Type): $Byte2FloatFunction
public "andThenByte"(arg0: $Float2ByteFunction$Type): $Float2ByteFunction
public "andThenShort"(arg0: $Float2ShortFunction$Type): $Float2ShortFunction
public "composeShort"(arg0: $Short2FloatFunction$Type): $Short2FloatFunction
public "andThenInt"(arg0: $Float2IntFunction$Type): $Float2IntFunction
public "andThenLong"(arg0: $Float2LongFunction$Type): $Float2LongFunction
public "composeLong"(arg0: $Long2FloatFunction$Type): $Long2FloatFunction
public "composeObject"<T>(arg0: $Object2FloatFunction$Type<(any)>): $Object2FloatFunction<(T)>
public "andThenChar"(arg0: $Float2CharFunction$Type): $Float2CharFunction
public "composeFloat"(arg0: $Float2FloatFunction$Type): $Float2FloatFunction
public "andThenFloat"(arg0: $Float2FloatFunction$Type): $Float2FloatFunction
public "andThenDouble"(arg0: $Float2DoubleFunction$Type): $Float2DoubleFunction
public "composeDouble"(arg0: $Double2FloatFunction$Type): $Double2FloatFunction
public "andThenReference"<T>(arg0: $Float2ReferenceFunction$Type<(any)>): $Float2ReferenceFunction<(T)>
public "composeReference"<T>(arg0: $Reference2FloatFunction$Type<(any)>): $Reference2FloatFunction<(T)>
public "andThenObject"<T>(arg0: $Float2ObjectFunction$Type<(any)>): $Float2ObjectFunction<(T)>
public "composeChar"(arg0: $Char2FloatFunction$Type): $Char2FloatFunction
public "clear"(): void
public "size"(): integer
public "apply"(arg0: float): float
public "compose"(arg0: $DoubleUnaryOperator$Type): $DoubleUnaryOperator
public "andThen"(arg0: $DoubleUnaryOperator$Type): $DoubleUnaryOperator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StepFunction$Type = ($StepFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StepFunction_ = $StepFunction$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/registry/$RegObjHelper" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$IForgeRegistry, $IForgeRegistry$Type} from "packages/net/minecraftforge/registries/$IForgeRegistry"
import {$Stat, $Stat$Type} from "packages/net/minecraft/stats/$Stat"
import {$PaintingVariant, $PaintingVariant$Type} from "packages/net/minecraft/world/entity/decoration/$PaintingVariant"
import {$StatType, $StatType$Type} from "packages/net/minecraft/stats/$StatType"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$ParticleOptions, $ParticleOptions$Type} from "packages/net/minecraft/core/particles/$ParticleOptions"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$ParticleType, $ParticleType$Type} from "packages/net/minecraft/core/particles/$ParticleType"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$FeatureConfiguration, $FeatureConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$FeatureConfiguration"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$Feature, $Feature$Type} from "packages/net/minecraft/world/level/levelgen/feature/$Feature"

export class $RegObjHelper {

constructor(arg0: string)

public static "create"<T>(arg0: string, arg1: string, arg2: $IForgeRegistry$Type<(any)>): $RegistryObject<(T)>
public "block"<T extends $Block>(arg0: string): $RegistryObject<(T)>
public "recipe"<C extends $Container, U extends $Recipe<(C)>, T extends $RecipeType<(U)>>(arg0: string): $RegistryObject<(T)>
public "feature"<U extends $FeatureConfiguration, T extends $Feature<(U)>>(arg0: string): $RegistryObject<(T)>
public "attribute"<T extends $Attribute>(arg0: string): $RegistryObject<(T)>
public "stat"<S, U extends $Stat<(S)>, T extends $StatType<(U)>>(arg0: string): $RegistryObject<(T)>
public "item"<T extends $Item>(arg0: string): $RegistryObject<(T)>
public "potion"<T extends $Potion>(arg0: string): $RegistryObject<(T)>
public "menu"<U extends $AbstractContainerMenu, T extends $MenuType<(U)>>(arg0: string): $RegistryObject<(T)>
public "sound"<T extends $SoundEvent>(arg0: string): $RegistryObject<(T)>
public "custom"<T>(arg0: string, arg1: $IForgeRegistry$Type<(any)>): $RegistryObject<(T)>
public "effect"<T extends $MobEffect>(arg0: string): $RegistryObject<(T)>
public "entity"<U extends $Entity, T extends $EntityType<(U)>>(arg0: string): $RegistryObject<(T)>
public "enchant"<T extends $Enchantment>(arg0: string): $RegistryObject<(T)>
public "recipeSerializer"<C extends $Container, U extends $Recipe<(C)>, T extends $RecipeSerializer<(U)>>(arg0: string): $RegistryObject<(T)>
public "blockEntity"<U extends $BlockEntity, T extends $BlockEntityType<(U)>>(arg0: string): $RegistryObject<(T)>
public "particle"<U extends $ParticleOptions, T extends $ParticleType<(U)>>(arg0: string): $RegistryObject<(T)>
public "fluid"<T extends $Fluid>(arg0: string): $RegistryObject<(T)>
public "painting"<T extends $PaintingVariant>(arg0: string): $RegistryObject<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegObjHelper$Type = ($RegObjHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegObjHelper_ = $RegObjHelper$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/$Placebo" {
import {$RegistryEvent$Register, $RegistryEvent$Register$Type} from "packages/dev/shadowsoffire/placebo/registry/$RegistryEvent$Register"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$RegisterCommandsEvent, $RegisterCommandsEvent$Type} from "packages/net/minecraftforge/event/$RegisterCommandsEvent"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$RegisterEvent, $RegisterEvent$Type} from "packages/net/minecraftforge/registries/$RegisterEvent"
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $Placebo {
static readonly "MODID": string
static readonly "LOGGER": $Logger
static readonly "CHANNEL": $SimpleChannel

constructor()

public "setup"(arg0: $FMLCommonSetupEvent$Type): void
public "registerCommands"(arg0: $RegisterCommandsEvent$Type): void
public "registerElse"(arg0: $RegistryEvent$Register$Type<($RecipeType$Type<(any)>)>): void
public "postRegistryEvents"(arg0: $RegisterEvent$Type): void
set "up"(value: $FMLCommonSetupEvent$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Placebo$Type = ($Placebo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Placebo_ = $Placebo$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/json/$NBTAdapter" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"
import {$JsonSerializer, $JsonSerializer$Type} from "packages/com/google/gson/$JsonSerializer"
import {$JsonSerializationContext, $JsonSerializationContext$Type} from "packages/com/google/gson/$JsonSerializationContext"
import {$JsonDeserializationContext, $JsonDeserializationContext$Type} from "packages/com/google/gson/$JsonDeserializationContext"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $NBTAdapter implements $JsonDeserializer<($CompoundTag)>, $JsonSerializer<($CompoundTag)> {
/**
 * 
 * @deprecated
 */
static readonly "INSTANCE": $NBTAdapter
static readonly "EITHER_CODEC": $Codec<($CompoundTag)>

constructor()

public "deserialize"(arg0: $JsonElement$Type, arg1: $Type$Type, arg2: $JsonDeserializationContext$Type): $CompoundTag
public "serialize"(arg0: $CompoundTag$Type, arg1: $Type$Type, arg2: $JsonSerializationContext$Type): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NBTAdapter$Type = ($NBTAdapter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NBTAdapter_ = $NBTAdapter$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/network/$PacketDistro" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $PacketDistro {

constructor()

public static "sendToTracking"(arg0: $SimpleChannel$Type, arg1: any, arg2: $ServerLevel$Type, arg3: $BlockPos$Type): void
public static "sendToAll"(arg0: $SimpleChannel$Type, arg1: any): void
public static "sendTo"(arg0: $SimpleChannel$Type, arg1: any, arg2: $Player$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketDistro$Type = ($PacketDistro);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketDistro_ = $PacketDistro$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/mobfx/$DetonationEffect" {
import {$AttributeMap, $AttributeMap$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeMap"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"

export class $DetonationEffect extends $MobEffect {

constructor()

public "removeAttributeModifiers"(arg0: $LivingEntity$Type, arg1: $AttributeMap$Type, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DetonationEffect$Type = ($DetonationEffect);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DetonationEffect_ = $DetonationEffect$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/reload/$DynamicRegistry" {
import {$SimpleJsonResourceReloadListener, $SimpleJsonResourceReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimpleJsonResourceReloadListener"
import {$DynamicHolder, $DynamicHolder$Type} from "packages/dev/shadowsoffire/placebo/reload/$DynamicHolder"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$CodecProvider, $CodecProvider$Type} from "packages/dev/shadowsoffire/placebo/codec/$CodecProvider"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$RegistryCallback, $RegistryCallback$Type} from "packages/dev/shadowsoffire/placebo/reload/$RegistryCallback"

export class $DynamicRegistry<R extends $CodecProvider<(any)>> extends $SimpleJsonResourceReloadListener {

constructor(arg0: $Logger$Type, arg1: string, arg2: boolean, arg3: boolean)

public "getValue"(arg0: $ResourceLocation$Type): R
public "getKey"(arg0: R): $ResourceLocation
public "getOrDefault"(arg0: $ResourceLocation$Type, arg1: R): R
public "holder"<T extends R>(arg0: T): $DynamicHolder<(T)>
public "holder"<T extends R>(arg0: $ResourceLocation$Type): $DynamicHolder<(T)>
public "getKeys"(): $Set<($ResourceLocation)>
public "registerToBus"(): void
public "holderCodec"(): $Codec<($DynamicHolder<(R)>)>
public "emptyHolder"(): $DynamicHolder<(R)>
public "registerCodec"(arg0: $ResourceLocation$Type, arg1: $Codec$Type<(any)>): void
public "removeCallback"(arg0: $RegistryCallback$Type<(R)>): boolean
public "addCallback"(arg0: $RegistryCallback$Type<(R)>): boolean
public "getValues"(): $Collection<(R)>
get "keys"(): $Set<($ResourceLocation)>
get "values"(): $Collection<(R)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicRegistry$Type<R> = ($DynamicRegistry<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicRegistry_<R> = $DynamicRegistry$Type<(R)>;
}}
declare module "packages/dev/shadowsoffire/placebo/json/$GearSet$SetPredicate" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$GearSet, $GearSet$Type} from "packages/dev/shadowsoffire/placebo/json/$GearSet"

export class $GearSet$SetPredicate implements $Predicate<($GearSet)> {
static readonly "CODEC": $Codec<($GearSet$SetPredicate)>

constructor(arg0: string)

public "toString"(): string
public "test"(arg0: $GearSet$Type): boolean
public "or"(arg0: $Predicate$Type<(any)>): $Predicate<($GearSet)>
public "negate"(): $Predicate<($GearSet)>
public "and"(arg0: $Predicate$Type<(any)>): $Predicate<($GearSet)>
public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<($GearSet)>
public static "isEqual"<T>(arg0: any): $Predicate<($GearSet)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GearSet$SetPredicate$Type = ($GearSet$SetPredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GearSet$SetPredicate_ = $GearSet$SetPredicate$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/$AttributesLib" {
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$DeferredHelper, $DeferredHelper$Type} from "packages/dev/shadowsoffire/placebo/registry/$DeferredHelper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"
import {$EntityAttributeModificationEvent, $EntityAttributeModificationEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityAttributeModificationEvent"
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $AttributesLib {
static readonly "MODID": string
static readonly "LOGGER": $Logger
static readonly "R": $DeferredHelper
static "localAtkStrength": float
static "knowledgeMult": integer
static readonly "CHANNEL": $SimpleChannel

constructor()

public "init"(arg0: $FMLCommonSetupEvent$Type): void
public "setup"(arg0: $FMLCommonSetupEvent$Type): void
public static "loc"(arg0: string): $ResourceLocation
public "applyAttribs"(arg0: $EntityAttributeModificationEvent$Type): void
public static "getTooltipFlag"(): $TooltipFlag
set "up"(value: $FMLCommonSetupEvent$Type)
get "tooltipFlag"(): $TooltipFlag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributesLib$Type = ($AttributesLib);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributesLib_ = $AttributesLib$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/menu/$FilteredSlot" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$InternalItemHandler, $InternalItemHandler$Type} from "packages/dev/shadowsoffire/placebo/cap/$InternalItemHandler"
import {$SlotItemHandler, $SlotItemHandler$Type} from "packages/net/minecraftforge/items/$SlotItemHandler"

export class $FilteredSlot extends $SlotItemHandler {
readonly "container": $Container
 "index": integer
 "x": integer
 "y": integer

constructor(arg0: $InternalItemHandler$Type, arg1: integer, arg2: integer, arg3: integer, arg4: $Predicate$Type<($ItemStack$Type)>)
constructor(arg0: $InternalItemHandler$Type, arg1: integer, arg2: integer, arg3: integer)

public "remove"(arg0: integer): $ItemStack
public "mayPlace"(arg0: $ItemStack$Type): boolean
public "mayPickup"(arg0: $Player$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FilteredSlot$Type = ($FilteredSlot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FilteredSlot_ = $FilteredSlot$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/api/$IFormattableAttribute" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export interface $IFormattableAttribute {

 "ths"(): $Attribute
 "getBaseUUID"(): $UUID
 "getDebugInfo"(arg0: $AttributeModifier$Type, arg1: $TooltipFlag$Type): $Component
 "toValueComponent"(arg0: $AttributeModifier$Operation$Type, arg1: double, arg2: $TooltipFlag$Type): $MutableComponent
 "toComponent"(arg0: $AttributeModifier$Type, arg1: $TooltipFlag$Type): $MutableComponent
 "getBonusBaseValue"(arg0: $ItemStack$Type): double
 "toBaseComponent"(arg0: double, arg1: double, arg2: boolean, arg3: $TooltipFlag$Type): $MutableComponent
 "addBonusTooltips"(arg0: $ItemStack$Type, arg1: $Consumer$Type<($Component$Type)>, arg2: $TooltipFlag$Type): void
}

export namespace $IFormattableAttribute {
function isNullOrAddition(arg0: $AttributeModifier$Operation$Type): boolean
function toValueComponent(arg0: $Attribute$Type, arg1: $AttributeModifier$Operation$Type, arg2: double, arg3: $TooltipFlag$Type): $MutableComponent
function toComponent(arg0: $Attribute$Type, arg1: $AttributeModifier$Type, arg2: $TooltipFlag$Type): $MutableComponent
function toBaseComponent(arg0: $Attribute$Type, arg1: double, arg2: double, arg3: boolean, arg4: $TooltipFlag$Type): $MutableComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IFormattableAttribute$Type = ($IFormattableAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IFormattableAttribute_ = $IFormattableAttribute$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/registry/$RegistryEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$GenericEvent, $GenericEvent$Type} from "packages/net/minecraftforge/eventbus/api/$GenericEvent"
import {$IModBusEvent, $IModBusEvent$Type} from "packages/net/minecraftforge/fml/event/$IModBusEvent"

/**
 * 
 * @deprecated
 */
export class $RegistryEvent<T> extends $GenericEvent<(T)> implements $IModBusEvent {

constructor()

public "getListenerList"(): $ListenerList
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryEvent$Type<T> = ($RegistryEvent<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryEvent_<T> = $RegistryEvent$Type<(T)>;
}}
declare module "packages/dev/shadowsoffire/placebo/recipe/$RecipeHelper" {
import {$PreparableReloadListener, $PreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener"
import {$RecipeHelper$RecipeFactory, $RecipeHelper$RecipeFactory$Type} from "packages/dev/shadowsoffire/placebo/recipe/$RecipeHelper$RecipeFactory"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$RecipeManager, $RecipeManager$Type} from "packages/net/minecraft/world/item/crafting/$RecipeManager"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

/**
 * 
 * @deprecated
 */
export class $RecipeHelper {

constructor(arg0: string)

public "registerProvider"(arg0: $Consumer$Type<($RecipeHelper$RecipeFactory$Type)>): void
public static "getReloader"(arg0: $RecipeManager$Type): $PreparableReloadListener
public static "makeStack"(arg0: any): $ItemStack
public static "createInput"(arg0: string, arg1: boolean, ...arg2: (any)[]): $NonNullList<($Ingredient)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeHelper$Type = ($RecipeHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeHelper_ = $RecipeHelper$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/tabs/$ITabFiller" {
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$CreativeModeTab$Output, $CreativeModeTab$Output$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$Output"

export interface $ITabFiller {

 "fillItemCategory"(arg0: $CreativeModeTab$Type, arg1: $CreativeModeTab$Output$Type): void

(arg0: $ItemLike$Type): $ITabFiller
}

export namespace $ITabFiller {
function simple(arg0: $ItemLike$Type): $ITabFiller
function delegating(arg0: $Supplier$Type<(any)>): $ITabFiller
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ITabFiller$Type = ($ITabFiller);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ITabFiller_ = $ITabFiller$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/impl/$BooleanAttribute" {
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$IFormattableAttribute, $IFormattableAttribute$Type} from "packages/dev/shadowsoffire/attributeslib/api/$IFormattableAttribute"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export class $BooleanAttribute extends $Attribute implements $IFormattableAttribute {
static readonly "MAX_NAME_LENGTH": integer

constructor(arg0: string, arg1: boolean)

public "toValueComponent"(arg0: $AttributeModifier$Operation$Type, arg1: double, arg2: $TooltipFlag$Type): $MutableComponent
public "sanitizeValue"(arg0: double): double
public "toComponent"(arg0: $AttributeModifier$Type, arg1: $TooltipFlag$Type): $MutableComponent
public static "isNullOrAddition"(arg0: $AttributeModifier$Operation$Type): boolean
public static "toValueComponent"(arg0: $Attribute$Type, arg1: $AttributeModifier$Operation$Type, arg2: double, arg3: $TooltipFlag$Type): $MutableComponent
public static "toComponent"(arg0: $Attribute$Type, arg1: $AttributeModifier$Type, arg2: $TooltipFlag$Type): $MutableComponent
public static "toBaseComponent"(arg0: $Attribute$Type, arg1: double, arg2: double, arg3: boolean, arg4: $TooltipFlag$Type): $MutableComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BooleanAttribute$Type = ($BooleanAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BooleanAttribute_ = $BooleanAttribute$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/mixin/$AttributeInstanceAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $AttributeInstanceAccessor {

 "getCachedValue"(): double

(): double
}

export namespace $AttributeInstanceAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributeInstanceAccessor$Type = ($AttributeInstanceAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributeInstanceAccessor_ = $AttributeInstanceAccessor$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/config/$IConfigElement" {
import {$ConfigGuiType, $ConfigGuiType$Type} from "packages/dev/shadowsoffire/placebo/config/$ConfigGuiType"
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$List, $List$Type} from "packages/java/util/$List"

export interface $IConfigElement {

 "getValidValuesDisplay"(): (string)[]
 "getName"(): string
 "get"(): any
 "getDefault"(): any
 "set"(arg0: any): void
 "set"(arg0: (any)[]): void
 "getType"(): $ConfigGuiType
 "isDefault"(): boolean
 "getComment"(): string
 "getList"(): (any)[]
 "getQualifiedName"(): string
 "isProperty"(): boolean
 "hasSlidingControl"(): boolean
 "getChildElements"(): $List<($IConfigElement)>
 "isListLengthFixed"(): boolean
 "requiresMcRestart"(): boolean
 "getMinValue"(): any
 "getMaxValue"(): any
 "getDefaults"(): (any)[]
 "showInGui"(): boolean
 "isList"(): boolean
 "getMaxListLength"(): integer
 "getLanguageKey"(): string
 "getValidValues"(): (string)[]
 "requiresWorldRestart"(): boolean
 "setToDefault"(): void
 "getValidationPattern"(): $Pattern
}

export namespace $IConfigElement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IConfigElement$Type = ($IConfigElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IConfigElement_ = $IConfigElement$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/config/$Property" {
import {$Property$Type, $Property$Type$Type} from "packages/dev/shadowsoffire/placebo/config/$Property$Type"
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"

export class $Property {

constructor(arg0: string, arg1: (string)[], arg2: $Property$Type$Type)
constructor(arg0: string, arg1: string, arg2: $Property$Type$Type, arg3: (string)[], arg4: string)
constructor(arg0: string, arg1: (string)[], arg2: $Property$Type$Type, arg3: string)
constructor(arg0: string, arg1: string, arg2: $Property$Type$Type)
constructor(arg0: string, arg1: string, arg2: $Property$Type$Type, arg3: boolean)
constructor(arg0: string, arg1: string, arg2: $Property$Type$Type, arg3: boolean, arg4: string)
constructor(arg0: string, arg1: string, arg2: $Property$Type$Type, arg3: string)
constructor(arg0: string, arg1: string, arg2: $Property$Type$Type, arg3: (string)[])

public "getName"(): string
public "getBoolean"(arg0: boolean): boolean
public "getBoolean"(): boolean
public "getInt"(): integer
public "getInt"(arg0: integer): integer
public "getLong"(): long
public "getLong"(arg0: long): long
public "getDouble"(): double
public "getDouble"(arg0: double): double
public "getDefault"(): string
public "set"(arg0: (string)[]): void
public "set"(arg0: string): void
public "set"(arg0: long): void
public "set"(arg0: (double)[]): void
public "set"(arg0: integer): void
public "set"(arg0: (boolean)[]): void
public "set"(arg0: (integer)[]): void
public "set"(arg0: double): void
public "set"(arg0: boolean): void
public "setName"(arg0: string): void
public "setValue"(arg0: boolean): $Property
public "setValue"(arg0: string): $Property
public "setValue"(arg0: double): $Property
public "setValue"(arg0: integer): $Property
public "getType"(): $Property$Type
public "isDefault"(): boolean
public "getString"(): string
public "isIntValue"(): boolean
public "setComment"(arg0: string): void
public "getComment"(): string
public "hasChanged"(): boolean
public "setValues"(arg0: (integer)[]): $Property
public "setValues"(arg0: (boolean)[]): $Property
public "setValues"(arg0: (string)[]): $Property
public "setValues"(arg0: (double)[]): $Property
public "getStringList"(): (string)[]
public "isListLengthFixed"(): boolean
public "setMinValue"(arg0: integer): $Property
public "setMinValue"(arg0: double): $Property
public "setMaxValue"(arg0: double): $Property
public "setMaxValue"(arg0: integer): $Property
public "setDefaultValue"(arg0: boolean): $Property
public "setDefaultValue"(arg0: string): $Property
public "setDefaultValue"(arg0: double): $Property
public "setDefaultValue"(arg0: integer): $Property
public "setDefaultValues"(arg0: (boolean)[]): $Property
public "setDefaultValues"(arg0: (double)[]): $Property
public "setDefaultValues"(arg0: (integer)[]): $Property
public "setDefaultValues"(arg0: (string)[]): $Property
public "isIntList"(): boolean
public "isBooleanValue"(): boolean
public "setMaxListLength"(arg0: integer): $Property
public "isBooleanList"(): boolean
public "isDoubleValue"(): boolean
public "setValidValues"(arg0: (string)[]): $Property
public "isDoubleList"(): boolean
public "requiresMcRestart"(): boolean
public "setLanguageKey"(arg0: string): $Property
public "getMinValue"(): string
public "getMaxValue"(): string
public "getDefaults"(): (string)[]
public "setShowInGui"(arg0: boolean): $Property
public "showInGui"(): boolean
public "isList"(): boolean
public "getIntList"(): (integer)[]
public "getMaxListLength"(): integer
public "getBooleanList"(): (boolean)[]
public "isLongValue"(): boolean
public "getDoubleList"(): (double)[]
public "getLanguageKey"(): string
public "wasRead"(): boolean
public "getValidValues"(): (string)[]
public "setRequiresMcRestart"(arg0: boolean): $Property
public "setIsListLengthFixed"(arg0: boolean): $Property
public "setRequiresWorldRestart"(arg0: boolean): $Property
public "setValidationPattern"(arg0: $Pattern$Type): $Property
public "requiresWorldRestart"(): boolean
public "setToDefault"(): $Property
public "getValidationPattern"(): $Pattern
get "name"(): string
get "boolean"(): boolean
get "int"(): integer
get "long"(): long
get "double"(): double
get "default"(): string
set "name"(value: string)
set "value"(value: boolean)
set "value"(value: string)
set "value"(value: double)
set "value"(value: integer)
get "type"(): $Property$Type
get "default"(): boolean
get "string"(): string
get "intValue"(): boolean
set "comment"(value: string)
get "comment"(): string
set "values"(value: (integer)[])
set "values"(value: (boolean)[])
set "values"(value: (string)[])
set "values"(value: (double)[])
get "stringList"(): (string)[]
get "listLengthFixed"(): boolean
set "minValue"(value: integer)
set "minValue"(value: double)
set "maxValue"(value: double)
set "maxValue"(value: integer)
set "defaultValue"(value: boolean)
set "defaultValue"(value: string)
set "defaultValue"(value: double)
set "defaultValue"(value: integer)
set "defaultValues"(value: (boolean)[])
set "defaultValues"(value: (double)[])
set "defaultValues"(value: (integer)[])
set "defaultValues"(value: (string)[])
get "intList"(): boolean
get "booleanValue"(): boolean
set "maxListLength"(value: integer)
get "booleanList"(): boolean
get "doubleValue"(): boolean
set "validValues"(value: (string)[])
get "doubleList"(): boolean
set "languageKey"(value: string)
get "minValue"(): string
get "maxValue"(): string
get "defaults"(): (string)[]
get "list"(): boolean
get "intList"(): (integer)[]
get "maxListLength"(): integer
get "booleanList"(): (boolean)[]
get "longValue"(): boolean
get "doubleList"(): (double)[]
get "languageKey"(): string
get "validValues"(): (string)[]
set "validationPattern"(value: $Pattern$Type)
get "validationPattern"(): $Pattern
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Property$Type = ($Property);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Property_ = $Property$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/mobfx/$FlyingEffect" {
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"

export class $FlyingEffect extends $MobEffect {

constructor()

public "getAttributeModifierValue"(arg0: integer, arg1: $AttributeModifier$Type): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlyingEffect$Type = ($FlyingEffect);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlyingEffect_ = $FlyingEffect$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/commands/$PlaceboCommand" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandBuildContext, $CommandBuildContext$Type} from "packages/net/minecraft/commands/$CommandBuildContext"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export class $PlaceboCommand {

constructor()

public static "register"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>, arg1: $CommandBuildContext$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceboCommand$Type = ($PlaceboCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceboCommand_ = $PlaceboCommand$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/api/client/$GatherSkippedAttributeTooltipsEvent" {
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$PlayerEvent, $PlayerEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $GatherSkippedAttributeTooltipsEvent extends $PlayerEvent {

constructor(arg0: $ItemStack$Type, arg1: $Player$Type, arg2: $Set$Type<($UUID$Type)>, arg3: $TooltipFlag$Type)
constructor()

public "getStack"(): $ItemStack
public "getFlags"(): $TooltipFlag
public "getListenerList"(): $ListenerList
public "skipUUID"(arg0: $UUID$Type): void
get "stack"(): $ItemStack
get "flags"(): $TooltipFlag
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GatherSkippedAttributeTooltipsEvent$Type = ($GatherSkippedAttributeTooltipsEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GatherSkippedAttributeTooltipsEvent_ = $GatherSkippedAttributeTooltipsEvent$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/reload/$WeightedDynamicRegistry$ILuckyWeighted" {
import {$WeightedEntry$Wrapper, $WeightedEntry$Wrapper$Type} from "packages/net/minecraft/util/random/$WeightedEntry$Wrapper"

export interface $WeightedDynamicRegistry$ILuckyWeighted {

 "wrap"<T extends $WeightedDynamicRegistry$ILuckyWeighted>(arg0: float): $WeightedEntry$Wrapper<(T)>
 "getQuality"(): float
 "getWeight"(): integer
}

export namespace $WeightedDynamicRegistry$ILuckyWeighted {
function wrap<T>(arg0: T, arg1: float): $WeightedEntry$Wrapper<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WeightedDynamicRegistry$ILuckyWeighted$Type = ($WeightedDynamicRegistry$ILuckyWeighted);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WeightedDynamicRegistry$ILuckyWeighted_ = $WeightedDynamicRegistry$ILuckyWeighted$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/config/$ConfigCategory" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$CharMatcher, $CharMatcher$Type} from "packages/com/google/common/base/$CharMatcher"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Property, $Property$Type} from "packages/dev/shadowsoffire/placebo/config/$Property"
import {$BufferedWriter, $BufferedWriter$Type} from "packages/java/io/$BufferedWriter"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ConfigCategory implements $Map<(string), ($Property)> {
static readonly "COMMENT_SEPARATOR": string
static readonly "NEW_LINE": string
static readonly "ALLOWED_CHARS": string
static readonly "allowedProperties": $CharMatcher
readonly "parent": $ConfigCategory

constructor(arg0: string)
constructor(arg0: string, arg1: $ConfigCategory$Type)

public "getName"(): string
public "remove"(arg0: any): $Property
public "get"(arg0: string): $Property
public "get"(arg0: any): $Property
public "put"(arg0: string, arg1: $Property$Type): $Property
public "equals"(arg0: any): boolean
public "values"(): $Collection<($Property)>
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "entrySet"(): $Set<($Map$Entry<(string), ($Property)>)>
public "putAll"(arg0: $Map$Type<(any), (any)>): void
public "write"(arg0: $BufferedWriter$Type, arg1: integer): void
public "containsKey"(arg0: string): boolean
public "containsKey"(arg0: any): boolean
public "keySet"(): $Set<(string)>
public "containsValue"(arg0: any): boolean
public "setComment"(arg0: string): void
public "getComment"(): string
public "getChildren"(): $Set<($ConfigCategory)>
public static "getQualifiedName"(arg0: string, arg1: $ConfigCategory$Type): string
public "getQualifiedName"(): string
public "hasChanged"(): boolean
public "removeChild"(arg0: $ConfigCategory$Type): void
public "requiresMcRestart"(): boolean
public "setPropertyOrder"(arg0: $List$Type<(string)>): $ConfigCategory
public "setLanguageKey"(arg0: string): $ConfigCategory
public "getFirstParent"(): $ConfigCategory
public "setShowInGui"(arg0: boolean): $ConfigCategory
public "getPropertyOrder"(): $List<(string)>
public "showInGui"(): boolean
public "getOrderedValues"(): $List<($Property)>
public "getLanguagekey"(): string
public "setRequiresMcRestart"(arg0: boolean): $ConfigCategory
public "setRequiresWorldRestart"(arg0: boolean): $ConfigCategory
public "requiresWorldRestart"(): boolean
public "isChild"(): boolean
public "getValues"(): $Map<(string), ($Property)>
public "remove"(arg0: any, arg1: any): boolean
public "hashCode"(): integer
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(string), ($Property)>
public "replace"(arg0: string, arg1: $Property$Type): $Property
public "replace"(arg0: string, arg1: $Property$Type, arg2: $Property$Type): boolean
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public static "of"<K, V>(arg0: string, arg1: $Property$Type, arg2: string, arg3: $Property$Type, arg4: string, arg5: $Property$Type, arg6: string, arg7: $Property$Type, arg8: string, arg9: $Property$Type): $Map<(string), ($Property)>
public static "of"<K, V>(arg0: string, arg1: $Property$Type, arg2: string, arg3: $Property$Type, arg4: string, arg5: $Property$Type, arg6: string, arg7: $Property$Type): $Map<(string), ($Property)>
public static "of"<K, V>(arg0: string, arg1: $Property$Type, arg2: string, arg3: $Property$Type, arg4: string, arg5: $Property$Type): $Map<(string), ($Property)>
public static "of"<K, V>(): $Map<(string), ($Property)>
public static "of"<K, V>(arg0: string, arg1: $Property$Type, arg2: string, arg3: $Property$Type): $Map<(string), ($Property)>
public static "of"<K, V>(arg0: string, arg1: $Property$Type): $Map<(string), ($Property)>
public static "of"<K, V>(arg0: string, arg1: $Property$Type, arg2: string, arg3: $Property$Type, arg4: string, arg5: $Property$Type, arg6: string, arg7: $Property$Type, arg8: string, arg9: $Property$Type, arg10: string, arg11: $Property$Type, arg12: string, arg13: $Property$Type, arg14: string, arg15: $Property$Type, arg16: string, arg17: $Property$Type, arg18: string, arg19: $Property$Type): $Map<(string), ($Property)>
public static "of"<K, V>(arg0: string, arg1: $Property$Type, arg2: string, arg3: $Property$Type, arg4: string, arg5: $Property$Type, arg6: string, arg7: $Property$Type, arg8: string, arg9: $Property$Type, arg10: string, arg11: $Property$Type, arg12: string, arg13: $Property$Type, arg14: string, arg15: $Property$Type, arg16: string, arg17: $Property$Type): $Map<(string), ($Property)>
public static "of"<K, V>(arg0: string, arg1: $Property$Type, arg2: string, arg3: $Property$Type, arg4: string, arg5: $Property$Type, arg6: string, arg7: $Property$Type, arg8: string, arg9: $Property$Type, arg10: string, arg11: $Property$Type, arg12: string, arg13: $Property$Type, arg14: string, arg15: $Property$Type): $Map<(string), ($Property)>
public static "of"<K, V>(arg0: string, arg1: $Property$Type, arg2: string, arg3: $Property$Type, arg4: string, arg5: $Property$Type, arg6: string, arg7: $Property$Type, arg8: string, arg9: $Property$Type, arg10: string, arg11: $Property$Type, arg12: string, arg13: $Property$Type): $Map<(string), ($Property)>
public static "of"<K, V>(arg0: string, arg1: $Property$Type, arg2: string, arg3: $Property$Type, arg4: string, arg5: $Property$Type, arg6: string, arg7: $Property$Type, arg8: string, arg9: $Property$Type, arg10: string, arg11: $Property$Type): $Map<(string), ($Property)>
public "merge"(arg0: string, arg1: $Property$Type, arg2: $BiFunction$Type<(any), (any), (any)>): $Property
public "putIfAbsent"(arg0: string, arg1: $Property$Type): $Property
public "compute"(arg0: string, arg1: $BiFunction$Type<(any), (any), (any)>): $Property
public static "entry"<K, V>(arg0: string, arg1: $Property$Type): $Map$Entry<(string), ($Property)>
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "computeIfAbsent"(arg0: string, arg1: $Function$Type<(any), (any)>): $Property
public "getOrDefault"(arg0: any, arg1: $Property$Type): $Property
public "computeIfPresent"(arg0: string, arg1: $BiFunction$Type<(any), (any), (any)>): $Property
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(string), ($Property)>
[index: string | number]: $Property
get "name"(): string
get "empty"(): boolean
set "comment"(value: string)
get "comment"(): string
get "children"(): $Set<($ConfigCategory)>
get "qualifiedName"(): string
set "propertyOrder"(value: $List$Type<(string)>)
set "languageKey"(value: string)
get "firstParent"(): $ConfigCategory
get "propertyOrder"(): $List<(string)>
get "orderedValues"(): $List<($Property)>
get "languagekey"(): string
get "child"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigCategory$Type = ($ConfigCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigCategory_ = $ConfigCategory$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/patreon/wings/$IWingModel" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$PlayerModel, $PlayerModel$Type} from "packages/net/minecraft/client/model/$PlayerModel"

export interface $IWingModel {

 "render"(arg0: $PoseStack$Type, arg1: $MultiBufferSource$Type, arg2: integer, arg3: $AbstractClientPlayer$Type, arg4: float, arg5: $ResourceLocation$Type, arg6: $PlayerModel$Type<($AbstractClientPlayer$Type)>): void

(arg0: $PoseStack$Type, arg1: $MultiBufferSource$Type, arg2: integer, arg3: $AbstractClientPlayer$Type, arg4: float, arg5: $ResourceLocation$Type, arg6: $PlayerModel$Type<($AbstractClientPlayer$Type)>): void
}

export namespace $IWingModel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IWingModel$Type = ($IWingModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IWingModel_ = $IWingModel$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/screen/$PlaceboContainerScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"
import {$MenuAccess, $MenuAccess$Type} from "packages/net/minecraft/client/gui/screens/inventory/$MenuAccess"

export class $PlaceboContainerScreen<T extends $AbstractContainerMenu> extends $AbstractContainerScreen<(T)> implements $MenuAccess<(T)> {
static readonly "INVENTORY_LOCATION": $ResourceLocation
static readonly "SLOT_ITEM_BLIT_OFFSET": integer
 "imageWidth": integer
 "hoveredSlot": $Slot
 "leftPos": integer
 "topPos": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: T, arg1: $Inventory$Type, arg2: $Component$Type)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "getMenu"(): T
get "menu"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceboContainerScreen$Type<T> = ($PlaceboContainerScreen<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceboContainerScreen_<T> = $PlaceboContainerScreen$Type<(T)>;
}}
declare module "packages/dev/shadowsoffire/placebo/menu/$LockedSlot" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export class $LockedSlot extends $Slot {
readonly "container": $Container
 "index": integer
 "x": integer
 "y": integer

constructor(arg0: $Inventory$Type, arg1: integer, arg2: integer, arg3: integer)

public "mayPlace"(arg0: $ItemStack$Type): boolean
public "mayPickup"(arg0: $Player$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LockedSlot$Type = ($LockedSlot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LockedSlot_ = $LockedSlot$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/codec/$IngredientCodec" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$UnboundedMapCodec, $UnboundedMapCodec$Type} from "packages/com/mojang/serialization/codecs/$UnboundedMapCodec"
import {$MapEncoder, $MapEncoder$Type} from "packages/com/mojang/serialization/$MapEncoder"
import {$DynamicOps, $DynamicOps$Type} from "packages/com/mojang/serialization/$DynamicOps"
import {$Encoder, $Encoder$Type} from "packages/com/mojang/serialization/$Encoder"
import {$Codec$ResultFunction, $Codec$ResultFunction$Type} from "packages/com/mojang/serialization/$Codec$ResultFunction"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Decoder, $Decoder$Type} from "packages/com/mojang/serialization/$Decoder"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Lifecycle, $Lifecycle$Type} from "packages/com/mojang/serialization/$Lifecycle"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Decoder$Boxed, $Decoder$Boxed$Type} from "packages/com/mojang/serialization/$Decoder$Boxed"
import {$MapDecoder, $MapDecoder$Type} from "packages/com/mojang/serialization/$MapDecoder"
import {$Dynamic, $Dynamic$Type} from "packages/com/mojang/serialization/$Dynamic"
import {$DataResult, $DataResult$Type} from "packages/com/mojang/serialization/$DataResult"
import {$Decoder$Simple, $Decoder$Simple$Type} from "packages/com/mojang/serialization/$Decoder$Simple"
import {$SimpleMapCodec, $SimpleMapCodec$Type} from "packages/com/mojang/serialization/codecs/$SimpleMapCodec"
import {$Decoder$Terminal, $Decoder$Terminal$Type} from "packages/com/mojang/serialization/$Decoder$Terminal"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"

export class $IngredientCodec implements $Codec<($Ingredient)> {
static "INSTANCE": $IngredientCodec

constructor()

public "decode"<T>(arg0: $DynamicOps$Type<(T)>, arg1: T): $DataResult<($Pair<($Ingredient), (T)>)>
public "encode"<T>(arg0: $Ingredient$Type, arg1: $DynamicOps$Type<(T)>, arg2: T): $DataResult<(T)>
public "dispatch"<E>(arg0: string, arg1: $Function$Type<(any), (any)>, arg2: $Function$Type<(any), (any)>): $Codec<(E)>
public "dispatch"<E>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(E)>
public static "of"<A>(arg0: $MapEncoder$Type<($Ingredient$Type)>, arg1: $MapDecoder$Type<($Ingredient$Type)>, arg2: $Supplier$Type<(string)>): $MapCodec<($Ingredient)>
public static "of"<A>(arg0: $MapEncoder$Type<($Ingredient$Type)>, arg1: $MapDecoder$Type<($Ingredient$Type)>): $MapCodec<($Ingredient)>
public static "of"<A>(arg0: $Encoder$Type<($Ingredient$Type)>, arg1: $Decoder$Type<($Ingredient$Type)>, arg2: string): $Codec<($Ingredient)>
public static "of"<A>(arg0: $Encoder$Type<($Ingredient$Type)>, arg1: $Decoder$Type<($Ingredient$Type)>): $Codec<($Ingredient)>
public static "list"<E>(arg0: $Codec$Type<(E)>): $Codec<($List<(E)>)>
public "orElse"(arg0: $Ingredient$Type): $Codec<($Ingredient)>
public "orElse"(arg0: $UnaryOperator$Type<(string)>, arg1: $Ingredient$Type): $Codec<($Ingredient)>
public "orElse"(arg0: $Consumer$Type<(string)>, arg1: $Ingredient$Type): $Codec<($Ingredient)>
public static "checkRange"<N extends (number) & ($Comparable<(N)>)>(arg0: N, arg1: N): $Function<(N), ($DataResult<(N)>)>
public static "unit"<A>(arg0: $Ingredient$Type): $Codec<($Ingredient)>
public static "unit"<A>(arg0: $Supplier$Type<($Ingredient$Type)>): $Codec<($Ingredient)>
public "orElseGet"(arg0: $Supplier$Type<(any)>): $Codec<($Ingredient)>
public "orElseGet"(arg0: $Consumer$Type<(string)>, arg1: $Supplier$Type<(any)>): $Codec<($Ingredient)>
public "orElseGet"(arg0: $UnaryOperator$Type<(string)>, arg1: $Supplier$Type<(any)>): $Codec<($Ingredient)>
public static "pair"<F, S>(arg0: $Codec$Type<(F)>, arg1: $Codec$Type<(S)>): $Codec<($Pair<(F), (S)>)>
public "xmap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public static "optionalField"<F>(arg0: string, arg1: $Codec$Type<(F)>): $MapCodec<($Optional<(F)>)>
public "deprecated"(arg0: integer): $Codec<($Ingredient)>
public "withLifecycle"(arg0: $Lifecycle$Type): $Codec<($Ingredient)>
public "optionalFieldOf"(arg0: string): $MapCodec<($Optional<($Ingredient)>)>
public "optionalFieldOf"(arg0: string, arg1: $Ingredient$Type, arg2: $Lifecycle$Type): $MapCodec<($Ingredient)>
public "optionalFieldOf"(arg0: string, arg1: $Lifecycle$Type, arg2: $Ingredient$Type, arg3: $Lifecycle$Type): $MapCodec<($Ingredient)>
public "optionalFieldOf"(arg0: string, arg1: $Ingredient$Type): $MapCodec<($Ingredient)>
public "mapResult"(arg0: $Codec$ResultFunction$Type<($Ingredient$Type)>): $Codec<($Ingredient)>
public "flatXmap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public "promotePartial"(arg0: $Consumer$Type<(string)>): $Codec<($Ingredient)>
public "partialDispatch"<E>(arg0: string, arg1: $Function$Type<(any), (any)>, arg2: $Function$Type<(any), (any)>): $Codec<(E)>
public "dispatchMap"<E>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $MapCodec<(E)>
public "dispatchMap"<E>(arg0: string, arg1: $Function$Type<(any), (any)>, arg2: $Function$Type<(any), (any)>): $MapCodec<(E)>
public static "compoundList"<K, V>(arg0: $Codec$Type<(K)>, arg1: $Codec$Type<(V)>): $Codec<($List<($Pair<(K), (V)>)>)>
public static "either"<F, S>(arg0: $Codec$Type<(F)>, arg1: $Codec$Type<(S)>): $Codec<($Either<(F), (S)>)>
public static "mapPair"<F, S>(arg0: $MapCodec$Type<(F)>, arg1: $MapCodec$Type<(S)>): $MapCodec<($Pair<(F), (S)>)>
public static "mapEither"<F, S>(arg0: $MapCodec$Type<(F)>, arg1: $MapCodec$Type<(S)>): $MapCodec<($Either<(F), (S)>)>
public "dispatchStable"<E>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(E)>
public "comapFlatMap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public static "unboundedMap"<K, V>(arg0: $Codec$Type<(K)>, arg1: $Codec$Type<(V)>): $UnboundedMapCodec<(K), (V)>
public "flatComapMap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public static "simpleMap"<K, V>(arg0: $Codec$Type<(K)>, arg1: $Codec$Type<(V)>, arg2: $Keyable$Type): $SimpleMapCodec<(K), (V)>
public static "doubleRange"(arg0: double, arg1: double): $Codec<(double)>
public static "floatRange"(arg0: float, arg1: float): $Codec<(float)>
public static "intRange"(arg0: integer, arg1: integer): $Codec<(integer)>
public "listOf"(): $Codec<($List<($Ingredient)>)>
public "stable"(): $Codec<($Ingredient)>
public static "empty"<A>(): $MapEncoder<($Ingredient)>
public static "error"<A>(arg0: string): $Encoder<($Ingredient)>
public "flatComap"<B>(arg0: $Function$Type<(any), (any)>): $Encoder<(B)>
public "comap"<B>(arg0: $Function$Type<(any), (any)>): $Encoder<(B)>
public "encodeStart"<T>(arg0: $DynamicOps$Type<(T)>, arg1: $Ingredient$Type): $DataResult<(T)>
public "decode"<T>(arg0: $Dynamic$Type<(T)>): $DataResult<($Pair<($Ingredient), (T)>)>
public "map"<B>(arg0: $Function$Type<(any), (any)>): $Decoder<(B)>
public "flatMap"<B>(arg0: $Function$Type<(any), (any)>): $Decoder<(B)>
public "parse"<T>(arg0: $Dynamic$Type<(T)>): $DataResult<($Ingredient)>
public "parse"<T>(arg0: $DynamicOps$Type<(T)>, arg1: T): $DataResult<($Ingredient)>
public "boxed"(): $Decoder$Boxed<($Ingredient)>
public "simple"(): $Decoder$Simple<($Ingredient)>
public static "ofBoxed"<A>(arg0: $Decoder$Boxed$Type<(any)>): $Decoder<($Ingredient)>
public static "ofTerminal"<A>(arg0: $Decoder$Terminal$Type<(any)>): $Decoder<($Ingredient)>
public static "ofSimple"<A>(arg0: $Decoder$Simple$Type<(any)>): $Decoder<($Ingredient)>
public "terminal"(): $Decoder$Terminal<($Ingredient)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientCodec$Type = ($IngredientCodec);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientCodec_ = $IngredientCodec$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/config/$ConfigGuiType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ConfigGuiType extends $Enum<($ConfigGuiType)> {
static readonly "STRING": $ConfigGuiType
static readonly "INTEGER": $ConfigGuiType
static readonly "BOOLEAN": $ConfigGuiType
static readonly "DOUBLE": $ConfigGuiType
static readonly "COLOR": $ConfigGuiType
static readonly "MOD_ID": $ConfigGuiType
static readonly "CONFIG_CATEGORY": $ConfigGuiType


public static "values"(): ($ConfigGuiType)[]
public static "valueOf"(arg0: string): $ConfigGuiType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigGuiType$Type = (("config_category") | ("boolean") | ("mod_id") | ("string") | ("color") | ("double") | ("integer")) | ($ConfigGuiType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigGuiType_ = $ConfigGuiType$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/api/$ALCombatRules" {
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $ALCombatRules {

constructor()

public static "getDamageAfterArmor"(arg0: $LivingEntity$Type, arg1: $DamageSource$Type, arg2: float, arg3: float, arg4: float): float
public static "getDamageAfterProtection"(arg0: $LivingEntity$Type, arg1: $DamageSource$Type, arg2: float, arg3: float): float
public static "getArmorDamageReduction"(arg0: float, arg1: float): float
public static "getProtDamageReduction"(arg0: float): float
public static "getAValue"(arg0: float): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ALCombatRules$Type = ($ALCombatRules);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ALCombatRules_ = $ALCombatRules$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/patreon/$TrailsManager" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$InputEvent$Key, $InputEvent$Key$Type} from "packages/net/minecraftforge/client/event/$InputEvent$Key"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$TickEvent$ClientTickEvent, $TickEvent$ClientTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ClientTickEvent"

export class $TrailsManager {
static readonly "TOGGLE": $KeyMapping
static readonly "DISABLED": $Set<($UUID)>

constructor()

public static "init"(): void
public static "keys"(arg0: $InputEvent$Key$Type): void
public static "clientTick"(arg0: $TickEvent$ClientTickEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TrailsManager$Type = ($TrailsManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TrailsManager_ = $TrailsManager$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/codec/$MapBackedCodec" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$UnboundedMapCodec, $UnboundedMapCodec$Type} from "packages/com/mojang/serialization/codecs/$UnboundedMapCodec"
import {$MapEncoder, $MapEncoder$Type} from "packages/com/mojang/serialization/$MapEncoder"
import {$DynamicOps, $DynamicOps$Type} from "packages/com/mojang/serialization/$DynamicOps"
import {$Encoder, $Encoder$Type} from "packages/com/mojang/serialization/$Encoder"
import {$Codec$ResultFunction, $Codec$ResultFunction$Type} from "packages/com/mojang/serialization/$Codec$ResultFunction"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Decoder, $Decoder$Type} from "packages/com/mojang/serialization/$Decoder"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Lifecycle, $Lifecycle$Type} from "packages/com/mojang/serialization/$Lifecycle"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$BiMap, $BiMap$Type} from "packages/com/google/common/collect/$BiMap"
import {$Decoder$Boxed, $Decoder$Boxed$Type} from "packages/com/mojang/serialization/$Decoder$Boxed"
import {$MapDecoder, $MapDecoder$Type} from "packages/com/mojang/serialization/$MapDecoder"
import {$Dynamic, $Dynamic$Type} from "packages/com/mojang/serialization/$Dynamic"
import {$DataResult, $DataResult$Type} from "packages/com/mojang/serialization/$DataResult"
import {$Decoder$Simple, $Decoder$Simple$Type} from "packages/com/mojang/serialization/$Decoder$Simple"
import {$SimpleMapCodec, $SimpleMapCodec$Type} from "packages/com/mojang/serialization/codecs/$SimpleMapCodec"
import {$Decoder$Terminal, $Decoder$Terminal$Type} from "packages/com/mojang/serialization/$Decoder$Terminal"
import {$CodecProvider, $CodecProvider$Type} from "packages/dev/shadowsoffire/placebo/codec/$CodecProvider"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"

export class $MapBackedCodec<V extends $CodecProvider<(any)>> implements $Codec<(V)> {

constructor(arg0: string, arg1: $BiMap$Type<($ResourceLocation$Type), ($Codec$Type<(any)>)>, arg2: $Supplier$Type<($Codec$Type<(any)>)>)
constructor(arg0: string, arg1: $BiMap$Type<($ResourceLocation$Type), ($Codec$Type<(any)>)>)

public "decode"<T>(arg0: $DynamicOps$Type<(T)>, arg1: T): $DataResult<($Pair<(V), (T)>)>
public "encode"<T>(arg0: V, arg1: $DynamicOps$Type<(T)>, arg2: T): $DataResult<(T)>
public "dispatch"<E>(arg0: string, arg1: $Function$Type<(any), (any)>, arg2: $Function$Type<(any), (any)>): $Codec<(E)>
public "dispatch"<E>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(E)>
public static "of"<A>(arg0: $MapEncoder$Type<(V)>, arg1: $MapDecoder$Type<(V)>, arg2: $Supplier$Type<(string)>): $MapCodec<(V)>
public static "of"<A>(arg0: $MapEncoder$Type<(V)>, arg1: $MapDecoder$Type<(V)>): $MapCodec<(V)>
public static "of"<A>(arg0: $Encoder$Type<(V)>, arg1: $Decoder$Type<(V)>, arg2: string): $Codec<(V)>
public static "of"<A>(arg0: $Encoder$Type<(V)>, arg1: $Decoder$Type<(V)>): $Codec<(V)>
public static "list"<E>(arg0: $Codec$Type<(E)>): $Codec<($List<(E)>)>
public "orElse"(arg0: V): $Codec<(V)>
public "orElse"(arg0: $UnaryOperator$Type<(string)>, arg1: V): $Codec<(V)>
public "orElse"(arg0: $Consumer$Type<(string)>, arg1: V): $Codec<(V)>
public static "checkRange"<N extends (number) & ($Comparable<(N)>)>(arg0: N, arg1: N): $Function<(N), ($DataResult<(N)>)>
public static "unit"<A>(arg0: V): $Codec<(V)>
public static "unit"<A>(arg0: $Supplier$Type<(V)>): $Codec<(V)>
public "orElseGet"(arg0: $Supplier$Type<(any)>): $Codec<(V)>
public "orElseGet"(arg0: $Consumer$Type<(string)>, arg1: $Supplier$Type<(any)>): $Codec<(V)>
public "orElseGet"(arg0: $UnaryOperator$Type<(string)>, arg1: $Supplier$Type<(any)>): $Codec<(V)>
public static "pair"<F, S>(arg0: $Codec$Type<(F)>, arg1: $Codec$Type<(S)>): $Codec<($Pair<(F), (S)>)>
public "xmap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public static "optionalField"<F>(arg0: string, arg1: $Codec$Type<(F)>): $MapCodec<($Optional<(F)>)>
public "deprecated"(arg0: integer): $Codec<(V)>
public "withLifecycle"(arg0: $Lifecycle$Type): $Codec<(V)>
public "optionalFieldOf"(arg0: string): $MapCodec<($Optional<(V)>)>
public "optionalFieldOf"(arg0: string, arg1: V, arg2: $Lifecycle$Type): $MapCodec<(V)>
public "optionalFieldOf"(arg0: string, arg1: $Lifecycle$Type, arg2: V, arg3: $Lifecycle$Type): $MapCodec<(V)>
public "optionalFieldOf"(arg0: string, arg1: V): $MapCodec<(V)>
public "mapResult"(arg0: $Codec$ResultFunction$Type<(V)>): $Codec<(V)>
public "flatXmap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public "promotePartial"(arg0: $Consumer$Type<(string)>): $Codec<(V)>
public "partialDispatch"<E>(arg0: string, arg1: $Function$Type<(any), (any)>, arg2: $Function$Type<(any), (any)>): $Codec<(E)>
public "dispatchMap"<E>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $MapCodec<(E)>
public "dispatchMap"<E>(arg0: string, arg1: $Function$Type<(any), (any)>, arg2: $Function$Type<(any), (any)>): $MapCodec<(E)>
public static "compoundList"<K, V>(arg0: $Codec$Type<(K)>, arg1: $Codec$Type<(V)>): $Codec<($List<($Pair<(K), (V)>)>)>
public static "either"<F, S>(arg0: $Codec$Type<(F)>, arg1: $Codec$Type<(S)>): $Codec<($Either<(F), (S)>)>
public static "mapPair"<F, S>(arg0: $MapCodec$Type<(F)>, arg1: $MapCodec$Type<(S)>): $MapCodec<($Pair<(F), (S)>)>
public static "mapEither"<F, S>(arg0: $MapCodec$Type<(F)>, arg1: $MapCodec$Type<(S)>): $MapCodec<($Either<(F), (S)>)>
public "dispatchStable"<E>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(E)>
public "comapFlatMap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public static "unboundedMap"<K, V>(arg0: $Codec$Type<(K)>, arg1: $Codec$Type<(V)>): $UnboundedMapCodec<(K), (V)>
public "flatComapMap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public static "simpleMap"<K, V>(arg0: $Codec$Type<(K)>, arg1: $Codec$Type<(V)>, arg2: $Keyable$Type): $SimpleMapCodec<(K), (V)>
public static "doubleRange"(arg0: double, arg1: double): $Codec<(double)>
public static "floatRange"(arg0: float, arg1: float): $Codec<(float)>
public static "intRange"(arg0: integer, arg1: integer): $Codec<(integer)>
public "listOf"(): $Codec<($List<(V)>)>
public "stable"(): $Codec<(V)>
public static "empty"<A>(): $MapEncoder<(V)>
public static "error"<A>(arg0: string): $Encoder<(V)>
public "flatComap"<B>(arg0: $Function$Type<(any), (any)>): $Encoder<(B)>
public "comap"<B>(arg0: $Function$Type<(any), (any)>): $Encoder<(B)>
public "encodeStart"<T>(arg0: $DynamicOps$Type<(T)>, arg1: V): $DataResult<(T)>
public "decode"<T>(arg0: $Dynamic$Type<(T)>): $DataResult<($Pair<(V), (T)>)>
public "map"<B>(arg0: $Function$Type<(any), (any)>): $Decoder<(B)>
public "flatMap"<B>(arg0: $Function$Type<(any), (any)>): $Decoder<(B)>
public "parse"<T>(arg0: $Dynamic$Type<(T)>): $DataResult<(V)>
public "parse"<T>(arg0: $DynamicOps$Type<(T)>, arg1: T): $DataResult<(V)>
public "boxed"(): $Decoder$Boxed<(V)>
public "simple"(): $Decoder$Simple<(V)>
public static "ofBoxed"<A>(arg0: $Decoder$Boxed$Type<(any)>): $Decoder<(V)>
public static "ofTerminal"<A>(arg0: $Decoder$Terminal$Type<(any)>): $Decoder<(V)>
public static "ofSimple"<A>(arg0: $Decoder$Simple$Type<(any)>): $Decoder<(V)>
public "terminal"(): $Decoder$Terminal<(V)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapBackedCodec$Type<V> = ($MapBackedCodec<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapBackedCodec_<V> = $MapBackedCodec$Type<(V)>;
}}
declare module "packages/dev/shadowsoffire/placebo/menu/$QuickMoveHandler" {
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$QuickMoveHandler$IExposedContainer, $QuickMoveHandler$IExposedContainer$Type} from "packages/dev/shadowsoffire/placebo/menu/$QuickMoveHandler$IExposedContainer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $QuickMoveHandler {

constructor()

public "quickMoveStack"(arg0: $QuickMoveHandler$IExposedContainer$Type, arg1: $Player$Type, arg2: integer): $ItemStack
public "registerRule"(arg0: $BiPredicate$Type<($ItemStack$Type), (integer)>, arg1: integer, arg2: integer): void
public "registerRule"(arg0: $BiPredicate$Type<($ItemStack$Type), (integer)>, arg1: integer, arg2: integer, arg3: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QuickMoveHandler$Type = ($QuickMoveHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QuickMoveHandler_ = $QuickMoveHandler$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/util/$PlaceboTaskQueue" {
import {$BooleanSupplier, $BooleanSupplier$Type} from "packages/java/util/function/$BooleanSupplier"
import {$ServerStartedEvent, $ServerStartedEvent$Type} from "packages/net/minecraftforge/event/server/$ServerStartedEvent"
import {$TickEvent$ServerTickEvent, $TickEvent$ServerTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ServerTickEvent"
import {$ServerStoppedEvent, $ServerStoppedEvent$Type} from "packages/net/minecraftforge/event/server/$ServerStoppedEvent"

export class $PlaceboTaskQueue {

constructor()

public static "started"(arg0: $ServerStartedEvent$Type): void
public static "tick"(arg0: $TickEvent$ServerTickEvent$Type): void
public static "submitTask"(arg0: string, arg1: $BooleanSupplier$Type): void
public static "stopped"(arg0: $ServerStoppedEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceboTaskQueue$Type = ($PlaceboTaskQueue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceboTaskQueue_ = $PlaceboTaskQueue$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/mobfx/$GrievousEffect" {
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"

export class $GrievousEffect extends $MobEffect {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GrievousEffect$Type = ($GrievousEffect);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GrievousEffect_ = $GrievousEffect$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/json/$GearSet" {
import {$WeightedItemStack, $WeightedItemStack$Type} from "packages/dev/shadowsoffire/placebo/json/$WeightedItemStack"
import {$WeightedDynamicRegistry$ILuckyWeighted, $WeightedDynamicRegistry$ILuckyWeighted$Type} from "packages/dev/shadowsoffire/placebo/reload/$WeightedDynamicRegistry$ILuckyWeighted"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$List, $List$Type} from "packages/java/util/$List"
import {$CodecProvider, $CodecProvider$Type} from "packages/dev/shadowsoffire/placebo/codec/$CodecProvider"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$WeightedEntry$Wrapper, $WeightedEntry$Wrapper$Type} from "packages/net/minecraft/util/random/$WeightedEntry$Wrapper"

export class $GearSet extends $Record implements $CodecProvider<($GearSet)>, $WeightedDynamicRegistry$ILuckyWeighted {
static readonly "CODEC": $Codec<($GearSet)>

constructor(weight: integer, quality: float, mainhands: $List$Type<($WeightedItemStack$Type)>, offhands: $List$Type<($WeightedItemStack$Type)>, boots: $List$Type<($WeightedItemStack$Type)>, leggings: $List$Type<($WeightedItemStack$Type)>, chestplates: $List$Type<($WeightedItemStack$Type)>, helmets: $List$Type<($WeightedItemStack$Type)>, tags: $List$Type<(string)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "apply"(arg0: $LivingEntity$Type): $LivingEntity
public "tags"(): $List<(string)>
public "getQuality"(): float
public "mainhands"(): $List<($WeightedItemStack)>
public "chestplates"(): $List<($WeightedItemStack)>
public "helmets"(): $List<($WeightedItemStack)>
public "offhands"(): $List<($WeightedItemStack)>
public "getPotentials"(arg0: $EquipmentSlot$Type): $List<($WeightedItemStack)>
public "quality"(): float
public "boots"(): $List<($WeightedItemStack)>
public "leggings"(): $List<($WeightedItemStack)>
public "getWeight"(): integer
public "weight"(): integer
public "getCodec"(): $Codec<(any)>
public static "wrap"<T extends $WeightedDynamicRegistry$ILuckyWeighted>(arg0: T, arg1: float): $WeightedEntry$Wrapper<(T)>
public "wrap"<T extends $WeightedDynamicRegistry$ILuckyWeighted>(arg0: float): $WeightedEntry$Wrapper<(T)>
get "codec"(): $Codec<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GearSet$Type = ($GearSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GearSet_ = $GearSet$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/menu/$SimpleDataSlots" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$BooleanSupplier, $BooleanSupplier$Type} from "packages/java/util/function/$BooleanSupplier"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IntSupplier, $IntSupplier$Type} from "packages/java/util/function/$IntSupplier"
import {$DataSlot, $DataSlot$Type} from "packages/net/minecraft/world/inventory/$DataSlot"
import {$IntConsumer, $IntConsumer$Type} from "packages/java/util/function/$IntConsumer"
import {$BooleanConsumer, $BooleanConsumer$Type} from "packages/it/unimi/dsi/fastutil/booleans/$BooleanConsumer"
import {$ModifiableEnergyStorage, $ModifiableEnergyStorage$Type} from "packages/dev/shadowsoffire/placebo/cap/$ModifiableEnergyStorage"

export class $SimpleDataSlots {

constructor()

public "register"(arg0: $Consumer$Type<($DataSlot$Type)>): void
public "addEnergy"(arg0: $ModifiableEnergyStorage$Type): void
public "getSlots"(): $List<($DataSlot)>
public "addSlot"(arg0: $DataSlot$Type): void
public "addData"(arg0: $IntSupplier$Type, arg1: $IntConsumer$Type): void
public "addData"(arg0: $BooleanSupplier$Type, arg1: $BooleanConsumer$Type): void
get "slots"(): $List<($DataSlot)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleDataSlots$Type = ($SimpleDataSlots);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleDataSlots_ = $SimpleDataSlots$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/block_entity/$TickingEntityBlock" {
import {$EntityBlock, $EntityBlock$Type} from "packages/net/minecraft/world/level/block/$EntityBlock"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$GameEventListener, $GameEventListener$Type} from "packages/net/minecraft/world/level/gameevent/$GameEventListener"

export interface $TickingEntityBlock extends $EntityBlock {

 "getTicker"<T extends $BlockEntity>(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockEntityType$Type<(T)>): $BlockEntityTicker<(T)>
 "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
 "getListener"<T extends $BlockEntity>(arg0: $ServerLevel$Type, arg1: T): $GameEventListener

(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockEntityType$Type<(T)>): $BlockEntityTicker<(T)>
}

export namespace $TickingEntityBlock {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickingEntityBlock$Type = ($TickingEntityBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickingEntityBlock_ = $TickingEntityBlock$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/$PlaceboClient" {
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"
import {$RegisterKeyMappingsEvent, $RegisterKeyMappingsEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterKeyMappingsEvent"
import {$TickEvent$ClientTickEvent, $TickEvent$ClientTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ClientTickEvent"
import {$TextColor, $TextColor$Type} from "packages/net/minecraft/network/chat/$TextColor"

export class $PlaceboClient {
static "ticks": long

constructor()

public static "setup"(arg0: $FMLClientSetupEvent$Type): void
public static "keys"(arg0: $RegisterKeyMappingsEvent$Type): void
public static "tick"(arg0: $TickEvent$ClientTickEvent$Type): void
public static "getColorTicks"(): float
/**
 * 
 * @deprecated
 */
public static "registerCustomColor"<T extends $TextColor>(arg0: string, arg1: T): void
set "up"(value: $FMLClientSetupEvent$Type)
get "colorTicks"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceboClient$Type = ($PlaceboClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceboClient_ = $PlaceboClient$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/menu/$QuickMoveHandler$IExposedContainer" {
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $QuickMoveHandler$IExposedContainer {

 "moveMenuItemStackTo"(arg0: $ItemStack$Type, arg1: integer, arg2: integer, arg3: boolean): boolean
 "onQuickMove"(arg0: $ItemStack$Type, arg1: $ItemStack$Type, arg2: $Slot$Type): void
 "getMenuSlot"(arg0: integer): $Slot
}

export namespace $QuickMoveHandler$IExposedContainer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QuickMoveHandler$IExposedContainer$Type = ($QuickMoveHandler$IExposedContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QuickMoveHandler$IExposedContainer_ = $QuickMoveHandler$IExposedContainer$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/commands/$HandToJsonCommand" {
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$DynamicCommandExceptionType, $DynamicCommandExceptionType$Type} from "packages/com/mojang/brigadier/exceptions/$DynamicCommandExceptionType"

export class $HandToJsonCommand {
static readonly "GSON": $Gson
static readonly "NOT_FOUND": $DynamicCommandExceptionType

constructor()

public static "register"(arg0: $LiteralArgumentBuilder$Type<($CommandSourceStack$Type)>): void
public static "toJsonStr"(arg0: $ItemStack$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HandToJsonCommand$Type = ($HandToJsonCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HandToJsonCommand_ = $HandToJsonCommand$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/menu/$PlaceboContainerMenu" {
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$QuickMoveHandler$IExposedContainer, $QuickMoveHandler$IExposedContainer$Type} from "packages/dev/shadowsoffire/placebo/menu/$QuickMoveHandler$IExposedContainer"
import {$IDataUpdateListener, $IDataUpdateListener$Type} from "packages/dev/shadowsoffire/placebo/menu/$IDataUpdateListener"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$SlotUpdateListener, $SlotUpdateListener$Type} from "packages/dev/shadowsoffire/placebo/menu/$SlotUpdateListener"

export class $PlaceboContainerMenu extends $AbstractContainerMenu implements $QuickMoveHandler$IExposedContainer {
static readonly "SLOT_CLICKED_OUTSIDE": integer
static readonly "QUICKCRAFT_TYPE_CHARITABLE": integer
static readonly "QUICKCRAFT_TYPE_GREEDY": integer
static readonly "QUICKCRAFT_TYPE_CLONE": integer
static readonly "QUICKCRAFT_HEADER_START": integer
static readonly "QUICKCRAFT_HEADER_CONTINUE": integer
static readonly "QUICKCRAFT_HEADER_END": integer
static readonly "CARRIED_SLOT_SIZE": integer
readonly "lastSlots": $NonNullList<($ItemStack)>
readonly "slots": $NonNullList<($Slot)>
 "remoteSlots": $NonNullList<($ItemStack)>
 "containerId": integer


public "quickMoveStack"(arg0: $Player$Type, arg1: integer): $ItemStack
public "setData"(arg0: integer, arg1: integer): void
public "m_38903_"(arg0: $ItemStack$Type, arg1: integer, arg2: integer, arg3: boolean): boolean
public "addSlotListener"(arg0: $SlotUpdateListener$Type): void
public "addDataListener"(arg0: $IDataUpdateListener$Type): void
public "moveMenuItemStackTo"(arg0: $ItemStack$Type, arg1: integer, arg2: integer, arg3: boolean): boolean
public "onQuickMove"(arg0: $ItemStack$Type, arg1: $ItemStack$Type, arg2: $Slot$Type): void
public "getMenuSlot"(arg0: integer): $Slot
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceboContainerMenu$Type = ($PlaceboContainerMenu);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceboContainerMenu_ = $PlaceboContainerMenu$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/patreon/$PatreonUtils" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PatreonUtils {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PatreonUtils$Type = ($PatreonUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PatreonUtils_ = $PatreonUtils$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/json/$ItemAdapter" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$JsonDeserializer, $JsonDeserializer$Type} from "packages/com/google/gson/$JsonDeserializer"
import {$JsonSerializer, $JsonSerializer$Type} from "packages/com/google/gson/$JsonSerializer"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$JsonSerializationContext, $JsonSerializationContext$Type} from "packages/com/google/gson/$JsonSerializationContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $ItemAdapter implements $JsonDeserializer<($ItemStack)>, $JsonSerializer<($ItemStack)> {
static readonly "INSTANCE": $ItemAdapter
/**
 * 
 * @deprecated
 */
static readonly "ITEM_READER": $Gson
static readonly "CODEC": $Codec<($ItemStack)>

constructor()

public static "readStack"(arg0: $JsonElement$Type): $ItemStack
public static "readStacks"(arg0: $JsonElement$Type): $List<($ItemStack)>
public "serialize"(arg0: $ItemStack$Type, arg1: $Type$Type, arg2: $JsonSerializationContext$Type): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemAdapter$Type = ($ItemAdapter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemAdapter_ = $ItemAdapter$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/cap/$InternalItemHandler" {
import {$ItemStackHandler, $ItemStackHandler$Type} from "packages/net/minecraftforge/items/$ItemStackHandler"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $InternalItemHandler extends $ItemStackHandler {

constructor(arg0: integer)

public "insertItemInternal"(arg0: integer, arg1: $ItemStack$Type, arg2: boolean): $ItemStack
public "extractItemInternal"(arg0: integer, arg1: integer, arg2: boolean): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InternalItemHandler$Type = ($InternalItemHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InternalItemHandler_ = $InternalItemHandler$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/loot/$LootSystem" {
import {$LootDataId, $LootDataId$Type} from "packages/net/minecraft/world/level/storage/loot/$LootDataId"
import {$LootTable$Builder, $LootTable$Builder$Type} from "packages/net/minecraft/world/level/storage/loot/$LootTable$Builder"
import {$LootTable, $LootTable$Type} from "packages/net/minecraft/world/level/storage/loot/$LootTable"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$PoolBuilder, $PoolBuilder$Type} from "packages/dev/shadowsoffire/placebo/loot/$PoolBuilder"

export class $LootSystem {
static readonly "PLACEBO_TABLES": $Map<($LootDataId<($LootTable)>), ($LootTable)>

constructor()

public static "defaultBlockTable"(arg0: $Block$Type): void
public static "registerLootTable"(arg0: $ResourceLocation$Type, arg1: $LootTable$Type): void
public static "tableBuilder"(): $LootTable$Builder
public static "poolBuilder"(arg0: integer, arg1: integer): $PoolBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootSystem$Type = ($LootSystem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootSystem_ = $LootSystem$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/codec/$CodecMap" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$MapCodec, $MapCodec$Type} from "packages/com/mojang/serialization/$MapCodec"
import {$UnboundedMapCodec, $UnboundedMapCodec$Type} from "packages/com/mojang/serialization/codecs/$UnboundedMapCodec"
import {$MapEncoder, $MapEncoder$Type} from "packages/com/mojang/serialization/$MapEncoder"
import {$DynamicOps, $DynamicOps$Type} from "packages/com/mojang/serialization/$DynamicOps"
import {$Encoder, $Encoder$Type} from "packages/com/mojang/serialization/$Encoder"
import {$Codec$ResultFunction, $Codec$ResultFunction$Type} from "packages/com/mojang/serialization/$Codec$ResultFunction"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Decoder, $Decoder$Type} from "packages/com/mojang/serialization/$Decoder"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Lifecycle, $Lifecycle$Type} from "packages/com/mojang/serialization/$Lifecycle"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Decoder$Boxed, $Decoder$Boxed$Type} from "packages/com/mojang/serialization/$Decoder$Boxed"
import {$MapDecoder, $MapDecoder$Type} from "packages/com/mojang/serialization/$MapDecoder"
import {$Dynamic, $Dynamic$Type} from "packages/com/mojang/serialization/$Dynamic"
import {$DataResult, $DataResult$Type} from "packages/com/mojang/serialization/$DataResult"
import {$Decoder$Simple, $Decoder$Simple$Type} from "packages/com/mojang/serialization/$Decoder$Simple"
import {$SimpleMapCodec, $SimpleMapCodec$Type} from "packages/com/mojang/serialization/codecs/$SimpleMapCodec"
import {$Decoder$Terminal, $Decoder$Terminal$Type} from "packages/com/mojang/serialization/$Decoder$Terminal"
import {$CodecProvider, $CodecProvider$Type} from "packages/dev/shadowsoffire/placebo/codec/$CodecProvider"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"

export class $CodecMap<V extends $CodecProvider<(any)>> implements $Codec<(V)> {

constructor(arg0: string)

public "decode"<T>(arg0: $DynamicOps$Type<(T)>, arg1: T): $DataResult<($Pair<(V), (T)>)>
public "encode"<T>(arg0: V, arg1: $DynamicOps$Type<(T)>, arg2: T): $DataResult<(T)>
public "getValue"(arg0: $ResourceLocation$Type): $Codec<(any)>
public "isEmpty"(): boolean
public "getKey"(arg0: $Codec$Type<(any)>): $ResourceLocation
public "register"(arg0: $ResourceLocation$Type, arg1: $Codec$Type<(any)>): void
public "containsKey"(arg0: $ResourceLocation$Type): boolean
public "getDefaultCodec"(): $Codec<(any)>
public "setDefaultCodec"(arg0: $Codec$Type<(any)>): void
public "dispatch"<E>(arg0: string, arg1: $Function$Type<(any), (any)>, arg2: $Function$Type<(any), (any)>): $Codec<(E)>
public "dispatch"<E>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(E)>
public static "of"<A>(arg0: $MapEncoder$Type<(V)>, arg1: $MapDecoder$Type<(V)>, arg2: $Supplier$Type<(string)>): $MapCodec<(V)>
public static "of"<A>(arg0: $MapEncoder$Type<(V)>, arg1: $MapDecoder$Type<(V)>): $MapCodec<(V)>
public static "of"<A>(arg0: $Encoder$Type<(V)>, arg1: $Decoder$Type<(V)>, arg2: string): $Codec<(V)>
public static "of"<A>(arg0: $Encoder$Type<(V)>, arg1: $Decoder$Type<(V)>): $Codec<(V)>
public static "list"<E>(arg0: $Codec$Type<(E)>): $Codec<($List<(E)>)>
public "orElse"(arg0: V): $Codec<(V)>
public "orElse"(arg0: $UnaryOperator$Type<(string)>, arg1: V): $Codec<(V)>
public "orElse"(arg0: $Consumer$Type<(string)>, arg1: V): $Codec<(V)>
public static "checkRange"<N extends (number) & ($Comparable<(N)>)>(arg0: N, arg1: N): $Function<(N), ($DataResult<(N)>)>
public static "unit"<A>(arg0: V): $Codec<(V)>
public static "unit"<A>(arg0: $Supplier$Type<(V)>): $Codec<(V)>
public "orElseGet"(arg0: $Supplier$Type<(any)>): $Codec<(V)>
public "orElseGet"(arg0: $Consumer$Type<(string)>, arg1: $Supplier$Type<(any)>): $Codec<(V)>
public "orElseGet"(arg0: $UnaryOperator$Type<(string)>, arg1: $Supplier$Type<(any)>): $Codec<(V)>
public static "pair"<F, S>(arg0: $Codec$Type<(F)>, arg1: $Codec$Type<(S)>): $Codec<($Pair<(F), (S)>)>
public "xmap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public static "optionalField"<F>(arg0: string, arg1: $Codec$Type<(F)>): $MapCodec<($Optional<(F)>)>
public "deprecated"(arg0: integer): $Codec<(V)>
public "withLifecycle"(arg0: $Lifecycle$Type): $Codec<(V)>
public "optionalFieldOf"(arg0: string): $MapCodec<($Optional<(V)>)>
public "optionalFieldOf"(arg0: string, arg1: V, arg2: $Lifecycle$Type): $MapCodec<(V)>
public "optionalFieldOf"(arg0: string, arg1: $Lifecycle$Type, arg2: V, arg3: $Lifecycle$Type): $MapCodec<(V)>
public "optionalFieldOf"(arg0: string, arg1: V): $MapCodec<(V)>
public "mapResult"(arg0: $Codec$ResultFunction$Type<(V)>): $Codec<(V)>
public "flatXmap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public "promotePartial"(arg0: $Consumer$Type<(string)>): $Codec<(V)>
public "partialDispatch"<E>(arg0: string, arg1: $Function$Type<(any), (any)>, arg2: $Function$Type<(any), (any)>): $Codec<(E)>
public "dispatchMap"<E>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $MapCodec<(E)>
public "dispatchMap"<E>(arg0: string, arg1: $Function$Type<(any), (any)>, arg2: $Function$Type<(any), (any)>): $MapCodec<(E)>
public static "compoundList"<K, V>(arg0: $Codec$Type<(K)>, arg1: $Codec$Type<(V)>): $Codec<($List<($Pair<(K), (V)>)>)>
public static "either"<F, S>(arg0: $Codec$Type<(F)>, arg1: $Codec$Type<(S)>): $Codec<($Either<(F), (S)>)>
public static "mapPair"<F, S>(arg0: $MapCodec$Type<(F)>, arg1: $MapCodec$Type<(S)>): $MapCodec<($Pair<(F), (S)>)>
public static "mapEither"<F, S>(arg0: $MapCodec$Type<(F)>, arg1: $MapCodec$Type<(S)>): $MapCodec<($Either<(F), (S)>)>
public "dispatchStable"<E>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(E)>
public "comapFlatMap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public static "unboundedMap"<K, V>(arg0: $Codec$Type<(K)>, arg1: $Codec$Type<(V)>): $UnboundedMapCodec<(K), (V)>
public "flatComapMap"<S>(arg0: $Function$Type<(any), (any)>, arg1: $Function$Type<(any), (any)>): $Codec<(S)>
public static "simpleMap"<K, V>(arg0: $Codec$Type<(K)>, arg1: $Codec$Type<(V)>, arg2: $Keyable$Type): $SimpleMapCodec<(K), (V)>
public static "doubleRange"(arg0: double, arg1: double): $Codec<(double)>
public static "floatRange"(arg0: float, arg1: float): $Codec<(float)>
public static "intRange"(arg0: integer, arg1: integer): $Codec<(integer)>
public "listOf"(): $Codec<($List<(V)>)>
public "stable"(): $Codec<(V)>
public static "empty"<A>(): $MapEncoder<(V)>
public static "error"<A>(arg0: string): $Encoder<(V)>
public "flatComap"<B>(arg0: $Function$Type<(any), (any)>): $Encoder<(B)>
public "comap"<B>(arg0: $Function$Type<(any), (any)>): $Encoder<(B)>
public "encodeStart"<T>(arg0: $DynamicOps$Type<(T)>, arg1: V): $DataResult<(T)>
public "decode"<T>(arg0: $Dynamic$Type<(T)>): $DataResult<($Pair<(V), (T)>)>
public "map"<B>(arg0: $Function$Type<(any), (any)>): $Decoder<(B)>
public "flatMap"<B>(arg0: $Function$Type<(any), (any)>): $Decoder<(B)>
public "parse"<T>(arg0: $Dynamic$Type<(T)>): $DataResult<(V)>
public "parse"<T>(arg0: $DynamicOps$Type<(T)>, arg1: T): $DataResult<(V)>
public "boxed"(): $Decoder$Boxed<(V)>
public "simple"(): $Decoder$Simple<(V)>
public static "ofBoxed"<A>(arg0: $Decoder$Boxed$Type<(any)>): $Decoder<(V)>
public static "ofTerminal"<A>(arg0: $Decoder$Terminal$Type<(any)>): $Decoder<(V)>
public static "ofSimple"<A>(arg0: $Decoder$Simple$Type<(any)>): $Decoder<(V)>
public "terminal"(): $Decoder$Terminal<(V)>
get "defaultCodec"(): $Codec<(any)>
set "defaultCodec"(value: $Codec$Type<(any)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CodecMap$Type<V> = ($CodecMap<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CodecMap_<V> = $CodecMap$Type<(V)>;
}}
declare module "packages/dev/shadowsoffire/attributeslib/util/$IAttributeManager" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IAttributeManager {

 "areAttributesUpdating"(): boolean
 "setAttributesUpdating"(arg0: boolean): void
}

export namespace $IAttributeManager {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IAttributeManager$Type = ($IAttributeManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IAttributeManager_ = $IAttributeManager$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/json/$ChancedEffectInstance" {
import {$StepFunction, $StepFunction$Type} from "packages/dev/shadowsoffire/placebo/util/$StepFunction"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"

export class $ChancedEffectInstance extends $Record {
static "CODEC": $Codec<($ChancedEffectInstance)>
static "CONSTANT_CODEC": $Codec<($ChancedEffectInstance)>

constructor(chance: float, effect: $MobEffect$Type, amplifier: $StepFunction$Type, ambient: boolean, visible: boolean)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "create"(arg0: $RandomSource$Type, arg1: integer): $MobEffectInstance
public "visible"(): boolean
public "effect"(): $MobEffect
public "ambient"(): boolean
public "chance"(): float
public "amplifier"(): $StepFunction
public "createDeterministic"(arg0: integer): $MobEffectInstance
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChancedEffectInstance$Type = ($ChancedEffectInstance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChancedEffectInstance_ = $ChancedEffectInstance$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/api/$AttributeChangedValueEvent" {
import {$AttributeInstance, $AttributeInstance$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeInstance"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $AttributeChangedValueEvent extends $Event {

constructor(arg0: $LivingEntity$Type, arg1: $AttributeInstance$Type, arg2: double, arg3: double)
constructor()

public "getOldValue"(): double
public "getNewValue"(): double
public "isCancelable"(): boolean
public "getEntity"(): $LivingEntity
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
public "getAttributeInstance"(): $AttributeInstance
get "oldValue"(): double
get "newValue"(): double
get "cancelable"(): boolean
get "entity"(): $LivingEntity
get "listenerList"(): $ListenerList
get "attributeInstance"(): $AttributeInstance
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributeChangedValueEvent$Type = ($AttributeChangedValueEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributeChangedValueEvent_ = $AttributeChangedValueEvent$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/mixin/$AbstractContainerMenuInvoker" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $AbstractContainerMenuInvoker {

 "_moveItemStackTo"(arg0: $ItemStack$Type, arg1: integer, arg2: integer, arg3: boolean): boolean

(arg0: $ItemStack$Type, arg1: integer, arg2: integer, arg3: boolean): boolean
}

export namespace $AbstractContainerMenuInvoker {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractContainerMenuInvoker$Type = ($AbstractContainerMenuInvoker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractContainerMenuInvoker_ = $AbstractContainerMenuInvoker$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/client/$ModifierSourceType" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$ModifierSource, $ModifierSource$Type} from "packages/dev/shadowsoffire/attributeslib/client/$ModifierSource"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ModifierSourceType<T> {
static readonly "EQUIPMENT": $ModifierSourceType<($ItemStack)>
static readonly "MOB_EFFECT": $ModifierSourceType<($MobEffectInstance)>

constructor()

public "extract"(arg0: $LivingEntity$Type, arg1: $BiConsumer$Type<($AttributeModifier$Type), ($ModifierSource$Type<(any)>)>): void
public static "register"<T extends $ModifierSourceType<(any)>>(arg0: T): T
public "getPriority"(): integer
public static "compareBySource"(arg0: $Map$Type<($UUID$Type), ($ModifierSource$Type<(any)>)>): $Comparator<($AttributeModifier)>
public static "getTypes"(): $Collection<($ModifierSourceType<(any)>)>
get "priority"(): integer
get "types"(): $Collection<($ModifierSourceType<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModifierSourceType$Type<T> = ($ModifierSourceType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModifierSourceType_<T> = $ModifierSourceType$Type<(T)>;
}}
declare module "packages/dev/shadowsoffire/attributeslib/api/client/$AddAttributeTooltipsEvent" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PlayerEvent, $PlayerEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent"
import {$ListIterator, $ListIterator$Type} from "packages/java/util/$ListIterator"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $AddAttributeTooltipsEvent extends $PlayerEvent {

constructor(arg0: $ItemStack$Type, arg1: $Player$Type, arg2: $List$Type<($Component$Type)>, arg3: $ListIterator$Type<($Component$Type)>, arg4: $TooltipFlag$Type)
constructor()

public "getStack"(): $ItemStack
public "getFlags"(): $TooltipFlag
public "getAttributeTooltipIterator"(): $ListIterator<($Component)>
public "getTooltip"(): $List<($Component)>
public "getListenerList"(): $ListenerList
get "stack"(): $ItemStack
get "flags"(): $TooltipFlag
get "attributeTooltipIterator"(): $ListIterator<($Component)>
get "tooltip"(): $List<($Component)>
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AddAttributeTooltipsEvent$Type = ($AddAttributeTooltipsEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AddAttributeTooltipsEvent_ = $AddAttributeTooltipsEvent$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/api/client/$GatherEffectScreenTooltipsEvent" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$EffectRenderingInventoryScreen, $EffectRenderingInventoryScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$EffectRenderingInventoryScreen"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"

export class $GatherEffectScreenTooltipsEvent extends $Event {

constructor()
constructor(arg0: $EffectRenderingInventoryScreen$Type<(any)>, arg1: $MobEffectInstance$Type, arg2: $List$Type<($Component$Type)>)

public "isCancelable"(): boolean
public "getScreen"(): $EffectRenderingInventoryScreen<(any)>
public "getTooltip"(): $List<($Component)>
public "getEffectInstance"(): $MobEffectInstance
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "cancelable"(): boolean
get "screen"(): $EffectRenderingInventoryScreen<(any)>
get "tooltip"(): $List<($Component)>
get "effectInstance"(): $MobEffectInstance
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GatherEffectScreenTooltipsEvent$Type = ($GatherEffectScreenTooltipsEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GatherEffectScreenTooltipsEvent_ = $GatherEffectScreenTooltipsEvent$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/util/$StackPrimer" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $StackPrimer {

constructor(arg0: $Block$Type, arg1: integer, arg2: $CompoundTag$Type)
constructor(arg0: $Block$Type, arg1: $CompoundTag$Type)
constructor(arg0: $Block$Type)
constructor(arg0: $Item$Type, arg1: integer, arg2: $CompoundTag$Type)
constructor(arg0: $Item$Type, arg1: $CompoundTag$Type)
constructor(arg0: $Item$Type)

public "genStack"(): $ItemStack
public "isEmpty"(): boolean
public "getCount"(): integer
public "getItem"(): $Item
public "getBlock"(): $Block
get "empty"(): boolean
get "count"(): integer
get "item"(): $Item
get "block"(): $Block
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StackPrimer$Type = ($StackPrimer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StackPrimer_ = $StackPrimer$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/mobfx/$SunderingEffect" {
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"

export class $SunderingEffect extends $MobEffect {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SunderingEffect$Type = ($SunderingEffect);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SunderingEffect_ = $SunderingEffect$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/util/$IFlying" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IFlying {

 "markFlying"(): void
 "getAndDestroyFlyingCache"(): boolean
}

export namespace $IFlying {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IFlying$Type = ($IFlying);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IFlying_ = $IFlying$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/compat/$TOPCompat" {
import {$TOPCompat$Provider, $TOPCompat$Provider$Type} from "packages/dev/shadowsoffire/placebo/compat/$TOPCompat$Provider"

export class $TOPCompat {

constructor()

public static "register"(): void
public static "registerProvider"(arg0: $TOPCompat$Provider$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TOPCompat$Type = ($TOPCompat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TOPCompat_ = $TOPCompat$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/$ALConfig" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ResourceManagerReloadListener, $ResourceManagerReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManagerReloadListener"
import {$Expression, $Expression$Type} from "packages/repack/evalex/$Expression"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ALConfig {
static readonly "DEFAULT_BLOCKED_ATTRIBUTES": (string)[]
static "enableAttributesGui": boolean
static "enablePotionTooltips": boolean
static "hiddenAttributes": $Set<($ResourceLocation)>

constructor()

public static "load"(): void
public static "getProtExpr"(): $Optional<($Expression)>
public static "getArmorExpr"(): $Optional<($Expression)>
public static "getAValueExpr"(): $Optional<($Expression)>
public static "makeReloader"(): $ResourceManagerReloadListener
get "protExpr"(): $Optional<($Expression)>
get "armorExpr"(): $Optional<($Expression)>
get "aValueExpr"(): $Optional<($Expression)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ALConfig$Type = ($ALConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ALConfig_ = $ALConfig$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/cap/$ModifiableEnergyStorage" {
import {$EnergyStorage, $EnergyStorage$Type} from "packages/net/minecraftforge/energy/$EnergyStorage"

export class $ModifiableEnergyStorage extends $EnergyStorage {

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer)
constructor(arg0: integer, arg1: integer, arg2: integer)
constructor(arg0: integer, arg1: integer)
constructor(arg0: integer)

public "setCapacity"(arg0: integer): void
public "setMaxExtract"(arg0: integer): void
public "setMaxReceive"(arg0: integer): void
public "setTransferRate"(arg0: integer): void
public "setEnergy"(arg0: integer): void
set "capacity"(value: integer)
set "maxExtract"(value: integer)
set "maxReceive"(value: integer)
set "transferRate"(value: integer)
set "energy"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModifiableEnergyStorage$Type = ($ModifiableEnergyStorage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModifiableEnergyStorage_ = $ModifiableEnergyStorage$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/patreon/wings/$Wing" {
import {$AbstractClientPlayer, $AbstractClientPlayer$Type} from "packages/net/minecraft/client/player/$AbstractClientPlayer"
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$EntityModel, $EntityModel$Type} from "packages/net/minecraft/client/model/$EntityModel"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"
import {$LayerDefinition, $LayerDefinition$Type} from "packages/net/minecraft/client/model/geom/builders/$LayerDefinition"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$PlayerModel, $PlayerModel$Type} from "packages/net/minecraft/client/model/$PlayerModel"
import {$IWingModel, $IWingModel$Type} from "packages/dev/shadowsoffire/placebo/patreon/wings/$IWingModel"

export class $Wing extends $EntityModel<($AbstractClientPlayer)> implements $IWingModel {
static "INSTANCE": $Wing
 "attackTime": float
 "riding": boolean
 "young": boolean

constructor(arg0: $ModelPart$Type)

public static "createLayer"(): $LayerDefinition
public "setRotationAngle"(arg0: $ModelPart$Type, arg1: float, arg2: float, arg3: float): void
public "render"(arg0: $PoseStack$Type, arg1: $MultiBufferSource$Type, arg2: integer, arg3: $AbstractClientPlayer$Type, arg4: float, arg5: $ResourceLocation$Type, arg6: $PlayerModel$Type<($AbstractClientPlayer$Type)>): void
public "setupAnim"(arg0: $AbstractClientPlayer$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float): void
public "renderToBuffer"(arg0: $PoseStack$Type, arg1: $VertexConsumer$Type, arg2: integer, arg3: integer, arg4: float, arg5: float, arg6: float, arg7: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Wing$Type = ($Wing);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Wing_ = $Wing$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/collections/$BlockedDeque" {
import {$ArrayDeque, $ArrayDeque$Type} from "packages/java/util/$ArrayDeque"

/**
 * 
 * @deprecated
 */
export class $BlockedDeque<T> extends $ArrayDeque<(T)> {

constructor()

public "add"(arg0: T): boolean
public "addFirst"(arg0: T): void
public "addLast"(arg0: T): void
public "offerLast"(arg0: T): boolean
public "offerFirst"(arg0: T): boolean
public "isBlocked"(arg0: T): boolean
public "equals"(arg0: any): boolean
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockedDeque$Type<T> = ($BlockedDeque<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockedDeque_<T> = $BlockedDeque$Type<(T)>;
}}
declare module "packages/dev/shadowsoffire/attributeslib/packet/$CritParticleMessage" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CritParticleMessage {

constructor(arg0: integer)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CritParticleMessage$Type = ($CritParticleMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CritParticleMessage_ = $CritParticleMessage$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/config/$Property$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Property$Type extends $Enum<($Property$Type)> {
static readonly "STRING": $Property$Type
static readonly "INTEGER": $Property$Type
static readonly "BOOLEAN": $Property$Type
static readonly "DOUBLE": $Property$Type
static readonly "COLOR": $Property$Type
static readonly "MOD_ID": $Property$Type


public static "values"(): ($Property$Type)[]
public static "valueOf"(arg0: string): $Property$Type
public "getID"(): character
public static "tryParse"(arg0: character): $Property$Type
get "iD"(): character
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Property$Type$Type = (("boolean") | ("mod_id") | ("string") | ("color") | ("double") | ("integer")) | ($Property$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Property$Type_ = $Property$Type$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/recipe/$RecipeHelper$RecipeFactory" {
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $RecipeHelper$RecipeFactory {


public "addShaped"(arg0: any, arg1: integer, arg2: integer, ...arg3: (any)[]): void
public "addShapeless"(arg0: any, ...arg1: (any)[]): void
public "addRecipe"(arg0: $Recipe$Type<(any)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeHelper$RecipeFactory$Type = ($RecipeHelper$RecipeFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeHelper$RecipeFactory_ = $RecipeHelper$RecipeFactory$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/reload/$RegistryCallback" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$CodecProvider, $CodecProvider$Type} from "packages/dev/shadowsoffire/placebo/codec/$CodecProvider"
import {$DynamicRegistry, $DynamicRegistry$Type} from "packages/dev/shadowsoffire/placebo/reload/$DynamicRegistry"

export interface $RegistryCallback<R extends $CodecProvider<(any)>> {

 "onReload"(arg0: $DynamicRegistry$Type<(R)>): void
 "beginReload"(arg0: $DynamicRegistry$Type<(R)>): void
}

export namespace $RegistryCallback {
function create<R>(arg0: $Consumer$Type<($DynamicRegistry$Type<(R)>)>, arg1: $Consumer$Type<($DynamicRegistry$Type<(R)>)>): $RegistryCallback<(R)>
function beginOnly<R>(arg0: $Consumer$Type<($DynamicRegistry$Type<(R)>)>): $RegistryCallback<(R)>
function reloadOnly<R>(arg0: $Consumer$Type<($DynamicRegistry$Type<(R)>)>): $RegistryCallback<(R)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryCallback$Type<R> = ($RegistryCallback<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryCallback_<R> = $RegistryCallback$Type<(R)>;
}}
declare module "packages/dev/shadowsoffire/placebo/util/$CachedObject" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $CachedObject<T> {
static readonly "HAS_NEVER_BEEN_INITIALIZED": integer
static readonly "EMPTY_NBT": integer

constructor(arg0: $ResourceLocation$Type, arg1: $Function$Type<($ItemStack$Type), (T)>, arg2: $ToIntFunction$Type<($ItemStack$Type)>)
constructor(arg0: $ResourceLocation$Type, arg1: $Function$Type<($ItemStack$Type), (T)>)

public "get"(arg0: $ItemStack$Type): T
public "reset"(): void
public static "defaultHash"(arg0: $ItemStack$Type): integer
public static "hashSubkey"(arg0: string): $ToIntFunction<($ItemStack)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CachedObject$Type<T> = ($CachedObject<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CachedObject_<T> = $CachedObject$Type<(T)>;
}}
declare module "packages/dev/shadowsoffire/placebo/json/$RandomAttributeModifier" {
import {$StepFunction, $StepFunction$Type} from "packages/dev/shadowsoffire/placebo/util/$StepFunction"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $RandomAttributeModifier extends $Record {
static "CODEC": $Codec<($RandomAttributeModifier)>
static "CONSTANT_CODEC": $Codec<($RandomAttributeModifier)>

constructor(attribute: $Attribute$Type, op: $AttributeModifier$Operation$Type, value: $StepFunction$Type)

public "value"(): $StepFunction
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getValue"(): $StepFunction
public "apply"(arg0: $RandomSource$Type, arg1: $LivingEntity$Type): void
public "create"(arg0: $RandomSource$Type): $AttributeModifier
public "create"(arg0: string, arg1: $RandomSource$Type): $AttributeModifier
public "op"(): $AttributeModifier$Operation
public "attribute"(): $Attribute
public "getAttribute"(): $Attribute
public "getOp"(): $AttributeModifier$Operation
public "createDeterministic"(arg0: string): $AttributeModifier
public "createDeterministic"(): $AttributeModifier
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RandomAttributeModifier$Type = ($RandomAttributeModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RandomAttributeModifier_ = $RandomAttributeModifier$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/util/$LinearEquation" {
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $LinearEquation {

constructor(arg0: $Vec3$Type, arg1: $Vec3$Type)

public "step"(arg0: double): $Vec3
public "getDest"(): $Vec3
public "getSrc"(): $Vec3
get "dest"(): $Vec3
get "src"(): $Vec3
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LinearEquation$Type = ($LinearEquation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LinearEquation_ = $LinearEquation$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/block_entity/$TickingBlockEntityType" {
import {$BlockEntityType$BlockEntitySupplier, $BlockEntityType$BlockEntitySupplier$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType$BlockEntitySupplier"
import {$ShulkerBoxBlockEntity, $ShulkerBoxBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$ShulkerBoxBlockEntity"
import {$BeehiveBlockEntity, $BeehiveBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BeehiveBlockEntity"
import {$ComparatorBlockEntity, $ComparatorBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$ComparatorBlockEntity"
import {$DynamicLightHandlerHolder, $DynamicLightHandlerHolder$Type} from "packages/dev/lambdaurora/lambdynlights/accessor/$DynamicLightHandlerHolder"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$DropperBlockEntity, $DropperBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$DropperBlockEntity"
import {$BrewingStandBlockEntity, $BrewingStandBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BrewingStandBlockEntity"
import {$DaylightDetectorBlockEntity, $DaylightDetectorBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$DaylightDetectorBlockEntity"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$HangingSignBlockEntity, $HangingSignBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$HangingSignBlockEntity"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$SignBlockEntity, $SignBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SignBlockEntity"
import {$JukeboxBlockEntity, $JukeboxBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$JukeboxBlockEntity"
import {$SculkShriekerBlockEntity, $SculkShriekerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SculkShriekerBlockEntity"
import {$EnchantmentTableBlockEntity, $EnchantmentTableBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$EnchantmentTableBlockEntity"
import {$CalibratedSculkSensorBlockEntity, $CalibratedSculkSensorBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$CalibratedSculkSensorBlockEntity"
import {$TickingBlockEntity, $TickingBlockEntity$Type} from "packages/dev/shadowsoffire/placebo/block_entity/$TickingBlockEntity"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$ChiseledBookShelfBlockEntity, $ChiseledBookShelfBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$ChiseledBookShelfBlockEntity"
import {$BrushableBlockEntity, $BrushableBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BrushableBlockEntity"
import {$FurnaceBlockEntity, $FurnaceBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$FurnaceBlockEntity"
import {$DispenserBlockEntity, $DispenserBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$DispenserBlockEntity"
import {$EnderChestBlockEntity, $EnderChestBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$EnderChestBlockEntity"
import {$SculkSensorBlockEntity, $SculkSensorBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SculkSensorBlockEntity"
import {$BarrelBlockEntity, $BarrelBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BarrelBlockEntity"
import {$ChestBlockEntity, $ChestBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$ChestBlockEntity"
import {$BannerBlockEntity, $BannerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BannerBlockEntity"
import {$TheEndGatewayBlockEntity, $TheEndGatewayBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$TheEndGatewayBlockEntity"
import {$CommandBlockEntity, $CommandBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$CommandBlockEntity"
import {$BellBlockEntity, $BellBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BellBlockEntity"
import {$TrappedChestBlockEntity, $TrappedChestBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$TrappedChestBlockEntity"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$SmokerBlockEntity, $SmokerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SmokerBlockEntity"
import {$BlastFurnaceBlockEntity, $BlastFurnaceBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlastFurnaceBlockEntity"
import {$DecoratedPotBlockEntity, $DecoratedPotBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$DecoratedPotBlockEntity"
import {$PistonMovingBlockEntity, $PistonMovingBlockEntity$Type} from "packages/net/minecraft/world/level/block/piston/$PistonMovingBlockEntity"
import {$HopperBlockEntity, $HopperBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$HopperBlockEntity"
import {$TheEndPortalBlockEntity, $TheEndPortalBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$TheEndPortalBlockEntity"
import {$BeaconBlockEntity, $BeaconBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BeaconBlockEntity"
import {$StructureBlockEntity, $StructureBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$StructureBlockEntity"
import {$SpawnerBlockEntity, $SpawnerBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SpawnerBlockEntity"
import {$JigsawBlockEntity, $JigsawBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$JigsawBlockEntity"
import {$ConduitBlockEntity, $ConduitBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$ConduitBlockEntity"
import {$BedBlockEntity, $BedBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BedBlockEntity"
import {$SculkCatalystBlockEntity, $SculkCatalystBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SculkCatalystBlockEntity"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$SkullBlockEntity, $SkullBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$SkullBlockEntity"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"
import {$LecternBlockEntity, $LecternBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$LecternBlockEntity"
import {$CampfireBlockEntity, $CampfireBlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$CampfireBlockEntity"

export class $TickingBlockEntityType<T extends ($BlockEntity) & ($TickingBlockEntity)> extends $BlockEntityType<(T)> {
static readonly "FURNACE": $BlockEntityType<($FurnaceBlockEntity)>
static readonly "CHEST": $BlockEntityType<($ChestBlockEntity)>
static readonly "TRAPPED_CHEST": $BlockEntityType<($TrappedChestBlockEntity)>
static readonly "ENDER_CHEST": $BlockEntityType<($EnderChestBlockEntity)>
static readonly "JUKEBOX": $BlockEntityType<($JukeboxBlockEntity)>
static readonly "DISPENSER": $BlockEntityType<($DispenserBlockEntity)>
static readonly "DROPPER": $BlockEntityType<($DropperBlockEntity)>
static readonly "SIGN": $BlockEntityType<($SignBlockEntity)>
static readonly "HANGING_SIGN": $BlockEntityType<($HangingSignBlockEntity)>
static readonly "MOB_SPAWNER": $BlockEntityType<($SpawnerBlockEntity)>
static readonly "PISTON": $BlockEntityType<($PistonMovingBlockEntity)>
static readonly "BREWING_STAND": $BlockEntityType<($BrewingStandBlockEntity)>
static readonly "ENCHANTING_TABLE": $BlockEntityType<($EnchantmentTableBlockEntity)>
static readonly "END_PORTAL": $BlockEntityType<($TheEndPortalBlockEntity)>
static readonly "BEACON": $BlockEntityType<($BeaconBlockEntity)>
static readonly "SKULL": $BlockEntityType<($SkullBlockEntity)>
static readonly "DAYLIGHT_DETECTOR": $BlockEntityType<($DaylightDetectorBlockEntity)>
static readonly "HOPPER": $BlockEntityType<($HopperBlockEntity)>
static readonly "COMPARATOR": $BlockEntityType<($ComparatorBlockEntity)>
static readonly "BANNER": $BlockEntityType<($BannerBlockEntity)>
static readonly "STRUCTURE_BLOCK": $BlockEntityType<($StructureBlockEntity)>
static readonly "END_GATEWAY": $BlockEntityType<($TheEndGatewayBlockEntity)>
static readonly "COMMAND_BLOCK": $BlockEntityType<($CommandBlockEntity)>
static readonly "SHULKER_BOX": $BlockEntityType<($ShulkerBoxBlockEntity)>
static readonly "BED": $BlockEntityType<($BedBlockEntity)>
static readonly "CONDUIT": $BlockEntityType<($ConduitBlockEntity)>
static readonly "BARREL": $BlockEntityType<($BarrelBlockEntity)>
static readonly "SMOKER": $BlockEntityType<($SmokerBlockEntity)>
static readonly "BLAST_FURNACE": $BlockEntityType<($BlastFurnaceBlockEntity)>
static readonly "LECTERN": $BlockEntityType<($LecternBlockEntity)>
static readonly "BELL": $BlockEntityType<($BellBlockEntity)>
static readonly "JIGSAW": $BlockEntityType<($JigsawBlockEntity)>
static readonly "CAMPFIRE": $BlockEntityType<($CampfireBlockEntity)>
static readonly "BEEHIVE": $BlockEntityType<($BeehiveBlockEntity)>
static readonly "SCULK_SENSOR": $BlockEntityType<($SculkSensorBlockEntity)>
static readonly "CALIBRATED_SCULK_SENSOR": $BlockEntityType<($CalibratedSculkSensorBlockEntity)>
static readonly "SCULK_CATALYST": $BlockEntityType<($SculkCatalystBlockEntity)>
static readonly "SCULK_SHRIEKER": $BlockEntityType<($SculkShriekerBlockEntity)>
static readonly "CHISELED_BOOKSHELF": $BlockEntityType<($ChiseledBookShelfBlockEntity)>
static readonly "BRUSHABLE_BLOCK": $BlockEntityType<($BrushableBlockEntity)>
static readonly "DECORATED_POT": $BlockEntityType<($DecoratedPotBlockEntity)>
 "validBlocks": $Set<($Block)>

constructor(arg0: $BlockEntityType$BlockEntitySupplier$Type<(any)>, arg1: $Set$Type<($Block$Type)>, arg2: boolean, arg3: boolean)

public "getTicker"(arg0: boolean): $BlockEntityTicker<(T)>
public static "cast"<T extends $BlockEntity>(arg0: $BlockEntityType$Type<(T)>): $DynamicLightHandlerHolder<(T)>
public static "cast"<T extends $Entity>(arg0: $EntityType$Type<(T)>): $DynamicLightHandlerHolder<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickingBlockEntityType$Type<T> = ($TickingBlockEntityType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickingBlockEntityType_<T> = $TickingBlockEntityType$Type<(T)>;
}}
declare module "packages/dev/shadowsoffire/placebo/util/$IReplacementBlock" {
import {$StateDefinition, $StateDefinition$Type} from "packages/net/minecraft/world/level/block/state/$StateDefinition"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

/**
 * 
 * @deprecated
 */
export interface $IReplacementBlock {

 "setStateContainer"(arg0: $StateDefinition$Type<($Block$Type), ($BlockState$Type)>): void
 "_setDefaultState"(arg0: $BlockState$Type): void
}

export namespace $IReplacementBlock {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IReplacementBlock$Type = ($IReplacementBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IReplacementBlock_ = $IReplacementBlock$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/util/$ClientUtil" {
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $ClientUtil {

constructor()

public static "colorBlit"(arg0: $PoseStack$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $TextureAtlasSprite$Type, arg7: integer): void
public static "colorBlit"(arg0: $PoseStack$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: integer): void
public static "colorBlit"(arg0: $PoseStack$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: float, arg6: float, arg7: integer, arg8: integer, arg9: integer, arg10: integer, arg11: integer): void
public static "innerBlit"(arg0: $PoseStack$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: integer, arg6: float, arg7: float, arg8: float, arg9: float, arg10: integer, arg11: integer, arg12: integer): void
public static "innerBlit"(arg0: $Matrix4f$Type, arg1: float, arg2: float, arg3: float, arg4: float, arg5: integer, arg6: float, arg7: float, arg8: float, arg9: float, arg10: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientUtil$Type = ($ClientUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientUtil_ = $ClientUtil$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/recipe/$NBTIngredient" {
import {$Ingredient$Value, $Ingredient$Value$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient$Value"
import {$PartialNBTIngredient, $PartialNBTIngredient$Type} from "packages/net/minecraftforge/common/crafting/$PartialNBTIngredient"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$IntList, $IntList$Type} from "packages/it/unimi/dsi/fastutil/ints/$IntList"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $NBTIngredient extends $PartialNBTIngredient {
static readonly "EMPTY": $Ingredient
 "values": ($Ingredient$Value)[]
 "itemStacks": ($ItemStack)[]
 "stackingIds": $IntList

constructor(arg0: $ItemStack$Type)

public static "not"<T>(arg0: $Predicate$Type<(any)>): $Predicate<(T)>
public static "isEqual"<T>(arg0: any): $Predicate<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NBTIngredient$Type = ($NBTIngredient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NBTIngredient_ = $NBTIngredient$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/mobfx/$VitalityEffect" {
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"

export class $VitalityEffect extends $MobEffect {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VitalityEffect$Type = ($VitalityEffect);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VitalityEffect_ = $VitalityEffect$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/screen/$ScreenUtil" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ScreenUtil {

constructor()

public static "getHeight"(arg0: float, arg1: integer, arg2: integer): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenUtil$Type = ($ScreenUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenUtil_ = $ScreenUtil$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/impl/$AttributeEvents" {
import {$ProjectileImpactEvent, $ProjectileImpactEvent$Type} from "packages/net/minecraftforge/event/entity/$ProjectileImpactEvent"
import {$LivingHealEvent, $LivingHealEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingHealEvent"
import {$BlockEvent$BreakEvent, $BlockEvent$BreakEvent$Type} from "packages/net/minecraftforge/event/level/$BlockEvent$BreakEvent"
import {$LivingAttackEvent, $LivingAttackEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingAttackEvent"
import {$PlayerEvent$PlayerLoggedInEvent, $PlayerEvent$PlayerLoggedInEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerLoggedInEvent"
import {$AttributeChangedValueEvent, $AttributeChangedValueEvent$Type} from "packages/dev/shadowsoffire/attributeslib/api/$AttributeChangedValueEvent"
import {$CriticalHitEvent, $CriticalHitEvent$Type} from "packages/net/minecraftforge/event/entity/player/$CriticalHitEvent"
import {$LivingExperienceDropEvent, $LivingExperienceDropEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingExperienceDropEvent"
import {$LivingEntityUseItemEvent$Tick, $LivingEntityUseItemEvent$Tick$Type} from "packages/net/minecraftforge/event/entity/living/$LivingEntityUseItemEvent$Tick"
import {$AttackEntityEvent, $AttackEntityEvent$Type} from "packages/net/minecraftforge/event/entity/player/$AttackEntityEvent"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$EntityJoinLevelEvent, $EntityJoinLevelEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityJoinLevelEvent"
import {$LivingHurtEvent, $LivingHurtEvent$Type} from "packages/net/minecraftforge/event/entity/living/$LivingHurtEvent"
import {$AddReloadListenerEvent, $AddReloadListenerEvent$Type} from "packages/net/minecraftforge/event/$AddReloadListenerEvent"
import {$ItemAttributeModifierEvent, $ItemAttributeModifierEvent$Type} from "packages/net/minecraftforge/event/$ItemAttributeModifierEvent"
import {$GameType, $GameType$Type} from "packages/net/minecraft/world/level/$GameType"
import {$PlayerEvent$BreakSpeed, $PlayerEvent$BreakSpeed$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$BreakSpeed"

export class $AttributeEvents {

constructor()

public "breakSpd"(arg0: $PlayerEvent$BreakSpeed$Type): void
public "vanillaCritDmg"(arg0: $CriticalHitEvent$Type): void
public "drawSpeed"(arg0: $LivingEntityUseItemEvent$Tick$Type): void
public "lifeStealOverheal"(arg0: $LivingHurtEvent$Type): void
public "dodge"(arg0: $LivingAttackEvent$Type): void
public "dodge"(arg0: $ProjectileImpactEvent$Type): void
public "mobXp"(arg0: $LivingExperienceDropEvent$Type): void
public "blockBreak"(arg0: $BlockEvent$BreakEvent$Type): void
public "affixModifiers"(arg0: $ItemAttributeModifierEvent$Type): void
public "fixMCF9370"(arg0: $ProjectileImpactEvent$Type): void
public "arrow"(arg0: $EntityJoinLevelEvent$Type): void
public "heal"(arg0: $LivingHealEvent$Type): void
public "valueChanged"(arg0: $AttributeChangedValueEvent$Type): void
public "reloads"(arg0: $AddReloadListenerEvent$Type): void
public "trackCooldown"(arg0: $AttackEntityEvent$Type): void
public "meleeDamageAttributes"(arg0: $LivingAttackEvent$Type): void
public "apothCriticalStrike"(arg0: $LivingHurtEvent$Type): void
public "fixChangedAttributes"(arg0: $PlayerEvent$PlayerLoggedInEvent$Type): void
public static "applyCreativeFlightModifier"(arg0: $Player$Type, arg1: $GameType$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributeEvents$Type = ($AttributeEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributeEvents_ = $AttributeEvents$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/client/$CuriosClientCompat" {
import {$ScreenEvent$Init$Post, $ScreenEvent$Init$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$Init$Post"

export class $CuriosClientCompat {

constructor()

public "addAttribComponent"(arg0: $ScreenEvent$Init$Post$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CuriosClientCompat$Type = ($CuriosClientCompat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CuriosClientCompat_ = $CuriosClientCompat$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/menu/$MenuUtil$PosFactory" {
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $MenuUtil$PosFactory<T> {

 "create"(arg0: integer, arg1: $Inventory$Type, arg2: $BlockPos$Type): T

(arg0: integer, arg1: $Inventory$Type, arg2: $BlockPos$Type): T
}

export namespace $MenuUtil$PosFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuUtil$PosFactory$Type<T> = ($MenuUtil$PosFactory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuUtil$PosFactory_<T> = $MenuUtil$PosFactory$Type<(T)>;
}}
declare module "packages/dev/shadowsoffire/attributeslib/util/$ItemAccess" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ItemAccess extends $Item {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any


public static "getBaseAD"(): $UUID
public static "getBaseAS"(): $UUID
get "baseAD"(): $UUID
get "baseAS"(): $UUID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemAccess$Type = ($ItemAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemAccess_ = $ItemAccess$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/util/$RandomRange" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"

export class $RandomRange extends $Record {

constructor(min: double, max: double)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "min"(): double
public "max"(): double
public "getInt"(arg0: $RandomSource$Type): integer
public "getFloat"(arg0: $RandomSource$Type): float
public "getDouble"(arg0: $RandomSource$Type): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RandomRange$Type = ($RandomRange);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RandomRange_ = $RandomRange$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/mobfx/$BleedingEffect" {
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"

export class $BleedingEffect extends $MobEffect {

constructor()

public "isDurationEffectTick"(arg0: integer, arg1: integer): boolean
public "applyEffectTick"(arg0: $LivingEntity$Type, arg1: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BleedingEffect$Type = ($BleedingEffect);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BleedingEffect_ = $BleedingEffect$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/json/$WeightedItemStack" {
import {$Weight, $Weight$Type} from "packages/net/minecraft/util/random/$Weight"
import {$List, $List$Type} from "packages/java/util/$List"
import {$WeightedEntry$IntrusiveBase, $WeightedEntry$IntrusiveBase$Type} from "packages/net/minecraft/util/random/$WeightedEntry$IntrusiveBase"
import {$EquipmentSlot, $EquipmentSlot$Type} from "packages/net/minecraft/world/entity/$EquipmentSlot"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$WeightedEntry$Wrapper, $WeightedEntry$Wrapper$Type} from "packages/net/minecraft/util/random/$WeightedEntry$Wrapper"

export class $WeightedItemStack extends $WeightedEntry$IntrusiveBase {
static readonly "CODEC": $Codec<($WeightedItemStack)>
static readonly "LIST_CODEC": $Codec<($List<($WeightedItemStack)>)>

constructor(arg0: $ItemStack$Type, arg1: $Weight$Type, arg2: float)

public "toString"(): string
public "apply"(arg0: $LivingEntity$Type, arg1: $EquipmentSlot$Type): void
public "getStack"(): $ItemStack
public static "wrap"<T>(arg0: T, arg1: integer): $WeightedEntry$Wrapper<(T)>
get "stack"(): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WeightedItemStack$Type = ($WeightedItemStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WeightedItemStack_ = $WeightedItemStack$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/patreon/$WingsManager" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$InputEvent$Key, $InputEvent$Key$Type} from "packages/net/minecraftforge/client/event/$InputEvent$Key"
import {$PatreonUtils$WingType, $PatreonUtils$WingType$Type} from "packages/dev/shadowsoffire/placebo/patreon/$PatreonUtils$WingType"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ModelLayerLocation, $ModelLayerLocation$Type} from "packages/net/minecraft/client/model/geom/$ModelLayerLocation"
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"
import {$EntityRenderersEvent$AddLayers, $EntityRenderersEvent$AddLayers$Type} from "packages/net/minecraftforge/client/event/$EntityRenderersEvent$AddLayers"

export class $WingsManager {
static readonly "TOGGLE": $KeyMapping
static readonly "DISABLED": $Set<($UUID)>
static readonly "WING_LOC": $ModelLayerLocation

constructor()

public static "init"(arg0: $FMLClientSetupEvent$Type): void
public static "keys"(arg0: $InputEvent$Key$Type): void
public static "getType"(arg0: $UUID$Type): $PatreonUtils$WingType
public static "addLayers"(arg0: $EntityRenderersEvent$AddLayers$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WingsManager$Type = ($WingsManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WingsManager_ = $WingsManager$Type;
}}
declare module "packages/dev/shadowsoffire/attributeslib/api/$AttributeHelper" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Multimap, $Multimap$Type} from "packages/com/google/common/collect/$Multimap"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$AttributeModifier$Operation, $AttributeModifier$Operation$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier$Operation"
import {$AttributeModifier, $AttributeModifier$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeModifier"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $AttributeHelper {
static readonly "BASE_ATTACK_DAMAGE": $UUID
static readonly "BASE_ATTACK_SPEED": $UUID
static readonly "BASE_ENTITY_REACH": $UUID
static readonly "ELYTRA_FLIGHT_UUID": $UUID
static readonly "CREATIVE_FLIGHT_UUID": $UUID

constructor()

public static "list"(): $MutableComponent
public static "sortedMap"(): $Multimap<($Attribute), ($AttributeModifier)>
public static "modifierComparator"(): $Comparator<($AttributeModifier)>
public static "modify"(arg0: $LivingEntity$Type, arg1: $Attribute$Type, arg2: string, arg3: double, arg4: $AttributeModifier$Operation$Type): void
public static "addXTimesNewBase"(arg0: $LivingEntity$Type, arg1: $Attribute$Type, arg2: string, arg3: double): void
public static "multiplyFinal"(arg0: $LivingEntity$Type, arg1: $Attribute$Type, arg2: string, arg3: double): void
public static "addToBase"(arg0: $LivingEntity$Type, arg1: $Attribute$Type, arg2: string, arg3: double): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributeHelper$Type = ($AttributeHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributeHelper_ = $AttributeHelper$Type;
}}
declare module "packages/dev/shadowsoffire/placebo/reload/$DynamicHolder" {
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $DynamicHolder<T> implements $Supplier<(T)> {
static readonly "EMPTY": $ResourceLocation


public "get"(): T
/**
 * 
 * @deprecated
 */
public "value"(): T
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "getId"(): $ResourceLocation
public "is"(arg0: $ResourceLocation$Type): boolean
public "getOptional"(): $Optional<(T)>
public "isBound"(): boolean
get "id"(): $ResourceLocation
get "optional"(): $Optional<(T)>
get "bound"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicHolder$Type<T> = ($DynamicHolder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicHolder_<T> = $DynamicHolder$Type<(T)>;
}}
declare module "packages/dev/shadowsoffire/placebo/events/$PlaceboEventFactory" {
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$IForgeItemStack, $IForgeItemStack$Type} from "packages/net/minecraftforge/common/extensions/$IForgeItemStack"
import {$Enchantment, $Enchantment$Type} from "packages/net/minecraft/world/item/enchantment/$Enchantment"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $PlaceboEventFactory {

constructor()

public static "getEnchantmentLevelSpecific"(arg0: integer, arg1: $IForgeItemStack$Type, arg2: $Enchantment$Type): integer
public static "getEnchantmentLevel"(arg0: $Map$Type<($Enchantment$Type), (integer)>, arg1: $IForgeItemStack$Type): $Map<($Enchantment), (integer)>
public static "onItemUse"(arg0: $ItemStack$Type, arg1: $UseOnContext$Type): $InteractionResult
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlaceboEventFactory$Type = ($PlaceboEventFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlaceboEventFactory_ = $PlaceboEventFactory$Type;
}}
