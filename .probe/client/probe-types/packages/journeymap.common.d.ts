declare module "packages/journeymap/common/nbt/cache/$CacheStorage" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"

export class $CacheStorage implements $AutoCloseable {

constructor(arg0: $Path$Type, arg1: boolean)

public "write"(arg0: $ChunkPos$Type, arg1: $CompoundTag$Type): void
public "read"(arg0: $ChunkPos$Type): $CompoundTag
public "close"(): void
public "flushWorker"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CacheStorage$Type = ($CacheStorage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CacheStorage_ = $CacheStorage$Type;
}}
declare module "packages/journeymap/common/$CommonConstants" {
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $CommonConstants {
static readonly "JOURNEYMAP_DIR": string
static readonly "LEGAL_CHARS": $Pattern
static readonly "PATTERN_WITH_UNICODE": $Pattern
static readonly "CSS_SAFE_PATTERN": $Pattern

constructor()

public static "debugOverride"(arg0: $Entity$Type): boolean
public static "isDev"(arg0: $Entity$Type): boolean
public static "getSafeString"(arg0: string, arg1: string): string
public static "getServerConfigDir"(): string
public static "getCSSSafeString"(arg0: string, arg1: string): string
get "serverConfigDir"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonConstants$Type = ($CommonConstants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonConstants_ = $CommonConstants$Type;
}}
declare module "packages/journeymap/common/migrate/$Migration" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Migration {

constructor(arg0: string)

public "performTasks"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Migration$Type = ($Migration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Migration_ = $Migration$Type;
}}
declare module "packages/journeymap/common/$JM" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $JM {
static "MOD_ID": string
static "SHORT_MOD_NAME": string
static "DISCORD_URL": string
static "DOWNLOAD_URL": string
static "VERSION_URL": string
static "WEBSITE_URL": string
static "MC_VERSION": string
static "LOADER_VERSION": string
static "LOADER_NAME": string
static "VERSION_MAJOR": string
static "VERSION_MINOR": string
static "VERSION_MICRO": string
static "VERSION_PATCH": string

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JM$Type = ($JM);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JM_ = $JM$Type;
}}
declare module "packages/journeymap/common/properties/config/$GsonHelper" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$ConfigField, $ConfigField$Type} from "packages/journeymap/common/properties/config/$ConfigField"
import {$JsonSerializationContext, $JsonSerializationContext$Type} from "packages/com/google/gson/$JsonSerializationContext"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"

export class $GsonHelper<T extends $ConfigField<(any)>> {

constructor(arg0: boolean)
constructor(arg0: boolean, arg1: boolean)

public "serializeAttributes"(arg0: $ConfigField$Type<(any)>, arg1: $Type$Type, arg2: $JsonSerializationContext$Type): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GsonHelper$Type<T> = ($GsonHelper<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GsonHelper_<T> = $GsonHelper$Type<(T)>;
}}
declare module "packages/journeymap/common/config/forge/$ForgeConfig" {
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$ForgeConfig$Server, $ForgeConfig$Server$Type} from "packages/journeymap/common/config/forge/$ForgeConfig$Server"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$AdminConfig, $AdminConfig$Type} from "packages/journeymap/common/config/$AdminConfig"

export class $ForgeConfig implements $AdminConfig {
static readonly "SERVER": $ForgeConfig$Server
static readonly "SERVER_SPEC": $ForgeConfigSpec
static "serverAdmins": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static "opAccess": $ForgeConfigSpec$ConfigValue<(boolean)>

constructor()

public "load"(): void
public "getOpAccess"(): boolean
public "getAdmins"(): $List<(string)>
get "opAccess"(): boolean
get "admins"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeConfig$Type = ($ForgeConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeConfig_ = $ForgeConfig$Type;
}}
declare module "packages/journeymap/common/network/data/$Side" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Side extends $Enum<($Side)> {
static readonly "CLIENT": $Side
static readonly "SERVER": $Side


public static "values"(): ($Side)[]
public static "valueOf"(arg0: string): $Side
public "opposite"(): $Side
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Side$Type = (("server") | ("client")) | ($Side);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Side_ = $Side$Type;
}}
declare module "packages/journeymap/common/thread/$JMThreadFactory" {
import {$Thread, $Thread$Type} from "packages/java/lang/$Thread"
import {$ThreadFactory, $ThreadFactory$Type} from "packages/java/util/concurrent/$ThreadFactory"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $JMThreadFactory implements $ThreadFactory {

constructor(arg0: string)

public "newThread"(arg0: $Runnable$Type): $Thread
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JMThreadFactory$Type = ($JMThreadFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JMThreadFactory_ = $JMThreadFactory$Type;
}}
declare module "packages/journeymap/common/network/data/$ServerPropertyType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ServerPropertyType extends $Enum<($ServerPropertyType)> {
static readonly "GLOBAL": $ServerPropertyType
static readonly "DEFAULT": $ServerPropertyType
static readonly "DIMENSION": $ServerPropertyType


public static "values"(): ($ServerPropertyType)[]
public static "valueOf"(arg0: string): $ServerPropertyType
public "getId"(): integer
public static "getFromType"(arg0: integer): $ServerPropertyType
get "id"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerPropertyType$Type = (("default") | ("global") | ("dimension")) | ($ServerPropertyType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerPropertyType_ = $ServerPropertyType$Type;
}}
declare module "packages/journeymap/common/util/$PermissionsManager" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"

export class $PermissionsManager {

constructor()

public static "getInstance"(): $PermissionsManager
public "sendPermissions"(arg0: $ServerPlayer$Type): void
public "canServerAdmin"(arg0: $ServerPlayer$Type): boolean
get "instance"(): $PermissionsManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PermissionsManager$Type = ($PermissionsManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PermissionsManager_ = $PermissionsManager$Type;
}}
declare module "packages/journeymap/common/properties/$ServerOption" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$KeyedEnum, $KeyedEnum$Type} from "packages/journeymap/client/api/option/$KeyedEnum"

export class $ServerOption extends $Enum<($ServerOption)> implements $KeyedEnum {
static readonly "ALL": $ServerOption
static readonly "OPS": $ServerOption
static readonly "NONE": $ServerOption


public static "values"(): ($ServerOption)[]
public static "valueOf"(arg0: string): $ServerOption
public "getKey"(): string
public "enabled"(): boolean
public "enabled"(arg0: boolean): $ServerOption
public "hasOption"(arg0: boolean): boolean
public "displayName"(): string
public "canOps"(): boolean
get "key"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerOption$Type = (("all") | ("ops") | ("none")) | ($ServerOption);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerOption_ = $ServerOption$Type;
}}
declare module "packages/journeymap/common/properties/$DimensionProperties" {
import {$PermissionProperties, $PermissionProperties$Type} from "packages/journeymap/common/properties/$PermissionProperties"
import {$BooleanField, $BooleanField$Type} from "packages/journeymap/common/properties/config/$BooleanField"
import {$EnumField, $EnumField$Type} from "packages/journeymap/common/properties/config/$EnumField"
import {$IntegerField, $IntegerField$Type} from "packages/journeymap/common/properties/config/$IntegerField"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$ServerOption, $ServerOption$Type} from "packages/journeymap/common/properties/$ServerOption"

export class $DimensionProperties extends $PermissionProperties {
readonly "enabled": $BooleanField
readonly "teleportEnabled": $BooleanField
readonly "renderRange": $IntegerField
readonly "surfaceMapping": $EnumField<($ServerOption)>
readonly "topoMapping": $EnumField<($ServerOption)>
readonly "biomeMapping": $EnumField<($ServerOption)>
readonly "caveMapping": $EnumField<($ServerOption)>
readonly "radarEnabled": $EnumField<($ServerOption)>
readonly "playerRadarEnabled": $BooleanField
readonly "villagerRadarEnabled": $BooleanField
readonly "animalRadarEnabled": $BooleanField
readonly "mobRadarEnabled": $BooleanField

constructor(arg0: $ResourceKey$Type<($Level$Type)>)
constructor(arg0: string)

public "getName"(): string
public "clone"(): any
public "build"(): $DimensionProperties
public "getDimension"(): $ResourceKey<($Level)>
get "name"(): string
get "dimension"(): $ResourceKey<($Level)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DimensionProperties$Type = ($DimensionProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DimensionProperties_ = $DimensionProperties$Type;
}}
declare module "packages/journeymap/common/config/$AdminConfig" {
import {$List, $List$Type} from "packages/java/util/$List"

export interface $AdminConfig {

 "load"(): void
 "getOpAccess"(): boolean
 "getAdmins"(): $List<(string)>
}

export namespace $AdminConfig {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdminConfig$Type = ($AdminConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdminConfig_ = $AdminConfig$Type;
}}
declare module "packages/journeymap/common/properties/$Permissions" {
import {$BooleanField, $BooleanField$Type} from "packages/journeymap/common/properties/config/$BooleanField"
import {$DimensionProperties, $DimensionProperties$Type} from "packages/journeymap/common/properties/$DimensionProperties"
import {$EnumField, $EnumField$Type} from "packages/journeymap/common/properties/config/$EnumField"
import {$IntegerField, $IntegerField$Type} from "packages/journeymap/common/properties/config/$IntegerField"
import {$ServerOption, $ServerOption$Type} from "packages/journeymap/common/properties/$ServerOption"

export class $Permissions extends $DimensionProperties {
readonly "enabled": $BooleanField
readonly "teleportEnabled": $BooleanField
readonly "renderRange": $IntegerField
readonly "surfaceMapping": $EnumField<($ServerOption)>
readonly "topoMapping": $EnumField<($ServerOption)>
readonly "biomeMapping": $EnumField<($ServerOption)>
readonly "caveMapping": $EnumField<($ServerOption)>
readonly "radarEnabled": $EnumField<($ServerOption)>
readonly "playerRadarEnabled": $BooleanField
readonly "villagerRadarEnabled": $BooleanField
readonly "animalRadarEnabled": $BooleanField
readonly "mobRadarEnabled": $BooleanField

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Permissions$Type = ($Permissions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Permissions_ = $Permissions$Type;
}}
declare module "packages/journeymap/common/nbt/$WorldIdData" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$SavedData, $SavedData$Type} from "packages/net/minecraft/world/level/saveddata/$SavedData"

export class $WorldIdData extends $SavedData {

constructor()

public static "getWorldId"(): string
public "save"(arg0: $CompoundTag$Type): $CompoundTag
get "worldId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldIdData$Type = ($WorldIdData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldIdData_ = $WorldIdData$Type;
}}
declare module "packages/journeymap/common/nbt/cache/$CacheFile" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$DataInputStream, $DataInputStream$Type} from "packages/java/io/$DataInputStream"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"
import {$DataOutputStream, $DataOutputStream$Type} from "packages/java/io/$DataOutputStream"
import {$RegionFileVersion, $RegionFileVersion$Type} from "packages/net/minecraft/world/level/chunk/storage/$RegionFileVersion"

export class $CacheFile implements $AutoCloseable {

constructor(arg0: $Path$Type, arg1: $Path$Type, arg2: boolean)
constructor(arg0: $Path$Type, arg1: $Path$Type, arg2: $RegionFileVersion$Type, arg3: boolean)

public "clear"(arg0: $ChunkPos$Type): void
public "flush"(): void
public "close"(): void
public "hasChunk"(arg0: $ChunkPos$Type): boolean
public "getChunkDataOutputStream"(arg0: $ChunkPos$Type): $DataOutputStream
public "getChunkDataInputStream"(arg0: $ChunkPos$Type): $DataInputStream
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CacheFile$Type = ($CacheFile);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CacheFile_ = $CacheFile$Type;
}}
declare module "packages/journeymap/common/properties/config/$CustomField" {
import {$ConfigField, $ConfigField$Type} from "packages/journeymap/common/properties/config/$ConfigField"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"

export class $CustomField extends $ConfigField<(any)> {
static readonly "ATTR_TYPE": string
static readonly "ATTR_CATEGORY": string
static readonly "ATTR_KEY": string
static readonly "ATTR_LABEL": string
static readonly "ATTR_TOOLTIP": string
static readonly "ATTR_ORDER": string
static readonly "ATTR_VALUE": string
static readonly "ATTR_DEFAULT": string
static readonly "ATTR_VALID_VALUES": string
static readonly "ATTR_PARENT": string
static readonly "ATTR_PARENT_VALUE": string

constructor(arg0: $Category$Type, arg1: string, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: boolean)
constructor(arg0: $Category$Type, arg1: string, arg2: integer, arg3: integer, arg4: integer, arg5: boolean)
constructor(arg0: $Category$Type, arg1: string, arg2: string)
constructor(arg0: $Category$Type, arg1: string)
constructor(arg0: $Category$Type, arg1: string, arg2: string, arg3: integer)

public "get"(): any
public "validate"(arg0: boolean): boolean
public "getDefaultValue"(): any
public "range"(arg0: integer, arg1: integer): $CustomField
public "getAsString"(): string
public "isNumber"(): boolean
public "getAsInteger"(): integer
public "allowNeg"(): boolean
public "getMinValue"(): integer
public "getMaxValue"(): integer
get "defaultValue"(): any
get "asString"(): string
get "number"(): boolean
get "asInteger"(): integer
get "minValue"(): integer
get "maxValue"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomField$Type = ($CustomField);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomField_ = $CustomField$Type;
}}
declare module "packages/journeymap/common/network/forge/$ForgeNetworkHandler" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$NetworkHandler, $NetworkHandler$Type} from "packages/journeymap/common/network/data/$NetworkHandler"
import {$PacketManager, $PacketManager$Type} from "packages/journeymap/common/network/packets/$PacketManager"

export class $ForgeNetworkHandler extends $PacketManager implements $NetworkHandler {

constructor()

public "sendToClient"<T>(arg0: T, arg1: $ServerPlayer$Type): void
public "sendToServer"<T>(arg0: T): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeNetworkHandler$Type = ($ForgeNetworkHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeNetworkHandler_ = $ForgeNetworkHandler$Type;
}}
declare module "packages/journeymap/common/log/$LogFormatter" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"

export class $LogFormatter {
static readonly "LINEBREAK": string

constructor()

public static "toString"(arg0: $Throwable$Type): string
public static "toPartialString"(arg0: $Throwable$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LogFormatter$Type = ($LogFormatter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LogFormatter_ = $LogFormatter$Type;
}}
declare module "packages/journeymap/common/version/$Version" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Side, $Side$Type} from "packages/journeymap/common/network/data/$Side"

export class $Version implements $Comparable<($Version)> {
readonly "major": integer
readonly "minor": integer
readonly "micro": integer
readonly "patch": string
 "loader": string
 "loaderVersion": string
 "minecraftVersion": string

constructor(arg0: integer, arg1: integer, arg2: integer)
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: string)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $Version$Type): integer
public static "from"(arg0: string, arg1: $Version$Type): $Version
public static "from"(arg0: string, arg1: string, arg2: string, arg3: string, arg4: $Version$Type): $Version
public "isValid"(arg0: $Version$Type, arg1: $Side$Type): boolean
public static "fromJson"(arg0: string): $Version
public "toJson"(): string
public "isRelease"(): boolean
public "toMajorMinorString"(): string
public "isNewerThan"(arg0: $Version$Type): boolean
public "toMajorMinorMicroString"(): string
get "release"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Version$Type = ($Version);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Version_ = $Version$Type;
}}
declare module "packages/journeymap/common/properties/$PropertiesBase" {
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$ExclusionStrategy, $ExclusionStrategy$Type} from "packages/com/google/gson/$ExclusionStrategy"
import {$File, $File$Type} from "packages/java/io/$File"
import {$ConfigField, $ConfigField$Type} from "packages/journeymap/common/properties/config/$ConfigField"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $PropertiesBase {


public "getName"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "load"<T extends $PropertiesBase>(arg0: boolean): T
public "load"<T extends $PropertiesBase>(arg0: $File$Type, arg1: boolean): T
public "load"<T extends $PropertiesBase>(): T
public "save"(): boolean
public "save"(arg0: $File$Type, arg1: boolean): boolean
public "getFile"(): $File
public "getFileName"(): string
public "lastModified"(): long
public "isValid"(arg0: boolean): boolean
public "getHeaders"(): (string)[]
public "getGson"(arg0: boolean): $Gson
public "fromJsonString"<T extends $PropertiesBase>(arg0: string, arg1: $Class$Type<(T)>, arg2: boolean): T
public "isCurrent"(): boolean
public "getCategoryByName"(arg0: string): $Category
public "toJsonString"(arg0: boolean): string
public "getConfigFields"(): $Map<(string), ($ConfigField<(any)>)>
public "updateFrom"<T extends $PropertiesBase>(arg0: T): void
public "getExclusionStrategies"(arg0: boolean): $List<($ExclusionStrategy)>
get "name"(): string
get "file"(): $File
get "fileName"(): string
get "headers"(): (string)[]
get "current"(): boolean
get "configFields"(): $Map<(string), ($ConfigField<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PropertiesBase$Type = ($PropertiesBase);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PropertiesBase_ = $PropertiesBase$Type;
}}
declare module "packages/journeymap/common/network/data/$PacketContext" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Side, $Side$Type} from "packages/journeymap/common/network/data/$Side"

export class $PacketContext<T> extends $Record {

constructor(arg0: T, arg1: $Side$Type)
constructor(sender: $ServerPlayer$Type, message: T, side: $Side$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "message"(): T
public "side"(): $Side
public "sender"(): $ServerPlayer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketContext$Type<T> = ($PacketContext<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketContext_<T> = $PacketContext$Type<(T)>;
}}
declare module "packages/journeymap/common/kotlin/extensions/$ResourcesKt" {
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ResourcesKt {


public static "getResourceAsStream"(arg0: $ResourceLocation$Type): $InputStream
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResourcesKt$Type = ($ResourcesKt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResourcesKt_ = $ResourcesKt$Type;
}}
declare module "packages/journeymap/common/properties/$DefaultDimensionProperties" {
import {$PermissionProperties, $PermissionProperties$Type} from "packages/journeymap/common/properties/$PermissionProperties"
import {$BooleanField, $BooleanField$Type} from "packages/journeymap/common/properties/config/$BooleanField"
import {$EnumField, $EnumField$Type} from "packages/journeymap/common/properties/config/$EnumField"
import {$IntegerField, $IntegerField$Type} from "packages/journeymap/common/properties/config/$IntegerField"
import {$ServerOption, $ServerOption$Type} from "packages/journeymap/common/properties/$ServerOption"

export class $DefaultDimensionProperties extends $PermissionProperties {
readonly "enabled": $BooleanField
readonly "teleportEnabled": $BooleanField
readonly "renderRange": $IntegerField
readonly "surfaceMapping": $EnumField<($ServerOption)>
readonly "topoMapping": $EnumField<($ServerOption)>
readonly "biomeMapping": $EnumField<($ServerOption)>
readonly "caveMapping": $EnumField<($ServerOption)>
readonly "radarEnabled": $EnumField<($ServerOption)>
readonly "playerRadarEnabled": $BooleanField
readonly "villagerRadarEnabled": $BooleanField
readonly "animalRadarEnabled": $BooleanField
readonly "mobRadarEnabled": $BooleanField

constructor()

public "getName"(): string
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultDimensionProperties$Type = ($DefaultDimensionProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultDimensionProperties_ = $DefaultDimensionProperties$Type;
}}
declare module "packages/journeymap/common/network/packets/$PacketManager" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PacketManager {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketManager$Type = ($PacketManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketManager_ = $PacketManager$Type;
}}
declare module "packages/journeymap/common/nbt/cache/$CacheWorker" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"

export class $CacheWorker implements $AutoCloseable {


public "load"(arg0: $ChunkPos$Type): $CompoundTag
public "store"(arg0: $ChunkPos$Type, arg1: $CompoundTag$Type): $CompletableFuture<(void)>
public "close"(): void
public "synchronize"(arg0: boolean): $CompletableFuture<(void)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CacheWorker$Type = ($CacheWorker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CacheWorker_ = $CacheWorker$Type;
}}
declare module "packages/journeymap/common/network/packets/$ServerAdminSavePropPacket" {
import {$PacketContext, $PacketContext$Type} from "packages/journeymap/common/network/data/$PacketContext"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ServerAdminSavePropPacket {
static readonly "CHANNEL": $ResourceLocation

constructor()
constructor(arg0: integer, arg1: string, arg2: string)

public static "decode"(arg0: $FriendlyByteBuf$Type): $ServerAdminSavePropPacket
public "encode"(arg0: $FriendlyByteBuf$Type): void
public "getType"(): integer
public static "handle"(arg0: $PacketContext$Type<($ServerAdminSavePropPacket$Type)>): void
public "getPayload"(): string
get "type"(): integer
get "payload"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerAdminSavePropPacket$Type = ($ServerAdminSavePropPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerAdminSavePropPacket_ = $ServerAdminSavePropPacket$Type;
}}
declare module "packages/journeymap/common/properties/$MultiplayerProperties" {
import {$BooleanField, $BooleanField$Type} from "packages/journeymap/common/properties/config/$BooleanField"
import {$ServerPropertiesBase, $ServerPropertiesBase$Type} from "packages/journeymap/common/properties/$ServerPropertiesBase"

export class $MultiplayerProperties extends $ServerPropertiesBase {
readonly "visible": $BooleanField
readonly "hideSelfUnderground": $BooleanField

constructor()

public "getName"(): string
public "save"(): boolean
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultiplayerProperties$Type = ($MultiplayerProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultiplayerProperties_ = $MultiplayerProperties$Type;
}}
declare module "packages/journeymap/common/properties/$PropertiesManager" {
import {$DimensionProperties, $DimensionProperties$Type} from "packages/journeymap/common/properties/$DimensionProperties"
import {$GlobalProperties, $GlobalProperties$Type} from "packages/journeymap/common/properties/$GlobalProperties"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$DefaultDimensionProperties, $DefaultDimensionProperties$Type} from "packages/journeymap/common/properties/$DefaultDimensionProperties"

export class $PropertiesManager {

constructor()

public static "getInstance"(): $PropertiesManager
public "reloadConfigs"(): void
public "getDimProperties"(arg0: $ResourceKey$Type<($Level$Type)>): $DimensionProperties
public "getGlobalProperties"(): $GlobalProperties
public "getDefaultDimensionProperties"(): $DefaultDimensionProperties
get "instance"(): $PropertiesManager
get "globalProperties"(): $GlobalProperties
get "defaultDimensionProperties"(): $DefaultDimensionProperties
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PropertiesManager$Type = ($PropertiesManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PropertiesManager_ = $PropertiesManager$Type;
}}
declare module "packages/journeymap/common/helper/$BiomeHelper" {
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $BiomeHelper {

constructor()

public static "getBiomeResource"(arg0: $Biome$Type): $ResourceLocation
public static "getTranslatedBiomeName"(arg0: $ResourceLocation$Type): string
public static "getTranslatedBiomeName"(arg0: $Biome$Type): string
public static "getBiomeFromResourceString"(arg0: string): $Biome
public static "getBiomeFromResource"(arg0: $ResourceLocation$Type): $Biome
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeHelper$Type = ($BiomeHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeHelper_ = $BiomeHelper$Type;
}}
declare module "packages/journeymap/common/network/packets/$ServerPlayerLocationPacket" {
import {$PlayerLocation, $PlayerLocation$Type} from "packages/journeymap/common/network/data/model/$PlayerLocation"
import {$PacketContext, $PacketContext$Type} from "packages/journeymap/common/network/data/$PacketContext"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ServerPlayerLocationPacket implements $PlayerLocation {
static readonly "CHANNEL": $ResourceLocation

constructor()
constructor(arg0: $Player$Type, arg1: boolean)

public static "decode"(arg0: $FriendlyByteBuf$Type): $ServerPlayerLocationPacket
public "encode"(arg0: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $PacketContext$Type<($ServerPlayerLocationPacket$Type)>): void
public "getY"(): double
public "getZ"(): double
public "getX"(): double
public "isVisible"(): boolean
public "getUniqueId"(): $UUID
public "getPitch"(): byte
public "getYaw"(): byte
public "getEntityId"(): integer
get "y"(): double
get "z"(): double
get "x"(): double
get "visible"(): boolean
get "uniqueId"(): $UUID
get "pitch"(): byte
get "yaw"(): byte
get "entityId"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerPlayerLocationPacket$Type = ($ServerPlayerLocationPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerPlayerLocationPacket_ = $ServerPlayerLocationPacket$Type;
}}
declare module "packages/journeymap/common/version/$VersionCheck" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $VersionCheck {

constructor()

public static "getVersionAvailable"(): string
public static "getVersionIsCurrent"(): boolean
public static "getVersionIsChecked"(): boolean
public static "getDownloadUrl"(): string
get "versionAvailable"(): string
get "versionIsCurrent"(): boolean
get "versionIsChecked"(): boolean
get "downloadUrl"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VersionCheck$Type = ($VersionCheck);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VersionCheck_ = $VersionCheck$Type;
}}
declare module "packages/journeymap/common/properties/config/$StringField" {
import {$ConfigField, $ConfigField$Type} from "packages/journeymap/common/properties/config/$ConfigField"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export class $StringField extends $ConfigField<(string)> {
static readonly "ATTR_VALUE_PROVIDER": string
static readonly "ATTR_VALUE_PATTERN": string
static readonly "ATTR_MULTILINE": string
static readonly "ATTR_TYPE": string
static readonly "ATTR_CATEGORY": string
static readonly "ATTR_KEY": string
static readonly "ATTR_LABEL": string
static readonly "ATTR_TOOLTIP": string
static readonly "ATTR_ORDER": string
static readonly "ATTR_VALUE": string
static readonly "ATTR_DEFAULT": string
static readonly "ATTR_VALID_VALUES": string
static readonly "ATTR_PARENT": string
static readonly "ATTR_PARENT_VALUE": string

constructor(arg0: $Category$Type, arg1: string, arg2: string, arg3: $Class$Type<(any)>)
constructor(arg0: $Category$Type, arg1: string, arg2: $Class$Type<(any)>)
constructor(arg0: $Category$Type, arg1: string, arg2: (string)[], arg3: string)
constructor(arg0: $Category$Type, arg1: string)

public "get"(): string
public "validate"(arg0: boolean): boolean
public "set"(arg0: string): $StringField
public "getDefaultValue"(): string
public "pattern"(arg0: string): $StringField
public "multiline"(arg0: boolean): $StringField
public "getPattern"(): string
public "isMultiline"(): boolean
public "getValuesProviderClass"(): $Class<(any)>
public "validValues"(arg0: $Iterable$Type<(string)>): $StringField
public "getValidValues"(): $List<(string)>
get "defaultValue"(): string
get "valuesProviderClass"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringField$Type = ($StringField);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringField_ = $StringField$Type;
}}
declare module "packages/journeymap/common/$LoaderHooks" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$ArrayList, $ArrayList$Type} from "packages/java/util/$ArrayList"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$URL, $URL$Type} from "packages/java/net/$URL"
import {$Location, $Location$Type} from "packages/journeymap/common/network/data/model/$Location"

export class $LoaderHooks {

constructor()

public static "getLoaderVersion"(): string
public static "getMCVersion"(): string
public static "getModNames"(): string
public static "isClient"(): boolean
public static "getMods"(): $ArrayList<(string)>
public static "isDedicatedServer"(): boolean
public static "doTeleport"(arg0: $ServerPlayer$Type, arg1: $ServerLevel$Type, arg2: $Location$Type): void
public static "getServer"(): $MinecraftServer
public static "getModFileLocation"(arg0: string): $URL
public static "isModLoaded"(arg0: string): boolean
get "loaderVersion"(): string
get "mCVersion"(): string
get "modNames"(): string
get "client"(): boolean
get "mods"(): $ArrayList<(string)>
get "dedicatedServer"(): boolean
get "server"(): $MinecraftServer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LoaderHooks$Type = ($LoaderHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LoaderHooks_ = $LoaderHooks$Type;
}}
declare module "packages/journeymap/common/events/forge/$ForgeServerEvents" {
import {$EntityLeaveLevelEvent, $EntityLeaveLevelEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityLeaveLevelEvent"
import {$RegisterCommandsEvent, $RegisterCommandsEvent$Type} from "packages/net/minecraftforge/event/$RegisterCommandsEvent"
import {$PlayerEvent$PlayerLoggedInEvent, $PlayerEvent$PlayerLoggedInEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerLoggedInEvent"
import {$TickEvent$ServerTickEvent, $TickEvent$ServerTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ServerTickEvent"
import {$EntityJoinLevelEvent, $EntityJoinLevelEvent$Type} from "packages/net/minecraftforge/event/entity/$EntityJoinLevelEvent"
import {$PermissionsChangedEvent, $PermissionsChangedEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PermissionsChangedEvent"

export class $ForgeServerEvents {

constructor()

public "onEntityJoinWorldEvent"(arg0: $EntityJoinLevelEvent$Type): void
public "onEntityLeaveWorldEvent"(arg0: $EntityLeaveLevelEvent$Type): void
public "registerCommandEvent"(arg0: $RegisterCommandsEvent$Type): void
public "onPermissionChangedEvent"(arg0: $PermissionsChangedEvent$Type): void
public "onPlayerLoggedInEvent"(arg0: $PlayerEvent$PlayerLoggedInEvent$Type): void
public "onServerTickEvent"(arg0: $TickEvent$ServerTickEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeServerEvents$Type = ($ForgeServerEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeServerEvents_ = $ForgeServerEvents$Type;
}}
declare module "packages/journeymap/common/properties/config/$FloatField" {
import {$ConfigField, $ConfigField$Type} from "packages/journeymap/common/properties/config/$ConfigField"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"

export class $FloatField extends $ConfigField<(float)> {
static readonly "ATTR_MIN": string
static readonly "ATTR_MAX": string
static readonly "ATTR_INC_VAL": string
static readonly "PRECISION": string
static readonly "ATTR_TYPE": string
static readonly "ATTR_CATEGORY": string
static readonly "ATTR_KEY": string
static readonly "ATTR_LABEL": string
static readonly "ATTR_TOOLTIP": string
static readonly "ATTR_ORDER": string
static readonly "ATTR_VALUE": string
static readonly "ATTR_DEFAULT": string
static readonly "ATTR_VALID_VALUES": string
static readonly "ATTR_PARENT": string
static readonly "ATTR_PARENT_VALUE": string

constructor(arg0: $Category$Type, arg1: string, arg2: float, arg3: float, arg4: float, arg5: integer)
constructor(arg0: $Category$Type, arg1: string, arg2: float, arg3: float, arg4: float, arg5: float, arg6: integer, arg7: integer)
constructor(arg0: $Category$Type, arg1: string, arg2: float, arg3: float, arg4: float, arg5: float, arg6: integer)
constructor(arg0: $Category$Type, arg1: string, arg2: float, arg3: float, arg4: float)

public "get"(): float
public "validate"(arg0: boolean): boolean
public "range"(arg0: float, arg1: float): $FloatField
public "getPrecision"(): integer
public "getIncrementValue"(): float
public "getMinValue"(): float
public "getMaxValue"(): float
get "precision"(): integer
get "incrementValue"(): float
get "minValue"(): float
get "maxValue"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FloatField$Type = ($FloatField);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FloatField_ = $FloatField$Type;
}}
declare module "packages/journeymap/common/properties/config/$StringField$ValuesProvider" {
import {$List, $List$Type} from "packages/java/util/$List"

export interface $StringField$ValuesProvider {

 "getStrings"(): $List<(string)>
 "getDefaultString"(): string
}

export namespace $StringField$ValuesProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringField$ValuesProvider$Type = ($StringField$ValuesProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringField$ValuesProvider_ = $StringField$ValuesProvider$Type;
}}
declare module "packages/journeymap/common/network/packets/$ClientPermissionsPacket" {
import {$PacketContext, $PacketContext$Type} from "packages/journeymap/common/network/data/$PacketContext"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ClientState, $ClientState$Type} from "packages/journeymap/common/network/data/model/$ClientState"

export class $ClientPermissionsPacket implements $ClientState {
static readonly "CHANNEL": $ResourceLocation

constructor()
constructor(arg0: string, arg1: boolean, arg2: boolean)

public static "decode"(arg0: $FriendlyByteBuf$Type): $ClientPermissionsPacket
public "encode"(arg0: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $PacketContext$Type<($ClientPermissionsPacket$Type)>): void
public "getPayload"(): string
public "hasServerMod"(): boolean
public "isServerAdmin"(): boolean
get "payload"(): string
get "serverAdmin"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientPermissionsPacket$Type = ($ClientPermissionsPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientPermissionsPacket_ = $ClientPermissionsPacket$Type;
}}
declare module "packages/journeymap/common/properties/$MultiplayerCategory" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"

export class $MultiplayerCategory {
static readonly "Multiplayer": $Category
static readonly "Radar": $Category
static readonly "values": $List<($Category)>

constructor()

public static "valueOf"(arg0: string): $Category
public static "create"(arg0: string, arg1: string, arg2: string): $Category
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultiplayerCategory$Type = ($MultiplayerCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultiplayerCategory_ = $MultiplayerCategory$Type;
}}
declare module "packages/journeymap/common/helper/$DimensionHelper" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$DimensionType, $DimensionType$Type} from "packages/net/minecraft/world/level/dimension/$DimensionType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $DimensionHelper {

constructor()

public static "getWorldKeyForName"(arg0: string): $ResourceKey<($Level)>
public static "getDimKeyName"(arg0: $Level$Type): string
public static "getDimKeyName"(arg0: $Entity$Type): string
public static "getDimKeyName"(arg0: $ResourceKey$Type<($Level$Type)>): string
public static "getServerDimNameList"(): $List<($ResourceKey<($Level)>)>
public static "getDimension"(arg0: $Entity$Type): $ResourceKey<($Level)>
public static "getDimension"(arg0: $Level$Type): $ResourceKey<($Level)>
public static "getDimName"(arg0: $Entity$Type): string
public static "getDimName"(arg0: string): string
public static "getDimName"(arg0: $Level$Type): string
public static "getDimName"(arg0: $ResourceKey$Type<($Level$Type)>): string
public static "isOverworldWorld"(arg0: $Level$Type): boolean
public static "getDimTypeForName"(arg0: string): $DimensionType
public static "getDimTypeMap"(): $Map<(string), ($DimensionType)>
public static "getSafeDimName"(arg0: $ResourceKey$Type<($Level$Type)>): string
public static "getDimTypeForKey"(arg0: $ResourceKey$Type<($Level$Type)>): $DimensionType
public static "getDimResource"(arg0: string): $ResourceLocation
public static "isNetherWorld"(arg0: $Level$Type): boolean
public static "isEndWorld"(arg0: $Level$Type): boolean
public static "getClientDimList"(): $Set<($ResourceKey<($Level)>)>
get "serverDimNameList"(): $List<($ResourceKey<($Level)>)>
get "dimTypeMap"(): $Map<(string), ($DimensionType)>
get "clientDimList"(): $Set<($ResourceKey<($Level)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DimensionHelper$Type = ($DimensionHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DimensionHelper_ = $DimensionHelper$Type;
}}
declare module "packages/journeymap/common/properties/config/$IntegerField" {
import {$ConfigField, $ConfigField$Type} from "packages/journeymap/common/properties/config/$ConfigField"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"

export class $IntegerField extends $ConfigField<(integer)> {
static readonly "ATTR_MIN": string
static readonly "ATTR_MAX": string
static readonly "ATTR_TYPE": string
static readonly "ATTR_CATEGORY": string
static readonly "ATTR_KEY": string
static readonly "ATTR_LABEL": string
static readonly "ATTR_TOOLTIP": string
static readonly "ATTR_ORDER": string
static readonly "ATTR_VALUE": string
static readonly "ATTR_DEFAULT": string
static readonly "ATTR_VALID_VALUES": string
static readonly "ATTR_PARENT": string
static readonly "ATTR_PARENT_VALUE": string

constructor(arg0: $Category$Type, arg1: string, arg2: integer, arg3: integer, arg4: integer, arg5: integer)
constructor(arg0: $Category$Type, arg1: string, arg2: integer, arg3: integer, arg4: integer)

public "get"(): integer
public "validate"(arg0: boolean): boolean
public "range"(arg0: integer, arg1: integer): $IntegerField
public "incrementAndGet"(): integer
public "decrementAndGet"(): integer
public "getMinValue"(): integer
public "getMaxValue"(): integer
get "minValue"(): integer
get "maxValue"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntegerField$Type = ($IntegerField);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntegerField_ = $IntegerField$Type;
}}
declare module "packages/journeymap/common/network/handler/$PacketHandler" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Location, $Location$Type} from "packages/journeymap/common/network/data/model/$Location"

export class $PacketHandler {

constructor()

public "onServerAdminSave"(arg0: $ServerPlayer$Type, arg1: integer, arg2: string, arg3: string): void
public "onAdminScreenOpen"(arg0: $ServerPlayer$Type, arg1: integer, arg2: string): void
public "onWorldIdRequest"(arg0: $ServerPlayer$Type): void
public "onClientPermsRequest"(arg0: $ServerPlayer$Type): void
public "handleTeleportPacket"(arg0: $ServerPlayer$Type, arg1: $Location$Type): void
public "onMultiplayerOptionsSave"(arg0: $ServerPlayer$Type, arg1: string): void
public "onMultiplayerOptionsOpen"(arg0: $ServerPlayer$Type): void
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
declare module "packages/journeymap/common/network/packets/$TeleportPacket" {
import {$PacketContext, $PacketContext$Type} from "packages/journeymap/common/network/data/$PacketContext"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Location, $Location$Type} from "packages/journeymap/common/network/data/model/$Location"

export class $TeleportPacket implements $Location {
static readonly "CHANNEL": $ResourceLocation

constructor()
constructor(arg0: double, arg1: double, arg2: double, arg3: string)

public static "decode"(arg0: $FriendlyByteBuf$Type): $TeleportPacket
public "encode"(arg0: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $PacketContext$Type<($TeleportPacket$Type)>): void
public "getY"(): double
public "getZ"(): double
public "getX"(): double
public "getDim"(): string
get "y"(): double
get "z"(): double
get "x"(): double
get "dim"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TeleportPacket$Type = ($TeleportPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TeleportPacket_ = $TeleportPacket$Type;
}}
declare module "packages/journeymap/common/network/data/$PacketContainer" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$PacketContext, $PacketContext$Type} from "packages/journeymap/common/network/data/$PacketContext"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $PacketContainer<T> extends $Record {

constructor(packetIdentifier: $ResourceLocation$Type, messageType: $Class$Type<(T)>, encoder: $BiConsumer$Type<(T), ($FriendlyByteBuf$Type)>, decoder: $Function$Type<($FriendlyByteBuf$Type), (T)>, handler: $Consumer$Type<($PacketContext$Type<(T)>)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "handler"(): $Consumer<($PacketContext<(T)>)>
public "encoder"(): $BiConsumer<(T), ($FriendlyByteBuf)>
public "decoder"(): $Function<($FriendlyByteBuf), (T)>
public "packetIdentifier"(): $ResourceLocation
public "messageType"(): $Class<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PacketContainer$Type<T> = ($PacketContainer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PacketContainer_<T> = $PacketContainer$Type<(T)>;
}}
declare module "packages/journeymap/common/properties/$ServerCategory" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"

export class $ServerCategory {
static readonly "Global": $Category
static readonly "Default": $Category
static readonly "Dimension": $Category
static readonly "values": $List<($Category)>

constructor()

public static "valueOf"(arg0: string): $Category
public static "create"(arg0: string, arg1: string, arg2: string): $Category
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerCategory$Type = ($ServerCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerCategory_ = $ServerCategory$Type;
}}
declare module "packages/journeymap/common/network/packets/$WorldIdPacket" {
import {$PacketContext, $PacketContext$Type} from "packages/journeymap/common/network/data/$PacketContext"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $WorldIdPacket {
static readonly "CHANNEL": $ResourceLocation

constructor()
constructor(arg0: string)

public static "decode"(arg0: $FriendlyByteBuf$Type): $WorldIdPacket
public "encode"(arg0: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $PacketContext$Type<($WorldIdPacket$Type)>): void
public "getWorldId"(): string
get "worldId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldIdPacket$Type = ($WorldIdPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldIdPacket_ = $WorldIdPacket$Type;
}}
declare module "packages/journeymap/common/accessors/$ScreenAccess" {
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"

export interface $ScreenAccess {

 "getRenderables"(): $List<($Renderable)>

(): $List<($Renderable)>
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
declare module "packages/journeymap/common/network/data/$NetworkHandler" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"

export interface $NetworkHandler {

 "sendToClient"<T>(arg0: T, arg1: $ServerPlayer$Type): void
 "sendToServer"<T>(arg0: T): void
}

export namespace $NetworkHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkHandler$Type = ($NetworkHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkHandler_ = $NetworkHandler$Type;
}}
declare module "packages/journeymap/common/nbt/$PlayerData" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$SavedData, $SavedData$Type} from "packages/net/minecraft/world/level/saveddata/$SavedData"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$PlayerData$Player, $PlayerData$Player$Type} from "packages/journeymap/common/nbt/$PlayerData$Player"

export class $PlayerData extends $SavedData {

constructor()

public static "getPlayerData"(): $PlayerData
public "getPlayer"(arg0: $ServerPlayer$Type): $PlayerData$Player
public "save"(arg0: $CompoundTag$Type): $CompoundTag
get "playerData"(): $PlayerData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerData$Type = ($PlayerData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerData_ = $PlayerData$Type;
}}
declare module "packages/journeymap/common/network/packets/$ServerAdminRequestPropPacket" {
import {$PacketContext, $PacketContext$Type} from "packages/journeymap/common/network/data/$PacketContext"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ServerAdminRequestPropPacket {
static readonly "CHANNEL": $ResourceLocation

constructor(arg0: integer, arg1: string)
constructor(arg0: integer, arg1: string, arg2: string)
constructor()

public static "decode"(arg0: $FriendlyByteBuf$Type): $ServerAdminRequestPropPacket
public "encode"(arg0: $FriendlyByteBuf$Type): void
public "getType"(): integer
public static "handle"(arg0: $PacketContext$Type<($ServerAdminRequestPropPacket$Type)>): void
public "getPayload"(): string
get "type"(): integer
get "payload"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerAdminRequestPropPacket$Type = ($ServerAdminRequestPropPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerAdminRequestPropPacket_ = $ServerAdminRequestPropPacket$Type;
}}
declare module "packages/journeymap/common/network/packets/$MultiplayerOptionsPacket" {
import {$PacketContext, $PacketContext$Type} from "packages/journeymap/common/network/data/$PacketContext"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $MultiplayerOptionsPacket {
static readonly "CHANNEL": $ResourceLocation

constructor()
constructor(arg0: string)

public static "decode"(arg0: $FriendlyByteBuf$Type): $MultiplayerOptionsPacket
public "encode"(arg0: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $PacketContext$Type<($MultiplayerOptionsPacket$Type)>): void
public "getPayload"(): string
get "payload"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultiplayerOptionsPacket$Type = ($MultiplayerOptionsPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultiplayerOptionsPacket_ = $MultiplayerOptionsPacket$Type;
}}
declare module "packages/journeymap/common/properties/$ServerPropertiesBase" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$ExclusionStrategy, $ExclusionStrategy$Type} from "packages/com/google/gson/$ExclusionStrategy"
import {$File, $File$Type} from "packages/java/io/$File"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"
import {$PropertiesBase, $PropertiesBase$Type} from "packages/journeymap/common/properties/$PropertiesBase"

export class $ServerPropertiesBase extends $PropertiesBase implements $Cloneable, $Serializable {


public "clone"(): any
public "load"<T extends $PropertiesBase>(arg0: string, arg1: boolean): T
public "save"(): boolean
public "getFile"(): $File
public "getFileName"(): string
public "isValid"(arg0: boolean): boolean
public "getHeaders"(): (string)[]
public "getCategoryByName"(arg0: string): $Category
public "updateFrom"<T extends $PropertiesBase>(arg0: T): void
public "getExclusionStrategies"(arg0: boolean): $List<($ExclusionStrategy)>
public "loadForClient"<T extends $PropertiesBase>(arg0: string, arg1: boolean): T
get "file"(): $File
get "fileName"(): string
get "headers"(): (string)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerPropertiesBase$Type = ($ServerPropertiesBase);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerPropertiesBase_ = $ServerPropertiesBase$Type;
}}
declare module "packages/journeymap/common/config/forge/$ForgeConfig$Server" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"

export class $ForgeConfig$Server {
readonly "serverAdmins": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
readonly "opAccess": $ForgeConfigSpec$ConfigValue<(boolean)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeConfig$Server$Type = ($ForgeConfig$Server);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeConfig$Server_ = $ForgeConfig$Server$Type;
}}
declare module "packages/journeymap/common/properties/catagory/$CategorySet" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"
import {$TreeSet, $TreeSet$Type} from "packages/java/util/$TreeSet"

export class $CategorySet extends $TreeSet<($Category)> {

constructor()

public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $Set<(E)>
public "toArray"<T>(arg0: (T)[]): (T)[]
public "toArray"(): (any)[]
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E): $Set<(E)>
public static "of"<E>(arg0: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E, arg9: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E, arg8: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E, arg7: E): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E, arg3: E, arg4: E, arg5: E, arg6: E): $Set<(E)>
public static "of"<E>(...arg0: (E)[]): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E, arg2: E): $Set<(E)>
public static "of"<E>(): $Set<(E)>
public static "of"<E>(arg0: E, arg1: E): $Set<(E)>
public "retainAll"(arg0: $Collection$Type<(any)>): boolean
public "containsAll"(arg0: $Collection$Type<(any)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CategorySet$Type = ($CategorySet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CategorySet_ = $CategorySet$Type;
}}
declare module "packages/journeymap/common/command/$CreateWaypoint" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export class $CreateWaypoint {

constructor()

public static "register"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CreateWaypoint$Type = ($CreateWaypoint);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CreateWaypoint_ = $CreateWaypoint$Type;
}}
declare module "packages/journeymap/common/util/$JourneyMapTeleport" {
import {$Location, $Location$Type} from "packages/journeymap/common/network/data/model/$Location"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $JourneyMapTeleport {


public static "instance"(): $JourneyMapTeleport
public "attemptTeleport"(arg0: $Entity$Type, arg1: $Location$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JourneyMapTeleport$Type = ($JourneyMapTeleport);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JourneyMapTeleport_ = $JourneyMapTeleport$Type;
}}
declare module "packages/journeymap/common/properties/config/$EnumField" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ConfigField, $ConfigField$Type} from "packages/journeymap/common/properties/config/$ConfigField"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"

export class $EnumField<E extends $Enum<(any)>> extends $ConfigField<(E)> {
static readonly "ATTR_ENUM_TYPE": string
static readonly "ATTR_TYPE": string
static readonly "ATTR_CATEGORY": string
static readonly "ATTR_KEY": string
static readonly "ATTR_LABEL": string
static readonly "ATTR_TOOLTIP": string
static readonly "ATTR_ORDER": string
static readonly "ATTR_VALUE": string
static readonly "ATTR_DEFAULT": string
static readonly "ATTR_VALID_VALUES": string
static readonly "ATTR_PARENT": string
static readonly "ATTR_PARENT_VALUE": string

constructor(arg0: $Category$Type, arg1: string, arg2: E, arg3: integer)
constructor(arg0: $Category$Type, arg1: string, arg2: E)

public "validate"(arg0: boolean): boolean
public "set"(arg0: E): $EnumField<(E)>
public "getDefaultValue"(): E
public "setParent"(arg0: string, arg1: any): $EnumField<(E)>
public "getEnumClass"(): $Class<(E)>
public "getValidValues"(): $Set<(E)>
get "defaultValue"(): E
get "enumClass"(): $Class<(E)>
get "validValues"(): $Set<(E)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumField$Type<E> = ($EnumField<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumField_<E> = $EnumField$Type<(E)>;
}}
declare module "packages/journeymap/common/nbt/$RegionDataStorageHandler$Key" {
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$RegionCoord, $RegionCoord$Type} from "packages/journeymap/client/model/$RegionCoord"

export class $RegionDataStorageHandler$Key {

constructor(arg0: $RegionCoord$Type, arg1: $MapType$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegionDataStorageHandler$Key$Type = ($RegionDataStorageHandler$Key);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegionDataStorageHandler$Key_ = $RegionDataStorageHandler$Key$Type;
}}
declare module "packages/journeymap/common/network/dispatch/$NetworkDispatcher" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$NetworkHandler, $NetworkHandler$Type} from "packages/journeymap/common/network/data/$NetworkHandler"

export class $NetworkDispatcher {

constructor(arg0: $NetworkHandler$Type)

public "sendWaypointPacket"(arg0: $ServerPlayer$Type, arg1: string, arg2: boolean, arg3: string): void
public "sendWorldIdPacket"(arg0: $ServerPlayer$Type, arg1: string): void
public "sendPlayerLocationPacket"(arg0: $ServerPlayer$Type, arg1: $ServerPlayer$Type, arg2: boolean): void
public "sendServerAdminPacket"(arg0: $ServerPlayer$Type, arg1: integer, arg2: string, arg3: string): void
public "sendMultiplayerOptionsPacket"(arg0: $ServerPlayer$Type, arg1: string): void
public "sendClientPermissions"(arg0: $ServerPlayer$Type, arg1: string, arg2: boolean): void
public "sendHandshakePacket"(arg0: $ServerPlayer$Type, arg1: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkDispatcher$Type = ($NetworkDispatcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkDispatcher_ = $NetworkDispatcher$Type;
}}
declare module "packages/journeymap/common/nbt/cache/$CacheFileStorage" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$AutoCloseable, $AutoCloseable$Type} from "packages/java/lang/$AutoCloseable"

export class $CacheFileStorage implements $AutoCloseable {
static readonly "EXTENSION": string


public "flush"(): void
public "read"(arg0: $ChunkPos$Type): $CompoundTag
public "close"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CacheFileStorage$Type = ($CacheFileStorage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CacheFileStorage_ = $CacheFileStorage$Type;
}}
declare module "packages/journeymap/common/config/$AdminAccessConfig" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$AdminConfig, $AdminConfig$Type} from "packages/journeymap/common/config/$AdminConfig"

export class $AdminAccessConfig implements $AdminConfig {

constructor()

public "load"(arg0: $AdminConfig$Type): void
public "load"(): void
public static "getInstance"(): $AdminAccessConfig
public "getOpAccess"(): boolean
public "getAdmins"(): $List<(string)>
get "instance"(): $AdminAccessConfig
get "opAccess"(): boolean
get "admins"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdminAccessConfig$Type = ($AdminAccessConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdminAccessConfig_ = $AdminAccessConfig$Type;
}}
declare module "packages/journeymap/common/network/handler/$ClientPacketHandler" {
import {$PlayerLocation, $PlayerLocation$Type} from "packages/journeymap/common/network/data/model/$PlayerLocation"
import {$ClientState, $ClientState$Type} from "packages/journeymap/common/network/data/model/$ClientState"

export class $ClientPacketHandler {

constructor()

public "onWorldIdReceived"(arg0: string): void
public "onPlayerLocationPacket"(arg0: $PlayerLocation$Type): void
public "onMultiplayerDataResponse"(arg0: string): void
public "onWaypointCreatePacket"(arg0: string, arg1: string, arg2: boolean): void
public "onServerAdminDataResponse"(arg0: integer, arg1: string, arg2: string): void
public "onClientStateUpdate"(arg0: $ClientState$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientPacketHandler$Type = ($ClientPacketHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientPacketHandler_ = $ClientPacketHandler$Type;
}}
declare module "packages/journeymap/common/nbt/$RegionDataStorageHandler" {
import {$RegionDataStorageHandler$Key, $RegionDataStorageHandler$Key$Type} from "packages/journeymap/common/nbt/$RegionDataStorageHandler$Key"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$RegionCoord, $RegionCoord$Type} from "packages/journeymap/client/model/$RegionCoord"
import {$RegionData, $RegionData$Type} from "packages/journeymap/common/nbt/$RegionData"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $RegionDataStorageHandler {

constructor()

public static "getInstance"(): $RegionDataStorageHandler
public "getRegionData"(arg0: $RegionDataStorageHandler$Key$Type): $RegionData
public "flushDataCache"(): void
public "deleteCache"(): void
public "getRegionDataAsyncNoCache"(arg0: $RegionCoord$Type, arg1: $MapType$Type): $RegionData
public "getRegionDataAsyncNoCache"(arg0: $BlockPos$Type, arg1: $MapType$Type): $RegionData
get "instance"(): $RegionDataStorageHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegionDataStorageHandler$Type = ($RegionDataStorageHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegionDataStorageHandler_ = $RegionDataStorageHandler$Type;
}}
declare module "packages/journeymap/common/kotlin/constants/$JM" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $JM {
static readonly "INSTANCE": $JM
static readonly "MOD_ID": string
static readonly "SHORT_MOD_NAME": string
static readonly "DISCORD_URL": string
static readonly "DOWNLOAD_URL": string
static readonly "VERSION_URL": string
static readonly "WEBSITE_URL": string
static readonly "MC_VERSION": string
static readonly "FORGE_VERSION": string
static readonly "VERSION_MAJOR": string
static readonly "VERSION_MINOR": string
static readonly "VERSION_MICRO": string
static readonly "VERSION_PATCH": string


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JM$Type = ($JM);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JM_ = $JM$Type;
}}
declare module "packages/journeymap/common/nbt/$PlayerData$Player" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$PlayerData, $PlayerData$Type} from "packages/journeymap/common/nbt/$PlayerData"

export class $PlayerData$Player {

constructor(arg0: $PlayerData$Type, arg1: string, arg2: $CompoundTag$Type)

public "setVisible"(arg0: boolean): void
public "isVisible"(): boolean
public "setHiddenUnderground"(arg0: boolean): void
public "isHiddenUnderground"(): boolean
set "visible"(value: boolean)
get "visible"(): boolean
set "hiddenUnderground"(value: boolean)
get "hiddenUnderground"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerData$Player$Type = ($PlayerData$Player);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerData$Player_ = $PlayerData$Player$Type;
}}
declare module "packages/journeymap/common/$Journeymap" {
import {$Version, $Version$Type} from "packages/journeymap/common/version/$Version"
import {$ModConfigEvent, $ModConfigEvent$Type} from "packages/net/minecraftforge/fml/event/config/$ModConfigEvent"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$NetworkDispatcher, $NetworkDispatcher$Type} from "packages/journeymap/common/network/dispatch/$NetworkDispatcher"
import {$PacketHandler, $PacketHandler$Type} from "packages/journeymap/common/network/handler/$PacketHandler"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"
import {$NetworkHandler, $NetworkHandler$Type} from "packages/journeymap/common/network/data/$NetworkHandler"
import {$ServerStartingEvent, $ServerStartingEvent$Type} from "packages/net/minecraftforge/event/server/$ServerStartingEvent"

export class $Journeymap {
static readonly "MINIMUM_SERVER_ACCEPTABLE_VERSION": $Version
static readonly "MINIMUM_CLIENT_ACCEPTABLE_VERSION": $Version
static readonly "DEV_VERSION": $Version
static readonly "MOD_ID": string
static readonly "SHORT_MOD_NAME": string
static "DEV_MODE": boolean
static readonly "JM_VERSION": $Version
static readonly "LOADER_VERSION": string
static readonly "LOADER_NAME": string
static readonly "MC_VERSION": string
static readonly "WEBSITE_URL": string
static readonly "DOWNLOAD_URL": string
static readonly "VERSION_URL": string

constructor()

public static "getInstance"(): $Journeymap
public static "getLogger"(): $Logger
public static "getLogger"(arg0: string): $Logger
public "getDispatcher"(): $NetworkDispatcher
public "serverConfig"(arg0: $ModConfigEvent$Type): void
public "commonSetupEvent"(arg0: $FMLCommonSetupEvent$Type): void
public static "isOp"(arg0: $Player$Type): boolean
public "getPacketHandler"(): $PacketHandler
public "getNetworkHandler"(): $NetworkHandler
public "getServer"(): $MinecraftServer
public "serverStartingEvent"(arg0: $ServerStartingEvent$Type): void
get "instance"(): $Journeymap
get "logger"(): $Logger
get "dispatcher"(): $NetworkDispatcher
get "packetHandler"(): $PacketHandler
get "networkHandler"(): $NetworkHandler
get "server"(): $MinecraftServer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Journeymap$Type = ($Journeymap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Journeymap_ = $Journeymap$Type;
}}
declare module "packages/journeymap/common/network/data/model/$ClientState" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ClientState {

 "getPayload"(): string
 "hasServerMod"(): boolean
 "isServerAdmin"(): boolean
}

export namespace $ClientState {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientState$Type = ($ClientState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientState_ = $ClientState$Type;
}}
declare module "packages/journeymap/common/properties/config/$BooleanField" {
import {$ConfigField, $ConfigField$Type} from "packages/journeymap/common/properties/config/$ConfigField"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"

export class $BooleanField extends $ConfigField<(boolean)> {
static readonly "ATTR_CATEGORY_MASTER": string
static readonly "ATTR_TYPE": string
static readonly "ATTR_CATEGORY": string
static readonly "ATTR_KEY": string
static readonly "ATTR_LABEL": string
static readonly "ATTR_TOOLTIP": string
static readonly "ATTR_ORDER": string
static readonly "ATTR_VALUE": string
static readonly "ATTR_DEFAULT": string
static readonly "ATTR_VALID_VALUES": string
static readonly "ATTR_PARENT": string
static readonly "ATTR_PARENT_VALUE": string

constructor(arg0: $Category$Type, arg1: string, arg2: boolean, arg3: boolean)
constructor(arg0: $Category$Type, arg1: string, arg2: boolean, arg3: boolean, arg4: integer)
constructor(arg0: $Category$Type, arg1: string, arg2: boolean, arg3: integer)
constructor(arg0: $Category$Type, arg1: boolean)
constructor(arg0: $Category$Type, arg1: string, arg2: boolean)

public "get"(): boolean
public "set"(arg0: boolean): $BooleanField
public "isCategoryMaster"(): boolean
public "categoryMaster"(arg0: boolean): $BooleanField
public "toggleAndSave"(): boolean
public "toggle"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BooleanField$Type = ($BooleanField);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BooleanField_ = $BooleanField$Type;
}}
declare module "packages/journeymap/common/network/data/model/$PlayerLocation" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"

export interface $PlayerLocation {

 "getY"(): double
 "getZ"(): double
 "getX"(): double
 "isVisible"(): boolean
 "getUniqueId"(): $UUID
 "getPitch"(): byte
 "getYaw"(): byte
 "getEntityId"(): integer
}

export namespace $PlayerLocation {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerLocation$Type = ($PlayerLocation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerLocation_ = $PlayerLocation$Type;
}}
declare module "packages/journeymap/common/network/packets/$CommonPacket" {
import {$PacketContext, $PacketContext$Type} from "packages/journeymap/common/network/data/$PacketContext"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $CommonPacket {
static readonly "CHANNEL": $ResourceLocation

constructor()

public static "decode"(arg0: $FriendlyByteBuf$Type): $CommonPacket
public "encode"(arg0: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $PacketContext$Type<($CommonPacket$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonPacket$Type = ($CommonPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonPacket_ = $CommonPacket$Type;
}}
declare module "packages/journeymap/common/properties/$PermissionProperties" {
import {$BooleanField, $BooleanField$Type} from "packages/journeymap/common/properties/config/$BooleanField"
import {$EnumField, $EnumField$Type} from "packages/journeymap/common/properties/config/$EnumField"
import {$IntegerField, $IntegerField$Type} from "packages/journeymap/common/properties/config/$IntegerField"
import {$ServerPropertiesBase, $ServerPropertiesBase$Type} from "packages/journeymap/common/properties/$ServerPropertiesBase"
import {$ServerOption, $ServerOption$Type} from "packages/journeymap/common/properties/$ServerOption"

export class $PermissionProperties extends $ServerPropertiesBase {
readonly "teleportEnabled": $BooleanField
readonly "renderRange": $IntegerField
readonly "surfaceMapping": $EnumField<($ServerOption)>
readonly "topoMapping": $EnumField<($ServerOption)>
readonly "biomeMapping": $EnumField<($ServerOption)>
readonly "caveMapping": $EnumField<($ServerOption)>
readonly "radarEnabled": $EnumField<($ServerOption)>
readonly "playerRadarEnabled": $BooleanField
readonly "villagerRadarEnabled": $BooleanField
readonly "animalRadarEnabled": $BooleanField
readonly "mobRadarEnabled": $BooleanField


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PermissionProperties$Type = ($PermissionProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PermissionProperties_ = $PermissionProperties$Type;
}}
declare module "packages/journeymap/common/properties/$GlobalProperties" {
import {$PermissionProperties, $PermissionProperties$Type} from "packages/journeymap/common/properties/$PermissionProperties"
import {$BooleanField, $BooleanField$Type} from "packages/journeymap/common/properties/config/$BooleanField"
import {$EnumField, $EnumField$Type} from "packages/journeymap/common/properties/config/$EnumField"
import {$IntegerField, $IntegerField$Type} from "packages/journeymap/common/properties/config/$IntegerField"
import {$ServerOption, $ServerOption$Type} from "packages/journeymap/common/properties/$ServerOption"

export class $GlobalProperties extends $PermissionProperties {
readonly "journeymapEnabled": $BooleanField
readonly "useWorldId": $BooleanField
readonly "viewOnlyServerProperties": $BooleanField
readonly "allowMultiplayerSettings": $EnumField<($ServerOption)>
readonly "worldPlayerRadar": $EnumField<($ServerOption)>
readonly "worldPlayerRadarUpdateTime": $IntegerField
readonly "seeUndergroundPlayers": $EnumField<($ServerOption)>
readonly "hideOps": $BooleanField
readonly "hideSpectators": $BooleanField
readonly "allowDeathPoints": $BooleanField
readonly "showInGameBeacons": $BooleanField
readonly "allowWaypoints": $BooleanField
readonly "teleportEnabled": $BooleanField
readonly "renderRange": $IntegerField
readonly "surfaceMapping": $EnumField<($ServerOption)>
readonly "topoMapping": $EnumField<($ServerOption)>
readonly "biomeMapping": $EnumField<($ServerOption)>
readonly "caveMapping": $EnumField<($ServerOption)>
readonly "radarEnabled": $EnumField<($ServerOption)>
readonly "playerRadarEnabled": $BooleanField
readonly "villagerRadarEnabled": $BooleanField
readonly "animalRadarEnabled": $BooleanField
readonly "mobRadarEnabled": $BooleanField

constructor()

public "getName"(): string
public "clone"(): any
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlobalProperties$Type = ($GlobalProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlobalProperties_ = $GlobalProperties$Type;
}}
declare module "packages/journeymap/common/migrate/$MigrationTask" {
import {$Version, $Version$Type} from "packages/journeymap/common/version/$Version"
import {$Callable, $Callable$Type} from "packages/java/util/concurrent/$Callable"

export interface $MigrationTask extends $Callable<(boolean)> {

 "isActive"(arg0: $Version$Type): boolean
 "call"(): boolean
}

export namespace $MigrationTask {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MigrationTask$Type = ($MigrationTask);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MigrationTask_ = $MigrationTask$Type;
}}
declare module "packages/journeymap/common/network/data/model/$Location" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Location {

 "getY"(): double
 "getZ"(): double
 "getX"(): double
 "getDim"(): string
}

export namespace $Location {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Location$Type = ($Location);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Location_ = $Location$Type;
}}
declare module "packages/journeymap/common/network/dispatch/$ClientNetworkDispatcher" {
import {$NetworkHandler, $NetworkHandler$Type} from "packages/journeymap/common/network/data/$NetworkHandler"

export class $ClientNetworkDispatcher {

constructor(arg0: $NetworkHandler$Type)

public "sendTeleportPacket"(arg0: double, arg1: integer, arg2: double, arg3: string): void
public "sendWorldIdRequest"(): void
public "sendMultiplayerOptionsRequest"(): void
public "sendSaveAdminDataPacket"(arg0: integer, arg1: string, arg2: string): void
public "sendHandshakePacket"(arg0: string): void
public "sendServerAdminScreenRequest"(arg0: integer, arg1: string): void
public "sendMultiplayerOptionsSaveRequest"(arg0: string): void
public "sendPermissionRequest"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientNetworkDispatcher$Type = ($ClientNetworkDispatcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientNetworkDispatcher_ = $ClientNetworkDispatcher$Type;
}}
declare module "packages/journeymap/common/nbt/$RegionData" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$MapType$Name, $MapType$Name$Type} from "packages/journeymap/client/model/$MapType$Name"
import {$RegionDataStorageHandler$Key, $RegionDataStorageHandler$Key$Type} from "packages/journeymap/common/nbt/$RegionDataStorageHandler$Key"
import {$MapType, $MapType$Type} from "packages/journeymap/client/model/$MapType"
import {$ChunkMD, $ChunkMD$Type} from "packages/journeymap/client/model/$ChunkMD"
import {$CacheStorage, $CacheStorage$Type} from "packages/journeymap/common/nbt/cache/$CacheStorage"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $RegionData {
static readonly "BIOME_TAG_NAME": string
static readonly "BLOCK_TAG_NAME": string
static readonly "BLOCKSTATES_TAG_NAME": string
static readonly "BLOCK_LIGHT_VALUE": string
static readonly "TOP_Y_TAG_NAME": string
static readonly "SURFACE_Y_TAG_NAME": string
static readonly "BLOCK_COLOR_TAG_NAME": string
static readonly "CHUNK_POS_NAME": string

constructor(arg0: $RegionDataStorageHandler$Key$Type, arg1: $CacheStorage$Type)

public "setBiome"(arg0: $CompoundTag$Type, arg1: $Biome$Type): void
public "getChunkNbt"(arg0: $ChunkPos$Type): $CompoundTag
public "getTopY"(arg0: $BlockPos$Type): integer
public "getBiome"(arg0: $BlockPos$Type): $Biome
public "setY"(arg0: $CompoundTag$Type, arg1: integer): void
public "setLightValue"(arg0: $CompoundTag$Type, arg1: integer): void
public "writeChunk"(arg0: $ChunkPos$Type, arg1: $CompoundTag$Type): void
public "setSurfaceY"(arg0: $CompoundTag$Type, arg1: integer): void
public "setBlockColor"(arg0: $CompoundTag$Type, arg1: integer, arg2: $MapType$Name$Type): void
public "getColor"(arg0: $BlockPos$Type): integer
public static "getBlockDataForChunk"(arg0: $CompoundTag$Type, arg1: integer, arg2: integer): $CompoundTag
public static "getBlockState"(arg0: $CompoundTag$Type, arg1: $BlockPos$Type, arg2: $MapType$Type): $BlockState
public "getBlockState"(arg0: $BlockPos$Type): $BlockState
public "setBlockState"(arg0: $CompoundTag$Type, arg1: $ChunkMD$Type, arg2: $BlockPos$Type): void
public "getBlockDataFromBlockPos"(arg0: $ChunkPos$Type, arg1: integer, arg2: integer): $CompoundTag
public "getBlockDataFromBlockPos"(arg0: $ChunkPos$Type, arg1: $CompoundTag$Type, arg2: integer, arg3: integer): $CompoundTag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegionData$Type = ($RegionData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegionData_ = $RegionData$Type;
}}
declare module "packages/journeymap/common/util/$PlayerRadarManager" {
import {$PlayerLocation, $PlayerLocation$Type} from "packages/journeymap/common/network/data/model/$PlayerLocation"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $PlayerRadarManager {


public static "getInstance"(): $PlayerRadarManager
public "updatePlayers"(arg0: $PlayerLocation$Type): void
public "getPlayers"(): $Map<($UUID), ($Player)>
get "instance"(): $PlayerRadarManager
get "players"(): $Map<($UUID), ($Player)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerRadarManager$Type = ($PlayerRadarManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerRadarManager_ = $PlayerRadarManager$Type;
}}
declare module "packages/journeymap/common/util/$ObfHelper" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$Method, $Method$Type} from "packages/java/lang/reflect/$Method"

export class $ObfHelper {

constructor()

public static "findMethod"(arg0: $Class$Type<(any)>, arg1: string, ...arg2: ($Class$Type<(any)>)[]): $Method
public static "findField"<T>(arg0: $Class$Type<(any)>, arg1: string): $Field
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObfHelper$Type = ($ObfHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObfHelper_ = $ObfHelper$Type;
}}
declare module "packages/journeymap/common/properties/catagory/$Category" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"

export class $Category implements $Comparable<($Category)> {
static readonly "Inherit": $Category
static readonly "Hidden": $Category

constructor(arg0: string, arg1: integer, arg2: string)
constructor(arg0: string, arg1: integer, arg2: string, arg3: string)
constructor(arg0: string, arg1: integer, arg2: string, arg3: boolean)

public "getName"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(arg0: $Category$Type): integer
public "getOrder"(): integer
public "isUnique"(): boolean
public "getTooltip"(): string
public "getLabel"(): string
get "name"(): string
get "order"(): integer
get "unique"(): boolean
get "tooltip"(): string
get "label"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Category$Type = ($Category);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Category_ = $Category$Type;
}}
declare module "packages/journeymap/common/network/packets/$WaypointPacket" {
import {$PacketContext, $PacketContext$Type} from "packages/journeymap/common/network/data/$PacketContext"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $WaypointPacket {
static readonly "CHANNEL": $ResourceLocation

constructor()
constructor(arg0: string, arg1: boolean, arg2: string)

public static "decode"(arg0: $FriendlyByteBuf$Type): $WaypointPacket
public "encode"(arg0: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $PacketContext$Type<($WaypointPacket$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaypointPacket$Type = ($WaypointPacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaypointPacket_ = $WaypointPacket$Type;
}}
declare module "packages/journeymap/common/network/packets/$HandshakePacket" {
import {$PacketContext, $PacketContext$Type} from "packages/journeymap/common/network/data/$PacketContext"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $HandshakePacket {
static readonly "CHANNEL": $ResourceLocation

constructor()
constructor(arg0: string)

public static "decode"(arg0: $FriendlyByteBuf$Type): $HandshakePacket
public "encode"(arg0: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $PacketContext$Type<($HandshakePacket$Type)>): void
public static "disconnect"(arg0: $ServerPlayer$Type, arg1: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HandshakePacket$Type = ($HandshakePacket);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HandshakePacket_ = $HandshakePacket$Type;
}}
declare module "packages/journeymap/common/events/$ServerEventHandler" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ServerEventHandler {

constructor()

public "sendConfigsToPlayer"(arg0: $ServerPlayer$Type): void
public "onEntityJoinWorldEvent"(arg0: $Entity$Type): void
public "onPlayerLoggedInEvent"(arg0: $Player$Type): void
public "unloadPlayer"(arg0: $Entity$Type, arg1: $ServerLevel$Type): void
public "onServerTickEvent"(arg0: $Level$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerEventHandler$Type = ($ServerEventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerEventHandler_ = $ServerEventHandler$Type;
}}
declare module "packages/journeymap/common/accessors/$FluidAccess" {
import {$FlowingFluid, $FlowingFluid$Type} from "packages/net/minecraft/world/level/material/$FlowingFluid"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export interface $FluidAccess {

 "getFluid"(arg0: $Block$Type): $FlowingFluid
}

export namespace $FluidAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidAccess$Type = ($FluidAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidAccess_ = $FluidAccess$Type;
}}
declare module "packages/journeymap/common/properties/config/$ConfigField" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Config, $Config$Type} from "packages/journeymap/client/api/option/$Config"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Category, $Category$Type} from "packages/journeymap/common/properties/catagory/$Category"
import {$PropertiesBase, $PropertiesBase$Type} from "packages/journeymap/common/properties/$PropertiesBase"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ConfigField<T> implements $Config<(T)> {
static readonly "ATTR_TYPE": string
static readonly "ATTR_CATEGORY": string
static readonly "ATTR_KEY": string
static readonly "ATTR_LABEL": string
static readonly "ATTR_TOOLTIP": string
static readonly "ATTR_ORDER": string
static readonly "ATTR_VALUE": string
static readonly "ATTR_DEFAULT": string
static readonly "ATTR_VALID_VALUES": string
static readonly "ATTR_PARENT": string
static readonly "ATTR_PARENT_VALUE": string

constructor()

public "get"(arg0: string): any
public "get"(): T
public "put"(arg0: string, arg1: any): $ConfigField<(T)>
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "validate"(arg0: boolean): boolean
public "getKey"(): string
public "getDeclaredField"(): string
public "save"(): boolean
public "defaultValue"(arg0: T): $ConfigField<(T)>
public "getType"(): string
public "getDefaultValue"(): T
public "setParent"(arg0: string, arg1: any): $ConfigField<(T)>
public "getOwner"(): $PropertiesBase
public "label"(arg0: string): $ConfigField<(T)>
public "setOwner"(arg0: string, arg1: $PropertiesBase$Type): void
public "category"(arg0: $Category$Type): $ConfigField<(T)>
public "getCategory"(): $Category
public "getBooleanAttr"(arg0: string): boolean
public "getStringAttr"(arg0: string): string
public "getEnumAttr"<E extends $Enum<(any)>>(arg0: string, arg1: $Class$Type<(E)>): E
public "getFloatAttr"(arg0: string): float
public "getIntegerAttr"(arg0: string): integer
public "getTooltip"(): string
public "setToDefault"(): void
public "getLabel"(): string
public "getAttributeMap"(): $Map<(string), (any)>
public "sortOrder"(arg0: integer): $ConfigField<(T)>
public "getAttributeNames"(): $Set<(string)>
public "getSortOrder"(): integer
get "key"(): string
get "declaredField"(): string
get "type"(): string
get "owner"(): $PropertiesBase
get "tooltip"(): string
get "attributeMap"(): $Map<(string), (any)>
get "attributeNames"(): $Set<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigField$Type<T> = ($ConfigField<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigField_<T> = $ConfigField$Type<(T)>;
}}
