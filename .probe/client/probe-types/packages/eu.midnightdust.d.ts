declare module "packages/eu/midnightdust/lib/config/$AutoCommand" {
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"

export class $AutoCommand {
static "commands": $List<($LiteralArgumentBuilder<($CommandSourceStack)>)>

constructor(entry: $Field$Type, modid: string)

public "register"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoCommand$Type = ($AutoCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoCommand_ = $AutoCommand$Type;
}}
declare module "packages/eu/midnightdust/lib/config/$MidnightConfig" {
import {$Tooltip, $Tooltip$Type} from "packages/net/minecraft/client/gui/components/$Tooltip"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$MidnightConfig$EntryInfo, $MidnightConfig$EntryInfo$Type} from "packages/eu/midnightdust/lib/config/$MidnightConfig$EntryInfo"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $MidnightConfig {
static readonly "configClass": $Map<(string), ($Class<(any)>)>

constructor()

public static "init"(modid: string, config: $Class$Type<(any)>): void
public static "write"(modid: string): void
public static "getScreen"(parent: $Screen$Type, modid: string): $Screen
public static "getTooltip"(info: $MidnightConfig$EntryInfo$Type): $Tooltip
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MidnightConfig$Type = ($MidnightConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MidnightConfig_ = $MidnightConfig$Type;
}}
declare module "packages/eu/midnightdust/core/$MidnightLibClient" {
import {$List, $List$Type} from "packages/java/util/$List"

export class $MidnightLibClient {
static "hiddenMods": $List<(string)>

constructor()

public static "onInitializeClient"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MidnightLibClient$Type = ($MidnightLibClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MidnightLibClient_ = $MidnightLibClient$Type;
}}
declare module "packages/eu/midnightdust/lib/util/$MidnightColorUtil" {
import {$Color, $Color$Type} from "packages/java/awt/$Color"

export class $MidnightColorUtil {
static "hue": float

constructor()

public static "tick"(): void
public static "radialRainbow"(saturation: float, brightness: float): $Color
public static "hex2Rgb"(colorStr: string): $Color
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MidnightColorUtil$Type = ($MidnightColorUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MidnightColorUtil_ = $MidnightColorUtil$Type;
}}
declare module "packages/eu/midnightdust/core/config/$MidnightLibConfig" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$MidnightConfig, $MidnightConfig$Type} from "packages/eu/midnightdust/lib/config/$MidnightConfig"
import {$MidnightLibConfig$ConfigButton, $MidnightLibConfig$ConfigButton$Type} from "packages/eu/midnightdust/core/config/$MidnightLibConfig$ConfigButton"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $MidnightLibConfig extends $MidnightConfig {
static "config_screen_list": $MidnightLibConfig$ConfigButton
static readonly "configClass": $Map<(string), ($Class<(any)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MidnightLibConfig$Type = ($MidnightLibConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MidnightLibConfig_ = $MidnightLibConfig$Type;
}}
declare module "packages/eu/midnightdust/forge/$MidnightLibServerEvents" {
import {$RegisterCommandsEvent, $RegisterCommandsEvent$Type} from "packages/net/minecraftforge/event/$RegisterCommandsEvent"

export class $MidnightLibServerEvents {

constructor()

public static "registerCommands"(event: $RegisterCommandsEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MidnightLibServerEvents$Type = ($MidnightLibServerEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MidnightLibServerEvents_ = $MidnightLibServerEvents$Type;
}}
declare module "packages/eu/midnightdust/lib/util/forge/$PlatformFunctionsImpl" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"

export class $PlatformFunctionsImpl {

constructor()

public static "registerCommand"(command: $LiteralArgumentBuilder$Type<($CommandSourceStack$Type)>): void
public static "getConfigDirectory"(): $Path
public static "isClientEnv"(): boolean
public static "isModLoaded"(modid: string): boolean
get "configDirectory"(): $Path
get "clientEnv"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformFunctionsImpl$Type = ($PlatformFunctionsImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformFunctionsImpl_ = $PlatformFunctionsImpl$Type;
}}
declare module "packages/eu/midnightdust/forge/$MidnightLibClientEvents" {
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"
import {$TickEvent$ClientTickEvent, $TickEvent$ClientTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ClientTickEvent"

export class $MidnightLibClientEvents {

constructor()

public static "registerClientTick"(event: $TickEvent$ClientTickEvent$Type): void
public static "onPostInit"(event: $FMLClientSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MidnightLibClientEvents$Type = ($MidnightLibClientEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MidnightLibClientEvents_ = $MidnightLibClientEvents$Type;
}}
declare module "packages/eu/midnightdust/lib/util/screen/$TexturedOverlayButtonWidget" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ImageButton, $ImageButton$Type} from "packages/net/minecraft/client/gui/components/$ImageButton"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $TexturedOverlayButtonWidget extends $ImageButton {
readonly "resourceLocation": $ResourceLocation
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

constructor(x: integer, y: integer, width: integer, height: integer, u: integer, v: integer, hoveredVOffset: integer, texture: $ResourceLocation$Type, textureWidth: integer, textureHeight: integer, pressAction: $Button$OnPress$Type, text: $Component$Type)
constructor(x: integer, y: integer, width: integer, height: integer, u: integer, v: integer, hoveredVOffset: integer, texture: $ResourceLocation$Type, textureWidth: integer, textureHeight: integer, pressAction: $Button$OnPress$Type)
constructor(x: integer, y: integer, width: integer, height: integer, u: integer, v: integer, hoveredVOffset: integer, texture: $ResourceLocation$Type, pressAction: $Button$OnPress$Type)
constructor(x: integer, y: integer, width: integer, height: integer, u: integer, v: integer, texture: $ResourceLocation$Type, pressAction: $Button$OnPress$Type)

public "renderWidget"(context: $GuiGraphics$Type, mouseX: integer, mouseY: integer, delta: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TexturedOverlayButtonWidget$Type = ($TexturedOverlayButtonWidget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TexturedOverlayButtonWidget_ = $TexturedOverlayButtonWidget$Type;
}}
declare module "packages/eu/midnightdust/core/screen/$MidnightConfigOverviewScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $MidnightConfigOverviewScreen extends $Screen {
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(parent: $Screen$Type)

public "render"(context: $GuiGraphics$Type, mouseX: integer, mouseY: integer, delta: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MidnightConfigOverviewScreen$Type = ($MidnightConfigOverviewScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MidnightConfigOverviewScreen_ = $MidnightConfigOverviewScreen$Type;
}}
declare module "packages/eu/midnightdust/lib/config/$MidnightConfig$EntryInfo" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MidnightConfig$EntryInfo {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MidnightConfig$EntryInfo$Type = ($MidnightConfig$EntryInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MidnightConfig$EntryInfo_ = $MidnightConfig$EntryInfo$Type;
}}
declare module "packages/eu/midnightdust/lib/util/$PlatformFunctions" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$LiteralArgumentBuilder, $LiteralArgumentBuilder$Type} from "packages/com/mojang/brigadier/builder/$LiteralArgumentBuilder"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"

export class $PlatformFunctions {

constructor()

public static "registerCommand"(command: $LiteralArgumentBuilder$Type<($CommandSourceStack$Type)>): void
public static "getConfigDirectory"(): $Path
public static "isClientEnv"(): boolean
public static "isModLoaded"(modid: string): boolean
get "configDirectory"(): $Path
get "clientEnv"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformFunctions$Type = ($PlatformFunctions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformFunctions_ = $PlatformFunctions$Type;
}}
declare module "packages/eu/midnightdust/forge/$MidnightLibForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MidnightLibForge {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MidnightLibForge$Type = ($MidnightLibForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MidnightLibForge_ = $MidnightLibForge$Type;
}}
declare module "packages/eu/midnightdust/core/config/$MidnightLibConfig$ConfigButton" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $MidnightLibConfig$ConfigButton extends $Enum<($MidnightLibConfig$ConfigButton)> {
static readonly "TRUE": $MidnightLibConfig$ConfigButton
static readonly "FALSE": $MidnightLibConfig$ConfigButton
static readonly "MODMENU": $MidnightLibConfig$ConfigButton


public static "values"(): ($MidnightLibConfig$ConfigButton)[]
public static "valueOf"(name: string): $MidnightLibConfig$ConfigButton
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MidnightLibConfig$ConfigButton$Type = (("modmenu") | ("true") | ("false")) | ($MidnightLibConfig$ConfigButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MidnightLibConfig$ConfigButton_ = $MidnightLibConfig$ConfigButton$Type;
}}
declare module "packages/eu/midnightdust/core/$MidnightLibServer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MidnightLibServer {

constructor()

public static "onInitializeServer"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MidnightLibServer$Type = ($MidnightLibServer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MidnightLibServer_ = $MidnightLibServer$Type;
}}
