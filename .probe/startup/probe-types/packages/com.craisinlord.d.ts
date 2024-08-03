declare module "packages/com/craisinlord/integrated_api/modinit/$IAStructures" {
import {$NetherJigsawStructure, $NetherJigsawStructure$Type} from "packages/com/craisinlord/integrated_api/world/structures/$NetherJigsawStructure"
import {$StructureType, $StructureType$Type} from "packages/net/minecraft/world/level/levelgen/structure/$StructureType"
import {$ModAdaptiveStructure, $ModAdaptiveStructure$Type} from "packages/com/craisinlord/integrated_api/world/structures/$ModAdaptiveStructure"
import {$OptionalDependencyStructure, $OptionalDependencyStructure$Type} from "packages/com/craisinlord/integrated_api/world/structures/$OptionalDependencyStructure"
import {$JigsawStructure, $JigsawStructure$Type} from "packages/com/craisinlord/integrated_api/world/structures/$JigsawStructure"
import {$BiomeFacingStructure, $BiomeFacingStructure$Type} from "packages/com/craisinlord/integrated_api/world/structures/$BiomeFacingStructure"
import {$OverLavaNetherStructure, $OverLavaNetherStructure$Type} from "packages/com/craisinlord/integrated_api/world/structures/$OverLavaNetherStructure"
import {$ResourcefulRegistry, $ResourcefulRegistry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$ResourcefulRegistry"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntry"

export class $IAStructures {
static readonly "STRUCTURE_TYPE": $ResourcefulRegistry<($StructureType<(any)>)>
static "JIGSAW_STRUCTURE": $RegistryEntry<($StructureType<($JigsawStructure)>)>
static "OPTIONAL_DEPENDENCY_STRUCTURE": $RegistryEntry<($StructureType<($OptionalDependencyStructure)>)>
static "MOD_ADAPTIVE_STRUCTURE": $RegistryEntry<($StructureType<($ModAdaptiveStructure)>)>
static "NETHER_JIGSAW_STRUCTURE": $RegistryEntry<($StructureType<($NetherJigsawStructure)>)>
static "OVER_LAVA_NETHER_STRUCTURE": $RegistryEntry<($StructureType<($OverLavaNetherStructure)>)>
static "BIOME_FACING_STRUCTURE": $RegistryEntry<($StructureType<($BiomeFacingStructure)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IAStructures$Type = ($IAStructures);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IAStructures_ = $IAStructures$Type;
}}
declare module "packages/com/craisinlord/integrated_api/misc/structurepiececounter/$StructurePieceCountsManager$RequiredPieceNeeds" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $StructurePieceCountsManager$RequiredPieceNeeds extends $Record {

constructor(maxLimit: integer, minDistanceFromCenter: integer)

public "minDistanceFromCenter"(): integer
public "getMinDistanceFromCenter"(): integer
public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getRequiredAmount"(): integer
public "maxLimit"(): integer
get "requiredAmount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructurePieceCountsManager$RequiredPieceNeeds$Type = ($StructurePieceCountsManager$RequiredPieceNeeds);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructurePieceCountsManager$RequiredPieceNeeds_ = $StructurePieceCountsManager$RequiredPieceNeeds$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/structures/targetselector/$StructureTargetSelectorType" {
import {$StructureTargetSelector, $StructureTargetSelector$Type} from "packages/com/craisinlord/integrated_api/world/structures/targetselector/$StructureTargetSelector"
import {$SelfTargetSelector, $SelfTargetSelector$Type} from "packages/com/craisinlord/integrated_api/world/structures/targetselector/$SelfTargetSelector"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $StructureTargetSelectorType<C extends $StructureTargetSelector> {

 "codec"(): $Codec<(C)>

(resourceLocation: $ResourceLocation$Type, codec: $Codec$Type<(C)>): $StructureTargetSelectorType<(C)>
}

export namespace $StructureTargetSelectorType {
const TARGET_SELECTOR_TYPES_BY_NAME: $Map<($ResourceLocation), ($StructureTargetSelectorType<(any)>)>
const NAME_BY_TARGET_SELECTOR_TYPES: $Map<($StructureTargetSelectorType<(any)>), ($ResourceLocation)>
const TARGET_SELECTOR_TYPE_CODEC: $Codec<($StructureTargetSelectorType<(any)>)>
const TARGET_SELECTOR_CODEC: $Codec<($StructureTargetSelector)>
const SELF: $StructureTargetSelectorType<($SelfTargetSelector)>
function register<C>(resourceLocation: $ResourceLocation$Type, codec: $Codec$Type<(C)>): $StructureTargetSelectorType<(C)>
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
declare module "packages/com/craisinlord/integrated_api/modinit/$IATags" {
import {$Structure, $Structure$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"

export class $IATags {
static "LARGER_LOCATE_SEARCH": $TagKey<($Structure)>

constructor()

public static "initTags"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IATags$Type = ($IATags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IATags_ = $IATags$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/terrainadaptation/$NoneAdaptation" {
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$EnhancedTerrainAdaptationType, $EnhancedTerrainAdaptationType$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptationType"

export class $NoneAdaptation extends $EnhancedTerrainAdaptation {
static readonly "CODEC": $Codec<($NoneAdaptation)>
static readonly "NONE": $EnhancedTerrainAdaptation

constructor()

public "computeDensityFactor"(xDistance: integer, yDistance: integer, zDistance: integer, yDistanceToBeardBase: integer): double
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
declare module "packages/com/craisinlord/integrated_api/modinit/registry/$EnumResourcefulRegistryChild" {
import {$RegistryEntries, $RegistryEntries$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntries"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourcefulRegistry, $ResourcefulRegistry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$ResourcefulRegistry"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntry"
import {$ResourcefulRegistryChild, $ResourcefulRegistryChild$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$ResourcefulRegistryChild"

export class $EnumResourcefulRegistryChild<E extends $Enum<(E)>, T> extends $ResourcefulRegistryChild<(T)> {

constructor(enumClass: $Class$Type<(E)>, parent: $ResourcefulRegistry$Type<(T)>)

public "register"<I extends T>(enumValue: E, id: string, supplier: $Supplier$Type<(I)>): $RegistryEntry<(I)>
public "getEntries"(enumValue: E): $RegistryEntries<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumResourcefulRegistryChild$Type<E, T> = ($EnumResourcefulRegistryChild<(E), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumResourcefulRegistryChild_<E, T> = $EnumResourcefulRegistryChild$Type<(E), (T)>;
}}
declare module "packages/com/craisinlord/integrated_api/world/structures/$JigsawStructure$BURYING_TYPE" {
import {$StringRepresentable$EnumCodec, $StringRepresentable$EnumCodec$Type} from "packages/net/minecraft/util/$StringRepresentable$EnumCodec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $JigsawStructure$BURYING_TYPE extends $Enum<($JigsawStructure$BURYING_TYPE)> implements $StringRepresentable {
static readonly "LOWEST_CORNER": $JigsawStructure$BURYING_TYPE
static readonly "AVERAGE_LAND": $JigsawStructure$BURYING_TYPE
static readonly "LOWEST_SIDE": $JigsawStructure$BURYING_TYPE


public static "values"(): ($JigsawStructure$BURYING_TYPE)[]
public static "valueOf"(name: string): $JigsawStructure$BURYING_TYPE
public "getSerializedName"(): string
public static "byName"(name: string): $JigsawStructure$BURYING_TYPE
public static "fromEnumWithMapping"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(string), (string)>): $StringRepresentable$EnumCodec<(E)>
public static "keys"(arg0: ($StringRepresentable$Type)[]): $Keyable
public static "fromEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>): $StringRepresentable$EnumCodec<(E)>
get "serializedName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JigsawStructure$BURYING_TYPE$Type = (("average_land") | ("lowest_side") | ("lowest_corner")) | ($JigsawStructure$BURYING_TYPE);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JigsawStructure$BURYING_TYPE_ = $JigsawStructure$BURYING_TYPE$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/processors/$CloseOffAirSourcesProcessor" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $CloseOffAirSourcesProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($CloseOffAirSourcesProcessor)>

constructor(weightedReplacementBlocks: $List$Type<($Pair$Type<($Block$Type), (integer)>)>)

public "processBlock"(levelReader: $LevelReader$Type, pos: $BlockPos$Type, pos2: $BlockPos$Type, infoIn1: $StructureTemplate$StructureBlockInfo$Type, infoIn2: $StructureTemplate$StructureBlockInfo$Type, settings: $StructurePlaceSettings$Type): $StructureTemplate$StructureBlockInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CloseOffAirSourcesProcessor$Type = ($CloseOffAirSourcesProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CloseOffAirSourcesProcessor_ = $CloseOffAirSourcesProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/processors/$FillEndPortalFrameProcessor" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $FillEndPortalFrameProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($FillEndPortalFrameProcessor)>

constructor(probability: float)

public "processBlock"(worldView: $LevelReader$Type, pos: $BlockPos$Type, blockPos: $BlockPos$Type, structureBlockInfoLocal: $StructureTemplate$StructureBlockInfo$Type, structureBlockInfoWorld: $StructureTemplate$StructureBlockInfo$Type, structurePlacementData: $StructurePlaceSettings$Type): $StructureTemplate$StructureBlockInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FillEndPortalFrameProcessor$Type = ($FillEndPortalFrameProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FillEndPortalFrameProcessor_ = $FillEndPortalFrameProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/structures/action/$StructureActionType" {
import {$TransformAction, $TransformAction$Type} from "packages/com/craisinlord/integrated_api/world/structures/action/$TransformAction"
import {$DelayGenerationAction, $DelayGenerationAction$Type} from "packages/com/craisinlord/integrated_api/world/structures/action/$DelayGenerationAction"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$StructureAction, $StructureAction$Type} from "packages/com/craisinlord/integrated_api/world/structures/action/$StructureAction"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $StructureActionType<C extends $StructureAction> {

 "codec"(): $Codec<(C)>

(resourceLocation: $ResourceLocation$Type, codec: $Codec$Type<(C)>): $StructureActionType<(C)>
}

export namespace $StructureActionType {
const ACTION_TYPES_BY_NAME: $Map<($ResourceLocation), ($StructureActionType<(any)>)>
const NAME_BY_ACTION_TYPES: $Map<($StructureActionType<(any)>), ($ResourceLocation)>
const ACTION_TYPE_CODEC: $Codec<($StructureActionType<(any)>)>
const ACTION_CODEC: $Codec<($StructureAction)>
const TRANSFORM: $StructureActionType<($TransformAction)>
const DELAY_GENERATION: $StructureActionType<($DelayGenerationAction)>
function register<C>(resourceLocation: $ResourceLocation$Type, codec: $Codec$Type<(C)>): $StructureActionType<(C)>
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
declare module "packages/com/craisinlord/integrated_api/world/processors/$CloseOffFluidSourcesProcessor" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $CloseOffFluidSourcesProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($CloseOffFluidSourcesProcessor)>

constructor(weightedReplacementBlocks: $List$Type<($Pair$Type<($Block$Type), (integer)>)>, ignoreDown: boolean, ifAirInWorld: boolean)

public "processBlock"(levelReader: $LevelReader$Type, pos: $BlockPos$Type, pos2: $BlockPos$Type, infoIn1: $StructureTemplate$StructureBlockInfo$Type, infoIn2: $StructureTemplate$StructureBlockInfo$Type, settings: $StructurePlaceSettings$Type): $StructureTemplate$StructureBlockInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CloseOffFluidSourcesProcessor$Type = ($CloseOffFluidSourcesProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CloseOffFluidSourcesProcessor_ = $CloseOffFluidSourcesProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_villages/world/processors/$WorkstationProcessor" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $WorkstationProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($WorkstationProcessor)>

constructor(inputBlock: $Block$Type, workstationType: string, enableIntegration: boolean)

public "processBlock"(worldView: $LevelReader$Type, pos: $BlockPos$Type, blockPos: $BlockPos$Type, structureBlockInfoLocal: $StructureTemplate$StructureBlockInfo$Type, structureBlockInfoWorld: $StructureTemplate$StructureBlockInfo$Type, structurePlacementData: $StructurePlaceSettings$Type): $StructureTemplate$StructureBlockInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorkstationProcessor$Type = ($WorkstationProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorkstationProcessor_ = $WorkstationProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/utils/$OpenSimplex2F" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $OpenSimplex2F {

constructor(seed: long)

public "noise4_Classic"(x: double, y: double, z: double, w: double): double
public "noise2"(x: double, y: double): double
public "noise2_XBeforeY"(x: double, y: double): double
public "noise3_XYBeforeZ"(x: double, y: double, z: double): double
public "noise3_XZBeforeY"(x: double, y: double, z: double): double
public "noise3_Classic"(x: double, y: double, z: double): double
public "noise4_XYZBeforeW"(x: double, y: double, z: double, w: double): double
public "noise4_XZBeforeYW"(x: double, y: double, z: double, w: double): double
public "noise4_XYBeforeZW"(x: double, y: double, z: double, w: double): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OpenSimplex2F$Type = ($OpenSimplex2F);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OpenSimplex2F_ = $OpenSimplex2F$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/processors/$IntegratedBlockReplaceProcessor" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $IntegratedBlockReplaceProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($IntegratedBlockReplaceProcessor)>


public "processBlock"(worldView: $LevelReader$Type, pos: $BlockPos$Type, blockPos: $BlockPos$Type, structureBlockInfoLocal: $StructureTemplate$StructureBlockInfo$Type, structureBlockInfoWorld: $StructureTemplate$StructureBlockInfo$Type, structurePlacementData: $StructurePlaceSettings$Type): $StructureTemplate$StructureBlockInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntegratedBlockReplaceProcessor$Type = ($IntegratedBlockReplaceProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntegratedBlockReplaceProcessor_ = $IntegratedBlockReplaceProcessor$Type;
}}
declare module "packages/com/craisinlord/idas/config/$ConfigGeneralForge" {
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"

export class $ConfigGeneralForge {
readonly "disableIaFStructures": $ForgeConfigSpec$ConfigValue<(boolean)>
readonly "applyMiningFatigue": $ForgeConfigSpec$ConfigValue<(boolean)>

constructor(arg0: $ForgeConfigSpec$Builder$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigGeneralForge$Type = ($ConfigGeneralForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigGeneralForge_ = $ConfigGeneralForge$Type;
}}
declare module "packages/com/craisinlord/integrated_api/events/base/$ReturnableEventHandler$ReturnableFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ReturnableEventHandler$ReturnableFunction<T, R> {

 "apply"(arg0: R, arg1: T): R

(arg0: R, arg1: T): R
}

export namespace $ReturnableEventHandler$ReturnableFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReturnableEventHandler$ReturnableFunction$Type<T, R> = ($ReturnableEventHandler$ReturnableFunction<(T), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReturnableEventHandler$ReturnableFunction_<T, R> = $ReturnableEventHandler$ReturnableFunction$Type<(T), (R)>;
}}
declare module "packages/com/craisinlord/integrated_api/world/condition/$PieceInHorizontalDirectionCondition" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructureContext, $StructureContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext"
import {$StructureCondition, $StructureCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureCondition"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureConditionType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $PieceInHorizontalDirectionCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($PieceInHorizontalDirectionCondition)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(pieces: $List$Type<($ResourceLocation$Type)>, range: integer, rotation: $Rotation$Type)

public "type"(): $StructureConditionType<(any)>
public "passes"(ctx: $StructureContext$Type): boolean
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
declare module "packages/com/craisinlord/integrated_api/mixins/structures/$BeardifierAccessor" {
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
declare module "packages/com/craisinlord/integrated_api/world/condition/$AnyOfCondition" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructureContext, $StructureContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext"
import {$StructureCondition, $StructureCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureCondition"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureConditionType"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $AnyOfCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($AnyOfCondition)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(conditions: $List$Type<($StructureCondition$Type)>)

public "type"(): $StructureConditionType<(any)>
public "passes"(ctx: $StructureContext$Type): boolean
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
declare module "packages/com/craisinlord/integrated_api/world/condition/$PieceInRangeCondition" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructureContext, $StructureContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext"
import {$StructureCondition, $StructureCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureCondition"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureConditionType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $PieceInRangeCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($PieceInRangeCondition)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(pieces: $List$Type<($ResourceLocation$Type)>, aboveRange: integer, horizontalRange: integer, belowRange: integer)

public "type"(): $StructureConditionType<(any)>
public "passes"(ctx: $StructureContext$Type): boolean
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
declare module "packages/com/craisinlord/integrated_api/world/structures/targetselector/$SelfTargetSelector" {
import {$StructureTargetSelector, $StructureTargetSelector$Type} from "packages/com/craisinlord/integrated_api/world/structures/targetselector/$StructureTargetSelector"
import {$StructureTargetSelectorType, $StructureTargetSelectorType$Type} from "packages/com/craisinlord/integrated_api/world/structures/targetselector/$StructureTargetSelectorType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructureContext, $StructureContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$PieceEntry, $PieceEntry$Type} from "packages/com/craisinlord/integrated_api/world/structures/pieces/assembler/$PieceEntry"

export class $SelfTargetSelector extends $StructureTargetSelector {
static readonly "CODEC": $Codec<($SelfTargetSelector)>

constructor()

public "type"(): $StructureTargetSelectorType<(any)>
public "apply"(ctx: $StructureContext$Type): $List<($PieceEntry)>
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
declare module "packages/com/craisinlord/integrated_stronghold/item/$ISItems" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$Rarity, $Rarity$Type} from "packages/net/minecraft/world/item/$Rarity"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $ISItems {
static readonly "ITEMS": $DeferredRegister<($Item)>
static readonly "RARITY_ANTIQUE": $Rarity
static readonly "DISC_FRAGMENT_SIGHT": $RegistryObject<($Item)>
static readonly "MUSIC_DISC_SIGHT": $RegistryObject<($Item)>
static readonly "MUSIC_DISC_FORLORN": $RegistryObject<($Item)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISItems$Type = ($ISItems);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISItems_ = $ISItems$Type;
}}
declare module "packages/com/craisinlord/integrated_api/modinit/registry/forge/$ResourcefulRegistriesImpl" {
import {$Pair, $Pair$Type} from "packages/org/apache/commons/lang3/tuple/$Pair"
import {$CustomRegistryLookup, $CustomRegistryLookup$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$CustomRegistryLookup"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$NewRegistryEvent, $NewRegistryEvent$Type} from "packages/net/minecraftforge/registries/$NewRegistryEvent"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ResourcefulRegistry, $ResourcefulRegistry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$ResourcefulRegistry"

export class $ResourcefulRegistriesImpl {

constructor()

public static "create"<T>(registry: $Registry$Type<(T)>, id: string): $ResourcefulRegistry<(T)>
public static "createCustomRegistryInternal"<T, K extends $Registry<(T)>>(modId: string, key: $ResourceKey$Type<(K)>, save: boolean, sync: boolean, allowModification: boolean): $Pair<($Supplier<($CustomRegistryLookup<(T)>)>), ($ResourcefulRegistry<(T)>)>
public static "onRegisterForgeRegistries"(event: $NewRegistryEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResourcefulRegistriesImpl$Type = ($ResourcefulRegistriesImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResourcefulRegistriesImpl_ = $ResourcefulRegistriesImpl$Type;
}}
declare module "packages/com/craisinlord/integrated_api/modinit/registry/$CustomRegistry" {
import {$CustomRegistryLookup, $CustomRegistryLookup$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$CustomRegistryLookup"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntry"
import {$ResourcefulRegistry, $ResourcefulRegistry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$ResourcefulRegistry"

export interface $CustomRegistry<T> extends $ResourcefulRegistry<(T)> {

 "stream"(): $Stream<($RegistryEntry<(T)>)>
 "lookup"(): $CustomRegistryLookup<(T)>
 "register"<I extends T>(id: string, supplier: $Supplier$Type<(I)>): $RegistryEntry<(I)>
 "init"(): void
 "getEntries"(): $Collection<($RegistryEntry<(T)>)>
 "registry"(): $ResourcefulRegistry<(T)>
}

export namespace $CustomRegistry {
function of<T, K>(modId: string, key: $ResourceKey$Type<(K)>, save: boolean, sync: boolean, allowModification: boolean): $CustomRegistry<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomRegistry$Type<T> = ($CustomRegistry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomRegistry_<T> = $CustomRegistry$Type<(T)>;
}}
declare module "packages/com/craisinlord/integrated_api/world/condition/$NotCondition" {
import {$StructureContext, $StructureContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext"
import {$StructureCondition, $StructureCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureCondition"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureConditionType"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $NotCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($NotCondition)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(condition: $StructureCondition$Type)

public "type"(): $StructureConditionType<(any)>
public "passes"(ctx: $StructureContext$Type): boolean
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
declare module "packages/com/craisinlord/integrated_api/mixins/structures/$PoolElementStructurePieceAccessor" {
import {$StructureTemplateManager, $StructureTemplateManager$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplateManager"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"

export interface $PoolElementStructurePieceAccessor {

 "getStructureManager"(): $StructureTemplateManager
 "setRotation"(arg0: $Rotation$Type): void
}

export namespace $PoolElementStructurePieceAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PoolElementStructurePieceAccessor$Type = ($PoolElementStructurePieceAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PoolElementStructurePieceAccessor_ = $PoolElementStructurePieceAccessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/processors/$CappedStructureSurfaceProcessor" {
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ServerLevelAccessor, $ServerLevelAccessor$Type} from "packages/net/minecraft/world/level/$ServerLevelAccessor"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $CappedStructureSurfaceProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($CappedStructureSurfaceProcessor)>

constructor(structureProcessor: $StructureProcessor$Type, allowVoidSides: boolean)

public "finalizeProcessing"(serverLevelAccessor: $ServerLevelAccessor$Type, nbtOriginPos: $BlockPos$Type, chunkCenter: $BlockPos$Type, nbtOriginBlockInfo: $List$Type<($StructureTemplate$StructureBlockInfo$Type)>, worldOriginBlockInfo: $List$Type<($StructureTemplate$StructureBlockInfo$Type)>, structurePlaceSettings: $StructurePlaceSettings$Type): $List<($StructureTemplate$StructureBlockInfo)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CappedStructureSurfaceProcessor$Type = ($CappedStructureSurfaceProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CappedStructureSurfaceProcessor_ = $CappedStructureSurfaceProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/events/lifecycle/$SetupEvent" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$EventHandler, $EventHandler$Type} from "packages/com/craisinlord/integrated_api/events/base/$EventHandler"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $SetupEvent extends $Record {
static readonly "EVENT": $EventHandler<($SetupEvent)>

constructor(enqueue: $Consumer$Type<($Runnable$Type)>)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "enqueue"(): $Consumer<($Runnable)>
public "enqueueWork"(runnable: $Runnable$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SetupEvent$Type = ($SetupEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SetupEvent_ = $SetupEvent$Type;
}}
declare module "packages/com/craisinlord/integrated_api/events/base/$CancellableEventHandler$CancellableFunctionOnlyReturn" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $CancellableEventHandler$CancellableFunctionOnlyReturn<T> {

 "apply"(arg0: T): boolean

(arg0: T): boolean
}

export namespace $CancellableEventHandler$CancellableFunctionOnlyReturn {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CancellableEventHandler$CancellableFunctionOnlyReturn$Type<T> = ($CancellableEventHandler$CancellableFunctionOnlyReturn<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CancellableEventHandler$CancellableFunctionOnlyReturn_<T> = $CancellableEventHandler$CancellableFunctionOnlyReturn$Type<(T)>;
}}
declare module "packages/com/craisinlord/integrated_api/mixins/structures/$StructurePoolAccessor" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$StructurePoolElement, $StructurePoolElement$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElement"
import {$ObjectArrayList, $ObjectArrayList$Type} from "packages/it/unimi/dsi/fastutil/objects/$ObjectArrayList"

export interface $StructurePoolAccessor {

 "integratedapi_getRawTemplates"(): $List<($Pair<($StructurePoolElement), (integer)>)>
 "integratedapi_setRawTemplates"(arg0: $List$Type<($Pair$Type<($StructurePoolElement$Type), (integer)>)>): void
 "integratedapi_getTemplates"(): $ObjectArrayList<($StructurePoolElement)>
 "integratedapi_setTemplates"(arg0: $ObjectArrayList$Type<($StructurePoolElement$Type)>): void
}

export namespace $StructurePoolAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructurePoolAccessor$Type = ($StructurePoolAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructurePoolAccessor_ = $StructurePoolAccessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/structures/placements/$AdvancedRandomSpread$SuperExclusionZone" {
import {$HolderSet, $HolderSet$Type} from "packages/net/minecraft/core/$HolderSet"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$StructureSet, $StructureSet$Type} from "packages/net/minecraft/world/level/levelgen/structure/$StructureSet"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $AdvancedRandomSpread$SuperExclusionZone extends $Record {
static readonly "CODEC": $Codec<($AdvancedRandomSpread$SuperExclusionZone)>

constructor(otherSet: $HolderSet$Type<($StructureSet$Type)>, chunkCount: integer, allowedChunkCount: $Optional$Type<(integer)>)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "chunkCount"(): integer
public "allowedChunkCount"(): $Optional<(integer)>
public "otherSet"(): $HolderSet<($StructureSet)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdvancedRandomSpread$SuperExclusionZone$Type = ($AdvancedRandomSpread$SuperExclusionZone);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdvancedRandomSpread$SuperExclusionZone_ = $AdvancedRandomSpread$SuperExclusionZone$Type;
}}
declare module "packages/com/craisinlord/integrated_villages/$IntegratedVillages" {
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$ConfigModule, $ConfigModule$Type} from "packages/com/craisinlord/integrated_villages/config/$ConfigModule"

export class $IntegratedVillages {
static readonly "MODID": string
static readonly "LOGGER": $Logger
static readonly "CONFIG": $ConfigModule

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntegratedVillages$Type = ($IntegratedVillages);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntegratedVillages_ = $IntegratedVillages$Type;
}}
declare module "packages/com/craisinlord/integrated_api/mixins/structures/$JigsawJunctionAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $JigsawJunctionAccessor {

 "setSourceX"(arg0: integer): void
 "setSourceGroundY"(arg0: integer): void
 "setSourceZ"(arg0: integer): void
}

export namespace $JigsawJunctionAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JigsawJunctionAccessor$Type = ($JigsawJunctionAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JigsawJunctionAccessor_ = $JigsawJunctionAccessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/misc/maptrades/$StructureMapManager" {
import {$SimpleJsonResourceReloadListener, $SimpleJsonResourceReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimpleJsonResourceReloadListener"
import {$List, $List$Type} from "packages/java/util/$List"
import {$WanderingTraderMapObj, $WanderingTraderMapObj$Type} from "packages/com/craisinlord/integrated_api/misc/maptrades/$WanderingTraderMapObj"
import {$WanderingTraderMapObj$TRADE_TYPE, $WanderingTraderMapObj$TRADE_TYPE$Type} from "packages/com/craisinlord/integrated_api/misc/maptrades/$WanderingTraderMapObj$TRADE_TYPE"
import {$VillagerMapObj, $VillagerMapObj$Type} from "packages/com/craisinlord/integrated_api/misc/maptrades/$VillagerMapObj"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $StructureMapManager extends $SimpleJsonResourceReloadListener {
static readonly "STRUCTURE_MAP_MANAGER": $StructureMapManager
 "VILLAGER_MAP_TRADES": $Map<(string), ($List<($VillagerMapObj)>)>
 "WANDERING_TRADER_MAP_TRADES": $Map<($WanderingTraderMapObj$TRADE_TYPE), ($List<($WanderingTraderMapObj)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureMapManager$Type = ($StructureMapManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureMapManager_ = $StructureMapManager$Type;
}}
declare module "packages/com/craisinlord/integrated_api/events/base/$CancellableEventHandler" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$CancellableEventHandler$CancellableFunctionNoReturn, $CancellableEventHandler$CancellableFunctionNoReturn$Type} from "packages/com/craisinlord/integrated_api/events/base/$CancellableEventHandler$CancellableFunctionNoReturn"
import {$CancellableEventHandler$CancellableFunctionOnlyReturn, $CancellableEventHandler$CancellableFunctionOnlyReturn$Type} from "packages/com/craisinlord/integrated_api/events/base/$CancellableEventHandler$CancellableFunctionOnlyReturn"
import {$CancellableEventHandler$CancellableFunction, $CancellableEventHandler$CancellableFunction$Type} from "packages/com/craisinlord/integrated_api/events/base/$CancellableEventHandler$CancellableFunction"

export class $CancellableEventHandler<T> {

constructor()

public "invoke"(event: T, isCancelled: boolean): boolean
public "invoke"(event: T): boolean
public "removeListener"(listener: $CancellableEventHandler$CancellableFunction$Type<(T)>): void
public "addListener"(listener: $Consumer$Type<(T)>): void
public "addListener"(listener: $CancellableEventHandler$CancellableFunction$Type<(T)>): void
public "addListener"(listener: $CancellableEventHandler$CancellableFunctionNoReturn$Type<(T)>): void
public "addListener"(listener: $CancellableEventHandler$CancellableFunctionOnlyReturn$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CancellableEventHandler$Type<T> = ($CancellableEventHandler<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CancellableEventHandler_<T> = $CancellableEventHandler$Type<(T)>;
}}
declare module "packages/com/craisinlord/integrated_villages/$IntegratedVilagesProcessors" {
import {$WindmillBearingProcessor, $WindmillBearingProcessor$Type} from "packages/com/craisinlord/integrated_villages/world/processors/$WindmillBearingProcessor"
import {$StructureProcessorType, $StructureProcessorType$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessorType"
import {$MechanicalBearingProcessor, $MechanicalBearingProcessor$Type} from "packages/com/craisinlord/integrated_villages/world/processors/$MechanicalBearingProcessor"
import {$TickBlocksProcessor, $TickBlocksProcessor$Type} from "packages/com/craisinlord/integrated_villages/world/processors/$TickBlocksProcessor"
import {$WorkstationProcessor, $WorkstationProcessor$Type} from "packages/com/craisinlord/integrated_villages/world/processors/$WorkstationProcessor"
import {$ResourcefulRegistry, $ResourcefulRegistry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$ResourcefulRegistry"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntry"

export class $IntegratedVilagesProcessors {
static readonly "STRUCTURE_PROCESSOR": $ResourcefulRegistry<($StructureProcessorType<(any)>)>
static readonly "WORKSTATION_PROCESSOR": $RegistryEntry<($StructureProcessorType<($WorkstationProcessor)>)>
static readonly "WINDMILL_BEARING_PROCESSOR": $RegistryEntry<($StructureProcessorType<($WindmillBearingProcessor)>)>
static readonly "MECHANICAL_BEARING_PROCESSOR": $RegistryEntry<($StructureProcessorType<($MechanicalBearingProcessor)>)>
static readonly "TICK_BLOCKS_PROCESSOR": $RegistryEntry<($StructureProcessorType<($TickBlocksProcessor)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntegratedVilagesProcessors$Type = ($IntegratedVilagesProcessors);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntegratedVilagesProcessors_ = $IntegratedVilagesProcessors$Type;
}}
declare module "packages/com/craisinlord/integrated_api/events/lifecycle/$RegisterReloadListenerEvent" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$PreparableReloadListener, $PreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$EventHandler, $EventHandler$Type} from "packages/com/craisinlord/integrated_api/events/base/$EventHandler"

export class $RegisterReloadListenerEvent extends $Record {
static readonly "EVENT": $EventHandler<($RegisterReloadListenerEvent)>

constructor(registrar: $BiConsumer$Type<($ResourceLocation$Type), ($PreparableReloadListener$Type)>)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "register"(id: $ResourceLocation$Type, listener: $PreparableReloadListener$Type): void
public "registrar"(): $BiConsumer<($ResourceLocation), ($PreparableReloadListener)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegisterReloadListenerEvent$Type = ($RegisterReloadListenerEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegisterReloadListenerEvent_ = $RegisterReloadListenerEvent$Type;
}}
declare module "packages/com/craisinlord/integrated_api/$IntegratedAPI" {
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$RegisterReloadListenerEvent, $RegisterReloadListenerEvent$Type} from "packages/com/craisinlord/integrated_api/events/lifecycle/$RegisterReloadListenerEvent"

export class $IntegratedAPI {
static readonly "MODID": string
static readonly "LOGGER": $Logger
static readonly "NEW_STRUCTURE_SIZE": integer

constructor()

public static "init"(): void
public static "registerDatapackListener"(event: $RegisterReloadListenerEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntegratedAPI$Type = ($IntegratedAPI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntegratedAPI_ = $IntegratedAPI$Type;
}}
declare module "packages/com/craisinlord/integrated_api/mixins/structures/$TemplateAccessor" {
import {$StructureTemplate$Palette, $StructureTemplate$Palette$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$Palette"
import {$List, $List$Type} from "packages/java/util/$List"

export interface $TemplateAccessor {

 "integratedapi_getPalettes"(): $List<($StructureTemplate$Palette)>

(): $List<($StructureTemplate$Palette)>
}

export namespace $TemplateAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TemplateAccessor$Type = ($TemplateAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TemplateAccessor_ = $TemplateAccessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/structures/pieces/$IAPoolElement" {
import {$BoundingBox, $BoundingBox$Type} from "packages/net/minecraft/world/level/levelgen/structure/$BoundingBox"
import {$RecordCodecBuilder, $RecordCodecBuilder$Type} from "packages/com/mojang/serialization/codecs/$RecordCodecBuilder"
import {$StructureTemplatePool$Projection, $StructureTemplatePool$Projection$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool$Projection"
import {$ChunkGenerator, $ChunkGenerator$Type} from "packages/net/minecraft/world/level/chunk/$ChunkGenerator"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$StructureCondition, $StructureCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureCondition"
import {$StructurePoolElement, $StructurePoolElement$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElement"
import {$WorldGenLevel, $WorldGenLevel$Type} from "packages/net/minecraft/world/level/$WorldGenLevel"
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$StructureTemplateManager, $StructureTemplateManager$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplateManager"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$StructureContext, $StructureContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$StructureManager, $StructureManager$Type} from "packages/net/minecraft/world/level/$StructureManager"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$StructurePoolElementType, $StructurePoolElementType$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElementType"

export class $IAPoolElement extends $StructurePoolElement {
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

constructor(projection: $StructureTemplatePool$Projection$Type, name: $Optional$Type<(string)>, maxCount: $Optional$Type<(integer)>, minRequiredDepth: $Optional$Type<(integer)>, maxPossibleDepth: $Optional$Type<(integer)>, isPriority: boolean, ignoreBounds: boolean, condition: $StructureCondition$Type, enhancedTerrainAdaptation: $Optional$Type<($EnhancedTerrainAdaptation$Type)>)

/**
 * 
 * @deprecated
 */
public "getMinRequiredDepth"(): $Optional<(integer)>
public static "minRequiredDepthCodec"<E extends $IAPoolElement>(): $RecordCodecBuilder<(E), ($Optional<(integer)>)>
public static "maxPossibleDepthCodec"<E extends $IAPoolElement>(): $RecordCodecBuilder<(E), ($Optional<(integer)>)>
public "getEnhancedTerrainAdaptation"(): $Optional<($EnhancedTerrainAdaptation)>
/**
 * 
 * @deprecated
 */
public "getMaxPossibleDepth"(): $Optional<(integer)>
public static "enhancedTerrainAdaptationCodec"<E extends $IAPoolElement>(): $RecordCodecBuilder<(E), ($Optional<($EnhancedTerrainAdaptation)>)>
public "getName"(): $Optional<(string)>
public static "conditionCodec"<E extends $IAPoolElement>(): $RecordCodecBuilder<(E), ($StructureCondition)>
public "isPriorityPiece"(): boolean
public static "ignoreBoundsCodec"<E extends $IAPoolElement>(): $RecordCodecBuilder<(E), (boolean)>
public "isAtValidDepth"(depth: integer): boolean
public static "isPriorityCodec"<E extends $IAPoolElement>(): $RecordCodecBuilder<(E), (boolean)>
public static "nameCodec"<E extends $IAPoolElement>(): $RecordCodecBuilder<(E), ($Optional<(string)>)>
public "passesConditions"(ctx: $StructureContext$Type): boolean
public "ignoresBounds"(): boolean
public static "maxCountCodec"<E extends $IAPoolElement>(): $RecordCodecBuilder<(E), ($Optional<(integer)>)>
public "getMaxCount"(): $Optional<(integer)>
public "getBoundingBox"(structureTemplateManager: $StructureTemplateManager$Type, blockPos: $BlockPos$Type, rotation: $Rotation$Type): $BoundingBox
public "place"(structureTemplateManager: $StructureTemplateManager$Type, worldGenLevel: $WorldGenLevel$Type, structureManager: $StructureManager$Type, chunkGenerator: $ChunkGenerator$Type, blockPos: $BlockPos$Type, blockPos2: $BlockPos$Type, rotation: $Rotation$Type, boundingBox: $BoundingBox$Type, randomSource: $RandomSource$Type, bl: boolean): boolean
public "getShuffledJigsawBlocks"(structureTemplateManager: $StructureTemplateManager$Type, blockPos: $BlockPos$Type, rotation: $Rotation$Type, randomSource: $RandomSource$Type): $List<($StructureTemplate$StructureBlockInfo)>
public "getType"(): $StructurePoolElementType<(any)>
public "getSize"(structureTemplateManager: $StructureTemplateManager$Type, rotation: $Rotation$Type): $Vec3i
public "getCondition"(): $StructureCondition
get "minRequiredDepth"(): $Optional<(integer)>
get "enhancedTerrainAdaptation"(): $Optional<($EnhancedTerrainAdaptation)>
get "maxPossibleDepth"(): $Optional<(integer)>
get "name"(): $Optional<(string)>
get "priorityPiece"(): boolean
get "priorityCodec"(): boolean
get "maxCount"(): $Optional<(integer)>
get "type"(): $StructurePoolElementType<(any)>
get "condition"(): $StructureCondition
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IAPoolElement$Type = ($IAPoolElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IAPoolElement_ = $IAPoolElement$Type;
}}
declare module "packages/com/craisinlord/integrated_api/misc/maptrades/$WanderingTraderMapObj" {
import {$WanderingTraderMapObj$TRADE_TYPE, $WanderingTraderMapObj$TRADE_TYPE$Type} from "packages/com/craisinlord/integrated_api/misc/maptrades/$WanderingTraderMapObj$TRADE_TYPE"

export class $WanderingTraderMapObj {
readonly "tradeType": $WanderingTraderMapObj$TRADE_TYPE
readonly "structure": string
readonly "mapIcon": string
readonly "mapName": string
readonly "emeraldsRequired": integer
readonly "tradesAllowed": integer
readonly "xpReward": integer
readonly "spawnRegionSearchRadius": integer

constructor(tradeType: $WanderingTraderMapObj$TRADE_TYPE$Type, structure: string, mapIcon: string, mapName: string, emeraldsRequired: integer, tradesAllowed: integer, xpReward: integer, spawnRegionSearchRadius: integer)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WanderingTraderMapObj$Type = ($WanderingTraderMapObj);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WanderingTraderMapObj_ = $WanderingTraderMapObj$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/processors/$FloodWithWaterProcessor" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $FloodWithWaterProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($FloodWithWaterProcessor)>


public "processBlock"(levelReader: $LevelReader$Type, pos: $BlockPos$Type, blockPos: $BlockPos$Type, structureBlockInfoLocal: $StructureTemplate$StructureBlockInfo$Type, structureBlockInfoWorld: $StructureTemplate$StructureBlockInfo$Type, structurePlacementData: $StructurePlaceSettings$Type): $StructureTemplate$StructureBlockInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FloodWithWaterProcessor$Type = ($FloodWithWaterProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FloodWithWaterProcessor_ = $FloodWithWaterProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/modinit/$IAPredicates" {
import {$PosRuleTestType, $PosRuleTestType$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$PosRuleTestType"
import {$RuleTestType, $RuleTestType$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$RuleTestType"
import {$YValuePosRuleTest, $YValuePosRuleTest$Type} from "packages/com/craisinlord/integrated_api/world/predicates/$YValuePosRuleTest"
import {$MatterPhaseRuleTest, $MatterPhaseRuleTest$Type} from "packages/com/craisinlord/integrated_api/world/predicates/$MatterPhaseRuleTest"
import {$PieceOriginAxisAlignedLinearPosRuleTest, $PieceOriginAxisAlignedLinearPosRuleTest$Type} from "packages/com/craisinlord/integrated_api/world/predicates/$PieceOriginAxisAlignedLinearPosRuleTest"
import {$ResourcefulRegistry, $ResourcefulRegistry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$ResourcefulRegistry"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntry"

export class $IAPredicates {
static readonly "RULE_TEST": $ResourcefulRegistry<($RuleTestType<(any)>)>
static readonly "POS_RULE_TEST": $ResourcefulRegistry<($PosRuleTestType<(any)>)>
static readonly "MATTER_PHASE_RULE_TEST": $RegistryEntry<($RuleTestType<($MatterPhaseRuleTest)>)>
static readonly "PIECE_ORIGIN_AXIS_ALIGNED_LINEAR_POS_RULE_TEST": $RegistryEntry<($PosRuleTestType<($PieceOriginAxisAlignedLinearPosRuleTest)>)>
static readonly "Y_VALUE_POS_RULE_TEST": $RegistryEntry<($PosRuleTestType<($YValuePosRuleTest)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IAPredicates$Type = ($IAPredicates);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IAPredicates_ = $IAPredicates$Type;
}}
declare module "packages/com/craisinlord/integrated_villages/config/$ConfigGeneralForge" {
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"

export class $ConfigGeneralForge {
readonly "disableVanillaVillages": $ForgeConfigSpec$ConfigValue<(boolean)>
readonly "activateCreateContraptions": $ForgeConfigSpec$ConfigValue<(boolean)>

constructor(BUILDER: $ForgeConfigSpec$Builder$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigGeneralForge$Type = ($ConfigGeneralForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigGeneralForge_ = $ConfigGeneralForge$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/forge/$AdditionsTemperatureModifier$TEMPERATURE_RANGE" {
import {$StringRepresentable$EnumCodec, $StringRepresentable$EnumCodec$Type} from "packages/net/minecraft/util/$StringRepresentable$EnumCodec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $AdditionsTemperatureModifier$TEMPERATURE_RANGE extends $Enum<($AdditionsTemperatureModifier$TEMPERATURE_RANGE)> implements $StringRepresentable {
static readonly "WARM": $AdditionsTemperatureModifier$TEMPERATURE_RANGE
static readonly "LUKEWARM": $AdditionsTemperatureModifier$TEMPERATURE_RANGE
static readonly "NEUTRAL": $AdditionsTemperatureModifier$TEMPERATURE_RANGE
static readonly "COLD": $AdditionsTemperatureModifier$TEMPERATURE_RANGE
static readonly "FROZEN": $AdditionsTemperatureModifier$TEMPERATURE_RANGE


public static "values"(): ($AdditionsTemperatureModifier$TEMPERATURE_RANGE)[]
public static "valueOf"(name: string): $AdditionsTemperatureModifier$TEMPERATURE_RANGE
public "getSerializedName"(): string
public static "byName"(name: string): $AdditionsTemperatureModifier$TEMPERATURE_RANGE
public static "fromEnumWithMapping"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(string), (string)>): $StringRepresentable$EnumCodec<(E)>
public static "keys"(arg0: ($StringRepresentable$Type)[]): $Keyable
public static "fromEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>): $StringRepresentable$EnumCodec<(E)>
get "serializedName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdditionsTemperatureModifier$TEMPERATURE_RANGE$Type = (("warm") | ("neutral") | ("frozen") | ("cold") | ("lukewarm")) | ($AdditionsTemperatureModifier$TEMPERATURE_RANGE);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdditionsTemperatureModifier$TEMPERATURE_RANGE_ = $AdditionsTemperatureModifier$TEMPERATURE_RANGE$Type;
}}
declare module "packages/com/craisinlord/integrated_villages/config/$IntegratedVillagesConfigForge" {
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"
import {$ConfigGeneralForge, $ConfigGeneralForge$Type} from "packages/com/craisinlord/integrated_villages/config/$ConfigGeneralForge"

export class $IntegratedVillagesConfigForge {
static readonly "BUILDER": $ForgeConfigSpec$Builder
static readonly "SPEC": $ForgeConfigSpec
static readonly "general": $ConfigGeneralForge

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntegratedVillagesConfigForge$Type = ($IntegratedVillagesConfigForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntegratedVillagesConfigForge_ = $IntegratedVillagesConfigForge$Type;
}}
declare module "packages/com/craisinlord/idas/state/$IStateCacheProvider" {
import {$stateCache, $stateCache$Type} from "packages/com/craisinlord/idas/state/$stateCache"

export interface $IStateCacheProvider {

 "getStateCache"(): $stateCache

(): $stateCache
}

export namespace $IStateCacheProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IStateCacheProvider$Type = ($IStateCacheProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IStateCacheProvider_ = $IStateCacheProvider$Type;
}}
declare module "packages/com/craisinlord/idas/$IDAS" {
import {$ConfigModule, $ConfigModule$Type} from "packages/com/craisinlord/idas/config/$ConfigModule"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"

export class $IDAS {
static readonly "MODID": string
static readonly "LOGGER": $Logger
static readonly "CONFIG": $ConfigModule

constructor()

public "setup"(arg0: $FMLCommonSetupEvent$Type): void
set "up"(value: $FMLCommonSetupEvent$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IDAS$Type = ($IDAS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IDAS_ = $IDAS$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/placements/$SnapToLowerNonAirPlacement" {
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$PlacementContext, $PlacementContext$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementContext"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$PlacementModifier, $PlacementModifier$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementModifier"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$PlacementModifierType, $PlacementModifierType$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementModifierType"

export class $SnapToLowerNonAirPlacement extends $PlacementModifier {
static readonly "CODEC": $Codec<($SnapToLowerNonAirPlacement)>

constructor()

public "getPositions"(placementContext: $PlacementContext$Type, random: $RandomSource$Type, blockPos: $BlockPos$Type): $Stream<($BlockPos)>
public "type"(): $PlacementModifierType<(any)>
public static "snapToLowerNonAir"(): $SnapToLowerNonAirPlacement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SnapToLowerNonAirPlacement$Type = ($SnapToLowerNonAirPlacement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SnapToLowerNonAirPlacement_ = $SnapToLowerNonAirPlacement$Type;
}}
declare module "packages/com/craisinlord/integrated_api/events/$RegisterWanderingTradesEvent" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$VillagerTrades$ItemListing, $VillagerTrades$ItemListing$Type} from "packages/net/minecraft/world/entity/npc/$VillagerTrades$ItemListing"
import {$EventHandler, $EventHandler$Type} from "packages/com/craisinlord/integrated_api/events/base/$EventHandler"

export class $RegisterWanderingTradesEvent extends $Record {
static readonly "EVENT": $EventHandler<($RegisterWanderingTradesEvent)>

constructor(basic: $Consumer$Type<($VillagerTrades$ItemListing$Type)>, rare: $Consumer$Type<($VillagerTrades$ItemListing$Type)>)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "basic"(): $Consumer<($VillagerTrades$ItemListing)>
public "addBasicTrade"(trade: $VillagerTrades$ItemListing$Type): void
public "rare"(): $Consumer<($VillagerTrades$ItemListing)>
public "addRareTrade"(trade: $VillagerTrades$ItemListing$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegisterWanderingTradesEvent$Type = ($RegisterWanderingTradesEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegisterWanderingTradesEvent_ = $RegisterWanderingTradesEvent$Type;
}}
declare module "packages/com/craisinlord/idas/config/$IDASConfigForge" {
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$ConfigGeneralForge, $ConfigGeneralForge$Type} from "packages/com/craisinlord/idas/config/$ConfigGeneralForge"
import {$ForgeConfigSpec$Builder, $ForgeConfigSpec$Builder$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$Builder"

export class $IDASConfigForge {
static readonly "BUILDER": $ForgeConfigSpec$Builder
static readonly "SPEC": $ForgeConfigSpec
static readonly "general": $ConfigGeneralForge

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IDASConfigForge$Type = ($IDASConfigForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IDASConfigForge_ = $IDASConfigForge$Type;
}}
declare module "packages/com/craisinlord/integrated_api/mixins/resources/$LootContextAccessor" {
import {$LootParams, $LootParams$Type} from "packages/net/minecraft/world/level/storage/loot/$LootParams"

export interface $LootContextAccessor {

 "getParams"(): $LootParams

(): $LootParams
}

export namespace $LootContextAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LootContextAccessor$Type = ($LootContextAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LootContextAccessor_ = $LootContextAccessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/processors/$RemoveFloatingBlocksProcessor" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $RemoveFloatingBlocksProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($RemoveFloatingBlocksProcessor)>


public "processBlock"(levelReader: $LevelReader$Type, pos: $BlockPos$Type, blockPos: $BlockPos$Type, structureBlockInfoLocal: $StructureTemplate$StructureBlockInfo$Type, structureBlockInfoWorld: $StructureTemplate$StructureBlockInfo$Type, structurePlacementData: $StructurePlaceSettings$Type): $StructureTemplate$StructureBlockInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RemoveFloatingBlocksProcessor$Type = ($RemoveFloatingBlocksProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RemoveFloatingBlocksProcessor_ = $RemoveFloatingBlocksProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/structures/targetselector/$StructureTargetSelector" {
import {$StructureTargetSelectorType, $StructureTargetSelectorType$Type} from "packages/com/craisinlord/integrated_api/world/structures/targetselector/$StructureTargetSelectorType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructureContext, $StructureContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext"
import {$PieceEntry, $PieceEntry$Type} from "packages/com/craisinlord/integrated_api/world/structures/pieces/assembler/$PieceEntry"

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
declare module "packages/com/craisinlord/integrated_api/world/processors/$TickBlocksProcessor" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $TickBlocksProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($TickBlocksProcessor)>


public "processBlock"(levelReader: $LevelReader$Type, pos: $BlockPos$Type, blockPos: $BlockPos$Type, structureBlockInfoLocal: $StructureTemplate$StructureBlockInfo$Type, structureBlockInfoWorld: $StructureTemplate$StructureBlockInfo$Type, structurePlacementData: $StructurePlaceSettings$Type): $StructureTemplate$StructureBlockInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickBlocksProcessor$Type = ($TickBlocksProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickBlocksProcessor_ = $TickBlocksProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/forge/$AdditionsTemperatureModifier" {
import {$HolderSet, $HolderSet$Type} from "packages/net/minecraft/core/$HolderSet"
import {$ModifiableBiomeInfo$BiomeInfo$Builder, $ModifiableBiomeInfo$BiomeInfo$Builder$Type} from "packages/net/minecraftforge/common/world/$ModifiableBiomeInfo$BiomeInfo$Builder"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$BiomeModifier, $BiomeModifier$Type} from "packages/net/minecraftforge/common/world/$BiomeModifier"
import {$BiomeModifier$Phase, $BiomeModifier$Phase$Type} from "packages/net/minecraftforge/common/world/$BiomeModifier$Phase"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$PlacedFeature, $PlacedFeature$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacedFeature"
import {$GenerationStep$Decoration, $GenerationStep$Decoration$Type} from "packages/net/minecraft/world/level/levelgen/$GenerationStep$Decoration"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$AdditionsTemperatureModifier$TEMPERATURE_RANGE, $AdditionsTemperatureModifier$TEMPERATURE_RANGE$Type} from "packages/com/craisinlord/integrated_api/world/forge/$AdditionsTemperatureModifier$TEMPERATURE_RANGE"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $AdditionsTemperatureModifier extends $Record implements $BiomeModifier {
static "CODEC": $Codec<($AdditionsTemperatureModifier)>

constructor(biomes: $HolderSet$Type<($Biome$Type)>, feature: $Holder$Type<($PlacedFeature$Type)>, step: $GenerationStep$Decoration$Type, temperatureRange: $AdditionsTemperatureModifier$TEMPERATURE_RANGE$Type)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "step"(): $GenerationStep$Decoration
public "feature"(): $Holder<($PlacedFeature)>
public "biomes"(): $HolderSet<($Biome)>
public "temperatureRange"(): $AdditionsTemperatureModifier$TEMPERATURE_RANGE
public "modify"(biome: $Holder$Type<($Biome$Type)>, phase: $BiomeModifier$Phase$Type, builder: $ModifiableBiomeInfo$BiomeInfo$Builder$Type): void
public "codec"(): $Codec<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdditionsTemperatureModifier$Type = ($AdditionsTemperatureModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdditionsTemperatureModifier_ = $AdditionsTemperatureModifier$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/condition/$AllOfCondition" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructureContext, $StructureContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext"
import {$StructureCondition, $StructureCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureCondition"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureConditionType"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $AllOfCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($AllOfCondition)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(conditions: $List$Type<($StructureCondition$Type)>)

public "type"(): $StructureConditionType<(any)>
public "passes"(ctx: $StructureContext$Type): boolean
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
declare module "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntries" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntry"

export class $RegistryEntries<T> {

constructor()

public "add"<I extends T>(entry: $RegistryEntry$Type<(I)>): $RegistryEntry<(I)>
public "getEntries"(): $List<($RegistryEntry<(T)>)>
get "entries"(): $List<($RegistryEntry<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryEntries$Type<T> = ($RegistryEntries<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryEntries_<T> = $RegistryEntries$Type<(T)>;
}}
declare module "packages/com/craisinlord/idas/sound/$IDASSounds" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $IDASSounds {
static readonly "SOUND_EVENTS": $DeferredRegister<($SoundEvent)>
static readonly "CALIDUM": $RegistryObject<($SoundEvent)>
static readonly "SLITHER": $RegistryObject<($SoundEvent)>

constructor()

public static "register"(arg0: $IEventBus$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IDASSounds$Type = ($IDASSounds);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IDASSounds_ = $IDASSounds$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/structures/$OverLavaNetherStructure" {
import {$StructureTemplatePool, $StructureTemplatePool$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool"
import {$StructureType, $StructureType$Type} from "packages/net/minecraft/world/level/levelgen/structure/$StructureType"
import {$Heightmap$Types, $Heightmap$Types$Type} from "packages/net/minecraft/world/level/levelgen/$Heightmap$Types"
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Structure, $Structure$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$HeightProvider, $HeightProvider$Type} from "packages/net/minecraft/world/level/levelgen/heightproviders/$HeightProvider"
import {$JigsawStructure$BURYING_TYPE, $JigsawStructure$BURYING_TYPE$Type} from "packages/com/craisinlord/integrated_api/world/structures/$JigsawStructure$BURYING_TYPE"
import {$JigsawStructure, $JigsawStructure$Type} from "packages/com/craisinlord/integrated_api/world/structures/$JigsawStructure"
import {$Structure$StructureSettings, $Structure$StructureSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$StructureSettings"

export class $OverLavaNetherStructure extends $JigsawStructure {
static readonly "CODEC": $Codec<($OverLavaNetherStructure)>
readonly "startPool": $Holder<($StructureTemplatePool)>
readonly "size": integer
readonly "minYAllowed": $Optional<(integer)>
readonly "maxYAllowed": $Optional<(integer)>
readonly "allowedYRangeFromStart": $Optional<(integer)>
readonly "startHeight": $HeightProvider
readonly "projectStartToHeightmap": $Optional<($Heightmap$Types)>
readonly "cannotSpawnInLiquid": boolean
readonly "terrainHeightCheckRadius": $Optional<(integer)>
readonly "allowedTerrainHeightRange": $Optional<(integer)>
readonly "biomeRadius": $Optional<(integer)>
readonly "maxDistanceFromCenter": $Optional<(integer)>
readonly "buryingType": $Optional<($JigsawStructure$BURYING_TYPE)>
readonly "rotationFixed": boolean
readonly "enhancedTerrainAdaptation": $EnhancedTerrainAdaptation
static readonly "DIRECT_CODEC": $Codec<($Structure)>

constructor(config: $Structure$StructureSettings$Type, startPool: $Holder$Type<($StructureTemplatePool$Type)>, size: integer, minYAllowed: $Optional$Type<(integer)>, maxYAllowed: $Optional$Type<(integer)>, allowedYRangeFromStart: $Optional$Type<(integer)>, startHeight: $HeightProvider$Type, projectStartToHeightmap: $Optional$Type<($Heightmap$Types$Type)>, cannotSpawnInLiquid: boolean, terrainHeightCheckRadius: $Optional$Type<(integer)>, allowedTerrainHeightRange: $Optional$Type<(integer)>, biomeRadius: $Optional$Type<(integer)>, maxDistanceFromCenter: $Optional$Type<(integer)>, buryingType: $Optional$Type<($JigsawStructure$BURYING_TYPE$Type)>, rotationFixed: boolean, enhancedTerrainAdaptation: $EnhancedTerrainAdaptation$Type)

public "type"(): $StructureType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OverLavaNetherStructure$Type = ($OverLavaNetherStructure);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OverLavaNetherStructure_ = $OverLavaNetherStructure$Type;
}}
declare module "packages/com/craisinlord/integrated_api/mixins/entities/$ShulkerEntityInvoker" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"

export interface $ShulkerEntityInvoker {

 "integratedapi_callSetAttachFace"(arg0: $Direction$Type): void

(arg0: $Direction$Type): void
}

export namespace $ShulkerEntityInvoker {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShulkerEntityInvoker$Type = ($ShulkerEntityInvoker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShulkerEntityInvoker_ = $ShulkerEntityInvoker$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext" {
import {$StructureTemplateManager, $StructureTemplateManager$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplateManager"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$PieceEntry, $PieceEntry$Type} from "packages/com/craisinlord/integrated_api/world/structures/pieces/assembler/$PieceEntry"

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
declare module "packages/com/craisinlord/integrated_api/misc/maptrades/$StructureMapTradesEvents" {
import {$RegisterVillagerTradesEvent, $RegisterVillagerTradesEvent$Type} from "packages/com/craisinlord/integrated_api/events/$RegisterVillagerTradesEvent"
import {$RegisterWanderingTradesEvent, $RegisterWanderingTradesEvent$Type} from "packages/com/craisinlord/integrated_api/events/$RegisterWanderingTradesEvent"

export class $StructureMapTradesEvents {


public static "addVillagerTrades"(event: $RegisterVillagerTradesEvent$Type): void
public static "addWanderingTrades"(event: $RegisterWanderingTradesEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureMapTradesEvents$Type = ($StructureMapTradesEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureMapTradesEvents_ = $StructureMapTradesEvents$Type;
}}
declare module "packages/com/craisinlord/integrated_stronghold/forge/$IntegratedStrongholdForge" {
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"

export class $IntegratedStrongholdForge {

constructor()

public "setup"(event: $FMLCommonSetupEvent$Type): void
set "up"(value: $FMLCommonSetupEvent$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntegratedStrongholdForge$Type = ($IntegratedStrongholdForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntegratedStrongholdForge_ = $IntegratedStrongholdForge$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/condition/$RandomChanceCondition" {
import {$StructureContext, $StructureContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext"
import {$StructureCondition, $StructureCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureCondition"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureConditionType"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $RandomChanceCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($RandomChanceCondition)>
readonly "chance": float
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(chance: float)

public "type"(): $StructureConditionType<(any)>
public "passes"(ctx: $StructureContext$Type): boolean
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
declare module "packages/com/craisinlord/integrated_api/forge/$IntegratedAPIForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $IntegratedAPIForge {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntegratedAPIForge$Type = ($IntegratedAPIForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntegratedAPIForge_ = $IntegratedAPIForge$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/structures/placements/$StrongholdPlacement" {
import {$StructurePlacement$ExclusionZone, $StructurePlacement$ExclusionZone$Type} from "packages/net/minecraft/world/level/levelgen/structure/placement/$StructurePlacement$ExclusionZone"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$StructurePlacementType, $StructurePlacementType$Type} from "packages/net/minecraft/world/level/levelgen/structure/placement/$StructurePlacementType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$RandomSpreadType, $RandomSpreadType$Type} from "packages/net/minecraft/world/level/levelgen/structure/placement/$RandomSpreadType"
import {$StructurePlacement$FrequencyReductionMethod, $StructurePlacement$FrequencyReductionMethod$Type} from "packages/net/minecraft/world/level/levelgen/structure/placement/$StructurePlacement$FrequencyReductionMethod"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$RandomSpreadStructurePlacement, $RandomSpreadStructurePlacement$Type} from "packages/net/minecraft/world/level/levelgen/structure/placement/$RandomSpreadStructurePlacement"

export class $StrongholdPlacement extends $RandomSpreadStructurePlacement {
static readonly "CODEC": $Codec<($StrongholdPlacement)>

constructor(locateOffset: $Vec3i$Type, frequencyReductionMethod: $StructurePlacement$FrequencyReductionMethod$Type, frequency: float, salt: integer, exclusionZone: $Optional$Type<($StructurePlacement$ExclusionZone$Type)>, spacing: integer, separation: integer, randomSpreadType: $RandomSpreadType$Type, chunkDistanceToFirstRing: integer, ringChunkThickness: integer, maxRingSection: $Optional$Type<(integer)>)

public "chunkDistanceToFirstRing"(): integer
public "type"(): $StructurePlacementType<(any)>
public "ringChunkThickness"(): integer
public "maxRingSection"(): $Optional<(integer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StrongholdPlacement$Type = ($StrongholdPlacement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StrongholdPlacement_ = $StrongholdPlacement$Type;
}}
declare module "packages/com/craisinlord/integrated_api/utils/$PlatformHooks" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PlatformHooks {

constructor()

public static "isDevEnvironment"(): boolean
public static "isModLoaded"(modid: string): boolean
get "devEnvironment"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformHooks$Type = ($PlatformHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformHooks_ = $PlatformHooks$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/processors/$BlockRemovalPostProcessor" {
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ServerLevelAccessor, $ServerLevelAccessor$Type} from "packages/net/minecraft/world/level/$ServerLevelAccessor"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $BlockRemovalPostProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($BlockRemovalPostProcessor)>


public "finalizeProcessing"(serverLevelAccessor: $ServerLevelAccessor$Type, blockPos: $BlockPos$Type, blockPos2: $BlockPos$Type, list: $List$Type<($StructureTemplate$StructureBlockInfo$Type)>, list2: $List$Type<($StructureTemplate$StructureBlockInfo$Type)>, structurePlaceSettings: $StructurePlaceSettings$Type): $List<($StructureTemplate$StructureBlockInfo)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockRemovalPostProcessor$Type = ($BlockRemovalPostProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockRemovalPostProcessor_ = $BlockRemovalPostProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/modinit/$IAStructurePlacementType" {
import {$StructurePlacementType, $StructurePlacementType$Type} from "packages/net/minecraft/world/level/levelgen/structure/placement/$StructurePlacementType"
import {$AdvancedRandomSpread, $AdvancedRandomSpread$Type} from "packages/com/craisinlord/integrated_api/world/structures/placements/$AdvancedRandomSpread"
import {$StrongholdPlacement, $StrongholdPlacement$Type} from "packages/com/craisinlord/integrated_api/world/structures/placements/$StrongholdPlacement"
import {$ResourcefulRegistry, $ResourcefulRegistry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$ResourcefulRegistry"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntry"

export class $IAStructurePlacementType {
static readonly "STRUCTURE_PLACEMENT_TYPE": $ResourcefulRegistry<($StructurePlacementType<(any)>)>
static readonly "ADVANCED_RANDOM_SPREAD": $RegistryEntry<($StructurePlacementType<($AdvancedRandomSpread)>)>
static readonly "STRONGHOLD_PLACEMENT": $RegistryEntry<($StructurePlacementType<($StrongholdPlacement)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IAStructurePlacementType$Type = ($IAStructurePlacementType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IAStructurePlacementType_ = $IAStructurePlacementType$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/processors/$ReplaceLiquidOnlyProcessor" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$HashSet, $HashSet$Type} from "packages/java/util/$HashSet"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $ReplaceLiquidOnlyProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($ReplaceLiquidOnlyProcessor)>
readonly "blocksToAlwaysPlace": $HashSet<($BlockState)>


public "processBlock"(worldView: $LevelReader$Type, pos: $BlockPos$Type, blockPos: $BlockPos$Type, structureBlockInfoLocal: $StructureTemplate$StructureBlockInfo$Type, structureBlockInfoWorld: $StructureTemplate$StructureBlockInfo$Type, structurePlacementData: $StructurePlaceSettings$Type): $StructureTemplate$StructureBlockInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReplaceLiquidOnlyProcessor$Type = ($ReplaceLiquidOnlyProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReplaceLiquidOnlyProcessor_ = $ReplaceLiquidOnlyProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/modinit/$IAPlacements" {
import {$UnlimitedCountPlacement, $UnlimitedCountPlacement$Type} from "packages/com/craisinlord/integrated_api/world/placements/$UnlimitedCountPlacement"
import {$SnapToLowerNonAirPlacement, $SnapToLowerNonAirPlacement$Type} from "packages/com/craisinlord/integrated_api/world/placements/$SnapToLowerNonAirPlacement"
import {$MinusEightPlacement, $MinusEightPlacement$Type} from "packages/com/craisinlord/integrated_api/world/placements/$MinusEightPlacement"
import {$ResourcefulRegistry, $ResourcefulRegistry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$ResourcefulRegistry"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntry"
import {$PlacementModifierType, $PlacementModifierType$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementModifierType"

export class $IAPlacements {
static readonly "PLACEMENT_MODIFIER": $ResourcefulRegistry<($PlacementModifierType<(any)>)>
static readonly "MINUS_EIGHT_PLACEMENT": $RegistryEntry<($PlacementModifierType<($MinusEightPlacement)>)>
static readonly "UNLIMITED_COUNT": $RegistryEntry<($PlacementModifierType<($UnlimitedCountPlacement)>)>
static readonly "SNAP_TO_LOWER_NON_AIR_PLACEMENT": $RegistryEntry<($PlacementModifierType<($SnapToLowerNonAirPlacement)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IAPlacements$Type = ($IAPlacements);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IAPlacements_ = $IAPlacements$Type;
}}
declare module "packages/com/craisinlord/integrated_api/datagen/$StructureNbtUpdater" {
import {$CachedOutput, $CachedOutput$Type} from "packages/net/minecraft/data/$CachedOutput"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$DataProvider, $DataProvider$Type} from "packages/net/minecraft/data/$DataProvider"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PackOutput, $PackOutput$Type} from "packages/net/minecraft/data/$PackOutput"
import {$ExistingFileHelper, $ExistingFileHelper$Type} from "packages/net/minecraftforge/common/data/$ExistingFileHelper"

export class $StructureNbtUpdater implements $DataProvider {

constructor(basePath: string, modid: string, helper: $ExistingFileHelper$Type, output: $PackOutput$Type)

public "run"(cache: $CachedOutput$Type): $CompletableFuture<(any)>
public "getName"(): string
public static "saveStable"(arg0: $CachedOutput$Type, arg1: $JsonElement$Type, arg2: $Path$Type): $CompletableFuture<(any)>
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureNbtUpdater$Type = ($StructureNbtUpdater);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureNbtUpdater_ = $StructureNbtUpdater$Type;
}}
declare module "packages/com/craisinlord/integrated_villages/lootmanager/$StructureModdedLootImporter" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $StructureModdedLootImporter {
static readonly "TABLE_IMPORTS": $Map<($ResourceLocation), ($ResourceLocation)>

constructor()

public static "createMap"(): $Map<($ResourceLocation), ($ResourceLocation)>
public static "isInvalidLootTableFound"(minecraftServer: $MinecraftServer$Type, entry: $Map$Entry$Type<($ResourceLocation$Type), ($ResourceLocation$Type)>): boolean
public static "checkLoottables"(minecraftServer: $MinecraftServer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureModdedLootImporter$Type = ($StructureModdedLootImporter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureModdedLootImporter_ = $StructureModdedLootImporter$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/forge/$RemovalsModifier" {
import {$HolderSet, $HolderSet$Type} from "packages/net/minecraft/core/$HolderSet"
import {$ModifiableBiomeInfo$BiomeInfo$Builder, $ModifiableBiomeInfo$BiomeInfo$Builder$Type} from "packages/net/minecraftforge/common/world/$ModifiableBiomeInfo$BiomeInfo$Builder"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$BiomeModifier, $BiomeModifier$Type} from "packages/net/minecraftforge/common/world/$BiomeModifier"
import {$BiomeModifier$Phase, $BiomeModifier$Phase$Type} from "packages/net/minecraftforge/common/world/$BiomeModifier$Phase"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$PlacedFeature, $PlacedFeature$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacedFeature"
import {$GenerationStep$Decoration, $GenerationStep$Decoration$Type} from "packages/net/minecraft/world/level/levelgen/$GenerationStep$Decoration"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $RemovalsModifier extends $Record implements $BiomeModifier {
static "CODEC": $Codec<($RemovalsModifier)>

constructor(biomes: $HolderSet$Type<($Biome$Type)>, feature: $Holder$Type<($PlacedFeature$Type)>, step: $GenerationStep$Decoration$Type)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "step"(): $GenerationStep$Decoration
public "feature"(): $Holder<($PlacedFeature)>
public "biomes"(): $HolderSet<($Biome)>
public "modify"(biome: $Holder$Type<($Biome$Type)>, phase: $BiomeModifier$Phase$Type, builder: $ModifiableBiomeInfo$BiomeInfo$Builder$Type): void
public "codec"(): $Codec<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RemovalsModifier$Type = ($RemovalsModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RemovalsModifier_ = $RemovalsModifier$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/condition/$StructureConditionType" {
import {$AltitudeCondition, $AltitudeCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$AltitudeCondition"
import {$RotationCondition, $RotationCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$RotationCondition"
import {$AlwaysTrueCondition, $AlwaysTrueCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$AlwaysTrueCondition"
import {$PieceInRangeCondition, $PieceInRangeCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$PieceInRangeCondition"
import {$ModLoadedCondition, $ModLoadedCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$ModLoadedCondition"
import {$NotCondition, $NotCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$NotCondition"
import {$StructureCondition, $StructureCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureCondition"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$PieceInHorizontalDirectionCondition, $PieceInHorizontalDirectionCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$PieceInHorizontalDirectionCondition"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$AnyOfCondition, $AnyOfCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$AnyOfCondition"
import {$DepthCondition, $DepthCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$DepthCondition"
import {$AllOfCondition, $AllOfCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$AllOfCondition"
import {$RandomChanceCondition, $RandomChanceCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$RandomChanceCondition"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $StructureConditionType<C extends $StructureCondition> {

 "codec"(): $Codec<(C)>

(resourceLocation: $ResourceLocation$Type, codec: $Codec$Type<(C)>): $StructureConditionType<(C)>
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
const MOD_LOADED: $StructureConditionType<($ModLoadedCondition)>
const PIECE_IN_HORIZONTAL_DIRECTION: $StructureConditionType<($PieceInHorizontalDirectionCondition)>
const ROTATION: $StructureConditionType<($RotationCondition)>
function register<C>(resourceLocation: $ResourceLocation$Type, codec: $Codec$Type<(C)>): $StructureConditionType<(C)>
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
declare module "packages/com/craisinlord/integrated_api/world/processors/$ReplaceAirOnlyProcessor" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$HashSet, $HashSet$Type} from "packages/java/util/$HashSet"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $ReplaceAirOnlyProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($ReplaceAirOnlyProcessor)>
readonly "blocksToAlwaysPlace": $HashSet<($BlockState)>


public "processBlock"(worldView: $LevelReader$Type, pos: $BlockPos$Type, blockPos: $BlockPos$Type, structureBlockInfoLocal: $StructureTemplate$StructureBlockInfo$Type, structureBlockInfoWorld: $StructureTemplate$StructureBlockInfo$Type, structurePlacementData: $StructurePlaceSettings$Type): $StructureTemplate$StructureBlockInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReplaceAirOnlyProcessor$Type = ($ReplaceAirOnlyProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReplaceAirOnlyProcessor_ = $ReplaceAirOnlyProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/condition/$ModLoadedCondition" {
import {$StructureContext, $StructureContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext"
import {$StructureCondition, $StructureCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureCondition"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureConditionType"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $ModLoadedCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($ModLoadedCondition)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(modId: string)

public "type"(): $StructureConditionType<(any)>
public "passes"(ctx: $StructureContext$Type): boolean
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
declare module "packages/com/craisinlord/integrated_api/world/terrainadaptation/beardifier/$EnhancedBeardifierHelper" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$EnhancedBeardifierData, $EnhancedBeardifierData$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/beardifier/$EnhancedBeardifierData"
import {$StructureManager, $StructureManager$Type} from "packages/net/minecraft/world/level/$StructureManager"
import {$DensityFunction$FunctionContext, $DensityFunction$FunctionContext$Type} from "packages/net/minecraft/world/level/levelgen/$DensityFunction$FunctionContext"
import {$Beardifier, $Beardifier$Type} from "packages/net/minecraft/world/level/levelgen/$Beardifier"

export class $EnhancedBeardifierHelper {

constructor()

public static "computeDensity"(ctx: $DensityFunction$FunctionContext$Type, density: double, data: $EnhancedBeardifierData$Type): double
public static "forStructuresInChunk"(structureManager: $StructureManager$Type, chunkPos: $ChunkPos$Type, original: $Beardifier$Type): $Beardifier
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
declare module "packages/com/craisinlord/integrated_api/mixins/structures/$StructureProcessorAccessor" {
import {$StructureProcessorType, $StructureProcessorType$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessorType"

export interface $StructureProcessorAccessor {

 "callGetType"(): $StructureProcessorType<(any)>

(): $StructureProcessorType<(any)>
}

export namespace $StructureProcessorAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureProcessorAccessor$Type = ($StructureProcessorAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureProcessorAccessor_ = $StructureProcessorAccessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/events/base/$CancellableEventHandler$CancellableFunction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $CancellableEventHandler$CancellableFunction<T> {

 "apply"(arg0: boolean, arg1: T): boolean

(arg0: boolean, arg1: T): boolean
}

export namespace $CancellableEventHandler$CancellableFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CancellableEventHandler$CancellableFunction$Type<T> = ($CancellableEventHandler$CancellableFunction<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CancellableEventHandler$CancellableFunction_<T> = $CancellableEventHandler$CancellableFunction$Type<(T)>;
}}
declare module "packages/com/craisinlord/integrated_api/world/condition/$RotationCondition" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructureContext, $StructureContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext"
import {$StructureCondition, $StructureCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureCondition"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureConditionType"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $RotationCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($RotationCondition)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(validRotations: $List$Type<($Rotation$Type)>)

public "type"(): $StructureConditionType<(any)>
public "passes"(ctx: $StructureContext$Type): boolean
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
declare module "packages/com/craisinlord/integrated_api/mixins/structures/$SinglePoolElementAccessor" {
import {$StructureTemplateManager, $StructureTemplateManager$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplateManager"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$StructureProcessorList, $StructureProcessorList$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessorList"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$StructureTemplate, $StructureTemplate$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate"

export interface $SinglePoolElementAccessor {

 "callGetTemplate"(arg0: $StructureTemplateManager$Type): $StructureTemplate
 "integratedapi_getTemplate"(): $Either<($ResourceLocation), ($StructureTemplate)>
 "integratedapi_getProcessors"(): $Holder<($StructureProcessorList)>
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
declare module "packages/com/craisinlord/integrated_api/mixins/features/$DungeonFeatureAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $DungeonFeatureAccessor {

}

export namespace $DungeonFeatureAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DungeonFeatureAccessor$Type = ($DungeonFeatureAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DungeonFeatureAccessor_ = $DungeonFeatureAccessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/misc/mobspawners/$MobSpawnerObj" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MobSpawnerObj {
 "name": string
 "weight": float
 "optional": boolean

constructor(name: string, weight: float)
constructor(name: string, weight: float, optional: boolean)

public "setEntityType"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobSpawnerObj$Type = ($MobSpawnerObj);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobSpawnerObj_ = $MobSpawnerObj$Type;
}}
declare module "packages/com/craisinlord/idas/item/$IDASItems" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $IDASItems {
static readonly "ITEMS": $DeferredRegister<($Item)>
static readonly "MUSIC_DISC_SLITHER": $RegistryObject<($Item)>
static readonly "MUSIC_DISC_CALIDUM": $RegistryObject<($Item)>
static readonly "DISC_FRAGMENT_SLITHER": $RegistryObject<($Item)>

constructor()

public static "register"(arg0: $IEventBus$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IDASItems$Type = ($IDASItems);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IDASItems_ = $IDASItems$Type;
}}
declare module "packages/com/craisinlord/integrated_api/utils/$BoxOctree" {
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export class $BoxOctree {

constructor(axisAlignedBB: $AABB$Type)

public "addBox"(axisAlignedBB: $AABB$Type): void
public "removeBox"(axisAlignedBB: $AABB$Type): void
public "intersectsAnyBox"(axisAlignedBB: $AABB$Type): boolean
public "boundaryContains"(axisAlignedBB: $AABB$Type): boolean
public "boundaryContainsFuzzy"(axisAlignedBB: $AABB$Type): boolean
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
declare module "packages/com/craisinlord/integrated_api/events/$RegisterVillagerTradesEvent" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$VillagerTrades$ItemListing, $VillagerTrades$ItemListing$Type} from "packages/net/minecraft/world/entity/npc/$VillagerTrades$ItemListing"
import {$EventHandler, $EventHandler$Type} from "packages/com/craisinlord/integrated_api/events/base/$EventHandler"
import {$VillagerProfession, $VillagerProfession$Type} from "packages/net/minecraft/world/entity/npc/$VillagerProfession"

export class $RegisterVillagerTradesEvent extends $Record {
static readonly "EVENT": $EventHandler<($RegisterVillagerTradesEvent)>

constructor(type: $VillagerProfession$Type, trade: $BiConsumer$Type<(integer), ($VillagerTrades$ItemListing$Type)>)

public "type"(): $VillagerProfession
public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "trade"(): $BiConsumer<(integer), ($VillagerTrades$ItemListing)>
public "addTrade"(level: integer, trade: $VillagerTrades$ItemListing$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegisterVillagerTradesEvent$Type = ($RegisterVillagerTradesEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegisterVillagerTradesEvent_ = $RegisterVillagerTradesEvent$Type;
}}
declare module "packages/com/craisinlord/integrated_api/utils/$AsyncLocator$LocateTask" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$Future, $Future$Type} from "packages/java/util/concurrent/$Future"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export class $AsyncLocator$LocateTask<T> extends $Record {

constructor(server: $MinecraftServer$Type, completableFuture: $CompletableFuture$Type<(T)>, taskFuture: $Future$Type<(any)>)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "cancel"(): void
public "server"(): $MinecraftServer
public "completableFuture"(): $CompletableFuture<(T)>
public "then"(action: $Consumer$Type<(T)>): $AsyncLocator$LocateTask<(T)>
public "thenOnServerThread"(action: $Consumer$Type<(T)>): $AsyncLocator$LocateTask<(T)>
public "taskFuture"(): $Future<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AsyncLocator$LocateTask$Type<T> = ($AsyncLocator$LocateTask<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AsyncLocator$LocateTask_<T> = $AsyncLocator$LocateTask$Type<(T)>;
}}
declare module "packages/com/craisinlord/integrated_api/world/processors/$WindmillBearingProcessor" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $WindmillBearingProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($WindmillBearingProcessor)>


public "processBlock"(worldView: $LevelReader$Type, pos: $BlockPos$Type, blockPos: $BlockPos$Type, structureBlockInfoLocal: $StructureTemplate$StructureBlockInfo$Type, structureBlockInfoWorld: $StructureTemplate$StructureBlockInfo$Type, structurePlacementData: $StructurePlaceSettings$Type): $StructureTemplate$StructureBlockInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WindmillBearingProcessor$Type = ($WindmillBearingProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WindmillBearingProcessor_ = $WindmillBearingProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/terrainadaptation/$CustomAdaptation" {
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$EnhancedTerrainAdaptationType, $EnhancedTerrainAdaptationType$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptationType"

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
declare module "packages/com/craisinlord/integrated_api/events/base/$CancellableEventHandler$CancellableFunctionNoReturn" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $CancellableEventHandler$CancellableFunctionNoReturn<T> {

 "apply"(arg0: boolean, arg1: T): void

(arg0: boolean, arg1: T): void
}

export namespace $CancellableEventHandler$CancellableFunctionNoReturn {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CancellableEventHandler$CancellableFunctionNoReturn$Type<T> = ($CancellableEventHandler$CancellableFunctionNoReturn<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CancellableEventHandler$CancellableFunctionNoReturn_<T> = $CancellableEventHandler$CancellableFunctionNoReturn$Type<(T)>;
}}
declare module "packages/com/craisinlord/integrated_api/world/placements/$MinusEightPlacement" {
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$PlacementContext, $PlacementContext$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementContext"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$PlacementModifier, $PlacementModifier$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementModifier"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$PlacementModifierType, $PlacementModifierType$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementModifierType"

export class $MinusEightPlacement extends $PlacementModifier {
static readonly "CODEC": $Codec<($MinusEightPlacement)>

constructor()

public static "subtractedEight"(): $MinusEightPlacement
public "getPositions"(placementContext: $PlacementContext$Type, random: $RandomSource$Type, blockPos: $BlockPos$Type): $Stream<($BlockPos)>
public "type"(): $PlacementModifierType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MinusEightPlacement$Type = ($MinusEightPlacement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MinusEightPlacement_ = $MinusEightPlacement$Type;
}}
declare module "packages/com/craisinlord/integrated_api/misc/maptrades/$StructureMapCollectionObj" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$WanderingTraderMapObj, $WanderingTraderMapObj$Type} from "packages/com/craisinlord/integrated_api/misc/maptrades/$WanderingTraderMapObj"
import {$WanderingTraderMapObj$TRADE_TYPE, $WanderingTraderMapObj$TRADE_TYPE$Type} from "packages/com/craisinlord/integrated_api/misc/maptrades/$WanderingTraderMapObj$TRADE_TYPE"
import {$VillagerMapObj, $VillagerMapObj$Type} from "packages/com/craisinlord/integrated_api/misc/maptrades/$VillagerMapObj"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $StructureMapCollectionObj {
 "villagerMaps": $Map<(string), ($List<($VillagerMapObj)>)>
 "wanderingTraderMap": $Map<($WanderingTraderMapObj$TRADE_TYPE), ($List<($WanderingTraderMapObj)>)>

constructor(villagerMapObjs: $Map$Type<(string), ($List$Type<($VillagerMapObj$Type)>)>, wanderingTraderMapObjs: $Map$Type<($WanderingTraderMapObj$TRADE_TYPE$Type), ($List$Type<($WanderingTraderMapObj$Type)>)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureMapCollectionObj$Type = ($StructureMapCollectionObj);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureMapCollectionObj_ = $StructureMapCollectionObj$Type;
}}
declare module "packages/com/craisinlord/integrated_api/utils/forge/$PlatformHooksImpl" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PlatformHooksImpl {

constructor()

public static "isDevEnvironment"(): boolean
public static "isModLoaded"(modid: string): boolean
get "devEnvironment"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformHooksImpl$Type = ($PlatformHooksImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformHooksImpl_ = $PlatformHooksImpl$Type;
}}
declare module "packages/com/craisinlord/idas/config/$ConfigModule" {
import {$ConfigModule$General, $ConfigModule$General$Type} from "packages/com/craisinlord/idas/config/$ConfigModule$General"

export class $ConfigModule {
 "general": $ConfigModule$General

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigModule$Type = ($ConfigModule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigModule_ = $ConfigModule$Type;
}}
declare module "packages/com/craisinlord/integrated_api/misc/maptrades/$MerchantMapUpdating" {
import {$MerchantOffer, $MerchantOffer$Type} from "packages/net/minecraft/world/item/trading/$MerchantOffer"
import {$HolderSet, $HolderSet$Type} from "packages/net/minecraft/core/$HolderSet"
import {$MapDecoration$Type, $MapDecoration$Type$Type} from "packages/net/minecraft/world/level/saveddata/maps/$MapDecoration$Type"
import {$Structure, $Structure$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure"
import {$AbstractVillager, $AbstractVillager$Type} from "packages/net/minecraft/world/entity/npc/$AbstractVillager"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $MerchantMapUpdating {


public static "updateMapAsync"(pTrader: $Entity$Type, emeraldCost: integer, displayName: string, destinationType: $MapDecoration$Type$Type, maxUses: integer, villagerXp: integer, destination: $TagKey$Type<($Structure$Type)>, searchRadius: integer): $MerchantOffer
public static "updateMapAsync"(pTrader: $Entity$Type, emeraldCost: integer, displayName: string, destinationType: $MapDecoration$Type$Type, maxUses: integer, villagerXp: integer, structureSet: $HolderSet$Type<($Structure$Type)>, searchRadius: integer): $MerchantOffer
public static "handleLocationFound"(level: $ServerLevel$Type, merchant: $AbstractVillager$Type, mapStack: $ItemStack$Type, displayName: string, destinationType: $MapDecoration$Type$Type, pos: $BlockPos$Type): void
public static "invalidateMap"(merchant: $AbstractVillager$Type, mapStack: $ItemStack$Type): void
public static "updateMap"(mapStack: $ItemStack$Type, level: $ServerLevel$Type, pos: $BlockPos$Type, scale: integer, destinationType: $MapDecoration$Type$Type, displayName: string): void
public static "removeOffer"(merchant: $AbstractVillager$Type, offer: $MerchantOffer$Type): void
public static "createEmptyMap"(): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MerchantMapUpdating$Type = ($MerchantMapUpdating);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MerchantMapUpdating_ = $MerchantMapUpdating$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/structures/$JigsawStructure" {
import {$BoundingBox, $BoundingBox$Type} from "packages/net/minecraft/world/level/levelgen/structure/$BoundingBox"
import {$StructureTemplatePool, $StructureTemplatePool$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool"
import {$StructureType, $StructureType$Type} from "packages/net/minecraft/world/level/levelgen/structure/$StructureType"
import {$Heightmap$Types, $Heightmap$Types$Type} from "packages/net/minecraft/world/level/levelgen/$Heightmap$Types"
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Structure, $Structure$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$HeightProvider, $HeightProvider$Type} from "packages/net/minecraft/world/level/levelgen/heightproviders/$HeightProvider"
import {$Structure$GenerationStub, $Structure$GenerationStub$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$GenerationStub"
import {$Structure$GenerationContext, $Structure$GenerationContext$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$GenerationContext"
import {$JigsawStructure$BURYING_TYPE, $JigsawStructure$BURYING_TYPE$Type} from "packages/com/craisinlord/integrated_api/world/structures/$JigsawStructure$BURYING_TYPE"
import {$Structure$StructureSettings, $Structure$StructureSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$StructureSettings"

export class $JigsawStructure extends $Structure {
static readonly "CODEC": $Codec<($JigsawStructure)>
readonly "startPool": $Holder<($StructureTemplatePool)>
readonly "size": integer
readonly "minYAllowed": $Optional<(integer)>
readonly "maxYAllowed": $Optional<(integer)>
readonly "allowedYRangeFromStart": $Optional<(integer)>
readonly "startHeight": $HeightProvider
readonly "projectStartToHeightmap": $Optional<($Heightmap$Types)>
readonly "cannotSpawnInLiquid": boolean
readonly "terrainHeightCheckRadius": $Optional<(integer)>
readonly "allowedTerrainHeightRange": $Optional<(integer)>
readonly "biomeRadius": $Optional<(integer)>
readonly "maxDistanceFromCenter": $Optional<(integer)>
readonly "buryingType": $Optional<($JigsawStructure$BURYING_TYPE)>
readonly "rotationFixed": boolean
readonly "enhancedTerrainAdaptation": $EnhancedTerrainAdaptation
static readonly "DIRECT_CODEC": $Codec<($Structure)>

constructor(config: $Structure$StructureSettings$Type, startPool: $Holder$Type<($StructureTemplatePool$Type)>, size: integer, minYAllowed: $Optional$Type<(integer)>, maxYAllowed: $Optional$Type<(integer)>, allowedYRangeFromStart: $Optional$Type<(integer)>, startHeight: $HeightProvider$Type, projectStartToHeightmap: $Optional$Type<($Heightmap$Types$Type)>, cannotSpawnInLiquid: boolean, terrainHeightCheckRadius: $Optional$Type<(integer)>, allowedTerrainHeightRange: $Optional$Type<(integer)>, biomeRadius: $Optional$Type<(integer)>, maxDistanceFromCenter: $Optional$Type<(integer)>, buryingType: $Optional$Type<($JigsawStructure$BURYING_TYPE$Type)>, rotationFixed: boolean, enhancedTerrainAdaptation: $EnhancedTerrainAdaptation$Type)

public "type"(): $StructureType<(any)>
public "adjustBoundingBox"(boundingBox: $BoundingBox$Type): $BoundingBox
public "m_214086_"(context: $Structure$GenerationContext$Type): $Optional<($Structure$GenerationStub)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JigsawStructure$Type = ($JigsawStructure);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JigsawStructure_ = $JigsawStructure$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/terrainadaptation/$SmallCarvedTopNoBeardAdaptation" {
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$EnhancedTerrainAdaptationType, $EnhancedTerrainAdaptationType$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptationType"

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
declare module "packages/com/craisinlord/integrated_api/compat/$AsyncLocatorCompat" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $AsyncLocatorCompat {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AsyncLocatorCompat$Type = ($AsyncLocatorCompat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AsyncLocatorCompat_ = $AsyncLocatorCompat$Type;
}}
declare module "packages/com/craisinlord/integrated_stronghold/$IntegratedStronghold" {
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $IntegratedStronghold {
static readonly "MODID": string
static readonly "LOGGER": $Logger

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntegratedStronghold$Type = ($IntegratedStronghold);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntegratedStronghold_ = $IntegratedStronghold$Type;
}}
declare module "packages/com/craisinlord/integrated_api/mixins/world/$WorldGenRegionAccessor" {
import {$StructureManager, $StructureManager$Type} from "packages/net/minecraft/world/level/$StructureManager"

export interface $WorldGenRegionAccessor {

 "getStructureManager"(): $StructureManager

(): $StructureManager
}

export namespace $WorldGenRegionAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldGenRegionAccessor$Type = ($WorldGenRegionAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldGenRegionAccessor_ = $WorldGenRegionAccessor$Type;
}}
declare module "packages/com/craisinlord/idas/config/$ConfigModule$General" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ConfigModule$General {
 "disableIaFStructures": boolean
 "applyMiningFatigue": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigModule$General$Type = ($ConfigModule$General);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigModule$General_ = $ConfigModule$General$Type;
}}
declare module "packages/com/craisinlord/integrated_api/modinit/registry/forge/$ForgeRegistryEntry" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntry"

export class $ForgeRegistryEntry<T> implements $RegistryEntry<(T)> {

constructor(object: $RegistryObject$Type<(T)>)

public "get"(): T
public "getId"(): $ResourceLocation
get "id"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeRegistryEntry$Type<T> = ($ForgeRegistryEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeRegistryEntry_<T> = $ForgeRegistryEntry$Type<(T)>;
}}
declare module "packages/com/craisinlord/integrated_api/modinit/registry/$BasicRegistryEntry" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntry"

export class $BasicRegistryEntry<T> implements $RegistryEntry<(T)> {

constructor(id: $ResourceLocation$Type, value: T)

public "get"(): T
public "getId"(): $ResourceLocation
get "id"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BasicRegistryEntry$Type<T> = ($BasicRegistryEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BasicRegistryEntry_<T> = $BasicRegistryEntry$Type<(T)>;
}}
declare module "packages/com/craisinlord/integrated_api/world/structures/pieces/manager/$PieceLimitedJigsawManager" {
import {$StructureTemplatePool, $StructureTemplatePool$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool"
import {$StructurePiecesBuilder, $StructurePiecesBuilder$Type} from "packages/net/minecraft/world/level/levelgen/structure/pieces/$StructurePiecesBuilder"
import {$Heightmap$Types, $Heightmap$Types$Type} from "packages/net/minecraft/world/level/levelgen/$Heightmap$Types"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Structure$GenerationStub, $Structure$GenerationStub$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$GenerationStub"
import {$Structure$GenerationContext, $Structure$GenerationContext$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$GenerationContext"
import {$PoolElementStructurePiece, $PoolElementStructurePiece$Type} from "packages/net/minecraft/world/level/levelgen/structure/$PoolElementStructurePiece"
import {$JigsawStructure$BURYING_TYPE, $JigsawStructure$BURYING_TYPE$Type} from "packages/com/craisinlord/integrated_api/world/structures/$JigsawStructure$BURYING_TYPE"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $PieceLimitedJigsawManager {

constructor()

public static "assembleJigsawStructure"(context: $Structure$GenerationContext$Type, startPoolHolder: $Holder$Type<($StructureTemplatePool$Type)>, size: integer, structureID: $ResourceLocation$Type, startPos: $BlockPos$Type, doBoundaryAdjustments: boolean, heightmapType: $Optional$Type<($Heightmap$Types$Type)>, maxY: integer, minY: integer, poolsThatIgnoreBounds: $Set$Type<($ResourceLocation$Type)>, maxDistanceFromCenter: $Optional$Type<(integer)>, rotationString: string, buryingType: $Optional$Type<($JigsawStructure$BURYING_TYPE$Type)>, structureBoundsAdjuster: $BiConsumer$Type<($StructurePiecesBuilder$Type), ($List$Type<($PoolElementStructurePiece$Type)>)>): $Optional<($Structure$GenerationStub)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PieceLimitedJigsawManager$Type = ($PieceLimitedJigsawManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PieceLimitedJigsawManager_ = $PieceLimitedJigsawManager$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/forge/$AdditionsModifier" {
import {$HolderSet, $HolderSet$Type} from "packages/net/minecraft/core/$HolderSet"
import {$ModifiableBiomeInfo$BiomeInfo$Builder, $ModifiableBiomeInfo$BiomeInfo$Builder$Type} from "packages/net/minecraftforge/common/world/$ModifiableBiomeInfo$BiomeInfo$Builder"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$BiomeModifier, $BiomeModifier$Type} from "packages/net/minecraftforge/common/world/$BiomeModifier"
import {$BiomeModifier$Phase, $BiomeModifier$Phase$Type} from "packages/net/minecraftforge/common/world/$BiomeModifier$Phase"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$PlacedFeature, $PlacedFeature$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacedFeature"
import {$GenerationStep$Decoration, $GenerationStep$Decoration$Type} from "packages/net/minecraft/world/level/levelgen/$GenerationStep$Decoration"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $AdditionsModifier extends $Record implements $BiomeModifier {
static "CODEC": $Codec<($AdditionsModifier)>

constructor(biomes: $HolderSet$Type<($Biome$Type)>, feature: $Holder$Type<($PlacedFeature$Type)>, step: $GenerationStep$Decoration$Type)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "step"(): $GenerationStep$Decoration
public "feature"(): $Holder<($PlacedFeature)>
public "biomes"(): $HolderSet<($Biome)>
public "modify"(biome: $Holder$Type<($Biome$Type)>, phase: $BiomeModifier$Phase$Type, builder: $ModifiableBiomeInfo$BiomeInfo$Builder$Type): void
public "codec"(): $Codec<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdditionsModifier$Type = ($AdditionsModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdditionsModifier_ = $AdditionsModifier$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/predicates/$PieceOriginAxisAlignedLinearPosRuleTest" {
import {$PosRuleTest, $PosRuleTest$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$PosRuleTest"
import {$Direction$Axis, $Direction$Axis$Type} from "packages/net/minecraft/core/$Direction$Axis"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $PieceOriginAxisAlignedLinearPosRuleTest extends $PosRuleTest {
static readonly "CODEC": $Codec<($PieceOriginAxisAlignedLinearPosRuleTest)>

constructor(minChance: float, maxChance: float, minDistance: integer, maxDistance: integer, axis: $Direction$Axis$Type)

public "test"(blockPos: $BlockPos$Type, blockPos2: $BlockPos$Type, blockPos3: $BlockPos$Type, random: $RandomSource$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PieceOriginAxisAlignedLinearPosRuleTest$Type = ($PieceOriginAxisAlignedLinearPosRuleTest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PieceOriginAxisAlignedLinearPosRuleTest_ = $PieceOriginAxisAlignedLinearPosRuleTest$Type;
}}
declare module "packages/com/craisinlord/idas/$IDASProcessors" {
import {$WaterloggingFixProcessor, $WaterloggingFixProcessor$Type} from "packages/com/craisinlord/integrated_api/world/processors/$WaterloggingFixProcessor"
import {$StructureProcessorType, $StructureProcessorType$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessorType"
import {$ResourcefulRegistry, $ResourcefulRegistry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$ResourcefulRegistry"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntry"

export class $IDASProcessors {
static readonly "IDAS_STRUCTURE_PROCESSOR": $ResourcefulRegistry<($StructureProcessorType<(any)>)>
static readonly "WATERLOGGING_FIX_PROCESSOR": $RegistryEntry<($StructureProcessorType<($WaterloggingFixProcessor)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IDASProcessors$Type = ($IDASProcessors);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IDASProcessors_ = $IDASProcessors$Type;
}}
declare module "packages/com/craisinlord/integrated_villages/config/$ConfigModule$General" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ConfigModule$General {
 "disableVanillaVillages": boolean
 "activateCreateContraptions": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigModule$General$Type = ($ConfigModule$General);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigModule$General_ = $ConfigModule$General$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/structures/placements/$AdvancedRandomSpread" {
import {$StructurePlacement$ExclusionZone, $StructurePlacement$ExclusionZone$Type} from "packages/net/minecraft/world/level/levelgen/structure/placement/$StructurePlacement$ExclusionZone"
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$AdvancedRandomSpread$SuperExclusionZone, $AdvancedRandomSpread$SuperExclusionZone$Type} from "packages/com/craisinlord/integrated_api/world/structures/placements/$AdvancedRandomSpread$SuperExclusionZone"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$StructurePlacementType, $StructurePlacementType$Type} from "packages/net/minecraft/world/level/levelgen/structure/placement/$StructurePlacementType"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ChunkGeneratorStructureState, $ChunkGeneratorStructureState$Type} from "packages/net/minecraft/world/level/chunk/$ChunkGeneratorStructureState"
import {$RandomSpreadType, $RandomSpreadType$Type} from "packages/net/minecraft/world/level/levelgen/structure/placement/$RandomSpreadType"
import {$StructurePlacement$FrequencyReductionMethod, $StructurePlacement$FrequencyReductionMethod$Type} from "packages/net/minecraft/world/level/levelgen/structure/placement/$StructurePlacement$FrequencyReductionMethod"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$RandomSpreadStructurePlacement, $RandomSpreadStructurePlacement$Type} from "packages/net/minecraft/world/level/levelgen/structure/placement/$RandomSpreadStructurePlacement"

export class $AdvancedRandomSpread extends $RandomSpreadStructurePlacement {
static readonly "CODEC": $Codec<($AdvancedRandomSpread)>

constructor(locationOffset: $Vec3i$Type, frequencyReductionMethod: $StructurePlacement$FrequencyReductionMethod$Type, frequency: float, salt: integer, exclusionZone: $Optional$Type<($StructurePlacement$ExclusionZone$Type)>, superExclusionZone: $Optional$Type<($AdvancedRandomSpread$SuperExclusionZone$Type)>, spacing: integer, separation: integer, spreadType: $RandomSpreadType$Type, minDistanceFromWorldOrigin: $Optional$Type<(integer)>)

public "minDistanceFromWorldOrigin"(): $Optional<(integer)>
public "getPotentialStructureChunk"(seed: long, x: integer, z: integer): $ChunkPos
public "spacing"(): integer
public "isStructureChunk"(chunkGeneratorStructureState: $ChunkGeneratorStructureState$Type, i: integer, j: integer): boolean
public "separation"(): integer
public "spreadType"(): $RandomSpreadType
public "type"(): $StructurePlacementType<(any)>
public "superExclusionZone"(): $Optional<($AdvancedRandomSpread$SuperExclusionZone)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdvancedRandomSpread$Type = ($AdvancedRandomSpread);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdvancedRandomSpread_ = $AdvancedRandomSpread$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/terrainadaptation/beardifier/$EnhancedJigsawJunction" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$JigsawJunction, $JigsawJunction$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$JigsawJunction"
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptation"

export class $EnhancedJigsawJunction extends $Record {

constructor(jigsawJunction: $JigsawJunction$Type, pieceTerrainAdaptation: $EnhancedTerrainAdaptation$Type)

public "equals"(o: any): boolean
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
declare module "packages/com/craisinlord/integrated_api/world/processors/$RandomReplaceWithPropertiesProcessor" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $RandomReplaceWithPropertiesProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($RandomReplaceWithPropertiesProcessor)>

constructor(inputBlock: $Block$Type, outputBlock: $Optional$Type<($Block$Type)>, outputBlocks: $List$Type<($Block$Type)>, probability: float)

public "processBlock"(worldReader: $LevelReader$Type, pos: $BlockPos$Type, pos2: $BlockPos$Type, infoIn1: $StructureTemplate$StructureBlockInfo$Type, infoIn2: $StructureTemplate$StructureBlockInfo$Type, settings: $StructurePlaceSettings$Type): $StructureTemplate$StructureBlockInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RandomReplaceWithPropertiesProcessor$Type = ($RandomReplaceWithPropertiesProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RandomReplaceWithPropertiesProcessor_ = $RandomReplaceWithPropertiesProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/mixins/resources/$NamespaceResourceManagerAccessor" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$FallbackResourceManager$PackEntry, $FallbackResourceManager$PackEntry$Type} from "packages/net/minecraft/server/packs/resources/$FallbackResourceManager$PackEntry"

export interface $NamespaceResourceManagerAccessor {

 "integratedapi_getFallbacks"(): $List<($FallbackResourceManager$PackEntry)>

(): $List<($FallbackResourceManager$PackEntry)>
}

export namespace $NamespaceResourceManagerAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NamespaceResourceManagerAccessor$Type = ($NamespaceResourceManagerAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NamespaceResourceManagerAccessor_ = $NamespaceResourceManagerAccessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/condition/$DepthCondition" {
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$StructureContext, $StructureContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext"
import {$StructureCondition, $StructureCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureCondition"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureConditionType"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $DepthCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($DepthCondition)>
readonly "minRequiredDepth": $Optional<(integer)>
readonly "maxPossibleDepth": $Optional<(integer)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(minRequiredDepth: $Optional$Type<(integer)>, maxPossibleDepth: $Optional$Type<(integer)>)

public "type"(): $StructureConditionType<(any)>
public "passes"(ctx: $StructureContext$Type): boolean
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
declare module "packages/com/craisinlord/integrated_api/events/lifecycle/$ServerGoingToStopEvent" {
import {$EventHandler, $EventHandler$Type} from "packages/com/craisinlord/integrated_api/events/base/$EventHandler"

export class $ServerGoingToStopEvent {
static readonly "INSTANCE": $ServerGoingToStopEvent
static readonly "EVENT": $EventHandler<($ServerGoingToStopEvent)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerGoingToStopEvent$Type = ($ServerGoingToStopEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerGoingToStopEvent_ = $ServerGoingToStopEvent$Type;
}}
declare module "packages/com/craisinlord/integrated_api/mixins/resources/$ReloadableResourceManagerImplAccessor" {
import {$FallbackResourceManager, $FallbackResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$FallbackResourceManager"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $ReloadableResourceManagerImplAccessor {

 "integratedapi_getNamespacedManagers"(): $Map<(string), ($FallbackResourceManager)>

(): $Map<(string), ($FallbackResourceManager)>
}

export namespace $ReloadableResourceManagerImplAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReloadableResourceManagerImplAccessor$Type = ($ReloadableResourceManagerImplAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReloadableResourceManagerImplAccessor_ = $ReloadableResourceManagerImplAccessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/structures/pieces/$IASinglePoolElement" {
import {$BoundingBox, $BoundingBox$Type} from "packages/net/minecraft/world/level/levelgen/structure/$BoundingBox"
import {$StructureTemplatePool$Projection, $StructureTemplatePool$Projection$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool$Projection"
import {$StructureProcessorList, $StructureProcessorList$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessorList"
import {$ChunkGenerator, $ChunkGenerator$Type} from "packages/net/minecraft/world/level/chunk/$ChunkGenerator"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$StructureModifier, $StructureModifier$Type} from "packages/com/craisinlord/integrated_api/world/structures/modifier/$StructureModifier"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$StructureCondition, $StructureCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureCondition"
import {$WorldGenLevel, $WorldGenLevel$Type} from "packages/net/minecraft/world/level/$WorldGenLevel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$StructureTemplate, $StructureTemplate$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$IAPoolElement, $IAPoolElement$Type} from "packages/com/craisinlord/integrated_api/world/structures/pieces/$IAPoolElement"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$StructurePoolElementType, $StructurePoolElementType$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElementType"
import {$RecordCodecBuilder, $RecordCodecBuilder$Type} from "packages/com/mojang/serialization/codecs/$RecordCodecBuilder"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$StructureTemplateManager, $StructureTemplateManager$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplateManager"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$StructureManager, $StructureManager$Type} from "packages/net/minecraft/world/level/$StructureManager"

export class $IASinglePoolElement extends $IAPoolElement {
static readonly "CODEC": $Codec<($IASinglePoolElement)>
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

constructor(template: $Either$Type<($ResourceLocation$Type), ($StructureTemplate$Type)>, processors: $Holder$Type<($StructureProcessorList$Type)>, projection: $StructureTemplatePool$Projection$Type, name: $Optional$Type<(string)>, maxCount: $Optional$Type<(integer)>, minRequiredDepth: $Optional$Type<(integer)>, maxPossibleDepth: $Optional$Type<(integer)>, isPriority: boolean, ignoreBounds: boolean, condition: $StructureCondition$Type, enhancedTerrainAdaptation: $Optional$Type<($EnhancedTerrainAdaptation$Type)>, deadendPool: $Optional$Type<($ResourceLocation$Type)>, modifiers: $List$Type<($StructureModifier$Type)>)

public "toString"(): string
public static "processorsCodec"<E extends $IASinglePoolElement>(): $RecordCodecBuilder<(E), ($Holder<($StructureProcessorList)>)>
public "getDeadendPool"(): $Optional<($ResourceLocation)>
public static "templateCodec"<E extends $IASinglePoolElement>(): $RecordCodecBuilder<(E), ($Either<($ResourceLocation), ($StructureTemplate)>)>
public "hasModifiers"(): boolean
public "getBoundingBox"(structureTemplateManager: $StructureTemplateManager$Type, blockPos: $BlockPos$Type, rotation: $Rotation$Type): $BoundingBox
public "place"(structureTemplateManager: $StructureTemplateManager$Type, worldGenLevel: $WorldGenLevel$Type, structureManager: $StructureManager$Type, chunkGenerator: $ChunkGenerator$Type, pos: $BlockPos$Type, pivotPos: $BlockPos$Type, rotation: $Rotation$Type, boundingBox: $BoundingBox$Type, randomSource: $RandomSource$Type, replaceJigsaws: boolean): boolean
public "getShuffledJigsawBlocks"(structureTemplateManager: $StructureTemplateManager$Type, blockPos: $BlockPos$Type, rotation: $Rotation$Type, randomSource: $RandomSource$Type): $List<($StructureTemplate$StructureBlockInfo)>
public "getType"(): $StructurePoolElementType<(any)>
public "getSize"(structureTemplateManager: $StructureTemplateManager$Type, rotation: $Rotation$Type): $Vec3i
public "getTemplate"(structureTemplateManager: $StructureTemplateManager$Type): $StructureTemplate
get "deadendPool"(): $Optional<($ResourceLocation)>
get "type"(): $StructurePoolElementType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IASinglePoolElement$Type = ($IASinglePoolElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IASinglePoolElement_ = $IASinglePoolElement$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptationType" {
import {$SmallCarvedTopNoBeardAdaptation, $SmallCarvedTopNoBeardAdaptation$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$SmallCarvedTopNoBeardAdaptation"
import {$CustomAdaptation, $CustomAdaptation$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$CustomAdaptation"
import {$LargeCarvedTopNoBeardAdaptation, $LargeCarvedTopNoBeardAdaptation$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$LargeCarvedTopNoBeardAdaptation"
import {$NoneAdaptation, $NoneAdaptation$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$NoneAdaptation"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $EnhancedTerrainAdaptationType<C extends $EnhancedTerrainAdaptation> {

 "codec"(): $Codec<(C)>

(resourceLocation: $ResourceLocation$Type, codec: $Codec$Type<(C)>): $EnhancedTerrainAdaptationType<(C)>
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
function register<C>(resourceLocation: $ResourceLocation$Type, codec: $Codec$Type<(C)>): $EnhancedTerrainAdaptationType<(C)>
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
declare module "packages/com/craisinlord/integrated_api/misc/maptrades/$WanderingTraderMapObj$TRADE_TYPE" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $WanderingTraderMapObj$TRADE_TYPE extends $Enum<($WanderingTraderMapObj$TRADE_TYPE)> {
static readonly "RARE": $WanderingTraderMapObj$TRADE_TYPE
static readonly "COMMON": $WanderingTraderMapObj$TRADE_TYPE


public static "values"(): ($WanderingTraderMapObj$TRADE_TYPE)[]
public static "valueOf"(name: string): $WanderingTraderMapObj$TRADE_TYPE
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WanderingTraderMapObj$TRADE_TYPE$Type = (("common") | ("rare")) | ($WanderingTraderMapObj$TRADE_TYPE);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WanderingTraderMapObj$TRADE_TYPE_ = $WanderingTraderMapObj$TRADE_TYPE$Type;
}}
declare module "packages/com/craisinlord/idas/state/$stateCache" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$stateRegion, $stateRegion$Type} from "packages/com/craisinlord/idas/state/$stateRegion"
import {$ConcurrentHashMap, $ConcurrentHashMap$Type} from "packages/java/util/concurrent/$ConcurrentHashMap"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $stateCache {
 "stateRegionMap": $ConcurrentHashMap<(string), ($stateRegion)>

constructor(arg0: $Path$Type)

public "isCleared"(arg0: $BlockPos$Type): boolean
public "setCleared"(arg0: $BlockPos$Type, arg1: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $stateCache$Type = ($stateCache);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $stateCache_ = $stateCache$Type;
}}
declare module "packages/com/craisinlord/idas/state/$stateRegion" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $stateRegion {

constructor(arg0: $Path$Type, arg1: string)

public "reset"(): void
public "isCleared"(arg0: $BlockPos$Type): boolean
public "setCleared"(arg0: $BlockPos$Type, arg1: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $stateRegion$Type = ($stateRegion);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $stateRegion_ = $stateRegion$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/structures/modifier/$StructureModifier" {
import {$StructureTargetSelector, $StructureTargetSelector$Type} from "packages/com/craisinlord/integrated_api/world/structures/targetselector/$StructureTargetSelector"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructureContext, $StructureContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext"
import {$StructureCondition, $StructureCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureCondition"
import {$StructureAction, $StructureAction$Type} from "packages/com/craisinlord/integrated_api/world/structures/action/$StructureAction"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $StructureModifier {
static readonly "CODEC": $Codec<($StructureModifier)>

constructor(condition: $StructureCondition$Type, actions: $List$Type<($StructureAction$Type)>, targetSelector: $StructureTargetSelector$Type)

public "apply"(structureContext: $StructureContext$Type): boolean
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
declare module "packages/com/craisinlord/integrated_api/utils/$AsyncLocator" {
import {$HolderSet, $HolderSet$Type} from "packages/net/minecraft/core/$HolderSet"
import {$AsyncLocator$LocateTask, $AsyncLocator$LocateTask$Type} from "packages/com/craisinlord/integrated_api/utils/$AsyncLocator$LocateTask"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Structure, $Structure$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"

export class $AsyncLocator {


public static "handleServerStoppingEvent"(): void
public static "handleServerAboutToStartEvent"(): void
public static "locate"(level: $ServerLevel$Type, structureTag: $TagKey$Type<($Structure$Type)>, pos: $BlockPos$Type, searchRadius: integer, skipKnownStructures: boolean): $AsyncLocator$LocateTask<($BlockPos)>
public static "locate"(level: $ServerLevel$Type, structureSet: $HolderSet$Type<($Structure$Type)>, pos: $BlockPos$Type, searchRadius: integer, skipKnownStructures: boolean): $AsyncLocator$LocateTask<($Pair<($BlockPos), ($Holder<($Structure)>)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AsyncLocator$Type = ($AsyncLocator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AsyncLocator_ = $AsyncLocator$Type;
}}
declare module "packages/com/craisinlord/integrated_api/events/base/$EventHandler" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"

export class $EventHandler<T> {

constructor()

public "invoke"(event: T): void
public "removeListener"(listener: $Consumer$Type<(T)>): void
public "addListener"(listener: $Consumer$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventHandler$Type<T> = ($EventHandler<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventHandler_<T> = $EventHandler$Type<(T)>;
}}
declare module "packages/com/craisinlord/integrated_api/datagen/$StructureNbtUpdaterDatagen" {
import {$GatherDataEvent, $GatherDataEvent$Type} from "packages/net/minecraftforge/data/event/$GatherDataEvent"

export class $StructureNbtUpdaterDatagen {

constructor()

public static "gatherData"(event: $GatherDataEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureNbtUpdaterDatagen$Type = ($StructureNbtUpdaterDatagen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureNbtUpdaterDatagen_ = $StructureNbtUpdaterDatagen$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/predicates/$YValuePosRuleTest" {
import {$PosRuleTest, $PosRuleTest$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$PosRuleTest"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $YValuePosRuleTest extends $PosRuleTest {
static readonly "CODEC": $Codec<($YValuePosRuleTest)>

constructor(minYValue: integer, maxYValue: integer)

public "test"(blockPos: $BlockPos$Type, blockPos2: $BlockPos$Type, blockPos3: $BlockPos$Type, random: $RandomSource$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $YValuePosRuleTest$Type = ($YValuePosRuleTest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $YValuePosRuleTest_ = $YValuePosRuleTest$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/structures/action/$TransformAction" {
import {$StructureActionType, $StructureActionType$Type} from "packages/com/craisinlord/integrated_api/world/structures/action/$StructureActionType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructureContext, $StructureContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$StructureAction, $StructureAction$Type} from "packages/com/craisinlord/integrated_api/world/structures/action/$StructureAction"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$StructureTemplate, $StructureTemplate$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate"
import {$PieceEntry, $PieceEntry$Type} from "packages/com/craisinlord/integrated_api/world/structures/pieces/assembler/$PieceEntry"

export class $TransformAction extends $StructureAction {
static readonly "CODEC": $Codec<($TransformAction)>

constructor(output: $List$Type<($Either$Type<($ResourceLocation$Type), ($StructureTemplate$Type)>)>, xOffset: integer, yOffset: integer, zOffset: integer)

public "type"(): $StructureActionType<(any)>
public "apply"(ctx: $StructureContext$Type, targetPieceEntry: $PieceEntry$Type): void
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
declare module "packages/com/craisinlord/integrated_api/world/condition/$StructureCondition" {
import {$StructureContext, $StructureContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureConditionType"

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
declare module "packages/com/craisinlord/integrated_villages/config/$ConfigModule" {
import {$ConfigModule$General, $ConfigModule$General$Type} from "packages/com/craisinlord/integrated_villages/config/$ConfigModule$General"

export class $ConfigModule {
 "general": $ConfigModule$General

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigModule$Type = ($ConfigModule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigModule_ = $ConfigModule$Type;
}}
declare module "packages/com/craisinlord/integrated_api/misc/structurepiececounter/$StructurePieceCountsManager" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$SimpleJsonResourceReloadListener, $SimpleJsonResourceReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimpleJsonResourceReloadListener"
import {$StructurePieceCountsManager$RequiredPieceNeeds, $StructurePieceCountsManager$RequiredPieceNeeds$Type} from "packages/com/craisinlord/integrated_api/misc/structurepiececounter/$StructurePieceCountsManager$RequiredPieceNeeds"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $StructurePieceCountsManager extends $SimpleJsonResourceReloadListener {
static readonly "STRUCTURE_PIECE_COUNTS_MANAGER": $StructurePieceCountsManager

constructor()

public "getMaximumCountForPieces"(structureRL: $ResourceLocation$Type): $Map<($ResourceLocation), (integer)>
public "parseAndAddCountsJSONObj"(structureRL: $ResourceLocation$Type, jsonElements: $List$Type<($JsonElement$Type)>): void
public "getRequirePieces"(structureRL: $ResourceLocation$Type): $Map<($ResourceLocation), ($StructurePieceCountsManager$RequiredPieceNeeds)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructurePieceCountsManager$Type = ($StructurePieceCountsManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructurePieceCountsManager_ = $StructurePieceCountsManager$Type;
}}
declare module "packages/com/craisinlord/integrated_api/modinit/registry/$ResourcefulRegistryChild" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourcefulRegistry, $ResourcefulRegistry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$ResourcefulRegistry"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntry"

export class $ResourcefulRegistryChild<T> implements $ResourcefulRegistry<(T)> {

constructor(parent: $ResourcefulRegistry$Type<(T)>)

public "register"<I extends T>(id: string, supplier: $Supplier$Type<(I)>): $RegistryEntry<(I)>
public "init"(): void
public "getEntries"(): $Collection<($RegistryEntry<(T)>)>
public "stream"(): $Stream<($RegistryEntry<(T)>)>
get "entries"(): $Collection<($RegistryEntry<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResourcefulRegistryChild$Type<T> = ($ResourcefulRegistryChild<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResourcefulRegistryChild_<T> = $ResourcefulRegistryChild$Type<(T)>;
}}
declare module "packages/com/craisinlord/integrated_api/world/processors/$WaterloggingFixProcessor" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $WaterloggingFixProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($WaterloggingFixProcessor)>


public "processBlock"(levelReader: $LevelReader$Type, pos: $BlockPos$Type, pos2: $BlockPos$Type, infoIn1: $StructureTemplate$StructureBlockInfo$Type, infoIn2: $StructureTemplate$StructureBlockInfo$Type, settings: $StructurePlaceSettings$Type): $StructureTemplate$StructureBlockInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaterloggingFixProcessor$Type = ($WaterloggingFixProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaterloggingFixProcessor_ = $WaterloggingFixProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/mixins/structures/$StructurePieceAccessor" {
import {$BoundingBox, $BoundingBox$Type} from "packages/net/minecraft/world/level/levelgen/structure/$BoundingBox"

export interface $StructurePieceAccessor {

 "setBoundingBox"(arg0: $BoundingBox$Type): void

(arg0: $BoundingBox$Type): void
}

export namespace $StructurePieceAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructurePieceAccessor$Type = ($StructurePieceAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructurePieceAccessor_ = $StructurePieceAccessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/misc/mobspawners/$MobSpawnerManager" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$SimpleJsonResourceReloadListener, $SimpleJsonResourceReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimpleJsonResourceReloadListener"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $MobSpawnerManager extends $SimpleJsonResourceReloadListener {
static readonly "MOB_SPAWNER_MANAGER": $MobSpawnerManager

constructor()

public "getSpawnerMob"(spawnerJsonEntry: $ResourceLocation$Type, random: $RandomSource$Type): $EntityType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobSpawnerManager$Type = ($MobSpawnerManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobSpawnerManager_ = $MobSpawnerManager$Type;
}}
declare module "packages/com/craisinlord/integrated_api/modinit/$IAConditionsRegistry" {
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntry"
import {$CustomRegistry, $CustomRegistry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$CustomRegistry"

export class $IAConditionsRegistry {
static readonly "IA_JSON_CONDITIONS_KEY": $ResourceKey<($Registry<($Supplier<(boolean)>)>)>
static readonly "IA_JSON_CONDITIONS_REGISTRY": $CustomRegistry<($Supplier<(boolean)>)>
static readonly "ALWAYS_TRUE": $RegistryEntry<($Supplier<(boolean)>)>
static readonly "ALWAYS_FALSE": $RegistryEntry<($Supplier<(boolean)>)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IAConditionsRegistry$Type = ($IAConditionsRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IAConditionsRegistry_ = $IAConditionsRegistry$Type;
}}
declare module "packages/com/craisinlord/integrated_stronghold/sound/$ISSounds" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $ISSounds {
static readonly "SOUND_EVENTS": $DeferredRegister<($SoundEvent)>
static "FORLORN": $RegistryObject<($SoundEvent)>
static "SIGHT": $RegistryObject<($SoundEvent)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ISSounds$Type = ($ISSounds);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ISSounds_ = $ISSounds$Type;
}}
declare module "packages/com/craisinlord/integrated_api/events/lifecycle/$TagsUpdatedEvent" {
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$EventHandler, $EventHandler$Type} from "packages/com/craisinlord/integrated_api/events/base/$EventHandler"

export class $TagsUpdatedEvent extends $Record {
static readonly "EVENT": $EventHandler<($TagsUpdatedEvent)>

constructor(registryAccess: $RegistryAccess$Type, fromPacket: boolean)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "fromPacket"(): boolean
public "registryAccess"(): $RegistryAccess
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TagsUpdatedEvent$Type = ($TagsUpdatedEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TagsUpdatedEvent_ = $TagsUpdatedEvent$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/structures/$ModAdaptiveStructure" {
import {$StructureTemplatePool, $StructureTemplatePool$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool"
import {$StructureType, $StructureType$Type} from "packages/net/minecraft/world/level/levelgen/structure/$StructureType"
import {$Heightmap$Types, $Heightmap$Types$Type} from "packages/net/minecraft/world/level/levelgen/$Heightmap$Types"
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Structure, $Structure$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$HeightProvider, $HeightProvider$Type} from "packages/net/minecraft/world/level/levelgen/heightproviders/$HeightProvider"
import {$Structure$GenerationStub, $Structure$GenerationStub$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$GenerationStub"
import {$Structure$GenerationContext, $Structure$GenerationContext$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$GenerationContext"
import {$JigsawStructure$BURYING_TYPE, $JigsawStructure$BURYING_TYPE$Type} from "packages/com/craisinlord/integrated_api/world/structures/$JigsawStructure$BURYING_TYPE"
import {$JigsawStructure, $JigsawStructure$Type} from "packages/com/craisinlord/integrated_api/world/structures/$JigsawStructure"
import {$Structure$StructureSettings, $Structure$StructureSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$StructureSettings"

export class $ModAdaptiveStructure extends $JigsawStructure {
static readonly "CODEC": $Codec<($ModAdaptiveStructure)>
readonly "changePoolMod": string
readonly "newPool": $Holder<($StructureTemplatePool)>
readonly "startPool": $Holder<($StructureTemplatePool)>
readonly "size": integer
readonly "minYAllowed": $Optional<(integer)>
readonly "maxYAllowed": $Optional<(integer)>
readonly "allowedYRangeFromStart": $Optional<(integer)>
readonly "startHeight": $HeightProvider
readonly "projectStartToHeightmap": $Optional<($Heightmap$Types)>
readonly "cannotSpawnInLiquid": boolean
readonly "terrainHeightCheckRadius": $Optional<(integer)>
readonly "allowedTerrainHeightRange": $Optional<(integer)>
readonly "biomeRadius": $Optional<(integer)>
readonly "maxDistanceFromCenter": $Optional<(integer)>
readonly "buryingType": $Optional<($JigsawStructure$BURYING_TYPE)>
readonly "rotationFixed": boolean
readonly "enhancedTerrainAdaptation": $EnhancedTerrainAdaptation
static readonly "DIRECT_CODEC": $Codec<($Structure)>

constructor(config: $Structure$StructureSettings$Type, startPool: $Holder$Type<($StructureTemplatePool$Type)>, size: integer, minYAllowed: $Optional$Type<(integer)>, maxYAllowed: $Optional$Type<(integer)>, startHeight: $HeightProvider$Type, projectStartToHeightmap: $Optional$Type<($Heightmap$Types$Type)>, cannotSpawnInLiquid: boolean, terrainHeightCheckRadius: $Optional$Type<(integer)>, allowedTerrainHeightRange: $Optional$Type<(integer)>, biomeRadius: $Optional$Type<(integer)>, maxDistanceFromCenter: $Optional$Type<(integer)>, rotationFixed: boolean, enhancedTerrainAdaptation: $EnhancedTerrainAdaptation$Type, changePoolMod: string, newPool: $Holder$Type<($StructureTemplatePool$Type)>)

public static "isLoaded"(name: string): boolean
public "type"(): $StructureType<(any)>
public "m_214086_"(context: $Structure$GenerationContext$Type): $Optional<($Structure$GenerationStub)>
public "allModsPresent"(convertedModList: $ArrayList$Type<(string)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModAdaptiveStructure$Type = ($ModAdaptiveStructure);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModAdaptiveStructure_ = $ModAdaptiveStructure$Type;
}}
declare module "packages/com/craisinlord/integrated_api/misc/structurepiececounter/$StructurePieceCountsObj" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $StructurePieceCountsObj {
 "nbtPieceName": string
 "alwaysSpawnThisMany": integer
 "neverSpawnMoreThanThisMany": integer
 "minimumDistanceFromCenterPiece": integer
 "condition": string

constructor(nbtPieceName: string, alwaysSpawnThisMany: integer, neverSpawnMoreThanThisMany: integer, minimumDistanceFromCenterPiece: integer, condition: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructurePieceCountsObj$Type = ($StructurePieceCountsObj);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructurePieceCountsObj_ = $StructurePieceCountsObj$Type;
}}
declare module "packages/com/craisinlord/integrated_api/mixins/structures/$ListPoolElementAccessor" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$StructurePoolElement, $StructurePoolElement$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElement"

export interface $ListPoolElementAccessor {

 "integratedapi_getElements"(): $List<($StructurePoolElement)>

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
declare module "packages/com/craisinlord/integrated_villages/world/processors/$MechanicalBearingProcessor" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $MechanicalBearingProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($MechanicalBearingProcessor)>


public "processBlock"(worldView: $LevelReader$Type, pos: $BlockPos$Type, blockPos: $BlockPos$Type, structureBlockInfoLocal: $StructureTemplate$StructureBlockInfo$Type, structureBlockInfoWorld: $StructureTemplate$StructureBlockInfo$Type, structurePlacementData: $StructurePlaceSettings$Type): $StructureTemplate$StructureBlockInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MechanicalBearingProcessor$Type = ($MechanicalBearingProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MechanicalBearingProcessor_ = $MechanicalBearingProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/modinit/registry/forge/$ForgeCustomRegistry" {
import {$IForgeRegistry, $IForgeRegistry$Type} from "packages/net/minecraftforge/registries/$IForgeRegistry"
import {$CustomRegistryLookup, $CustomRegistryLookup$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$CustomRegistryLookup"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"

export class $ForgeCustomRegistry<T> implements $CustomRegistryLookup<(T)> {

constructor(registry: $Supplier$Type<($IForgeRegistry$Type<(T)>)>)

public "get"(id: $ResourceLocation$Type): T
public "iterator"(): $Iterator<(T)>
public "getKey"(value: T): $ResourceLocation
public "containsKey"(id: $ResourceLocation$Type): boolean
public "containsValue"(value: T): boolean
public "getKeys"(): $Collection<($ResourceLocation)>
public "getValues"(): $Collection<(T)>
public "spliterator"(): $Spliterator<(T)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
[Symbol.iterator](): IterableIterator<T>;
get "keys"(): $Collection<($ResourceLocation)>
get "values"(): $Collection<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeCustomRegistry$Type<T> = ($ForgeCustomRegistry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeCustomRegistry_<T> = $ForgeCustomRegistry$Type<(T)>;
}}
declare module "packages/com/craisinlord/integrated_api/world/terrainadaptation/$LargeCarvedTopNoBeardAdaptation" {
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$EnhancedTerrainAdaptationType, $EnhancedTerrainAdaptationType$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptationType"

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
declare module "packages/com/craisinlord/integrated_api/world/placements/$UnlimitedCountPlacement" {
import {$RepeatingPlacement, $RepeatingPlacement$Type} from "packages/net/minecraft/world/level/levelgen/placement/$RepeatingPlacement"
import {$IntProvider, $IntProvider$Type} from "packages/net/minecraft/util/valueproviders/$IntProvider"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$PlacementModifierType, $PlacementModifierType$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementModifierType"

export class $UnlimitedCountPlacement extends $RepeatingPlacement {
static readonly "CODEC": $Codec<($UnlimitedCountPlacement)>


public static "of"(i: integer): $UnlimitedCountPlacement
public static "of"(intProvider: $IntProvider$Type): $UnlimitedCountPlacement
public "type"(): $PlacementModifierType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnlimitedCountPlacement$Type = ($UnlimitedCountPlacement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnlimitedCountPlacement_ = $UnlimitedCountPlacement$Type;
}}
declare module "packages/com/craisinlord/integrated_api/events/lifecycle/$FinalSetupEvent" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$EventHandler, $EventHandler$Type} from "packages/com/craisinlord/integrated_api/events/base/$EventHandler"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $FinalSetupEvent extends $Record {
static readonly "EVENT": $EventHandler<($FinalSetupEvent)>

constructor(enqueue: $Consumer$Type<($Runnable$Type)>)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "enqueue"(): $Consumer<($Runnable)>
public "enqueueWork"(runnable: $Runnable$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FinalSetupEvent$Type = ($FinalSetupEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FinalSetupEvent_ = $FinalSetupEvent$Type;
}}
declare module "packages/com/craisinlord/integrated_api/modinit/$IAProcessors" {
import {$BlockRemovalPostProcessor, $BlockRemovalPostProcessor$Type} from "packages/com/craisinlord/integrated_api/world/processors/$BlockRemovalPostProcessor"
import {$ReplaceAirOnlyProcessor, $ReplaceAirOnlyProcessor$Type} from "packages/com/craisinlord/integrated_api/world/processors/$ReplaceAirOnlyProcessor"
import {$ReplaceLiquidOnlyProcessor, $ReplaceLiquidOnlyProcessor$Type} from "packages/com/craisinlord/integrated_api/world/processors/$ReplaceLiquidOnlyProcessor"
import {$IntegratedBlockReplaceProcessor, $IntegratedBlockReplaceProcessor$Type} from "packages/com/craisinlord/integrated_api/world/processors/$IntegratedBlockReplaceProcessor"
import {$CloseOffFluidSourcesProcessor, $CloseOffFluidSourcesProcessor$Type} from "packages/com/craisinlord/integrated_api/world/processors/$CloseOffFluidSourcesProcessor"
import {$RemoveFloatingBlocksProcessor, $RemoveFloatingBlocksProcessor$Type} from "packages/com/craisinlord/integrated_api/world/processors/$RemoveFloatingBlocksProcessor"
import {$PostProcessListProcessor, $PostProcessListProcessor$Type} from "packages/com/craisinlord/integrated_api/world/processors/$PostProcessListProcessor"
import {$ResourcefulRegistry, $ResourcefulRegistry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$ResourcefulRegistry"
import {$MechanicalBearingProcessor, $MechanicalBearingProcessor$Type} from "packages/com/craisinlord/integrated_api/world/processors/$MechanicalBearingProcessor"
import {$WaterloggingFixProcessor, $WaterloggingFixProcessor$Type} from "packages/com/craisinlord/integrated_api/world/processors/$WaterloggingFixProcessor"
import {$TickBlocksProcessor, $TickBlocksProcessor$Type} from "packages/com/craisinlord/integrated_api/world/processors/$TickBlocksProcessor"
import {$StructureProcessorType, $StructureProcessorType$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessorType"
import {$WindmillBearingProcessor, $WindmillBearingProcessor$Type} from "packages/com/craisinlord/integrated_api/world/processors/$WindmillBearingProcessor"
import {$FloodWithWaterProcessor, $FloodWithWaterProcessor$Type} from "packages/com/craisinlord/integrated_api/world/processors/$FloodWithWaterProcessor"
import {$CloseOffAirSourcesProcessor, $CloseOffAirSourcesProcessor$Type} from "packages/com/craisinlord/integrated_api/world/processors/$CloseOffAirSourcesProcessor"
import {$FillEndPortalFrameProcessor, $FillEndPortalFrameProcessor$Type} from "packages/com/craisinlord/integrated_api/world/processors/$FillEndPortalFrameProcessor"
import {$CappedStructureSurfaceProcessor, $CappedStructureSurfaceProcessor$Type} from "packages/com/craisinlord/integrated_api/world/processors/$CappedStructureSurfaceProcessor"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntry"
import {$RandomReplaceWithPropertiesProcessor, $RandomReplaceWithPropertiesProcessor$Type} from "packages/com/craisinlord/integrated_api/world/processors/$RandomReplaceWithPropertiesProcessor"
import {$SpawnerRandomizingProcessor, $SpawnerRandomizingProcessor$Type} from "packages/com/craisinlord/integrated_api/world/processors/$SpawnerRandomizingProcessor"

export class $IAProcessors {
static readonly "STRUCTURE_PROCESSOR": $ResourcefulRegistry<($StructureProcessorType<(any)>)>
static readonly "BLOCK_REMOVAL_POST_PROCESSOR": $RegistryEntry<($StructureProcessorType<($BlockRemovalPostProcessor)>)>
static readonly "FLOOD_WITH_WATER_PROCESSOR": $RegistryEntry<($StructureProcessorType<($FloodWithWaterProcessor)>)>
static readonly "REPLACE_AIR_ONLY_PROCESSOR": $RegistryEntry<($StructureProcessorType<($ReplaceAirOnlyProcessor)>)>
static readonly "REPLACE_LIQUIDS_ONLY_PROCESSOR": $RegistryEntry<($StructureProcessorType<($ReplaceLiquidOnlyProcessor)>)>
static readonly "SPAWNER_RANDOMIZING_PROCESSOR": $RegistryEntry<($StructureProcessorType<($SpawnerRandomizingProcessor)>)>
static readonly "FILL_END_PORTAL_FRAME_PROCESSOR": $RegistryEntry<($StructureProcessorType<($FillEndPortalFrameProcessor)>)>
static readonly "REMOVE_FLOATING_BLOCKS_PROCESSOR": $RegistryEntry<($StructureProcessorType<($RemoveFloatingBlocksProcessor)>)>
static readonly "CLOSE_OFF_FLUID_SOURCES_PROCESSOR": $RegistryEntry<($StructureProcessorType<($CloseOffFluidSourcesProcessor)>)>
static readonly "CLOSE_OFF_AIR_SOURCES_PROCESSOR": $RegistryEntry<($StructureProcessorType<($CloseOffAirSourcesProcessor)>)>
static readonly "RANDOM_REPLACE_WITH_PROPERTIES_PROCESSOR": $RegistryEntry<($StructureProcessorType<($RandomReplaceWithPropertiesProcessor)>)>
static readonly "WATERLOGGING_FIX_PROCESSOR": $RegistryEntry<($StructureProcessorType<($WaterloggingFixProcessor)>)>
static readonly "CAPPED_STRUCTURE_SURFACE_PROCESSOR": $RegistryEntry<($StructureProcessorType<($CappedStructureSurfaceProcessor)>)>
static readonly "POST_PROCESS_LIST_PROCESSOR": $RegistryEntry<($StructureProcessorType<($PostProcessListProcessor)>)>
static readonly "WINDMILL_BEARING_PROCESSOR": $RegistryEntry<($StructureProcessorType<($WindmillBearingProcessor)>)>
static readonly "MECHANICAL_BEARING_PROCESSOR": $RegistryEntry<($StructureProcessorType<($MechanicalBearingProcessor)>)>
static readonly "TICK_BLOCKS_PROCESSOR": $RegistryEntry<($StructureProcessorType<($TickBlocksProcessor)>)>
static readonly "INTEGRATED_BLOCK_REPLACE_PROCESSOR": $RegistryEntry<($StructureProcessorType<($IntegratedBlockReplaceProcessor)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IAProcessors$Type = ($IAProcessors);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IAProcessors_ = $IAProcessors$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/processors/$MechanicalBearingProcessor" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $MechanicalBearingProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($MechanicalBearingProcessor)>


public "processBlock"(worldView: $LevelReader$Type, pos: $BlockPos$Type, blockPos: $BlockPos$Type, structureBlockInfoLocal: $StructureTemplate$StructureBlockInfo$Type, structureBlockInfoWorld: $StructureTemplate$StructureBlockInfo$Type, structurePlacementData: $StructurePlaceSettings$Type): $StructureTemplate$StructureBlockInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MechanicalBearingProcessor$Type = ($MechanicalBearingProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MechanicalBearingProcessor_ = $MechanicalBearingProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/events/base/$ReturnableEventHandler" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ReturnableEventHandler$ReturnableFunction, $ReturnableEventHandler$ReturnableFunction$Type} from "packages/com/craisinlord/integrated_api/events/base/$ReturnableEventHandler$ReturnableFunction"

export class $ReturnableEventHandler<T, R> {

constructor()

public "invoke"(event: T, defaultValue: R): R
public "invoke"(event: T): R
public "removeListener"(listener: $ReturnableEventHandler$ReturnableFunction$Type<(T), (R)>): void
public "addListener"(listener: $BiConsumer$Type<(R), (T)>): void
public "addListener"(listener: $ReturnableEventHandler$ReturnableFunction$Type<(T), (R)>): void
public "addListener"(listener: $Function$Type<(T), (R)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReturnableEventHandler$Type<T, R> = ($ReturnableEventHandler<(T), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReturnableEventHandler_<T, R> = $ReturnableEventHandler$Type<(T), (R)>;
}}
declare module "packages/com/craisinlord/integrated_api/modinit/registry/forge/$ForgeResourcefulRegistry" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntry"
import {$ResourcefulRegistry, $ResourcefulRegistry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$ResourcefulRegistry"

export class $ForgeResourcefulRegistry<T> implements $ResourcefulRegistry<(T)> {

constructor(registry: $ResourceKey$Type<(any)>, id: string)

public "register"<I extends T>(id: string, supplier: $Supplier$Type<(I)>): $RegistryEntry<(I)>
public "init"(): void
public "getEntries"(): $Collection<($RegistryEntry<(T)>)>
public "stream"(): $Stream<($RegistryEntry<(T)>)>
get "entries"(): $Collection<($RegistryEntry<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeResourcefulRegistry$Type<T> = ($ForgeResourcefulRegistry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeResourcefulRegistry_<T> = $ForgeResourcefulRegistry$Type<(T)>;
}}
declare module "packages/com/craisinlord/integrated_api/misc/structurepiececounter/$StructurePieceCountsAdditionsMerger" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $StructurePieceCountsAdditionsMerger {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructurePieceCountsAdditionsMerger$Type = ($StructurePieceCountsAdditionsMerger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructurePieceCountsAdditionsMerger_ = $StructurePieceCountsAdditionsMerger$Type;
}}
declare module "packages/com/craisinlord/integrated_villages/world/processors/$TickBlocksProcessor" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $TickBlocksProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($TickBlocksProcessor)>


public "processBlock"(levelReader: $LevelReader$Type, pos: $BlockPos$Type, blockPos: $BlockPos$Type, structureBlockInfoLocal: $StructureTemplate$StructureBlockInfo$Type, structureBlockInfoWorld: $StructureTemplate$StructureBlockInfo$Type, structurePlacementData: $StructurePlaceSettings$Type): $StructureTemplate$StructureBlockInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickBlocksProcessor$Type = ($TickBlocksProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickBlocksProcessor_ = $TickBlocksProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/misc/maptrades/$VillagerMapObj" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $VillagerMapObj {
readonly "structure": string
readonly "mapIcon": string
readonly "mapName": string
readonly "tradeLevel": integer
readonly "emeraldsRequired": integer
readonly "tradesAllowed": integer
readonly "xpReward": integer
readonly "spawnRegionSearchRadius": integer

constructor(structure: string, mapIcon: string, mapName: string, tradeLevel: integer, emeraldsRequired: integer, tradesAllowed: integer, xpReward: integer, spawnRegionSearchRadius: integer)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VillagerMapObj$Type = ($VillagerMapObj);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VillagerMapObj_ = $VillagerMapObj$Type;
}}
declare module "packages/com/craisinlord/idas/item/$IDASCreativeModeTab" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $IDASCreativeModeTab {
static readonly "CREATIVE_MODE_TABS": $DeferredRegister<($CreativeModeTab)>
static readonly "IDAS_TAB": $RegistryObject<($CreativeModeTab)>

constructor()

public static "register"(arg0: $IEventBus$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IDASCreativeModeTab$Type = ($IDASCreativeModeTab);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IDASCreativeModeTab_ = $IDASCreativeModeTab$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/condition/$AltitudeCondition" {
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$StructureContext, $StructureContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext"
import {$StructureCondition, $StructureCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureCondition"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureConditionType"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $AltitudeCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($AltitudeCondition)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor(bottomCutoffY: $Optional$Type<(double)>, topCutoffY: $Optional$Type<(double)>)

public "type"(): $StructureConditionType<(any)>
public "passes"(ctx: $StructureContext$Type): boolean
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
declare module "packages/com/craisinlord/integrated_api/world/condition/$AlwaysTrueCondition" {
import {$StructureContext, $StructureContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext"
import {$StructureCondition, $StructureCondition$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureCondition"
import {$StructureConditionType, $StructureConditionType$Type} from "packages/com/craisinlord/integrated_api/world/condition/$StructureConditionType"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $AlwaysTrueCondition extends $StructureCondition {
static readonly "CODEC": $Codec<($AlwaysTrueCondition)>
static readonly "ALWAYS_TRUE": $StructureCondition

constructor()

public "type"(): $StructureConditionType<(any)>
public "passes"(ctx: $StructureContext$Type): boolean
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
declare module "packages/com/craisinlord/integrated_api/world/structures/$NetherJigsawStructure" {
import {$StructureTemplatePool, $StructureTemplatePool$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool"
import {$StructureType, $StructureType$Type} from "packages/net/minecraft/world/level/levelgen/structure/$StructureType"
import {$Heightmap$Types, $Heightmap$Types$Type} from "packages/net/minecraft/world/level/levelgen/$Heightmap$Types"
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$NetherJigsawStructure$LAND_SEARCH_DIRECTION, $NetherJigsawStructure$LAND_SEARCH_DIRECTION$Type} from "packages/com/craisinlord/integrated_api/world/structures/$NetherJigsawStructure$LAND_SEARCH_DIRECTION"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Structure, $Structure$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$HeightProvider, $HeightProvider$Type} from "packages/net/minecraft/world/level/levelgen/heightproviders/$HeightProvider"
import {$JigsawStructure$BURYING_TYPE, $JigsawStructure$BURYING_TYPE$Type} from "packages/com/craisinlord/integrated_api/world/structures/$JigsawStructure$BURYING_TYPE"
import {$JigsawStructure, $JigsawStructure$Type} from "packages/com/craisinlord/integrated_api/world/structures/$JigsawStructure"
import {$Structure$StructureSettings, $Structure$StructureSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$StructureSettings"

export class $NetherJigsawStructure extends $JigsawStructure {
static readonly "CODEC": $Codec<($NetherJigsawStructure)>
readonly "ledgeOffsetY": $Optional<(integer)>
readonly "searchDirection": $NetherJigsawStructure$LAND_SEARCH_DIRECTION
readonly "startPool": $Holder<($StructureTemplatePool)>
readonly "size": integer
readonly "minYAllowed": $Optional<(integer)>
readonly "maxYAllowed": $Optional<(integer)>
readonly "allowedYRangeFromStart": $Optional<(integer)>
readonly "startHeight": $HeightProvider
readonly "projectStartToHeightmap": $Optional<($Heightmap$Types)>
readonly "cannotSpawnInLiquid": boolean
readonly "terrainHeightCheckRadius": $Optional<(integer)>
readonly "allowedTerrainHeightRange": $Optional<(integer)>
readonly "biomeRadius": $Optional<(integer)>
readonly "maxDistanceFromCenter": $Optional<(integer)>
readonly "buryingType": $Optional<($JigsawStructure$BURYING_TYPE)>
readonly "rotationFixed": boolean
readonly "enhancedTerrainAdaptation": $EnhancedTerrainAdaptation
static readonly "DIRECT_CODEC": $Codec<($Structure)>

constructor(config: $Structure$StructureSettings$Type, startPool: $Holder$Type<($StructureTemplatePool$Type)>, size: integer, minYAllowed: $Optional$Type<(integer)>, maxYAllowed: $Optional$Type<(integer)>, allowedYRangeFromStart: $Optional$Type<(integer)>, startHeight: $HeightProvider$Type, cannotSpawnInLiquid: boolean, biomeRadius: $Optional$Type<(integer)>, maxDistanceFromCenter: $Optional$Type<(integer)>, ledgeOffsetY: $Optional$Type<(integer)>, searchDirection: $NetherJigsawStructure$LAND_SEARCH_DIRECTION$Type, rotationFixed: boolean, enhancedTerrainAdaptation: $EnhancedTerrainAdaptation$Type)

public "type"(): $StructureType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetherJigsawStructure$Type = ($NetherJigsawStructure);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetherJigsawStructure_ = $NetherJigsawStructure$Type;
}}
declare module "packages/com/craisinlord/integrated_api/mixins/structures/$StructureTemplateManagerAccessor" {
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"

export interface $StructureTemplateManagerAccessor {

 "integratedapi_getResourceManager"(): $ResourceManager

(): $ResourceManager
}

export namespace $StructureTemplateManagerAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureTemplateManagerAccessor$Type = ($StructureTemplateManagerAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureTemplateManagerAccessor_ = $StructureTemplateManagerAccessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/predicates/$MatterPhaseRuleTest" {
import {$RuleTest, $RuleTest$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$RuleTest"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $MatterPhaseRuleTest extends $RuleTest {
static readonly "CODEC": $Codec<($MatterPhaseRuleTest)>


public "test"(state: $BlockState$Type, random: $RandomSource$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MatterPhaseRuleTest$Type = ($MatterPhaseRuleTest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MatterPhaseRuleTest_ = $MatterPhaseRuleTest$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/processors/$SpawnerRandomizingProcessor" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$InclusiveRange, $InclusiveRange$Type} from "packages/net/minecraft/util/$InclusiveRange"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $SpawnerRandomizingProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($SpawnerRandomizingProcessor)>
readonly "rsSpawnerResourcelocation": $ResourceLocation
readonly "validBlockLightLevel": $Optional<($InclusiveRange<(integer)>)>
readonly "validSkyLightLevel": $Optional<($InclusiveRange<(integer)>)>
readonly "delay": integer
readonly "maxNearbyEntities": integer
readonly "maxSpawnDelay": integer
readonly "minSpawnDelay": integer
readonly "requiredPlayerRange": integer
readonly "spawnCount": integer
readonly "spawnRange": integer
readonly "replacementState": $BlockState


public "processBlock"(worldView: $LevelReader$Type, pos: $BlockPos$Type, blockPos: $BlockPos$Type, structureBlockInfoLocal: $StructureTemplate$StructureBlockInfo$Type, structureBlockInfoWorld: $StructureTemplate$StructureBlockInfo$Type, structurePlacementData: $StructurePlaceSettings$Type): $StructureTemplate$StructureBlockInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpawnerRandomizingProcessor$Type = ($SpawnerRandomizingProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpawnerRandomizingProcessor_ = $SpawnerRandomizingProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/modinit/$IAStructurePieces" {
import {$StructurePieceType, $StructurePieceType$Type} from "packages/net/minecraft/world/level/levelgen/structure/pieces/$StructurePieceType"
import {$StructurePoolElementType, $StructurePoolElementType$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElementType"
import {$IASinglePoolElement, $IASinglePoolElement$Type} from "packages/com/craisinlord/integrated_api/world/structures/pieces/$IASinglePoolElement"
import {$ResourcefulRegistry, $ResourcefulRegistry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$ResourcefulRegistry"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntry"

export class $IAStructurePieces {
static readonly "STRUCTURE_POOL_ELEMENT": $ResourcefulRegistry<($StructurePoolElementType<(any)>)>
static readonly "STRUCTURE_PIECE": $ResourcefulRegistry<($StructurePieceType)>
static readonly "IA_POOL_ELEMENT": $RegistryEntry<($StructurePoolElementType<($IASinglePoolElement)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IAStructurePieces$Type = ($IAStructurePieces);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IAStructurePieces_ = $IAStructurePieces$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/terrainadaptation/beardifier/$EnhancedBeardifierRigid" {
import {$BoundingBox, $BoundingBox$Type} from "packages/net/minecraft/world/level/levelgen/structure/$BoundingBox"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptation"

export class $EnhancedBeardifierRigid extends $Record {

constructor(pieceBoundingBox: $BoundingBox$Type, pieceTerrainAdaptation: $EnhancedTerrainAdaptation$Type, pieceGroundLevelDelta: integer)

public "equals"(o: any): boolean
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
declare module "packages/com/craisinlord/integrated_api/mixins/entities/$MerchantOfferAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $MerchantOfferAccessor {

 "setMaxUses"(arg0: integer): void

(arg0: integer): void
}

export namespace $MerchantOfferAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MerchantOfferAccessor$Type = ($MerchantOfferAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MerchantOfferAccessor_ = $MerchantOfferAccessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/misc/maptrades/$StructureSpecificMaps" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $StructureSpecificMaps {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureSpecificMaps$Type = ($StructureSpecificMaps);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureSpecificMaps_ = $StructureSpecificMaps$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/structures/$OptionalDependencyStructure" {
import {$StructureTemplatePool, $StructureTemplatePool$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool"
import {$StructureType, $StructureType$Type} from "packages/net/minecraft/world/level/levelgen/structure/$StructureType"
import {$Heightmap$Types, $Heightmap$Types$Type} from "packages/net/minecraft/world/level/levelgen/$Heightmap$Types"
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Structure, $Structure$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$HeightProvider, $HeightProvider$Type} from "packages/net/minecraft/world/level/levelgen/heightproviders/$HeightProvider"
import {$JigsawStructure$BURYING_TYPE, $JigsawStructure$BURYING_TYPE$Type} from "packages/com/craisinlord/integrated_api/world/structures/$JigsawStructure$BURYING_TYPE"
import {$JigsawStructure, $JigsawStructure$Type} from "packages/com/craisinlord/integrated_api/world/structures/$JigsawStructure"
import {$Structure$StructureSettings, $Structure$StructureSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$StructureSettings"

export class $OptionalDependencyStructure extends $JigsawStructure {
static readonly "CODEC": $Codec<($OptionalDependencyStructure)>
readonly "requiredMod": $Optional<(string)>
readonly "illegalMod": $Optional<(string)>
readonly "startPool": $Holder<($StructureTemplatePool)>
readonly "size": integer
readonly "minYAllowed": $Optional<(integer)>
readonly "maxYAllowed": $Optional<(integer)>
readonly "allowedYRangeFromStart": $Optional<(integer)>
readonly "startHeight": $HeightProvider
readonly "projectStartToHeightmap": $Optional<($Heightmap$Types)>
readonly "cannotSpawnInLiquid": boolean
readonly "terrainHeightCheckRadius": $Optional<(integer)>
readonly "allowedTerrainHeightRange": $Optional<(integer)>
readonly "biomeRadius": $Optional<(integer)>
readonly "maxDistanceFromCenter": $Optional<(integer)>
readonly "buryingType": $Optional<($JigsawStructure$BURYING_TYPE)>
readonly "rotationFixed": boolean
readonly "enhancedTerrainAdaptation": $EnhancedTerrainAdaptation
static readonly "DIRECT_CODEC": $Codec<($Structure)>

constructor(config: $Structure$StructureSettings$Type, startPool: $Holder$Type<($StructureTemplatePool$Type)>, size: integer, minYAllowed: $Optional$Type<(integer)>, maxYAllowed: $Optional$Type<(integer)>, startHeight: $HeightProvider$Type, projectStartToHeightmap: $Optional$Type<($Heightmap$Types$Type)>, cannotSpawnInLiquid: boolean, terrainHeightCheckRadius: $Optional$Type<(integer)>, allowedTerrainHeightRange: $Optional$Type<(integer)>, biomeRadius: $Optional$Type<(integer)>, maxDistanceFromCenter: $Optional$Type<(integer)>, requiredMod: $Optional$Type<(string)>, illegalMod: $Optional$Type<(string)>, rotationFixed: boolean, enhancedTerrainAdaptation: $EnhancedTerrainAdaptation$Type)

public "isLoaded"(name: string): boolean
public "type"(): $StructureType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptionalDependencyStructure$Type = ($OptionalDependencyStructure);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptionalDependencyStructure_ = $OptionalDependencyStructure$Type;
}}
declare module "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntry" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $RegistryEntry<T> extends $Supplier<(T)> {

 "get"(): T
 "getId"(): $ResourceLocation
}

export namespace $RegistryEntry {
const probejs$$marker: never
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
declare module "packages/com/craisinlord/integrated_api/world/structures/action/$DelayGenerationAction" {
import {$StructureActionType, $StructureActionType$Type} from "packages/com/craisinlord/integrated_api/world/structures/action/$StructureActionType"
import {$StructureContext, $StructureContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext"
import {$StructureAction, $StructureAction$Type} from "packages/com/craisinlord/integrated_api/world/structures/action/$StructureAction"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$PieceEntry, $PieceEntry$Type} from "packages/com/craisinlord/integrated_api/world/structures/pieces/assembler/$PieceEntry"

export class $DelayGenerationAction extends $StructureAction {
static readonly "CODEC": $Codec<($DelayGenerationAction)>

constructor()

public "type"(): $StructureActionType<(any)>
public "apply"(ctx: $StructureContext$Type, targetPieceEntry: $PieceEntry$Type): void
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
declare module "packages/com/craisinlord/integrated_api/world/structures/pieces/assembler/$PieceContext" {
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$StructurePoolElement, $StructurePoolElement$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElement"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$MutableObject, $MutableObject$Type} from "packages/org/apache/commons/lang3/mutable/$MutableObject"
import {$BoxOctree, $BoxOctree$Type} from "packages/com/craisinlord/integrated_api/utils/$BoxOctree"
import {$ObjectArrayList, $ObjectArrayList$Type} from "packages/it/unimi/dsi/fastutil/objects/$ObjectArrayList"
import {$PieceEntry, $PieceEntry$Type} from "packages/com/craisinlord/integrated_api/world/structures/pieces/assembler/$PieceEntry"

export class $PieceContext {
 "candidatePoolElements": $ObjectArrayList<($Pair<($StructurePoolElement), (integer)>)>
 "jigsawBlock": $StructureTemplate$StructureBlockInfo
 "jigsawBlockTargetPos": $BlockPos
 "pieceMinY": integer
 "jigsawBlockPos": $BlockPos
 "boxOctree": $MutableObject<($BoxOctree)>
 "pieceEntry": $PieceEntry
 "depth": integer

constructor(candidatePoolElements: $ObjectArrayList$Type<($Pair$Type<($StructurePoolElement$Type), (integer)>)>, jigsawBlock: $StructureTemplate$StructureBlockInfo$Type, jigsawBlockTargetPos: $BlockPos$Type, pieceMinY: integer, jigsawBlockPos: $BlockPos$Type, boxOctree: $MutableObject$Type<($BoxOctree$Type)>, pieceEntry: $PieceEntry$Type, depth: integer)

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
declare module "packages/com/craisinlord/integrated_api/world/structures/$NetherJigsawStructure$LAND_SEARCH_DIRECTION" {
import {$StringRepresentable$EnumCodec, $StringRepresentable$EnumCodec$Type} from "packages/net/minecraft/util/$StringRepresentable$EnumCodec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $NetherJigsawStructure$LAND_SEARCH_DIRECTION extends $Enum<($NetherJigsawStructure$LAND_SEARCH_DIRECTION)> implements $StringRepresentable {
static readonly "HIGHEST_LAND": $NetherJigsawStructure$LAND_SEARCH_DIRECTION
static readonly "LOWEST_LAND": $NetherJigsawStructure$LAND_SEARCH_DIRECTION


public static "values"(): ($NetherJigsawStructure$LAND_SEARCH_DIRECTION)[]
public static "valueOf"(name: string): $NetherJigsawStructure$LAND_SEARCH_DIRECTION
public "getSerializedName"(): string
public static "byName"(name: string): $NetherJigsawStructure$LAND_SEARCH_DIRECTION
public static "fromEnumWithMapping"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(string), (string)>): $StringRepresentable$EnumCodec<(E)>
public static "keys"(arg0: ($StringRepresentable$Type)[]): $Keyable
public static "fromEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>): $StringRepresentable$EnumCodec<(E)>
get "serializedName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetherJigsawStructure$LAND_SEARCH_DIRECTION$Type = (("highest_land") | ("lowest_land")) | ($NetherJigsawStructure$LAND_SEARCH_DIRECTION);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetherJigsawStructure$LAND_SEARCH_DIRECTION_ = $NetherJigsawStructure$LAND_SEARCH_DIRECTION$Type;
}}
declare module "packages/com/craisinlord/integrated_api/modinit/forge/$IABiomeModifiers" {
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $IABiomeModifiers {
static readonly "BIOME_MODIFIER_SERIALIZERS": $DeferredRegister<($Codec<(any)>)>
static readonly "ADDITIONS_MODIFIER": $RegistryObject<($Codec<(any)>)>
static readonly "ADDITIONS_TEMPERATURE_MODIFIER": $RegistryObject<($Codec<(any)>)>
static readonly "REMOVALS_MODIFIER": $RegistryObject<($Codec<(any)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IABiomeModifiers$Type = ($IABiomeModifiers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IABiomeModifiers_ = $IABiomeModifiers$Type;
}}
declare module "packages/com/craisinlord/integrated_api/modinit/registry/$ResourcefulRegistries" {
import {$Pair, $Pair$Type} from "packages/org/apache/commons/lang3/tuple/$Pair"
import {$CustomRegistryLookup, $CustomRegistryLookup$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$CustomRegistryLookup"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ResourcefulRegistry, $ResourcefulRegistry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$ResourcefulRegistry"

export class $ResourcefulRegistries {

constructor()

public static "create"<T>(registry: $Registry$Type<(T)>, id: string): $ResourcefulRegistry<(T)>
public static "create"<T>(parent: $ResourcefulRegistry$Type<(T)>): $ResourcefulRegistry<(T)>
public static "createCustomRegistryInternal"<T, K extends $Registry<(T)>>(modId: string, key: $ResourceKey$Type<(K)>, save: boolean, sync: boolean, allowModification: boolean): $Pair<($Supplier<($CustomRegistryLookup<(T)>)>), ($ResourcefulRegistry<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResourcefulRegistries$Type = ($ResourcefulRegistries);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResourcefulRegistries_ = $ResourcefulRegistries$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/terrainadaptation/beardifier/$EnhancedBeardifierData" {
import {$EnhancedBeardifierRigid, $EnhancedBeardifierRigid$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/beardifier/$EnhancedBeardifierRigid"
import {$ObjectListIterator, $ObjectListIterator$Type} from "packages/it/unimi/dsi/fastutil/objects/$ObjectListIterator"
import {$EnhancedJigsawJunction, $EnhancedJigsawJunction$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/beardifier/$EnhancedJigsawJunction"

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
declare module "packages/com/craisinlord/idas/$IDASTags" {
import {$Structure, $Structure$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"

export class $IDASTags {
static "APPLIES_MINING_FATIGUE": $TagKey<($Structure)>

constructor()

public static "initTags"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IDASTags$Type = ($IDASTags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IDASTags_ = $IDASTags$Type;
}}
declare module "packages/com/craisinlord/integrated_api/events/lifecycle/$ServerGoingToStartEvent" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$EventHandler, $EventHandler$Type} from "packages/com/craisinlord/integrated_api/events/base/$EventHandler"

export class $ServerGoingToStartEvent extends $Record {
static readonly "EVENT": $EventHandler<($ServerGoingToStartEvent)>

constructor(server: $MinecraftServer$Type)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "server"(): $MinecraftServer
public "getServer"(): $MinecraftServer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerGoingToStartEvent$Type = ($ServerGoingToStartEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerGoingToStartEvent_ = $ServerGoingToStartEvent$Type;
}}
declare module "packages/com/craisinlord/integrated_api/utils/$GeneralUtils" {
import {$BoundingBox, $BoundingBox$Type} from "packages/net/minecraft/world/level/levelgen/structure/$BoundingBox"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$ChunkGenerator, $ChunkGenerator$Type} from "packages/net/minecraft/world/level/chunk/$ChunkGenerator"
import {$LevelHeightAccessor, $LevelHeightAccessor$Type} from "packages/net/minecraft/world/level/$LevelHeightAccessor"
import {$StructurePiece, $StructurePiece$Type} from "packages/net/minecraft/world/level/levelgen/structure/$StructurePiece"
import {$RandomState, $RandomState$Type} from "packages/net/minecraft/world/level/levelgen/$RandomState"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$ServerLevelAccessor, $ServerLevelAccessor$Type} from "packages/net/minecraft/world/level/$ServerLevelAccessor"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $GeneralUtils {


public static "isFullCube"(world: $BlockGetter$Type, pos: $BlockPos$Type, state: $BlockState$Type): boolean
public static "enchantRandomly"(random: $RandomSource$Type, itemToEnchant: $ItemStack$Type, chance: float): $ItemStack
public static "centerAllPieces"(targetPos: $BlockPos$Type, pieces: $List$Type<(any)>): void
public static "orientateChest"(blockView: $ServerLevelAccessor$Type, blockPos: $BlockPos$Type, blockState: $BlockState$Type): $BlockState
public static "canJigsawsAttach"(jigsaw1: $StructureTemplate$StructureBlockInfo$Type, jigsaw2: $StructureTemplate$StructureBlockInfo$Type): boolean
public static "getAllFileStreams"(resourceManager: $ResourceManager$Type, fileID: $ResourceLocation$Type): $List<($InputStream)>
public static "movePieceProperly"(piece: $StructurePiece$Type, x: integer, y: integer, z: integer): void
public static "getRandomEntry"<T>(rlList: $List$Type<($Pair$Type<(T), (integer)>)>, random: $RandomSource$Type): T
public static "getFirstLandYFromPos"(worldView: $LevelReader$Type, pos: $BlockPos$Type): integer
public static "getAllDatapacksJSONElement"(resourceManager: $ResourceManager$Type, gson: $Gson$Type, dataType: string, fileSuffixLength: integer): $Map<($ResourceLocation), ($List<($JsonElement)>)>
public static "getHighestLand"(chunkGenerator: $ChunkGenerator$Type, randomState: $RandomState$Type, boundingBox: $BoundingBox$Type, heightLimitView: $LevelHeightAccessor$Type, canBeOnLiquid: boolean): $BlockPos
public static "getLowestLand"(chunkGenerator: $ChunkGenerator$Type, randomState: $RandomState$Type, boundingBox: $BoundingBox$Type, heightLimitView: $LevelHeightAccessor$Type, canBeOnLiquid: boolean): $BlockPos
public static "getMaxTerrainLimit"(chunkGenerator: $ChunkGenerator$Type): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GeneralUtils$Type = ($GeneralUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GeneralUtils_ = $GeneralUtils$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/processors/$PostProcessListProcessor" {
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ServerLevelAccessor, $ServerLevelAccessor$Type} from "packages/net/minecraft/world/level/$ServerLevelAccessor"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $PostProcessListProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($PostProcessListProcessor)>

constructor(structureProcessors: $List$Type<($StructureProcessor$Type)>)

public "finalizeProcessing"(serverLevelAccessor: $ServerLevelAccessor$Type, nbtOriginPos: $BlockPos$Type, chunkCenter: $BlockPos$Type, nbtOriginBlockInfo: $List$Type<($StructureTemplate$StructureBlockInfo$Type)>, worldOriginBlockInfo: $List$Type<($StructureTemplate$StructureBlockInfo$Type)>, structurePlaceSettings: $StructurePlaceSettings$Type): $List<($StructureTemplate$StructureBlockInfo)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PostProcessListProcessor$Type = ($PostProcessListProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PostProcessListProcessor_ = $PostProcessListProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/modinit/registry/$ResourcefulRegistry" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$RegistryEntry, $RegistryEntry$Type} from "packages/com/craisinlord/integrated_api/modinit/registry/$RegistryEntry"

export interface $ResourcefulRegistry<T> {

 "stream"(): $Stream<($RegistryEntry<(T)>)>
 "register"<I extends T>(arg0: string, arg1: $Supplier$Type<(I)>): $RegistryEntry<(I)>
 "init"(): void
 "getEntries"(): $Collection<($RegistryEntry<(T)>)>
}

export namespace $ResourcefulRegistry {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResourcefulRegistry$Type<T> = ($ResourcefulRegistry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResourcefulRegistry_<T> = $ResourcefulRegistry$Type<(T)>;
}}
declare module "packages/com/craisinlord/integrated_api/modinit/registry/$CustomRegistryLookup" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export interface $CustomRegistryLookup<T> extends $Iterable<(T)> {

 "get"(arg0: $ResourceLocation$Type): T
 "getKey"(arg0: T): $ResourceLocation
 "containsKey"(arg0: $ResourceLocation$Type): boolean
 "containsValue"(arg0: T): boolean
 "getKeys"(): $Collection<($ResourceLocation)>
 "getValues"(): $Collection<(T)>
 "iterator"(): $Iterator<(T)>
 "spliterator"(): $Spliterator<(T)>
 "forEach"(arg0: $Consumer$Type<(any)>): void
}

export namespace $CustomRegistryLookup {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomRegistryLookup$Type<T> = ($CustomRegistryLookup<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomRegistryLookup_<T> = $CustomRegistryLookup$Type<(T)>;
}}
declare module "packages/com/craisinlord/idas/config/$ConfigModuleForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ConfigModuleForge {

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigModuleForge$Type = ($ConfigModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigModuleForge_ = $ConfigModuleForge$Type;
}}
declare module "packages/com/craisinlord/integrated_villages/forge/$IntegratedVillagesForge" {
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"

export class $IntegratedVillagesForge {

constructor()

public "setup"(event: $FMLCommonSetupEvent$Type): void
set "up"(value: $FMLCommonSetupEvent$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntegratedVillagesForge$Type = ($IntegratedVillagesForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntegratedVillagesForge_ = $IntegratedVillagesForge$Type;
}}
declare module "packages/com/craisinlord/integrated_villages/world/processors/$WindmillBearingProcessor" {
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$StructurePlaceSettings, $StructurePlaceSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructurePlaceSettings"
import {$StructureProcessor, $StructureProcessor$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureProcessor"
import {$StructureTemplate$StructureBlockInfo, $StructureTemplate$StructureBlockInfo$Type} from "packages/net/minecraft/world/level/levelgen/structure/templatesystem/$StructureTemplate$StructureBlockInfo"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $WindmillBearingProcessor extends $StructureProcessor {
static readonly "CODEC": $Codec<($WindmillBearingProcessor)>


public "processBlock"(worldView: $LevelReader$Type, pos: $BlockPos$Type, blockPos: $BlockPos$Type, structureBlockInfoLocal: $StructureTemplate$StructureBlockInfo$Type, structureBlockInfoWorld: $StructureTemplate$StructureBlockInfo$Type, structurePlacementData: $StructurePlaceSettings$Type): $StructureTemplate$StructureBlockInfo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WindmillBearingProcessor$Type = ($WindmillBearingProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WindmillBearingProcessor_ = $WindmillBearingProcessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/mixins/items/$MapItemAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $MapItemAccessor {

}

export namespace $MapItemAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapItemAccessor$Type = ($MapItemAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapItemAccessor_ = $MapItemAccessor$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptation" {
import {$EnhancedTerrainAdaptationType, $EnhancedTerrainAdaptationType$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptationType"

export class $EnhancedTerrainAdaptation {
static readonly "NONE": $EnhancedTerrainAdaptation


public "computeDensityFactor"(xDistance: integer, yDistance: integer, zDistance: integer, yDistanceToBeardBase: integer): double
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
declare module "packages/com/craisinlord/integrated_api/world/structures/$BiomeFacingStructure" {
import {$HolderSet, $HolderSet$Type} from "packages/net/minecraft/core/$HolderSet"
import {$StructureTemplatePool, $StructureTemplatePool$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructureTemplatePool"
import {$StructureType, $StructureType$Type} from "packages/net/minecraft/world/level/levelgen/structure/$StructureType"
import {$Heightmap$Types, $Heightmap$Types$Type} from "packages/net/minecraft/world/level/levelgen/$Heightmap$Types"
import {$EnhancedTerrainAdaptation, $EnhancedTerrainAdaptation$Type} from "packages/com/craisinlord/integrated_api/world/terrainadaptation/$EnhancedTerrainAdaptation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Structure, $Structure$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$HeightProvider, $HeightProvider$Type} from "packages/net/minecraft/world/level/levelgen/heightproviders/$HeightProvider"
import {$Structure$GenerationStub, $Structure$GenerationStub$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$GenerationStub"
import {$Structure$GenerationContext, $Structure$GenerationContext$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$GenerationContext"
import {$JigsawStructure$BURYING_TYPE, $JigsawStructure$BURYING_TYPE$Type} from "packages/com/craisinlord/integrated_api/world/structures/$JigsawStructure$BURYING_TYPE"
import {$JigsawStructure, $JigsawStructure$Type} from "packages/com/craisinlord/integrated_api/world/structures/$JigsawStructure"
import {$Structure$StructureSettings, $Structure$StructureSettings$Type} from "packages/net/minecraft/world/level/levelgen/structure/$Structure$StructureSettings"

export class $BiomeFacingStructure extends $JigsawStructure {
static readonly "CODEC": $Codec<($BiomeFacingStructure)>
readonly "startPool": $Holder<($StructureTemplatePool)>
readonly "size": integer
readonly "minYAllowed": $Optional<(integer)>
readonly "maxYAllowed": $Optional<(integer)>
readonly "allowedYRangeFromStart": $Optional<(integer)>
readonly "startHeight": $HeightProvider
readonly "projectStartToHeightmap": $Optional<($Heightmap$Types)>
readonly "cannotSpawnInLiquid": boolean
readonly "terrainHeightCheckRadius": $Optional<(integer)>
readonly "allowedTerrainHeightRange": $Optional<(integer)>
readonly "biomeRadius": $Optional<(integer)>
readonly "maxDistanceFromCenter": $Optional<(integer)>
readonly "buryingType": $Optional<($JigsawStructure$BURYING_TYPE)>
readonly "rotationFixed": boolean
readonly "enhancedTerrainAdaptation": $EnhancedTerrainAdaptation
static readonly "DIRECT_CODEC": $Codec<($Structure)>

constructor(config: $Structure$StructureSettings$Type, startPool: $Holder$Type<($StructureTemplatePool$Type)>, size: integer, minYAllowed: $Optional$Type<(integer)>, maxYAllowed: $Optional$Type<(integer)>, allowedYRangeFromStart: $Optional$Type<(integer)>, startHeight: $HeightProvider$Type, projectStartToHeightmap: $Optional$Type<($Heightmap$Types$Type)>, cannotSpawnInLiquid: boolean, terrainHeightCheckRadius: $Optional$Type<(integer)>, allowedTerrainHeightRange: $Optional$Type<(integer)>, biomeRadius: $Optional$Type<(integer)>, maxDistanceFromCenter: $Optional$Type<(integer)>, enhancedTerrainAdaptation: $EnhancedTerrainAdaptation$Type, targetBiomeRadius: integer, targetBiomes: $HolderSet$Type<($Biome$Type)>)

public "type"(): $StructureType<(any)>
public "m_214086_"(context: $Structure$GenerationContext$Type): $Optional<($Structure$GenerationStub)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeFacingStructure$Type = ($BiomeFacingStructure);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeFacingStructure_ = $BiomeFacingStructure$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/structures/action/$StructureAction" {
import {$StructureActionType, $StructureActionType$Type} from "packages/com/craisinlord/integrated_api/world/structures/action/$StructureActionType"
import {$StructureContext, $StructureContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/context/$StructureContext"
import {$PieceEntry, $PieceEntry$Type} from "packages/com/craisinlord/integrated_api/world/structures/pieces/assembler/$PieceEntry"

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
declare module "packages/com/craisinlord/integrated_villages/pooladditions/$PoolAdditionMerger" {
import {$ServerGoingToStartEvent, $ServerGoingToStartEvent$Type} from "packages/com/craisinlord/integrated_api/events/lifecycle/$ServerGoingToStartEvent"

export class $PoolAdditionMerger {


public static "mergeAdditionPools"(event: $ServerGoingToStartEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PoolAdditionMerger$Type = ($PoolAdditionMerger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PoolAdditionMerger_ = $PoolAdditionMerger$Type;
}}
declare module "packages/com/craisinlord/integrated_villages/config/$ConfigModuleForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ConfigModuleForge {

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigModuleForge$Type = ($ConfigModuleForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigModuleForge_ = $ConfigModuleForge$Type;
}}
declare module "packages/com/craisinlord/integrated_api/world/structures/pieces/assembler/$PieceEntry" {
import {$PieceContext, $PieceContext$Type} from "packages/com/craisinlord/integrated_api/world/structures/pieces/assembler/$PieceContext"
import {$JigsawJunction, $JigsawJunction$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$JigsawJunction"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$PoolElementStructurePiece, $PoolElementStructurePiece$Type} from "packages/net/minecraft/world/level/levelgen/structure/$PoolElementStructurePiece"
import {$MutableObject, $MutableObject$Type} from "packages/org/apache/commons/lang3/mutable/$MutableObject"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BoxOctree, $BoxOctree$Type} from "packages/com/craisinlord/integrated_api/utils/$BoxOctree"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export class $PieceEntry {

constructor(piece: $PoolElementStructurePiece$Type, boxOctree: $MutableObject$Type<($BoxOctree$Type)>, pieceAabb: $AABB$Type, depth: integer, parentEntry: $PieceEntry$Type, sourcePieceContext: $PieceContext$Type, parentJunction: $JigsawJunction$Type)

public "equals"(obj: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getDeadendPool"(): $Optional<($ResourceLocation)>
public "getPieceAabb"(): $AABB
public "setPiece"(newPiece: $PoolElementStructurePiece$Type): void
public "getBoxOctree"(): $MutableObject<($BoxOctree)>
public "setDelayGeneration"(delayGeneration: boolean): void
public "hasChildren"(): boolean
public "getParentEntry"(): $PieceEntry
public "getDepth"(): integer
public "getPiece"(): $PoolElementStructurePiece
public "getSourcePieceContext"(): $PieceContext
public "addChildEntry"(childEntry: $PieceEntry$Type): void
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
