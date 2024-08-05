declare module "packages/me/shedaniel/clothconfig2/gui/entries/$LongSliderEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$TooltipListEntry, $TooltipListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$TooltipListEntry"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $LongSliderEntry extends $TooltipListEntry<(long)> {

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, minimum: long, maximum: long, value: long, saveConsumer: $Consumer$Type<(long)>, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(long)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, requiresRestart: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, minimum: long, maximum: long, value: long, saveConsumer: $Consumer$Type<(long)>, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(long)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, minimum: long, maximum: long, value: long, saveConsumer: $Consumer$Type<(long)>, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(long)>)

public "getValue"(): long
/**
 * 
 * @deprecated
 */
public "setValue"(value: long): void
public "getDefaultValue"(): $Optional<(long)>
public "render"(graphics: $GuiGraphics$Type, index: integer, y: integer, x: integer, entryWidth: integer, entryHeight: integer, mouseX: integer, mouseY: integer, isHovered: boolean, delta: float): void
public "children"(): $List<(any)>
public "narratables"(): $List<(any)>
public "isEdited"(): boolean
public "setMaximum"(maximum: long): $LongSliderEntry
public "getTextGetter"(): $Function<(long), ($Component)>
public "setMinimum"(minimum: long): $LongSliderEntry
public "setTextGetter"(textGetter: $Function$Type<(long), ($Component$Type)>): $LongSliderEntry
get "value"(): long
set "value"(value: long)
get "defaultValue"(): $Optional<(long)>
get "edited"(): boolean
set "maximum"(value: long)
get "textGetter"(): $Function<(long), ($Component)>
set "minimum"(value: long)
set "textGetter"(value: $Function$Type<(long), ($Component$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongSliderEntry$Type = ($LongSliderEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongSliderEntry_ = $LongSliderEntry$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/external/com/google/gdata/util/common/base/$PercentEscaper" {
import {$UnicodeEscaper, $UnicodeEscaper$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/external/com/google/gdata/util/common/base/$UnicodeEscaper"

export class $PercentEscaper extends $UnicodeEscaper {
static readonly "SAFECHARS_URLENCODER": string
static readonly "SAFEPATHCHARS_URLENCODER": string
static readonly "SAFEQUERYSTRINGCHARS_URLENCODER": string

constructor(safeChars: string, plusForSpace: boolean)

public "escape"(s: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PercentEscaper$Type = ($PercentEscaper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PercentEscaper_ = $PercentEscaper$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$ScrollingContainer" {
import {$EasingMethod, $EasingMethod$Type} from "packages/me/shedaniel/clothconfig2/impl/$EasingMethod"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"

/**
 * 
 * @deprecated
 */
export class $ScrollingContainer {
 "scrollAmount": double
 "scrollTarget": double
 "start": long
 "duration": long
 "draggingScrollBar": boolean

constructor()

public "offset"(value: double, animated: boolean): void
public "getBounds"(): $Rectangle
public "hasScrollBar"(): boolean
public "updatePosition"(delta: float): void
public static "ease"(start: double, end: double, amount: double, easingMethod: $EasingMethod$Type): double
public "clamp"(v: double): double
public "clamp"(v: double, clampExtension: double): double
public "scrollTo"(value: double, animated: boolean, duration: long): void
public "scrollTo"(value: double, animated: boolean): void
public "getMaxScroll"(): integer
public static "clampExtension"(v: double, maxScroll: double, clampExtension: double): double
public static "clampExtension"(value: double, maxScroll: double): double
public "mouseDragged"(mouseX: double, mouseY: double, button: integer, dx: double, dy: double): boolean
public "mouseDragged"(mouseX: double, mouseY: double, button: integer, dx: double, dy: double, snapToRows: boolean, rowSize: double): boolean
public "renderScrollBar"(graphics: $GuiGraphics$Type, background: integer, alpha: float, scrollBarAlphaOffset: float): void
public "renderScrollBar"(graphics: $GuiGraphics$Type): void
public static "handleScrollingPosition"(target: (double)[], scroll: double, maxScroll: double, delta: float, start: double, duration: double): double
public static "handleScrollingPosition"(target: (double)[], scroll: double, maxScroll: double, delta: float, start: double, duration: double, bounceBackMultiplier: double, easingMethod: $EasingMethod$Type): double
public "updateDraggingState"(mouseX: double, mouseY: double, button: integer): boolean
public "getScissorBounds"(): $Rectangle
public "getScrollBarX"(): integer
public "getMaxScrollHeight"(): integer
get "bounds"(): $Rectangle
get "maxScroll"(): integer
get "scissorBounds"(): $Rectangle
get "scrollBarX"(): integer
get "maxScrollHeight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScrollingContainer$Type = ($ScrollingContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScrollingContainer_ = $ScrollingContainer$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$TabbedConfigScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ConfigScreen, $ConfigScreen$Type} from "packages/me/shedaniel/clothconfig2/api/$ConfigScreen"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$Tooltip, $Tooltip$Type} from "packages/me/shedaniel/clothconfig2/api/$Tooltip"

export interface $TabbedConfigScreen extends $ConfigScreen {

 "getSelectedCategory"(): $Component
 "registerCategoryBackground"(arg0: string, arg1: $ResourceLocation$Type): void
 "setSavingRunnable"(arg0: $Runnable$Type): void
 "saveAll"(arg0: boolean): void
 "isRequiresRestart"(): boolean
 "isEdited"(): boolean
 "matchesSearch"(arg0: $Iterator$Type<(string)>): boolean
 "getBackgroundLocation"(): $ResourceLocation
 "addTooltip"(arg0: $Tooltip$Type): void
 "setAfterInitConsumer"(arg0: $Consumer$Type<($Screen$Type)>): void
}

export namespace $TabbedConfigScreen {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TabbedConfigScreen$Type = ($TabbedConfigScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TabbedConfigScreen_ = $TabbedConfigScreen$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$SelectionListEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$TooltipListEntry, $TooltipListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$TooltipListEntry"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $SelectionListEntry<T> extends $TooltipListEntry<(T)> {

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, valuesArray: (T)[], value: T, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(T)>, saveConsumer: $Consumer$Type<(T)>, nameProvider: $Function$Type<(T), ($Component$Type)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, requiresRestart: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, valuesArray: (T)[], value: T, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(T)>, saveConsumer: $Consumer$Type<(T)>, nameProvider: $Function$Type<(T), ($Component$Type)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, valuesArray: (T)[], value: T, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(T)>, saveConsumer: $Consumer$Type<(T)>, nameProvider: $Function$Type<(T), ($Component$Type)>)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, valuesArray: (T)[], value: T, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(T)>, saveConsumer: $Consumer$Type<(T)>)

public "getValue"(): T
public "getDefaultValue"(): $Optional<(T)>
public "render"(graphics: $GuiGraphics$Type, index: integer, y: integer, x: integer, entryWidth: integer, entryHeight: integer, mouseX: integer, mouseY: integer, isHovered: boolean, delta: float): void
public "children"(): $List<(any)>
public "narratables"(): $List<(any)>
public "isEdited"(): boolean
get "value"(): T
get "defaultValue"(): $Optional<(T)>
get "edited"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SelectionListEntry$Type<T> = ($SelectionListEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SelectionListEntry_<T> = $SelectionListEntry$Type<(T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$BlockEntryToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $BlockEntryToken extends $Token {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntryToken$Type = ($BlockEntryToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntryToken_ = $BlockEntryToken$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/util/$ArrayStack" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ArrayStack<T> {

constructor(initSize: integer)

public "clear"(): void
public "isEmpty"(): boolean
public "push"(obj: T): void
public "pop"(): T
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArrayStack$Type<T> = ($ArrayStack<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArrayStack_<T> = $ArrayStack$Type<(T)>;
}}
declare module "packages/me/shedaniel/autoconfig/serializer/$PartitioningSerializer$GlobalData" {
import {$ConfigData, $ConfigData$Type} from "packages/me/shedaniel/autoconfig/$ConfigData"

export class $PartitioningSerializer$GlobalData implements $ConfigData {

constructor()

public "validatePostLoad"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PartitioningSerializer$GlobalData$Type = ($PartitioningSerializer$GlobalData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PartitioningSerializer$GlobalData_ = $PartitioningSerializer$GlobalData$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/widget/$DynamicEntryListWidget$Entry" {
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$ComponentPath, $ComponentPath$Type} from "packages/net/minecraft/client/gui/$ComponentPath"
import {$FocusNavigationEvent, $FocusNavigationEvent$Type} from "packages/net/minecraft/client/gui/navigation/$FocusNavigationEvent"
import {$HideableWidget, $HideableWidget$Type} from "packages/me/shedaniel/clothconfig2/api/$HideableWidget"
import {$List, $List$Type} from "packages/java/util/$List"
import {$DynamicEntryListWidget, $DynamicEntryListWidget$Type} from "packages/me/shedaniel/clothconfig2/gui/widget/$DynamicEntryListWidget"
import {$DisableableWidget, $DisableableWidget$Type} from "packages/me/shedaniel/clothconfig2/api/$DisableableWidget"
import {$ScreenRectangle, $ScreenRectangle$Type} from "packages/net/minecraft/client/gui/navigation/$ScreenRectangle"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$TickableWidget, $TickableWidget$Type} from "packages/me/shedaniel/clothconfig2/api/$TickableWidget"
import {$Requirement, $Requirement$Type} from "packages/me/shedaniel/clothconfig2/api/$Requirement"

export class $DynamicEntryListWidget$Entry<E extends $DynamicEntryListWidget$Entry<(E)>> implements $GuiEventListener, $TickableWidget, $HideableWidget, $DisableableWidget {

constructor()

public "getParent"(): $DynamicEntryListWidget<(E)>
public "setParent"(parent: $DynamicEntryListWidget$Type<(E)>): void
public "tick"(): void
public "isEnabled"(): boolean
public "getRequirement"(): $Requirement
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: boolean, arg9: float): void
public "isMouseOver"(double_1: double, double_2: double): boolean
public "narratables"(): $List<(any)>
public "setDisplayRequirement"(requirement: $Requirement$Type): void
/**
 * 
 * @deprecated
 */
public "getMorePossibleHeight"(): integer
public "getDisplayRequirement"(): $Requirement
public "isDisplayed"(): boolean
public "getItemHeight"(): integer
public "setRequirement"(requirement: $Requirement$Type): void
public "getCurrentFocusPath"(): $ComponentPath
public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "nextFocusPath"(arg0: $FocusNavigationEvent$Type): $ComponentPath
public "getRectangle"(): $ScreenRectangle
public "setFocused"(arg0: boolean): void
public "mouseReleased"(arg0: double, arg1: double, arg2: integer): boolean
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "charTyped"(arg0: character, arg1: integer): boolean
public "mouseScrolled"(arg0: double, arg1: double, arg2: double): boolean
public "mouseDragged"(arg0: double, arg1: double, arg2: integer, arg3: double, arg4: double): boolean
public "isFocused"(): boolean
public "keyReleased"(arg0: integer, arg1: integer, arg2: integer): boolean
public "mouseMoved"(arg0: double, arg1: double): void
public "getTabOrderGroup"(): integer
get "parent"(): $DynamicEntryListWidget<(E)>
set "parent"(value: $DynamicEntryListWidget$Type<(E)>)
get "enabled"(): boolean
get "requirement"(): $Requirement
set "displayRequirement"(value: $Requirement$Type)
get "morePossibleHeight"(): integer
get "displayRequirement"(): $Requirement
get "displayed"(): boolean
get "itemHeight"(): integer
set "requirement"(value: $Requirement$Type)
get "currentFocusPath"(): $ComponentPath
get "rectangle"(): $ScreenRectangle
set "focused"(value: boolean)
get "focused"(): boolean
get "tabOrderGroup"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicEntryListWidget$Entry$Type<E> = ($DynamicEntryListWidget$Entry<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicEntryListWidget$Entry_<E> = $DynamicEntryListWidget$Entry$Type<(E)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonGrammar" {
import {$JsonGrammar$Builder, $JsonGrammar$Builder$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonGrammar$Builder"

export class $JsonGrammar {
static readonly "JANKSON": $JsonGrammar
static readonly "JSON5": $JsonGrammar
static readonly "STRICT": $JsonGrammar
static readonly "COMPACT": $JsonGrammar

constructor()

public static "builder"(): $JsonGrammar$Builder
public "shouldOutputWhitespace"(): boolean
public "hasComments"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonGrammar$Type = ($JsonGrammar);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonGrammar_ = $JsonGrammar$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/reader/$ReaderException" {
import {$YAMLException, $YAMLException$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$YAMLException"

export class $ReaderException extends $YAMLException {

constructor(name: string, position: integer, codePoint: integer, message: string)

public "getName"(): string
public "toString"(): string
public "getCodePoint"(): integer
public "getPosition"(): integer
get "name"(): string
get "codePoint"(): integer
get "position"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReaderException$Type = ($ReaderException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReaderException_ = $ReaderException$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$Requirement" {
import {$ValueHolder, $ValueHolder$Type} from "packages/me/shedaniel/clothconfig2/api/$ValueHolder"

export interface $Requirement {

 "check"(): boolean

(firstDependency: $ValueHolder$Type<(T)>, secondDependency: $ValueHolder$Type<(T)>): $Requirement
}

export namespace $Requirement {
function matches<T>(firstDependency: $ValueHolder$Type<(T)>, secondDependency: $ValueHolder$Type<(T)>): $Requirement
function one(...requirements: ($Requirement$Type)[]): $Requirement
function not(requirement: $Requirement$Type): $Requirement
function all(...requirements: ($Requirement$Type)[]): $Requirement
function any(...requirements: ($Requirement$Type)[]): $Requirement
function isTrue(dependency: $ValueHolder$Type<(boolean)>): $Requirement
function none(...requirements: ($Requirement$Type)[]): $Requirement
function isValue<T>(dependency: $ValueHolder$Type<(T)>, firstValue: T, ...otherValues: (T)[]): $Requirement
function isFalse(dependency: $ValueHolder$Type<(boolean)>): $Requirement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Requirement$Type = ($Requirement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Requirement_ = $Requirement$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/serializer/$AnchorGenerator" {
import {$Node, $Node$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Node"

export interface $AnchorGenerator {

 "nextAnchor"(arg0: $Node$Type): string

(arg0: $Node$Type): string
}

export namespace $AnchorGenerator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnchorGenerator$Type = ($AnchorGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnchorGenerator_ = $AnchorGenerator$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg12$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg12$Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, T> {

 "construct"(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11, arg11: A12): T

(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11, arg11: A12): T
}

export namespace $RecordValueAnimatorArgs$Arg12$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg12$Op$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, T> = ($RecordValueAnimatorArgs$Arg12$Op<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg12$Op_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, T> = $RecordValueAnimatorArgs$Arg12$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ObjectValueWriter" {
import {$WriterContext, $WriterContext$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$WriterContext"
import {$ValueWriter, $ValueWriter$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueWriter"

export class $ObjectValueWriter implements $ValueWriter {


public "write"(value: any, context: $WriterContext$Type): void
public "canWrite"(value: any): boolean
public "isPrimitiveType"(): boolean
get "primitiveType"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectValueWriter$Type = ($ObjectValueWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectValueWriter_ = $ObjectValueWriter$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg12$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg12$Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>, arg12: $RecordValueAnimatorArgs$Setter$Type<(A12)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>, arg12: $RecordValueAnimatorArgs$Setter$Type<(A12)>): void
}

export namespace $RecordValueAnimatorArgs$Arg12$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg12$Up$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, T> = ($RecordValueAnimatorArgs$Arg12$Up<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg12$Up_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, T> = $RecordValueAnimatorArgs$Arg12$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/widget/$DynamicNewSmoothScrollingEntryListWidget" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$DynamicEntryListWidget$Entry, $DynamicEntryListWidget$Entry$Type} from "packages/me/shedaniel/clothconfig2/gui/widget/$DynamicEntryListWidget$Entry"
import {$DynamicEntryListWidget, $DynamicEntryListWidget$Type} from "packages/me/shedaniel/clothconfig2/gui/widget/$DynamicEntryListWidget"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

/**
 * 
 * @deprecated
 */
export class $DynamicNewSmoothScrollingEntryListWidget<E extends $DynamicEntryListWidget$Entry<(E)>> extends $DynamicEntryListWidget<(E)> {
 "width": integer
 "height": integer
 "top": integer
 "bottom": integer
 "right": integer
 "left": integer

constructor(client: $Minecraft$Type, width: integer, height: integer, top: integer, bottom: integer, backgroundLocation: $ResourceLocation$Type)

public "offset"(value: double, animated: boolean): void
public "render"(graphics: $GuiGraphics$Type, mouseX: integer, mouseY: integer, delta: float): void
public "mouseScrolled"(mouseX: double, mouseY: double, amount: double): boolean
public "mouseDragged"(mouseX: double, mouseY: double, button: integer, deltaX: double, deltaY: double): boolean
public "scrollTo"(value: double, animated: boolean, duration: long): void
public "scrollTo"(value: double, animated: boolean): void
public "capYPosition"(double_1: double): void
public "isSmoothScrolling"(): boolean
public "setSmoothScrolling"(smoothScrolling: boolean): void
get "smoothScrolling"(): boolean
set "smoothScrolling"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicNewSmoothScrollingEntryListWidget$Type<E> = ($DynamicNewSmoothScrollingEntryListWidget<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicNewSmoothScrollingEntryListWidget_<E> = $DynamicNewSmoothScrollingEntryListWidget$Type<(E)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$BlockMappingStartToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $BlockMappingStartToken extends $Token {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockMappingStartToken$Type = ($BlockMappingStartToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockMappingStartToken_ = $BlockMappingStartToken$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractRangeFieldBuilder" {
import {$AbstractFieldBuilder, $AbstractFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractFieldBuilder"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$FieldBuilder, $FieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$FieldBuilder"

export class $AbstractRangeFieldBuilder<T, A extends $AbstractConfigListEntry<(any)>, SELF extends $FieldBuilder<(T), (A), (SELF)>> extends $AbstractFieldBuilder<(T), (A), (SELF)> {


public "setMin"(min: T): SELF
public "setMax"(max: T): SELF
public "removeMin"(): SELF
public "removeMax"(): SELF
set "min"(value: T)
set "max"(value: T)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractRangeFieldBuilder$Type<T, A, SELF> = ($AbstractRangeFieldBuilder<(T), (A), (SELF)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractRangeFieldBuilder_<T, A, SELF> = $AbstractRangeFieldBuilder$Type<(T), (A), (SELF)>;
}}
declare module "packages/me/shedaniel/math/impl/$PointHelper" {
import {$FloatingPoint, $FloatingPoint$Type} from "packages/me/shedaniel/math/$FloatingPoint"
import {$Point, $Point$Type} from "packages/me/shedaniel/math/$Point"

export class $PointHelper {

constructor()

public static "ofFloatingMouse"(): $FloatingPoint
public static "getMouseFloatingY"(): double
public static "getMouseFloatingX"(): double
public static "getMouseX"(): integer
public static "getMouseY"(): integer
public static "ofMouse"(): $Point
get "mouseFloatingY"(): double
get "mouseFloatingX"(): double
get "mouseX"(): integer
get "mouseY"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PointHelper$Type = ($PointHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PointHelper_ = $PointHelper$Type;
}}
declare module "packages/me/shedaniel/math/$Dimension" {
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$FloatingDimension, $FloatingDimension$Type} from "packages/me/shedaniel/math/$FloatingDimension"

export class $Dimension implements $Cloneable {
 "width": integer
 "height": integer

constructor(arg0: integer, arg1: integer)
constructor(arg0: double, arg1: double)
constructor()
constructor(arg0: $FloatingDimension$Type)
constructor(arg0: $Dimension$Type)

public "getFloatingSize"(): $FloatingDimension
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): $Dimension
public "getSize"(): $Dimension
public "setSize"(arg0: $Dimension$Type): void
public "setSize"(arg0: double, arg1: double): void
public "setSize"(arg0: $FloatingDimension$Type): void
public "setSize"(arg0: integer, arg1: integer): void
public "getWidth"(): integer
public "getHeight"(): integer
get "floatingSize"(): $FloatingDimension
get "size"(): $Dimension
set "size"(value: $Dimension$Type)
set "size"(value: $FloatingDimension$Type)
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Dimension$Type = ($Dimension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Dimension_ = $Dimension$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/$ScissorsHandlerImpl" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ScissorsHandler, $ScissorsHandler$Type} from "packages/me/shedaniel/clothconfig2/api/$ScissorsHandler"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"

export class $ScissorsHandlerImpl implements $ScissorsHandler {
static readonly "INSTANCE": $ScissorsHandler

constructor()

public "scissor"(rectangle: $Rectangle$Type): void
public "removeLastScissor"(): void
public "clearScissors"(): void
public "applyScissors"(): void
public "getScissorsAreas"(): $List<($Rectangle)>
public "_applyScissor"(r: $Rectangle$Type): void
get "scissorsAreas"(): $List<($Rectangle)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScissorsHandlerImpl$Type = ($ScissorsHandlerImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScissorsHandlerImpl_ = $ScissorsHandlerImpl$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DumperOptions$FlowStyle extends $Enum<($DumperOptions$FlowStyle)> {
static readonly "FLOW": $DumperOptions$FlowStyle
static readonly "BLOCK": $DumperOptions$FlowStyle
static readonly "AUTO": $DumperOptions$FlowStyle


public "toString"(): string
public static "values"(): ($DumperOptions$FlowStyle)[]
public static "valueOf"(name: string): $DumperOptions$FlowStyle
/**
 * 
 * @deprecated
 */
public static "fromBoolean"(flowStyle: boolean): $DumperOptions$FlowStyle
public "getStyleBoolean"(): boolean
get "styleBoolean"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DumperOptions$FlowStyle$Type = (("auto") | ("block") | ("flow")) | ($DumperOptions$FlowStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DumperOptions$FlowStyle_ = $DumperOptions$FlowStyle$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$DirectiveToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $DirectiveToken<T> extends $Token {

constructor(name: string, value: $List$Type<(T)>, startMark: $Mark$Type, endMark: $Mark$Type)

public "getName"(): string
public "getValue"(): $List<(T)>
public "getTokenId"(): $Token$ID
get "name"(): string
get "value"(): $List<(T)>
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DirectiveToken$Type<T> = ($DirectiveToken<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DirectiveToken_<T> = $DirectiveToken$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$LongFieldBuilder" {
import {$AbstractRangeFieldBuilder, $AbstractRangeFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractRangeFieldBuilder"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$LongListEntry, $LongListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$LongListEntry"

export class $LongFieldBuilder extends $AbstractRangeFieldBuilder<(long), ($LongListEntry), ($LongFieldBuilder)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, value: long)

public "setMin"(min: long): $LongFieldBuilder
public "setSaveConsumer"(saveConsumer: $Consumer$Type<(long)>): $LongFieldBuilder
public "requireRestart"(): $LongFieldBuilder
public "setTooltip"(tooltip: $Optional$Type<(($Component$Type)[])>): $LongFieldBuilder
public "setErrorSupplier"(errorSupplier: $Function$Type<(long), ($Optional$Type<($Component$Type)>)>): $LongFieldBuilder
public "setDefaultValue"(defaultValue: long): $LongFieldBuilder
public "setMax"(max: long): $LongFieldBuilder
public "removeMax"(): $LongFieldBuilder
set "min"(value: long)
set "saveConsumer"(value: $Consumer$Type<(long)>)
set "tooltip"(value: $Optional$Type<(($Component$Type)[])>)
set "errorSupplier"(value: $Function$Type<(long), ($Optional$Type<($Component$Type)>)>)
set "defaultValue"(value: long)
set "max"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongFieldBuilder$Type = ($LongFieldBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongFieldBuilder_ = $LongFieldBuilder$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$DoubleListListEntry$DoubleListCell" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$AbstractTextFieldListListEntry$AbstractTextFieldListCell, $AbstractTextFieldListListEntry$AbstractTextFieldListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractTextFieldListListEntry$AbstractTextFieldListCell"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$DoubleListListEntry, $DoubleListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DoubleListListEntry"

export class $DoubleListListEntry$DoubleListCell extends $AbstractTextFieldListListEntry$AbstractTextFieldListCell<(double), ($DoubleListListEntry$DoubleListCell), ($DoubleListListEntry)> {

constructor(value: double, listListEntry: $DoubleListListEntry$Type)

public "getValue"(): double
public "getError"(): $Optional<($Component)>
get "value"(): double
get "error"(): $Optional<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleListListEntry$DoubleListCell$Type = ($DoubleListListEntry$DoubleListCell);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleListListEntry$DoubleListCell_ = $DoubleListListEntry$DoubleListCell$Type;
}}
declare module "packages/me/shedaniel/autoconfig/gui/$DefaultGuiProviders" {
import {$GuiRegistry, $GuiRegistry$Type} from "packages/me/shedaniel/autoconfig/gui/registry/$GuiRegistry"

export class $DefaultGuiProviders {


public static "apply"(registry: $GuiRegistry$Type): $GuiRegistry
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultGuiProviders$Type = ($DefaultGuiProviders);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultGuiProviders_ = $DefaultGuiProviders$Type;
}}
declare module "packages/me/shedaniel/autoconfig/example/$ExampleConfig$ExampleEnum" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ExampleConfig$ExampleEnum extends $Enum<($ExampleConfig$ExampleEnum)> {
static readonly "FOO": $ExampleConfig$ExampleEnum
static readonly "BAR": $ExampleConfig$ExampleEnum
static readonly "BAZ": $ExampleConfig$ExampleEnum


public static "values"(): ($ExampleConfig$ExampleEnum)[]
public static "valueOf"(name: string): $ExampleConfig$ExampleEnum
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExampleConfig$ExampleEnum$Type = (("bar") | ("foo") | ("baz")) | ($ExampleConfig$ExampleEnum);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExampleConfig$ExampleEnum_ = $ExampleConfig$ExampleEnum$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$SelectionElement" {
import {$DropdownBoxEntry$DropdownMenuElement, $DropdownBoxEntry$DropdownMenuElement$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$DropdownMenuElement"
import {$DropdownBoxEntry$SelectionCellCreator, $DropdownBoxEntry$SelectionCellCreator$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$SelectionCellCreator"
import {$DropdownBoxEntry, $DropdownBoxEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractContainerEventHandler, $AbstractContainerEventHandler$Type} from "packages/net/minecraft/client/gui/components/events/$AbstractContainerEventHandler"
import {$DropdownBoxEntry$SelectionTopCellElement, $DropdownBoxEntry$SelectionTopCellElement$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$SelectionTopCellElement"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"

export class $DropdownBoxEntry$SelectionElement<R> extends $AbstractContainerEventHandler implements $Renderable {

constructor(entry: $DropdownBoxEntry$Type<(R)>, bounds: $Rectangle$Type, menu: $DropdownBoxEntry$DropdownMenuElement$Type<(R)>, topRenderer: $DropdownBoxEntry$SelectionTopCellElement$Type<(R)>, cellCreator: $DropdownBoxEntry$SelectionCellCreator$Type<(R)>)

public "getValue"(): R
public "children"(): $List<(any)>
public "render"(graphics: $GuiGraphics$Type, mouseX: integer, mouseY: integer, delta: float): void
public "getMorePossibleHeight"(): integer
public "lateRender"(graphics: $GuiGraphics$Type, mouseX: integer, mouseY: integer, delta: float): void
public "mouseClicked"(double_1: double, double_2: double, int_1: integer): boolean
public "mouseScrolled"(double_1: double, double_2: double, double_3: double): boolean
/**
 * 
 * @deprecated
 */
public "getTopRenderer"(): $DropdownBoxEntry$SelectionTopCellElement<(R)>
get "value"(): R
get "morePossibleHeight"(): integer
get "topRenderer"(): $DropdownBoxEntry$SelectionTopCellElement<(R)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropdownBoxEntry$SelectionElement$Type<R> = ($DropdownBoxEntry$SelectionElement<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropdownBoxEntry$SelectionElement_<R> = $DropdownBoxEntry$SelectionElement$Type<(R)>;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$EnumSelectorBuilder" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$EnumListEntry, $EnumListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$EnumListEntry"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AbstractFieldBuilder, $AbstractFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractFieldBuilder"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $EnumSelectorBuilder<T extends $Enum<(any)>> extends $AbstractFieldBuilder<(T), ($EnumListEntry<(T)>), ($EnumSelectorBuilder<(T)>)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, clazz: $Class$Type<(T)>, value: T)

public "setSaveConsumer"(saveConsumer: $Consumer$Type<(T)>): $EnumSelectorBuilder<(T)>
public "setTooltipSupplier"(tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>): $EnumSelectorBuilder<(T)>
public "setEnumNameProvider"(enumNameProvider: $Function$Type<($Enum$Type), ($Component$Type)>): $EnumSelectorBuilder<(T)>
public "requireRestart"(): $EnumSelectorBuilder<(T)>
public "setTooltip"(...tooltip: ($Component$Type)[]): $EnumSelectorBuilder<(T)>
public "setErrorSupplier"(errorSupplier: $Function$Type<(T), ($Optional$Type<($Component$Type)>)>): $EnumSelectorBuilder<(T)>
public "setDefaultValue"(defaultValue: $Supplier$Type<(T)>): $EnumSelectorBuilder<(T)>
public "setDefaultValue"(defaultValue: T): $EnumSelectorBuilder<(T)>
set "saveConsumer"(value: $Consumer$Type<(T)>)
set "tooltipSupplier"(value: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
set "enumNameProvider"(value: $Function$Type<($Enum$Type), ($Component$Type)>)
set "tooltip"(value: ($Component$Type)[])
set "errorSupplier"(value: $Function$Type<(T), ($Optional$Type<($Component$Type)>)>)
set "defaultValue"(value: $Supplier$Type<(T)>)
set "defaultValue"(value: T)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumSelectorBuilder$Type<T> = ($EnumSelectorBuilder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumSelectorBuilder_<T> = $EnumSelectorBuilder$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$BooleanListEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipListEntry, $TooltipListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$TooltipListEntry"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $BooleanListEntry extends $TooltipListEntry<(boolean)> {

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, bool: boolean, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(boolean)>, saveConsumer: $Consumer$Type<(boolean)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, requiresRestart: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, bool: boolean, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(boolean)>, saveConsumer: $Consumer$Type<(boolean)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, bool: boolean, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(boolean)>, saveConsumer: $Consumer$Type<(boolean)>)

public "getValue"(): boolean
public "getDefaultValue"(): $Optional<(boolean)>
public "render"(graphics: $GuiGraphics$Type, index: integer, y: integer, x: integer, entryWidth: integer, entryHeight: integer, mouseX: integer, mouseY: integer, isHovered: boolean, delta: float): void
public "children"(): $List<(any)>
public "narratables"(): $List<(any)>
public "isEdited"(): boolean
public "getYesNoText"(bool: boolean): $Component
get "value"(): boolean
get "defaultValue"(): $Optional<(boolean)>
get "edited"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BooleanListEntry$Type = ($BooleanListEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BooleanListEntry_ = $BooleanListEntry$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$NumberParserContext" {
import {$ParserContext, $ParserContext$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$ParserContext"
import {$Jankson, $Jankson$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$Jankson"
import {$JsonPrimitive, $JsonPrimitive$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonPrimitive"

export class $NumberParserContext implements $ParserContext<($JsonPrimitive)> {

constructor(firstCodePoint: integer)

public "consume"(codePoint: integer, loader: $Jankson$Type): boolean
public "eof"(): void
public "getResult"(): $JsonPrimitive
public "isComplete"(): boolean
get "result"(): $JsonPrimitive
get "complete"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NumberParserContext$Type = ($NumberParserContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NumberParserContext_ = $NumberParserContext$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$StringListBuilder" {
import {$AbstractListBuilder, $AbstractListBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractListBuilder"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$StringListListEntry, $StringListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$StringListListEntry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$StringListListEntry$StringListCell, $StringListListEntry$StringListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$StringListListEntry$StringListCell"

export class $StringListBuilder extends $AbstractListBuilder<(string), ($StringListListEntry), ($StringListBuilder)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, value: $List$Type<(string)>)

public "setTooltip"(tooltip: $Optional$Type<(($Component$Type)[])>): $StringListBuilder
public "setErrorSupplier"(errorSupplier: $Function$Type<($List$Type<(string)>), ($Optional$Type<($Component$Type)>)>): $StringListBuilder
public "getCellErrorSupplier"(): $Function<(string), ($Optional<($Component)>)>
public "setCellErrorSupplier"(cellErrorSupplier: $Function$Type<(string), ($Optional$Type<($Component$Type)>)>): $StringListBuilder
public "setDeleteButtonEnabled"(deleteButtonEnabled: boolean): $StringListBuilder
public "setCreateNewInstance"(createNewInstance: $Function$Type<($StringListListEntry$Type), ($StringListListEntry$StringListCell$Type)>): $StringListBuilder
public "setDefaultValue"(defaultValue: $List$Type<(string)>): $StringListBuilder
set "tooltip"(value: $Optional$Type<(($Component$Type)[])>)
set "errorSupplier"(value: $Function$Type<($List$Type<(string)>), ($Optional$Type<($Component$Type)>)>)
get "cellErrorSupplier"(): $Function<(string), ($Optional<($Component)>)>
set "cellErrorSupplier"(value: $Function$Type<(string), ($Optional$Type<($Component$Type)>)>)
set "deleteButtonEnabled"(value: boolean)
set "createNewInstance"(value: $Function$Type<($StringListListEntry$Type), ($StringListListEntry$StringListCell$Type)>)
set "defaultValue"(value: $List$Type<(string)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringListBuilder$Type = ($StringListBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringListBuilder_ = $StringListBuilder$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/parser/$ParserException" {
import {$MarkedYAMLException, $MarkedYAMLException$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$MarkedYAMLException"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $ParserException extends $MarkedYAMLException {

constructor(context: string, contextMark: $Mark$Type, problem: string, problemMark: $Mark$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParserException$Type = ($ParserException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParserException_ = $ParserException$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$DisableableWidget" {
import {$Requirement, $Requirement$Type} from "packages/me/shedaniel/clothconfig2/api/$Requirement"

export interface $DisableableWidget {

 "isEnabled"(): boolean
 "getRequirement"(): $Requirement
 "setRequirement"(arg0: $Requirement$Type): void
}

export namespace $DisableableWidget {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DisableableWidget$Type = ($DisableableWidget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DisableableWidget_ = $DisableableWidget$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$TableArrayValueWriter" {
import {$ArrayValueWriter, $ArrayValueWriter$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ArrayValueWriter"
import {$WriterContext, $WriterContext$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$WriterContext"

export class $TableArrayValueWriter extends $ArrayValueWriter {


public "toString"(): string
public "write"(from: any, context: $WriterContext$Type): void
public "canWrite"(value: any): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TableArrayValueWriter$Type = ($TableArrayValueWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TableArrayValueWriter_ = $TableArrayValueWriter$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg7$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg7$Up<A1, A2, A3, A4, A5, A6, A7, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>): void
}

export namespace $RecordValueAnimatorArgs$Arg7$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg7$Up$Type<A1, A2, A3, A4, A5, A6, A7, T> = ($RecordValueAnimatorArgs$Arg7$Up<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg7$Up_<A1, A2, A3, A4, A5, A6, A7, T> = $RecordValueAnimatorArgs$Arg7$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (T)>;
}}
declare module "packages/me/shedaniel/autoconfig/serializer/$DummyConfigSerializer" {
import {$Config, $Config$Type} from "packages/me/shedaniel/autoconfig/annotation/$Config"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ConfigSerializer, $ConfigSerializer$Type} from "packages/me/shedaniel/autoconfig/serializer/$ConfigSerializer"
import {$ConfigData, $ConfigData$Type} from "packages/me/shedaniel/autoconfig/$ConfigData"

export class $DummyConfigSerializer<T extends $ConfigData> implements $ConfigSerializer<(T)> {

constructor(definition: $Config$Type, configClass: $Class$Type<(T)>)

public "deserialize"(): T
public "createDefault"(): T
public "serialize"(config: T): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DummyConfigSerializer$Type<T> = ($DummyConfigSerializer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DummyConfigSerializer_<T> = $DummyConfigSerializer$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig/$ClothConfigForge" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ClothConfigForge {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClothConfigForge$Type = ($ClothConfigForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClothConfigForge_ = $ClothConfigForge$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg7$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg7$Op<A1, A2, A3, A4, A5, A6, A7, T> {

 "construct"(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7): T

(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7): T
}

export namespace $RecordValueAnimatorArgs$Arg7$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg7$Op$Type<A1, A2, A3, A4, A5, A6, A7, T> = ($RecordValueAnimatorArgs$Arg7$Op<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg7$Op_<A1, A2, A3, A4, A5, A6, A7, T> = $RecordValueAnimatorArgs$Arg7$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$YAMLException" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$RuntimeException, $RuntimeException$Type} from "packages/java/lang/$RuntimeException"

export class $YAMLException extends $RuntimeException {

constructor(message: string)
constructor(cause: $Throwable$Type)
constructor(message: string, cause: $Throwable$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $YAMLException$Type = ($YAMLException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $YAMLException_ = $YAMLException$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$SequenceEndEvent" {
import {$Event$ID, $Event$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$CollectionEndEvent, $CollectionEndEvent$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$CollectionEndEvent"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $SequenceEndEvent extends $CollectionEndEvent {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getEventId"(): $Event$ID
get "eventId"(): $Event$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SequenceEndEvent$Type = ($SequenceEndEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SequenceEndEvent_ = $SequenceEndEvent$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$PrimitiveArrayValueWriter" {
import {$ArrayValueWriter, $ArrayValueWriter$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ArrayValueWriter"
import {$WriterContext, $WriterContext$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$WriterContext"

export class $PrimitiveArrayValueWriter extends $ArrayValueWriter {


public "toString"(): string
public "write"(o: any, context: $WriterContext$Type): void
public "canWrite"(value: any): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrimitiveArrayValueWriter$Type = ($PrimitiveArrayValueWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrimitiveArrayValueWriter_ = $PrimitiveArrayValueWriter$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/serializer/$CommentSerializer" {
import {$JsonGrammar, $JsonGrammar$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonGrammar"
import {$StringBuilder, $StringBuilder$Type} from "packages/java/lang/$StringBuilder"

export class $CommentSerializer {

constructor()

public static "print"(builder: $StringBuilder$Type, comment: string, indent: integer, grammar: $JsonGrammar$Type): void
public static "print"(builder: $StringBuilder$Type, comment: string, indent: integer, comments: boolean, whitespace: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommentSerializer$Type = ($CommentSerializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommentSerializer_ = $CommentSerializer$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/composer/$Composer" {
import {$Node, $Node$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Node"
import {$Parser, $Parser$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/parser/$Parser"
import {$LoaderOptions, $LoaderOptions$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$LoaderOptions"
import {$Resolver, $Resolver$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/resolver/$Resolver"

export class $Composer {

constructor(parser: $Parser$Type, resolver: $Resolver$Type)
constructor(parser: $Parser$Type, resolver: $Resolver$Type, loadingConfig: $LoaderOptions$Type)

public "getNode"(): $Node
public "getSingleNode"(): $Node
public "checkNode"(): boolean
get "node"(): $Node
get "singleNode"(): $Node
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Composer$Type = ($Composer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Composer_ = $Composer$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$ConfigCategory" {
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $ConfigCategory {

 "addEntry"(arg0: $AbstractConfigListEntry$Type<(any)>): $ConfigCategory
/**
 * 
 * @deprecated
 */
 "getEntries"(): $List<(any)>
 "getDescription"(): $Supplier<($Optional<(($FormattedText)[])>)>
 "setDescription"(description: ($FormattedText$Type)[]): void
 "setDescription"(arg0: $Supplier$Type<($Optional$Type<(($FormattedText$Type)[])>)>): void
 "removeCategory"(): void
 "getBackground"(): $ResourceLocation
 "setBackground"(arg0: $ResourceLocation$Type): void
 "setCategoryBackground"(arg0: $ResourceLocation$Type): $ConfigCategory
 "getCategoryKey"(): $Component
}

export namespace $ConfigCategory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigCategory$Type = ($ConfigCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigCategory_ = $ConfigCategory$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/annotation/$SerializedName" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $SerializedName extends $Annotation {

 "value"(): string
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $SerializedName {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SerializedName$Type = ($SerializedName);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SerializedName_ = $SerializedName$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$MissingEnvironmentVariableException" {
import {$YAMLException, $YAMLException$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$YAMLException"

export class $MissingEnvironmentVariableException extends $YAMLException {

constructor(message: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MissingEnvironmentVariableException$Type = ($MissingEnvironmentVariableException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MissingEnvironmentVariableException_ = $MissingEnvironmentVariableException$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$LazyResettable" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $LazyResettable<T> implements $Supplier<(T)> {

constructor(supplier: $Supplier$Type<(T)>)

public "get"(): T
public "equals"(o: any): boolean
public "hashCode"(): integer
public "reset"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LazyResettable$Type<T> = ($LazyResettable<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LazyResettable_<T> = $LazyResettable$Type<(T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$StreamEndToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $StreamEndToken extends $Token {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StreamEndToken$Type = ($StreamEndToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StreamEndToken_ = $StreamEndToken$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/serializer/$Serializer" {
import {$DumperOptions, $DumperOptions$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions"
import {$Emitable, $Emitable$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/emitter/$Emitable"
import {$Node, $Node$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Node"
import {$Tag, $Tag$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Tag"
import {$Resolver, $Resolver$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/resolver/$Resolver"

export class $Serializer {

constructor(emitter: $Emitable$Type, resolver: $Resolver$Type, opts: $DumperOptions$Type, rootTag: $Tag$Type)

public "close"(): void
public "open"(): void
public "serialize"(node: $Node$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Serializer$Type = ($Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Serializer_ = $Serializer$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$IntegerSliderEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$TooltipListEntry, $TooltipListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$TooltipListEntry"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $IntegerSliderEntry extends $TooltipListEntry<(integer)> {

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, minimum: integer, maximum: integer, value: integer, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(integer)>, saveConsumer: $Consumer$Type<(integer)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, requiresRestart: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, minimum: integer, maximum: integer, value: integer, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(integer)>, saveConsumer: $Consumer$Type<(integer)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, minimum: integer, maximum: integer, value: integer, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(integer)>, saveConsumer: $Consumer$Type<(integer)>)

public "getValue"(): integer
/**
 * 
 * @deprecated
 */
public "setValue"(value: integer): void
public "getDefaultValue"(): $Optional<(integer)>
public "render"(graphics: $GuiGraphics$Type, index: integer, y: integer, x: integer, entryWidth: integer, entryHeight: integer, mouseX: integer, mouseY: integer, isHovered: boolean, delta: float): void
public "children"(): $List<(any)>
public "narratables"(): $List<(any)>
public "isEdited"(): boolean
public "setMaximum"(maximum: integer): $IntegerSliderEntry
public "getTextGetter"(): $Function<(integer), ($Component)>
public "setMinimum"(minimum: integer): $IntegerSliderEntry
public "setTextGetter"(textGetter: $Function$Type<(integer), ($Component$Type)>): $IntegerSliderEntry
get "value"(): integer
set "value"(value: integer)
get "defaultValue"(): $Optional<(integer)>
get "edited"(): boolean
set "maximum"(value: integer)
get "textGetter"(): $Function<(integer), ($Component)>
set "minimum"(value: integer)
set "textGetter"(value: $Function$Type<(integer), ($Component$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntegerSliderEntry$Type = ($IntegerSliderEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntegerSliderEntry_ = $IntegerSliderEntry$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$CollectionStartEvent" {
import {$NodeEvent, $NodeEvent$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$NodeEvent"
import {$DumperOptions$FlowStyle, $DumperOptions$FlowStyle$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $CollectionStartEvent extends $NodeEvent {

constructor(anchor: string, tag: string, implicit: boolean, startMark: $Mark$Type, endMark: $Mark$Type, flowStyle: $DumperOptions$FlowStyle$Type)
/**
 * 
 * @deprecated
 */
constructor(anchor: string, tag: string, implicit: boolean, startMark: $Mark$Type, endMark: $Mark$Type, flowStyle: boolean)

public "getTag"(): string
public "getFlowStyle"(): $DumperOptions$FlowStyle
public "getImplicit"(): boolean
public "isFlow"(): boolean
get "tag"(): string
get "flowStyle"(): $DumperOptions$FlowStyle
get "implicit"(): boolean
get "flow"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CollectionStartEvent$Type = ($CollectionStartEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CollectionStartEvent_ = $CollectionStartEvent$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg5$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg5$Up<A1, A2, A3, A4, A5, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>): void
}

export namespace $RecordValueAnimatorArgs$Arg5$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg5$Up$Type<A1, A2, A3, A4, A5, T> = ($RecordValueAnimatorArgs$Arg5$Up<(A1), (A2), (A3), (A4), (A5), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg5$Up_<A1, A2, A3, A4, A5, T> = $RecordValueAnimatorArgs$Arg5$Up$Type<(A1), (A2), (A3), (A4), (A5), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$NestedListListEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$NestedListListEntry$NestedListCell, $NestedListListEntry$NestedListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$NestedListListEntry$NestedListCell"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AbstractListListEntry, $AbstractListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractListListEntry"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"

export class $NestedListListEntry<T, INNER extends $AbstractConfigListEntry<(T)>> extends $AbstractListListEntry<(T), ($NestedListListEntry$NestedListCell<(T), (INNER)>), ($NestedListListEntry<(T), (INNER)>)> {

constructor(fieldName: $Component$Type, value: $List$Type<(T)>, defaultExpanded: boolean, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, saveConsumer: $Consumer$Type<($List$Type<(T)>)>, defaultValue: $Supplier$Type<($List$Type<(T)>)>, resetButtonKey: $Component$Type, deleteButtonEnabled: boolean, insertInFront: boolean, createNewCell: $BiFunction$Type<(T), ($NestedListListEntry$Type<(T), (INNER)>), (INNER)>)

public "getSearchTags"(): $Iterator<(string)>
get "searchTags"(): $Iterator<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NestedListListEntry$Type<T, INNER> = ($NestedListListEntry<(T), (INNER)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NestedListListEntry_<T, INNER> = $NestedListListEntry$Type<(T), (INNER)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg5$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg5$Op<A1, A2, A3, A4, A5, T> {

 "construct"(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5): T

(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5): T
}

export namespace $RecordValueAnimatorArgs$Arg5$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg5$Op$Type<A1, A2, A3, A4, A5, T> = ($RecordValueAnimatorArgs$Arg5$Op<(A1), (A2), (A3), (A4), (A5), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg5$Op_<A1, A2, A3, A4, A5, T> = $RecordValueAnimatorArgs$Arg5$Op$Type<(A1), (A2), (A3), (A4), (A5), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$StringParserContext" {
import {$ParserContext, $ParserContext$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$ParserContext"
import {$Jankson, $Jankson$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$Jankson"
import {$JsonPrimitive, $JsonPrimitive$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonPrimitive"

export class $StringParserContext implements $ParserContext<($JsonPrimitive)> {

constructor(quote: integer)

public "consume"(codePoint: integer, loader: $Jankson$Type): boolean
public "eof"(): void
public "isComplete"(): boolean
get "complete"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringParserContext$Type = ($StringParserContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringParserContext_ = $StringParserContext$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$AliasEvent" {
import {$Event$ID, $Event$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$NodeEvent, $NodeEvent$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$NodeEvent"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $AliasEvent extends $NodeEvent {

constructor(anchor: string, startMark: $Mark$Type, endMark: $Mark$Type)

public "getEventId"(): $Event$ID
get "eventId"(): $Event$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AliasEvent$Type = ($AliasEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AliasEvent_ = $AliasEvent$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$MappingStartEvent" {
import {$Event$ID, $Event$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$CollectionStartEvent, $CollectionStartEvent$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$CollectionStartEvent"
import {$DumperOptions$FlowStyle, $DumperOptions$FlowStyle$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $MappingStartEvent extends $CollectionStartEvent {

constructor(anchor: string, tag: string, implicit: boolean, startMark: $Mark$Type, endMark: $Mark$Type, flowStyle: $DumperOptions$FlowStyle$Type)
/**
 * 
 * @deprecated
 */
constructor(anchor: string, tag: string, implicit: boolean, startMark: $Mark$Type, endMark: $Mark$Type, flowStyle: boolean)

public "getEventId"(): $Event$ID
get "eventId"(): $Event$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MappingStartEvent$Type = ($MappingStartEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MappingStartEvent_ = $MappingStartEvent$Type;
}}
declare module "packages/me/shedaniel/autoconfig/serializer/$Toml4jConfigSerializer" {
import {$Config, $Config$Type} from "packages/me/shedaniel/autoconfig/annotation/$Config"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ConfigSerializer, $ConfigSerializer$Type} from "packages/me/shedaniel/autoconfig/serializer/$ConfigSerializer"
import {$ConfigData, $ConfigData$Type} from "packages/me/shedaniel/autoconfig/$ConfigData"
import {$TomlWriter, $TomlWriter$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$TomlWriter"

export class $Toml4jConfigSerializer<T extends $ConfigData> implements $ConfigSerializer<(T)> {

constructor(definition: $Config$Type, configClass: $Class$Type<(T)>, tomlWriter: $TomlWriter$Type)
constructor(definition: $Config$Type, configClass: $Class$Type<(T)>)

public "deserialize"(): T
public "createDefault"(): T
public "serialize"(config: T): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Toml4jConfigSerializer$Type<T> = ($Toml4jConfigSerializer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Toml4jConfigSerializer_<T> = $Toml4jConfigSerializer$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$Modifier" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Modifier {


public "matchesCurrent"(): boolean
public "equals"(other: any): boolean
public "hashCode"(): integer
public "getValue"(): short
public "isEmpty"(): boolean
public static "of"(alt: boolean, control: boolean, shift: boolean): $Modifier
public static "of"(value: short): $Modifier
public static "current"(): $Modifier
public static "none"(): $Modifier
public "hasControl"(): boolean
public "hasShift"(): boolean
public "hasAlt"(): boolean
get "value"(): short
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Modifier$Type = ($Modifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Modifier_ = $Modifier$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/extensions/compactnotation/$PackageCompactConstructor" {
import {$SafeConstructor$ConstructUndefined, $SafeConstructor$ConstructUndefined$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$SafeConstructor$ConstructUndefined"
import {$CompactConstructor, $CompactConstructor$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/extensions/compactnotation/$CompactConstructor"

export class $PackageCompactConstructor extends $CompactConstructor {
static readonly "undefinedConstructor": $SafeConstructor$ConstructUndefined

constructor(packageName: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PackageCompactConstructor$Type = ($PackageCompactConstructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PackageCompactConstructor_ = $PackageCompactConstructor$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$KeyToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $KeyToken extends $Token {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyToken$Type = ($KeyToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyToken_ = $KeyToken$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/parser/$Parser" {
import {$Event$ID, $Event$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$Event, $Event$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event"

export interface $Parser {

 "checkEvent"(arg0: $Event$ID$Type): boolean
 "peekEvent"(): $Event
 "getEvent"(): $Event
}

export namespace $Parser {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Parser$Type = ($Parser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Parser_ = $Parser$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$BlockEndToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $BlockEndToken extends $Token {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEndToken$Type = ($BlockEndToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEndToken_ = $BlockEndToken$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$Expandable" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Expandable {

 "setExpanded"(arg0: boolean): void
 "isExpanded"(): boolean
}

export namespace $Expandable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Expandable$Type = ($Expandable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Expandable_ = $Expandable$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$DateValueReaderWriter" {
import {$ValueReader, $ValueReader$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueReader"
import {$WriterContext, $WriterContext$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$WriterContext"
import {$Context, $Context$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$Context"
import {$AtomicInteger, $AtomicInteger$Type} from "packages/java/util/concurrent/atomic/$AtomicInteger"
import {$ValueWriter, $ValueWriter$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueWriter"

export class $DateValueReaderWriter implements $ValueReader, $ValueWriter {


public "toString"(): string
public "write"(value: any, context: $WriterContext$Type): void
public "read"(original: string, index: $AtomicInteger$Type, context: $Context$Type): any
public "canRead"(s: string): boolean
public "canWrite"(value: any): boolean
public "isPrimitiveType"(): boolean
get "primitiveType"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DateValueReaderWriter$Type = ($DateValueReaderWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DateValueReaderWriter_ = $DateValueReaderWriter$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg10$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg10$Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>): void
}

export namespace $RecordValueAnimatorArgs$Arg10$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg10$Up$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, T> = ($RecordValueAnimatorArgs$Arg10$Up<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg10$Up_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, T> = $RecordValueAnimatorArgs$Arg10$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$NumberAnimatorWrapped" {
import {$FloatingPoint, $FloatingPoint$Type} from "packages/me/shedaniel/math/$FloatingPoint"
import {$Color, $Color$Type} from "packages/me/shedaniel/math/$Color"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$NumberAnimator, $NumberAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$NumberAnimator"
import {$Dimension, $Dimension$Type} from "packages/me/shedaniel/math/$Dimension"
import {$ValueProvider, $ValueProvider$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueProvider"
import {$ProgressValueAnimator, $ProgressValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ProgressValueAnimator"
import {$FloatingDimension, $FloatingDimension$Type} from "packages/me/shedaniel/math/$FloatingDimension"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"
import {$FloatingRectangle, $FloatingRectangle$Type} from "packages/me/shedaniel/math/$FloatingRectangle"
import {$Point, $Point$Type} from "packages/me/shedaniel/math/$Point"

export class $NumberAnimatorWrapped<T extends number, R extends number> extends $NumberAnimator<(T)> {


public "target"(): T
public "update"(delta: double): void
public "intValue"(): integer
public "longValue"(): long
public "floatValue"(): float
public "doubleValue"(): double
public "setToNumber"(value: number, duration: long): $NumberAnimator<(T)>
public "setTargetNumber"(value: number): $NumberAnimator<(T)>
public static "ofLong"(): $NumberAnimator<(long)>
public static "ofLong"(initialValue: long): $NumberAnimator<(long)>
public static "ofFloatingPoint"(): $ValueAnimator<($FloatingPoint)>
public static "ofFloatingPoint"(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
/**
 * 
 * @deprecated
 */
public static "ofDimension"(initialValue: $Point$Type): $ValueAnimator<($Point)>
public static "ofDimension"(initialValue: $Dimension$Type): $ValueAnimator<($Dimension)>
public static "ofDimension"(): $ValueAnimator<($Dimension)>
public static "ofRectangle"(initialValue: $Rectangle$Type): $ValueAnimator<($Rectangle)>
public static "ofRectangle"(): $ValueAnimator<($Rectangle)>
public static "ofPoint"(initialValue: $Point$Type): $ValueAnimator<($Point)>
public static "ofPoint"(): $ValueAnimator<($Point)>
public static "ofInt"(): $NumberAnimator<(integer)>
public static "ofInt"(initialValue: integer): $NumberAnimator<(integer)>
public static "ofDouble"(): $NumberAnimator<(double)>
public static "ofDouble"(initialValue: double): $NumberAnimator<(double)>
public static "ofBoolean"(): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(switchPoint: double, initialValue: boolean): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(switchPoint: double): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(initialValue: boolean): $ProgressValueAnimator<(boolean)>
public static "ofFloat"(initialValue: float): $NumberAnimator<(float)>
public static "ofFloat"(): $NumberAnimator<(float)>
public static "ofColor"(): $ValueAnimator<($Color)>
public static "ofColor"(initialValue: $Color$Type): $ValueAnimator<($Color)>
public static "ofFloatingRectangle"(initialValue: $FloatingRectangle$Type): $ValueAnimator<($FloatingRectangle)>
public static "ofFloatingRectangle"(): $ValueAnimator<($FloatingRectangle)>
/**
 * 
 * @deprecated
 */
public static "ofFloatingDimension"(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
public static "ofFloatingDimension"(): $ValueAnimator<($FloatingDimension)>
public static "ofFloatingDimension"(initialValue: $FloatingDimension$Type): $ValueAnimator<($FloatingDimension)>
public static "typicalTransitionTime"(): long
public static "constant"<T>(value: T): $ValueProvider<(T)>
set "targetNumber"(value: number)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NumberAnimatorWrapped$Type<T, R> = ($NumberAnimatorWrapped<(T), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NumberAnimatorWrapped_<T, R> = $NumberAnimatorWrapped$Type<(T), (R)>;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$TextFieldListEntry" {
import {$TooltipListEntry, $TooltipListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$TooltipListEntry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $TextFieldListEntry<T> extends $TooltipListEntry<(T)> {


/**
 * 
 * @deprecated
 */
public "setValue"(s: string): void
public "getDefaultValue"(): $Optional<(T)>
public "render"(graphics: $GuiGraphics$Type, index: integer, y: integer, x: integer, entryWidth: integer, entryHeight: integer, mouseX: integer, mouseY: integer, isHovered: boolean, delta: float): void
public "children"(): $List<(any)>
public "narratables"(): $List<(any)>
public "isEdited"(): boolean
public "updateSelected"(isSelected: boolean): void
set "value"(value: string)
get "defaultValue"(): $Optional<(T)>
get "edited"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextFieldListEntry$Type<T> = ($TextFieldListEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextFieldListEntry_<T> = $TextFieldListEntry$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$FloatFieldBuilder" {
import {$AbstractRangeFieldBuilder, $AbstractRangeFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractRangeFieldBuilder"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$FloatListEntry, $FloatListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$FloatListEntry"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export class $FloatFieldBuilder extends $AbstractRangeFieldBuilder<(float), ($FloatListEntry), ($FloatFieldBuilder)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, value: float)

public "setMin"(min: float): $FloatFieldBuilder
public "setSaveConsumer"(saveConsumer: $Consumer$Type<(float)>): $FloatFieldBuilder
public "requireRestart"(): $FloatFieldBuilder
public "setTooltip"(tooltip: $Optional$Type<(($Component$Type)[])>): $FloatFieldBuilder
public "setErrorSupplier"(errorSupplier: $Function$Type<(float), ($Optional$Type<($Component$Type)>)>): $FloatFieldBuilder
public "setDefaultValue"(defaultValue: float): $FloatFieldBuilder
public "setMax"(max: float): $FloatFieldBuilder
public "removeMax"(): $FloatFieldBuilder
set "min"(value: float)
set "saveConsumer"(value: $Consumer$Type<(float)>)
set "tooltip"(value: $Optional$Type<(($Component$Type)[])>)
set "errorSupplier"(value: $Function$Type<(float), ($Optional$Type<($Component$Type)>)>)
set "defaultValue"(value: float)
set "max"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FloatFieldBuilder$Type = ($FloatFieldBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FloatFieldBuilder_ = $FloatFieldBuilder$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg10$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg10$Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, T> {

 "construct"(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10): T

(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10): T
}

export namespace $RecordValueAnimatorArgs$Arg10$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg10$Op$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, T> = ($RecordValueAnimatorArgs$Arg10$Op<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg10$Op_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, T> = $RecordValueAnimatorArgs$Arg10$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/$ClothConfigScreen$ListWidget" {
import {$AbstractConfigScreen, $AbstractConfigScreen$Type} from "packages/me/shedaniel/clothconfig2/gui/$AbstractConfigScreen"
import {$DynamicElementListWidget, $DynamicElementListWidget$Type} from "packages/me/shedaniel/clothconfig2/gui/widget/$DynamicElementListWidget"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$DynamicElementListWidget$ElementEntry, $DynamicElementListWidget$ElementEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/widget/$DynamicElementListWidget$ElementEntry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"

export class $ClothConfigScreen$ListWidget<R extends $DynamicElementListWidget$ElementEntry<(R)>> extends $DynamicElementListWidget<(R)> {
 "entriesTransformer": $UnaryOperator<($List<(R)>)>
 "thisTimeTarget": $Rectangle
 "lastTouch": long
 "width": integer
 "height": integer
 "top": integer
 "bottom": integer
 "right": integer
 "left": integer

constructor(screen: $AbstractConfigScreen$Type, client: $Minecraft$Type, width: integer, height: integer, top: integer, bottom: integer, backgroundLocation: $ResourceLocation$Type)

public "children"(): $List<(R)>
public "mouseClicked"(mouseX: double, mouseY: double, button: integer): boolean
public "getItemWidth"(): integer
get "itemWidth"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClothConfigScreen$ListWidget$Type<R> = ($ClothConfigScreen$ListWidget<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClothConfigScreen$ListWidget_<R> = $ClothConfigScreen$ListWidget$Type<(R)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$POJODeserializer" {
import {$Marshaller, $Marshaller$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/api/$Marshaller"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$JsonElement, $JsonElement$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonElement"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$JsonObject, $JsonObject$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonObject"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $POJODeserializer {

constructor()

public static "unpackFieldData"(parent: any, field: $Field$Type, elem: $JsonElement$Type, marshaller: $Marshaller$Type): boolean
public static "unpackField"(parent: any, f: $Field$Type, source: $JsonObject$Type, failFast: boolean): void
public static "unpackMap"(map: $Map$Type<(any), (any)>, keyType: $Type$Type, valueType: $Type$Type, elem: $JsonElement$Type, marshaller: $Marshaller$Type): void
public static "unpackCollection"(collection: $Collection$Type<(any)>, elementType: $Type$Type, elem: $JsonElement$Type, marshaller: $Marshaller$Type): void
public static "unpack"(t: $Type$Type, elem: $JsonElement$Type, marshaller: $Marshaller$Type): any
public static "unpackObject"(target: any, source: $JsonObject$Type): void
public static "unpackObject"(target: any, source: $JsonObject$Type, failFast: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $POJODeserializer$Type = ($POJODeserializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $POJODeserializer_ = $POJODeserializer$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$ReferenceBuildingConfigScreen" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ConfigScreen, $ConfigScreen$Type} from "packages/me/shedaniel/clothconfig2/api/$ConfigScreen"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$Tooltip, $Tooltip$Type} from "packages/me/shedaniel/clothconfig2/api/$Tooltip"

export interface $ReferenceBuildingConfigScreen extends $ConfigScreen {

 "requestReferenceRebuilding"(): void
 "setSavingRunnable"(arg0: $Runnable$Type): void
 "saveAll"(arg0: boolean): void
 "isRequiresRestart"(): boolean
 "isEdited"(): boolean
 "matchesSearch"(arg0: $Iterator$Type<(string)>): boolean
 "getBackgroundLocation"(): $ResourceLocation
 "addTooltip"(arg0: $Tooltip$Type): void
 "setAfterInitConsumer"(arg0: $Consumer$Type<($Screen$Type)>): void
}

export namespace $ReferenceBuildingConfigScreen {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReferenceBuildingConfigScreen$Type = ($ReferenceBuildingConfigScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReferenceBuildingConfigScreen_ = $ReferenceBuildingConfigScreen$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$HideableWidget" {
import {$Requirement, $Requirement$Type} from "packages/me/shedaniel/clothconfig2/api/$Requirement"

export interface $HideableWidget {

 "setDisplayRequirement"(arg0: $Requirement$Type): void
 "getDisplayRequirement"(): $Requirement
 "isDisplayed"(): boolean
}

export namespace $HideableWidget {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HideableWidget$Type = ($HideableWidget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HideableWidget_ = $HideableWidget$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/$GlobalizedClothConfigScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$AbstractConfigScreen, $AbstractConfigScreen$Type} from "packages/me/shedaniel/clothconfig2/gui/$AbstractConfigScreen"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$ConfigCategory, $ConfigCategory$Type} from "packages/me/shedaniel/clothconfig2/api/$ConfigCategory"
import {$ClothConfigScreen$ListWidget, $ClothConfigScreen$ListWidget$Type} from "packages/me/shedaniel/clothconfig2/gui/$ClothConfigScreen$ListWidget"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Expandable, $Expandable$Type} from "packages/me/shedaniel/clothconfig2/api/$Expandable"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$ReferenceBuildingConfigScreen, $ReferenceBuildingConfigScreen$Type} from "packages/me/shedaniel/clothconfig2/api/$ReferenceBuildingConfigScreen"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$AbstractConfigEntry, $AbstractConfigEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigEntry"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $GlobalizedClothConfigScreen extends $AbstractConfigScreen implements $ReferenceBuildingConfigScreen, $Expandable {
 "listWidget": $ClothConfigScreen$ListWidget<($AbstractConfigEntry<($AbstractConfigEntry<(any)>)>)>
 "selectedCategoryIndex": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(parent: $Screen$Type, title: $Component$Type, categoryMap: $Map$Type<(string), ($ConfigCategory$Type)>, backgroundLocation: $ResourceLocation$Type)

public "setExpanded"(expanded: boolean): void
public "render"(graphics: $GuiGraphics$Type, mouseX: integer, mouseY: integer, delta: float): void
public "matchesSearch"(tags: $Iterator$Type<(string)>): boolean
public "mouseClicked"(mouseX: double, mouseY: double, button: integer): boolean
public "mouseScrolled"(mouseX: double, mouseY: double, amount: double): boolean
public "isExpanded"(): boolean
public "requestReferenceRebuilding"(): void
public "getCategorizedEntries"(): $Map<($Component), ($List<($AbstractConfigEntry<(any)>)>)>
set "expanded"(value: boolean)
get "expanded"(): boolean
get "categorizedEntries"(): $Map<($Component), ($List<($AbstractConfigEntry<(any)>)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlobalizedClothConfigScreen$Type = ($GlobalizedClothConfigScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlobalizedClothConfigScreen_ = $GlobalizedClothConfigScreen$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$LineBreak" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DumperOptions$LineBreak extends $Enum<($DumperOptions$LineBreak)> {
static readonly "WIN": $DumperOptions$LineBreak
static readonly "MAC": $DumperOptions$LineBreak
static readonly "UNIX": $DumperOptions$LineBreak


public "toString"(): string
public static "values"(): ($DumperOptions$LineBreak)[]
public static "valueOf"(name: string): $DumperOptions$LineBreak
public "getString"(): string
public static "getPlatformLineBreak"(): $DumperOptions$LineBreak
get "string"(): string
get "platformLineBreak"(): $DumperOptions$LineBreak
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DumperOptions$LineBreak$Type = (("win") | ("mac") | ("unix")) | ($DumperOptions$LineBreak);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DumperOptions$LineBreak_ = $DumperOptions$LineBreak$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$ParserContext" {
import {$Jankson, $Jankson$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$Jankson"

export interface $ParserContext<T> {

 "consume"(arg0: integer, arg1: $Jankson$Type): boolean
 "eof"(): void
 "getResult"(): T
 "isComplete"(): boolean
}

export namespace $ParserContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParserContext$Type<T> = ($ParserContext<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParserContext_<T> = $ParserContext$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg20$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg20$Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, T> {

 "construct"(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11, arg11: A12, arg12: A13, arg13: A14, arg14: A15, arg15: A16, arg16: A17, arg17: A18, arg18: A19, arg19: A20): T

(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11, arg11: A12, arg12: A13, arg13: A14, arg14: A15, arg15: A16, arg16: A17, arg17: A18, arg18: A19, arg19: A20): T
}

export namespace $RecordValueAnimatorArgs$Arg20$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg20$Op$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, T> = ($RecordValueAnimatorArgs$Arg20$Op<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (A20), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg20$Op_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, T> = $RecordValueAnimatorArgs$Arg20$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (A20), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg19$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg19$Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, T> {

 "construct"(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11, arg11: A12, arg12: A13, arg13: A14, arg14: A15, arg15: A16, arg16: A17, arg17: A18, arg18: A19): T

(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11, arg11: A12, arg12: A13, arg13: A14, arg14: A15, arg15: A16, arg16: A17, arg17: A18, arg18: A19): T
}

export namespace $RecordValueAnimatorArgs$Arg19$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg19$Op$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, T> = ($RecordValueAnimatorArgs$Arg19$Op<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg19$Op_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, T> = $RecordValueAnimatorArgs$Arg19$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$LoaderOptions" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $LoaderOptions {

constructor()

public "isAllowDuplicateKeys"(): boolean
public "isWrappedToRootException"(): boolean
public "setWrappedToRootException"(wrappedToRootException: boolean): void
public "setAllowDuplicateKeys"(allowDuplicateKeys: boolean): void
public "getAllowRecursiveKeys"(): boolean
public "setMaxAliasesForCollections"(maxAliasesForCollections: integer): void
public "setAllowRecursiveKeys"(allowRecursiveKeys: boolean): void
public "getMaxAliasesForCollections"(): integer
get "allowDuplicateKeys"(): boolean
get "wrappedToRootException"(): boolean
set "wrappedToRootException"(value: boolean)
set "allowDuplicateKeys"(value: boolean)
get "allowRecursiveKeys"(): boolean
set "maxAliasesForCollections"(value: integer)
set "allowRecursiveKeys"(value: boolean)
get "maxAliasesForCollections"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LoaderOptions$Type = ($LoaderOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LoaderOptions_ = $LoaderOptions$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonPrimitive" {
import {$JsonGrammar, $JsonGrammar$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonGrammar"
import {$JsonElement, $JsonElement$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonElement"

export class $JsonPrimitive extends $JsonElement {
static "TRUE": $JsonPrimitive
static "FALSE": $JsonPrimitive

constructor(value: any)

public "equals"(other: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): $JsonPrimitive
public "getValue"(): any
public static "escape"(s: string): string
public "asInt"(defaultValue: integer): integer
public "asDouble"(defaultValue: double): double
public "toJson"(grammar: $JsonGrammar$Type, depth: integer): string
public "toJson"(comments: boolean, newlines: boolean, depth: integer): string
public "asString"(): string
public "asChar"(defaultValue: character): character
public "asFloat"(defaultValue: float): float
public "asShort"(defaultValue: short): short
public "asBoolean"(defaultValue: boolean): boolean
public "asLong"(defaultValue: long): long
public "asByte"(defaultValue: byte): byte
get "value"(): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonPrimitive$Type = ($JsonPrimitive);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonPrimitive_ = $JsonPrimitive$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/widget/$DynamicEntryListWidget" {
import {$DynamicEntryListWidget$Entry, $DynamicEntryListWidget$Entry$Type} from "packages/me/shedaniel/clothconfig2/gui/widget/$DynamicEntryListWidget$Entry"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$NarratableEntry$NarrationPriority, $NarratableEntry$NarrationPriority$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry$NarrationPriority"
import {$ScreenRectangle, $ScreenRectangle$Type} from "packages/net/minecraft/client/gui/navigation/$ScreenRectangle"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$NarrationElementOutput, $NarrationElementOutput$Type} from "packages/net/minecraft/client/gui/narration/$NarrationElementOutput"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractContainerEventHandler, $AbstractContainerEventHandler$Type} from "packages/net/minecraft/client/gui/components/events/$AbstractContainerEventHandler"

export class $DynamicEntryListWidget<E extends $DynamicEntryListWidget$Entry<(E)>> extends $AbstractContainerEventHandler implements $Renderable, $NarratableEntry {
 "width": integer
 "height": integer
 "top": integer
 "bottom": integer
 "right": integer
 "left": integer

constructor(client: $Minecraft$Type, width: integer, height: integer, top: integer, bottom: integer, backgroundLocation: $ResourceLocation$Type)

public "getSelectedItem"(): E
public "ensureVisible"(rowTop: integer, itemHeight: integer): void
public "updateSize"(width: integer, height: integer, top: integer, bottom: integer): void
public "keyPressed"(int_1: integer, int_2: integer, int_3: integer): boolean
public "children"(): $List<(E)>
public "isMouseOver"(double_1: double, double_2: double): boolean
public "updateNarration"(narrationElementOutput: $NarrationElementOutput$Type): void
public "narrationPriority"(): $NarratableEntry$NarrationPriority
public "getRectangle"(): $ScreenRectangle
public "setFocused"(guiEventListener: $GuiEventListener$Type): void
public "visibleChildren"(): $List<(E)>
public "getScroll"(): double
public "render"(graphics: $GuiGraphics$Type, mouseX: integer, mouseY: integer, delta: float): void
public "mouseReleased"(double_1: double, double_2: double, int_1: integer): boolean
public "mouseClicked"(double_1: double, double_2: double, int_1: integer): boolean
public "mouseScrolled"(double_1: double, double_2: double, double_3: double): boolean
public "mouseDragged"(mouseX: double, mouseY: double, button: integer, deltaX: double, deltaY: double): boolean
public "isFocused"(): boolean
public "getFocused"(): E
public "tickList"(): void
public "setRenderSelection"(boolean_1: boolean): void
public "getItemWidth"(): integer
public "selectItem"(item: E): void
public "getRowTop"(index: integer): integer
public "capYPosition"(double_1: double): void
public "getScrollBottom"(): integer
public "setLeftPos"(left: integer): void
public "isActive"(): boolean
get "selectedItem"(): E
get "rectangle"(): $ScreenRectangle
set "focused"(value: $GuiEventListener$Type)
get "scroll"(): double
get "focused"(): boolean
get "focused"(): E
set "renderSelection"(value: boolean)
get "itemWidth"(): integer
get "scrollBottom"(): integer
set "leftPos"(value: integer)
get "active"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicEntryListWidget$Type<E> = ($DynamicEntryListWidget<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicEntryListWidget_<E> = $DynamicEntryListWidget$Type<(E)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$AnchorNode" {
import {$Node, $Node$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Node"
import {$NodeId, $NodeId$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$NodeId"

export class $AnchorNode extends $Node {

constructor(realNode: $Node$Type)

public "getNodeId"(): $NodeId
public "getRealNode"(): $Node
get "nodeId"(): $NodeId
get "realNode"(): $Node
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnchorNode$Type = ($AnchorNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnchorNode_ = $AnchorNode$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg19$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg19$Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>, arg12: $RecordValueAnimatorArgs$Setter$Type<(A12)>, arg13: $RecordValueAnimatorArgs$Setter$Type<(A13)>, arg14: $RecordValueAnimatorArgs$Setter$Type<(A14)>, arg15: $RecordValueAnimatorArgs$Setter$Type<(A15)>, arg16: $RecordValueAnimatorArgs$Setter$Type<(A16)>, arg17: $RecordValueAnimatorArgs$Setter$Type<(A17)>, arg18: $RecordValueAnimatorArgs$Setter$Type<(A18)>, arg19: $RecordValueAnimatorArgs$Setter$Type<(A19)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>, arg12: $RecordValueAnimatorArgs$Setter$Type<(A12)>, arg13: $RecordValueAnimatorArgs$Setter$Type<(A13)>, arg14: $RecordValueAnimatorArgs$Setter$Type<(A14)>, arg15: $RecordValueAnimatorArgs$Setter$Type<(A15)>, arg16: $RecordValueAnimatorArgs$Setter$Type<(A16)>, arg17: $RecordValueAnimatorArgs$Setter$Type<(A17)>, arg18: $RecordValueAnimatorArgs$Setter$Type<(A18)>, arg19: $RecordValueAnimatorArgs$Setter$Type<(A19)>): void
}

export namespace $RecordValueAnimatorArgs$Arg19$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg19$Up$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, T> = ($RecordValueAnimatorArgs$Arg19$Up<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg19$Up_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, T> = $RecordValueAnimatorArgs$Arg19$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$Container" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Container {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Container$Type = ($Container);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Container_ = $Container$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg20$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg20$Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>, arg12: $RecordValueAnimatorArgs$Setter$Type<(A12)>, arg13: $RecordValueAnimatorArgs$Setter$Type<(A13)>, arg14: $RecordValueAnimatorArgs$Setter$Type<(A14)>, arg15: $RecordValueAnimatorArgs$Setter$Type<(A15)>, arg16: $RecordValueAnimatorArgs$Setter$Type<(A16)>, arg17: $RecordValueAnimatorArgs$Setter$Type<(A17)>, arg18: $RecordValueAnimatorArgs$Setter$Type<(A18)>, arg19: $RecordValueAnimatorArgs$Setter$Type<(A19)>, arg20: $RecordValueAnimatorArgs$Setter$Type<(A20)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>, arg12: $RecordValueAnimatorArgs$Setter$Type<(A12)>, arg13: $RecordValueAnimatorArgs$Setter$Type<(A13)>, arg14: $RecordValueAnimatorArgs$Setter$Type<(A14)>, arg15: $RecordValueAnimatorArgs$Setter$Type<(A15)>, arg16: $RecordValueAnimatorArgs$Setter$Type<(A16)>, arg17: $RecordValueAnimatorArgs$Setter$Type<(A17)>, arg18: $RecordValueAnimatorArgs$Setter$Type<(A18)>, arg19: $RecordValueAnimatorArgs$Setter$Type<(A19)>, arg20: $RecordValueAnimatorArgs$Setter$Type<(A20)>): void
}

export namespace $RecordValueAnimatorArgs$Arg20$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg20$Up$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, T> = ($RecordValueAnimatorArgs$Arg20$Up<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (A20), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg20$Up_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, T> = $RecordValueAnimatorArgs$Arg20$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (A20), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueWriter" {
import {$WriterContext, $WriterContext$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$WriterContext"

export interface $ValueWriter {

 "write"(arg0: any, arg1: $WriterContext$Type): void
 "canWrite"(arg0: any): boolean
 "isPrimitiveType"(): boolean
}

export namespace $ValueWriter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueWriter$Type = ($ValueWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueWriter_ = $ValueWriter$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$FloatListListEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$FloatListListEntry$FloatListCell, $FloatListListEntry$FloatListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$FloatListListEntry$FloatListCell"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractTextFieldListListEntry, $AbstractTextFieldListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractTextFieldListListEntry"

export class $FloatListListEntry extends $AbstractTextFieldListListEntry<(float), ($FloatListListEntry$FloatListCell), ($FloatListListEntry)> {

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: $List$Type<(float)>, defaultExpanded: boolean, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, saveConsumer: $Consumer$Type<($List$Type<(float)>)>, defaultValue: $Supplier$Type<($List$Type<(float)>)>, resetButtonKey: $Component$Type, requiresRestart: boolean, deleteButtonEnabled: boolean, insertInFront: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: $List$Type<(float)>, defaultExpanded: boolean, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, saveConsumer: $Consumer$Type<($List$Type<(float)>)>, defaultValue: $Supplier$Type<($List$Type<(float)>)>, resetButtonKey: $Component$Type, requiresRestart: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: $List$Type<(float)>, defaultExpanded: boolean, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, saveConsumer: $Consumer$Type<($List$Type<(float)>)>, defaultValue: $Supplier$Type<($List$Type<(float)>)>, resetButtonKey: $Component$Type)

public "self"(): $FloatListListEntry
public "setMaximum"(maximum: float): $FloatListListEntry
public "setMinimum"(minimum: float): $FloatListListEntry
set "maximum"(value: float)
set "minimum"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FloatListListEntry$Type = ($FloatListListEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FloatListListEntry_ = $FloatListListEntry$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/scanner/$SimpleKey" {
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $SimpleKey {

constructor(tokenNumber: integer, required: boolean, index: integer, line: integer, column: integer, mark: $Mark$Type)

public "toString"(): string
public "getIndex"(): integer
public "getLine"(): integer
public "isRequired"(): boolean
public "getTokenNumber"(): integer
public "getMark"(): $Mark
public "getColumn"(): integer
get "index"(): integer
get "line"(): integer
get "required"(): boolean
get "tokenNumber"(): integer
get "mark"(): $Mark
get "column"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleKey$Type = ($SimpleKey);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleKey_ = $SimpleKey$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonArray" {
import {$Marshaller, $Marshaller$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/api/$Marshaller"
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$JsonGrammar, $JsonGrammar$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonGrammar"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ListIterator, $ListIterator$Type} from "packages/java/util/$ListIterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$JsonElement, $JsonElement$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonElement"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $JsonArray extends $JsonElement implements $List<($JsonElement)>, $Iterable<($JsonElement)> {

constructor(ts: $Collection$Type<(any)>, marshaller: $Marshaller$Type)
constructor<T>(ts: (T)[], marshaller: $Marshaller$Type)
constructor()

public "add"(e: $JsonElement$Type, comment: string): boolean
public "add"(e: $JsonElement$Type): boolean
public "add"(index: integer, element: $JsonElement$Type): void
public "remove"(index: integer): $JsonElement
public "remove"(o: any): boolean
public "get"<E>(clazz: $Class$Type<(E)>, index: integer): E
public "equals"(other: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): $JsonArray
public "indexOf"(obj: any): integer
public "getBoolean"(index: integer, defaultValue: boolean): boolean
public "getByte"(index: integer, defaultValue: byte): byte
public "getShort"(index: integer, defaultValue: short): short
public "getChar"(index: integer, defaultValue: character): character
public "getInt"(index: integer, defaultValue: integer): integer
public "getLong"(index: integer, defaultValue: long): long
public "getFloat"(index: integer, defaultValue: float): float
public "getDouble"(index: integer, defaultValue: double): double
public "clear"(): void
public "lastIndexOf"(obj: any): integer
public "isEmpty"(): boolean
public "size"(): integer
public "subList"(arg0: integer, arg1: integer): $List<($JsonElement)>
public "toArray"<T>(a: (T)[]): (T)[]
public "toArray"(): ($JsonElement)[]
public "iterator"(): $Iterator<($JsonElement)>
public "contains"(o: any): boolean
public "addAll"(c: $Collection$Type<(any)>): boolean
public "addAll"(index: integer, elements: $Collection$Type<(any)>): boolean
public "set"(index: integer, element: $JsonElement$Type): $JsonElement
public "removeAll"(c: $Collection$Type<(any)>): boolean
public "retainAll"(c: $Collection$Type<(any)>): boolean
public "listIterator"(index: integer): $ListIterator<($JsonElement)>
public "listIterator"(): $ListIterator<($JsonElement)>
public "containsAll"(c: $Collection$Type<(any)>): boolean
public "getString"(index: integer, defaultValue: string): string
public "setComment"(i: integer, comment: string): void
public "getComment"(i: integer): string
public "toJson"(comments: boolean, newlines: boolean, depth: integer): string
public "toJson"(grammar: $JsonGrammar$Type, depth: integer): string
public "getMarshaller"(): $Marshaller
public "setMarshaller"(marshaller: $Marshaller$Type): void
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<($JsonElement)>
public "replaceAll"(arg0: $UnaryOperator$Type<($JsonElement$Type)>): void
public static "of"<E>(arg0: $JsonElement$Type, arg1: $JsonElement$Type, arg2: $JsonElement$Type, arg3: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(arg0: $JsonElement$Type, arg1: $JsonElement$Type, arg2: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(arg0: $JsonElement$Type, arg1: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(arg0: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(): $List<($JsonElement)>
public static "of"<E>(arg0: $JsonElement$Type, arg1: $JsonElement$Type, arg2: $JsonElement$Type, arg3: $JsonElement$Type, arg4: $JsonElement$Type, arg5: $JsonElement$Type, arg6: $JsonElement$Type, arg7: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(arg0: $JsonElement$Type, arg1: $JsonElement$Type, arg2: $JsonElement$Type, arg3: $JsonElement$Type, arg4: $JsonElement$Type, arg5: $JsonElement$Type, arg6: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(arg0: $JsonElement$Type, arg1: $JsonElement$Type, arg2: $JsonElement$Type, arg3: $JsonElement$Type, arg4: $JsonElement$Type, arg5: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(arg0: $JsonElement$Type, arg1: $JsonElement$Type, arg2: $JsonElement$Type, arg3: $JsonElement$Type, arg4: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(arg0: $JsonElement$Type, arg1: $JsonElement$Type, arg2: $JsonElement$Type, arg3: $JsonElement$Type, arg4: $JsonElement$Type, arg5: $JsonElement$Type, arg6: $JsonElement$Type, arg7: $JsonElement$Type, arg8: $JsonElement$Type, arg9: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(arg0: $JsonElement$Type, arg1: $JsonElement$Type, arg2: $JsonElement$Type, arg3: $JsonElement$Type, arg4: $JsonElement$Type, arg5: $JsonElement$Type, arg6: $JsonElement$Type, arg7: $JsonElement$Type, arg8: $JsonElement$Type): $List<($JsonElement)>
public static "of"<E>(...arg0: ($JsonElement$Type)[]): $List<($JsonElement)>
public "spliterator"(): $Spliterator<($JsonElement)>
public "sort"(arg0: $Comparator$Type<(any)>): void
public "forEach"(arg0: $Consumer$Type<(any)>): void
public "toArray"<T>(arg0: $IntFunction$Type<((T)[])>): (T)[]
public "stream"(): $Stream<($JsonElement)>
public "removeIf"(arg0: $Predicate$Type<(any)>): boolean
public "parallelStream"(): $Stream<($JsonElement)>
[Symbol.iterator](): IterableIterator<$JsonElement>;
[index: number]: $JsonElement
get "empty"(): boolean
get "marshaller"(): $Marshaller
set "marshaller"(value: $Marshaller$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonArray$Type = ($JsonArray);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonArray_ = $JsonArray$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$TooltipListEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $TooltipListEntry<T> extends $AbstractConfigListEntry<(T)> {

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, requiresRestart: boolean)

public "setTooltipSupplier"(tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>): void
public "render"(graphics: $GuiGraphics$Type, index: integer, y: integer, x: integer, entryWidth: integer, entryHeight: integer, mouseX: integer, mouseY: integer, isHovered: boolean, delta: float): void
public "getTooltip"(mouseX: integer, mouseY: integer): $Optional<(($Component)[])>
public "getTooltip"(): $Optional<(($Component)[])>
public "getTooltipSupplier"(): $Supplier<($Optional<(($Component)[])>)>
set "tooltipSupplier"(value: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
get "tooltip"(): $Optional<(($Component)[])>
get "tooltipSupplier"(): $Supplier<($Optional<(($Component)[])>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TooltipListEntry$Type<T> = ($TooltipListEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TooltipListEntry_<T> = $TooltipListEntry$Type<(T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/parser/$VersionTagsTuple" {
import {$DumperOptions$Version, $DumperOptions$Version$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$Version"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $VersionTagsTuple {

constructor(version: $DumperOptions$Version$Type, tags: $Map$Type<(string), (string)>)

public "toString"(): string
public "getVersion"(): $DumperOptions$Version
public "getTags"(): $Map<(string), (string)>
get "version"(): $DumperOptions$Version
get "tags"(): $Map<(string), (string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VersionTagsTuple$Type = ($VersionTagsTuple);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VersionTagsTuple_ = $VersionTagsTuple$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$SelectionTopCellElement" {
import {$DropdownBoxEntry, $DropdownBoxEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AbstractContainerEventHandler, $AbstractContainerEventHandler$Type} from "packages/net/minecraft/client/gui/components/events/$AbstractContainerEventHandler"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $DropdownBoxEntry$SelectionTopCellElement<R> extends $AbstractContainerEventHandler {

constructor()

public "getValue"(): R
public "getParent"(): $DropdownBoxEntry<(R)>
public "setValue"(arg0: R): void
public "hasError"(): boolean
public "getError"(): $Optional<($Component)>
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: float): void
public "getSearchTerm"(): $Component
public "getConfigError"(): $Optional<($Component)>
public "isEdited"(): boolean
public "getPreferredTextColor"(): integer
public "selectFirstRecommendation"(): void
public "hasConfigError"(): boolean
public "isSuggestionMode"(): boolean
get "value"(): R
get "parent"(): $DropdownBoxEntry<(R)>
set "value"(value: R)
get "error"(): $Optional<($Component)>
get "searchTerm"(): $Component
get "configError"(): $Optional<($Component)>
get "edited"(): boolean
get "preferredTextColor"(): integer
get "suggestionMode"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropdownBoxEntry$SelectionTopCellElement$Type<R> = ($DropdownBoxEntry$SelectionTopCellElement<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropdownBoxEntry$SelectionTopCellElement_<R> = $DropdownBoxEntry$SelectionTopCellElement$Type<(R)>;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/$ConfigBuilderImpl" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ConfigEntryBuilder, $ConfigEntryBuilder$Type} from "packages/me/shedaniel/clothconfig2/api/$ConfigEntryBuilder"
import {$ConfigCategory, $ConfigCategory$Type} from "packages/me/shedaniel/clothconfig2/api/$ConfigCategory"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ConfigBuilder, $ConfigBuilder$Type} from "packages/me/shedaniel/clothconfig2/api/$ConfigBuilder"
import {$ConfigEntryBuilderImpl, $ConfigEntryBuilderImpl$Type} from "packages/me/shedaniel/clothconfig2/impl/$ConfigEntryBuilderImpl"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $ConfigBuilderImpl implements $ConfigBuilder {

constructor()

public "build"(): $Screen
public "setSavingRunnable"(runnable: $Runnable$Type): $ConfigBuilder
public "getOrCreateCategory"(categoryKey: $Component$Type): $ConfigCategory
public "setTitle"(title: $Component$Type): $ConfigBuilder
public "isEditable"(): boolean
public "setEditable"(editable: boolean): $ConfigBuilder
public "getTitle"(): $Component
public "hasCategory"(category: $Component$Type): boolean
public "removeCategory"(category: $Component$Type): $ConfigBuilder
public "setParentScreen"(parent: $Screen$Type): $ConfigBuilder
public "getAfterInitConsumer"(): $Consumer<($Screen)>
public "setFallbackCategory"(fallbackCategory: $ConfigCategory$Type): $ConfigBuilder
public "setShouldListSmoothScroll"(shouldListSmoothScroll: boolean): $ConfigBuilder
public "setShouldTabsSmoothScroll"(shouldTabsSmoothScroll: boolean): $ConfigBuilder
public "isTabsSmoothScrolling"(): boolean
public "setAfterInitConsumer"(afterInitConsumer: $Consumer$Type<($Screen$Type)>): $ConfigBuilder
public "hasTransparentBackground"(): boolean
public "setTransparentBackground"(transparentBackground: boolean): $ConfigBuilder
public "setGlobalizedExpanded"(globalizedExpanded: boolean): void
public "setDefaultBackgroundTexture"(texture: $ResourceLocation$Type): $ConfigBuilder
public "removeCategoryIfExists"(category: $Component$Type): $ConfigBuilder
public "getDefaultBackgroundTexture"(): $ResourceLocation
public "isListSmoothScrolling"(): boolean
public "setGlobalized"(globalized: boolean): void
public "isAlwaysShowTabs"(): boolean
public "doesConfirmSave"(): boolean
public "setAlwaysShowTabs"(alwaysShowTabs: boolean): $ConfigBuilder
public "getParentScreen"(): $Screen
public "getSavingRunnable"(): $Runnable
public "setDoesConfirmSave"(confirmSave: boolean): $ConfigBuilder
public static "create"(): $ConfigBuilder
public "entryBuilder"(): $ConfigEntryBuilder
public "transparentBackground"(): $ConfigBuilder
/**
 * 
 * @deprecated
 */
public "setDoesProcessErrors"(processErrors: boolean): $ConfigBuilder
public "alwaysShowTabs"(): $ConfigBuilder
/**
 * 
 * @deprecated
 */
public "doesProcessErrors"(): boolean
public "solidBackground"(): $ConfigBuilder
/**
 * 
 * @deprecated
 */
public "getEntryBuilder"(): $ConfigEntryBuilderImpl
set "savingRunnable"(value: $Runnable$Type)
set "title"(value: $Component$Type)
get "editable"(): boolean
set "editable"(value: boolean)
get "title"(): $Component
set "parentScreen"(value: $Screen$Type)
get "afterInitConsumer"(): $Consumer<($Screen)>
set "fallbackCategory"(value: $ConfigCategory$Type)
set "shouldListSmoothScroll"(value: boolean)
set "shouldTabsSmoothScroll"(value: boolean)
get "tabsSmoothScrolling"(): boolean
set "afterInitConsumer"(value: $Consumer$Type<($Screen$Type)>)
set "globalizedExpanded"(value: boolean)
set "defaultBackgroundTexture"(value: $ResourceLocation$Type)
get "defaultBackgroundTexture"(): $ResourceLocation
get "listSmoothScrolling"(): boolean
set "globalized"(value: boolean)
get "parentScreen"(): $Screen
get "savingRunnable"(): $Runnable
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigBuilderImpl$Type = ($ConfigBuilderImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigBuilderImpl_ = $ConfigBuilderImpl$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$StreamStartEvent" {
import {$Event$ID, $Event$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Event, $Event$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event"

export class $StreamStartEvent extends $Event {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getEventId"(): $Event$ID
get "eventId"(): $Event$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StreamStartEvent$Type = ($StreamStartEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StreamStartEvent_ = $StreamStartEvent$Type;
}}
declare module "packages/me/shedaniel/math/$FloatingRectangle" {
import {$FloatingPoint, $FloatingPoint$Type} from "packages/me/shedaniel/math/$FloatingPoint"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Dimension, $Dimension$Type} from "packages/me/shedaniel/math/$Dimension"
import {$FloatingDimension, $FloatingDimension$Type} from "packages/me/shedaniel/math/$FloatingDimension"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"
import {$Point, $Point$Type} from "packages/me/shedaniel/math/$Point"

export class $FloatingRectangle implements $Cloneable {
 "x": double
 "y": double
 "width": double
 "height": double

constructor(arg0: $FloatingPoint$Type)
constructor(arg0: $Point$Type)
constructor(arg0: $FloatingPoint$Type, arg1: $FloatingDimension$Type)
constructor(arg0: $FloatingPoint$Type, arg1: $Dimension$Type)
constructor(arg0: $Dimension$Type)
constructor(arg0: $FloatingDimension$Type)
constructor(arg0: double, arg1: double, arg2: double, arg3: double)
constructor()
constructor(arg0: $FloatingRectangle$Type)
constructor(arg0: $Rectangle$Type)
constructor(arg0: $Point$Type, arg1: $FloatingDimension$Type)
constructor(arg0: $Point$Type, arg1: $Dimension$Type)
constructor(arg0: integer, arg1: integer)

public "add"(arg0: $Point$Type): void
public "add"(arg0: $FloatingRectangle$Type): void
public "add"(arg0: $FloatingPoint$Type): void
public "add"(arg0: double, arg1: double): void
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "isEmpty"(): boolean
public "contains"(arg0: integer, arg1: integer): boolean
public "contains"(arg0: $FloatingPoint$Type): boolean
public "contains"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
public "contains"(arg0: double, arg1: double): boolean
public "contains"(arg0: $Rectangle$Type): boolean
public "contains"(arg0: $FloatingRectangle$Type): boolean
public "contains"(arg0: $Point$Type): boolean
public "getBounds"(): $Rectangle
public "getLocation"(): $Point
public "getSize"(): $Dimension
public "grow"(arg0: double, arg1: double): void
public "resize"(arg0: double, arg1: double): void
public "move"(arg0: double, arg1: double): void
public "setSize"(arg0: double, arg1: double): void
public "setSize"(arg0: $Dimension$Type): void
public "setSize"(arg0: $FloatingDimension$Type): void
public "union"(arg0: $FloatingRectangle$Type): $FloatingRectangle
public "intersects"(arg0: $FloatingRectangle$Type): boolean
public "getY"(): double
public "getMaxX"(): double
public "getMinX"(): double
public "getMaxY"(): double
public "getMinY"(): double
public "translate"(arg0: double, arg1: double): void
public "getX"(): double
public "getWidth"(): double
public "getHeight"(): double
public "setLocation"(arg0: $FloatingPoint$Type): void
public "setLocation"(arg0: double, arg1: double): void
public "setLocation"(arg0: $Point$Type): void
public "setBounds"(arg0: double, arg1: double, arg2: double, arg3: double): void
public "setBounds"(arg0: $Rectangle$Type): void
public "setBounds"(arg0: $FloatingRectangle$Type): void
public "inside"(arg0: double, arg1: double): boolean
public "getCenterX"(): double
public "getFloatingBounds"(): $FloatingRectangle
public "reshape"(arg0: double, arg1: double, arg2: double, arg3: double): void
public "getCenterY"(): double
public "intersection"(arg0: $FloatingRectangle$Type): $FloatingRectangle
public "getFloatingLocation"(): $FloatingPoint
get "empty"(): boolean
get "bounds"(): $Rectangle
get "location"(): $Point
get "size"(): $Dimension
set "size"(value: $Dimension$Type)
set "size"(value: $FloatingDimension$Type)
get "y"(): double
get "maxX"(): double
get "minX"(): double
get "maxY"(): double
get "minY"(): double
get "x"(): double
get "width"(): double
get "height"(): double
set "location"(value: $FloatingPoint$Type)
set "location"(value: $Point$Type)
set "bounds"(value: $Rectangle$Type)
set "bounds"(value: $FloatingRectangle$Type)
get "centerX"(): double
get "floatingBounds"(): $FloatingRectangle
get "centerY"(): double
get "floatingLocation"(): $FloatingPoint
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FloatingRectangle$Type = ($FloatingRectangle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FloatingRectangle_ = $FloatingRectangle$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$NodeEvent" {
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Event, $Event$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event"

export class $NodeEvent extends $Event {

constructor(anchor: string, startMark: $Mark$Type, endMark: $Mark$Type)

public "getAnchor"(): string
get "anchor"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NodeEvent$Type = ($NodeEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NodeEvent_ = $NodeEvent$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/extensions/compactnotation/$CompactConstructor" {
import {$Constructor, $Constructor$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$Constructor"
import {$CompactData, $CompactData$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/extensions/compactnotation/$CompactData"
import {$SafeConstructor$ConstructUndefined, $SafeConstructor$ConstructUndefined$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$SafeConstructor$ConstructUndefined"

export class $CompactConstructor extends $Constructor {
static readonly "undefinedConstructor": $SafeConstructor$ConstructUndefined

constructor()

public "getCompactData"(scalar: string): $CompactData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompactConstructor$Type = ($CompactConstructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompactConstructor_ = $CompactConstructor$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$TomlWriter" {
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"

export class $TomlWriter {

constructor()

public "write"(from: any, target: $File$Type): void
public "write"(from: any, target: $Writer$Type): void
public "write"(from: any, target: $OutputStream$Type): void
public "write"(from: any): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TomlWriter$Type = ($TomlWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TomlWriter_ = $TomlWriter$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$DropdownMenuBuilder" {
import {$DropdownBoxEntry$SelectionCellCreator, $DropdownBoxEntry$SelectionCellCreator$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$SelectionCellCreator"
import {$DropdownBoxEntry, $DropdownBoxEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$DropdownBoxEntry$SelectionTopCellElement, $DropdownBoxEntry$SelectionTopCellElement$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$SelectionTopCellElement"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$FieldBuilder, $FieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$FieldBuilder"

export class $DropdownMenuBuilder<T> extends $FieldBuilder<(T), ($DropdownBoxEntry<(T)>), ($DropdownMenuBuilder<(T)>)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, topCellElement: $DropdownBoxEntry$SelectionTopCellElement$Type<(T)>, cellCreator: $DropdownBoxEntry$SelectionCellCreator$Type<(T)>)

public "build"(): $DropdownBoxEntry<(T)>
public "setSaveConsumer"(saveConsumer: $Consumer$Type<(T)>): $DropdownMenuBuilder<(T)>
public "setTooltipSupplier"(tooltipSupplier: $Function$Type<(T), ($Optional$Type<(($Component$Type)[])>)>): $DropdownMenuBuilder<(T)>
public "setTooltipSupplier"(tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>): $DropdownMenuBuilder<(T)>
public "requireRestart"(): $DropdownMenuBuilder<(T)>
public "setTooltip"(...tooltip: ($Component$Type)[]): $DropdownMenuBuilder<(T)>
public "setTooltip"(tooltip: $Optional$Type<(($Component$Type)[])>): $DropdownMenuBuilder<(T)>
public "setErrorSupplier"(errorSupplier: $Function$Type<(T), ($Optional$Type<($Component$Type)>)>): $DropdownMenuBuilder<(T)>
public "setDefaultValue"(defaultValue: $Supplier$Type<(T)>): $DropdownMenuBuilder<(T)>
public "setDefaultValue"(defaultValue: T): $DropdownMenuBuilder<(T)>
public "setSelections"(selections: $Iterable$Type<(T)>): $DropdownMenuBuilder<(T)>
public "setSuggestionMode"(suggestionMode: boolean): $DropdownMenuBuilder<(T)>
public "isSuggestionMode"(): boolean
set "saveConsumer"(value: $Consumer$Type<(T)>)
set "tooltipSupplier"(value: $Function$Type<(T), ($Optional$Type<(($Component$Type)[])>)>)
set "tooltipSupplier"(value: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
set "tooltip"(value: ($Component$Type)[])
set "tooltip"(value: $Optional$Type<(($Component$Type)[])>)
set "errorSupplier"(value: $Function$Type<(T), ($Optional$Type<($Component$Type)>)>)
set "defaultValue"(value: $Supplier$Type<(T)>)
set "defaultValue"(value: T)
set "selections"(value: $Iterable$Type<(T)>)
set "suggestionMode"(value: boolean)
get "suggestionMode"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropdownMenuBuilder$Type<T> = ($DropdownMenuBuilder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropdownMenuBuilder_<T> = $DropdownMenuBuilder$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$FloatListListEntry$FloatListCell" {
import {$FloatListListEntry, $FloatListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$FloatListListEntry"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$AbstractTextFieldListListEntry$AbstractTextFieldListCell, $AbstractTextFieldListListEntry$AbstractTextFieldListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractTextFieldListListEntry$AbstractTextFieldListCell"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export class $FloatListListEntry$FloatListCell extends $AbstractTextFieldListListEntry$AbstractTextFieldListCell<(float), ($FloatListListEntry$FloatListCell), ($FloatListListEntry)> {

constructor(value: float, listListEntry: $FloatListListEntry$Type)

public "getValue"(): float
public "getError"(): $Optional<($Component)>
get "value"(): float
get "error"(): $Optional<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FloatListListEntry$FloatListCell$Type = ($FloatListListEntry$FloatListCell);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FloatListListEntry$FloatListCell_ = $FloatListListEntry$FloatListCell$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/$EasingMethod" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $EasingMethod {

 "apply"(arg0: double): double

(arg0: double): double
}

export namespace $EasingMethod {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EasingMethod$Type = ($EasingMethod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EasingMethod_ = $EasingMethod$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$LongListListEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$LongListListEntry$LongListCell, $LongListListEntry$LongListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$LongListListEntry$LongListCell"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractTextFieldListListEntry, $AbstractTextFieldListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractTextFieldListListEntry"

export class $LongListListEntry extends $AbstractTextFieldListListEntry<(long), ($LongListListEntry$LongListCell), ($LongListListEntry)> {

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: $List$Type<(long)>, defaultExpanded: boolean, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, saveConsumer: $Consumer$Type<($List$Type<(long)>)>, defaultValue: $Supplier$Type<($List$Type<(long)>)>, resetButtonKey: $Component$Type, requiresRestart: boolean, deleteButtonEnabled: boolean, insertInFront: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: $List$Type<(long)>, defaultExpanded: boolean, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, saveConsumer: $Consumer$Type<($List$Type<(long)>)>, defaultValue: $Supplier$Type<($List$Type<(long)>)>, resetButtonKey: $Component$Type, requiresRestart: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: $List$Type<(long)>, defaultExpanded: boolean, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, saveConsumer: $Consumer$Type<($List$Type<(long)>)>, defaultValue: $Supplier$Type<($List$Type<(long)>)>, resetButtonKey: $Component$Type)

public "self"(): $LongListListEntry
public "setMaximum"(maximum: long): $LongListListEntry
public "setMinimum"(minimum: long): $LongListListEntry
set "maximum"(value: long)
set "minimum"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongListListEntry$Type = ($LongListListEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongListListEntry_ = $LongListListEntry$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg2$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg2$Op<A1, A2, T> {

 "construct"(arg0: A1, arg1: A2): T

(arg0: A1, arg1: A2): T
}

export namespace $RecordValueAnimatorArgs$Arg2$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg2$Op$Type<A1, A2, T> = ($RecordValueAnimatorArgs$Arg2$Op<(A1), (A2), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg2$Op_<A1, A2, T> = $RecordValueAnimatorArgs$Arg2$Op$Type<(A1), (A2), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg2$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg2$Up<A1, A2, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>): void
}

export namespace $RecordValueAnimatorArgs$Arg2$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg2$Up$Type<A1, A2, T> = ($RecordValueAnimatorArgs$Arg2$Up<(A1), (A2), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg2$Up_<A1, A2, T> = $RecordValueAnimatorArgs$Arg2$Up$Type<(A1), (A2), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/resolver/$ResolverTuple" {
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$Tag, $Tag$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Tag"

export class $ResolverTuple {

constructor(tag: $Tag$Type, regexp: $Pattern$Type)

public "toString"(): string
public "getTag"(): $Tag
public "getRegexp"(): $Pattern
get "tag"(): $Tag
get "regexp"(): $Pattern
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ResolverTuple$Type = ($ResolverTuple);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ResolverTuple_ = $ResolverTuple$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$ScalarNode" {
import {$DumperOptions$ScalarStyle, $DumperOptions$ScalarStyle$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$ScalarStyle"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Node, $Node$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Node"
import {$Tag, $Tag$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Tag"
import {$NodeId, $NodeId$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$NodeId"

export class $ScalarNode extends $Node {

/**
 * 
 * @deprecated
 */
constructor(tag: $Tag$Type, resolved: boolean, value: string, startMark: $Mark$Type, endMark: $Mark$Type, style: character)
/**
 * 
 * @deprecated
 */
constructor(tag: $Tag$Type, value: string, startMark: $Mark$Type, endMark: $Mark$Type, style: character)
constructor(tag: $Tag$Type, resolved: boolean, value: string, startMark: $Mark$Type, endMark: $Mark$Type, style: $DumperOptions$ScalarStyle$Type)
constructor(tag: $Tag$Type, value: string, startMark: $Mark$Type, endMark: $Mark$Type, style: $DumperOptions$ScalarStyle$Type)

public "toString"(): string
public "getValue"(): string
public "getNodeId"(): $NodeId
public "getScalarStyle"(): $DumperOptions$ScalarStyle
/**
 * 
 * @deprecated
 */
public "getStyle"(): character
public "isPlain"(): boolean
get "value"(): string
get "nodeId"(): $NodeId
get "scalarStyle"(): $DumperOptions$ScalarStyle
get "style"(): character
get "plain"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScalarNode$Type = ($ScalarNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScalarNode_ = $ScalarNode$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$CommentParserContext" {
import {$ParserContext, $ParserContext$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$ParserContext"
import {$Jankson, $Jankson$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$Jankson"

export class $CommentParserContext implements $ParserContext<(string)> {

constructor(codePoint: integer)

public "consume"(codePoint: integer, loader: $Jankson$Type): boolean
public "eof"(): void
public "getResult"(): string
public "isComplete"(): boolean
get "result"(): string
get "complete"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommentParserContext$Type = ($CommentParserContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommentParserContext_ = $CommentParserContext$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$TokenParserContext" {
import {$ParserContext, $ParserContext$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$ParserContext"
import {$Jankson, $Jankson$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$Jankson"
import {$JsonPrimitive, $JsonPrimitive$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonPrimitive"

export class $TokenParserContext implements $ParserContext<($JsonPrimitive)> {

constructor(firstCodePoint: integer)

public "consume"(codePoint: integer, loader: $Jankson$Type): boolean
public "eof"(): void
public "getResult"(): $JsonPrimitive
public "isComplete"(): boolean
get "result"(): $JsonPrimitive
get "complete"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TokenParserContext$Type = ($TokenParserContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TokenParserContext_ = $TokenParserContext$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$StreamEndEvent" {
import {$Event$ID, $Event$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Event, $Event$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event"

export class $StreamEndEvent extends $Event {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getEventId"(): $Event$ID
get "eventId"(): $Event$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StreamEndEvent$Type = ($StreamEndEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StreamEndEvent_ = $StreamEndEvent$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$CollectionNode" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$DumperOptions$FlowStyle, $DumperOptions$FlowStyle$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Node, $Node$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Node"
import {$Tag, $Tag$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Tag"

export class $CollectionNode<T> extends $Node {

constructor(tag: $Tag$Type, startMark: $Mark$Type, endMark: $Mark$Type, flowStyle: $DumperOptions$FlowStyle$Type)
/**
 * 
 * @deprecated
 */
constructor(tag: $Tag$Type, startMark: $Mark$Type, endMark: $Mark$Type, flowStyle: boolean)

public "getValue"(): $List<(T)>
public "getFlowStyle"(): $DumperOptions$FlowStyle
public "setFlowStyle"(flowStyle: $DumperOptions$FlowStyle$Type): void
/**
 * 
 * @deprecated
 */
public "setFlowStyle"(flowStyle: boolean): void
public "setEndMark"(endMark: $Mark$Type): void
get "value"(): $List<(T)>
get "flowStyle"(): $DumperOptions$FlowStyle
set "flowStyle"(value: $DumperOptions$FlowStyle$Type)
set "flowStyle"(value: boolean)
set "endMark"(value: $Mark$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CollectionNode$Type<T> = ($CollectionNode<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CollectionNode_<T> = $CollectionNode$Type<(T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$Toml" {
import {$Date, $Date$Type} from "packages/java/util/$Date"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Toml {

constructor(defaults: $Toml$Type)
constructor()

public "getBoolean"(key: string, defaultValue: boolean): boolean
public "getBoolean"(key: string): boolean
public "getLong"(key: string, defaultValue: long): long
public "getLong"(key: string): long
public "getDouble"(key: string): double
public "getDouble"(key: string, defaultValue: double): double
public "isEmpty"(): boolean
public "to"<T>(targetClass: $Class$Type<(T)>): T
public "contains"(key: string): boolean
public "entrySet"(): $Set<($Map$Entry<(string), (any)>)>
public "toMap"(): $Map<(string), (any)>
public "read"(reader: $Reader$Type): $Toml
public "read"(file: $File$Type): $Toml
public "read"(tomlString: string): $Toml
public "read"(otherToml: $Toml$Type): $Toml
public "read"(inputStream: $InputStream$Type): $Toml
public "getTable"(key: string): $Toml
public "getString"(key: string, defaultValue: string): string
public "getString"(key: string): string
public "getDate"(key: string, defaultValue: $Date$Type): $Date
public "getDate"(key: string): $Date
public "getList"<T>(key: string, defaultValue: $List$Type<(T)>): $List<(T)>
public "getList"<T>(key: string): $List<(T)>
public "getTables"(key: string): $List<($Toml)>
public "containsTableArray"(key: string): boolean
public "containsTable"(key: string): boolean
public "containsPrimitive"(key: string): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Toml$Type = ($Toml);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Toml_ = $Toml$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$IntegerListListEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$IntegerListListEntry$IntegerListCell, $IntegerListListEntry$IntegerListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$IntegerListListEntry$IntegerListCell"
import {$AbstractTextFieldListListEntry, $AbstractTextFieldListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractTextFieldListListEntry"

export class $IntegerListListEntry extends $AbstractTextFieldListListEntry<(integer), ($IntegerListListEntry$IntegerListCell), ($IntegerListListEntry)> {

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: $List$Type<(integer)>, defaultExpanded: boolean, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, saveConsumer: $Consumer$Type<($List$Type<(integer)>)>, defaultValue: $Supplier$Type<($List$Type<(integer)>)>, resetButtonKey: $Component$Type, requiresRestart: boolean, deleteButtonEnabled: boolean, insertInFront: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: $List$Type<(integer)>, defaultExpanded: boolean, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, saveConsumer: $Consumer$Type<($List$Type<(integer)>)>, defaultValue: $Supplier$Type<($List$Type<(integer)>)>, resetButtonKey: $Component$Type, requiresRestart: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: $List$Type<(integer)>, defaultExpanded: boolean, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, saveConsumer: $Consumer$Type<($List$Type<(integer)>)>, defaultValue: $Supplier$Type<($List$Type<(integer)>)>, resetButtonKey: $Component$Type)

public "self"(): $IntegerListListEntry
public "setMaximum"(maximum: integer): $IntegerListListEntry
public "setMinimum"(minimum: integer): $IntegerListListEntry
set "maximum"(value: integer)
set "minimum"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntegerListListEntry$Type = ($IntegerListListEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntegerListListEntry_ = $IntegerListListEntry$Type;
}}
declare module "packages/me/shedaniel/autoconfig/event/$ConfigSerializeEvent" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ConfigSerializeEvent {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigSerializeEvent$Type = ($ConfigSerializeEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigSerializeEvent_ = $ConfigSerializeEvent$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$TomlParser" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $TomlParser {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TomlParser$Type = ($TomlParser);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TomlParser_ = $TomlParser$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/emitter/$EmitterState" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $EmitterState {

 "expect"(): void

(): void
}

export namespace $EmitterState {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmitterState$Type = ($EmitterState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmitterState_ = $EmitterState$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $RecordValueAnimatorArgs {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Type = ($RecordValueAnimatorArgs);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs_ = $RecordValueAnimatorArgs$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/scanner/$Constant" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Constant {
static readonly "LINEBR": $Constant
static readonly "FULL_LINEBR": $Constant
static readonly "NULL_OR_LINEBR": $Constant
static readonly "NULL_BL_LINEBR": $Constant
static readonly "NULL_BL_T_LINEBR": $Constant
static readonly "NULL_BL_T": $Constant
static readonly "URI_CHARS": $Constant
static readonly "ALPHA": $Constant


public "has"(c: integer, additional: string): boolean
public "has"(c: integer): boolean
public "hasNo"(c: integer): boolean
public "hasNo"(c: integer, additional: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Constant$Type = ($Constant);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Constant_ = $Constant$Type;
}}
declare module "packages/me/shedaniel/autoconfig/gui/$DefaultGuiTransformers" {
import {$GuiRegistry, $GuiRegistry$Type} from "packages/me/shedaniel/autoconfig/gui/registry/$GuiRegistry"

export class $DefaultGuiTransformers {


public static "apply"(registry: $GuiRegistry$Type): $GuiRegistry
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultGuiTransformers$Type = ($DefaultGuiTransformers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultGuiTransformers_ = $DefaultGuiTransformers$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/api/$DeserializerFunction" {
import {$Marshaller, $Marshaller$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/api/$Marshaller"
import {$InternalDeserializerFunction, $InternalDeserializerFunction$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/serializer/$InternalDeserializerFunction"

export interface $DeserializerFunction<A, B> extends $InternalDeserializerFunction<(B)> {

 "apply"(arg0: A, arg1: $Marshaller$Type): B
 "deserialize"(a: any, m: $Marshaller$Type): B

(arg0: A, arg1: $Marshaller$Type): B
}

export namespace $DeserializerFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeserializerFunction$Type<A, B> = ($DeserializerFunction<(A), (B)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeserializerFunction_<A, B> = $DeserializerFunction$Type<(A), (B)>;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$BooleanToggleBuilder" {
import {$BooleanListEntry, $BooleanListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$BooleanListEntry"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$AbstractFieldBuilder, $AbstractFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractFieldBuilder"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export class $BooleanToggleBuilder extends $AbstractFieldBuilder<(boolean), ($BooleanListEntry), ($BooleanToggleBuilder)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, value: boolean)

public "setErrorSupplier"(errorSupplier: $Function$Type<(boolean), ($Optional$Type<($Component$Type)>)>): $BooleanToggleBuilder
public "setDefaultValue"(defaultValue: boolean): $BooleanToggleBuilder
public "getYesNoTextSupplier"(): $Function<(boolean), ($Component)>
public "setYesNoTextSupplier"(yesNoTextSupplier: $Function$Type<(boolean), ($Component$Type)>): $BooleanToggleBuilder
set "errorSupplier"(value: $Function$Type<(boolean), ($Optional$Type<($Component$Type)>)>)
set "defaultValue"(value: boolean)
get "yesNoTextSupplier"(): $Function<(boolean), ($Component)>
set "yesNoTextSupplier"(value: $Function$Type<(boolean), ($Component$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BooleanToggleBuilder$Type = ($BooleanToggleBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BooleanToggleBuilder_ = $BooleanToggleBuilder$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Setter<T> {

 "set"(arg0: T): void

(arg0: T): void
}

export namespace $RecordValueAnimatorArgs$Setter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Setter$Type<T> = ($RecordValueAnimatorArgs$Setter<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Setter_<T> = $RecordValueAnimatorArgs$Setter$Type<(T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$FlowMappingStartToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $FlowMappingStartToken extends $Token {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlowMappingStartToken$Type = ($FlowMappingStartToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlowMappingStartToken_ = $FlowMappingStartToken$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$FieldBuilder" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$Requirement, $Requirement$Type} from "packages/me/shedaniel/clothconfig2/api/$Requirement"

export class $FieldBuilder<T, A extends $AbstractConfigListEntry<(any)>, SELF extends $FieldBuilder<(T), (A), (SELF)>> {


/**
 * 
 * @deprecated
 */
public "buildEntry"(): $AbstractConfigListEntry<(any)>
public "getResetButtonKey"(): $Component
public "isRequireRestart"(): boolean
public "getFieldNameKey"(): $Component
public "getDefaultValue"(): $Supplier<(T)>
public "build"(): A
public "requireRestart"(requireRestart: boolean): void
public "setDisplayRequirement"(requirement: $Requirement$Type): SELF
public "setRequirement"(requirement: $Requirement$Type): SELF
get "resetButtonKey"(): $Component
get "fieldNameKey"(): $Component
get "defaultValue"(): $Supplier<(T)>
set "displayRequirement"(value: $Requirement$Type)
set "requirement"(value: $Requirement$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FieldBuilder$Type<T, A, SELF> = ($FieldBuilder<(T), (A), (SELF)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FieldBuilder_<T, A, SELF> = $FieldBuilder$Type<(T), (A), (SELF)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$MarshallerImpl" {
import {$Marshaller, $Marshaller$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/api/$Marshaller"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$DeserializerFunction, $DeserializerFunction$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/api/$DeserializerFunction"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$JsonElement, $JsonElement$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonElement"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$JsonObject, $JsonObject$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonObject"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"

/**
 * 
 * @deprecated
 */
export class $MarshallerImpl implements $Marshaller {

constructor()

public "register"<T>(clazz: $Class$Type<(T)>, marshaller: $Function$Type<(any), (T)>): void
public "marshallCarefully"<T>(clazz: $Class$Type<(T)>, elem: $JsonElement$Type): T
public "serialize"(obj: any): $JsonElement
public "registerTypeFactory"<T>(clazz: $Class$Type<(T)>, supplier: $Supplier$Type<(T)>): void
public "registerDeserializer"<A, B>(sourceClass: $Class$Type<(A)>, targetClass: $Class$Type<(B)>, arg2: $DeserializerFunction$Type<(A), (B)>): void
public "registerTypeAdapter"<T>(clazz: $Class$Type<(T)>, adapter: $Function$Type<($JsonObject$Type), (T)>): void
public "marshall"<T>(type: $Type$Type, elem: $JsonElement$Type): T
public "marshall"<T>(clazz: $Class$Type<(T)>, elem: $JsonElement$Type): T
public "marshall"<T>(clazz: $Class$Type<(T)>, elem: $JsonElement$Type, failFast: boolean): T
public static "getFallback"(): $Marshaller
public "registerSerializer"<T>(clazz: $Class$Type<(T)>, serializer: $BiFunction$Type<(T), ($Marshaller$Type), ($JsonElement$Type)>): void
public "registerSerializer"<T>(clazz: $Class$Type<(T)>, serializer: $Function$Type<(T), ($JsonElement$Type)>): void
get "fallback"(): $Marshaller
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MarshallerImpl$Type = ($MarshallerImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MarshallerImpl_ = $MarshallerImpl$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$PropertyUtils" {
import {$Property, $Property$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$Property"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$BeanAccess, $BeanAccess$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$BeanAccess"

export class $PropertyUtils {

constructor()

public "getProperty"(type: $Class$Type<(any)>, name: string): $Property
public "getProperty"(type: $Class$Type<(any)>, name: string, bAccess: $BeanAccess$Type): $Property
public "getProperties"(type: $Class$Type<(any)>): $Set<($Property)>
public "getProperties"(type: $Class$Type<(any)>, bAccess: $BeanAccess$Type): $Set<($Property)>
public "setBeanAccess"(beanAccess: $BeanAccess$Type): void
public "isAllowReadOnlyProperties"(): boolean
public "setAllowReadOnlyProperties"(allowReadOnlyProperties: boolean): void
public "setSkipMissingProperties"(skipMissingProperties: boolean): void
public "isSkipMissingProperties"(): boolean
set "beanAccess"(value: $BeanAccess$Type)
get "allowReadOnlyProperties"(): boolean
set "allowReadOnlyProperties"(value: boolean)
set "skipMissingProperties"(value: boolean)
get "skipMissingProperties"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PropertyUtils$Type = ($PropertyUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PropertyUtils_ = $PropertyUtils$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$DoubleListListEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$DoubleListListEntry$DoubleListCell, $DoubleListListEntry$DoubleListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DoubleListListEntry$DoubleListCell"
import {$AbstractTextFieldListListEntry, $AbstractTextFieldListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractTextFieldListListEntry"

export class $DoubleListListEntry extends $AbstractTextFieldListListEntry<(double), ($DoubleListListEntry$DoubleListCell), ($DoubleListListEntry)> {

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: $List$Type<(double)>, defaultExpanded: boolean, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, saveConsumer: $Consumer$Type<($List$Type<(double)>)>, defaultValue: $Supplier$Type<($List$Type<(double)>)>, resetButtonKey: $Component$Type, requiresRestart: boolean, deleteButtonEnabled: boolean, insertInFront: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: $List$Type<(double)>, defaultExpanded: boolean, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, saveConsumer: $Consumer$Type<($List$Type<(double)>)>, defaultValue: $Supplier$Type<($List$Type<(double)>)>, resetButtonKey: $Component$Type, requiresRestart: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: $List$Type<(double)>, defaultExpanded: boolean, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, saveConsumer: $Consumer$Type<($List$Type<(double)>)>, defaultValue: $Supplier$Type<($List$Type<(double)>)>, resetButtonKey: $Component$Type)

public "self"(): $DoubleListListEntry
public "setMaximum"(maximum: double): $DoubleListListEntry
public "setMinimum"(minimum: double): $DoubleListListEntry
set "maximum"(value: double)
set "minimum"(value: double)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleListListEntry$Type = ($DoubleListListEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleListListEntry_ = $DoubleListListEntry$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$ValueToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $ValueToken extends $Token {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueToken$Type = ($ValueToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueToken_ = $ValueToken$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonElement" {
import {$JsonGrammar, $JsonGrammar$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonGrammar"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"

export class $JsonElement implements $Cloneable {

constructor()

public "toJson"(arg0: $JsonGrammar$Type, arg1: integer): string
public "toJson"(arg0: boolean, arg1: boolean, arg2: integer): string
public "toJson"(grammar: $JsonGrammar$Type): string
public "toJson"(comments: boolean, newlines: boolean): string
public "toJson"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonElement$Type = ($JsonElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonElement_ = $JsonElement$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$TypeDescription" {
import {$PropertyUtils, $PropertyUtils$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$PropertyUtils"
import {$Property, $Property$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$Property"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Node, $Node$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Node"
import {$PropertySubstitute, $PropertySubstitute$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$PropertySubstitute"
import {$Tag, $Tag$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Tag"

export class $TypeDescription {

constructor(clazz: $Class$Type<(any)>, impl: $Class$Type<(any)>)
constructor(clazz: $Class$Type<(any)>)
constructor(clazz: $Class$Type<(any)>, tag: string)
constructor(clazz: $Class$Type<(any)>, tag: $Tag$Type, impl: $Class$Type<(any)>)
constructor(clazz: $Class$Type<(any)>, tag: $Tag$Type)

public "getProperty"(name: string): $Property
public "toString"(): string
public "newInstance"(propertyName: string, node: $Node$Type): any
public "newInstance"(node: $Node$Type): any
public "setProperty"(targetBean: any, propertyName: string, value: any): boolean
public "getProperties"(): $Set<($Property)>
public "getType"(): $Class<(any)>
public "getTag"(): $Tag
public "addPropertyParameters"(pName: string, ...classes: ($Class$Type<(any)>)[]): void
/**
 * 
 * @deprecated
 */
public "putListPropertyType"(property: string, type: $Class$Type<(any)>): void
public "substituteProperty"(substitute: $PropertySubstitute$Type): void
public "substituteProperty"(pName: string, pType: $Class$Type<(any)>, getter: string, setter: string, ...argParams: ($Class$Type<(any)>)[]): void
public "setupPropertyType"(key: string, valueNode: $Node$Type): boolean
/**
 * 
 * @deprecated
 */
public "putMapPropertyType"(property: string, key: $Class$Type<(any)>, value: $Class$Type<(any)>): void
public "setIncludes"(...propNames: (string)[]): void
public "setPropertyUtils"(propertyUtils: $PropertyUtils$Type): void
public "finalizeConstruction"(obj: any): any
public "setExcludes"(...propNames: (string)[]): void
/**
 * 
 * @deprecated
 */
public "setTag"(tag: $Tag$Type): void
/**
 * 
 * @deprecated
 */
public "setTag"(tag: string): void
/**
 * 
 * @deprecated
 */
public "getListPropertyType"(property: string): $Class<(any)>
/**
 * 
 * @deprecated
 */
public "getMapValueType"(property: string): $Class<(any)>
/**
 * 
 * @deprecated
 */
public "getMapKeyType"(property: string): $Class<(any)>
get "properties"(): $Set<($Property)>
get "type"(): $Class<(any)>
get "tag"(): $Tag
set "includes"(value: (string)[])
set "propertyUtils"(value: $PropertyUtils$Type)
set "excludes"(value: (string)[])
set "tag"(value: $Tag$Type)
set "tag"(value: string)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeDescription$Type = ($TypeDescription);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeDescription_ = $TypeDescription$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg20" {
import {$RecordValueAnimatorArgs$Arg20$Up, $RecordValueAnimatorArgs$Arg20$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg20$Up"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"
import {$RecordValueAnimatorArgs$Arg20$Op, $RecordValueAnimatorArgs$Arg20$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg20$Op"

export class $RecordValueAnimatorArgs$Arg20<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, a12: $ValueAnimator$Type<(A12)>, a13: $ValueAnimator$Type<(A13)>, a14: $ValueAnimator$Type<(A14)>, a15: $ValueAnimator$Type<(A15)>, a16: $ValueAnimator$Type<(A16)>, a17: $ValueAnimator$Type<(A17)>, a18: $ValueAnimator$Type<(A18)>, a19: $ValueAnimator$Type<(A19)>, a20: $ValueAnimator$Type<(A20)>, op: $RecordValueAnimatorArgs$Arg20$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (A20), (T)>, up: $RecordValueAnimatorArgs$Arg20$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (A20), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg20$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, T> = ($RecordValueAnimatorArgs$Arg20<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (A20), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg20_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, T> = $RecordValueAnimatorArgs$Arg20$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (A20), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg15" {
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$RecordValueAnimatorArgs$Arg15$Up, $RecordValueAnimatorArgs$Arg15$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg15$Up"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimatorArgs$Arg15$Op, $RecordValueAnimatorArgs$Arg15$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg15$Op"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"

export class $RecordValueAnimatorArgs$Arg15<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, a12: $ValueAnimator$Type<(A12)>, a13: $ValueAnimator$Type<(A13)>, a14: $ValueAnimator$Type<(A14)>, a15: $ValueAnimator$Type<(A15)>, op: $RecordValueAnimatorArgs$Arg15$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (T)>, up: $RecordValueAnimatorArgs$Arg15$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg15$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, T> = ($RecordValueAnimatorArgs$Arg15<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg15_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, T> = $RecordValueAnimatorArgs$Arg15$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg14" {
import {$RecordValueAnimatorArgs$Arg14$Up, $RecordValueAnimatorArgs$Arg14$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg14$Up"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimatorArgs$Arg14$Op, $RecordValueAnimatorArgs$Arg14$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg14$Op"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"

export class $RecordValueAnimatorArgs$Arg14<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, a12: $ValueAnimator$Type<(A12)>, a13: $ValueAnimator$Type<(A13)>, a14: $ValueAnimator$Type<(A14)>, op: $RecordValueAnimatorArgs$Arg14$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (T)>, up: $RecordValueAnimatorArgs$Arg14$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg14$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, T> = ($RecordValueAnimatorArgs$Arg14<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg14_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, T> = $RecordValueAnimatorArgs$Arg14$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg13" {
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimatorArgs$Arg13$Up, $RecordValueAnimatorArgs$Arg13$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg13$Up"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"
import {$RecordValueAnimatorArgs$Arg13$Op, $RecordValueAnimatorArgs$Arg13$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg13$Op"

export class $RecordValueAnimatorArgs$Arg13<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, a12: $ValueAnimator$Type<(A12)>, a13: $ValueAnimator$Type<(A13)>, op: $RecordValueAnimatorArgs$Arg13$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (T)>, up: $RecordValueAnimatorArgs$Arg13$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg13$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, T> = ($RecordValueAnimatorArgs$Arg13<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg13_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, T> = $RecordValueAnimatorArgs$Arg13$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg12" {
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimatorArgs$Arg12$Up, $RecordValueAnimatorArgs$Arg12$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg12$Up"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"
import {$RecordValueAnimatorArgs$Arg12$Op, $RecordValueAnimatorArgs$Arg12$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg12$Op"

export class $RecordValueAnimatorArgs$Arg12<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, a12: $ValueAnimator$Type<(A12)>, op: $RecordValueAnimatorArgs$Arg12$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (T)>, up: $RecordValueAnimatorArgs$Arg12$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg12$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, T> = ($RecordValueAnimatorArgs$Arg12<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg12_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, T> = $RecordValueAnimatorArgs$Arg12$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg19" {
import {$RecordValueAnimatorArgs$Arg19$Up, $RecordValueAnimatorArgs$Arg19$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg19$Up"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimatorArgs$Arg19$Op, $RecordValueAnimatorArgs$Arg19$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg19$Op"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"

export class $RecordValueAnimatorArgs$Arg19<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, a12: $ValueAnimator$Type<(A12)>, a13: $ValueAnimator$Type<(A13)>, a14: $ValueAnimator$Type<(A14)>, a15: $ValueAnimator$Type<(A15)>, a16: $ValueAnimator$Type<(A16)>, a17: $ValueAnimator$Type<(A17)>, a18: $ValueAnimator$Type<(A18)>, a19: $ValueAnimator$Type<(A19)>, op: $RecordValueAnimatorArgs$Arg19$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (T)>, up: $RecordValueAnimatorArgs$Arg19$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg19$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, T> = ($RecordValueAnimatorArgs$Arg19<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg19_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, T> = $RecordValueAnimatorArgs$Arg19$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg18" {
import {$RecordValueAnimatorArgs$Arg18$Up, $RecordValueAnimatorArgs$Arg18$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg18$Up"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimatorArgs$Arg18$Op, $RecordValueAnimatorArgs$Arg18$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg18$Op"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"

export class $RecordValueAnimatorArgs$Arg18<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, a12: $ValueAnimator$Type<(A12)>, a13: $ValueAnimator$Type<(A13)>, a14: $ValueAnimator$Type<(A14)>, a15: $ValueAnimator$Type<(A15)>, a16: $ValueAnimator$Type<(A16)>, a17: $ValueAnimator$Type<(A17)>, a18: $ValueAnimator$Type<(A18)>, op: $RecordValueAnimatorArgs$Arg18$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (T)>, up: $RecordValueAnimatorArgs$Arg18$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg18$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, T> = ($RecordValueAnimatorArgs$Arg18<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg18_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, T> = $RecordValueAnimatorArgs$Arg18$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg17" {
import {$RecordValueAnimatorArgs$Arg17$Up, $RecordValueAnimatorArgs$Arg17$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg17$Up"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimatorArgs$Arg17$Op, $RecordValueAnimatorArgs$Arg17$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg17$Op"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"

export class $RecordValueAnimatorArgs$Arg17<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, a12: $ValueAnimator$Type<(A12)>, a13: $ValueAnimator$Type<(A13)>, a14: $ValueAnimator$Type<(A14)>, a15: $ValueAnimator$Type<(A15)>, a16: $ValueAnimator$Type<(A16)>, a17: $ValueAnimator$Type<(A17)>, op: $RecordValueAnimatorArgs$Arg17$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (T)>, up: $RecordValueAnimatorArgs$Arg17$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg17$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, T> = ($RecordValueAnimatorArgs$Arg17<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg17_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, T> = $RecordValueAnimatorArgs$Arg17$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg16" {
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$RecordValueAnimatorArgs$Arg16$Up, $RecordValueAnimatorArgs$Arg16$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg16$Up"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimatorArgs$Arg16$Op, $RecordValueAnimatorArgs$Arg16$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg16$Op"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"

export class $RecordValueAnimatorArgs$Arg16<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, a12: $ValueAnimator$Type<(A12)>, a13: $ValueAnimator$Type<(A13)>, a14: $ValueAnimator$Type<(A14)>, a15: $ValueAnimator$Type<(A15)>, a16: $ValueAnimator$Type<(A16)>, op: $RecordValueAnimatorArgs$Arg16$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (T)>, up: $RecordValueAnimatorArgs$Arg16$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg16$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, T> = ($RecordValueAnimatorArgs$Arg16<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg16_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, T> = $RecordValueAnimatorArgs$Arg16$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg11" {
import {$RecordValueAnimatorArgs$Arg11$Op, $RecordValueAnimatorArgs$Arg11$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg11$Op"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimatorArgs$Arg11$Up, $RecordValueAnimatorArgs$Arg11$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg11$Up"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"

export class $RecordValueAnimatorArgs$Arg11<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, op: $RecordValueAnimatorArgs$Arg11$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (T)>, up: $RecordValueAnimatorArgs$Arg11$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg11$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, T> = ($RecordValueAnimatorArgs$Arg11<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg11_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, T> = $RecordValueAnimatorArgs$Arg11$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg10" {
import {$RecordValueAnimatorArgs$Arg10$Up, $RecordValueAnimatorArgs$Arg10$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg10$Up"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimatorArgs$Arg10$Op, $RecordValueAnimatorArgs$Arg10$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg10$Op"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"

export class $RecordValueAnimatorArgs$Arg10<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, op: $RecordValueAnimatorArgs$Arg10$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (T)>, up: $RecordValueAnimatorArgs$Arg10$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg10$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, T> = ($RecordValueAnimatorArgs$Arg10<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg10_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, T> = $RecordValueAnimatorArgs$Arg10$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonObject" {
import {$Marshaller, $Marshaller$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/api/$Marshaller"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$JsonGrammar, $JsonGrammar$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonGrammar"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$JsonElement, $JsonElement$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonElement"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $JsonObject extends $JsonElement implements $Map<(string), ($JsonElement)> {

constructor()

public "remove"(key: any): $JsonElement
public "get"<E>(clazz: $Class$Type<(E)>, key: string): E
public "put"(key: string, elem: $JsonElement$Type): $JsonElement
public "put"(key: string, elem: $JsonElement$Type, comment: string): $JsonElement
public "equals"(other: any): boolean
public "toString"(): string
public "values"(): $Collection<($JsonElement)>
public "hashCode"(): integer
public "clone"(): $JsonObject
public "getBoolean"(key: string, defaultValue: boolean): boolean
public "getByte"(key: string, defaultValue: byte): byte
public "getShort"(key: string, defaultValue: short): short
public "getChar"(key: string, defaultValue: character): character
public "getInt"(key: string, defaultValue: integer): integer
public "getLong"(key: string, defaultValue: long): long
public "getFloat"(key: string, defaultValue: float): float
public "getDouble"(key: string, defaultValue: double): double
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "entrySet"(): $Set<($Map$Entry<(string), ($JsonElement)>)>
public "putAll"(map: $Map$Type<(any), (any)>): void
public "containsKey"(key: any): boolean
public "keySet"(): $Set<(string)>
public "containsValue"(val: any): boolean
public "getObject"(name: string): $JsonObject
public "setComment"(name: string, comment: string): void
public "getComment"(name: string): string
public "toJson"(grammar: $JsonGrammar$Type, depth: integer): string
public "toJson"(comments: boolean, newlines: boolean, depth: integer): string
public "recursiveGetOrCreate"<E extends $JsonElement>(clazz: $Class$Type<(E)>, key: string, fallback: E, comment: string): E
public "getDelta"(defaults: $JsonObject$Type): $JsonObject
public "putDefault"<T>(key: string, elem: T, clazz: $Class$Type<(any)>, comment: string): T
public "putDefault"<T>(key: string, elem: T, comment: string): T
public "putDefault"(key: string, elem: $JsonElement$Type, comment: string): $JsonElement
public "recursiveGet"<E>(clazz: $Class$Type<(E)>, key: string): E
public "getMarshaller"(): $Marshaller
public "setMarshaller"(marshaller: $Marshaller$Type): void
public "remove"(arg0: any, arg1: any): boolean
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(string), ($JsonElement)>
public "replace"(arg0: string, arg1: $JsonElement$Type): $JsonElement
public "replace"(arg0: string, arg1: $JsonElement$Type, arg2: $JsonElement$Type): boolean
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type, arg2: string, arg3: $JsonElement$Type, arg4: string, arg5: $JsonElement$Type, arg6: string, arg7: $JsonElement$Type, arg8: string, arg9: $JsonElement$Type): $Map<(string), ($JsonElement)>
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type, arg2: string, arg3: $JsonElement$Type, arg4: string, arg5: $JsonElement$Type, arg6: string, arg7: $JsonElement$Type): $Map<(string), ($JsonElement)>
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type, arg2: string, arg3: $JsonElement$Type, arg4: string, arg5: $JsonElement$Type): $Map<(string), ($JsonElement)>
public static "of"<K, V>(): $Map<(string), ($JsonElement)>
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type, arg2: string, arg3: $JsonElement$Type): $Map<(string), ($JsonElement)>
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type): $Map<(string), ($JsonElement)>
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type, arg2: string, arg3: $JsonElement$Type, arg4: string, arg5: $JsonElement$Type, arg6: string, arg7: $JsonElement$Type, arg8: string, arg9: $JsonElement$Type, arg10: string, arg11: $JsonElement$Type, arg12: string, arg13: $JsonElement$Type, arg14: string, arg15: $JsonElement$Type, arg16: string, arg17: $JsonElement$Type, arg18: string, arg19: $JsonElement$Type): $Map<(string), ($JsonElement)>
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type, arg2: string, arg3: $JsonElement$Type, arg4: string, arg5: $JsonElement$Type, arg6: string, arg7: $JsonElement$Type, arg8: string, arg9: $JsonElement$Type, arg10: string, arg11: $JsonElement$Type, arg12: string, arg13: $JsonElement$Type, arg14: string, arg15: $JsonElement$Type, arg16: string, arg17: $JsonElement$Type): $Map<(string), ($JsonElement)>
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type, arg2: string, arg3: $JsonElement$Type, arg4: string, arg5: $JsonElement$Type, arg6: string, arg7: $JsonElement$Type, arg8: string, arg9: $JsonElement$Type, arg10: string, arg11: $JsonElement$Type, arg12: string, arg13: $JsonElement$Type, arg14: string, arg15: $JsonElement$Type): $Map<(string), ($JsonElement)>
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type, arg2: string, arg3: $JsonElement$Type, arg4: string, arg5: $JsonElement$Type, arg6: string, arg7: $JsonElement$Type, arg8: string, arg9: $JsonElement$Type, arg10: string, arg11: $JsonElement$Type, arg12: string, arg13: $JsonElement$Type): $Map<(string), ($JsonElement)>
public static "of"<K, V>(arg0: string, arg1: $JsonElement$Type, arg2: string, arg3: $JsonElement$Type, arg4: string, arg5: $JsonElement$Type, arg6: string, arg7: $JsonElement$Type, arg8: string, arg9: $JsonElement$Type, arg10: string, arg11: $JsonElement$Type): $Map<(string), ($JsonElement)>
public "merge"(arg0: string, arg1: $JsonElement$Type, arg2: $BiFunction$Type<(any), (any), (any)>): $JsonElement
public "putIfAbsent"(arg0: string, arg1: $JsonElement$Type): $JsonElement
public "compute"(arg0: string, arg1: $BiFunction$Type<(any), (any), (any)>): $JsonElement
public static "entry"<K, V>(arg0: string, arg1: $JsonElement$Type): $Map$Entry<(string), ($JsonElement)>
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "computeIfAbsent"(arg0: string, arg1: $Function$Type<(any), (any)>): $JsonElement
public "getOrDefault"(arg0: any, arg1: $JsonElement$Type): $JsonElement
public "computeIfPresent"(arg0: string, arg1: $BiFunction$Type<(any), (any), (any)>): $JsonElement
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(string), ($JsonElement)>
get "empty"(): boolean
get "marshaller"(): $Marshaller
set "marshaller"(value: $Marshaller$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonObject$Type = ($JsonObject);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonObject_ = $JsonObject$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$NodeId" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $NodeId extends $Enum<($NodeId)> {
static readonly "scalar": $NodeId
static readonly "sequence": $NodeId
static readonly "mapping": $NodeId
static readonly "anchor": $NodeId


public static "values"(): ($NodeId)[]
public static "valueOf"(name: string): $NodeId
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NodeId$Type = (("sequence") | ("scalar") | ("mapping") | ("anchor")) | ($NodeId);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NodeId_ = $NodeId$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$LongListEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractNumberListEntry, $AbstractNumberListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractNumberListEntry"

export class $LongListEntry extends $AbstractNumberListEntry<(long)> {

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: long, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(long)>, saveConsumer: $Consumer$Type<(long)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, requiresRestart: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: long, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(long)>, saveConsumer: $Consumer$Type<(long)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: long, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(long)>, saveConsumer: $Consumer$Type<(long)>)

public "getValue"(): long
public "getError"(): $Optional<($Component)>
public "setMaximum"(maximum: long): $LongListEntry
public "setMinimum"(minimum: long): $LongListEntry
get "value"(): long
get "error"(): $Optional<($Component)>
set "maximum"(value: long)
set "minimum"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongListEntry$Type = ($LongListEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongListEntry_ = $LongListEntry$Type;
}}
declare module "packages/me/shedaniel/autoconfig/serializer/$ConfigSerializer" {
import {$ConfigData, $ConfigData$Type} from "packages/me/shedaniel/autoconfig/$ConfigData"

export interface $ConfigSerializer<T extends $ConfigData> {

 "deserialize"(): T
 "createDefault"(): T
 "serialize"(arg0: T): void
}

export namespace $ConfigSerializer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigSerializer$Type<T> = ($ConfigSerializer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigSerializer_<T> = $ConfigSerializer$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$ValueProvider" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ValueProvider<T> {

 "value"(): T
 "target"(): T
 "update"(arg0: double): void
 "completeImmediately"(): void
}

export namespace $ValueProvider {
function constant<T>(value: T): $ValueProvider<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueProvider$Type<T> = ($ValueProvider<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueProvider_<T> = $ValueProvider$Type<(T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$Jankson$Builder" {
import {$Marshaller, $Marshaller$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/api/$Marshaller"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$DeserializerFunction, $DeserializerFunction$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/api/$DeserializerFunction"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$JsonElement, $JsonElement$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonElement"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$JsonObject, $JsonObject$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonObject"
import {$Jankson, $Jankson$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$Jankson"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"

export class $Jankson$Builder {

constructor()

public "build"(): $Jankson
public "registerTypeFactory"<T>(clazz: $Class$Type<(T)>, factory: $Supplier$Type<(T)>): $Jankson$Builder
public "registerDeserializer"<A, B>(sourceClass: $Class$Type<(A)>, targetClass: $Class$Type<(B)>, arg2: $DeserializerFunction$Type<(A), (B)>): $Jankson$Builder
/**
 * 
 * @deprecated
 */
public "registerPrimitiveTypeAdapter"<T>(clazz: $Class$Type<(T)>, adapter: $Function$Type<(any), (T)>): $Jankson$Builder
/**
 * 
 * @deprecated
 */
public "registerTypeAdapter"<T>(clazz: $Class$Type<(T)>, adapter: $Function$Type<($JsonObject$Type), (T)>): $Jankson$Builder
public "registerSerializer"<T>(clazz: $Class$Type<(T)>, serializer: $BiFunction$Type<(T), ($Marshaller$Type), ($JsonElement$Type)>): $Jankson$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Jankson$Builder$Type = ($Jankson$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Jankson$Builder_ = $Jankson$Builder$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$IndentationPolicy" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $IndentationPolicy {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IndentationPolicy$Type = ($IndentationPolicy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IndentationPolicy_ = $IndentationPolicy$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/representer/$BaseRepresenter" {
import {$PropertyUtils, $PropertyUtils$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$PropertyUtils"
import {$DumperOptions$ScalarStyle, $DumperOptions$ScalarStyle$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$ScalarStyle"
import {$DumperOptions$FlowStyle, $DumperOptions$FlowStyle$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle"
import {$Node, $Node$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Node"

export class $BaseRepresenter {

constructor()

public "getPropertyUtils"(): $PropertyUtils
public "setPropertyUtils"(propertyUtils: $PropertyUtils$Type): void
public "represent"(data: any): $Node
public "setDefaultScalarStyle"(defaultStyle: $DumperOptions$ScalarStyle$Type): void
public "getDefaultFlowStyle"(): $DumperOptions$FlowStyle
public "getDefaultScalarStyle"(): $DumperOptions$ScalarStyle
public "isExplicitPropertyUtils"(): boolean
public "setDefaultFlowStyle"(defaultFlowStyle: $DumperOptions$FlowStyle$Type): void
get "propertyUtils"(): $PropertyUtils
set "propertyUtils"(value: $PropertyUtils$Type)
set "defaultScalarStyle"(value: $DumperOptions$ScalarStyle$Type)
get "defaultFlowStyle"(): $DumperOptions$FlowStyle
get "defaultScalarStyle"(): $DumperOptions$ScalarStyle
get "explicitPropertyUtils"(): boolean
set "defaultFlowStyle"(value: $DumperOptions$FlowStyle$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseRepresenter$Type = ($BaseRepresenter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseRepresenter_ = $BaseRepresenter$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$AliasToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $AliasToken extends $Token {

constructor(value: string, startMark: $Mark$Type, endMark: $Mark$Type)

public "getValue"(): string
public "getTokenId"(): $Token$ID
get "value"(): string
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AliasToken$Type = ($AliasToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AliasToken_ = $AliasToken$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/emitter/$EmitterException" {
import {$YAMLException, $YAMLException$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$YAMLException"

export class $EmitterException extends $YAMLException {

constructor(msg: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmitterException$Type = ($EmitterException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmitterException_ = $EmitterException$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$Yaml" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$TypeDescription, $TypeDescription$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$TypeDescription"
import {$DumperOptions$FlowStyle, $DumperOptions$FlowStyle$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle"
import {$Node, $Node$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Node"
import {$Representer, $Representer$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/representer/$Representer"
import {$Tag, $Tag$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Tag"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$Resolver, $Resolver$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/resolver/$Resolver"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"
import {$DumperOptions, $DumperOptions$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions"
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$BaseConstructor, $BaseConstructor$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$BaseConstructor"
import {$LoaderOptions, $LoaderOptions$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$LoaderOptions"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"
import {$Event, $Event$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event"
import {$BeanAccess, $BeanAccess$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$BeanAccess"

export class $Yaml {

constructor(arg0: $BaseConstructor$Type, representer: $Representer$Type, dumperOptions: $DumperOptions$Type, loadingConfig: $LoaderOptions$Type)
constructor(arg0: $BaseConstructor$Type, representer: $Representer$Type, dumperOptions: $DumperOptions$Type)
constructor(representer: $Representer$Type, dumperOptions: $DumperOptions$Type)
constructor(arg0: $BaseConstructor$Type, representer: $Representer$Type, dumperOptions: $DumperOptions$Type, resolver: $Resolver$Type)
constructor(arg0: $BaseConstructor$Type, representer: $Representer$Type, dumperOptions: $DumperOptions$Type, loadingConfig: $LoaderOptions$Type, resolver: $Resolver$Type)
constructor()
constructor(loadingConfig: $LoaderOptions$Type)
constructor(representer: $Representer$Type)
constructor(arg0: $BaseConstructor$Type)
constructor(arg0: $BaseConstructor$Type, representer: $Representer$Type)
constructor(dumperOptions: $DumperOptions$Type)

public "getName"(): string
public "toString"(): string
public "load"<T>(yaml: string): T
public "load"<T>(io: $Reader$Type): T
public "load"<T>(io: $InputStream$Type): T
public "setName"(name: string): void
public "parse"(yaml: $Reader$Type): $Iterable<($Event)>
public "compose"(yaml: $Reader$Type): $Node
public "loadAll"(yaml: $Reader$Type): $Iterable<(any)>
public "loadAll"(yaml: string): $Iterable<(any)>
public "loadAll"(yaml: $InputStream$Type): $Iterable<(any)>
public "loadAs"<T>(yaml: string, type: $Class$Type<(T)>): T
public "loadAs"<T>(input: $InputStream$Type, type: $Class$Type<(T)>): T
public "loadAs"<T>(io: $Reader$Type, type: $Class$Type<(T)>): T
public "dumpAll"(data: $Iterator$Type<(any)>): string
public "dumpAll"(data: $Iterator$Type<(any)>, output: $Writer$Type): void
public "dumpAs"(data: any, rootTag: $Tag$Type, flowStyle: $DumperOptions$FlowStyle$Type): string
public "represent"(data: any): $Node
public "dumpAsMap"(data: any): string
public "addTypeDescription"(td: $TypeDescription$Type): void
public "composeAll"(yaml: $Reader$Type): $Iterable<($Node)>
public "setBeanAccess"(beanAccess: $BeanAccess$Type): void
public "serialize"(node: $Node$Type, output: $Writer$Type): void
public "serialize"(data: $Node$Type): $List<($Event)>
public "addImplicitResolver"(tag: $Tag$Type, regexp: $Pattern$Type, first: string): void
public "dump"(data: any): string
public "dump"(data: any, output: $Writer$Type): void
get "name"(): string
set "name"(value: string)
set "beanAccess"(value: $BeanAccess$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Yaml$Type = ($Yaml);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Yaml_ = $Yaml$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg17$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg17$Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, T> {

 "construct"(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11, arg11: A12, arg12: A13, arg13: A14, arg14: A15, arg15: A16, arg16: A17): T

(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11, arg11: A12, arg12: A13, arg13: A14, arg14: A15, arg15: A16, arg16: A17): T
}

export namespace $RecordValueAnimatorArgs$Arg17$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg17$Op$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, T> = ($RecordValueAnimatorArgs$Arg17$Op<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg17$Op_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, T> = $RecordValueAnimatorArgs$Arg17$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg17$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg17$Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>, arg12: $RecordValueAnimatorArgs$Setter$Type<(A12)>, arg13: $RecordValueAnimatorArgs$Setter$Type<(A13)>, arg14: $RecordValueAnimatorArgs$Setter$Type<(A14)>, arg15: $RecordValueAnimatorArgs$Setter$Type<(A15)>, arg16: $RecordValueAnimatorArgs$Setter$Type<(A16)>, arg17: $RecordValueAnimatorArgs$Setter$Type<(A17)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>, arg12: $RecordValueAnimatorArgs$Setter$Type<(A12)>, arg13: $RecordValueAnimatorArgs$Setter$Type<(A13)>, arg14: $RecordValueAnimatorArgs$Setter$Type<(A14)>, arg15: $RecordValueAnimatorArgs$Setter$Type<(A15)>, arg16: $RecordValueAnimatorArgs$Setter$Type<(A16)>, arg17: $RecordValueAnimatorArgs$Setter$Type<(A17)>): void
}

export namespace $RecordValueAnimatorArgs$Arg17$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg17$Up$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, T> = ($RecordValueAnimatorArgs$Arg17$Up<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg17$Up_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, T> = $RecordValueAnimatorArgs$Arg17$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/env/$EnvScalarConstructor" {
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$Constructor, $Constructor$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$Constructor"
import {$Tag, $Tag$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Tag"
import {$SafeConstructor$ConstructUndefined, $SafeConstructor$ConstructUndefined$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$SafeConstructor$ConstructUndefined"

export class $EnvScalarConstructor extends $Constructor {
static readonly "ENV_TAG": $Tag
static readonly "ENV_FORMAT": $Pattern
static readonly "undefinedConstructor": $SafeConstructor$ConstructUndefined

constructor()

public "apply"(name: string, separator: string, value: string, environment: string): string
public "getEnv"(key: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnvScalarConstructor$Type = ($EnvScalarConstructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnvScalarConstructor_ = $EnvScalarConstructor$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$EnumListEntry" {
import {$SelectionListEntry, $SelectionListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$SelectionListEntry"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $EnumListEntry<T extends $Enum<(any)>> extends $SelectionListEntry<(T)> {
static readonly "DEFAULT_NAME_PROVIDER": $Function<($Enum), ($Component)>

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, clazz: $Class$Type<(T)>, value: T, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(T)>, saveConsumer: $Consumer$Type<(T)>, enumNameProvider: $Function$Type<($Enum$Type), ($Component$Type)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, requiresRestart: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, clazz: $Class$Type<(T)>, value: T, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(T)>, saveConsumer: $Consumer$Type<(T)>, enumNameProvider: $Function$Type<($Enum$Type), ($Component$Type)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, clazz: $Class$Type<(T)>, value: T, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(T)>, saveConsumer: $Consumer$Type<(T)>, enumNameProvider: $Function$Type<($Enum$Type), ($Component$Type)>)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, clazz: $Class$Type<(T)>, value: T, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(T)>, saveConsumer: $Consumer$Type<(T)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumListEntry$Type<T> = ($EnumListEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumListEntry_<T> = $EnumListEntry$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractNumberListEntry" {
import {$TextFieldListEntry, $TextFieldListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$TextFieldListEntry"

export class $AbstractNumberListEntry<T> extends $TextFieldListEntry<(T)> {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractNumberListEntry$Type<T> = ($AbstractNumberListEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractNumberListEntry_<T> = $AbstractNumberListEntry$Type<(T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/util/$UriEncoder" {
import {$ByteBuffer, $ByteBuffer$Type} from "packages/java/nio/$ByteBuffer"

export class $UriEncoder {

constructor()

public static "decode"(buff: string): string
public static "decode"(buff: $ByteBuffer$Type): string
public static "encode"(uri: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UriEncoder$Type = ($UriEncoder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UriEncoder_ = $UriEncoder$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonNull" {
import {$JsonGrammar, $JsonGrammar$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonGrammar"
import {$JsonElement, $JsonElement$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonElement"

export class $JsonNull extends $JsonElement {
static readonly "INSTANCE": $JsonNull


public "equals"(other: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "toJson"(comments: boolean, newlines: boolean, depth: integer): string
public "toJson"(grammar: $JsonGrammar$Type, depth: integer): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonNull$Type = ($JsonNull);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonNull_ = $JsonNull$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/serializer/$DeserializerFunctionPool" {
import {$Marshaller, $Marshaller$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/api/$Marshaller"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$JsonElement, $JsonElement$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonElement"
import {$InternalDeserializerFunction, $InternalDeserializerFunction$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/serializer/$InternalDeserializerFunction"

export class $DeserializerFunctionPool<B> {

constructor(targetClass: $Class$Type<(B)>)

public "apply"(elem: $JsonElement$Type, marshaller: $Marshaller$Type): B
public "getFunction"(sourceClass: $Class$Type<(any)>): $InternalDeserializerFunction<(B)>
public "registerUnsafe"(sourceClass: $Class$Type<(any)>, arg1: $InternalDeserializerFunction$Type<(B)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeserializerFunctionPool$Type<B> = ($DeserializerFunctionPool<(B)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeserializerFunctionPool_<B> = $DeserializerFunctionPool$Type<(B)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$TagToken" {
import {$TagTuple, $TagTuple$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$TagTuple"
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $TagToken extends $Token {

constructor(value: $TagTuple$Type, startMark: $Mark$Type, endMark: $Mark$Type)

public "getValue"(): $TagTuple
public "getTokenId"(): $Token$ID
get "value"(): $TagTuple
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TagToken$Type = ($TagToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TagToken_ = $TagToken$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractListListEntry$AbstractListCell" {
import {$AbstractListListEntry, $AbstractListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractListListEntry"
import {$BaseListCell, $BaseListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$BaseListCell"

export class $AbstractListListEntry$AbstractListCell<T, SELF extends $AbstractListListEntry$AbstractListCell<(T), (SELF), (OUTER_SELF)>, OUTER_SELF extends $AbstractListListEntry<(T), (SELF), (OUTER_SELF)>> extends $BaseListCell {

constructor(value: T, listListEntry: OUTER_SELF)

public "getValue"(): T
get "value"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractListListEntry$AbstractListCell$Type<T, SELF, OUTER_SELF> = ($AbstractListListEntry$AbstractListCell<(T), (SELF), (OUTER_SELF)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractListListEntry$AbstractListCell_<T, SELF, OUTER_SELF> = $AbstractListListEntry$AbstractListCell$Type<(T), (SELF), (OUTER_SELF)>;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$LongListListEntry$LongListCell" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$AbstractTextFieldListListEntry$AbstractTextFieldListCell, $AbstractTextFieldListListEntry$AbstractTextFieldListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractTextFieldListListEntry$AbstractTextFieldListCell"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$LongListListEntry, $LongListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$LongListListEntry"

export class $LongListListEntry$LongListCell extends $AbstractTextFieldListListEntry$AbstractTextFieldListCell<(long), ($LongListListEntry$LongListCell), ($LongListListEntry)> {

constructor(value: long, listListEntry: $LongListListEntry$Type)

public "getValue"(): long
public "getError"(): $Optional<($Component)>
get "value"(): long
get "error"(): $Optional<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongListListEntry$LongListCell$Type = ($LongListListEntry$LongListCell);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongListListEntry$LongListCell_ = $LongListListEntry$LongListCell$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/annotation/$Deserializer" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $Deserializer extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $Deserializer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Deserializer$Type = ($Deserializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Deserializer_ = $Deserializer$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg15$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg15$Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, T> {

 "construct"(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11, arg11: A12, arg12: A13, arg13: A14, arg14: A15): T

(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11, arg11: A12, arg12: A13, arg13: A14, arg14: A15): T
}

export namespace $RecordValueAnimatorArgs$Arg15$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg15$Op$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, T> = ($RecordValueAnimatorArgs$Arg15$Op<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg15$Op_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, T> = $RecordValueAnimatorArgs$Arg15$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"

export interface $RecordValueAnimator$Arg<T> {

 "value"(): T
 "target"(): T
 "set"(arg0: T, arg1: long): void
 "setTarget"(arg0: T): void
 "dependencies"(): $List<($ValueAnimator<(any)>)>
}

export namespace $RecordValueAnimator$Arg {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimator$Arg$Type<T> = ($RecordValueAnimator$Arg<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimator$Arg_<T> = $RecordValueAnimator$Arg$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg15$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg15$Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>, arg12: $RecordValueAnimatorArgs$Setter$Type<(A12)>, arg13: $RecordValueAnimatorArgs$Setter$Type<(A13)>, arg14: $RecordValueAnimatorArgs$Setter$Type<(A14)>, arg15: $RecordValueAnimatorArgs$Setter$Type<(A15)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>, arg12: $RecordValueAnimatorArgs$Setter$Type<(A12)>, arg13: $RecordValueAnimatorArgs$Setter$Type<(A13)>, arg14: $RecordValueAnimatorArgs$Setter$Type<(A14)>, arg15: $RecordValueAnimatorArgs$Setter$Type<(A15)>): void
}

export namespace $RecordValueAnimatorArgs$Arg15$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg15$Up$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, T> = ($RecordValueAnimatorArgs$Arg15$Up<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg15$Up_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, T> = $RecordValueAnimatorArgs$Arg15$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$LongSliderBuilder" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractSliderFieldBuilder, $AbstractSliderFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractSliderFieldBuilder"
import {$LongSliderEntry, $LongSliderEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$LongSliderEntry"

export class $LongSliderBuilder extends $AbstractSliderFieldBuilder<(long), ($LongSliderEntry), ($LongSliderBuilder)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, value: long, min: long, max: long)

public "setTooltipSupplier"(tooltipSupplier: $Function$Type<(long), ($Optional$Type<(($Component$Type)[])>)>): $LongSliderBuilder
public "setTooltipSupplier"(tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>): $LongSliderBuilder
public "setTooltip"(...tooltip: ($Component$Type)[]): $LongSliderBuilder
public "setDefaultValue"(defaultValue: long): $LongSliderBuilder
public "setDefaultValue"(defaultValue: $Supplier$Type<(long)>): $LongSliderBuilder
set "tooltipSupplier"(value: $Function$Type<(long), ($Optional$Type<(($Component$Type)[])>)>)
set "tooltipSupplier"(value: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
set "tooltip"(value: ($Component$Type)[])
set "defaultValue"(value: long)
set "defaultValue"(value: $Supplier$Type<(long)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongSliderBuilder$Type = ($LongSliderBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongSliderBuilder_ = $LongSliderBuilder$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractTextFieldListListEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractTextFieldListListEntry$AbstractTextFieldListCell, $AbstractTextFieldListListEntry$AbstractTextFieldListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractTextFieldListListEntry$AbstractTextFieldListCell"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AbstractListListEntry, $AbstractListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractListListEntry"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"

export class $AbstractTextFieldListListEntry<T, C extends $AbstractTextFieldListListEntry$AbstractTextFieldListCell<(T), (C), (SELF)>, SELF extends $AbstractTextFieldListListEntry<(T), (C), (SELF)>> extends $AbstractListListEntry<(T), (C), (SELF)> {

constructor(fieldName: $Component$Type, value: $List$Type<(T)>, defaultExpanded: boolean, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, saveConsumer: $Consumer$Type<($List$Type<(T)>)>, defaultValue: $Supplier$Type<($List$Type<(T)>)>, resetButtonKey: $Component$Type, requiresRestart: boolean, deleteButtonEnabled: boolean, insertInFront: boolean, createNewCell: $BiFunction$Type<(T), (SELF), (C)>)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractTextFieldListListEntry$Type<T, C, SELF> = ($AbstractTextFieldListListEntry<(T), (C), (SELF)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractTextFieldListListEntry_<T, C, SELF> = $AbstractTextFieldListListEntry$Type<(T), (C), (SELF)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$ConstructorException" {
import {$MarkedYAMLException, $MarkedYAMLException$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$MarkedYAMLException"

export class $ConstructorException extends $MarkedYAMLException {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConstructorException$Type = ($ConstructorException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConstructorException_ = $ConstructorException$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/$ClothConfigInitializer" {
import {$EasingMethod, $EasingMethod$Type} from "packages/me/shedaniel/clothconfig2/impl/$EasingMethod"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $ClothConfigInitializer {
static readonly "LOGGER": $Logger
static readonly "MOD_ID": string

constructor()

public static "getScrollStep"(): double
/**
 * 
 * @deprecated
 */
public static "clamp"(v: double, maxScroll: double, clampExtension: double): double
/**
 * 
 * @deprecated
 */
public static "clamp"(v: double, maxScroll: double): double
public static "getScrollDuration"(): long
/**
 * 
 * @deprecated
 */
public static "handleScrollingPosition"(target: (double)[], scroll: double, maxScroll: double, delta: float, start: double, duration: double): double
public static "getBounceBackMultiplier"(): double
public static "getEasingMethod"(): $EasingMethod
/**
 * 
 * @deprecated
 */
public static "expoEase"(start: double, end: double, amount: double): double
get "scrollStep"(): double
get "scrollDuration"(): long
get "bounceBackMultiplier"(): double
get "easingMethod"(): $EasingMethod
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClothConfigInitializer$Type = ($ClothConfigInitializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClothConfigInitializer_ = $ClothConfigInitializer$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$SelectionCellElement" {
import {$DropdownBoxEntry, $DropdownBoxEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$AbstractContainerEventHandler, $AbstractContainerEventHandler$Type} from "packages/net/minecraft/client/gui/components/events/$AbstractContainerEventHandler"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $DropdownBoxEntry$SelectionCellElement<R> extends $AbstractContainerEventHandler {

constructor()

public "getEntry"(): $DropdownBoxEntry<(R)>
public "getSelection"(): R
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: float): void
public "dontRender"(arg0: $GuiGraphics$Type, arg1: float): void
public "getSearchKey"(): $Component
get "entry"(): $DropdownBoxEntry<(R)>
get "selection"(): R
get "searchKey"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropdownBoxEntry$SelectionCellElement$Type<R> = ($DropdownBoxEntry$SelectionCellElement<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropdownBoxEntry$SelectionCellElement_<R> = $DropdownBoxEntry$SelectionCellElement$Type<(R)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Node" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Tag, $Tag$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Tag"
import {$NodeId, $NodeId$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$NodeId"

export class $Node {

constructor(tag: $Tag$Type, startMark: $Mark$Type, endMark: $Mark$Type)

public "equals"(obj: any): boolean
public "hashCode"(): integer
public "getType"(): $Class<(any)>
/**
 * 
 * @deprecated
 */
public "isResolved"(): boolean
public "getTag"(): $Tag
public "setType"(type: $Class$Type<(any)>): void
public "getAnchor"(): string
public "getStartMark"(): $Mark
public "getNodeId"(): $NodeId
public "getEndMark"(): $Mark
public "setAnchor"(anchor: string): void
public "isTwoStepsConstruction"(): boolean
public "useClassConstructor"(): boolean
public "setUseClassConstructor"(useClassConstructor: boolean): void
public "setTwoStepsConstruction"(twoStepsConstruction: boolean): void
public "setTag"(tag: $Tag$Type): void
get "type"(): $Class<(any)>
get "resolved"(): boolean
get "tag"(): $Tag
set "type"(value: $Class$Type<(any)>)
get "anchor"(): string
get "startMark"(): $Mark
get "nodeId"(): $NodeId
get "endMark"(): $Mark
set "anchor"(value: string)
get "twoStepsConstruction"(): boolean
set "twoStepsConstruction"(value: boolean)
set "tag"(value: $Tag$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Node$Type = ($Node);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Node_ = $Node$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$SelectionCellCreator" {
import {$DropdownBoxEntry$SelectionCellElement, $DropdownBoxEntry$SelectionCellElement$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$SelectionCellElement"

export class $DropdownBoxEntry$SelectionCellCreator<R> {

constructor()

public "create"(arg0: R): $DropdownBoxEntry$SelectionCellElement<(R)>
public "getCellHeight"(): integer
public "getCellWidth"(): integer
public "getDropBoxMaxHeight"(): integer
get "cellHeight"(): integer
get "cellWidth"(): integer
get "dropBoxMaxHeight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropdownBoxEntry$SelectionCellCreator$Type<R> = ($DropdownBoxEntry$SelectionCellCreator<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropdownBoxEntry$SelectionCellCreator_<R> = $DropdownBoxEntry$SelectionCellCreator$Type<(R)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$NodeTuple" {
import {$Node, $Node$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Node"

export class $NodeTuple {

constructor(keyNode: $Node$Type, valueNode: $Node$Type)

public "toString"(): string
public "getKeyNode"(): $Node
public "getValueNode"(): $Node
get "keyNode"(): $Node
get "valueNode"(): $Node
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NodeTuple$Type = ($NodeTuple);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NodeTuple_ = $NodeTuple$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$MultiElementListEntry" {
import {$Expandable, $Expandable$Type} from "packages/me/shedaniel/clothconfig2/api/$Expandable"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipListEntry, $TooltipListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$TooltipListEntry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"

export class $MultiElementListEntry<T> extends $TooltipListEntry<(T)> implements $Expandable {

constructor(categoryName: $Component$Type, object: T, entries: $List$Type<($AbstractConfigListEntry$Type<(any)>)>, defaultExpanded: boolean)

public "getValue"(): T
public "save"(): void
public "getDefaultValue"(): $Optional<(T)>
public "setRequiresRestart"(requiresRestart: boolean): void
public "setExpanded"(expanded: boolean): void
public "getError"(): $Optional<($Component)>
public "render"(graphics: $GuiGraphics$Type, index: integer, y: integer, x: integer, entryWidth: integer, entryHeight: integer, mouseX: integer, mouseY: integer, isHovered: boolean, delta: float): void
public "children"(): $List<(any)>
public "narratables"(): $List<(any)>
public "isRequiresRestart"(): boolean
public "isEdited"(): boolean
public "getSearchTags"(): $Iterator<(string)>
public "updateSelected"(isSelected: boolean): void
public "getEntryArea"(x: integer, y: integer, entryWidth: integer, entryHeight: integer): $Rectangle
public "getMorePossibleHeight"(): integer
public "lateRender"(graphics: $GuiGraphics$Type, mouseX: integer, mouseY: integer, delta: float): void
public "getCategoryName"(): $Component
public "mouseClicked"(mouseX: double, mouseY: double, button: integer): boolean
public "getItemHeight"(): integer
public "isExpanded"(): boolean
public "getInitialReferenceOffset"(): integer
get "value"(): T
get "defaultValue"(): $Optional<(T)>
set "requiresRestart"(value: boolean)
set "expanded"(value: boolean)
get "error"(): $Optional<($Component)>
get "requiresRestart"(): boolean
get "edited"(): boolean
get "searchTags"(): $Iterator<(string)>
get "morePossibleHeight"(): integer
get "categoryName"(): $Component
get "itemHeight"(): integer
get "expanded"(): boolean
get "initialReferenceOffset"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultiElementListEntry$Type<T> = ($MultiElementListEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultiElementListEntry_<T> = $MultiElementListEntry$Type<(T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueReader" {
import {$Context, $Context$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$Context"
import {$AtomicInteger, $AtomicInteger$Type} from "packages/java/util/concurrent/atomic/$AtomicInteger"

export interface $ValueReader {

 "read"(arg0: string, arg1: $AtomicInteger$Type, arg2: $Context$Type): any
 "canRead"(arg0: string): boolean
}

export namespace $ValueReader {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueReader$Type = ($ValueReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueReader_ = $ValueReader$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$Version" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DumperOptions$Version extends $Enum<($DumperOptions$Version)> {
static readonly "V1_0": $DumperOptions$Version
static readonly "V1_1": $DumperOptions$Version


public "toString"(): string
public static "values"(): ($DumperOptions$Version)[]
public static "valueOf"(name: string): $DumperOptions$Version
public "major"(): integer
public "minor"(): integer
public "getRepresentation"(): string
get "representation"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DumperOptions$Version$Type = (("v1_0") | ("v1_1")) | ($DumperOptions$Version);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DumperOptions$Version_ = $DumperOptions$Version$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$StringListListEntry$StringListCell" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$StringListListEntry, $StringListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$StringListListEntry"
import {$AbstractTextFieldListListEntry$AbstractTextFieldListCell, $AbstractTextFieldListListEntry$AbstractTextFieldListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractTextFieldListListEntry$AbstractTextFieldListCell"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export class $StringListListEntry$StringListCell extends $AbstractTextFieldListListEntry$AbstractTextFieldListCell<(string), ($StringListListEntry$StringListCell), ($StringListListEntry)> {

constructor(value: string, listListEntry: $StringListListEntry$Type)

public "getError"(): $Optional<($Component)>
get "error"(): $Optional<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringListListEntry$StringListCell$Type = ($StringListListEntry$StringListCell);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringListListEntry$StringListCell_ = $StringListListEntry$StringListCell$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$SequenceStartEvent" {
import {$Event$ID, $Event$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$CollectionStartEvent, $CollectionStartEvent$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$CollectionStartEvent"
import {$DumperOptions$FlowStyle, $DumperOptions$FlowStyle$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $SequenceStartEvent extends $CollectionStartEvent {

constructor(anchor: string, tag: string, implicit: boolean, startMark: $Mark$Type, endMark: $Mark$Type, flowStyle: $DumperOptions$FlowStyle$Type)
/**
 * 
 * @deprecated
 */
constructor(anchor: string, tag: string, implicit: boolean, startMark: $Mark$Type, endMark: $Mark$Type, flowStyle: boolean)

public "getEventId"(): $Event$ID
get "eventId"(): $Event$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SequenceStartEvent$Type = ($SequenceStartEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SequenceStartEvent_ = $SequenceStartEvent$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$DoubleListBuilder" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$DoubleListListEntry, $DoubleListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DoubleListListEntry"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractRangeListBuilder, $AbstractRangeListBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractRangeListBuilder"
import {$DoubleListListEntry$DoubleListCell, $DoubleListListEntry$DoubleListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DoubleListListEntry$DoubleListCell"

export class $DoubleListBuilder extends $AbstractRangeListBuilder<(double), ($DoubleListListEntry), ($DoubleListBuilder)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, value: $List$Type<(double)>)

public "setMin"(min: double): $DoubleListBuilder
public "setTooltip"(...tooltip: ($Component$Type)[]): $DoubleListBuilder
public "setTooltip"(tooltip: $Optional$Type<(($Component$Type)[])>): $DoubleListBuilder
public "setErrorSupplier"(errorSupplier: $Function$Type<($List$Type<(double)>), ($Optional$Type<($Component$Type)>)>): $DoubleListBuilder
public "getCellErrorSupplier"(): $Function<(double), ($Optional<($Component)>)>
public "setCellErrorSupplier"(cellErrorSupplier: $Function$Type<(double), ($Optional$Type<($Component$Type)>)>): $DoubleListBuilder
public "setDeleteButtonEnabled"(deleteButtonEnabled: boolean): $DoubleListBuilder
public "setCreateNewInstance"(createNewInstance: $Function$Type<($DoubleListListEntry$Type), ($DoubleListListEntry$DoubleListCell$Type)>): $DoubleListBuilder
public "setDefaultValue"(defaultValue: $List$Type<(double)>): $DoubleListBuilder
public "setDefaultValue"(defaultValue: $Supplier$Type<($List$Type<(double)>)>): $DoubleListBuilder
public "setMax"(max: double): $DoubleListBuilder
public "setInsertInFront"(insertInFront: boolean): $DoubleListBuilder
public "removeMin"(): $DoubleListBuilder
public "removeMax"(): $DoubleListBuilder
public "setRemoveButtonTooltip"(removeTooltip: $Component$Type): $DoubleListBuilder
set "min"(value: double)
set "tooltip"(value: ($Component$Type)[])
set "tooltip"(value: $Optional$Type<(($Component$Type)[])>)
set "errorSupplier"(value: $Function$Type<($List$Type<(double)>), ($Optional$Type<($Component$Type)>)>)
get "cellErrorSupplier"(): $Function<(double), ($Optional<($Component)>)>
set "cellErrorSupplier"(value: $Function$Type<(double), ($Optional$Type<($Component$Type)>)>)
set "deleteButtonEnabled"(value: boolean)
set "createNewInstance"(value: $Function$Type<($DoubleListListEntry$Type), ($DoubleListListEntry$DoubleListCell$Type)>)
set "defaultValue"(value: $List$Type<(double)>)
set "defaultValue"(value: $Supplier$Type<($List$Type<(double)>)>)
set "max"(value: double)
set "insertInFront"(value: boolean)
set "removeButtonTooltip"(value: $Component$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleListBuilder$Type = ($DoubleListBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleListBuilder_ = $DoubleListBuilder$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$Results" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Results {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Results$Type = ($Results);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Results_ = $Results$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractListBuilder" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$AbstractFieldBuilder, $AbstractFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractFieldBuilder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"

export class $AbstractListBuilder<T, A extends $AbstractConfigListEntry<(any)>, SELF extends $AbstractListBuilder<(T), (A), (SELF)>> extends $AbstractFieldBuilder<($List<(T)>), (A), (SELF)> {


public "setExpanded"(expanded: boolean): SELF
public "getCellErrorSupplier"(): $Function<(T), ($Optional<($Component)>)>
public "setCellErrorSupplier"(cellErrorSupplier: $Function$Type<(T), ($Optional$Type<($Component$Type)>)>): SELF
public "isInsertButtonEnabled"(): boolean
public "setDeleteButtonEnabled"(deleteButtonEnabled: boolean): SELF
public "isDeleteButtonEnabled"(): boolean
public "setInsertButtonEnabled"(insertButtonEnabled: boolean): SELF
public "isExpanded"(): boolean
public "getRemoveTooltip"(): $Component
public "getAddTooltip"(): $Component
public "setInsertInFront"(insertInFront: boolean): SELF
public "isInsertInFront"(): boolean
public "setAddButtonTooltip"(addTooltip: $Component$Type): SELF
public "setRemoveButtonTooltip"(removeTooltip: $Component$Type): SELF
set "expanded"(value: boolean)
get "cellErrorSupplier"(): $Function<(T), ($Optional<($Component)>)>
set "cellErrorSupplier"(value: $Function$Type<(T), ($Optional$Type<($Component$Type)>)>)
get "insertButtonEnabled"(): boolean
set "deleteButtonEnabled"(value: boolean)
get "deleteButtonEnabled"(): boolean
set "insertButtonEnabled"(value: boolean)
get "expanded"(): boolean
get "removeTooltip"(): $Component
get "addTooltip"(): $Component
set "insertInFront"(value: boolean)
get "insertInFront"(): boolean
set "addButtonTooltip"(value: $Component$Type)
set "removeButtonTooltip"(value: $Component$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractListBuilder$Type<T, A, SELF> = ($AbstractListBuilder<(T), (A), (SELF)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractListBuilder_<T, A, SELF> = $AbstractListBuilder$Type<(T), (A), (SELF)>;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/$EasingMethods" {
import {$EasingMethod, $EasingMethod$Type} from "packages/me/shedaniel/clothconfig2/impl/$EasingMethod"
import {$List, $List$Type} from "packages/java/util/$List"

export class $EasingMethods {

constructor()

public static "getMethods"(): $List<($EasingMethod)>
public static "register"(easingMethod: $EasingMethod$Type): void
get "methods"(): $List<($EasingMethod)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EasingMethods$Type = ($EasingMethods);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EasingMethods_ = $EasingMethods$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/$ClothRequiresRestartScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ConfirmScreen, $ConfirmScreen$Type} from "packages/net/minecraft/client/gui/screens/$ConfirmScreen"
import {$BooleanConsumer, $BooleanConsumer$Type} from "packages/it/unimi/dsi/fastutil/booleans/$BooleanConsumer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ClothRequiresRestartScreen extends $ConfirmScreen {
readonly "callback": $BooleanConsumer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(parent: $Screen$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClothRequiresRestartScreen$Type = ($ClothRequiresRestartScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClothRequiresRestartScreen_ = $ClothRequiresRestartScreen$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg9$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg9$Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>): void
}

export namespace $RecordValueAnimatorArgs$Arg9$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg9$Up$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, T> = ($RecordValueAnimatorArgs$Arg9$Up<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg9$Up_<A1, A2, A3, A4, A5, A6, A7, A8, A9, T> = $RecordValueAnimatorArgs$Arg9$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg9$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg9$Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, T> {

 "construct"(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9): T

(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9): T
}

export namespace $RecordValueAnimatorArgs$Arg9$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg9$Op$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, T> = ($RecordValueAnimatorArgs$Arg9$Op<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg9$Op_<A1, A2, A3, A4, A5, A6, A7, A8, A9, T> = $RecordValueAnimatorArgs$Arg9$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/util/$ArrayUtils" {
import {$List, $List$Type} from "packages/java/util/$List"

export class $ArrayUtils {


public static "toUnmodifiableList"<E>(elements: (E)[]): $List<(E)>
public static "toUnmodifiableCompositeList"<E>(array1: (E)[], array2: (E)[]): $List<(E)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArrayUtils$Type = ($ArrayUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArrayUtils_ = $ArrayUtils$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$DropdownMenuElement" {
import {$DropdownBoxEntry$SelectionCellCreator, $DropdownBoxEntry$SelectionCellCreator$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$SelectionCellCreator"
import {$DropdownBoxEntry, $DropdownBoxEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry"
import {$ComponentPath, $ComponentPath$Type} from "packages/net/minecraft/client/gui/$ComponentPath"
import {$FocusNavigationEvent, $FocusNavigationEvent$Type} from "packages/net/minecraft/client/gui/navigation/$FocusNavigationEvent"
import {$ImmutableList, $ImmutableList$Type} from "packages/com/google/common/collect/$ImmutableList"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractContainerEventHandler, $AbstractContainerEventHandler$Type} from "packages/net/minecraft/client/gui/components/events/$AbstractContainerEventHandler"
import {$DropdownBoxEntry$SelectionCellElement, $DropdownBoxEntry$SelectionCellElement$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$SelectionCellElement"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"

export class $DropdownBoxEntry$DropdownMenuElement<R> extends $AbstractContainerEventHandler {

constructor()

public "getEntry"(): $DropdownBoxEntry<(R)>
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: $Rectangle$Type, arg4: float): void
public "initCells"(): void
public "nextFocusPath"(focusNavigationEvent: $FocusNavigationEvent$Type): $ComponentPath
public "children"(): $List<($DropdownBoxEntry$SelectionCellElement<(R)>)>
public "getHeight"(): integer
public "lateRender"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "isExpanded"(): boolean
public "getCellCreator"(): $DropdownBoxEntry$SelectionCellCreator<(R)>
public "isSuggestionMode"(): boolean
public "getSelections"(): $ImmutableList<(R)>
get "entry"(): $DropdownBoxEntry<(R)>
get "height"(): integer
get "expanded"(): boolean
get "cellCreator"(): $DropdownBoxEntry$SelectionCellCreator<(R)>
get "suggestionMode"(): boolean
get "selections"(): $ImmutableList<(R)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropdownBoxEntry$DropdownMenuElement$Type<R> = ($DropdownBoxEntry$DropdownMenuElement<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropdownBoxEntry$DropdownMenuElement_<R> = $DropdownBoxEntry$DropdownMenuElement$Type<(R)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$Results$Errors" {
import {$AtomicInteger, $AtomicInteger$Type} from "packages/java/util/concurrent/atomic/$AtomicInteger"

export class $Results$Errors {


public "add"(other: $Results$Errors$Type): void
public "toString"(): string
public "keyDuplicatesTable"(key: string, line: $AtomicInteger$Type): void
public "tableDuplicatesKey"(table: string, line: $AtomicInteger$Type): void
public "heterogenous"(key: string, line: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Results$Errors$Type = ($Results$Errors);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Results$Errors_ = $Results$Errors$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$AnchorToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $AnchorToken extends $Token {

constructor(value: string, startMark: $Mark$Type, endMark: $Mark$Type)

public "getValue"(): string
public "getTokenId"(): $Token$ID
get "value"(): string
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnchorToken$Type = ($AnchorToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnchorToken_ = $AnchorToken$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$IdentifierConverter" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $IdentifierConverter {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IdentifierConverter$Type = ($IdentifierConverter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IdentifierConverter_ = $IdentifierConverter$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$NumberValueReaderWriter" {
import {$ValueReader, $ValueReader$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueReader"
import {$WriterContext, $WriterContext$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$WriterContext"
import {$Context, $Context$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$Context"
import {$AtomicInteger, $AtomicInteger$Type} from "packages/java/util/concurrent/atomic/$AtomicInteger"
import {$ValueWriter, $ValueWriter$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueWriter"

export class $NumberValueReaderWriter implements $ValueReader, $ValueWriter {


public "toString"(): string
public "write"(value: any, context: $WriterContext$Type): void
public "read"(s: string, index: $AtomicInteger$Type, context: $Context$Type): any
public "canRead"(s: string): boolean
public "canWrite"(value: any): boolean
public "isPrimitiveType"(): boolean
get "primitiveType"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NumberValueReaderWriter$Type = ($NumberValueReaderWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NumberValueReaderWriter_ = $NumberValueReaderWriter$Type;
}}
declare module "packages/me/shedaniel/autoconfig/serializer/$GsonConfigSerializer" {
import {$Config, $Config$Type} from "packages/me/shedaniel/autoconfig/annotation/$Config"
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ConfigSerializer, $ConfigSerializer$Type} from "packages/me/shedaniel/autoconfig/serializer/$ConfigSerializer"
import {$ConfigData, $ConfigData$Type} from "packages/me/shedaniel/autoconfig/$ConfigData"

export class $GsonConfigSerializer<T extends $ConfigData> implements $ConfigSerializer<(T)> {

constructor(definition: $Config$Type, configClass: $Class$Type<(T)>, gson: $Gson$Type)
constructor(definition: $Config$Type, configClass: $Class$Type<(T)>)

public "deserialize"(): T
public "createDefault"(): T
public "serialize"(config: T): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GsonConfigSerializer$Type<T> = ($GsonConfigSerializer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GsonConfigSerializer_<T> = $GsonConfigSerializer$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipListEntry, $TooltipListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$TooltipListEntry"
import {$DropdownBoxEntry$SelectionElement, $DropdownBoxEntry$SelectionElement$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$SelectionElement"
import {$DropdownBoxEntry$SelectionTopCellElement, $DropdownBoxEntry$SelectionTopCellElement$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$SelectionTopCellElement"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$DropdownBoxEntry$SelectionCellCreator, $DropdownBoxEntry$SelectionCellCreator$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$SelectionCellCreator"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ImmutableList, $ImmutableList$Type} from "packages/com/google/common/collect/$ImmutableList"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $DropdownBoxEntry<T> extends $TooltipListEntry<(T)> {

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, resetButtonKey: $Component$Type, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, requiresRestart: boolean, defaultValue: $Supplier$Type<(T)>, saveConsumer: $Consumer$Type<(T)>, selections: $Iterable$Type<(T)>, topRenderer: $DropdownBoxEntry$SelectionTopCellElement$Type<(T)>, cellCreator: $DropdownBoxEntry$SelectionCellCreator$Type<(T)>)

public "getValue"(): T
public "getDefaultValue"(): $Optional<(T)>
public "getError"(): $Optional<($Component)>
public "render"(graphics: $GuiGraphics$Type, index: integer, y: integer, x: integer, entryWidth: integer, entryHeight: integer, mouseX: integer, mouseY: integer, isHovered: boolean, delta: float): void
public "children"(): $List<(any)>
public "narratables"(): $List<(any)>
public "isEdited"(): boolean
public "updateSelected"(isSelected: boolean): void
public "getMorePossibleHeight"(): integer
public "lateRender"(graphics: $GuiGraphics$Type, mouseX: integer, mouseY: integer, delta: float): void
public "mouseScrolled"(double_1: double, double_2: double, double_3: double): boolean
public "setSuggestionMode"(suggestionMode: boolean): void
public "isSuggestionMode"(): boolean
public "getSelections"(): $ImmutableList<(T)>
/**
 * 
 * @deprecated
 */
public "getSelectionElement"(): $DropdownBoxEntry$SelectionElement<(T)>
get "value"(): T
get "defaultValue"(): $Optional<(T)>
get "error"(): $Optional<($Component)>
get "edited"(): boolean
get "morePossibleHeight"(): integer
set "suggestionMode"(value: boolean)
get "suggestionMode"(): boolean
get "selections"(): $ImmutableList<(T)>
get "selectionElement"(): $DropdownBoxEntry$SelectionElement<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropdownBoxEntry$Type<T> = ($DropdownBoxEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropdownBoxEntry_<T> = $DropdownBoxEntry$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$EmptyEntry" {
import {$ComponentPath, $ComponentPath$Type} from "packages/net/minecraft/client/gui/$ComponentPath"
import {$FocusNavigationEvent, $FocusNavigationEvent$Type} from "packages/net/minecraft/client/gui/navigation/$FocusNavigationEvent"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $EmptyEntry extends $AbstractConfigListEntry<(any)> {

constructor(height: integer)

public "getValue"(): any
public "getDefaultValue"(): $Optional<(any)>
public "render"(graphics: $GuiGraphics$Type, index: integer, y: integer, x: integer, entryWidth: integer, entryHeight: integer, mouseX: integer, mouseY: integer, isHovered: boolean, delta: float): void
public "nextFocusPath"(focusNavigationEvent: $FocusNavigationEvent$Type): $ComponentPath
public "children"(): $List<(any)>
public "narratables"(): $List<(any)>
public "getSearchTags"(): $Iterator<(string)>
public "isMouseInside"(mouseX: integer, mouseY: integer, x: integer, y: integer, entryWidth: integer, entryHeight: integer): boolean
public "getItemHeight"(): integer
get "value"(): any
get "defaultValue"(): $Optional<(any)>
get "searchTags"(): $Iterator<(string)>
get "itemHeight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmptyEntry$Type = ($EmptyEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmptyEntry_ = $EmptyEntry$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$MultilineStringValueReader" {
import {$ValueReader, $ValueReader$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueReader"
import {$Context, $Context$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$Context"
import {$AtomicInteger, $AtomicInteger$Type} from "packages/java/util/concurrent/atomic/$AtomicInteger"

export class $MultilineStringValueReader implements $ValueReader {


public "read"(s: string, index: $AtomicInteger$Type, context: $Context$Type): any
public "canRead"(s: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultilineStringValueReader$Type = ($MultilineStringValueReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultilineStringValueReader_ = $MultilineStringValueReader$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/reader/$StreamReader" {
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"

export class $StreamReader {

constructor(stream: string)
constructor(reader: $Reader$Type)

public "prefix"(length: integer): string
public "peek"(index: integer): integer
public "peek"(): integer
public "getIndex"(): integer
public static "isPrintable"(data: string): boolean
public static "isPrintable"(c: integer): boolean
public "getLine"(): integer
public "forward"(length: integer): void
public "forward"(): void
public "prefixForward"(length: integer): string
public "getMark"(): $Mark
public "getColumn"(): integer
get "index"(): integer
get "line"(): integer
get "mark"(): $Mark
get "column"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StreamReader$Type = ($StreamReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StreamReader_ = $StreamReader$Type;
}}
declare module "packages/me/shedaniel/autoconfig/example/$ExampleConfig" {
import {$ExampleConfig$ModuleA, $ExampleConfig$ModuleA$Type} from "packages/me/shedaniel/autoconfig/example/$ExampleConfig$ModuleA"
import {$ExampleConfig$ModuleB, $ExampleConfig$ModuleB$Type} from "packages/me/shedaniel/autoconfig/example/$ExampleConfig$ModuleB"
import {$PartitioningSerializer$GlobalData, $PartitioningSerializer$GlobalData$Type} from "packages/me/shedaniel/autoconfig/serializer/$PartitioningSerializer$GlobalData"
import {$ExampleConfig$Empty, $ExampleConfig$Empty$Type} from "packages/me/shedaniel/autoconfig/example/$ExampleConfig$Empty"

export class $ExampleConfig extends $PartitioningSerializer$GlobalData {
 "moduleA": $ExampleConfig$ModuleA
 "empty": $ExampleConfig$Empty
 "moduleB": $ExampleConfig$ModuleB

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExampleConfig$Type = ($ExampleConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExampleConfig_ = $ExampleConfig$Type;
}}
declare module "packages/me/shedaniel/math/$FloatingDimension" {
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Dimension, $Dimension$Type} from "packages/me/shedaniel/math/$Dimension"

export class $FloatingDimension implements $Cloneable {
 "width": double
 "height": double

constructor()
constructor(arg0: double, arg1: double)
constructor(arg0: $FloatingDimension$Type)
constructor(arg0: $Dimension$Type)

public "getFloatingSize"(): $FloatingDimension
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): $FloatingDimension
public "getSize"(): $Dimension
public "setSize"(arg0: $FloatingDimension$Type): void
public "setSize"(arg0: double, arg1: double): void
public "setSize"(arg0: $Dimension$Type): void
public "getWidth"(): double
public "getHeight"(): double
get "floatingSize"(): $FloatingDimension
get "size"(): $Dimension
set "size"(value: $FloatingDimension$Type)
set "size"(value: $Dimension$Type)
get "width"(): double
get "height"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FloatingDimension$Type = ($FloatingDimension);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FloatingDimension_ = $FloatingDimension$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/serializer/$InternalDeserializerFunction" {
import {$Marshaller, $Marshaller$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/api/$Marshaller"

export interface $InternalDeserializerFunction<B> {

 "deserialize"(arg0: any, arg1: $Marshaller$Type): B

(arg0: any, arg1: $Marshaller$Type): B
}

export namespace $InternalDeserializerFunction {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InternalDeserializerFunction$Type<B> = ($InternalDeserializerFunction<(B)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InternalDeserializerFunction_<B> = $InternalDeserializerFunction$Type<(B)>;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$SubCategoryBuilder" {
import {$Comparator, $Comparator$Type} from "packages/java/util/$Comparator"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$SubCategoryListEntry, $SubCategoryListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$SubCategoryListEntry"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ListIterator, $ListIterator$Type} from "packages/java/util/$ListIterator"
import {$Spliterator, $Spliterator$Type} from "packages/java/util/$Spliterator"
import {$FieldBuilder, $FieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$FieldBuilder"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$UnaryOperator, $UnaryOperator$Type} from "packages/java/util/function/$UnaryOperator"
import {$IntFunction, $IntFunction$Type} from "packages/java/util/function/$IntFunction"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $SubCategoryBuilder extends $FieldBuilder<($List<($AbstractConfigListEntry)>), ($SubCategoryListEntry), ($SubCategoryBuilder)> implements $List<($AbstractConfigListEntry)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type)

public "add"(abstractConfigListEntry: $AbstractConfigListEntry$Type<(any)>): boolean
public "add"(index: integer, element: $AbstractConfigListEntry$Type<(any)>): void
public "remove"(index: integer): $AbstractConfigListEntry<(any)>
public "remove"(o: any): boolean
public "get"(index: integer): $AbstractConfigListEntry<(any)>
public "indexOf"(o: any): integer
public "clear"(): void
public "lastIndexOf"(o: any): integer
public "isEmpty"(): boolean
public "size"(): integer
public "subList"(fromIndex: integer, toIndex: integer): $List<($AbstractConfigListEntry)>
public "toArray"(): (any)[]
public "toArray"<T>(a: (T)[]): (T)[]
public "iterator"(): $Iterator<($AbstractConfigListEntry)>
public "contains"(o: any): boolean
public "addAll"(c: $Collection$Type<(any)>): boolean
public "addAll"(index: integer, c: $Collection$Type<(any)>): boolean
public "set"(index: integer, element: $AbstractConfigListEntry$Type<(any)>): $AbstractConfigListEntry<(any)>
public "removeAll"(c: $Collection$Type<(any)>): boolean
public "retainAll"(c: $Collection$Type<(any)>): boolean
public "listIterator"(index: integer): $ListIterator<($AbstractConfigListEntry)>
public "listIterator"(): $ListIterator<($AbstractConfigListEntry)>
public "containsAll"(c: $Collection$Type<(any)>): boolean
public "build"(): $SubCategoryListEntry
public "setTooltipSupplier"(tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>): $SubCategoryBuilder
public "setTooltipSupplier"(tooltipSupplier: $Function$Type<($List$Type<($AbstractConfigListEntry$Type)>), ($Optional$Type<(($Component$Type)[])>)>): $SubCategoryBuilder
public "setExpanded"(expanded: boolean): $SubCategoryBuilder
public "requireRestart"(requireRestart: boolean): void
public "setTooltip"(...tooltip: ($Component$Type)[]): $SubCategoryBuilder
public "setTooltip"(tooltip: $Optional$Type<(($Component$Type)[])>): $SubCategoryBuilder
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public static "copyOf"<E>(arg0: $Collection$Type<(any)>): $List<($AbstractConfigListEntry<(any)>)>
public "replaceAll"(arg0: $UnaryOperator$Type<($AbstractConfigListEntry$Type<(any)>)>): void
public static "of"<E>(arg0: $AbstractConfigListEntry$Type<(any)>, arg1: $AbstractConfigListEntry$Type<(any)>, arg2: $AbstractConfigListEntry$Type<(any)>, arg3: $AbstractConfigListEntry$Type<(any)>): $List<($AbstractConfigListEntry<(any)>)>
public static "of"<E>(arg0: $AbstractConfigListEntry$Type<(any)>, arg1: $AbstractConfigListEntry$Type<(any)>, arg2: $AbstractConfigListEntry$Type<(any)>): $List<($AbstractConfigListEntry<(any)>)>
public static "of"<E>(arg0: $AbstractConfigListEntry$Type<(any)>, arg1: $AbstractConfigListEntry$Type<(any)>): $List<($AbstractConfigListEntry<(any)>)>
public static "of"<E>(arg0: $AbstractConfigListEntry$Type<(any)>): $List<($AbstractConfigListEntry<(any)>)>
public static "of"<E>(): $List<($AbstractConfigListEntry<(any)>)>
public static "of"<E>(arg0: $AbstractConfigListEntry$Type<(any)>, arg1: $AbstractConfigListEntry$Type<(any)>, arg2: $AbstractConfigListEntry$Type<(any)>, arg3: $AbstractConfigListEntry$Type<(any)>, arg4: $AbstractConfigListEntry$Type<(any)>, arg5: $AbstractConfigListEntry$Type<(any)>, arg6: $AbstractConfigListEntry$Type<(any)>, arg7: $AbstractConfigListEntry$Type<(any)>): $List<($AbstractConfigListEntry<(any)>)>
public static "of"<E>(arg0: $AbstractConfigListEntry$Type<(any)>, arg1: $AbstractConfigListEntry$Type<(any)>, arg2: $AbstractConfigListEntry$Type<(any)>, arg3: $AbstractConfigListEntry$Type<(any)>, arg4: $AbstractConfigListEntry$Type<(any)>, arg5: $AbstractConfigListEntry$Type<(any)>, arg6: $AbstractConfigListEntry$Type<(any)>): $List<($AbstractConfigListEntry<(any)>)>
public static "of"<E>(arg0: $AbstractConfigListEntry$Type<(any)>, arg1: $AbstractConfigListEntry$Type<(any)>, arg2: $AbstractConfigListEntry$Type<(any)>, arg3: $AbstractConfigListEntry$Type<(any)>, arg4: $AbstractConfigListEntry$Type<(any)>, arg5: $AbstractConfigListEntry$Type<(any)>): $List<($AbstractConfigListEntry<(any)>)>
public static "of"<E>(arg0: $AbstractConfigListEntry$Type<(any)>, arg1: $AbstractConfigListEntry$Type<(any)>, arg2: $AbstractConfigListEntry$Type<(any)>, arg3: $AbstractConfigListEntry$Type<(any)>, arg4: $AbstractConfigListEntry$Type<(any)>): $List<($AbstractConfigListEntry<(any)>)>
public static "of"<E>(arg0: $AbstractConfigListEntry$Type<(any)>, arg1: $AbstractConfigListEntry$Type<(any)>, arg2: $AbstractConfigListEntry$Type<(any)>, arg3: $AbstractConfigListEntry$Type<(any)>, arg4: $AbstractConfigListEntry$Type<(any)>, arg5: $AbstractConfigListEntry$Type<(any)>, arg6: $AbstractConfigListEntry$Type<(any)>, arg7: $AbstractConfigListEntry$Type<(any)>, arg8: $AbstractConfigListEntry$Type<(any)>, arg9: $AbstractConfigListEntry$Type<(any)>): $List<($AbstractConfigListEntry<(any)>)>
public static "of"<E>(arg0: $AbstractConfigListEntry$Type<(any)>, arg1: $AbstractConfigListEntry$Type<(any)>, arg2: $AbstractConfigListEntry$Type<(any)>, arg3: $AbstractConfigListEntry$Type<(any)>, arg4: $AbstractConfigListEntry$Type<(any)>, arg5: $AbstractConfigListEntry$Type<(any)>, arg6: $AbstractConfigListEntry$Type<(any)>, arg7: $AbstractConfigListEntry$Type<(any)>, arg8: $AbstractConfigListEntry$Type<(any)>): $List<($AbstractConfigListEntry<(any)>)>
public static "of"<E>(...arg0: ($AbstractConfigListEntry$Type<(any)>)[]): $List<($AbstractConfigListEntry<(any)>)>
public "spliterator"(): $Spliterator<($AbstractConfigListEntry<(any)>)>
public "sort"(arg0: $Comparator$Type<(any)>): void
public "toArray"<T>(arg0: $IntFunction$Type<((T)[])>): (T)[]
public "stream"(): $Stream<($AbstractConfigListEntry<(any)>)>
public "removeIf"(arg0: $Predicate$Type<(any)>): boolean
public "parallelStream"(): $Stream<($AbstractConfigListEntry<(any)>)>
public "forEach"(arg0: $Consumer$Type<(any)>): void
[Symbol.iterator](): IterableIterator<$AbstractConfigListEntry>;
[index: number]: $AbstractConfigListEntry
get "empty"(): boolean
set "tooltipSupplier"(value: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
set "tooltipSupplier"(value: $Function$Type<($List$Type<($AbstractConfigListEntry$Type)>), ($Optional$Type<(($Component$Type)[])>)>)
set "expanded"(value: boolean)
set "tooltip"(value: ($Component$Type)[])
set "tooltip"(value: $Optional$Type<(($Component$Type)[])>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SubCategoryBuilder$Type = ($SubCategoryBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SubCategoryBuilder_ = $SubCategoryBuilder$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$Tooltip" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Point, $Point$Type} from "packages/me/shedaniel/math/$Point"

export interface $Tooltip {

 "getText"(): $List<($FormattedCharSequence)>
 "getPoint"(): $Point
 "getY"(): integer
 "getX"(): integer
}

export namespace $Tooltip {
function of(location: $Point$Type, ...text: ($FormattedCharSequence$Type)[]): $Tooltip
function of(location: $Point$Type, ...text: ($FormattedText$Type)[]): $Tooltip
function of(location: $Point$Type, ...text: ($Component$Type)[]): $Tooltip
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Tooltip$Type = ($Tooltip);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Tooltip_ = $Tooltip$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/$ConfigEntryBuilderImpl" {
import {$IntFieldBuilder, $IntFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$IntFieldBuilder"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IntSliderBuilder, $IntSliderBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$IntSliderBuilder"
import {$LongFieldBuilder, $LongFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$LongFieldBuilder"
import {$StringListBuilder, $StringListBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$StringListBuilder"
import {$StringFieldBuilder, $StringFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$StringFieldBuilder"
import {$DoubleListBuilder, $DoubleListBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$DoubleListBuilder"
import {$LongListBuilder, $LongListBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$LongListBuilder"
import {$DropdownBoxEntry$SelectionCellCreator, $DropdownBoxEntry$SelectionCellCreator$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$SelectionCellCreator"
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$SelectorBuilder, $SelectorBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$SelectorBuilder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BooleanToggleBuilder, $BooleanToggleBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$BooleanToggleBuilder"
import {$DoubleFieldBuilder, $DoubleFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$DoubleFieldBuilder"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$TextFieldBuilder, $TextFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$TextFieldBuilder"
import {$FloatFieldBuilder, $FloatFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$FloatFieldBuilder"
import {$TextDescriptionBuilder, $TextDescriptionBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$TextDescriptionBuilder"
import {$Color, $Color$Type} from "packages/me/shedaniel/math/$Color"
import {$ModifierKeyCode, $ModifierKeyCode$Type} from "packages/me/shedaniel/clothconfig2/api/$ModifierKeyCode"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$LongSliderBuilder, $LongSliderBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$LongSliderBuilder"
import {$ColorFieldBuilder, $ColorFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$ColorFieldBuilder"
import {$DropdownBoxEntry$SelectionTopCellElement, $DropdownBoxEntry$SelectionTopCellElement$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$SelectionTopCellElement"
import {$IntListBuilder, $IntListBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$IntListBuilder"
import {$EnumSelectorBuilder, $EnumSelectorBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$EnumSelectorBuilder"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$ConfigEntryBuilder, $ConfigEntryBuilder$Type} from "packages/me/shedaniel/clothconfig2/api/$ConfigEntryBuilder"
import {$KeyCodeBuilder, $KeyCodeBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$KeyCodeBuilder"
import {$FloatListBuilder, $FloatListBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$FloatListBuilder"
import {$SubCategoryBuilder, $SubCategoryBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$SubCategoryBuilder"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$DropdownMenuBuilder, $DropdownMenuBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$DropdownMenuBuilder"
import {$TextColor, $TextColor$Type} from "packages/net/minecraft/network/chat/$TextColor"

export class $ConfigEntryBuilderImpl implements $ConfigEntryBuilder {


public "getResetButtonKey"(): $Component
public static "create"(): $ConfigEntryBuilderImpl
public "startIntSlider"(fieldNameKey: $Component$Type, value: integer, min: integer, max: integer): $IntSliderBuilder
public "startBooleanToggle"(fieldNameKey: $Component$Type, value: boolean): $BooleanToggleBuilder
public "startIntField"(fieldNameKey: $Component$Type, value: integer): $IntFieldBuilder
public "startDoubleField"(fieldNameKey: $Component$Type, value: double): $DoubleFieldBuilder
public "startFloatField"(fieldNameKey: $Component$Type, value: float): $FloatFieldBuilder
public "startLongField"(fieldNameKey: $Component$Type, value: long): $LongFieldBuilder
public "startEnumSelector"<T extends $Enum<(any)>>(fieldNameKey: $Component$Type, clazz: $Class$Type<(T)>, value: T): $EnumSelectorBuilder<(T)>
public "startTextField"(fieldNameKey: $Component$Type, value: string): $TextFieldBuilder
public "startLongSlider"(fieldNameKey: $Component$Type, value: long, min: long, max: long): $LongSliderBuilder
public "startStrList"(fieldNameKey: $Component$Type, value: $List$Type<(string)>): $StringListBuilder
public "startSubCategory"(fieldNameKey: $Component$Type, entries: $List$Type<($AbstractConfigListEntry$Type)>): $SubCategoryBuilder
public "startSubCategory"(fieldNameKey: $Component$Type): $SubCategoryBuilder
public "startTextDescription"(value: $Component$Type): $TextDescriptionBuilder
public static "createImmutable"(): $ConfigEntryBuilderImpl
public "startModifierKeyCodeField"(fieldNameKey: $Component$Type, value: $ModifierKeyCode$Type): $KeyCodeBuilder
public "startSelector"<T>(fieldNameKey: $Component$Type, valuesArray: (T)[], value: T): $SelectorBuilder<(T)>
public "startLongList"(fieldNameKey: $Component$Type, value: $List$Type<(long)>): $LongListBuilder
public "setResetButtonKey"(resetButtonKey: $Component$Type): $ConfigEntryBuilder
public "startFloatList"(fieldNameKey: $Component$Type, value: $List$Type<(float)>): $FloatListBuilder
public "startDropdownMenu"<T>(fieldNameKey: $Component$Type, topCellElement: $DropdownBoxEntry$SelectionTopCellElement$Type<(T)>, cellCreator: $DropdownBoxEntry$SelectionCellCreator$Type<(T)>): $DropdownMenuBuilder<(T)>
public "startIntList"(fieldNameKey: $Component$Type, value: $List$Type<(integer)>): $IntListBuilder
public "startDoubleList"(fieldNameKey: $Component$Type, value: $List$Type<(double)>): $DoubleListBuilder
public "startColorField"(fieldNameKey: $Component$Type, value: integer): $ColorFieldBuilder
public "startStrField"(fieldNameKey: $Component$Type, value: string): $StringFieldBuilder
public "startAlphaColorField"(fieldNameKey: $Component$Type, value: integer): $ColorFieldBuilder
public "startAlphaColorField"(fieldNameKey: $Component$Type, color: $Color$Type): $ColorFieldBuilder
public "fillKeybindingField"(fieldNameKey: $Component$Type, value: $KeyMapping$Type): $KeyCodeBuilder
public "startStringDropdownMenu"(fieldNameKey: $Component$Type, value: string): $DropdownMenuBuilder<(string)>
public "startStringDropdownMenu"(fieldNameKey: $Component$Type, value: string, toTextFunction: $Function$Type<(string), ($Component$Type)>): $DropdownMenuBuilder<(string)>
public "startStringDropdownMenu"(fieldNameKey: $Component$Type, value: string, cellCreator: $DropdownBoxEntry$SelectionCellCreator$Type<(string)>): $DropdownMenuBuilder<(string)>
public "startStringDropdownMenu"(fieldNameKey: $Component$Type, value: string, toTextFunction: $Function$Type<(string), ($Component$Type)>, cellCreator: $DropdownBoxEntry$SelectionCellCreator$Type<(string)>): $DropdownMenuBuilder<(string)>
public "startDropdownMenu"<T>(fieldNameKey: $Component$Type, value: T, toObjectFunction: $Function$Type<(string), (T)>, toTextFunction: $Function$Type<(T), ($Component$Type)>): $DropdownMenuBuilder<(T)>
public "startDropdownMenu"<T>(fieldNameKey: $Component$Type, value: T, toObjectFunction: $Function$Type<(string), (T)>): $DropdownMenuBuilder<(T)>
public "startDropdownMenu"<T>(fieldNameKey: $Component$Type, value: T, toObjectFunction: $Function$Type<(string), (T)>, cellCreator: $DropdownBoxEntry$SelectionCellCreator$Type<(T)>): $DropdownMenuBuilder<(T)>
public "startDropdownMenu"<T>(fieldNameKey: $Component$Type, topCellElement: $DropdownBoxEntry$SelectionTopCellElement$Type<(T)>): $DropdownMenuBuilder<(T)>
public "startDropdownMenu"<T>(fieldNameKey: $Component$Type, value: T, toObjectFunction: $Function$Type<(string), (T)>, toTextFunction: $Function$Type<(T), ($Component$Type)>, cellCreator: $DropdownBoxEntry$SelectionCellCreator$Type<(T)>): $DropdownMenuBuilder<(T)>
public "startKeyCodeField"(fieldNameKey: $Component$Type, value: $InputConstants$Key$Type): $KeyCodeBuilder
public "startColorField"(fieldNameKey: $Component$Type, color: $Color$Type): $ColorFieldBuilder
public "startColorField"(fieldNameKey: $Component$Type, color: $TextColor$Type): $ColorFieldBuilder
get "resetButtonKey"(): $Component
set "resetButtonKey"(value: $Component$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigEntryBuilderImpl$Type = ($ConfigEntryBuilderImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigEntryBuilderImpl_ = $ConfigEntryBuilderImpl$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $Token {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getStartMark"(): $Mark
public "getEndMark"(): $Mark
public "getTokenId"(): $Token$ID
get "startMark"(): $Mark
get "endMark"(): $Mark
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Token$Type = ($Token);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Token_ = $Token$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$MultilineLiteralStringValueReader" {
import {$ValueReader, $ValueReader$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueReader"
import {$Context, $Context$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$Context"
import {$AtomicInteger, $AtomicInteger$Type} from "packages/java/util/concurrent/atomic/$AtomicInteger"

export class $MultilineLiteralStringValueReader implements $ValueReader {


public "read"(s: string, index: $AtomicInteger$Type, context: $Context$Type): any
public "canRead"(s: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MultilineLiteralStringValueReader$Type = ($MultilineLiteralStringValueReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MultilineLiteralStringValueReader_ = $MultilineLiteralStringValueReader$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$BaseListCell" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AbstractContainerEventHandler, $AbstractContainerEventHandler$Type} from "packages/net/minecraft/client/gui/components/events/$AbstractContainerEventHandler"
import {$NarratableEntry$NarrationPriority, $NarratableEntry$NarrationPriority$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry$NarrationPriority"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$NarrationElementOutput, $NarrationElementOutput$Type} from "packages/net/minecraft/client/gui/narration/$NarrationElementOutput"

export class $BaseListCell extends $AbstractContainerEventHandler implements $NarratableEntry {

constructor()

public "getError"(): $Optional<($Component)>
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: boolean, arg9: float): void
public "isRequiresRestart"(): boolean
public "getConfigError"(): $Optional<($Component)>
public "isEdited"(): boolean
public "setErrorSupplier"(errorSupplier: $Supplier$Type<($Optional$Type<($Component$Type)>)>): void
public "updateSelected"(isSelected: boolean): void
public "onDelete"(): void
public "getCellHeight"(): integer
public "getPreferredTextColor"(): integer
public "onAdd"(): void
public "isActive"(): boolean
public "narrationPriority"(): $NarratableEntry$NarrationPriority
public "updateNarration"(arg0: $NarrationElementOutput$Type): void
get "error"(): $Optional<($Component)>
get "requiresRestart"(): boolean
get "configError"(): $Optional<($Component)>
get "edited"(): boolean
set "errorSupplier"(value: $Supplier$Type<($Optional$Type<($Component$Type)>)>)
get "cellHeight"(): integer
get "preferredTextColor"(): integer
get "active"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseListCell$Type = ($BaseListCell);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseListCell_ = $BaseListCell$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$Context" {
import {$Results$Errors, $Results$Errors$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$Results$Errors"
import {$AtomicInteger, $AtomicInteger$Type} from "packages/java/util/concurrent/atomic/$AtomicInteger"
import {$Identifier, $Identifier$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$Identifier"

export class $Context {

constructor(identifier: $Identifier$Type, line: $AtomicInteger$Type, errors: $Results$Errors$Type)

public "with"(identifier: $Identifier$Type): $Context
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Context$Type = ($Context);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Context_ = $Context$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$DocumentStartEvent" {
import {$DumperOptions$Version, $DumperOptions$Version$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$Version"
import {$Event$ID, $Event$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Event, $Event$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $DocumentStartEvent extends $Event {

constructor(startMark: $Mark$Type, endMark: $Mark$Type, explicit: boolean, version: $DumperOptions$Version$Type, tags: $Map$Type<(string), (string)>)

public "getVersion"(): $DumperOptions$Version
public "getEventId"(): $Event$ID
public "getExplicit"(): boolean
public "getTags"(): $Map<(string), (string)>
get "version"(): $DumperOptions$Version
get "eventId"(): $Event$ID
get "explicit"(): boolean
get "tags"(): $Map<(string), (string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DocumentStartEvent$Type = ($DocumentStartEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DocumentStartEvent_ = $DocumentStartEvent$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/$AbstractConfigScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$KeyCodeEntry, $KeyCodeEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$KeyCodeEntry"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Style, $Style$Type} from "packages/net/minecraft/network/chat/$Style"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Tooltip, $Tooltip$Type} from "packages/me/shedaniel/clothconfig2/api/$Tooltip"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ConfigScreen, $ConfigScreen$Type} from "packages/me/shedaniel/clothconfig2/api/$ConfigScreen"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$AbstractConfigEntry, $AbstractConfigEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigEntry"

export class $AbstractConfigScreen extends $Screen implements $ConfigScreen {
 "selectedCategoryIndex": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering


public "save"(): void
public "setSavingRunnable"(savingRunnable: $Runnable$Type): void
public "saveAll"(openOtherScreens: boolean): void
public "keyPressed"(int_1: integer, int_2: integer, int_3: integer): boolean
public "handleComponentClicked"(style: $Style$Type): boolean
public "isEditable"(): boolean
public "isRequiresRestart"(): boolean
public "isEdited"(): boolean
public "setEditable"(editable: boolean): void
public "render"(graphics: $GuiGraphics$Type, mouseX: integer, mouseY: integer, delta: float): void
public "tick"(): void
public "mouseReleased"(double_1: double, double_2: double, int_1: integer): boolean
public "mouseClicked"(double_1: double, double_2: double, int_1: integer): boolean
public "keyReleased"(int_1: integer, int_2: integer, int_3: integer): boolean
public "getBackgroundLocation"(): $ResourceLocation
public "addTooltip"(tooltip: $Tooltip$Type): void
public "getCategorizedEntries"(): $Map<($Component), ($List<($AbstractConfigEntry<(any)>)>)>
public "isTransparentBackground"(): boolean
public "setFallbackCategory"(defaultFallbackCategory: $Component$Type): void
public "setAfterInitConsumer"(afterInitConsumer: $Consumer$Type<($Screen$Type)>): void
public "getFallbackCategory"(): $Component
public "setTransparentBackground"(transparentBackground: boolean): void
public "isAlwaysShowTabs"(): boolean
public "setAlwaysShowTabs"(alwaysShowTabs: boolean): void
public "setConfirmSave"(confirmSave: boolean): void
public "isShowingTabs"(): boolean
public "childrenL"(): $List<($GuiEventListener)>
public "getFocusedBinding"(): $KeyCodeEntry
public "setFocusedBinding"(focusedBinding: $KeyCodeEntry$Type): void
public "matchesSearch"(arg0: $Iterator$Type<(string)>): boolean
set "savingRunnable"(value: $Runnable$Type)
get "editable"(): boolean
get "requiresRestart"(): boolean
get "edited"(): boolean
set "editable"(value: boolean)
get "backgroundLocation"(): $ResourceLocation
get "categorizedEntries"(): $Map<($Component), ($List<($AbstractConfigEntry<(any)>)>)>
get "transparentBackground"(): boolean
set "fallbackCategory"(value: $Component$Type)
set "afterInitConsumer"(value: $Consumer$Type<($Screen$Type)>)
get "fallbackCategory"(): $Component
set "transparentBackground"(value: boolean)
get "alwaysShowTabs"(): boolean
set "alwaysShowTabs"(value: boolean)
set "confirmSave"(value: boolean)
get "showingTabs"(): boolean
get "focusedBinding"(): $KeyCodeEntry
set "focusedBinding"(value: $KeyCodeEntry$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractConfigScreen$Type = ($AbstractConfigScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractConfigScreen_ = $AbstractConfigScreen$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/composer/$ComposerException" {
import {$MarkedYAMLException, $MarkedYAMLException$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$MarkedYAMLException"

export class $ComposerException extends $MarkedYAMLException {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComposerException$Type = ($ComposerException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComposerException_ = $ComposerException$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/$ModifierKeyCodeImpl" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ModifierKeyCode, $ModifierKeyCode$Type} from "packages/me/shedaniel/clothconfig2/api/$ModifierKeyCode"
import {$Modifier, $Modifier$Type} from "packages/me/shedaniel/clothconfig2/api/$Modifier"
import {$InputConstants$Type, $InputConstants$Type$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Type"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $ModifierKeyCodeImpl implements $ModifierKeyCode {

constructor()

public "equals"(o: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getLocalizedName"(): $Component
public "getKeyCode"(): $InputConstants$Key
public "setModifier"(modifier: $Modifier$Type): $ModifierKeyCode
public "setKeyCode"(keyCode: $InputConstants$Key$Type): $ModifierKeyCode
public "getModifier"(): $Modifier
public "matchesCurrentKey"(): boolean
public "clearModifier"(): $ModifierKeyCode
public static "copyOf"(code: $ModifierKeyCode$Type): $ModifierKeyCode
public static "of"(keyCode: $InputConstants$Key$Type, modifier: $Modifier$Type): $ModifierKeyCode
public "getType"(): $InputConstants$Type
public "copy"(): $ModifierKeyCode
public "matchesKey"(keyCode: integer, scanCode: integer): boolean
public "isUnknown"(): boolean
public "matchesMouse"(button: integer): boolean
public static "unknown"(): $ModifierKeyCode
public "matchesCurrentMouse"(): boolean
public "setKeyCodeAndModifier"(keyCode: $InputConstants$Key$Type, modifier: $Modifier$Type): $ModifierKeyCode
get "localizedName"(): $Component
get "keyCode"(): $InputConstants$Key
set "modifier"(value: $Modifier$Type)
set "keyCode"(value: $InputConstants$Key$Type)
get "modifier"(): $Modifier
get "type"(): $InputConstants$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModifierKeyCodeImpl$Type = ($ModifierKeyCodeImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModifierKeyCodeImpl_ = $ModifierKeyCodeImpl$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/$GameOptionsHooks" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"

export interface $GameOptionsHooks {

 "cloth_setKeysAll"(arg0: ($KeyMapping$Type)[]): void

(arg0: ($KeyMapping$Type)[]): void
}

export namespace $GameOptionsHooks {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GameOptionsHooks$Type = ($GameOptionsHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GameOptionsHooks_ = $GameOptionsHooks$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractSliderFieldBuilder" {
import {$AbstractRangeFieldBuilder, $AbstractRangeFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractRangeFieldBuilder"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$FieldBuilder, $FieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$FieldBuilder"

export class $AbstractSliderFieldBuilder<T, A extends $AbstractConfigListEntry<(any)>, SELF extends $FieldBuilder<(T), (A), (SELF)>> extends $AbstractRangeFieldBuilder<(T), (A), (SELF)> {


public "setMin"(min: T): SELF
public "setTextGetter"(textGetter: $Function$Type<(T), ($Component$Type)>): SELF
public "setMax"(max: T): SELF
set "min"(value: T)
set "textGetter"(value: $Function$Type<(T), ($Component$Type)>)
set "max"(value: T)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractSliderFieldBuilder$Type<T, A, SELF> = ($AbstractSliderFieldBuilder<(T), (A), (SELF)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractSliderFieldBuilder_<T, A, SELF> = $AbstractSliderFieldBuilder$Type<(T), (A), (SELF)>;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/$ClothConfigTabButton" {
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ClothConfigScreen, $ClothConfigScreen$Type} from "packages/me/shedaniel/clothconfig2/gui/$ClothConfigScreen"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$AbstractButton, $AbstractButton$Type} from "packages/net/minecraft/client/gui/components/$AbstractButton"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$NarrationElementOutput, $NarrationElementOutput$Type} from "packages/net/minecraft/client/gui/narration/$NarrationElementOutput"

export class $ClothConfigTabButton extends $AbstractButton {
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(screen: $ClothConfigScreen$Type, index: integer, int_1: integer, int_2: integer, int_3: integer, int_4: integer, string_1: $Component$Type, descriptionSupplier: $Supplier$Type<($Optional$Type<(($FormattedText$Type)[])>)>)
constructor(screen: $ClothConfigScreen$Type, index: integer, int_1: integer, int_2: integer, int_3: integer, int_4: integer, string_1: $Component$Type)

public "getDescription"(): $Optional<(($FormattedText)[])>
public "isMouseOver"(double_1: double, double_2: double): boolean
public "render"(graphics: $GuiGraphics$Type, mouseX: integer, mouseY: integer, delta: float): void
public "m_168797_"(narrationElementOutput: $NarrationElementOutput$Type): void
public "onPress"(): void
get "description"(): $Optional<(($FormattedText)[])>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClothConfigTabButton$Type = ($ClothConfigTabButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClothConfigTabButton_ = $ClothConfigTabButton$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$Keys" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Keys {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Keys$Type = ($Keys);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Keys_ = $Keys$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$MappingNode" {
import {$CollectionNode, $CollectionNode$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$CollectionNode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$DumperOptions$FlowStyle, $DumperOptions$FlowStyle$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Tag, $Tag$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Tag"
import {$NodeTuple, $NodeTuple$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$NodeTuple"
import {$NodeId, $NodeId$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$NodeId"

export class $MappingNode extends $CollectionNode<($NodeTuple)> {

/**
 * 
 * @deprecated
 */
constructor(tag: $Tag$Type, value: $List$Type<($NodeTuple$Type)>, flowStyle: boolean)
/**
 * 
 * @deprecated
 */
constructor(tag: $Tag$Type, resolved: boolean, value: $List$Type<($NodeTuple$Type)>, startMark: $Mark$Type, endMark: $Mark$Type, flowStyle: boolean)
constructor(tag: $Tag$Type, value: $List$Type<($NodeTuple$Type)>, flowStyle: $DumperOptions$FlowStyle$Type)
constructor(tag: $Tag$Type, resolved: boolean, value: $List$Type<($NodeTuple$Type)>, startMark: $Mark$Type, endMark: $Mark$Type, flowStyle: $DumperOptions$FlowStyle$Type)

public "toString"(): string
public "getValue"(): $List<($NodeTuple)>
public "setValue"(mergedValue: $List$Type<($NodeTuple$Type)>): void
public "setMerged"(merged: boolean): void
public "getNodeId"(): $NodeId
public "setOnlyKeyType"(keyType: $Class$Type<(any)>): void
public "setTypes"(keyType: $Class$Type<(any)>, valueType: $Class$Type<(any)>): void
public "isMerged"(): boolean
get "value"(): $List<($NodeTuple)>
set "value"(value: $List$Type<($NodeTuple$Type)>)
set "merged"(value: boolean)
get "nodeId"(): $NodeId
set "onlyKeyType"(value: $Class$Type<(any)>)
get "merged"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MappingNode$Type = ($MappingNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MappingNode_ = $MappingNode$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimatorAsNumberAnimator" {
import {$FloatingPoint, $FloatingPoint$Type} from "packages/me/shedaniel/math/$FloatingPoint"
import {$Color, $Color$Type} from "packages/me/shedaniel/math/$Color"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$NumberAnimator, $NumberAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$NumberAnimator"
import {$Dimension, $Dimension$Type} from "packages/me/shedaniel/math/$Dimension"
import {$ValueProvider, $ValueProvider$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueProvider"
import {$ProgressValueAnimator, $ProgressValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ProgressValueAnimator"
import {$FloatingDimension, $FloatingDimension$Type} from "packages/me/shedaniel/math/$FloatingDimension"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"
import {$FloatingRectangle, $FloatingRectangle$Type} from "packages/me/shedaniel/math/$FloatingRectangle"
import {$Point, $Point$Type} from "packages/me/shedaniel/math/$Point"

export class $ValueAnimatorAsNumberAnimator<T extends number> extends $NumberAnimator<(T)> {


public "target"(): T
public "update"(delta: double): void
public "intValue"(): integer
public "longValue"(): long
public "floatValue"(): float
public "doubleValue"(): double
public static "ofLong"(): $NumberAnimator<(long)>
public static "ofLong"(initialValue: long): $NumberAnimator<(long)>
public static "ofFloatingPoint"(): $ValueAnimator<($FloatingPoint)>
public static "ofFloatingPoint"(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
/**
 * 
 * @deprecated
 */
public static "ofDimension"(initialValue: $Point$Type): $ValueAnimator<($Point)>
public static "ofDimension"(initialValue: $Dimension$Type): $ValueAnimator<($Dimension)>
public static "ofDimension"(): $ValueAnimator<($Dimension)>
public static "ofRectangle"(initialValue: $Rectangle$Type): $ValueAnimator<($Rectangle)>
public static "ofRectangle"(): $ValueAnimator<($Rectangle)>
public static "ofPoint"(initialValue: $Point$Type): $ValueAnimator<($Point)>
public static "ofPoint"(): $ValueAnimator<($Point)>
public static "ofInt"(): $NumberAnimator<(integer)>
public static "ofInt"(initialValue: integer): $NumberAnimator<(integer)>
public static "ofDouble"(): $NumberAnimator<(double)>
public static "ofDouble"(initialValue: double): $NumberAnimator<(double)>
public static "ofBoolean"(): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(switchPoint: double, initialValue: boolean): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(switchPoint: double): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(initialValue: boolean): $ProgressValueAnimator<(boolean)>
public static "ofFloat"(initialValue: float): $NumberAnimator<(float)>
public static "ofFloat"(): $NumberAnimator<(float)>
public static "ofColor"(): $ValueAnimator<($Color)>
public static "ofColor"(initialValue: $Color$Type): $ValueAnimator<($Color)>
public static "ofFloatingRectangle"(initialValue: $FloatingRectangle$Type): $ValueAnimator<($FloatingRectangle)>
public static "ofFloatingRectangle"(): $ValueAnimator<($FloatingRectangle)>
/**
 * 
 * @deprecated
 */
public static "ofFloatingDimension"(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
public static "ofFloatingDimension"(): $ValueAnimator<($FloatingDimension)>
public static "ofFloatingDimension"(initialValue: $FloatingDimension$Type): $ValueAnimator<($FloatingDimension)>
public static "typicalTransitionTime"(): long
public static "constant"<T>(value: T): $ValueProvider<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueAnimatorAsNumberAnimator$Type<T> = ($ValueAnimatorAsNumberAnimator<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueAnimatorAsNumberAnimator_<T> = $ValueAnimatorAsNumberAnimator$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg8$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg8$Up<A1, A2, A3, A4, A5, A6, A7, A8, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>): void
}

export namespace $RecordValueAnimatorArgs$Arg8$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg8$Up$Type<A1, A2, A3, A4, A5, A6, A7, A8, T> = ($RecordValueAnimatorArgs$Arg8$Up<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg8$Up_<A1, A2, A3, A4, A5, A6, A7, A8, T> = $RecordValueAnimatorArgs$Arg8$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$SafeConstructor$ConstructUndefined" {
import {$AbstractConstruct, $AbstractConstruct$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$AbstractConstruct"
import {$Node, $Node$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Node"

export class $SafeConstructor$ConstructUndefined extends $AbstractConstruct {

constructor()

public "construct"(node: $Node$Type): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SafeConstructor$ConstructUndefined$Type = ($SafeConstructor$ConstructUndefined);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SafeConstructor$ConstructUndefined_ = $SafeConstructor$ConstructUndefined$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$StringValueReaderWriter" {
import {$ValueReader, $ValueReader$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueReader"
import {$WriterContext, $WriterContext$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$WriterContext"
import {$Context, $Context$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$Context"
import {$AtomicInteger, $AtomicInteger$Type} from "packages/java/util/concurrent/atomic/$AtomicInteger"
import {$ValueWriter, $ValueWriter$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueWriter"

export class $StringValueReaderWriter implements $ValueReader, $ValueWriter {


public "toString"(): string
public "write"(value: any, context: $WriterContext$Type): void
public "read"(s: string, index: $AtomicInteger$Type, context: $Context$Type): any
public "canRead"(s: string): boolean
public "canWrite"(value: any): boolean
public "isPrimitiveType"(): boolean
get "primitiveType"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringValueReaderWriter$Type = ($StringValueReaderWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringValueReaderWriter_ = $StringValueReaderWriter$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$ElementParserContext" {
import {$JsonElement, $JsonElement$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonElement"
import {$ParserContext, $ParserContext$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$ParserContext"
import {$AnnotatedElement, $AnnotatedElement$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$AnnotatedElement"
import {$Jankson, $Jankson$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$Jankson"

export class $ElementParserContext implements $ParserContext<($AnnotatedElement)> {

constructor()

public "setResult"(elem: $JsonElement$Type): void
public "consume"(codePoint: integer, loader: $Jankson$Type): boolean
public "eof"(): void
public "getResult"(): $AnnotatedElement
public "isComplete"(): boolean
set "result"(value: $JsonElement$Type)
get "result"(): $AnnotatedElement
get "complete"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ElementParserContext$Type = ($ElementParserContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ElementParserContext_ = $ElementParserContext$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg8$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg8$Op<A1, A2, A3, A4, A5, A6, A7, A8, T> {

 "construct"(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8): T

(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8): T
}

export namespace $RecordValueAnimatorArgs$Arg8$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg8$Op$Type<A1, A2, A3, A4, A5, A6, A7, A8, T> = ($RecordValueAnimatorArgs$Arg8$Op<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg8$Op_<A1, A2, A3, A4, A5, A6, A7, A8, T> = $RecordValueAnimatorArgs$Arg8$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$Property" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Comparable, $Comparable$Type} from "packages/java/lang/$Comparable"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"

export class $Property implements $Comparable<($Property)> {

constructor(name: string, type: $Class$Type<(any)>)

public "getName"(): string
public "get"(arg0: any): any
public "equals"(other: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "compareTo"(o: $Property$Type): integer
public "getAnnotation"<A extends $Annotation>(arg0: $Class$Type<(A)>): A
public "getAnnotations"(): $List<($Annotation)>
public "set"(arg0: any, arg1: any): void
public "getType"(): $Class<(any)>
public "getActualTypeArguments"(): ($Class<(any)>)[]
public "isReadable"(): boolean
public "isWritable"(): boolean
get "name"(): string
get "annotations"(): $List<($Annotation)>
get "type"(): $Class<(any)>
get "actualTypeArguments"(): ($Class<(any)>)[]
get "readable"(): boolean
get "writable"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Property$Type = ($Property);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Property_ = $Property$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$BeanAccess" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $BeanAccess extends $Enum<($BeanAccess)> {
static readonly "DEFAULT": $BeanAccess
static readonly "FIELD": $BeanAccess
static readonly "PROPERTY": $BeanAccess


public static "values"(): ($BeanAccess)[]
public static "valueOf"(name: string): $BeanAccess
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BeanAccess$Type = (("default") | ("field") | ("property")) | ($BeanAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BeanAccess_ = $BeanAccess$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg4" {
import {$RecordValueAnimatorArgs$Arg4$Up, $RecordValueAnimatorArgs$Arg4$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg4$Up"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimatorArgs$Arg4$Op, $RecordValueAnimatorArgs$Arg4$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg4$Op"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"

export class $RecordValueAnimatorArgs$Arg4<A1, A2, A3, A4, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, op: $RecordValueAnimatorArgs$Arg4$Op$Type<(A1), (A2), (A3), (A4), (T)>, up: $RecordValueAnimatorArgs$Arg4$Up$Type<(A1), (A2), (A3), (A4), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg4$Type<A1, A2, A3, A4, T> = ($RecordValueAnimatorArgs$Arg4<(A1), (A2), (A3), (A4), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg4_<A1, A2, A3, A4, T> = $RecordValueAnimatorArgs$Arg4$Type<(A1), (A2), (A3), (A4), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg3" {
import {$RecordValueAnimatorArgs$Arg3$Up, $RecordValueAnimatorArgs$Arg3$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg3$Up"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimatorArgs$Arg3$Op, $RecordValueAnimatorArgs$Arg3$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg3$Op"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"

export class $RecordValueAnimatorArgs$Arg3<A1, A2, A3, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, op: $RecordValueAnimatorArgs$Arg3$Op$Type<(A1), (A2), (A3), (T)>, up: $RecordValueAnimatorArgs$Arg3$Up$Type<(A1), (A2), (A3), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg3$Type<A1, A2, A3, T> = ($RecordValueAnimatorArgs$Arg3<(A1), (A2), (A3), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg3_<A1, A2, A3, T> = $RecordValueAnimatorArgs$Arg3$Type<(A1), (A2), (A3), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg2" {
import {$RecordValueAnimatorArgs$Arg2$Op, $RecordValueAnimatorArgs$Arg2$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg2$Op"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimatorArgs$Arg2$Up, $RecordValueAnimatorArgs$Arg2$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg2$Up"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"

export class $RecordValueAnimatorArgs$Arg2<A1, A2, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, op: $RecordValueAnimatorArgs$Arg2$Op$Type<(A1), (A2), (T)>, up: $RecordValueAnimatorArgs$Arg2$Up$Type<(A1), (A2), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg2$Type<A1, A2, T> = ($RecordValueAnimatorArgs$Arg2<(A1), (A2), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg2_<A1, A2, T> = $RecordValueAnimatorArgs$Arg2$Type<(A1), (A2), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg1" {
import {$RecordValueAnimatorArgs$Arg1$Op, $RecordValueAnimatorArgs$Arg1$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg1$Op"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimatorArgs$Arg1$Up, $RecordValueAnimatorArgs$Arg1$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg1$Up"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"

export class $RecordValueAnimatorArgs$Arg1<A1, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, op: $RecordValueAnimatorArgs$Arg1$Op$Type<(A1), (T)>, up: $RecordValueAnimatorArgs$Arg1$Up$Type<(A1), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg1$Type<A1, T> = ($RecordValueAnimatorArgs$Arg1<(A1), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg1_<A1, T> = $RecordValueAnimatorArgs$Arg1$Type<(A1), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg9" {
import {$RecordValueAnimatorArgs$Arg9$Op, $RecordValueAnimatorArgs$Arg9$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg9$Op"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"
import {$RecordValueAnimatorArgs$Arg9$Up, $RecordValueAnimatorArgs$Arg9$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg9$Up"

export class $RecordValueAnimatorArgs$Arg9<A1, A2, A3, A4, A5, A6, A7, A8, A9, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, op: $RecordValueAnimatorArgs$Arg9$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (T)>, up: $RecordValueAnimatorArgs$Arg9$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg9$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, T> = ($RecordValueAnimatorArgs$Arg9<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg9_<A1, A2, A3, A4, A5, A6, A7, A8, A9, T> = $RecordValueAnimatorArgs$Arg9$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg8" {
import {$RecordValueAnimatorArgs$Arg8$Op, $RecordValueAnimatorArgs$Arg8$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg8$Op"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimatorArgs$Arg8$Up, $RecordValueAnimatorArgs$Arg8$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg8$Up"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"

export class $RecordValueAnimatorArgs$Arg8<A1, A2, A3, A4, A5, A6, A7, A8, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, op: $RecordValueAnimatorArgs$Arg8$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (T)>, up: $RecordValueAnimatorArgs$Arg8$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg8$Type<A1, A2, A3, A4, A5, A6, A7, A8, T> = ($RecordValueAnimatorArgs$Arg8<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg8_<A1, A2, A3, A4, A5, A6, A7, A8, T> = $RecordValueAnimatorArgs$Arg8$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg7" {
import {$RecordValueAnimatorArgs$Arg7$Op, $RecordValueAnimatorArgs$Arg7$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg7$Op"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimatorArgs$Arg7$Up, $RecordValueAnimatorArgs$Arg7$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg7$Up"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"

export class $RecordValueAnimatorArgs$Arg7<A1, A2, A3, A4, A5, A6, A7, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, op: $RecordValueAnimatorArgs$Arg7$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (T)>, up: $RecordValueAnimatorArgs$Arg7$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg7$Type<A1, A2, A3, A4, A5, A6, A7, T> = ($RecordValueAnimatorArgs$Arg7<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg7_<A1, A2, A3, A4, A5, A6, A7, T> = $RecordValueAnimatorArgs$Arg7$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg6" {
import {$RecordValueAnimatorArgs$Arg6$Up, $RecordValueAnimatorArgs$Arg6$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg6$Up"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$RecordValueAnimatorArgs$Arg6$Op, $RecordValueAnimatorArgs$Arg6$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg6$Op"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"

export class $RecordValueAnimatorArgs$Arg6<A1, A2, A3, A4, A5, A6, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, op: $RecordValueAnimatorArgs$Arg6$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (T)>, up: $RecordValueAnimatorArgs$Arg6$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg6$Type<A1, A2, A3, A4, A5, A6, T> = ($RecordValueAnimatorArgs$Arg6<(A1), (A2), (A3), (A4), (A5), (A6), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg6_<A1, A2, A3, A4, A5, A6, T> = $RecordValueAnimatorArgs$Arg6$Type<(A1), (A2), (A3), (A4), (A5), (A6), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg5" {
import {$RecordValueAnimatorArgs$Arg5$Up, $RecordValueAnimatorArgs$Arg5$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg5$Up"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$RecordValueAnimatorArgs$Arg5$Op, $RecordValueAnimatorArgs$Arg5$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg5$Op"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"

export class $RecordValueAnimatorArgs$Arg5<A1, A2, A3, A4, A5, T> implements $RecordValueAnimator$Arg<(T)> {

constructor(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, op: $RecordValueAnimatorArgs$Arg5$Op$Type<(A1), (A2), (A3), (A4), (A5), (T)>, up: $RecordValueAnimatorArgs$Arg5$Up$Type<(A1), (A2), (A3), (A4), (A5), (T)>)

public "value"(): T
public "target"(): T
public "set"(value: T, duration: long): void
public "setTarget"(value: T): void
public "dependencies"(): $List<($ValueAnimator<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg5$Type<A1, A2, A3, A4, A5, T> = ($RecordValueAnimatorArgs$Arg5<(A1), (A2), (A3), (A4), (A5), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg5_<A1, A2, A3, A4, A5, T> = $RecordValueAnimatorArgs$Arg5$Type<(A1), (A2), (A3), (A4), (A5), (T)>;
}}
declare module "packages/me/shedaniel/math/$FloatingPoint" {
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Point, $Point$Type} from "packages/me/shedaniel/math/$Point"

export class $FloatingPoint implements $Cloneable {
 "x": double
 "y": double

constructor(arg0: double, arg1: double)
constructor(arg0: $FloatingPoint$Type)
constructor(arg0: $Point$Type)
constructor()

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): $FloatingPoint
public "getLocation"(): $Point
public "move"(arg0: double, arg1: double): void
public "getY"(): double
public "translate"(arg0: double, arg1: double): void
public "getX"(): double
public "setLocation"(arg0: double, arg1: double): void
public "getFloatingLocation"(): $FloatingPoint
get "location"(): $Point
get "y"(): double
get "x"(): double
get "floatingLocation"(): $FloatingPoint
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FloatingPoint$Type = ($FloatingPoint);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FloatingPoint_ = $FloatingPoint$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg13$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg13$Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, T> {

 "construct"(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11, arg11: A12, arg12: A13): T

(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11, arg11: A12, arg12: A13): T
}

export namespace $RecordValueAnimatorArgs$Arg13$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg13$Op$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, T> = ($RecordValueAnimatorArgs$Arg13$Op<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg13$Op_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, T> = $RecordValueAnimatorArgs$Arg13$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$KeyCodeEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipListEntry, $TooltipListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$TooltipListEntry"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ModifierKeyCode, $ModifierKeyCode$Type} from "packages/me/shedaniel/clothconfig2/api/$ModifierKeyCode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $KeyCodeEntry extends $TooltipListEntry<($ModifierKeyCode)> {

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: $ModifierKeyCode$Type, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<($ModifierKeyCode$Type)>, saveConsumer: $Consumer$Type<($ModifierKeyCode$Type)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, requiresRestart: boolean)

public "getValue"(): $ModifierKeyCode
public "setValue"(value: $ModifierKeyCode$Type): void
public "getDefaultValue"(): $Optional<($ModifierKeyCode)>
public "render"(graphics: $GuiGraphics$Type, index: integer, y: integer, x: integer, entryWidth: integer, entryHeight: integer, mouseX: integer, mouseY: integer, isHovered: boolean, delta: float): void
public "children"(): $List<(any)>
public "narratables"(): $List<(any)>
public "isEdited"(): boolean
public "setAllowMouse"(allowMouse: boolean): void
public "setAllowKey"(allowKey: boolean): void
public "setAllowModifiers"(allowModifiers: boolean): void
public "isAllowMouse"(): boolean
public "isAllowKey"(): boolean
public "isAllowModifiers"(): boolean
get "value"(): $ModifierKeyCode
set "value"(value: $ModifierKeyCode$Type)
get "defaultValue"(): $Optional<($ModifierKeyCode)>
get "edited"(): boolean
set "allowMouse"(value: boolean)
set "allowKey"(value: boolean)
set "allowModifiers"(value: boolean)
get "allowMouse"(): boolean
get "allowKey"(): boolean
get "allowModifiers"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyCodeEntry$Type = ($KeyCodeEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyCodeEntry_ = $KeyCodeEntry$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$Construct" {
import {$Node, $Node$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Node"

export interface $Construct {

 "construct"(arg0: $Node$Type): any
 "construct2ndStep"(arg0: $Node$Type, arg1: any): void
}

export namespace $Construct {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Construct$Type = ($Construct);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Construct_ = $Construct$Type;
}}
declare module "packages/me/shedaniel/autoconfig/gui/registry/$DefaultGuiRegistryAccess" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$GuiRegistryAccess, $GuiRegistryAccess$Type} from "packages/me/shedaniel/autoconfig/gui/registry/api/$GuiRegistryAccess"

export class $DefaultGuiRegistryAccess implements $GuiRegistryAccess {

constructor()

public "get"(i18n: string, field: $Field$Type, config: any, defaults: any, registry: $GuiRegistryAccess$Type): $List<($AbstractConfigListEntry)>
public "transform"(guis: $List$Type<($AbstractConfigListEntry$Type)>, i18n: string, field: $Field$Type, config: any, defaults: any, registry: $GuiRegistryAccess$Type): $List<($AbstractConfigListEntry)>
public "getAndTransform"(i18n: string, field: $Field$Type, config: any, defaults: any, registry: $GuiRegistryAccess$Type): $List<($AbstractConfigListEntry)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultGuiRegistryAccess$Type = ($DefaultGuiRegistryAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultGuiRegistryAccess_ = $DefaultGuiRegistryAccess$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$ConstantValueProvider" {
import {$ValueProvider, $ValueProvider$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueProvider"

export class $ConstantValueProvider<T> implements $ValueProvider<(T)> {

constructor(value: T)

public "value"(): T
public "target"(): T
public "update"(delta: double): void
public "completeImmediately"(): void
public static "constant"<T>(value: T): $ValueProvider<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConstantValueProvider$Type<T> = ($ConstantValueProvider<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConstantValueProvider_<T> = $ConstantValueProvider$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/$ClothConfigScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$ConfigCategory, $ConfigCategory$Type} from "packages/me/shedaniel/clothconfig2/api/$ConfigCategory"
import {$ClothConfigScreen$ListWidget, $ClothConfigScreen$ListWidget$Type} from "packages/me/shedaniel/clothconfig2/gui/$ClothConfigScreen$ListWidget"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$AbstractTabbedConfigScreen, $AbstractTabbedConfigScreen$Type} from "packages/me/shedaniel/clothconfig2/gui/$AbstractTabbedConfigScreen"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$AbstractConfigEntry, $AbstractConfigEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigEntry"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ClothConfigScreen extends $AbstractTabbedConfigScreen {
 "listWidget": $ClothConfigScreen$ListWidget<($AbstractConfigEntry<($AbstractConfigEntry<(any)>)>)>
 "selectedCategoryIndex": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(parent: $Screen$Type, title: $Component$Type, categoryMap: $Map$Type<(string), ($ConfigCategory$Type)>, backgroundLocation: $ResourceLocation$Type)

public "save"(): void
public "isEditable"(): boolean
public "render"(graphics: $GuiGraphics$Type, mouseX: integer, mouseY: integer, delta: float): void
public "matchesSearch"(tags: $Iterator$Type<(string)>): boolean
public "mouseScrolled"(mouseX: double, mouseY: double, amount: double): boolean
public "getCategorizedEntries"(): $Map<($Component), ($List<($AbstractConfigEntry<(any)>)>)>
public "getSelectedCategory"(): $Component
public "getTabsMaximumScrolled"(): double
public "resetTabsMaximumScrolled"(): void
get "editable"(): boolean
get "categorizedEntries"(): $Map<($Component), ($List<($AbstractConfigEntry<(any)>)>)>
get "selectedCategory"(): $Component
get "tabsMaximumScrolled"(): double
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
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$Jankson" {
import {$Jankson$Builder, $Jankson$Builder$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$Jankson$Builder"
import {$Marshaller, $Marshaller$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/api/$Marshaller"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$File, $File$Type} from "packages/java/io/$File"
import {$ParserContext, $ParserContext$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$ParserContext"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$JsonElement, $JsonElement$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonElement"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$SyntaxError, $SyntaxError$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/api/$SyntaxError"
import {$JsonObject, $JsonObject$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonObject"

export class $Jankson {


public "load"(arg0: $InputStream$Type): $JsonObject
public "load"(s: string): $JsonObject
public "load"(f: $File$Type): $JsonObject
public static "builder"(): $Jankson$Builder
public "getCodePoint"(arg0: $InputStream$Type): integer
public "push"<T>(t: $ParserContext$Type<(T)>, consumer: $Consumer$Type<(T)>): void
public "fromJson"<T>(json: string, clazz: $Class$Type<(T)>): T
public "fromJson"<T>(obj: $JsonObject$Type, clazz: $Class$Type<(T)>): T
public "toJson"<T>(t: T): $JsonElement
public "toJson"<T>(t: T, alternateMarshaller: $Marshaller$Type): $JsonElement
public "fromJsonCarefully"<T>(obj: $JsonObject$Type, clazz: $Class$Type<(T)>): T
public "fromJsonCarefully"<T>(json: string, clazz: $Class$Type<(T)>): T
public "throwDelayed"(syntaxError: $SyntaxError$Type): void
public "loadElement"(arg0: $InputStream$Type): $JsonElement
public "loadElement"(f: $File$Type): $JsonElement
public "loadElement"(s: string): $JsonElement
public "getMarshaller"(): $Marshaller
get "marshaller"(): $Marshaller
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Jankson$Type = ($Jankson);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Jankson_ = $Jankson$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg13$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg13$Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>, arg12: $RecordValueAnimatorArgs$Setter$Type<(A12)>, arg13: $RecordValueAnimatorArgs$Setter$Type<(A13)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>, arg12: $RecordValueAnimatorArgs$Setter$Type<(A12)>, arg13: $RecordValueAnimatorArgs$Setter$Type<(A13)>): void
}

export namespace $RecordValueAnimatorArgs$Arg13$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg13$Up$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, T> = ($RecordValueAnimatorArgs$Arg13$Up<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg13$Up_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, T> = $RecordValueAnimatorArgs$Arg13$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/widget/$DynamicElementListWidget" {
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$ComponentPath, $ComponentPath$Type} from "packages/net/minecraft/client/gui/$ComponentPath"
import {$FocusNavigationEvent, $FocusNavigationEvent$Type} from "packages/net/minecraft/client/gui/navigation/$FocusNavigationEvent"
import {$DynamicElementListWidget$ElementEntry, $DynamicElementListWidget$ElementEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/widget/$DynamicElementListWidget$ElementEntry"
import {$DynamicSmoothScrollingEntryListWidget, $DynamicSmoothScrollingEntryListWidget$Type} from "packages/me/shedaniel/clothconfig2/gui/widget/$DynamicSmoothScrollingEntryListWidget"
import {$NarratableEntry$NarrationPriority, $NarratableEntry$NarrationPriority$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry$NarrationPriority"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$NarrationElementOutput, $NarrationElementOutput$Type} from "packages/net/minecraft/client/gui/narration/$NarrationElementOutput"

export class $DynamicElementListWidget<E extends $DynamicElementListWidget$ElementEntry<(E)>> extends $DynamicSmoothScrollingEntryListWidget<(E)> {
 "width": integer
 "height": integer
 "top": integer
 "bottom": integer
 "right": integer
 "left": integer

constructor(client: $Minecraft$Type, width: integer, height: integer, top: integer, bottom: integer, backgroundLocation: $ResourceLocation$Type)

public "nextFocusPath"(focusNavigationEvent: $FocusNavigationEvent$Type): $ComponentPath
public "updateNarration"(narrationElementOutput: $NarrationElementOutput$Type): void
public "narrationPriority"(): $NarratableEntry$NarrationPriority
public "setFocused"(guiEventListener: $GuiEventListener$Type): void
set "focused"(value: $GuiEventListener$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicElementListWidget$Type<E> = ($DynamicElementListWidget<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicElementListWidget_<E> = $DynamicElementListWidget$Type<(E)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$AbstractConfigEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$AbstractConfigScreen, $AbstractConfigScreen$Type} from "packages/me/shedaniel/clothconfig2/gui/$AbstractConfigScreen"
import {$ReferenceProvider, $ReferenceProvider$Type} from "packages/me/shedaniel/clothconfig2/api/$ReferenceProvider"
import {$DynamicElementListWidget$ElementEntry, $DynamicElementListWidget$ElementEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/widget/$DynamicElementListWidget$ElementEntry"
import {$Iterable, $Iterable$Type} from "packages/java/lang/$Iterable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Tooltip, $Tooltip$Type} from "packages/me/shedaniel/clothconfig2/api/$Tooltip"
import {$ValueHolder, $ValueHolder$Type} from "packages/me/shedaniel/clothconfig2/api/$ValueHolder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $AbstractConfigEntry<T> extends $DynamicElementListWidget$ElementEntry<($AbstractConfigEntry<(T)>)> implements $ReferenceProvider<(T)>, $ValueHolder<(T)> {

constructor()

public "save"(): void
public "getDefaultValue"(): $Optional<(T)>
public "getFieldName"(): $Component
public "setRequiresRestart"(arg0: boolean): void
public "getError"(): $Optional<($Component)>
public "setScreen"(screen: $AbstractConfigScreen$Type): void
public "getConfigScreen"(): $AbstractConfigScreen
public "isRequiresRestart"(): boolean
public "getConfigError"(): $Optional<($Component)>
public "isEdited"(): boolean
public "getSearchTags"(): $Iterator<(string)>
public "setErrorSupplier"(errorSupplier: $Supplier$Type<($Optional$Type<($Component$Type)>)>): void
public "updateSelected"(isSelected: boolean): void
public "appendSearchTags"(tags: $Iterable$Type<(string)>): void
public "lateRender"(graphics: $GuiGraphics$Type, mouseX: integer, mouseY: integer, delta: float): void
public "getItemHeight"(): integer
public "addTooltip"(tooltip: $Tooltip$Type): void
public "getDisplayedFieldName"(): $Component
public "setReferenceProviderEntries"(referencableEntries: $List$Type<($ReferenceProvider$Type<(any)>)>): void
public "getInitialReferenceOffset"(): integer
public "getReferenceProviderEntries"(): $List<($ReferenceProvider<(any)>)>
public "requestReferenceRebuilding"(): void
public "provideReferenceEntry"(): $AbstractConfigEntry<(T)>
public "getValue"(): T
get "defaultValue"(): $Optional<(T)>
get "fieldName"(): $Component
set "requiresRestart"(value: boolean)
get "error"(): $Optional<($Component)>
set "screen"(value: $AbstractConfigScreen$Type)
get "configScreen"(): $AbstractConfigScreen
get "requiresRestart"(): boolean
get "configError"(): $Optional<($Component)>
get "edited"(): boolean
get "searchTags"(): $Iterator<(string)>
set "errorSupplier"(value: $Supplier$Type<($Optional$Type<($Component$Type)>)>)
get "itemHeight"(): integer
get "displayedFieldName"(): $Component
set "referenceProviderEntries"(value: $List$Type<($ReferenceProvider$Type<(any)>)>)
get "initialReferenceOffset"(): integer
get "referenceProviderEntries"(): $List<($ReferenceProvider<(any)>)>
get "value"(): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractConfigEntry$Type<T> = ($AbstractConfigEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractConfigEntry_<T> = $AbstractConfigEntry$Type<(T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/scanner/$ScannerException" {
import {$MarkedYAMLException, $MarkedYAMLException$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$MarkedYAMLException"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $ScannerException extends $MarkedYAMLException {

constructor(context: string, contextMark: $Mark$Type, problem: string, problemMark: $Mark$Type, note: string)
constructor(context: string, contextMark: $Mark$Type, problem: string, problemMark: $Mark$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScannerException$Type = ($ScannerException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScannerException_ = $ScannerException$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg6$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg6$Up<A1, A2, A3, A4, A5, A6, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>): void
}

export namespace $RecordValueAnimatorArgs$Arg6$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg6$Up$Type<A1, A2, A3, A4, A5, A6, T> = ($RecordValueAnimatorArgs$Arg6$Up<(A1), (A2), (A3), (A4), (A5), (A6), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg6$Up_<A1, A2, A3, A4, A5, A6, T> = $RecordValueAnimatorArgs$Arg6$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg6$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg6$Op<A1, A2, A3, A4, A5, A6, T> {

 "construct"(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6): T

(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6): T
}

export namespace $RecordValueAnimatorArgs$Arg6$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg6$Op$Type<A1, A2, A3, A4, A5, A6, T> = ($RecordValueAnimatorArgs$Arg6$Op<(A1), (A2), (A3), (A4), (A5), (A6), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg6$Op_<A1, A2, A3, A4, A5, A6, T> = $RecordValueAnimatorArgs$Arg6$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$ScalarStyle" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DumperOptions$ScalarStyle extends $Enum<($DumperOptions$ScalarStyle)> {
static readonly "DOUBLE_QUOTED": $DumperOptions$ScalarStyle
static readonly "SINGLE_QUOTED": $DumperOptions$ScalarStyle
static readonly "LITERAL": $DumperOptions$ScalarStyle
static readonly "FOLDED": $DumperOptions$ScalarStyle
static readonly "PLAIN": $DumperOptions$ScalarStyle


public "toString"(): string
public static "values"(): ($DumperOptions$ScalarStyle)[]
public "getChar"(): character
public static "valueOf"(name: string): $DumperOptions$ScalarStyle
public static "createStyle"(style: character): $DumperOptions$ScalarStyle
get "char"(): character
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DumperOptions$ScalarStyle$Type = (("double_quoted") | ("plain") | ("single_quoted") | ("folded") | ("literal")) | ($DumperOptions$ScalarStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DumperOptions$ScalarStyle_ = $DumperOptions$ScalarStyle$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg11$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg11$Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>): void
}

export namespace $RecordValueAnimatorArgs$Arg11$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg11$Up$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, T> = ($RecordValueAnimatorArgs$Arg11$Up<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg11$Up_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, T> = $RecordValueAnimatorArgs$Arg11$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/$ConfigCategoryImpl" {
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ConfigCategory, $ConfigCategory$Type} from "packages/me/shedaniel/clothconfig2/api/$ConfigCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ConfigCategoryImpl implements $ConfigCategory {


public "addEntry"(entry: $AbstractConfigListEntry$Type<(any)>): $ConfigCategory
public "getEntries"(): $List<(any)>
public "getDescription"(): $Supplier<($Optional<(($FormattedText)[])>)>
public "setDescription"(description: $Supplier$Type<($Optional$Type<(($FormattedText$Type)[])>)>): void
public "removeCategory"(): void
public "getBackground"(): $ResourceLocation
public "setBackground"(background: $ResourceLocation$Type): void
public "setCategoryBackground"(identifier: $ResourceLocation$Type): $ConfigCategory
public "getCategoryKey"(): $Component
public "setDescription"(description: ($FormattedText$Type)[]): void
get "entries"(): $List<(any)>
get "description"(): $Supplier<($Optional<(($FormattedText)[])>)>
set "description"(value: $Supplier$Type<($Optional$Type<(($FormattedText$Type)[])>)>)
get "background"(): $ResourceLocation
set "background"(value: $ResourceLocation$Type)
set "categoryBackground"(value: $ResourceLocation$Type)
get "categoryKey"(): $Component
set "description"(value: ($FormattedText$Type)[])
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigCategoryImpl$Type = ($ConfigCategoryImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigCategoryImpl_ = $ConfigCategoryImpl$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg11$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg11$Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, T> {

 "construct"(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11): T

(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11): T
}

export namespace $RecordValueAnimatorArgs$Arg11$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg11$Op$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, T> = ($RecordValueAnimatorArgs$Arg11$Op<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg11$Op_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, T> = $RecordValueAnimatorArgs$Arg11$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/scroll/$ScrollingContainer" {
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"

export class $ScrollingContainer {

constructor()

public "scrollAmountInt"(): integer
public "scrollTarget"(): double
public "scrollAmount"(): double
public "offset"(value: double, animated: boolean): void
public "getBounds"(): $Rectangle
public "hasScrollBar"(): boolean
public "updatePosition"(delta: float): void
public "clamp"(v: double, clampExtension: double): double
public "clamp"(v: double): double
public "scrollTo"(value: double, animated: boolean, duration: long): void
public "scrollTo"(value: double, animated: boolean): void
public "getMaxScroll"(): integer
public static "handleBounceBack"(target: double, maxScroll: double, delta: float, bounceBackMultiplier: double): double
public static "handleBounceBack"(target: double, maxScroll: double, delta: float): double
public static "clampExtension"(v: double, maxScroll: double, clampExtension: double): double
public static "clampExtension"(value: double, maxScroll: double): double
public "setScrollDuration"(scrollDuration: long): void
public "mouseDragged"(mouseX: double, mouseY: double, button: integer, dx: double, dy: double): boolean
public "mouseDragged"(mouseX: double, mouseY: double, button: integer, dx: double, dy: double, snapToRows: boolean, rowSize: double): boolean
public "renderScrollBar"(graphics: $GuiGraphics$Type): void
public "renderScrollBar"(graphics: $GuiGraphics$Type, background: integer, alpha: float, scrollBarAlphaOffset: float): void
public "updateDraggingState"(mouseX: double, mouseY: double, button: integer): boolean
public "getScissorBounds"(): $Rectangle
public "getScrollBarX"(maxX: integer): integer
public "getMaxScrollHeight"(): integer
get "bounds"(): $Rectangle
get "maxScroll"(): integer
set "scrollDuration"(value: long)
get "scissorBounds"(): $Rectangle
get "maxScrollHeight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScrollingContainer$Type = ($ScrollingContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScrollingContainer_ = $ScrollingContainer$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$WriterContext" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $WriterContext {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WriterContext$Type = ($WriterContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WriterContext_ = $WriterContext$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$TextFieldBuilder" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$StringListEntry, $StringListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$StringListEntry"
import {$AbstractFieldBuilder, $AbstractFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractFieldBuilder"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $TextFieldBuilder extends $AbstractFieldBuilder<(string), ($StringListEntry), ($TextFieldBuilder)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, value: string)

public "build"(): $StringListEntry
public "setSaveConsumer"(saveConsumer: $Consumer$Type<(string)>): $TextFieldBuilder
public "requireRestart"(): $TextFieldBuilder
public "setTooltip"(tooltip: $Optional$Type<(($Component$Type)[])>): $TextFieldBuilder
public "setErrorSupplier"(errorSupplier: $Function$Type<(string), ($Optional$Type<($Component$Type)>)>): $TextFieldBuilder
public "setDefaultValue"(defaultValue: string): $TextFieldBuilder
public "setDefaultValue"(defaultValue: $Supplier$Type<(string)>): $TextFieldBuilder
set "saveConsumer"(value: $Consumer$Type<(string)>)
set "tooltip"(value: $Optional$Type<(($Component$Type)[])>)
set "errorSupplier"(value: $Function$Type<(string), ($Optional$Type<($Component$Type)>)>)
set "defaultValue"(value: string)
set "defaultValue"(value: $Supplier$Type<(string)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextFieldBuilder$Type = ($TextFieldBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextFieldBuilder_ = $TextFieldBuilder$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$QueuedTooltip" {
import {$FormattedCharSequence, $FormattedCharSequence$Type} from "packages/net/minecraft/util/$FormattedCharSequence"
import {$FormattedText, $FormattedText$Type} from "packages/net/minecraft/network/chat/$FormattedText"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Tooltip, $Tooltip$Type} from "packages/me/shedaniel/clothconfig2/api/$Tooltip"
import {$Point, $Point$Type} from "packages/me/shedaniel/math/$Point"

export class $QueuedTooltip implements $Tooltip {


public static "create"(location: $Point$Type, ...text: ($FormattedText$Type)[]): $QueuedTooltip
public static "create"(location: $Point$Type, ...text: ($FormattedCharSequence$Type)[]): $QueuedTooltip
public static "create"(location: $Point$Type, ...text: ($Component$Type)[]): $QueuedTooltip
public static "create"(location: $Point$Type, text: $List$Type<($Component$Type)>): $QueuedTooltip
public "getText"(): $List<($FormattedCharSequence)>
public "getPoint"(): $Point
public static "of"(location: $Point$Type, ...text: ($FormattedCharSequence$Type)[]): $Tooltip
public static "of"(location: $Point$Type, ...text: ($FormattedText$Type)[]): $Tooltip
public static "of"(location: $Point$Type, ...text: ($Component$Type)[]): $Tooltip
public "getY"(): integer
public "getX"(): integer
get "text"(): $List<($FormattedCharSequence)>
get "point"(): $Point
get "y"(): integer
get "x"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $QueuedTooltip$Type = ($QueuedTooltip);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $QueuedTooltip_ = $QueuedTooltip$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$DoubleValueAnimatorImpl" {
import {$FloatingPoint, $FloatingPoint$Type} from "packages/me/shedaniel/math/$FloatingPoint"
import {$Color, $Color$Type} from "packages/me/shedaniel/math/$Color"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$NumberAnimator, $NumberAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$NumberAnimator"
import {$Dimension, $Dimension$Type} from "packages/me/shedaniel/math/$Dimension"
import {$ValueProvider, $ValueProvider$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueProvider"
import {$ProgressValueAnimator, $ProgressValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ProgressValueAnimator"
import {$FloatingDimension, $FloatingDimension$Type} from "packages/me/shedaniel/math/$FloatingDimension"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"
import {$FloatingRectangle, $FloatingRectangle$Type} from "packages/me/shedaniel/math/$FloatingRectangle"
import {$Point, $Point$Type} from "packages/me/shedaniel/math/$Point"

export class $DoubleValueAnimatorImpl extends $NumberAnimator<(double)> {


public "update"(delta: double): void
public "intValue"(): integer
public "longValue"(): long
public "floatValue"(): float
public "doubleValue"(): double
public "setToNumber"(value: number, duration: long): $NumberAnimator<(double)>
public "setTargetNumber"(value: number): $NumberAnimator<(double)>
public static "ofLong"(): $NumberAnimator<(long)>
public static "ofLong"(initialValue: long): $NumberAnimator<(long)>
public static "ofFloatingPoint"(): $ValueAnimator<($FloatingPoint)>
public static "ofFloatingPoint"(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
/**
 * 
 * @deprecated
 */
public static "ofDimension"(initialValue: $Point$Type): $ValueAnimator<($Point)>
public static "ofDimension"(initialValue: $Dimension$Type): $ValueAnimator<($Dimension)>
public static "ofDimension"(): $ValueAnimator<($Dimension)>
public static "ofRectangle"(initialValue: $Rectangle$Type): $ValueAnimator<($Rectangle)>
public static "ofRectangle"(): $ValueAnimator<($Rectangle)>
public static "ofPoint"(initialValue: $Point$Type): $ValueAnimator<($Point)>
public static "ofPoint"(): $ValueAnimator<($Point)>
public static "ofInt"(): $NumberAnimator<(integer)>
public static "ofInt"(initialValue: integer): $NumberAnimator<(integer)>
public static "ofDouble"(): $NumberAnimator<(double)>
public static "ofDouble"(initialValue: double): $NumberAnimator<(double)>
public static "ofBoolean"(): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(switchPoint: double, initialValue: boolean): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(switchPoint: double): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(initialValue: boolean): $ProgressValueAnimator<(boolean)>
public static "ofFloat"(initialValue: float): $NumberAnimator<(float)>
public static "ofFloat"(): $NumberAnimator<(float)>
public static "ofColor"(): $ValueAnimator<($Color)>
public static "ofColor"(initialValue: $Color$Type): $ValueAnimator<($Color)>
public static "ofFloatingRectangle"(initialValue: $FloatingRectangle$Type): $ValueAnimator<($FloatingRectangle)>
public static "ofFloatingRectangle"(): $ValueAnimator<($FloatingRectangle)>
/**
 * 
 * @deprecated
 */
public static "ofFloatingDimension"(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
public static "ofFloatingDimension"(): $ValueAnimator<($FloatingDimension)>
public static "ofFloatingDimension"(initialValue: $FloatingDimension$Type): $ValueAnimator<($FloatingDimension)>
public static "typicalTransitionTime"(): long
public static "constant"<T>(value: T): $ValueProvider<(T)>
set "targetNumber"(value: number)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleValueAnimatorImpl$Type = ($DoubleValueAnimatorImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleValueAnimatorImpl_ = $DoubleValueAnimatorImpl$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/api/$DeserializationException" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Exception, $Exception$Type} from "packages/java/lang/$Exception"

export class $DeserializationException extends $Exception {

constructor(cause: $Throwable$Type)
constructor(message: string, cause: $Throwable$Type)
constructor(message: string)
constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeserializationException$Type = ($DeserializationException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeserializationException_ = $DeserializationException$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/$ClothConfigDemo" {
import {$ConfigBuilder, $ConfigBuilder$Type} from "packages/me/shedaniel/clothconfig2/api/$ConfigBuilder"

export class $ClothConfigDemo {

constructor()

public static "getConfigBuilderWithDemo"(): $ConfigBuilder
get "configBuilderWithDemo"(): $ConfigBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClothConfigDemo$Type = ($ClothConfigDemo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClothConfigDemo_ = $ClothConfigDemo$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/reader/$UnicodeReader" {
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"

export class $UnicodeReader extends $Reader {

constructor(arg0: $InputStream$Type)

public "read"(cbuf: (character)[], off: integer, len: integer): integer
public "close"(): void
public "getEncoding"(): string
get "encoding"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnicodeReader$Type = ($UnicodeReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnicodeReader_ = $UnicodeReader$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$Constructor" {
import {$SafeConstructor, $SafeConstructor$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$SafeConstructor"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$TypeDescription, $TypeDescription$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$TypeDescription"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$LoaderOptions, $LoaderOptions$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$LoaderOptions"
import {$SafeConstructor$ConstructUndefined, $SafeConstructor$ConstructUndefined$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$SafeConstructor$ConstructUndefined"

export class $Constructor extends $SafeConstructor {
static readonly "undefinedConstructor": $SafeConstructor$ConstructUndefined

constructor(theRoot: $TypeDescription$Type)
constructor(theRoot: $TypeDescription$Type, loadingConfig: $LoaderOptions$Type)
constructor(theRoot: $TypeDescription$Type, moreTDs: $Collection$Type<($TypeDescription$Type)>)
constructor(theRoot: $TypeDescription$Type, moreTDs: $Collection$Type<($TypeDescription$Type)>, loadingConfig: $LoaderOptions$Type)
constructor(theRoot: string)
constructor(theRoot: string, loadingConfig: $LoaderOptions$Type)
constructor(loadingConfig: $LoaderOptions$Type)
constructor(theRoot: $Class$Type<(any)>)
constructor(theRoot: $Class$Type<(any)>, loadingConfig: $LoaderOptions$Type)
constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Constructor$Type = ($Constructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Constructor_ = $Constructor$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/$AbstractTabbedConfigScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$AbstractConfigScreen, $AbstractConfigScreen$Type} from "packages/me/shedaniel/clothconfig2/gui/$AbstractConfigScreen"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$TabbedConfigScreen, $TabbedConfigScreen$Type} from "packages/me/shedaniel/clothconfig2/api/$TabbedConfigScreen"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $AbstractTabbedConfigScreen extends $AbstractConfigScreen implements $TabbedConfigScreen {
 "selectedCategoryIndex": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering


public "getBackgroundLocation"(): $ResourceLocation
public "registerCategoryBackground"(text: string, identifier: $ResourceLocation$Type): void
public "getSelectedCategory"(): $Component
get "backgroundLocation"(): $ResourceLocation
get "selectedCategory"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractTabbedConfigScreen$Type = ($AbstractTabbedConfigScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractTabbedConfigScreen_ = $AbstractTabbedConfigScreen$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/widget/$ColorDisplayWidget" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$EditBox, $EditBox$Type} from "packages/net/minecraft/client/gui/components/$EditBox"
import {$NarrationElementOutput, $NarrationElementOutput$Type} from "packages/net/minecraft/client/gui/narration/$NarrationElementOutput"

export class $ColorDisplayWidget extends $AbstractWidget {
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(textFieldWidget: $EditBox$Type, x: integer, y: integer, size: integer, color: integer)

public "setColor"(color: integer): void
public "m_87963_"(graphics: $GuiGraphics$Type, mouseX: integer, mouseY: integer, delta: float): void
public "onClick"(mouseX: double, mouseY: double): void
public "onRelease"(mouseX: double, mouseY: double): void
public "m_168797_"(narrationElementOutput: $NarrationElementOutput$Type): void
set "color"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorDisplayWidget$Type = ($ColorDisplayWidget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorDisplayWidget_ = $ColorDisplayWidget$Type;
}}
declare module "packages/me/shedaniel/autoconfig/example/$ExampleConfig$ModuleA" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ExampleConfig$PairOfInts, $ExampleConfig$PairOfInts$Type} from "packages/me/shedaniel/autoconfig/example/$ExampleConfig$PairOfInts"
import {$ConfigData, $ConfigData$Type} from "packages/me/shedaniel/autoconfig/$ConfigData"
import {$ExampleConfig$PairOfIntPairs, $ExampleConfig$PairOfIntPairs$Type} from "packages/me/shedaniel/autoconfig/example/$ExampleConfig$PairOfIntPairs"
import {$ExampleConfig$ExampleEnum, $ExampleConfig$ExampleEnum$Type} from "packages/me/shedaniel/autoconfig/example/$ExampleConfig$ExampleEnum"

export class $ExampleConfig$ModuleA implements $ConfigData {
 "aBoolean": boolean
 "anEnum": $ExampleConfig$ExampleEnum
 "anEnumWithButton": $ExampleConfig$ExampleEnum
 "aString": string
 "anObject": $ExampleConfig$PairOfIntPairs
 "list": $List<(integer)>
 "array": (integer)[]
 "complexList": $List<($ExampleConfig$PairOfInts)>
 "complexArray": ($ExampleConfig$PairOfInts)[]

constructor()

public "validatePostLoad"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExampleConfig$ModuleA$Type = ($ExampleConfig$ModuleA);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExampleConfig$ModuleA_ = $ExampleConfig$ModuleA$Type;
}}
declare module "packages/me/shedaniel/autoconfig/example/$ExampleConfig$ModuleB" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ExampleConfig$PairOfInts, $ExampleConfig$PairOfInts$Type} from "packages/me/shedaniel/autoconfig/example/$ExampleConfig$PairOfInts"
import {$ConfigData, $ConfigData$Type} from "packages/me/shedaniel/autoconfig/$ConfigData"
import {$ExampleConfig$PairOfIntPairs, $ExampleConfig$PairOfIntPairs$Type} from "packages/me/shedaniel/autoconfig/example/$ExampleConfig$PairOfIntPairs"

export class $ExampleConfig$ModuleB implements $ConfigData {
 "intSlider": integer
 "longSlider": long
 "anObject": $ExampleConfig$PairOfIntPairs
 "aList": $List<($ExampleConfig$PairOfInts)>
 "color": integer

constructor()

public "validatePostLoad"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExampleConfig$ModuleB$Type = ($ExampleConfig$ModuleB);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExampleConfig$ModuleB_ = $ExampleConfig$ModuleB$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ArrayValueReader" {
import {$ValueReader, $ValueReader$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueReader"
import {$Context, $Context$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$Context"
import {$AtomicInteger, $AtomicInteger$Type} from "packages/java/util/concurrent/atomic/$AtomicInteger"

export class $ArrayValueReader implements $ValueReader {


public "read"(s: string, index: $AtomicInteger$Type, context: $Context$Type): any
public "canRead"(s: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArrayValueReader$Type = ($ArrayValueReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArrayValueReader_ = $ArrayValueReader$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$DocumentEndToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $DocumentEndToken extends $Token {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DocumentEndToken$Type = ($DocumentEndToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DocumentEndToken_ = $DocumentEndToken$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/emitter/$Emitable" {
import {$Event, $Event$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event"

export interface $Emitable {

 "emit"(arg0: $Event$Type): void

(arg0: $Event$Type): void
}

export namespace $Emitable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Emitable$Type = ($Emitable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Emitable_ = $Emitable$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$AbstractConstruct" {
import {$Node, $Node$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Node"
import {$Construct, $Construct$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$Construct"

export class $AbstractConstruct implements $Construct {

constructor()

public "construct2ndStep"(node: $Node$Type, data: any): void
public "construct"(arg0: $Node$Type): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractConstruct$Type = ($AbstractConstruct);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractConstruct_ = $AbstractConstruct$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/widget/$DynamicSmoothScrollingEntryListWidget" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$DynamicEntryListWidget$Entry, $DynamicEntryListWidget$Entry$Type} from "packages/me/shedaniel/clothconfig2/gui/widget/$DynamicEntryListWidget$Entry"
import {$DynamicEntryListWidget, $DynamicEntryListWidget$Type} from "packages/me/shedaniel/clothconfig2/gui/widget/$DynamicEntryListWidget"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $DynamicSmoothScrollingEntryListWidget<E extends $DynamicEntryListWidget$Entry<(E)>> extends $DynamicEntryListWidget<(E)> {
 "width": integer
 "height": integer
 "top": integer
 "bottom": integer
 "right": integer
 "left": integer

constructor(client: $Minecraft$Type, width: integer, height: integer, top: integer, bottom: integer, backgroundLocation: $ResourceLocation$Type)

public "offset"(value: double, animated: boolean): void
public "render"(graphics: $GuiGraphics$Type, mouseX: integer, mouseY: integer, delta: float): void
public "mouseScrolled"(mouseX: double, mouseY: double, amount: double): boolean
public "mouseDragged"(mouseX: double, mouseY: double, button: integer, deltaX: double, deltaY: double): boolean
public "scrollTo"(value: double, animated: boolean, duration: long): void
public "scrollTo"(value: double, animated: boolean): void
public "capYPosition"(scroll: double): void
public "isSmoothScrolling"(): boolean
public "setSmoothScrolling"(smoothScrolling: boolean): void
get "smoothScrolling"(): boolean
set "smoothScrolling"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicSmoothScrollingEntryListWidget$Type<E> = ($DynamicSmoothScrollingEntryListWidget<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicSmoothScrollingEntryListWidget_<E> = $DynamicSmoothScrollingEntryListWidget$Type<(E)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$NumberAnimator" {
import {$FloatingPoint, $FloatingPoint$Type} from "packages/me/shedaniel/math/$FloatingPoint"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Color, $Color$Type} from "packages/me/shedaniel/math/$Color"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$Dimension, $Dimension$Type} from "packages/me/shedaniel/math/$Dimension"
import {$ValueProvider, $ValueProvider$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueProvider"
import {$ProgressValueAnimator, $ProgressValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ProgressValueAnimator"
import {$FloatingDimension, $FloatingDimension$Type} from "packages/me/shedaniel/math/$FloatingDimension"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"
import {$FloatingRectangle, $FloatingRectangle$Type} from "packages/me/shedaniel/math/$FloatingRectangle"
import {$Point, $Point$Type} from "packages/me/shedaniel/math/$Point"

export class $NumberAnimator<T extends number> extends number implements $ValueAnimator<(T)> {

constructor()

public "setTarget"(value: float): $NumberAnimator<(T)>
public "setTarget"(value: long): $NumberAnimator<(T)>
public "setTarget"(value: integer): $NumberAnimator<(T)>
public "setTarget"(target: T): $ValueAnimator<(T)>
public "setTarget"(value: double): $NumberAnimator<(T)>
public "asInt"(): $NumberAnimator<(integer)>
public "asDouble"(): $NumberAnimator<(double)>
public "setAsNumber"(value: number): $NumberAnimator<(T)>
public "setToNumber"(arg0: number, arg1: long): $NumberAnimator<(T)>
public "setTargetNumber"(arg0: number): $NumberAnimator<(T)>
public "setAs"(value: T): $NumberAnimator<(T)>
public "setAs"(value: integer): $NumberAnimator<(T)>
public "setAs"(value: double): $NumberAnimator<(T)>
public "setAs"(value: float): $NumberAnimator<(T)>
public "setAs"(value: long): $NumberAnimator<(T)>
public "setTo"(value: long, duration: long): $NumberAnimator<(T)>
public "setTo"(value: integer, duration: long): $NumberAnimator<(T)>
public "setTo"(value: T, duration: long): $NumberAnimator<(T)>
public "setTo"(value: double, duration: long): $NumberAnimator<(T)>
public "setTo"(value: float, duration: long): $NumberAnimator<(T)>
public "asFloat"(): $NumberAnimator<(float)>
public "asLong"(): $NumberAnimator<(long)>
public "map"<R>(converter: $Function$Type<(T), (R)>, backwardsConverter: $Function$Type<(R), (T)>): $ValueAnimator<(R)>
public static "ofLong"(): $NumberAnimator<(long)>
public static "ofLong"(initialValue: long): $NumberAnimator<(long)>
public static "ofFloatingPoint"(): $ValueAnimator<($FloatingPoint)>
public static "ofFloatingPoint"(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
/**
 * 
 * @deprecated
 */
public static "ofDimension"(initialValue: $Point$Type): $ValueAnimator<($Point)>
public static "ofDimension"(initialValue: $Dimension$Type): $ValueAnimator<($Dimension)>
public static "ofDimension"(): $ValueAnimator<($Dimension)>
public static "ofRectangle"(initialValue: $Rectangle$Type): $ValueAnimator<($Rectangle)>
public static "ofRectangle"(): $ValueAnimator<($Rectangle)>
public static "ofPoint"(initialValue: $Point$Type): $ValueAnimator<($Point)>
public static "ofPoint"(): $ValueAnimator<($Point)>
public static "ofInt"(): $NumberAnimator<(integer)>
public static "ofInt"(initialValue: integer): $NumberAnimator<(integer)>
public static "ofDouble"(): $NumberAnimator<(double)>
public static "ofDouble"(initialValue: double): $NumberAnimator<(double)>
public static "ofBoolean"(): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(switchPoint: double, initialValue: boolean): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(switchPoint: double): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(initialValue: boolean): $ProgressValueAnimator<(boolean)>
public static "ofFloat"(initialValue: float): $NumberAnimator<(float)>
public static "ofFloat"(): $NumberAnimator<(float)>
public static "ofColor"(): $ValueAnimator<($Color)>
public static "ofColor"(initialValue: $Color$Type): $ValueAnimator<($Color)>
public "completeImmediately"(): void
public static "ofFloatingRectangle"(initialValue: $FloatingRectangle$Type): $ValueAnimator<($FloatingRectangle)>
public static "ofFloatingRectangle"(): $ValueAnimator<($FloatingRectangle)>
/**
 * 
 * @deprecated
 */
public static "ofFloatingDimension"(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
public static "ofFloatingDimension"(): $ValueAnimator<($FloatingDimension)>
public static "ofFloatingDimension"(initialValue: $FloatingDimension$Type): $ValueAnimator<($FloatingDimension)>
public static "typicalTransitionTime"(): long
public "value"(): T
public "target"(): T
public "update"(arg0: double): void
public static "constant"<T>(value: T): $ValueProvider<(T)>
set "asNumber"(value: number)
set "targetNumber"(value: number)
set "as"(value: T)
set "as"(value: integer)
set "as"(value: double)
set "as"(value: float)
set "as"(value: long)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NumberAnimator$Type<T> = ($NumberAnimator<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NumberAnimator_<T> = $NumberAnimator$Type<(T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$InlineTableValueReader" {
import {$ValueReader, $ValueReader$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueReader"
import {$Context, $Context$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$Context"
import {$AtomicInteger, $AtomicInteger$Type} from "packages/java/util/concurrent/atomic/$AtomicInteger"

export class $InlineTableValueReader implements $ValueReader {


public "read"(s: string, sharedIndex: $AtomicInteger$Type, context: $Context$Type): any
public "canRead"(s: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InlineTableValueReader$Type = ($InlineTableValueReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InlineTableValueReader_ = $InlineTableValueReader$Type;
}}
declare module "packages/me/shedaniel/math/$Color" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Color {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "ofRGBA"(arg0: float, arg1: float, arg2: float, arg3: float): $Color
public static "ofRGBA"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $Color
public static "ofTransparent"(arg0: integer): $Color
public static "ofOpaque"(arg0: integer): $Color
public static "ofRGB"(arg0: integer, arg1: integer, arg2: integer): $Color
public static "ofRGB"(arg0: float, arg1: float, arg2: float): $Color
public static "ofHSB"(arg0: float, arg1: float, arg2: float): $Color
public "getRed"(): integer
public "getGreen"(): integer
public "getBlue"(): integer
public "getAlpha"(): integer
public "getColor"(): integer
public static "HSBtoRGB"(arg0: float, arg1: float, arg2: float): integer
public "brighter"(arg0: double): $Color
public "darker"(arg0: double): $Color
get "red"(): integer
get "green"(): integer
get "blue"(): integer
get "alpha"(): integer
get "color"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Color$Type = ($Color);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Color_ = $Color$Type;
}}
declare module "packages/me/shedaniel/autoconfig/serializer/$YamlConfigSerializer" {
import {$Config, $Config$Type} from "packages/me/shedaniel/autoconfig/annotation/$Config"
import {$Yaml, $Yaml$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$Yaml"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ConfigSerializer, $ConfigSerializer$Type} from "packages/me/shedaniel/autoconfig/serializer/$ConfigSerializer"
import {$ConfigData, $ConfigData$Type} from "packages/me/shedaniel/autoconfig/$ConfigData"

export class $YamlConfigSerializer<T extends $ConfigData> implements $ConfigSerializer<(T)> {

constructor(definition: $Config$Type, configClass: $Class$Type<(T)>, yaml: $Yaml$Type)
constructor(definition: $Config$Type, configClass: $Class$Type<(T)>)

public "deserialize"(): T
public "createDefault"(): T
public "serialize"(config: T): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $YamlConfigSerializer$Type<T> = ($YamlConfigSerializer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $YamlConfigSerializer_<T> = $YamlConfigSerializer$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$MappingValueAnimator" {
import {$FloatingPoint, $FloatingPoint$Type} from "packages/me/shedaniel/math/$FloatingPoint"
import {$Color, $Color$Type} from "packages/me/shedaniel/math/$Color"
import {$NumberAnimator, $NumberAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$NumberAnimator"
import {$Dimension, $Dimension$Type} from "packages/me/shedaniel/math/$Dimension"
import {$ProgressValueAnimator, $ProgressValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ProgressValueAnimator"
import {$FloatingDimension, $FloatingDimension$Type} from "packages/me/shedaniel/math/$FloatingDimension"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"
import {$FloatingRectangle, $FloatingRectangle$Type} from "packages/me/shedaniel/math/$FloatingRectangle"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ValueProvider, $ValueProvider$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueProvider"
import {$Point, $Point$Type} from "packages/me/shedaniel/math/$Point"

export class $MappingValueAnimator<T, R> implements $ValueAnimator<(R)> {


public "value"(): R
public "target"(): R
public "update"(delta: double): void
public "setTarget"(target: R): $ValueAnimator<(R)>
public "setTo"(value: R, duration: long): $ValueAnimator<(R)>
public "map"<R>(converter: $Function$Type<(R), (R)>, backwardsConverter: $Function$Type<(R), (R)>): $ValueAnimator<(R)>
public static "ofLong"(): $NumberAnimator<(long)>
public static "ofLong"(initialValue: long): $NumberAnimator<(long)>
public static "ofFloatingPoint"(): $ValueAnimator<($FloatingPoint)>
public static "ofFloatingPoint"(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
/**
 * 
 * @deprecated
 */
public static "ofDimension"(initialValue: $Point$Type): $ValueAnimator<($Point)>
public static "ofDimension"(initialValue: $Dimension$Type): $ValueAnimator<($Dimension)>
public static "ofDimension"(): $ValueAnimator<($Dimension)>
public static "ofRectangle"(initialValue: $Rectangle$Type): $ValueAnimator<($Rectangle)>
public static "ofRectangle"(): $ValueAnimator<($Rectangle)>
public static "ofPoint"(initialValue: $Point$Type): $ValueAnimator<($Point)>
public static "ofPoint"(): $ValueAnimator<($Point)>
public "withConvention"(convention: $Supplier$Type<(R)>, duration: long): $ValueAnimator<(R)>
public static "ofInt"(): $NumberAnimator<(integer)>
public static "ofInt"(initialValue: integer): $NumberAnimator<(integer)>
public "setAs"(value: R): $ValueAnimator<(R)>
public static "ofDouble"(): $NumberAnimator<(double)>
public static "ofDouble"(initialValue: double): $NumberAnimator<(double)>
public static "ofBoolean"(): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(switchPoint: double, initialValue: boolean): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(switchPoint: double): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(initialValue: boolean): $ProgressValueAnimator<(boolean)>
public static "ofFloat"(initialValue: float): $NumberAnimator<(float)>
public static "ofFloat"(): $NumberAnimator<(float)>
public static "ofColor"(): $ValueAnimator<($Color)>
public static "ofColor"(initialValue: $Color$Type): $ValueAnimator<($Color)>
public "completeImmediately"(): void
public static "ofFloatingRectangle"(initialValue: $FloatingRectangle$Type): $ValueAnimator<($FloatingRectangle)>
public static "ofFloatingRectangle"(): $ValueAnimator<($FloatingRectangle)>
/**
 * 
 * @deprecated
 */
public static "ofFloatingDimension"(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
public static "ofFloatingDimension"(): $ValueAnimator<($FloatingDimension)>
public static "ofFloatingDimension"(initialValue: $FloatingDimension$Type): $ValueAnimator<($FloatingDimension)>
public static "typicalTransitionTime"(): long
public static "constant"<T>(value: R): $ValueProvider<(R)>
set "as"(value: R)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MappingValueAnimator$Type<T, R> = ($MappingValueAnimator<(T), (R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MappingValueAnimator_<T, R> = $MappingValueAnimator$Type<(T), (R)>;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$TextDescriptionBuilder" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$FieldBuilder, $FieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$FieldBuilder"
import {$TextListEntry, $TextListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$TextListEntry"

export class $TextDescriptionBuilder extends $FieldBuilder<($Component), ($TextListEntry), ($TextDescriptionBuilder)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, value: $Component$Type)

public "build"(): $TextListEntry
public "setColor"(color: integer): $TextDescriptionBuilder
public "setTooltipSupplier"(tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>): $TextDescriptionBuilder
public "requireRestart"(requireRestart: boolean): void
public "setTooltip"(...tooltip: ($Component$Type)[]): $TextDescriptionBuilder
public "setTooltip"(tooltip: $Optional$Type<(($Component$Type)[])>): $TextDescriptionBuilder
set "color"(value: integer)
set "tooltipSupplier"(value: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
set "tooltip"(value: ($Component$Type)[])
set "tooltip"(value: $Optional$Type<(($Component$Type)[])>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextDescriptionBuilder$Type = ($TextDescriptionBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextDescriptionBuilder_ = $TextDescriptionBuilder$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/emitter/$Emitter" {
import {$DumperOptions, $DumperOptions$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions"
import {$Emitable, $Emitable$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/emitter/$Emitable"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"
import {$Event, $Event$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event"

export class $Emitter implements $Emitable {
static readonly "MIN_INDENT": integer
static readonly "MAX_INDENT": integer

constructor(stream: $Writer$Type, opts: $DumperOptions$Type)

public "emit"(event: $Event$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Emitter$Type = ($Emitter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Emitter_ = $Emitter$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$ConfigScreen" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$Tooltip, $Tooltip$Type} from "packages/me/shedaniel/clothconfig2/api/$Tooltip"

export interface $ConfigScreen {

 "setSavingRunnable"(arg0: $Runnable$Type): void
 "saveAll"(arg0: boolean): void
 "isRequiresRestart"(): boolean
 "isEdited"(): boolean
 "matchesSearch"(arg0: $Iterator$Type<(string)>): boolean
 "getBackgroundLocation"(): $ResourceLocation
 "addTooltip"(arg0: $Tooltip$Type): void
 "setAfterInitConsumer"(arg0: $Consumer$Type<($Screen$Type)>): void
}

export namespace $ConfigScreen {
const probejs$$marker: never
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
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$NonPrintableStyle" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DumperOptions$NonPrintableStyle extends $Enum<($DumperOptions$NonPrintableStyle)> {
static readonly "BINARY": $DumperOptions$NonPrintableStyle
static readonly "ESCAPE": $DumperOptions$NonPrintableStyle


public static "values"(): ($DumperOptions$NonPrintableStyle)[]
public static "valueOf"(name: string): $DumperOptions$NonPrintableStyle
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DumperOptions$NonPrintableStyle$Type = (("binary") | ("escape")) | ($DumperOptions$NonPrintableStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DumperOptions$NonPrintableStyle_ = $DumperOptions$NonPrintableStyle$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/external/com/google/gdata/util/common/base/$UnicodeEscaper" {
import {$Appendable, $Appendable$Type} from "packages/java/lang/$Appendable"
import {$Escaper, $Escaper$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/external/com/google/gdata/util/common/base/$Escaper"

export class $UnicodeEscaper implements $Escaper {

constructor()

public "escape"(out: $Appendable$Type): $Appendable
public "escape"(string: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnicodeEscaper$Type = ($UnicodeEscaper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnicodeEscaper_ = $UnicodeEscaper$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/serializer/$NumberAnchorGenerator" {
import {$AnchorGenerator, $AnchorGenerator$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/serializer/$AnchorGenerator"
import {$Node, $Node$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Node"

export class $NumberAnchorGenerator implements $AnchorGenerator {

constructor(lastAnchorId: integer)

public "nextAnchor"(node: $Node$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NumberAnchorGenerator$Type = ($NumberAnchorGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NumberAnchorGenerator_ = $NumberAnchorGenerator$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$NestedListListEntry$NestedListCell" {
import {$NestedListListEntry, $NestedListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$NestedListListEntry"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ReferenceProvider, $ReferenceProvider$Type} from "packages/me/shedaniel/clothconfig2/api/$ReferenceProvider"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$NarratableEntry$NarrationPriority, $NarratableEntry$NarrationPriority$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry$NarrationPriority"
import {$AbstractListListEntry$AbstractListCell, $AbstractListListEntry$AbstractListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractListListEntry$AbstractListCell"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$NarrationElementOutput, $NarrationElementOutput$Type} from "packages/net/minecraft/client/gui/narration/$NarrationElementOutput"
import {$AbstractConfigEntry, $AbstractConfigEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigEntry"

export class $NestedListListEntry$NestedListCell<T, INNER extends $AbstractConfigListEntry<(T)>> extends $AbstractListListEntry$AbstractListCell<(T), ($NestedListListEntry$NestedListCell<(T), (INNER)>), ($NestedListListEntry<(T), (INNER)>)> implements $ReferenceProvider<(T)> {

constructor(value: T, listListEntry: $NestedListListEntry$Type<(T), (INNER)>, nestedEntry: INNER)

public "getValue"(): T
public "getError"(): $Optional<($Component)>
public "render"(graphics: $GuiGraphics$Type, index: integer, y: integer, x: integer, entryWidth: integer, entryHeight: integer, mouseX: integer, mouseY: integer, isSelected: boolean, delta: float): void
public "children"(): $List<(any)>
public "updateNarration"(narrationElementOutput: $NarrationElementOutput$Type): void
public "narrationPriority"(): $NarratableEntry$NarrationPriority
public "isRequiresRestart"(): boolean
public "isEdited"(): boolean
public "updateSelected"(isSelected: boolean): void
public "onDelete"(): void
public "getCellHeight"(): integer
public "provideReferenceEntry"(): $AbstractConfigEntry<(T)>
public "onAdd"(): void
get "value"(): T
get "error"(): $Optional<($Component)>
get "requiresRestart"(): boolean
get "edited"(): boolean
get "cellHeight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NestedListListEntry$NestedListCell$Type<T, INNER> = ($NestedListListEntry$NestedListCell<(T), (INNER)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NestedListListEntry$NestedListCell_<T, INNER> = $NestedListListEntry$NestedListCell$Type<(T), (INNER)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$ProgressValueAnimator" {
import {$FloatingPoint, $FloatingPoint$Type} from "packages/me/shedaniel/math/$FloatingPoint"
import {$Color, $Color$Type} from "packages/me/shedaniel/math/$Color"
import {$NumberAnimator, $NumberAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$NumberAnimator"
import {$Dimension, $Dimension$Type} from "packages/me/shedaniel/math/$Dimension"
import {$FloatingDimension, $FloatingDimension$Type} from "packages/me/shedaniel/math/$FloatingDimension"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"
import {$FloatingRectangle, $FloatingRectangle$Type} from "packages/me/shedaniel/math/$FloatingRectangle"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ValueProvider, $ValueProvider$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueProvider"
import {$Point, $Point$Type} from "packages/me/shedaniel/math/$Point"

export interface $ProgressValueAnimator<T> extends $ValueAnimator<(T)> {

 "setTarget"(arg0: T): $ProgressValueAnimator<(T)>
 "progress"(): double
 "setTo"(arg0: T, arg1: long): $ProgressValueAnimator<(T)>
 "map"<R>(converter: $Function$Type<(T), (R)>, backwardsConverter: $Function$Type<(R), (T)>): $ValueAnimator<(R)>
 "withConvention"(convention: $Supplier$Type<(T)>, duration: long): $ValueAnimator<(T)>
 "completeImmediately"(): void
 "value"(): T
 "target"(): T
 "update"(arg0: double): void
}

export namespace $ProgressValueAnimator {
function mapProgress<R>(parent: $NumberAnimator$Type<(any)>, converter: $Function$Type<(double), (R)>, backwardsConverter: $Function$Type<(R), (double)>): $ProgressValueAnimator<(R)>
function ofLong(): $NumberAnimator<(long)>
function ofLong(initialValue: long): $NumberAnimator<(long)>
function ofFloatingPoint(): $ValueAnimator<($FloatingPoint)>
function ofFloatingPoint(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
function ofDimension(initialValue: $Point$Type): $ValueAnimator<($Point)>
function ofDimension(initialValue: $Dimension$Type): $ValueAnimator<($Dimension)>
function ofDimension(): $ValueAnimator<($Dimension)>
function ofRectangle(initialValue: $Rectangle$Type): $ValueAnimator<($Rectangle)>
function ofRectangle(): $ValueAnimator<($Rectangle)>
function ofPoint(initialValue: $Point$Type): $ValueAnimator<($Point)>
function ofPoint(): $ValueAnimator<($Point)>
function ofInt(): $NumberAnimator<(integer)>
function ofInt(initialValue: integer): $NumberAnimator<(integer)>
function ofDouble(): $NumberAnimator<(double)>
function ofDouble(initialValue: double): $NumberAnimator<(double)>
function ofBoolean(): $ProgressValueAnimator<(boolean)>
function ofBoolean(switchPoint: double, initialValue: boolean): $ProgressValueAnimator<(boolean)>
function ofBoolean(switchPoint: double): $ProgressValueAnimator<(boolean)>
function ofBoolean(initialValue: boolean): $ProgressValueAnimator<(boolean)>
function ofFloat(initialValue: float): $NumberAnimator<(float)>
function ofFloat(): $NumberAnimator<(float)>
function ofColor(): $ValueAnimator<($Color)>
function ofColor(initialValue: $Color$Type): $ValueAnimator<($Color)>
function ofFloatingRectangle(initialValue: $FloatingRectangle$Type): $ValueAnimator<($FloatingRectangle)>
function ofFloatingRectangle(): $ValueAnimator<($FloatingRectangle)>
function ofFloatingDimension(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
function ofFloatingDimension(): $ValueAnimator<($FloatingDimension)>
function ofFloatingDimension(initialValue: $FloatingDimension$Type): $ValueAnimator<($FloatingDimension)>
function typicalTransitionTime(): long
function constant<T>(value: T): $ValueProvider<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProgressValueAnimator$Type<T> = ($ProgressValueAnimator<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProgressValueAnimator_<T> = $ProgressValueAnimator$Type<(T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$MethodProperty" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$PropertyDescriptor, $PropertyDescriptor$Type} from "packages/java/beans/$PropertyDescriptor"
import {$GenericProperty, $GenericProperty$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$GenericProperty"

export class $MethodProperty extends $GenericProperty {

constructor(property: $PropertyDescriptor$Type)

public "get"(object: any): any
public "getAnnotation"<A extends $Annotation>(annotationType: $Class$Type<(A)>): A
public "getAnnotations"(): $List<($Annotation)>
public "set"(object: any, value: any): void
public "isReadable"(): boolean
public "isWritable"(): boolean
get "annotations"(): $List<($Annotation)>
get "readable"(): boolean
get "writable"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MethodProperty$Type = ($MethodProperty);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MethodProperty_ = $MethodProperty$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions" {
import {$DumperOptions$NonPrintableStyle, $DumperOptions$NonPrintableStyle$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$NonPrintableStyle"
import {$DumperOptions$Version, $DumperOptions$Version$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$Version"
import {$AnchorGenerator, $AnchorGenerator$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/serializer/$AnchorGenerator"
import {$DumperOptions$ScalarStyle, $DumperOptions$ScalarStyle$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$ScalarStyle"
import {$DumperOptions$FlowStyle, $DumperOptions$FlowStyle$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle"
import {$DumperOptions$LineBreak, $DumperOptions$LineBreak$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$LineBreak"
import {$TimeZone, $TimeZone$Type} from "packages/java/util/$TimeZone"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $DumperOptions {

constructor()

public "setTimeZone"(timeZone: $TimeZone$Type): void
public "getTimeZone"(): $TimeZone
public "getVersion"(): $DumperOptions$Version
public "setVersion"(version: $DumperOptions$Version$Type): void
public "setWidth"(bestWidth: integer): void
public "setIndent"(indent: integer): void
public "setSplitLines"(splitLines: boolean): void
public "setCanonical"(canonical: boolean): void
public "setAllowUnicode"(allowUnicode: boolean): void
public "setIndicatorIndent"(indicatorIndent: integer): void
public "isAllowUnicode"(): boolean
public "isCanonical"(): boolean
public "setPrettyFlow"(prettyFlow: boolean): void
public "isPrettyFlow"(): boolean
public "isExplicitStart"(): boolean
public "getAnchorGenerator"(): $AnchorGenerator
public "getLineBreak"(): $DumperOptions$LineBreak
public "setLineBreak"(lineBreak: $DumperOptions$LineBreak$Type): void
public "isExplicitEnd"(): boolean
public "setAnchorGenerator"(anchorGenerator: $AnchorGenerator$Type): void
public "setTags"(tags: $Map$Type<(string), (string)>): void
public "setExplicitStart"(explicitStart: boolean): void
public "getSplitLines"(): boolean
public "setExplicitEnd"(explicitEnd: boolean): void
public "getWidth"(): integer
public "getIndicatorIndent"(): integer
public "setDefaultScalarStyle"(defaultStyle: $DumperOptions$ScalarStyle$Type): void
public "isAllowReadOnlyProperties"(): boolean
public "getDefaultFlowStyle"(): $DumperOptions$FlowStyle
public "getIndentWithIndicator"(): boolean
public "getDefaultScalarStyle"(): $DumperOptions$ScalarStyle
public "setAllowReadOnlyProperties"(allowReadOnlyProperties: boolean): void
public "setDefaultFlowStyle"(defaultFlowStyle: $DumperOptions$FlowStyle$Type): void
public "getIndent"(): integer
public "getTags"(): $Map<(string), (string)>
public "setNonPrintableStyle"(style: $DumperOptions$NonPrintableStyle$Type): void
public "setIndentWithIndicator"(indentWithIndicator: boolean): void
public "getMaxSimpleKeyLength"(): integer
public "setMaxSimpleKeyLength"(maxSimpleKeyLength: integer): void
public "getNonPrintableStyle"(): $DumperOptions$NonPrintableStyle
set "timeZone"(value: $TimeZone$Type)
get "timeZone"(): $TimeZone
get "version"(): $DumperOptions$Version
set "version"(value: $DumperOptions$Version$Type)
set "width"(value: integer)
set "indent"(value: integer)
set "splitLines"(value: boolean)
set "canonical"(value: boolean)
set "allowUnicode"(value: boolean)
set "indicatorIndent"(value: integer)
get "allowUnicode"(): boolean
get "canonical"(): boolean
set "prettyFlow"(value: boolean)
get "prettyFlow"(): boolean
get "explicitStart"(): boolean
get "anchorGenerator"(): $AnchorGenerator
get "lineBreak"(): $DumperOptions$LineBreak
set "lineBreak"(value: $DumperOptions$LineBreak$Type)
get "explicitEnd"(): boolean
set "anchorGenerator"(value: $AnchorGenerator$Type)
set "tags"(value: $Map$Type<(string), (string)>)
set "explicitStart"(value: boolean)
get "splitLines"(): boolean
set "explicitEnd"(value: boolean)
get "width"(): integer
get "indicatorIndent"(): integer
set "defaultScalarStyle"(value: $DumperOptions$ScalarStyle$Type)
get "allowReadOnlyProperties"(): boolean
get "defaultFlowStyle"(): $DumperOptions$FlowStyle
get "indentWithIndicator"(): boolean
get "defaultScalarStyle"(): $DumperOptions$ScalarStyle
set "allowReadOnlyProperties"(value: boolean)
set "defaultFlowStyle"(value: $DumperOptions$FlowStyle$Type)
get "indent"(): integer
get "tags"(): $Map<(string), (string)>
set "nonPrintableStyle"(value: $DumperOptions$NonPrintableStyle$Type)
set "indentWithIndicator"(value: boolean)
get "maxSimpleKeyLength"(): integer
set "maxSimpleKeyLength"(value: integer)
get "nonPrintableStyle"(): $DumperOptions$NonPrintableStyle
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DumperOptions$Type = ($DumperOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DumperOptions_ = $DumperOptions$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$ReferenceProvider" {
import {$AbstractConfigEntry, $AbstractConfigEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigEntry"

export interface $ReferenceProvider<T> {

 "provideReferenceEntry"(): $AbstractConfigEntry<(T)>

(): $AbstractConfigEntry<(T)>
}

export namespace $ReferenceProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReferenceProvider$Type<T> = ($ReferenceProvider<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReferenceProvider_<T> = $ReferenceProvider$Type<(T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$MappingEndEvent" {
import {$Event$ID, $Event$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$CollectionEndEvent, $CollectionEndEvent$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$CollectionEndEvent"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $MappingEndEvent extends $CollectionEndEvent {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getEventId"(): $Event$ID
get "eventId"(): $Event$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MappingEndEvent$Type = ($MappingEndEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MappingEndEvent_ = $MappingEndEvent$Type;
}}
declare module "packages/me/shedaniel/autoconfig/gui/registry/api/$GuiTransformer" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$GuiRegistryAccess, $GuiRegistryAccess$Type} from "packages/me/shedaniel/autoconfig/gui/registry/api/$GuiRegistryAccess"

export interface $GuiTransformer {

 "transform"(arg0: $List$Type<($AbstractConfigListEntry$Type)>, arg1: string, arg2: $Field$Type, arg3: any, arg4: any, arg5: $GuiRegistryAccess$Type): $List<($AbstractConfigListEntry)>

(arg0: $List$Type<($AbstractConfigListEntry$Type)>, arg1: string, arg2: $Field$Type, arg3: any, arg4: any, arg5: $GuiRegistryAccess$Type): $List<($AbstractConfigListEntry)>
}

export namespace $GuiTransformer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiTransformer$Type = ($GuiTransformer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiTransformer_ = $GuiTransformer$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event$ID" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Event$ID extends $Enum<($Event$ID)> {
static readonly "Alias": $Event$ID
static readonly "DocumentEnd": $Event$ID
static readonly "DocumentStart": $Event$ID
static readonly "MappingEnd": $Event$ID
static readonly "MappingStart": $Event$ID
static readonly "Scalar": $Event$ID
static readonly "SequenceEnd": $Event$ID
static readonly "SequenceStart": $Event$ID
static readonly "StreamEnd": $Event$ID
static readonly "StreamStart": $Event$ID


public static "values"(): ($Event$ID)[]
public static "valueOf"(name: string): $Event$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Event$ID$Type = (("mappingend") | ("scalar") | ("sequencestart") | ("documentend") | ("mappingstart") | ("alias") | ("streamstart") | ("sequenceend") | ("streamend") | ("documentstart")) | ($Event$ID);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Event$ID_ = $Event$ID$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$ConfigBuilder" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ConfigEntryBuilder, $ConfigEntryBuilder$Type} from "packages/me/shedaniel/clothconfig2/api/$ConfigEntryBuilder"
import {$ConfigCategory, $ConfigCategory$Type} from "packages/me/shedaniel/clothconfig2/api/$ConfigCategory"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ConfigEntryBuilderImpl, $ConfigEntryBuilderImpl$Type} from "packages/me/shedaniel/clothconfig2/impl/$ConfigEntryBuilderImpl"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export interface $ConfigBuilder {

 "build"(): $Screen
 "entryBuilder"(): $ConfigEntryBuilder
 "setSavingRunnable"(arg0: $Runnable$Type): $ConfigBuilder
 "getOrCreateCategory"(arg0: $Component$Type): $ConfigCategory
 "setTitle"(arg0: $Component$Type): $ConfigBuilder
 "isEditable"(): boolean
 "setEditable"(arg0: boolean): $ConfigBuilder
 "getTitle"(): $Component
 "hasCategory"(arg0: $Component$Type): boolean
 "removeCategory"(arg0: $Component$Type): $ConfigBuilder
 "setParentScreen"(arg0: $Screen$Type): $ConfigBuilder
 "getAfterInitConsumer"(): $Consumer<($Screen)>
 "setFallbackCategory"(arg0: $ConfigCategory$Type): $ConfigBuilder
 "setShouldListSmoothScroll"(arg0: boolean): $ConfigBuilder
 "setShouldTabsSmoothScroll"(arg0: boolean): $ConfigBuilder
 "isTabsSmoothScrolling"(): boolean
 "setAfterInitConsumer"(arg0: $Consumer$Type<($Screen$Type)>): $ConfigBuilder
 "hasTransparentBackground"(): boolean
 "setTransparentBackground"(arg0: boolean): $ConfigBuilder
 "setGlobalizedExpanded"(arg0: boolean): void
 "setDefaultBackgroundTexture"(arg0: $ResourceLocation$Type): $ConfigBuilder
 "removeCategoryIfExists"(arg0: $Component$Type): $ConfigBuilder
 "getDefaultBackgroundTexture"(): $ResourceLocation
 "transparentBackground"(): $ConfigBuilder
/**
 * 
 * @deprecated
 */
 "setDoesProcessErrors"(processErrors: boolean): $ConfigBuilder
 "isListSmoothScrolling"(): boolean
 "setGlobalized"(arg0: boolean): void
 "isAlwaysShowTabs"(): boolean
 "doesConfirmSave"(): boolean
 "setAlwaysShowTabs"(arg0: boolean): $ConfigBuilder
 "alwaysShowTabs"(): $ConfigBuilder
/**
 * 
 * @deprecated
 */
 "doesProcessErrors"(): boolean
 "getParentScreen"(): $Screen
 "getSavingRunnable"(): $Runnable
 "solidBackground"(): $ConfigBuilder
/**
 * 
 * @deprecated
 */
 "getEntryBuilder"(): $ConfigEntryBuilderImpl
 "setDoesConfirmSave"(arg0: boolean): $ConfigBuilder
}

export namespace $ConfigBuilder {
function create(): $ConfigBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigBuilder$Type = ($ConfigBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigBuilder_ = $ConfigBuilder$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$DatePolicy" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $DatePolicy {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DatePolicy$Type = ($DatePolicy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DatePolicy_ = $DatePolicy$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/representer/$Representer" {
import {$PropertyUtils, $PropertyUtils$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$PropertyUtils"
import {$DumperOptions, $DumperOptions$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions"
import {$TypeDescription, $TypeDescription$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$TypeDescription"
import {$SafeRepresenter, $SafeRepresenter$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/representer/$SafeRepresenter"

export class $Representer extends $SafeRepresenter {

constructor()
constructor(options: $DumperOptions$Type)

public "setPropertyUtils"(propertyUtils: $PropertyUtils$Type): void
public "addTypeDescription"(td: $TypeDescription$Type): $TypeDescription
set "propertyUtils"(value: $PropertyUtils$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Representer$Type = ($Representer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Representer_ = $Representer$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$MappingProgressValueAnimator" {
import {$FloatingPoint, $FloatingPoint$Type} from "packages/me/shedaniel/math/$FloatingPoint"
import {$Color, $Color$Type} from "packages/me/shedaniel/math/$Color"
import {$NumberAnimator, $NumberAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$NumberAnimator"
import {$Dimension, $Dimension$Type} from "packages/me/shedaniel/math/$Dimension"
import {$ProgressValueAnimator, $ProgressValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ProgressValueAnimator"
import {$FloatingDimension, $FloatingDimension$Type} from "packages/me/shedaniel/math/$FloatingDimension"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"
import {$FloatingRectangle, $FloatingRectangle$Type} from "packages/me/shedaniel/math/$FloatingRectangle"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ValueProvider, $ValueProvider$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueProvider"
import {$Point, $Point$Type} from "packages/me/shedaniel/math/$Point"

export class $MappingProgressValueAnimator<R> implements $ProgressValueAnimator<(R)> {


public "value"(): R
public "target"(): R
public "update"(delta: double): void
public "setTarget"(target: R): $ProgressValueAnimator<(R)>
public "progress"(): double
public "setTo"(value: R, duration: long): $ProgressValueAnimator<(R)>
public static "mapProgress"<R>(parent: $NumberAnimator$Type<(any)>, converter: $Function$Type<(double), (R)>, backwardsConverter: $Function$Type<(R), (double)>): $ProgressValueAnimator<(R)>
public "map"<R>(converter: $Function$Type<(R), (R)>, backwardsConverter: $Function$Type<(R), (R)>): $ValueAnimator<(R)>
public static "ofLong"(): $NumberAnimator<(long)>
public static "ofLong"(initialValue: long): $NumberAnimator<(long)>
public static "ofFloatingPoint"(): $ValueAnimator<($FloatingPoint)>
public static "ofFloatingPoint"(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
/**
 * 
 * @deprecated
 */
public static "ofDimension"(initialValue: $Point$Type): $ValueAnimator<($Point)>
public static "ofDimension"(initialValue: $Dimension$Type): $ValueAnimator<($Dimension)>
public static "ofDimension"(): $ValueAnimator<($Dimension)>
public static "ofRectangle"(initialValue: $Rectangle$Type): $ValueAnimator<($Rectangle)>
public static "ofRectangle"(): $ValueAnimator<($Rectangle)>
public static "ofPoint"(initialValue: $Point$Type): $ValueAnimator<($Point)>
public static "ofPoint"(): $ValueAnimator<($Point)>
public "withConvention"(convention: $Supplier$Type<(R)>, duration: long): $ValueAnimator<(R)>
public static "ofInt"(): $NumberAnimator<(integer)>
public static "ofInt"(initialValue: integer): $NumberAnimator<(integer)>
public static "ofDouble"(): $NumberAnimator<(double)>
public static "ofDouble"(initialValue: double): $NumberAnimator<(double)>
public static "ofBoolean"(): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(switchPoint: double, initialValue: boolean): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(switchPoint: double): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(initialValue: boolean): $ProgressValueAnimator<(boolean)>
public static "ofFloat"(initialValue: float): $NumberAnimator<(float)>
public static "ofFloat"(): $NumberAnimator<(float)>
public static "ofColor"(): $ValueAnimator<($Color)>
public static "ofColor"(initialValue: $Color$Type): $ValueAnimator<($Color)>
public "completeImmediately"(): void
public static "ofFloatingRectangle"(initialValue: $FloatingRectangle$Type): $ValueAnimator<($FloatingRectangle)>
public static "ofFloatingRectangle"(): $ValueAnimator<($FloatingRectangle)>
/**
 * 
 * @deprecated
 */
public static "ofFloatingDimension"(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
public static "ofFloatingDimension"(): $ValueAnimator<($FloatingDimension)>
public static "ofFloatingDimension"(initialValue: $FloatingDimension$Type): $ValueAnimator<($FloatingDimension)>
public static "typicalTransitionTime"(): long
public static "constant"<T>(value: R): $ValueProvider<(R)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MappingProgressValueAnimator$Type<R> = ($MappingProgressValueAnimator<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MappingProgressValueAnimator_<R> = $MappingProgressValueAnimator$Type<(R)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$ScalarToken" {
import {$DumperOptions$ScalarStyle, $DumperOptions$ScalarStyle$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$ScalarStyle"
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $ScalarToken extends $Token {

constructor(value: string, startMark: $Mark$Type, endMark: $Mark$Type, plain: boolean)
constructor(value: string, plain: boolean, startMark: $Mark$Type, endMark: $Mark$Type, style: $DumperOptions$ScalarStyle$Type)

public "getValue"(): string
public "getPlain"(): boolean
public "getTokenId"(): $Token$ID
public "getStyle"(): $DumperOptions$ScalarStyle
get "value"(): string
get "plain"(): boolean
get "tokenId"(): $Token$ID
get "style"(): $DumperOptions$ScalarStyle
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScalarToken$Type = ($ScalarToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScalarToken_ = $ScalarToken$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$StringListEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$TextFieldListEntry, $TextFieldListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$TextFieldListEntry"

export class $StringListEntry extends $TextFieldListEntry<(string)> {

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: string, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(string)>, saveConsumer: $Consumer$Type<(string)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, requiresRestart: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: string, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(string)>, saveConsumer: $Consumer$Type<(string)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: string, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(string)>, saveConsumer: $Consumer$Type<(string)>)

public "getValue"(): string
get "value"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringListEntry$Type = ($StringListEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringListEntry_ = $StringListEntry$Type;
}}
declare module "packages/me/shedaniel/clothconfig/$ClothConfigForgeDemo" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ClothConfigForgeDemo {

constructor()

public static "registerModsPage"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClothConfigForgeDemo$Type = ($ClothConfigForgeDemo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClothConfigForgeDemo_ = $ClothConfigForgeDemo$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/$KeyBindingHooks" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $KeyBindingHooks {

 "cloth_setId"(arg0: string): void

(arg0: string): void
}

export namespace $KeyBindingHooks {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyBindingHooks$Type = ($KeyBindingHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyBindingHooks_ = $KeyBindingHooks$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$SafeConstructor" {
import {$LoaderOptions, $LoaderOptions$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$LoaderOptions"
import {$BaseConstructor, $BaseConstructor$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$BaseConstructor"
import {$SafeConstructor$ConstructUndefined, $SafeConstructor$ConstructUndefined$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$SafeConstructor$ConstructUndefined"

export class $SafeConstructor extends $BaseConstructor {
static readonly "undefinedConstructor": $SafeConstructor$ConstructUndefined

constructor()
constructor(loadingConfig: $LoaderOptions$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SafeConstructor$Type = ($SafeConstructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SafeConstructor_ = $SafeConstructor$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/annotation/$Serializer" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $Serializer extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $Serializer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Serializer$Type = ($Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Serializer_ = $Serializer$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event" {
import {$Event$ID, $Event$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $Event {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "equals"(obj: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "is"(id: $Event$ID$Type): boolean
public "getStartMark"(): $Mark
public "getEndMark"(): $Mark
public "getEventId"(): $Event$ID
get "startMark"(): $Mark
get "endMark"(): $Mark
get "eventId"(): $Event$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Event$Type = ($Event);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Event_ = $Event$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$PropertySubstitute" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Property, $Property$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$Property"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"

export class $PropertySubstitute extends $Property {

constructor(name: string, type: $Class$Type<(any)>, readMethod: string, writeMethod: string, ...params: ($Class$Type<(any)>)[])
constructor(name: string, type: $Class$Type<(any)>, ...params: ($Class$Type<(any)>)[])

public "getName"(): string
public "get"(object: any): any
public "getAnnotation"<A extends $Annotation>(annotationType: $Class$Type<(A)>): A
public "getAnnotations"(): $List<($Annotation)>
public "set"(object: any, value: any): void
public "getType"(): $Class<(any)>
public "setDelegate"(delegate: $Property$Type): void
public "getActualTypeArguments"(): ($Class<(any)>)[]
public "isReadable"(): boolean
public "isWritable"(): boolean
public "setActualTypeArguments"(...args: ($Class$Type<(any)>)[]): void
public "setTargetType"(targetType: $Class$Type<(any)>): void
get "name"(): string
get "annotations"(): $List<($Annotation)>
get "type"(): $Class<(any)>
set "delegate"(value: $Property$Type)
get "actualTypeArguments"(): ($Class<(any)>)[]
get "readable"(): boolean
get "writable"(): boolean
set "actualTypeArguments"(value: ($Class$Type<(any)>)[])
set "targetType"(value: $Class$Type<(any)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PropertySubstitute$Type = ($PropertySubstitute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PropertySubstitute_ = $PropertySubstitute$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$ConventionValueAnimator" {
import {$FloatingPoint, $FloatingPoint$Type} from "packages/me/shedaniel/math/$FloatingPoint"
import {$Color, $Color$Type} from "packages/me/shedaniel/math/$Color"
import {$NumberAnimator, $NumberAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$NumberAnimator"
import {$Dimension, $Dimension$Type} from "packages/me/shedaniel/math/$Dimension"
import {$ProgressValueAnimator, $ProgressValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ProgressValueAnimator"
import {$FloatingDimension, $FloatingDimension$Type} from "packages/me/shedaniel/math/$FloatingDimension"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"
import {$FloatingRectangle, $FloatingRectangle$Type} from "packages/me/shedaniel/math/$FloatingRectangle"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ValueProvider, $ValueProvider$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueProvider"
import {$Point, $Point$Type} from "packages/me/shedaniel/math/$Point"

export class $ConventionValueAnimator<T> implements $ValueAnimator<(T)> {


public "value"(): T
public "target"(): T
public "update"(delta: double): void
public "setTarget"(target: T): $ValueAnimator<(T)>
public "setTo"(value: T, duration: long): $ValueAnimator<(T)>
public "map"<R>(converter: $Function$Type<(T), (R)>, backwardsConverter: $Function$Type<(R), (T)>): $ValueAnimator<(R)>
public static "ofLong"(): $NumberAnimator<(long)>
public static "ofLong"(initialValue: long): $NumberAnimator<(long)>
public static "ofFloatingPoint"(): $ValueAnimator<($FloatingPoint)>
public static "ofFloatingPoint"(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
/**
 * 
 * @deprecated
 */
public static "ofDimension"(initialValue: $Point$Type): $ValueAnimator<($Point)>
public static "ofDimension"(initialValue: $Dimension$Type): $ValueAnimator<($Dimension)>
public static "ofDimension"(): $ValueAnimator<($Dimension)>
public static "ofRectangle"(initialValue: $Rectangle$Type): $ValueAnimator<($Rectangle)>
public static "ofRectangle"(): $ValueAnimator<($Rectangle)>
public static "ofPoint"(initialValue: $Point$Type): $ValueAnimator<($Point)>
public static "ofPoint"(): $ValueAnimator<($Point)>
public "withConvention"(convention: $Supplier$Type<(T)>, duration: long): $ValueAnimator<(T)>
public static "ofInt"(): $NumberAnimator<(integer)>
public static "ofInt"(initialValue: integer): $NumberAnimator<(integer)>
public "setAs"(value: T): $ValueAnimator<(T)>
public static "ofDouble"(): $NumberAnimator<(double)>
public static "ofDouble"(initialValue: double): $NumberAnimator<(double)>
public static "ofBoolean"(): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(switchPoint: double, initialValue: boolean): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(switchPoint: double): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(initialValue: boolean): $ProgressValueAnimator<(boolean)>
public static "ofFloat"(initialValue: float): $NumberAnimator<(float)>
public static "ofFloat"(): $NumberAnimator<(float)>
public static "ofColor"(): $ValueAnimator<($Color)>
public static "ofColor"(initialValue: $Color$Type): $ValueAnimator<($Color)>
public "completeImmediately"(): void
public static "ofFloatingRectangle"(initialValue: $FloatingRectangle$Type): $ValueAnimator<($FloatingRectangle)>
public static "ofFloatingRectangle"(): $ValueAnimator<($FloatingRectangle)>
/**
 * 
 * @deprecated
 */
public static "ofFloatingDimension"(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
public static "ofFloatingDimension"(): $ValueAnimator<($FloatingDimension)>
public static "ofFloatingDimension"(initialValue: $FloatingDimension$Type): $ValueAnimator<($FloatingDimension)>
public static "typicalTransitionTime"(): long
public static "constant"<T>(value: T): $ValueProvider<(T)>
set "as"(value: T)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConventionValueAnimator$Type<T> = ($ConventionValueAnimator<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConventionValueAnimator_<T> = $ConventionValueAnimator$Type<(T)>;
}}
declare module "packages/me/shedaniel/autoconfig/example/$ExampleConfig$PairOfIntPairs" {
import {$ExampleConfig$PairOfInts, $ExampleConfig$PairOfInts$Type} from "packages/me/shedaniel/autoconfig/example/$ExampleConfig$PairOfInts"

export class $ExampleConfig$PairOfIntPairs {
 "first": $ExampleConfig$PairOfInts
 "second": $ExampleConfig$PairOfInts

constructor()
constructor(first: $ExampleConfig$PairOfInts$Type, second: $ExampleConfig$PairOfInts$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExampleConfig$PairOfIntPairs$Type = ($ExampleConfig$PairOfIntPairs);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExampleConfig$PairOfIntPairs_ = $ExampleConfig$PairOfIntPairs$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$FlowSequenceEndToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $FlowSequenceEndToken extends $Token {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlowSequenceEndToken$Type = ($FlowSequenceEndToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlowSequenceEndToken_ = $FlowSequenceEndToken$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/external/biz/base64Coder/$Base64Coder" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Base64Coder {


public static "encodeString"(s: string): string
public static "decode"(arg0: (character)[]): (byte)[]
public static "decode"(arg0: (character)[], iOff: integer, iLen: integer): (byte)[]
public static "decode"(s: string): (byte)[]
public static "encode"(arg0: (byte)[]): (character)[]
public static "encode"(arg0: (byte)[], iLen: integer): (character)[]
public static "encode"(arg0: (byte)[], iOff: integer, iLen: integer): (character)[]
public static "decodeLines"(s: string): (byte)[]
public static "encodeLines"(arg0: (byte)[], iOff: integer, iLen: integer, lineLen: integer, lineSeparator: string): string
public static "encodeLines"(arg0: (byte)[]): string
public static "decodeString"(s: string): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Base64Coder$Type = ($Base64Coder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Base64Coder_ = $Base64Coder$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/api/$Marshaller" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$JsonElement, $JsonElement$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonElement"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"

export interface $Marshaller {

 "marshallCarefully"<E>(arg0: $Class$Type<(E)>, arg1: $JsonElement$Type): E
 "serialize"(arg0: any): $JsonElement
 "marshall"<E>(arg0: $Class$Type<(E)>, arg1: $JsonElement$Type): E
 "marshall"<E>(arg0: $Type$Type, arg1: $JsonElement$Type): E
}

export namespace $Marshaller {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Marshaller$Type = ($Marshaller);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Marshaller_ = $Marshaller$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$TagTuple" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $TagTuple {

constructor(handle: string, suffix: string)

public "getHandle"(): string
public "getSuffix"(): string
get "handle"(): string
get "suffix"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TagTuple$Type = ($TagTuple);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TagTuple_ = $TagTuple$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$IntegerListEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractNumberListEntry, $AbstractNumberListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractNumberListEntry"

export class $IntegerListEntry extends $AbstractNumberListEntry<(integer)> {

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: integer, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(integer)>, saveConsumer: $Consumer$Type<(integer)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, requiresRestart: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: integer, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(integer)>, saveConsumer: $Consumer$Type<(integer)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: integer, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(integer)>, saveConsumer: $Consumer$Type<(integer)>)

public "getValue"(): integer
public "getError"(): $Optional<($Component)>
public "setMaximum"(maximum: integer): $IntegerListEntry
public "setMinimum"(minimum: integer): $IntegerListEntry
get "value"(): integer
get "error"(): $Optional<($Component)>
set "maximum"(value: integer)
set "minimum"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntegerListEntry$Type = ($IntegerListEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntegerListEntry_ = $IntegerListEntry$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$ObjectParserContext" {
import {$ParserContext, $ParserContext$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$ParserContext"
import {$JsonObject, $JsonObject$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonObject"
import {$Jankson, $Jankson$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$Jankson"

export class $ObjectParserContext implements $ParserContext<($JsonObject)> {

constructor()

public "consume"(codePoint: integer, loader: $Jankson$Type): boolean
public "eof"(): void
public "isComplete"(): boolean
get "complete"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ObjectParserContext$Type = ($ObjectParserContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ObjectParserContext_ = $ObjectParserContext$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/parser/$ParserImpl" {
import {$Event$ID, $Event$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$StreamReader, $StreamReader$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/reader/$StreamReader"
import {$Scanner, $Scanner$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/scanner/$Scanner"
import {$Parser, $Parser$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/parser/$Parser"
import {$Event, $Event$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event"

export class $ParserImpl implements $Parser {

constructor(reader: $StreamReader$Type)
constructor(scanner: $Scanner$Type)

public "checkEvent"(choice: $Event$ID$Type): boolean
public "peekEvent"(): $Event
public "getEvent"(): $Event
get "event"(): $Event
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ParserImpl$Type = ($ParserImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ParserImpl_ = $ParserImpl$Type;
}}
declare module "packages/me/shedaniel/autoconfig/serializer/$PartitioningSerializer" {
import {$PartitioningSerializer$GlobalData, $PartitioningSerializer$GlobalData$Type} from "packages/me/shedaniel/autoconfig/serializer/$PartitioningSerializer$GlobalData"
import {$ConfigSerializer, $ConfigSerializer$Type} from "packages/me/shedaniel/autoconfig/serializer/$ConfigSerializer"
import {$ConfigData, $ConfigData$Type} from "packages/me/shedaniel/autoconfig/$ConfigData"
import {$ConfigSerializer$Factory, $ConfigSerializer$Factory$Type} from "packages/me/shedaniel/autoconfig/serializer/$ConfigSerializer$Factory"

export class $PartitioningSerializer<T extends $PartitioningSerializer$GlobalData, M extends $ConfigData> implements $ConfigSerializer<(T)> {


public static "wrap"<T extends $PartitioningSerializer$GlobalData, M extends $ConfigData>(inner: $ConfigSerializer$Factory$Type<(M)>): $ConfigSerializer$Factory<(T)>
public "deserialize"(): T
public "createDefault"(): T
public "serialize"(config: T): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PartitioningSerializer$Type<T, M> = ($PartitioningSerializer<(T), (M)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PartitioningSerializer_<T, M> = $PartitioningSerializer$Type<(T), (M)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/representer/$SafeRepresenter" {
import {$DumperOptions, $DumperOptions$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Tag, $Tag$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Tag"
import {$BaseRepresenter, $BaseRepresenter$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/representer/$BaseRepresenter"
import {$TimeZone, $TimeZone$Type} from "packages/java/util/$TimeZone"

export class $SafeRepresenter extends $BaseRepresenter {

constructor()
constructor(options: $DumperOptions$Type)

public "setTimeZone"(timeZone: $TimeZone$Type): void
public "getTimeZone"(): $TimeZone
public "addClassTag"(clazz: $Class$Type<(any)>, tag: $Tag$Type): $Tag
set "timeZone"(value: $TimeZone$Type)
get "timeZone"(): $TimeZone
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SafeRepresenter$Type = ($SafeRepresenter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SafeRepresenter_ = $SafeRepresenter$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$ColorEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$TextFieldListEntry, $TextFieldListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$TextFieldListEntry"

export class $ColorEntry extends $TextFieldListEntry<(integer)> {

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: integer, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(integer)>, saveConsumer: $Consumer$Type<(integer)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, requiresRestart: boolean)

public "getValue"(): integer
/**
 * 
 * @deprecated
 */
public "setValue"(color: integer): void
public "getError"(): $Optional<($Component)>
public "render"(graphics: $GuiGraphics$Type, index: integer, y: integer, x: integer, entryWidth: integer, entryHeight: integer, mouseX: integer, mouseY: integer, isHovered: boolean, delta: float): void
public "isEdited"(): boolean
public "withoutAlpha"(): void
public "withAlpha"(): void
get "value"(): integer
set "value"(value: integer)
get "error"(): $Optional<($Component)>
get "edited"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorEntry$Type = ($ColorEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorEntry_ = $ColorEntry$Type;
}}
declare module "packages/me/shedaniel/autoconfig/$ConfigManager" {
import {$Config, $Config$Type} from "packages/me/shedaniel/autoconfig/annotation/$Config"
import {$ConfigHolder, $ConfigHolder$Type} from "packages/me/shedaniel/autoconfig/$ConfigHolder"
import {$ConfigSerializeEvent$Save, $ConfigSerializeEvent$Save$Type} from "packages/me/shedaniel/autoconfig/event/$ConfigSerializeEvent$Save"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ConfigSerializeEvent$Load, $ConfigSerializeEvent$Load$Type} from "packages/me/shedaniel/autoconfig/event/$ConfigSerializeEvent$Load"
import {$ConfigSerializer, $ConfigSerializer$Type} from "packages/me/shedaniel/autoconfig/serializer/$ConfigSerializer"
import {$ConfigData, $ConfigData$Type} from "packages/me/shedaniel/autoconfig/$ConfigData"

export class $ConfigManager<T extends $ConfigData> implements $ConfigHolder<(T)> {


public "load"(): boolean
public "save"(): void
public "getDefinition"(): $Config
public "resetToDefault"(): void
public "getSerializer"(): $ConfigSerializer<(T)>
public "getConfig"(): T
public "getConfigClass"(): $Class<(T)>
public "setConfig"(config: T): void
public "registerSaveListener"(save: $ConfigSerializeEvent$Save$Type<(T)>): void
public "registerLoadListener"(load: $ConfigSerializeEvent$Load$Type<(T)>): void
public "get"(): T
get "definition"(): $Config
get "serializer"(): $ConfigSerializer<(T)>
get "config"(): T
get "configClass"(): $Class<(T)>
set "config"(value: T)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigManager$Type<T> = ($ConfigManager<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigManager_<T> = $ConfigManager$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg4$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg4$Up<A1, A2, A3, A4, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>): void
}

export namespace $RecordValueAnimatorArgs$Arg4$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg4$Up$Type<A1, A2, A3, A4, T> = ($RecordValueAnimatorArgs$Arg4$Up<(A1), (A2), (A3), (A4), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg4$Up_<A1, A2, A3, A4, T> = $RecordValueAnimatorArgs$Arg4$Up$Type<(A1), (A2), (A3), (A4), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$CollectionEndEvent" {
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Event, $Event$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event"

export class $CollectionEndEvent extends $Event {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CollectionEndEvent$Type = ($CollectionEndEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CollectionEndEvent_ = $CollectionEndEvent$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator" {
import {$RecordValueAnimatorArgs$Arg10$Up, $RecordValueAnimatorArgs$Arg10$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg10$Up"
import {$RecordValueAnimatorArgs$Arg16$Op, $RecordValueAnimatorArgs$Arg16$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg16$Op"
import {$RecordValueAnimatorArgs$Arg1$Up, $RecordValueAnimatorArgs$Arg1$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg1$Up"
import {$RecordValueAnimatorArgs$Arg8$Up, $RecordValueAnimatorArgs$Arg8$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg8$Up"
import {$ProgressValueAnimator, $ProgressValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ProgressValueAnimator"
import {$RecordValueAnimatorArgs$Arg12$Op, $RecordValueAnimatorArgs$Arg12$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg12$Op"
import {$RecordValueAnimatorArgs$Arg20$Up, $RecordValueAnimatorArgs$Arg20$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg20$Up"
import {$RecordValueAnimatorArgs$Arg4$Up, $RecordValueAnimatorArgs$Arg4$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg4$Up"
import {$RecordValueAnimatorArgs$Arg17$Up, $RecordValueAnimatorArgs$Arg17$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg17$Up"
import {$RecordValueAnimatorArgs$Arg6$Op, $RecordValueAnimatorArgs$Arg6$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg6$Op"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$FloatingPoint, $FloatingPoint$Type} from "packages/me/shedaniel/math/$FloatingPoint"
import {$Color, $Color$Type} from "packages/me/shedaniel/math/$Color"
import {$RecordValueAnimatorArgs$Arg17$Op, $RecordValueAnimatorArgs$Arg17$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg17$Op"
import {$RecordValueAnimatorArgs$Arg11$Up, $RecordValueAnimatorArgs$Arg11$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg11$Up"
import {$RecordValueAnimatorArgs$Arg2$Up, $RecordValueAnimatorArgs$Arg2$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg2$Up"
import {$RecordValueAnimatorArgs$Arg7$Up, $RecordValueAnimatorArgs$Arg7$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg7$Up"
import {$RecordValueAnimatorArgs$Arg13$Op, $RecordValueAnimatorArgs$Arg13$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg13$Op"
import {$RecordValueAnimatorArgs$Arg18$Up, $RecordValueAnimatorArgs$Arg18$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg18$Up"
import {$RecordValueAnimatorArgs$Arg9$Op, $RecordValueAnimatorArgs$Arg9$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg9$Op"
import {$RecordValueAnimatorArgs$Arg3$Up, $RecordValueAnimatorArgs$Arg3$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg3$Up"
import {$RecordValueAnimatorArgs$Arg14$Up, $RecordValueAnimatorArgs$Arg14$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg14$Up"
import {$RecordValueAnimatorArgs$Arg5$Op, $RecordValueAnimatorArgs$Arg5$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg5$Op"
import {$ValueProvider, $ValueProvider$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueProvider"
import {$RecordValueAnimatorArgs$Arg1$Op, $RecordValueAnimatorArgs$Arg1$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg1$Op"
import {$RecordValueAnimatorArgs$Arg12$Up, $RecordValueAnimatorArgs$Arg12$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg12$Up"
import {$Dimension, $Dimension$Type} from "packages/me/shedaniel/math/$Dimension"
import {$RecordValueAnimatorArgs$Arg18$Op, $RecordValueAnimatorArgs$Arg18$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg18$Op"
import {$RecordValueAnimatorArgs$Arg14$Op, $RecordValueAnimatorArgs$Arg14$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg14$Op"
import {$RecordValueAnimatorArgs$Arg10$Op, $RecordValueAnimatorArgs$Arg10$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg10$Op"
import {$RecordValueAnimatorArgs$Arg4, $RecordValueAnimatorArgs$Arg4$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg4"
import {$RecordValueAnimatorArgs$Arg19$Up, $RecordValueAnimatorArgs$Arg19$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg19$Up"
import {$RecordValueAnimatorArgs$Arg3, $RecordValueAnimatorArgs$Arg3$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg3"
import {$RecordValueAnimatorArgs$Arg6$Up, $RecordValueAnimatorArgs$Arg6$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg6$Up"
import {$RecordValueAnimatorArgs$Arg2, $RecordValueAnimatorArgs$Arg2$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg2"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$RecordValueAnimatorArgs$Arg1, $RecordValueAnimatorArgs$Arg1$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg1"
import {$RecordValueAnimatorArgs$Arg8$Op, $RecordValueAnimatorArgs$Arg8$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg8$Op"
import {$ValueAnimator, $ValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator"
import {$RecordValueAnimatorArgs$Arg15$Up, $RecordValueAnimatorArgs$Arg15$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg15$Up"
import {$RecordValueAnimatorArgs$Arg4$Op, $RecordValueAnimatorArgs$Arg4$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg4$Op"
import {$RecordValueAnimatorArgs$Arg9, $RecordValueAnimatorArgs$Arg9$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg9"
import {$RecordValueAnimatorArgs$Arg8, $RecordValueAnimatorArgs$Arg8$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg8"
import {$RecordValueAnimatorArgs$Arg7, $RecordValueAnimatorArgs$Arg7$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg7"
import {$RecordValueAnimatorArgs$Arg20, $RecordValueAnimatorArgs$Arg20$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg20"
import {$RecordValueAnimatorArgs$Arg6, $RecordValueAnimatorArgs$Arg6$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg6"
import {$RecordValueAnimatorArgs$Arg20$Op, $RecordValueAnimatorArgs$Arg20$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg20$Op"
import {$RecordValueAnimatorArgs$Arg5, $RecordValueAnimatorArgs$Arg5$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg5"
import {$Point, $Point$Type} from "packages/me/shedaniel/math/$Point"
import {$RecordValueAnimatorArgs$Arg11$Op, $RecordValueAnimatorArgs$Arg11$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg11$Op"
import {$RecordValueAnimatorArgs$Arg2$Op, $RecordValueAnimatorArgs$Arg2$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg2$Op"
import {$NumberAnimator, $NumberAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$NumberAnimator"
import {$RecordValueAnimatorArgs$Arg13$Up, $RecordValueAnimatorArgs$Arg13$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg13$Up"
import {$RecordValueAnimatorArgs$Arg19$Op, $RecordValueAnimatorArgs$Arg19$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg19$Op"
import {$RecordValueAnimatorArgs$Arg15$Op, $RecordValueAnimatorArgs$Arg15$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg15$Op"
import {$FloatingDimension, $FloatingDimension$Type} from "packages/me/shedaniel/math/$FloatingDimension"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"
import {$FloatingRectangle, $FloatingRectangle$Type} from "packages/me/shedaniel/math/$FloatingRectangle"
import {$RecordValueAnimator$Arg, $RecordValueAnimator$Arg$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimator$Arg"
import {$RecordValueAnimatorArgs$Arg9$Up, $RecordValueAnimatorArgs$Arg9$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg9$Up"
import {$RecordValueAnimatorArgs$Arg15, $RecordValueAnimatorArgs$Arg15$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg15"
import {$RecordValueAnimatorArgs$Arg14, $RecordValueAnimatorArgs$Arg14$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg14"
import {$RecordValueAnimatorArgs$Arg5$Up, $RecordValueAnimatorArgs$Arg5$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg5$Up"
import {$RecordValueAnimatorArgs$Arg13, $RecordValueAnimatorArgs$Arg13$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg13"
import {$RecordValueAnimatorArgs$Arg12, $RecordValueAnimatorArgs$Arg12$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg12"
import {$RecordValueAnimatorArgs$Arg19, $RecordValueAnimatorArgs$Arg19$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg19"
import {$RecordValueAnimatorArgs$Arg7$Op, $RecordValueAnimatorArgs$Arg7$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg7$Op"
import {$RecordValueAnimatorArgs$Arg18, $RecordValueAnimatorArgs$Arg18$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg18"
import {$RecordValueAnimatorArgs$Arg17, $RecordValueAnimatorArgs$Arg17$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg17"
import {$RecordValueAnimatorArgs$Arg16, $RecordValueAnimatorArgs$Arg16$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg16"
import {$RecordValueAnimatorArgs$Arg16$Up, $RecordValueAnimatorArgs$Arg16$Up$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg16$Up"
import {$RecordValueAnimatorArgs$Arg3$Op, $RecordValueAnimatorArgs$Arg3$Op$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg3$Op"
import {$RecordValueAnimatorArgs$Arg11, $RecordValueAnimatorArgs$Arg11$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg11"
import {$RecordValueAnimatorArgs$Arg10, $RecordValueAnimatorArgs$Arg10$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg10"

export class $RecordValueAnimator<T, A extends $RecordValueAnimator$Arg<(T)>> implements $ValueAnimator<(T)> {


public "value"(): T
public "target"(): T
public "update"(delta: double): void
public static "of"<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, T>(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, a12: $ValueAnimator$Type<(A12)>, a13: $ValueAnimator$Type<(A13)>, a14: $ValueAnimator$Type<(A14)>, a15: $ValueAnimator$Type<(A15)>, op: $RecordValueAnimatorArgs$Arg15$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (T)>, up: $RecordValueAnimatorArgs$Arg15$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg15<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (T)>)>
public static "of"<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, T>(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, a12: $ValueAnimator$Type<(A12)>, a13: $ValueAnimator$Type<(A13)>, a14: $ValueAnimator$Type<(A14)>, op: $RecordValueAnimatorArgs$Arg14$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (T)>, up: $RecordValueAnimatorArgs$Arg14$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg14<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (T)>)>
public static "of"<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, T>(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, a12: $ValueAnimator$Type<(A12)>, a13: $ValueAnimator$Type<(A13)>, op: $RecordValueAnimatorArgs$Arg13$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (T)>, up: $RecordValueAnimatorArgs$Arg13$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg13<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (T)>)>
public static "of"<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, T>(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, a12: $ValueAnimator$Type<(A12)>, op: $RecordValueAnimatorArgs$Arg12$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (T)>, up: $RecordValueAnimatorArgs$Arg12$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg12<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (T)>)>
public static "of"<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, T>(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, op: $RecordValueAnimatorArgs$Arg10$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (T)>, up: $RecordValueAnimatorArgs$Arg10$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg10<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (T)>)>
public static "of"<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, T>(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, op: $RecordValueAnimatorArgs$Arg11$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (T)>, up: $RecordValueAnimatorArgs$Arg11$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg11<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (T)>)>
public static "of"<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, T>(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, a12: $ValueAnimator$Type<(A12)>, a13: $ValueAnimator$Type<(A13)>, a14: $ValueAnimator$Type<(A14)>, a15: $ValueAnimator$Type<(A15)>, a16: $ValueAnimator$Type<(A16)>, a17: $ValueAnimator$Type<(A17)>, a18: $ValueAnimator$Type<(A18)>, a19: $ValueAnimator$Type<(A19)>, a20: $ValueAnimator$Type<(A20)>, op: $RecordValueAnimatorArgs$Arg20$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (A20), (T)>, up: $RecordValueAnimatorArgs$Arg20$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (A20), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg20<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (A20), (T)>)>
public static "of"<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, T>(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, a12: $ValueAnimator$Type<(A12)>, a13: $ValueAnimator$Type<(A13)>, a14: $ValueAnimator$Type<(A14)>, a15: $ValueAnimator$Type<(A15)>, a16: $ValueAnimator$Type<(A16)>, a17: $ValueAnimator$Type<(A17)>, a18: $ValueAnimator$Type<(A18)>, a19: $ValueAnimator$Type<(A19)>, op: $RecordValueAnimatorArgs$Arg19$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (T)>, up: $RecordValueAnimatorArgs$Arg19$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg19<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (A19), (T)>)>
public static "of"<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, T>(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, a12: $ValueAnimator$Type<(A12)>, a13: $ValueAnimator$Type<(A13)>, a14: $ValueAnimator$Type<(A14)>, a15: $ValueAnimator$Type<(A15)>, a16: $ValueAnimator$Type<(A16)>, a17: $ValueAnimator$Type<(A17)>, a18: $ValueAnimator$Type<(A18)>, op: $RecordValueAnimatorArgs$Arg18$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (T)>, up: $RecordValueAnimatorArgs$Arg18$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg18<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (T)>)>
public static "of"<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, T>(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, a12: $ValueAnimator$Type<(A12)>, a13: $ValueAnimator$Type<(A13)>, a14: $ValueAnimator$Type<(A14)>, a15: $ValueAnimator$Type<(A15)>, a16: $ValueAnimator$Type<(A16)>, a17: $ValueAnimator$Type<(A17)>, op: $RecordValueAnimatorArgs$Arg17$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (T)>, up: $RecordValueAnimatorArgs$Arg17$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg17<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (T)>)>
public static "of"<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, T>(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, a10: $ValueAnimator$Type<(A10)>, a11: $ValueAnimator$Type<(A11)>, a12: $ValueAnimator$Type<(A12)>, a13: $ValueAnimator$Type<(A13)>, a14: $ValueAnimator$Type<(A14)>, a15: $ValueAnimator$Type<(A15)>, a16: $ValueAnimator$Type<(A16)>, op: $RecordValueAnimatorArgs$Arg16$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (T)>, up: $RecordValueAnimatorArgs$Arg16$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg16<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (T)>)>
public static "of"<A1, A2, A3, A4, A5, T>(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, op: $RecordValueAnimatorArgs$Arg5$Op$Type<(A1), (A2), (A3), (A4), (A5), (T)>, up: $RecordValueAnimatorArgs$Arg5$Up$Type<(A1), (A2), (A3), (A4), (A5), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg5<(A1), (A2), (A3), (A4), (A5), (T)>)>
public static "of"<A1, A2, A3, A4, T>(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, op: $RecordValueAnimatorArgs$Arg4$Op$Type<(A1), (A2), (A3), (A4), (T)>, up: $RecordValueAnimatorArgs$Arg4$Up$Type<(A1), (A2), (A3), (A4), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg4<(A1), (A2), (A3), (A4), (T)>)>
public static "of"<A1, A2, A3, T>(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, op: $RecordValueAnimatorArgs$Arg3$Op$Type<(A1), (A2), (A3), (T)>, up: $RecordValueAnimatorArgs$Arg3$Up$Type<(A1), (A2), (A3), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg3<(A1), (A2), (A3), (T)>)>
public static "of"<A1, A2, T>(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, op: $RecordValueAnimatorArgs$Arg2$Op$Type<(A1), (A2), (T)>, up: $RecordValueAnimatorArgs$Arg2$Up$Type<(A1), (A2), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg2<(A1), (A2), (T)>)>
public static "of"<A1, T>(a1: $ValueAnimator$Type<(A1)>, op: $RecordValueAnimatorArgs$Arg1$Op$Type<(A1), (T)>, up: $RecordValueAnimatorArgs$Arg1$Up$Type<(A1), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg1<(A1), (T)>)>
public static "of"<A1, A2, A3, A4, A5, A6, A7, A8, A9, T>(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, a9: $ValueAnimator$Type<(A9)>, op: $RecordValueAnimatorArgs$Arg9$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (T)>, up: $RecordValueAnimatorArgs$Arg9$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg9<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (T)>)>
public static "of"<A1, A2, A3, A4, A5, A6, A7, A8, T>(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, a8: $ValueAnimator$Type<(A8)>, op: $RecordValueAnimatorArgs$Arg8$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (T)>, up: $RecordValueAnimatorArgs$Arg8$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg8<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (T)>)>
public static "of"<A1, A2, A3, A4, A5, A6, A7, T>(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, a7: $ValueAnimator$Type<(A7)>, op: $RecordValueAnimatorArgs$Arg7$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (T)>, up: $RecordValueAnimatorArgs$Arg7$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg7<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (T)>)>
public static "of"<A1, A2, A3, A4, A5, A6, T>(a1: $ValueAnimator$Type<(A1)>, a2: $ValueAnimator$Type<(A2)>, a3: $ValueAnimator$Type<(A3)>, a4: $ValueAnimator$Type<(A4)>, a5: $ValueAnimator$Type<(A5)>, a6: $ValueAnimator$Type<(A6)>, op: $RecordValueAnimatorArgs$Arg6$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (T)>, up: $RecordValueAnimatorArgs$Arg6$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (T)>): $RecordValueAnimator<(T), ($RecordValueAnimatorArgs$Arg6<(A1), (A2), (A3), (A4), (A5), (A6), (T)>)>
public "setTarget"(target: T): $ValueAnimator<(T)>
public "setTo"(value: T, duration: long): $ValueAnimator<(T)>
public "map"<R>(converter: $Function$Type<(T), (R)>, backwardsConverter: $Function$Type<(R), (T)>): $ValueAnimator<(R)>
public static "ofLong"(): $NumberAnimator<(long)>
public static "ofLong"(initialValue: long): $NumberAnimator<(long)>
public static "ofFloatingPoint"(): $ValueAnimator<($FloatingPoint)>
public static "ofFloatingPoint"(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
/**
 * 
 * @deprecated
 */
public static "ofDimension"(initialValue: $Point$Type): $ValueAnimator<($Point)>
public static "ofDimension"(initialValue: $Dimension$Type): $ValueAnimator<($Dimension)>
public static "ofDimension"(): $ValueAnimator<($Dimension)>
public static "ofRectangle"(initialValue: $Rectangle$Type): $ValueAnimator<($Rectangle)>
public static "ofRectangle"(): $ValueAnimator<($Rectangle)>
public static "ofPoint"(initialValue: $Point$Type): $ValueAnimator<($Point)>
public static "ofPoint"(): $ValueAnimator<($Point)>
public "withConvention"(convention: $Supplier$Type<(T)>, duration: long): $ValueAnimator<(T)>
public static "ofInt"(): $NumberAnimator<(integer)>
public static "ofInt"(initialValue: integer): $NumberAnimator<(integer)>
public "setAs"(value: T): $ValueAnimator<(T)>
public static "ofDouble"(): $NumberAnimator<(double)>
public static "ofDouble"(initialValue: double): $NumberAnimator<(double)>
public static "ofBoolean"(): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(switchPoint: double, initialValue: boolean): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(switchPoint: double): $ProgressValueAnimator<(boolean)>
public static "ofBoolean"(initialValue: boolean): $ProgressValueAnimator<(boolean)>
public static "ofFloat"(initialValue: float): $NumberAnimator<(float)>
public static "ofFloat"(): $NumberAnimator<(float)>
public static "ofColor"(): $ValueAnimator<($Color)>
public static "ofColor"(initialValue: $Color$Type): $ValueAnimator<($Color)>
public "completeImmediately"(): void
public static "ofFloatingRectangle"(initialValue: $FloatingRectangle$Type): $ValueAnimator<($FloatingRectangle)>
public static "ofFloatingRectangle"(): $ValueAnimator<($FloatingRectangle)>
/**
 * 
 * @deprecated
 */
public static "ofFloatingDimension"(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
public static "ofFloatingDimension"(): $ValueAnimator<($FloatingDimension)>
public static "ofFloatingDimension"(initialValue: $FloatingDimension$Type): $ValueAnimator<($FloatingDimension)>
public static "typicalTransitionTime"(): long
public static "constant"<T>(value: T): $ValueProvider<(T)>
set "as"(value: T)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimator$Type<T, A> = ($RecordValueAnimator<(T), (A)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimator_<T, A> = $RecordValueAnimator$Type<(T), (A)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$ValueHolder" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ValueHolder<T> {

 "getValue"(): T

(): T
}

export namespace $ValueHolder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueHolder$Type<T> = ($ValueHolder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueHolder_<T> = $ValueHolder$Type<(T)>;
}}
declare module "packages/me/shedaniel/math/$Point" {
import {$FloatingPoint, $FloatingPoint$Type} from "packages/me/shedaniel/math/$FloatingPoint"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"

export class $Point implements $Cloneable {
 "x": integer
 "y": integer

constructor(arg0: integer, arg1: integer)
constructor(arg0: double, arg1: double)
constructor(arg0: $FloatingPoint$Type)
constructor(arg0: $Point$Type)
constructor()

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): $Point
public "getLocation"(): $Point
public "move"(arg0: integer, arg1: integer): void
public "getY"(): integer
public "translate"(arg0: integer, arg1: integer): void
public "getX"(): integer
public "setLocation"(arg0: double, arg1: double): void
public "getFloatingLocation"(): $FloatingPoint
get "location"(): $Point
get "y"(): integer
get "x"(): integer
get "floatingLocation"(): $FloatingPoint
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Point$Type = ($Point);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Point_ = $Point$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg4$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg4$Op<A1, A2, A3, A4, T> {

 "construct"(arg0: A1, arg1: A2, arg2: A3, arg3: A4): T

(arg0: A1, arg1: A2, arg2: A3, arg3: A4): T
}

export namespace $RecordValueAnimatorArgs$Arg4$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg4$Op$Type<A1, A2, A3, A4, T> = ($RecordValueAnimatorArgs$Arg4$Op<(A1), (A2), (A3), (A4), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg4$Op_<A1, A2, A3, A4, T> = $RecordValueAnimatorArgs$Arg4$Op$Type<(A1), (A2), (A3), (A4), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$FlowSequenceStartToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $FlowSequenceStartToken extends $Token {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlowSequenceStartToken$Type = ($FlowSequenceStartToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlowSequenceStartToken_ = $FlowSequenceStartToken$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$FlowEntryToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $FlowEntryToken extends $Token {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlowEntryToken$Type = ($FlowEntryToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlowEntryToken_ = $FlowEntryToken$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$FlowMappingEndToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $FlowMappingEndToken extends $Token {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlowMappingEndToken$Type = ($FlowMappingEndToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlowMappingEndToken_ = $FlowMappingEndToken$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/scanner/$ScannerImpl" {
import {$StreamReader, $StreamReader$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/reader/$StreamReader"
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Scanner, $Scanner$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/scanner/$Scanner"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ScannerImpl implements $Scanner {
static readonly "ESCAPE_REPLACEMENTS": $Map<(character), (string)>
static readonly "ESCAPE_CODES": $Map<(character), (integer)>

constructor(reader: $StreamReader$Type)

public "getToken"(): $Token
public "checkToken"(...choices: ($Token$ID$Type)[]): boolean
public "peekToken"(): $Token
get "token"(): $Token
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScannerImpl$Type = ($ScannerImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScannerImpl_ = $ScannerImpl$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$DoubleListEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractNumberListEntry, $AbstractNumberListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractNumberListEntry"

export class $DoubleListEntry extends $AbstractNumberListEntry<(double)> {

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: double, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(double)>, saveConsumer: $Consumer$Type<(double)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, requiresRestart: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: double, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(double)>, saveConsumer: $Consumer$Type<(double)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: double, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(double)>, saveConsumer: $Consumer$Type<(double)>)

public "getValue"(): double
public "getError"(): $Optional<($Component)>
public "setMaximum"(maximum: double): $DoubleListEntry
public "setMinimum"(minimum: double): $DoubleListEntry
get "value"(): double
get "error"(): $Optional<($Component)>
set "maximum"(value: double)
set "minimum"(value: double)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleListEntry$Type = ($DoubleListEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleListEntry_ = $DoubleListEntry$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$ImplicitTuple" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ImplicitTuple {

constructor(plain: boolean, nonplain: boolean)

public "toString"(): string
public "bothFalse"(): boolean
public "canOmitTagInPlainScalar"(): boolean
public "canOmitTagInNonPlainScalar"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImplicitTuple$Type = ($ImplicitTuple);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImplicitTuple_ = $ImplicitTuple$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/extensions/compactnotation/$CompactData" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CompactData {

constructor(prefix: string)

public "toString"(): string
public "getProperties"(): $Map<(string), (string)>
public "getPrefix"(): string
public "getArguments"(): $List<(string)>
get "properties"(): $Map<(string), (string)>
get "prefix"(): string
get "arguments"(): $List<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompactData$Type = ($CompactData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompactData_ = $CompactData$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$IntFieldBuilder" {
import {$AbstractRangeFieldBuilder, $AbstractRangeFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractRangeFieldBuilder"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IntegerListEntry, $IntegerListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$IntegerListEntry"

export class $IntFieldBuilder extends $AbstractRangeFieldBuilder<(integer), ($IntegerListEntry), ($IntFieldBuilder)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, value: integer)

public "setMin"(min: integer): $IntFieldBuilder
public "setTooltipSupplier"(tooltipSupplier: $Function$Type<(integer), ($Optional$Type<(($Component$Type)[])>)>): $IntFieldBuilder
public "setDefaultValue"(defaultValue: integer): $IntFieldBuilder
public "setMax"(max: integer): $IntFieldBuilder
set "min"(value: integer)
set "tooltipSupplier"(value: $Function$Type<(integer), ($Optional$Type<(($Component$Type)[])>)>)
set "defaultValue"(value: integer)
set "max"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntFieldBuilder$Type = ($IntFieldBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntFieldBuilder_ = $IntFieldBuilder$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Tag" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$URI, $URI$Type} from "packages/java/net/$URI"

export class $Tag {
static readonly "PREFIX": string
static readonly "YAML": $Tag
static readonly "MERGE": $Tag
static readonly "SET": $Tag
static readonly "PAIRS": $Tag
static readonly "OMAP": $Tag
static readonly "BINARY": $Tag
static readonly "INT": $Tag
static readonly "FLOAT": $Tag
static readonly "TIMESTAMP": $Tag
static readonly "BOOL": $Tag
static readonly "NULL": $Tag
static readonly "STR": $Tag
static readonly "SEQ": $Tag
static readonly "MAP": $Tag

constructor(uri: $URI$Type)
constructor(clazz: $Class$Type<(any)>)
constructor(tag: string)

public "equals"(obj: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getValue"(): string
public "startsWith"(prefix: string): boolean
public "matches"(clazz: $Class$Type<(any)>): boolean
public "getClassName"(): string
public "isSecondary"(): boolean
public "isCompatible"(clazz: $Class$Type<(any)>): boolean
get "value"(): string
get "className"(): string
get "secondary"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Tag$Type = ($Tag);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Tag_ = $Tag$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$TextListEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipListEntry, $TooltipListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$TooltipListEntry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $TextListEntry extends $TooltipListEntry<(any)> {
static readonly "LINE_HEIGHT": integer
static readonly "DISABLED_COLOR": integer

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, text: $Component$Type, color: integer, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, text: $Component$Type, color: integer)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, text: $Component$Type)

public "getValue"(): any
public "getDefaultValue"(): $Optional<(any)>
public "render"(graphics: $GuiGraphics$Type, index: integer, y: integer, x: integer, entryWidth: integer, entryHeight: integer, mouseX: integer, mouseY: integer, isHovered: boolean, delta: float): void
public "children"(): $List<(any)>
public "narratables"(): $List<(any)>
public "mouseClicked"(mouseX: double, mouseY: double, button: integer): boolean
public "getItemHeight"(): integer
get "value"(): any
get "defaultValue"(): $Optional<(any)>
get "itemHeight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextListEntry$Type = ($TextListEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextListEntry_ = $TextListEntry$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$LiteralStringValueReader" {
import {$ValueReader, $ValueReader$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueReader"
import {$Context, $Context$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$Context"
import {$AtomicInteger, $AtomicInteger$Type} from "packages/java/util/concurrent/atomic/$AtomicInteger"

export class $LiteralStringValueReader implements $ValueReader {


public "read"(s: string, index: $AtomicInteger$Type, context: $Context$Type): any
public "canRead"(s: string): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LiteralStringValueReader$Type = ($LiteralStringValueReader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LiteralStringValueReader_ = $LiteralStringValueReader$Type;
}}
declare module "packages/me/shedaniel/autoconfig/serializer/$JanksonConfigSerializer" {
import {$Config, $Config$Type} from "packages/me/shedaniel/autoconfig/annotation/$Config"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ConfigSerializer, $ConfigSerializer$Type} from "packages/me/shedaniel/autoconfig/serializer/$ConfigSerializer"
import {$ConfigData, $ConfigData$Type} from "packages/me/shedaniel/autoconfig/$ConfigData"
import {$Jankson, $Jankson$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$Jankson"

export class $JanksonConfigSerializer<T extends $ConfigData> implements $ConfigSerializer<(T)> {

constructor(definition: $Config$Type, configClass: $Class$Type<(T)>, jankson: $Jankson$Type)
constructor(definition: $Config$Type, configClass: $Class$Type<(T)>)

public "deserialize"(): T
public "createDefault"(): T
public "serialize"(config: T): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JanksonConfigSerializer$Type<T> = ($JanksonConfigSerializer<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JanksonConfigSerializer_<T> = $JanksonConfigSerializer$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg18$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg18$Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, T> {

 "construct"(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11, arg11: A12, arg12: A13, arg13: A14, arg14: A15, arg15: A16, arg16: A17, arg17: A18): T

(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11, arg11: A12, arg12: A13, arg13: A14, arg14: A15, arg15: A16, arg16: A17, arg17: A18): T
}

export namespace $RecordValueAnimatorArgs$Arg18$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg18$Op$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, T> = ($RecordValueAnimatorArgs$Arg18$Op<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg18$Op_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, T> = $RecordValueAnimatorArgs$Arg18$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractFieldBuilder" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$FieldBuilder, $FieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$FieldBuilder"

export class $AbstractFieldBuilder<T, A extends $AbstractConfigListEntry<(any)>, SELF extends $FieldBuilder<(T), (A), (SELF)>> extends $FieldBuilder<(T), (A), (SELF)> {


public "getSaveConsumer"(): $Consumer<(T)>
public "setSaveConsumer"(saveConsumer: $Consumer$Type<(T)>): SELF
public "setTooltipSupplier"(tooltipSupplier: $Function$Type<(T), ($Optional$Type<(($Component$Type)[])>)>): SELF
public "setTooltipSupplier"(tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>): SELF
public "requireRestart"(): SELF
public "setTooltip"(tooltip: $Optional$Type<(($Component$Type)[])>): SELF
public "setTooltip"(...tooltip: ($Component$Type)[]): SELF
public "setErrorSupplier"(errorSupplier: $Function$Type<(T), ($Optional$Type<($Component$Type)>)>): SELF
public "setDefaultValue"(defaultValue: T): SELF
public "setDefaultValue"(defaultValue: $Supplier$Type<(T)>): SELF
public "getTooltipSupplier"(): $Function<(T), ($Optional<(($Component)[])>)>
get "saveConsumer"(): $Consumer<(T)>
set "saveConsumer"(value: $Consumer$Type<(T)>)
set "tooltipSupplier"(value: $Function$Type<(T), ($Optional$Type<(($Component$Type)[])>)>)
set "tooltipSupplier"(value: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
set "tooltip"(value: $Optional$Type<(($Component$Type)[])>)
set "tooltip"(value: ($Component$Type)[])
set "errorSupplier"(value: $Function$Type<(T), ($Optional$Type<($Component$Type)>)>)
set "defaultValue"(value: T)
set "defaultValue"(value: $Supplier$Type<(T)>)
get "tooltipSupplier"(): $Function<(T), ($Optional<(($Component)[])>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractFieldBuilder$Type<T, A, SELF> = ($AbstractFieldBuilder<(T), (A), (SELF)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractFieldBuilder_<T, A, SELF> = $AbstractFieldBuilder$Type<(T), (A), (SELF)>;
}}
declare module "packages/me/shedaniel/autoconfig/util/forge/$UtilsImpl" {
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"

export class $UtilsImpl {

constructor()

public static "getConfigFolder"(): $Path
get "configFolder"(): $Path
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UtilsImpl$Type = ($UtilsImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UtilsImpl_ = $UtilsImpl$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$BaseConstructor" {
import {$PropertyUtils, $PropertyUtils$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$PropertyUtils"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$TypeDescription, $TypeDescription$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$TypeDescription"
import {$LoaderOptions, $LoaderOptions$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$LoaderOptions"
import {$Composer, $Composer$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/composer/$Composer"

export class $BaseConstructor {

constructor()
constructor(loadingConfig: $LoaderOptions$Type)

public "getData"(): any
public "checkData"(): boolean
public "getPropertyUtils"(): $PropertyUtils
public "setPropertyUtils"(propertyUtils: $PropertyUtils$Type): void
public "getSingleData"(type: $Class$Type<(any)>): any
public "setComposer"(composer: $Composer$Type): void
public "addTypeDescription"(definition: $TypeDescription$Type): $TypeDescription
public "isAllowDuplicateKeys"(): boolean
public "isWrappedToRootException"(): boolean
public "setWrappedToRootException"(wrappedToRootException: boolean): void
public "setAllowDuplicateKeys"(allowDuplicateKeys: boolean): void
public "isExplicitPropertyUtils"(): boolean
get "data"(): any
get "propertyUtils"(): $PropertyUtils
set "propertyUtils"(value: $PropertyUtils$Type)
set "composer"(value: $Composer$Type)
get "allowDuplicateKeys"(): boolean
get "wrappedToRootException"(): boolean
set "wrappedToRootException"(value: boolean)
set "allowDuplicateKeys"(value: boolean)
get "explicitPropertyUtils"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseConstructor$Type = ($BaseConstructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseConstructor_ = $BaseConstructor$Type;
}}
declare module "packages/me/shedaniel/autoconfig/$ConfigHolder" {
import {$ConfigSerializeEvent$Save, $ConfigSerializeEvent$Save$Type} from "packages/me/shedaniel/autoconfig/event/$ConfigSerializeEvent$Save"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ConfigSerializeEvent$Load, $ConfigSerializeEvent$Load$Type} from "packages/me/shedaniel/autoconfig/event/$ConfigSerializeEvent$Load"
import {$ConfigData, $ConfigData$Type} from "packages/me/shedaniel/autoconfig/$ConfigData"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export interface $ConfigHolder<T extends $ConfigData> extends $Supplier<(T)> {

 "get"(): T
 "load"(): boolean
 "save"(): void
 "resetToDefault"(): void
 "getConfig"(): T
 "getConfigClass"(): $Class<(T)>
 "setConfig"(arg0: T): void
 "registerSaveListener"(arg0: $ConfigSerializeEvent$Save$Type<(T)>): void
 "registerLoadListener"(arg0: $ConfigSerializeEvent$Load$Type<(T)>): void
}

export namespace $ConfigHolder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigHolder$Type<T> = ($ConfigHolder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigHolder_<T> = $ConfigHolder$Type<(T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/external/com/google/gdata/util/common/base/$Escaper" {
import {$Appendable, $Appendable$Type} from "packages/java/lang/$Appendable"

export interface $Escaper {

 "escape"(arg0: string): string
 "escape"(arg0: $Appendable$Type): $Appendable
}

export namespace $Escaper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Escaper$Type = ($Escaper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Escaper_ = $Escaper$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$DocumentStartToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $DocumentStartToken extends $Token {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DocumentStartToken$Type = ($DocumentStartToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DocumentStartToken_ = $DocumentStartToken$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/representer/$Represent" {
import {$Node, $Node$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Node"

export interface $Represent {

 "representData"(arg0: any): $Node

(arg0: any): $Node
}

export namespace $Represent {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Represent$Type = ($Represent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Represent_ = $Represent$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg18$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg18$Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>, arg12: $RecordValueAnimatorArgs$Setter$Type<(A12)>, arg13: $RecordValueAnimatorArgs$Setter$Type<(A13)>, arg14: $RecordValueAnimatorArgs$Setter$Type<(A14)>, arg15: $RecordValueAnimatorArgs$Setter$Type<(A15)>, arg16: $RecordValueAnimatorArgs$Setter$Type<(A16)>, arg17: $RecordValueAnimatorArgs$Setter$Type<(A17)>, arg18: $RecordValueAnimatorArgs$Setter$Type<(A18)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>, arg12: $RecordValueAnimatorArgs$Setter$Type<(A12)>, arg13: $RecordValueAnimatorArgs$Setter$Type<(A13)>, arg14: $RecordValueAnimatorArgs$Setter$Type<(A14)>, arg15: $RecordValueAnimatorArgs$Setter$Type<(A15)>, arg16: $RecordValueAnimatorArgs$Setter$Type<(A16)>, arg17: $RecordValueAnimatorArgs$Setter$Type<(A17)>, arg18: $RecordValueAnimatorArgs$Setter$Type<(A18)>): void
}

export namespace $RecordValueAnimatorArgs$Arg18$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg18$Up$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, T> = ($RecordValueAnimatorArgs$Arg18$Up<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg18$Up_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, T> = $RecordValueAnimatorArgs$Arg18$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (A17), (A18), (T)>;
}}
declare module "packages/me/shedaniel/autoconfig/serializer/$ConfigSerializer$Factory" {
import {$Config, $Config$Type} from "packages/me/shedaniel/autoconfig/annotation/$Config"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ConfigSerializer, $ConfigSerializer$Type} from "packages/me/shedaniel/autoconfig/serializer/$ConfigSerializer"
import {$ConfigData, $ConfigData$Type} from "packages/me/shedaniel/autoconfig/$ConfigData"

export interface $ConfigSerializer$Factory<T extends $ConfigData> {

 "create"(arg0: $Config$Type, arg1: $Class$Type<(T)>): $ConfigSerializer<(T)>

(arg0: $Config$Type, arg1: $Class$Type<(T)>): $ConfigSerializer<(T)>
}

export namespace $ConfigSerializer$Factory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigSerializer$Factory$Type<T> = ($ConfigSerializer$Factory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigSerializer$Factory_<T> = $ConfigSerializer$Factory$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$TickableWidget" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $TickableWidget {

 "tick"(): void

(): void
}

export namespace $TickableWidget {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickableWidget$Type = ($TickableWidget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickableWidget_ = $TickableWidget$Type;
}}
declare module "packages/me/shedaniel/autoconfig/gui/registry/api/$GuiProvider" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$GuiRegistryAccess, $GuiRegistryAccess$Type} from "packages/me/shedaniel/autoconfig/gui/registry/api/$GuiRegistryAccess"

export interface $GuiProvider {

 "get"(arg0: string, arg1: $Field$Type, arg2: any, arg3: any, arg4: $GuiRegistryAccess$Type): $List<($AbstractConfigListEntry)>

(arg0: string, arg1: $Field$Type, arg2: any, arg3: any, arg4: $GuiRegistryAccess$Type): $List<($AbstractConfigListEntry)>
}

export namespace $GuiProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiProvider$Type = ($GuiProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiProvider_ = $GuiProvider$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$ModifierKeyCode" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Modifier, $Modifier$Type} from "packages/me/shedaniel/clothconfig2/api/$Modifier"
import {$InputConstants$Type, $InputConstants$Type$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Type"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export interface $ModifierKeyCode {

 "matchesCurrentKey"(): boolean
 "clearModifier"(): $ModifierKeyCode
 "toString"(): string
 "getType"(): $InputConstants$Type
 "copy"(): $ModifierKeyCode
 "matchesKey"(keyCode: integer, scanCode: integer): boolean
 "getLocalizedName"(): $Component
 "isUnknown"(): boolean
 "getKeyCode"(): $InputConstants$Key
 "matchesMouse"(button: integer): boolean
 "setModifier"(arg0: $Modifier$Type): $ModifierKeyCode
 "setKeyCode"(arg0: $InputConstants$Key$Type): $ModifierKeyCode
 "matchesCurrentMouse"(): boolean
 "getModifier"(): $Modifier
 "setKeyCodeAndModifier"(keyCode: $InputConstants$Key$Type, modifier: $Modifier$Type): $ModifierKeyCode
}

export namespace $ModifierKeyCode {
function copyOf(code: $ModifierKeyCode$Type): $ModifierKeyCode
function of(keyCode: $InputConstants$Key$Type, modifier: $Modifier$Type): $ModifierKeyCode
function unknown(): $ModifierKeyCode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModifierKeyCode$Type = ($ModifierKeyCode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModifierKeyCode_ = $ModifierKeyCode$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/annotation/$NonnullByDefault" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $NonnullByDefault extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $NonnullByDefault {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NonnullByDefault$Type = ($NonnullByDefault);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NonnullByDefault_ = $NonnullByDefault$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$KeyCodeBuilder" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$ModifierKeyCode, $ModifierKeyCode$Type} from "packages/me/shedaniel/clothconfig2/api/$ModifierKeyCode"
import {$KeyCodeEntry, $KeyCodeEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$KeyCodeEntry"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$FieldBuilder, $FieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$FieldBuilder"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $KeyCodeBuilder extends $FieldBuilder<($ModifierKeyCode), ($KeyCodeEntry), ($KeyCodeBuilder)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, value: $ModifierKeyCode$Type)

public "build"(): $KeyCodeEntry
public "setTooltipSupplier"(tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>): $KeyCodeBuilder
public "requireRestart"(): $KeyCodeBuilder
public "setTooltip"(...tooltip: ($Component$Type)[]): $KeyCodeBuilder
public "setTooltip"(tooltip: $Optional$Type<(($Component$Type)[])>): $KeyCodeBuilder
public "setErrorSupplier"(errorSupplier: $Function$Type<($InputConstants$Key$Type), ($Optional$Type<($Component$Type)>)>): $KeyCodeBuilder
public "setDefaultValue"(defaultValue: $Supplier$Type<($InputConstants$Key$Type)>): $KeyCodeBuilder
public "setDefaultValue"(defaultValue: $ModifierKeyCode$Type): $KeyCodeBuilder
public "setDefaultValue"(defaultValue: $InputConstants$Key$Type): $KeyCodeBuilder
public "setAllowMouse"(allowMouse: boolean): $KeyCodeBuilder
public "setAllowKey"(allowKey: boolean): $KeyCodeBuilder
public "setModifierErrorSupplier"(errorSupplier: $Function$Type<($ModifierKeyCode$Type), ($Optional$Type<($Component$Type)>)>): $KeyCodeBuilder
public "setModifierSaveConsumer"(saveConsumer: $Consumer$Type<($ModifierKeyCode$Type)>): $KeyCodeBuilder
public "setModifierDefaultValue"(defaultValue: $Supplier$Type<($ModifierKeyCode$Type)>): $KeyCodeBuilder
public "setKeyTooltipSupplier"(tooltipSupplier: $Function$Type<($InputConstants$Key$Type), ($Optional$Type<(($Component$Type)[])>)>): $KeyCodeBuilder
public "setModifierTooltipSupplier"(tooltipSupplier: $Function$Type<($ModifierKeyCode$Type), ($Optional$Type<(($Component$Type)[])>)>): $KeyCodeBuilder
public "setAllowModifiers"(allowModifiers: boolean): $KeyCodeBuilder
public "setKeySaveConsumer"(saveConsumer: $Consumer$Type<($InputConstants$Key$Type)>): $KeyCodeBuilder
set "tooltipSupplier"(value: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
set "tooltip"(value: ($Component$Type)[])
set "tooltip"(value: $Optional$Type<(($Component$Type)[])>)
set "errorSupplier"(value: $Function$Type<($InputConstants$Key$Type), ($Optional$Type<($Component$Type)>)>)
set "defaultValue"(value: $Supplier$Type<($InputConstants$Key$Type)>)
set "defaultValue"(value: $ModifierKeyCode$Type)
set "defaultValue"(value: $InputConstants$Key$Type)
set "allowMouse"(value: boolean)
set "allowKey"(value: boolean)
set "modifierErrorSupplier"(value: $Function$Type<($ModifierKeyCode$Type), ($Optional$Type<($Component$Type)>)>)
set "modifierSaveConsumer"(value: $Consumer$Type<($ModifierKeyCode$Type)>)
set "modifierDefaultValue"(value: $Supplier$Type<($ModifierKeyCode$Type)>)
set "keyTooltipSupplier"(value: $Function$Type<($InputConstants$Key$Type), ($Optional$Type<(($Component$Type)[])>)>)
set "modifierTooltipSupplier"(value: $Function$Type<($ModifierKeyCode$Type), ($Optional$Type<(($Component$Type)[])>)>)
set "allowModifiers"(value: boolean)
set "keySaveConsumer"(value: $Consumer$Type<($InputConstants$Key$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyCodeBuilder$Type = ($KeyCodeBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyCodeBuilder_ = $KeyCodeBuilder$Type;
}}
declare module "packages/me/shedaniel/autoconfig/annotation/$ConfigEntry" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ConfigEntry {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigEntry$Type = ($ConfigEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigEntry_ = $ConfigEntry$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$MapValueWriter" {
import {$WriterContext, $WriterContext$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$WriterContext"
import {$ValueWriter, $ValueWriter$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueWriter"

export class $MapValueWriter implements $ValueWriter {


public "write"(value: any, context: $WriterContext$Type): void
public "canWrite"(value: any): boolean
public "isPrimitiveType"(): boolean
get "primitiveType"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MapValueWriter$Type = ($MapValueWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MapValueWriter_ = $MapValueWriter$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$ValueAnimator" {
import {$FloatingPoint, $FloatingPoint$Type} from "packages/me/shedaniel/math/$FloatingPoint"
import {$Color, $Color$Type} from "packages/me/shedaniel/math/$Color"
import {$NumberAnimator, $NumberAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$NumberAnimator"
import {$Dimension, $Dimension$Type} from "packages/me/shedaniel/math/$Dimension"
import {$ProgressValueAnimator, $ProgressValueAnimator$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ProgressValueAnimator"
import {$FloatingDimension, $FloatingDimension$Type} from "packages/me/shedaniel/math/$FloatingDimension"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"
import {$FloatingRectangle, $FloatingRectangle$Type} from "packages/me/shedaniel/math/$FloatingRectangle"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ValueProvider, $ValueProvider$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$ValueProvider"
import {$Point, $Point$Type} from "packages/me/shedaniel/math/$Point"

export interface $ValueAnimator<T> extends $ValueProvider<(T)> {

 "map"<R>(converter: $Function$Type<(T), (R)>, backwardsConverter: $Function$Type<(R), (T)>): $ValueAnimator<(R)>
 "setTarget"(arg0: T): $ValueAnimator<(T)>
 "withConvention"(convention: $Supplier$Type<(T)>, duration: long): $ValueAnimator<(T)>
 "setAs"(value: T): $ValueAnimator<(T)>
 "setTo"(arg0: T, arg1: long): $ValueAnimator<(T)>
 "completeImmediately"(): void
 "value"(): T
 "target"(): T
 "update"(arg0: double): void
}

export namespace $ValueAnimator {
function ofLong(): $NumberAnimator<(long)>
function ofLong(initialValue: long): $NumberAnimator<(long)>
function ofFloatingPoint(): $ValueAnimator<($FloatingPoint)>
function ofFloatingPoint(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
function ofDimension(initialValue: $Point$Type): $ValueAnimator<($Point)>
function ofDimension(initialValue: $Dimension$Type): $ValueAnimator<($Dimension)>
function ofDimension(): $ValueAnimator<($Dimension)>
function ofRectangle(initialValue: $Rectangle$Type): $ValueAnimator<($Rectangle)>
function ofRectangle(): $ValueAnimator<($Rectangle)>
function ofPoint(initialValue: $Point$Type): $ValueAnimator<($Point)>
function ofPoint(): $ValueAnimator<($Point)>
function ofInt(): $NumberAnimator<(integer)>
function ofInt(initialValue: integer): $NumberAnimator<(integer)>
function ofDouble(): $NumberAnimator<(double)>
function ofDouble(initialValue: double): $NumberAnimator<(double)>
function ofBoolean(): $ProgressValueAnimator<(boolean)>
function ofBoolean(switchPoint: double, initialValue: boolean): $ProgressValueAnimator<(boolean)>
function ofBoolean(switchPoint: double): $ProgressValueAnimator<(boolean)>
function ofBoolean(initialValue: boolean): $ProgressValueAnimator<(boolean)>
function ofFloat(initialValue: float): $NumberAnimator<(float)>
function ofFloat(): $NumberAnimator<(float)>
function ofColor(): $ValueAnimator<($Color)>
function ofColor(initialValue: $Color$Type): $ValueAnimator<($Color)>
function ofFloatingRectangle(initialValue: $FloatingRectangle$Type): $ValueAnimator<($FloatingRectangle)>
function ofFloatingRectangle(): $ValueAnimator<($FloatingRectangle)>
function ofFloatingDimension(initialValue: $FloatingPoint$Type): $ValueAnimator<($FloatingPoint)>
function ofFloatingDimension(): $ValueAnimator<($FloatingDimension)>
function ofFloatingDimension(initialValue: $FloatingDimension$Type): $ValueAnimator<($FloatingDimension)>
function typicalTransitionTime(): long
function constant<T>(value: T): $ValueProvider<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueAnimator$Type<T> = ($ValueAnimator<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueAnimator_<T> = $ValueAnimator$Type<(T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/magic/$TypeMagic" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"

export class $TypeMagic {

constructor()

public static "shoehorn"<T>(o: any): T
public static "createAndCast"<U>(t: $Class$Type<(U)>, failFast: boolean): U
public static "createAndCast"<U>(t: $Type$Type): U
public static "classForType"(t: $Type$Type): $Class<(any)>
public static "createAndCastCarefully"<U>(t: $Type$Type): U
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TypeMagic$Type = ($TypeMagic);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TypeMagic_ = $TypeMagic$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$WhitespaceToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $WhitespaceToken extends $Token {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WhitespaceToken$Type = ($WhitespaceToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WhitespaceToken_ = $WhitespaceToken$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$Comment" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $Comment extends $Annotation {

 "value"(): string
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $Comment {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Comment$Type = ($Comment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Comment_ = $Comment$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/widget/$SearchFieldEntry" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ConfigScreen, $ConfigScreen$Type} from "packages/me/shedaniel/clothconfig2/api/$ConfigScreen"
import {$ClothConfigScreen$ListWidget, $ClothConfigScreen$ListWidget$Type} from "packages/me/shedaniel/clothconfig2/gui/$ClothConfigScreen$ListWidget"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$AbstractConfigEntry, $AbstractConfigEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigEntry"

export class $SearchFieldEntry extends $AbstractConfigListEntry<(any)> {

constructor(screen: $ConfigScreen$Type, listWidget: $ClothConfigScreen$ListWidget$Type<($AbstractConfigEntry$Type<($AbstractConfigEntry$Type<(any)>)>)>)

public "getValue"(): any
public "getDefaultValue"(): $Optional<(any)>
public "render"(graphics: $GuiGraphics$Type, index: integer, y: integer, x: integer, entryWidth: integer, entryHeight: integer, mouseX: integer, mouseY: integer, isHovered: boolean, delta: float): void
public "children"(): $List<(any)>
public "narratables"(): $List<(any)>
public "matchesSearch"(tags: $Iterator$Type<(string)>): boolean
get "value"(): any
get "defaultValue"(): $Optional<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SearchFieldEntry$Type = ($SearchFieldEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SearchFieldEntry_ = $SearchFieldEntry$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$ScissorsHandler" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"

export interface $ScissorsHandler {

 "scissor"(arg0: $Rectangle$Type): void
 "removeLastScissor"(): void
 "clearScissors"(): void
 "applyScissors"(): void
 "getScissorsAreas"(): $List<($Rectangle)>
}

export namespace $ScissorsHandler {
const INSTANCE: $ScissorsHandler
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScissorsHandler$Type = ($ScissorsHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScissorsHandler_ = $ScissorsHandler$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$CustomClassLoaderConstructor" {
import {$ClassLoader, $ClassLoader$Type} from "packages/java/lang/$ClassLoader"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Constructor, $Constructor$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$Constructor"
import {$SafeConstructor$ConstructUndefined, $SafeConstructor$ConstructUndefined$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$SafeConstructor$ConstructUndefined"

export class $CustomClassLoaderConstructor extends $Constructor {
static readonly "undefinedConstructor": $SafeConstructor$ConstructUndefined

constructor(cLoader: $ClassLoader$Type)
constructor(theRoot: $Class$Type<(any)>, theLoader: $ClassLoader$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomClassLoaderConstructor$Type = ($CustomClassLoaderConstructor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomClassLoaderConstructor_ = $CustomClassLoaderConstructor$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ArrayValueWriter" {
import {$WriterContext, $WriterContext$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$WriterContext"
import {$ValueWriter, $ValueWriter$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueWriter"

export class $ArrayValueWriter implements $ValueWriter {


public "isPrimitiveType"(): boolean
public "write"(arg0: any, arg1: $WriterContext$Type): void
public "canWrite"(arg0: any): boolean
get "primitiveType"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArrayValueWriter$Type = ($ArrayValueWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArrayValueWriter_ = $ArrayValueWriter$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg3$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg3$Up<A1, A2, A3, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>): void
}

export namespace $RecordValueAnimatorArgs$Arg3$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg3$Up$Type<A1, A2, A3, T> = ($RecordValueAnimatorArgs$Arg3$Up<(A1), (A2), (A3), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg3$Up_<A1, A2, A3, T> = $RecordValueAnimatorArgs$Arg3$Up$Type<(A1), (A2), (A3), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg3$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg3$Op<A1, A2, A3, T> {

 "construct"(arg0: A1, arg1: A2, arg2: A3): T

(arg0: A1, arg1: A2, arg2: A3): T
}

export namespace $RecordValueAnimatorArgs$Arg3$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg3$Op$Type<A1, A2, A3, T> = ($RecordValueAnimatorArgs$Arg3$Op<(A1), (A2), (A3), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg3$Op_<A1, A2, A3, T> = $RecordValueAnimatorArgs$Arg3$Op$Type<(A1), (A2), (A3), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/parser/$Production" {
import {$Event, $Event$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event"

export interface $Production {

 "produce"(): $Event

(): $Event
}

export namespace $Production {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Production$Type = ($Production);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Production_ = $Production$Type;
}}
declare module "packages/me/shedaniel/autoconfig/example/$ExampleInits" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ExampleInits {

constructor()

public static "exampleClientInit"(): void
public static "exampleCommonInit"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExampleInits$Type = ($ExampleInits);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExampleInits_ = $ExampleInits$Type;
}}
declare module "packages/me/shedaniel/autoconfig/gui/$ConfigScreenProvider" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ConfigBuilder, $ConfigBuilder$Type} from "packages/me/shedaniel/clothconfig2/api/$ConfigBuilder"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ConfigData, $ConfigData$Type} from "packages/me/shedaniel/autoconfig/$ConfigData"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$ConfigManager, $ConfigManager$Type} from "packages/me/shedaniel/autoconfig/$ConfigManager"
import {$GuiRegistryAccess, $GuiRegistryAccess$Type} from "packages/me/shedaniel/autoconfig/gui/registry/api/$GuiRegistryAccess"

export class $ConfigScreenProvider<T extends $ConfigData> implements $Supplier<($Screen)> {

constructor(manager: $ConfigManager$Type<(T)>, registry: $GuiRegistryAccess$Type, parent: $Screen$Type)

/**
 * 
 * @deprecated
 */
public "setCategoryFunction"(categoryFunction: $BiFunction$Type<(string), (string), (string)>): void
/**
 * 
 * @deprecated
 */
public "setI13nFunction"(i18nFunction: $Function$Type<($ConfigManager$Type<(T)>), (string)>): void
/**
 * 
 * @deprecated
 */
public "setOptionFunction"(optionFunction: $BiFunction$Type<(string), ($Field$Type), (string)>): void
/**
 * 
 * @deprecated
 */
public "setBuildFunction"(buildFunction: $Function$Type<($ConfigBuilder$Type), ($Screen$Type)>): void
set "categoryFunction"(value: $BiFunction$Type<(string), (string), (string)>)
set "i13nFunction"(value: $Function$Type<($ConfigManager$Type<(T)>), (string)>)
set "optionFunction"(value: $BiFunction$Type<(string), ($Field$Type), (string)>)
set "buildFunction"(value: $Function$Type<($ConfigBuilder$Type), ($Screen$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigScreenProvider$Type<T> = ($ConfigScreenProvider<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigScreenProvider_<T> = $ConfigScreenProvider$Type<(T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$MissingProperty" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Property, $Property$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$Property"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"

export class $MissingProperty extends $Property {

constructor(name: string)

public "get"(object: any): any
public "getAnnotation"<A extends $Annotation>(annotationType: $Class$Type<(A)>): A
public "getAnnotations"(): $List<($Annotation)>
public "set"(object: any, value: any): void
public "getActualTypeArguments"(): ($Class<(any)>)[]
get "annotations"(): $List<($Annotation)>
get "actualTypeArguments"(): ($Class<(any)>)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MissingProperty$Type = ($MissingProperty);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MissingProperty_ = $MissingProperty$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueWriters" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ValueWriters {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueWriters$Type = ($ValueWriters);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueWriters_ = $ValueWriters$Type;
}}
declare module "packages/me/shedaniel/autoconfig/example/$ExampleConfig$Empty" {
import {$ConfigData, $ConfigData$Type} from "packages/me/shedaniel/autoconfig/$ConfigData"

export class $ExampleConfig$Empty implements $ConfigData {

constructor()

public "validatePostLoad"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExampleConfig$Empty$Type = ($ExampleConfig$Empty);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExampleConfig$Empty_ = $ExampleConfig$Empty$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/resolver/$Resolver" {
import {$Pattern, $Pattern$Type} from "packages/java/util/regex/$Pattern"
import {$Tag, $Tag$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Tag"
import {$NodeId, $NodeId$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$NodeId"

export class $Resolver {
static readonly "BOOL": $Pattern
static readonly "FLOAT": $Pattern
static readonly "INT": $Pattern
static readonly "MERGE": $Pattern
static readonly "NULL": $Pattern
static readonly "EMPTY": $Pattern
static readonly "TIMESTAMP": $Pattern
static readonly "VALUE": $Pattern
static readonly "YAML": $Pattern

constructor()

public "resolve"(kind: $NodeId$Type, value: string, implicit: boolean): $Tag
public "addImplicitResolver"(tag: $Tag$Type, regexp: $Pattern$Type, first: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Resolver$Type = ($Resolver);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Resolver_ = $Resolver$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$IntListBuilder" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AbstractRangeListBuilder, $AbstractRangeListBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractRangeListBuilder"
import {$IntegerListListEntry$IntegerListCell, $IntegerListListEntry$IntegerListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$IntegerListListEntry$IntegerListCell"
import {$IntegerListListEntry, $IntegerListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$IntegerListListEntry"

export class $IntListBuilder extends $AbstractRangeListBuilder<(integer), ($IntegerListListEntry), ($IntListBuilder)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, value: $List$Type<(integer)>)

public "build"(): $IntegerListListEntry
public "setMin"(min: integer): $IntListBuilder
public "setTooltip"(...tooltip: ($Component$Type)[]): $IntListBuilder
public "setTooltip"(tooltip: $Optional$Type<(($Component$Type)[])>): $IntListBuilder
public "setErrorSupplier"(errorSupplier: $Function$Type<($List$Type<(integer)>), ($Optional$Type<($Component$Type)>)>): $IntListBuilder
public "getCellErrorSupplier"(): $Function<(integer), ($Optional<($Component)>)>
public "setCreateNewInstance"(createNewInstance: $Function$Type<($IntegerListListEntry$Type), ($IntegerListListEntry$IntegerListCell$Type)>): $IntListBuilder
public "setDefaultValue"(defaultValue: $List$Type<(integer)>): $IntListBuilder
public "setMax"(max: integer): $IntListBuilder
public "setInsertInFront"(insertInFront: boolean): $IntListBuilder
public "removeMin"(): $IntListBuilder
public "setAddButtonTooltip"(addTooltip: $Component$Type): $IntListBuilder
public "setRemoveButtonTooltip"(removeTooltip: $Component$Type): $IntListBuilder
set "min"(value: integer)
set "tooltip"(value: ($Component$Type)[])
set "tooltip"(value: $Optional$Type<(($Component$Type)[])>)
set "errorSupplier"(value: $Function$Type<($List$Type<(integer)>), ($Optional$Type<($Component$Type)>)>)
get "cellErrorSupplier"(): $Function<(integer), ($Optional<($Component)>)>
set "createNewInstance"(value: $Function$Type<($IntegerListListEntry$Type), ($IntegerListListEntry$IntegerListCell$Type)>)
set "defaultValue"(value: $List$Type<(integer)>)
set "max"(value: integer)
set "insertInFront"(value: boolean)
set "addButtonTooltip"(value: $Component$Type)
set "removeButtonTooltip"(value: $Component$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntListBuilder$Type = ($IntListBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntListBuilder_ = $IntListBuilder$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg1$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg1$Up<A1, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>): void
}

export namespace $RecordValueAnimatorArgs$Arg1$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg1$Up$Type<A1, T> = ($RecordValueAnimatorArgs$Arg1$Up<(A1), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg1$Up_<A1, T> = $RecordValueAnimatorArgs$Arg1$Up$Type<(A1), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$FloatListEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractNumberListEntry, $AbstractNumberListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractNumberListEntry"

export class $FloatListEntry extends $AbstractNumberListEntry<(float)> {

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: float, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(float)>, saveConsumer: $Consumer$Type<(float)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, requiresRestart: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: float, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(float)>, saveConsumer: $Consumer$Type<(float)>, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: float, resetButtonKey: $Component$Type, defaultValue: $Supplier$Type<(float)>, saveConsumer: $Consumer$Type<(float)>)

public "getValue"(): float
public "getError"(): $Optional<($Component)>
public "setMaximum"(maximum: float): $FloatListEntry
public "setMinimum"(minimum: float): $FloatListEntry
get "value"(): float
get "error"(): $Optional<($Component)>
set "maximum"(value: float)
set "minimum"(value: float)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FloatListEntry$Type = ($FloatListEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FloatListEntry_ = $FloatListEntry$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"
import {$AbstractConfigEntry, $AbstractConfigEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigEntry"

export class $AbstractConfigListEntry<T> extends $AbstractConfigEntry<(T)> {

constructor(fieldName: $Component$Type, requiresRestart: boolean)

public "getFieldName"(): $Component
public "setRequiresRestart"(requiresRestart: boolean): void
public "render"(graphics: $GuiGraphics$Type, index: integer, y: integer, x: integer, entryWidth: integer, entryHeight: integer, mouseX: integer, mouseY: integer, isHovered: boolean, delta: float): void
public "isEditable"(): boolean
public "isRequiresRestart"(): boolean
public "setEditable"(editable: boolean): void
public "getEntryArea"(x: integer, y: integer, entryWidth: integer, entryHeight: integer): $Rectangle
public "isMouseInside"(mouseX: integer, mouseY: integer, x: integer, y: integer, entryWidth: integer, entryHeight: integer): boolean
public "getPreferredTextColor"(): integer
get "fieldName"(): $Component
set "requiresRestart"(value: boolean)
get "editable"(): boolean
get "requiresRestart"(): boolean
set "editable"(value: boolean)
get "preferredTextColor"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractConfigListEntry$Type<T> = ($AbstractConfigListEntry<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractConfigListEntry_<T> = $AbstractConfigListEntry$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$IntSliderBuilder" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractSliderFieldBuilder, $AbstractSliderFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractSliderFieldBuilder"
import {$IntegerSliderEntry, $IntegerSliderEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$IntegerSliderEntry"

export class $IntSliderBuilder extends $AbstractSliderFieldBuilder<(integer), ($IntegerSliderEntry), ($IntSliderBuilder)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, value: integer, min: integer, max: integer)

public "setMin"(min: integer): $IntSliderBuilder
public "setTooltipSupplier"(tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>): $IntSliderBuilder
public "setTooltipSupplier"(tooltipSupplier: $Function$Type<(integer), ($Optional$Type<(($Component$Type)[])>)>): $IntSliderBuilder
public "requireRestart"(): $IntSliderBuilder
public "setDefaultValue"(defaultValue: $Supplier$Type<(integer)>): $IntSliderBuilder
public "setDefaultValue"(defaultValue: integer): $IntSliderBuilder
public "setMax"(max: integer): $IntSliderBuilder
public "removeMin"(): $IntSliderBuilder
set "min"(value: integer)
set "tooltipSupplier"(value: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
set "tooltipSupplier"(value: $Function$Type<(integer), ($Optional$Type<(($Component$Type)[])>)>)
set "defaultValue"(value: $Supplier$Type<(integer)>)
set "defaultValue"(value: integer)
set "max"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntSliderBuilder$Type = ($IntSliderBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntSliderBuilder_ = $IntSliderBuilder$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg1$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg1$Op<A1, T> {

 "construct"(arg0: A1): T

(arg0: A1): T
}

export namespace $RecordValueAnimatorArgs$Arg1$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg1$Op$Type<A1, T> = ($RecordValueAnimatorArgs$Arg1$Op<(A1), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg1$Op_<A1, T> = $RecordValueAnimatorArgs$Arg1$Op$Type<(A1), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/util/$PlatformFeatureDetector" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PlatformFeatureDetector {

constructor()

public "isRunningOnAndroid"(): boolean
get "runningOnAndroid"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformFeatureDetector$Type = ($PlatformFeatureDetector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformFeatureDetector_ = $PlatformFeatureDetector$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueReaders" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ValueReaders {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueReaders$Type = ($ValueReaders);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueReaders_ = $ValueReaders$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$BooleanValueReaderWriter" {
import {$ValueReader, $ValueReader$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueReader"
import {$WriterContext, $WriterContext$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$WriterContext"
import {$Context, $Context$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$Context"
import {$AtomicInteger, $AtomicInteger$Type} from "packages/java/util/concurrent/atomic/$AtomicInteger"
import {$ValueWriter, $ValueWriter$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$ValueWriter"

export class $BooleanValueReaderWriter implements $ValueReader, $ValueWriter {


public "toString"(): string
public "write"(value: any, context: $WriterContext$Type): void
public "read"(s: string, index: $AtomicInteger$Type, context: $Context$Type): any
public "canRead"(s: string): boolean
public "canWrite"(value: any): boolean
public "isPrimitiveType"(): boolean
get "primitiveType"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BooleanValueReaderWriter$Type = ($BooleanValueReaderWriter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BooleanValueReaderWriter_ = $BooleanValueReaderWriter$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$SelectorBuilder" {
import {$SelectionListEntry, $SelectionListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$SelectionListEntry"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$AbstractFieldBuilder, $AbstractFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractFieldBuilder"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"

export class $SelectorBuilder<T> extends $AbstractFieldBuilder<(T), ($SelectionListEntry<(T)>), ($SelectorBuilder<(T)>)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, valuesArray: (T)[], value: T)

public "setNameProvider"(enumNameProvider: $Function$Type<(T), ($Component$Type)>): $SelectorBuilder<(T)>
public "setSaveConsumer"(saveConsumer: $Consumer$Type<(T)>): $SelectorBuilder<(T)>
public "requireRestart"(): $SelectorBuilder<(T)>
public "setTooltip"(tooltip: $Optional$Type<(($Component$Type)[])>): $SelectorBuilder<(T)>
public "setDefaultValue"(defaultValue: T): $SelectorBuilder<(T)>
set "nameProvider"(value: $Function$Type<(T), ($Component$Type)>)
set "saveConsumer"(value: $Consumer$Type<(T)>)
set "tooltip"(value: $Optional$Type<(($Component$Type)[])>)
set "defaultValue"(value: T)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SelectorBuilder$Type<T> = ($SelectorBuilder<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SelectorBuilder_<T> = $SelectorBuilder$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$IntegerListListEntry$IntegerListCell" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$AbstractTextFieldListListEntry$AbstractTextFieldListCell, $AbstractTextFieldListListEntry$AbstractTextFieldListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractTextFieldListListEntry$AbstractTextFieldListCell"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$IntegerListListEntry, $IntegerListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$IntegerListListEntry"

export class $IntegerListListEntry$IntegerListCell extends $AbstractTextFieldListListEntry$AbstractTextFieldListCell<(integer), ($IntegerListListEntry$IntegerListCell), ($IntegerListListEntry)> {

constructor(value: integer, listListEntry: $IntegerListListEntry$Type)

public "getValue"(): integer
public "getError"(): $Optional<($Component)>
get "value"(): integer
get "error"(): $Optional<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IntegerListListEntry$IntegerListCell$Type = ($IntegerListListEntry$IntegerListCell);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IntegerListListEntry$IntegerListCell_ = $IntegerListListEntry$IntegerListCell$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/com/moandjiezana/toml/$Identifier" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Identifier {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Identifier$Type = ($Identifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Identifier_ = $Identifier$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$LongListBuilder" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$LongListListEntry$LongListCell, $LongListListEntry$LongListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$LongListListEntry$LongListCell"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$LongListListEntry, $LongListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$LongListListEntry"
import {$AbstractRangeListBuilder, $AbstractRangeListBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractRangeListBuilder"

export class $LongListBuilder extends $AbstractRangeListBuilder<(long), ($LongListListEntry), ($LongListBuilder)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, value: $List$Type<(long)>)

public "build"(): $LongListListEntry
public "setMin"(min: long): $LongListBuilder
public "setTooltip"(...tooltip: ($Component$Type)[]): $LongListBuilder
public "setTooltip"(tooltip: $Optional$Type<(($Component$Type)[])>): $LongListBuilder
public "setErrorSupplier"(errorSupplier: $Function$Type<($List$Type<(long)>), ($Optional$Type<($Component$Type)>)>): $LongListBuilder
public "getCellErrorSupplier"(): $Function<(long), ($Optional<($Component)>)>
public "setCreateNewInstance"(createNewInstance: $Function$Type<($LongListListEntry$Type), ($LongListListEntry$LongListCell$Type)>): $LongListBuilder
public "setDefaultValue"(defaultValue: $List$Type<(long)>): $LongListBuilder
public "setMax"(max: long): $LongListBuilder
public "setInsertInFront"(insertInFront: boolean): $LongListBuilder
public "removeMin"(): $LongListBuilder
public "setAddButtonTooltip"(addTooltip: $Component$Type): $LongListBuilder
public "setRemoveButtonTooltip"(removeTooltip: $Component$Type): $LongListBuilder
set "min"(value: long)
set "tooltip"(value: ($Component$Type)[])
set "tooltip"(value: $Optional$Type<(($Component$Type)[])>)
set "errorSupplier"(value: $Function$Type<($List$Type<(long)>), ($Optional$Type<($Component$Type)>)>)
get "cellErrorSupplier"(): $Function<(long), ($Optional<($Component)>)>
set "createNewInstance"(value: $Function$Type<($LongListListEntry$Type), ($LongListListEntry$LongListCell$Type)>)
set "defaultValue"(value: $List$Type<(long)>)
set "max"(value: long)
set "insertInFront"(value: boolean)
set "addButtonTooltip"(value: $Component$Type)
set "removeButtonTooltip"(value: $Component$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LongListBuilder$Type = ($LongListBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LongListBuilder_ = $LongListBuilder$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/annotation/$Nullable" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $Nullable extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $Nullable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Nullable$Type = ($Nullable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Nullable_ = $Nullable$Type;
}}
declare module "packages/me/shedaniel/autoconfig/gui/registry/$ComposedGuiRegistryAccess" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$GuiRegistryAccess, $GuiRegistryAccess$Type} from "packages/me/shedaniel/autoconfig/gui/registry/api/$GuiRegistryAccess"

export class $ComposedGuiRegistryAccess implements $GuiRegistryAccess {

constructor(...children: ($GuiRegistryAccess$Type)[])

public "get"(i18n: string, field: $Field$Type, config: any, defaults: any, registry: $GuiRegistryAccess$Type): $List<($AbstractConfigListEntry)>
public "transform"(guis: $List$Type<($AbstractConfigListEntry$Type)>, i18n: string, field: $Field$Type, config: any, defaults: any, registry: $GuiRegistryAccess$Type): $List<($AbstractConfigListEntry)>
public "getAndTransform"(i18n: string, field: $Field$Type, config: any, defaults: any, registry: $GuiRegistryAccess$Type): $List<($AbstractConfigListEntry)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComposedGuiRegistryAccess$Type = ($ComposedGuiRegistryAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComposedGuiRegistryAccess_ = $ComposedGuiRegistryAccess$Type;
}}
declare module "packages/me/shedaniel/autoconfig/$AutoConfig" {
import {$ConfigHolder, $ConfigHolder$Type} from "packages/me/shedaniel/autoconfig/$ConfigHolder"
import {$GuiRegistry, $GuiRegistry$Type} from "packages/me/shedaniel/autoconfig/gui/registry/$GuiRegistry"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$ConfigData, $ConfigData$Type} from "packages/me/shedaniel/autoconfig/$ConfigData"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ConfigSerializer$Factory, $ConfigSerializer$Factory$Type} from "packages/me/shedaniel/autoconfig/serializer/$ConfigSerializer$Factory"

export class $AutoConfig {
static readonly "MOD_ID": string


public static "register"<T extends $ConfigData>(configClass: $Class$Type<(T)>, serializerFactory: $ConfigSerializer$Factory$Type<(T)>): $ConfigHolder<(T)>
public static "getGuiRegistry"<T extends $ConfigData>(configClass: $Class$Type<(T)>): $GuiRegistry
public static "getConfigScreen"<T extends $ConfigData>(configClass: $Class$Type<(T)>, parent: $Screen$Type): $Supplier<($Screen)>
public static "getConfigHolder"<T extends $ConfigData>(configClass: $Class$Type<(T)>): $ConfigHolder<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AutoConfig$Type = ($AutoConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AutoConfig_ = $AutoConfig$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$DocumentEndEvent" {
import {$Event$ID, $Event$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Event, $Event$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event"

export class $DocumentEndEvent extends $Event {

constructor(startMark: $Mark$Type, endMark: $Mark$Type, explicit: boolean)

public "getEventId"(): $Event$ID
public "getExplicit"(): boolean
get "eventId"(): $Event$ID
get "explicit"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DocumentEndEvent$Type = ($DocumentEndEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DocumentEndEvent_ = $DocumentEndEvent$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$GenericProperty" {
import {$Property, $Property$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$Property"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Type, $Type$Type} from "packages/java/lang/reflect/$Type"

export class $GenericProperty extends $Property {

constructor(name: string, aClass: $Class$Type<(any)>, aType: $Type$Type)

public "getActualTypeArguments"(): ($Class<(any)>)[]
get "actualTypeArguments"(): ($Class<(any)>)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenericProperty$Type = ($GenericProperty);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenericProperty_ = $GenericProperty$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$MarkedYAMLException" {
import {$YAMLException, $YAMLException$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$YAMLException"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $MarkedYAMLException extends $YAMLException {


public "toString"(): string
public "getMessage"(): string
public "getContext"(): string
public "getContextMark"(): $Mark
public "getProblem"(): string
public "getProblemMark"(): $Mark
get "message"(): string
get "context"(): string
get "contextMark"(): $Mark
get "problem"(): string
get "problemMark"(): $Mark
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MarkedYAMLException$Type = ($MarkedYAMLException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MarkedYAMLException_ = $MarkedYAMLException$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonGrammar$Builder" {
import {$JsonGrammar, $JsonGrammar$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonGrammar"

export class $JsonGrammar$Builder {

constructor()

public "build"(): $JsonGrammar
public "printTrailingCommas"(trailing: boolean): $JsonGrammar$Builder
public "bareSpecialNumerics"(bare: boolean): $JsonGrammar$Builder
public "printCommas"(commas: boolean): $JsonGrammar$Builder
public "bareRootObject"(bare: boolean): $JsonGrammar$Builder
public "printUnquotedKeys"(unquoted: boolean): $JsonGrammar$Builder
public "printWhitespace"(whitespace: boolean): $JsonGrammar$Builder
public "withComments"(comments: boolean): $JsonGrammar$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonGrammar$Builder$Type = ($JsonGrammar$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonGrammar$Builder_ = $JsonGrammar$Builder$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$StringListListEntry" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$StringListListEntry$StringListCell, $StringListListEntry$StringListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$StringListListEntry$StringListCell"
import {$AbstractTextFieldListListEntry, $AbstractTextFieldListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractTextFieldListListEntry"

export class $StringListListEntry extends $AbstractTextFieldListListEntry<(string), ($StringListListEntry$StringListCell), ($StringListListEntry)> {

/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: $List$Type<(string)>, defaultExpanded: boolean, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, saveConsumer: $Consumer$Type<($List$Type<(string)>)>, defaultValue: $Supplier$Type<($List$Type<(string)>)>, resetButtonKey: $Component$Type, requiresRestart: boolean, deleteButtonEnabled: boolean, insertInFront: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: $List$Type<(string)>, defaultExpanded: boolean, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, saveConsumer: $Consumer$Type<($List$Type<(string)>)>, defaultValue: $Supplier$Type<($List$Type<(string)>)>, resetButtonKey: $Component$Type, requiresRestart: boolean)
/**
 * 
 * @deprecated
 */
constructor(fieldName: $Component$Type, value: $List$Type<(string)>, defaultExpanded: boolean, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, saveConsumer: $Consumer$Type<($List$Type<(string)>)>, defaultValue: $Supplier$Type<($List$Type<(string)>)>, resetButtonKey: $Component$Type)

public "self"(): $StringListListEntry
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringListListEntry$Type = ($StringListListEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringListListEntry_ = $StringListListEntry$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$AnnotatedElement" {
import {$JsonElement, $JsonElement$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonElement"

export class $AnnotatedElement {

constructor(elem: $JsonElement$Type, comment: string)

public "getComment"(): string
public "getElement"(): $JsonElement
get "comment"(): string
get "element"(): $JsonElement
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AnnotatedElement$Type = ($AnnotatedElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AnnotatedElement_ = $AnnotatedElement$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Token$ID extends $Enum<($Token$ID)> {
static readonly "Alias": $Token$ID
static readonly "Anchor": $Token$ID
static readonly "BlockEnd": $Token$ID
static readonly "BlockEntry": $Token$ID
static readonly "BlockMappingStart": $Token$ID
static readonly "BlockSequenceStart": $Token$ID
static readonly "Directive": $Token$ID
static readonly "DocumentEnd": $Token$ID
static readonly "DocumentStart": $Token$ID
static readonly "FlowEntry": $Token$ID
static readonly "FlowMappingEnd": $Token$ID
static readonly "FlowMappingStart": $Token$ID
static readonly "FlowSequenceEnd": $Token$ID
static readonly "FlowSequenceStart": $Token$ID
static readonly "Key": $Token$ID
static readonly "Scalar": $Token$ID
static readonly "StreamEnd": $Token$ID
static readonly "StreamStart": $Token$ID
static readonly "Tag": $Token$ID
static readonly "Value": $Token$ID
static readonly "Whitespace": $Token$ID
static readonly "Comment": $Token$ID
static readonly "Error": $Token$ID


public "toString"(): string
public static "values"(): ($Token$ID)[]
public static "valueOf"(name: string): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Token$ID$Type = (("blockentry") | ("flowsequencestart") | ("blocksequencestart") | ("streamend") | ("error") | ("blockend") | ("flowmappingend") | ("flowentry") | ("flowsequenceend") | ("directive") | ("blockmappingstart") | ("scalar") | ("documentend") | ("anchor") | ("flowmappingstart") | ("alias") | ("streamstart") | ("comment") | ("tag") | ("documentstart") | ("whitespace") | ("value") | ("key")) | ($Token$ID);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Token$ID_ = $Token$ID$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$FloatListBuilder" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$FloatListListEntry, $FloatListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$FloatListListEntry"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$FloatListListEntry$FloatListCell, $FloatListListEntry$FloatListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$FloatListListEntry$FloatListCell"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$AbstractRangeListBuilder, $AbstractRangeListBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractRangeListBuilder"

export class $FloatListBuilder extends $AbstractRangeListBuilder<(float), ($FloatListListEntry), ($FloatListBuilder)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, value: $List$Type<(float)>)

public "build"(): $FloatListListEntry
public "setMin"(min: float): $FloatListBuilder
public "setTooltipSupplier"(tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>): $FloatListBuilder
public "requireRestart"(): $FloatListBuilder
public "setTooltip"(tooltip: $Optional$Type<(($Component$Type)[])>): $FloatListBuilder
public "setTooltip"(...tooltip: ($Component$Type)[]): $FloatListBuilder
public "getCellErrorSupplier"(): $Function<(float), ($Optional<($Component)>)>
public "setCellErrorSupplier"(cellErrorSupplier: $Function$Type<(float), ($Optional$Type<($Component$Type)>)>): $FloatListBuilder
public "setCreateNewInstance"(createNewInstance: $Function$Type<($FloatListListEntry$Type), ($FloatListListEntry$FloatListCell$Type)>): $FloatListBuilder
public "setDefaultValue"(defaultValue: $List$Type<(float)>): $FloatListBuilder
public "setMax"(max: float): $FloatListBuilder
public "removeMin"(): $FloatListBuilder
public "setAddButtonTooltip"(addTooltip: $Component$Type): $FloatListBuilder
public "setRemoveButtonTooltip"(removeTooltip: $Component$Type): $FloatListBuilder
set "min"(value: float)
set "tooltipSupplier"(value: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
set "tooltip"(value: $Optional$Type<(($Component$Type)[])>)
set "tooltip"(value: ($Component$Type)[])
get "cellErrorSupplier"(): $Function<(float), ($Optional<($Component)>)>
set "cellErrorSupplier"(value: $Function$Type<(float), ($Optional$Type<($Component$Type)>)>)
set "createNewInstance"(value: $Function$Type<($FloatListListEntry$Type), ($FloatListListEntry$FloatListCell$Type)>)
set "defaultValue"(value: $List$Type<(float)>)
set "max"(value: float)
set "addButtonTooltip"(value: $Component$Type)
set "removeButtonTooltip"(value: $Component$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FloatListBuilder$Type = ($FloatListBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FloatListBuilder_ = $FloatListBuilder$Type;
}}
declare module "packages/me/shedaniel/autoconfig/event/$ConfigSerializeEvent$Load" {
import {$ConfigHolder, $ConfigHolder$Type} from "packages/me/shedaniel/autoconfig/$ConfigHolder"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$ConfigData, $ConfigData$Type} from "packages/me/shedaniel/autoconfig/$ConfigData"

export interface $ConfigSerializeEvent$Load<T extends $ConfigData> {

 "onLoad"(arg0: $ConfigHolder$Type<(T)>, arg1: T): $InteractionResult

(arg0: $ConfigHolder$Type<(T)>, arg1: T): $InteractionResult
}

export namespace $ConfigSerializeEvent$Load {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigSerializeEvent$Load$Type<T> = ($ConfigSerializeEvent$Load<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigSerializeEvent$Load_<T> = $ConfigSerializeEvent$Load$Type<(T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractTextFieldListListEntry$AbstractTextFieldListCell" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry$NarrationPriority, $NarratableEntry$NarrationPriority$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry$NarrationPriority"
import {$AbstractListListEntry$AbstractListCell, $AbstractListListEntry$AbstractListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractListListEntry$AbstractListCell"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$AbstractTextFieldListListEntry, $AbstractTextFieldListListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractTextFieldListListEntry"
import {$NarrationElementOutput, $NarrationElementOutput$Type} from "packages/net/minecraft/client/gui/narration/$NarrationElementOutput"

export class $AbstractTextFieldListListEntry$AbstractTextFieldListCell<T, SELF extends $AbstractTextFieldListListEntry$AbstractTextFieldListCell<(T), (SELF), (OUTER_SELF)>, OUTER_SELF extends $AbstractTextFieldListListEntry<(T), (SELF), (OUTER_SELF)>> extends $AbstractListListEntry$AbstractListCell<(T), (SELF), (OUTER_SELF)> {

constructor(value: T, listListEntry: OUTER_SELF)

public "render"(graphics: $GuiGraphics$Type, index: integer, y: integer, x: integer, entryWidth: integer, entryHeight: integer, mouseX: integer, mouseY: integer, isSelected: boolean, delta: float): void
public "children"(): $List<(any)>
public "updateNarration"(narrationElementOutput: $NarrationElementOutput$Type): void
public "narrationPriority"(): $NarratableEntry$NarrationPriority
public "updateSelected"(isSelected: boolean): void
public "getCellHeight"(): integer
get "cellHeight"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractTextFieldListListEntry$AbstractTextFieldListCell$Type<T, SELF, OUTER_SELF> = ($AbstractTextFieldListListEntry$AbstractTextFieldListCell<(T), (SELF), (OUTER_SELF)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractTextFieldListListEntry$AbstractTextFieldListCell_<T, SELF, OUTER_SELF> = $AbstractTextFieldListListEntry$AbstractTextFieldListCell$Type<(T), (SELF), (OUTER_SELF)>;
}}
declare module "packages/me/shedaniel/autoconfig/example/$ExampleConfig$PairOfInts" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ExampleConfig$PairOfInts {
 "foo": integer
 "bar": integer

constructor()
constructor(foo: integer, bar: integer)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExampleConfig$PairOfInts$Type = ($ExampleConfig$PairOfInts);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExampleConfig$PairOfInts_ = $ExampleConfig$PairOfInts$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg16$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg16$Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>, arg12: $RecordValueAnimatorArgs$Setter$Type<(A12)>, arg13: $RecordValueAnimatorArgs$Setter$Type<(A13)>, arg14: $RecordValueAnimatorArgs$Setter$Type<(A14)>, arg15: $RecordValueAnimatorArgs$Setter$Type<(A15)>, arg16: $RecordValueAnimatorArgs$Setter$Type<(A16)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>, arg12: $RecordValueAnimatorArgs$Setter$Type<(A12)>, arg13: $RecordValueAnimatorArgs$Setter$Type<(A13)>, arg14: $RecordValueAnimatorArgs$Setter$Type<(A14)>, arg15: $RecordValueAnimatorArgs$Setter$Type<(A15)>, arg16: $RecordValueAnimatorArgs$Setter$Type<(A16)>): void
}

export namespace $RecordValueAnimatorArgs$Arg16$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg16$Up$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, T> = ($RecordValueAnimatorArgs$Arg16$Up<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg16$Up_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, T> = $RecordValueAnimatorArgs$Arg16$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$DuplicateKeyException" {
import {$ConstructorException, $ConstructorException$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/constructor/$ConstructorException"

export class $DuplicateKeyException extends $ConstructorException {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DuplicateKeyException$Type = ($DuplicateKeyException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DuplicateKeyException_ = $DuplicateKeyException$Type;
}}
declare module "packages/me/shedaniel/autoconfig/util/$Utils" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Path, $Path$Type} from "packages/java/nio/file/$Path"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Collector, $Collector$Type} from "packages/java/util/stream/$Collector"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Utils {


public static "constructUnsafely"<V>(cls: $Class$Type<(V)>): V
public static "getUnsafely"<V>(field: $Field$Type, obj: any, defaultValue: V): V
public static "getUnsafely"<V>(field: $Field$Type, obj: any): V
public static "toLinkedMap"<T, K, U>(keyMapper: $Function$Type<(any), (any)>, valueMapper: $Function$Type<(any), (any)>): $Collector<(T), (any), ($Map<(K), (U)>)>
public static "setUnsafely"(field: $Field$Type, obj: any, newValue: any): void
public static "getConfigFolder"(): $Path
get "configFolder"(): $Path
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Utils$Type = ($Utils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Utils_ = $Utils$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$SubCategoryListEntry" {
import {$Expandable, $Expandable$Type} from "packages/me/shedaniel/clothconfig2/api/$Expandable"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$TooltipListEntry, $TooltipListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$TooltipListEntry"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"

export class $SubCategoryListEntry extends $TooltipListEntry<($List<($AbstractConfigListEntry)>)> implements $Expandable {

/**
 * 
 * @deprecated
 */
constructor(categoryName: $Component$Type, entries: $List$Type<($AbstractConfigListEntry$Type)>, defaultExpanded: boolean)

public "save"(): void
public "getDefaultValue"(): $Optional<($List<($AbstractConfigListEntry)>)>
public "tick"(): void
public "setRequiresRestart"(requiresRestart: boolean): void
public "setExpanded"(expanded: boolean): void
public "getError"(): $Optional<($Component)>
public "render"(graphics: $GuiGraphics$Type, index: integer, y: integer, x: integer, entryWidth: integer, entryHeight: integer, mouseX: integer, mouseY: integer, isHovered: boolean, delta: float): void
public "children"(): $List<(any)>
public "narratables"(): $List<(any)>
public "setFocused"(guiEventListener: $GuiEventListener$Type): void
public "isRequiresRestart"(): boolean
public "isEdited"(): boolean
public "getSearchTags"(): $Iterator<(string)>
public "updateSelected"(isSelected: boolean): void
public "getEntryArea"(x: integer, y: integer, entryWidth: integer, entryHeight: integer): $Rectangle
public "getMorePossibleHeight"(): integer
public "lateRender"(graphics: $GuiGraphics$Type, mouseX: integer, mouseY: integer, delta: float): void
public "filteredEntries"(): $List<($AbstractConfigListEntry)>
public "getCategoryName"(): $Component
public "getItemHeight"(): integer
public "isExpanded"(): boolean
public "getInitialReferenceOffset"(): integer
get "defaultValue"(): $Optional<($List<($AbstractConfigListEntry)>)>
set "requiresRestart"(value: boolean)
set "expanded"(value: boolean)
get "error"(): $Optional<($Component)>
set "focused"(value: $GuiEventListener$Type)
get "requiresRestart"(): boolean
get "edited"(): boolean
get "searchTags"(): $Iterator<(string)>
get "morePossibleHeight"(): integer
get "categoryName"(): $Component
get "itemHeight"(): integer
get "expanded"(): boolean
get "initialReferenceOffset"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SubCategoryListEntry$Type = ($SubCategoryListEntry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SubCategoryListEntry_ = $SubCategoryListEntry$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg16$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg16$Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, T> {

 "construct"(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11, arg11: A12, arg12: A13, arg13: A14, arg14: A15, arg15: A16): T

(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11, arg11: A12, arg12: A13, arg13: A14, arg14: A15, arg15: A16): T
}

export namespace $RecordValueAnimatorArgs$Arg16$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg16$Op$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, T> = ($RecordValueAnimatorArgs$Arg16$Op<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg16$Op_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, T> = $RecordValueAnimatorArgs$Arg16$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (A15), (A16), (T)>;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractListListEntry" {
import {$BaseListEntry, $BaseListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$BaseListEntry"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AbstractListListEntry$AbstractListCell, $AbstractListListEntry$AbstractListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$AbstractListListEntry$AbstractListCell"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"

export class $AbstractListListEntry<T, C extends $AbstractListListEntry$AbstractListCell<(T), (C), (SELF)>, SELF extends $AbstractListListEntry<(T), (C), (SELF)>> extends $BaseListEntry<(T), (C), (SELF)> {

constructor(fieldName: $Component$Type, value: $List$Type<(T)>, defaultExpanded: boolean, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, saveConsumer: $Consumer$Type<($List$Type<(T)>)>, defaultValue: $Supplier$Type<($List$Type<(T)>)>, resetButtonKey: $Component$Type, requiresRestart: boolean, deleteButtonEnabled: boolean, insertInFront: boolean, createNewCell: $BiFunction$Type<(T), (SELF), (C)>)

public "getValue"(): $List<(T)>
public "isEdited"(): boolean
public "getCellErrorSupplier"(): $Function<(T), ($Optional<($Component)>)>
public "setCellErrorSupplier"(cellErrorSupplier: $Function$Type<(T), ($Optional$Type<($Component$Type)>)>): void
get "value"(): $List<(T)>
get "edited"(): boolean
get "cellErrorSupplier"(): $Function<(T), ($Optional<($Component)>)>
set "cellErrorSupplier"(value: $Function$Type<(T), ($Optional$Type<($Component$Type)>)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractListListEntry$Type<T, C, SELF> = ($AbstractListListEntry<(T), (C), (SELF)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractListListEntry_<T, C, SELF> = $AbstractListListEntry$Type<(T), (C), (SELF)>;
}}
declare module "packages/me/shedaniel/autoconfig/$ConfigData" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ConfigData {

 "validatePostLoad"(): void
}

export namespace $ConfigData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigData$Type = ($ConfigData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigData_ = $ConfigData$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractRangeListBuilder" {
import {$AbstractListBuilder, $AbstractListBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractListBuilder"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"

export class $AbstractRangeListBuilder<T, A extends $AbstractConfigListEntry<(any)>, SELF extends $AbstractRangeListBuilder<(T), (A), (SELF)>> extends $AbstractListBuilder<(T), (A), (SELF)> {


public "setMin"(min: T): SELF
public "setMax"(max: T): SELF
public "removeMin"(): SELF
public "removeMax"(): SELF
set "min"(value: T)
set "max"(value: T)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractRangeListBuilder$Type<T, A, SELF> = ($AbstractRangeListBuilder<(T), (A), (SELF)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractRangeListBuilder_<T, A, SELF> = $AbstractRangeListBuilder$Type<(T), (A), (SELF)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$ArrayParserContext" {
import {$JsonArray, $JsonArray$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$JsonArray"
import {$ParserContext, $ParserContext$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/impl/$ParserContext"
import {$Jankson, $Jankson$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/$Jankson"

export class $ArrayParserContext implements $ParserContext<($JsonArray)> {

constructor()

public "consume"(codePoint: integer, loader: $Jankson$Type): boolean
public "eof"(): void
public "isComplete"(): boolean
get "complete"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ArrayParserContext$Type = ($ArrayParserContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ArrayParserContext_ = $ArrayParserContext$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$DoubleFieldBuilder" {
import {$DoubleListEntry, $DoubleListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DoubleListEntry"
import {$AbstractRangeFieldBuilder, $AbstractRangeFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractRangeFieldBuilder"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $DoubleFieldBuilder extends $AbstractRangeFieldBuilder<(double), ($DoubleListEntry), ($DoubleFieldBuilder)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, value: double)

public "setMin"(min: double): $DoubleFieldBuilder
public "setSaveConsumer"(saveConsumer: $Consumer$Type<(double)>): $DoubleFieldBuilder
public "setTooltipSupplier"(tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>): $DoubleFieldBuilder
public "setTooltipSupplier"(tooltipSupplier: $Function$Type<(double), ($Optional$Type<(($Component$Type)[])>)>): $DoubleFieldBuilder
public "setDefaultValue"(defaultValue: $Supplier$Type<(double)>): $DoubleFieldBuilder
public "setDefaultValue"(defaultValue: double): $DoubleFieldBuilder
public "setMax"(max: double): $DoubleFieldBuilder
public "removeMax"(): $DoubleFieldBuilder
set "min"(value: double)
set "saveConsumer"(value: $Consumer$Type<(double)>)
set "tooltipSupplier"(value: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>)
set "tooltipSupplier"(value: $Function$Type<(double), ($Optional$Type<(($Component$Type)[])>)>)
set "defaultValue"(value: $Supplier$Type<(double)>)
set "defaultValue"(value: double)
set "max"(value: double)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DoubleFieldBuilder$Type = ($DoubleFieldBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DoubleFieldBuilder_ = $DoubleFieldBuilder$Type;
}}
declare module "packages/me/shedaniel/autoconfig/annotation/$Config" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $Config extends $Annotation {

 "name"(): string
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $Config {
const probejs$$marker: never
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
declare module "packages/me/shedaniel/math/$Rectangle" {
import {$FloatingPoint, $FloatingPoint$Type} from "packages/me/shedaniel/math/$FloatingPoint"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Dimension, $Dimension$Type} from "packages/me/shedaniel/math/$Dimension"
import {$FloatingDimension, $FloatingDimension$Type} from "packages/me/shedaniel/math/$FloatingDimension"
import {$FloatingRectangle, $FloatingRectangle$Type} from "packages/me/shedaniel/math/$FloatingRectangle"
import {$Point, $Point$Type} from "packages/me/shedaniel/math/$Point"

export class $Rectangle implements $Cloneable {
 "x": integer
 "y": integer
 "width": integer
 "height": integer

constructor(arg0: $Dimension$Type)
constructor(arg0: $FloatingPoint$Type)
constructor(arg0: $Point$Type)
constructor(arg0: $FloatingPoint$Type, arg1: $FloatingDimension$Type)
constructor(arg0: $FloatingDimension$Type)
constructor(arg0: double, arg1: double, arg2: double, arg3: double)
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer)
constructor(arg0: integer, arg1: integer)
constructor(arg0: $FloatingRectangle$Type)
constructor(arg0: $Rectangle$Type)
constructor()
constructor(arg0: $FloatingPoint$Type, arg1: $Dimension$Type)
constructor(arg0: $Point$Type, arg1: $FloatingDimension$Type)
constructor(arg0: $Point$Type, arg1: $Dimension$Type)

public "getFloatingSize"(): $FloatingDimension
public "add"(arg0: $Point$Type): void
public "add"(arg0: $Rectangle$Type): void
public "add"(arg0: $FloatingPoint$Type): void
public "add"(arg0: integer, arg1: integer): void
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): $Rectangle
public "isEmpty"(): boolean
public "contains"(arg0: $Rectangle$Type): boolean
public "contains"(arg0: $FloatingPoint$Type): boolean
public "contains"(arg0: $Point$Type): boolean
public "contains"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): boolean
public "contains"(arg0: double, arg1: double): boolean
public "contains"(arg0: $FloatingRectangle$Type): boolean
public "contains"(arg0: integer, arg1: integer): boolean
public "getBounds"(): $Rectangle
public "getLocation"(): $Point
public "getSize"(): $Dimension
public "grow"(arg0: integer, arg1: integer): void
public "resize"(arg0: integer, arg1: integer): void
public "move"(arg0: integer, arg1: integer): void
public "setSize"(arg0: $FloatingDimension$Type): void
public "setSize"(arg0: $Dimension$Type): void
public "setSize"(arg0: double, arg1: double): void
public "setSize"(arg0: integer, arg1: integer): void
public "union"(arg0: $Rectangle$Type): $Rectangle
public "intersects"(arg0: $Rectangle$Type): boolean
public "getY"(): integer
public "getMaxX"(): integer
public "getMinX"(): integer
public "getMaxY"(): integer
public "getMinY"(): integer
public "translate"(arg0: integer, arg1: integer): void
public "getX"(): integer
public "getWidth"(): integer
public "getHeight"(): integer
public "setLocation"(arg0: $FloatingPoint$Type): void
public "setLocation"(arg0: double, arg1: double): void
public "setLocation"(arg0: integer, arg1: integer): void
public "setLocation"(arg0: $Point$Type): void
public "setBounds"(arg0: $Rectangle$Type): void
public "setBounds"(arg0: double, arg1: double, arg2: double, arg3: double): void
public "setBounds"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "setBounds"(arg0: $FloatingRectangle$Type): void
public "inside"(arg0: integer, arg1: integer): boolean
public "getCenterX"(): integer
public "getFloatingBounds"(): $FloatingRectangle
public "reshape"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "getCenterY"(): integer
public "intersection"(arg0: $Rectangle$Type): $Rectangle
public "getFloatingLocation"(): $FloatingPoint
get "floatingSize"(): $FloatingDimension
get "empty"(): boolean
get "bounds"(): $Rectangle
get "location"(): $Point
get "size"(): $Dimension
set "size"(value: $FloatingDimension$Type)
set "size"(value: $Dimension$Type)
get "y"(): integer
get "maxX"(): integer
get "minX"(): integer
get "maxY"(): integer
get "minY"(): integer
get "x"(): integer
get "width"(): integer
get "height"(): integer
set "location"(value: $FloatingPoint$Type)
set "location"(value: $Point$Type)
set "bounds"(value: $Rectangle$Type)
set "bounds"(value: $FloatingRectangle$Type)
get "centerX"(): integer
get "floatingBounds"(): $FloatingRectangle
get "centerY"(): integer
get "floatingLocation"(): $FloatingPoint
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Rectangle$Type = ($Rectangle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Rectangle_ = $Rectangle$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$SequenceNode" {
import {$CollectionNode, $CollectionNode$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$CollectionNode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$DumperOptions$FlowStyle, $DumperOptions$FlowStyle$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$FlowStyle"
import {$Node, $Node$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Node"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$Tag, $Tag$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$Tag"
import {$NodeId, $NodeId$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/nodes/$NodeId"

export class $SequenceNode extends $CollectionNode<($Node)> {

/**
 * 
 * @deprecated
 */
constructor(tag: $Tag$Type, resolved: boolean, value: $List$Type<($Node$Type)>, startMark: $Mark$Type, endMark: $Mark$Type, style: boolean)
/**
 * 
 * @deprecated
 */
constructor(tag: $Tag$Type, value: $List$Type<($Node$Type)>, style: boolean)
constructor(tag: $Tag$Type, value: $List$Type<($Node$Type)>, flowStyle: $DumperOptions$FlowStyle$Type)
constructor(tag: $Tag$Type, resolved: boolean, value: $List$Type<($Node$Type)>, startMark: $Mark$Type, endMark: $Mark$Type, flowStyle: $DumperOptions$FlowStyle$Type)

public "toString"(): string
public "getValue"(): $List<($Node)>
public "setListType"(listType: $Class$Type<(any)>): void
public "getNodeId"(): $NodeId
get "value"(): $List<($Node)>
set "listType"(value: $Class$Type<(any)>)
get "nodeId"(): $NodeId
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SequenceNode$Type = ($SequenceNode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SequenceNode_ = $SequenceNode$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$CommentToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $CommentToken extends $Token {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommentToken$Type = ($CommentToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommentToken_ = $CommentToken$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$ScalarEvent" {
import {$Event$ID, $Event$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$Event$ID"
import {$DumperOptions$ScalarStyle, $DumperOptions$ScalarStyle$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/$DumperOptions$ScalarStyle"
import {$NodeEvent, $NodeEvent$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$NodeEvent"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"
import {$ImplicitTuple, $ImplicitTuple$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/events/$ImplicitTuple"

export class $ScalarEvent extends $NodeEvent {

constructor(anchor: string, tag: string, implicit: $ImplicitTuple$Type, value: string, startMark: $Mark$Type, endMark: $Mark$Type, style: $DumperOptions$ScalarStyle$Type)
/**
 * 
 * @deprecated
 */
constructor(anchor: string, tag: string, implicit: $ImplicitTuple$Type, value: string, startMark: $Mark$Type, endMark: $Mark$Type, style: character)

public "getValue"(): string
public "getTag"(): string
public "getImplicit"(): $ImplicitTuple
public "getEventId"(): $Event$ID
public "getScalarStyle"(): $DumperOptions$ScalarStyle
/**
 * 
 * @deprecated
 */
public "getStyle"(): character
public "isPlain"(): boolean
get "value"(): string
get "tag"(): string
get "implicit"(): $ImplicitTuple
get "eventId"(): $Event$ID
get "scalarStyle"(): $DumperOptions$ScalarStyle
get "style"(): character
get "plain"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScalarEvent$Type = ($ScalarEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScalarEvent_ = $ScalarEvent$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/scanner/$Scanner" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"

export interface $Scanner {

 "getToken"(): $Token
 "checkToken"(...arg0: ($Token$ID$Type)[]): boolean
 "peekToken"(): $Token
}

export namespace $Scanner {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Scanner$Type = ($Scanner);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Scanner_ = $Scanner$Type;
}}
declare module "packages/me/shedaniel/math/api/$Executor" {
import {$Callable, $Callable$Type} from "packages/java/util/concurrent/$Callable"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$Dist, $Dist$Type} from "packages/net/minecraftforge/api/distmarker/$Dist"

export class $Executor {


public static "run"(runnableSupplier: $Supplier$Type<($Runnable$Type)>): void
public static "call"<T>(runnableSupplier: $Supplier$Type<($Callable$Type<(T)>)>): T
public static "callIf"<T>(predicate: $Supplier$Type<(boolean)>, runnableSupplier: $Supplier$Type<($Callable$Type<(T)>)>): $Optional<(T)>
public static "callIfEnv"<T>(env: $Dist$Type, runnableSupplier: $Supplier$Type<($Callable$Type<(T)>)>): $Optional<(T)>
public static "runIfEnv"(env: $Dist$Type, runnableSupplier: $Supplier$Type<($Runnable$Type)>): void
public static "runIf"(predicate: $Supplier$Type<(boolean)>, runnableSupplier: $Supplier$Type<($Runnable$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Executor$Type = ($Executor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Executor_ = $Executor$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$ColorFieldBuilder" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Color, $Color$Type} from "packages/me/shedaniel/math/$Color"
import {$AbstractFieldBuilder, $AbstractFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractFieldBuilder"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ColorEntry, $ColorEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$ColorEntry"
import {$TextColor, $TextColor$Type} from "packages/net/minecraft/network/chat/$TextColor"

export class $ColorFieldBuilder extends $AbstractFieldBuilder<(integer), ($ColorEntry), ($ColorFieldBuilder)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, value: integer)

public "build"(): $ColorEntry
public "setAlphaMode"(withAlpha: boolean): $ColorFieldBuilder
public "setSaveConsumer"(saveConsumer: $Consumer$Type<(integer)>): $ColorFieldBuilder
public "setTooltipSupplier"(tooltipSupplier: $Function$Type<(integer), ($Optional$Type<(($Component$Type)[])>)>): $ColorFieldBuilder
public "setErrorSupplier"(errorSupplier: $Function$Type<(integer), ($Optional$Type<($Component$Type)>)>): $ColorFieldBuilder
public "setDefaultValue"(defaultValue: $Supplier$Type<(integer)>): $ColorFieldBuilder
public "setDefaultValue"(defaultValue: $TextColor$Type): $ColorFieldBuilder
public "setDefaultValue"(defaultValue: integer): $ColorFieldBuilder
public "setSaveConsumer2"(saveConsumer: $Consumer$Type<($Color$Type)>): $ColorFieldBuilder
public "setSaveConsumer3"(saveConsumer: $Consumer$Type<($TextColor$Type)>): $ColorFieldBuilder
public "setDefaultValue2"(defaultValue: $Supplier$Type<($Color$Type)>): $ColorFieldBuilder
public "setDefaultValue3"(defaultValue: $Supplier$Type<($TextColor$Type)>): $ColorFieldBuilder
set "alphaMode"(value: boolean)
set "saveConsumer"(value: $Consumer$Type<(integer)>)
set "tooltipSupplier"(value: $Function$Type<(integer), ($Optional$Type<(($Component$Type)[])>)>)
set "errorSupplier"(value: $Function$Type<(integer), ($Optional$Type<($Component$Type)>)>)
set "defaultValue"(value: $Supplier$Type<(integer)>)
set "defaultValue"(value: $TextColor$Type)
set "defaultValue"(value: integer)
set "saveConsumer2"(value: $Consumer$Type<($Color$Type)>)
set "saveConsumer3"(value: $Consumer$Type<($TextColor$Type)>)
set "defaultValue2"(value: $Supplier$Type<($Color$Type)>)
set "defaultValue3"(value: $Supplier$Type<($TextColor$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorFieldBuilder$Type = ($ColorFieldBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorFieldBuilder_ = $ColorFieldBuilder$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg14$Up" {
import {$RecordValueAnimatorArgs$Setter, $RecordValueAnimatorArgs$Setter$Type} from "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Setter"

export interface $RecordValueAnimatorArgs$Arg14$Up<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, T> {

 "update"(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>, arg12: $RecordValueAnimatorArgs$Setter$Type<(A12)>, arg13: $RecordValueAnimatorArgs$Setter$Type<(A13)>, arg14: $RecordValueAnimatorArgs$Setter$Type<(A14)>): void

(arg0: T, arg1: $RecordValueAnimatorArgs$Setter$Type<(A1)>, arg2: $RecordValueAnimatorArgs$Setter$Type<(A2)>, arg3: $RecordValueAnimatorArgs$Setter$Type<(A3)>, arg4: $RecordValueAnimatorArgs$Setter$Type<(A4)>, arg5: $RecordValueAnimatorArgs$Setter$Type<(A5)>, arg6: $RecordValueAnimatorArgs$Setter$Type<(A6)>, arg7: $RecordValueAnimatorArgs$Setter$Type<(A7)>, arg8: $RecordValueAnimatorArgs$Setter$Type<(A8)>, arg9: $RecordValueAnimatorArgs$Setter$Type<(A9)>, arg10: $RecordValueAnimatorArgs$Setter$Type<(A10)>, arg11: $RecordValueAnimatorArgs$Setter$Type<(A11)>, arg12: $RecordValueAnimatorArgs$Setter$Type<(A12)>, arg13: $RecordValueAnimatorArgs$Setter$Type<(A13)>, arg14: $RecordValueAnimatorArgs$Setter$Type<(A14)>): void
}

export namespace $RecordValueAnimatorArgs$Arg14$Up {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg14$Up$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, T> = ($RecordValueAnimatorArgs$Arg14$Up<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg14$Up_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, T> = $RecordValueAnimatorArgs$Arg14$Up$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/serializer/$SerializerException" {
import {$YAMLException, $YAMLException$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$YAMLException"

export class $SerializerException extends $YAMLException {

constructor(message: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SerializerException$Type = ($SerializerException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SerializerException_ = $SerializerException$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/blue/endless/jankson/api/$SyntaxError" {
import {$Exception, $Exception$Type} from "packages/java/lang/$Exception"

export class $SyntaxError extends $Exception {

constructor(message: string)

public "setEndParsing"(line: integer, column: integer): void
public "setStartParsing"(line: integer, column: integer): void
public "getCompleteMessage"(): string
public "getLineMessage"(): string
get "completeMessage"(): string
get "lineMessage"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SyntaxError$Type = ($SyntaxError);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SyntaxError_ = $SyntaxError$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$ConfigEntryBuilder" {
import {$IntFieldBuilder, $IntFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$IntFieldBuilder"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IntSliderBuilder, $IntSliderBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$IntSliderBuilder"
import {$LongFieldBuilder, $LongFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$LongFieldBuilder"
import {$StringListBuilder, $StringListBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$StringListBuilder"
import {$StringFieldBuilder, $StringFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$StringFieldBuilder"
import {$DoubleListBuilder, $DoubleListBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$DoubleListBuilder"
import {$LongListBuilder, $LongListBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$LongListBuilder"
import {$DropdownBoxEntry$SelectionCellCreator, $DropdownBoxEntry$SelectionCellCreator$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$SelectionCellCreator"
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$SelectorBuilder, $SelectorBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$SelectorBuilder"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BooleanToggleBuilder, $BooleanToggleBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$BooleanToggleBuilder"
import {$DoubleFieldBuilder, $DoubleFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$DoubleFieldBuilder"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$TextFieldBuilder, $TextFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$TextFieldBuilder"
import {$FloatFieldBuilder, $FloatFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$FloatFieldBuilder"
import {$TextDescriptionBuilder, $TextDescriptionBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$TextDescriptionBuilder"
import {$Color, $Color$Type} from "packages/me/shedaniel/math/$Color"
import {$ModifierKeyCode, $ModifierKeyCode$Type} from "packages/me/shedaniel/clothconfig2/api/$ModifierKeyCode"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$LongSliderBuilder, $LongSliderBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$LongSliderBuilder"
import {$ColorFieldBuilder, $ColorFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$ColorFieldBuilder"
import {$DropdownBoxEntry$SelectionTopCellElement, $DropdownBoxEntry$SelectionTopCellElement$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$DropdownBoxEntry$SelectionTopCellElement"
import {$IntListBuilder, $IntListBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$IntListBuilder"
import {$EnumSelectorBuilder, $EnumSelectorBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$EnumSelectorBuilder"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$KeyCodeBuilder, $KeyCodeBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$KeyCodeBuilder"
import {$FloatListBuilder, $FloatListBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$FloatListBuilder"
import {$SubCategoryBuilder, $SubCategoryBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$SubCategoryBuilder"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$DropdownMenuBuilder, $DropdownMenuBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$DropdownMenuBuilder"
import {$TextColor, $TextColor$Type} from "packages/net/minecraft/network/chat/$TextColor"

export interface $ConfigEntryBuilder {

 "getResetButtonKey"(): $Component
 "startIntSlider"(arg0: $Component$Type, arg1: integer, arg2: integer, arg3: integer): $IntSliderBuilder
 "startBooleanToggle"(arg0: $Component$Type, arg1: boolean): $BooleanToggleBuilder
 "startIntField"(arg0: $Component$Type, arg1: integer): $IntFieldBuilder
 "startDoubleField"(arg0: $Component$Type, arg1: double): $DoubleFieldBuilder
 "startFloatField"(arg0: $Component$Type, arg1: float): $FloatFieldBuilder
 "startLongField"(arg0: $Component$Type, arg1: long): $LongFieldBuilder
 "startEnumSelector"<T extends $Enum<(any)>>(arg0: $Component$Type, arg1: $Class$Type<(T)>, arg2: T): $EnumSelectorBuilder<(T)>
 "startTextField"(arg0: $Component$Type, arg1: string): $TextFieldBuilder
 "startLongSlider"(arg0: $Component$Type, arg1: long, arg2: long, arg3: long): $LongSliderBuilder
 "startStrList"(arg0: $Component$Type, arg1: $List$Type<(string)>): $StringListBuilder
 "startSubCategory"(arg0: $Component$Type): $SubCategoryBuilder
 "startSubCategory"(arg0: $Component$Type, arg1: $List$Type<($AbstractConfigListEntry$Type)>): $SubCategoryBuilder
 "startAlphaColorField"(fieldNameKey: $Component$Type, value: integer): $ColorFieldBuilder
 "startAlphaColorField"(fieldNameKey: $Component$Type, color: $Color$Type): $ColorFieldBuilder
 "startTextDescription"(arg0: $Component$Type): $TextDescriptionBuilder
 "fillKeybindingField"(fieldNameKey: $Component$Type, value: $KeyMapping$Type): $KeyCodeBuilder
 "startModifierKeyCodeField"(arg0: $Component$Type, arg1: $ModifierKeyCode$Type): $KeyCodeBuilder
 "startStringDropdownMenu"(fieldNameKey: $Component$Type, value: string): $DropdownMenuBuilder<(string)>
 "startStringDropdownMenu"(fieldNameKey: $Component$Type, value: string, toTextFunction: $Function$Type<(string), ($Component$Type)>): $DropdownMenuBuilder<(string)>
 "startStringDropdownMenu"(fieldNameKey: $Component$Type, value: string, cellCreator: $DropdownBoxEntry$SelectionCellCreator$Type<(string)>): $DropdownMenuBuilder<(string)>
 "startStringDropdownMenu"(fieldNameKey: $Component$Type, value: string, toTextFunction: $Function$Type<(string), ($Component$Type)>, cellCreator: $DropdownBoxEntry$SelectionCellCreator$Type<(string)>): $DropdownMenuBuilder<(string)>
 "startSelector"<T>(arg0: $Component$Type, arg1: (T)[], arg2: T): $SelectorBuilder<(T)>
 "startLongList"(arg0: $Component$Type, arg1: $List$Type<(long)>): $LongListBuilder
 "setResetButtonKey"(arg0: $Component$Type): $ConfigEntryBuilder
 "startFloatList"(arg0: $Component$Type, arg1: $List$Type<(float)>): $FloatListBuilder
 "startDropdownMenu"<T>(fieldNameKey: $Component$Type, value: T, toObjectFunction: $Function$Type<(string), (T)>, toTextFunction: $Function$Type<(T), ($Component$Type)>): $DropdownMenuBuilder<(T)>
 "startDropdownMenu"<T>(fieldNameKey: $Component$Type, value: T, toObjectFunction: $Function$Type<(string), (T)>): $DropdownMenuBuilder<(T)>
 "startDropdownMenu"<T>(fieldNameKey: $Component$Type, value: T, toObjectFunction: $Function$Type<(string), (T)>, cellCreator: $DropdownBoxEntry$SelectionCellCreator$Type<(T)>): $DropdownMenuBuilder<(T)>
 "startDropdownMenu"<T>(arg0: $Component$Type, arg1: $DropdownBoxEntry$SelectionTopCellElement$Type<(T)>, arg2: $DropdownBoxEntry$SelectionCellCreator$Type<(T)>): $DropdownMenuBuilder<(T)>
 "startDropdownMenu"<T>(fieldNameKey: $Component$Type, topCellElement: $DropdownBoxEntry$SelectionTopCellElement$Type<(T)>): $DropdownMenuBuilder<(T)>
 "startDropdownMenu"<T>(fieldNameKey: $Component$Type, value: T, toObjectFunction: $Function$Type<(string), (T)>, toTextFunction: $Function$Type<(T), ($Component$Type)>, cellCreator: $DropdownBoxEntry$SelectionCellCreator$Type<(T)>): $DropdownMenuBuilder<(T)>
 "startIntList"(arg0: $Component$Type, arg1: $List$Type<(integer)>): $IntListBuilder
 "startDoubleList"(arg0: $Component$Type, arg1: $List$Type<(double)>): $DoubleListBuilder
 "startKeyCodeField"(fieldNameKey: $Component$Type, value: $InputConstants$Key$Type): $KeyCodeBuilder
 "startColorField"(arg0: $Component$Type, arg1: integer): $ColorFieldBuilder
 "startColorField"(fieldNameKey: $Component$Type, color: $Color$Type): $ColorFieldBuilder
 "startColorField"(fieldNameKey: $Component$Type, color: $TextColor$Type): $ColorFieldBuilder
 "startStrField"(arg0: $Component$Type, arg1: string): $StringFieldBuilder
}

export namespace $ConfigEntryBuilder {
function create(): $ConfigEntryBuilder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigEntryBuilder$Type = ($ConfigEntryBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigEntryBuilder_ = $ConfigEntryBuilder$Type;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$StreamStartToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $StreamStartToken extends $Token {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StreamStartToken$Type = ($StreamStartToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StreamStartToken_ = $StreamStartToken$Type;
}}
declare module "packages/me/shedaniel/autoconfig/event/$ConfigSerializeEvent$Save" {
import {$ConfigHolder, $ConfigHolder$Type} from "packages/me/shedaniel/autoconfig/$ConfigHolder"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$ConfigData, $ConfigData$Type} from "packages/me/shedaniel/autoconfig/$ConfigData"

export interface $ConfigSerializeEvent$Save<T extends $ConfigData> {

 "onSave"(arg0: $ConfigHolder$Type<(T)>, arg1: T): $InteractionResult

(arg0: $ConfigHolder$Type<(T)>, arg1: T): $InteractionResult
}

export namespace $ConfigSerializeEvent$Save {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigSerializeEvent$Save$Type<T> = ($ConfigSerializeEvent$Save<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigSerializeEvent$Save_<T> = $ConfigSerializeEvent$Save$Type<(T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$BlockSequenceStartToken" {
import {$Token$ID, $Token$ID$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token$ID"
import {$Token, $Token$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/tokens/$Token"
import {$Mark, $Mark$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark"

export class $BlockSequenceStartToken extends $Token {

constructor(startMark: $Mark$Type, endMark: $Mark$Type)

public "getTokenId"(): $Token$ID
get "tokenId"(): $Token$ID
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockSequenceStartToken$Type = ($BlockSequenceStartToken);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockSequenceStartToken_ = $BlockSequenceStartToken$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/entries/$BaseListEntry" {
import {$Expandable, $Expandable$Type} from "packages/me/shedaniel/clothconfig2/api/$Expandable"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$TooltipListEntry, $TooltipListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$TooltipListEntry"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BaseListCell, $BaseListCell$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$BaseListCell"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"

export class $BaseListEntry<T, C extends $BaseListCell, SELF extends $BaseListEntry<(T), (C), (SELF)>> extends $TooltipListEntry<($List<(T)>)> implements $Expandable {

constructor(fieldName: $Component$Type, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, defaultValue: $Supplier$Type<($List$Type<(T)>)>, createNewInstance: $Function$Type<(SELF), (C)>, saveConsumer: $Consumer$Type<($List$Type<(T)>)>, resetButtonKey: $Component$Type, requiresRestart: boolean, deleteButtonEnabled: boolean, insertInFront: boolean)
constructor(fieldName: $Component$Type, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, defaultValue: $Supplier$Type<($List$Type<(T)>)>, createNewInstance: $Function$Type<(SELF), (C)>, saveConsumer: $Consumer$Type<($List$Type<(T)>)>, resetButtonKey: $Component$Type, requiresRestart: boolean)
constructor(fieldName: $Component$Type, tooltipSupplier: $Supplier$Type<($Optional$Type<(($Component$Type)[])>)>, defaultValue: $Supplier$Type<($List$Type<(T)>)>, createNewInstance: $Function$Type<(SELF), (C)>, saveConsumer: $Consumer$Type<($List$Type<(T)>)>, resetButtonKey: $Component$Type)

public "save"(): void
public "getDefaultValue"(): $Optional<($List<(T)>)>
public "self"(): SELF
public "setRequiresRestart"(requiresRestart: boolean): void
public "setExpanded"(expanded: boolean): void
public "getError"(): $Optional<($Component)>
public "render"(graphics: $GuiGraphics$Type, index: integer, y: integer, x: integer, entryWidth: integer, entryHeight: integer, mouseX: integer, mouseY: integer, isHovered: boolean, delta: float): void
public "children"(): $List<(any)>
public "narratables"(): $List<(any)>
public "isRequiresRestart"(): boolean
public "isEdited"(): boolean
public "updateSelected"(isSelected: boolean): void
public "getEntryArea"(x: integer, y: integer, entryWidth: integer, entryHeight: integer): $Rectangle
public "isInsertButtonEnabled"(): boolean
public "setDeleteButtonEnabled"(deleteButtonEnabled: boolean): void
public "getCreateNewInstance"(): $Function<(SELF), (C)>
public "setCreateNewInstance"(createNewInstance: $Function$Type<(SELF), (C)>): void
public "isDeleteButtonEnabled"(): boolean
public "setInsertButtonEnabled"(insertButtonEnabled: boolean): void
public "getTooltip"(mouseX: integer, mouseY: integer): $Optional<(($Component)[])>
public "getItemHeight"(): integer
public "isMatchDefault"(): boolean
public "isExpanded"(): boolean
public "insertInFront"(): boolean
public "setAddTooltip"(addTooltip: $Component$Type): void
public "setRemoveTooltip"(removeTooltip: $Component$Type): void
public "getRemoveTooltip"(): $Component
public "getAddTooltip"(): $Component
public "getInitialReferenceOffset"(): integer
get "defaultValue"(): $Optional<($List<(T)>)>
set "requiresRestart"(value: boolean)
set "expanded"(value: boolean)
get "error"(): $Optional<($Component)>
get "requiresRestart"(): boolean
get "edited"(): boolean
get "insertButtonEnabled"(): boolean
set "deleteButtonEnabled"(value: boolean)
get "createNewInstance"(): $Function<(SELF), (C)>
set "createNewInstance"(value: $Function$Type<(SELF), (C)>)
get "deleteButtonEnabled"(): boolean
set "insertButtonEnabled"(value: boolean)
get "itemHeight"(): integer
get "matchDefault"(): boolean
get "expanded"(): boolean
set "addTooltip"(value: $Component$Type)
set "removeTooltip"(value: $Component$Type)
get "removeTooltip"(): $Component
get "addTooltip"(): $Component
get "initialReferenceOffset"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BaseListEntry$Type<T, C, SELF> = ($BaseListEntry<(T), (C), (SELF)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BaseListEntry_<T, C, SELF> = $BaseListEntry$Type<(T), (C), (SELF)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/error/$Mark" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

export class $Mark implements $Serializable {

constructor(name: string, index: integer, line: integer, column: integer, buffer: (integer)[], pointer: integer)
/**
 * 
 * @deprecated
 */
constructor(name: string, index: integer, line: integer, column: integer, buffer: string, pointer: integer)
constructor(name: string, index: integer, line: integer, column: integer, str: (character)[], pointer: integer)

public "getName"(): string
public "toString"(): string
public "getIndex"(): integer
public "getBuffer"(): (integer)[]
public "get_snippet"(indent: integer, max_length: integer): string
public "get_snippet"(): string
public "getLine"(): integer
public "getPointer"(): integer
public "getColumn"(): integer
get "name"(): string
get "index"(): integer
get "buffer"(): (integer)[]
get "_snippet"(): string
get "line"(): integer
get "pointer"(): integer
get "column"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Mark$Type = ($Mark);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Mark_ = $Mark$Type;
}}
declare module "packages/me/shedaniel/autoconfig/gui/registry/api/$GuiRegistryAccess" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiProvider, $GuiProvider$Type} from "packages/me/shedaniel/autoconfig/gui/registry/api/$GuiProvider"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$GuiTransformer, $GuiTransformer$Type} from "packages/me/shedaniel/autoconfig/gui/registry/api/$GuiTransformer"

export interface $GuiRegistryAccess extends $GuiProvider, $GuiTransformer {

 "getAndTransform"(i18n: string, field: $Field$Type, config: any, defaults: any, registry: $GuiRegistryAccess$Type): $List<($AbstractConfigListEntry)>
 "get"(arg0: string, arg1: $Field$Type, arg2: any, arg3: any, arg4: $GuiRegistryAccess$Type): $List<($AbstractConfigListEntry)>
 "transform"(arg0: $List$Type<($AbstractConfigListEntry$Type)>, arg1: string, arg2: $Field$Type, arg3: any, arg4: any, arg5: $GuiRegistryAccess$Type): $List<($AbstractConfigListEntry)>
}

export namespace $GuiRegistryAccess {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiRegistryAccess$Type = ($GuiRegistryAccess);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiRegistryAccess_ = $GuiRegistryAccess$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/gui/widget/$DynamicElementListWidget$ElementEntry" {
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$ComponentPath, $ComponentPath$Type} from "packages/net/minecraft/client/gui/$ComponentPath"
import {$FocusNavigationEvent, $FocusNavigationEvent$Type} from "packages/net/minecraft/client/gui/navigation/$FocusNavigationEvent"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$DynamicEntryListWidget$Entry, $DynamicEntryListWidget$Entry$Type} from "packages/me/shedaniel/clothconfig2/gui/widget/$DynamicEntryListWidget$Entry"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$NarratableEntry$NarrationPriority, $NarratableEntry$NarrationPriority$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry$NarrationPriority"
import {$ContainerEventHandler, $ContainerEventHandler$Type} from "packages/net/minecraft/client/gui/components/events/$ContainerEventHandler"
import {$NarrationElementOutput, $NarrationElementOutput$Type} from "packages/net/minecraft/client/gui/narration/$NarrationElementOutput"

export class $DynamicElementListWidget$ElementEntry<E extends $DynamicElementListWidget$ElementEntry<(E)>> extends $DynamicEntryListWidget$Entry<(E)> implements $ContainerEventHandler, $NarratableEntry {

constructor()

public "keyPressed"(i: integer, j: integer, k: integer): boolean
public "nextFocusPath"(focusNavigationEvent: $FocusNavigationEvent$Type): $ComponentPath
public "isActive"(): boolean
public "updateNarration"(narrationElementOutput: $NarrationElementOutput$Type): void
public "narrationPriority"(): $NarratableEntry$NarrationPriority
public "narratables"(): $List<(any)>
public "setDragging"(bl: boolean): void
public "setFocused"(guiEventListener: $GuiEventListener$Type): void
public "isDragging"(): boolean
public "getFocused"(): $GuiEventListener
public "mouseReleased"(d: double, e: double, i: integer): boolean
public "mouseClicked"(d: double, e: double, i: integer): boolean
public "charTyped"(c: character, i: integer): boolean
public "mouseScrolled"(d: double, e: double, f: double): boolean
public "mouseDragged"(d: double, e: double, i: integer, f: double, g: double): boolean
public "keyReleased"(i: integer, j: integer, k: integer): boolean
public "focusPathAtIndex"(focusNavigationEvent: $FocusNavigationEvent$Type, i: integer): $ComponentPath
public "getCurrentFocusPath"(): $ComponentPath
public "children"(): $List<(any)>
public "setFocused"(arg0: boolean): void
public "getChildAt"(arg0: double, arg1: double): $Optional<($GuiEventListener)>
public "isFocused"(): boolean
public "magicalSpecialHackyFocus"(arg0: $GuiEventListener$Type): void
get "active"(): boolean
set "dragging"(value: boolean)
set "focused"(value: $GuiEventListener$Type)
get "dragging"(): boolean
get "focused"(): $GuiEventListener
get "currentFocusPath"(): $ComponentPath
set "focused"(value: boolean)
get "focused"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DynamicElementListWidget$ElementEntry$Type<E> = ($DynamicElementListWidget$ElementEntry<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DynamicElementListWidget$ElementEntry_<E> = $DynamicElementListWidget$ElementEntry$Type<(E)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$FieldProperty" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$GenericProperty, $GenericProperty$Type} from "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/introspector/$GenericProperty"

export class $FieldProperty extends $GenericProperty {

constructor(field: $Field$Type)

public "get"(object: any): any
public "getAnnotation"<A extends $Annotation>(annotationType: $Class$Type<(A)>): A
public "getAnnotations"(): $List<($Annotation)>
public "set"(object: any, value: any): void
get "annotations"(): $List<($Annotation)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FieldProperty$Type = ($FieldProperty);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FieldProperty_ = $FieldProperty$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/$ScissorsScreen" {
import {$Rectangle, $Rectangle$Type} from "packages/me/shedaniel/math/$Rectangle"

export interface $ScissorsScreen {

 "handleScissor"(arg0: $Rectangle$Type): $Rectangle

(arg0: $Rectangle$Type): $Rectangle
}

export namespace $ScissorsScreen {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScissorsScreen$Type = ($ScissorsScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScissorsScreen_ = $ScissorsScreen$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/impl/builders/$StringFieldBuilder" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$StringListEntry, $StringListEntry$Type} from "packages/me/shedaniel/clothconfig2/gui/entries/$StringListEntry"
import {$AbstractFieldBuilder, $AbstractFieldBuilder$Type} from "packages/me/shedaniel/clothconfig2/impl/builders/$AbstractFieldBuilder"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $StringFieldBuilder extends $AbstractFieldBuilder<(string), ($StringListEntry), ($StringFieldBuilder)> {

constructor(resetButtonKey: $Component$Type, fieldNameKey: $Component$Type, value: string)

public "build"(): $StringListEntry
public "setSaveConsumer"(saveConsumer: $Consumer$Type<(string)>): $StringFieldBuilder
public "requireRestart"(): $StringFieldBuilder
public "setTooltip"(tooltip: $Optional$Type<(($Component$Type)[])>): $StringFieldBuilder
public "setErrorSupplier"(errorSupplier: $Function$Type<(string), ($Optional$Type<($Component$Type)>)>): $StringFieldBuilder
public "setDefaultValue"(defaultValue: string): $StringFieldBuilder
public "setDefaultValue"(defaultValue: $Supplier$Type<(string)>): $StringFieldBuilder
set "saveConsumer"(value: $Consumer$Type<(string)>)
set "tooltip"(value: $Optional$Type<(($Component$Type)[])>)
set "errorSupplier"(value: $Function$Type<(string), ($Optional$Type<($Component$Type)>)>)
set "defaultValue"(value: string)
set "defaultValue"(value: $Supplier$Type<(string)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StringFieldBuilder$Type = ($StringFieldBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StringFieldBuilder_ = $StringFieldBuilder$Type;
}}
declare module "packages/me/shedaniel/autoconfig/gui/registry/$GuiRegistry" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GuiProvider, $GuiProvider$Type} from "packages/me/shedaniel/autoconfig/gui/registry/api/$GuiProvider"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$AbstractConfigListEntry, $AbstractConfigListEntry$Type} from "packages/me/shedaniel/clothconfig2/api/$AbstractConfigListEntry"
import {$GuiRegistryAccess, $GuiRegistryAccess$Type} from "packages/me/shedaniel/autoconfig/gui/registry/api/$GuiRegistryAccess"
import {$GuiTransformer, $GuiTransformer$Type} from "packages/me/shedaniel/autoconfig/gui/registry/api/$GuiTransformer"

export class $GuiRegistry implements $GuiRegistryAccess {

constructor()

public "get"(i18n: string, field: $Field$Type, config: any, defaults: any, registry: $GuiRegistryAccess$Type): $List<($AbstractConfigListEntry)>
public "transform"(guis: $List$Type<($AbstractConfigListEntry$Type)>, i18n: string, field: $Field$Type, config: any, defaults: any, registry: $GuiRegistryAccess$Type): $List<($AbstractConfigListEntry)>
public "registerTypeProvider"(provider: $GuiProvider$Type, ...types: ($Class$Type<(any)>)[]): void
public "registerAnnotationProvider"(provider: $GuiProvider$Type, ...types: ($Class$Type<(any)>)[]): void
public "registerAnnotationProvider"(provider: $GuiProvider$Type, predicate: $Predicate$Type<($Field$Type)>, ...types: ($Class$Type<(any)>)[]): void
public "registerPredicateProvider"(provider: $GuiProvider$Type, predicate: $Predicate$Type<($Field$Type)>): void
public "registerAnnotationTransformer"(transformer: $GuiTransformer$Type, predicate: $Predicate$Type<($Field$Type)>, ...types: ($Class$Type<(any)>)[]): void
public "registerAnnotationTransformer"(transformer: $GuiTransformer$Type, ...types: ($Class$Type<(any)>)[]): void
public "registerPredicateTransformer"(transformer: $GuiTransformer$Type, predicate: $Predicate$Type<($Field$Type)>): void
public "getAndTransform"(i18n: string, field: $Field$Type, config: any, defaults: any, registry: $GuiRegistryAccess$Type): $List<($AbstractConfigListEntry)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiRegistry$Type = ($GuiRegistry);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiRegistry_ = $GuiRegistry$Type;
}}
declare module "packages/me/shedaniel/clothconfig2/api/animator/$RecordValueAnimatorArgs$Arg14$Op" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $RecordValueAnimatorArgs$Arg14$Op<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, T> {

 "construct"(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11, arg11: A12, arg12: A13, arg13: A14): T

(arg0: A1, arg1: A2, arg2: A3, arg3: A4, arg4: A5, arg5: A6, arg6: A7, arg7: A8, arg8: A9, arg9: A10, arg10: A11, arg11: A12, arg12: A13, arg13: A14): T
}

export namespace $RecordValueAnimatorArgs$Arg14$Op {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecordValueAnimatorArgs$Arg14$Op$Type<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, T> = ($RecordValueAnimatorArgs$Arg14$Op<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecordValueAnimatorArgs$Arg14$Op_<A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, T> = $RecordValueAnimatorArgs$Arg14$Op$Type<(A1), (A2), (A3), (A4), (A5), (A6), (A7), (A8), (A9), (A10), (A11), (A12), (A13), (A14), (T)>;
}}
declare module "packages/me/shedaniel/cloth/clothconfig/shadowed/org/yaml/snakeyaml/emitter/$ScalarAnalysis" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ScalarAnalysis {

constructor(scalar: string, empty: boolean, multiline: boolean, allowFlowPlain: boolean, allowBlockPlain: boolean, allowSingleQuoted: boolean, allowBlock: boolean)

public "isEmpty"(): boolean
public "getScalar"(): string
public "isMultiline"(): boolean
public "isAllowFlowPlain"(): boolean
public "isAllowBlockPlain"(): boolean
public "isAllowBlock"(): boolean
public "isAllowSingleQuoted"(): boolean
get "empty"(): boolean
get "scalar"(): string
get "multiline"(): boolean
get "allowFlowPlain"(): boolean
get "allowBlockPlain"(): boolean
get "allowBlock"(): boolean
get "allowSingleQuoted"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScalarAnalysis$Type = ($ScalarAnalysis);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScalarAnalysis_ = $ScalarAnalysis$Type;
}}
