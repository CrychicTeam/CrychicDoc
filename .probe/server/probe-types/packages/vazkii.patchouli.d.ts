declare module "packages/vazkii/patchouli/api/$BookDrawScreenEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $BookDrawScreenEvent extends $Event {

constructor()
constructor(arg0: $ResourceLocation$Type, arg1: $Screen$Type, arg2: integer, arg3: integer, arg4: float, arg5: $GuiGraphics$Type)

public "getBook"(): $ResourceLocation
public "isCancelable"(): boolean
public "getGraphics"(): $GuiGraphics
public "getMouseX"(): integer
public "getMouseY"(): integer
public "getScreen"(): $Screen
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
public "getPartialTicks"(): float
get "book"(): $ResourceLocation
get "cancelable"(): boolean
get "graphics"(): $GuiGraphics
get "mouseX"(): integer
get "mouseY"(): integer
get "screen"(): $Screen
get "listenerList"(): $ListenerList
get "partialTicks"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookDrawScreenEvent$Type = ($BookDrawScreenEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookDrawScreenEvent_ = $BookDrawScreenEvent$Type;
}}
declare module "packages/vazkii/patchouli/client/book/gui/button/$GuiButtonBookEye" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$GuiButtonBook, $GuiButtonBook$Type} from "packages/vazkii/patchouli/client/book/gui/button/$GuiButtonBook"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $GuiButtonBookEye extends $GuiButtonBook {
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

constructor(arg0: $GuiBook$Type, arg1: integer, arg2: integer, arg3: $Button$OnPress$Type)

public "m_87963_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiButtonBookEye$Type = ($GuiButtonBookEye);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiButtonBookEye_ = $GuiButtonBookEye$Type;
}}
declare module "packages/vazkii/patchouli/api/$IStyleStack" {
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$Style, $Style$Type} from "packages/net/minecraft/network/chat/$Style"

export interface $IStyleStack {

 "modifyStyle"(arg0: $UnaryOperator$Type<($Style$Type)>): void
 "pushStyle"(arg0: $Style$Type): void
 "popStyle"(): $Style
 "reset"(): void
 "peekStyle"(): $Style
}

export namespace $IStyleStack {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IStyleStack$Type = ($IStyleStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IStyleStack_ = $IStyleStack$Type;
}}
declare module "packages/vazkii/patchouli/mixin/client/$AccessorScreen" {
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"

export interface $AccessorScreen {

 "getRenderables"(): $List<($Renderable)>
 "getNarratables"(): $List<($NarratableEntry)>
}

export namespace $AccessorScreen {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessorScreen$Type = ($AccessorScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessorScreen_ = $AccessorScreen$Type;
}}
declare module "packages/vazkii/patchouli/common/handler/$LecternEventHandler" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export class $LecternEventHandler {

constructor()

public static "rightClick"(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $BlockHitResult$Type): $InteractionResult
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LecternEventHandler$Type = ($LecternEventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LecternEventHandler_ = $LecternEventHandler$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/$PageStonecutting" {
import {$StonecutterRecipe, $StonecutterRecipe$Type} from "packages/net/minecraft/world/item/crafting/$StonecutterRecipe"
import {$PageSimpleProcessingRecipe, $PageSimpleProcessingRecipe$Type} from "packages/vazkii/patchouli/client/book/page/abstr/$PageSimpleProcessingRecipe"

export class $PageStonecutting extends $PageSimpleProcessingRecipe<($StonecutterRecipe)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageStonecutting$Type = ($PageStonecutting);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageStonecutting_ = $PageStonecutting$Type;
}}
declare module "packages/vazkii/patchouli/client/book/text/$BookTextParser" {
import {$BookTextParser$FunctionProcessor, $BookTextParser$FunctionProcessor$Type} from "packages/vazkii/patchouli/client/book/text/$BookTextParser$FunctionProcessor"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BookTextParser$CommandProcessor, $BookTextParser$CommandProcessor$Type} from "packages/vazkii/patchouli/client/book/text/$BookTextParser$CommandProcessor"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$Style, $Style$Type} from "packages/net/minecraft/network/chat/$Style"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"
import {$Span, $Span$Type} from "packages/vazkii/patchouli/client/book/text/$Span"

export class $BookTextParser {
static readonly "EMPTY_STRING_COMPONENT": $MutableComponent

constructor(arg0: $GuiBook$Type, arg1: $Book$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: $Style$Type)

public "expandMacros"(arg0: string): string
public static "register"(arg0: $BookTextParser$CommandProcessor$Type, ...arg1: (string)[]): void
public static "register"(arg0: $BookTextParser$FunctionProcessor$Type, ...arg1: (string)[]): void
public "parse"(arg0: $Component$Type): $List<($Span)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookTextParser$Type = ($BookTextParser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookTextParser_ = $BookTextParser$Type;
}}
declare module "packages/vazkii/patchouli/api/$IVariableSerializer" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"

export interface $IVariableSerializer<T> {

 "fromJson"(arg0: $JsonElement$Type): T
 "toJson"(arg0: T): $JsonElement
}

export namespace $IVariableSerializer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IVariableSerializer$Type<T> = ($IVariableSerializer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IVariableSerializer_<T> = $IVariableSerializer$Type<(T)>;
}}
declare module "packages/vazkii/patchouli/xplat/$XplatModContainer" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$List, $List$Type} from "packages/java/util/$List"

export interface $XplatModContainer {

 "getName"(): string
 "getId"(): string
 "getPath"(arg0: string): $Path
 "getRootPaths"(): $List<($Path)>
}

export namespace $XplatModContainer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $XplatModContainer$Type = ($XplatModContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $XplatModContainer_ = $XplatModContainer$Type;
}}
declare module "packages/vazkii/patchouli/common/util/$RotationUtil" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"

export class $RotationUtil {


public static "rotationFromFacing"(arg0: $Direction$Type): $Rotation
public static "fixHorizontal"(arg0: $Rotation$Type): $Rotation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RotationUtil$Type = ($RotationUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RotationUtil_ = $RotationUtil$Type;
}}
declare module "packages/vazkii/patchouli/client/gui/$GuiButtonInventoryBook" {
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"

export class $GuiButtonInventoryBook extends $Button {
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

constructor(arg0: $Book$Type, arg1: integer, arg2: integer)

public "getBook"(): $Book
public "m_87963_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
get "book"(): $Book
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiButtonInventoryBook$Type = ($GuiButtonInventoryBook);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiButtonInventoryBook_ = $GuiButtonInventoryBook$Type;
}}
declare module "packages/vazkii/patchouli/api/$PatchouliAPI" {
import {$PatchouliAPI$IPatchouliAPI, $PatchouliAPI$IPatchouliAPI$Type} from "packages/vazkii/patchouli/api/$PatchouliAPI$IPatchouliAPI"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $PatchouliAPI {
static readonly "MOD_ID": string
static readonly "LOGGER": $Logger

constructor()

public static "get"(): $PatchouliAPI$IPatchouliAPI
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PatchouliAPI$Type = ($PatchouliAPI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PatchouliAPI_ = $PatchouliAPI$Type;
}}
declare module "packages/vazkii/patchouli/forge/common/$ForgeModInitializer" {
import {$RegisterEvent, $RegisterEvent$Type} from "packages/net/minecraftforge/registries/$RegisterEvent"
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"
import {$BuildCreativeModeTabContentsEvent, $BuildCreativeModeTabContentsEvent$Type} from "packages/net/minecraftforge/event/$BuildCreativeModeTabContentsEvent"

export class $ForgeModInitializer {

constructor()

public static "register"(arg0: $RegisterEvent$Type): void
public static "onInitialize"(arg0: $FMLCommonSetupEvent$Type): void
public static "processCreativeTabs"(arg0: $BuildCreativeModeTabContentsEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeModInitializer$Type = ($ForgeModInitializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeModInitializer_ = $ForgeModInitializer$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/$PageSmithing" {
import {$SmithingRecipe, $SmithingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$SmithingRecipe"
import {$PageDoubleRecipeRegistry, $PageDoubleRecipeRegistry$Type} from "packages/vazkii/patchouli/client/book/page/abstr/$PageDoubleRecipeRegistry"

export class $PageSmithing extends $PageDoubleRecipeRegistry<($SmithingRecipe)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageSmithing$Type = ($PageSmithing);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageSmithing_ = $PageSmithing$Type;
}}
declare module "packages/vazkii/patchouli/api/$IStateMatcher" {
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$TriPredicate, $TriPredicate$Type} from "packages/vazkii/patchouli/api/$TriPredicate"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $IStateMatcher {

 "getStatePredicate"(): $TriPredicate<($BlockGetter), ($BlockPos), ($BlockState)>
 "getDisplayedState"(arg0: long): $BlockState
}

export namespace $IStateMatcher {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IStateMatcher$Type = ($IStateMatcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IStateMatcher_ = $IStateMatcher$Type;
}}
declare module "packages/vazkii/patchouli/client/gui/$GuiAdvancementsExt" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$ClientAdvancements, $ClientAdvancements$Type} from "packages/net/minecraft/client/multiplayer/$ClientAdvancements"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$AdvancementsScreen, $AdvancementsScreen$Type} from "packages/net/minecraft/client/gui/screens/advancements/$AdvancementsScreen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $GuiAdvancementsExt extends $AdvancementsScreen {
static readonly "TABS_LOCATION": $ResourceLocation
static readonly "WINDOW_WIDTH": integer
static readonly "WINDOW_HEIGHT": integer
static readonly "WINDOW_INSIDE_WIDTH": integer
static readonly "WINDOW_INSIDE_HEIGHT": integer
static readonly "BACKGROUND_TILE_WIDTH": integer
static readonly "BACKGROUND_TILE_HEIGHT": integer
static readonly "BACKGROUND_TILE_COUNT_X": integer
static readonly "BACKGROUND_TILE_COUNT_Y": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $ClientAdvancements$Type, arg1: $Screen$Type, arg2: $ResourceLocation$Type)

public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiAdvancementsExt$Type = ($GuiAdvancementsExt);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiAdvancementsExt_ = $GuiAdvancementsExt$Type;
}}
declare module "packages/vazkii/patchouli/client/book/gui/$GuiBookHistory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$GuiBookEntryList, $GuiBookEntryList$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBookEntryList"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"

export class $GuiBookHistory extends $GuiBookEntryList {
static readonly "ENTRIES_PER_PAGE": integer
static readonly "ENTRIES_IN_FIRST_PAGE": integer
static readonly "FULL_WIDTH": integer
static readonly "FULL_HEIGHT": integer
static readonly "PAGE_WIDTH": integer
static readonly "PAGE_HEIGHT": integer
static readonly "TOP_PADDING": integer
static readonly "LEFT_PAGE_X": integer
static readonly "RIGHT_PAGE_X": integer
static readonly "TEXT_LINE_HEIGHT": integer
static readonly "MAX_BOOKMARKS": integer
readonly "book": $Book
 "bookLeft": integer
 "bookTop": integer
 "ticksInBook": integer
 "maxScale": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Book$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiBookHistory$Type = ($GuiBookHistory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiBookHistory_ = $GuiBookHistory$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/component/$ComponentSeparator" {
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$BookPage, $BookPage$Type} from "packages/vazkii/patchouli/client/book/$BookPage"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$TemplateComponent, $TemplateComponent$Type} from "packages/vazkii/patchouli/client/book/template/$TemplateComponent"

export class $ComponentSeparator extends $TemplateComponent {
 "group": string
 "x": integer
 "y": integer
 "flag": string
 "advancement": string
 "guard": string

constructor()

public "build"(arg0: $BookContentsBuilder$Type, arg1: $BookPage$Type, arg2: $BookEntry$Type, arg3: integer): void
public "render"(arg0: $GuiGraphics$Type, arg1: $BookPage$Type, arg2: integer, arg3: integer, arg4: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComponentSeparator$Type = ($ComponentSeparator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComponentSeparator_ = $ComponentSeparator$Type;
}}
declare module "packages/vazkii/patchouli/client/book/gui/button/$GuiButtonBook" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$List, $List$Type} from "packages/java/util/$List"
import {$SoundManager, $SoundManager$Type} from "packages/net/minecraft/client/sounds/$SoundManager"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $GuiButtonBook extends $Button {
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

constructor(arg0: $GuiBook$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: $Button$OnPress$Type, ...arg8: ($Component$Type)[])
constructor(arg0: $GuiBook$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: $Supplier$Type<(boolean)>, arg8: $Button$OnPress$Type, ...arg9: ($Component$Type)[])

public "getTooltipLines"(): $List<($Component)>
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "m_87963_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "playDownSound"(arg0: $SoundManager$Type): void
get "tooltipLines"(): $List<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiButtonBook$Type = ($GuiButtonBook);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiButtonBook_ = $GuiButtonBook$Type;
}}
declare module "packages/vazkii/patchouli/client/book/text/$SpanState" {
import {$IStyleStack, $IStyleStack$Type} from "packages/vazkii/patchouli/api/$IStyleStack"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$Style, $Style$Type} from "packages/net/minecraft/network/chat/$Style"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$TextColor, $TextColor$Type} from "packages/net/minecraft/network/chat/$TextColor"
import {$Span, $Span$Type} from "packages/vazkii/patchouli/client/book/text/$Span"

export class $SpanState implements $IStyleStack {
readonly "gui": $GuiBook
readonly "book": $Book
 "tooltip": $MutableComponent
 "onClick": $Supplier<(boolean)>
 "cluster": $List<($Span)>
 "isExternalLink": boolean
 "endingExternal": boolean
 "lineBreaks": integer
 "spacingLeft": integer
 "spacingRight": integer
readonly "spaceWidth": integer

constructor(arg0: $GuiBook$Type, arg1: $Book$Type, arg2: $Style$Type)

public "modifyStyle"(arg0: $UnaryOperator$Type<($Style$Type)>): void
public "pushStyle"(arg0: $Style$Type): void
public "popStyle"(): $Style
public "changeBaseStyle"(arg0: $Style$Type): void
public "reset"(): void
public "color"(arg0: $TextColor$Type): void
public "getBase"(): $Style
public "baseColor"(): void
public "peekStyle"(): $Style
get "base"(): $Style
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SpanState$Type = ($SpanState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SpanState_ = $SpanState$Type;
}}
declare module "packages/vazkii/patchouli/client/book/$BookContentLoader" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$BookContentLoader$LoadResult, $BookContentLoader$LoadResult$Type} from "packages/vazkii/patchouli/client/book/$BookContentLoader$LoadResult"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"

export interface $BookContentLoader {

 "loadJson"(arg0: $Book$Type, arg1: $ResourceLocation$Type): $BookContentLoader$LoadResult
 "findFiles"(arg0: $Book$Type, arg1: string, arg2: $List$Type<($ResourceLocation$Type)>): void
}

export namespace $BookContentLoader {
function streamToJson(arg0: $InputStream$Type): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookContentLoader$Type = ($BookContentLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookContentLoader_ = $BookContentLoader$Type;
}}
declare module "packages/vazkii/patchouli/common/util/$ItemStackUtil$StackWrapper" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $ItemStackUtil$StackWrapper {
static readonly "EMPTY_WRAPPER": $ItemStackUtil$StackWrapper
readonly "stack": $ItemStack

constructor(arg0: $ItemStack$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemStackUtil$StackWrapper$Type = ($ItemStackUtil$StackWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemStackUtil$StackWrapper_ = $ItemStackUtil$StackWrapper$Type;
}}
declare module "packages/vazkii/patchouli/common/base/$PatchouliConfig" {
import {$PatchouliConfigAccess, $PatchouliConfigAccess$Type} from "packages/vazkii/patchouli/api/$PatchouliConfigAccess"

export class $PatchouliConfig {

constructor()

public static "get"(): $PatchouliConfigAccess
public static "set"(arg0: $PatchouliConfigAccess$Type): void
public static "getConfigFlagAND"(arg0: (string)[]): boolean
public static "reloadBuiltinFlags"(): void
public static "getConfigFlagOR"(arg0: (string)[]): boolean
public static "getConfigFlag"(arg0: string): boolean
public static "setFlag"(arg0: string, arg1: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PatchouliConfig$Type = ($PatchouliConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PatchouliConfig_ = $PatchouliConfig$Type;
}}
declare module "packages/vazkii/patchouli/common/book/$BookFolderLoader" {
import {$File, $File$Type} from "packages/java/io/$File"

export class $BookFolderLoader {
static "loadDir": $File

constructor()

public static "findBooks"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookFolderLoader$Type = ($BookFolderLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookFolderLoader_ = $BookFolderLoader$Type;
}}
declare module "packages/vazkii/patchouli/mixin/$AccessorSmithingTransformRecipe" {
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"

export interface $AccessorSmithingTransformRecipe {

 "getBase"(): $Ingredient
 "getAddition"(): $Ingredient
 "getTemplate"(): $Ingredient
}

export namespace $AccessorSmithingTransformRecipe {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessorSmithingTransformRecipe$Type = ($AccessorSmithingTransformRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessorSmithingTransformRecipe_ = $AccessorSmithingTransformRecipe$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/$JsonVariableWrapper" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"
import {$IVariableProvider, $IVariableProvider$Type} from "packages/vazkii/patchouli/api/$IVariableProvider"

export class $JsonVariableWrapper implements $IVariableProvider {

constructor(arg0: $JsonObject$Type)

public "get"(arg0: string): $IVariable
public "has"(arg0: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonVariableWrapper$Type = ($JsonVariableWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonVariableWrapper_ = $JsonVariableWrapper$Type;
}}
declare module "packages/vazkii/patchouli/client/base/$PersistentData" {
import {$PersistentData$DataHolder, $PersistentData$DataHolder$Type} from "packages/vazkii/patchouli/client/base/$PersistentData$DataHolder"

export class $PersistentData {
static "data": $PersistentData$DataHolder

constructor()

public static "setup"(): void
public static "save"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PersistentData$Type = ($PersistentData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PersistentData_ = $PersistentData$Type;
}}
declare module "packages/vazkii/patchouli/common/util/$ItemStackUtil" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Triple, $Triple$Type} from "packages/org/apache/commons/lang3/tuple/$Triple"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"
import {$ItemStackUtil$StackWrapper, $ItemStackUtil$StackWrapper$Type} from "packages/vazkii/patchouli/common/util/$ItemStackUtil$StackWrapper"

export class $ItemStackUtil {


public static "loadIngredientFromString"(arg0: string): $Ingredient
public static "loadStackListFromString"(arg0: string): $List<($ItemStack)>
public static "loadStackFromJson"(arg0: $JsonObject$Type): $ItemStack
public static "loadFromParsed"(arg0: $Triple$Type<($ResourceLocation$Type), (integer), ($CompoundTag$Type)>): $ItemStack
public static "getBookFromStack"(arg0: $ItemStack$Type): $Book
public static "wrapStack"(arg0: $ItemStack$Type): $ItemStackUtil$StackWrapper
public static "loadStackFromString"(arg0: string): $ItemStack
public static "parseItemStackString"(arg0: string): $Triple<($ResourceLocation), (integer), ($CompoundTag)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemStackUtil$Type = ($ItemStackUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemStackUtil_ = $ItemStackUtil$Type;
}}
declare module "packages/vazkii/patchouli/client/book/$BookContentResourceListenerLoader" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$SimpleJsonResourceReloadListener, $SimpleJsonResourceReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimpleJsonResourceReloadListener"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$BookContentLoader$LoadResult, $BookContentLoader$LoadResult$Type} from "packages/vazkii/patchouli/client/book/$BookContentLoader$LoadResult"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"
import {$BookContentLoader, $BookContentLoader$Type} from "packages/vazkii/patchouli/client/book/$BookContentLoader"

export class $BookContentResourceListenerLoader extends $SimpleJsonResourceReloadListener implements $BookContentLoader {
static readonly "INSTANCE": $BookContentResourceListenerLoader


public "loadJson"(arg0: $Book$Type, arg1: $ResourceLocation$Type): $BookContentLoader$LoadResult
public "findFiles"(arg0: $Book$Type, arg1: string, arg2: $List$Type<($ResourceLocation$Type)>): void
public static "streamToJson"(arg0: $InputStream$Type): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookContentResourceListenerLoader$Type = ($BookContentResourceListenerLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookContentResourceListenerLoader_ = $BookContentResourceListenerLoader$Type;
}}
declare module "packages/vazkii/patchouli/api/$PatchouliConfigAccess$TextOverflowMode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $PatchouliConfigAccess$TextOverflowMode extends $Enum<($PatchouliConfigAccess$TextOverflowMode)> {
static readonly "OVERFLOW": $PatchouliConfigAccess$TextOverflowMode
static readonly "TRUNCATE": $PatchouliConfigAccess$TextOverflowMode
static readonly "RESIZE": $PatchouliConfigAccess$TextOverflowMode


public static "values"(): ($PatchouliConfigAccess$TextOverflowMode)[]
public static "valueOf"(arg0: string): $PatchouliConfigAccess$TextOverflowMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PatchouliConfigAccess$TextOverflowMode$Type = (("overflow") | ("truncate") | ("resize")) | ($PatchouliConfigAccess$TextOverflowMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PatchouliConfigAccess$TextOverflowMode_ = $PatchouliConfigAccess$TextOverflowMode$Type;
}}
declare module "packages/vazkii/patchouli/common/base/$PatchouliSounds" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $PatchouliSounds {
static readonly "BOOK_OPEN": $SoundEvent
static readonly "BOOK_FLIP": $SoundEvent

constructor()

public static "submitRegistrations"(arg0: $BiConsumer$Type<($ResourceLocation$Type), ($SoundEvent$Type)>): void
public static "getSound"(arg0: $ResourceLocation$Type, arg1: $SoundEvent$Type): $SoundEvent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PatchouliSounds$Type = ($PatchouliSounds);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PatchouliSounds_ = $PatchouliSounds$Type;
}}
declare module "packages/vazkii/patchouli/client/base/$PersistentData$Bookmark" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"

export class $PersistentData$Bookmark {
readonly "entry": $ResourceLocation
readonly "spread": integer

constructor(arg0: $ResourceLocation$Type, arg1: integer)
constructor(arg0: $JsonObject$Type)

public "getEntry"(arg0: $Book$Type): $BookEntry
public "serialize"(): $JsonObject
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PersistentData$Bookmark$Type = ($PersistentData$Bookmark);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PersistentData$Bookmark_ = $PersistentData$Bookmark$Type;
}}
declare module "packages/vazkii/patchouli/client/book/$BookPage" {
import {$GuiBookEntry, $GuiBookEntry$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBookEntry"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"

export class $BookPage {

constructor()

public "build"(arg0: $Level$Type, arg1: $BookEntry$Type, arg2: $BookContentsBuilder$Type, arg3: integer): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "onDisplayed"(arg0: $GuiBookEntry$Type, arg1: integer, arg2: integer): void
public "isPageUnlocked"(): boolean
public "onHidden"(arg0: $GuiBookEntry$Type): void
public "canAdd"(arg0: $Book$Type): boolean
public "i18nText"(arg0: string): $Component
public "i18n"(arg0: string): string
get "pageUnlocked"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookPage$Type = ($BookPage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookPage_ = $BookPage$Type;
}}
declare module "packages/vazkii/patchouli/mixin/$AccessorSmithingTrimRecipe" {
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"

export interface $AccessorSmithingTrimRecipe {

 "getBase"(): $Ingredient
 "getAddition"(): $Ingredient
 "getTemplate"(): $Ingredient
}

export namespace $AccessorSmithingTrimRecipe {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessorSmithingTrimRecipe$Type = ($AccessorSmithingTrimRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessorSmithingTrimRecipe_ = $AccessorSmithingTrimRecipe$Type;
}}
declare module "packages/vazkii/patchouli/client/book/gui/button/$GuiButtonBookResize" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$GuiButtonBook, $GuiButtonBook$Type} from "packages/vazkii/patchouli/client/book/gui/button/$GuiButtonBook"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $GuiButtonBookResize extends $GuiButtonBook {
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

constructor(arg0: $GuiBook$Type, arg1: integer, arg2: integer, arg3: $Button$OnPress$Type)

public "getTooltipLines"(): $List<($Component)>
get "tooltipLines"(): $List<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiButtonBookResize$Type = ($GuiButtonBookResize);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiButtonBookResize_ = $GuiButtonBookResize$Type;
}}
declare module "packages/vazkii/patchouli/common/handler/$ReloadContentsHandler" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export class $ReloadContentsHandler {

constructor()

public static "dataReloaded"(arg0: $MinecraftServer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReloadContentsHandler$Type = ($ReloadContentsHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReloadContentsHandler_ = $ReloadContentsHandler$Type;
}}
declare module "packages/vazkii/patchouli/common/multiblock/$SerializedMultiblock" {
import {$DenseMultiblock, $DenseMultiblock$Type} from "packages/vazkii/patchouli/common/multiblock/$DenseMultiblock"
import {$AbstractMultiblock, $AbstractMultiblock$Type} from "packages/vazkii/patchouli/common/multiblock/$AbstractMultiblock"

export class $SerializedMultiblock {

constructor()

public "deserializeDense"(): $DenseMultiblock
public "toMultiblock"(): $AbstractMultiblock
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SerializedMultiblock$Type = ($SerializedMultiblock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SerializedMultiblock_ = $SerializedMultiblock$Type;
}}
declare module "packages/vazkii/patchouli/api/$IMultiblock$SimulateResult" {
import {$IStateMatcher, $IStateMatcher$Type} from "packages/vazkii/patchouli/api/$IStateMatcher"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $IMultiblock$SimulateResult {

 "test"(arg0: $Level$Type, arg1: $Rotation$Type): boolean
 "getWorldPosition"(): $BlockPos
 "getStateMatcher"(): $IStateMatcher
 "getCharacter"(): character
}

export namespace $IMultiblock$SimulateResult {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMultiblock$SimulateResult$Type = ($IMultiblock$SimulateResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMultiblock$SimulateResult_ = $IMultiblock$SimulateResult$Type;
}}
declare module "packages/vazkii/patchouli/client/book/text/$Word" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$Style, $Style$Type} from "packages/net/minecraft/network/chat/$Style"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Span, $Span$Type} from "packages/vazkii/patchouli/client/book/text/$Span"

export class $Word {
readonly "x": integer
readonly "y": integer
readonly "width": integer
readonly "height": integer

constructor(arg0: $GuiBook$Type, arg1: $Span$Type, arg2: $MutableComponent$Type, arg3: integer, arg4: integer, arg5: integer, arg6: $List$Type<($Word$Type)>)

public "render"(arg0: $GuiGraphics$Type, arg1: $Font$Type, arg2: $Style$Type, arg3: integer, arg4: integer): void
public "click"(arg0: double, arg1: double, arg2: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Word$Type = ($Word);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Word_ = $Word$Type;
}}
declare module "packages/vazkii/patchouli/client/book/gui/$GuiBookCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$GuiBookEntryList, $GuiBookEntryList$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBookEntryList"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BookCategory, $BookCategory$Type} from "packages/vazkii/patchouli/client/book/$BookCategory"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"

export class $GuiBookCategory extends $GuiBookEntryList {
static readonly "ENTRIES_PER_PAGE": integer
static readonly "ENTRIES_IN_FIRST_PAGE": integer
static readonly "FULL_WIDTH": integer
static readonly "FULL_HEIGHT": integer
static readonly "PAGE_WIDTH": integer
static readonly "PAGE_HEIGHT": integer
static readonly "TOP_PADDING": integer
static readonly "LEFT_PAGE_X": integer
static readonly "RIGHT_PAGE_X": integer
static readonly "TEXT_LINE_HEIGHT": integer
static readonly "MAX_BOOKMARKS": integer
readonly "book": $Book
 "bookLeft": integer
 "bookTop": integer
 "ticksInBook": integer
 "maxScale": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Book$Type, arg1: $BookCategory$Type)

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "canBeOpened"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiBookCategory$Type = ($GuiBookCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiBookCategory_ = $GuiBookCategory$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/$PageQuest" {
import {$GuiBookEntry, $GuiBookEntry$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBookEntry"
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$PageWithText, $PageWithText$Type} from "packages/vazkii/patchouli/client/book/page/abstr/$PageWithText"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"

export class $PageQuest extends $PageWithText {

constructor()

public "build"(arg0: $Level$Type, arg1: $BookEntry$Type, arg2: $BookContentsBuilder$Type, arg3: integer): void
public "isCompleted"(arg0: $Book$Type): boolean
public "getTextHeight"(): integer
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "onDisplayed"(arg0: $GuiBookEntry$Type, arg1: integer, arg2: integer): void
get "textHeight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageQuest$Type = ($PageQuest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageQuest_ = $PageQuest$Type;
}}
declare module "packages/vazkii/patchouli/client/book/$BookEntry" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BookPage, $BookPage$Type} from "packages/vazkii/patchouli/client/book/$BookPage"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$AbstractReadStateHolder, $AbstractReadStateHolder$Type} from "packages/vazkii/patchouli/client/book/$AbstractReadStateHolder"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BookIcon, $BookIcon$Type} from "packages/vazkii/patchouli/client/book/$BookIcon"
import {$BookCategory, $BookCategory$Type} from "packages/vazkii/patchouli/client/book/$BookCategory"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"

export class $BookEntry extends $AbstractReadStateHolder implements $Comparable<($BookEntry)> {

constructor(arg0: $JsonObject$Type, arg1: $ResourceLocation$Type, arg2: $Book$Type, arg3: string)

public "initCategory"(arg0: $ResourceLocation$Type, arg1: $Function$Type<($ResourceLocation$Type), ($BookCategory$Type)>): void
public "getPageFromAnchor"(arg0: string): integer
public "getEntryColor"(): integer
public "isFoundByQuery"(arg0: string): boolean
public "getName"(): $MutableComponent
public "compareTo"(arg0: $BookEntry$Type): integer
public "getId"(): $ResourceLocation
public "build"(arg0: $Level$Type, arg1: $BookContentsBuilder$Type): void
public "isLocked"(): boolean
public "updateLockStatus"(): void
public "getAddedBy"(): string
public "addRelevantStack"(arg0: $BookContentsBuilder$Type, arg1: $ItemStack$Type, arg2: integer): void
public "getBook"(): $Book
public "getCategory"(): $BookCategory
public "canAdd"(): boolean
public "markReadStateDirty"(): void
public "getPages"(): $List<($BookPage)>
public "shouldHide"(): boolean
public "getIcon"(): $BookIcon
public "isPriority"(): boolean
public "isSecret"(): boolean
get "entryColor"(): integer
get "name"(): $MutableComponent
get "id"(): $ResourceLocation
get "locked"(): boolean
get "addedBy"(): string
get "book"(): $Book
get "category"(): $BookCategory
get "pages"(): $List<($BookPage)>
get "icon"(): $BookIcon
get "priority"(): boolean
get "secret"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookEntry$Type = ($BookEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookEntry_ = $BookEntry$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/abstr/$PageSimpleProcessingRecipe" {
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$PageDoubleRecipeRegistry, $PageDoubleRecipeRegistry$Type} from "packages/vazkii/patchouli/client/book/page/abstr/$PageDoubleRecipeRegistry"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $PageSimpleProcessingRecipe<T extends $Recipe<(any)>> extends $PageDoubleRecipeRegistry<(T)> {

constructor(arg0: $RecipeType$Type<(T)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageSimpleProcessingRecipe$Type<T> = ($PageSimpleProcessingRecipe<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageSimpleProcessingRecipe_<T> = $PageSimpleProcessingRecipe$Type<(T)>;
}}
declare module "packages/vazkii/patchouli/common/multiblock/$DenseMultiblock" {
import {$IStateMatcher, $IStateMatcher$Type} from "packages/vazkii/patchouli/api/$IStateMatcher"
import {$LevelHeightAccessor, $LevelHeightAccessor$Type} from "packages/net/minecraft/world/level/$LevelHeightAccessor"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IMultiblock$SimulateResult, $IMultiblock$SimulateResult$Type} from "packages/vazkii/patchouli/api/$IMultiblock$SimulateResult"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$AbstractMultiblock, $AbstractMultiblock$Type} from "packages/vazkii/patchouli/common/multiblock/$AbstractMultiblock"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $DenseMultiblock extends $AbstractMultiblock {
 "id": $ResourceLocation

constructor(arg0: ((string)[])[], arg1: $Map$Type<(character), ($IStateMatcher$Type)>)
constructor(arg0: ((string)[])[], ...arg1: (any)[])

public "test"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: integer, arg3: integer, arg4: integer, arg5: $Rotation$Type): boolean
public "getSize"(): $Vec3i
public "getHeight"(): integer
public "getMinBuildHeight"(): integer
public "getBlockState"(arg0: $BlockPos$Type): $BlockState
public "simulate"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $Rotation$Type, arg3: boolean): $Pair<($BlockPos), ($Collection<($IMultiblock$SimulateResult)>)>
public static "traverseBlocks"<T, C>(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: C, arg3: $BiFunction$Type<(C), ($BlockPos$Type), (T)>, arg4: $Function$Type<(C), (T)>): T
public static "create"(arg0: integer, arg1: integer): $LevelHeightAccessor
get "size"(): $Vec3i
get "height"(): integer
get "minBuildHeight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DenseMultiblock$Type = ($DenseMultiblock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DenseMultiblock_ = $DenseMultiblock$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/$PageSpotlight" {
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$PageWithText, $PageWithText$Type} from "packages/vazkii/patchouli/client/book/page/abstr/$PageWithText"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $PageSpotlight extends $PageWithText {

constructor()

public "build"(arg0: $Level$Type, arg1: $BookEntry$Type, arg2: $BookContentsBuilder$Type, arg3: integer): void
public "getTextHeight"(): integer
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
get "textHeight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageSpotlight$Type = ($PageSpotlight);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageSpotlight_ = $PageSpotlight$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/variable/$ItemStackVariableSerializer" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$IVariableSerializer, $IVariableSerializer$Type} from "packages/vazkii/patchouli/api/$IVariableSerializer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $ItemStackVariableSerializer implements $IVariableSerializer<($ItemStack)> {

constructor()

public "toJson"(arg0: $ItemStack$Type): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemStackVariableSerializer$Type = ($ItemStackVariableSerializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemStackVariableSerializer_ = $ItemStackVariableSerializer$Type;
}}
declare module "packages/vazkii/patchouli/common/multiblock/$StringStateMatcher" {
import {$IStateMatcher, $IStateMatcher$Type} from "packages/vazkii/patchouli/api/$IStateMatcher"

export class $StringStateMatcher {

constructor()

public static "fromString"(arg0: string): $IStateMatcher
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringStateMatcher$Type = ($StringStateMatcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringStateMatcher_ = $StringStateMatcher$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/$PageSmelting" {
import {$PageSimpleProcessingRecipe, $PageSimpleProcessingRecipe$Type} from "packages/vazkii/patchouli/client/book/page/abstr/$PageSimpleProcessingRecipe"
import {$SmeltingRecipe, $SmeltingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$SmeltingRecipe"

export class $PageSmelting extends $PageSimpleProcessingRecipe<($SmeltingRecipe)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageSmelting$Type = ($PageSmelting);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageSmelting_ = $PageSmelting$Type;
}}
declare module "packages/vazkii/patchouli/client/handler/$TooltipHandler" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $TooltipHandler {

constructor()

public static "onTooltip"(arg0: $GuiGraphics$Type, arg1: $ItemStack$Type, arg2: integer, arg3: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TooltipHandler$Type = ($TooltipHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TooltipHandler_ = $TooltipHandler$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/$PageCrafting" {
import {$PageDoubleRecipeRegistry, $PageDoubleRecipeRegistry$Type} from "packages/vazkii/patchouli/client/book/page/abstr/$PageDoubleRecipeRegistry"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $PageCrafting extends $PageDoubleRecipeRegistry<($Recipe<(any)>)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageCrafting$Type = ($PageCrafting);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageCrafting_ = $PageCrafting$Type;
}}
declare module "packages/vazkii/patchouli/forge/network/$ForgeNetworkHandler" {
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $ForgeNetworkHandler {
static readonly "CHANNEL": $SimpleChannel

constructor()

public static "registerMessages"(): void
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
declare module "packages/vazkii/patchouli/client/book/template/component/$ComponentEntity" {
import {$GuiBookEntry, $GuiBookEntry$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBookEntry"
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$BookPage, $BookPage$Type} from "packages/vazkii/patchouli/client/book/$BookPage"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$TemplateComponent, $TemplateComponent$Type} from "packages/vazkii/patchouli/client/book/template/$TemplateComponent"

export class $ComponentEntity extends $TemplateComponent {
 "entityId": $IVariable
 "group": string
 "x": integer
 "y": integer
 "flag": string
 "advancement": string
 "guard": string

constructor()

public "build"(arg0: $BookContentsBuilder$Type, arg1: $BookPage$Type, arg2: $BookEntry$Type, arg3: integer): void
public "render"(arg0: $GuiGraphics$Type, arg1: $BookPage$Type, arg2: integer, arg3: integer, arg4: float): void
public "onDisplayed"(arg0: $BookPage$Type, arg1: $GuiBookEntry$Type, arg2: integer, arg3: integer): void
public "onVariablesAvailable"(arg0: $UnaryOperator$Type<($IVariable$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComponentEntity$Type = ($ComponentEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComponentEntity_ = $ComponentEntity$Type;
}}
declare module "packages/vazkii/patchouli/client/book/gui/button/$GuiButtonBookBookmark" {
import {$PersistentData$Bookmark, $PersistentData$Bookmark$Type} from "packages/vazkii/patchouli/client/base/$PersistentData$Bookmark"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$GuiButtonBook, $GuiButtonBook$Type} from "packages/vazkii/patchouli/client/book/gui/button/$GuiButtonBook"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $GuiButtonBookBookmark extends $GuiButtonBook {
readonly "bookmark": $PersistentData$Bookmark
readonly "multiblock": boolean
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

constructor(arg0: $GuiBook$Type, arg1: integer, arg2: integer, arg3: $PersistentData$Bookmark$Type)
constructor(arg0: $GuiBook$Type, arg1: integer, arg2: integer, arg3: $PersistentData$Bookmark$Type, arg4: boolean)

public "m_87963_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiButtonBookBookmark$Type = ($GuiButtonBookBookmark);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiButtonBookBookmark_ = $GuiButtonBookBookmark$Type;
}}
declare module "packages/vazkii/patchouli/client/book/$BookIcon" {
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $BookIcon {

 "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void

(arg0: string): $BookIcon
}

export namespace $BookIcon {
function from(arg0: string): $BookIcon
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookIcon$Type = ($BookIcon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookIcon_ = $BookIcon$Type;
}}
declare module "packages/vazkii/patchouli/mixin/client/$AccessorMultiBufferSource" {
import {$BufferBuilder, $BufferBuilder$Type} from "packages/com/mojang/blaze3d/vertex/$BufferBuilder"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"

export interface $AccessorMultiBufferSource {

 "getFixedBuffers"(): $Map<($RenderType), ($BufferBuilder)>
 "getFallbackBuffer"(): $BufferBuilder
}

export namespace $AccessorMultiBufferSource {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessorMultiBufferSource$Type = ($AccessorMultiBufferSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessorMultiBufferSource_ = $AccessorMultiBufferSource$Type;
}}
declare module "packages/vazkii/patchouli/client/base/$PersistentData$DataHolder" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$PersistentData$BookData, $PersistentData$BookData$Type} from "packages/vazkii/patchouli/client/base/$PersistentData$BookData"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"

export class $PersistentData$DataHolder {
 "bookGuiScale": integer
 "clickedVisualize": boolean

constructor(arg0: $JsonObject$Type)

public "getBookData"(arg0: $Book$Type): $PersistentData$BookData
public "serialize"(): $JsonObject
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PersistentData$DataHolder$Type = ($PersistentData$DataHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PersistentData$DataHolder_ = $PersistentData$DataHolder$Type;
}}
declare module "packages/vazkii/patchouli/xplat/$IClientXplatAbstractions" {
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"

export interface $IClientXplatAbstractions {

 "renderForMultiblock"(arg0: $BlockState$Type, arg1: $BlockPos$Type, arg2: $BlockAndTintGetter$Type, arg3: $PoseStack$Type, arg4: $MultiBufferSource$Type, arg5: $RandomSource$Type): void

(arg0: $BlockState$Type, arg1: $BlockPos$Type, arg2: $BlockAndTintGetter$Type, arg3: $PoseStack$Type, arg4: $MultiBufferSource$Type, arg5: $RandomSource$Type): void
}

export namespace $IClientXplatAbstractions {
const INSTANCE: $IClientXplatAbstractions
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IClientXplatAbstractions$Type = ($IClientXplatAbstractions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IClientXplatAbstractions_ = $IClientXplatAbstractions$Type;
}}
declare module "packages/vazkii/patchouli/common/util/$EntityUtil" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EntityUtil {


public static "getEntityName"(arg0: string): string
public static "loadEntity"(arg0: string): $Function<($Level), ($Entity)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityUtil$Type = ($EntityUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityUtil_ = $EntityUtil$Type;
}}
declare module "packages/vazkii/patchouli/api/$IVariable" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export interface $IVariable {

 "asList"(): $List<($IVariable)>
 "unwrap"(): $JsonElement
 "as"<T>(arg0: $Class$Type<(T)>): T
 "as"<T>(arg0: $Class$Type<(T)>, arg1: T): T
 "asListOrSingleton"(): $List<($IVariable)>
 "asString"(arg0: string): string
 "asString"(): string
 "asStreamOrSingleton"(): $Stream<($IVariable)>
 "asNumber"(): number
 "asNumber"(arg0: number): number
 "asStream"(): $Stream<($IVariable)>
 "asBoolean"(arg0: boolean): boolean
 "asBoolean"(): boolean
}

export namespace $IVariable {
function wrap(arg0: boolean): $IVariable
function wrap(arg0: $JsonElement$Type): $IVariable
function wrap(arg0: string): $IVariable
function wrap(arg0: number): $IVariable
function from<T>(arg0: T): $IVariable
function empty(): $IVariable
function wrapList(arg0: $Iterable$Type<($IVariable$Type)>): $IVariable
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IVariable$Type = ($IVariable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IVariable_ = $IVariable$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/component/$ComponentHeader" {
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$BookPage, $BookPage$Type} from "packages/vazkii/patchouli/client/book/$BookPage"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$TemplateComponent, $TemplateComponent$Type} from "packages/vazkii/patchouli/client/book/template/$TemplateComponent"

export class $ComponentHeader extends $TemplateComponent {
 "text": $IVariable
 "colorStr": $IVariable
 "group": string
 "x": integer
 "y": integer
 "flag": string
 "advancement": string
 "guard": string

constructor()

public "build"(arg0: $BookContentsBuilder$Type, arg1: $BookPage$Type, arg2: $BookEntry$Type, arg3: integer): void
public "render"(arg0: $GuiGraphics$Type, arg1: $BookPage$Type, arg2: integer, arg3: integer, arg4: float): void
public "onVariablesAvailable"(arg0: $UnaryOperator$Type<($IVariable$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComponentHeader$Type = ($ComponentHeader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComponentHeader_ = $ComponentHeader$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/variable/$ItemStackArrayVariableSerializer" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$GenericArrayVariableSerializer, $GenericArrayVariableSerializer$Type} from "packages/vazkii/patchouli/client/book/template/variable/$GenericArrayVariableSerializer"

export class $ItemStackArrayVariableSerializer extends $GenericArrayVariableSerializer<($ItemStack)> {

constructor()

public "fromNonArray"(arg0: $JsonElement$Type): ($ItemStack)[]
public "fromJson"(arg0: $JsonElement$Type): ($ItemStack)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemStackArrayVariableSerializer$Type = ($ItemStackArrayVariableSerializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemStackArrayVariableSerializer_ = $ItemStackArrayVariableSerializer$Type;
}}
declare module "packages/vazkii/patchouli/client/book/gui/button/$GuiButtonCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$SoundManager, $SoundManager$Type} from "packages/net/minecraft/client/sounds/$SoundManager"
import {$BookIcon, $BookIcon$Type} from "packages/vazkii/patchouli/client/book/$BookIcon"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$BookCategory, $BookCategory$Type} from "packages/vazkii/patchouli/client/book/$BookCategory"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $GuiButtonCategory extends $Button {
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

constructor(arg0: $GuiBook$Type, arg1: integer, arg2: integer, arg3: $BookCategory$Type, arg4: $Button$OnPress$Type)
constructor(arg0: $GuiBook$Type, arg1: integer, arg2: integer, arg3: $BookIcon$Type, arg4: $Component$Type, arg5: $Button$OnPress$Type)

public "getCategory"(): $BookCategory
public "m_87963_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "playDownSound"(arg0: $SoundManager$Type): void
get "category"(): $BookCategory
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiButtonCategory$Type = ($GuiButtonCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiButtonCategory_ = $GuiButtonCategory$Type;
}}
declare module "packages/vazkii/patchouli/client/handler/$MultiblockVisualizationHandler" {
import {$PersistentData$Bookmark, $PersistentData$Bookmark$Type} from "packages/vazkii/patchouli/client/base/$PersistentData$Bookmark"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$IMultiblock, $IMultiblock$Type} from "packages/vazkii/patchouli/api/$IMultiblock"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $MultiblockVisualizationHandler {
static "hasMultiblock": boolean
static "bookmark": $PersistentData$Bookmark

constructor()

public static "renderMultiblock"(arg0: $Level$Type, arg1: $PoseStack$Type): void
public static "setMultiblock"(arg0: $IMultiblock$Type, arg1: $Component$Type, arg2: $PersistentData$Bookmark$Type, arg3: boolean, arg4: $Function$Type<($BlockPos$Type), ($BlockPos$Type)>): void
public static "setMultiblock"(arg0: $IMultiblock$Type, arg1: $Component$Type, arg2: $PersistentData$Bookmark$Type, arg3: boolean): void
public static "anchorTo"(arg0: $BlockPos$Type, arg1: $Rotation$Type): void
public static "isAnchored"(): boolean
public static "onWorldRenderLast"(arg0: $PoseStack$Type): void
public static "getMultiblock"(): $IMultiblock
public static "onPlayerInteract"(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $BlockHitResult$Type): $InteractionResult
public static "onRenderHUD"(arg0: $GuiGraphics$Type, arg1: float): void
public static "onClientTick"(arg0: $Minecraft$Type): void
public static "renderBlock"(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockPos$Type, arg3: float, arg4: $PoseStack$Type): void
public static "getFacingRotation"(): $Rotation
public static "getStartPos"(): $BlockPos
get "anchored"(): boolean
get "multiblock"(): $IMultiblock
get "facingRotation"(): $Rotation
get "startPos"(): $BlockPos
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultiblockVisualizationHandler$Type = ($MultiblockVisualizationHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultiblockVisualizationHandler_ = $MultiblockVisualizationHandler$Type;
}}
declare module "packages/vazkii/patchouli/client/book/$BookContentResourceDirectLoader" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$BookContentLoader$LoadResult, $BookContentLoader$LoadResult$Type} from "packages/vazkii/patchouli/client/book/$BookContentLoader$LoadResult"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"
import {$BookContentLoader, $BookContentLoader$Type} from "packages/vazkii/patchouli/client/book/$BookContentLoader"

export class $BookContentResourceDirectLoader implements $BookContentLoader {
static readonly "INSTANCE": $BookContentResourceDirectLoader


public "loadJson"(arg0: $Book$Type, arg1: $ResourceLocation$Type): $BookContentLoader$LoadResult
public "findFiles"(arg0: $Book$Type, arg1: string, arg2: $List$Type<($ResourceLocation$Type)>): void
public static "streamToJson"(arg0: $InputStream$Type): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookContentResourceDirectLoader$Type = ($BookContentResourceDirectLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookContentResourceDirectLoader_ = $BookContentResourceDirectLoader$Type;
}}
declare module "packages/vazkii/patchouli/api/stub/$StubMatcher" {
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$TriPredicate, $TriPredicate$Type} from "packages/vazkii/patchouli/api/$TriPredicate"
import {$IStateMatcher, $IStateMatcher$Type} from "packages/vazkii/patchouli/api/$IStateMatcher"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $StubMatcher implements $IStateMatcher {
static readonly "INSTANCE": $StubMatcher


public "getStatePredicate"(): $TriPredicate<($BlockGetter), ($BlockPos), ($BlockState)>
public "getDisplayedState"(arg0: long): $BlockState
get "statePredicate"(): $TriPredicate<($BlockGetter), ($BlockPos), ($BlockState)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StubMatcher$Type = ($StubMatcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StubMatcher_ = $StubMatcher$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/variable/$GenericArrayVariableSerializer" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$IVariableSerializer, $IVariableSerializer$Type} from "packages/vazkii/patchouli/api/$IVariableSerializer"
import {$JsonArray, $JsonArray$Type} from "packages/com/google/gson/$JsonArray"

export class $GenericArrayVariableSerializer<T> implements $IVariableSerializer<((T)[])> {

constructor(arg0: $IVariableSerializer$Type<(T)>, arg1: $Class$Type<(T)>)

public "toJson"(arg0: (T)[]): $JsonArray
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenericArrayVariableSerializer$Type<T> = ($GenericArrayVariableSerializer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenericArrayVariableSerializer_<T> = $GenericArrayVariableSerializer$Type<(T)>;
}}
declare module "packages/vazkii/patchouli/client/book/template/component/$ComponentImage" {
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$BookPage, $BookPage$Type} from "packages/vazkii/patchouli/client/book/$BookPage"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$TemplateComponent, $TemplateComponent$Type} from "packages/vazkii/patchouli/client/book/template/$TemplateComponent"

export class $ComponentImage extends $TemplateComponent {
 "image": string
 "u": integer
 "v": integer
 "width": integer
 "height": integer
 "textureWidth": integer
 "textureHeight": integer
 "scale": float
 "group": string
 "x": integer
 "y": integer
 "flag": string
 "advancement": string
 "guard": string

constructor()

public "build"(arg0: $BookContentsBuilder$Type, arg1: $BookPage$Type, arg2: $BookEntry$Type, arg3: integer): void
public "render"(arg0: $GuiGraphics$Type, arg1: $BookPage$Type, arg2: integer, arg3: integer, arg4: float): void
public "onVariablesAvailable"(arg0: $UnaryOperator$Type<($IVariable$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComponentImage$Type = ($ComponentImage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComponentImage_ = $ComponentImage$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/component/$ComponentFrame" {
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$BookPage, $BookPage$Type} from "packages/vazkii/patchouli/client/book/$BookPage"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$TemplateComponent, $TemplateComponent$Type} from "packages/vazkii/patchouli/client/book/template/$TemplateComponent"

export class $ComponentFrame extends $TemplateComponent {
 "group": string
 "x": integer
 "y": integer
 "flag": string
 "advancement": string
 "guard": string

constructor()

public "build"(arg0: $BookContentsBuilder$Type, arg1: $BookPage$Type, arg2: $BookEntry$Type, arg3: integer): void
public "render"(arg0: $GuiGraphics$Type, arg1: $BookPage$Type, arg2: integer, arg3: integer, arg4: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComponentFrame$Type = ($ComponentFrame);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComponentFrame_ = $ComponentFrame$Type;
}}
declare module "packages/vazkii/patchouli/client/book/$BookCategory" {
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$AbstractReadStateHolder, $AbstractReadStateHolder$Type} from "packages/vazkii/patchouli/client/book/$AbstractReadStateHolder"
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BookIcon, $BookIcon$Type} from "packages/vazkii/patchouli/client/book/$BookIcon"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export class $BookCategory extends $AbstractReadStateHolder implements $Comparable<($BookCategory)> {

constructor(arg0: $JsonObject$Type, arg1: $ResourceLocation$Type, arg2: $Book$Type)

public "addChildCategory"(arg0: $BookCategory$Type): void
public "getName"(): $MutableComponent
public "compareTo"(arg0: $BookCategory$Type): integer
public "getId"(): $ResourceLocation
public "addEntry"(arg0: $BookEntry$Type): void
public "getEntries"(): $List<($BookEntry)>
public "build"(arg0: $BookContentsBuilder$Type): void
public "isLocked"(): boolean
public "getDescription"(): string
public "updateLockStatus"(arg0: boolean): void
public "getBook"(): $Book
public "canAdd"(): boolean
public "markReadStateDirty"(): void
public "shouldHide"(): boolean
public "getIcon"(): $BookIcon
public "getParentCategory"(): $BookCategory
public "isRootCategory"(): boolean
public "isSecret"(): boolean
get "name"(): $MutableComponent
get "id"(): $ResourceLocation
get "entries"(): $List<($BookEntry)>
get "locked"(): boolean
get "description"(): string
get "book"(): $Book
get "icon"(): $BookIcon
get "parentCategory"(): $BookCategory
get "rootCategory"(): boolean
get "secret"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookCategory$Type = ($BookCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookCategory_ = $BookCategory$Type;
}}
declare module "packages/vazkii/patchouli/client/book/text/$Span" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$SpanState, $SpanState$Type} from "packages/vazkii/patchouli/client/book/text/$SpanState"
import {$Style, $Style$Type} from "packages/net/minecraft/network/chat/$Style"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"

export class $Span {
readonly "text": string
readonly "style": $Style
readonly "linkCluster": $List<($Span)>
readonly "tooltip": $Component
readonly "onClick": $Supplier<(boolean)>
readonly "lineBreaks": integer
readonly "spacingLeft": integer
readonly "spacingRight": integer
readonly "bold": boolean

constructor(arg0: $SpanState$Type, arg1: string)

public static "error"(arg0: $SpanState$Type, arg1: string): $Span
public "styledSubstring"(arg0: integer): $MutableComponent
public "styledSubstring"(arg0: integer, arg1: integer): $MutableComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Span$Type = ($Span);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Span_ = $Span$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/$PageText" {
import {$PageWithText, $PageWithText$Type} from "packages/vazkii/patchouli/client/book/page/abstr/$PageWithText"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $PageText extends $PageWithText {

constructor()

public "setText"(arg0: string): void
public "getTextHeight"(): integer
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
set "text"(value: string)
get "textHeight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageText$Type = ($PageText);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageText_ = $PageText$Type;
}}
declare module "packages/vazkii/patchouli/forge/client/$ForgeClientXplatImpl" {
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$IClientXplatAbstractions, $IClientXplatAbstractions$Type} from "packages/vazkii/patchouli/xplat/$IClientXplatAbstractions"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"

export class $ForgeClientXplatImpl implements $IClientXplatAbstractions {

constructor()

public "renderForMultiblock"(arg0: $BlockState$Type, arg1: $BlockPos$Type, arg2: $BlockAndTintGetter$Type, arg3: $PoseStack$Type, arg4: $MultiBufferSource$Type, arg5: $RandomSource$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeClientXplatImpl$Type = ($ForgeClientXplatImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeClientXplatImpl_ = $ForgeClientXplatImpl$Type;
}}
declare module "packages/vazkii/patchouli/client/book/$AbstractReadStateHolder" {
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$EntryDisplayState, $EntryDisplayState$Type} from "packages/vazkii/patchouli/client/book/$EntryDisplayState"

export class $AbstractReadStateHolder {

constructor()

public "markReadStateDirty"(): void
public static "mostImportantState"(arg0: $Stream$Type<($EntryDisplayState$Type)>): $EntryDisplayState
public "getReadState"(): $EntryDisplayState
get "readState"(): $EntryDisplayState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractReadStateHolder$Type = ($AbstractReadStateHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractReadStateHolder_ = $AbstractReadStateHolder$Type;
}}
declare module "packages/vazkii/patchouli/api/stub/$StubPatchouliAPI" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$IStateMatcher, $IStateMatcher$Type} from "packages/vazkii/patchouli/api/$IStateMatcher"
import {$PatchouliAPI$IPatchouliAPI, $PatchouliAPI$IPatchouliAPI$Type} from "packages/vazkii/patchouli/api/$PatchouliAPI$IPatchouliAPI"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$IStyleStack, $IStyleStack$Type} from "packages/vazkii/patchouli/api/$IStyleStack"
import {$PatchouliConfigAccess, $PatchouliConfigAccess$Type} from "packages/vazkii/patchouli/api/$PatchouliConfigAccess"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$IMultiblock, $IMultiblock$Type} from "packages/vazkii/patchouli/api/$IMultiblock"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $StubPatchouliAPI implements $PatchouliAPI$IPatchouliAPI {
static readonly "INSTANCE": $StubPatchouliAPI


public "registerFunction"(arg0: string, arg1: $BiFunction$Type<(string), ($IStyleStack$Type), (string)>): void
public "registerCommand"(arg0: string, arg1: $Function$Type<($IStyleStack$Type), (string)>): void
public "makeSparseMultiblock"(arg0: $Map$Type<($BlockPos$Type), ($IStateMatcher$Type)>): $IMultiblock
public "registerTemplateAsBuiltin"(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($InputStream$Type)>): void
public "getCurrentMultiblock"(): $IMultiblock
public "getConfig"(): $PatchouliConfigAccess
public "getBookStack"(arg0: $ResourceLocation$Type): $ItemStack
public "getMultiblock"(arg0: $ResourceLocation$Type): $IMultiblock
public "setConfigFlag"(arg0: string, arg1: boolean): void
public "openBookEntry"(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type, arg2: integer): void
public "openBookEntry"(arg0: $ServerPlayer$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: integer): void
public "getOpenBookGui"(): $ResourceLocation
public "isStub"(): boolean
public "registerMultiblock"(arg0: $ResourceLocation$Type, arg1: $IMultiblock$Type): $IMultiblock
public "showMultiblock"(arg0: $IMultiblock$Type, arg1: $Component$Type, arg2: $BlockPos$Type, arg3: $Rotation$Type): void
public "clearMultiblock"(): void
public "makeMultiblock"(arg0: ((string)[])[], ...arg1: (any)[]): $IMultiblock
public "predicateMatcher"(arg0: $Block$Type, arg1: $Predicate$Type<($BlockState$Type)>): $IStateMatcher
public "predicateMatcher"(arg0: $BlockState$Type, arg1: $Predicate$Type<($BlockState$Type)>): $IStateMatcher
public "anyMatcher"(): $IStateMatcher
public "tagMatcher"(arg0: $TagKey$Type<($Block$Type)>): $IStateMatcher
public "stateMatcher"(arg0: $BlockState$Type): $IStateMatcher
public "looseBlockMatcher"(arg0: $Block$Type): $IStateMatcher
public "airMatcher"(): $IStateMatcher
public "strictBlockMatcher"(arg0: $Block$Type): $IStateMatcher
public "displayOnlyMatcher"(arg0: $Block$Type): $IStateMatcher
public "displayOnlyMatcher"(arg0: $BlockState$Type): $IStateMatcher
public "propertyMatcher"(arg0: $BlockState$Type, ...arg1: ($Property$Type<(any)>)[]): $IStateMatcher
public "getSubtitle"(arg0: $ResourceLocation$Type): $Component
public "getConfigFlag"(arg0: string): boolean
public "openBookGUI"(arg0: $ServerPlayer$Type, arg1: $ResourceLocation$Type): void
public "openBookGUI"(arg0: $ResourceLocation$Type): void
get "currentMultiblock"(): $IMultiblock
get "config"(): $PatchouliConfigAccess
get "openBookGui"(): $ResourceLocation
get "stub"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StubPatchouliAPI$Type = ($StubPatchouliAPI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StubPatchouliAPI_ = $StubPatchouliAPI$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/abstr/$PageWithText" {
import {$GuiBookEntry, $GuiBookEntry$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBookEntry"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$BookPage, $BookPage$Type} from "packages/vazkii/patchouli/client/book/$BookPage"

export class $PageWithText extends $BookPage {

constructor()

public "getTextHeight"(): integer
public "shouldRenderText"(): boolean
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "onDisplayed"(arg0: $GuiBookEntry$Type, arg1: integer, arg2: integer): void
get "textHeight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageWithText$Type = ($PageWithText);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageWithText_ = $PageWithText$Type;
}}
declare module "packages/vazkii/patchouli/forge/network/$ForgeMessageOpenBookGui" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ForgeMessageOpenBookGui {

constructor(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type, arg2: integer)

public static "decode"(arg0: $FriendlyByteBuf$Type): $ForgeMessageOpenBookGui
public "encode"(arg0: $FriendlyByteBuf$Type): void
public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public static "send"(arg0: $ServerPlayer$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeMessageOpenBookGui$Type = ($ForgeMessageOpenBookGui);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeMessageOpenBookGui_ = $ForgeMessageOpenBookGui$Type;
}}
declare module "packages/vazkii/patchouli/api/stub/$StubMultiblock" {
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$IMultiblock, $IMultiblock$Type} from "packages/vazkii/patchouli/api/$IMultiblock"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IMultiblock$SimulateResult, $IMultiblock$SimulateResult$Type} from "packages/vazkii/patchouli/api/$IMultiblock$SimulateResult"

export class $StubMultiblock implements $IMultiblock {
static readonly "INSTANCE": $StubMultiblock


public "test"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: integer, arg3: integer, arg4: integer, arg5: $Rotation$Type): boolean
public "offset"(arg0: integer, arg1: integer, arg2: integer): $IMultiblock
public "validate"(arg0: $Level$Type, arg1: $BlockPos$Type): $Rotation
public "validate"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $Rotation$Type): boolean
public "getSize"(): $Vec3i
public "getID"(): $ResourceLocation
public "place"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $Rotation$Type): void
public "offsetView"(arg0: integer, arg1: integer, arg2: integer): $IMultiblock
public "setSymmetrical"(arg0: boolean): $IMultiblock
public "isSymmetrical"(): boolean
public "setId"(arg0: $ResourceLocation$Type): $IMultiblock
public "simulate"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $Rotation$Type, arg3: boolean): $Pair<($BlockPos), ($Collection<($IMultiblock$SimulateResult)>)>
get "size"(): $Vec3i
get "iD"(): $ResourceLocation
set "symmetrical"(value: boolean)
get "symmetrical"(): boolean
set "id"(value: $ResourceLocation$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StubMultiblock$Type = ($StubMultiblock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StubMultiblock_ = $StubMultiblock$Type;
}}
declare module "packages/vazkii/patchouli/api/$PatchouliAPI$IPatchouliAPI" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$IStateMatcher, $IStateMatcher$Type} from "packages/vazkii/patchouli/api/$IStateMatcher"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$IStyleStack, $IStyleStack$Type} from "packages/vazkii/patchouli/api/$IStyleStack"
import {$PatchouliConfigAccess, $PatchouliConfigAccess$Type} from "packages/vazkii/patchouli/api/$PatchouliConfigAccess"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$IMultiblock, $IMultiblock$Type} from "packages/vazkii/patchouli/api/$IMultiblock"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $PatchouliAPI$IPatchouliAPI {

 "registerFunction"(arg0: string, arg1: $BiFunction$Type<(string), ($IStyleStack$Type), (string)>): void
 "registerCommand"(arg0: string, arg1: $Function$Type<($IStyleStack$Type), (string)>): void
 "makeSparseMultiblock"(arg0: $Map$Type<($BlockPos$Type), ($IStateMatcher$Type)>): $IMultiblock
 "registerTemplateAsBuiltin"(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($InputStream$Type)>): void
 "getCurrentMultiblock"(): $IMultiblock
 "getConfig"(): $PatchouliConfigAccess
 "getBookStack"(arg0: $ResourceLocation$Type): $ItemStack
 "getMultiblock"(arg0: $ResourceLocation$Type): $IMultiblock
 "setConfigFlag"(arg0: string, arg1: boolean): void
 "openBookEntry"(arg0: $ServerPlayer$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: integer): void
 "openBookEntry"(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type, arg2: integer): void
 "getOpenBookGui"(): $ResourceLocation
 "isStub"(): boolean
 "registerMultiblock"(arg0: $ResourceLocation$Type, arg1: $IMultiblock$Type): $IMultiblock
 "showMultiblock"(arg0: $IMultiblock$Type, arg1: $Component$Type, arg2: $BlockPos$Type, arg3: $Rotation$Type): void
 "clearMultiblock"(): void
 "makeMultiblock"(arg0: ((string)[])[], ...arg1: (any)[]): $IMultiblock
 "predicateMatcher"(arg0: $Block$Type, arg1: $Predicate$Type<($BlockState$Type)>): $IStateMatcher
 "predicateMatcher"(arg0: $BlockState$Type, arg1: $Predicate$Type<($BlockState$Type)>): $IStateMatcher
 "anyMatcher"(): $IStateMatcher
 "tagMatcher"(arg0: $TagKey$Type<($Block$Type)>): $IStateMatcher
 "stateMatcher"(arg0: $BlockState$Type): $IStateMatcher
 "looseBlockMatcher"(arg0: $Block$Type): $IStateMatcher
 "airMatcher"(): $IStateMatcher
 "strictBlockMatcher"(arg0: $Block$Type): $IStateMatcher
 "displayOnlyMatcher"(arg0: $Block$Type): $IStateMatcher
 "displayOnlyMatcher"(arg0: $BlockState$Type): $IStateMatcher
 "propertyMatcher"(arg0: $BlockState$Type, ...arg1: ($Property$Type<(any)>)[]): $IStateMatcher
 "getSubtitle"(arg0: $ResourceLocation$Type): $Component
 "getConfigFlag"(arg0: string): boolean
 "openBookGUI"(arg0: $ResourceLocation$Type): void
 "openBookGUI"(arg0: $ServerPlayer$Type, arg1: $ResourceLocation$Type): void
}

export namespace $PatchouliAPI$IPatchouliAPI {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PatchouliAPI$IPatchouliAPI$Type = ($PatchouliAPI$IPatchouliAPI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PatchouliAPI$IPatchouliAPI_ = $PatchouliAPI$IPatchouliAPI$Type;
}}
declare module "packages/vazkii/patchouli/common/book/$BookRegistry" {
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$XplatModContainer, $XplatModContainer$Type} from "packages/vazkii/patchouli/xplat/$XplatModContainer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $BookRegistry {
static readonly "INSTANCE": $BookRegistry
static readonly "BOOKS_LOCATION": string
readonly "books": $Map<($ResourceLocation), ($Book)>
static readonly "GSON": $Gson


public "init"(): void
public "reloadContents"(arg0: $Level$Type): void
public "loadBook"(arg0: $XplatModContainer$Type, arg1: $ResourceLocation$Type, arg2: $InputStream$Type, arg3: boolean): void
public static "findFiles"(arg0: $XplatModContainer$Type, arg1: string, arg2: $Predicate$Type<($Path$Type)>, arg3: $BiFunction$Type<($Path$Type), ($Path$Type), (boolean)>, arg4: boolean): void
public static "findFiles"(arg0: $XplatModContainer$Type, arg1: string, arg2: $Predicate$Type<($Path$Type)>, arg3: $BiFunction$Type<($Path$Type), ($Path$Type), (boolean)>, arg4: boolean, arg5: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookRegistry$Type = ($BookRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookRegistry_ = $BookRegistry$Type;
}}
declare module "packages/vazkii/patchouli/client/book/gui/$GuiBookWriter" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"

export class $GuiBookWriter extends $GuiBook {
static readonly "FULL_WIDTH": integer
static readonly "FULL_HEIGHT": integer
static readonly "PAGE_WIDTH": integer
static readonly "PAGE_HEIGHT": integer
static readonly "TOP_PADDING": integer
static readonly "LEFT_PAGE_X": integer
static readonly "RIGHT_PAGE_X": integer
static readonly "TEXT_LINE_HEIGHT": integer
static readonly "MAX_BOOKMARKS": integer
readonly "book": $Book
 "bookLeft": integer
 "bookTop": integer
 "ticksInBook": integer
 "maxScale": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Book$Type)

public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "m_7856_"(): void
public "charTyped"(arg0: character, arg1: integer): boolean
public "mouseClickedScaled"(arg0: double, arg1: double, arg2: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiBookWriter$Type = ($GuiBookWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiBookWriter_ = $GuiBookWriter$Type;
}}
declare module "packages/vazkii/patchouli/common/multiblock/$MultiblockRegistry" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IMultiblock, $IMultiblock$Type} from "packages/vazkii/patchouli/api/$IMultiblock"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $MultiblockRegistry {
static readonly "MULTIBLOCKS": $Map<($ResourceLocation), ($IMultiblock)>

constructor()

public static "registerMultiblock"(arg0: $ResourceLocation$Type, arg1: $IMultiblock$Type): $IMultiblock
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultiblockRegistry$Type = ($MultiblockRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultiblockRegistry_ = $MultiblockRegistry$Type;
}}
declare module "packages/vazkii/patchouli/common/multiblock/$SparseMultiblock" {
import {$IStateMatcher, $IStateMatcher$Type} from "packages/vazkii/patchouli/api/$IStateMatcher"
import {$LevelHeightAccessor, $LevelHeightAccessor$Type} from "packages/net/minecraft/world/level/$LevelHeightAccessor"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IMultiblock$SimulateResult, $IMultiblock$SimulateResult$Type} from "packages/vazkii/patchouli/api/$IMultiblock$SimulateResult"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$AbstractMultiblock, $AbstractMultiblock$Type} from "packages/vazkii/patchouli/common/multiblock/$AbstractMultiblock"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $SparseMultiblock extends $AbstractMultiblock {
 "id": $ResourceLocation

constructor(arg0: $Map$Type<($BlockPos$Type), ($IStateMatcher$Type)>)

public "test"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: integer, arg3: integer, arg4: integer, arg5: $Rotation$Type): boolean
public "getSize"(): $Vec3i
public "getHeight"(): integer
public "getMinBuildHeight"(): integer
public "getBlockState"(arg0: $BlockPos$Type): $BlockState
public "simulate"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $Rotation$Type, arg3: boolean): $Pair<($BlockPos), ($Collection<($IMultiblock$SimulateResult)>)>
public static "traverseBlocks"<T, C>(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: C, arg3: $BiFunction$Type<(C), ($BlockPos$Type), (T)>, arg4: $Function$Type<(C), (T)>): T
public static "create"(arg0: integer, arg1: integer): $LevelHeightAccessor
get "size"(): $Vec3i
get "height"(): integer
get "minBuildHeight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SparseMultiblock$Type = ($SparseMultiblock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SparseMultiblock_ = $SparseMultiblock$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/abstr/$PageDoubleRecipe" {
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$PageWithText, $PageWithText$Type} from "packages/vazkii/patchouli/client/book/page/abstr/$PageWithText"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $PageDoubleRecipe<T> extends $PageWithText {

constructor()

public "build"(arg0: $Level$Type, arg1: $BookEntry$Type, arg2: $BookContentsBuilder$Type, arg3: integer): void
public "getTextHeight"(): integer
public "shouldRenderText"(): boolean
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
get "textHeight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageDoubleRecipe$Type<T> = ($PageDoubleRecipe<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageDoubleRecipe_<T> = $PageDoubleRecipe$Type<(T)>;
}}
declare module "packages/vazkii/patchouli/common/recipe/$ShapelessBookRecipe" {
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$ShapelessRecipe, $ShapelessRecipe$Type} from "packages/net/minecraft/world/item/crafting/$ShapelessRecipe"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ShapelessBookRecipe extends $ShapelessRecipe {
static readonly "SERIALIZER": $RecipeSerializer<($ShapelessBookRecipe)>
readonly "group": string
readonly "result": $ItemStack
readonly "ingredients": $NonNullList<($Ingredient)>

constructor(arg0: $ShapelessRecipe$Type, arg1: $ResourceLocation$Type)

public "getSerializer"(): $RecipeSerializer<(any)>
get "serializer"(): $RecipeSerializer<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShapelessBookRecipe$Type = ($ShapelessBookRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShapelessBookRecipe_ = $ShapelessBookRecipe$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/test/$RecipeTestProcessor" {
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"
import {$IVariableProvider, $IVariableProvider$Type} from "packages/vazkii/patchouli/api/$IVariableProvider"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$IComponentProcessor, $IComponentProcessor$Type} from "packages/vazkii/patchouli/api/$IComponentProcessor"

export class $RecipeTestProcessor implements $IComponentProcessor {

constructor()

public "setup"(arg0: $Level$Type, arg1: $IVariableProvider$Type): void
public "process"(arg0: $Level$Type, arg1: string): $IVariable
public "refresh"(arg0: $Screen$Type, arg1: integer, arg2: integer): void
public "allowRender"(arg0: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipeTestProcessor$Type = ($RecipeTestProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipeTestProcessor_ = $RecipeTestProcessor$Type;
}}
declare module "packages/vazkii/patchouli/api/$BookContentsReloadEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $BookContentsReloadEvent extends $Event {

constructor()
constructor(arg0: $ResourceLocation$Type)

public "getBook"(): $ResourceLocation
public "isCancelable"(): boolean
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "book"(): $ResourceLocation
get "cancelable"(): boolean
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookContentsReloadEvent$Type = ($BookContentsReloadEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookContentsReloadEvent_ = $BookContentsReloadEvent$Type;
}}
declare module "packages/vazkii/patchouli/api/$TriPredicate" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $TriPredicate<A, B, C> {

 "test"(arg0: A, arg1: B, arg2: C): boolean

(arg0: A, arg1: B, arg2: C): boolean
}

export namespace $TriPredicate {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TriPredicate$Type<A, B, C> = ($TriPredicate<(A), (B), (C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TriPredicate_<A, B, C> = $TriPredicate$Type<(A), (B), (C)>;
}}
declare module "packages/vazkii/patchouli/client/book/gui/$GuiBookIndex" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$GuiBookEntryList, $GuiBookEntryList$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBookEntryList"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"

export class $GuiBookIndex extends $GuiBookEntryList {
static readonly "ENTRIES_PER_PAGE": integer
static readonly "ENTRIES_IN_FIRST_PAGE": integer
static readonly "FULL_WIDTH": integer
static readonly "FULL_HEIGHT": integer
static readonly "PAGE_WIDTH": integer
static readonly "PAGE_HEIGHT": integer
static readonly "TOP_PADDING": integer
static readonly "LEFT_PAGE_X": integer
static readonly "RIGHT_PAGE_X": integer
static readonly "TEXT_LINE_HEIGHT": integer
static readonly "MAX_BOOKMARKS": integer
readonly "book": $Book
 "bookLeft": integer
 "bookTop": integer
 "ticksInBook": integer
 "maxScale": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Book$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiBookIndex$Type = ($GuiBookIndex);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiBookIndex_ = $GuiBookIndex$Type;
}}
declare module "packages/vazkii/patchouli/api/$IComponentRenderContext" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Style, $Style$Type} from "packages/net/minecraft/network/chat/$Style"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export interface $IComponentRenderContext {

 "renderIngredient"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $Ingredient$Type): void
 "renderItemStack"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $ItemStack$Type): void
 "addWidget"(arg0: $AbstractWidget$Type, arg1: integer): void
 "setHoverTooltipComponents"(arg0: $List$Type<($Component$Type)>): void
 "getFont"(): $Style
 "getGui"(): $Screen
 "navigateToEntry"(arg0: $ResourceLocation$Type, arg1: integer, arg2: boolean): boolean
/**
 * 
 * @deprecated
 */
 "setHoverTooltip"(arg0: $List$Type<(string)>): void
 "getHeaderColor"(): integer
 "isAreaHovered"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): boolean
/**
 * 
 * @deprecated
 */
 "registerButton"(arg0: $Button$Type, arg1: integer, arg2: $Runnable$Type): void
 "getBookTexture"(): $ResourceLocation
 "getCraftingTexture"(): $ResourceLocation
 "getTicksInBook"(): integer
 "getTextColor"(): integer
}

export namespace $IComponentRenderContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IComponentRenderContext$Type = ($IComponentRenderContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IComponentRenderContext_ = $IComponentRenderContext$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/$PageCampfireCooking" {
import {$CampfireCookingRecipe, $CampfireCookingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$CampfireCookingRecipe"
import {$PageSimpleProcessingRecipe, $PageSimpleProcessingRecipe$Type} from "packages/vazkii/patchouli/client/book/page/abstr/$PageSimpleProcessingRecipe"

export class $PageCampfireCooking extends $PageSimpleProcessingRecipe<($CampfireCookingRecipe)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageCampfireCooking$Type = ($PageCampfireCooking);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageCampfireCooking_ = $PageCampfireCooking$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/$PageBlasting" {
import {$PageSimpleProcessingRecipe, $PageSimpleProcessingRecipe$Type} from "packages/vazkii/patchouli/client/book/page/abstr/$PageSimpleProcessingRecipe"
import {$BlastingRecipe, $BlastingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$BlastingRecipe"

export class $PageBlasting extends $PageSimpleProcessingRecipe<($BlastingRecipe)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageBlasting$Type = ($PageBlasting);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageBlasting_ = $PageBlasting$Type;
}}
declare module "packages/vazkii/patchouli/client/base/$PersistentData$BookData" {
import {$PersistentData$Bookmark, $PersistentData$Bookmark$Type} from "packages/vazkii/patchouli/client/base/$PersistentData$Bookmark"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $PersistentData$BookData {
readonly "viewedEntries": $List<($ResourceLocation)>
readonly "bookmarks": $List<($PersistentData$Bookmark)>
readonly "history": $List<($ResourceLocation)>
readonly "completedManualQuests": $List<($ResourceLocation)>

constructor(arg0: $JsonObject$Type)

public "serialize"(): $JsonObject
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PersistentData$BookData$Type = ($PersistentData$BookData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PersistentData$BookData_ = $PersistentData$BookData$Type;
}}
declare module "packages/vazkii/patchouli/client/base/$ClientTicker" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"

export class $ClientTicker {
static "ticksInGame": long
static "partialTicks": float
static "delta": float
static "total": float

constructor()

public static "renderTickStart"(arg0: float): void
public static "renderTickEnd"(): void
public static "endClientTick"(arg0: $Minecraft$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTicker$Type = ($ClientTicker);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTicker_ = $ClientTicker$Type;
}}
declare module "packages/vazkii/patchouli/forge/common/$ForgePatchouliConfig" {
import {$ForgeConfigSpec$EnumValue, $ForgeConfigSpec$EnumValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$EnumValue"
import {$PatchouliConfigAccess$TextOverflowMode, $PatchouliConfigAccess$TextOverflowMode$Type} from "packages/vazkii/patchouli/api/$PatchouliConfigAccess$TextOverflowMode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"

export class $ForgePatchouliConfig {
static readonly "disableAdvancementLocking": $ForgeConfigSpec$ConfigValue<(boolean)>
static readonly "noAdvancementBooks": $ForgeConfigSpec$ConfigValue<($List<(any)>)>
static readonly "testingMode": $ForgeConfigSpec$ConfigValue<(boolean)>
static readonly "inventoryButtonBook": $ForgeConfigSpec$ConfigValue<(string)>
static readonly "useShiftForQuickLookup": $ForgeConfigSpec$ConfigValue<(boolean)>
static readonly "overflowMode": $ForgeConfigSpec$EnumValue<($PatchouliConfigAccess$TextOverflowMode)>
static readonly "quickLookupTime": $ForgeConfigSpec$ConfigValue<(integer)>

constructor()

public static "setup"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgePatchouliConfig$Type = ($ForgePatchouliConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgePatchouliConfig_ = $ForgePatchouliConfig$Type;
}}
declare module "packages/vazkii/patchouli/forge/network/$ForgeMessageReloadBookContents" {
import {$NetworkEvent$Context, $NetworkEvent$Context$Type} from "packages/net/minecraftforge/network/$NetworkEvent$Context"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $ForgeMessageReloadBookContents {

constructor()

public static "decode"(arg0: $FriendlyByteBuf$Type): $ForgeMessageReloadBookContents
public "encode"(arg0: $FriendlyByteBuf$Type): void
public "handle"(arg0: $Supplier$Type<($NetworkEvent$Context$Type)>): void
public static "sendToAll"(arg0: $MinecraftServer$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeMessageReloadBookContents$Type = ($ForgeMessageReloadBookContents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeMessageReloadBookContents_ = $ForgeMessageReloadBookContents$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/variable/$TextComponentVariableSerializer" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IVariableSerializer, $IVariableSerializer$Type} from "packages/vazkii/patchouli/api/$IVariableSerializer"

export class $TextComponentVariableSerializer implements $IVariableSerializer<($Component)> {

constructor()

public "toJson"(arg0: $Component$Type): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextComponentVariableSerializer$Type = ($TextComponentVariableSerializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextComponentVariableSerializer_ = $TextComponentVariableSerializer$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/component/$ComponentItemStack" {
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$BookPage, $BookPage$Type} from "packages/vazkii/patchouli/client/book/$BookPage"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$TemplateComponent, $TemplateComponent$Type} from "packages/vazkii/patchouli/client/book/template/$TemplateComponent"

export class $ComponentItemStack extends $TemplateComponent {
 "item": $IVariable
 "group": string
 "x": integer
 "y": integer
 "flag": string
 "advancement": string
 "guard": string

constructor()

public "build"(arg0: $BookContentsBuilder$Type, arg1: $BookPage$Type, arg2: $BookEntry$Type, arg3: integer): void
public "render"(arg0: $GuiGraphics$Type, arg1: $BookPage$Type, arg2: integer, arg3: integer, arg4: float): void
public "onVariablesAvailable"(arg0: $UnaryOperator$Type<($IVariable$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComponentItemStack$Type = ($ComponentItemStack);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComponentItemStack_ = $ComponentItemStack$Type;
}}
declare module "packages/vazkii/patchouli/common/item/$ItemModBook" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$InteractionResultHolder, $InteractionResultHolder$Type} from "packages/net/minecraft/world/$InteractionResultHolder"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ItemModBook extends $Item {
static readonly "TAG_BOOK": string
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor()

public static "getBook"(arg0: $ItemStack$Type): $Book
public static "forBook"(arg0: $Book$Type): $ItemStack
public static "forBook"(arg0: $ResourceLocation$Type): $ItemStack
public "use"(arg0: $Level$Type, arg1: $Player$Type, arg2: $InteractionHand$Type): $InteractionResultHolder<($ItemStack)>
public "getName"(arg0: $ItemStack$Type): $Component
public "appendHoverText"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void
public "getCreatorModId"(arg0: $ItemStack$Type): string
public static "getCompletion"(arg0: $ItemStack$Type): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemModBook$Type = ($ItemModBook);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemModBook_ = $ItemModBook$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/component/$ComponentText" {
import {$GuiBookEntry, $GuiBookEntry$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBookEntry"
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$BookPage, $BookPage$Type} from "packages/vazkii/patchouli/client/book/$BookPage"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$TemplateComponent, $TemplateComponent$Type} from "packages/vazkii/patchouli/client/book/template/$TemplateComponent"

export class $ComponentText extends $TemplateComponent {
 "text": $IVariable
 "colorStr": $IVariable
 "group": string
 "x": integer
 "y": integer
 "flag": string
 "advancement": string
 "guard": string

constructor()

public "build"(arg0: $BookContentsBuilder$Type, arg1: $BookPage$Type, arg2: $BookEntry$Type, arg3: integer): void
public "render"(arg0: $GuiGraphics$Type, arg1: $BookPage$Type, arg2: integer, arg3: integer, arg4: float): void
public "mouseClicked"(arg0: $BookPage$Type, arg1: double, arg2: double, arg3: integer): boolean
public "onDisplayed"(arg0: $BookPage$Type, arg1: $GuiBookEntry$Type, arg2: integer, arg3: integer): void
public "onVariablesAvailable"(arg0: $UnaryOperator$Type<($IVariable$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComponentText$Type = ($ComponentText);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComponentText_ = $ComponentText$Type;
}}
declare module "packages/vazkii/patchouli/client/book/gui/$GuiBookEntryList" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"

export class $GuiBookEntryList extends $GuiBook {
static readonly "ENTRIES_PER_PAGE": integer
static readonly "ENTRIES_IN_FIRST_PAGE": integer
static readonly "FULL_WIDTH": integer
static readonly "FULL_HEIGHT": integer
static readonly "PAGE_WIDTH": integer
static readonly "PAGE_HEIGHT": integer
static readonly "TOP_PADDING": integer
static readonly "LEFT_PAGE_X": integer
static readonly "RIGHT_PAGE_X": integer
static readonly "TEXT_LINE_HEIGHT": integer
static readonly "MAX_BOOKMARKS": integer
readonly "book": $Book
 "bookLeft": integer
 "bookTop": integer
 "ticksInBook": integer
 "maxScale": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Book$Type, arg1: $Component$Type)

public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "m_7856_"(): void
public "handleButtonEntry"(arg0: $Button$Type): void
public "handleButtonCategory"(arg0: $Button$Type): void
public "getSearchQuery"(): string
public "charTyped"(arg0: character, arg1: integer): boolean
public "mouseClickedScaled"(arg0: double, arg1: double, arg2: integer): boolean
get "searchQuery"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiBookEntryList$Type = ($GuiBookEntryList);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiBookEntryList_ = $GuiBookEntryList$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/$PageEntity" {
import {$GuiBookEntry, $GuiBookEntry$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBookEntry"
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$PageWithText, $PageWithText$Type} from "packages/vazkii/patchouli/client/book/page/abstr/$PageWithText"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $PageEntity extends $PageWithText {
 "entityId": string

constructor()

public "build"(arg0: $Level$Type, arg1: $BookEntry$Type, arg2: $BookContentsBuilder$Type, arg3: integer): void
public "getTextHeight"(): integer
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "onDisplayed"(arg0: $GuiBookEntry$Type, arg1: integer, arg2: integer): void
public static "renderEntity"(arg0: $GuiGraphics$Type, arg1: $Entity$Type, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float): void
get "textHeight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageEntity$Type = ($PageEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageEntity_ = $PageEntity$Type;
}}
declare module "packages/vazkii/patchouli/client/handler/$BookRightClickHandler" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $BookRightClickHandler {

constructor()

public static "onRenderHUD"(arg0: $GuiGraphics$Type, arg1: float): void
public static "onRightClick"(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $BlockHitResult$Type): $InteractionResult
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookRightClickHandler$Type = ($BookRightClickHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookRightClickHandler_ = $BookRightClickHandler$Type;
}}
declare module "packages/vazkii/patchouli/client/base/$ClientAdvancements" {
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"

export class $ClientAdvancements {

constructor()

public static "sendBookToast"(arg0: $Book$Type): void
public static "hasDone"(arg0: string): boolean
public static "onClientPacket"(): void
public static "playerLogout"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientAdvancements$Type = ($ClientAdvancements);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientAdvancements_ = $ClientAdvancements$Type;
}}
declare module "packages/vazkii/patchouli/client/book/gui/button/$GuiButtonEntry" {
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$SoundManager, $SoundManager$Type} from "packages/net/minecraft/client/sounds/$SoundManager"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $GuiButtonEntry extends $Button {
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

constructor(arg0: $GuiBook$Type, arg1: integer, arg2: integer, arg3: $BookEntry$Type, arg4: $Button$OnPress$Type)

public "getEntry"(): $BookEntry
public "playDownSound"(arg0: $SoundManager$Type): void
get "entry"(): $BookEntry
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiButtonEntry$Type = ($GuiButtonEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiButtonEntry_ = $GuiButtonEntry$Type;
}}
declare module "packages/vazkii/patchouli/client/base/$BookModel" {
import {$ModelData, $ModelData$Type} from "packages/net/minecraftforge/client/model/data/$ModelData"
import {$ItemTransforms, $ItemTransforms$Type} from "packages/net/minecraft/client/renderer/block/model/$ItemTransforms"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$ItemOverrides, $ItemOverrides$Type} from "packages/net/minecraft/client/renderer/block/model/$ItemOverrides"
import {$ItemDisplayContext, $ItemDisplayContext$Type} from "packages/net/minecraft/world/item/$ItemDisplayContext"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$ChunkRenderTypeSet, $ChunkRenderTypeSet$Type} from "packages/net/minecraftforge/client/$ChunkRenderTypeSet"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ModelBakery, $ModelBakery$Type} from "packages/net/minecraft/client/resources/model/$ModelBakery"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"
import {$BakedQuad, $BakedQuad$Type} from "packages/net/minecraft/client/renderer/block/model/$BakedQuad"

export class $BookModel implements $BakedModel {

constructor(arg0: $BakedModel$Type, arg1: $ModelBakery$Type)

public "usesBlockLight"(): boolean
public "isGui3d"(): boolean
public "getParticleIcon"(): $TextureAtlasSprite
public "getQuads"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $RandomSource$Type): $List<($BakedQuad)>
public "isCustomRenderer"(): boolean
public "getOverrides"(): $ItemOverrides
public "getTransforms"(): $ItemTransforms
public "useAmbientOcclusion"(): boolean
public "useAmbientOcclusion"(arg0: $BlockState$Type, arg1: $RenderType$Type): boolean
public "useAmbientOcclusion"(arg0: $BlockState$Type): boolean
public "getRenderTypes"(arg0: $ItemStack$Type, arg1: boolean): $List<($RenderType)>
public "getRenderTypes"(arg0: $BlockState$Type, arg1: $RandomSource$Type, arg2: $ModelData$Type): $ChunkRenderTypeSet
public "getRenderPasses"(arg0: $ItemStack$Type, arg1: boolean): $List<($BakedModel)>
public "applyTransform"(arg0: $ItemDisplayContext$Type, arg1: $PoseStack$Type, arg2: boolean): $BakedModel
public "getQuads"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $RandomSource$Type, arg3: $ModelData$Type, arg4: $RenderType$Type): $List<($BakedQuad)>
public "getParticleIcon"(arg0: $ModelData$Type): $TextureAtlasSprite
public "getModelData"(arg0: $BlockAndTintGetter$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $ModelData$Type): $ModelData
public "useAmbientOcclusionWithLightEmission"(arg0: $BlockState$Type, arg1: $RenderType$Type): boolean
get "gui3d"(): boolean
get "particleIcon"(): $TextureAtlasSprite
get "customRenderer"(): boolean
get "overrides"(): $ItemOverrides
get "transforms"(): $ItemTransforms
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookModel$Type = ($BookModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookModel_ = $BookModel$Type;
}}
declare module "packages/vazkii/patchouli/api/$IVariableProvider" {
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"

export interface $IVariableProvider {

 "get"(arg0: string): $IVariable
 "has"(arg0: string): boolean
}

export namespace $IVariableProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IVariableProvider$Type = ($IVariableProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IVariableProvider_ = $IVariableProvider$Type;
}}
declare module "packages/vazkii/patchouli/client/book/text/$BookTextParser$CommandProcessor" {
import {$SpanState, $SpanState$Type} from "packages/vazkii/patchouli/client/book/text/$SpanState"

export interface $BookTextParser$CommandProcessor {

 "process"(arg0: $SpanState$Type): string

(arg0: $SpanState$Type): string
}

export namespace $BookTextParser$CommandProcessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookTextParser$CommandProcessor$Type = ($BookTextParser$CommandProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookTextParser$CommandProcessor_ = $BookTextParser$CommandProcessor$Type;
}}
declare module "packages/vazkii/patchouli/common/recipe/$ShapedBookRecipe" {
import {$ShapedRecipe, $ShapedRecipe$Type} from "packages/net/minecraft/world/item/crafting/$ShapedRecipe"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ShapedBookRecipe extends $ShapedRecipe {
static readonly "SERIALIZER": $RecipeSerializer<($ShapedBookRecipe)>
readonly "width": integer
readonly "height": integer
readonly "result": $ItemStack

constructor(arg0: $ShapedRecipe$Type, arg1: $ResourceLocation$Type)

public "getSerializer"(): $RecipeSerializer<(any)>
get "serializer"(): $RecipeSerializer<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ShapedBookRecipe$Type = ($ShapedBookRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ShapedBookRecipe_ = $ShapedBookRecipe$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/$TemplateInclusion" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"
import {$IVariableProvider, $IVariableProvider$Type} from "packages/vazkii/patchouli/api/$IVariableProvider"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$IComponentProcessor, $IComponentProcessor$Type} from "packages/vazkii/patchouli/api/$IComponentProcessor"

export class $TemplateInclusion {
 "template": string
 "as": string
 "localBindings": $JsonObject
 "x": integer
 "y": integer

constructor()

public "attemptVariableLookup"(arg0: string): $IVariable
public "qualifyName"(arg0: string): string
public "isUpreference"(arg0: $IVariable$Type): boolean
public "process"(arg0: $Level$Type, arg1: $IComponentProcessor$Type): void
public "wrapProvider"(arg0: $IVariableProvider$Type): $IVariableProvider
public "upperMerge"(arg0: $TemplateInclusion$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TemplateInclusion$Type = ($TemplateInclusion);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TemplateInclusion_ = $TemplateInclusion$Type;
}}
declare module "packages/vazkii/patchouli/client/book/gui/$BookTextRenderer" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $BookTextRenderer {

constructor(arg0: $GuiBook$Type, arg1: $Component$Type, arg2: integer, arg3: integer)
constructor(arg0: $GuiBook$Type, arg1: $Component$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer): void
public "click"(arg0: double, arg1: double, arg2: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookTextRenderer$Type = ($BookTextRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookTextRenderer_ = $BookTextRenderer$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/$PageEmpty" {
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$BookPage, $BookPage$Type} from "packages/vazkii/patchouli/client/book/$BookPage"

export class $PageEmpty extends $BookPage {

constructor()

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageEmpty$Type = ($PageEmpty);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageEmpty_ = $PageEmpty$Type;
}}
declare module "packages/vazkii/patchouli/client/book/gui/$GuiBookLanding" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"

export class $GuiBookLanding extends $GuiBook {
static readonly "FULL_WIDTH": integer
static readonly "FULL_HEIGHT": integer
static readonly "PAGE_WIDTH": integer
static readonly "PAGE_HEIGHT": integer
static readonly "TOP_PADDING": integer
static readonly "LEFT_PAGE_X": integer
static readonly "RIGHT_PAGE_X": integer
static readonly "TEXT_LINE_HEIGHT": integer
static readonly "MAX_BOOKMARKS": integer
readonly "book": $Book
 "bookLeft": integer
 "bookTop": integer
 "ticksInBook": integer
 "maxScale": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Book$Type)

public "m_7856_"(): void
public "handleButtonCategory"(arg0: $Button$Type): void
public "handleButtonPamphletEntry"(arg0: $Button$Type): void
public "mouseClickedScaled"(arg0: double, arg1: double, arg2: integer): boolean
public "handleButtonIndex"(arg0: $Button$Type): void
public "handleButtonResize"(arg0: $Button$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiBookLanding$Type = ($GuiBookLanding);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiBookLanding_ = $GuiBookLanding$Type;
}}
declare module "packages/vazkii/patchouli/mixin/client/$AccessorClientAdvancements" {
import {$Advancement, $Advancement$Type} from "packages/net/minecraft/advancements/$Advancement"
import {$AdvancementProgress, $AdvancementProgress$Type} from "packages/net/minecraft/advancements/$AdvancementProgress"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $AccessorClientAdvancements {

 "getProgress"(): $Map<($Advancement), ($AdvancementProgress)>

(): $Map<($Advancement), ($AdvancementProgress)>
}

export namespace $AccessorClientAdvancements {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessorClientAdvancements$Type = ($AccessorClientAdvancements);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessorClientAdvancements_ = $AccessorClientAdvancements$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/abstr/$PageDoubleRecipeRegistry" {
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$PageDoubleRecipe, $PageDoubleRecipe$Type} from "packages/vazkii/patchouli/client/book/page/abstr/$PageDoubleRecipe"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $PageDoubleRecipeRegistry<T extends $Recipe<(any)>> extends $PageDoubleRecipe<(T)> {

constructor(arg0: $RecipeType$Type<(any)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageDoubleRecipeRegistry$Type<T> = ($PageDoubleRecipeRegistry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageDoubleRecipeRegistry_<T> = $PageDoubleRecipeRegistry$Type<(T)>;
}}
declare module "packages/vazkii/patchouli/client/book/text/$BookTextParser$FunctionProcessor" {
import {$SpanState, $SpanState$Type} from "packages/vazkii/patchouli/client/book/text/$SpanState"

export interface $BookTextParser$FunctionProcessor {

 "process"(arg0: string, arg1: $SpanState$Type): string

(arg0: string, arg1: $SpanState$Type): string
}

export namespace $BookTextParser$FunctionProcessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookTextParser$FunctionProcessor$Type = ($BookTextParser$FunctionProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookTextParser$FunctionProcessor_ = $BookTextParser$FunctionProcessor$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/test/$EntityTestProcessor" {
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"
import {$IVariableProvider, $IVariableProvider$Type} from "packages/vazkii/patchouli/api/$IVariableProvider"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$IComponentProcessor, $IComponentProcessor$Type} from "packages/vazkii/patchouli/api/$IComponentProcessor"

export class $EntityTestProcessor implements $IComponentProcessor {

constructor()

public "setup"(arg0: $Level$Type, arg1: $IVariableProvider$Type): void
public "process"(arg0: $Level$Type, arg1: string): $IVariable
public "refresh"(arg0: $Screen$Type, arg1: integer, arg2: integer): void
public "allowRender"(arg0: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityTestProcessor$Type = ($EntityTestProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityTestProcessor_ = $EntityTestProcessor$Type;
}}
declare module "packages/vazkii/patchouli/client/book/text/$TextLayouter" {
import {$Font, $Font$Type} from "packages/net/minecraft/client/gui/$Font"
import {$PatchouliConfigAccess$TextOverflowMode, $PatchouliConfigAccess$TextOverflowMode$Type} from "packages/vazkii/patchouli/api/$PatchouliConfigAccess$TextOverflowMode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$Word, $Word$Type} from "packages/vazkii/patchouli/client/book/text/$Word"
import {$Span, $Span$Type} from "packages/vazkii/patchouli/client/book/text/$Span"

export class $TextLayouter {

constructor(arg0: $GuiBook$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $PatchouliConfigAccess$TextOverflowMode$Type)

public "getWords"(): $List<($Word)>
public "flush"(): void
public "getScale"(): float
public "layout"(arg0: $Font$Type, arg1: $List$Type<($Span$Type)>): void
get "words"(): $List<($Word)>
get "scale"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextLayouter$Type = ($TextLayouter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextLayouter_ = $TextLayouter$Type;
}}
declare module "packages/vazkii/patchouli/client/handler/$BookCrashHandler" {
import {$SystemReport, $SystemReport$Type} from "packages/net/minecraft/$SystemReport"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $BookCrashHandler implements $Supplier<(string)> {

constructor()

public static "appendToCrashReport"(arg0: $SystemReport$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookCrashHandler$Type = ($BookCrashHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookCrashHandler_ = $BookCrashHandler$Type;
}}
declare module "packages/vazkii/patchouli/common/book/$Book" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$PatchouliConfigAccess$TextOverflowMode, $PatchouliConfigAccess$TextOverflowMode$Type} from "packages/vazkii/patchouli/api/$PatchouliConfigAccess$TextOverflowMode"
import {$BookContents, $BookContents$Type} from "packages/vazkii/patchouli/client/book/$BookContents"
import {$BookIcon, $BookIcon$Type} from "packages/vazkii/patchouli/client/book/$BookIcon"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$XplatModContainer, $XplatModContainer$Type} from "packages/vazkii/patchouli/xplat/$XplatModContainer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Style, $Style$Type} from "packages/net/minecraft/network/chat/$Style"
import {$MutableComponent, $MutableComponent$Type} from "packages/net/minecraft/network/chat/$MutableComponent"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Book {
readonly "owner": $XplatModContainer
readonly "id": $ResourceLocation
readonly "textColor": integer
readonly "headerColor": integer
readonly "nameplateColor": integer
readonly "linkColor": integer
readonly "linkHoverColor": integer
readonly "progressBarColor": integer
readonly "progressBarBackground": integer
readonly "isExternal": boolean
readonly "name": string
readonly "landingText": string
readonly "bookTexture": $ResourceLocation
readonly "fillerTexture": $ResourceLocation
readonly "craftingTexture": $ResourceLocation
readonly "model": $ResourceLocation
readonly "useBlockyFont": boolean
readonly "openSound": $ResourceLocation
readonly "flipSound": $ResourceLocation
readonly "showProgress": boolean
readonly "indexIconRaw": string
readonly "version": string
readonly "subtitle": string
readonly "creativeTab": $ResourceLocation
readonly "advancementsTab": $ResourceLocation
readonly "noBook": boolean
readonly "showToasts": boolean
readonly "pauseGame": boolean
readonly "isPamphlet": boolean
readonly "i18n": boolean
readonly "overflowMode": $PatchouliConfigAccess$TextOverflowMode
readonly "macros": $Map<(string), (string)>

constructor(arg0: $JsonObject$Type, arg1: $XplatModContainer$Type, arg2: $ResourceLocation$Type, arg3: boolean)

public "getContents"(): $BookContents
public "getBookItem"(): $ItemStack
public "popUpdated"(): boolean
public "reloadContents"(arg0: $Level$Type, arg1: boolean): void
public "reloadLocks"(arg0: boolean): void
public "getIcon"(): $BookIcon
public "markUpdated"(): void
public "getFontStyle"(): $Style
public "getOwnerName"(): string
public "getSubtitle"(): $MutableComponent
public "advancementsEnabled"(): boolean
get "contents"(): $BookContents
get "bookItem"(): $ItemStack
get "icon"(): $BookIcon
get "fontStyle"(): $Style
get "ownerName"(): string
get "subtitle"(): $MutableComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Book$Type = ($Book);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Book_ = $Book$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/variable/$Variable" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"

export class $Variable implements $IVariable {

constructor(arg0: $JsonElement$Type, arg1: $Class$Type<(any)>)

public "toString"(): string
public "unwrap"(): $JsonElement
public "as"<T>(arg0: $Class$Type<(T)>): T
public static "wrap"(arg0: boolean): $IVariable
public static "wrap"(arg0: $JsonElement$Type): $IVariable
public static "wrap"(arg0: string): $IVariable
public static "wrap"(arg0: number): $IVariable
public static "from"<T>(arg0: T): $IVariable
public static "empty"(): $IVariable
public "asList"(): $List<($IVariable)>
public "as"<T>(arg0: $Class$Type<(T)>, arg1: T): T
public "asListOrSingleton"(): $List<($IVariable)>
public "asString"(arg0: string): string
public "asString"(): string
public static "wrapList"(arg0: $Iterable$Type<($IVariable$Type)>): $IVariable
public "asStreamOrSingleton"(): $Stream<($IVariable)>
public "asNumber"(): number
public "asNumber"(arg0: number): number
public "asStream"(): $Stream<($IVariable)>
public "asBoolean"(arg0: boolean): boolean
public "asBoolean"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Variable$Type = ($Variable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Variable_ = $Variable$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/$PageRelations" {
import {$GuiBookEntry, $GuiBookEntry$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBookEntry"
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$PageWithText, $PageWithText$Type} from "packages/vazkii/patchouli/client/book/page/abstr/$PageWithText"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $PageRelations extends $PageWithText {

constructor()

public "build"(arg0: $Level$Type, arg1: $BookEntry$Type, arg2: $BookContentsBuilder$Type, arg3: integer): void
public "getTextHeight"(): integer
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "onDisplayed"(arg0: $GuiBookEntry$Type, arg1: integer, arg2: integer): void
public "handleButtonEntry"(arg0: $Button$Type): void
get "textHeight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageRelations$Type = ($PageRelations);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageRelations_ = $PageRelations$Type;
}}
declare module "packages/vazkii/patchouli/api/$ICustomComponent" {
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$IVariablesAvailableCallback, $IVariablesAvailableCallback$Type} from "packages/vazkii/patchouli/api/$IVariablesAvailableCallback"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IComponentRenderContext, $IComponentRenderContext$Type} from "packages/vazkii/patchouli/api/$IComponentRenderContext"

export interface $ICustomComponent extends $IVariablesAvailableCallback {

 "build"(arg0: integer, arg1: integer, arg2: integer): void
 "render"(arg0: $GuiGraphics$Type, arg1: $IComponentRenderContext$Type, arg2: float, arg3: integer, arg4: integer): void
 "mouseClicked"(arg0: $IComponentRenderContext$Type, arg1: double, arg2: double, arg3: integer): boolean
 "onDisplayed"(arg0: $IComponentRenderContext$Type): void
 "onVariablesAvailable"(arg0: $UnaryOperator$Type<($IVariable$Type)>): void
}

export namespace $ICustomComponent {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ICustomComponent$Type = ($ICustomComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ICustomComponent_ = $ICustomComponent$Type;
}}
declare module "packages/vazkii/patchouli/client/book/gui/button/$GuiButtonBookMarkRead" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$GuiButtonBook, $GuiButtonBook$Type} from "packages/vazkii/patchouli/client/book/gui/button/$GuiButtonBook"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $GuiButtonBookMarkRead extends $GuiButtonBook {
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

constructor(arg0: $GuiBook$Type, arg1: integer, arg2: integer)

public "m_87963_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "onPress"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiButtonBookMarkRead$Type = ($GuiButtonBookMarkRead);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiButtonBookMarkRead_ = $GuiButtonBookMarkRead$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/$PageSmoking" {
import {$PageSimpleProcessingRecipe, $PageSimpleProcessingRecipe$Type} from "packages/vazkii/patchouli/client/book/page/abstr/$PageSimpleProcessingRecipe"
import {$SmokingRecipe, $SmokingRecipe$Type} from "packages/net/minecraft/world/item/crafting/$SmokingRecipe"

export class $PageSmoking extends $PageSimpleProcessingRecipe<($SmokingRecipe)> {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageSmoking$Type = ($PageSmoking);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageSmoking_ = $PageSmoking$Type;
}}
declare module "packages/vazkii/patchouli/forge/client/$ForgeClientInitializer" {
import {$RegisterGuiOverlaysEvent, $RegisterGuiOverlaysEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterGuiOverlaysEvent"
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"
import {$ModelEvent$RegisterAdditional, $ModelEvent$RegisterAdditional$Type} from "packages/net/minecraftforge/client/event/$ModelEvent$RegisterAdditional"
import {$ModelEvent$ModifyBakingResult, $ModelEvent$ModifyBakingResult$Type} from "packages/net/minecraftforge/client/event/$ModelEvent$ModifyBakingResult"
import {$RegisterClientReloadListenersEvent, $RegisterClientReloadListenersEvent$Type} from "packages/net/minecraftforge/client/event/$RegisterClientReloadListenersEvent"

export class $ForgeClientInitializer {

constructor()

public static "onInitializeClient"(arg0: $FMLClientSetupEvent$Type): void
public static "registerReloadListeners"(arg0: $RegisterClientReloadListenersEvent$Type): void
public static "modelRegistry"(arg0: $ModelEvent$RegisterAdditional$Type): void
public static "signalBooksLoaded"(): void
public static "registerOverlays"(arg0: $RegisterGuiOverlaysEvent$Type): void
public static "replaceBookModel"(arg0: $ModelEvent$ModifyBakingResult$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeClientInitializer$Type = ($ForgeClientInitializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeClientInitializer_ = $ForgeClientInitializer$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/$PageImage" {
import {$GuiBookEntry, $GuiBookEntry$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBookEntry"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$PageWithText, $PageWithText$Type} from "packages/vazkii/patchouli/client/book/page/abstr/$PageWithText"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $PageImage extends $PageWithText {

constructor()

public "getTextHeight"(): integer
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "onDisplayed"(arg0: $GuiBookEntry$Type, arg1: integer, arg2: integer): void
public "handleButtonArrow"(arg0: $Button$Type): void
get "textHeight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageImage$Type = ($PageImage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageImage_ = $PageImage$Type;
}}
declare module "packages/vazkii/patchouli/forge/xplat/$ForgeXplatImpl" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$IXplatAbstractions, $IXplatAbstractions$Type} from "packages/vazkii/patchouli/xplat/$IXplatAbstractions"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$XplatModContainer, $XplatModContainer$Type} from "packages/vazkii/patchouli/xplat/$XplatModContainer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ForgeXplatImpl implements $IXplatAbstractions {

constructor()

public "isPhysicalClient"(): boolean
public "fireBookReload"(arg0: $ResourceLocation$Type): void
public "sendOpenBookGui"(arg0: $ServerPlayer$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: integer): void
public "fireDrawBookScreen"(arg0: $ResourceLocation$Type, arg1: $Screen$Type, arg2: integer, arg3: integer, arg4: float, arg5: $GuiGraphics$Type): void
public "handleRecipeKeybind"(arg0: integer, arg1: integer, arg2: $ItemStack$Type): boolean
public "getAllMods"(): $Collection<($XplatModContainer)>
public "isDevEnvironment"(): boolean
public "sendReloadContentsMessage"(arg0: $MinecraftServer$Type): void
public "getModContainer"(arg0: string): $XplatModContainer
public "signalBooksLoaded"(): void
public "isModLoaded"(arg0: string): boolean
get "physicalClient"(): boolean
get "allMods"(): $Collection<($XplatModContainer)>
get "devEnvironment"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeXplatImpl$Type = ($ForgeXplatImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeXplatImpl_ = $ForgeXplatImpl$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/component/$ComponentTooltip" {
import {$GuiBookEntry, $GuiBookEntry$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBookEntry"
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$BookPage, $BookPage$Type} from "packages/vazkii/patchouli/client/book/$BookPage"
import {$TemplateComponent, $TemplateComponent$Type} from "packages/vazkii/patchouli/client/book/template/$TemplateComponent"

export class $ComponentTooltip extends $TemplateComponent {
 "tooltipRaw": ($IVariable)[]
 "group": string
 "x": integer
 "y": integer
 "flag": string
 "advancement": string
 "guard": string

constructor()

public "render"(arg0: $GuiGraphics$Type, arg1: $BookPage$Type, arg2: integer, arg3: integer, arg4: float): void
public "onDisplayed"(arg0: $BookPage$Type, arg1: $GuiBookEntry$Type, arg2: integer, arg3: integer): void
public "onVariablesAvailable"(arg0: $UnaryOperator$Type<($IVariable$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComponentTooltip$Type = ($ComponentTooltip);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComponentTooltip_ = $ComponentTooltip$Type;
}}
declare module "packages/vazkii/patchouli/mixin/client/$AccessorKeyMapping" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $AccessorKeyMapping {

}

export namespace $AccessorKeyMapping {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessorKeyMapping$Type = ($AccessorKeyMapping);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessorKeyMapping_ = $AccessorKeyMapping$Type;
}}
declare module "packages/vazkii/patchouli/common/util/$SerializationUtil" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $SerializationUtil {
static readonly "RAW_GSON": $Gson
static readonly "PRETTY_GSON": $Gson


public static "getAsEnum"<T extends $Enum<(T)>>(arg0: $JsonObject$Type, arg1: string, arg2: $Class$Type<(T)>, arg3: T): T
public static "getAsResourceLocation"(arg0: $JsonObject$Type, arg1: string, arg2: $ResourceLocation$Type): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SerializationUtil$Type = ($SerializationUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SerializationUtil_ = $SerializationUtil$Type;
}}
declare module "packages/vazkii/patchouli/client/book/$BookContentExternalLoader" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$BookContentLoader$LoadResult, $BookContentLoader$LoadResult$Type} from "packages/vazkii/patchouli/client/book/$BookContentLoader$LoadResult"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"
import {$BookContentLoader, $BookContentLoader$Type} from "packages/vazkii/patchouli/client/book/$BookContentLoader"

export class $BookContentExternalLoader implements $BookContentLoader {
static readonly "INSTANCE": $BookContentExternalLoader


public "loadJson"(arg0: $Book$Type, arg1: $ResourceLocation$Type): $BookContentLoader$LoadResult
public "findFiles"(arg0: $Book$Type, arg1: string, arg2: $List$Type<($ResourceLocation$Type)>): void
public static "streamToJson"(arg0: $InputStream$Type): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookContentExternalLoader$Type = ($BookContentExternalLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookContentExternalLoader_ = $BookContentExternalLoader$Type;
}}
declare module "packages/vazkii/patchouli/client/book/$BookContentsBuilder" {
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$BookContents, $BookContents$Type} from "packages/vazkii/patchouli/client/book/$BookContents"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BookCategory, $BookCategory$Type} from "packages/vazkii/patchouli/client/book/$BookCategory"
import {$ItemStackUtil$StackWrapper, $ItemStackUtil$StackWrapper$Type} from "packages/vazkii/patchouli/common/util/$ItemStackUtil$StackWrapper"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"
import {$BookTemplate, $BookTemplate$Type} from "packages/vazkii/patchouli/client/book/template/$BookTemplate"

export class $BookContentsBuilder {
static readonly "DEFAULT_LANG": string


public "addRecipeMapping"(arg0: $ItemStackUtil$StackWrapper$Type, arg1: $BookEntry$Type, arg2: integer): void
public "getEntry"(arg0: $ResourceLocation$Type): $BookEntry
public static "loadAndBuildFor"(arg0: $Level$Type, arg1: $Book$Type, arg2: boolean): $BookContents
public "getCategory"(arg0: $ResourceLocation$Type): $BookCategory
public "getTemplate"(arg0: $ResourceLocation$Type): $Supplier<($BookTemplate)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookContentsBuilder$Type = ($BookContentsBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookContentsBuilder_ = $BookContentsBuilder$Type;
}}
declare module "packages/vazkii/patchouli/api/$IVariablesAvailableCallback" {
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"

export interface $IVariablesAvailableCallback {

 "onVariablesAvailable"(arg0: $UnaryOperator$Type<($IVariable$Type)>): void

(arg0: $UnaryOperator$Type<($IVariable$Type)>): void
}

export namespace $IVariablesAvailableCallback {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IVariablesAvailableCallback$Type = ($IVariablesAvailableCallback);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IVariablesAvailableCallback_ = $IVariablesAvailableCallback$Type;
}}
declare module "packages/vazkii/patchouli/api/$VariableHelper" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$IVariableSerializer, $IVariableSerializer$Type} from "packages/vazkii/patchouli/api/$IVariableSerializer"

export interface $VariableHelper {

 "serializerForClass"<T>(arg0: $Class$Type<(T)>): $IVariableSerializer<(T)>
 "createFromObject"<T>(arg0: T): $IVariable
 "createFromJson"(arg0: $JsonElement$Type): $IVariable
 "registerSerializer"<T>(arg0: $IVariableSerializer$Type<(T)>, arg1: $Class$Type<(T)>): $VariableHelper
}

export namespace $VariableHelper {
const INSTANCE: $Supplier<($VariableHelper)>
function instance(): $VariableHelper
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VariableHelper$Type = ($VariableHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VariableHelper_ = $VariableHelper$Type;
}}
declare module "packages/vazkii/patchouli/client/book/$LiquidBlockVertexConsumer" {
import {$VertexFormatElement, $VertexFormatElement$Type} from "packages/com/mojang/blaze3d/vertex/$VertexFormatElement"
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Vector3f, $Vector3f$Type} from "packages/org/joml/$Vector3f"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$PoseStack$Pose, $PoseStack$Pose$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack$Pose"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$Matrix3f, $Matrix3f$Type} from "packages/org/joml/$Matrix3f"
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BakedQuad, $BakedQuad$Type} from "packages/net/minecraft/client/renderer/block/model/$BakedQuad"

export class $LiquidBlockVertexConsumer extends $Record implements $VertexConsumer {

constructor(prior: $VertexConsumer$Type, pose: $PoseStack$Type, pos: $BlockPos$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "pos"(): $BlockPos
public "vertex"(arg0: double, arg1: double, arg2: double): $VertexConsumer
public "uv"(arg0: float, arg1: float): $VertexConsumer
public "color"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $VertexConsumer
public "normal"(arg0: float, arg1: float, arg2: float): $VertexConsumer
public "overlayCoords"(arg0: integer, arg1: integer): $VertexConsumer
public "uv2"(arg0: integer, arg1: integer): $VertexConsumer
public "defaultColor"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "unsetDefaultColor"(): void
public "pose"(): $PoseStack
public "endVertex"(): void
public "prior"(): $VertexConsumer
public "overlayCoords"(arg0: integer): $VertexConsumer
public "uv2"(arg0: integer): $VertexConsumer
public "vertex"(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float, arg6: float, arg7: float, arg8: float, arg9: integer, arg10: integer, arg11: float, arg12: float, arg13: float): void
public "putBulkData"(arg0: $PoseStack$Pose$Type, arg1: $BakedQuad$Type, arg2: float, arg3: float, arg4: float, arg5: integer, arg6: integer): void
public "putBulkData"(arg0: $PoseStack$Pose$Type, arg1: $BakedQuad$Type, arg2: (float)[], arg3: float, arg4: float, arg5: float, arg6: float, arg7: (integer)[], arg8: integer, arg9: boolean): void
public "putBulkData"(arg0: $PoseStack$Pose$Type, arg1: $BakedQuad$Type, arg2: (float)[], arg3: float, arg4: float, arg5: float, arg6: (integer)[], arg7: integer, arg8: boolean): void
public "color"(arg0: integer): $VertexConsumer
public "normal"(arg0: $Matrix3f$Type, arg1: float, arg2: float, arg3: float): $VertexConsumer
public "vertex"(arg0: $Matrix4f$Type, arg1: float, arg2: float, arg3: float): $VertexConsumer
public "color"(arg0: float, arg1: float, arg2: float, arg3: float): $VertexConsumer
public "putBulkData"(arg0: $PoseStack$Pose$Type, arg1: $BakedQuad$Type, arg2: float, arg3: float, arg4: float, arg5: float, arg6: integer, arg7: integer, arg8: boolean): void
public "applyBakedLighting"(arg0: integer, arg1: $ByteBuffer$Type): integer
public "applyBakedNormals"(arg0: $Vector3f$Type, arg1: $ByteBuffer$Type, arg2: $Matrix3f$Type): void
public "misc"(arg0: $VertexFormatElement$Type, ...arg1: (integer)[]): $VertexConsumer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LiquidBlockVertexConsumer$Type = ($LiquidBlockVertexConsumer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LiquidBlockVertexConsumer_ = $LiquidBlockVertexConsumer$Type;
}}
declare module "packages/vazkii/patchouli/client/book/$EntryDisplayState" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $EntryDisplayState extends $Enum<($EntryDisplayState)> {
static readonly "UNREAD": $EntryDisplayState
static readonly "PENDING": $EntryDisplayState
static readonly "NEUTRAL": $EntryDisplayState
static readonly "COMPLETED": $EntryDisplayState
static readonly "DEFAULT_TYPE": $EntryDisplayState
readonly "hasIcon": boolean
readonly "showInInventory": boolean
readonly "hasAnimation": boolean
readonly "u": integer


public static "values"(): ($EntryDisplayState)[]
public static "valueOf"(arg0: string): $EntryDisplayState
public static "fromOrdinal"(arg0: integer): $EntryDisplayState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntryDisplayState$Type = (("unread") | ("pending") | ("neutral") | ("completed")) | ($EntryDisplayState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntryDisplayState_ = $EntryDisplayState$Type;
}}
declare module "packages/vazkii/patchouli/common/item/$PatchouliItems" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $PatchouliItems {
static readonly "BOOK_ID": $ResourceLocation
static readonly "BOOK": $Item

constructor()

public static "submitRecipeSerializerRegistrations"(arg0: $BiConsumer$Type<($ResourceLocation$Type), ($RecipeSerializer$Type<(any)>)>): void
public static "submitItemRegistrations"(arg0: $BiConsumer$Type<($ResourceLocation$Type), ($Item$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PatchouliItems$Type = ($PatchouliItems);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PatchouliItems_ = $PatchouliItems$Type;
}}
declare module "packages/vazkii/patchouli/common/command/$OpenBookCommand" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export class $OpenBookCommand {

constructor()

public static "register"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OpenBookCommand$Type = ($OpenBookCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OpenBookCommand_ = $OpenBookCommand$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/$TemplateComponent" {
import {$GuiBookEntry, $GuiBookEntry$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBookEntry"
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$IVariablesAvailableCallback, $IVariablesAvailableCallback$Type} from "packages/vazkii/patchouli/api/$IVariablesAvailableCallback"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BookPage, $BookPage$Type} from "packages/vazkii/patchouli/client/book/$BookPage"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IComponentProcessor, $IComponentProcessor$Type} from "packages/vazkii/patchouli/api/$IComponentProcessor"
import {$TemplateInclusion, $TemplateInclusion$Type} from "packages/vazkii/patchouli/client/book/template/$TemplateInclusion"
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"
import {$IVariableProvider, $IVariableProvider$Type} from "packages/vazkii/patchouli/api/$IVariableProvider"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"

export class $TemplateComponent implements $IVariablesAvailableCallback {
 "group": string
 "x": integer
 "y": integer
 "flag": string
 "advancement": string
 "guard": string

constructor()

public "compile"(arg0: $Level$Type, arg1: $IVariableProvider$Type, arg2: $IComponentProcessor$Type, arg3: $TemplateInclusion$Type): void
public "build"(arg0: $BookContentsBuilder$Type, arg1: $BookPage$Type, arg2: $BookEntry$Type, arg3: integer): void
public "getVisibleStatus"(arg0: $IComponentProcessor$Type): boolean
public "render"(arg0: $GuiGraphics$Type, arg1: $BookPage$Type, arg2: integer, arg3: integer, arg4: float): void
public "mouseClicked"(arg0: $BookPage$Type, arg1: double, arg2: double, arg3: integer): boolean
public "onDisplayed"(arg0: $BookPage$Type, arg1: $GuiBookEntry$Type, arg2: integer, arg3: integer): void
public "onVariablesAvailable"(arg0: $UnaryOperator$Type<($IVariable$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TemplateComponent$Type = ($TemplateComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TemplateComponent_ = $TemplateComponent$Type;
}}
declare module "packages/vazkii/patchouli/common/multiblock/$SimulateResultImpl" {
import {$IStateMatcher, $IStateMatcher$Type} from "packages/vazkii/patchouli/api/$IStateMatcher"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$IMultiblock$SimulateResult, $IMultiblock$SimulateResult$Type} from "packages/vazkii/patchouli/api/$IMultiblock$SimulateResult"

export class $SimulateResultImpl implements $IMultiblock$SimulateResult {

constructor(arg0: $BlockPos$Type, arg1: $IStateMatcher$Type, arg2: character)

public "test"(arg0: $Level$Type, arg1: $Rotation$Type): boolean
public "getWorldPosition"(): $BlockPos
public "getStateMatcher"(): $IStateMatcher
public "getCharacter"(): character
get "worldPosition"(): $BlockPos
get "stateMatcher"(): $IStateMatcher
get "character"(): character
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimulateResultImpl$Type = ($SimulateResultImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimulateResultImpl_ = $SimulateResultImpl$Type;
}}
declare module "packages/vazkii/patchouli/client/jei/$PatchouliJeiPlugin" {
import {$IGuiHandlerRegistration, $IGuiHandlerRegistration$Type} from "packages/mezz/jei/api/registration/$IGuiHandlerRegistration"
import {$IJeiConfigManager, $IJeiConfigManager$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigManager"
import {$IAdvancedRegistration, $IAdvancedRegistration$Type} from "packages/mezz/jei/api/registration/$IAdvancedRegistration"
import {$IVanillaCategoryExtensionRegistration, $IVanillaCategoryExtensionRegistration$Type} from "packages/mezz/jei/api/registration/$IVanillaCategoryExtensionRegistration"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IRecipeTransferRegistration, $IRecipeTransferRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeTransferRegistration"
import {$IRecipeRegistration, $IRecipeRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IJeiRuntime, $IJeiRuntime$Type} from "packages/mezz/jei/api/runtime/$IJeiRuntime"
import {$IRecipeCatalystRegistration, $IRecipeCatalystRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCatalystRegistration"
import {$IModPlugin, $IModPlugin$Type} from "packages/mezz/jei/api/$IModPlugin"
import {$IRuntimeRegistration, $IRuntimeRegistration$Type} from "packages/mezz/jei/api/registration/$IRuntimeRegistration"
import {$IRecipeCategoryRegistration, $IRecipeCategoryRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCategoryRegistration"
import {$IModIngredientRegistration, $IModIngredientRegistration$Type} from "packages/mezz/jei/api/registration/$IModIngredientRegistration"
import {$ISubtypeRegistration, $ISubtypeRegistration$Type} from "packages/mezz/jei/api/registration/$ISubtypeRegistration"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"

export class $PatchouliJeiPlugin implements $IModPlugin {

constructor()

public "registerItemSubtypes"(arg0: $ISubtypeRegistration$Type): void
public static "handleRecipeKeybind"(arg0: integer, arg1: integer, arg2: $ItemStack$Type): boolean
public "getPluginUid"(): $ResourceLocation
public "onRuntimeAvailable"(arg0: $IJeiRuntime$Type): void
public "registerVanillaCategoryExtensions"(arg0: $IVanillaCategoryExtensionRegistration$Type): void
public "registerFluidSubtypes"<T>(arg0: $ISubtypeRegistration$Type, arg1: $IPlatformFluidHelper$Type<(T)>): void
public "onConfigManagerAvailable"(arg0: $IJeiConfigManager$Type): void
public "registerGuiHandlers"(arg0: $IGuiHandlerRegistration$Type): void
public "onRuntimeUnavailable"(): void
public "registerIngredients"(arg0: $IModIngredientRegistration$Type): void
public "registerRecipeTransferHandlers"(arg0: $IRecipeTransferRegistration$Type): void
public "registerRecipeCatalysts"(arg0: $IRecipeCatalystRegistration$Type): void
public "registerRecipes"(arg0: $IRecipeRegistration$Type): void
public "registerAdvanced"(arg0: $IAdvancedRegistration$Type): void
public "registerCategories"(arg0: $IRecipeCategoryRegistration$Type): void
public "registerRuntime"(arg0: $IRuntimeRegistration$Type): void
get "pluginUid"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PatchouliJeiPlugin$Type = ($PatchouliJeiPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PatchouliJeiPlugin_ = $PatchouliJeiPlugin$Type;
}}
declare module "packages/vazkii/patchouli/xplat/$IXplatAbstractions" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$XplatModContainer, $XplatModContainer$Type} from "packages/vazkii/patchouli/xplat/$XplatModContainer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $IXplatAbstractions {

 "isPhysicalClient"(): boolean
 "fireBookReload"(arg0: $ResourceLocation$Type): void
 "sendOpenBookGui"(arg0: $ServerPlayer$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: integer): void
 "fireDrawBookScreen"(arg0: $ResourceLocation$Type, arg1: $Screen$Type, arg2: integer, arg3: integer, arg4: float, arg5: $GuiGraphics$Type): void
 "handleRecipeKeybind"(arg0: integer, arg1: integer, arg2: $ItemStack$Type): boolean
 "getAllMods"(): $Collection<($XplatModContainer)>
 "isDevEnvironment"(): boolean
 "sendReloadContentsMessage"(arg0: $MinecraftServer$Type): void
 "getModContainer"(arg0: string): $XplatModContainer
 "signalBooksLoaded"(): void
 "isModLoaded"(arg0: string): boolean
}

export namespace $IXplatAbstractions {
const INSTANCE: $IXplatAbstractions
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IXplatAbstractions$Type = ($IXplatAbstractions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IXplatAbstractions_ = $IXplatAbstractions$Type;
}}
declare module "packages/vazkii/patchouli/client/book/gui/$GuiBook" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"
import {$EntryDisplayState, $EntryDisplayState$Type} from "packages/vazkii/patchouli/client/book/$EntryDisplayState"

export class $GuiBook extends $Screen {
static readonly "FULL_WIDTH": integer
static readonly "FULL_HEIGHT": integer
static readonly "PAGE_WIDTH": integer
static readonly "PAGE_HEIGHT": integer
static readonly "TOP_PADDING": integer
static readonly "LEFT_PAGE_X": integer
static readonly "RIGHT_PAGE_X": integer
static readonly "TEXT_LINE_HEIGHT": integer
static readonly "MAX_BOOKMARKS": integer
readonly "book": $Book
 "bookLeft": integer
 "bookTop": integer
 "ticksInBook": integer
 "maxScale": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Book$Type, arg1: $Component$Type)

public static "drawSeparator"(arg0: $GuiGraphics$Type, arg1: $Book$Type, arg2: integer, arg3: integer): void
public static "openWebLink"(arg0: $Screen$Type, arg1: string): void
public "getMinecraft"(): $Minecraft
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "addRenderableWidget"<T extends ($GuiEventListener) & ($Renderable) & ($NarratableEntry)>(arg0: T): T
public "m_7856_"(): void
public "removeDrawablesIn"(arg0: $Collection$Type<(any)>): void
public static "drawFromTexture"(arg0: $GuiGraphics$Type, arg1: $Book$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer): void
public "handleButtonArrow"(arg0: $Button$Type): void
public "addBookmarkButtons"(): void
public static "drawPageFiller"(arg0: $GuiGraphics$Type, arg1: $Book$Type): void
public static "drawPageFiller"(arg0: $GuiGraphics$Type, arg1: $Book$Type, arg2: integer, arg3: integer): void
public "setTooltip"(arg0: $List$Type<($Component$Type)>): void
public "setTooltip"(...arg0: ($Component$Type)[]): void
public "handleButtonBookmark"(arg0: $Button$Type): void
public "getSpread"(): integer
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "isPauseScreen"(): boolean
public "tick"(): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "drawCenteredStringNoShadow"(arg0: $GuiGraphics$Type, arg1: $FormattedCharSequence$Type, arg2: integer, arg3: integer, arg4: integer): void
public "drawCenteredStringNoShadow"(arg0: $GuiGraphics$Type, arg1: string, arg2: integer, arg3: integer, arg4: integer): void
public "isMouseInRelativeRange"(arg0: double, arg1: double, arg2: integer, arg3: integer, arg4: integer, arg5: integer): boolean
public "onFirstOpened"(): void
public "canSeeBackButton"(): boolean
public "removeDrawablesIf"(arg0: $Predicate$Type<($Renderable$Type)>): void
public static "drawLock"(arg0: $GuiGraphics$Type, arg1: $Book$Type, arg2: integer, arg3: integer): void
public "canSeePageButton"(arg0: boolean): boolean
public "getRelativeY"(arg0: double): double
public static "playBookFlipSound"(arg0: $Book$Type): void
public "bookmarkThis"(): void
public "setTooltipStack"(arg0: $ItemStack$Type): void
public "mouseClickedScaled"(arg0: double, arg1: double, arg2: integer): boolean
public "getRelativeX"(arg0: double): double
public "drawProgressBar"(arg0: $GuiGraphics$Type, arg1: $Book$Type, arg2: integer, arg3: integer, arg4: $Predicate$Type<($BookEntry$Type)>): void
public "displayLexiconGui"(arg0: $GuiBook$Type, arg1: boolean): void
public static "drawMarking"(arg0: $GuiGraphics$Type, arg1: $Book$Type, arg2: integer, arg3: integer, arg4: integer, arg5: $EntryDisplayState$Type): void
public "canBeOpened"(): boolean
get "minecraft"(): $Minecraft
set "tooltip"(value: $List$Type<($Component$Type)>)
set "tooltip"(value: ($Component$Type)[])
get "spread"(): integer
get "pauseScreen"(): boolean
set "tooltipStack"(value: $ItemStack$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiBook$Type = ($GuiBook);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiBook_ = $GuiBook$Type;
}}
declare module "packages/vazkii/patchouli/client/book/gui/button/$GuiButtonBookArrow" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$GuiButtonBook, $GuiButtonBook$Type} from "packages/vazkii/patchouli/client/book/gui/button/$GuiButtonBook"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $GuiButtonBookArrow extends $GuiButtonBook {
readonly "left": boolean
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

constructor(arg0: $GuiBook$Type, arg1: integer, arg2: integer, arg3: boolean)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiButtonBookArrow$Type = ($GuiButtonBookArrow);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiButtonBookArrow_ = $GuiButtonBookArrow$Type;
}}
declare module "packages/vazkii/patchouli/client/book/gui/button/$GuiButtonBookConfig" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$GuiButtonBook, $GuiButtonBook$Type} from "packages/vazkii/patchouli/client/book/gui/button/$GuiButtonBook"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $GuiButtonBookConfig extends $GuiButtonBook {
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

constructor(arg0: $GuiBook$Type, arg1: integer, arg2: integer, arg3: $Button$OnPress$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiButtonBookConfig$Type = ($GuiButtonBookConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiButtonBookConfig_ = $GuiButtonBookConfig$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/$VariableAssigner" {
import {$IVariableProvider, $IVariableProvider$Type} from "packages/vazkii/patchouli/api/$IVariableProvider"
import {$IVariablesAvailableCallback, $IVariablesAvailableCallback$Type} from "packages/vazkii/patchouli/api/$IVariablesAvailableCallback"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$IComponentProcessor, $IComponentProcessor$Type} from "packages/vazkii/patchouli/api/$IComponentProcessor"
import {$TemplateInclusion, $TemplateInclusion$Type} from "packages/vazkii/patchouli/client/book/template/$TemplateInclusion"

export class $VariableAssigner {

constructor()

public static "assignVariableHolders"(arg0: $Level$Type, arg1: $IVariablesAvailableCallback$Type, arg2: $IVariableProvider$Type, arg3: $IComponentProcessor$Type, arg4: $TemplateInclusion$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VariableAssigner$Type = ($VariableAssigner);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VariableAssigner_ = $VariableAssigner$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/$BookTemplate" {
import {$GuiBookEntry, $GuiBookEntry$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBookEntry"
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$HashMap, $HashMap$Type} from "packages/java/util/$HashMap"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BookPage, $BookPage$Type} from "packages/vazkii/patchouli/client/book/$BookPage"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$TemplateInclusion, $TemplateInclusion$Type} from "packages/vazkii/patchouli/client/book/template/$TemplateInclusion"
import {$IVariableProvider, $IVariableProvider$Type} from "packages/vazkii/patchouli/api/$IVariableProvider"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"

export class $BookTemplate {
static readonly "componentTypes": $HashMap<($ResourceLocation), ($Class<(any)>)>

constructor()

public "compile"(arg0: $Level$Type, arg1: $BookContentsBuilder$Type, arg2: $IVariableProvider$Type): void
public "build"(arg0: $BookContentsBuilder$Type, arg1: $BookPage$Type, arg2: $BookEntry$Type, arg3: integer): void
public "render"(arg0: $GuiGraphics$Type, arg1: $BookPage$Type, arg2: integer, arg3: integer, arg4: float): void
public "mouseClicked"(arg0: $BookPage$Type, arg1: double, arg2: double, arg3: integer): boolean
public "onDisplayed"(arg0: $BookPage$Type, arg1: $GuiBookEntry$Type, arg2: integer, arg3: integer): void
public static "registerComponent"(arg0: $ResourceLocation$Type, arg1: $Class$Type<(any)>): void
public static "createTemplate"(arg0: $Book$Type, arg1: $BookContentsBuilder$Type, arg2: string, arg3: $TemplateInclusion$Type): $BookTemplate
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookTemplate$Type = ($BookTemplate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookTemplate_ = $BookTemplate$Type;
}}
declare module "packages/vazkii/patchouli/api/$PatchouliConfigAccess" {
import {$PatchouliConfigAccess$TextOverflowMode, $PatchouliConfigAccess$TextOverflowMode$Type} from "packages/vazkii/patchouli/api/$PatchouliConfigAccess$TextOverflowMode"
import {$List, $List$Type} from "packages/java/util/$List"

export interface $PatchouliConfigAccess {

 "inventoryButtonBook"(): string
 "disableAdvancementLocking"(): boolean
 "useShiftForQuickLookup"(): boolean
 "noAdvancementBooks"(): $List<(string)>
 "testingMode"(): boolean
 "quickLookupTime"(): integer
 "overflowMode"(): $PatchouliConfigAccess$TextOverflowMode
}

export namespace $PatchouliConfigAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PatchouliConfigAccess$Type = ($PatchouliConfigAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PatchouliConfigAccess_ = $PatchouliConfigAccess$Type;
}}
declare module "packages/vazkii/patchouli/api/$IMultiblock" {
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IMultiblock$SimulateResult, $IMultiblock$SimulateResult$Type} from "packages/vazkii/patchouli/api/$IMultiblock$SimulateResult"

export interface $IMultiblock {

 "test"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: integer, arg3: integer, arg4: integer, arg5: $Rotation$Type): boolean
 "offset"(arg0: integer, arg1: integer, arg2: integer): $IMultiblock
 "validate"(arg0: $Level$Type, arg1: $BlockPos$Type): $Rotation
 "validate"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $Rotation$Type): boolean
 "getSize"(): $Vec3i
 "getID"(): $ResourceLocation
 "place"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $Rotation$Type): void
 "offsetView"(arg0: integer, arg1: integer, arg2: integer): $IMultiblock
 "setSymmetrical"(arg0: boolean): $IMultiblock
 "isSymmetrical"(): boolean
 "setId"(arg0: $ResourceLocation$Type): $IMultiblock
 "simulate"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $Rotation$Type, arg3: boolean): $Pair<($BlockPos), ($Collection<($IMultiblock$SimulateResult)>)>
}

export namespace $IMultiblock {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMultiblock$Type = ($IMultiblock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMultiblock_ = $IMultiblock$Type;
}}
declare module "packages/vazkii/patchouli/api/$IComponentProcessor" {
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"
import {$IVariableProvider, $IVariableProvider$Type} from "packages/vazkii/patchouli/api/$IVariableProvider"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export interface $IComponentProcessor {

 "setup"(arg0: $Level$Type, arg1: $IVariableProvider$Type): void
 "process"(arg0: $Level$Type, arg1: string): $IVariable
 "refresh"(arg0: $Screen$Type, arg1: integer, arg2: integer): void
 "allowRender"(arg0: string): boolean
}

export namespace $IComponentProcessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IComponentProcessor$Type = ($IComponentProcessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IComponentProcessor_ = $IComponentProcessor$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/variable/$IngredientVariableSerializer" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$IVariableSerializer, $IVariableSerializer$Type} from "packages/vazkii/patchouli/api/$IVariableSerializer"

export class $IngredientVariableSerializer implements $IVariableSerializer<($Ingredient)> {

constructor()

public "toJson"(arg0: $Ingredient$Type): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IngredientVariableSerializer$Type = ($IngredientVariableSerializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IngredientVariableSerializer_ = $IngredientVariableSerializer$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/$PageMultiblock" {
import {$GuiBookEntry, $GuiBookEntry$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBookEntry"
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$PageWithText, $PageWithText$Type} from "packages/vazkii/patchouli/client/book/page/abstr/$PageWithText"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $PageMultiblock extends $PageWithText {

constructor()

public "build"(arg0: $Level$Type, arg1: $BookEntry$Type, arg2: $BookContentsBuilder$Type, arg3: integer): void
public "getTextHeight"(): integer
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "onDisplayed"(arg0: $GuiBookEntry$Type, arg1: integer, arg2: integer): void
public "handleButtonVisualize"(arg0: $Button$Type): void
get "textHeight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageMultiblock$Type = ($PageMultiblock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageMultiblock_ = $PageMultiblock$Type;
}}
declare module "packages/vazkii/patchouli/client/book/$BookContents" {
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$Deque, $Deque$Type} from "packages/java/util/$Deque"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BookTemplate, $BookTemplate$Type} from "packages/vazkii/patchouli/client/book/template/$BookTemplate"
import {$ItemStackUtil$StackWrapper, $ItemStackUtil$StackWrapper$Type} from "packages/vazkii/patchouli/common/util/$ItemStackUtil$StackWrapper"
import {$AbstractReadStateHolder, $AbstractReadStateHolder$Type} from "packages/vazkii/patchouli/client/book/$AbstractReadStateHolder"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ImmutableMap, $ImmutableMap$Type} from "packages/com/google/common/collect/$ImmutableMap"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Exception, $Exception$Type} from "packages/java/lang/$Exception"
import {$BookCategory, $BookCategory$Type} from "packages/vazkii/patchouli/client/book/$BookCategory"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $BookContents extends $AbstractReadStateHolder {
static readonly "addonTemplates": $Map<($ResourceLocation), ($Supplier<($BookTemplate)>)>
readonly "categories": $Map<($ResourceLocation), ($BookCategory)>
readonly "entries": $Map<($ResourceLocation), ($BookEntry)>
readonly "pamphletCategory": $BookCategory
readonly "guiStack": $Deque<($GuiBook)>
 "currentGui": $GuiBook

constructor(arg0: $Book$Type, arg1: $ImmutableMap$Type<($ResourceLocation$Type), ($BookCategory$Type)>, arg2: $ImmutableMap$Type<($ResourceLocation$Type), ($BookEntry$Type)>, arg3: $ImmutableMap$Type<($ItemStackUtil$StackWrapper$Type), ($Pair$Type<($BookEntry$Type), (integer)>)>, arg4: $BookCategory$Type)

public static "empty"(arg0: $Book$Type, arg1: $Exception$Type): $BookContents
public "getException"(): $Exception
public "getCurrentGui"(): $GuiBook
public "openLexiconGui"(arg0: $GuiBook$Type, arg1: boolean): void
public "setTopEntry"(arg0: $ResourceLocation$Type, arg1: integer): void
public "isErrored"(): boolean
public "checkValidCurrentEntry"(): void
public "getEntryForStack"(arg0: $ItemStack$Type): $Pair<($BookEntry), (integer)>
get "exception"(): $Exception
get "currentGui"(): $GuiBook
get "errored"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookContents$Type = ($BookContents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookContents_ = $BookContents$Type;
}}
declare module "packages/vazkii/patchouli/common/multiblock/$StateMatcher" {
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$TriPredicate, $TriPredicate$Type} from "packages/vazkii/patchouli/api/$TriPredicate"
import {$IStateMatcher, $IStateMatcher$Type} from "packages/vazkii/patchouli/api/$IStateMatcher"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $StateMatcher implements $IStateMatcher {
static readonly "ANY": $StateMatcher
static readonly "AIR": $StateMatcher


public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "getStatePredicate"(): $TriPredicate<($BlockGetter), ($BlockPos), ($BlockState)>
public static "fromState"(arg0: $BlockState$Type): $StateMatcher
public static "fromState"(arg0: $BlockState$Type, arg1: boolean): $StateMatcher
public static "displayOnly"(arg0: $Block$Type): $StateMatcher
public static "displayOnly"(arg0: $BlockState$Type): $StateMatcher
public static "fromBlockStrict"(arg0: $Block$Type): $StateMatcher
public static "fromPredicate"(arg0: $Block$Type, arg1: $Predicate$Type<($BlockState$Type)>): $StateMatcher
public static "fromPredicate"(arg0: $BlockState$Type, arg1: $Predicate$Type<($BlockState$Type)>): $StateMatcher
public static "fromPredicate"(arg0: $Block$Type, arg1: $TriPredicate$Type<($BlockGetter$Type), ($BlockPos$Type), ($BlockState$Type)>): $StateMatcher
public static "fromPredicate"(arg0: $BlockState$Type, arg1: $TriPredicate$Type<($BlockGetter$Type), ($BlockPos$Type), ($BlockState$Type)>): $StateMatcher
public static "fromBlockLoose"(arg0: $Block$Type): $StateMatcher
public "getDisplayedState"(arg0: long): $BlockState
public static "fromStateWithFilter"(arg0: $BlockState$Type, arg1: $Predicate$Type<($Property$Type<(any)>)>): $StateMatcher
get "statePredicate"(): $TriPredicate<($BlockGetter), ($BlockPos), ($BlockState)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StateMatcher$Type = ($StateMatcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StateMatcher_ = $StateMatcher$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/$PageTemplate" {
import {$GuiBookEntry, $GuiBookEntry$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBookEntry"
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$BookPage, $BookPage$Type} from "packages/vazkii/patchouli/client/book/$BookPage"

export class $PageTemplate extends $BookPage {

constructor()

public "build"(arg0: $Level$Type, arg1: $BookEntry$Type, arg2: $BookContentsBuilder$Type, arg3: integer): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "onDisplayed"(arg0: $GuiBookEntry$Type, arg1: integer, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageTemplate$Type = ($PageTemplate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageTemplate_ = $PageTemplate$Type;
}}
declare module "packages/vazkii/patchouli/client/book/gui/$GuiBookEntry" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Style, $Style$Type} from "packages/net/minecraft/network/chat/$Style"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$IComponentRenderContext, $IComponentRenderContext$Type} from "packages/vazkii/patchouli/api/$IComponentRenderContext"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$Book, $Book$Type} from "packages/vazkii/patchouli/common/book/$Book"

export class $GuiBookEntry extends $GuiBook implements $IComponentRenderContext {
static readonly "FULL_WIDTH": integer
static readonly "FULL_HEIGHT": integer
static readonly "PAGE_WIDTH": integer
static readonly "PAGE_HEIGHT": integer
static readonly "TOP_PADDING": integer
static readonly "LEFT_PAGE_X": integer
static readonly "RIGHT_PAGE_X": integer
static readonly "TEXT_LINE_HEIGHT": integer
static readonly "MAX_BOOKMARKS": integer
readonly "book": $Book
 "bookLeft": integer
 "bookTop": integer
 "ticksInBook": integer
 "maxScale": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $Book$Type, arg1: $BookEntry$Type)
constructor(arg0: $Book$Type, arg1: $BookEntry$Type, arg2: integer)

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "getEntry"(): $BookEntry
public "renderIngredient"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $Ingredient$Type): void
public "renderItemStack"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $ItemStack$Type): void
public "addWidget"(arg0: $AbstractWidget$Type, arg1: integer): void
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "m_7856_"(): void
public static "displayOrBookmark"(arg0: $GuiBook$Type, arg1: $BookEntry$Type): void
public "setHoverTooltipComponents"(arg0: $List$Type<($Component$Type)>): void
public "getFont"(): $Style
public "getGui"(): $Screen
public "onFirstOpened"(): void
public "bookmarkThis"(): void
public "mouseClickedScaled"(arg0: double, arg1: double, arg2: integer): boolean
public "navigateToEntry"(arg0: $ResourceLocation$Type, arg1: integer, arg2: boolean): boolean
public "setHoverTooltip"(arg0: $List$Type<(string)>): void
public "getHeaderColor"(): integer
public "isAreaHovered"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): boolean
public "registerButton"(arg0: $Button$Type, arg1: integer, arg2: $Runnable$Type): void
public "getBookTexture"(): $ResourceLocation
public "getCraftingTexture"(): $ResourceLocation
public "getTicksInBook"(): integer
public "canBeOpened"(): boolean
public "getTextColor"(): integer
get "entry"(): $BookEntry
set "hoverTooltipComponents"(value: $List$Type<($Component$Type)>)
get "font"(): $Style
get "gui"(): $Screen
set "hoverTooltip"(value: $List$Type<(string)>)
get "headerColor"(): integer
get "bookTexture"(): $ResourceLocation
get "craftingTexture"(): $ResourceLocation
get "ticksInBook"(): integer
get "textColor"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiBookEntry$Type = ($GuiBookEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiBookEntry_ = $GuiBookEntry$Type;
}}
declare module "packages/vazkii/patchouli/client/book/$BookContentLoader$LoadResult" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $BookContentLoader$LoadResult extends $Record {

constructor(json: $JsonElement$Type, addedBy: string)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "json"(): $JsonElement
public "addedBy"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookContentLoader$LoadResult$Type = ($BookContentLoader$LoadResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookContentLoader$LoadResult_ = $BookContentLoader$LoadResult$Type;
}}
declare module "packages/vazkii/patchouli/client/book/$ClientBookRegistry" {
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ClientBookRegistry {
readonly "pageTypes": $Map<($ResourceLocation), ($Class<(any)>)>
readonly "gson": $Gson
 "currentLang": string
static readonly "INSTANCE": $ClientBookRegistry


public "init"(): void
public "reload"(): void
public "reloadLocks"(arg0: boolean): void
public "displayBookGui"(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientBookRegistry$Type = ($ClientBookRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientBookRegistry_ = $ClientBookRegistry$Type;
}}
declare module "packages/vazkii/patchouli/client/book/page/$PageLink" {
import {$GuiBookEntry, $GuiBookEntry$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBookEntry"
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$PageText, $PageText$Type} from "packages/vazkii/patchouli/client/book/page/$PageText"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export class $PageLink extends $PageText {

constructor()

public "build"(arg0: $Level$Type, arg1: $BookEntry$Type, arg2: $BookContentsBuilder$Type, arg3: integer): void
public "onDisplayed"(arg0: $GuiBookEntry$Type, arg1: integer, arg2: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageLink$Type = ($PageLink);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageLink_ = $PageLink$Type;
}}
declare module "packages/vazkii/patchouli/common/multiblock/$AbstractMultiblock" {
import {$ModelDataManager, $ModelDataManager$Type} from "packages/net/minecraftforge/client/model/data/$ModelDataManager"
import {$LevelLightEngine, $LevelLightEngine$Type} from "packages/net/minecraft/world/level/lighting/$LevelLightEngine"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IMultiblock$SimulateResult, $IMultiblock$SimulateResult$Type} from "packages/vazkii/patchouli/api/$IMultiblock$SimulateResult"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$ColorResolver, $ColorResolver$Type} from "packages/net/minecraft/world/level/$ColorResolver"
import {$ClipContext, $ClipContext$Type} from "packages/net/minecraft/world/level/$ClipContext"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$IMultiblock, $IMultiblock$Type} from "packages/vazkii/patchouli/api/$IMultiblock"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"
import {$LightLayer, $LightLayer$Type} from "packages/net/minecraft/world/level/$LightLayer"
import {$LevelHeightAccessor, $LevelHeightAccessor$Type} from "packages/net/minecraft/world/level/$LevelHeightAccessor"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$ClipBlockStateContext, $ClipBlockStateContext$Type} from "packages/net/minecraft/world/level/$ClipBlockStateContext"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"

export class $AbstractMultiblock implements $IMultiblock, $BlockAndTintGetter {
 "id": $ResourceLocation

constructor()

public "offset"(arg0: integer, arg1: integer, arg2: integer): $IMultiblock
public "validate"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $Rotation$Type): boolean
public "validate"(arg0: $Level$Type, arg1: $BlockPos$Type): $Rotation
public "getSize"(): $Vec3i
public "setOffset"(arg0: integer, arg1: integer, arg2: integer): $IMultiblock
public "getID"(): $ResourceLocation
public "getBlockEntity"(arg0: $BlockPos$Type): $BlockEntity
public "getFluidState"(arg0: $BlockPos$Type): $FluidState
public "place"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $Rotation$Type): void
public "getLightEngine"(): $LevelLightEngine
public "offsetView"(arg0: integer, arg1: integer, arg2: integer): $IMultiblock
public "setSymmetrical"(arg0: boolean): $IMultiblock
public "isSymmetrical"(): boolean
public "setViewOffset"(arg0: integer, arg1: integer, arg2: integer): $IMultiblock
public "getHeight"(): integer
public "getBlockTint"(arg0: $BlockPos$Type, arg1: $ColorResolver$Type): integer
public "getRawBrightness"(arg0: $BlockPos$Type, arg1: integer): integer
public "getShade"(arg0: $Direction$Type, arg1: boolean): float
public "getBrightness"(arg0: $LightLayer$Type, arg1: $BlockPos$Type): integer
public "setWorld"(arg0: $Level$Type): void
public "getMinBuildHeight"(): integer
public "setId"(arg0: $ResourceLocation$Type): $IMultiblock
public "test"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: integer, arg3: integer, arg4: integer, arg5: $Rotation$Type): boolean
public "simulate"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $Rotation$Type, arg3: boolean): $Pair<($BlockPos), ($Collection<($IMultiblock$SimulateResult)>)>
public "canSeeSky"(arg0: $BlockPos$Type): boolean
public "getBlockEntity"<T extends $BlockEntity>(arg0: $BlockPos$Type, arg1: $BlockEntityType$Type<(T)>): $Optional<(T)>
public "getBlockStates"(arg0: $AABB$Type): $Stream<($BlockState)>
public "getMaxLightLevel"(): integer
public "isBlockInLine"(arg0: $ClipBlockStateContext$Type): $BlockHitResult
public "clipWithInteractionOverride"(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: $BlockPos$Type, arg3: $VoxelShape$Type, arg4: $BlockState$Type): $BlockHitResult
public "getLightEmission"(arg0: $BlockPos$Type): integer
public static "traverseBlocks"<T, C>(arg0: $Vec3$Type, arg1: $Vec3$Type, arg2: C, arg3: $BiFunction$Type<(C), ($BlockPos$Type), (T)>, arg4: $Function$Type<(C), (T)>): T
public "getBlockFloorHeight"(arg0: $VoxelShape$Type, arg1: $Supplier$Type<($VoxelShape$Type)>): double
public "getBlockFloorHeight"(arg0: $BlockPos$Type): double
public "clip"(arg0: $ClipContext$Type): $BlockHitResult
public "getBlockState"(arg0: $BlockPos$Type): $BlockState
public "getShade"(arg0: float, arg1: float, arg2: float, arg3: boolean): float
public "getSectionsCount"(): integer
public "isOutsideBuildHeight"(arg0: integer): boolean
public "getMinSection"(): integer
public "getMaxSection"(): integer
public "getSectionIndexFromSectionY"(arg0: integer): integer
public "getSectionYFromSectionIndex"(arg0: integer): integer
public "getSectionIndex"(arg0: integer): integer
public static "create"(arg0: integer, arg1: integer): $LevelHeightAccessor
public "isOutsideBuildHeight"(arg0: $BlockPos$Type): boolean
public "getMaxBuildHeight"(): integer
public "getExistingBlockEntity"(arg0: $BlockPos$Type): $BlockEntity
public "getModelDataManager"(): $ModelDataManager
get "size"(): $Vec3i
get "iD"(): $ResourceLocation
get "lightEngine"(): $LevelLightEngine
set "symmetrical"(value: boolean)
get "symmetrical"(): boolean
get "height"(): integer
set "world"(value: $Level$Type)
get "minBuildHeight"(): integer
set "id"(value: $ResourceLocation$Type)
get "maxLightLevel"(): integer
get "sectionsCount"(): integer
get "minSection"(): integer
get "maxSection"(): integer
get "maxBuildHeight"(): integer
get "modelDataManager"(): $ModelDataManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractMultiblock$Type = ($AbstractMultiblock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractMultiblock_ = $AbstractMultiblock$Type;
}}
declare module "packages/vazkii/patchouli/common/recipe/$BookRecipeSerializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $BookRecipeSerializer<T extends $Recipe<(any)>, U extends T> extends $Record implements $RecipeSerializer<(U)> {

constructor(compose: $RecipeSerializer$Type<(T)>, converter: $BiFunction$Type<(T), ($ResourceLocation$Type), (U)>)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compose"(): $RecipeSerializer<(T)>
public "toNetwork"(arg0: $FriendlyByteBuf$Type, arg1: U): void
public "fromNetwork"(arg0: $ResourceLocation$Type, arg1: $FriendlyByteBuf$Type): U
public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type): U
public "converter"(): $BiFunction<(T), ($ResourceLocation), (U)>
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type, arg2: $ICondition$IContext$Type): U
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BookRecipeSerializer$Type<T, U> = ($BookRecipeSerializer<(T), (U)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BookRecipeSerializer_<T, U> = $BookRecipeSerializer$Type<(T), (U)>;
}}
declare module "packages/vazkii/patchouli/client/book/gui/button/$GuiButtonBookArrowSmall" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiBook, $GuiBook$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBook"
import {$GuiButtonBook, $GuiButtonBook$Type} from "packages/vazkii/patchouli/client/book/gui/button/$GuiButtonBook"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $GuiButtonBookArrowSmall extends $GuiButtonBook {
readonly "left": boolean
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

constructor(arg0: $GuiBook$Type, arg1: integer, arg2: integer, arg3: boolean, arg4: $Supplier$Type<(boolean)>, arg5: $Button$OnPress$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiButtonBookArrowSmall$Type = ($GuiButtonBookArrowSmall);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiButtonBookArrowSmall_ = $GuiButtonBookArrowSmall$Type;
}}
declare module "packages/vazkii/patchouli/forge/xplat/$ForgeXplatModContainer" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$ModContainer, $ModContainer$Type} from "packages/net/minecraftforge/fml/$ModContainer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$XplatModContainer, $XplatModContainer$Type} from "packages/vazkii/patchouli/xplat/$XplatModContainer"

export class $ForgeXplatModContainer implements $XplatModContainer {

constructor(arg0: $ModContainer$Type)

public "getName"(): string
public "getId"(): string
public "getPath"(arg0: string): $Path
public "getRootPaths"(): $List<($Path)>
get "name"(): string
get "id"(): string
get "rootPaths"(): $List<($Path)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeXplatModContainer$Type = ($ForgeXplatModContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeXplatModContainer_ = $ForgeXplatModContainer$Type;
}}
declare module "packages/vazkii/patchouli/common/base/$PatchouliAPIImpl" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$IStateMatcher, $IStateMatcher$Type} from "packages/vazkii/patchouli/api/$IStateMatcher"
import {$PatchouliAPI$IPatchouliAPI, $PatchouliAPI$IPatchouliAPI$Type} from "packages/vazkii/patchouli/api/$PatchouliAPI$IPatchouliAPI"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$IStyleStack, $IStyleStack$Type} from "packages/vazkii/patchouli/api/$IStyleStack"
import {$PatchouliConfigAccess, $PatchouliConfigAccess$Type} from "packages/vazkii/patchouli/api/$PatchouliConfigAccess"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Property, $Property$Type} from "packages/net/minecraft/world/level/block/state/properties/$Property"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$IMultiblock, $IMultiblock$Type} from "packages/vazkii/patchouli/api/$IMultiblock"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $PatchouliAPIImpl implements $PatchouliAPI$IPatchouliAPI {

constructor()

public "registerFunction"(arg0: string, arg1: $BiFunction$Type<(string), ($IStyleStack$Type), (string)>): void
public "registerCommand"(arg0: string, arg1: $Function$Type<($IStyleStack$Type), (string)>): void
public "makeSparseMultiblock"(arg0: $Map$Type<($BlockPos$Type), ($IStateMatcher$Type)>): $IMultiblock
public "registerTemplateAsBuiltin"(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($InputStream$Type)>): void
public "getCurrentMultiblock"(): $IMultiblock
public "getConfig"(): $PatchouliConfigAccess
public "getBookStack"(arg0: $ResourceLocation$Type): $ItemStack
public "getMultiblock"(arg0: $ResourceLocation$Type): $IMultiblock
public "setConfigFlag"(arg0: string, arg1: boolean): void
public "openBookEntry"(arg0: $ServerPlayer$Type, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type, arg3: integer): void
public "openBookEntry"(arg0: $ResourceLocation$Type, arg1: $ResourceLocation$Type, arg2: integer): void
public "getOpenBookGui"(): $ResourceLocation
public "isStub"(): boolean
public "registerMultiblock"(arg0: $ResourceLocation$Type, arg1: $IMultiblock$Type): $IMultiblock
public "showMultiblock"(arg0: $IMultiblock$Type, arg1: $Component$Type, arg2: $BlockPos$Type, arg3: $Rotation$Type): void
public "clearMultiblock"(): void
public "makeMultiblock"(arg0: ((string)[])[], ...arg1: (any)[]): $IMultiblock
public "predicateMatcher"(arg0: $Block$Type, arg1: $Predicate$Type<($BlockState$Type)>): $IStateMatcher
public "predicateMatcher"(arg0: $BlockState$Type, arg1: $Predicate$Type<($BlockState$Type)>): $IStateMatcher
public "anyMatcher"(): $IStateMatcher
public "tagMatcher"(arg0: $TagKey$Type<($Block$Type)>): $IStateMatcher
public "stateMatcher"(arg0: $BlockState$Type): $IStateMatcher
public "looseBlockMatcher"(arg0: $Block$Type): $IStateMatcher
public "airMatcher"(): $IStateMatcher
public "strictBlockMatcher"(arg0: $Block$Type): $IStateMatcher
public "displayOnlyMatcher"(arg0: $BlockState$Type): $IStateMatcher
public "displayOnlyMatcher"(arg0: $Block$Type): $IStateMatcher
public "propertyMatcher"(arg0: $BlockState$Type, ...arg1: ($Property$Type<(any)>)[]): $IStateMatcher
public "getSubtitle"(arg0: $ResourceLocation$Type): $Component
public "getConfigFlag"(arg0: string): boolean
public "openBookGUI"(arg0: $ServerPlayer$Type, arg1: $ResourceLocation$Type): void
public "openBookGUI"(arg0: $ResourceLocation$Type): void
get "currentMultiblock"(): $IMultiblock
get "config"(): $PatchouliConfigAccess
get "openBookGui"(): $ResourceLocation
get "stub"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PatchouliAPIImpl$Type = ($PatchouliAPIImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PatchouliAPIImpl_ = $PatchouliAPIImpl$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/test/$ComponentCustomTest" {
import {$ICustomComponent, $ICustomComponent$Type} from "packages/vazkii/patchouli/api/$ICustomComponent"
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IComponentRenderContext, $IComponentRenderContext$Type} from "packages/vazkii/patchouli/api/$IComponentRenderContext"

export class $ComponentCustomTest implements $ICustomComponent {

constructor()

public "build"(arg0: integer, arg1: integer, arg2: integer): void
public "render"(arg0: $GuiGraphics$Type, arg1: $IComponentRenderContext$Type, arg2: float, arg3: integer, arg4: integer): void
public "mouseClicked"(arg0: $IComponentRenderContext$Type, arg1: double, arg2: double, arg3: integer): boolean
public "onVariablesAvailable"(arg0: $UnaryOperator$Type<($IVariable$Type)>): void
public "onDisplayed"(arg0: $IComponentRenderContext$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComponentCustomTest$Type = ($ComponentCustomTest);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComponentCustomTest_ = $ComponentCustomTest$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/variable/$VariableHelperImpl" {
import {$JsonElement, $JsonElement$Type} from "packages/com/google/gson/$JsonElement"
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"
import {$VariableHelper, $VariableHelper$Type} from "packages/vazkii/patchouli/api/$VariableHelper"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$IVariableSerializer, $IVariableSerializer$Type} from "packages/vazkii/patchouli/api/$IVariableSerializer"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $VariableHelperImpl implements $VariableHelper {
 "serializers": $Map<($Class<(any)>), ($IVariableSerializer<(any)>)>

constructor()

public "serializerForClass"<T>(arg0: $Class$Type<(T)>): $IVariableSerializer<(T)>
public "createFromObject"<T>(arg0: T): $IVariable
public "createFromJson"(arg0: $JsonElement$Type): $IVariable
public "registerSerializer"<T>(arg0: $IVariableSerializer$Type<(T)>, arg1: $Class$Type<(T)>): $VariableHelper
public static "instance"(): $VariableHelper
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VariableHelperImpl$Type = ($VariableHelperImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VariableHelperImpl_ = $VariableHelperImpl$Type;
}}
declare module "packages/vazkii/patchouli/client/book/template/component/$ComponentCustom" {
import {$GuiBookEntry, $GuiBookEntry$Type} from "packages/vazkii/patchouli/client/book/gui/$GuiBookEntry"
import {$BookEntry, $BookEntry$Type} from "packages/vazkii/patchouli/client/book/$BookEntry"
import {$IVariable, $IVariable$Type} from "packages/vazkii/patchouli/api/$IVariable"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$BookContentsBuilder, $BookContentsBuilder$Type} from "packages/vazkii/patchouli/client/book/$BookContentsBuilder"
import {$BookPage, $BookPage$Type} from "packages/vazkii/patchouli/client/book/$BookPage"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$TemplateComponent, $TemplateComponent$Type} from "packages/vazkii/patchouli/client/book/template/$TemplateComponent"

export class $ComponentCustom extends $TemplateComponent {
 "group": string
 "x": integer
 "y": integer
 "flag": string
 "advancement": string
 "guard": string

constructor()

public "build"(arg0: $BookContentsBuilder$Type, arg1: $BookPage$Type, arg2: $BookEntry$Type, arg3: integer): void
public "render"(arg0: $GuiGraphics$Type, arg1: $BookPage$Type, arg2: integer, arg3: integer, arg4: float): void
public "mouseClicked"(arg0: $BookPage$Type, arg1: double, arg2: double, arg3: integer): boolean
public "onDisplayed"(arg0: $BookPage$Type, arg1: $GuiBookEntry$Type, arg2: integer, arg3: integer): void
public "onVariablesAvailable"(arg0: $UnaryOperator$Type<($IVariable$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComponentCustom$Type = ($ComponentCustom);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComponentCustom_ = $ComponentCustom$Type;
}}
