declare module "packages/com/mrcrayfish/catalogue/platform/services/$IComponentHelper" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export interface $IComponentHelper {

 "createVersion"(arg0: string): $MutableComponent
 "createTitle"(): $MutableComponent
 "getCreditsKey"(): string
 "createFormatted"(arg0: string, arg1: string): $MutableComponent
 "createFilterUpdates"(): $Component
}

export namespace $IComponentHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IComponentHelper$Type = ($IComponentHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IComponentHelper_ = $IComponentHelper$Type;
}}
declare module "packages/com/mrcrayfish/configured/impl/framework/$FrameworkConfigProvider" {
import {$ModContext, $ModContext$Type} from "packages/com/mrcrayfish/configured/api/$ModContext"
import {$IModConfigProvider, $IModConfigProvider$Type} from "packages/com/mrcrayfish/configured/api/$IModConfigProvider"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IModConfig, $IModConfig$Type} from "packages/com/mrcrayfish/configured/api/$IModConfig"

export class $FrameworkConfigProvider implements $IModConfigProvider {

constructor()

public "getConfigurationsForMod"(arg0: $ModContext$Type): $Set<($IModConfig)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FrameworkConfigProvider$Type = ($FrameworkConfigProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FrameworkConfigProvider_ = $FrameworkConfigProvider$Type;
}}
declare module "packages/com/mrcrayfish/configured/impl/framework/handler/$FrameworkClientHandler" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$MessageFramework$Response, $MessageFramework$Response$Type} from "packages/com/mrcrayfish/configured/impl/framework/message/$MessageFramework$Response"

export class $FrameworkClientHandler {

constructor()

public static "handleResponse"(arg0: $MessageFramework$Response$Type, arg1: $Consumer$Type<($Component$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FrameworkClientHandler$Type = ($FrameworkClientHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FrameworkClientHandler_ = $FrameworkClientHandler$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/$EditingTracker" {
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export class $EditingTracker {


public static "instance"(): $EditingTracker
public "onScreenOpen"(arg0: $Screen$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EditingTracker$Type = ($EditingTracker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EditingTracker_ = $EditingTracker$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/list/$ListType" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IListType, $IListType$Type} from "packages/com/mrcrayfish/configured/client/screen/list/$IListType"

export class $ListType<T> implements $IListType<(T)> {

constructor(arg0: $Function$Type<(T), (string)>, arg1: $Function$Type<(string), (T)>, arg2: string)

public "getHint"(): $Component
public "getValueParser"(): $Function<(string), (T)>
public "getStringParser"(): $Function<(T), (string)>
get "hint"(): $Component
get "valueParser"(): $Function<(string), (T)>
get "stringParser"(): $Function<(T), (string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListType$Type<T> = ($ListType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListType_<T> = $ListType$Type<(T)>;
}}
declare module "packages/com/mrcrayfish/configured/api/$Environment" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Environment extends $Enum<($Environment)> {
static readonly "CLIENT": $Environment
static readonly "DEDICATED_SERVER": $Environment


public static "values"(): ($Environment)[]
public static "valueOf"(arg0: string): $Environment
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Environment$Type = (("dedicated_server") | ("client")) | ($Environment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Environment_ = $Environment$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/list/$IListConfigValue" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IListType, $IListType$Type} from "packages/com/mrcrayfish/configured/client/screen/list/$IListType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IConfigValue, $IConfigValue$Type} from "packages/com/mrcrayfish/configured/api/$IConfigValue"

export interface $IListConfigValue<T> extends $IConfigValue<($List<(T)>)> {

 "getListType"(): $IListType<(T)>
 "getName"(): string
 "get"(): $List<(T)>
 "getDefault"(): $List<(T)>
 "set"(arg0: $List$Type<(T)>): void
 "isDefault"(): boolean
 "isValid"(arg0: $List$Type<(T)>): boolean
 "getComment"(): $Component
 "getValidationHint"(): $Component
 "restore"(): void
 "isChanged"(): boolean
 "requiresGameRestart"(): boolean
 "requiresWorldRestart"(): boolean
 "cleanCache"(): void
 "getTranslationKey"(): string
}

export namespace $IListConfigValue {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IListConfigValue$Type<T> = ($IListConfigValue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IListConfigValue_<T> = $IListConfigValue$Type<(T)>;
}}
declare module "packages/com/mrcrayfish/catalogue/client/$ClientHandler" {
import {$ScreenEvent$Opening, $ScreenEvent$Opening$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$Opening"

export class $ClientHandler {

constructor()

public static "onOpenScreen"(arg0: $ScreenEvent$Opening$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientHandler$Type = ($ClientHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientHandler_ = $ClientHandler$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/$ListMenuScreen" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$TooltipScreen, $TooltipScreen$Type} from "packages/com/mrcrayfish/configured/client/screen/$TooltipScreen"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IBackgroundTexture, $IBackgroundTexture$Type} from "packages/com/mrcrayfish/configured/client/screen/$IBackgroundTexture"

export class $ListMenuScreen extends $TooltipScreen implements $IBackgroundTexture {
static readonly "CONFIGURED_LOGO": $ResourceLocation
 "tooltipText": $List<($FormattedCharSequence)>
 "tooltipOutlineColour": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering


public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "tick"(): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "getBackgroundTexture"(): $ResourceLocation
public static "loadTexture"(arg0: any, arg1: $ResourceLocation$Type): $ResourceLocation
get "backgroundTexture"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListMenuScreen$Type = ($ListMenuScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListMenuScreen_ = $ListMenuScreen$Type;
}}
declare module "packages/com/mrcrayfish/configured/network/$ClientPlayHandler" {
import {$MessageSessionData, $MessageSessionData$Type} from "packages/com/mrcrayfish/configured/network/message/$MessageSessionData"

export class $ClientPlayHandler {

constructor()

public static "handleSessionData"(arg0: $MessageSessionData$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientPlayHandler$Type = ($ClientPlayHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientPlayHandler_ = $ClientPlayHandler$Type;
}}
declare module "packages/com/mrcrayfish/configured/util/$ConfigHelper" {
import {$IConfigEntry, $IConfigEntry$Type} from "packages/com/mrcrayfish/configured/api/$IConfigEntry"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IModConfig, $IModConfig$Type} from "packages/com/mrcrayfish/configured/api/$IModConfig"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$IConfigValue, $IConfigValue$Type} from "packages/com/mrcrayfish/configured/api/$IConfigValue"
import {$UnmodifiableConfig, $UnmodifiableConfig$Type} from "packages/com/electronwill/nightconfig/core/$UnmodifiableConfig"

export class $ConfigHelper {

constructor()

public static "gatherAllConfigValues"(arg0: $IModConfig$Type): $List<($IConfigValue<(any)>)>
public static "gatherAllConfigValues"(arg0: $IConfigEntry$Type): $List<($IConfigValue<(any)>)>
public static "isServerOwnedByPlayer"(arg0: $Player$Type): boolean
public static "getBytes"(arg0: $UnmodifiableConfig$Type): (byte)[]
public static "readBytes"(arg0: $Path$Type): (byte)[]
public static "isPlayingGame"(): boolean
public static "isRunningLocalServer"(): boolean
public static "isServerConfig"(arg0: $IModConfig$Type): boolean
public static "getClientPlayer"(): $Player
public static "isOperator"(arg0: $Player$Type): boolean
public static "createBackup"(arg0: $UnmodifiableConfig$Type): void
public static "isWorldConfig"(arg0: $IModConfig$Type): boolean
public static "loadConfig"(arg0: $UnmodifiableConfig$Type): void
public static "saveConfig"(arg0: $UnmodifiableConfig$Type): void
public static "gatherAllConfigEntries"(arg0: $IConfigEntry$Type): $List<($IConfigEntry)>
public static "closeConfig"(arg0: $UnmodifiableConfig$Type): void
public static "getChangedValues"(arg0: $IConfigEntry$Type): $Set<($IConfigValue<(any)>)>
public static "isConfiguredInstalledOnServer"(): boolean
public static "hasPermissionToEdit"(arg0: $Player$Type, arg1: $IModConfig$Type): boolean
public static "watchConfig"(arg0: $UnmodifiableConfig$Type, arg1: $Runnable$Type): void
get "playingGame"(): boolean
get "runningLocalServer"(): boolean
get "clientPlayer"(): $Player
get "configuredInstalledOnServer"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigHelper$Type = ($ConfigHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigHelper_ = $ConfigHelper$Type;
}}
declare module "packages/com/mrcrayfish/configured/$Config" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"

export class $Config {

constructor()

public static "load"(): void
public static "getChangedFormatting"(): $ChatFormatting
public static "isDeveloperEnabled"(): boolean
public static "getDevelopers"(): $List<(string)>
public static "shouldBroadcastLogs"(): boolean
public static "isIncludeFoldersInSearch"(): boolean
public static "isForceConfiguredMenu"(): boolean
get "changedFormatting"(): $ChatFormatting
get "developerEnabled"(): boolean
get "developers"(): $List<(string)>
get "includeFoldersInSearch"(): boolean
get "forceConfiguredMenu"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Config$Type = ($Config);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Config_ = $Config$Type;
}}
declare module "packages/com/mrcrayfish/configured/impl/forge/$ForgeValue" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$ForgeConfigSpec$ValueSpec, $ForgeConfigSpec$ValueSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ValueSpec"
import {$IConfigValue, $IConfigValue$Type} from "packages/com/mrcrayfish/configured/api/$IConfigValue"

export class $ForgeValue<T> implements $IConfigValue<(T)> {
readonly "configValue": $ForgeConfigSpec$ConfigValue<(T)>
readonly "valueSpec": $ForgeConfigSpec$ValueSpec

constructor(arg0: $ForgeConfigSpec$ConfigValue$Type<(T)>, arg1: $ForgeConfigSpec$ValueSpec$Type)

public "getName"(): string
public "get"(): T
public "getDefault"(): T
public "set"(arg0: T): void
public "isDefault"(): boolean
public "isValid"(arg0: T): boolean
public "getComment"(): $Component
public "loadRange"(): void
public "getValidationHint"(): $Component
public "restore"(): void
public static "lastValue"<V>(arg0: $List$Type<(V)>, arg1: V): V
public "isChanged"(): boolean
public "requiresGameRestart"(): boolean
public "requiresWorldRestart"(): boolean
public "cleanCache"(): void
public "getTranslationKey"(): string
get "name"(): string
get "default"(): T
get "default"(): boolean
get "comment"(): $Component
get "validationHint"(): $Component
get "changed"(): boolean
get "translationKey"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeValue$Type<T> = ($ForgeValue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeValue_<T> = $ForgeValue$Type<(T)>;
}}
declare module "packages/com/mrcrayfish/configured/impl/forge/$ForgeConfig" {
import {$IConfigEntry, $IConfigEntry$Type} from "packages/com/mrcrayfish/configured/api/$IConfigEntry"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$ModConfig, $ModConfig$Type} from "packages/net/minecraftforge/fml/config/$ModConfig"
import {$IModConfig, $IModConfig$Type} from "packages/com/mrcrayfish/configured/api/$IModConfig"
import {$ConfigType, $ConfigType$Type} from "packages/com/mrcrayfish/configured/api/$ConfigType"

export class $ForgeConfig implements $IModConfig {

constructor(arg0: $ModConfig$Type, arg1: $ForgeConfigSpec$Type)

public "update"(arg0: $IConfigEntry$Type): void
public "getRoot"(): $IConfigEntry
public "getType"(): $ConfigType
public "getFileName"(): string
public "loadWorldConfig"(arg0: $Path$Type, arg1: $Consumer$Type<($IModConfig$Type)>): void
public "stopEditing"(): void
public "restoreDefaults"(): void
public "isChanged"(): boolean
public "getModId"(): string
public "isReadOnly"(): boolean
public "requestFromServer"(): void
public "startEditing"(): void
public "getTranslationKey"(): string
get "root"(): $IConfigEntry
get "type"(): $ConfigType
get "fileName"(): string
get "changed"(): boolean
get "modId"(): string
get "readOnly"(): boolean
get "translationKey"(): string
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
declare module "packages/com/mrcrayfish/configured/impl/forge/$ForgeFolderEntry" {
import {$IConfigEntry, $IConfigEntry$Type} from "packages/com/mrcrayfish/configured/api/$IConfigEntry"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IConfigValue, $IConfigValue$Type} from "packages/com/mrcrayfish/configured/api/$IConfigValue"
import {$UnmodifiableConfig, $UnmodifiableConfig$Type} from "packages/com/electronwill/nightconfig/core/$UnmodifiableConfig"

export class $ForgeFolderEntry implements $IConfigEntry {

constructor(arg0: $UnmodifiableConfig$Type, arg1: $ForgeConfigSpec$Type)
constructor(arg0: $List$Type<(string)>, arg1: $UnmodifiableConfig$Type, arg2: $ForgeConfigSpec$Type)

public "getValue"(): $IConfigValue<(any)>
public "isRoot"(): boolean
public "getEntryName"(): string
public "isLeaf"(): boolean
public "getChildren"(): $List<($IConfigEntry)>
public "getTooltip"(): $Component
public "getTranslationKey"(): string
get "value"(): $IConfigValue<(any)>
get "root"(): boolean
get "entryName"(): string
get "leaf"(): boolean
get "children"(): $List<($IConfigEntry)>
get "tooltip"(): $Component
get "translationKey"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeFolderEntry$Type = ($ForgeFolderEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeFolderEntry_ = $ForgeFolderEntry$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/$EditListScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$IModConfig, $IModConfig$Type} from "packages/com/mrcrayfish/configured/api/$IModConfig"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$IEditing, $IEditing$Type} from "packages/com/mrcrayfish/configured/client/screen/$IEditing"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IConfigValue, $IConfigValue$Type} from "packages/com/mrcrayfish/configured/api/$IConfigValue"
import {$IBackgroundTexture, $IBackgroundTexture$Type} from "packages/com/mrcrayfish/configured/client/screen/$IBackgroundTexture"

export class $EditListScreen<T> extends $Screen implements $IBackgroundTexture, $IEditing {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Screen$Type, arg1: $IModConfig$Type, arg2: $Component$Type, arg3: $IConfigValue$Type<($List$Type<(T)>)>, arg4: $ResourceLocation$Type)

public "getActiveConfig"(): $IModConfig
public "isModified"(): boolean
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "getBackgroundTexture"(): $ResourceLocation
public static "loadTexture"(arg0: any, arg1: $ResourceLocation$Type): $ResourceLocation
get "activeConfig"(): $IModConfig
get "modified"(): boolean
get "backgroundTexture"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EditListScreen$Type<T> = ($EditListScreen<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EditListScreen_<T> = $EditListScreen$Type<(T)>;
}}
declare module "packages/com/mrcrayfish/configured/api/$ModContext" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $ModContext extends $Record {

constructor(modId: string)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "modId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModContext$Type = ($ModContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModContext_ = $ModContext$Type;
}}
declare module "packages/com/mrcrayfish/catalogue/platform/$ForgeComponentHelper" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IComponentHelper, $IComponentHelper$Type} from "packages/com/mrcrayfish/catalogue/platform/services/$IComponentHelper"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export class $ForgeComponentHelper implements $IComponentHelper {

constructor()

public "createVersion"(arg0: string): $MutableComponent
public "createTitle"(): $MutableComponent
public "getCreditsKey"(): string
public "createFormatted"(arg0: string, arg1: string): $MutableComponent
public "createFilterUpdates"(): $Component
get "creditsKey"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeComponentHelper$Type = ($ForgeComponentHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeComponentHelper_ = $ForgeComponentHelper$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/$SessionData" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export class $SessionData {

constructor()

public static "isLan"(): boolean
public static "isDeveloper"(arg0: $Player$Type): boolean
public static "setLan"(arg0: boolean): void
public static "setDeveloper"(arg0: boolean): void
get "lan"(): boolean
set "lan"(value: boolean)
set "developer"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SessionData$Type = ($SessionData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SessionData_ = $SessionData$Type;
}}
declare module "packages/com/mrcrayfish/configured/util/$OptiFineHelper" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $OptiFineHelper {

constructor()

public static "isLoaded"(): boolean
get "loaded"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OptiFineHelper$Type = ($OptiFineHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OptiFineHelper_ = $OptiFineHelper$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/$IBackgroundTexture" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $IBackgroundTexture {

 "getBackgroundTexture"(): $ResourceLocation

(arg0: any, arg1: $ResourceLocation$Type): $ResourceLocation
}

export namespace $IBackgroundTexture {
function loadTexture(arg0: any, arg1: $ResourceLocation$Type): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IBackgroundTexture$Type = ($IBackgroundTexture);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IBackgroundTexture_ = $IBackgroundTexture$Type;
}}
declare module "packages/com/mrcrayfish/configured/platform/$ForgeConfigHelper" {
import {$IConfigHelper, $IConfigHelper$Type} from "packages/com/mrcrayfish/configured/platform/services/$IConfigHelper"
import {$IModConfigProvider, $IModConfigProvider$Type} from "packages/com/mrcrayfish/configured/api/$IModConfigProvider"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$LevelResource, $LevelResource$Type} from "packages/net/minecraft/world/level/storage/$LevelResource"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ForgeConfigHelper implements $IConfigHelper {

constructor()

public "getProviders"(): $Set<($IModConfigProvider)>
public "getServerConfigResource"(): $LevelResource
public "getBackgroundTexture"(arg0: string): $ResourceLocation
get "providers"(): $Set<($IModConfigProvider)>
get "serverConfigResource"(): $LevelResource
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeConfigHelper$Type = ($ForgeConfigHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeConfigHelper_ = $ForgeConfigHelper$Type;
}}
declare module "packages/com/mrcrayfish/catalogue/client/$ClientHelper" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ClientHelper {

constructor()

public static "isMouseWithin"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientHelper$Type = ($ClientHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientHelper_ = $ClientHelper$Type;
}}
declare module "packages/com/mrcrayfish/configured/api/util/$ConfigScreenHelper" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IModConfig, $IModConfig$Type} from "packages/com/mrcrayfish/configured/api/$IModConfig"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ConfigType, $ConfigType$Type} from "packages/com/mrcrayfish/configured/api/$ConfigType"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ConfigScreenHelper {

constructor()

public static "createSelectionScreen"(arg0: $Screen$Type, arg1: $Component$Type, arg2: $IModConfig$Type, arg3: $ResourceLocation$Type): $Screen
public static "createSelectionScreen"(arg0: $Component$Type, arg1: $IModConfig$Type, arg2: $ResourceLocation$Type): $Screen
public static "createSelectionScreen"(arg0: $Screen$Type, arg1: $Component$Type, arg2: $Map$Type<($ConfigType$Type), ($Set$Type<($IModConfig$Type)>)>, arg3: $ResourceLocation$Type): $Screen
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigScreenHelper$Type = ($ConfigScreenHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigScreenHelper_ = $ConfigScreenHelper$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/$ILabelProvider" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ILabelProvider {

 "getLabel"(): string

(): string
}

export namespace $ILabelProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ILabelProvider$Type = ($ILabelProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ILabelProvider_ = $ILabelProvider$Type;
}}
declare module "packages/com/mrcrayfish/configured/$Events" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"

export class $Events {

constructor()

public static "onPlayerLoggedIn"(arg0: $ServerPlayer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Events$Type = ($Events);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Events_ = $Events$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/$EditStringScreen" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$IModConfig, $IModConfig$Type} from "packages/com/mrcrayfish/configured/api/$IModConfig"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$TooltipScreen, $TooltipScreen$Type} from "packages/com/mrcrayfish/configured/client/screen/$TooltipScreen"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$IEditing, $IEditing$Type} from "packages/com/mrcrayfish/configured/client/screen/$IEditing"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IBackgroundTexture, $IBackgroundTexture$Type} from "packages/com/mrcrayfish/configured/client/screen/$IBackgroundTexture"

export class $EditStringScreen extends $TooltipScreen implements $IBackgroundTexture, $IEditing {
 "tooltipText": $List<($FormattedCharSequence)>
 "tooltipOutlineColour": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering


public "getActiveConfig"(): $IModConfig
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "getBackgroundTexture"(): $ResourceLocation
public static "loadTexture"(arg0: any, arg1: $ResourceLocation$Type): $ResourceLocation
get "activeConfig"(): $IModConfig
get "backgroundTexture"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EditStringScreen$Type = ($EditStringScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EditStringScreen_ = $EditStringScreen$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/$IEditing" {
import {$IModConfig, $IModConfig$Type} from "packages/com/mrcrayfish/configured/api/$IModConfig"

export interface $IEditing {

 "getActiveConfig"(): $IModConfig

(): $IModConfig
}

export namespace $IEditing {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IEditing$Type = ($IEditing);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IEditing_ = $IEditing$Type;
}}
declare module "packages/com/mrcrayfish/configured/$Reference" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Reference {
static readonly "MOD_ID": string

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Reference$Type = ($Reference);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Reference_ = $Reference$Type;
}}
declare module "packages/com/mrcrayfish/configured/api/$IConfigEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IConfigValue, $IConfigValue$Type} from "packages/com/mrcrayfish/configured/api/$IConfigValue"

export interface $IConfigEntry {

 "getValue"(): $IConfigValue<(any)>
 "isRoot"(): boolean
 "getEntryName"(): string
 "isLeaf"(): boolean
 "getChildren"(): $List<($IConfigEntry)>
 "getTooltip"(): $Component
 "getTranslationKey"(): string
}

export namespace $IConfigEntry {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IConfigEntry$Type = ($IConfigEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IConfigEntry_ = $IConfigEntry$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/list/$IListType" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"

export interface $IListType<T> {

 "getHint"(): $Component
 "getValueParser"(): $Function<(string), (T)>
 "getStringParser"(): $Function<(T), (string)>
}

export namespace $IListType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IListType$Type<T> = ($IListType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IListType_<T> = $IListType$Type<(T)>;
}}
declare module "packages/com/mrcrayfish/catalogue/client/$ForgeModData" {
import {$IModInfo, $IModInfo$Type} from "packages/net/minecraftforge/forgespi/language/$IModInfo"
import {$IModData$Update, $IModData$Update$Type} from "packages/com/mrcrayfish/catalogue/client/$IModData$Update"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$IModData, $IModData$Type} from "packages/com/mrcrayfish/catalogue/client/$IModData"
import {$IModData$Type, $IModData$Type$Type} from "packages/com/mrcrayfish/catalogue/client/$IModData$Type"

export class $ForgeModData implements $IModData {

constructor(arg0: $IModInfo$Type)

public "getType"(): $IModData$Type
public "getDisplayName"(): string
public "getVersion"(): string
public "getDescription"(): string
public "getItemIcon"(): string
public "getUpdate"(): $IModData$Update
public "getBanner"(): string
public "getImageIcon"(): string
public "hasConfig"(): boolean
public "openConfigScreen"(arg0: $Screen$Type): void
public "isLogoSmooth"(): boolean
public "getHomepage"(): string
public "getIssueTracker"(): string
public "getBackground"(): string
public "getLicense"(): string
public "getAuthors"(): string
public "getCredits"(): string
public "getModId"(): string
get "type"(): $IModData$Type
get "displayName"(): string
get "version"(): string
get "description"(): string
get "itemIcon"(): string
get "update"(): $IModData$Update
get "banner"(): string
get "imageIcon"(): string
get "logoSmooth"(): boolean
get "homepage"(): string
get "issueTracker"(): string
get "background"(): string
get "license"(): string
get "authors"(): string
get "credits"(): string
get "modId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeModData$Type = ($ForgeModData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeModData_ = $ForgeModData$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/$ConfirmationScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$ConfirmationScreen$Icon, $ConfirmationScreen$Icon$Type} from "packages/com/mrcrayfish/configured/client/screen/$ConfirmationScreen$Icon"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IBackgroundTexture, $IBackgroundTexture$Type} from "packages/com/mrcrayfish/configured/client/screen/$IBackgroundTexture"

export class $ConfirmationScreen extends $Screen implements $IBackgroundTexture {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Screen$Type, arg1: $Component$Type, arg2: $ConfirmationScreen$Icon$Type, arg3: $Function$Type<(boolean), (boolean)>)

public "setPositiveText"(arg0: $Component$Type): void
public "setNegativeText"(arg0: $Component$Type): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "setBackground"(arg0: $ResourceLocation$Type): $ConfirmationScreen
public static "drawListBackground"(arg0: double, arg1: double, arg2: double, arg3: double): void
public "getBackgroundTexture"(): $ResourceLocation
public static "loadTexture"(arg0: any, arg1: $ResourceLocation$Type): $ResourceLocation
set "positiveText"(value: $Component$Type)
set "negativeText"(value: $Component$Type)
set "background"(value: $ResourceLocation$Type)
get "backgroundTexture"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfirmationScreen$Type = ($ConfirmationScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfirmationScreen_ = $ConfirmationScreen$Type;
}}
declare module "packages/com/mrcrayfish/configured/util/$ForgeConfigHelper" {
import {$ModConfigEvent, $ModConfigEvent$Type} from "packages/net/minecraftforge/fml/event/config/$ModConfigEvent"
import {$Pair, $Pair$Type} from "packages/org/apache/commons/lang3/tuple/$Pair"
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$ModConfig, $ModConfig$Type} from "packages/net/minecraftforge/fml/config/$ModConfig"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$ForgeConfigSpec$ValueSpec, $ForgeConfigSpec$ValueSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ValueSpec"
import {$UnmodifiableConfig, $UnmodifiableConfig$Type} from "packages/com/electronwill/nightconfig/core/$UnmodifiableConfig"
import {$CommentedConfig, $CommentedConfig$Type} from "packages/com/electronwill/nightconfig/core/$CommentedConfig"

export class $ForgeConfigHelper {

constructor()

public static "gatherAllForgeConfigValues"(arg0: $UnmodifiableConfig$Type, arg1: $ForgeConfigSpec$Type): $List<($Pair<($ForgeConfigSpec$ConfigValue<(any)>), ($ForgeConfigSpec$ValueSpec)>)>
public static "gatherAllForgeConfigValues"(arg0: $ModConfig$Type): $List<($Pair<($ForgeConfigSpec$ConfigValue<(any)>), ($ForgeConfigSpec$ValueSpec)>)>
public static "fireForgeConfigEvent"(arg0: $ModConfig$Type, arg1: $ModConfigEvent$Type): void
public static "findConfigSpec"(arg0: $UnmodifiableConfig$Type): $ForgeConfigSpec
public static "getForgeConfig"(arg0: string): $ModConfig
public static "setForgeConfigData"(arg0: $ModConfig$Type, arg1: $CommentedConfig$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeConfigHelper$Type = ($ForgeConfigHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeConfigHelper_ = $ForgeConfigHelper$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/util/$ScreenUtil" {
import {$Tooltip, $Tooltip$Type} from "packages/net/minecraft/client/gui/components/$Tooltip"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"
import {$EditBox, $EditBox$Type} from "packages/net/minecraft/client/gui/components/$EditBox"

export class $ScreenUtil {

constructor()

public static "button"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $Component$Type, arg5: $Button$OnPress$Type): $Button
public static "isMouseWithin"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): boolean
public static "createTooltip"(arg0: $Screen$Type, arg1: $Component$Type, arg2: integer, arg3: $Predicate$Type<($Button$Type)>): $Tooltip
public static "createTooltip"(arg0: $Screen$Type, arg1: $Component$Type, arg2: integer): $Tooltip
public static "updateSearchTextFieldSuggestion"(arg0: $EditBox$Type, arg1: string, arg2: $List$Type<(any)>): void
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
declare module "packages/com/mrcrayfish/configured/client/screen/$ConfirmationScreen$Icon" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ConfirmationScreen$Icon extends $Enum<($ConfirmationScreen$Icon)> {
static readonly "INFO": $ConfirmationScreen$Icon
static readonly "WARNING": $ConfirmationScreen$Icon
static readonly "ERROR": $ConfirmationScreen$Icon


public static "values"(): ($ConfirmationScreen$Icon)[]
public static "valueOf"(arg0: string): $ConfirmationScreen$Icon
public "v"(): integer
public "u"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfirmationScreen$Icon$Type = (("warning") | ("error") | ("info")) | ($ConfirmationScreen$Icon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfirmationScreen$Icon_ = $ConfirmationScreen$Icon$Type;
}}
declare module "packages/com/mrcrayfish/catalogue/$Catalogue" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Catalogue {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Catalogue$Type = ($Catalogue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Catalogue_ = $Catalogue$Type;
}}
declare module "packages/com/mrcrayfish/configured/network/$ForgeNetwork" {
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $ForgeNetwork {

constructor()

public static "init"(): void
public static "getPlay"(): $SimpleChannel
get "play"(): $SimpleChannel
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeNetwork$Type = ($ForgeNetwork);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeNetwork_ = $ForgeNetwork$Type;
}}
declare module "packages/com/mrcrayfish/configured/impl/jei/$JeiConfig" {
import {$IConfigEntry, $IConfigEntry$Type} from "packages/com/mrcrayfish/configured/api/$IConfigEntry"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$IModConfig, $IModConfig$Type} from "packages/com/mrcrayfish/configured/api/$IModConfig"
import {$IJeiConfigFile, $IJeiConfigFile$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigFile"
import {$ConfigType, $ConfigType$Type} from "packages/com/mrcrayfish/configured/api/$ConfigType"

export class $JeiConfig implements $IModConfig {

constructor(arg0: string, arg1: $ConfigType$Type, arg2: $IJeiConfigFile$Type)

public "update"(arg0: $IConfigEntry$Type): void
public "getRoot"(): $IConfigEntry
public "getType"(): $ConfigType
public "getFileName"(): string
public "loadWorldConfig"(arg0: $Path$Type, arg1: $Consumer$Type<($IModConfig$Type)>): void
public "getModId"(): string
public "isReadOnly"(): boolean
public "requestFromServer"(): void
public "stopEditing"(): void
public "startEditing"(): void
public "restoreDefaults"(): void
public "isChanged"(): boolean
public "getTranslationKey"(): string
get "root"(): $IConfigEntry
get "type"(): $ConfigType
get "fileName"(): string
get "modId"(): string
get "readOnly"(): boolean
get "changed"(): boolean
get "translationKey"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiConfig$Type = ($JeiConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiConfig_ = $JeiConfig$Type;
}}
declare module "packages/com/mrcrayfish/catalogue/client/screen/widget/$CatalogueIconButton" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $CatalogueIconButton extends $Button {
static readonly "SMALL_WIDTH": integer
static readonly "DEFAULT_WIDTH": integer
static readonly "DEFAULT_HEIGHT": integer
 "onPress": $Button$OnPress
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $Button$OnPress$Type)
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $Component$Type, arg6: $Button$OnPress$Type)

public "m_87963_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CatalogueIconButton$Type = ($CatalogueIconButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CatalogueIconButton_ = $CatalogueIconButton$Type;
}}
declare module "packages/com/mrcrayfish/configured/api/$IModConfig" {
import {$IConfigEntry, $IConfigEntry$Type} from "packages/com/mrcrayfish/configured/api/$IConfigEntry"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ConfigType, $ConfigType$Type} from "packages/com/mrcrayfish/configured/api/$ConfigType"

export interface $IModConfig {

 "update"(arg0: $IConfigEntry$Type): void
 "getRoot"(): $IConfigEntry
 "getType"(): $ConfigType
 "getFileName"(): string
 "isReadOnly"(): boolean
 "loadWorldConfig"(arg0: $Path$Type, arg1: $Consumer$Type<($IModConfig$Type)>): void
 "requestFromServer"(): void
 "stopEditing"(): void
 "startEditing"(): void
 "restoreDefaults"(): void
 "isChanged"(): boolean
 "getTranslationKey"(): string
 "getModId"(): string
}

export namespace $IModConfig {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IModConfig$Type = ($IModConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IModConfig_ = $IModConfig$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/list/$ListTypes" {
import {$IListType, $IListType$Type} from "packages/com/mrcrayfish/configured/client/screen/list/$IListType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IConfigValue, $IConfigValue$Type} from "packages/com/mrcrayfish/configured/api/$IConfigValue"

export class $ListTypes {
static readonly "BOOLEAN": $IListType<(boolean)>
static readonly "INTEGER": $IListType<(integer)>
static readonly "LONG": $IListType<(long)>
static readonly "DOUBLE": $IListType<(double)>
static readonly "STRING": $IListType<(string)>

constructor()

public static "getType"<T>(arg0: $IConfigValue$Type<($List$Type<(T)>)>): $IListType<(T)>
public static "getUnknown"<T>(): $IListType<(T)>
get "unknown"(): $IListType<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListTypes$Type = ($ListTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListTypes_ = $ListTypes$Type;
}}
declare module "packages/com/mrcrayfish/configured/api/$IAllowedEnums" {
import {$Set, $Set$Type} from "packages/java/util/$Set"

export interface $IAllowedEnums<T> {

 "getAllowedValues"(): $Set<(T)>

(): $Set<(T)>
}

export namespace $IAllowedEnums {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IAllowedEnums$Type<T> = ($IAllowedEnums<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IAllowedEnums_<T> = $IAllowedEnums$Type<(T)>;
}}
declare module "packages/com/mrcrayfish/catalogue/client/screen/widget/$CatalogueCheckBoxButton$OnPress" {
import {$Checkbox, $Checkbox$Type} from "packages/net/minecraft/client/gui/components/$Checkbox"

export interface $CatalogueCheckBoxButton$OnPress {

 "onPress"(arg0: $Checkbox$Type): void

(arg0: $Checkbox$Type): void
}

export namespace $CatalogueCheckBoxButton$OnPress {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CatalogueCheckBoxButton$OnPress$Type = ($CatalogueCheckBoxButton$OnPress);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CatalogueCheckBoxButton$OnPress_ = $CatalogueCheckBoxButton$OnPress$Type;
}}
declare module "packages/com/mrcrayfish/catalogue/client/$IModData$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"

export class $IModData$Type extends $Enum<($IModData$Type)> {
static readonly "DEFAULT": $IModData$Type
static readonly "LIBRARY": $IModData$Type
static readonly "GENERATED": $IModData$Type


public static "values"(): ($IModData$Type)[]
public static "valueOf"(arg0: string): $IModData$Type
public "getStyle"(): $ChatFormatting
get "style"(): $ChatFormatting
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IModData$Type$Type = (("default") | ("library") | ("generated")) | ($IModData$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IModData$Type_ = $IModData$Type$Type;
}}
declare module "packages/com/mrcrayfish/configured/util/$ForgeConfigScreenHelper" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ModContainer, $ModContainer$Type} from "packages/net/minecraftforge/fml/$ModContainer"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ForgeConfigScreenHelper {

constructor()

public static "createForgeConfigSelectionScreen"(arg0: $Component$Type, arg1: $ModContainer$Type, arg2: $ResourceLocation$Type): $Screen
public static "createForgeConfigSelectionScreen"(arg0: $Screen$Type, arg1: $Component$Type, arg2: $ModContainer$Type, arg3: $ResourceLocation$Type): $Screen
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeConfigScreenHelper$Type = ($ForgeConfigScreenHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeConfigScreenHelper_ = $ForgeConfigScreenHelper$Type;
}}
declare module "packages/com/mrcrayfish/configured/platform/services/$IPlatformHelper" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Environment, $Environment$Type} from "packages/com/mrcrayfish/configured/api/$Environment"
import {$ClientPacketListener, $ClientPacketListener$Type} from "packages/net/minecraft/client/multiplayer/$ClientPacketListener"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $IPlatformHelper {

 "getEnvironmentName"(): string
 "isDevelopmentEnvironment"(): boolean
 "getConfigPath"(): $Path
 "getGamePath"(): $Path
 "getPlatformName"(): string
 "sendSessionData"(arg0: $ServerPlayer$Type): void
 "isConnectionActive"(arg0: $ClientPacketListener$Type): boolean
 "getEnvironment"(): $Environment
 "sendFrameworkConfigResponse"(arg0: $ServerPlayer$Type, arg1: (byte)[]): void
 "sendFrameworkConfigToServer"(arg0: $ResourceLocation$Type, arg1: (byte)[]): void
 "sendFrameworkConfigRequest"(arg0: $ResourceLocation$Type): void
 "getDefaultConfigPath"(): string
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
declare module "packages/com/mrcrayfish/configured/network/message/$MessageSessionData" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $MessageSessionData extends $Record {
static readonly "ID": $ResourceLocation

constructor(developer: boolean, lan: boolean)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "decode"(arg0: $FriendlyByteBuf$Type): $MessageSessionData
public static "encode"(arg0: $MessageSessionData$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $MessageSessionData$Type, arg1: $Consumer$Type<($Runnable$Type)>): void
public "lan"(): boolean
public "developer"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageSessionData$Type = ($MessageSessionData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageSessionData_ = $MessageSessionData$Type;
}}
declare module "packages/com/mrcrayfish/configured/impl/jei/$JeiCategoryEntry" {
import {$IConfigEntry, $IConfigEntry$Type} from "packages/com/mrcrayfish/configured/api/$IConfigEntry"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IJeiConfigCategory, $IJeiConfigCategory$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigCategory"
import {$IConfigValue, $IConfigValue$Type} from "packages/com/mrcrayfish/configured/api/$IConfigValue"

export class $JeiCategoryEntry implements $IConfigEntry {

constructor(arg0: $IJeiConfigCategory$Type)

public "getValue"(): $IConfigValue<(any)>
public "isRoot"(): boolean
public "getEntryName"(): string
public "isLeaf"(): boolean
public "getChildren"(): $List<($IConfigEntry)>
public "getTooltip"(): $Component
public "getTranslationKey"(): string
get "value"(): $IConfigValue<(any)>
get "root"(): boolean
get "entryName"(): string
get "leaf"(): boolean
get "children"(): $List<($IConfigEntry)>
get "tooltip"(): $Component
get "translationKey"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiCategoryEntry$Type = ($JeiCategoryEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiCategoryEntry_ = $JeiCategoryEntry$Type;
}}
declare module "packages/com/mrcrayfish/catalogue/platform/$ForgePlatformHelper" {
import {$IPlatformHelper, $IPlatformHelper$Type} from "packages/com/mrcrayfish/catalogue/platform/services/$IPlatformHelper"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$File, $File$Type} from "packages/java/io/$File"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$IModData, $IModData$Type} from "packages/com/mrcrayfish/catalogue/client/$IModData"

export class $ForgePlatformHelper implements $IPlatformHelper {

constructor()

public "getAllModData"(): $List<($IModData)>
public "loadNativeImage"(arg0: string, arg1: string, arg2: $Consumer$Type<($NativeImage$Type)>): void
public "getModDirectory"(): $File
public "isForge"(): boolean
get "allModData"(): $List<($IModData)>
get "modDirectory"(): $File
get "forge"(): boolean
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
declare module "packages/com/mrcrayfish/configured/network/handler/$ForgeClientPlayHandler" {
import {$Connection, $Connection$Type} from "packages/net/minecraft/network/$Connection"
import {$MessageSyncForgeConfig, $MessageSyncForgeConfig$Type} from "packages/com/mrcrayfish/configured/network/message/play/$MessageSyncForgeConfig"

export class $ForgeClientPlayHandler {

constructor()

public static "handleSyncServerConfigMessage"(arg0: $Connection$Type, arg1: $MessageSyncForgeConfig$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeClientPlayHandler$Type = ($ForgeClientPlayHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeClientPlayHandler_ = $ForgeClientPlayHandler$Type;
}}
declare module "packages/com/mrcrayfish/catalogue/client/screen/$CatalogueModListScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $CatalogueModListScreen extends $Screen {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Screen$Type)

public "onClose"(): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CatalogueModListScreen$Type = ($CatalogueModListScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CatalogueModListScreen_ = $CatalogueModListScreen$Type;
}}
declare module "packages/com/mrcrayfish/configured/impl/framework/message/$MessageFramework" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MessageFramework {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageFramework$Type = ($MessageFramework);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageFramework_ = $MessageFramework$Type;
}}
declare module "packages/com/mrcrayfish/configured/network/handler/$ForgeServerPlayHandler" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$MessageSyncForgeConfig, $MessageSyncForgeConfig$Type} from "packages/com/mrcrayfish/configured/network/message/play/$MessageSyncForgeConfig"

export class $ForgeServerPlayHandler {

constructor()

public static "handleSyncServerConfigMessage"(arg0: $ServerPlayer$Type, arg1: $MessageSyncForgeConfig$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeServerPlayHandler$Type = ($ForgeServerPlayHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeServerPlayHandler_ = $ForgeServerPlayHandler$Type;
}}
declare module "packages/com/mrcrayfish/catalogue/client/$IModData" {
import {$IModData$Update, $IModData$Update$Type} from "packages/com/mrcrayfish/catalogue/client/$IModData$Update"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$IModData$Type, $IModData$Type$Type} from "packages/com/mrcrayfish/catalogue/client/$IModData$Type"

export interface $IModData {

 "getType"(): $IModData$Type
 "getDisplayName"(): string
 "getVersion"(): string
 "getDescription"(): string
 "getItemIcon"(): string
 "getUpdate"(): $IModData$Update
 "getBanner"(): string
 "getImageIcon"(): string
 "hasConfig"(): boolean
 "openConfigScreen"(arg0: $Screen$Type): void
 "isLogoSmooth"(): boolean
 "getHomepage"(): string
 "getIssueTracker"(): string
 "getBackground"(): string
 "getLicense"(): string
 "getAuthors"(): string
 "getCredits"(): string
 "getModId"(): string
}

export namespace $IModData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IModData$Type = ($IModData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IModData_ = $IModData$Type;
}}
declare module "packages/com/mrcrayfish/configured/impl/jei/$JeiValue" {
import {$IJeiConfigValue, $IJeiConfigValue$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigValue"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IConfigValue, $IConfigValue$Type} from "packages/com/mrcrayfish/configured/api/$IConfigValue"

export class $JeiValue<T> implements $IConfigValue<(T)> {

constructor(arg0: $IJeiConfigValue$Type<(T)>)

public "getName"(): string
public "get"(): T
public "getDefault"(): T
public "set"(arg0: T): void
public "isDefault"(): boolean
public "isValid"(arg0: T): boolean
public "getComment"(): $Component
public "getValidationHint"(): $Component
public "restore"(): void
public "isChanged"(): boolean
public "requiresGameRestart"(): boolean
public "requiresWorldRestart"(): boolean
public "cleanCache"(): void
public "getTranslationKey"(): string
public "updateConfigValue"(): void
get "name"(): string
get "default"(): T
get "default"(): boolean
get "comment"(): $Component
get "validationHint"(): $Component
get "changed"(): boolean
get "translationKey"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiValue$Type<T> = ($JeiValue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiValue_<T> = $JeiValue$Type<(T)>;
}}
declare module "packages/com/mrcrayfish/configured/$Bootstrap" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Bootstrap {

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Bootstrap$Type = ($Bootstrap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Bootstrap_ = $Bootstrap$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/$TooltipScreen" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $TooltipScreen extends $Screen {
 "tooltipText": $List<($FormattedCharSequence)>
 "tooltipOutlineColour": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering


public "setActiveTooltip"(arg0: $Component$Type): void
public "setActiveTooltip"(arg0: $Component$Type, arg1: integer): void
public "setActiveTooltip"(arg0: $List$Type<($FormattedCharSequence$Type)>): void
set "activeTooltip"(value: $Component$Type)
set "activeTooltip"(value: $List$Type<($FormattedCharSequence$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TooltipScreen$Type = ($TooltipScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TooltipScreen_ = $TooltipScreen$Type;
}}
declare module "packages/com/mrcrayfish/configured/network/message/play/$MessageSyncForgeConfig" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $MessageSyncForgeConfig extends $Record {

constructor(fileName: string, data: (byte)[])

public "equals"(arg0: any): boolean
public "toString"(): string
public "fileName"(): string
public "hashCode"(): integer
public static "decode"(arg0: $FriendlyByteBuf$Type): $MessageSyncForgeConfig
public static "encode"(arg0: $MessageSyncForgeConfig$Type, arg1: $FriendlyByteBuf$Type): void
public "data"(): (byte)[]
public static "handle"(arg0: $MessageSyncForgeConfig$Type, arg1: $Supplier$Type<($NetworkEvent$Context$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageSyncForgeConfig$Type = ($MessageSyncForgeConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageSyncForgeConfig_ = $MessageSyncForgeConfig$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/$ClientHandler" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$AbstractSelectionList, $AbstractSelectionList$Type} from "packages/net/minecraft/client/gui/components/$AbstractSelectionList"
import {$ModContext, $ModContext$Type} from "packages/com/mrcrayfish/configured/api/$ModContext"
import {$IModConfigProvider, $IModConfigProvider$Type} from "packages/com/mrcrayfish/configured/api/$IModConfigProvider"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IModConfig, $IModConfig$Type} from "packages/com/mrcrayfish/configured/api/$IModConfig"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ConfigType, $ConfigType$Type} from "packages/com/mrcrayfish/configured/api/$ConfigType"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ClientHandler {
static readonly "KEY_OPEN_MOD_LIST": $KeyMapping

constructor()

public static "init"(): void
public static "getProviders"(): $Set<($IModConfigProvider)>
public static "updateAbstractListTexture"(arg0: $AbstractSelectionList$Type<(any)>): void
public static "updateScreenTexture"(arg0: $Screen$Type): void
public static "createConfigMap"(arg0: $ModContext$Type): $Map<($ConfigType), ($Set<($IModConfig)>)>
get "providers"(): $Set<($IModConfigProvider)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientHandler$Type = ($ClientHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientHandler_ = $ClientHandler$Type;
}}
declare module "packages/com/mrcrayfish/configured/platform/services/$IConfigHelper" {
import {$IModConfigProvider, $IModConfigProvider$Type} from "packages/com/mrcrayfish/configured/api/$IModConfigProvider"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$LevelResource, $LevelResource$Type} from "packages/net/minecraft/world/level/storage/$LevelResource"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $IConfigHelper {

 "getProviders"(): $Set<($IModConfigProvider)>
 "getServerConfigResource"(): $LevelResource
 "getBackgroundTexture"(arg0: string): $ResourceLocation
}

export namespace $IConfigHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IConfigHelper$Type = ($IConfigHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IConfigHelper_ = $IConfigHelper$Type;
}}
declare module "packages/com/mrcrayfish/configured/network/$ServerPlayHelper" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"

export class $ServerPlayHelper {

constructor()

public static "canEditServerConfigs"(arg0: $ServerPlayer$Type): boolean
public static "sendMessageToOperators"(arg0: $Component$Type, arg1: $ServerPlayer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerPlayHelper$Type = ($ServerPlayHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerPlayHelper_ = $ServerPlayHelper$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/$ConfigScreen$BooleanItem" {
import {$ConfigScreen, $ConfigScreen$Type} from "packages/com/mrcrayfish/configured/client/screen/$ConfigScreen"
import {$ConfigScreen$ConfigItem, $ConfigScreen$ConfigItem$Type} from "packages/com/mrcrayfish/configured/client/screen/$ConfigScreen$ConfigItem"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IConfigValue, $IConfigValue$Type} from "packages/com/mrcrayfish/configured/api/$IConfigValue"

export class $ConfigScreen$BooleanItem extends $ConfigScreen$ConfigItem<(boolean)> {

constructor(arg0: $ConfigScreen$Type, arg1: $IConfigValue$Type<(any)>)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: boolean, arg9: float): void
public "onResetValue"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigScreen$BooleanItem$Type = ($ConfigScreen$BooleanItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigScreen$BooleanItem_ = $ConfigScreen$BooleanItem$Type;
}}
declare module "packages/com/mrcrayfish/configured/impl/jei/$JeiConfigProvider" {
import {$ModContext, $ModContext$Type} from "packages/com/mrcrayfish/configured/api/$ModContext"
import {$IModConfigProvider, $IModConfigProvider$Type} from "packages/com/mrcrayfish/configured/api/$IModConfigProvider"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IModConfig, $IModConfig$Type} from "packages/com/mrcrayfish/configured/api/$IModConfig"

export class $JeiConfigProvider implements $IModConfigProvider {

constructor()

public "getConfigurationsForMod"(arg0: $ModContext$Type): $Set<($IModConfig)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiConfigProvider$Type = ($JeiConfigProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiConfigProvider_ = $JeiConfigProvider$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/$ClientConfigHelper" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export class $ClientConfigHelper {

constructor()

public static "isPlayingGame"(): boolean
public static "isRunningLocalServer"(): boolean
public static "getClientPlayer"(): $Player
get "playingGame"(): boolean
get "runningLocalServer"(): boolean
get "clientPlayer"(): $Player
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientConfigHelper$Type = ($ClientConfigHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientConfigHelper_ = $ClientConfigHelper$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/widget/$CheckBoxButton$OnPress" {
import {$Checkbox, $Checkbox$Type} from "packages/net/minecraft/client/gui/components/$Checkbox"

export interface $CheckBoxButton$OnPress {

 "onPress"(arg0: $Checkbox$Type): void

(arg0: $Checkbox$Type): void
}

export namespace $CheckBoxButton$OnPress {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CheckBoxButton$OnPress$Type = ($CheckBoxButton$OnPress);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CheckBoxButton$OnPress_ = $CheckBoxButton$OnPress$Type;
}}
declare module "packages/com/mrcrayfish/configured/impl/forge/$ForgeEnumValue" {
import {$ForgeConfigSpec$EnumValue, $ForgeConfigSpec$EnumValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$EnumValue"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$IAllowedEnums, $IAllowedEnums$Type} from "packages/com/mrcrayfish/configured/api/$IAllowedEnums"
import {$ForgeConfigSpec$ValueSpec, $ForgeConfigSpec$ValueSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ValueSpec"
import {$ForgeValue, $ForgeValue$Type} from "packages/com/mrcrayfish/configured/impl/forge/$ForgeValue"

export class $ForgeEnumValue<T extends $Enum<(T)>> extends $ForgeValue<(T)> implements $IAllowedEnums<(T)> {
readonly "configValue": $ForgeConfigSpec$ConfigValue<(T)>
readonly "valueSpec": $ForgeConfigSpec$ValueSpec

constructor(arg0: $ForgeConfigSpec$EnumValue$Type<(T)>, arg1: $ForgeConfigSpec$ValueSpec$Type)

public "getAllowedValues"(): $Set<(T)>
get "allowedValues"(): $Set<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeEnumValue$Type<T> = ($ForgeEnumValue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeEnumValue_<T> = $ForgeEnumValue$Type<(T)>;
}}
declare module "packages/com/mrcrayfish/catalogue/client/$IModData$Update" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $IModData$Update extends $Record {

constructor(animated: boolean, url: string, texOffset: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "url"(): string
public "animated"(): boolean
public "texOffset"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IModData$Update$Type = ($IModData$Update);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IModData$Update_ = $IModData$Update$Type;
}}
declare module "packages/com/mrcrayfish/configured/$Configured" {
import {$PlayerEvent$PlayerLoggedInEvent, $PlayerEvent$PlayerLoggedInEvent$Type} from "packages/net/minecraftforge/event/entity/player/$PlayerEvent$PlayerLoggedInEvent"

export class $Configured {

constructor()

public "onPlayerLoggedIn"(arg0: $PlayerEvent$PlayerLoggedInEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Configured$Type = ($Configured);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Configured_ = $Configured$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/$ListMenuScreen$Item" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ILabelProvider, $ILabelProvider$Type} from "packages/com/mrcrayfish/configured/client/screen/$ILabelProvider"
import {$ContainerObjectSelectionList$Entry, $ContainerObjectSelectionList$Entry$Type} from "packages/net/minecraft/client/gui/components/$ContainerObjectSelectionList$Entry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ListMenuScreen, $ListMenuScreen$Type} from "packages/com/mrcrayfish/configured/client/screen/$ListMenuScreen"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ListMenuScreen$Item extends $ContainerObjectSelectionList$Entry<($ListMenuScreen$Item)> implements $ILabelProvider, $Comparable<($ListMenuScreen$Item)> {

constructor(arg0: $ListMenuScreen$Type, arg1: $Component$Type)
constructor(arg0: $ListMenuScreen$Type, arg1: string)

public "compareTo"(arg0: $ListMenuScreen$Item$Type): integer
public "children"(): $List<(any)>
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: boolean, arg9: float): void
public "narratables"(): $List<(any)>
public "getLabel"(): string
get "label"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListMenuScreen$Item$Type = ($ListMenuScreen$Item);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListMenuScreen$Item_ = $ListMenuScreen$Item$Type;
}}
declare module "packages/com/mrcrayfish/configured/impl/forge/$ForgeConfigProvider" {
import {$ModContext, $ModContext$Type} from "packages/com/mrcrayfish/configured/api/$ModContext"
import {$IModConfigProvider, $IModConfigProvider$Type} from "packages/com/mrcrayfish/configured/api/$IModConfigProvider"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IModConfig, $IModConfig$Type} from "packages/com/mrcrayfish/configured/api/$IModConfig"

export class $ForgeConfigProvider implements $IModConfigProvider {

constructor()

public "getConfigurationsForMod"(arg0: $ModContext$Type): $Set<($IModConfig)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeConfigProvider$Type = ($ForgeConfigProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeConfigProvider_ = $ForgeConfigProvider$Type;
}}
declare module "packages/com/mrcrayfish/configured/api/$IModConfigProvider" {
import {$ModContext, $ModContext$Type} from "packages/com/mrcrayfish/configured/api/$ModContext"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$IModConfig, $IModConfig$Type} from "packages/com/mrcrayfish/configured/api/$IModConfig"

export interface $IModConfigProvider {

 "getConfigurationsForMod"(arg0: $ModContext$Type): $Set<($IModConfig)>

(arg0: $ModContext$Type): $Set<($IModConfig)>
}

export namespace $IModConfigProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IModConfigProvider$Type = ($IModConfigProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IModConfigProvider_ = $IModConfigProvider$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/$ClientConfigured" {
import {$InputEvent$Key, $InputEvent$Key$Type} from "packages/net/minecraftforge/client/event/$InputEvent$Key"
import {$RenderTooltipEvent$GatherComponents, $RenderTooltipEvent$GatherComponents$Type} from "packages/net/minecraftforge/client/event/$RenderTooltipEvent$GatherComponents"
import {$RenderTooltipEvent$Color, $RenderTooltipEvent$Color$Type} from "packages/net/minecraftforge/client/event/$RenderTooltipEvent$Color"
import {$RegisterClientTooltipComponentFactoriesEvent, $RegisterClientTooltipComponentFactoriesEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterClientTooltipComponentFactoriesEvent"
import {$ScreenEvent$Opening, $ScreenEvent$Opening$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$Opening"
import {$RegisterKeyMappingsEvent, $RegisterKeyMappingsEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterKeyMappingsEvent"

export class $ClientConfigured {

constructor()

public static "onGatherTooltipComponents"(arg0: $RenderTooltipEvent$GatherComponents$Type): void
public static "onGetTooltipColor"(arg0: $RenderTooltipEvent$Color$Type): void
public static "onScreenOpen"(arg0: $ScreenEvent$Opening$Type): void
public static "onKeyPress"(arg0: $InputEvent$Key$Type): void
public static "onRegisterKeyMappings"(arg0: $RegisterKeyMappingsEvent$Type): void
public static "onRegisterTooltipComponentFactory"(arg0: $RegisterClientTooltipComponentFactoriesEvent$Type): void
public static "generateConfigFactories"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientConfigured$Type = ($ClientConfigured);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientConfigured_ = $ClientConfigured$Type;
}}
declare module "packages/com/mrcrayfish/configured/impl/jei/$ConfiguredJeiPlugin" {
import {$IGuiHandlerRegistration, $IGuiHandlerRegistration$Type} from "packages/mezz/jei/api/registration/$IGuiHandlerRegistration"
import {$IJeiConfigManager, $IJeiConfigManager$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigManager"
import {$IAdvancedRegistration, $IAdvancedRegistration$Type} from "packages/mezz/jei/api/registration/$IAdvancedRegistration"
import {$IVanillaCategoryExtensionRegistration, $IVanillaCategoryExtensionRegistration$Type} from "packages/mezz/jei/api/registration/$IVanillaCategoryExtensionRegistration"
import {$IRecipeTransferRegistration, $IRecipeTransferRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeTransferRegistration"
import {$IRecipeRegistration, $IRecipeRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IJeiRuntime, $IJeiRuntime$Type} from "packages/mezz/jei/api/runtime/$IJeiRuntime"
import {$IRecipeCatalystRegistration, $IRecipeCatalystRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCatalystRegistration"
import {$IModPlugin, $IModPlugin$Type} from "packages/mezz/jei/api/$IModPlugin"
import {$IRuntimeRegistration, $IRuntimeRegistration$Type} from "packages/mezz/jei/api/registration/$IRuntimeRegistration"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IRecipeCategoryRegistration, $IRecipeCategoryRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCategoryRegistration"
import {$IModIngredientRegistration, $IModIngredientRegistration$Type} from "packages/mezz/jei/api/registration/$IModIngredientRegistration"
import {$ISubtypeRegistration, $ISubtypeRegistration$Type} from "packages/mezz/jei/api/registration/$ISubtypeRegistration"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"

export class $ConfiguredJeiPlugin implements $IModPlugin {

constructor()

public static "getJeiConfigManager"(): $Optional<($IJeiConfigManager)>
public "onConfigManagerAvailable"(arg0: $IJeiConfigManager$Type): void
public "getPluginUid"(): $ResourceLocation
public "registerItemSubtypes"(arg0: $ISubtypeRegistration$Type): void
public "registerVanillaCategoryExtensions"(arg0: $IVanillaCategoryExtensionRegistration$Type): void
public "registerFluidSubtypes"<T>(arg0: $ISubtypeRegistration$Type, arg1: $IPlatformFluidHelper$Type<(T)>): void
public "registerGuiHandlers"(arg0: $IGuiHandlerRegistration$Type): void
public "onRuntimeUnavailable"(): void
public "registerIngredients"(arg0: $IModIngredientRegistration$Type): void
public "registerRecipeTransferHandlers"(arg0: $IRecipeTransferRegistration$Type): void
public "registerRecipeCatalysts"(arg0: $IRecipeCatalystRegistration$Type): void
public "registerRecipes"(arg0: $IRecipeRegistration$Type): void
public "registerAdvanced"(arg0: $IAdvancedRegistration$Type): void
public "onRuntimeAvailable"(arg0: $IJeiRuntime$Type): void
public "registerCategories"(arg0: $IRecipeCategoryRegistration$Type): void
public "registerRuntime"(arg0: $IRuntimeRegistration$Type): void
get "jeiConfigManager"(): $Optional<($IJeiConfigManager)>
get "pluginUid"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfiguredJeiPlugin$Type = ($ConfiguredJeiPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfiguredJeiPlugin_ = $ConfiguredJeiPlugin$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/widget/$CheckBoxButton" {
import {$Checkbox, $Checkbox$Type} from "packages/net/minecraft/client/gui/components/$Checkbox"
import {$CheckBoxButton$OnPress, $CheckBoxButton$OnPress$Type} from "packages/com/mrcrayfish/configured/client/screen/widget/$CheckBoxButton$OnPress"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $CheckBoxButton extends $Checkbox {
static readonly "ICONS": $ResourceLocation
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: integer, arg1: integer, arg2: $CheckBoxButton$OnPress$Type)

public "renderWidget"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "onPress"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CheckBoxButton$Type = ($CheckBoxButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CheckBoxButton_ = $CheckBoxButton$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/$RequestScreen" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$IModConfig, $IModConfig$Type} from "packages/com/mrcrayfish/configured/api/$IModConfig"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ListMenuScreen, $ListMenuScreen$Type} from "packages/com/mrcrayfish/configured/client/screen/$ListMenuScreen"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$IEditing, $IEditing$Type} from "packages/com/mrcrayfish/configured/client/screen/$IEditing"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"

export class $RequestScreen extends $ListMenuScreen implements $IEditing {
static readonly "CONFIGURED_LOGO": $ResourceLocation
 "tooltipText": $List<($FormattedCharSequence)>
 "tooltipOutlineColour": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering


public "getActiveConfig"(): $IModConfig
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "tick"(): void
public "handleResponse"(arg0: $IModConfig$Type, arg1: $Component$Type): void
public static "loadTexture"(arg0: any, arg1: $ResourceLocation$Type): $ResourceLocation
get "activeConfig"(): $IModConfig
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RequestScreen$Type = ($RequestScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RequestScreen_ = $RequestScreen$Type;
}}
declare module "packages/com/mrcrayfish/catalogue/platform/services/$IPlatformHelper" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$File, $File$Type} from "packages/java/io/$File"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NativeImage, $NativeImage$Type} from "packages/com/mojang/blaze3d/platform/$NativeImage"
import {$IModData, $IModData$Type} from "packages/com/mrcrayfish/catalogue/client/$IModData"

export interface $IPlatformHelper {

 "getAllModData"(): $List<($IModData)>
 "loadNativeImage"(arg0: string, arg1: string, arg2: $Consumer$Type<($NativeImage$Type)>): void
 "getModDirectory"(): $File
 "isForge"(): boolean
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
declare module "packages/com/mrcrayfish/configured/client/screen/$ConfigScreen$FolderItem" {
import {$IConfigEntry, $IConfigEntry$Type} from "packages/com/mrcrayfish/configured/api/$IConfigEntry"
import {$ConfigScreen, $ConfigScreen$Type} from "packages/com/mrcrayfish/configured/client/screen/$ConfigScreen"
import {$ListMenuScreen$Item, $ListMenuScreen$Item$Type} from "packages/com/mrcrayfish/configured/client/screen/$ListMenuScreen$Item"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ConfigScreen$FolderItem extends $ListMenuScreen$Item {

constructor(arg0: $ConfigScreen$Type, arg1: $IConfigEntry$Type)

public "children"(): $List<(any)>
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: boolean, arg9: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigScreen$FolderItem$Type = ($ConfigScreen$FolderItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigScreen$FolderItem_ = $ConfigScreen$FolderItem$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/$ConfigScreen$ConfigItem" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ConfigScreen, $ConfigScreen$Type} from "packages/com/mrcrayfish/configured/client/screen/$ConfigScreen"
import {$ListMenuScreen$Item, $ListMenuScreen$Item$Type} from "packages/com/mrcrayfish/configured/client/screen/$ListMenuScreen$Item"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IConfigValue, $IConfigValue$Type} from "packages/com/mrcrayfish/configured/api/$IConfigValue"

export class $ConfigScreen$ConfigItem<T> extends $ListMenuScreen$Item {

constructor(arg0: $ConfigScreen$Type, arg1: $IConfigValue$Type<(any)>)

public "children"(): $List<(any)>
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: boolean, arg9: float): void
public "setValidationHint"(arg0: $Component$Type): void
set "validationHint"(value: $Component$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigScreen$ConfigItem$Type<T> = ($ConfigScreen$ConfigItem<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigScreen$ConfigItem_<T> = $ConfigScreen$ConfigItem$Type<(T)>;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/$ModConfigSelectionScreen" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$IModConfig, $IModConfig$Type} from "packages/com/mrcrayfish/configured/api/$IModConfig"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ListMenuScreen, $ListMenuScreen$Type} from "packages/com/mrcrayfish/configured/client/screen/$ListMenuScreen"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ConfigType, $ConfigType$Type} from "packages/com/mrcrayfish/configured/api/$ConfigType"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ModConfigSelectionScreen extends $ListMenuScreen {
static readonly "CONFIGURED_LOGO": $ResourceLocation
 "tooltipText": $List<($FormattedCharSequence)>
 "tooltipOutlineColour": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Screen$Type, arg1: $Component$Type, arg2: $ResourceLocation$Type, arg3: $Map$Type<($ConfigType$Type), ($Set$Type<($IModConfig$Type)>)>)

public static "isPlayingRemotely"(): boolean
public static "canEditConfig"(arg0: $Player$Type, arg1: $IModConfig$Type): boolean
public static "canRestoreConfig"(arg0: $Player$Type, arg1: $IModConfig$Type): boolean
public static "isRunningLocalServer"(): boolean
public static "createLabelFromModConfig"(arg0: $IModConfig$Type): string
public static "loadTexture"(arg0: any, arg1: $ResourceLocation$Type): $ResourceLocation
get "playingRemotely"(): boolean
get "runningLocalServer"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModConfigSelectionScreen$Type = ($ModConfigSelectionScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModConfigSelectionScreen_ = $ModConfigSelectionScreen$Type;
}}
declare module "packages/com/mrcrayfish/configured/api/$ValueEntry" {
import {$IConfigEntry, $IConfigEntry$Type} from "packages/com/mrcrayfish/configured/api/$IConfigEntry"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IConfigValue, $IConfigValue$Type} from "packages/com/mrcrayfish/configured/api/$IConfigValue"

export class $ValueEntry implements $IConfigEntry {

constructor(arg0: $IConfigValue$Type<(any)>)

public "getValue"(): $IConfigValue<(any)>
public "isRoot"(): boolean
public "getEntryName"(): string
public "isLeaf"(): boolean
public "getChildren"(): $List<($IConfigEntry)>
public "getTooltip"(): $Component
public "getTranslationKey"(): string
get "value"(): $IConfigValue<(any)>
get "root"(): boolean
get "entryName"(): string
get "leaf"(): boolean
get "children"(): $List<($IConfigEntry)>
get "tooltip"(): $Component
get "translationKey"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueEntry$Type = ($ValueEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueEntry_ = $ValueEntry$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/$ChangeEnumScreen" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$IModConfig, $IModConfig$Type} from "packages/com/mrcrayfish/configured/api/$IModConfig"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$TooltipScreen, $TooltipScreen$Type} from "packages/com/mrcrayfish/configured/client/screen/$TooltipScreen"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$IEditing, $IEditing$Type} from "packages/com/mrcrayfish/configured/client/screen/$IEditing"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IBackgroundTexture, $IBackgroundTexture$Type} from "packages/com/mrcrayfish/configured/client/screen/$IBackgroundTexture"

export class $ChangeEnumScreen extends $TooltipScreen implements $IBackgroundTexture, $IEditing {
 "tooltipText": $List<($FormattedCharSequence)>
 "tooltipOutlineColour": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering


public "getActiveConfig"(): $IModConfig
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "getBackgroundTexture"(): $ResourceLocation
public static "loadTexture"(arg0: any, arg1: $ResourceLocation$Type): $ResourceLocation
get "activeConfig"(): $IModConfig
get "backgroundTexture"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChangeEnumScreen$Type = ($ChangeEnumScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChangeEnumScreen_ = $ChangeEnumScreen$Type;
}}
declare module "packages/com/mrcrayfish/configured/impl/jei/$JeiListValue" {
import {$IJeiConfigValue, $IJeiConfigValue$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigValue"
import {$IListType, $IListType$Type} from "packages/com/mrcrayfish/configured/client/screen/list/$IListType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IListConfigValue, $IListConfigValue$Type} from "packages/com/mrcrayfish/configured/client/screen/list/$IListConfigValue"
import {$JeiValue, $JeiValue$Type} from "packages/com/mrcrayfish/configured/impl/jei/$JeiValue"

export class $JeiListValue<T> extends $JeiValue<($List<(T)>)> implements $IListConfigValue<(T)> {

constructor(arg0: $IJeiConfigValue$Type<($List$Type<(T)>)>)

public "getListType"(): $IListType<(T)>
get "listType"(): $IListType<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiListValue$Type<T> = ($JeiListValue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiListValue_<T> = $JeiListValue$Type<(T)>;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/widget/$IconButton" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ConfiguredButton, $ConfiguredButton$Type} from "packages/com/mrcrayfish/configured/client/screen/widget/$ConfiguredButton"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $IconButton extends $ConfiguredButton {
static readonly "ICONS": $ResourceLocation
static readonly "SMALL_WIDTH": integer
static readonly "DEFAULT_WIDTH": integer
static readonly "DEFAULT_HEIGHT": integer
 "onPress": $Button$OnPress
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $Button$OnPress$Type)
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $Component$Type, arg6: $Button$OnPress$Type)

public "m_87963_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IconButton$Type = ($IconButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IconButton_ = $IconButton$Type;
}}
declare module "packages/com/mrcrayfish/configured/platform/$Services" {
import {$IConfigHelper, $IConfigHelper$Type} from "packages/com/mrcrayfish/configured/platform/services/$IConfigHelper"
import {$IPlatformHelper, $IPlatformHelper$Type} from "packages/com/mrcrayfish/configured/platform/services/$IPlatformHelper"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $Services {
static readonly "PLATFORM": $IPlatformHelper
static readonly "CONFIG": $IConfigHelper

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
declare module "packages/com/mrcrayfish/configured/client/screen/$WorldSelectionScreen" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$IModConfig, $IModConfig$Type} from "packages/com/mrcrayfish/configured/api/$IModConfig"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$ListMenuScreen, $ListMenuScreen$Type} from "packages/com/mrcrayfish/configured/client/screen/$ListMenuScreen"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"

export class $WorldSelectionScreen extends $ListMenuScreen {
static readonly "CONFIGURED_LOGO": $ResourceLocation
 "tooltipText": $List<($FormattedCharSequence)>
 "tooltipOutlineColour": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Screen$Type, arg1: $ResourceLocation$Type, arg2: $IModConfig$Type, arg3: $Component$Type)

public "onClose"(): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public static "loadTexture"(arg0: any, arg1: $ResourceLocation$Type): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldSelectionScreen$Type = ($WorldSelectionScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldSelectionScreen_ = $WorldSelectionScreen$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/list/$EnumListType" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$IListType, $IListType$Type} from "packages/com/mrcrayfish/configured/client/screen/list/$IListType"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$IAllowedEnums, $IAllowedEnums$Type} from "packages/com/mrcrayfish/configured/api/$IAllowedEnums"

export class $EnumListType<T extends $Enum<(T)>> implements $IListType<(T)>, $IAllowedEnums<(T)> {

constructor(arg0: $Class$Type<(T)>)

public "getHint"(): $Component
public "getAllowedValues"(): $Set<(T)>
public "getValueParser"(): $Function<(string), (T)>
public "getStringParser"(): $Function<(T), (string)>
get "hint"(): $Component
get "allowedValues"(): $Set<(T)>
get "valueParser"(): $Function<(string), (T)>
get "stringParser"(): $Function<(T), (string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumListType$Type<T> = ($EnumListType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumListType_<T> = $EnumListType$Type<(T)>;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/$ConfigScreen" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$IConfigEntry, $IConfigEntry$Type} from "packages/com/mrcrayfish/configured/api/$IConfigEntry"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$IModConfig, $IModConfig$Type} from "packages/com/mrcrayfish/configured/api/$IModConfig"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ListMenuScreen, $ListMenuScreen$Type} from "packages/com/mrcrayfish/configured/client/screen/$ListMenuScreen"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$IEditing, $IEditing$Type} from "packages/com/mrcrayfish/configured/client/screen/$IEditing"
import {$ListMenuScreen$Item, $ListMenuScreen$Item$Type} from "packages/com/mrcrayfish/configured/client/screen/$ListMenuScreen$Item"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ConfigScreen extends $ListMenuScreen implements $IEditing {
static readonly "TOOLTIP_WIDTH": integer
static readonly "SORT_ALPHABETICALLY": $Comparator<($ListMenuScreen$Item)>
static readonly "CONFIGURED_LOGO": $ResourceLocation
 "tooltipText": $List<($FormattedCharSequence)>
 "tooltipOutlineColour": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Screen$Type, arg1: $Component$Type, arg2: $IModConfig$Type, arg3: $ResourceLocation$Type)

public static "createLabel"(arg0: string): string
public "getActiveConfig"(): $IModConfig
public "shouldCloseOnEsc"(): boolean
public "isModified"(arg0: $IConfigEntry$Type): boolean
public "removed"(): void
public "isChanged"(arg0: $IConfigEntry$Type): boolean
public static "loadTexture"(arg0: any, arg1: $ResourceLocation$Type): $ResourceLocation
get "activeConfig"(): $IModConfig
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigScreen$Type = ($ConfigScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigScreen_ = $ConfigScreen$Type;
}}
declare module "packages/com/mrcrayfish/catalogue/client/screen/widget/$CatalogueCheckBoxButton" {
import {$Checkbox, $Checkbox$Type} from "packages/net/minecraft/client/gui/components/$Checkbox"
import {$CatalogueCheckBoxButton$OnPress, $CatalogueCheckBoxButton$OnPress$Type} from "packages/com/mrcrayfish/catalogue/client/screen/widget/$CatalogueCheckBoxButton$OnPress"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $CatalogueCheckBoxButton extends $Checkbox {
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: integer, arg1: integer, arg2: $CatalogueCheckBoxButton$OnPress$Type)

public "renderWidget"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "onPress"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CatalogueCheckBoxButton$Type = ($CatalogueCheckBoxButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CatalogueCheckBoxButton_ = $CatalogueCheckBoxButton$Type;
}}
declare module "packages/com/mrcrayfish/configured/api/$IConfigValue" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"

export interface $IConfigValue<T> {

 "getName"(): string
 "get"(): T
 "getDefault"(): T
 "set"(arg0: T): void
 "isDefault"(): boolean
 "isValid"(arg0: T): boolean
 "getComment"(): $Component
 "getValidationHint"(): $Component
 "restore"(): void
 "isChanged"(): boolean
 "requiresGameRestart"(): boolean
 "requiresWorldRestart"(): boolean
 "cleanCache"(): void
 "getTranslationKey"(): string
}

export namespace $IConfigValue {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IConfigValue$Type<T> = ($IConfigValue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IConfigValue_<T> = $IConfigValue$Type<(T)>;
}}
declare module "packages/com/mrcrayfish/catalogue/platform/$ClientServices" {
import {$IPlatformHelper, $IPlatformHelper$Type} from "packages/com/mrcrayfish/catalogue/platform/services/$IPlatformHelper"
import {$IComponentHelper, $IComponentHelper$Type} from "packages/com/mrcrayfish/catalogue/platform/services/$IComponentHelper"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $ClientServices {
static readonly "PLATFORM": $IPlatformHelper
static readonly "COMPONENT": $IComponentHelper

constructor()

public static "load"<T>(arg0: $Class$Type<(T)>): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientServices$Type = ($ClientServices);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientServices_ = $ClientServices$Type;
}}
declare module "packages/com/mrcrayfish/catalogue/$Constants" {
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"

export class $Constants {
static readonly "MOD_ID": string
static readonly "MOD_NAME": string
static readonly "LOG": $Logger

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Constants$Type = ($Constants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Constants_ = $Constants$Type;
}}
declare module "packages/com/mrcrayfish/configured/platform/$ForgePlatformHelper" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$IPlatformHelper, $IPlatformHelper$Type} from "packages/com/mrcrayfish/configured/platform/services/$IPlatformHelper"
import {$Environment, $Environment$Type} from "packages/com/mrcrayfish/configured/api/$Environment"
import {$ClientPacketListener, $ClientPacketListener$Type} from "packages/net/minecraft/client/multiplayer/$ClientPacketListener"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ForgePlatformHelper implements $IPlatformHelper {

constructor()

public "isDevelopmentEnvironment"(): boolean
public "getConfigPath"(): $Path
public "getGamePath"(): $Path
public "getPlatformName"(): string
public "sendSessionData"(arg0: $ServerPlayer$Type): void
public "isConnectionActive"(arg0: $ClientPacketListener$Type): boolean
public "getEnvironment"(): $Environment
public "sendFrameworkConfigResponse"(arg0: $ServerPlayer$Type, arg1: (byte)[]): void
public "sendFrameworkConfigToServer"(arg0: $ResourceLocation$Type, arg1: (byte)[]): void
public "sendFrameworkConfigRequest"(arg0: $ResourceLocation$Type): void
public "getDefaultConfigPath"(): string
public "isModLoaded"(arg0: string): boolean
public "getEnvironmentName"(): string
get "developmentEnvironment"(): boolean
get "configPath"(): $Path
get "gamePath"(): $Path
get "platformName"(): string
get "environment"(): $Environment
get "defaultConfigPath"(): string
get "environmentName"(): string
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
declare module "packages/com/mrcrayfish/configured/client/screen/widget/$ConfiguredButton" {
import {$Tooltip, $Tooltip$Type} from "packages/net/minecraft/client/gui/components/$Tooltip"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $ConfiguredButton extends $Button {
static readonly "SMALL_WIDTH": integer
static readonly "DEFAULT_WIDTH": integer
static readonly "DEFAULT_HEIGHT": integer
 "onPress": $Button$OnPress
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer


public "setTooltip"(arg0: $Tooltip$Type, arg1: $Predicate$Type<($Button$Type)>): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfiguredButton$Type = ($ConfiguredButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfiguredButton_ = $ConfiguredButton$Type;
}}
declare module "packages/com/mrcrayfish/configured/api/$ConfigType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Environment, $Environment$Type} from "packages/com/mrcrayfish/configured/api/$Environment"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export class $ConfigType extends $Enum<($ConfigType)> {
static readonly "CLIENT": $ConfigType
static readonly "UNIVERSAL": $ConfigType
static readonly "SERVER": $ConfigType
static readonly "SERVER_SYNC": $ConfigType
static readonly "DEDICATED_SERVER": $ConfigType
static readonly "WORLD": $ConfigType
static readonly "WORLD_SYNC": $ConfigType
static readonly "MEMORY": $ConfigType


public static "values"(): ($ConfigType)[]
public static "valueOf"(arg0: string): $ConfigType
public "isSync"(): boolean
public "getEnv"(): $Optional<($Environment)>
public "isServer"(): boolean
get "sync"(): boolean
get "env"(): $Optional<($Environment)>
get "server"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigType$Type = (("server") | ("world") | ("memory") | ("world_sync") | ("dedicated_server") | ("client") | ("universal") | ("server_sync")) | ($ConfigType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigType_ = $ConfigType$Type;
}}
declare module "packages/com/mrcrayfish/configured/impl/jei/$JeiCategoryListEntry" {
import {$IConfigEntry, $IConfigEntry$Type} from "packages/com/mrcrayfish/configured/api/$IConfigEntry"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IConfigValue, $IConfigValue$Type} from "packages/com/mrcrayfish/configured/api/$IConfigValue"

export class $JeiCategoryListEntry implements $IConfigEntry {

constructor(arg0: string, arg1: $List$Type<(any)>)

public "getValue"(): $IConfigValue<(any)>
public "isRoot"(): boolean
public "getEntryName"(): string
public "isLeaf"(): boolean
public "getChildren"(): $List<($IConfigEntry)>
public "getTooltip"(): $Component
public "getTranslationKey"(): string
get "value"(): $IConfigValue<(any)>
get "root"(): boolean
get "entryName"(): string
get "leaf"(): boolean
get "children"(): $List<($IConfigEntry)>
get "tooltip"(): $Component
get "translationKey"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JeiCategoryListEntry$Type = ($JeiCategoryListEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JeiCategoryListEntry_ = $JeiCategoryListEntry$Type;
}}
declare module "packages/com/mrcrayfish/configured/impl/framework/message/$MessageFramework$Response" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $MessageFramework$Response extends $Record {
static readonly "ID": $ResourceLocation

constructor(data: (byte)[])

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "decode"(arg0: $FriendlyByteBuf$Type): $MessageFramework$Response
public static "encode"(arg0: $MessageFramework$Response$Type, arg1: $FriendlyByteBuf$Type): void
public "data"(): (byte)[]
public static "handle"(arg0: $MessageFramework$Response$Type, arg1: $Consumer$Type<($Runnable$Type)>, arg2: $Consumer$Type<($Component$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageFramework$Response$Type = ($MessageFramework$Response);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageFramework$Response_ = $MessageFramework$Response$Type;
}}
declare module "packages/com/mrcrayfish/configured/$Constants" {
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"

export class $Constants {
static readonly "MOD_ID": string
static readonly "MOD_NAME": string
static readonly "LOG": $Logger

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Constants$Type = ($Constants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Constants_ = $Constants$Type;
}}
declare module "packages/com/mrcrayfish/configured/impl/forge/$ForgeListValue" {
import {$IListType, $IListType$Type} from "packages/com/mrcrayfish/configured/client/screen/list/$IListType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IListConfigValue, $IListConfigValue$Type} from "packages/com/mrcrayfish/configured/client/screen/list/$IListConfigValue"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$ForgeConfigSpec$ValueSpec, $ForgeConfigSpec$ValueSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ValueSpec"
import {$ForgeValue, $ForgeValue$Type} from "packages/com/mrcrayfish/configured/impl/forge/$ForgeValue"

export class $ForgeListValue<T> extends $ForgeValue<($List<(T)>)> implements $IListConfigValue<(T)> {
readonly "configValue": $ForgeConfigSpec$ConfigValue<(T)>
readonly "valueSpec": $ForgeConfigSpec$ValueSpec

constructor(arg0: $ForgeConfigSpec$ConfigValue$Type<($List$Type<(T)>)>, arg1: $ForgeConfigSpec$ValueSpec$Type)

public "set"(arg0: $List$Type<(T)>): void
public "getListType"(): $IListType<(T)>
public "getConverted"(): $List<(T)>
get "listType"(): $IListType<(T)>
get "converted"(): $List<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeListValue$Type<T> = ($ForgeListValue<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeListValue_<T> = $ForgeListValue$Type<(T)>;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/$ActiveConfirmationScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$IModConfig, $IModConfig$Type} from "packages/com/mrcrayfish/configured/api/$IModConfig"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$IEditing, $IEditing$Type} from "packages/com/mrcrayfish/configured/client/screen/$IEditing"
import {$ConfirmationScreen, $ConfirmationScreen$Type} from "packages/com/mrcrayfish/configured/client/screen/$ConfirmationScreen"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$ConfirmationScreen$Icon, $ConfirmationScreen$Icon$Type} from "packages/com/mrcrayfish/configured/client/screen/$ConfirmationScreen$Icon"
import {$List, $List$Type} from "packages/java/util/$List"

export class $ActiveConfirmationScreen extends $ConfirmationScreen implements $IEditing {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Screen$Type, arg1: $IModConfig$Type, arg2: $Component$Type, arg3: $ConfirmationScreen$Icon$Type, arg4: $Function$Type<(boolean), (boolean)>)

public "getActiveConfig"(): $IModConfig
public static "loadTexture"(arg0: any, arg1: $ResourceLocation$Type): $ResourceLocation
get "activeConfig"(): $IModConfig
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ActiveConfirmationScreen$Type = ($ActiveConfirmationScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ActiveConfirmationScreen_ = $ActiveConfirmationScreen$Type;
}}
declare module "packages/com/mrcrayfish/configured/client/screen/$IColouredTooltip" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export interface $IColouredTooltip {

 "getTooltipText"(): $List<($FormattedCharSequence)>
 "getTooltipY"(): integer
 "getTooltipX"(): integer
 "drawColouredTooltip"(arg0: $PoseStack$Type, arg1: integer, arg2: integer, arg3: $Screen$Type): boolean
 "getTooltipOutlineColour"(): integer
 "getTooltipBackgroundColour"(): integer
}

export namespace $IColouredTooltip {
const DUMMY_TOOLTIP: $List<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IColouredTooltip$Type = ($IColouredTooltip);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IColouredTooltip_ = $IColouredTooltip$Type;
}}
