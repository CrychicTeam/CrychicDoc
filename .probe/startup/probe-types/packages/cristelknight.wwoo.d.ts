declare module "packages/cristelknight/wwoo/config/configs/$ReplaceBiomesConfig" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$CommentedConfig, $CommentedConfig$Type} from "packages/cristelknight/wwoo/config/jankson/config/$CommentedConfig"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ReplaceBiomesConfig extends $Record implements $CommentedConfig<($ReplaceBiomesConfig)> {
static readonly "DEFAULT": $ReplaceBiomesConfig
static readonly "CODEC": $Codec<($ReplaceBiomesConfig)>

constructor(enableBiomes: boolean, bannedBiomes: $Map$Type<(string), (string)>)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getHeader"(): string
public "isSorted"(): boolean
public "getComments"(): $HashMap<(string), (string)>
public "enableBiomes"(): boolean
public "bannedBiomes"(): $Map<(string), (string)>
public "getCodec"(): $Codec<($ReplaceBiomesConfig)>
public "setInstance"(instance: $ReplaceBiomesConfig$Type): void
public "getSubPath"(): string
public "createConfig"(): void
public "getConfigPath"(): $Path
public "getConfig"(): $ReplaceBiomesConfig
public "getConfig"(fromFile: boolean, save: boolean): $ReplaceBiomesConfig
public "readConfig"(recreate: boolean): $ReplaceBiomesConfig
get "header"(): string
get "sorted"(): boolean
get "comments"(): $HashMap<(string), (string)>
get "codec"(): $Codec<($ReplaceBiomesConfig)>
set "instance"(value: $ReplaceBiomesConfig$Type)
get "subPath"(): string
get "configPath"(): $Path
get "config"(): $ReplaceBiomesConfig
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReplaceBiomesConfig$Type = ($ReplaceBiomesConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReplaceBiomesConfig_ = $ReplaceBiomesConfig$Type;
}}
declare module "packages/cristelknight/wwoo/utils/$Updater" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export class $Updater {

constructor(currentVersion: string)

public "checkForUpdates"(): void
public "getUpdateMessage"(): $Optional<($Component)>
public "isBig"(): boolean
public static "getReleaseTarget"(): string
get "updateMessage"(): $Optional<($Component)>
get "big"(): boolean
get "releaseTarget"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Updater$Type = ($Updater);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Updater_ = $Updater$Type;
}}
declare module "packages/cristelknight/wwoo/$ExpandedEcosphere$Mode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ExpandedEcosphere$Mode extends $Enum<($ExpandedEcosphere$Mode)> {
static readonly "COMPATIBLE": $ExpandedEcosphere$Mode
static readonly "DEFAULT": $ExpandedEcosphere$Mode


public static "values"(): ($ExpandedEcosphere$Mode)[]
public static "valueOf"(name: string): $ExpandedEcosphere$Mode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExpandedEcosphere$Mode$Type = (("compatible") | ("default")) | ($ExpandedEcosphere$Mode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExpandedEcosphere$Mode_ = $ExpandedEcosphere$Mode$Type;
}}
declare module "packages/cristelknight/wwoo/utils/$Util" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ExpandedEcosphere$Mode, $ExpandedEcosphere$Mode$Type} from "packages/cristelknight/wwoo/$ExpandedEcosphere$Mode"
import {$DynamicOps, $DynamicOps$Type} from "packages/com/mojang/serialization/$DynamicOps"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $Util {

constructor()

public static "getMode"(mode: string): $ExpandedEcosphere$Mode
public static "translatableText"(id: string): $MutableComponent
public static "getObjectFromPath"(path: $Path$Type): $JsonObject
public static "readConfig"<T>(load: $JsonElement$Type, codec: $Codec$Type<(T)>, ops: $DynamicOps$Type<($JsonElement$Type)>): T
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
declare module "packages/cristelknight/wwoo/forge/$ExpandedEcosphereForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ExpandedEcosphereForge {

constructor()

public static "isClothConfigLoaded"(): boolean
get "clothConfigLoaded"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExpandedEcosphereForge$Type = ($ExpandedEcosphereForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExpandedEcosphereForge_ = $ExpandedEcosphereForge$Type;
}}
declare module "packages/cristelknight/wwoo/$EERL" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $EERL extends $ResourceLocation {
static readonly "CODEC": $Codec<($ResourceLocation)>
static readonly "NAMESPACE_SEPARATOR": character
static readonly "DEFAULT_NAMESPACE": string
static readonly "REALMS_NAMESPACE": string

constructor(path: string)

public static "asString"(path: string): string
public static "checkSpecialEquality"(o: any, o1: any, shallow: boolean): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EERL$Type = ($EERL);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EERL_ = $EERL$Type;
}}
declare module "packages/cristelknight/wwoo/utils/$Update" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Update {
 "semanticVersion": string
 "modDownloadFA": string
 "modDownloadFO": string
 "isBig": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Update$Type = ($Update);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Update_ = $Update$Type;
}}
declare module "packages/cristelknight/wwoo/forge/$EEExpectPlatformImpl" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $EEExpectPlatformImpl {

constructor()

public static "isNewer"(oldVersion: string, newVersion: string): boolean
public static "getVersionForMod"(modId: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EEExpectPlatformImpl$Type = ($EEExpectPlatformImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EEExpectPlatformImpl_ = $EEExpectPlatformImpl$Type;
}}
declare module "packages/cristelknight/wwoo/utils/$BiomeReplace" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$JsonArray, $JsonArray$Type} from "packages/com/google/gson/$JsonArray"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $BiomeReplace {

constructor()

public static "replace"(): void
public static "replaceObject"(object: $JsonObject$Type, skipVanilla: boolean): void
public static "replaceBiomes"(biomes: $JsonArray$Type, biome: string, integers: $Set$Type<(integer)>): void
public static "addDimensionFile"(identifier: $ResourceLocation$Type, structure: $JsonObject$Type): (byte)[]
public static "getAllBiomes"(biomes: $JsonArray$Type, biome: string): $Set<(integer)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeReplace$Type = ($BiomeReplace);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeReplace_ = $BiomeReplace$Type;
}}
declare module "packages/cristelknight/wwoo/$EEExpectPlatform" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $EEExpectPlatform {

constructor()

public static "isNewer"(oldVersion: string, newVersion: string): boolean
public static "getVersionForMod"(modId: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EEExpectPlatform$Type = ($EEExpectPlatform);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EEExpectPlatform_ = $EEExpectPlatform$Type;
}}
declare module "packages/cristelknight/wwoo/$ExpandedEcosphere" {
import {$Updater, $Updater$Type} from "packages/cristelknight/wwoo/utils/$Updater"
import {$ExpandedEcosphere$Mode, $ExpandedEcosphere$Mode$Type} from "packages/cristelknight/wwoo/$ExpandedEcosphere$Mode"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $ExpandedEcosphere {
static readonly "MODID": string
static readonly "LOGGER": $Logger
static readonly "WWOOVersion": string
static "currentMode": $ExpandedEcosphere$Mode
static readonly "LINK_DC": string
static readonly "LINK_MODRINTH": string
static readonly "LINK_CF": string
static readonly "backupVersionNumber": string
static readonly "minTerraBlenderVersion": string

constructor()

public static "init"(): void
public static "getUpdater"(): $Updater
public static "isTerraBlenderLoaded"(): boolean
get "updater"(): $Updater
get "terraBlenderLoaded"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExpandedEcosphere$Type = ($ExpandedEcosphere);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExpandedEcosphere_ = $ExpandedEcosphere$Type;
}}
declare module "packages/cristelknight/wwoo/config/jankson/$Comments" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Comments {

 "getComment"(arg0: string): string
 "addComment"(arg0: string, arg1: string): void
}

export namespace $Comments {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Comments$Type = ($Comments);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Comments_ = $Comments$Type;
}}
declare module "packages/cristelknight/wwoo/config/configs/$EEConfig" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$CommentedConfig, $CommentedConfig$Type} from "packages/cristelknight/wwoo/config/jankson/config/$CommentedConfig"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $EEConfig extends $Record implements $CommentedConfig<($EEConfig)> {
static readonly "DEFAULT": $EEConfig
static readonly "CODEC": $Codec<($EEConfig)>

constructor(mode: string, forceLargeBiomes: boolean, removeOreBlobs: boolean, showUpdates: boolean, showBigUpdates: boolean, backGroundBlock: $BlockState$Type)

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getInstance"(): $EEConfig
public "mode"(): string
public "getHeader"(): string
public "isSorted"(): boolean
public "getComments"(): $HashMap<(string), (string)>
public "forceLargeBiomes"(): boolean
public "removeOreBlobs"(): boolean
public "getCodec"(): $Codec<($EEConfig)>
public "setInstance"(instance: $EEConfig$Type): void
public "showUpdates"(): boolean
public "backGroundBlock"(): $BlockState
public "getSubPath"(): string
public "showBigUpdates"(): boolean
public "createConfig"(): void
public "getConfigPath"(): $Path
public "getConfig"(): $EEConfig
public "getConfig"(fromFile: boolean, save: boolean): $EEConfig
public "readConfig"(recreate: boolean): $EEConfig
get "instance"(): $EEConfig
get "header"(): string
get "sorted"(): boolean
get "comments"(): $HashMap<(string), (string)>
get "codec"(): $Codec<($EEConfig)>
set "instance"(value: $EEConfig$Type)
get "subPath"(): string
get "configPath"(): $Path
get "config"(): $EEConfig
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EEConfig$Type = ($EEConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EEConfig_ = $EEConfig$Type;
}}
declare module "packages/cristelknight/wwoo/config/cloth/$ClothConfigScreen" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export class $ClothConfigScreen {

constructor()

public "create"(parent: $Screen$Type): $Screen
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClothConfigScreen$Type = ($ClothConfigScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClothConfigScreen_ = $ClothConfigScreen$Type;
}}
declare module "packages/cristelknight/wwoo/config/jankson/config/$CommentedConfig" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export interface $CommentedConfig<T extends $Record> {

 "getDefault"(): T
 "getInstance"(): T
 "getHeader"(): string
 "isSorted"(): boolean
 "createConfig"(): void
 "getConfigPath"(): $Path
 "getComments"(): $HashMap<(string), (string)>
 "getConfig"(): T
 "getConfig"(fromFile: boolean, save: boolean): T
 "getCodec"(): $Codec<(T)>
 "setInstance"(arg0: T): void
 "getSubPath"(): string
 "readConfig"(recreate: boolean): T
}

export namespace $CommentedConfig {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommentedConfig$Type<T> = ($CommentedConfig<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommentedConfig_<T> = $CommentedConfig$Type<(T)>;
}}
declare module "packages/cristelknight/wwoo/config/jankson/$ConfigUtil" {
import {$JsonGrammar$Builder, $JsonGrammar$Builder$Type} from "packages/blue/endless/jankson/$JsonGrammar$Builder"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$JsonObject, $JsonObject$Type} from "packages/blue/endless/jankson/$JsonObject"
import {$Jankson, $Jankson$Type} from "packages/blue/endless/jankson/$Jankson"
import {$DynamicOps, $DynamicOps$Type} from "packages/com/mojang/serialization/$DynamicOps"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$JsonGrammar, $JsonGrammar$Type} from "packages/blue/endless/jankson/$JsonGrammar"
import {$JsonElement, $JsonElement$Type} from "packages/blue/endless/jankson/$JsonElement"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ConfigUtil {
static readonly "JANKSON": $Jankson
static readonly "JSON_GRAMMAR_BUILDER": $Supplier<($JsonGrammar$Builder)>
static readonly "JSON_GRAMMAR": $JsonGrammar
static readonly "MODID": string

constructor()

public static "createConfig"<T>(path: $Path$Type, codec: $Codec$Type<(T)>, comments: $Map$Type<(string), (string)>, ops: $DynamicOps$Type<($JsonElement$Type)>, from: T, sorted: boolean, header: string): void
public static "addCommentsAndAlphabeticallySortRecursively"(comments: $Map$Type<(string), (string)>, object: $JsonObject$Type, parentKey: string, alphabeticallySorted: boolean): $JsonObject
public static "readConfig"<T>(path: $Path$Type, codec: $Codec$Type<(T)>, ops: $DynamicOps$Type<($JsonElement$Type)>): T
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
declare module "packages/cristelknight/wwoo/config/jankson/$JanksonOps" {
import {$LongStream, $LongStream$Type} from "packages/java/util/stream/$LongStream"
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"
import {$DynamicOps, $DynamicOps$Type} from "packages/com/mojang/serialization/$DynamicOps"
import {$Encoder, $Encoder$Type} from "packages/com/mojang/serialization/$Encoder"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$JsonElement, $JsonElement$Type} from "packages/blue/endless/jankson/$JsonElement"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$ListBuilder, $ListBuilder$Type} from "packages/com/mojang/serialization/$ListBuilder"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$RecordBuilder, $RecordBuilder$Type} from "packages/com/mojang/serialization/$RecordBuilder"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$DataResult, $DataResult$Type} from "packages/com/mojang/serialization/$DataResult"
import {$MapLike, $MapLike$Type} from "packages/com/mojang/serialization/$MapLike"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Decoder, $Decoder$Type} from "packages/com/mojang/serialization/$Decoder"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $JanksonOps extends $Record implements $DynamicOps<($JsonElement)> {
static readonly "INSTANCE": $JanksonOps
static readonly "COMPRESSED": $JanksonOps

constructor(compressed: boolean)

public "remove"(input: $JsonElement$Type, key: string): $JsonElement
public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getMap"(input: $JsonElement$Type): $DataResult<($MapLike<($JsonElement)>)>
public "compressed"(): boolean
public "getList"(input: $JsonElement$Type): $DataResult<($Consumer<($Consumer<($JsonElement)>)>)>
public "getBooleanValue"(input: $JsonElement$Type): $DataResult<(boolean)>
public "getStringValue"(input: $JsonElement$Type): $DataResult<(string)>
public "mapBuilder"(): $RecordBuilder<($JsonElement)>
public "compressMaps"(): boolean
public "getStream"(input: $JsonElement$Type): $DataResult<($Stream<($JsonElement)>)>
public "getNumberValue"(input: $JsonElement$Type): $DataResult<(number)>
public "mergeToMap"(map: $JsonElement$Type, key: $JsonElement$Type, value: $JsonElement$Type): $DataResult<($JsonElement)>
public "mergeToMap"(map: $JsonElement$Type, values: $MapLike$Type<($JsonElement$Type)>): $DataResult<($JsonElement)>
public "mergeToList"(list: $JsonElement$Type, values: $List$Type<($JsonElement$Type)>): $DataResult<($JsonElement)>
public "mergeToList"(list: $JsonElement$Type, value: $JsonElement$Type): $DataResult<($JsonElement)>
public "getMapValues"(input: $JsonElement$Type): $DataResult<($Stream<($Pair<($JsonElement), ($JsonElement)>)>)>
public "convertTo"<U>(outOps: $DynamicOps$Type<(U)>, input: $JsonElement$Type): U
public "getMapEntries"(input: $JsonElement$Type): $DataResult<($Consumer<($BiConsumer<($JsonElement), ($JsonElement)>)>)>
public "listBuilder"(): $ListBuilder<($JsonElement)>
public "get"(arg0: $JsonElement$Type, arg1: string): $DataResult<($JsonElement)>
public "update"(arg0: $JsonElement$Type, arg1: string, arg2: $Function$Type<($JsonElement$Type), ($JsonElement$Type)>): $JsonElement
public "set"(arg0: $JsonElement$Type, arg1: string, arg2: $JsonElement$Type): $JsonElement
public "emptyList"(): $JsonElement
public "getByteBuffer"(arg0: $JsonElement$Type): $DataResult<($ByteBuffer)>
public "createMap"(arg0: $Map$Type<($JsonElement$Type), ($JsonElement$Type)>): $JsonElement
public "emptyMap"(): $JsonElement
public "createLong"(arg0: long): $JsonElement
public "mergeToPrimitive"(arg0: $JsonElement$Type, arg1: $JsonElement$Type): $DataResult<($JsonElement)>
public "createFloat"(arg0: float): $JsonElement
public "createDouble"(arg0: double): $JsonElement
public "getNumberValue"(arg0: $JsonElement$Type, arg1: number): number
public "getGeneric"(arg0: $JsonElement$Type, arg1: $JsonElement$Type): $DataResult<($JsonElement)>
public "createInt"(arg0: integer): $JsonElement
public "createShort"(arg0: short): $JsonElement
public "createByte"(arg0: byte): $JsonElement
public "createLongList"(arg0: $LongStream$Type): $JsonElement
public "mergeToMap"(arg0: $JsonElement$Type, arg1: $Map$Type<($JsonElement$Type), ($JsonElement$Type)>): $DataResult<($JsonElement)>
public "getIntStream"(arg0: $JsonElement$Type): $DataResult<($IntStream)>
public "createIntList"(arg0: $IntStream$Type): $JsonElement
public "getLongStream"(arg0: $JsonElement$Type): $DataResult<($LongStream)>
public "createByteList"(arg0: $ByteBuffer$Type): $JsonElement
public "withDecoder"<E>(arg0: $Decoder$Type<(E)>): $Function<($JsonElement), ($DataResult<($Pair<(E), ($JsonElement)>)>)>
public "updateGeneric"(arg0: $JsonElement$Type, arg1: $JsonElement$Type, arg2: $Function$Type<($JsonElement$Type), ($JsonElement$Type)>): $JsonElement
public "convertList"<U>(arg0: $DynamicOps$Type<(U)>, arg1: $JsonElement$Type): U
public "convertMap"<U>(arg0: $DynamicOps$Type<(U)>, arg1: $JsonElement$Type): U
public "withEncoder"<E>(arg0: $Encoder$Type<(E)>): $Function<(E), ($DataResult<($JsonElement)>)>
public "withParser"<E>(arg0: $Decoder$Type<(E)>): $Function<($JsonElement), ($DataResult<(E)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JanksonOps$Type = ($JanksonOps);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JanksonOps_ = $JanksonOps$Type;
}}
declare module "packages/cristelknight/wwoo/config/jankson/$Array2" {
import {$JsonArray, $JsonArray$Type} from "packages/blue/endless/jankson/$JsonArray"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$JsonGrammar, $JsonGrammar$Type} from "packages/blue/endless/jankson/$JsonGrammar"

export class $Array2 extends $JsonArray {

constructor()

public "toJson"(grammar: $JsonGrammar$Type, depth: integer): string
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
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Array2$Type = ($Array2);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Array2_ = $Array2$Type;
}}
declare module "packages/cristelknight/wwoo/config/jankson/$CommentsMap" {
import {$DynamicOps, $DynamicOps$Type} from "packages/com/mojang/serialization/$DynamicOps"
import {$MapLike, $MapLike$Type} from "packages/com/mojang/serialization/$MapLike"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Comments, $Comments$Type} from "packages/cristelknight/wwoo/config/jankson/$Comments"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $CommentsMap<A> extends $MapLike<(A)>, $Comments {

 "get"(arg0: A): A
 "get"(arg0: string): A
 "entries"(): $Stream<($Pair<(A), (A)>)>
 "getComment"(arg0: string): string
 "addComment"(arg0: string, arg1: string): void
}

export namespace $CommentsMap {
function forMap<T>(arg0: $Map$Type<(A), (A)>, arg1: $DynamicOps$Type<(A)>): $MapLike<(A)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommentsMap$Type<A> = ($CommentsMap<(A)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommentsMap_<A> = $CommentsMap$Type<(A)>;
}}
