declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$PostLoadModuleForge" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Method, $Method$Type} from "packages/java/lang/reflect/$Method"

export class $PostLoadModuleForge {
static "METHODS": $List<($Method)>

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PostLoadModuleForge$Type = ($PostLoadModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PostLoadModuleForge_ = $PostLoadModuleForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/action/$StructureAction" {
import {$PieceEntry, $PieceEntry$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/$PieceEntry"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$StructureActionType, $StructureActionType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/action/$StructureActionType"

export class $StructureAction {

constructor()

public "type"(): $StructureActionType<(any)>
public "apply"(arg0: $StructureContext$Type, arg1: $PieceEntry$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureAction$Type = ($StructureAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureAction_ = $StructureAction$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegisterBlockEntityType$Builder" {
import {$Type, $Type$Type} from "packages/com/mojang/datafixers/types/$Type"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$AutoRegisterBlockEntityType$Builder$BlockEntitySupplier, $AutoRegisterBlockEntityType$Builder$BlockEntitySupplier$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegisterBlockEntityType$Builder$BlockEntitySupplier"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $AutoRegisterBlockEntityType$Builder<T extends $BlockEntity> {


public static "of"<T extends $BlockEntity>(arg0: $AutoRegisterBlockEntityType$Builder$BlockEntitySupplier$Type<(any)>, ...arg1: ($Block$Type)[]): $AutoRegisterBlockEntityType$Builder<(T)>
public "getFactory"(): $AutoRegisterBlockEntityType$Builder$BlockEntitySupplier<(any)>
public "build"(arg0: $Type$Type<(any)>): $BlockEntityType<(T)>
public "build"(): $BlockEntityType<(T)>
public "getBlocks"(): ($Block)[]
get "factory"(): $AutoRegisterBlockEntityType$Builder$BlockEntitySupplier<(any)>
get "blocks"(): ($Block)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoRegisterBlockEntityType$Builder$Type<T> = ($AutoRegisterBlockEntityType$Builder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoRegisterBlockEntityType$Builder_<T> = $AutoRegisterBlockEntityType$Builder$Type<(T)>;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/$YungsApiMixinPlugin" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $YungsApiMixinPlugin implements $IMixinConfigPlugin {

constructor()

public "onLoad"(arg0: string): void
public "postApply"(arg0: string, arg1: $ClassNode$Type, arg2: string, arg3: $IMixinInfo$Type): void
public "getMixins"(): $List<(string)>
public "getRefMapperConfig"(): string
public "shouldApplyMixin"(arg0: string, arg1: string): boolean
public "preApply"(arg0: string, arg1: $ClassNode$Type, arg2: string, arg3: $IMixinInfo$Type): void
public "acceptTargets"(arg0: $Set$Type<(string)>, arg1: $Set$Type<(string)>): void
get "mixins"(): $List<(string)>
get "refMapperConfig"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $YungsApiMixinPlugin$Type = ($YungsApiMixinPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $YungsApiMixinPlugin_ = $YungsApiMixinPlugin$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$StructurePlacementTypeModuleForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $StructurePlacementTypeModuleForge {

constructor()

public static "processEntries"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructurePlacementTypeModuleForge$Type = ($StructurePlacementTypeModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructurePlacementTypeModuleForge_ = $StructurePlacementTypeModuleForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$BlockEntityTypeModuleForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $BlockEntityTypeModuleForge {

constructor()

public static "processEntries"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityTypeModuleForge$Type = ($BlockEntityTypeModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityTypeModuleForge_ = $BlockEntityTypeModuleForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/world/randomize/$ItemRandomizer$Entry" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $ItemRandomizer$Entry {
static "CODEC": $Codec<($ItemRandomizer$Entry)>
 "item": $Item
 "probability": float

constructor(arg0: $Item$Type, arg1: float)

public "equals"(arg0: any): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemRandomizer$Entry$Type = ($ItemRandomizer$Entry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemRandomizer$Entry_ = $ItemRandomizer$Entry$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$LargeCarvedTopNoBeardAdaptation" {
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$EnhancedTerrainAdaptation"
import {$EnhancedTerrainAdaptationType, $EnhancedTerrainAdaptationType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$EnhancedTerrainAdaptationType"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $LargeCarvedTopNoBeardAdaptation extends $EnhancedTerrainAdaptation {
static readonly "CODEC": $Codec<($LargeCarvedTopNoBeardAdaptation)>
static readonly "NONE": $EnhancedTerrainAdaptation

constructor()

public "type"(): $EnhancedTerrainAdaptationType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LargeCarvedTopNoBeardAdaptation$Type = ($LargeCarvedTopNoBeardAdaptation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LargeCarvedTopNoBeardAdaptation_ = $LargeCarvedTopNoBeardAdaptation$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/criteria/$SafeStructureLocationPredicate" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Structure, $Structure$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $SafeStructureLocationPredicate {

constructor(arg0: $ResourceKey$Type<($Structure$Type)>)

public "matches"(arg0: $ServerLevel$Type, arg1: float, arg2: float, arg3: float): boolean
public "matches"(arg0: $ServerLevel$Type, arg1: double, arg2: double, arg3: double): boolean
public static "fromJson"(arg0: $JsonElement$Type): $SafeStructureLocationPredicate
public "serializeToJson"(): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SafeStructureLocationPredicate$Type = ($SafeStructureLocationPredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SafeStructureLocationPredicate_ = $SafeStructureLocationPredicate$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/noise/$FastNoise$NoiseType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $FastNoise$NoiseType extends $Enum<($FastNoise$NoiseType)> {
static readonly "Value": $FastNoise$NoiseType
static readonly "ValueFractal": $FastNoise$NoiseType
static readonly "Perlin": $FastNoise$NoiseType
static readonly "PerlinFractal": $FastNoise$NoiseType
static readonly "Simplex": $FastNoise$NoiseType
static readonly "SimplexFractal": $FastNoise$NoiseType
static readonly "Cellular": $FastNoise$NoiseType
static readonly "WhiteNoise": $FastNoise$NoiseType
static readonly "Cubic": $FastNoise$NoiseType
static readonly "CubicFractal": $FastNoise$NoiseType


public static "values"(): ($FastNoise$NoiseType)[]
public static "valueOf"(arg0: string): $FastNoise$NoiseType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastNoise$NoiseType$Type = (("cubicfractal") | ("cubic") | ("perlinfractal") | ("perlin") | ("cellular") | ("simplex") | ("simplexfractal") | ("whitenoise") | ("valuefractal") | ("value")) | ($FastNoise$NoiseType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastNoise$NoiseType_ = $FastNoise$NoiseType$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$AllOfCondition" {
import {$StructureCondition, $StructureCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureCondition"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureConditionType"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $AllOfCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($AllOfCondition)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(arg0: $List$Type<($StructureCondition$Type)>)

public "type"(): $StructureConditionType<(any)>
public "passes"(arg0: $StructureContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AllOfCondition$Type = ($AllOfCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AllOfCondition_ = $AllOfCondition$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/services/$IAutoRegisterHelper" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"

export interface $IAutoRegisterHelper {

 "addCompostableItem"(arg0: $Supplier$Type<($Item$Type)>, arg1: float): void
 "collectAllAutoRegisterFieldsInPackage"(arg0: string): void
 "processQueuedAutoRegEntries"(): void
 "invokeAllAutoRegisterMethods"(arg0: string): void
 "registerBrewingRecipe"(arg0: $Supplier$Type<($Potion$Type)>, arg1: $Supplier$Type<($Item$Type)>, arg2: $Supplier$Type<($Potion$Type)>): void
}

export namespace $IAutoRegisterHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IAutoRegisterHelper$Type = ($IAutoRegisterHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IAutoRegisterHelper_ = $IAutoRegisterHelper$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/world/randomize/$BlockStateRandomizer" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$BlockStateRandomizer$Entry, $BlockStateRandomizer$Entry$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/api/world/randomize/$BlockStateRandomizer$Entry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$Random, $Random$Type} from "packages/java/util/$Random"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $BlockStateRandomizer {
static readonly "CODEC": $Codec<($BlockStateRandomizer)>

constructor()
constructor(arg0: $List$Type<($BlockStateRandomizer$Entry$Type)>, arg1: $BlockState$Type)
constructor(arg0: $Map$Type<($BlockState$Type), (float)>, arg1: $BlockState$Type)
constructor(arg0: $CompoundTag$Type)
constructor(arg0: $BlockState$Type)

public "get"(arg0: $Random$Type): $BlockState
public "get"(arg0: $RandomSource$Type): $BlockState
public "get"(arg0: $RandomSource$Type, arg1: $StructureContext$Type): $BlockState
public static "from"(...arg0: ($BlockState$Type)[]): $BlockStateRandomizer
public "getEntries"(): $List<($BlockStateRandomizer$Entry)>
public "saveTag"(): $CompoundTag
public "getEntriesAsMap"(): $Map<($BlockState), (float)>
public "addBlock"(arg0: $BlockState$Type, arg1: float): $BlockStateRandomizer
public "setDefaultBlockState"(arg0: $BlockState$Type): void
public "getDefaultBlockState"(): $BlockState
get "entries"(): $List<($BlockStateRandomizer$Entry)>
get "entriesAsMap"(): $Map<($BlockState), (float)>
set "defaultBlockState"(value: $BlockState$Type)
get "defaultBlockState"(): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStateRandomizer$Type = ($BlockStateRandomizer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStateRandomizer_ = $BlockStateRandomizer$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/mixin/accessor/$ListPoolElementAccessor" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructurePoolElement, $StructurePoolElement$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElement"

export interface $ListPoolElementAccessor {

 "getElements"(): $List<($StructurePoolElement)>

(): $List<($StructurePoolElement)>
}

export namespace $ListPoolElementAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListPoolElementAccessor$Type = ($ListPoolElementAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListPoolElementAccessor_ = $ListPoolElementAccessor$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/services/$ForgePlatformHelper" {
import {$IPlatformHelper, $IPlatformHelper$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/services/$IPlatformHelper"

export class $ForgePlatformHelper implements $IPlatformHelper {

constructor()

public "isDevelopmentEnvironment"(): boolean
public "getPlatformName"(): string
public "isForge"(): boolean
public "isModLoaded"(arg0: string): boolean
public "isFabric"(): boolean
get "developmentEnvironment"(): boolean
get "platformName"(): string
get "forge"(): boolean
get "fabric"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgePlatformHelper$Type = ($ForgePlatformHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgePlatformHelper_ = $ForgePlatformHelper$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegisterItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AutoRegisterEntry, $AutoRegisterEntry$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/autoregister/$AutoRegisterEntry"

export class $AutoRegisterItem extends $AutoRegisterEntry<($Item)> {


public static "of"(arg0: $Supplier$Type<($Item$Type)>): $AutoRegisterItem
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoRegisterItem$Type = ($AutoRegisterItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoRegisterItem_ = $AutoRegisterItem$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/json/$BlockStateRandomizerAdapter" {
import {$JsonReader, $JsonReader$Type} from "packages/com/google/gson/stream/$JsonReader"
import {$TypeAdapter, $TypeAdapter$Type} from "packages/com/google/gson/$TypeAdapter"
import {$BlockStateRandomizer, $BlockStateRandomizer$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/api/world/randomize/$BlockStateRandomizer"
import {$JsonWriter, $JsonWriter$Type} from "packages/com/google/gson/stream/$JsonWriter"

export class $BlockStateRandomizerAdapter extends $TypeAdapter<($BlockStateRandomizer)> {

constructor()

public "write"(arg0: $JsonWriter$Type, arg1: $BlockStateRandomizer$Type): void
public "read"(arg0: $JsonReader$Type): $BlockStateRandomizer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStateRandomizerAdapter$Type = ($BlockStateRandomizerAdapter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStateRandomizerAdapter_ = $BlockStateRandomizerAdapter$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$FeatureModuleForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $FeatureModuleForge {

constructor()

public static "processEntries"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FeatureModuleForge$Type = ($FeatureModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FeatureModuleForge_ = $FeatureModuleForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$CustomAdaptation" {
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$EnhancedTerrainAdaptation"
import {$EnhancedTerrainAdaptationType, $EnhancedTerrainAdaptationType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$EnhancedTerrainAdaptationType"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $CustomAdaptation extends $EnhancedTerrainAdaptation {
static readonly "CODEC": $Codec<($CustomAdaptation)>
static readonly "NONE": $EnhancedTerrainAdaptation


public "type"(): $EnhancedTerrainAdaptationType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomAdaptation$Type = ($CustomAdaptation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomAdaptation_ = $CustomAdaptation$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/placement/$EnhancedRandomSpread" {
import {$StructurePlacement$ExclusionZone, $StructurePlacement$ExclusionZone$Type} from "packages/net/minecraft/world/level/levelgen/structure/placement/$StructurePlacement$ExclusionZone"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$EnhancedExclusionZone, $EnhancedExclusionZone$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/exclusion/$EnhancedExclusionZone"
import {$StructurePlacementType, $StructurePlacementType$Type} from "packages/net/minecraft/world/level/levelgen/structure/placement/$StructurePlacementType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ChunkGeneratorStructureState, $ChunkGeneratorStructureState$Type} from "packages/net/minecraft/world/level/chunk/$ChunkGeneratorStructureState"
import {$RandomSpreadType, $RandomSpreadType$Type} from "packages/net/minecraft/world/level/levelgen/structure/placement/$RandomSpreadType"
import {$StructurePlacement$FrequencyReductionMethod, $StructurePlacement$FrequencyReductionMethod$Type} from "packages/net/minecraft/world/level/levelgen/structure/placement/$StructurePlacement$FrequencyReductionMethod"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$RandomSpreadStructurePlacement, $RandomSpreadStructurePlacement$Type} from "packages/net/minecraft/world/level/levelgen/structure/placement/$RandomSpreadStructurePlacement"

export class $EnhancedRandomSpread extends $RandomSpreadStructurePlacement {
static readonly "CODEC": $Codec<($EnhancedRandomSpread)>

constructor(arg0: $Vec3i$Type, arg1: $StructurePlacement$FrequencyReductionMethod$Type, arg2: float, arg3: integer, arg4: $Optional$Type<($StructurePlacement$ExclusionZone$Type)>, arg5: $Optional$Type<($EnhancedExclusionZone$Type)>, arg6: integer, arg7: integer, arg8: $RandomSpreadType$Type)

public "isStructureChunk"(arg0: $ChunkGeneratorStructureState$Type, arg1: integer, arg2: integer): boolean
public "type"(): $StructurePlacementType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnhancedRandomSpread$Type = ($EnhancedRandomSpread);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnhancedRandomSpread_ = $EnhancedRandomSpread$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$AlwaysTrueCondition" {
import {$StructureCondition, $StructureCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureCondition"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureConditionType"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $AlwaysTrueCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($AlwaysTrueCondition)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor()

public "type"(): $StructureConditionType<(any)>
public "passes"(arg0: $StructureContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AlwaysTrueCondition$Type = ($AlwaysTrueCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AlwaysTrueCondition_ = $AlwaysTrueCondition$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$BlockModuleForge" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockModuleForge$ExtraBlockData, $BlockModuleForge$ExtraBlockData$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/module/$BlockModuleForge$ExtraBlockData"

export class $BlockModuleForge {
static readonly "EXTRA_BLOCKS": $List<($BlockModuleForge$ExtraBlockData)>

constructor()

public static "processEntries"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockModuleForge$Type = ($BlockModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockModuleForge_ = $BlockModuleForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$PlacementModifierTypeModuleForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PlacementModifierTypeModuleForge {

constructor()

public static "processEntries"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlacementModifierTypeModuleForge$Type = ($PlacementModifierTypeModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlacementModifierTypeModuleForge_ = $PlacementModifierTypeModuleForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/processor/$ISafeWorldModifier" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$LevelChunkSection, $LevelChunkSection$Type} from "packages/net/minecraft/world/level/chunk/$LevelChunkSection"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"

export interface $ISafeWorldModifier {

 "getBlockStateSafe"(arg0: $LevelChunkSection$Type, arg1: $BlockPos$Type): $Optional<($BlockState)>
 "getBlockStateSafe"(arg0: $LevelReader$Type, arg1: $BlockPos$Type): $Optional<($BlockState)>
 "getFluidStateSafe"(arg0: $LevelChunkSection$Type, arg1: $BlockPos$Type): $FluidState
 "getFluidStateSafe"(arg0: $LevelReader$Type, arg1: $BlockPos$Type): $FluidState
 "setBlockStateSafe"(arg0: $LevelChunkSection$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): $Optional<($BlockState)>
 "setBlockStateSafe"(arg0: $LevelReader$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): $Optional<($BlockState)>
 "isBlockStateAirSafe"(arg0: $LevelReader$Type, arg1: $BlockPos$Type): boolean
 "isMaterialLiquidSafe"(arg0: $LevelReader$Type, arg1: $BlockPos$Type): boolean
}

export namespace $ISafeWorldModifier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISafeWorldModifier$Type = ($ISafeWorldModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISafeWorldModifier_ = $ISafeWorldModifier$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$DepthCondition" {
import {$StructureCondition, $StructureCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureCondition"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureConditionType"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $DepthCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($DepthCondition)>
readonly "minRequiredDepth": $Optional<(integer)>
readonly "maxPossibleDepth": $Optional<(integer)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(arg0: $Optional$Type<(integer)>, arg1: $Optional$Type<(integer)>)

public "type"(): $StructureConditionType<(any)>
public "passes"(arg0: $StructureContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DepthCondition$Type = ($DepthCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DepthCondition_ = $DepthCondition$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/$YungAutoRegister" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $YungAutoRegister {

constructor()

public static "scanPackageForAnnotations"(arg0: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $YungAutoRegister$Type = ($YungAutoRegister);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $YungAutoRegister_ = $YungAutoRegister$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$MaxCountListPoolElement" {
import {$StructureTemplatePool$Projection, $StructureTemplatePool$Projection$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool$Projection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IMaxCountJigsawPoolElement, $IMaxCountJigsawPoolElement$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$IMaxCountJigsawPoolElement"
import {$StructurePoolElement, $StructurePoolElement$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElement"
import {$StructurePoolElementType, $StructurePoolElementType$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElementType"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$ListPoolElement, $ListPoolElement$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$ListPoolElement"

export class $MaxCountListPoolElement extends $ListPoolElement implements $IMaxCountJigsawPoolElement {
static readonly "CODEC": $Codec<($MaxCountListPoolElement)>

constructor(arg0: $List$Type<($StructurePoolElement$Type)>, arg1: $StructureTemplatePool$Projection$Type, arg2: string, arg3: integer)

public "getName"(): string
public "toString"(): string
public "getMaxCount"(): integer
public "getType"(): $StructurePoolElementType<(any)>
get "name"(): string
get "maxCount"(): integer
get "type"(): $StructurePoolElementType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MaxCountListPoolElement$Type = ($MaxCountListPoolElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MaxCountListPoolElement_ = $MaxCountListPoolElement$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/world/randomize/$BlockStateRandomizer$Entry" {
import {$StructureCondition, $StructureCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureCondition"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $BlockStateRandomizer$Entry {
static "CODEC": $Codec<($BlockStateRandomizer$Entry)>
 "blockState": $BlockState
 "probability": float
 "condition": $Optional<($StructureCondition)>

constructor(arg0: $BlockState$Type, arg1: float)
constructor(arg0: $BlockState$Type, arg1: float, arg2: $Optional$Type<($StructureCondition$Type)>)

public "equals"(arg0: any): boolean
public "passesCondition"(arg0: $StructureContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStateRandomizer$Entry$Type = ($BlockStateRandomizer$Entry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStateRandomizer$Entry_ = $BlockStateRandomizer$Entry$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/action/$StructureActionType" {
import {$DelayGenerationAction, $DelayGenerationAction$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/action/$DelayGenerationAction"
import {$StructureAction, $StructureAction$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/action/$StructureAction"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$TransformAction, $TransformAction$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/action/$TransformAction"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $StructureActionType<C extends $StructureAction> {

 "codec"(): $Codec<(C)>

(arg0: $ResourceLocation$Type, arg1: $Codec$Type<(C)>): $StructureActionType<(C)>
}

export namespace $StructureActionType {
const ACTION_TYPES_BY_NAME: $Map<($ResourceLocation), ($StructureActionType<(any)>)>
const NAME_BY_ACTION_TYPES: $Map<($StructureActionType<(any)>), ($ResourceLocation)>
const ACTION_TYPE_CODEC: $Codec<($StructureActionType<(any)>)>
const ACTION_CODEC: $Codec<($StructureAction)>
const TRANSFORM: $StructureActionType<($TransformAction)>
const DELAY_GENERATION: $StructureActionType<($DelayGenerationAction)>
function register<C>(arg0: $ResourceLocation$Type, arg1: $Codec$Type<(C)>): $StructureActionType<(C)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureActionType$Type<C> = ($StructureActionType<(C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureActionType_<C> = $StructureActionType$Type<(C)>;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/noise/$INoiseLibrary" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $INoiseLibrary {

 "GetNoise"(arg0: float, arg1: float, arg2: float): float

(arg0: float, arg1: float, arg2: float): float
}

export namespace $INoiseLibrary {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $INoiseLibrary$Type = ($INoiseLibrary);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $INoiseLibrary_ = $INoiseLibrary$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/services/$Services" {
import {$IPlatformHelper, $IPlatformHelper$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/services/$IPlatformHelper"
import {$IBlockEntityTypeHelper, $IBlockEntityTypeHelper$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/services/$IBlockEntityTypeHelper"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$IAutoRegisterHelper, $IAutoRegisterHelper$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/services/$IAutoRegisterHelper"

export class $Services {
static readonly "PLATFORM": $IPlatformHelper
static readonly "AUTO_REGISTER": $IAutoRegisterHelper
static readonly "BLOCK_ENTITY_TYPE_HELPER": $IBlockEntityTypeHelper

constructor()

public static "load"<T>(arg0: $Class$Type<(T)>): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Services$Type = ($Services);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Services_ = $Services$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/targetselector/$StructureTargetSelectorType" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$StructureTargetSelector, $StructureTargetSelector$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/targetselector/$StructureTargetSelector"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$SelfTargetSelector, $SelfTargetSelector$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/targetselector/$SelfTargetSelector"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $StructureTargetSelectorType<C extends $StructureTargetSelector> {

 "codec"(): $Codec<(C)>

(arg0: $ResourceLocation$Type, arg1: $Codec$Type<(C)>): $StructureTargetSelectorType<(C)>
}

export namespace $StructureTargetSelectorType {
const TARGET_SELECTOR_TYPES_BY_NAME: $Map<($ResourceLocation), ($StructureTargetSelectorType<(any)>)>
const NAME_BY_TARGET_SELECTOR_TYPES: $Map<($StructureTargetSelectorType<(any)>), ($ResourceLocation)>
const TARGET_SELECTOR_TYPE_CODEC: $Codec<($StructureTargetSelectorType<(any)>)>
const TARGET_SELECTOR_CODEC: $Codec<($StructureTargetSelector)>
const SELF: $StructureTargetSelectorType<($SelfTargetSelector)>
function register<C>(arg0: $ResourceLocation$Type, arg1: $Codec$Type<(C)>): $StructureTargetSelectorType<(C)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureTargetSelectorType$Type<C> = ($StructureTargetSelectorType<(C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureTargetSelectorType_<C> = $StructureTargetSelectorType$Type<(C)>;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/math/$Vector2f" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Vector2f {
 "x": float
 "y": float

constructor(arg0: $Vector2f$Type)
constructor(arg0: (float)[])
constructor(arg0: float, arg1: float)

public "length"(): float
public "dot"(arg0: $Vector2f$Type): float
public "lengthSquared"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector2f$Type = ($Vector2f);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector2f_ = $Vector2f$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/math/$Vector3f" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Vector3f {
 "x": float
 "y": float
 "z": float

constructor(arg0: $Vector3f$Type)
constructor(arg0: (float)[])
constructor(arg0: float, arg1: float, arg2: float)

public "length"(): float
public "dot"(arg0: $Vector3f$Type): float
public "lengthSquared"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Vector3f$Type = ($Vector3f);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Vector3f_ = $Vector3f$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/util/$BoxOctree" {
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export class $BoxOctree {

constructor(arg0: $AABB$Type)

public "addBox"(arg0: $AABB$Type): void
public "removeBox"(arg0: $AABB$Type): void
public "intersectsAnyBox"(arg0: $AABB$Type): boolean
public "boundaryContains"(arg0: $AABB$Type): boolean
public "boundaryIntersectsFuzzy"(arg0: $AABB$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BoxOctree$Type = ($BoxOctree);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BoxOctree_ = $BoxOctree$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/autoregister/$AutoRegisterField" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $AutoRegisterField {
 "object": any
 "name": $ResourceLocation
 "processed": boolean

constructor(arg0: any, arg1: $ResourceLocation$Type)

public "name"(): $ResourceLocation
public "object"(): any
public "processed"(): boolean
public "markProcessed"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoRegisterField$Type = ($AutoRegisterField);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoRegisterField_ = $AutoRegisterField$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/paxi/services/$Services" {
import {$IPlatformHelper, $IPlatformHelper$Type} from "packages/com/yungnickyoung/minecraft/paxi/services/$IPlatformHelper"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $Services {
static readonly "PLATFORM": $IPlatformHelper

constructor()

public static "load"<T>(arg0: $Class$Type<(T)>): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Services$Type = ($Services);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Services_ = $Services$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/targetselector/$StructureTargetSelector" {
import {$StructureTargetSelectorType, $StructureTargetSelectorType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/targetselector/$StructureTargetSelectorType"
import {$PieceEntry, $PieceEntry$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/$PieceEntry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"

export class $StructureTargetSelector {

constructor()

public "type"(): $StructureTargetSelectorType<(any)>
public "apply"(arg0: $StructureContext$Type): $List<($PieceEntry)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureTargetSelector$Type = ($StructureTargetSelector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureTargetSelector_ = $StructureTargetSelector$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$YungJigsawSinglePoolElement" {
import {$BoundingBox, $BoundingBox$Type} from "packages/net/minecraft/world/level/levelgen/structure/$BoundingBox"
import {$StructureTemplatePool$Projection, $StructureTemplatePool$Projection$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool$Projection"
import {$StructureProcessorList, $StructureProcessorList$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessorList"
import {$ChunkGenerator, $ChunkGenerator$Type} from "packages/net/minecraft/world/level/chunk/$ChunkGenerator"
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$EnhancedTerrainAdaptation"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$YungJigsawPoolElement, $YungJigsawPoolElement$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$YungJigsawPoolElement"
import {$WorldGenLevel, $WorldGenLevel$Type} from "packages/net/minecraft/world/level/$WorldGenLevel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$StructureTemplate, $StructureTemplate$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate"
import {$StructureModifier, $StructureModifier$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/modifier/$StructureModifier"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$StructurePoolElementType, $StructurePoolElementType$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElementType"
import {$RecordCodecBuilder, $RecordCodecBuilder$Type} from "packages/com/mojang/serialization/codecs/$RecordCodecBuilder"
import {$StructureCondition, $StructureCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureCondition"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$StructureTemplateManager, $StructureTemplateManager$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplateManager"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$StructureManager, $StructureManager$Type} from "packages/net/minecraft/world/level/$StructureManager"

export class $YungJigsawSinglePoolElement extends $YungJigsawPoolElement {
static readonly "CODEC": $Codec<($YungJigsawSinglePoolElement)>
readonly "template": $Either<($ResourceLocation), ($StructureTemplate)>
readonly "processors": $Holder<($StructureProcessorList)>
readonly "deadendPool": $Optional<($ResourceLocation)>
readonly "modifiers": $List<($StructureModifier)>
readonly "name": $Optional<(string)>
readonly "maxCount": $Optional<(integer)>
/**
 * 
 * @deprecated
 */
readonly "minRequiredDepth": $Optional<(integer)>
/**
 * 
 * @deprecated
 */
readonly "maxPossibleDepth": $Optional<(integer)>
readonly "isPriority": boolean
readonly "ignoreBounds": boolean
readonly "condition": $StructureCondition
readonly "enhancedTerrainAdaptation": $Optional<($EnhancedTerrainAdaptation)>

constructor(arg0: $Either$Type<($ResourceLocation$Type), ($StructureTemplate$Type)>, arg1: $Holder$Type<($StructureProcessorList$Type)>, arg2: $StructureTemplatePool$Projection$Type, arg3: $Optional$Type<(string)>, arg4: $Optional$Type<(integer)>, arg5: $Optional$Type<(integer)>, arg6: $Optional$Type<(integer)>, arg7: boolean, arg8: boolean, arg9: $StructureCondition$Type, arg10: $Optional$Type<($EnhancedTerrainAdaptation$Type)>, arg11: $Optional$Type<($ResourceLocation$Type)>, arg12: $List$Type<($StructureModifier$Type)>)

public "toString"(): string
public static "processorsCodec"<E extends $YungJigsawSinglePoolElement>(): $RecordCodecBuilder<(E), ($Holder<($StructureProcessorList)>)>
public "getDeadendPool"(): $Optional<($ResourceLocation)>
public static "templateCodec"<E extends $YungJigsawSinglePoolElement>(): $RecordCodecBuilder<(E), ($Either<($ResourceLocation), ($StructureTemplate)>)>
public "hasModifiers"(): boolean
public "getBoundingBox"(arg0: $StructureTemplateManager$Type, arg1: $BlockPos$Type, arg2: $Rotation$Type): $BoundingBox
public "place"(arg0: $StructureTemplateManager$Type, arg1: $WorldGenLevel$Type, arg2: $StructureManager$Type, arg3: $ChunkGenerator$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type, arg6: $Rotation$Type, arg7: $BoundingBox$Type, arg8: $RandomSource$Type, arg9: boolean): boolean
public "getShuffledJigsawBlocks"(arg0: $StructureTemplateManager$Type, arg1: $BlockPos$Type, arg2: $Rotation$Type, arg3: $RandomSource$Type): $List<($StructureTemplate$StructureBlockInfo)>
public "getType"(): $StructurePoolElementType<(any)>
public "getSize"(arg0: $StructureTemplateManager$Type, arg1: $Rotation$Type): $Vec3i
public "getTemplate"(arg0: $StructureTemplateManager$Type): $StructureTemplate
get "deadendPool"(): $Optional<($ResourceLocation)>
get "type"(): $StructurePoolElementType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $YungJigsawSinglePoolElement$Type = ($YungJigsawSinglePoolElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $YungJigsawSinglePoolElement_ = $YungJigsawSinglePoolElement$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$SmallCarvedTopNoBeardAdaptation" {
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$EnhancedTerrainAdaptation"
import {$EnhancedTerrainAdaptationType, $EnhancedTerrainAdaptationType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$EnhancedTerrainAdaptationType"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $SmallCarvedTopNoBeardAdaptation extends $EnhancedTerrainAdaptation {
static readonly "CODEC": $Codec<($SmallCarvedTopNoBeardAdaptation)>
static readonly "NONE": $EnhancedTerrainAdaptation

constructor()

public "type"(): $EnhancedTerrainAdaptationType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SmallCarvedTopNoBeardAdaptation$Type = ($SmallCarvedTopNoBeardAdaptation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SmallCarvedTopNoBeardAdaptation_ = $SmallCarvedTopNoBeardAdaptation$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegisterCreativeTab" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$CreativeModeTab$Type, $CreativeModeTab$Type$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$Type"
import {$CreativeModeTab$DisplayItemsGenerator, $CreativeModeTab$DisplayItemsGenerator$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$DisplayItemsGenerator"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$AutoRegisterCreativeTab$Builder, $AutoRegisterCreativeTab$Builder$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegisterCreativeTab$Builder"
import {$AutoRegisterEntry, $AutoRegisterEntry$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/autoregister/$AutoRegisterEntry"

export class $AutoRegisterCreativeTab extends $AutoRegisterEntry<($CreativeModeTab)> {


public static "builder"(): $AutoRegisterCreativeTab$Builder
public "getType"(): $CreativeModeTab$Type
public "getDisplayName"(): $Component
public "getIconItemStackSupplier"(): $Supplier<($ItemStack)>
public "getDisplayItemsGenerator"(): $CreativeModeTab$DisplayItemsGenerator
public "getBackgroundSuffix"(): string
public "alignedRight"(): boolean
public "showTitle"(): boolean
public "canScroll"(): boolean
get "type"(): $CreativeModeTab$Type
get "displayName"(): $Component
get "iconItemStackSupplier"(): $Supplier<($ItemStack)>
get "displayItemsGenerator"(): $CreativeModeTab$DisplayItemsGenerator
get "backgroundSuffix"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoRegisterCreativeTab$Type = ($AutoRegisterCreativeTab);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoRegisterCreativeTab_ = $AutoRegisterCreativeTab$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$AnyOfCondition" {
import {$StructureCondition, $StructureCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureCondition"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureConditionType"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $AnyOfCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($AnyOfCondition)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(arg0: $List$Type<($StructureCondition$Type)>)

public "type"(): $StructureConditionType<(any)>
public "passes"(arg0: $StructureContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnyOfCondition$Type = ($AnyOfCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnyOfCondition_ = $AnyOfCondition$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/world/randomize/$ItemRandomizer" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$ItemRandomizer$Entry, $ItemRandomizer$Entry$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/api/world/randomize/$ItemRandomizer$Entry"
import {$Random, $Random$Type} from "packages/java/util/$Random"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ItemRandomizer {
static readonly "CODEC": $Codec<($ItemRandomizer)>

constructor(arg0: $Item$Type)
constructor(arg0: $List$Type<($ItemRandomizer$Entry$Type)>, arg1: $Item$Type)
constructor(arg0: $CompoundTag$Type)
constructor()

public "get"(arg0: $RandomSource$Type): $Item
public "get"(arg0: $Random$Type): $Item
public static "from"(...arg0: ($Item$Type)[]): $ItemRandomizer
public "getEntries"(): $List<($ItemRandomizer$Entry)>
public "getDefaultItem"(): $Item
public "addItem"(arg0: $Item$Type, arg1: float): $ItemRandomizer
public "saveTag"(): $CompoundTag
public "getEntriesAsMap"(): $Map<($Item), (float)>
public "setDefaultItem"(arg0: $Item$Type): void
get "entries"(): $List<($ItemRandomizer$Entry)>
get "defaultItem"(): $Item
get "entriesAsMap"(): $Map<($Item), (float)>
set "defaultItem"(value: $Item$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemRandomizer$Type = ($ItemRandomizer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemRandomizer_ = $ItemRandomizer$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/exclusion/$EnhancedExclusionZone" {
import {$HolderSet, $HolderSet$Type} from "packages/net/minecraft/core/$HolderSet"
import {$ChunkGeneratorStructureState, $ChunkGeneratorStructureState$Type} from "packages/net/minecraft/world/level/chunk/$ChunkGeneratorStructureState"
import {$StructureSet, $StructureSet$Type} from "packages/net/minecraft/world/level/levelgen/structure/$StructureSet"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $EnhancedExclusionZone {
static readonly "CODEC": $Codec<($EnhancedExclusionZone)>

constructor(arg0: $HolderSet$Type<($StructureSet$Type)>, arg1: integer)

public "isPlacementForbidden"(arg0: $ChunkGeneratorStructureState$Type, arg1: integer, arg2: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnhancedExclusionZone$Type = ($EnhancedExclusionZone);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnhancedExclusionZone_ = $EnhancedExclusionZone$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/io/$JSON" {
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$GsonBuilder, $GsonBuilder$Type} from "packages/com/google/gson/$GsonBuilder"

export class $JSON {
static "gson": $Gson


public static "toJsonString"(arg0: any): string
public static "toJsonString"(arg0: any, arg1: $Gson$Type): string
public static "newGsonBuilder"(): $GsonBuilder
public static "createJsonFileFromObject"(arg0: $Path$Type, arg1: any, arg2: $Gson$Type): void
public static "createJsonFileFromObject"(arg0: $Path$Type, arg1: any): void
public static "loadObjectFromJsonFile"<T>(arg0: $Path$Type, arg1: $Class$Type<(T)>, arg2: $Gson$Type): T
public static "loadObjectFromJsonFile"<T>(arg0: $Path$Type, arg1: $Class$Type<(T)>): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JSON$Type = ($JSON);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JSON_ = $JSON$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/paxi/mixin/accessor/$FolderRepositorySourceAccessor" {
import {$PackType, $PackType$Type} from "packages/net/minecraft/server/packs/$PackType"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"

export interface $FolderRepositorySourceAccessor {

 "getPackType"(): $PackType
 "getFolder"(): $Path
}

export namespace $FolderRepositorySourceAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FolderRepositorySourceAccessor$Type = ($FolderRepositorySourceAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FolderRepositorySourceAccessor_ = $FolderRepositorySourceAccessor$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/util/$BoundingBoxHelper" {
import {$BoundingBox, $BoundingBox$Type} from "packages/net/minecraft/world/level/levelgen/structure/$BoundingBox"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"

export class $BoundingBoxHelper {

constructor()

public static "boxFromCoordsWithRotation"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $Direction$Type): $BoundingBox
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BoundingBoxHelper$Type = ($BoundingBoxHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BoundingBoxHelper_ = $BoundingBoxHelper$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/noise/$FastNoise$CellularReturnType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $FastNoise$CellularReturnType extends $Enum<($FastNoise$CellularReturnType)> {
static readonly "CellValue": $FastNoise$CellularReturnType
static readonly "NoiseLookup": $FastNoise$CellularReturnType
static readonly "Distance": $FastNoise$CellularReturnType
static readonly "Distance2": $FastNoise$CellularReturnType
static readonly "Distance2Add": $FastNoise$CellularReturnType
static readonly "Distance2Sub": $FastNoise$CellularReturnType
static readonly "Distance2Mul": $FastNoise$CellularReturnType
static readonly "Distance2Div": $FastNoise$CellularReturnType


public static "values"(): ($FastNoise$CellularReturnType)[]
public static "valueOf"(arg0: string): $FastNoise$CellularReturnType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastNoise$CellularReturnType$Type = (("noiselookup") | ("distance2") | ("distance") | ("cellvalue") | ("distance2sub") | ("distance2add") | ("distance2div") | ("distance2mul")) | ($FastNoise$CellularReturnType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastNoise$CellularReturnType_ = $FastNoise$CellularReturnType$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/paxi/$PaxiForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PaxiForge {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PaxiForge$Type = ($PaxiForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PaxiForge_ = $PaxiForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$StructurePieceTypeModuleForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $StructurePieceTypeModuleForge {

constructor()

public static "processEntries"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructurePieceTypeModuleForge$Type = ($StructurePieceTypeModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructurePieceTypeModuleForge_ = $StructurePieceTypeModuleForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureCondition" {
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureConditionType"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"

export class $StructureCondition {
static readonly "ALWAYS_TRUE": $StructureCondition

constructor()

public "type"(): $StructureConditionType<(any)>
public "passes"(arg0: $StructureContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureCondition$Type = ($StructureCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureCondition_ = $StructureCondition$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$ItemModuleForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ItemModuleForge {

constructor()

public static "processEntries"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemModuleForge$Type = ($ItemModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemModuleForge_ = $ItemModuleForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$YungJigsawPoolElement" {
import {$RecordCodecBuilder, $RecordCodecBuilder$Type} from "packages/com/mojang/serialization/codecs/$RecordCodecBuilder"
import {$StructureTemplatePool$Projection, $StructureTemplatePool$Projection$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool$Projection"
import {$StructureCondition, $StructureCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureCondition"
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$EnhancedTerrainAdaptation"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$StructurePoolElement, $StructurePoolElement$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElement"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $YungJigsawPoolElement extends $StructurePoolElement {
readonly "name": $Optional<(string)>
readonly "maxCount": $Optional<(integer)>
/**
 * 
 * @deprecated
 */
readonly "minRequiredDepth": $Optional<(integer)>
/**
 * 
 * @deprecated
 */
readonly "maxPossibleDepth": $Optional<(integer)>
readonly "isPriority": boolean
readonly "ignoreBounds": boolean
readonly "condition": $StructureCondition
readonly "enhancedTerrainAdaptation": $Optional<($EnhancedTerrainAdaptation)>
static readonly "CODEC": $Codec<($StructurePoolElement)>

constructor(arg0: $StructureTemplatePool$Projection$Type, arg1: $Optional$Type<(string)>, arg2: $Optional$Type<(integer)>, arg3: $Optional$Type<(integer)>, arg4: $Optional$Type<(integer)>, arg5: boolean, arg6: boolean, arg7: $StructureCondition$Type, arg8: $Optional$Type<($EnhancedTerrainAdaptation$Type)>)

/**
 * 
 * @deprecated
 */
public "getMinRequiredDepth"(): $Optional<(integer)>
public static "minRequiredDepthCodec"<E extends $YungJigsawPoolElement>(): $RecordCodecBuilder<(E), ($Optional<(integer)>)>
public static "maxPossibleDepthCodec"<E extends $YungJigsawPoolElement>(): $RecordCodecBuilder<(E), ($Optional<(integer)>)>
public "getEnhancedTerrainAdaptation"(): $Optional<($EnhancedTerrainAdaptation)>
/**
 * 
 * @deprecated
 */
public "getMaxPossibleDepth"(): $Optional<(integer)>
public static "enhancedTerrainAdaptationCodec"<E extends $YungJigsawPoolElement>(): $RecordCodecBuilder<(E), ($Optional<($EnhancedTerrainAdaptation)>)>
public "getName"(): $Optional<(string)>
public static "conditionCodec"<E extends $YungJigsawPoolElement>(): $RecordCodecBuilder<(E), ($StructureCondition)>
public "isPriorityPiece"(): boolean
public static "ignoreBoundsCodec"<E extends $YungJigsawPoolElement>(): $RecordCodecBuilder<(E), (boolean)>
public "isAtValidDepth"(arg0: integer): boolean
public static "isPriorityCodec"<E extends $YungJigsawPoolElement>(): $RecordCodecBuilder<(E), (boolean)>
public static "nameCodec"<E extends $YungJigsawPoolElement>(): $RecordCodecBuilder<(E), ($Optional<(string)>)>
public "passesConditions"(arg0: $StructureContext$Type): boolean
public "ignoresBounds"(): boolean
public static "maxCountCodec"<E extends $YungJigsawPoolElement>(): $RecordCodecBuilder<(E), ($Optional<(integer)>)>
public "getMaxCount"(): $Optional<(integer)>
public "getCondition"(): $StructureCondition
get "minRequiredDepth"(): $Optional<(integer)>
get "enhancedTerrainAdaptation"(): $Optional<($EnhancedTerrainAdaptation)>
get "maxPossibleDepth"(): $Optional<(integer)>
get "name"(): $Optional<(string)>
get "priorityPiece"(): boolean
get "priorityCodec"(): boolean
get "maxCount"(): $Optional<(integer)>
get "condition"(): $StructureCondition
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $YungJigsawPoolElement$Type = ($YungJigsawPoolElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $YungJigsawPoolElement_ = $YungJigsawPoolElement$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/services/$ForgeAutoRegisterHelper" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$IAutoRegisterHelper, $IAutoRegisterHelper$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/services/$IAutoRegisterHelper"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"

export class $ForgeAutoRegisterHelper implements $IAutoRegisterHelper {

constructor()

public "addCompostableItem"(arg0: $Supplier$Type<($Item$Type)>, arg1: float): void
public "collectAllAutoRegisterFieldsInPackage"(arg0: string): void
public "processQueuedAutoRegEntries"(): void
public "invokeAllAutoRegisterMethods"(arg0: string): void
public "registerBrewingRecipe"(arg0: $Supplier$Type<($Potion$Type)>, arg1: $Supplier$Type<($Item$Type)>, arg2: $Supplier$Type<($Potion$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeAutoRegisterHelper$Type = ($ForgeAutoRegisterHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeAutoRegisterHelper_ = $ForgeAutoRegisterHelper$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/paxi/$PaxiCommon" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $PaxiCommon {
static readonly "MOD_ID": string
static "BASE_PACK_DIRECTORY": $File
static "DATA_PACK_DIRECTORY": $Path
static "RESOURCE_PACK_DIRECTORY": $Path
static "DATAPACK_ORDERING_FILE": $File
static "RESOURCEPACK_ORDERING_FILE": $File
static readonly "LOGGER": $Logger

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PaxiCommon$Type = ($PaxiCommon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PaxiCommon_ = $PaxiCommon$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureConditionType" {
import {$PieceInHorizontalDirectionCondition, $PieceInHorizontalDirectionCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$PieceInHorizontalDirectionCondition"
import {$StructureCondition, $StructureCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureCondition"
import {$NotCondition, $NotCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$NotCondition"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$AlwaysTrueCondition, $AlwaysTrueCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$AlwaysTrueCondition"
import {$PieceInRangeCondition, $PieceInRangeCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$PieceInRangeCondition"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$ModLoadedCondition, $ModLoadedCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$ModLoadedCondition"
import {$AltitudeCondition, $AltitudeCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$AltitudeCondition"
import {$RotationCondition, $RotationCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$RotationCondition"
import {$ModLoaderCondition, $ModLoaderCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$ModLoaderCondition"
import {$AnyOfCondition, $AnyOfCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$AnyOfCondition"
import {$DepthCondition, $DepthCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$DepthCondition"
import {$AllOfCondition, $AllOfCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$AllOfCondition"
import {$RandomChanceCondition, $RandomChanceCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$RandomChanceCondition"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $StructureConditionType<C extends $StructureCondition> {

 "codec"(): $Codec<(C)>

(arg0: $ResourceLocation$Type, arg1: $Codec$Type<(C)>): $StructureConditionType<(C)>
}

export namespace $StructureConditionType {
const CONDITION_TYPES_BY_NAME: $Map<($ResourceLocation), ($StructureConditionType<(any)>)>
const NAME_BY_CONDITION_TYPES: $Map<($StructureConditionType<(any)>), ($ResourceLocation)>
const CONDITION_TYPE_CODEC: $Codec<($StructureConditionType<(any)>)>
const CONDITION_CODEC: $Codec<($StructureCondition)>
const ALWAYS_TRUE: $StructureConditionType<($AlwaysTrueCondition)>
const ANY_OF: $StructureConditionType<($AnyOfCondition)>
const ALL_OF: $StructureConditionType<($AllOfCondition)>
const NOT: $StructureConditionType<($NotCondition)>
const ALTITUDE: $StructureConditionType<($AltitudeCondition)>
const DEPTH: $StructureConditionType<($DepthCondition)>
const RANDOM_CHANCE: $StructureConditionType<($RandomChanceCondition)>
const PIECE_IN_RANGE: $StructureConditionType<($PieceInRangeCondition)>
const MOD_LOADER: $StructureConditionType<($ModLoaderCondition)>
const MOD_LOADED: $StructureConditionType<($ModLoadedCondition)>
const PIECE_IN_HORIZONTAL_DIRECTION: $StructureConditionType<($PieceInHorizontalDirectionCondition)>
const ROTATION: $StructureConditionType<($RotationCondition)>
function register<C>(arg0: $ResourceLocation$Type, arg1: $Codec$Type<(C)>): $StructureConditionType<(C)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureConditionType$Type<C> = ($StructureConditionType<(C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureConditionType_<C> = $StructureConditionType$Type<(C)>;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/modifier/$StructureModifier" {
import {$StructureCondition, $StructureCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureCondition"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructureAction, $StructureAction$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/action/$StructureAction"
import {$StructureTargetSelector, $StructureTargetSelector$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/targetselector/$StructureTargetSelector"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $StructureModifier {
static readonly "CODEC": $Codec<($StructureModifier)>

constructor(arg0: $StructureCondition$Type, arg1: $List$Type<($StructureAction$Type)>, arg2: $StructureTargetSelector$Type)

public "apply"(arg0: $StructureContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureModifier$Type = ($StructureModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureModifier_ = $StructureModifier$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$StructureTypeModuleForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $StructureTypeModuleForge {

constructor()

public static "processEntries"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureTypeModuleForge$Type = ($StructureTypeModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureTypeModuleForge_ = $StructureTypeModuleForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$MaxCountFeaturePoolElement" {
import {$FeaturePoolElement, $FeaturePoolElement$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$FeaturePoolElement"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$StructureTemplatePool$Projection, $StructureTemplatePool$Projection$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool$Projection"
import {$PlacedFeature, $PlacedFeature$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacedFeature"
import {$IMaxCountJigsawPoolElement, $IMaxCountJigsawPoolElement$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$IMaxCountJigsawPoolElement"
import {$StructurePoolElementType, $StructurePoolElementType$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElementType"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

/**
 * 
 * @deprecated
 */
export class $MaxCountFeaturePoolElement extends $FeaturePoolElement implements $IMaxCountJigsawPoolElement {
static readonly "CODEC": $Codec<($MaxCountFeaturePoolElement)>

constructor(arg0: $Holder$Type<($PlacedFeature$Type)>, arg1: $StructureTemplatePool$Projection$Type, arg2: string, arg3: integer)

public "getName"(): string
public "toString"(): string
public "getMaxCount"(): integer
public "getType"(): $StructurePoolElementType<(any)>
get "name"(): string
get "maxCount"(): integer
get "type"(): $StructurePoolElementType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MaxCountFeaturePoolElement$Type = ($MaxCountFeaturePoolElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MaxCountFeaturePoolElement_ = $MaxCountFeaturePoolElement$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$PotionModuleForge" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$IBrewingRecipe, $IBrewingRecipe$Type} from "packages/net/minecraftforge/common/brewing/$IBrewingRecipe"

export class $PotionModuleForge {
static readonly "BREWING_RECIPES": $List<($IBrewingRecipe)>

constructor()

public static "registerBrewingRecipes"(): void
public static "processEntries"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PotionModuleForge$Type = ($PotionModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PotionModuleForge_ = $PotionModuleForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/criteria/$SafeStructureLocationTrigger" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$SimpleCriterionTrigger, $SimpleCriterionTrigger$Type} from "packages/net/minecraft/advancements/critereon/$SimpleCriterionTrigger"
import {$SafeStructureLocationTrigger$TriggerInstance, $SafeStructureLocationTrigger$TriggerInstance$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/criteria/$SafeStructureLocationTrigger$TriggerInstance"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$DeserializationContext, $DeserializationContext$Type} from "packages/net/minecraft/advancements/critereon/$DeserializationContext"
import {$ContextAwarePredicate, $ContextAwarePredicate$Type} from "packages/net/minecraft/advancements/critereon/$ContextAwarePredicate"

export class $SafeStructureLocationTrigger extends $SimpleCriterionTrigger<($SafeStructureLocationTrigger$TriggerInstance)> {

constructor(arg0: $ResourceLocation$Type)

public "trigger"(arg0: $Player$Type): void
public "createInstance"(arg0: $JsonObject$Type, arg1: $ContextAwarePredicate$Type, arg2: $DeserializationContext$Type): $SafeStructureLocationTrigger$TriggerInstance
public "getId"(): $ResourceLocation
get "id"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SafeStructureLocationTrigger$Type = ($SafeStructureLocationTrigger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SafeStructureLocationTrigger_ = $SafeStructureLocationTrigger$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$CompostModuleForge" {
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$Object2FloatMap, $Object2FloatMap$Type} from "packages/it/unimi/dsi/fastutil/objects/$Object2FloatMap"

export class $CompostModuleForge {
static readonly "COMPOSTABLES": $Object2FloatMap<($ItemLike)>

constructor()

public static "registerCompostables"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompostModuleForge$Type = ($CompostModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompostModuleForge_ = $CompostModuleForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$MobEffectModuleForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MobEffectModuleForge {

constructor()

public static "processEntries"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobEffectModuleForge$Type = ($MobEffectModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobEffectModuleForge_ = $MobEffectModuleForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/beardifier/$EnhancedBeardifierData" {
import {$ObjectListIterator, $ObjectListIterator$Type} from "packages/it/unimi/dsi/fastutil/objects/$ObjectListIterator"
import {$EnhancedBeardifierRigid, $EnhancedBeardifierRigid$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/beardifier/$EnhancedBeardifierRigid"
import {$EnhancedJigsawJunction, $EnhancedJigsawJunction$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/beardifier/$EnhancedJigsawJunction"

export interface $EnhancedBeardifierData {

 "setEnhancedRigidIterator"(arg0: $ObjectListIterator$Type<($EnhancedBeardifierRigid$Type)>): void
 "getEnhancedRigidIterator"(): $ObjectListIterator<($EnhancedBeardifierRigid)>
 "getEnhancedJunctionIterator"(): $ObjectListIterator<($EnhancedJigsawJunction)>
 "setEnhancedJunctionIterator"(arg0: $ObjectListIterator$Type<($EnhancedJigsawJunction$Type)>): void
}

export namespace $EnhancedBeardifierData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnhancedBeardifierData$Type = ($EnhancedBeardifierData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnhancedBeardifierData_ = $EnhancedBeardifierData$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegister" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $AutoRegister extends $Annotation {

 "value"(): string
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $AutoRegister {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoRegister$Type = ($AutoRegister);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoRegister_ = $AutoRegister$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$EnhancedTerrainAdaptationType" {
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$EnhancedTerrainAdaptation"
import {$NoneAdaptation, $NoneAdaptation$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$NoneAdaptation"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LargeCarvedTopNoBeardAdaptation, $LargeCarvedTopNoBeardAdaptation$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$LargeCarvedTopNoBeardAdaptation"
import {$SmallCarvedTopNoBeardAdaptation, $SmallCarvedTopNoBeardAdaptation$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$SmallCarvedTopNoBeardAdaptation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$CustomAdaptation, $CustomAdaptation$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$CustomAdaptation"

export interface $EnhancedTerrainAdaptationType<C extends $EnhancedTerrainAdaptation> {

 "codec"(): $Codec<(C)>

(arg0: $ResourceLocation$Type, arg1: $Codec$Type<(C)>): $EnhancedTerrainAdaptationType<(C)>
}

export namespace $EnhancedTerrainAdaptationType {
const ADAPTATION_TYPES_BY_NAME: $Map<($ResourceLocation), ($EnhancedTerrainAdaptationType<(any)>)>
const NAME_BY_ADAPTATION_TYPES: $Map<($EnhancedTerrainAdaptationType<(any)>), ($ResourceLocation)>
const ADAPTATION_TYPE_CODEC: $Codec<($EnhancedTerrainAdaptationType<(any)>)>
const ADAPTATION_CODEC: $Codec<($EnhancedTerrainAdaptation)>
const NONE: $EnhancedTerrainAdaptationType<($NoneAdaptation)>
const LARGE_CARVED_TOP_NO_BEARD: $EnhancedTerrainAdaptationType<($LargeCarvedTopNoBeardAdaptation)>
const SMALL_CARVED_TOP_NO_BEARD: $EnhancedTerrainAdaptationType<($SmallCarvedTopNoBeardAdaptation)>
const CUSTOM: $EnhancedTerrainAdaptationType<($CustomAdaptation)>
function register<C>(arg0: $ResourceLocation$Type, arg1: $Codec$Type<(C)>): $EnhancedTerrainAdaptationType<(C)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnhancedTerrainAdaptationType$Type<C> = ($EnhancedTerrainAdaptationType<(C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnhancedTerrainAdaptationType_<C> = $EnhancedTerrainAdaptationType$Type<(C)>;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$RandomChanceCondition" {
import {$StructureCondition, $StructureCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureCondition"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureConditionType"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $RandomChanceCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($RandomChanceCondition)>
readonly "chance": float
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(arg0: float)

public "type"(): $StructureConditionType<(any)>
public "passes"(arg0: $StructureContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RandomChanceCondition$Type = ($RandomChanceCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RandomChanceCondition_ = $RandomChanceCondition$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/util/$SurfaceHelper" {
import {$ColumnPos, $ColumnPos$Type} from "packages/net/minecraft/server/level/$ColumnPos"
import {$ChunkAccess, $ChunkAccess$Type} from "packages/net/minecraft/world/level/chunk/$ChunkAccess"

export class $SurfaceHelper {


public static "getSurfaceHeight"(arg0: $ChunkAccess$Type, arg1: $ColumnPos$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SurfaceHelper$Type = ($SurfaceHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SurfaceHelper_ = $SurfaceHelper$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/criteria/$SafeStructureLocationTrigger$TriggerInstance" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$SafeStructureLocationPredicate, $SafeStructureLocationPredicate$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/criteria/$SafeStructureLocationPredicate"
import {$AbstractCriterionTriggerInstance, $AbstractCriterionTriggerInstance$Type} from "packages/net/minecraft/advancements/critereon/$AbstractCriterionTriggerInstance"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$SerializationContext, $SerializationContext$Type} from "packages/net/minecraft/advancements/critereon/$SerializationContext"
import {$ContextAwarePredicate, $ContextAwarePredicate$Type} from "packages/net/minecraft/advancements/critereon/$ContextAwarePredicate"

export class $SafeStructureLocationTrigger$TriggerInstance extends $AbstractCriterionTriggerInstance {

constructor(arg0: $ResourceLocation$Type, arg1: $ContextAwarePredicate$Type, arg2: $SafeStructureLocationPredicate$Type)

public "matches"(arg0: $ServerLevel$Type, arg1: double, arg2: double, arg3: double): boolean
public static "located"(arg0: $SafeStructureLocationPredicate$Type): $SafeStructureLocationTrigger$TriggerInstance
public "serializeToJson"(arg0: $SerializationContext$Type): $JsonObject
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SafeStructureLocationTrigger$TriggerInstance$Type = ($SafeStructureLocationTrigger$TriggerInstance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SafeStructureLocationTrigger$TriggerInstance_ = $SafeStructureLocationTrigger$TriggerInstance$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/beardifier/$EnhancedBeardifierRigid" {
import {$BoundingBox, $BoundingBox$Type} from "packages/net/minecraft/world/level/levelgen/structure/$BoundingBox"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$EnhancedTerrainAdaptation"

export class $EnhancedBeardifierRigid extends $Record {

constructor(pieceBoundingBox: $BoundingBox$Type, pieceTerrainAdaptation: $EnhancedTerrainAdaptation$Type, pieceGroundLevelDelta: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "pieceGroundLevelDelta"(): integer
public "pieceBoundingBox"(): $BoundingBox
public "pieceTerrainAdaptation"(): $EnhancedTerrainAdaptation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnhancedBeardifierRigid$Type = ($EnhancedBeardifierRigid);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnhancedBeardifierRigid_ = $EnhancedBeardifierRigid$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$YungJigsawFeatureElement" {
import {$BoundingBox, $BoundingBox$Type} from "packages/net/minecraft/world/level/levelgen/structure/$BoundingBox"
import {$StructureTemplatePool$Projection, $StructureTemplatePool$Projection$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool$Projection"
import {$PlacedFeature, $PlacedFeature$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacedFeature"
import {$ChunkGenerator, $ChunkGenerator$Type} from "packages/net/minecraft/world/level/chunk/$ChunkGenerator"
import {$StructureCondition, $StructureCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureCondition"
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$EnhancedTerrainAdaptation"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$YungJigsawPoolElement, $YungJigsawPoolElement$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$YungJigsawPoolElement"
import {$WorldGenLevel, $WorldGenLevel$Type} from "packages/net/minecraft/world/level/$WorldGenLevel"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$StructureTemplateManager, $StructureTemplateManager$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplateManager"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$StructureManager, $StructureManager$Type} from "packages/net/minecraft/world/level/$StructureManager"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$StructurePoolElementType, $StructurePoolElementType$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElementType"

export class $YungJigsawFeatureElement extends $YungJigsawPoolElement {
static readonly "CODEC": $Codec<($YungJigsawFeatureElement)>
readonly "name": $Optional<(string)>
readonly "maxCount": $Optional<(integer)>
/**
 * 
 * @deprecated
 */
readonly "minRequiredDepth": $Optional<(integer)>
/**
 * 
 * @deprecated
 */
readonly "maxPossibleDepth": $Optional<(integer)>
readonly "isPriority": boolean
readonly "ignoreBounds": boolean
readonly "condition": $StructureCondition
readonly "enhancedTerrainAdaptation": $Optional<($EnhancedTerrainAdaptation)>

constructor(arg0: $Holder$Type<($PlacedFeature$Type)>, arg1: $StructureTemplatePool$Projection$Type, arg2: $Optional$Type<(string)>, arg3: $Optional$Type<(integer)>, arg4: $Optional$Type<(integer)>, arg5: $Optional$Type<(integer)>, arg6: boolean, arg7: boolean, arg8: $StructureCondition$Type, arg9: $Optional$Type<($EnhancedTerrainAdaptation$Type)>)

public "toString"(): string
public "getBoundingBox"(arg0: $StructureTemplateManager$Type, arg1: $BlockPos$Type, arg2: $Rotation$Type): $BoundingBox
public "place"(arg0: $StructureTemplateManager$Type, arg1: $WorldGenLevel$Type, arg2: $StructureManager$Type, arg3: $ChunkGenerator$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type, arg6: $Rotation$Type, arg7: $BoundingBox$Type, arg8: $RandomSource$Type, arg9: boolean): boolean
public "getShuffledJigsawBlocks"(arg0: $StructureTemplateManager$Type, arg1: $BlockPos$Type, arg2: $Rotation$Type, arg3: $RandomSource$Type): $List<($StructureTemplate$StructureBlockInfo)>
public "getType"(): $StructurePoolElementType<(any)>
public "getSize"(arg0: $StructureTemplateManager$Type, arg1: $Rotation$Type): $Vec3i
get "type"(): $StructurePoolElementType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $YungJigsawFeatureElement$Type = ($YungJigsawFeatureElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $YungJigsawFeatureElement_ = $YungJigsawFeatureElement$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/mixin/accessor/$StructureTemplatePoolAccessor" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$StructurePoolElement, $StructurePoolElement$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElement"

export interface $StructureTemplatePoolAccessor {

 "getRawTemplates"(): $List<($Pair<($StructurePoolElement), (integer)>)>

(): $List<($Pair<($StructurePoolElement), (integer)>)>
}

export namespace $StructureTemplatePoolAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureTemplatePoolAccessor$Type = ($StructureTemplatePoolAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureTemplatePoolAccessor_ = $StructureTemplatePoolAccessor$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/mixin/accessor/$PotionBrewingAccessor" {
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
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/action/$TransformAction" {
import {$PieceEntry, $PieceEntry$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/$PieceEntry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructureAction, $StructureAction$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/action/$StructureAction"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$StructureTemplate, $StructureTemplate$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate"
import {$StructureActionType, $StructureActionType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/action/$StructureActionType"

export class $TransformAction extends $StructureAction {
static readonly "CODEC": $Codec<($TransformAction)>

constructor(arg0: $List$Type<($Either$Type<($ResourceLocation$Type), ($StructureTemplate$Type)>)>, arg1: integer, arg2: integer, arg3: integer)

public "type"(): $StructureActionType<(any)>
public "apply"(arg0: $StructureContext$Type, arg1: $PieceEntry$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TransformAction$Type = ($TransformAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TransformAction_ = $TransformAction$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$EntityTypeModuleForge" {
import {$AutoRegisterEntityType, $AutoRegisterEntityType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegisterEntityType"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AttributeSupplier$Builder, $AttributeSupplier$Builder$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeSupplier$Builder"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $EntityTypeModuleForge {
static readonly "ENTITY_ATTRIBUTES": $Map<($AutoRegisterEntityType<(any)>), ($Supplier<($AttributeSupplier$Builder)>)>

constructor()

public static "processEntries"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityTypeModuleForge$Type = ($EntityTypeModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityTypeModuleForge_ = $EntityTypeModuleForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegisterSoundEvent" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$AutoRegisterEntry, $AutoRegisterEntry$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/autoregister/$AutoRegisterEntry"

export class $AutoRegisterSoundEvent extends $AutoRegisterEntry<($SoundEvent)> {


public static "create"(): $AutoRegisterSoundEvent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoRegisterSoundEvent$Type = ($AutoRegisterSoundEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoRegisterSoundEvent_ = $AutoRegisterSoundEvent$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegisterCommand" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandBuildContext, $CommandBuildContext$Type} from "packages/net/minecraft/commands/$CommandBuildContext"
import {$TriConsumer, $TriConsumer$Type} from "packages/org/apache/logging/log4j/util/$TriConsumer"
import {$Commands$CommandSelection, $Commands$CommandSelection$Type} from "packages/net/minecraft/commands/$Commands$CommandSelection"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export class $AutoRegisterCommand {


public static "of"(arg0: $TriConsumer$Type<($CommandDispatcher$Type<($CommandSourceStack$Type)>), ($CommandBuildContext$Type), ($Commands$CommandSelection$Type)>): $AutoRegisterCommand
public "invokeHandler"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>, arg1: $CommandBuildContext$Type, arg2: $Commands$CommandSelection$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoRegisterCommand$Type = ($AutoRegisterCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoRegisterCommand_ = $AutoRegisterCommand$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/$YungsApiForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $YungsApiForge {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $YungsApiForge$Type = ($YungsApiForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $YungsApiForge_ = $YungsApiForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/paxi/services/$ForgePlatformHelper" {
import {$IPlatformHelper, $IPlatformHelper$Type} from "packages/com/yungnickyoung/minecraft/paxi/services/$IPlatformHelper"

export class $ForgePlatformHelper implements $IPlatformHelper {

constructor()

public "isDevelopmentEnvironment"(): boolean
public "getPlatformName"(): string
public "isModLoaded"(arg0: string): boolean
get "developmentEnvironment"(): boolean
get "platformName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgePlatformHelper$Type = ($ForgePlatformHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgePlatformHelper_ = $ForgePlatformHelper$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/$YungJigsawStructure" {
import {$BoundingBox, $BoundingBox$Type} from "packages/net/minecraft/world/level/levelgen/structure/$BoundingBox"
import {$StructureTemplatePool, $StructureTemplatePool$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool"
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$EnhancedTerrainAdaptation"
import {$StructureType, $StructureType$Type} from "packages/net/minecraft/world/level/levelgen/structure/$StructureType"
import {$Heightmap$Types, $Heightmap$Types$Type} from "packages/net/minecraft/world/level/levelgen/$Heightmap$Types"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Structure, $Structure$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$HeightProvider, $HeightProvider$Type} from "packages/net/minecraft/world/level/levelgen/heightproviders/$HeightProvider"
import {$IntProvider, $IntProvider$Type} from "packages/net/minecraft/util/valueproviders/$IntProvider"
import {$Structure$GenerationStub, $Structure$GenerationStub$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$GenerationStub"
import {$Structure$GenerationContext, $Structure$GenerationContext$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$GenerationContext"
import {$Structure$StructureSettings, $Structure$StructureSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$StructureSettings"

export class $YungJigsawStructure extends $Structure {
static readonly "MAX_TOTAL_STRUCTURE_RADIUS": integer
static readonly "CODEC": $Codec<($YungJigsawStructure)>
readonly "startPool": $Holder<($StructureTemplatePool)>
readonly "maxDepth": integer
readonly "startHeight": $HeightProvider
readonly "xOffsetInChunk": $IntProvider
readonly "zOffsetInChunk": $IntProvider
readonly "useExpansionHack": boolean
readonly "projectStartToHeightmap": $Optional<($Heightmap$Types)>
readonly "maxDistanceFromCenter": integer
readonly "maxY": $Optional<(integer)>
readonly "minY": $Optional<(integer)>
readonly "enhancedTerrainAdaptation": $EnhancedTerrainAdaptation
static readonly "DIRECT_CODEC": $Codec<($Structure)>

constructor(arg0: $Structure$StructureSettings$Type, arg1: $Holder$Type<($StructureTemplatePool$Type)>, arg2: $Optional$Type<($ResourceLocation$Type)>, arg3: integer, arg4: $HeightProvider$Type, arg5: $IntProvider$Type, arg6: $IntProvider$Type, arg7: boolean, arg8: $Optional$Type<($Heightmap$Types$Type)>, arg9: integer, arg10: $Optional$Type<(integer)>, arg11: $Optional$Type<(integer)>, arg12: $EnhancedTerrainAdaptation$Type)

public "type"(): $StructureType<(any)>
public "adjustBoundingBox"(arg0: $BoundingBox$Type): $BoundingBox
public "m_214086_"(arg0: $Structure$GenerationContext$Type): $Optional<($Structure$GenerationStub)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $YungJigsawStructure$Type = ($YungJigsawStructure);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $YungJigsawStructure_ = $YungJigsawStructure$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/$YungJigsawManager" {
import {$StructureTemplatePool, $StructureTemplatePool$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Heightmap$Types, $Heightmap$Types$Type} from "packages/net/minecraft/world/level/levelgen/$Heightmap$Types"
import {$Structure$GenerationStub, $Structure$GenerationStub$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$GenerationStub"
import {$Structure$GenerationContext, $Structure$GenerationContext$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$GenerationContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $YungJigsawManager {

constructor()

public static "assembleJigsawStructure"(arg0: $Structure$GenerationContext$Type, arg1: $Holder$Type<($StructureTemplatePool$Type)>, arg2: $Optional$Type<($ResourceLocation$Type)>, arg3: integer, arg4: $BlockPos$Type, arg5: boolean, arg6: $Optional$Type<($Heightmap$Types$Type)>, arg7: integer, arg8: $Optional$Type<(integer)>, arg9: $Optional$Type<(integer)>): $Optional<($Structure$GenerationStub)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $YungJigsawManager$Type = ($YungJigsawManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $YungJigsawManager_ = $YungJigsawManager$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$RotationCondition" {
import {$StructureCondition, $StructureCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureCondition"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureConditionType"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $RotationCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($RotationCondition)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(arg0: $List$Type<($Rotation$Type)>)

public "type"(): $StructureConditionType<(any)>
public "passes"(arg0: $StructureContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RotationCondition$Type = ($RotationCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RotationCondition_ = $RotationCondition$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/paxi/$PaxiPackSource" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$PackSource, $PackSource$Type} from "packages/net/minecraft/server/packs/repository/$PackSource"

export interface $PaxiPackSource extends $PackSource {

 "decorate"(arg0: $Component$Type): $Component
 "shouldAddAutomatically"(): boolean
}

export namespace $PaxiPackSource {
const PACK_SOURCE_PAXI: $PackSource
function decorateWithPaxiSource(): $UnaryOperator<($Component)>
function create(arg0: $UnaryOperator$Type<($Component$Type)>, arg1: boolean): $PackSource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PaxiPackSource$Type = ($PaxiPackSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PaxiPackSource_ = $PaxiPackSource$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegisterBlockEntityType" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$AutoRegisterEntry, $AutoRegisterEntry$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/autoregister/$AutoRegisterEntry"

export class $AutoRegisterBlockEntityType<T extends $BlockEntity> extends $AutoRegisterEntry<($BlockEntityType<(T)>)> {


public static "of"<U extends $BlockEntity>(arg0: $Supplier$Type<($BlockEntityType$Type<(U)>)>): $AutoRegisterBlockEntityType<(U)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoRegisterBlockEntityType$Type<T> = ($AutoRegisterBlockEntityType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoRegisterBlockEntityType_<T> = $AutoRegisterBlockEntityType$Type<(T)>;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$CriteriaModuleForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CriteriaModuleForge {

constructor()

public static "processEntries"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CriteriaModuleForge$Type = ($CriteriaModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CriteriaModuleForge_ = $CriteriaModuleForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/assembler/$JigsawStructureAssembler$Settings" {
import {$StructureTemplatePool, $StructureTemplatePool$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool"
import {$StructureTemplateManager, $StructureTemplateManager$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplateManager"
import {$LevelHeightAccessor, $LevelHeightAccessor$Type} from "packages/net/minecraft/world/level/$LevelHeightAccessor"
import {$ChunkGenerator, $ChunkGenerator$Type} from "packages/net/minecraft/world/level/chunk/$ChunkGenerator"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$RandomState, $RandomState$Type} from "packages/net/minecraft/world/level/levelgen/$RandomState"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"

export class $JigsawStructureAssembler$Settings {
 "randomState": $RandomState

constructor()

public "levelHeightAccessor"(arg0: $LevelHeightAccessor$Type): $JigsawStructureAssembler$Settings
public "maxDepth"(arg0: integer): $JigsawStructureAssembler$Settings
public "rand"(arg0: $RandomSource$Type): $JigsawStructureAssembler$Settings
public "poolRegistry"(arg0: $Registry$Type<($StructureTemplatePool$Type)>): $JigsawStructureAssembler$Settings
public "chunkGenerator"(arg0: $ChunkGenerator$Type): $JigsawStructureAssembler$Settings
public "minY"(arg0: $Optional$Type<(integer)>): $JigsawStructureAssembler$Settings
public "maxY"(arg0: $Optional$Type<(integer)>): $JigsawStructureAssembler$Settings
public "randomState"(arg0: $RandomState$Type): $JigsawStructureAssembler$Settings
public "structureTemplateManager"(arg0: $StructureTemplateManager$Type): $JigsawStructureAssembler$Settings
public "useExpansionHack"(arg0: boolean): $JigsawStructureAssembler$Settings
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JigsawStructureAssembler$Settings$Type = ($JigsawStructureAssembler$Settings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JigsawStructureAssembler$Settings_ = $JigsawStructureAssembler$Settings$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/spawner/$MobSpawnerData$Builder" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$SimpleWeightedRandomList, $SimpleWeightedRandomList$Type} from "packages/net/minecraft/util/random/$SimpleWeightedRandomList"
import {$SpawnData, $SpawnData$Type} from "packages/net/minecraft/world/level/$SpawnData"
import {$MobSpawnerData, $MobSpawnerData$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/spawner/$MobSpawnerData"

export class $MobSpawnerData$Builder {

constructor()

public "requiredPlayerRange"(arg0: integer): $MobSpawnerData$Builder
public "build"(): $MobSpawnerData
public "spawnCount"(arg0: integer): $MobSpawnerData$Builder
public "spawnRange"(arg0: integer): $MobSpawnerData$Builder
public "setEntityType"(arg0: $EntityType$Type<(any)>): $MobSpawnerData$Builder
public "spawnDelay"(arg0: integer): $MobSpawnerData$Builder
public "spawnPotentials"(arg0: $SimpleWeightedRandomList$Type<($SpawnData$Type)>): $MobSpawnerData$Builder
public "maxNearbyEntities"(arg0: integer): $MobSpawnerData$Builder
public "minSpawnDelay"(arg0: integer): $MobSpawnerData$Builder
public "maxSpawnDelay"(arg0: integer): $MobSpawnerData$Builder
set "entityType"(value: $EntityType$Type<(any)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobSpawnerData$Builder$Type = ($MobSpawnerData$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobSpawnerData$Builder_ = $MobSpawnerData$Builder$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$StructurePoolElementTypeModuleForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $StructurePoolElementTypeModuleForge {

constructor()

public static "processEntries"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructurePoolElementTypeModuleForge$Type = ($StructurePoolElementTypeModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructurePoolElementTypeModuleForge_ = $StructurePoolElementTypeModuleForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegisterEntityType" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AttributeSupplier$Builder, $AttributeSupplier$Builder$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeSupplier$Builder"
import {$AutoRegisterEntry, $AutoRegisterEntry$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/autoregister/$AutoRegisterEntry"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $AutoRegisterEntityType<T extends $Entity> extends $AutoRegisterEntry<($EntityType<(T)>)> {


public static "of"<U extends $Entity>(arg0: $Supplier$Type<($EntityType$Type<(U)>)>): $AutoRegisterEntityType<(U)>
public "attributes"(arg0: $Supplier$Type<($AttributeSupplier$Builder$Type)>): $AutoRegisterEntityType<(T)>
public "hasAttributes"(): boolean
public "getAttributesSupplier"(): $Supplier<($AttributeSupplier$Builder)>
get "attributesSupplier"(): $Supplier<($AttributeSupplier$Builder)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoRegisterEntityType$Type<T> = ($AutoRegisterEntityType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoRegisterEntityType_<T> = $AutoRegisterEntityType$Type<(T)>;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/$PieceEntry" {
import {$JigsawJunction, $JigsawJunction$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$JigsawJunction"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$PoolElementStructurePiece, $PoolElementStructurePiece$Type} from "packages/net/minecraft/world/level/levelgen/structure/$PoolElementStructurePiece"
import {$MutableObject, $MutableObject$Type} from "packages/org/apache/commons/lang3/mutable/$MutableObject"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$BoxOctree, $BoxOctree$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/util/$BoxOctree"
import {$PieceContext, $PieceContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/assembler/$PieceContext"

export class $PieceEntry {

constructor(arg0: $PoolElementStructurePiece$Type, arg1: $MutableObject$Type<($BoxOctree$Type)>, arg2: $AABB$Type, arg3: integer, arg4: $PieceEntry$Type, arg5: $PieceContext$Type, arg6: $JigsawJunction$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getDeadendPool"(): $Optional<($ResourceLocation)>
public "getPieceAabb"(): $AABB
public "setPiece"(arg0: $PoolElementStructurePiece$Type): void
public "getBoxOctree"(): $MutableObject<($BoxOctree)>
public "setDelayGeneration"(arg0: boolean): void
public "hasChildren"(): boolean
public "getParentEntry"(): $PieceEntry
public "getDepth"(): integer
public "getPiece"(): $PoolElementStructurePiece
public "getSourcePieceContext"(): $PieceContext
public "addChildEntry"(arg0: $PieceEntry$Type): void
public "getParentJunction"(): $JigsawJunction
public "isDelayGeneration"(): boolean
get "deadendPool"(): $Optional<($ResourceLocation)>
get "pieceAabb"(): $AABB
set "piece"(value: $PoolElementStructurePiece$Type)
get "boxOctree"(): $MutableObject<($BoxOctree)>
set "delayGeneration"(value: boolean)
get "parentEntry"(): $PieceEntry
get "depth"(): integer
get "piece"(): $PoolElementStructurePiece
get "sourcePieceContext"(): $PieceContext
get "parentJunction"(): $JigsawJunction
get "delayGeneration"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PieceEntry$Type = ($PieceEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PieceEntry_ = $PieceEntry$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/services/$IPlatformHelper" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IPlatformHelper {

 "isDevelopmentEnvironment"(): boolean
 "getPlatformName"(): string
 "isFabric"(): boolean
 "isForge"(): boolean
 "isModLoaded"(arg0: string): boolean
}

export namespace $IPlatformHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlatformHelper$Type = ($IPlatformHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlatformHelper_ = $IPlatformHelper$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$ModLoadedCondition" {
import {$StructureCondition, $StructureCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureCondition"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureConditionType"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $ModLoadedCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($ModLoadedCondition)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(arg0: string)

public "type"(): $StructureConditionType<(any)>
public "passes"(arg0: $StructureContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModLoadedCondition$Type = ($ModLoadedCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModLoadedCondition_ = $ModLoadedCondition$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$BlockModuleForge$ExtraBlockData" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $BlockModuleForge$ExtraBlockData extends $Record {

constructor(block: $Block$Type, itemProperties: $Supplier$Type<($Item$Properties$Type)>, blockRegisteredName: $ResourceLocation$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "block"(): $Block
public "blockRegisteredName"(): $ResourceLocation
public "itemProperties"(): $Supplier<($Item$Properties)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockModuleForge$ExtraBlockData$Type = ($BlockModuleForge$ExtraBlockData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockModuleForge$ExtraBlockData_ = $BlockModuleForge$ExtraBlockData$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/mixin/accessor/$FeaturePoolElementAccessor" {
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$PlacedFeature, $PlacedFeature$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacedFeature"

export interface $FeaturePoolElementAccessor {

 "getFeature"(): $Holder<($PlacedFeature)>

(): $Holder<($PlacedFeature)>
}

export namespace $FeaturePoolElementAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FeaturePoolElementAccessor$Type = ($FeaturePoolElementAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FeaturePoolElementAccessor_ = $FeaturePoolElementAccessor$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/action/$DelayGenerationAction" {
import {$PieceEntry, $PieceEntry$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/$PieceEntry"
import {$StructureAction, $StructureAction$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/action/$StructureAction"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$StructureActionType, $StructureActionType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/action/$StructureActionType"

export class $DelayGenerationAction extends $StructureAction {
static readonly "CODEC": $Codec<($DelayGenerationAction)>

constructor()

public "type"(): $StructureActionType<(any)>
public "apply"(arg0: $StructureContext$Type, arg1: $PieceEntry$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DelayGenerationAction$Type = ($DelayGenerationAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DelayGenerationAction_ = $DelayGenerationAction$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/$YungsApiCommon" {
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $YungsApiCommon {
static readonly "MOD_ID": string
static readonly "LOGGER": $Logger

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $YungsApiCommon$Type = ($YungsApiCommon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $YungsApiCommon_ = $YungsApiCommon$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegisterBlockEntityType$Builder$BlockEntitySupplier" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $AutoRegisterBlockEntityType$Builder$BlockEntitySupplier<T extends $BlockEntity> {

 "create"(arg0: $BlockPos$Type, arg1: $BlockState$Type): T

(arg0: $BlockPos$Type, arg1: $BlockState$Type): T
}

export namespace $AutoRegisterBlockEntityType$Builder$BlockEntitySupplier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoRegisterBlockEntityType$Builder$BlockEntitySupplier$Type<T> = ($AutoRegisterBlockEntityType$Builder$BlockEntitySupplier<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoRegisterBlockEntityType$Builder$BlockEntitySupplier_<T> = $AutoRegisterBlockEntityType$Builder$BlockEntitySupplier$Type<(T)>;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/mixin/accessor/$BoundingBoxAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $BoundingBoxAccessor {

 "setMinY"(arg0: integer): void
 "setMinZ"(arg0: integer): void
 "setMaxX"(arg0: integer): void
 "setMinX"(arg0: integer): void
 "setMaxY"(arg0: integer): void
 "setMaxZ"(arg0: integer): void
}

export namespace $BoundingBoxAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BoundingBoxAccessor$Type = ($BoundingBoxAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BoundingBoxAccessor_ = $BoundingBoxAccessor$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$PieceInHorizontalDirectionCondition" {
import {$StructureCondition, $StructureCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureCondition"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureConditionType"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $PieceInHorizontalDirectionCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($PieceInHorizontalDirectionCondition)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(arg0: $List$Type<($ResourceLocation$Type)>, arg1: integer, arg2: $Rotation$Type)

public "type"(): $StructureConditionType<(any)>
public "passes"(arg0: $StructureContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PieceInHorizontalDirectionCondition$Type = ($PieceInHorizontalDirectionCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PieceInHorizontalDirectionCondition_ = $PieceInHorizontalDirectionCondition$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/paxi/$PaxiRepositorySource" {
import {$FolderRepositorySource, $FolderRepositorySource$Type} from "packages/net/minecraft/server/packs/repository/$FolderRepositorySource"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$PackType, $PackType$Type} from "packages/net/minecraft/server/packs/$PackType"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$File, $File$Type} from "packages/java/io/$File"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PackSource, $PackSource$Type} from "packages/net/minecraft/server/packs/repository/$PackSource"
import {$Pack, $Pack$Type} from "packages/net/minecraft/server/packs/repository/$Pack"

export class $PaxiRepositorySource extends $FolderRepositorySource {
 "orderedPaxiPacks": $List<(string)>
 "unorderedPaxiPacks": $List<(string)>
readonly "folder": $Path
readonly "packType": $PackType
readonly "packSource": $PackSource

constructor(arg0: $Path$Type, arg1: $PackType$Type, arg2: $File$Type)

public "loadPacks"(arg0: $Consumer$Type<($Pack$Type)>): void
public "hasPacks"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PaxiRepositorySource$Type = ($PaxiRepositorySource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PaxiRepositorySource_ = $PaxiRepositorySource$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext" {
import {$StructureTemplateManager, $StructureTemplateManager$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplateManager"
import {$PieceEntry, $PieceEntry$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/$PieceEntry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $StructureContext {


public "depth"(): integer
public "pos"(): $BlockPos
public "random"(): $RandomSource
public "pieceMinY"(): integer
public "pieceMaxY"(): integer
public "pieceEntry"(): $PieceEntry
public "rotation"(): $Rotation
public "pieces"(): $List<($PieceEntry)>
public "structureTemplateManager"(): $StructureTemplateManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureContext$Type = ($StructureContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureContext_ = $StructureContext$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$MaxCountSinglePoolElement" {
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$StructureTemplatePool$Projection, $StructureTemplatePool$Projection$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool$Projection"
import {$StructureProcessorList, $StructureProcessorList$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessorList"
import {$SinglePoolElement, $SinglePoolElement$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$SinglePoolElement"
import {$IMaxCountJigsawPoolElement, $IMaxCountJigsawPoolElement$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$IMaxCountJigsawPoolElement"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$StructurePoolElementType, $StructurePoolElementType$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElementType"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$StructureTemplate, $StructureTemplate$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate"

/**
 * 
 * @deprecated
 */
export class $MaxCountSinglePoolElement extends $SinglePoolElement implements $IMaxCountJigsawPoolElement {
static readonly "CODEC": $Codec<($MaxCountSinglePoolElement)>

constructor(arg0: $Either$Type<($ResourceLocation$Type), ($StructureTemplate$Type)>, arg1: $Holder$Type<($StructureProcessorList$Type)>, arg2: $StructureTemplatePool$Projection$Type, arg3: string, arg4: integer)

public "getName"(): string
public "toString"(): string
public "getMaxCount"(): integer
public "getType"(): $StructurePoolElementType<(any)>
get "name"(): string
get "maxCount"(): integer
get "type"(): $StructurePoolElementType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MaxCountSinglePoolElement$Type = ($MaxCountSinglePoolElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MaxCountSinglePoolElement_ = $MaxCountSinglePoolElement$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/autoregister/$AutoRegisterEntry" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $AutoRegisterEntry<T> {

constructor(arg0: $Supplier$Type<(T)>)

public "get"(): T
public "setSupplier"(arg0: $Supplier$Type<(T)>): void
public "getSupplier"(): $Supplier<(T)>
set "supplier"(value: $Supplier$Type<(T)>)
get "supplier"(): $Supplier<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoRegisterEntry$Type<T> = ($AutoRegisterEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoRegisterEntry_<T> = $AutoRegisterEntry$Type<(T)>;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/assembler/$JigsawStructureAssembler" {
import {$JigsawStructureAssembler$Settings, $JigsawStructureAssembler$Settings$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/assembler/$JigsawStructureAssembler$Settings"
import {$StructurePiecesBuilder, $StructurePiecesBuilder$Type} from "packages/net/minecraft/world/level/levelgen/structure/pieces/$StructurePiecesBuilder"
import {$PieceEntry, $PieceEntry$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/$PieceEntry"
import {$Deque, $Deque$Type} from "packages/java/util/$Deque"
import {$PoolElementStructurePiece, $PoolElementStructurePiece$Type} from "packages/net/minecraft/world/level/levelgen/structure/$PoolElementStructurePiece"
import {$BoxOctree, $BoxOctree$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/util/$BoxOctree"

export class $JigsawStructureAssembler {
 "unprocessedPieceEntries": $Deque<($PieceEntry)>

constructor(arg0: $JigsawStructureAssembler$Settings$Type)

public "addAllPiecesToStructureBuilder"(arg0: $StructurePiecesBuilder$Type): void
public "assembleStructure"(arg0: $PoolElementStructurePiece$Type, arg1: $BoxOctree$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JigsawStructureAssembler$Type = ($JigsawStructureAssembler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JigsawStructureAssembler_ = $JigsawStructureAssembler$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/services/$IBlockEntityTypeHelper" {
import {$Type, $Type$Type} from "packages/com/mojang/datafixers/types/$Type"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$AutoRegisterBlockEntityType$Builder, $AutoRegisterBlockEntityType$Builder$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegisterBlockEntityType$Builder"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"

export interface $IBlockEntityTypeHelper {

 "build"<T extends $BlockEntity>(arg0: $AutoRegisterBlockEntityType$Builder$Type<(T)>, arg1: $Type$Type<(any)>): $BlockEntityType<(T)>

(arg0: $AutoRegisterBlockEntityType$Builder$Type<(T)>, arg1: $Type$Type<(any)>): $BlockEntityType<(T)>
}

export namespace $IBlockEntityTypeHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IBlockEntityTypeHelper$Type = ($IBlockEntityTypeHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IBlockEntityTypeHelper_ = $IBlockEntityTypeHelper$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/paxi/services/$IPlatformHelper" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IPlatformHelper {

 "isDevelopmentEnvironment"(): boolean
 "getPlatformName"(): string
 "isModLoaded"(arg0: string): boolean
}

export namespace $IPlatformHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlatformHelper$Type = ($IPlatformHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlatformHelper_ = $IPlatformHelper$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/banner/$BannerPattern" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $BannerPattern {

constructor(arg0: string, arg1: integer)

public "setColor"(arg0: integer): void
public "getPattern"(): string
public "setPattern"(arg0: string): void
public "getColor"(): integer
set "color"(value: integer)
get "pattern"(): string
set "pattern"(value: string)
get "color"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BannerPattern$Type = ($BannerPattern);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BannerPattern_ = $BannerPattern$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$CommandModuleForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CommandModuleForge {

constructor()

public static "processEntries"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommandModuleForge$Type = ($CommandModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommandModuleForge_ = $CommandModuleForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/math/$ColPos" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $ColPos {

constructor(arg0: $BlockPos$Type)
constructor(arg0: integer, arg1: integer)
constructor()

public "offset"(arg0: $Direction$Type, arg1: integer): $ColPos
public "offset"(arg0: $Direction$Type): $ColPos
public "rotate"(arg0: $Rotation$Type): $ColPos
public "up"(arg0: integer): $ColPos
public "up"(): $ColPos
public "down"(): $ColPos
public "down"(arg0: integer): $ColPos
public "toLong"(): long
public static "fromLong"(arg0: long): $ColPos
public "getZ"(): integer
public "getX"(): integer
public static "fromBlockPos"(arg0: $BlockPos$Type): $ColPos
public "toBlockPos"(): $BlockPos
public "east"(): $ColPos
public "east"(arg0: integer): $ColPos
public "north"(arg0: integer): $ColPos
public "north"(): $ColPos
public "west"(arg0: integer): $ColPos
public "west"(): $ColPos
public "south"(arg0: integer): $ColPos
public "south"(): $ColPos
get "z"(): integer
get "x"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColPos$Type = ($ColPos);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColPos_ = $ColPos$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegisterPotion" {
import {$MobEffectInstance, $MobEffectInstance$Type} from "packages/net/minecraft/world/effect/$MobEffectInstance"
import {$AutoRegisterEntry, $AutoRegisterEntry$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/autoregister/$AutoRegisterEntry"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"

export class $AutoRegisterPotion extends $AutoRegisterEntry<($Potion)> {


public "getMobEffectInstance"(): $MobEffectInstance
public static "mobEffect"(arg0: $MobEffectInstance$Type): $AutoRegisterPotion
get "mobEffectInstance"(): $MobEffectInstance
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoRegisterPotion$Type = ($AutoRegisterPotion);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoRegisterPotion_ = $AutoRegisterPotion$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/mixin/accessor/$BeardifierAccessor" {
import {$ObjectListIterator, $ObjectListIterator$Type} from "packages/it/unimi/dsi/fastutil/objects/$ObjectListIterator"
import {$JigsawJunction, $JigsawJunction$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$JigsawJunction"
import {$Beardifier$Rigid, $Beardifier$Rigid$Type} from "packages/net/minecraft/world/level/levelgen/$Beardifier$Rigid"

export interface $BeardifierAccessor {

 "getPieceIterator"(): $ObjectListIterator<($Beardifier$Rigid)>
 "getJunctionIterator"(): $ObjectListIterator<($JigsawJunction)>
}

export namespace $BeardifierAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BeardifierAccessor$Type = ($BeardifierAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BeardifierAccessor_ = $BeardifierAccessor$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/noise/$FastNoise$Interp" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $FastNoise$Interp extends $Enum<($FastNoise$Interp)> {
static readonly "Linear": $FastNoise$Interp
static readonly "Hermite": $FastNoise$Interp
static readonly "Quintic": $FastNoise$Interp


public static "values"(): ($FastNoise$Interp)[]
public static "valueOf"(arg0: string): $FastNoise$Interp
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastNoise$Interp$Type = (("linear") | ("quintic") | ("hermite")) | ($FastNoise$Interp);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastNoise$Interp_ = $FastNoise$Interp$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/beardifier/$EnhancedJigsawJunction" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$EnhancedTerrainAdaptation"
import {$JigsawJunction, $JigsawJunction$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$JigsawJunction"

export class $EnhancedJigsawJunction extends $Record {

constructor(jigsawJunction: $JigsawJunction$Type, pieceTerrainAdaptation: $EnhancedTerrainAdaptation$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "jigsawJunction"(): $JigsawJunction
public "pieceTerrainAdaptation"(): $EnhancedTerrainAdaptation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnhancedJigsawJunction$Type = ($EnhancedJigsawJunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnhancedJigsawJunction_ = $EnhancedJigsawJunction$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/json/$BlockStateAdapter" {
import {$TypeAdapter, $TypeAdapter$Type} from "packages/com/google/gson/$TypeAdapter"
import {$JsonWriter, $JsonWriter$Type} from "packages/com/google/gson/stream/$JsonWriter"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export class $BlockStateAdapter extends $TypeAdapter<($BlockState)> {

constructor()

public "write"(arg0: $JsonWriter$Type, arg1: $BlockState$Type): void
public static "resolveBlockState"(arg0: string): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockStateAdapter$Type = ($BlockStateAdapter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockStateAdapter_ = $BlockStateAdapter$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/autoregister/$AutoRegistrationManager" {
import {$AutoRegisterField, $AutoRegisterField$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/autoregister/$AutoRegisterField"
import {$List, $List$Type} from "packages/java/util/$List"

export class $AutoRegistrationManager {
static readonly "STRUCTURE_TYPES": $List<($AutoRegisterField)>
static readonly "STRUCTURE_POOL_ELEMENT_TYPES": $List<($AutoRegisterField)>
static readonly "STRUCTURE_PROCESSOR_TYPES": $List<($AutoRegisterField)>
static readonly "STRUCTURE_PIECE_TYPES": $List<($AutoRegisterField)>
static readonly "STRUCTURE_PLACEMENT_TYPES": $List<($AutoRegisterField)>
static readonly "FEATURES": $List<($AutoRegisterField)>
static readonly "PLACEMENT_MODIFIER_TYPES": $List<($AutoRegisterField)>
static readonly "CRITERION_TRIGGERS": $List<($AutoRegisterField)>
static readonly "BLOCKS": $List<($AutoRegisterField)>
static readonly "ITEMS": $List<($AutoRegisterField)>
static readonly "BLOCK_ENTITY_TYPES": $List<($AutoRegisterField)>
static readonly "CREATIVE_MODE_TABS": $List<($AutoRegisterField)>
static readonly "ENTITY_TYPES": $List<($AutoRegisterField)>
static readonly "MOB_EFFECTS": $List<($AutoRegisterField)>
static readonly "POTIONS": $List<($AutoRegisterField)>
static readonly "SOUND_EVENTS": $List<($AutoRegisterField)>
static readonly "COMMANDS": $List<($AutoRegisterField)>

constructor()

public static "initAutoRegPackage"(arg0: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoRegistrationManager$Type = ($AutoRegistrationManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoRegistrationManager_ = $AutoRegistrationManager$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/noise/$FastNoise$FractalType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $FastNoise$FractalType extends $Enum<($FastNoise$FractalType)> {
static readonly "FBM": $FastNoise$FractalType
static readonly "Billow": $FastNoise$FractalType
static readonly "RigidMulti": $FastNoise$FractalType


public static "values"(): ($FastNoise$FractalType)[]
public static "valueOf"(arg0: string): $FastNoise$FractalType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastNoise$FractalType$Type = (("fbm") | ("rigidmulti") | ("billow")) | ($FastNoise$FractalType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastNoise$FractalType_ = $FastNoise$FractalType$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegisterUtils" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Potion, $Potion$Type} from "packages/net/minecraft/world/item/alchemy/$Potion"

export class $AutoRegisterUtils {

constructor()

public static "addCompostableItem"(arg0: $Supplier$Type<($Item$Type)>, arg1: float): void
public static "registerBrewingRecipe"(arg0: $Supplier$Type<($Potion$Type)>, arg1: $Supplier$Type<($Item$Type)>, arg2: $Supplier$Type<($Potion$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoRegisterUtils$Type = ($AutoRegisterUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoRegisterUtils_ = $AutoRegisterUtils$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$PieceInRangeCondition" {
import {$StructureCondition, $StructureCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureCondition"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureConditionType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $PieceInRangeCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($PieceInRangeCondition)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(arg0: $List$Type<($ResourceLocation$Type)>, arg1: integer, arg2: integer, arg3: integer)

public "type"(): $StructureConditionType<(any)>
public "passes"(arg0: $StructureContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PieceInRangeCondition$Type = ($PieceInRangeCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PieceInRangeCondition_ = $PieceInRangeCondition$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/noise/$OpenSimplex2S" {
import {$INoiseLibrary, $INoiseLibrary$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/noise/$INoiseLibrary"

export class $OpenSimplex2S implements $INoiseLibrary {
static readonly "N2": double
static readonly "N3": double

constructor(arg0: long)

public "noise2"(arg0: double, arg1: double): double
public "noise2_XBeforeY"(arg0: double, arg1: double): double
public "noise3_XYBeforeZ"(arg0: double, arg1: double, arg2: double): double
public "noise3_XZBeforeY"(arg0: double, arg1: double, arg2: double): double
public "noise3_Classic"(arg0: double, arg1: double, arg2: double): double
public "setFrequency"(arg0: double): void
public "setGain"(arg0: double): void
public "GetNoise"(arg0: float, arg1: float, arg2: float): float
public "setOctaves"(arg0: integer): void
public "setLacunarity"(arg0: double): void
set "frequency"(value: double)
set "gain"(value: double)
set "octaves"(value: integer)
set "lacunarity"(value: double)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OpenSimplex2S$Type = ($OpenSimplex2S);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OpenSimplex2S_ = $OpenSimplex2S$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$StructurePlacementTypeModule" {
import {$EnhancedRandomSpread, $EnhancedRandomSpread$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/placement/$EnhancedRandomSpread"
import {$StructurePlacementType, $StructurePlacementType$Type} from "packages/net/minecraft/world/level/levelgen/structure/placement/$StructurePlacementType"

export class $StructurePlacementTypeModule {
static readonly "ENHANCED_RANDOM_SPREAD": $StructurePlacementType<($EnhancedRandomSpread)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructurePlacementTypeModule$Type = ($StructurePlacementTypeModule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructurePlacementTypeModule_ = $StructurePlacementTypeModule$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$NotCondition" {
import {$StructureCondition, $StructureCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureCondition"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureConditionType"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $NotCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($NotCondition)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(arg0: $StructureCondition$Type)

public "type"(): $StructureConditionType<(any)>
public "passes"(arg0: $StructureContext$Type): boolean
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
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$NoneAdaptation" {
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$EnhancedTerrainAdaptation"
import {$EnhancedTerrainAdaptationType, $EnhancedTerrainAdaptationType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$EnhancedTerrainAdaptationType"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $NoneAdaptation extends $EnhancedTerrainAdaptation {
static readonly "CODEC": $Codec<($NoneAdaptation)>
static readonly "NONE": $EnhancedTerrainAdaptation

constructor()

public "computeDensityFactor"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): double
public "type"(): $EnhancedTerrainAdaptationType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NoneAdaptation$Type = ($NoneAdaptation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NoneAdaptation_ = $NoneAdaptation$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegisterMobEffect" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"
import {$AutoRegisterEntry, $AutoRegisterEntry$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/autoregister/$AutoRegisterEntry"

export class $AutoRegisterMobEffect extends $AutoRegisterEntry<($MobEffect)> {


public static "of"(arg0: $Supplier$Type<($MobEffect$Type)>): $AutoRegisterMobEffect
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoRegisterMobEffect$Type = ($AutoRegisterMobEffect);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoRegisterMobEffect_ = $AutoRegisterMobEffect$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/mixin/accessor/$SinglePoolElementAccessor" {
import {$StructureTemplateManager, $StructureTemplateManager$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplateManager"
import {$StructureTemplate, $StructureTemplate$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate"

export interface $SinglePoolElementAccessor {

 "callGetTemplate"(arg0: $StructureTemplateManager$Type): $StructureTemplate

(arg0: $StructureTemplateManager$Type): $StructureTemplate
}

export namespace $SinglePoolElementAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SinglePoolElementAccessor$Type = ($SinglePoolElementAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SinglePoolElementAccessor_ = $SinglePoolElementAccessor$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/autoregister/$AutoRegisterFieldRouter" {
import {$AutoRegisterField, $AutoRegisterField$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/autoregister/$AutoRegisterField"

export class $AutoRegisterFieldRouter {

constructor()

public static "queueField"(arg0: $AutoRegisterField$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoRegisterFieldRouter$Type = ($AutoRegisterFieldRouter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoRegisterFieldRouter_ = $AutoRegisterFieldRouter$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$MaxCountLegacySinglePoolElement" {
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$StructureTemplatePool$Projection, $StructureTemplatePool$Projection$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool$Projection"
import {$StructureProcessorList, $StructureProcessorList$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessorList"
import {$LegacySinglePoolElement, $LegacySinglePoolElement$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$LegacySinglePoolElement"
import {$IMaxCountJigsawPoolElement, $IMaxCountJigsawPoolElement$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$IMaxCountJigsawPoolElement"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$StructurePoolElementType, $StructurePoolElementType$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElementType"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$StructureTemplate, $StructureTemplate$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate"

export class $MaxCountLegacySinglePoolElement extends $LegacySinglePoolElement implements $IMaxCountJigsawPoolElement {
static readonly "CODEC": $Codec<($MaxCountLegacySinglePoolElement)>

constructor(arg0: $Either$Type<($ResourceLocation$Type), ($StructureTemplate$Type)>, arg1: $Holder$Type<($StructureProcessorList$Type)>, arg2: $StructureTemplatePool$Projection$Type, arg3: string, arg4: integer)

public "getName"(): string
public "toString"(): string
public "getMaxCount"(): integer
public "getType"(): $StructurePoolElementType<(any)>
get "name"(): string
get "maxCount"(): integer
get "type"(): $StructurePoolElementType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MaxCountLegacySinglePoolElement$Type = ($MaxCountLegacySinglePoolElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MaxCountLegacySinglePoolElement_ = $MaxCountLegacySinglePoolElement$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/assembler/$PieceContext" {
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$PieceEntry, $PieceEntry$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/$PieceEntry"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$StructurePoolElement, $StructurePoolElement$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElement"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$MutableObject, $MutableObject$Type} from "packages/org/apache/commons/lang3/mutable/$MutableObject"
import {$ObjectArrayList, $ObjectArrayList$Type} from "packages/it/unimi/dsi/fastutil/objects/$ObjectArrayList"
import {$BoxOctree, $BoxOctree$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/util/$BoxOctree"

export class $PieceContext {
 "candidatePoolElements": $ObjectArrayList<($Pair<($StructurePoolElement), (integer)>)>
 "jigsawBlock": $StructureTemplate$StructureBlockInfo
 "jigsawBlockTargetPos": $BlockPos
 "pieceMinY": integer
 "jigsawBlockPos": $BlockPos
 "boxOctree": $MutableObject<($BoxOctree)>
 "pieceEntry": $PieceEntry
 "depth": integer

constructor(arg0: $ObjectArrayList$Type<($Pair$Type<($StructurePoolElement$Type), (integer)>)>, arg1: $StructureTemplate$StructureBlockInfo$Type, arg2: $BlockPos$Type, arg3: integer, arg4: $BlockPos$Type, arg5: $MutableObject$Type<($BoxOctree$Type)>, arg6: $PieceEntry$Type, arg7: integer)

public "copy"(): $PieceContext
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PieceContext$Type = ($PieceContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PieceContext_ = $PieceContext$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$CreativeModeTabModuleForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CreativeModeTabModuleForge {

constructor()

public static "processEntries"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CreativeModeTabModuleForge$Type = ($CreativeModeTabModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CreativeModeTabModuleForge_ = $CreativeModeTabModuleForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/json/$ItemAdapter" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$JsonReader, $JsonReader$Type} from "packages/com/google/gson/stream/$JsonReader"
import {$TypeAdapter, $TypeAdapter$Type} from "packages/com/google/gson/$TypeAdapter"
import {$JsonWriter, $JsonWriter$Type} from "packages/com/google/gson/stream/$JsonWriter"

export class $ItemAdapter extends $TypeAdapter<($Item)> {

constructor()

public "write"(arg0: $JsonWriter$Type, arg1: $Item$Type): void
public "read"(arg0: $JsonReader$Type): $Item
public static "resolveItem"(arg0: string): $Item
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
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$ModLoaderCondition" {
import {$StructureCondition, $StructureCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureCondition"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureConditionType"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $ModLoaderCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($ModLoaderCondition)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(arg0: $List$Type<(string)>)

public "type"(): $StructureConditionType<(any)>
public "passes"(arg0: $StructureContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModLoaderCondition$Type = ($ModLoaderCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModLoaderCondition_ = $ModLoaderCondition$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$SoundEventModuleForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SoundEventModuleForge {

constructor()

public static "processEntries"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SoundEventModuleForge$Type = ($SoundEventModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SoundEventModuleForge_ = $SoundEventModuleForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/noise/$FastNoise$CellularDistanceFunction" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $FastNoise$CellularDistanceFunction extends $Enum<($FastNoise$CellularDistanceFunction)> {
static readonly "Euclidean": $FastNoise$CellularDistanceFunction
static readonly "Manhattan": $FastNoise$CellularDistanceFunction
static readonly "Natural": $FastNoise$CellularDistanceFunction


public static "values"(): ($FastNoise$CellularDistanceFunction)[]
public static "valueOf"(arg0: string): $FastNoise$CellularDistanceFunction
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastNoise$CellularDistanceFunction$Type = (("natural") | ("manhattan") | ("euclidean")) | ($FastNoise$CellularDistanceFunction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastNoise$CellularDistanceFunction_ = $FastNoise$CellularDistanceFunction$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/targetselector/$SelfTargetSelector" {
import {$StructureTargetSelectorType, $StructureTargetSelectorType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/targetselector/$StructureTargetSelectorType"
import {$PieceEntry, $PieceEntry$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/$PieceEntry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$StructureTargetSelector, $StructureTargetSelector$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/targetselector/$StructureTargetSelector"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $SelfTargetSelector extends $StructureTargetSelector {
static readonly "CODEC": $Codec<($SelfTargetSelector)>

constructor()

public "type"(): $StructureTargetSelectorType<(any)>
public "apply"(arg0: $StructureContext$Type): $List<($PieceEntry)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SelfTargetSelector$Type = ($SelfTargetSelector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SelfTargetSelector_ = $SelfTargetSelector$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$StructureProcessorTypeModuleForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $StructureProcessorTypeModuleForge {

constructor()

public static "processEntries"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureProcessorTypeModuleForge$Type = ($StructureProcessorTypeModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureProcessorTypeModuleForge_ = $StructureProcessorTypeModuleForge$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/noise/$FastNoise" {
import {$FastNoise$Interp, $FastNoise$Interp$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/noise/$FastNoise$Interp"
import {$Vector3f, $Vector3f$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/math/$Vector3f"
import {$Vector2f, $Vector2f$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/math/$Vector2f"
import {$FastNoise$NoiseType, $FastNoise$NoiseType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/noise/$FastNoise$NoiseType"
import {$FastNoise$FractalType, $FastNoise$FractalType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/noise/$FastNoise$FractalType"
import {$INoiseLibrary, $INoiseLibrary$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/noise/$INoiseLibrary"
import {$FastNoise$CellularDistanceFunction, $FastNoise$CellularDistanceFunction$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/noise/$FastNoise$CellularDistanceFunction"
import {$FastNoise$CellularReturnType, $FastNoise$CellularReturnType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/noise/$FastNoise$CellularReturnType"

export class $FastNoise implements $INoiseLibrary {

constructor()
constructor(arg0: integer)

public "SetFractalOctaves"(arg0: integer): void
public "SetInterp"(arg0: $FastNoise$Interp$Type): void
public "SetFractalGain"(arg0: float): void
public "GetSeed"(): integer
public "SetFrequency"(arg0: float): void
public "SetFractalType"(arg0: $FastNoise$FractalType$Type): void
public static "GetDecimalType"(): float
public "SetSeed"(arg0: integer): void
public "SetNoiseType"(arg0: $FastNoise$NoiseType$Type): void
public "GetNoise"(arg0: float, arg1: float): float
public "GetNoise"(arg0: float, arg1: float, arg2: float): float
public "GetWhiteNoise"(arg0: float, arg1: float): float
public "GetWhiteNoise"(arg0: float, arg1: float, arg2: float): float
public "GetWhiteNoise"(arg0: float, arg1: float, arg2: float, arg3: float): float
public "GetCubic"(arg0: float, arg1: float, arg2: float): float
public "GetCubic"(arg0: float, arg1: float): float
public "GetValueFractal"(arg0: float, arg1: float, arg2: float): float
public "GetValueFractal"(arg0: float, arg1: float): float
public "GetCellular"(arg0: float, arg1: float, arg2: float): float
public "GetCellular"(arg0: float, arg1: float): float
public "GetSimplex"(arg0: float, arg1: float, arg2: float, arg3: float): float
public "GetSimplex"(arg0: float, arg1: float): float
public "GetSimplex"(arg0: float, arg1: float, arg2: float): float
public "GetPerlin"(arg0: float, arg1: float, arg2: float): float
public "GetPerlin"(arg0: float, arg1: float): float
public "GetPerlinFractal"(arg0: float, arg1: float): float
public "GetPerlinFractal"(arg0: float, arg1: float, arg2: float): float
public "GetSimplexFractal"(arg0: float, arg1: float, arg2: float): float
public "GetSimplexFractal"(arg0: float, arg1: float): float
public "GetWhiteNoiseInt"(arg0: integer, arg1: integer): float
public "GetWhiteNoiseInt"(arg0: integer, arg1: integer, arg2: integer): float
public "GetWhiteNoiseInt"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): float
public "GetValue"(arg0: float, arg1: float, arg2: float): float
public "GetValue"(arg0: float, arg1: float): float
public "GetCubicFractal"(arg0: float, arg1: float, arg2: float): float
public "GetCubicFractal"(arg0: float, arg1: float): float
public "GradientPerturb"(arg0: $Vector2f$Type): void
public "GradientPerturb"(arg0: $Vector3f$Type): void
public "SetGradientPerturbAmp"(arg0: float): void
public "SetCellularDistanceFunction"(arg0: $FastNoise$CellularDistanceFunction$Type): void
public "SetCellularReturnType"(arg0: $FastNoise$CellularReturnType$Type): void
public "SetCellularNoiseLookup"(arg0: $FastNoise$Type): void
public "GradientPerturbFractal"(arg0: $Vector2f$Type): void
public "GradientPerturbFractal"(arg0: $Vector3f$Type): void
public "SetFractalLacunarity"(arg0: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastNoise$Type = ($FastNoise);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastNoise_ = $FastNoise$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$IMaxCountJigsawPoolElement" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IMaxCountJigsawPoolElement {

 "getName"(): string
 "getMaxCount"(): integer
}

export namespace $IMaxCountJigsawPoolElement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMaxCountJigsawPoolElement$Type = ($IMaxCountJigsawPoolElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMaxCountJigsawPoolElement_ = $IMaxCountJigsawPoolElement$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegisterCreativeTab$Builder" {
import {$AutoRegisterCreativeTab, $AutoRegisterCreativeTab$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegisterCreativeTab"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$CreativeModeTab$DisplayItemsGenerator, $CreativeModeTab$DisplayItemsGenerator$Type} from "packages/net/minecraft/world/item/$CreativeModeTab$DisplayItemsGenerator"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $AutoRegisterCreativeTab$Builder {


public "backgroundSuffix"(arg0: string): $AutoRegisterCreativeTab$Builder
public "entries"(arg0: $CreativeModeTab$DisplayItemsGenerator$Type): $AutoRegisterCreativeTab$Builder
public "build"(): $AutoRegisterCreativeTab
public "iconItem"(arg0: $Supplier$Type<($ItemStack$Type)>): $AutoRegisterCreativeTab$Builder
public "title"(arg0: $Component$Type): $AutoRegisterCreativeTab$Builder
public "noScrollBar"(): $AutoRegisterCreativeTab$Builder
public "hideTitle"(): $AutoRegisterCreativeTab$Builder
public "alignedRight"(): $AutoRegisterCreativeTab$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoRegisterCreativeTab$Builder$Type = ($AutoRegisterCreativeTab$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoRegisterCreativeTab$Builder_ = $AutoRegisterCreativeTab$Builder$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$AltitudeCondition" {
import {$StructureCondition, $StructureCondition$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureCondition"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/condition/$StructureConditionType"
import {$StructureContext, $StructureContext$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/context/$StructureContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $AltitudeCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($AltitudeCondition)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(arg0: $Optional$Type<(double)>, arg1: $Optional$Type<(double)>)

public "type"(): $StructureConditionType<(any)>
public "passes"(arg0: $StructureContext$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AltitudeCondition$Type = ($AltitudeCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AltitudeCondition_ = $AltitudeCondition$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegisterBlock" {
import {$WoodType, $WoodType$Type} from "packages/net/minecraft/world/level/block/state/properties/$WoodType"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$AutoRegisterEntry, $AutoRegisterEntry$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/autoregister/$AutoRegisterEntry"

export class $AutoRegisterBlock extends $AutoRegisterEntry<($Block)> {


public static "of"(arg0: $Supplier$Type<($Block$Type)>): $AutoRegisterBlock
public "getFence"(): $Block
public "getItemProperties"(): $Supplier<($Item$Properties)>
public "hasItemProperties"(): boolean
public "withItem"(arg0: $Supplier$Type<($Item$Properties$Type)>): $AutoRegisterBlock
public "getFenceGateWoodType"(): $WoodType
public "hasStairs"(): boolean
public "hasSlab"(): boolean
public "setStairs"(arg0: $Block$Type): void
public "setSlab"(arg0: $Block$Type): void
public "hasFence"(): boolean
public "setFence"(arg0: $Block$Type): void
public "hasFenceGate"(): boolean
public "setFenceGate"(arg0: $Block$Type): void
public "setWall"(arg0: $Block$Type): void
public "hasWall"(): boolean
public "getSlab"(): $Block
public "withStairs"(): $AutoRegisterBlock
public "withSlab"(): $AutoRegisterBlock
public "withFence"(): $AutoRegisterBlock
public "withFenceGate"(arg0: $WoodType$Type): $AutoRegisterBlock
public "getWall"(): $Block
public "getFenceGate"(): $Block
public "withWall"(): $AutoRegisterBlock
public "getStairs"(): $Block
get "fence"(): $Block
get "itemProperties"(): $Supplier<($Item$Properties)>
get "fenceGateWoodType"(): $WoodType
set "stairs"(value: $Block$Type)
set "slab"(value: $Block$Type)
set "fence"(value: $Block$Type)
set "fenceGate"(value: $Block$Type)
set "wall"(value: $Block$Type)
get "slab"(): $Block
get "wall"(): $Block
get "fenceGate"(): $Block
get "stairs"(): $Block
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoRegisterBlock$Type = ($AutoRegisterBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoRegisterBlock_ = $AutoRegisterBlock$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$EnhancedTerrainAdaptation" {
import {$EnhancedTerrainAdaptationType, $EnhancedTerrainAdaptationType$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/$EnhancedTerrainAdaptationType"

export class $EnhancedTerrainAdaptation {
static readonly "NONE": $EnhancedTerrainAdaptation


public "computeDensityFactor"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): double
public "type"(): $EnhancedTerrainAdaptationType<(any)>
public "getKernelRadius"(): integer
public "beards"(): boolean
public "getKernel"(): (float)[]
public "getKernelSize"(): integer
public "getKernelDistance"(): integer
public "carves"(): boolean
get "kernelRadius"(): integer
get "kernel"(): (float)[]
get "kernelSize"(): integer
get "kernelDistance"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnhancedTerrainAdaptation$Type = ($EnhancedTerrainAdaptation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnhancedTerrainAdaptation_ = $EnhancedTerrainAdaptation$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/$JigsawManager" {
import {$StructureTemplatePool, $StructureTemplatePool$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Heightmap$Types, $Heightmap$Types$Type} from "packages/net/minecraft/world/level/levelgen/$Heightmap$Types"
import {$Structure$GenerationStub, $Structure$GenerationStub$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$GenerationStub"
import {$Structure$GenerationContext, $Structure$GenerationContext$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$GenerationContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $JigsawManager {

constructor()

public static "assembleJigsawStructure"(arg0: $Structure$GenerationContext$Type, arg1: $Holder$Type<($StructureTemplatePool$Type)>, arg2: $Optional$Type<($ResourceLocation$Type)>, arg3: integer, arg4: $BlockPos$Type, arg5: boolean, arg6: $Optional$Type<($Heightmap$Types$Type)>, arg7: integer, arg8: $Optional$Type<(integer)>, arg9: $Optional$Type<(integer)>): $Optional<($Structure$GenerationStub)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JigsawManager$Type = ($JigsawManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JigsawManager_ = $JigsawManager$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/codec/$CodecHelper" {
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $CodecHelper {
static "BLOCKSTATE_STRING_CODEC": $Codec<($BlockState)>

constructor()

public static "blockStateFromString"(arg0: string): $BlockState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CodecHelper$Type = ($CodecHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CodecHelper_ = $CodecHelper$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/beardifier/$EnhancedBeardifierHelper" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$StructureManager, $StructureManager$Type} from "packages/net/minecraft/world/level/$StructureManager"
import {$DensityFunction$FunctionContext, $DensityFunction$FunctionContext$Type} from "packages/net/minecraft/world/level/levelgen/$DensityFunction$FunctionContext"
import {$EnhancedBeardifierData, $EnhancedBeardifierData$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/terrainadaptation/beardifier/$EnhancedBeardifierData"
import {$Beardifier, $Beardifier$Type} from "packages/net/minecraft/world/level/levelgen/$Beardifier"

export class $EnhancedBeardifierHelper {

constructor()

public static "computeDensity"(arg0: $DensityFunction$FunctionContext$Type, arg1: double, arg2: $EnhancedBeardifierData$Type): double
public static "forStructuresInChunk"(arg0: $StructureManager$Type, arg1: $ChunkPos$Type, arg2: $Beardifier$Type): $Beardifier
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnhancedBeardifierHelper$Type = ($EnhancedBeardifierHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnhancedBeardifierHelper_ = $EnhancedBeardifierHelper$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$StructureTypeModule" {
import {$StructureType, $StructureType$Type} from "packages/net/minecraft/world/level/levelgen/structure/$StructureType"
import {$YungJigsawStructure, $YungJigsawStructure$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/$YungJigsawStructure"

export class $StructureTypeModule {
static "YUNG_JIGSAW": $StructureType<($YungJigsawStructure)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureTypeModule$Type = ($StructureTypeModule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureTypeModule_ = $StructureTypeModule$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/services/$ForgeBlockEntityTypeHelper" {
import {$Type, $Type$Type} from "packages/com/mojang/datafixers/types/$Type"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$IBlockEntityTypeHelper, $IBlockEntityTypeHelper$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/services/$IBlockEntityTypeHelper"
import {$AutoRegisterBlockEntityType$Builder, $AutoRegisterBlockEntityType$Builder$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/api/autoregister/$AutoRegisterBlockEntityType$Builder"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"

export class $ForgeBlockEntityTypeHelper implements $IBlockEntityTypeHelper {

constructor()

public "build"<T extends $BlockEntity>(arg0: $AutoRegisterBlockEntityType$Builder$Type<(T)>, arg1: $Type$Type<(any)>): $BlockEntityType<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBlockEntityTypeHelper$Type = ($ForgeBlockEntityTypeHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBlockEntityTypeHelper_ = $ForgeBlockEntityTypeHelper$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$StructurePoolElementTypeModule" {
import {$YungJigsawSinglePoolElement, $YungJigsawSinglePoolElement$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$YungJigsawSinglePoolElement"
import {$MaxCountListPoolElement, $MaxCountListPoolElement$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$MaxCountListPoolElement"
import {$YungJigsawFeatureElement, $YungJigsawFeatureElement$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$YungJigsawFeatureElement"
import {$MaxCountLegacySinglePoolElement, $MaxCountLegacySinglePoolElement$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$MaxCountLegacySinglePoolElement"
import {$MaxCountSinglePoolElement, $MaxCountSinglePoolElement$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$MaxCountSinglePoolElement"
import {$MaxCountFeaturePoolElement, $MaxCountFeaturePoolElement$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/structure/jigsaw/element/$MaxCountFeaturePoolElement"
import {$StructurePoolElementType, $StructurePoolElementType$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElementType"

export class $StructurePoolElementTypeModule {
static "MAX_COUNT_SINGLE_ELEMENT": $StructurePoolElementType<($MaxCountSinglePoolElement)>
static "MAX_COUNT_LEGACY_SINGLE_ELEMENT": $StructurePoolElementType<($MaxCountLegacySinglePoolElement)>
static "MAX_COUNT_FEATURE_ELEMENT": $StructurePoolElementType<($MaxCountFeaturePoolElement)>
static "MAX_COUNT_LIST_ELEMENT": $StructurePoolElementType<($MaxCountListPoolElement)>
static "YUNG_SINGLE_ELEMENT": $StructurePoolElementType<($YungJigsawSinglePoolElement)>
static "YUNG_FEATURE_ELEMENT": $StructurePoolElementType<($YungJigsawFeatureElement)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructurePoolElementTypeModule$Type = ($StructurePoolElementTypeModule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructurePoolElementTypeModule_ = $StructurePoolElementTypeModule$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/module/$CriteriaModule" {
import {$SafeStructureLocationTrigger, $SafeStructureLocationTrigger$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/criteria/$SafeStructureLocationTrigger"

export class $CriteriaModule {
static readonly "SAFE_STRUCTURE_LOCATION": $SafeStructureLocationTrigger

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CriteriaModule$Type = ($CriteriaModule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CriteriaModule_ = $CriteriaModule$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/spawner/$MobSpawnerData" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$SimpleWeightedRandomList, $SimpleWeightedRandomList$Type} from "packages/net/minecraft/util/random/$SimpleWeightedRandomList"
import {$SpawnData, $SpawnData$Type} from "packages/net/minecraft/world/level/$SpawnData"
import {$MobSpawnerData$Builder, $MobSpawnerData$Builder$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/spawner/$MobSpawnerData$Builder"

export class $MobSpawnerData {
readonly "spawnDelay": integer
readonly "spawnPotentials": $SimpleWeightedRandomList<($SpawnData)>
readonly "nextSpawnData": $SpawnData
readonly "minSpawnDelay": integer
readonly "maxSpawnDelay": integer
readonly "spawnCount": integer
readonly "maxNearbyEntities": integer
readonly "requiredPlayerRange": integer
readonly "spawnRange": integer

constructor(arg0: $MobSpawnerData$Builder$Type)

public static "builder"(): $MobSpawnerData$Builder
public "save"(arg0: $CompoundTag$Type): $CompoundTag
public "save"(): $CompoundTag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobSpawnerData$Type = ($MobSpawnerData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobSpawnerData_ = $MobSpawnerData$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/mixin/accessor/$CriteriaTriggersAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $CriteriaTriggersAccessor {

}

export namespace $CriteriaTriggersAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CriteriaTriggersAccessor$Type = ($CriteriaTriggersAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CriteriaTriggersAccessor_ = $CriteriaTriggersAccessor$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/world/banner/$Banner" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BannerPattern, $BannerPattern$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/world/banner/$BannerPattern"

export class $Banner {

constructor(arg0: $List$Type<($BannerPattern$Type)>, arg1: $BlockState$Type, arg2: $CompoundTag$Type)
constructor(arg0: $List$Type<($BannerPattern$Type)>, arg1: $BlockState$Type, arg2: $CompoundTag$Type, arg3: boolean)

public "getState"(): $BlockState
public "setState"(arg0: $BlockState$Type): void
public "setNbt"(arg0: $CompoundTag$Type): void
public "getNbt"(): $CompoundTag
public "getPatterns"(): $List<($BannerPattern)>
public "isWallBanner"(): boolean
public "setPatterns"(arg0: $List$Type<($BannerPattern$Type)>): void
public "setWallBanner"(arg0: boolean): void
get "state"(): $BlockState
set "state"(value: $BlockState$Type)
set "nbt"(value: $CompoundTag$Type)
get "nbt"(): $CompoundTag
get "patterns"(): $List<($BannerPattern)>
get "wallBanner"(): boolean
set "patterns"(value: $List$Type<($BannerPattern$Type)>)
set "wallBanner"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Banner$Type = ($Banner);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Banner_ = $Banner$Type;
}}
declare module "packages/com/yungnickyoung/minecraft/yungsapi/json/$ItemRandomizerAdapter" {
import {$ItemRandomizer, $ItemRandomizer$Type} from "packages/com/yungnickyoung/minecraft/yungsapi/api/world/randomize/$ItemRandomizer"
import {$TypeAdapter, $TypeAdapter$Type} from "packages/com/google/gson/$TypeAdapter"
import {$JsonWriter, $JsonWriter$Type} from "packages/com/google/gson/stream/$JsonWriter"

export class $ItemRandomizerAdapter extends $TypeAdapter<($ItemRandomizer)> {

constructor()

public "write"(arg0: $JsonWriter$Type, arg1: $ItemRandomizer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemRandomizerAdapter$Type = ($ItemRandomizerAdapter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemRandomizerAdapter_ = $ItemRandomizerAdapter$Type;
}}
