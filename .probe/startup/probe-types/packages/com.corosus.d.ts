declare module "packages/com/corosus/coroutil/util/$CoroUtilCompatibility" {
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $CoroUtilCompatibility {

constructor()

public static "tryPathToXYZModCompat"(arg0: $Mob$Type, arg1: integer, arg2: integer, arg3: integer, arg4: double): boolean
public static "coldEnoughToSnow"(arg0: $Biome$Type, arg1: $BlockPos$Type, arg2: $Level$Type): boolean
public static "warmEnoughToRain"(arg0: $Biome$Type, arg1: $BlockPos$Type, arg2: $Level$Type): boolean
public static "tryPathToXYZVanilla"(arg0: $Mob$Type, arg1: integer, arg2: integer, arg3: integer, arg4: double): boolean
public static "getAdjustedTemperature"(arg0: $Level$Type, arg1: $Biome$Type, arg2: $BlockPos$Type): float
public static "isSereneSeasonsInstalled"(): boolean
get "sereneSeasonsInstalled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoroUtilCompatibility$Type = ($CoroUtilCompatibility);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoroUtilCompatibility_ = $CoroUtilCompatibility$Type;
}}
declare module "packages/com/corosus/modconfig/$ConfigParams" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $ConfigParams extends $Annotation {

 "min"(): double
 "max"(): double
 "comment"(): string
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $ConfigParams {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigParams$Type = ($ConfigParams);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigParams_ = $ConfigParams$Type;
}}
declare module "packages/com/corosus/coroutil/repack/de/androidpit/colorthief/$MMCQ$CMap" {
import {$MMCQ$VBox, $MMCQ$VBox$Type} from "packages/com/corosus/coroutil/repack/de/androidpit/colorthief/$MMCQ$VBox"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"

export class $MMCQ$CMap {
readonly "vboxes": $ArrayList<($MMCQ$VBox)>

constructor()

public "size"(): integer
public "map"(arg0: (integer)[]): (integer)[]
public "push"(arg0: $MMCQ$VBox$Type): void
public "nearest"(arg0: (integer)[]): (integer)[]
public "palette"(): ((integer)[])[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MMCQ$CMap$Type = ($MMCQ$CMap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MMCQ$CMap_ = $MMCQ$CMap$Type;
}}
declare module "packages/com/corosus/coroutil/util/$ChunkCoordinatesBlock" {
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $ChunkCoordinatesBlock extends $BlockPos {
 "block": $Block
static readonly "CODEC": $Codec<($BlockPos)>
static readonly "ZERO": $BlockPos
static readonly "PACKED_Y_LENGTH": integer

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: $Block$Type)
constructor(arg0: $BlockPos$Type, arg1: $Block$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkCoordinatesBlock$Type = ($ChunkCoordinatesBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkCoordinatesBlock_ = $ChunkCoordinatesBlock$Type;
}}
declare module "packages/com/corosus/coroutil/util/$CoroUtilEntOrParticle" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export class $CoroUtilEntOrParticle {

constructor()

public static "getMotionX"(arg0: any): double
public static "setPosX"(arg0: any, arg1: double): void
public static "setPosY"(arg0: any, arg1: double): void
public static "setMotionX"(arg0: any, arg1: double): void
public static "getDistance"(arg0: any, arg1: double, arg2: double, arg3: double): double
public static "getPosY"(arg0: any): double
public static "getPosX"(arg0: any): double
public static "getWorld"(arg0: any): $Level
public static "getMotionY"(arg0: any): double
public static "setPosZ"(arg0: any, arg1: double): void
public static "getPosZ"(arg0: any): double
public static "getMotionZ"(arg0: any): double
public static "setMotionY"(arg0: any, arg1: double): void
public static "setMotionZ"(arg0: any, arg1: double): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoroUtilEntOrParticle$Type = ($CoroUtilEntOrParticle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoroUtilEntOrParticle_ = $CoroUtilEntOrParticle$Type;
}}
declare module "packages/com/corosus/modconfig/$CoroConfigRegistry" {
import {$ModConfigData, $ModConfigData$Type} from "packages/com/corosus/modconfig/$ModConfigData"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ConcurrentHashMap, $ConcurrentHashMap$Type} from "packages/java/util/concurrent/$ConcurrentHashMap"
import {$IConfigCategory, $IConfigCategory$Type} from "packages/com/corosus/modconfig/$IConfigCategory"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CoroConfigRegistry {
 "configs": $List<($ModConfigData)>
 "liveEditConfigs": $List<($ModConfigData)>
 "lookupRegistryNameToConfig": $ConcurrentHashMap<(string), ($ModConfigData)>
 "lookupFilePathToConfig": $ConcurrentHashMap<(string), ($ModConfigData)>
 "needsInitialConfigRegistration": boolean


public "getField"(arg0: string, arg1: string): any
public static "instance"(): $CoroConfigRegistry
public "getComment"(arg0: string, arg1: string): string
public "addConfigFile"(arg0: string, arg1: $IConfigCategory$Type): void
public "allModsConfigsLoadedAndRegisteredHook"(): void
public "onLoadOrReload"(arg0: string): void
public "processHashMap"(arg0: string, arg1: $Map$Type<(any), (any)>): void
public "forceLoadRuntimeSettingsFromFile"(): void
public "updateField"(arg0: string, arg1: string, arg2: any): boolean
public "forceSaveAllFilesFromRuntimeSettings"(): void
public static "dbg"(arg0: any): void
public "updateAllConfigsFromForge"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoroConfigRegistry$Type = ($CoroConfigRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoroConfigRegistry_ = $CoroConfigRegistry$Type;
}}
declare module "packages/com/corosus/modconfig/$IConfigInstance" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IConfigInstance {

 "readData"(): void
 "writeData"(): void
}

export namespace $IConfigInstance {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IConfigInstance$Type = ($IConfigInstance);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IConfigInstance_ = $IConfigInstance$Type;
}}
declare module "packages/com/corosus/coroutil/loader/forge/$ModConfigDataForge" {
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$ConfigEntryInfo, $ConfigEntryInfo$Type} from "packages/com/corosus/modconfig/$ConfigEntryInfo"
import {$ModConfigData, $ModConfigData$Type} from "packages/com/corosus/modconfig/$ModConfigData"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$IConfigCategory, $IConfigCategory$Type} from "packages/com/corosus/modconfig/$IConfigCategory"

export class $ModConfigDataForge extends $ModConfigData {
 "valsStringConfig": $HashMap<(string), ($ForgeConfigSpec$ConfigValue<(string)>)>
 "valsIntegerConfig": $HashMap<(string), ($ForgeConfigSpec$ConfigValue<(integer)>)>
 "valsDoubleConfig": $HashMap<(string), ($ForgeConfigSpec$ConfigValue<(double)>)>
 "valsBooleanConfig": $HashMap<(string), ($ForgeConfigSpec$ConfigValue<(boolean)>)>
 "configID": string
 "configClass": $Class<(any)>
 "configInstance": $IConfigCategory
 "valsString": $HashMap<(string), (string)>
 "valsInteger": $HashMap<(string), (integer)>
 "valsDouble": $HashMap<(string), (double)>
 "valsBoolean": $HashMap<(string), (boolean)>
 "configData": $List<($ConfigEntryInfo)>
 "saveFilePath": string

constructor(arg0: string, arg1: string, arg2: $Class$Type<(any)>, arg3: $IConfigCategory$Type)

public "getConfigBoolean"(arg0: string): boolean
public "getConfigString"(arg0: string): string
public "getConfigInteger"(arg0: string): integer
public "getConfigDouble"(arg0: string): double
public "writeConfigFile"(arg0: boolean): void
public "setConfig"<T>(arg0: string, arg1: T): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModConfigDataForge$Type = ($ModConfigDataForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModConfigDataForge_ = $ModConfigDataForge$Type;
}}
declare module "packages/com/corosus/modconfig/$ConfigEntryInfo" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ConfigEntryInfo {
 "index": integer
 "name": string
 "value": any
 "comment": string
 "markForUpdate": boolean

constructor(arg0: integer, arg1: string, arg2: any, arg3: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigEntryInfo$Type = ($ConfigEntryInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigEntryInfo_ = $ConfigEntryInfo$Type;
}}
declare module "packages/com/corosus/coroutil/loader/forge/$ConfigModForge" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ConfigMod, $ConfigMod$Type} from "packages/com/corosus/modconfig/$ConfigMod"

export class $ConfigModForge extends $ConfigMod {
static readonly "MODID": string

constructor()

public "getConfigPath"(): $Path
get "configPath"(): $Path
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigModForge$Type = ($ConfigModForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigModForge_ = $ConfigModForge$Type;
}}
declare module "packages/com/corosus/coroutil/util/$CoroUtilParticle" {
import {$Random, $Random$Type} from "packages/java/util/$Random"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export class $CoroUtilParticle {
static "rainPositions": ($Vec3)[]
static "maxRainDrops": integer
static "rand": $Random

constructor()

public static "getWorldParticle"(arg0: any): $Level
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoroUtilParticle$Type = ($CoroUtilParticle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoroUtilParticle_ = $CoroUtilParticle$Type;
}}
declare module "packages/com/corosus/coroutil/repack/de/androidpit/colorthief/$RGBUtil" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $RGBUtil {

constructor()

public static "packRGB"(arg0: (integer)[]): integer
public static "unpackRGB"(arg0: integer): (integer)[]
public static "packRGBArray"(arg0: ((integer)[])[]): (integer)[]
public static "unpackRGBArray"(arg0: (integer)[]): ((integer)[])[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RGBUtil$Type = ($RGBUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RGBUtil_ = $RGBUtil$Type;
}}
declare module "packages/com/corosus/modconfig/$ConfigMod" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$IConfigCategory, $IConfigCategory$Type} from "packages/com/corosus/modconfig/$IConfigCategory"

export class $ConfigMod {
static readonly "MODID": string

constructor()

public static "instance"(): $ConfigMod
public "getConfigPath"(): $Path
public static "addConfigFile"(arg0: string, arg1: $IConfigCategory$Type): void
public static "forceSaveAllFilesFromRuntimeSettings"(): void
get "configPath"(): $Path
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigMod$Type = ($ConfigMod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigMod_ = $ConfigMod$Type;
}}
declare module "packages/com/corosus/coroutil/util/$CoroUtilMisc" {
import {$Random, $Random$Type} from "packages/java/util/$Random"

export class $CoroUtilMisc {
static "random": $Random

constructor()

public static "random"(): $Random
public static "adjVal"(arg0: float, arg1: float, arg2: float): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoroUtilMisc$Type = ($CoroUtilMisc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoroUtilMisc_ = $CoroUtilMisc$Type;
}}
declare module "packages/com/corosus/mobtimizations/$Mobtimizations" {
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"
import {$BlockPathTypes, $BlockPathTypes$Type} from "packages/net/minecraft/world/level/pathfinder/$BlockPathTypes"
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"
import {$BlockPos$MutableBlockPos, $BlockPos$MutableBlockPos$Type} from "packages/net/minecraft/core/$BlockPos$MutableBlockPos"

export class $Mobtimizations {
static readonly "MODID": string
static readonly "LOGGER": $Logger
static "modActive": boolean
static "testSpawningActive": boolean

constructor()

public static "canTarget"(arg0: $Mob$Type): boolean
public static "canWander"(arg0: $Mob$Type): boolean
public static "useReducedRates"(arg0: $Mob$Type): boolean
public static "canAvoidHazards"(arg0: $Mob$Type): boolean
public static "rollPercentChance"(arg0: float): boolean
public static "getCancels"(): integer
public static "canRecomputePath"(): boolean
public static "incCancel"(): void
public static "canCrushEggs"(): boolean
public static "canVillageRaid"(): boolean
public static "getBlockPathTypeStatic"(arg0: $BlockGetter$Type, arg1: $BlockPos$MutableBlockPos$Type): $BlockPathTypes
get "cancels"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Mobtimizations$Type = ($Mobtimizations);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Mobtimizations_ = $Mobtimizations$Type;
}}
declare module "packages/com/corosus/mobtimizations/config/$ConfigFeaturesCustomization" {
import {$IConfigCategory, $IConfigCategory$Type} from "packages/com/corosus/modconfig/$IConfigCategory"

export class $ConfigFeaturesCustomization implements $IConfigCategory {
static "zombieVillageRaidPercentChance": integer
static "zombieSearchAndDestroyTurtleEggPercentChance": integer
static "mobWanderingPercentChance": integer
static "mobWanderingDelay": integer
static "mobWanderingReducedRateMultiplier": integer
static "mobEnemyTargetingReducedRatePercentChance": integer
static "playerProximityReducedRateRangeCutoff": integer
static "playerProximityReducedRatePlayerScanRate": integer

constructor()

public "getName"(): string
public "hookUpdatedValues"(): void
public "getCategory"(): string
public "getConfigFileName"(): string
public "getRegistryName"(): string
get "name"(): string
get "category"(): string
get "configFileName"(): string
get "registryName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigFeaturesCustomization$Type = ($ConfigFeaturesCustomization);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigFeaturesCustomization_ = $ConfigFeaturesCustomization$Type;
}}
declare module "packages/com/corosus/coroutil/util/$CU" {
import {$Random, $Random$Type} from "packages/java/util/$Random"

export class $CU {
static "random": $Random

constructor()

public static "rand"(): $Random
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CU$Type = ($CU);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CU_ = $CU$Type;
}}
declare module "packages/com/corosus/modconfig/$ConfigComparatorName" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ConfigEntryInfo, $ConfigEntryInfo$Type} from "packages/com/corosus/modconfig/$ConfigEntryInfo"
import {$ToIntFunction, $ToIntFunction$Type} from "packages/java/util/function/$ToIntFunction"
import {$ToLongFunction, $ToLongFunction$Type} from "packages/java/util/function/$ToLongFunction"
import {$ToDoubleFunction, $ToDoubleFunction$Type} from "packages/java/util/function/$ToDoubleFunction"

export class $ConfigComparatorName implements $Comparator<($ConfigEntryInfo)> {

constructor()

public "compare"(arg0: $ConfigEntryInfo$Type, arg1: $ConfigEntryInfo$Type): integer
public "equals"(arg0: any): boolean
public static "reverseOrder"<T extends $Comparable<(any)>>(): $Comparator<($ConfigEntryInfo)>
public static "comparing"<T, U extends $Comparable<(any)>>(arg0: $Function$Type<(any), (any)>): $Comparator<($ConfigEntryInfo)>
public static "comparing"<T, U>(arg0: $Function$Type<(any), (any)>, arg1: $Comparator$Type<(any)>): $Comparator<($ConfigEntryInfo)>
public "thenComparing"(arg0: $Comparator$Type<(any)>): $Comparator<($ConfigEntryInfo)>
public "thenComparing"<U>(arg0: $Function$Type<(any), (any)>, arg1: $Comparator$Type<(any)>): $Comparator<($ConfigEntryInfo)>
public "thenComparing"<U extends $Comparable<(any)>>(arg0: $Function$Type<(any), (any)>): $Comparator<($ConfigEntryInfo)>
public static "comparingInt"<T>(arg0: $ToIntFunction$Type<(any)>): $Comparator<($ConfigEntryInfo)>
public static "comparingLong"<T>(arg0: $ToLongFunction$Type<(any)>): $Comparator<($ConfigEntryInfo)>
public static "comparingDouble"<T>(arg0: $ToDoubleFunction$Type<(any)>): $Comparator<($ConfigEntryInfo)>
public "reversed"(): $Comparator<($ConfigEntryInfo)>
public "thenComparingInt"(arg0: $ToIntFunction$Type<(any)>): $Comparator<($ConfigEntryInfo)>
public "thenComparingLong"(arg0: $ToLongFunction$Type<(any)>): $Comparator<($ConfigEntryInfo)>
public "thenComparingDouble"(arg0: $ToDoubleFunction$Type<(any)>): $Comparator<($ConfigEntryInfo)>
public static "naturalOrder"<T extends $Comparable<(any)>>(): $Comparator<($ConfigEntryInfo)>
public static "nullsFirst"<T>(arg0: $Comparator$Type<(any)>): $Comparator<($ConfigEntryInfo)>
public static "nullsLast"<T>(arg0: $Comparator$Type<(any)>): $Comparator<($ConfigEntryInfo)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigComparatorName$Type = ($ConfigComparatorName);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigComparatorName_ = $ConfigComparatorName$Type;
}}
declare module "packages/com/corosus/coroutil/util/$CoroUtilBlock" {
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $CoroUtilBlock {

constructor()

public static "isAir"(arg0: $Block$Type): boolean
public static "blockPos"(arg0: $Vec3$Type): $BlockPos
public static "blockPos"(arg0: double, arg1: double, arg2: double): $BlockPos
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoroUtilBlock$Type = ($CoroUtilBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoroUtilBlock_ = $CoroUtilBlock$Type;
}}
declare module "packages/com/corosus/modconfig/$ConfigAddQueue" {
import {$IConfigCategory, $IConfigCategory$Type} from "packages/com/corosus/modconfig/$IConfigCategory"

export class $ConfigAddQueue {
 "modID": string
 "config": $IConfigCategory

constructor(arg0: string, arg1: $IConfigCategory$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigAddQueue$Type = ($ConfigAddQueue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigAddQueue_ = $ConfigAddQueue$Type;
}}
declare module "packages/com/corosus/coroutil/util/$CoroUtilEntity" {
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $CoroUtilEntity {

constructor()

public static "getName"(arg0: $Entity$Type): string
public static "canSee"(arg0: $Entity$Type, arg1: $BlockPos$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoroUtilEntity$Type = ($CoroUtilEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoroUtilEntity_ = $CoroUtilEntity$Type;
}}
declare module "packages/com/corosus/coroutil/util/$CoroUtilColor" {
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $CoroUtilColor {

constructor()

public static "getPixelRGBA"(arg0: $TextureAtlasSprite$Type, arg1: integer, arg2: integer, arg3: integer): integer
public static "getColors"(arg0: $TextureAtlasSprite$Type): (integer)[]
public static "getColors"(arg0: $BlockState$Type): (integer)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoroUtilColor$Type = ($CoroUtilColor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoroUtilColor_ = $CoroUtilColor$Type;
}}
declare module "packages/com/corosus/mobtimizations/$MobtimizationEntityFields" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $MobtimizationEntityFields {

 "getlastPlayerScanTime"(): long
 "setlastPlayerScanTime"(arg0: long): void
 "getlastWanderTime"(): long
 "setlastWanderTime"(arg0: long): void
 "setplayerInRange"(arg0: boolean): void
 "isplayerInRange"(): boolean
}

export namespace $MobtimizationEntityFields {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobtimizationEntityFields$Type = ($MobtimizationEntityFields);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobtimizationEntityFields_ = $MobtimizationEntityFields$Type;
}}
declare module "packages/com/corosus/coroutil/util/$CULog" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CULog {

constructor()

public static "log"(arg0: string): void
public static "err"(arg0: string): void
public static "dbg"(arg0: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CULog$Type = ($CULog);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CULog_ = $CULog$Type;
}}
declare module "packages/com/corosus/coroutil/util/$CoroUtilPath" {
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $CoroUtilPath {

constructor()

public static "tryMoveToXYZLongDist"(arg0: $Mob$Type, arg1: double, arg2: double, arg3: double, arg4: double): boolean
public static "tryMoveToEntityLivingLongDist"(arg0: $Mob$Type, arg1: $Entity$Type, arg2: double): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoroUtilPath$Type = ($CoroUtilPath);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoroUtilPath_ = $CoroUtilPath$Type;
}}
declare module "packages/com/corosus/coroutil/loader/forge/$EventHandlerForge" {
import {$RegisterCommandsEvent, $RegisterCommandsEvent$Type} from "packages/net/minecraftforge/event/$RegisterCommandsEvent"
import {$ServerAboutToStartEvent, $ServerAboutToStartEvent$Type} from "packages/net/minecraftforge/event/server/$ServerAboutToStartEvent"
import {$TickEvent$ClientTickEvent, $TickEvent$ClientTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ClientTickEvent"
import {$RegisterClientCommandsEvent, $RegisterClientCommandsEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterClientCommandsEvent"

export class $EventHandlerForge {

constructor()

public "onGameTick"(arg0: $TickEvent$ClientTickEvent$Type): void
public "registerCommands"(arg0: $RegisterCommandsEvent$Type): void
public "serverAboutToStart"(arg0: $ServerAboutToStartEvent$Type): void
public "registerCommandsClient"(arg0: $RegisterClientCommandsEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventHandlerForge$Type = ($EventHandlerForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventHandlerForge_ = $EventHandlerForge$Type;
}}
declare module "packages/com/corosus/coroutil/repack/de/androidpit/colorthief/$MMCQ$VBox" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MMCQ$VBox {

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: (integer)[])

public "toString"(): string
public "clone"(): $MMCQ$VBox
public "count"(arg0: boolean): integer
public "contains"(arg0: (integer)[]): boolean
public "avg"(arg0: boolean): (integer)[]
public "volume"(arg0: boolean): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MMCQ$VBox$Type = ($MMCQ$VBox);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MMCQ$VBox_ = $MMCQ$VBox$Type;
}}
declare module "packages/com/corosus/coroutil/util/$ReflectionHelper" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$Constructor, $Constructor$Type} from "packages/java/lang/reflect/$Constructor"
import {$Method, $Method$Type} from "packages/java/lang/reflect/$Method"

export class $ReflectionHelper {

constructor()

public static "findConstructor"<T>(arg0: $Class$Type<(T)>, ...arg1: ($Class$Type<(any)>)[]): $Constructor<(T)>
public static "findMethod"(arg0: $Class$Type<(any)>, arg1: string, ...arg2: ($Class$Type<(any)>)[]): $Method
public static "setPrivateValue"<T, E>(arg0: $Class$Type<(any)>, arg1: T, arg2: E, arg3: string): void
public static "getPrivateValue"<T, E>(arg0: $Class$Type<(any)>, arg1: E, arg2: string): T
public static "findField"<T>(arg0: $Class$Type<(any)>, arg1: string): $Field
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReflectionHelper$Type = ($ReflectionHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReflectionHelper_ = $ReflectionHelper$Type;
}}
declare module "packages/com/corosus/modconfig/$ConfigComment" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $ConfigComment extends $Annotation {

 "value"(): (string)[]
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $ConfigComment {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigComment$Type = ($ConfigComment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigComment_ = $ConfigComment$Type;
}}
declare module "packages/com/corosus/coroutil/repack/de/androidpit/colorthief/$MMCQ" {
import {$MMCQ$CMap, $MMCQ$CMap$Type} from "packages/com/corosus/coroutil/repack/de/androidpit/colorthief/$MMCQ$CMap"

export class $MMCQ {

constructor()

public static "quantize"(arg0: ((integer)[])[], arg1: integer): $MMCQ$CMap
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MMCQ$Type = ($MMCQ);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MMCQ_ = $MMCQ$Type;
}}
declare module "packages/com/corosus/modconfig/$ModConfigData" {
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$ConfigEntryInfo, $ConfigEntryInfo$Type} from "packages/com/corosus/modconfig/$ConfigEntryInfo"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IConfigCategory, $IConfigCategory$Type} from "packages/com/corosus/modconfig/$IConfigCategory"

export class $ModConfigData {
 "configID": string
 "configClass": $Class<(any)>
 "configInstance": $IConfigCategory
 "valsString": $HashMap<(string), (string)>
 "valsInteger": $HashMap<(string), (integer)>
 "valsDouble": $HashMap<(string), (double)>
 "valsBoolean": $HashMap<(string), (boolean)>
 "configData": $List<($ConfigEntryInfo)>
 "saveFilePath": string

constructor(arg0: string, arg1: string, arg2: $Class$Type<(any)>, arg3: $IConfigCategory$Type)

public "getConfigBoolean"(arg0: string): boolean
public "getConfigString"(arg0: string): string
public "getConfigInteger"(arg0: string): integer
public "getConfigDouble"(arg0: string): double
public "updateHashMaps"(): void
public "updateConfigFileWithRuntimeValues"(): void
public "updateField"(arg0: string, arg1: any): boolean
public "writeConfigFile"(arg0: boolean): void
public "initData"(): void
public "setConfig"<T>(arg0: string, arg1: T): void
public "updateConfigFieldValues"(): void
public "setFieldBasedOnType"(arg0: string, arg1: any): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModConfigData$Type = ($ModConfigData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModConfigData_ = $ModConfigData$Type;
}}
declare module "packages/com/corosus/coroutil/util/$CoroUtilAttributes" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"

export class $CoroUtilAttributes {
static readonly "SPEED_BOOST_UUID": $UUID

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoroUtilAttributes$Type = ($CoroUtilAttributes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoroUtilAttributes_ = $CoroUtilAttributes$Type;
}}
declare module "packages/com/corosus/coroutil/util/$CoroUtilPhysics" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $CoroUtilPhysics {

constructor()

public static "distBetweenPointAndLine"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): double
public static "isInConvexShape"(arg0: $Vec3$Type, arg1: $List$Type<($Vec3$Type)>): boolean
public static "distBetween"(arg0: double, arg1: double, arg2: double, arg3: double): double
public static "getDistanceToShape"(arg0: $Vec3$Type, arg1: $List$Type<($Vec3$Type)>): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoroUtilPhysics$Type = ($CoroUtilPhysics);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoroUtilPhysics_ = $CoroUtilPhysics$Type;
}}
declare module "packages/com/corosus/coroutil/util/$CoroUtilWorldTime" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export class $CoroUtilWorldTime {

constructor()

public static "getDayLength"(): integer
public static "isNight"(arg0: $Level$Type): boolean
public static "getDayFirstTick"(): integer
public static "isNightPadded"(arg0: $Level$Type, arg1: integer): boolean
public static "isNightPadded"(arg0: $Level$Type): boolean
public static "getNightFirstTick"(): integer
get "dayLength"(): integer
get "dayFirstTick"(): integer
get "nightFirstTick"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CoroUtilWorldTime$Type = ($CoroUtilWorldTime);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CoroUtilWorldTime_ = $CoroUtilWorldTime$Type;
}}
declare module "packages/com/corosus/mobtimizations/loader/forge/$MobtimizationsForge" {
import {$Mobtimizations, $Mobtimizations$Type} from "packages/com/corosus/mobtimizations/$Mobtimizations"
import {$RegisterCommandsEvent, $RegisterCommandsEvent$Type} from "packages/net/minecraftforge/event/$RegisterCommandsEvent"
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"
import {$TickEvent$LevelTickEvent, $TickEvent$LevelTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$LevelTickEvent"

export class $MobtimizationsForge extends $Mobtimizations {
static readonly "MODID": string
static readonly "LOGGER": $Logger
static "modActive": boolean
static "testSpawningActive": boolean

constructor()

public "registerCommands"(arg0: $RegisterCommandsEvent$Type): void
public "worldTick"(arg0: $TickEvent$LevelTickEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MobtimizationsForge$Type = ($MobtimizationsForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MobtimizationsForge_ = $MobtimizationsForge$Type;
}}
declare module "packages/com/corosus/coroutil/command/$CommandCoroConfig" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$ArgumentBuilder, $ArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$ArgumentBuilder"
import {$ModConfig$Type, $ModConfig$Type$Type} from "packages/net/minecraftforge/fml/config/$ModConfig$Type"

export class $CommandCoroConfig {

constructor()

public static "register"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>): void
public static "getConfigs"(): $Iterable<(string)>
public static "getCommandName"(): string
public static "argumentSet"(): $ArgumentBuilder<($CommandSourceStack), (any)>
public static "argumentGet"(): $ArgumentBuilder<($CommandSourceStack), (any)>
public static "argumentReload"(arg0: $ModConfig$Type$Type): $ArgumentBuilder<($CommandSourceStack), (any)>
public static "argumentSave"(): $ArgumentBuilder<($CommandSourceStack), (any)>
public static "fileToConfig"(arg0: string): string
public static "getConfigSettings"(arg0: string): $Iterable<(string)>
get "configs"(): $Iterable<(string)>
get "commandName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommandCoroConfig$Type = ($CommandCoroConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommandCoroConfig_ = $CommandCoroConfig$Type;
}}
declare module "packages/com/corosus/mobtimizations/config/$ConfigFeatures" {
import {$IConfigCategory, $IConfigCategory$Type} from "packages/com/corosus/modconfig/$IConfigCategory"

export class $ConfigFeatures implements $IConfigCategory {
static "optimizationZombieVillageRaid": boolean
static "optimizationMobEnemyTargeting": boolean
static "optimizationMobWandering": boolean
static "optimizationMobRepathfinding": boolean
static "optimizationZombieSearchAndDestroyTurtleEgg": boolean
static "optimizationMonsterHazardAvoidingPathfollowing": boolean
static "playerProximityReducedRate": boolean

constructor()

public "getName"(): string
public "hookUpdatedValues"(): void
public "getCategory"(): string
public "getConfigFileName"(): string
public "getRegistryName"(): string
get "name"(): string
get "category"(): string
get "configFileName"(): string
get "registryName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigFeatures$Type = ($ConfigFeatures);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigFeatures_ = $ConfigFeatures$Type;
}}
declare module "packages/com/corosus/coroutil/util/$MultiLoaderUtil" {
import {$ModConfigData, $ModConfigData$Type} from "packages/com/corosus/modconfig/$ModConfigData"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$IConfigCategory, $IConfigCategory$Type} from "packages/com/corosus/modconfig/$IConfigCategory"

export class $MultiLoaderUtil {


public static "instance"(): $MultiLoaderUtil
public "isFabric"(): boolean
public "isForge"(): boolean
public "makeLoaderSpecificConfigData"(arg0: string, arg1: string, arg2: $Class$Type<(any)>, arg3: $IConfigCategory$Type): $ModConfigData
get "fabric"(): boolean
get "forge"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultiLoaderUtil$Type = ($MultiLoaderUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultiLoaderUtil_ = $MultiLoaderUtil$Type;
}}
declare module "packages/com/corosus/coroutil/command/$CommandCoroConfigClient" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export class $CommandCoroConfigClient {

constructor()

public static "register"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>): void
public static "getCommandName"(): string
get "commandName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommandCoroConfigClient$Type = ($CommandCoroConfigClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommandCoroConfigClient_ = $CommandCoroConfigClient$Type;
}}
declare module "packages/com/corosus/coroutil/config/$ConfigCoroUtil" {
import {$IConfigCategory, $IConfigCategory$Type} from "packages/com/corosus/modconfig/$IConfigCategory"

export class $ConfigCoroUtil implements $IConfigCategory {
static "useLoggingLog": boolean
static "useLoggingDebug": boolean
static "useLoggingError": boolean

constructor()

public "getName"(): string
public "hookUpdatedValues"(): void
public "getCategory"(): string
public "getConfigFileName"(): string
public "getRegistryName"(): string
get "name"(): string
get "category"(): string
get "configFileName"(): string
get "registryName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigCoroUtil$Type = ($ConfigCoroUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigCoroUtil_ = $ConfigCoroUtil$Type;
}}
declare module "packages/com/corosus/coroutil/util/$OldUtil" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $OldUtil {

constructor()

public static "setPrivateValue"<T, E>(arg0: $Class$Type<(any)>, arg1: T, arg2: string, arg3: E): void
public static "getPrivateValue"(arg0: $Class$Type<(any)>, arg1: any, arg2: string): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OldUtil$Type = ($OldUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OldUtil_ = $OldUtil$Type;
}}
declare module "packages/com/corosus/modconfig/$IConfigCategory" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IConfigCategory {

 "getName"(): string
 "hookUpdatedValues"(): void
 "getCategory"(): string
 "getConfigFileName"(): string
 "getRegistryName"(): string
}

export namespace $IConfigCategory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IConfigCategory$Type = ($IConfigCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IConfigCategory_ = $IConfigCategory$Type;
}}
declare module "packages/com/corosus/mobtimizations/$CommandMisc" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export class $CommandMisc {

constructor()

public static "register"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>): void
public static "getCommandName"(): string
get "commandName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommandMisc$Type = ($CommandMisc);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommandMisc_ = $CommandMisc$Type;
}}
declare module "packages/com/corosus/coroutil/repack/de/androidpit/colorthief/$ColorThief" {
import {$BufferedImage, $BufferedImage$Type} from "packages/java/awt/image/$BufferedImage"
import {$MMCQ$CMap, $MMCQ$CMap$Type} from "packages/com/corosus/coroutil/repack/de/androidpit/colorthief/$MMCQ$CMap"

export class $ColorThief {

constructor()

public static "getPalette"(arg0: $BufferedImage$Type, arg1: integer, arg2: integer, arg3: boolean): ((integer)[])[]
public static "getPalette"(arg0: $BufferedImage$Type, arg1: integer): ((integer)[])[]
public static "getColor"(arg0: $BufferedImage$Type, arg1: integer, arg2: boolean): (integer)[]
public static "getColor"(arg0: $BufferedImage$Type): (integer)[]
public static "getColorMap"(arg0: $BufferedImage$Type, arg1: integer): $MMCQ$CMap
public static "getColorMap"(arg0: $BufferedImage$Type, arg1: integer, arg2: integer, arg3: boolean): $MMCQ$CMap
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorThief$Type = ($ColorThief);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorThief_ = $ColorThief$Type;
}}
