declare module "packages/yalter/mousetweaks/forge/$ClientHelper" {
import {$ConfigScreenHandler$ConfigScreenFactory, $ConfigScreenHandler$ConfigScreenFactory$Type} from "packages/net/minecraftforge/client/$ConfigScreenHandler$ConfigScreenFactory"

export class $ClientHelper {

constructor()

public static "createConfigScreenFactory"(): $ConfigScreenHandler$ConfigScreenFactory
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
declare module "packages/yalter/mousetweaks/handlers/$IMTModGuiContainer3ExHandler" {
import {$MouseButton, $MouseButton$Type} from "packages/yalter/mousetweaks/$MouseButton"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$IMTModGuiContainer3Ex, $IMTModGuiContainer3Ex$Type} from "packages/yalter/mousetweaks/api/$IMTModGuiContainer3Ex"
import {$IGuiScreenHandler, $IGuiScreenHandler$Type} from "packages/yalter/mousetweaks/$IGuiScreenHandler"

export class $IMTModGuiContainer3ExHandler implements $IGuiScreenHandler {

constructor(arg0: $IMTModGuiContainer3Ex$Type)

public "getSlots"(): $List<($Slot)>
public "isIgnored"(arg0: $Slot$Type): boolean
public "isCraftingOutput"(arg0: $Slot$Type): boolean
public "clickSlot"(arg0: $Slot$Type, arg1: $MouseButton$Type, arg2: boolean): void
public "isWheelTweakDisabled"(): boolean
public "disableRMBDraggingFunctionality"(): boolean
public "isMouseTweaksDisabled"(): boolean
public "getSlotUnderMouse"(arg0: double, arg1: double): $Slot
get "slots"(): $List<($Slot)>
get "wheelTweakDisabled"(): boolean
get "mouseTweaksDisabled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMTModGuiContainer3ExHandler$Type = ($IMTModGuiContainer3ExHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMTModGuiContainer3ExHandler_ = $IMTModGuiContainer3ExHandler$Type;
}}
declare module "packages/yalter/mousetweaks/$Logger" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Logger {

constructor()

public static "Log"(arg0: string): void
public static "DebugLog"(arg0: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Logger$Type = ($Logger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Logger_ = $Logger$Type;
}}
declare module "packages/yalter/mousetweaks/$WheelScrollDirection" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $WheelScrollDirection extends $Enum<($WheelScrollDirection)> {
static readonly "NORMAL": $WheelScrollDirection
static readonly "INVERTED": $WheelScrollDirection
static readonly "INVENTORY_POSITION_AWARE": $WheelScrollDirection
static readonly "INVENTORY_POSITION_AWARE_INVERTED": $WheelScrollDirection


public static "values"(): ($WheelScrollDirection)[]
public static "valueOf"(arg0: string): $WheelScrollDirection
public "getValue"(): integer
public "isPositionAware"(): boolean
public "isInverted"(): boolean
public static "fromId"(arg0: integer): $WheelScrollDirection
get "value"(): integer
get "positionAware"(): boolean
get "inverted"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WheelScrollDirection$Type = (("normal") | ("inventory_position_aware") | ("inverted") | ("inventory_position_aware_inverted")) | ($WheelScrollDirection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WheelScrollDirection_ = $WheelScrollDirection$Type;
}}
declare module "packages/yalter/mousetweaks/handlers/$GuiContainerCreativeHandler" {
import {$GuiContainerHandler, $GuiContainerHandler$Type} from "packages/yalter/mousetweaks/handlers/$GuiContainerHandler"
import {$CreativeModeInventoryScreen, $CreativeModeInventoryScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$CreativeModeInventoryScreen"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"

export class $GuiContainerCreativeHandler extends $GuiContainerHandler {

constructor(arg0: $CreativeModeInventoryScreen$Type)

public "isIgnored"(arg0: $Slot$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiContainerCreativeHandler$Type = ($GuiContainerCreativeHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiContainerCreativeHandler_ = $GuiContainerCreativeHandler$Type;
}}
declare module "packages/yalter/mousetweaks/$ScrollHandling" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ScrollHandling extends $Enum<($ScrollHandling)> {
static readonly "SIMPLE": $ScrollHandling
static readonly "EVENT_BASED": $ScrollHandling


public static "values"(): ($ScrollHandling)[]
public static "valueOf"(arg0: string): $ScrollHandling
public "getValue"(): integer
public static "fromId"(arg0: integer): $ScrollHandling
get "value"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScrollHandling$Type = (("event_based") | ("simple")) | ($ScrollHandling);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScrollHandling_ = $ScrollHandling$Type;
}}
declare module "packages/yalter/mousetweaks/$Constants" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Constants {
static readonly "MOD_ID": string

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
declare module "packages/yalter/mousetweaks/handlers/$GuiContainerHandler" {
import {$MouseButton, $MouseButton$Type} from "packages/yalter/mousetweaks/$MouseButton"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"
import {$IGuiScreenHandler, $IGuiScreenHandler$Type} from "packages/yalter/mousetweaks/$IGuiScreenHandler"

export class $GuiContainerHandler implements $IGuiScreenHandler {

constructor(arg0: $AbstractContainerScreen$Type<(any)>)

public "getSlots"(): $List<($Slot)>
public "isIgnored"(arg0: $Slot$Type): boolean
public "isCraftingOutput"(arg0: $Slot$Type): boolean
public "clickSlot"(arg0: $Slot$Type, arg1: $MouseButton$Type, arg2: boolean): void
public "isWheelTweakDisabled"(): boolean
public "disableRMBDraggingFunctionality"(): boolean
public "isMouseTweaksDisabled"(): boolean
public "getSlotUnderMouse"(arg0: double, arg1: double): $Slot
get "slots"(): $List<($Slot)>
get "wheelTweakDisabled"(): boolean
get "mouseTweaksDisabled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiContainerHandler$Type = ($GuiContainerHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiContainerHandler_ = $GuiContainerHandler$Type;
}}
declare module "packages/yalter/mousetweaks/$MouseButton" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $MouseButton extends $Enum<($MouseButton)> {
static readonly "LEFT": $MouseButton
static readonly "RIGHT": $MouseButton


public static "values"(): ($MouseButton)[]
public static "valueOf"(arg0: string): $MouseButton
public "getValue"(): integer
public static "fromEventButton"(arg0: integer): $MouseButton
get "value"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MouseButton$Type = (("left") | ("right")) | ($MouseButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MouseButton_ = $MouseButton$Type;
}}
declare module "packages/yalter/mousetweaks/api/$IMTModGuiContainer3Ex" {
import {$ClickType, $ClickType$Type} from "packages/net/minecraft/world/inventory/$ClickType"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"

export interface $IMTModGuiContainer3Ex {

 "MT_isIgnored"(arg0: $Slot$Type): boolean
 "MT_clickSlot"(arg0: $Slot$Type, arg1: integer, arg2: $ClickType$Type): void
 "MT_getSlots"(): $List<($Slot)>
 "MT_isWheelTweakDisabled"(): boolean
 "MT_disableRMBDraggingFunctionality"(): boolean
 "MT_isMouseTweaksDisabled"(): boolean
 "MT_getSlotUnderMouse"(arg0: double, arg1: double): $Slot
 "MT_isCraftingOutput"(arg0: $Slot$Type): boolean
}

export namespace $IMTModGuiContainer3Ex {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMTModGuiContainer3Ex$Type = ($IMTModGuiContainer3Ex);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMTModGuiContainer3Ex_ = $IMTModGuiContainer3Ex$Type;
}}
declare module "packages/yalter/mousetweaks/forge/$MouseTweaksForge" {
import {$ScreenEvent$MouseScrolled$Post, $ScreenEvent$MouseScrolled$Post$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$MouseScrolled$Post"
import {$ScreenEvent$MouseDragged$Pre, $ScreenEvent$MouseDragged$Pre$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$MouseDragged$Pre"
import {$ScreenEvent$MouseButtonReleased$Pre, $ScreenEvent$MouseButtonReleased$Pre$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$MouseButtonReleased$Pre"
import {$ScreenEvent$MouseButtonPressed$Pre, $ScreenEvent$MouseButtonPressed$Pre$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$MouseButtonPressed$Pre"

export class $MouseTweaksForge {

constructor()

public "onGuiMouseDragPre"(arg0: $ScreenEvent$MouseDragged$Pre$Type): void
public "onGuiMouseScrollPost"(arg0: $ScreenEvent$MouseScrolled$Post$Type): void
public "onGuiMouseClickedPre"(arg0: $ScreenEvent$MouseButtonPressed$Pre$Type): void
public "onGuiMouseReleasedPre"(arg0: $ScreenEvent$MouseButtonReleased$Pre$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MouseTweaksForge$Type = ($MouseTweaksForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MouseTweaksForge_ = $MouseTweaksForge$Type;
}}
declare module "packages/yalter/mousetweaks/$Config" {
import {$WheelSearchOrder, $WheelSearchOrder$Type} from "packages/yalter/mousetweaks/$WheelSearchOrder"
import {$WheelScrollDirection, $WheelScrollDirection$Type} from "packages/yalter/mousetweaks/$WheelScrollDirection"
import {$ScrollItemScaling, $ScrollItemScaling$Type} from "packages/yalter/mousetweaks/$ScrollItemScaling"

export class $Config {
 "rmbTweak": boolean
 "lmbTweakWithItem": boolean
 "lmbTweakWithoutItem": boolean
 "wheelTweak": boolean
 "wheelSearchOrder": $WheelSearchOrder
 "wheelScrollDirection": $WheelScrollDirection
 "scrollItemScaling": $ScrollItemScaling
static "debug": boolean


public "read"(): void
public "save"(): void
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
declare module "packages/yalter/mousetweaks/api/$MouseTweaksIgnore" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $MouseTweaksIgnore extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $MouseTweaksIgnore {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MouseTweaksIgnore$Type = ($MouseTweaksIgnore);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MouseTweaksIgnore_ = $MouseTweaksIgnore$Type;
}}
declare module "packages/yalter/mousetweaks/$ScrollItemScaling" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ScrollItemScaling extends $Enum<($ScrollItemScaling)> {
static readonly "PROPORTIONAL": $ScrollItemScaling
static readonly "ALWAYS_ONE": $ScrollItemScaling


public static "values"(): ($ScrollItemScaling)[]
public static "valueOf"(arg0: string): $ScrollItemScaling
public "scale"(arg0: double): double
public "getValue"(): integer
public static "fromId"(arg0: integer): $ScrollItemScaling
get "value"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScrollItemScaling$Type = (("always_one") | ("proportional")) | ($ScrollItemScaling);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScrollItemScaling_ = $ScrollItemScaling$Type;
}}
declare module "packages/yalter/mousetweaks/$ConfigScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ConfigScreen extends $Screen {
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
public "removed"(): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
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
declare module "packages/yalter/mousetweaks/$WheelSearchOrder" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $WheelSearchOrder extends $Enum<($WheelSearchOrder)> {
static readonly "FIRST_TO_LAST": $WheelSearchOrder
static readonly "LAST_TO_FIRST": $WheelSearchOrder


public static "values"(): ($WheelSearchOrder)[]
public static "valueOf"(arg0: string): $WheelSearchOrder
public "getValue"(): integer
public static "fromId"(arg0: integer): $WheelSearchOrder
get "value"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WheelSearchOrder$Type = (("last_to_first") | ("first_to_last")) | ($WheelSearchOrder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WheelSearchOrder_ = $WheelSearchOrder$Type;
}}
declare module "packages/yalter/mousetweaks/$IMouseState" {
import {$MouseButton, $MouseButton$Type} from "packages/yalter/mousetweaks/$MouseButton"

export interface $IMouseState {

 "update"(): void
 "clear"(): void
 "isButtonPressed"(arg0: $MouseButton$Type): boolean
 "consumeScrollAmount"(): integer
}

export namespace $IMouseState {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMouseState$Type = ($IMouseState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMouseState_ = $IMouseState$Type;
}}
declare module "packages/yalter/mousetweaks/mixin/$AbstractContainerScreenAccessor" {
import {$ClickType, $ClickType$Type} from "packages/net/minecraft/world/inventory/$ClickType"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"

export interface $AbstractContainerScreenAccessor {

 "mousetweaks$invokeFindSlot"(arg0: double, arg1: double): $Slot
 "mousetweaks$invokeSlotClicked"(arg0: $Slot$Type, arg1: integer, arg2: integer, arg3: $ClickType$Type): void
 "mousetweaks$getIsQuickCrafting"(): boolean
 "mousetweaks$getQuickCraftingButton"(): integer
 "mousetweaks$setIsQuickCrafting"(arg0: boolean): void
 "mousetweaks$setSkipNextRelease"(arg0: boolean): void
}

export namespace $AbstractContainerScreenAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractContainerScreenAccessor$Type = ($AbstractContainerScreenAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractContainerScreenAccessor_ = $AbstractContainerScreenAccessor$Type;
}}
declare module "packages/yalter/mousetweaks/api/$MouseTweaksDisableWheelTweak" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $MouseTweaksDisableWheelTweak extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $MouseTweaksDisableWheelTweak {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MouseTweaksDisableWheelTweak$Type = ($MouseTweaksDisableWheelTweak);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MouseTweaksDisableWheelTweak_ = $MouseTweaksDisableWheelTweak$Type;
}}
declare module "packages/yalter/mousetweaks/$IGuiScreenHandler" {
import {$MouseButton, $MouseButton$Type} from "packages/yalter/mousetweaks/$MouseButton"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"

export interface $IGuiScreenHandler {

 "getSlots"(): $List<($Slot)>
 "isIgnored"(arg0: $Slot$Type): boolean
 "isCraftingOutput"(arg0: $Slot$Type): boolean
 "clickSlot"(arg0: $Slot$Type, arg1: $MouseButton$Type, arg2: boolean): void
 "isWheelTweakDisabled"(): boolean
 "disableRMBDraggingFunctionality"(): boolean
 "isMouseTweaksDisabled"(): boolean
 "getSlotUnderMouse"(arg0: double, arg1: double): $Slot
}

export namespace $IGuiScreenHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IGuiScreenHandler$Type = ($IGuiScreenHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IGuiScreenHandler_ = $IGuiScreenHandler$Type;
}}
declare module "packages/yalter/mousetweaks/$Main" {
import {$MouseButton, $MouseButton$Type} from "packages/yalter/mousetweaks/$MouseButton"
import {$Config, $Config$Type} from "packages/yalter/mousetweaks/$Config"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export class $Main {
static "config": $Config

constructor()

public static "initialize"(): void
public static "onMouseScrolled"(arg0: $Screen$Type, arg1: double, arg2: double, arg3: double): boolean
public static "onMouseDrag"(arg0: $Screen$Type, arg1: double, arg2: double, arg3: $MouseButton$Type): boolean
public static "onMouseReleased"(arg0: $Screen$Type, arg1: double, arg2: double, arg3: $MouseButton$Type): boolean
public static "onMouseClicked"(arg0: $Screen$Type, arg1: double, arg2: double, arg3: $MouseButton$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Main$Type = ($Main);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Main_ = $Main$Type;
}}
