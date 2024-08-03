declare module "packages/me/srrapero720/embeddiumplus/foundation/fps/$DebugOverlayEvent" {
import {$RenderGuiOverlayEvent$Pre, $RenderGuiOverlayEvent$Pre$Type} from "packages/net/minecraftforge/client/event/$RenderGuiOverlayEvent$Pre"
import {$RenderGuiEvent$Pre, $RenderGuiEvent$Pre$Type} from "packages/net/minecraftforge/client/event/$RenderGuiEvent$Pre"
import {$AverageQueue, $AverageQueue$Type} from "packages/me/srrapero720/embeddiumplus/foundation/fps/$AverageQueue"

export class $DebugOverlayEvent {
static readonly "AVERAGE": $AverageQueue

constructor()

public static "onRenderOverlayItem"(arg0: $RenderGuiOverlayEvent$Pre$Type): void
public static "onRenderOverlay"(arg0: $RenderGuiEvent$Pre$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DebugOverlayEvent$Type = ($DebugOverlayEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DebugOverlayEvent_ = $DebugOverlayEvent$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/$EmbyConfig$FPSDisplayGravity" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $EmbyConfig$FPSDisplayGravity extends $Enum<($EmbyConfig$FPSDisplayGravity)> {
static readonly "LEFT": $EmbyConfig$FPSDisplayGravity
static readonly "CENTER": $EmbyConfig$FPSDisplayGravity
static readonly "RIGHT": $EmbyConfig$FPSDisplayGravity


public static "values"(): ($EmbyConfig$FPSDisplayGravity)[]
public static "valueOf"(arg0: string): $EmbyConfig$FPSDisplayGravity
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbyConfig$FPSDisplayGravity$Type = (("left") | ("center") | ("right")) | ($EmbyConfig$FPSDisplayGravity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbyConfig$FPSDisplayGravity_ = $EmbyConfig$FPSDisplayGravity$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/$EmbyConfig" {
import {$ForgeConfigSpec$EnumValue, $ForgeConfigSpec$EnumValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$EnumValue"
import {$ForgeConfigSpec$IntValue, $ForgeConfigSpec$IntValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$IntValue"
import {$EmbyConfig$ChunkFadeSpeed, $EmbyConfig$ChunkFadeSpeed$Type} from "packages/me/srrapero720/embeddiumplus/$EmbyConfig$ChunkFadeSpeed"
import {$EmbyConfig$FPSDisplayMode, $EmbyConfig$FPSDisplayMode$Type} from "packages/me/srrapero720/embeddiumplus/$EmbyConfig$FPSDisplayMode"
import {$EmbyConfig$DarknessMode, $EmbyConfig$DarknessMode$Type} from "packages/me/srrapero720/embeddiumplus/$EmbyConfig$DarknessMode"
import {$EmbyConfig$FullScreenMode, $EmbyConfig$FullScreenMode$Type} from "packages/me/srrapero720/embeddiumplus/$EmbyConfig$FullScreenMode"
import {$EmbyConfig$LeavesCullingMode, $EmbyConfig$LeavesCullingMode$Type} from "packages/me/srrapero720/embeddiumplus/$EmbyConfig$LeavesCullingMode"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$EmbyConfig$DynLightsSpeed, $EmbyConfig$DynLightsSpeed$Type} from "packages/me/srrapero720/embeddiumplus/$EmbyConfig$DynLightsSpeed"
import {$EmbyConfig$FPSDisplaySystemMode, $EmbyConfig$FPSDisplaySystemMode$Type} from "packages/me/srrapero720/embeddiumplus/$EmbyConfig$FPSDisplaySystemMode"
import {$ForgeConfigSpec$BooleanValue, $ForgeConfigSpec$BooleanValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$BooleanValue"
import {$ForgeConfigSpec$DoubleValue, $ForgeConfigSpec$DoubleValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$DoubleValue"
import {$ModConfigEvent, $ModConfigEvent$Type} from "packages/net/minecraftforge/fml/event/config/$ModConfigEvent"
import {$Options, $Options$Type} from "packages/net/minecraft/client/$Options"
import {$Marker, $Marker$Type} from "packages/org/apache/logging/log4j/$Marker"
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$EmbyConfig$FPSDisplayGravity, $EmbyConfig$FPSDisplayGravity$Type} from "packages/me/srrapero720/embeddiumplus/$EmbyConfig$FPSDisplayGravity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$EmbyConfig$AttachMode, $EmbyConfig$AttachMode$Type} from "packages/me/srrapero720/embeddiumplus/$EmbyConfig$AttachMode"

export class $EmbyConfig {
static readonly "IT": $Marker
static readonly "SPECS": $ForgeConfigSpec
static readonly "fullScreen": $ForgeConfigSpec$EnumValue<($EmbyConfig$FullScreenMode)>
static readonly "fpsDisplayMode": $ForgeConfigSpec$EnumValue<($EmbyConfig$FPSDisplayMode)>
static readonly "fpsDisplayGravity": $ForgeConfigSpec$EnumValue<($EmbyConfig$FPSDisplayGravity)>
static readonly "fpsDisplaySystemMode": $ForgeConfigSpec$EnumValue<($EmbyConfig$FPSDisplaySystemMode)>
static readonly "fpsDisplayMargin": $ForgeConfigSpec$IntValue
static readonly "fpsDisplayShadow": $ForgeConfigSpec$BooleanValue
static "fpsDisplayMarginCache": integer
static "fpsDisplayShadowCache": boolean
static readonly "fog": $ForgeConfigSpec$BooleanValue
static readonly "blueBand": $ForgeConfigSpec$BooleanValue
static readonly "cloudsHeight": $ForgeConfigSpec$IntValue
static readonly "disableNameTagRender": $ForgeConfigSpec$BooleanValue
static readonly "chunkFadeSpeed": $ForgeConfigSpec$EnumValue<($EmbyConfig$ChunkFadeSpeed)>
static "fogCache": boolean
static "blueBandCache": boolean
static "cloudsHeightCache": integer
static "disableNameTagRenderCache": boolean
static readonly "darknessMode": $ForgeConfigSpec$EnumValue<($EmbyConfig$DarknessMode)>
static readonly "darknessOnOverworld": $ForgeConfigSpec$BooleanValue
static readonly "darknessOnNether": $ForgeConfigSpec$BooleanValue
static readonly "darknessNetherFogBright": $ForgeConfigSpec$DoubleValue
static readonly "darknessOnEnd": $ForgeConfigSpec$BooleanValue
static readonly "darknessEndFogBright": $ForgeConfigSpec$DoubleValue
static readonly "darknessByDefault": $ForgeConfigSpec$BooleanValue
static readonly "darknessDimensionWhiteList": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "darknessOnNoSkyLight": $ForgeConfigSpec$BooleanValue
static readonly "darknessBlockLightOnly": $ForgeConfigSpec$BooleanValue
static readonly "darknessAffectedByMoonPhase": $ForgeConfigSpec$BooleanValue
static readonly "darknessNewMoonBright": $ForgeConfigSpec$DoubleValue
static readonly "darknessFullMoonBright": $ForgeConfigSpec$DoubleValue
static "darknessOnOverworldCache": boolean
static "darknessOnNetherCache": boolean
static "darknessNetherFogBrightCache": double
static "darknessOnEndCache": boolean
static "darknessEndFogBrightCache": double
static "darknessByDefaultCache": boolean
static "darknessOnNoSkyLightCache": boolean
static "darknessBlockLightOnlyCache": boolean
static "darknessAffectedByMoonPhaseCache": boolean
static "darknessNewMoonBrightCache": double
static "darknessFullMoonBrightCache": double
static readonly "hideJREMI": $ForgeConfigSpec$BooleanValue
static readonly "fontShadows": $ForgeConfigSpec$BooleanValue
static readonly "leavesCulling": $ForgeConfigSpec$EnumValue<($EmbyConfig$LeavesCullingMode)>
static readonly "fastChests": $ForgeConfigSpec$BooleanValue
static readonly "fastBeds": $ForgeConfigSpec$BooleanValue
static "hideJREMICache": boolean
static "fontShadowsCache": boolean
static "fastChestsCache": boolean
static "fastBedsCache": boolean
static readonly "tileEntityDistanceCulling": $ForgeConfigSpec$BooleanValue
static readonly "tileEntityCullingDistanceX": $ForgeConfigSpec$IntValue
static readonly "tileEntityCullingDistanceY": $ForgeConfigSpec$IntValue
static readonly "entityDistanceCulling": $ForgeConfigSpec$BooleanValue
static readonly "entityCullingDistanceX": $ForgeConfigSpec$IntValue
static readonly "entityCullingDistanceY": $ForgeConfigSpec$IntValue
static readonly "monsterDistanceCulling": $ForgeConfigSpec$BooleanValue
static readonly "monsterCullingDistanceX": $ForgeConfigSpec$IntValue
static readonly "monsterCullingDistanceY": $ForgeConfigSpec$IntValue
static readonly "entityWhitelist": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "monsterWhitelist": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "tileEntityWhitelist": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static "tileEntityDistanceCullingCache": boolean
static "tileEntityCullingDistanceXCache": integer
static "tileEntityCullingDistanceYCache": integer
static "entityDistanceCullingCache": boolean
static "entityCullingDistanceXCache": integer
static "entityCullingDistanceYCache": integer
static "monsterDistanceCullingCache": boolean
static "monsterCullingDistanceXCache": integer
static "monsterCullingDistanceYCache": integer
static readonly "borderlessAttachModeF11": $ForgeConfigSpec$EnumValue<($EmbyConfig$AttachMode)>
static readonly "fastLanguageReload": $ForgeConfigSpec$BooleanValue
static "fastLanguageReloadCache": boolean
static readonly "dynLightSpeed": $ForgeConfigSpec$EnumValue<($EmbyConfig$DynLightsSpeed)>
static readonly "dynLightsOnEntities": $ForgeConfigSpec$BooleanValue
static readonly "dynLightsOnTileEntities": $ForgeConfigSpec$BooleanValue
static readonly "dynLightsUpdateOnPositionChange": $ForgeConfigSpec$BooleanValue
static "dynLightsOnEntitiesCache": boolean
static "dynLightsOnTileEntitiesCache": boolean
static "dynLightsUpdateOnPositionChangeCache": boolean

constructor()

public static "load"(): void
public static "isLoaded"(): boolean
public static "setFullScreenMode"(arg0: $Options$Type, arg1: $EmbyConfig$FullScreenMode$Type): void
public static "updateCache"(arg0: $ModConfigEvent$Type): void
get "loaded"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbyConfig$Type = ($EmbyConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbyConfig_ = $EmbyConfig$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/foundation/fps/$AverageQueue" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $AverageQueue {

constructor()

public "push"(arg0: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AverageQueue$Type = ($AverageQueue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AverageQueue_ = $AverageQueue$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/foundation/embeddium/$EmbPlusOptions" {
import {$OptionPageConstructionEvent, $OptionPageConstructionEvent$Type} from "packages/org/embeddedt/embeddium/api/$OptionPageConstructionEvent"
import {$OptionStorage, $OptionStorage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/storage/$OptionStorage"
import {$OptionGUIConstructionEvent, $OptionGUIConstructionEvent$Type} from "packages/org/embeddedt/embeddium/api/$OptionGUIConstructionEvent"
import {$OptionGroupConstructionEvent, $OptionGroupConstructionEvent$Type} from "packages/org/embeddedt/embeddium/api/$OptionGroupConstructionEvent"

export class $EmbPlusOptions {
static readonly "STORAGE": $OptionStorage<(any)>

constructor()

public static "onEmbeddiumPagesRegister"(arg0: $OptionGUIConstructionEvent$Type): void
public static "onEmbeddiumPagesRegister"(arg0: $OptionGroupConstructionEvent$Type): void
public static "onEmbeddiumGroupRegister"(arg0: $OptionPageConstructionEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbPlusOptions$Type = ($EmbPlusOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbPlusOptions_ = $EmbPlusOptions$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/foundation/embeddium/pages/$ZoomPage" {
import {$OptionIdentifier, $OptionIdentifier$Type} from "packages/org/embeddedt/embeddium/client/gui/options/$OptionIdentifier"
import {$OptionPage, $OptionPage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionPage"

/**
 * 
 * @deprecated
 */
export class $ZoomPage extends $OptionPage {
static readonly "ID": $OptionIdentifier<(void)>
static readonly "DEFAULT_ID": $OptionIdentifier<(void)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ZoomPage$Type = ($ZoomPage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ZoomPage_ = $ZoomPage$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/$EmbyConfig$DarknessMode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $EmbyConfig$DarknessMode extends $Enum<($EmbyConfig$DarknessMode)> {
static readonly "TOTAL_DARKNESS": $EmbyConfig$DarknessMode
static readonly "PITCH_BLACK": $EmbyConfig$DarknessMode
static readonly "DARK": $EmbyConfig$DarknessMode
static readonly "DIM": $EmbyConfig$DarknessMode
static readonly "OFF": $EmbyConfig$DarknessMode
readonly "value": float


public static "values"(): ($EmbyConfig$DarknessMode)[]
public static "valueOf"(arg0: string): $EmbyConfig$DarknessMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbyConfig$DarknessMode$Type = (("total_darkness") | ("pitch_black") | ("dark") | ("dim") | ("off")) | ($EmbyConfig$DarknessMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbyConfig$DarknessMode_ = $EmbyConfig$DarknessMode$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/util/$XenonInstalledException" {
import {$RuntimeException, $RuntimeException$Type} from "packages/java/lang/$RuntimeException"

export class $XenonInstalledException extends $RuntimeException {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $XenonInstalledException$Type = ($XenonInstalledException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $XenonInstalledException_ = $XenonInstalledException$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/$EmbyConfig$DynLightsSpeed" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $EmbyConfig$DynLightsSpeed extends $Enum<($EmbyConfig$DynLightsSpeed)> {
static readonly "OFF": $EmbyConfig$DynLightsSpeed
static readonly "SLOW": $EmbyConfig$DynLightsSpeed
static readonly "NORMAL": $EmbyConfig$DynLightsSpeed
static readonly "FAST": $EmbyConfig$DynLightsSpeed
static readonly "SUPERFAST": $EmbyConfig$DynLightsSpeed
static readonly "FASTESTS": $EmbyConfig$DynLightsSpeed
static readonly "REALTIME": $EmbyConfig$DynLightsSpeed


public static "values"(): ($EmbyConfig$DynLightsSpeed)[]
public static "valueOf"(arg0: string): $EmbyConfig$DynLightsSpeed
public "off"(): boolean
public "getDelay"(): integer
get "delay"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbyConfig$DynLightsSpeed$Type = (("normal") | ("realtime") | ("fast") | ("slow") | ("superfast") | ("fastests") | ("off")) | ($EmbyConfig$DynLightsSpeed);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbyConfig$DynLightsSpeed_ = $EmbyConfig$DynLightsSpeed$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/foundation/embeddium/storage/$EmbPlusOptionsStorage" {
import {$OptionStorage, $OptionStorage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/storage/$OptionStorage"

export class $EmbPlusOptionsStorage implements $OptionStorage<(any)> {

constructor()

public "save"(): void
public "getData"(): any
get "data"(): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbPlusOptionsStorage$Type = ($EmbPlusOptionsStorage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbPlusOptionsStorage_ = $EmbPlusOptionsStorage$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/foundation/embeddium/pages/$EntityCullingPage" {
import {$OptionIdentifier, $OptionIdentifier$Type} from "packages/org/embeddedt/embeddium/client/gui/options/$OptionIdentifier"
import {$OptionPage, $OptionPage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionPage"

export class $EntityCullingPage extends $OptionPage {
static readonly "ID": $OptionIdentifier<(void)>
static readonly "DEFAULT_ID": $OptionIdentifier<(void)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityCullingPage$Type = ($EntityCullingPage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityCullingPage_ = $EntityCullingPage$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/foundation/embeddium/pages/$OthersPage" {
import {$OptionIdentifier, $OptionIdentifier$Type} from "packages/org/embeddedt/embeddium/client/gui/options/$OptionIdentifier"
import {$OptionPage, $OptionPage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionPage"

export class $OthersPage extends $OptionPage {
static readonly "ID": $OptionIdentifier<(void)>
static readonly "DEFAULT_ID": $OptionIdentifier<(void)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OthersPage$Type = ($OthersPage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OthersPage_ = $OthersPage$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/util/$MockerInstalledException" {
import {$RuntimeException, $RuntimeException$Type} from "packages/java/lang/$RuntimeException"

export class $MockerInstalledException extends $RuntimeException {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MockerInstalledException$Type = ($MockerInstalledException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MockerInstalledException_ = $MockerInstalledException$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/mixins/$MixinPlugin" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $MixinPlugin implements $IMixinConfigPlugin {

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
export type $MixinPlugin$Type = ($MixinPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinPlugin_ = $MixinPlugin$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/foundation/entitydistance/$IWhitelistCheck" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IWhitelistCheck {

 "embPlus$isWhitelisted"(): boolean

(): boolean
}

export namespace $IWhitelistCheck {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IWhitelistCheck$Type = ($IWhitelistCheck);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IWhitelistCheck_ = $IWhitelistCheck$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/foundation/darkness/accessors/$TextureAccess" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $TextureAccess {

 "embPlus$enableUploadHook"(): void

(): void
}

export namespace $TextureAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextureAccess$Type = ($TextureAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextureAccess_ = $TextureAccess$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/foundation/fps/$FPSDisplay" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"

export class $FPSDisplay {

constructor()

public "add"(arg0: string): $FPSDisplay
public "add"(arg0: $Component$Type): $FPSDisplay
public "add"(arg0: $ChatFormatting$Type): $FPSDisplay
public "add"(arg0: integer): $FPSDisplay
public "toString"(): string
public "append"(arg0: string): $FPSDisplay
public "append"(arg0: $ChatFormatting$Type): $FPSDisplay
public "isEmpty"(): boolean
public "split"(): void
public "release"(): void
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FPSDisplay$Type = ($FPSDisplay);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FPSDisplay_ = $FPSDisplay$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/foundation/embeddium/pages/$MetricsPage" {
import {$OptionIdentifier, $OptionIdentifier$Type} from "packages/org/embeddedt/embeddium/client/gui/options/$OptionIdentifier"
import {$OptionPage, $OptionPage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionPage"

export class $MetricsPage extends $OptionPage {
static readonly "ID": $OptionIdentifier<(void)>
static readonly "DEFAULT_ID": $OptionIdentifier<(void)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MetricsPage$Type = ($MetricsPage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MetricsPage_ = $MetricsPage$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/foundation/leaves_culling/$LeavesCulling" {
import {$ICulleableLeaves, $ICulleableLeaves$Type} from "packages/me/srrapero720/embeddiumplus/foundation/leaves_culling/$ICulleableLeaves"
import {$LeavesBlock, $LeavesBlock$Type} from "packages/net/minecraft/world/level/block/$LeavesBlock"

export class $LeavesCulling {

constructor()

public static "fastLeaves"(): boolean
public static "should"(arg0: $LeavesBlock$Type, arg1: $ICulleableLeaves$Type, arg2: $LeavesBlock$Type, arg3: $ICulleableLeaves$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LeavesCulling$Type = ($LeavesCulling);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LeavesCulling_ = $LeavesCulling$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/mixins/impl/darkness/accessors/$LightTextureAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LightTextureAccessor {

 "embPlus$getFlicker"(): float
 "embPlus$isDirty"(): boolean
}

export namespace $LightTextureAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LightTextureAccessor$Type = ($LightTextureAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LightTextureAccessor_ = $LightTextureAccessor$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/$EmbyConfig$ChunkFadeSpeed" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $EmbyConfig$ChunkFadeSpeed extends $Enum<($EmbyConfig$ChunkFadeSpeed)> {
static readonly "OFF": $EmbyConfig$ChunkFadeSpeed
static readonly "FAST": $EmbyConfig$ChunkFadeSpeed
static readonly "SLOW": $EmbyConfig$ChunkFadeSpeed


public static "values"(): ($EmbyConfig$ChunkFadeSpeed)[]
public static "valueOf"(arg0: string): $EmbyConfig$ChunkFadeSpeed
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbyConfig$ChunkFadeSpeed$Type = (("fast") | ("slow") | ("off")) | ($EmbyConfig$ChunkFadeSpeed);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbyConfig$ChunkFadeSpeed_ = $EmbyConfig$ChunkFadeSpeed$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/$EmbyConfig$FullScreenMode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $EmbyConfig$FullScreenMode extends $Enum<($EmbyConfig$FullScreenMode)> {
static readonly "WINDOWED": $EmbyConfig$FullScreenMode
static readonly "BORDERLESS": $EmbyConfig$FullScreenMode
static readonly "FULLSCREEN": $EmbyConfig$FullScreenMode


public static "values"(): ($EmbyConfig$FullScreenMode)[]
public static "valueOf"(arg0: string): $EmbyConfig$FullScreenMode
public "isBorderless"(): boolean
public static "nextFullscreen"(arg0: $EmbyConfig$FullScreenMode$Type): $EmbyConfig$FullScreenMode
public static "getVanillaConfig"(): $EmbyConfig$FullScreenMode
public static "nextOf"(arg0: $EmbyConfig$FullScreenMode$Type): $EmbyConfig$FullScreenMode
public static "nextBorderless"(arg0: $EmbyConfig$FullScreenMode$Type): $EmbyConfig$FullScreenMode
get "borderless"(): boolean
get "vanillaConfig"(): $EmbyConfig$FullScreenMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbyConfig$FullScreenMode$Type = (("borderless") | ("fullscreen") | ("windowed")) | ($EmbyConfig$FullScreenMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbyConfig$FullScreenMode_ = $EmbyConfig$FullScreenMode$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/foundation/nametag/$NameTagToggle" {
import {$RenderNameTagEvent, $RenderNameTagEvent$Type} from "packages/net/minecraftforge/client/event/$RenderNameTagEvent"

export class $NameTagToggle {

constructor()

public static "onRenderNameTagEvent"(arg0: $RenderNameTagEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NameTagToggle$Type = ($NameTagToggle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NameTagToggle_ = $NameTagToggle$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/$EmbyConfig$FPSDisplaySystemMode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $EmbyConfig$FPSDisplaySystemMode extends $Enum<($EmbyConfig$FPSDisplaySystemMode)> {
static readonly "OFF": $EmbyConfig$FPSDisplaySystemMode
static readonly "ON": $EmbyConfig$FPSDisplaySystemMode
static readonly "GPU": $EmbyConfig$FPSDisplaySystemMode
static readonly "RAM": $EmbyConfig$FPSDisplaySystemMode


public static "values"(): ($EmbyConfig$FPSDisplaySystemMode)[]
public static "valueOf"(arg0: string): $EmbyConfig$FPSDisplaySystemMode
public "off"(): boolean
public "gpu"(): boolean
public "ram"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbyConfig$FPSDisplaySystemMode$Type = (("gpu") | ("off") | ("on") | ("ram")) | ($EmbyConfig$FPSDisplaySystemMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbyConfig$FPSDisplaySystemMode_ = $EmbyConfig$FPSDisplaySystemMode$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/$EmbyConfig$AttachMode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $EmbyConfig$AttachMode extends $Enum<($EmbyConfig$AttachMode)> {
static readonly "ATTACH": $EmbyConfig$AttachMode
static readonly "REPLACE": $EmbyConfig$AttachMode
static readonly "OFF": $EmbyConfig$AttachMode


public static "values"(): ($EmbyConfig$AttachMode)[]
public static "valueOf"(arg0: string): $EmbyConfig$AttachMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbyConfig$AttachMode$Type = (("replace") | ("attach") | ("off")) | ($EmbyConfig$AttachMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbyConfig$AttachMode_ = $EmbyConfig$AttachMode$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/foundation/embeddium/pages/$QualityPlusPage" {
import {$OptionIdentifier, $OptionIdentifier$Type} from "packages/org/embeddedt/embeddium/client/gui/options/$OptionIdentifier"
import {$OptionPage, $OptionPage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionPage"

export class $QualityPlusPage extends $OptionPage {
static readonly "ID": $OptionIdentifier<(void)>
static readonly "DEFAULT_ID": $OptionIdentifier<(void)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QualityPlusPage$Type = ($QualityPlusPage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QualityPlusPage_ = $QualityPlusPage$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/$EmbyConfig$LeavesCullingMode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $EmbyConfig$LeavesCullingMode extends $Enum<($EmbyConfig$LeavesCullingMode)> {
static readonly "ALL": $EmbyConfig$LeavesCullingMode
static readonly "OFF": $EmbyConfig$LeavesCullingMode


public static "values"(): ($EmbyConfig$LeavesCullingMode)[]
public static "valueOf"(arg0: string): $EmbyConfig$LeavesCullingMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbyConfig$LeavesCullingMode$Type = (("all") | ("off")) | ($EmbyConfig$LeavesCullingMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbyConfig$LeavesCullingMode_ = $EmbyConfig$LeavesCullingMode$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/foundation/embeddium/pages/$TrueDarknessPage" {
import {$OptionIdentifier, $OptionIdentifier$Type} from "packages/org/embeddedt/embeddium/client/gui/options/$OptionIdentifier"
import {$OptionPage, $OptionPage$Type} from "packages/me/jellysquid/mods/sodium/client/gui/options/$OptionPage"

export class $TrueDarknessPage extends $OptionPage {
static readonly "ID": $OptionIdentifier<(void)>
static readonly "DEFAULT_ID": $OptionIdentifier<(void)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TrueDarknessPage$Type = ($TrueDarknessPage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TrueDarknessPage_ = $TrueDarknessPage$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/foundation/fastmodels/$FastModels" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $FastModels {

constructor()

public static "canUseOnChests"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastModels$Type = ($FastModels);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastModels_ = $FastModels$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/foundation/darkness/$DarknessPlus" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$GameRenderer, $GameRenderer$Type} from "packages/net/minecraft/client/renderer/$GameRenderer"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $DarknessPlus {
static readonly "MIN": double
static "enabled": boolean

constructor()

public static "luminance"(arg0: float, arg1: float, arg2: float): float
public static "darken"(arg0: integer, arg1: integer, arg2: integer): integer
public static "updateLuminance"(arg0: float, arg1: $Minecraft$Type, arg2: $GameRenderer$Type, arg3: float): void
public static "getDarkFogColor"(arg0: $Vec3$Type, arg1: double): $Vec3
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DarknessPlus$Type = ($DarknessPlus);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DarknessPlus_ = $DarknessPlus$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/$EmbyConfig$FPSDisplayMode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $EmbyConfig$FPSDisplayMode extends $Enum<($EmbyConfig$FPSDisplayMode)> {
static readonly "OFF": $EmbyConfig$FPSDisplayMode
static readonly "SIMPLE": $EmbyConfig$FPSDisplayMode
static readonly "ADVANCED": $EmbyConfig$FPSDisplayMode


public static "values"(): ($EmbyConfig$FPSDisplayMode)[]
public static "valueOf"(arg0: string): $EmbyConfig$FPSDisplayMode
public "off"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbyConfig$FPSDisplayMode$Type = (("advanced") | ("simple") | ("off")) | ($EmbyConfig$FPSDisplayMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbyConfig$FPSDisplayMode_ = $EmbyConfig$FPSDisplayMode$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/foundation/leaves_culling/$ICulleableLeaves" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $ICulleableLeaves {

 "embplus$activeNeighbors"(): integer
 "embplus$getResourceLocation"(): $ResourceLocation
}

export namespace $ICulleableLeaves {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ICulleableLeaves$Type = ($ICulleableLeaves);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ICulleableLeaves_ = $ICulleableLeaves$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/$EmbyTools" {
import {$Pair, $Pair$Type} from "packages/it/unimi/dsi/fastutil/$Pair"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EmbyTools {

constructor()

public static "resourceLocationPair"(arg0: string): $Pair<(string), (string)>
public static "isModInstalled"(arg0: string): boolean
public static "colorByPercent"(arg0: integer): $ChatFormatting
public static "colorByLow"(arg0: integer): $ChatFormatting
public static "isEntityInRange"(arg0: $BlockPos$Type, arg1: $Vec3$Type, arg2: integer, arg3: integer): boolean
public static "isEntityInRange"(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: integer, arg3: integer): boolean
public static "isEntityInRange"(arg0: $Entity$Type, arg1: double, arg2: double, arg3: double, arg4: integer, arg5: integer): boolean
public static "ramUsed"(): long
public static "tintByPercent"(arg0: long): string
public static "tintByLower"(arg0: integer): string
public static "getColorARGB"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): integer
public static "bytesToMB"(arg0: long): long
public static "isWhitelisted"(arg0: $ResourceLocation$Type, arg1: $ForgeConfigSpec$ConfigValue$Type<($List$Type<(any)>)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbyTools$Type = ($EmbyTools);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbyTools_ = $EmbyTools$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/$EmbeddiumPlus" {
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $EmbeddiumPlus {
static readonly "ID": string
static readonly "LOGGER": $Logger

constructor()

public "onGameStarting"(arg0: $FMLClientSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmbeddiumPlus$Type = ($EmbeddiumPlus);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmbeddiumPlus_ = $EmbeddiumPlus$Type;
}}
declare module "packages/me/srrapero720/embeddiumplus/mixins/impl/borderless/accessors/$MainWindowAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $MainWindowAccessor {

 "setDirty"(arg0: boolean): void

(arg0: boolean): void
}

export namespace $MainWindowAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MainWindowAccessor$Type = ($MainWindowAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MainWindowAccessor_ = $MainWindowAccessor$Type;
}}
