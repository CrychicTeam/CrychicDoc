declare module "packages/net/cristellib/builtinpacks/$UnsafeByteArrayOutputStream" {
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"

export class $UnsafeByteArrayOutputStream extends $OutputStream implements $AutoCloseable {

constructor()
constructor(size: integer)

public "getBytes"(): (byte)[]
public "write"(b: integer): void
public "write"(b: (byte)[], off: integer, len: integer): void
public "close"(): void
get "bytes"(): (byte)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnsafeByteArrayOutputStream$Type = ($UnsafeByteArrayOutputStream);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnsafeByteArrayOutputStream_ = $UnsafeByteArrayOutputStream$Type;
}}
declare module "packages/net/cristellib/data/$ReadData" {
import {$StructureConfig, $StructureConfig$Type} from "packages/net/cristellib/$StructureConfig"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$CristelLibRegistry, $CristelLibRegistry$Type} from "packages/net/cristellib/$CristelLibRegistry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ReadData {

constructor()

public static "copyFile"(modid: string): void
public static "findInConfigFiles"(subPath: string, rootFilter: $Predicate$Type<($Path$Type)>, processor: $BiFunction$Type<($Path$Type), ($Path$Type), (boolean)>, visitAllFiles: boolean, maxDepth: integer): void
public static "copyFileFromJar"(from: string, to: string, fromModId: string): void
public static "modifyObject"(modifier: string, strings: $List$Type<($Pair$Type<(string), (string)>)>, at: string): void
public static "getPathsInDir"(modid: string, subPath: string): $List<($Path)>
public static "modifyJson5File"(modid: string): void
public static "findFiles"(rootPaths: $List$Type<($Path$Type)>, modid: string, subPath: string, rootFilter: $Predicate$Type<($Path$Type)>, processor: $BiFunction$Type<($Path$Type), ($Path$Type), (boolean)>, visitAllFiles: boolean, maxDepth: integer): void
public static "getStructureConfigs"(modid: string, modidAndConfigs: $Map$Type<(string), ($Set$Type<($StructureConfig$Type)>)>, registry: $CristelLibRegistry$Type): void
public static "getBuiltInPacks"(modid: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReadData$Type = ($ReadData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReadData_ = $ReadData$Type;
}}
declare module "packages/net/cristellib/forge/extraapiutil/$APIFinder" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$CristelLibAPI, $CristelLibAPI$Type} from "packages/net/cristellib/api/$CristelLibAPI"

export class $APIFinder {

constructor()

public static "scanForAPIs"(): $List<($Pair<($List<(string)>), ($CristelLibAPI)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $APIFinder$Type = ($APIFinder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $APIFinder_ = $APIFinder$Type;
}}
declare module "packages/net/cristellib/builtinpacks/$BuiltinResourcePackSource" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$PackSource, $PackSource$Type} from "packages/net/minecraft/server/packs/repository/$PackSource"

export class $BuiltinResourcePackSource implements $PackSource {

constructor()

public "decorate"(packName: $Component$Type): $Component
public "shouldAddAutomatically"(): boolean
public static "create"(arg0: $UnaryOperator$Type<($Component$Type)>, arg1: boolean): $PackSource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuiltinResourcePackSource$Type = ($BuiltinResourcePackSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuiltinResourcePackSource_ = $BuiltinResourcePackSource$Type;
}}
declare module "packages/net/cristellib/util/$Util" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Util {

constructor()

public static "addAll"<V, S>(addTo: $Map$Type<(V), ($Set$Type<(S)>)>, addFrom: $Map$Type<(V), ($Set$Type<(S)>)>): void
public static "getLocationForStructureSet"(location: $ResourceLocation$Type): $ResourceLocation
public static "createResourceLocation"(prefix: string, end: string, identifier: $ResourceLocation$Type): $ResourceLocation
public static "createJsonLocation"(prefix: string, identifier: $ResourceLocation$Type): $ResourceLocation
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
declare module "packages/net/cristellib/util/$JanksonUtil" {
import {$JsonObject, $JsonObject$Type} from "packages/blue/endless/jankson/$JsonObject"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"

export class $JanksonUtil {

constructor()

public static "addToObject"(jsonObject: $JsonObject$Type, path: string, toAdd: $List$Type<($Pair$Type<(string), (string)>)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JanksonUtil$Type = ($JanksonUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JanksonUtil_ = $JanksonUtil$Type;
}}
declare module "packages/net/cristellib/$CristelLibRegistry" {
import {$StructureConfig, $StructureConfig$Type} from "packages/net/cristellib/$StructureConfig"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ImmutableMap, $ImmutableMap$Type} from "packages/com/google/common/collect/$ImmutableMap"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $CristelLibRegistry {


public static "getConfigs"(): $ImmutableMap<(string), ($Set<($StructureConfig)>)>
public "registerSetToConfig"(modid: string, set: $ResourceLocation$Type, ...configs: ($StructureConfig$Type)[]): void
get "configs"(): $ImmutableMap<(string), ($Set<($StructureConfig)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CristelLibRegistry$Type = ($CristelLibRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CristelLibRegistry_ = $CristelLibRegistry$Type;
}}
declare module "packages/net/cristellib/config/$ConfigType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ConfigType extends $Enum<($ConfigType)> {
static readonly "ENABLE_DISABLE": $ConfigType
static readonly "PLACEMENT": $ConfigType


public static "values"(): ($ConfigType)[]
public static "valueOf"(name: string): $ConfigType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigType$Type = (("placement") | ("enable_disable")) | ($ConfigType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigType_ = $ConfigType$Type;
}}
declare module "packages/net/cristellib/util/$Platform" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Platform extends $Enum<($Platform)> {
static readonly "FORGE": $Platform
static readonly "FABRIC": $Platform
static readonly "QUILT": $Platform


public static "values"(): ($Platform)[]
public static "valueOf"(name: string): $Platform
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Platform$Type = (("quilt") | ("forge") | ("fabric")) | ($Platform);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Platform_ = $Platform$Type;
}}
declare module "packages/net/cristellib/forge/$CristelLibForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CristelLibForge {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CristelLibForge$Type = ($CristelLibForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CristelLibForge_ = $CristelLibForge$Type;
}}
declare module "packages/net/cristellib/forge/$ModLoadingUtilImpl" {
import {$ArtifactVersion, $ArtifactVersion$Type} from "packages/org/apache/maven/artifact/versioning/$ArtifactVersion"
import {$ModInfo, $ModInfo$Type} from "packages/net/minecraftforge/fml/loading/moddiscovery/$ModInfo"

export class $ModLoadingUtilImpl {

constructor()

public static "isModPreLoaded"(modid: string): boolean
public static "isModLoaded"(modid: string): boolean
public static "getPreLoadedModInfo"(modId: string): $ModInfo
public static "getPreLoadedModVersion"(modid: string): $ArtifactVersion
public static "isModLoadedWithVersion"(modid: string, minVersion: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModLoadingUtilImpl$Type = ($ModLoadingUtilImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModLoadingUtilImpl_ = $ModLoadingUtilImpl$Type;
}}
declare module "packages/net/cristellib/util/$TerrablenderUtil" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $TerrablenderUtil {

constructor()

public static "isModLoaded"(): boolean
public static "mixinEnabled"(): boolean
public static "setMixinEnabled"(bl: boolean): void
get "modLoaded"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TerrablenderUtil$Type = ($TerrablenderUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TerrablenderUtil_ = $TerrablenderUtil$Type;
}}
declare module "packages/net/cristellib/forge/$CristelLibExpectPlatformImpl" {
import {$StructureConfig, $StructureConfig$Type} from "packages/net/cristellib/$StructureConfig"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Platform, $Platform$Type} from "packages/net/cristellib/util/$Platform"
import {$CristelLibRegistry, $CristelLibRegistry$Type} from "packages/net/cristellib/$CristelLibRegistry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PackResources, $PackResources$Type} from "packages/net/minecraft/server/packs/$PackResources"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CristelLibExpectPlatformImpl {

constructor()

public static "data"(registry: $CristelLibRegistry$Type): $Map<(string), ($Set<($StructureConfig)>)>
public static "getModIds"(): $List<(string)>
public static "getRootPaths"(modId: string): $List<($Path)>
public static "getPlatform"(): $Platform
public static "getConfigs"(registry: $CristelLibRegistry$Type): $Map<(string), ($Set<($StructureConfig)>)>
public static "getConfigDirectory"(): $Path
public static "registerBuiltinResourcePack"(id: $ResourceLocation$Type, displayName: $Component$Type, modid: string): $PackResources
public static "getResourceDirectory"(modId: string, subPath: string): $Path
get "modIds"(): $List<(string)>
get "platform"(): $Platform
get "configDirectory"(): $Path
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CristelLibExpectPlatformImpl$Type = ($CristelLibExpectPlatformImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CristelLibExpectPlatformImpl_ = $CristelLibExpectPlatformImpl$Type;
}}
declare module "packages/net/cristellib/$StructureConfig" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ConfigType, $ConfigType$Type} from "packages/net/cristellib/config/$ConfigType"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$Placement, $Placement$Type} from "packages/net/cristellib/config/$Placement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $StructureConfig {


public static "create"(path: $Path$Type, name: string, type: $ConfigType$Type): $StructureConfig
public "getPath"(): $Path
public "getHeader"(): string
public "setComments"(comments: $HashMap$Type<(string), (string)>): void
public "getComments"(): $HashMap<(string), (string)>
public static "createWithDefaultConfigPath"(name: string, type: $ConfigType$Type): $StructureConfig
public static "createWithDefaultConfigPath"(subPath: string, name: string, type: $ConfigType$Type): $StructureConfig
public "getStructurePlacement"(): $Map<($ResourceLocation), ($Placement)>
public "setHeader"(header: string): void
public "getStructures"(): $Map<($ResourceLocation), ($List<(string)>)>
get "path"(): $Path
get "header"(): string
set "comments"(value: $HashMap$Type<(string), (string)>)
get "comments"(): $HashMap<(string), (string)>
get "structurePlacement"(): $Map<($ResourceLocation), ($Placement)>
set "header"(value: string)
get "structures"(): $Map<($ResourceLocation), ($List<(string)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureConfig$Type = ($StructureConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureConfig_ = $StructureConfig$Type;
}}
declare module "packages/net/cristellib/api/$CristelLibAPI" {
import {$StructureConfig, $StructureConfig$Type} from "packages/net/cristellib/$StructureConfig"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$CristelLibRegistry, $CristelLibRegistry$Type} from "packages/net/cristellib/$CristelLibRegistry"

export interface $CristelLibAPI {

 "registerConfigs"(sets: $Set$Type<($StructureConfig$Type)>): void
 "registerStructureSets"(registry: $CristelLibRegistry$Type): void
}

export namespace $CristelLibAPI {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CristelLibAPI$Type = ($CristelLibAPI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CristelLibAPI_ = $CristelLibAPI$Type;
}}
declare module "packages/net/cristellib/mixin/$TerrablenderPlugin" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $TerrablenderPlugin implements $IMixinConfigPlugin {

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
export type $TerrablenderPlugin$Type = ($TerrablenderPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TerrablenderPlugin_ = $TerrablenderPlugin$Type;
}}
declare module "packages/net/cristellib/$CristelLib" {
import {$RuntimePack, $RuntimePack$Type} from "packages/net/cristellib/builtinpacks/$RuntimePack"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $CristelLib {
static readonly "MOD_ID": string
static readonly "LOGGER": $Logger
static readonly "DATA_PACK": $RuntimePack
static readonly "minTerraBlenderVersion": string

constructor()

public static "init"(): void
public static "preInit"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CristelLib$Type = ($CristelLib);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CristelLib_ = $CristelLib$Type;
}}
declare module "packages/net/cristellib/builtinpacks/$RuntimePack" {
import {$PackResources$ResourceOutput, $PackResources$ResourceOutput$Type} from "packages/net/minecraft/server/packs/$PackResources$ResourceOutput"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$MetadataSectionSerializer, $MetadataSectionSerializer$Type} from "packages/net/minecraft/server/packs/metadata/$MetadataSectionSerializer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$PackType, $PackType$Type} from "packages/net/minecraft/server/packs/$PackType"
import {$ZipInputStream, $ZipInputStream$Type} from "packages/java/util/zip/$ZipInputStream"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$IoSupplier, $IoSupplier$Type} from "packages/net/minecraft/server/packs/resources/$IoSupplier"
import {$PackResources, $PackResources$Type} from "packages/net/minecraft/server/packs/$PackResources"

export class $RuntimePack implements $PackResources {
static readonly "GSON": $Gson
readonly "packVersion": integer

constructor(name: string, version: integer, imageFile: $Path$Type)

public "load"(dir: $Path$Type): void
public "load"(stream: $ZipInputStream$Type): void
public "getResource"(location: $ResourceLocation$Type): $JsonObject
public "close"(): void
public "getResource"(packType: $PackType$Type, id: $ResourceLocation$Type): $IoSupplier<($InputStream)>
public "listResources"(packType: $PackType$Type, namespace: string, prefix: string, resourceOutput: $PackResources$ResourceOutput$Type): void
public "getRootResource"(...strings: (string)[]): $IoSupplier<($InputStream)>
public "removeData"(path: $ResourceLocation$Type): void
public "addDataForJsonLocation"(prefix: string, identifier: $ResourceLocation$Type, object: $JsonObject$Type): (byte)[]
public "addAndSerializeDataForLocation"(prefix: string, end: string, identifier: $ResourceLocation$Type, object: $JsonObject$Type): (byte)[]
public "addDataForJsonLocationFromPath"(prefix: string, identifier: $ResourceLocation$Type, fromSubPath: string, fromModID: string): (byte)[]
public "addRootResource"(path: string, data: (byte)[]): (byte)[]
public static "serializeJson"(object: $JsonObject$Type): (byte)[]
public static "extractImageBytes"(imageName: $Path$Type): (byte)[]
public "addStructure"(identifier: $ResourceLocation$Type, structure: $JsonObject$Type): (byte)[]
public "addStructureSet"(identifier: $ResourceLocation$Type, set: $JsonObject$Type): (byte)[]
public "addBiome"(identifier: $ResourceLocation$Type, biome: $JsonObject$Type): (byte)[]
public "addLootTable"(identifier: $ResourceLocation$Type, table: $JsonObject$Type): (byte)[]
public "hasResource"(location: $ResourceLocation$Type): boolean
public "addData"(path: $ResourceLocation$Type, data: (byte)[]): (byte)[]
public "isBuiltin"(): boolean
public "packId"(): string
public "getMetadataSection"<T>(metadataSectionSerializer: $MetadataSectionSerializer$Type<(T)>): T
public "getNamespaces"(packType: $PackType$Type): $Set<(string)>
public "isHidden"(): boolean
public "getChildren"(): $Collection<($PackResources)>
get "builtin"(): boolean
get "hidden"(): boolean
get "children"(): $Collection<($PackResources)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RuntimePack$Type = ($RuntimePack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RuntimePack_ = $RuntimePack$Type;
}}
declare module "packages/net/cristellib/config/$ConfigUtil" {
import {$JsonGrammar$Builder, $JsonGrammar$Builder$Type} from "packages/blue/endless/jankson/$JsonGrammar$Builder"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$StructureConfig, $StructureConfig$Type} from "packages/net/cristellib/$StructureConfig"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$JsonObject, $JsonObject$Type} from "packages/blue/endless/jankson/$JsonObject"
import {$Jankson, $Jankson$Type} from "packages/blue/endless/jankson/$Jankson"
import {$Placement, $Placement$Type} from "packages/net/cristellib/config/$Placement"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$JsonGrammar, $JsonGrammar$Type} from "packages/blue/endless/jankson/$JsonGrammar"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ConfigUtil {
static readonly "CONFIG_DIR": $Path
static readonly "CONFIG_LIB": $Path
static readonly "JANKSON": $Jankson
static readonly "JSON_GRAMMAR_BUILDER": $Supplier<($JsonGrammar$Builder)>
static readonly "JSON_GRAMMAR": $JsonGrammar

constructor()

public static "getElement"(getDataFromModId: string, location: string): $JsonElement
public static "createConfig"(config: $StructureConfig$Type): void
public static "readPlacementConfig"(path: $Path$Type): $Map<(string), ($Placement)>
public static "newStringBooleanMap"(object: $JsonObject$Type): $Map<(string), (boolean)>
public static "createPlacementConfig"(config: $StructureConfig$Type): void
public static "getSetElement"(getDataFromModId: string, location: $ResourceLocation$Type): $JsonElement
public static "addComments"(comments: $Map$Type<(string), (string)>, object: $JsonObject$Type, parentKey: string): $JsonObject
public static "stringPlacementMap"(object: $JsonObject$Type): $Map<(string), ($Placement)>
public static "stringBooleanMap"(object: $JsonObject$Type, parent: string): $Map<(string), (boolean)>
public static "readConfig"(path: $Path$Type): $Map<(string), (boolean)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigUtil$Type = ($ConfigUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigUtil_ = $ConfigUtil$Type;
}}
declare module "packages/net/cristellib/config/$Placement" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Placement {
 "spacing": integer
 "separation": integer
 "frequency": double
 "salt": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Placement$Type = ($Placement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Placement_ = $Placement$Type;
}}
declare module "packages/net/cristellib/api/$BuiltInAPI" {
import {$StructureConfig, $StructureConfig$Type} from "packages/net/cristellib/$StructureConfig"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$CristelLibRegistry, $CristelLibRegistry$Type} from "packages/net/cristellib/$CristelLibRegistry"
import {$CristelLibAPI, $CristelLibAPI$Type} from "packages/net/cristellib/api/$CristelLibAPI"

export class $BuiltInAPI implements $CristelLibAPI {
static readonly "MINECRAFT_ED": $StructureConfig
static readonly "MINECRAFT_P": $StructureConfig

constructor()

public "registerConfigs"(sets: $Set$Type<($StructureConfig$Type)>): void
public "registerStructureSets"(registry: $CristelLibRegistry$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuiltInAPI$Type = ($BuiltInAPI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuiltInAPI_ = $BuiltInAPI$Type;
}}
declare module "packages/net/cristellib/data/$Conditions" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"

export class $Conditions {

constructor()

public static "readConditions"(object: $JsonObject$Type): boolean
public static "readCondition"(object: $JsonObject$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Conditions$Type = ($Conditions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Conditions_ = $Conditions$Type;
}}
declare module "packages/net/cristellib/api/$CristelPlugin" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $CristelPlugin extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $CristelPlugin {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CristelPlugin$Type = ($CristelPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CristelPlugin_ = $CristelPlugin$Type;
}}
declare module "packages/net/cristellib/$CristelLibExpectPlatform" {
import {$StructureConfig, $StructureConfig$Type} from "packages/net/cristellib/$StructureConfig"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Platform, $Platform$Type} from "packages/net/cristellib/util/$Platform"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$CristelLibRegistry, $CristelLibRegistry$Type} from "packages/net/cristellib/$CristelLibRegistry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PackResources, $PackResources$Type} from "packages/net/minecraft/server/packs/$PackResources"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CristelLibExpectPlatform {

constructor()

public static "getRootPaths"(modId: string): $List<($Path)>
public static "getPlatform"(): $Platform
public static "getConfigs"(registry: $CristelLibRegistry$Type): $Map<(string), ($Set<($StructureConfig)>)>
public static "getConfigDirectory"(): $Path
public static "registerBuiltinResourcePack"(id: $ResourceLocation$Type, displayName: $Component$Type, modid: string): $PackResources
public static "getResourceDirectory"(modid: string, subPath: string): $Path
get "platform"(): $Platform
get "configDirectory"(): $Path
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CristelLibExpectPlatform$Type = ($CristelLibExpectPlatform);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CristelLibExpectPlatform_ = $CristelLibExpectPlatform$Type;
}}
declare module "packages/net/cristellib/builtinpacks/$BuiltInDataPacks" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Pack, $Pack$Type} from "packages/net/minecraft/server/packs/repository/$Pack"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$PackResources, $PackResources$Type} from "packages/net/minecraft/server/packs/$PackResources"

export class $BuiltInDataPacks {

constructor()

public static "registerAlwaysOnPack"(path: $ResourceLocation$Type, modid: string, displayName: $Component$Type): void
public static "getPacks"(consumer: $Consumer$Type<($Pack$Type)>): void
public static "registerPack"(packResource: $PackResources$Type, displayName: $Component$Type, supplier: $Supplier$Type<(boolean)>): void
public static "registerPack"(path: $ResourceLocation$Type, modid: string, displayName: $Component$Type, supplier: $Supplier$Type<(boolean)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BuiltInDataPacks$Type = ($BuiltInDataPacks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BuiltInDataPacks_ = $BuiltInDataPacks$Type;
}}
declare module "packages/net/cristellib/$ModLoadingUtil" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ModLoadingUtil {

constructor()

public static "isModLoaded"(modid: string): boolean
public static "isModLoadedWithVersion"(modid: string, minVersion: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModLoadingUtil$Type = ($ModLoadingUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModLoadingUtil_ = $ModLoadingUtil$Type;
}}
declare module "packages/net/cristellib/forge/extrapackutil/$RepositorySourceMaker" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$RepositorySource, $RepositorySource$Type} from "packages/net/minecraft/server/packs/repository/$RepositorySource"
import {$Pack, $Pack$Type} from "packages/net/minecraft/server/packs/repository/$Pack"

export class $RepositorySourceMaker implements $RepositorySource {

constructor()

public "loadPacks"(consumer: $Consumer$Type<($Pack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RepositorySourceMaker$Type = ($RepositorySourceMaker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RepositorySourceMaker_ = $RepositorySourceMaker$Type;
}}
