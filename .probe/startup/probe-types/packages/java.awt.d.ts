declare module "packages/java/awt/event/$MouseMotionListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$MouseEvent, $MouseEvent$Type} from "packages/java/awt/event/$MouseEvent"

export interface $MouseMotionListener extends $EventListener {

 "mouseMoved"(arg0: $MouseEvent$Type): void
 "mouseDragged"(arg0: $MouseEvent$Type): void
}

export namespace $MouseMotionListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MouseMotionListener$Type = ($MouseMotionListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MouseMotionListener_ = $MouseMotionListener$Type;
}}
declare module "packages/java/awt/im/$InputMethodHighlight" {
import {$TextAttribute, $TextAttribute$Type} from "packages/java/awt/font/$TextAttribute"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $InputMethodHighlight {
static readonly "RAW_TEXT": integer
static readonly "CONVERTED_TEXT": integer
static readonly "UNSELECTED_RAW_TEXT_HIGHLIGHT": $InputMethodHighlight
static readonly "SELECTED_RAW_TEXT_HIGHLIGHT": $InputMethodHighlight
static readonly "UNSELECTED_CONVERTED_TEXT_HIGHLIGHT": $InputMethodHighlight
static readonly "SELECTED_CONVERTED_TEXT_HIGHLIGHT": $InputMethodHighlight

constructor(arg0: boolean, arg1: integer, arg2: integer, arg3: $Map$Type<($TextAttribute$Type), (any)>)
constructor(arg0: boolean, arg1: integer, arg2: integer)
constructor(arg0: boolean, arg1: integer)

public "getState"(): integer
public "isSelected"(): boolean
public "getVariation"(): integer
public "getStyle"(): $Map<($TextAttribute), (any)>
get "state"(): integer
get "selected"(): boolean
get "variation"(): integer
get "style"(): $Map<($TextAttribute), (any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputMethodHighlight$Type = ($InputMethodHighlight);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputMethodHighlight_ = $InputMethodHighlight$Type;
}}
declare module "packages/java/awt/event/$ItemListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$ItemEvent, $ItemEvent$Type} from "packages/java/awt/event/$ItemEvent"

export interface $ItemListener extends $EventListener {

 "itemStateChanged"(arg0: $ItemEvent$Type): void

(arg0: $ItemEvent$Type): void
}

export namespace $ItemListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemListener$Type = ($ItemListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemListener_ = $ItemListener$Type;
}}
declare module "packages/java/awt/geom/$Point2D" {
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"

export class $Point2D implements $Cloneable {


public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "clone"(): any
public static "distance"(arg0: double, arg1: double, arg2: double, arg3: double): double
public "distance"(arg0: $Point2D$Type): double
public "distance"(arg0: double, arg1: double): double
public "getY"(): double
public "getX"(): double
public "setLocation"(arg0: double, arg1: double): void
public "setLocation"(arg0: $Point2D$Type): void
public "distanceSq"(arg0: double, arg1: double): double
public "distanceSq"(arg0: $Point2D$Type): double
public static "distanceSq"(arg0: double, arg1: double, arg2: double, arg3: double): double
get "y"(): double
get "x"(): double
set "location"(value: $Point2D$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Point2D$Type = ($Point2D);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Point2D_ = $Point2D$Type;
}}
declare module "packages/java/awt/$Stroke" {
import {$Shape, $Shape$Type} from "packages/java/awt/$Shape"

export interface $Stroke {

 "createStrokedShape"(arg0: $Shape$Type): $Shape

(arg0: $Shape$Type): $Shape
}

export namespace $Stroke {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Stroke$Type = ($Stroke);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Stroke_ = $Stroke$Type;
}}
declare module "packages/java/awt/event/$AWTEventListener" {
import {$AWTEvent, $AWTEvent$Type} from "packages/java/awt/$AWTEvent"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"

export interface $AWTEventListener extends $EventListener {

 "eventDispatched"(arg0: $AWTEvent$Type): void

(arg0: $AWTEvent$Type): void
}

export namespace $AWTEventListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AWTEventListener$Type = ($AWTEventListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AWTEventListener_ = $AWTEventListener$Type;
}}
declare module "packages/java/awt/$Event" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

/**
 * 
 * @deprecated
 */
export class $Event implements $Serializable {
static readonly "SHIFT_MASK": integer
static readonly "CTRL_MASK": integer
static readonly "META_MASK": integer
static readonly "ALT_MASK": integer
static readonly "HOME": integer
static readonly "END": integer
static readonly "PGUP": integer
static readonly "PGDN": integer
static readonly "UP": integer
static readonly "DOWN": integer
static readonly "LEFT": integer
static readonly "RIGHT": integer
static readonly "F1": integer
static readonly "F2": integer
static readonly "F3": integer
static readonly "F4": integer
static readonly "F5": integer
static readonly "F6": integer
static readonly "F7": integer
static readonly "F8": integer
static readonly "F9": integer
static readonly "F10": integer
static readonly "F11": integer
static readonly "F12": integer
static readonly "PRINT_SCREEN": integer
static readonly "SCROLL_LOCK": integer
static readonly "CAPS_LOCK": integer
static readonly "NUM_LOCK": integer
static readonly "PAUSE": integer
static readonly "INSERT": integer
static readonly "ENTER": integer
static readonly "BACK_SPACE": integer
static readonly "TAB": integer
static readonly "ESCAPE": integer
static readonly "DELETE": integer
static readonly "WINDOW_DESTROY": integer
static readonly "WINDOW_EXPOSE": integer
static readonly "WINDOW_ICONIFY": integer
static readonly "WINDOW_DEICONIFY": integer
static readonly "WINDOW_MOVED": integer
static readonly "KEY_PRESS": integer
static readonly "KEY_RELEASE": integer
static readonly "KEY_ACTION": integer
static readonly "KEY_ACTION_RELEASE": integer
static readonly "MOUSE_DOWN": integer
static readonly "MOUSE_UP": integer
static readonly "MOUSE_MOVE": integer
static readonly "MOUSE_ENTER": integer
static readonly "MOUSE_EXIT": integer
static readonly "MOUSE_DRAG": integer
static readonly "SCROLL_LINE_UP": integer
static readonly "SCROLL_LINE_DOWN": integer
static readonly "SCROLL_PAGE_UP": integer
static readonly "SCROLL_PAGE_DOWN": integer
static readonly "SCROLL_ABSOLUTE": integer
static readonly "SCROLL_BEGIN": integer
static readonly "SCROLL_END": integer
static readonly "LIST_SELECT": integer
static readonly "LIST_DESELECT": integer
static readonly "ACTION_EVENT": integer
static readonly "LOAD_FILE": integer
static readonly "SAVE_FILE": integer
static readonly "GOT_FOCUS": integer
static readonly "LOST_FOCUS": integer
 "target": any
 "when": long
 "id": integer
 "x": integer
 "y": integer
 "key": integer
 "modifiers": integer
 "clickCount": integer
 "arg": any
 "evt": $Event

constructor(arg0: any, arg1: long, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer)
constructor(arg0: any, arg1: long, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: any)
constructor(arg0: any, arg1: integer, arg2: any)

public "toString"(): string
public "controlDown"(): boolean
public "translate"(arg0: integer, arg1: integer): void
public "shiftDown"(): boolean
public "metaDown"(): boolean
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
declare module "packages/java/awt/dnd/$DragSourceListener" {
import {$DragSourceDragEvent, $DragSourceDragEvent$Type} from "packages/java/awt/dnd/$DragSourceDragEvent"
import {$DragSourceDropEvent, $DragSourceDropEvent$Type} from "packages/java/awt/dnd/$DragSourceDropEvent"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$DragSourceEvent, $DragSourceEvent$Type} from "packages/java/awt/dnd/$DragSourceEvent"

export interface $DragSourceListener extends $EventListener {

 "dragDropEnd"(arg0: $DragSourceDropEvent$Type): void
 "dragEnter"(arg0: $DragSourceDragEvent$Type): void
 "dragOver"(arg0: $DragSourceDragEvent$Type): void
 "dropActionChanged"(arg0: $DragSourceDragEvent$Type): void
 "dragExit"(arg0: $DragSourceEvent$Type): void
}

export namespace $DragSourceListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DragSourceListener$Type = ($DragSourceListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DragSourceListener_ = $DragSourceListener$Type;
}}
declare module "packages/java/awt/event/$ActionListener" {
import {$ActionEvent, $ActionEvent$Type} from "packages/java/awt/event/$ActionEvent"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"

export interface $ActionListener extends $EventListener {

 "actionPerformed"(arg0: $ActionEvent$Type): void

(arg0: $ActionEvent$Type): void
}

export namespace $ActionListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ActionListener$Type = ($ActionListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ActionListener_ = $ActionListener$Type;
}}
declare module "packages/java/awt/event/$FocusEvent$Cause" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $FocusEvent$Cause extends $Enum<($FocusEvent$Cause)> {
static readonly "UNKNOWN": $FocusEvent$Cause
static readonly "MOUSE_EVENT": $FocusEvent$Cause
static readonly "TRAVERSAL": $FocusEvent$Cause
static readonly "TRAVERSAL_UP": $FocusEvent$Cause
static readonly "TRAVERSAL_DOWN": $FocusEvent$Cause
static readonly "TRAVERSAL_FORWARD": $FocusEvent$Cause
static readonly "TRAVERSAL_BACKWARD": $FocusEvent$Cause
static readonly "ROLLBACK": $FocusEvent$Cause
static readonly "UNEXPECTED": $FocusEvent$Cause
static readonly "ACTIVATION": $FocusEvent$Cause
static readonly "CLEAR_GLOBAL_FOCUS_OWNER": $FocusEvent$Cause


public static "values"(): ($FocusEvent$Cause)[]
public static "valueOf"(arg0: string): $FocusEvent$Cause
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FocusEvent$Cause$Type = (("rollback") | ("traversal_up") | ("traversal_backward") | ("unexpected") | ("clear_global_focus_owner") | ("traversal") | ("activation") | ("traversal_down") | ("mouse_event") | ("unknown") | ("traversal_forward")) | ($FocusEvent$Cause);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FocusEvent$Cause_ = $FocusEvent$Cause$Type;
}}
declare module "packages/java/awt/event/$MouseListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$MouseEvent, $MouseEvent$Type} from "packages/java/awt/event/$MouseEvent"

export interface $MouseListener extends $EventListener {

 "mouseClicked"(arg0: $MouseEvent$Type): void
 "mousePressed"(arg0: $MouseEvent$Type): void
 "mouseExited"(arg0: $MouseEvent$Type): void
 "mouseEntered"(arg0: $MouseEvent$Type): void
 "mouseReleased"(arg0: $MouseEvent$Type): void
}

export namespace $MouseListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MouseListener$Type = ($MouseListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MouseListener_ = $MouseListener$Type;
}}
declare module "packages/java/awt/$JobAttributes$DefaultSelectionType" {
import {$AttributeValue, $AttributeValue$Type} from "packages/java/awt/$AttributeValue"

export class $JobAttributes$DefaultSelectionType extends $AttributeValue {
static readonly "ALL": $JobAttributes$DefaultSelectionType
static readonly "RANGE": $JobAttributes$DefaultSelectionType
static readonly "SELECTION": $JobAttributes$DefaultSelectionType


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JobAttributes$DefaultSelectionType$Type = ($JobAttributes$DefaultSelectionType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JobAttributes$DefaultSelectionType_ = $JobAttributes$DefaultSelectionType$Type;
}}
declare module "packages/java/awt/image/$IndexColorModel" {
import {$WritableRaster, $WritableRaster$Type} from "packages/java/awt/image/$WritableRaster"
import {$Raster, $Raster$Type} from "packages/java/awt/image/$Raster"
import {$BigInteger, $BigInteger$Type} from "packages/java/math/$BigInteger"
import {$BufferedImage, $BufferedImage$Type} from "packages/java/awt/image/$BufferedImage"
import {$SampleModel, $SampleModel$Type} from "packages/java/awt/image/$SampleModel"
import {$ColorModel, $ColorModel$Type} from "packages/java/awt/image/$ColorModel"

export class $IndexColorModel extends $ColorModel {

constructor(arg0: integer, arg1: integer, arg2: (byte)[], arg3: (byte)[], arg4: (byte)[], arg5: (byte)[])
constructor(arg0: integer, arg1: integer, arg2: (byte)[], arg3: integer, arg4: boolean)
constructor(arg0: integer, arg1: integer, arg2: (byte)[], arg3: integer, arg4: boolean, arg5: integer)
constructor(arg0: integer, arg1: integer, arg2: (integer)[], arg3: integer, arg4: integer, arg5: $BigInteger$Type)
constructor(arg0: integer, arg1: integer, arg2: (byte)[], arg3: (byte)[], arg4: (byte)[], arg5: integer)
constructor(arg0: integer, arg1: integer, arg2: (byte)[], arg3: (byte)[], arg4: (byte)[])
constructor(arg0: integer, arg1: integer, arg2: (integer)[], arg3: integer, arg4: boolean, arg5: integer, arg6: integer)

/**
 * 
 * @deprecated
 */
public "finalize"(): void
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "isValid"(): boolean
public "isValid"(arg0: integer): boolean
public "createCompatibleWritableRaster"(arg0: integer, arg1: integer): $WritableRaster
public "getMapSize"(): integer
public "isCompatibleRaster"(arg0: $Raster$Type): boolean
public "getComponentSize"(): (integer)[]
public "getDataElements"(arg0: (integer)[], arg1: integer, arg2: any): any
public "getDataElements"(arg0: integer, arg1: any): any
public "createCompatibleSampleModel"(arg0: integer, arg1: integer): $SampleModel
public "getTransparentPixel"(): integer
public "getReds"(arg0: (byte)[]): void
public "getGreens"(arg0: (byte)[]): void
public "getBlues"(arg0: (byte)[]): void
public "getAlphas"(arg0: (byte)[]): void
public "getRGBs"(arg0: (integer)[]): void
public "convertToIntDiscrete"(arg0: $Raster$Type, arg1: boolean): $BufferedImage
public "getValidPixels"(): $BigInteger
public "getRGB"(arg0: integer): integer
public "getRed"(arg0: integer): integer
public "getGreen"(arg0: integer): integer
public "getBlue"(arg0: integer): integer
public "getAlpha"(arg0: integer): integer
public "getTransparency"(): integer
public "getComponents"(arg0: integer, arg1: (integer)[], arg2: integer): (integer)[]
public "getComponents"(arg0: any, arg1: (integer)[], arg2: integer): (integer)[]
public "getDataElement"(arg0: (integer)[], arg1: integer): integer
public "isCompatibleSampleModel"(arg0: $SampleModel$Type): boolean
get "valid"(): boolean
get "mapSize"(): integer
get "componentSize"(): (integer)[]
get "transparentPixel"(): integer
get "validPixels"(): $BigInteger
get "transparency"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IndexColorModel$Type = ($IndexColorModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IndexColorModel_ = $IndexColorModel$Type;
}}
declare module "packages/java/awt/$EventQueue" {
import {$AWTEvent, $AWTEvent$Type} from "packages/java/awt/$AWTEvent"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$SecondaryLoop, $SecondaryLoop$Type} from "packages/java/awt/$SecondaryLoop"

export class $EventQueue {

constructor()

public "push"(arg0: $EventQueue$Type): void
public "peekEvent"(): $AWTEvent
public "peekEvent"(arg0: integer): $AWTEvent
public "postEvent"(arg0: $AWTEvent$Type): void
public static "getCurrentEvent"(): $AWTEvent
public static "invokeLater"(arg0: $Runnable$Type): void
public static "isDispatchThread"(): boolean
public static "invokeAndWait"(arg0: $Runnable$Type): void
public "createSecondaryLoop"(): $SecondaryLoop
public "getNextEvent"(): $AWTEvent
public static "getMostRecentEventTime"(): long
get "currentEvent"(): $AWTEvent
get "dispatchThread"(): boolean
get "nextEvent"(): $AWTEvent
get "mostRecentEventTime"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventQueue$Type = ($EventQueue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventQueue_ = $EventQueue$Type;
}}
declare module "packages/java/awt/$DisplayMode" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $DisplayMode {
static readonly "BIT_DEPTH_MULTI": integer
static readonly "REFRESH_RATE_UNKNOWN": integer

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer)

public "equals"(arg0: $DisplayMode$Type): boolean
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getWidth"(): integer
public "getHeight"(): integer
public "getBitDepth"(): integer
public "getRefreshRate"(): integer
get "width"(): integer
get "height"(): integer
get "bitDepth"(): integer
get "refreshRate"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DisplayMode$Type = ($DisplayMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DisplayMode_ = $DisplayMode$Type;
}}
declare module "packages/java/awt/$MenuBar" {
import {$Enumeration, $Enumeration$Type} from "packages/java/util/$Enumeration"
import {$MenuShortcut, $MenuShortcut$Type} from "packages/java/awt/$MenuShortcut"
import {$Event, $Event$Type} from "packages/java/awt/$Event"
import {$MenuContainer, $MenuContainer$Type} from "packages/java/awt/$MenuContainer"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$MenuComponent, $MenuComponent$Type} from "packages/java/awt/$MenuComponent"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$Menu, $Menu$Type} from "packages/java/awt/$Menu"
import {$Font, $Font$Type} from "packages/java/awt/$Font"
import {$MenuItem, $MenuItem$Type} from "packages/java/awt/$MenuItem"

export class $MenuBar extends $MenuComponent implements $MenuContainer, $Accessible {

constructor()

public "add"(arg0: $Menu$Type): $Menu
public "remove"(arg0: $MenuComponent$Type): void
public "remove"(arg0: integer): void
public "removeNotify"(): void
public "addNotify"(): void
public "getAccessibleContext"(): $AccessibleContext
public "getMenu"(arg0: integer): $Menu
/**
 * 
 * @deprecated
 */
public "countMenus"(): integer
public "shortcuts"(): $Enumeration<($MenuShortcut)>
public "getShortcutMenuItem"(arg0: $MenuShortcut$Type): $MenuItem
public "deleteShortcut"(arg0: $MenuShortcut$Type): void
public "getMenuCount"(): integer
public "setHelpMenu"(arg0: $Menu$Type): void
public "getHelpMenu"(): $Menu
/**
 * 
 * @deprecated
 */
public "postEvent"(arg0: $Event$Type): boolean
public "getFont"(): $Font
get "accessibleContext"(): $AccessibleContext
get "menuCount"(): integer
set "helpMenu"(value: $Menu$Type)
get "helpMenu"(): $Menu
get "font"(): $Font
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuBar$Type = ($MenuBar);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuBar_ = $MenuBar$Type;
}}
declare module "packages/java/awt/$PageAttributes$ColorType" {
import {$AttributeValue, $AttributeValue$Type} from "packages/java/awt/$AttributeValue"

export class $PageAttributes$ColorType extends $AttributeValue {
static readonly "COLOR": $PageAttributes$ColorType
static readonly "MONOCHROME": $PageAttributes$ColorType


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageAttributes$ColorType$Type = ($PageAttributes$ColorType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageAttributes$ColorType_ = $PageAttributes$ColorType$Type;
}}
declare module "packages/java/awt/image/$ImageConsumer" {
import {$Hashtable, $Hashtable$Type} from "packages/java/util/$Hashtable"
import {$ColorModel, $ColorModel$Type} from "packages/java/awt/image/$ColorModel"

export interface $ImageConsumer {

 "setProperties"(arg0: $Hashtable$Type<(any), (any)>): void
 "setDimensions"(arg0: integer, arg1: integer): void
 "setPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ColorModel$Type, arg5: (integer)[], arg6: integer, arg7: integer): void
 "setPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $ColorModel$Type, arg5: (byte)[], arg6: integer, arg7: integer): void
 "setColorModel"(arg0: $ColorModel$Type): void
 "setHints"(arg0: integer): void
 "imageComplete"(arg0: integer): void
}

export namespace $ImageConsumer {
const RANDOMPIXELORDER: integer
const TOPDOWNLEFTRIGHT: integer
const COMPLETESCANLINES: integer
const SINGLEPASS: integer
const SINGLEFRAME: integer
const IMAGEERROR: integer
const SINGLEFRAMEDONE: integer
const STATICIMAGEDONE: integer
const IMAGEABORTED: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImageConsumer$Type = ($ImageConsumer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImageConsumer_ = $ImageConsumer$Type;
}}
declare module "packages/java/awt/$Component" {
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$PopupMenu, $PopupMenu$Type} from "packages/java/awt/$PopupMenu"
import {$ImageProducer, $ImageProducer$Type} from "packages/java/awt/image/$ImageProducer"
import {$InputMethodListener, $InputMethodListener$Type} from "packages/java/awt/event/$InputMethodListener"
import {$ComponentListener, $ComponentListener$Type} from "packages/java/awt/event/$ComponentListener"
import {$Color, $Color$Type} from "packages/java/awt/$Color"
import {$Event, $Event$Type} from "packages/java/awt/$Event"
import {$InputContext, $InputContext$Type} from "packages/java/awt/im/$InputContext"
import {$MouseListener, $MouseListener$Type} from "packages/java/awt/event/$MouseListener"
import {$HierarchyListener, $HierarchyListener$Type} from "packages/java/awt/event/$HierarchyListener"
import {$PrintStream, $PrintStream$Type} from "packages/java/io/$PrintStream"
import {$Image, $Image$Type} from "packages/java/awt/$Image"
import {$FocusListener, $FocusListener$Type} from "packages/java/awt/event/$FocusListener"
import {$ComponentOrientation, $ComponentOrientation$Type} from "packages/java/awt/$ComponentOrientation"
import {$HierarchyBoundsListener, $HierarchyBoundsListener$Type} from "packages/java/awt/event/$HierarchyBoundsListener"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Cursor, $Cursor$Type} from "packages/java/awt/$Cursor"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$FocusEvent$Cause, $FocusEvent$Cause$Type} from "packages/java/awt/event/$FocusEvent$Cause"
import {$ImageObserver, $ImageObserver$Type} from "packages/java/awt/image/$ImageObserver"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"
import {$Toolkit, $Toolkit$Type} from "packages/java/awt/$Toolkit"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$ColorModel, $ColorModel$Type} from "packages/java/awt/image/$ColorModel"
import {$InputMethodRequests, $InputMethodRequests$Type} from "packages/java/awt/im/$InputMethodRequests"
import {$KeyListener, $KeyListener$Type} from "packages/java/awt/event/$KeyListener"
import {$AWTEvent, $AWTEvent$Type} from "packages/java/awt/$AWTEvent"
import {$MouseMotionListener, $MouseMotionListener$Type} from "packages/java/awt/event/$MouseMotionListener"
import {$AWTKeyStroke, $AWTKeyStroke$Type} from "packages/java/awt/$AWTKeyStroke"
import {$Component$BaselineResizeBehavior, $Component$BaselineResizeBehavior$Type} from "packages/java/awt/$Component$BaselineResizeBehavior"
import {$MouseWheelListener, $MouseWheelListener$Type} from "packages/java/awt/event/$MouseWheelListener"
import {$DropTarget, $DropTarget$Type} from "packages/java/awt/dnd/$DropTarget"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$GraphicsConfiguration, $GraphicsConfiguration$Type} from "packages/java/awt/$GraphicsConfiguration"
import {$PropertyChangeListener, $PropertyChangeListener$Type} from "packages/java/beans/$PropertyChangeListener"
import {$Container, $Container$Type} from "packages/java/awt/$Container"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$FontMetrics, $FontMetrics$Type} from "packages/java/awt/$FontMetrics"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$VolatileImage, $VolatileImage$Type} from "packages/java/awt/image/$VolatileImage"
import {$Font, $Font$Type} from "packages/java/awt/$Font"
import {$ImageCapabilities, $ImageCapabilities$Type} from "packages/java/awt/$ImageCapabilities"
import {$Shape, $Shape$Type} from "packages/java/awt/$Shape"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$MenuContainer, $MenuContainer$Type} from "packages/java/awt/$MenuContainer"
import {$MenuComponent, $MenuComponent$Type} from "packages/java/awt/$MenuComponent"
import {$Dimension, $Dimension$Type} from "packages/java/awt/$Dimension"
import {$PrintWriter, $PrintWriter$Type} from "packages/java/io/$PrintWriter"

export class $Component implements $ImageObserver, $MenuContainer, $Serializable {
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float


public "getName"(): string
public "add"(arg0: $PopupMenu$Type): void
public "remove"(arg0: $MenuComponent$Type): void
public "toString"(): string
public "update"(arg0: $Graphics$Type): void
/**
 * 
 * @deprecated
 */
public "size"(): $Dimension
public "contains"(arg0: $Point$Type): boolean
public "contains"(arg0: integer, arg1: integer): boolean
public "list"(arg0: $PrintStream$Type, arg1: integer): void
public "list"(): void
public "list"(arg0: $PrintWriter$Type, arg1: integer): void
public "list"(arg0: $PrintWriter$Type): void
public "list"(arg0: $PrintStream$Type): void
public "getBounds"(arg0: $Rectangle$Type): $Rectangle
public "getBounds"(): $Rectangle
public "validate"(): void
/**
 * 
 * @deprecated
 */
public "bounds"(): $Rectangle
public "getLocation"(arg0: $Point$Type): $Point
public "getLocation"(): $Point
public "getParent"(): $Container
/**
 * 
 * @deprecated
 */
public "action"(arg0: $Event$Type, arg1: any): boolean
public "setName"(arg0: string): void
public "print"(arg0: $Graphics$Type): void
/**
 * 
 * @deprecated
 */
public "location"(): $Point
public "getSize"(arg0: $Dimension$Type): $Dimension
public "getSize"(): $Dimension
public "isOpaque"(): boolean
/**
 * 
 * @deprecated
 */
public "resize"(arg0: $Dimension$Type): void
/**
 * 
 * @deprecated
 */
public "resize"(arg0: integer, arg1: integer): void
/**
 * 
 * @deprecated
 */
public "move"(arg0: integer, arg1: integer): void
public "getLocale"(): $Locale
public "setLocale"(arg0: $Locale$Type): void
public "isValid"(): boolean
/**
 * 
 * @deprecated
 */
public "enable"(arg0: boolean): void
/**
 * 
 * @deprecated
 */
public "enable"(): void
public "setSize"(arg0: $Dimension$Type): void
public "setSize"(arg0: integer, arg1: integer): void
public "isEnabled"(): boolean
public "invalidate"(): void
public "getLocationOnScreen"(): $Point
public "getY"(): integer
/**
 * 
 * @deprecated
 */
public "handleEvent"(arg0: $Event$Type): boolean
public "dispatchEvent"(arg0: $AWTEvent$Type): void
public "setEnabled"(arg0: boolean): void
public "prepareImage"(arg0: $Image$Type, arg1: $ImageObserver$Type): boolean
public "prepareImage"(arg0: $Image$Type, arg1: integer, arg2: integer, arg3: $ImageObserver$Type): boolean
public "getGraphics"(): $Graphics
public "setVisible"(arg0: boolean): void
public "getListeners"<T extends $EventListener>(arg0: $Class$Type<(T)>): (T)[]
public "getCursor"(): $Cursor
public "getX"(): integer
public "getBaseline"(arg0: integer, arg1: integer): integer
public "isVisible"(): boolean
/**
 * 
 * @deprecated
 */
public "hide"(): void
public "getWidth"(): integer
public "getHeight"(): integer
public "getColorModel"(): $ColorModel
public "createImage"(arg0: $ImageProducer$Type): $Image
public "createImage"(arg0: integer, arg1: integer): $Image
public "firePropertyChange"(arg0: string, arg1: character, arg2: character): void
public "firePropertyChange"(arg0: string, arg1: byte, arg2: byte): void
public "firePropertyChange"(arg0: string, arg1: float, arg2: float): void
public "firePropertyChange"(arg0: string, arg1: double, arg2: double): void
public "firePropertyChange"(arg0: string, arg1: long, arg2: long): void
public "firePropertyChange"(arg0: string, arg1: short, arg2: short): void
public "getPropertyChangeListeners"(arg0: string): ($PropertyChangeListener)[]
public "getPropertyChangeListeners"(): ($PropertyChangeListener)[]
public "checkImage"(arg0: $Image$Type, arg1: integer, arg2: integer, arg3: $ImageObserver$Type): integer
public "checkImage"(arg0: $Image$Type, arg1: $ImageObserver$Type): integer
/**
 * 
 * @deprecated
 */
public "postEvent"(arg0: $Event$Type): boolean
/**
 * 
 * @deprecated
 */
public "minimumSize"(): $Dimension
public "getTreeLock"(): any
public "removeNotify"(): void
public "addNotify"(): void
public "isShowing"(): boolean
public "isFocusOwner"(): boolean
public "transferFocus"(): void
public "getInputContext"(): $InputContext
public "repaint"(arg0: long): void
public "repaint"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "repaint"(arg0: long, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public "repaint"(): void
/**
 * 
 * @deprecated
 */
public "preferredSize"(): $Dimension
public "isPreferredSizeSet"(): boolean
public "getPreferredSize"(): $Dimension
public "getMinimumSize"(): $Dimension
public "isMinimumSizeSet"(): boolean
public "isMaximumSizeSet"(): boolean
public "createVolatileImage"(arg0: integer, arg1: integer, arg2: $ImageCapabilities$Type): $VolatileImage
public "createVolatileImage"(arg0: integer, arg1: integer): $VolatileImage
public "getComponentAt"(arg0: integer, arg1: integer): $Component
public "getComponentAt"(arg0: $Point$Type): $Component
public "setLocation"(arg0: $Point$Type): void
public "setLocation"(arg0: integer, arg1: integer): void
/**
 * 
 * @deprecated
 */
public "mouseEnter"(arg0: $Event$Type, arg1: integer, arg2: integer): boolean
/**
 * 
 * @deprecated
 */
public "mouseExit"(arg0: $Event$Type, arg1: integer, arg2: integer): boolean
/**
 * 
 * @deprecated
 */
public "mouseMove"(arg0: $Event$Type, arg1: integer, arg2: integer): boolean
/**
 * 
 * @deprecated
 */
public "mouseDown"(arg0: $Event$Type, arg1: integer, arg2: integer): boolean
/**
 * 
 * @deprecated
 */
public "mouseDrag"(arg0: $Event$Type, arg1: integer, arg2: integer): boolean
/**
 * 
 * @deprecated
 */
public "mouseUp"(arg0: $Event$Type, arg1: integer, arg2: integer): boolean
/**
 * 
 * @deprecated
 */
public "keyDown"(arg0: $Event$Type, arg1: integer): boolean
/**
 * 
 * @deprecated
 */
public "keyUp"(arg0: $Event$Type, arg1: integer): boolean
/**
 * 
 * @deprecated
 */
public "gotFocus"(arg0: $Event$Type, arg1: any): boolean
/**
 * 
 * @deprecated
 */
public "lostFocus"(arg0: $Event$Type, arg1: any): boolean
/**
 * 
 * @deprecated
 */
public "isFocusTraversable"(): boolean
public "getFocusTraversalKeys"(arg0: integer): $Set<($AWTKeyStroke)>
public "isFocusCycleRoot"(arg0: $Container$Type): boolean
public "getFocusCycleRootAncestor"(): $Container
/**
 * 
 * @deprecated
 */
public "nextFocus"(): void
public "requestFocusInWindow"(): boolean
public "requestFocusInWindow"(arg0: $FocusEvent$Cause$Type): boolean
public "transferFocusBackward"(): void
public "addComponentListener"(arg0: $ComponentListener$Type): void
public "addFocusListener"(arg0: $FocusListener$Type): void
public "addKeyListener"(arg0: $KeyListener$Type): void
public "addMouseListener"(arg0: $MouseListener$Type): void
public "addMouseMotionListener"(arg0: $MouseMotionListener$Type): void
public "addInputMethodListener"(arg0: $InputMethodListener$Type): void
public "addHierarchyListener"(arg0: $HierarchyListener$Type): void
public "addHierarchyBoundsListener"(arg0: $HierarchyBoundsListener$Type): void
public "addMouseWheelListener"(arg0: $MouseWheelListener$Type): void
public "setComponentOrientation"(arg0: $ComponentOrientation$Type): void
public "isDisplayable"(): boolean
public "getAccessibleContext"(): $AccessibleContext
public "setDropTarget"(arg0: $DropTarget$Type): void
public "getDropTarget"(): $DropTarget
public "getMousePosition"(): $Point
public "isDoubleBuffered"(): boolean
public "enableInputMethods"(arg0: boolean): void
public "isForegroundSet"(): boolean
public "isBackgroundSet"(): boolean
public "isFontSet"(): boolean
public "setPreferredSize"(arg0: $Dimension$Type): void
public "setMinimumSize"(arg0: $Dimension$Type): void
public "setMaximumSize"(arg0: $Dimension$Type): void
public "getMaximumSize"(): $Dimension
public "getAlignmentX"(): float
public "getAlignmentY"(): float
public "getBaselineResizeBehavior"(): $Component$BaselineResizeBehavior
public "doLayout"(): void
public "revalidate"(): void
public "isCursorSet"(): boolean
public "paintAll"(arg0: $Graphics$Type): void
public "printAll"(arg0: $Graphics$Type): void
public "setIgnoreRepaint"(arg0: boolean): void
public "getIgnoreRepaint"(): boolean
/**
 * 
 * @deprecated
 */
public "deliverEvent"(arg0: $Event$Type): void
public "removeComponentListener"(arg0: $ComponentListener$Type): void
public "getComponentListeners"(): ($ComponentListener)[]
public "removeFocusListener"(arg0: $FocusListener$Type): void
public "getFocusListeners"(): ($FocusListener)[]
public "removeHierarchyListener"(arg0: $HierarchyListener$Type): void
public "getHierarchyListeners"(): ($HierarchyListener)[]
public "removeHierarchyBoundsListener"(arg0: $HierarchyBoundsListener$Type): void
public "getHierarchyBoundsListeners"(): ($HierarchyBoundsListener)[]
public "removeKeyListener"(arg0: $KeyListener$Type): void
public "getKeyListeners"(): ($KeyListener)[]
public "removeMouseListener"(arg0: $MouseListener$Type): void
public "getMouseListeners"(): ($MouseListener)[]
public "removeMouseMotionListener"(arg0: $MouseMotionListener$Type): void
public "getMouseMotionListeners"(): ($MouseMotionListener)[]
public "removeMouseWheelListener"(arg0: $MouseWheelListener$Type): void
public "getMouseWheelListeners"(): ($MouseWheelListener)[]
public "removeInputMethodListener"(arg0: $InputMethodListener$Type): void
public "getInputMethodListeners"(): ($InputMethodListener)[]
public "getInputMethodRequests"(): $InputMethodRequests
public "setFocusTraversalKeys"(arg0: integer, arg1: $Set$Type<(any)>): void
public "areFocusTraversalKeysSet"(arg0: integer): boolean
public "setFocusTraversalKeysEnabled"(arg0: boolean): void
public "getFocusTraversalKeysEnabled"(): boolean
public "transferFocusUpCycle"(): void
public "getComponentOrientation"(): $ComponentOrientation
public "applyComponentOrientation"(arg0: $ComponentOrientation$Type): void
public "setMixingCutoutShape"(arg0: $Shape$Type): void
public "getBackground"(): $Color
public "setBackground"(arg0: $Color$Type): void
public "getForeground"(): $Color
public "setForeground"(arg0: $Color$Type): void
public "getFont"(): $Font
public "setFont"(arg0: $Font$Type): void
public "getToolkit"(): $Toolkit
/**
 * 
 * @deprecated
 */
public "show"(): void
/**
 * 
 * @deprecated
 */
public "show"(arg0: boolean): void
public "paint"(arg0: $Graphics$Type): void
public "setBounds"(arg0: $Rectangle$Type): void
public "setBounds"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "isFocusable"(): boolean
public "setFocusable"(arg0: boolean): void
public "hasFocus"(): boolean
public "requestFocus"(arg0: $FocusEvent$Cause$Type): void
public "requestFocus"(): void
/**
 * 
 * @deprecated
 */
public "inside"(arg0: integer, arg1: integer): boolean
/**
 * 
 * @deprecated
 */
public "disable"(): void
public "addPropertyChangeListener"(arg0: $PropertyChangeListener$Type): void
public "addPropertyChangeListener"(arg0: string, arg1: $PropertyChangeListener$Type): void
public "removePropertyChangeListener"(arg0: string, arg1: $PropertyChangeListener$Type): void
public "removePropertyChangeListener"(arg0: $PropertyChangeListener$Type): void
/**
 * 
 * @deprecated
 */
public "layout"(): void
public "getFontMetrics"(arg0: $Font$Type): $FontMetrics
/**
 * 
 * @deprecated
 */
public "reshape"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "setCursor"(arg0: $Cursor$Type): void
/**
 * 
 * @deprecated
 */
public "locate"(arg0: integer, arg1: integer): $Component
public "getGraphicsConfiguration"(): $GraphicsConfiguration
public "imageUpdate"(arg0: $Image$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): boolean
public "isLightweight"(): boolean
get "name"(): string
get "parent"(): $Container
set "name"(value: string)
get "opaque"(): boolean
get "locale"(): $Locale
set "locale"(value: $Locale$Type)
get "valid"(): boolean
get "enabled"(): boolean
get "locationOnScreen"(): $Point
get "y"(): integer
set "enabled"(value: boolean)
get "graphics"(): $Graphics
set "visible"(value: boolean)
get "cursor"(): $Cursor
get "x"(): integer
get "visible"(): boolean
get "width"(): integer
get "height"(): integer
get "colorModel"(): $ColorModel
get "propertyChangeListeners"(): ($PropertyChangeListener)[]
get "treeLock"(): any
get "showing"(): boolean
get "focusOwner"(): boolean
get "inputContext"(): $InputContext
get "preferredSizeSet"(): boolean
get "minimumSizeSet"(): boolean
get "maximumSizeSet"(): boolean
get "focusTraversable"(): boolean
get "focusCycleRootAncestor"(): $Container
set "componentOrientation"(value: $ComponentOrientation$Type)
get "displayable"(): boolean
get "accessibleContext"(): $AccessibleContext
set "dropTarget"(value: $DropTarget$Type)
get "dropTarget"(): $DropTarget
get "mousePosition"(): $Point
get "doubleBuffered"(): boolean
get "foregroundSet"(): boolean
get "backgroundSet"(): boolean
get "fontSet"(): boolean
set "maximumSize"(value: $Dimension$Type)
get "maximumSize"(): $Dimension
get "alignmentX"(): float
get "alignmentY"(): float
get "baselineResizeBehavior"(): $Component$BaselineResizeBehavior
get "cursorSet"(): boolean
set "ignoreRepaint"(value: boolean)
get "ignoreRepaint"(): boolean
get "componentListeners"(): ($ComponentListener)[]
get "focusListeners"(): ($FocusListener)[]
get "hierarchyListeners"(): ($HierarchyListener)[]
get "hierarchyBoundsListeners"(): ($HierarchyBoundsListener)[]
get "keyListeners"(): ($KeyListener)[]
get "mouseListeners"(): ($MouseListener)[]
get "mouseMotionListeners"(): ($MouseMotionListener)[]
get "mouseWheelListeners"(): ($MouseWheelListener)[]
get "inputMethodListeners"(): ($InputMethodListener)[]
get "inputMethodRequests"(): $InputMethodRequests
set "focusTraversalKeysEnabled"(value: boolean)
get "focusTraversalKeysEnabled"(): boolean
get "componentOrientation"(): $ComponentOrientation
set "mixingCutoutShape"(value: $Shape$Type)
get "background"(): $Color
set "background"(value: $Color$Type)
get "foreground"(): $Color
set "foreground"(value: $Color$Type)
get "font"(): $Font
set "font"(value: $Font$Type)
get "toolkit"(): $Toolkit
get "focusable"(): boolean
set "focusable"(value: boolean)
set "cursor"(value: $Cursor$Type)
get "graphicsConfiguration"(): $GraphicsConfiguration
get "lightweight"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Component$Type = ($Component);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Component_ = $Component$Type;
}}
declare module "packages/java/awt/$GraphicsConfiguration" {
import {$GraphicsDevice, $GraphicsDevice$Type} from "packages/java/awt/$GraphicsDevice"
import {$BufferedImage, $BufferedImage$Type} from "packages/java/awt/image/$BufferedImage"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$ColorModel, $ColorModel$Type} from "packages/java/awt/image/$ColorModel"
import {$VolatileImage, $VolatileImage$Type} from "packages/java/awt/image/$VolatileImage"
import {$BufferCapabilities, $BufferCapabilities$Type} from "packages/java/awt/$BufferCapabilities"
import {$ImageCapabilities, $ImageCapabilities$Type} from "packages/java/awt/$ImageCapabilities"
import {$AffineTransform, $AffineTransform$Type} from "packages/java/awt/geom/$AffineTransform"

export class $GraphicsConfiguration {


public "getBounds"(): $Rectangle
public "getColorModel"(): $ColorModel
public "getColorModel"(arg0: integer): $ColorModel
public "createCompatibleImage"(arg0: integer, arg1: integer, arg2: integer): $BufferedImage
public "createCompatibleImage"(arg0: integer, arg1: integer): $BufferedImage
public "getDevice"(): $GraphicsDevice
public "getNormalizingTransform"(): $AffineTransform
public "createCompatibleVolatileImage"(arg0: integer, arg1: integer): $VolatileImage
public "createCompatibleVolatileImage"(arg0: integer, arg1: integer, arg2: integer): $VolatileImage
public "createCompatibleVolatileImage"(arg0: integer, arg1: integer, arg2: $ImageCapabilities$Type): $VolatileImage
public "createCompatibleVolatileImage"(arg0: integer, arg1: integer, arg2: $ImageCapabilities$Type, arg3: integer): $VolatileImage
public "getImageCapabilities"(): $ImageCapabilities
public "getBufferCapabilities"(): $BufferCapabilities
public "getDefaultTransform"(): $AffineTransform
public "isTranslucencyCapable"(): boolean
get "bounds"(): $Rectangle
get "colorModel"(): $ColorModel
get "device"(): $GraphicsDevice
get "normalizingTransform"(): $AffineTransform
get "imageCapabilities"(): $ImageCapabilities
get "bufferCapabilities"(): $BufferCapabilities
get "defaultTransform"(): $AffineTransform
get "translucencyCapable"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GraphicsConfiguration$Type = ($GraphicsConfiguration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GraphicsConfiguration_ = $GraphicsConfiguration$Type;
}}
declare module "packages/java/awt/event/$MouseWheelEvent" {
import {$MouseEvent, $MouseEvent$Type} from "packages/java/awt/event/$MouseEvent"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export class $MouseWheelEvent extends $MouseEvent {
static readonly "WHEEL_UNIT_SCROLL": integer
static readonly "WHEEL_BLOCK_SCROLL": integer
static readonly "MOUSE_FIRST": integer
static readonly "MOUSE_LAST": integer
static readonly "MOUSE_CLICKED": integer
static readonly "MOUSE_PRESSED": integer
static readonly "MOUSE_RELEASED": integer
static readonly "MOUSE_MOVED": integer
static readonly "MOUSE_ENTERED": integer
static readonly "MOUSE_EXITED": integer
static readonly "MOUSE_DRAGGED": integer
static readonly "MOUSE_WHEEL": integer
static readonly "NOBUTTON": integer
static readonly "BUTTON1": integer
static readonly "BUTTON2": integer
static readonly "BUTTON3": integer
/**
 * 
 * @deprecated
 */
static readonly "SHIFT_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "CTRL_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "META_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "ALT_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "ALT_GRAPH_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "BUTTON1_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "BUTTON2_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "BUTTON3_MASK": integer
static readonly "SHIFT_DOWN_MASK": integer
static readonly "CTRL_DOWN_MASK": integer
static readonly "META_DOWN_MASK": integer
static readonly "ALT_DOWN_MASK": integer
static readonly "BUTTON1_DOWN_MASK": integer
static readonly "BUTTON2_DOWN_MASK": integer
static readonly "BUTTON3_DOWN_MASK": integer
static readonly "ALT_GRAPH_DOWN_MASK": integer
static readonly "COMPONENT_FIRST": integer
static readonly "COMPONENT_LAST": integer
static readonly "COMPONENT_MOVED": integer
static readonly "COMPONENT_RESIZED": integer
static readonly "COMPONENT_SHOWN": integer
static readonly "COMPONENT_HIDDEN": integer
static readonly "COMPONENT_EVENT_MASK": long
static readonly "CONTAINER_EVENT_MASK": long
static readonly "FOCUS_EVENT_MASK": long
static readonly "KEY_EVENT_MASK": long
static readonly "MOUSE_EVENT_MASK": long
static readonly "MOUSE_MOTION_EVENT_MASK": long
static readonly "WINDOW_EVENT_MASK": long
static readonly "ACTION_EVENT_MASK": long
static readonly "ADJUSTMENT_EVENT_MASK": long
static readonly "ITEM_EVENT_MASK": long
static readonly "TEXT_EVENT_MASK": long
static readonly "INPUT_METHOD_EVENT_MASK": long
static readonly "PAINT_EVENT_MASK": long
static readonly "INVOCATION_EVENT_MASK": long
static readonly "HIERARCHY_EVENT_MASK": long
static readonly "HIERARCHY_BOUNDS_EVENT_MASK": long
static readonly "MOUSE_WHEEL_EVENT_MASK": long
static readonly "WINDOW_STATE_EVENT_MASK": long
static readonly "WINDOW_FOCUS_EVENT_MASK": long
static readonly "RESERVED_ID_MAX": integer

constructor(arg0: $Component$Type, arg1: integer, arg2: long, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: boolean, arg10: integer, arg11: integer, arg12: integer, arg13: double)
constructor(arg0: $Component$Type, arg1: integer, arg2: long, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: boolean, arg10: integer, arg11: integer, arg12: integer)
constructor(arg0: $Component$Type, arg1: integer, arg2: long, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: boolean, arg8: integer, arg9: integer, arg10: integer)

public "paramString"(): string
public "getUnitsToScroll"(): integer
public "getScrollType"(): integer
public "getScrollAmount"(): integer
public "getWheelRotation"(): integer
public "getPreciseWheelRotation"(): double
get "unitsToScroll"(): integer
get "scrollType"(): integer
get "scrollAmount"(): integer
get "wheelRotation"(): integer
get "preciseWheelRotation"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MouseWheelEvent$Type = ($MouseWheelEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MouseWheelEvent_ = $MouseWheelEvent$Type;
}}
declare module "packages/java/awt/event/$FocusListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$FocusEvent, $FocusEvent$Type} from "packages/java/awt/event/$FocusEvent"

export interface $FocusListener extends $EventListener {

 "focusGained"(arg0: $FocusEvent$Type): void
 "focusLost"(arg0: $FocusEvent$Type): void
}

export namespace $FocusListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FocusListener$Type = ($FocusListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FocusListener_ = $FocusListener$Type;
}}
declare module "packages/java/awt/image/$WritableRaster" {
import {$Raster, $Raster$Type} from "packages/java/awt/image/$Raster"

export class $WritableRaster extends $Raster {


public "setDataElements"(arg0: integer, arg1: integer, arg2: any): void
public "setDataElements"(arg0: integer, arg1: integer, arg2: $Raster$Type): void
public "setDataElements"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: any): void
public "createWritableChild"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: (integer)[]): $WritableRaster
public "setPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (integer)[]): void
public "setPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (double)[]): void
public "setPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (float)[]): void
public "setPixel"(arg0: integer, arg1: integer, arg2: (double)[]): void
public "setPixel"(arg0: integer, arg1: integer, arg2: (float)[]): void
public "setPixel"(arg0: integer, arg1: integer, arg2: (integer)[]): void
public "setRect"(arg0: $Raster$Type): void
public "setRect"(arg0: integer, arg1: integer, arg2: $Raster$Type): void
public "setSample"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "setSample"(arg0: integer, arg1: integer, arg2: integer, arg3: float): void
public "setSample"(arg0: integer, arg1: integer, arg2: integer, arg3: double): void
public "setSamples"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: (float)[]): void
public "setSamples"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: (double)[]): void
public "setSamples"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: (integer)[]): void
public "getWritableParent"(): $WritableRaster
public "createWritableTranslatedChild"(arg0: integer, arg1: integer): $WritableRaster
set "rect"(value: $Raster$Type)
get "writableParent"(): $WritableRaster
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WritableRaster$Type = ($WritableRaster);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WritableRaster_ = $WritableRaster$Type;
}}
declare module "packages/java/awt/event/$WindowListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$WindowEvent, $WindowEvent$Type} from "packages/java/awt/event/$WindowEvent"

export interface $WindowListener extends $EventListener {

 "windowOpened"(arg0: $WindowEvent$Type): void
 "windowClosing"(arg0: $WindowEvent$Type): void
 "windowClosed"(arg0: $WindowEvent$Type): void
 "windowIconified"(arg0: $WindowEvent$Type): void
 "windowDeiconified"(arg0: $WindowEvent$Type): void
 "windowActivated"(arg0: $WindowEvent$Type): void
 "windowDeactivated"(arg0: $WindowEvent$Type): void
}

export namespace $WindowListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WindowListener$Type = ($WindowListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WindowListener_ = $WindowListener$Type;
}}
declare module "packages/java/awt/$GraphicsConfigTemplate" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$GraphicsConfiguration, $GraphicsConfiguration$Type} from "packages/java/awt/$GraphicsConfiguration"

export class $GraphicsConfigTemplate implements $Serializable {
static readonly "REQUIRED": integer
static readonly "PREFERRED": integer
static readonly "UNNECESSARY": integer

constructor()

public "isGraphicsConfigSupported"(arg0: $GraphicsConfiguration$Type): boolean
public "getBestConfiguration"(arg0: ($GraphicsConfiguration$Type)[]): $GraphicsConfiguration
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GraphicsConfigTemplate$Type = ($GraphicsConfigTemplate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GraphicsConfigTemplate_ = $GraphicsConfigTemplate$Type;
}}
declare module "packages/java/awt/dnd/$DragSourceDropEvent" {
import {$DragSourceEvent, $DragSourceEvent$Type} from "packages/java/awt/dnd/$DragSourceEvent"
import {$DragSourceContext, $DragSourceContext$Type} from "packages/java/awt/dnd/$DragSourceContext"

export class $DragSourceDropEvent extends $DragSourceEvent {

constructor(arg0: $DragSourceContext$Type)
constructor(arg0: $DragSourceContext$Type, arg1: integer, arg2: boolean, arg3: integer, arg4: integer)
constructor(arg0: $DragSourceContext$Type, arg1: integer, arg2: boolean)

public "getDropSuccess"(): boolean
public "getDropAction"(): integer
get "dropSuccess"(): boolean
get "dropAction"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DragSourceDropEvent$Type = ($DragSourceDropEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DragSourceDropEvent_ = $DragSourceDropEvent$Type;
}}
declare module "packages/java/awt/$Point" {
import {$Point2D, $Point2D$Type} from "packages/java/awt/geom/$Point2D"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

export class $Point extends $Point2D implements $Serializable {
 "x": integer
 "y": integer

constructor(arg0: integer, arg1: integer)
constructor(arg0: $Point$Type)
constructor()

public "equals"(arg0: any): boolean
public "toString"(): string
public "getLocation"(): $Point
public "move"(arg0: integer, arg1: integer): void
public "getY"(): double
public "translate"(arg0: integer, arg1: integer): void
public "getX"(): double
public "setLocation"(arg0: $Point$Type): void
public "setLocation"(arg0: integer, arg1: integer): void
public "setLocation"(arg0: double, arg1: double): void
get "location"(): $Point
get "y"(): double
get "x"(): double
set "location"(value: $Point$Type)
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
declare module "packages/java/awt/geom/$AffineTransform" {
import {$Point2D, $Point2D$Type} from "packages/java/awt/geom/$Point2D"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Shape, $Shape$Type} from "packages/java/awt/$Shape"

export class $AffineTransform implements $Cloneable, $Serializable {
static readonly "TYPE_IDENTITY": integer
static readonly "TYPE_TRANSLATION": integer
static readonly "TYPE_UNIFORM_SCALE": integer
static readonly "TYPE_GENERAL_SCALE": integer
static readonly "TYPE_MASK_SCALE": integer
static readonly "TYPE_FLIP": integer
static readonly "TYPE_QUADRANT_ROTATION": integer
static readonly "TYPE_GENERAL_ROTATION": integer
static readonly "TYPE_MASK_ROTATION": integer
static readonly "TYPE_GENERAL_TRANSFORM": integer

constructor(arg0: float, arg1: float, arg2: float, arg3: float, arg4: float, arg5: float)
constructor(arg0: (float)[])
constructor(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double)
constructor(arg0: (double)[])
constructor()
constructor(arg0: $AffineTransform$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "scale"(arg0: double, arg1: double): void
public "transform"(arg0: (float)[], arg1: integer, arg2: (double)[], arg3: integer, arg4: integer): void
public "transform"(arg0: (double)[], arg1: integer, arg2: (double)[], arg3: integer, arg4: integer): void
public "transform"(arg0: $Point2D$Type, arg1: $Point2D$Type): $Point2D
public "transform"(arg0: (float)[], arg1: integer, arg2: (float)[], arg3: integer, arg4: integer): void
public "transform"(arg0: ($Point2D$Type)[], arg1: integer, arg2: ($Point2D$Type)[], arg3: integer, arg4: integer): void
public "transform"(arg0: (double)[], arg1: integer, arg2: (float)[], arg3: integer, arg4: integer): void
public "getType"(): integer
public "isIdentity"(): boolean
public "rotate"(arg0: double, arg1: double, arg2: double, arg3: double): void
public "rotate"(arg0: double, arg1: double, arg2: double): void
public "rotate"(arg0: double): void
public "rotate"(arg0: double, arg1: double): void
public "invert"(): void
public "translate"(arg0: double, arg1: double): void
public "getScaleX"(): double
public "getScaleY"(): double
public "getDeterminant"(): double
public "getShearX"(): double
public "getShearY"(): double
public "getTranslateX"(): double
public "concatenate"(arg0: $AffineTransform$Type): void
public "createInverse"(): $AffineTransform
public "setToTranslation"(arg0: double, arg1: double): void
public "setToRotation"(arg0: double): void
public "setToRotation"(arg0: double, arg1: double, arg2: double): void
public "setToRotation"(arg0: double, arg1: double): void
public "setToRotation"(arg0: double, arg1: double, arg2: double, arg3: double): void
public "setToQuadrantRotation"(arg0: integer, arg1: double, arg2: double): void
public "setToQuadrantRotation"(arg0: integer): void
public "setToScale"(arg0: double, arg1: double): void
public "setToShear"(arg0: double, arg1: double): void
public static "getRotateInstance"(arg0: double): $AffineTransform
public static "getRotateInstance"(arg0: double, arg1: double, arg2: double, arg3: double): $AffineTransform
public static "getRotateInstance"(arg0: double, arg1: double, arg2: double): $AffineTransform
public static "getRotateInstance"(arg0: double, arg1: double): $AffineTransform
public static "getQuadrantRotateInstance"(arg0: integer, arg1: double, arg2: double): $AffineTransform
public static "getQuadrantRotateInstance"(arg0: integer): $AffineTransform
public static "getScaleInstance"(arg0: double, arg1: double): $AffineTransform
public static "getShearInstance"(arg0: double, arg1: double): $AffineTransform
public "quadrantRotate"(arg0: integer): void
public "quadrantRotate"(arg0: integer, arg1: double, arg2: double): void
public "setToIdentity"(): void
public "inverseTransform"(arg0: $Point2D$Type, arg1: $Point2D$Type): $Point2D
public "inverseTransform"(arg0: (double)[], arg1: integer, arg2: (double)[], arg3: integer, arg4: integer): void
public "createTransformedShape"(arg0: $Shape$Type): $Shape
public "getMatrix"(arg0: (double)[]): void
public static "getTranslateInstance"(arg0: double, arg1: double): $AffineTransform
public "preConcatenate"(arg0: $AffineTransform$Type): void
public "getTranslateY"(): double
public "deltaTransform"(arg0: (double)[], arg1: integer, arg2: (double)[], arg3: integer, arg4: integer): void
public "deltaTransform"(arg0: $Point2D$Type, arg1: $Point2D$Type): $Point2D
public "setTransform"(arg0: $AffineTransform$Type): void
public "setTransform"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): void
public "shear"(arg0: double, arg1: double): void
get "type"(): integer
get "identity"(): boolean
get "scaleX"(): double
get "scaleY"(): double
get "determinant"(): double
get "shearX"(): double
get "shearY"(): double
get "translateX"(): double
set "toRotation"(value: double)
set "toQuadrantRotation"(value: integer)
get "translateY"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AffineTransform$Type = ($AffineTransform);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AffineTransform_ = $AffineTransform$Type;
}}
declare module "packages/java/awt/$Container" {
import {$ComponentOrientation, $ComponentOrientation$Type} from "packages/java/awt/$ComponentOrientation"
import {$PropertyChangeListener, $PropertyChangeListener$Type} from "packages/java/beans/$PropertyChangeListener"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$LayoutManager, $LayoutManager$Type} from "packages/java/awt/$LayoutManager"
import {$AWTKeyStroke, $AWTKeyStroke$Type} from "packages/java/awt/$AWTKeyStroke"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$Insets, $Insets$Type} from "packages/java/awt/$Insets"
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$Font, $Font$Type} from "packages/java/awt/$Font"
import {$FocusTraversalPolicy, $FocusTraversalPolicy$Type} from "packages/java/awt/$FocusTraversalPolicy"
import {$ContainerListener, $ContainerListener$Type} from "packages/java/awt/event/$ContainerListener"
import {$Component, $Component$Type} from "packages/java/awt/$Component"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Event, $Event$Type} from "packages/java/awt/$Event"
import {$Dimension, $Dimension$Type} from "packages/java/awt/$Dimension"
import {$PrintWriter, $PrintWriter$Type} from "packages/java/io/$PrintWriter"
import {$PrintStream, $PrintStream$Type} from "packages/java/io/$PrintStream"

export class $Container extends $Component {
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float

constructor()

public "add"(arg0: $Component$Type): $Component
public "add"(arg0: string, arg1: $Component$Type): $Component
public "add"(arg0: $Component$Type, arg1: any): void
public "add"(arg0: $Component$Type, arg1: integer): $Component
public "add"(arg0: $Component$Type, arg1: any, arg2: integer): void
public "remove"(arg0: $Component$Type): void
public "remove"(arg0: integer): void
public "update"(arg0: $Graphics$Type): void
public "list"(arg0: $PrintWriter$Type, arg1: integer): void
public "list"(arg0: $PrintStream$Type, arg1: integer): void
public "validate"(): void
public "print"(arg0: $Graphics$Type): void
public "removeAll"(): void
public "invalidate"(): void
public "getListeners"<T extends $EventListener>(arg0: $Class$Type<(T)>): (T)[]
public "getLayout"(): $LayoutManager
public "getComponent"(arg0: integer): $Component
/**
 * 
 * @deprecated
 */
public "insets"(): $Insets
public "setLayout"(arg0: $LayoutManager$Type): void
/**
 * 
 * @deprecated
 */
public "minimumSize"(): $Dimension
public "removeNotify"(): void
public "addNotify"(): void
public "findComponentAt"(arg0: integer, arg1: integer): $Component
public "findComponentAt"(arg0: $Point$Type): $Component
/**
 * 
 * @deprecated
 */
public "countComponents"(): integer
/**
 * 
 * @deprecated
 */
public "preferredSize"(): $Dimension
public "getPreferredSize"(): $Dimension
public "getMinimumSize"(): $Dimension
public "isValidateRoot"(): boolean
public "getInsets"(): $Insets
public "getComponentAt"(arg0: integer, arg1: integer): $Component
public "getComponentAt"(arg0: $Point$Type): $Component
public "getFocusTraversalKeys"(arg0: integer): $Set<($AWTKeyStroke)>
public "isFocusCycleRoot"(arg0: $Container$Type): boolean
public "isFocusCycleRoot"(): boolean
public "getFocusTraversalPolicy"(): $FocusTraversalPolicy
public "getComponentZOrder"(arg0: $Component$Type): integer
public "getComponentCount"(): integer
public "getMousePosition"(arg0: boolean): $Point
public "getMaximumSize"(): $Dimension
public "getAlignmentX"(): float
public "getAlignmentY"(): float
public "doLayout"(): void
/**
 * 
 * @deprecated
 */
public "deliverEvent"(arg0: $Event$Type): void
public "setFocusTraversalKeys"(arg0: integer, arg1: $Set$Type<(any)>): void
public "areFocusTraversalKeysSet"(arg0: integer): boolean
public "applyComponentOrientation"(arg0: $ComponentOrientation$Type): void
public "setFont"(arg0: $Font$Type): void
public "paint"(arg0: $Graphics$Type): void
public "addPropertyChangeListener"(arg0: string, arg1: $PropertyChangeListener$Type): void
public "addPropertyChangeListener"(arg0: $PropertyChangeListener$Type): void
/**
 * 
 * @deprecated
 */
public "layout"(): void
public "getComponents"(): ($Component)[]
/**
 * 
 * @deprecated
 */
public "locate"(arg0: integer, arg1: integer): $Component
public "setFocusTraversalPolicy"(arg0: $FocusTraversalPolicy$Type): void
public "isFocusTraversalPolicyProvider"(): boolean
public "addContainerListener"(arg0: $ContainerListener$Type): void
public "setComponentZOrder"(arg0: $Component$Type, arg1: integer): void
public "paintComponents"(arg0: $Graphics$Type): void
public "printComponents"(arg0: $Graphics$Type): void
public "removeContainerListener"(arg0: $ContainerListener$Type): void
public "getContainerListeners"(): ($ContainerListener)[]
public "isFocusTraversalPolicySet"(): boolean
public "setFocusCycleRoot"(arg0: boolean): void
public "setFocusTraversalPolicyProvider"(arg0: boolean): void
public "transferFocusDownCycle"(): void
public "isAncestorOf"(arg0: $Component$Type): boolean
get "validateRoot"(): boolean
get "focusCycleRoot"(): boolean
get "focusTraversalPolicy"(): $FocusTraversalPolicy
get "componentCount"(): integer
get "maximumSize"(): $Dimension
get "alignmentX"(): float
get "alignmentY"(): float
set "font"(value: $Font$Type)
get "components"(): ($Component)[]
set "focusTraversalPolicy"(value: $FocusTraversalPolicy$Type)
get "focusTraversalPolicyProvider"(): boolean
get "containerListeners"(): ($ContainerListener)[]
get "focusTraversalPolicySet"(): boolean
set "focusCycleRoot"(value: boolean)
set "focusTraversalPolicyProvider"(value: boolean)
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
declare module "packages/java/awt/$Shape" {
import {$Point2D, $Point2D$Type} from "packages/java/awt/geom/$Point2D"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$PathIterator, $PathIterator$Type} from "packages/java/awt/geom/$PathIterator"
import {$Rectangle2D, $Rectangle2D$Type} from "packages/java/awt/geom/$Rectangle2D"
import {$AffineTransform, $AffineTransform$Type} from "packages/java/awt/geom/$AffineTransform"

export interface $Shape {

 "contains"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
 "contains"(arg0: double, arg1: double): boolean
 "contains"(arg0: $Rectangle2D$Type): boolean
 "contains"(arg0: $Point2D$Type): boolean
 "getBounds"(): $Rectangle
 "intersects"(arg0: $Rectangle2D$Type): boolean
 "intersects"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
 "getBounds2D"(): $Rectangle2D
 "getPathIterator"(arg0: $AffineTransform$Type, arg1: double): $PathIterator
 "getPathIterator"(arg0: $AffineTransform$Type): $PathIterator
}

export namespace $Shape {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Shape$Type = ($Shape);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Shape_ = $Shape$Type;
}}
declare module "packages/java/awt/event/$InputMethodListener" {
import {$InputMethodEvent, $InputMethodEvent$Type} from "packages/java/awt/event/$InputMethodEvent"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"

export interface $InputMethodListener extends $EventListener {

 "inputMethodTextChanged"(arg0: $InputMethodEvent$Type): void
 "caretPositionChanged"(arg0: $InputMethodEvent$Type): void
}

export namespace $InputMethodListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputMethodListener$Type = ($InputMethodListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputMethodListener_ = $InputMethodListener$Type;
}}
declare module "packages/java/awt/$SecondaryLoop" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $SecondaryLoop {

 "exit"(): boolean
 "enter"(): boolean
}

export namespace $SecondaryLoop {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SecondaryLoop$Type = ($SecondaryLoop);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SecondaryLoop_ = $SecondaryLoop$Type;
}}
declare module "packages/java/awt/$Component$BaselineResizeBehavior" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Component$BaselineResizeBehavior extends $Enum<($Component$BaselineResizeBehavior)> {
static readonly "CONSTANT_ASCENT": $Component$BaselineResizeBehavior
static readonly "CONSTANT_DESCENT": $Component$BaselineResizeBehavior
static readonly "CENTER_OFFSET": $Component$BaselineResizeBehavior
static readonly "OTHER": $Component$BaselineResizeBehavior


public static "values"(): ($Component$BaselineResizeBehavior)[]
public static "valueOf"(arg0: string): $Component$BaselineResizeBehavior
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Component$BaselineResizeBehavior$Type = (("other") | ("center_offset") | ("constant_descent") | ("constant_ascent")) | ($Component$BaselineResizeBehavior);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Component$BaselineResizeBehavior_ = $Component$BaselineResizeBehavior$Type;
}}
declare module "packages/java/awt/datatransfer/$DataFlavor" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ClassLoader, $ClassLoader$Type} from "packages/java/lang/$ClassLoader"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Externalizable, $Externalizable$Type} from "packages/java/io/$Externalizable"
import {$ObjectOutput, $ObjectOutput$Type} from "packages/java/io/$ObjectOutput"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"
import {$ObjectInput, $ObjectInput$Type} from "packages/java/io/$ObjectInput"
import {$Transferable, $Transferable$Type} from "packages/java/awt/datatransfer/$Transferable"

export class $DataFlavor implements $Externalizable, $Cloneable {
static readonly "stringFlavor": $DataFlavor
static readonly "imageFlavor": $DataFlavor
/**
 * 
 * @deprecated
 */
static readonly "plainTextFlavor": $DataFlavor
static readonly "javaSerializedObjectMimeType": string
static readonly "javaFileListFlavor": $DataFlavor
static readonly "javaJVMLocalObjectMimeType": string
static readonly "javaRemoteObjectMimeType": string
static readonly "selectionHtmlFlavor": $DataFlavor
static readonly "fragmentHtmlFlavor": $DataFlavor
static readonly "allHtmlFlavor": $DataFlavor

constructor()
constructor(arg0: string, arg1: string)
constructor(arg0: $Class$Type<(any)>, arg1: string)
constructor(arg0: string, arg1: string, arg2: $ClassLoader$Type)
constructor(arg0: string)

public "equals"(arg0: any): boolean
/**
 * 
 * @deprecated
 */
public "equals"(arg0: string): boolean
public "equals"(arg0: $DataFlavor$Type): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "match"(arg0: $DataFlavor$Type): boolean
public "writeExternal"(arg0: $ObjectOutput$Type): void
public "readExternal"(arg0: $ObjectInput$Type): void
public "getParameter"(arg0: string): string
public "isRepresentationClassInputStream"(): boolean
public "isRepresentationClassByteBuffer"(): boolean
public "isFlavorTextType"(): boolean
public "isMimeTypeEqual"(arg0: $DataFlavor$Type): boolean
public "isMimeTypeEqual"(arg0: string): boolean
public "isRepresentationClassReader"(): boolean
public "isRepresentationClassCharBuffer"(): boolean
public "getDefaultRepresentationClass"(): $Class<(any)>
public "isRepresentationClassSerializable"(): boolean
public "isRepresentationClassRemote"(): boolean
public static "getTextPlainUnicodeFlavor"(): $DataFlavor
public static "selectBestTextFlavor"(arg0: ($DataFlavor$Type)[]): $DataFlavor
public "getReaderForText"(arg0: $Transferable$Type): $Reader
public "getHumanPresentableName"(): string
public "setHumanPresentableName"(arg0: string): void
public "isMimeTypeSerializedObject"(): boolean
public "getDefaultRepresentationClassAsString"(): string
public "isFlavorSerializedObjectType"(): boolean
public "isFlavorRemoteObjectType"(): boolean
public "isFlavorJavaFileListType"(): boolean
public "getPrimaryType"(): string
public "getMimeType"(): string
public "getSubType"(): string
public "getRepresentationClass"(): $Class<(any)>
get "representationClassInputStream"(): boolean
get "representationClassByteBuffer"(): boolean
get "flavorTextType"(): boolean
get "representationClassReader"(): boolean
get "representationClassCharBuffer"(): boolean
get "defaultRepresentationClass"(): $Class<(any)>
get "representationClassSerializable"(): boolean
get "representationClassRemote"(): boolean
get "textPlainUnicodeFlavor"(): $DataFlavor
get "humanPresentableName"(): string
set "humanPresentableName"(value: string)
get "mimeTypeSerializedObject"(): boolean
get "defaultRepresentationClassAsString"(): string
get "flavorSerializedObjectType"(): boolean
get "flavorRemoteObjectType"(): boolean
get "flavorJavaFileListType"(): boolean
get "primaryType"(): string
get "mimeType"(): string
get "subType"(): string
get "representationClass"(): $Class<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataFlavor$Type = ($DataFlavor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataFlavor_ = $DataFlavor$Type;
}}
declare module "packages/java/awt/geom/$Rectangle2D$Double" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Rectangle2D, $Rectangle2D$Type} from "packages/java/awt/geom/$Rectangle2D"

export class $Rectangle2D$Double extends $Rectangle2D implements $Serializable {
 "x": double
 "y": double
 "width": double
 "height": double
static readonly "OUT_LEFT": integer
static readonly "OUT_TOP": integer
static readonly "OUT_RIGHT": integer
static readonly "OUT_BOTTOM": integer

constructor()
constructor(arg0: double, arg1: double, arg2: double, arg3: double)

public "toString"(): string
public "isEmpty"(): boolean
public "getY"(): double
public "getX"(): double
public "getWidth"(): double
public "getHeight"(): double
public "setRect"(arg0: double, arg1: double, arg2: double, arg3: double): void
public "setRect"(arg0: $Rectangle2D$Type): void
public "getBounds2D"(): $Rectangle2D
public "outcode"(arg0: double, arg1: double): integer
public "createIntersection"(arg0: $Rectangle2D$Type): $Rectangle2D
public "createUnion"(arg0: $Rectangle2D$Type): $Rectangle2D
get "empty"(): boolean
get "y"(): double
get "x"(): double
get "width"(): double
get "height"(): double
set "rect"(value: $Rectangle2D$Type)
get "bounds2D"(): $Rectangle2D
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Rectangle2D$Double$Type = ($Rectangle2D$Double);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Rectangle2D$Double_ = $Rectangle2D$Double$Type;
}}
declare module "packages/java/awt/image/$Raster" {
import {$WritableRaster, $WritableRaster$Type} from "packages/java/awt/image/$WritableRaster"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$SampleModel, $SampleModel$Type} from "packages/java/awt/image/$SampleModel"
import {$DataBuffer, $DataBuffer$Type} from "packages/java/awt/image/$DataBuffer"

export class $Raster {


public "getBounds"(): $Rectangle
public "getParent"(): $Raster
public "getMinX"(): integer
public "getMinY"(): integer
public "getWidth"(): integer
public "getHeight"(): integer
public "getSampleModel"(): $SampleModel
public "createCompatibleWritableRaster"(arg0: $Rectangle$Type): $WritableRaster
public "createCompatibleWritableRaster"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $WritableRaster
public "createCompatibleWritableRaster"(arg0: integer, arg1: integer): $WritableRaster
public "createCompatibleWritableRaster"(): $WritableRaster
public static "createInterleavedRaster"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $Point$Type): $WritableRaster
public static "createInterleavedRaster"(arg0: $DataBuffer$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: (integer)[], arg6: $Point$Type): $WritableRaster
public static "createInterleavedRaster"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: (integer)[], arg6: $Point$Type): $WritableRaster
public static "createPackedRaster"(arg0: integer, arg1: integer, arg2: integer, arg3: (integer)[], arg4: $Point$Type): $WritableRaster
public static "createPackedRaster"(arg0: $DataBuffer$Type, arg1: integer, arg2: integer, arg3: integer, arg4: (integer)[], arg5: $Point$Type): $WritableRaster
public static "createPackedRaster"(arg0: $DataBuffer$Type, arg1: integer, arg2: integer, arg3: integer, arg4: $Point$Type): $WritableRaster
public static "createPackedRaster"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $Point$Type): $WritableRaster
public "getNumBands"(): integer
public "getDataElements"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: any): any
public "getDataElements"(arg0: integer, arg1: integer, arg2: any): any
public "getDataBuffer"(): $DataBuffer
public "getSampleModelTranslateX"(): integer
public "getSampleModelTranslateY"(): integer
public static "createWritableRaster"(arg0: $SampleModel$Type, arg1: $Point$Type): $WritableRaster
public static "createWritableRaster"(arg0: $SampleModel$Type, arg1: $DataBuffer$Type, arg2: $Point$Type): $WritableRaster
public "getPixel"(arg0: integer, arg1: integer, arg2: (float)[]): (float)[]
public "getPixel"(arg0: integer, arg1: integer, arg2: (double)[]): (double)[]
public "getPixel"(arg0: integer, arg1: integer, arg2: (integer)[]): (integer)[]
public "getTransferType"(): integer
public static "createBandedRaster"(arg0: $DataBuffer$Type, arg1: integer, arg2: integer, arg3: integer, arg4: (integer)[], arg5: (integer)[], arg6: $Point$Type): $WritableRaster
public static "createBandedRaster"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (integer)[], arg5: (integer)[], arg6: $Point$Type): $WritableRaster
public static "createBandedRaster"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $Point$Type): $WritableRaster
public "getNumDataElements"(): integer
public "createChild"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: (integer)[]): $Raster
public "getSample"(arg0: integer, arg1: integer, arg2: integer): integer
public "getSampleFloat"(arg0: integer, arg1: integer, arg2: integer): float
public "getSampleDouble"(arg0: integer, arg1: integer, arg2: integer): double
public "getSamples"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: (integer)[]): (integer)[]
public "getSamples"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: (double)[]): (double)[]
public "getSamples"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: (float)[]): (float)[]
public static "createRaster"(arg0: $SampleModel$Type, arg1: $DataBuffer$Type, arg2: $Point$Type): $Raster
public "createTranslatedChild"(arg0: integer, arg1: integer): $Raster
public "getPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (double)[]): (double)[]
public "getPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (integer)[]): (integer)[]
public "getPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (float)[]): (float)[]
get "bounds"(): $Rectangle
get "parent"(): $Raster
get "minX"(): integer
get "minY"(): integer
get "width"(): integer
get "height"(): integer
get "sampleModel"(): $SampleModel
get "numBands"(): integer
get "dataBuffer"(): $DataBuffer
get "sampleModelTranslateX"(): integer
get "sampleModelTranslateY"(): integer
get "transferType"(): integer
get "numDataElements"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Raster$Type = ($Raster);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Raster_ = $Raster$Type;
}}
declare module "packages/java/awt/print/$PageFormat" {
import {$Paper, $Paper$Type} from "packages/java/awt/print/$Paper"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"

export class $PageFormat implements $Cloneable {
static readonly "LANDSCAPE": integer
static readonly "PORTRAIT": integer
static readonly "REVERSE_LANDSCAPE": integer

constructor()

public "clone"(): any
public "getImageableY"(): double
public "getImageableHeight"(): double
public "getImageableX"(): double
public "getImageableWidth"(): double
public "getPaper"(): $Paper
public "setPaper"(arg0: $Paper$Type): void
public "setOrientation"(arg0: integer): void
public "getWidth"(): double
public "getHeight"(): double
public "getMatrix"(): (double)[]
public "getOrientation"(): integer
get "imageableY"(): double
get "imageableHeight"(): double
get "imageableX"(): double
get "imageableWidth"(): double
get "paper"(): $Paper
set "paper"(value: $Paper$Type)
set "orientation"(value: integer)
get "width"(): double
get "height"(): double
get "matrix"(): (double)[]
get "orientation"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageFormat$Type = ($PageFormat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageFormat_ = $PageFormat$Type;
}}
declare module "packages/java/awt/font/$LineMetrics" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $LineMetrics {


public "getNumChars"(): integer
public "getBaselineIndex"(): integer
public "getBaselineOffsets"(): (float)[]
public "getHeight"(): float
public "getUnderlineOffset"(): float
public "getStrikethroughOffset"(): float
public "getStrikethroughThickness"(): float
public "getUnderlineThickness"(): float
public "getAscent"(): float
public "getDescent"(): float
public "getLeading"(): float
get "numChars"(): integer
get "baselineIndex"(): integer
get "baselineOffsets"(): (float)[]
get "height"(): float
get "underlineOffset"(): float
get "strikethroughOffset"(): float
get "strikethroughThickness"(): float
get "underlineThickness"(): float
get "ascent"(): float
get "descent"(): float
get "leading"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LineMetrics$Type = ($LineMetrics);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LineMetrics_ = $LineMetrics$Type;
}}
declare module "packages/java/awt/event/$AdjustmentListener" {
import {$AdjustmentEvent, $AdjustmentEvent$Type} from "packages/java/awt/event/$AdjustmentEvent"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"

export interface $AdjustmentListener extends $EventListener {

 "adjustmentValueChanged"(arg0: $AdjustmentEvent$Type): void

(arg0: $AdjustmentEvent$Type): void
}

export namespace $AdjustmentListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdjustmentListener$Type = ($AdjustmentListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdjustmentListener_ = $AdjustmentListener$Type;
}}
declare module "packages/java/awt/print/$Paper" {
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"

export class $Paper implements $Cloneable {

constructor()

public "clone"(): any
public "setSize"(arg0: double, arg1: double): void
public "getImageableY"(): double
public "getImageableHeight"(): double
public "getImageableX"(): double
public "getImageableWidth"(): double
public "setImageableArea"(arg0: double, arg1: double, arg2: double, arg3: double): void
public "getWidth"(): double
public "getHeight"(): double
get "imageableY"(): double
get "imageableHeight"(): double
get "imageableX"(): double
get "imageableWidth"(): double
get "width"(): double
get "height"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Paper$Type = ($Paper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Paper_ = $Paper$Type;
}}
declare module "packages/java/awt/image/$BufferedImage" {
import {$WritableRaster, $WritableRaster$Type} from "packages/java/awt/image/$WritableRaster"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$Hashtable, $Hashtable$Type} from "packages/java/util/$Hashtable"
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$ImageObserver, $ImageObserver$Type} from "packages/java/awt/image/$ImageObserver"
import {$ImageProducer, $ImageProducer$Type} from "packages/java/awt/image/$ImageProducer"
import {$RenderedImage, $RenderedImage$Type} from "packages/java/awt/image/$RenderedImage"
import {$Vector, $Vector$Type} from "packages/java/util/$Vector"
import {$WritableRenderedImage, $WritableRenderedImage$Type} from "packages/java/awt/image/$WritableRenderedImage"
import {$IndexColorModel, $IndexColorModel$Type} from "packages/java/awt/image/$IndexColorModel"
import {$Raster, $Raster$Type} from "packages/java/awt/image/$Raster"
import {$TileObserver, $TileObserver$Type} from "packages/java/awt/image/$TileObserver"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$SampleModel, $SampleModel$Type} from "packages/java/awt/image/$SampleModel"
import {$ColorModel, $ColorModel$Type} from "packages/java/awt/image/$ColorModel"
import {$Transparency, $Transparency$Type} from "packages/java/awt/$Transparency"
import {$Graphics2D, $Graphics2D$Type} from "packages/java/awt/$Graphics2D"
import {$Image, $Image$Type} from "packages/java/awt/$Image"

export class $BufferedImage extends $Image implements $WritableRenderedImage, $Transparency {
static readonly "TYPE_CUSTOM": integer
static readonly "TYPE_INT_RGB": integer
static readonly "TYPE_INT_ARGB": integer
static readonly "TYPE_INT_ARGB_PRE": integer
static readonly "TYPE_INT_BGR": integer
static readonly "TYPE_3BYTE_BGR": integer
static readonly "TYPE_4BYTE_ABGR": integer
static readonly "TYPE_4BYTE_ABGR_PRE": integer
static readonly "TYPE_USHORT_565_RGB": integer
static readonly "TYPE_USHORT_555_RGB": integer
static readonly "TYPE_BYTE_GRAY": integer
static readonly "TYPE_USHORT_GRAY": integer
static readonly "TYPE_BYTE_BINARY": integer
static readonly "TYPE_BYTE_INDEXED": integer
static readonly "UndefinedProperty": any
static readonly "SCALE_DEFAULT": integer
static readonly "SCALE_FAST": integer
static readonly "SCALE_SMOOTH": integer
static readonly "SCALE_REPLICATE": integer
static readonly "SCALE_AREA_AVERAGING": integer

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: $IndexColorModel$Type)
constructor(arg0: integer, arg1: integer, arg2: integer)
constructor(arg0: $ColorModel$Type, arg1: $WritableRaster$Type, arg2: boolean, arg3: $Hashtable$Type<(any), (any)>)

public "getProperty"(arg0: string): any
public "getProperty"(arg0: string, arg1: $ImageObserver$Type): any
public "toString"(): string
public "getType"(): integer
public "getSource"(): $ImageProducer
public "getData"(): $Raster
public "getData"(arg0: $Rectangle$Type): $Raster
public "copyData"(arg0: $WritableRaster$Type): $WritableRaster
public "getMinX"(): integer
public "getMinY"(): integer
public "getGraphics"(): $Graphics
public "setData"(arg0: $Raster$Type): void
public "getPropertyNames"(): (string)[]
public "getWidth"(): integer
public "getWidth"(arg0: $ImageObserver$Type): integer
public "getHeight"(arg0: $ImageObserver$Type): integer
public "getHeight"(): integer
public "getColorModel"(): $ColorModel
public "getSampleModel"(): $SampleModel
public "getNumXTiles"(): integer
public "getNumYTiles"(): integer
public "getMinTileX"(): integer
public "getMinTileY"(): integer
public "getTileWidth"(): integer
public "getTileHeight"(): integer
public "getTileGridXOffset"(): integer
public "getTileGridYOffset"(): integer
public "createGraphics"(): $Graphics2D
public "isAlphaPremultiplied"(): boolean
public "coerceData"(arg0: boolean): void
public "getAlphaRaster"(): $WritableRaster
public "setRGB"(arg0: integer, arg1: integer, arg2: integer): void
public "setRGB"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (integer)[], arg5: integer, arg6: integer): void
public "getTile"(arg0: integer, arg1: integer): $Raster
public "getRGB"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (integer)[], arg5: integer, arg6: integer): (integer)[]
public "getRGB"(arg0: integer, arg1: integer): integer
public "getTransparency"(): integer
public "getSources"(): $Vector<($RenderedImage)>
public "getRaster"(): $WritableRaster
public "getSubimage"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $BufferedImage
public "addTileObserver"(arg0: $TileObserver$Type): void
public "removeTileObserver"(arg0: $TileObserver$Type): void
public "isTileWritable"(arg0: integer, arg1: integer): boolean
public "getWritableTileIndices"(): ($Point)[]
public "hasTileWriters"(): boolean
public "getWritableTile"(arg0: integer, arg1: integer): $WritableRaster
public "releaseWritableTile"(arg0: integer, arg1: integer): void
get "type"(): integer
get "source"(): $ImageProducer
get "data"(): $Raster
get "minX"(): integer
get "minY"(): integer
get "graphics"(): $Graphics
set "data"(value: $Raster$Type)
get "propertyNames"(): (string)[]
get "width"(): integer
get "height"(): integer
get "colorModel"(): $ColorModel
get "sampleModel"(): $SampleModel
get "numXTiles"(): integer
get "numYTiles"(): integer
get "minTileX"(): integer
get "minTileY"(): integer
get "tileWidth"(): integer
get "tileHeight"(): integer
get "tileGridXOffset"(): integer
get "tileGridYOffset"(): integer
get "alphaPremultiplied"(): boolean
get "alphaRaster"(): $WritableRaster
get "transparency"(): integer
get "sources"(): $Vector<($RenderedImage)>
get "raster"(): $WritableRaster
get "writableTileIndices"(): ($Point)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BufferedImage$Type = ($BufferedImage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BufferedImage_ = $BufferedImage$Type;
}}
declare module "packages/java/awt/$Dimension" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Dimension2D, $Dimension2D$Type} from "packages/java/awt/geom/$Dimension2D"

export class $Dimension extends $Dimension2D implements $Serializable {
 "width": integer
 "height": integer

constructor(arg0: $Dimension$Type)
constructor()
constructor(arg0: integer, arg1: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getSize"(): $Dimension
public "setSize"(arg0: $Dimension$Type): void
public "setSize"(arg0: double, arg1: double): void
public "setSize"(arg0: integer, arg1: integer): void
public "getWidth"(): double
public "getHeight"(): double
get "size"(): $Dimension
set "size"(value: $Dimension$Type)
get "width"(): double
get "height"(): double
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
declare module "packages/java/awt/$Graphics" {
import {$Color, $Color$Type} from "packages/java/awt/$Color"
import {$Polygon, $Polygon$Type} from "packages/java/awt/$Polygon"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$AttributedCharacterIterator, $AttributedCharacterIterator$Type} from "packages/java/text/$AttributedCharacterIterator"
import {$FontMetrics, $FontMetrics$Type} from "packages/java/awt/$FontMetrics"
import {$ImageObserver, $ImageObserver$Type} from "packages/java/awt/image/$ImageObserver"
import {$Font, $Font$Type} from "packages/java/awt/$Font"
import {$Shape, $Shape$Type} from "packages/java/awt/$Shape"
import {$Image, $Image$Type} from "packages/java/awt/$Image"

export class $Graphics {


/**
 * 
 * @deprecated
 */
public "finalize"(): void
public "toString"(): string
public "create"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $Graphics
public "create"(): $Graphics
public "setColor"(arg0: $Color$Type): void
public "dispose"(): void
public "drawRect"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "drawString"(arg0: $AttributedCharacterIterator$Type, arg1: integer, arg2: integer): void
public "drawString"(arg0: string, arg1: integer, arg2: integer): void
public "getClip"(): $Shape
public "translate"(arg0: integer, arg1: integer): void
public "fillRect"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "drawImage"(arg0: $Image$Type, arg1: integer, arg2: integer, arg3: $Color$Type, arg4: $ImageObserver$Type): boolean
public "drawImage"(arg0: $Image$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $ImageObserver$Type): boolean
public "drawImage"(arg0: $Image$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $Color$Type, arg6: $ImageObserver$Type): boolean
public "drawImage"(arg0: $Image$Type, arg1: integer, arg2: integer, arg3: $ImageObserver$Type): boolean
public "drawImage"(arg0: $Image$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: $Color$Type, arg10: $ImageObserver$Type): boolean
public "drawImage"(arg0: $Image$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: $ImageObserver$Type): boolean
public "copyArea"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public "drawLine"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "drawRoundRect"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public "fillRoundRect"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public "drawOval"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "fillOval"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "drawArc"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public "fillArc"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
public "drawPolyline"(arg0: (integer)[], arg1: (integer)[], arg2: integer): void
public "drawPolygon"(arg0: (integer)[], arg1: (integer)[], arg2: integer): void
public "drawPolygon"(arg0: $Polygon$Type): void
public "fillPolygon"(arg0: (integer)[], arg1: (integer)[], arg2: integer): void
public "fillPolygon"(arg0: $Polygon$Type): void
public "drawChars"(arg0: (character)[], arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public "clearRect"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "setClip"(arg0: $Shape$Type): void
public "setClip"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "getColor"(): $Color
public "getFont"(): $Font
public "setFont"(arg0: $Font$Type): void
public "clipRect"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "getFontMetrics"(): $FontMetrics
public "getFontMetrics"(arg0: $Font$Type): $FontMetrics
public "getClipBounds"(arg0: $Rectangle$Type): $Rectangle
public "getClipBounds"(): $Rectangle
public "hitClip"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): boolean
public "setPaintMode"(): void
public "setXORMode"(arg0: $Color$Type): void
public "drawBytes"(arg0: (byte)[], arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public "draw3DRect"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: boolean): void
public "fill3DRect"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: boolean): void
/**
 * 
 * @deprecated
 */
public "getClipRect"(): $Rectangle
set "color"(value: $Color$Type)
get "clip"(): $Shape
set "clip"(value: $Shape$Type)
get "color"(): $Color
get "font"(): $Font
set "font"(value: $Font$Type)
get "fontMetrics"(): $FontMetrics
get "clipBounds"(): $Rectangle
set "xORMode"(value: $Color$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Graphics$Type = ($Graphics);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Graphics_ = $Graphics$Type;
}}
declare module "packages/java/awt/$MenuItem" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$MenuShortcut, $MenuShortcut$Type} from "packages/java/awt/$MenuShortcut"
import {$ActionListener, $ActionListener$Type} from "packages/java/awt/event/$ActionListener"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$MenuComponent, $MenuComponent$Type} from "packages/java/awt/$MenuComponent"

export class $MenuItem extends $MenuComponent implements $Accessible {

constructor(arg0: string, arg1: $MenuShortcut$Type)
constructor(arg0: string)
constructor()

public "paramString"(): string
/**
 * 
 * @deprecated
 */
public "enable"(arg0: boolean): void
/**
 * 
 * @deprecated
 */
public "enable"(): void
public "isEnabled"(): boolean
public "addActionListener"(arg0: $ActionListener$Type): void
public "removeActionListener"(arg0: $ActionListener$Type): void
public "getActionListeners"(): ($ActionListener)[]
public "setActionCommand"(arg0: string): void
public "setEnabled"(arg0: boolean): void
public "getListeners"<T extends $EventListener>(arg0: $Class$Type<(T)>): (T)[]
public "addNotify"(): void
public "setLabel"(arg0: string): void
public "getAccessibleContext"(): $AccessibleContext
public "getActionCommand"(): string
/**
 * 
 * @deprecated
 */
public "disable"(): void
public "deleteShortcut"(): void
public "getShortcut"(): $MenuShortcut
public "setShortcut"(arg0: $MenuShortcut$Type): void
public "getLabel"(): string
get "enabled"(): boolean
get "actionListeners"(): ($ActionListener)[]
set "actionCommand"(value: string)
set "enabled"(value: boolean)
set "label"(value: string)
get "accessibleContext"(): $AccessibleContext
get "actionCommand"(): string
get "shortcut"(): $MenuShortcut
set "shortcut"(value: $MenuShortcut$Type)
get "label"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuItem$Type = ($MenuItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuItem_ = $MenuItem$Type;
}}
declare module "packages/java/awt/event/$ContainerEvent" {
import {$Container, $Container$Type} from "packages/java/awt/$Container"
import {$Component, $Component$Type} from "packages/java/awt/$Component"
import {$ComponentEvent, $ComponentEvent$Type} from "packages/java/awt/event/$ComponentEvent"

export class $ContainerEvent extends $ComponentEvent {
static readonly "CONTAINER_FIRST": integer
static readonly "CONTAINER_LAST": integer
static readonly "COMPONENT_ADDED": integer
static readonly "COMPONENT_REMOVED": integer
static readonly "COMPONENT_FIRST": integer
static readonly "COMPONENT_LAST": integer
static readonly "COMPONENT_MOVED": integer
static readonly "COMPONENT_RESIZED": integer
static readonly "COMPONENT_SHOWN": integer
static readonly "COMPONENT_HIDDEN": integer
static readonly "COMPONENT_EVENT_MASK": long
static readonly "CONTAINER_EVENT_MASK": long
static readonly "FOCUS_EVENT_MASK": long
static readonly "KEY_EVENT_MASK": long
static readonly "MOUSE_EVENT_MASK": long
static readonly "MOUSE_MOTION_EVENT_MASK": long
static readonly "WINDOW_EVENT_MASK": long
static readonly "ACTION_EVENT_MASK": long
static readonly "ADJUSTMENT_EVENT_MASK": long
static readonly "ITEM_EVENT_MASK": long
static readonly "TEXT_EVENT_MASK": long
static readonly "INPUT_METHOD_EVENT_MASK": long
static readonly "PAINT_EVENT_MASK": long
static readonly "INVOCATION_EVENT_MASK": long
static readonly "HIERARCHY_EVENT_MASK": long
static readonly "HIERARCHY_BOUNDS_EVENT_MASK": long
static readonly "MOUSE_WHEEL_EVENT_MASK": long
static readonly "WINDOW_STATE_EVENT_MASK": long
static readonly "WINDOW_FOCUS_EVENT_MASK": long
static readonly "RESERVED_ID_MAX": integer

constructor(arg0: $Component$Type, arg1: integer, arg2: $Component$Type)

public "paramString"(): string
public "getChild"(): $Component
public "getContainer"(): $Container
get "child"(): $Component
get "container"(): $Container
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContainerEvent$Type = ($ContainerEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContainerEvent_ = $ContainerEvent$Type;
}}
declare module "packages/java/awt/image/$RenderedImage" {
import {$WritableRaster, $WritableRaster$Type} from "packages/java/awt/image/$WritableRaster"
import {$Vector, $Vector$Type} from "packages/java/util/$Vector"
import {$Raster, $Raster$Type} from "packages/java/awt/image/$Raster"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$SampleModel, $SampleModel$Type} from "packages/java/awt/image/$SampleModel"
import {$ColorModel, $ColorModel$Type} from "packages/java/awt/image/$ColorModel"

export interface $RenderedImage {

 "getProperty"(arg0: string): any
 "getData"(arg0: $Rectangle$Type): $Raster
 "getData"(): $Raster
 "copyData"(arg0: $WritableRaster$Type): $WritableRaster
 "getMinX"(): integer
 "getMinY"(): integer
 "getPropertyNames"(): (string)[]
 "getWidth"(): integer
 "getHeight"(): integer
 "getColorModel"(): $ColorModel
 "getSampleModel"(): $SampleModel
 "getNumXTiles"(): integer
 "getNumYTiles"(): integer
 "getMinTileX"(): integer
 "getMinTileY"(): integer
 "getTileWidth"(): integer
 "getTileHeight"(): integer
 "getTileGridXOffset"(): integer
 "getTileGridYOffset"(): integer
 "getTile"(arg0: integer, arg1: integer): $Raster
 "getSources"(): $Vector<($RenderedImage)>
}

export namespace $RenderedImage {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderedImage$Type = ($RenderedImage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderedImage_ = $RenderedImage$Type;
}}
declare module "packages/java/awt/$MenuShortcut" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

export class $MenuShortcut implements $Serializable {

constructor(arg0: integer)
constructor(arg0: integer, arg1: boolean)

public "equals"(arg0: $MenuShortcut$Type): boolean
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getKey"(): integer
public "usesShiftModifier"(): boolean
get "key"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuShortcut$Type = ($MenuShortcut);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuShortcut_ = $MenuShortcut$Type;
}}
declare module "packages/java/awt/font/$FontRenderContext" {
import {$AffineTransform, $AffineTransform$Type} from "packages/java/awt/geom/$AffineTransform"

export class $FontRenderContext {

constructor(arg0: $AffineTransform$Type, arg1: any, arg2: any)
constructor(arg0: $AffineTransform$Type, arg1: boolean, arg2: boolean)

public "equals"(arg0: any): boolean
public "equals"(arg0: $FontRenderContext$Type): boolean
public "hashCode"(): integer
public "isAntiAliased"(): boolean
public "usesFractionalMetrics"(): boolean
public "getTransformType"(): integer
public "getTransform"(): $AffineTransform
public "getAntiAliasingHint"(): any
public "getFractionalMetricsHint"(): any
public "isTransformed"(): boolean
get "antiAliased"(): boolean
get "transformType"(): integer
get "transform"(): $AffineTransform
get "antiAliasingHint"(): any
get "fractionalMetricsHint"(): any
get "transformed"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FontRenderContext$Type = ($FontRenderContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FontRenderContext_ = $FontRenderContext$Type;
}}
declare module "packages/java/awt/event/$InputMethodEvent" {
import {$AWTEvent, $AWTEvent$Type} from "packages/java/awt/$AWTEvent"
import {$AttributedCharacterIterator, $AttributedCharacterIterator$Type} from "packages/java/text/$AttributedCharacterIterator"
import {$TextHitInfo, $TextHitInfo$Type} from "packages/java/awt/font/$TextHitInfo"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export class $InputMethodEvent extends $AWTEvent {
static readonly "INPUT_METHOD_FIRST": integer
static readonly "INPUT_METHOD_TEXT_CHANGED": integer
static readonly "CARET_POSITION_CHANGED": integer
static readonly "INPUT_METHOD_LAST": integer
static readonly "COMPONENT_EVENT_MASK": long
static readonly "CONTAINER_EVENT_MASK": long
static readonly "FOCUS_EVENT_MASK": long
static readonly "KEY_EVENT_MASK": long
static readonly "MOUSE_EVENT_MASK": long
static readonly "MOUSE_MOTION_EVENT_MASK": long
static readonly "WINDOW_EVENT_MASK": long
static readonly "ACTION_EVENT_MASK": long
static readonly "ADJUSTMENT_EVENT_MASK": long
static readonly "ITEM_EVENT_MASK": long
static readonly "TEXT_EVENT_MASK": long
static readonly "INPUT_METHOD_EVENT_MASK": long
static readonly "PAINT_EVENT_MASK": long
static readonly "INVOCATION_EVENT_MASK": long
static readonly "HIERARCHY_EVENT_MASK": long
static readonly "HIERARCHY_BOUNDS_EVENT_MASK": long
static readonly "MOUSE_WHEEL_EVENT_MASK": long
static readonly "WINDOW_STATE_EVENT_MASK": long
static readonly "WINDOW_FOCUS_EVENT_MASK": long
static readonly "RESERVED_ID_MAX": integer

constructor(arg0: $Component$Type, arg1: integer, arg2: $TextHitInfo$Type, arg3: $TextHitInfo$Type)
constructor(arg0: $Component$Type, arg1: integer, arg2: $AttributedCharacterIterator$Type, arg3: integer, arg4: $TextHitInfo$Type, arg5: $TextHitInfo$Type)
constructor(arg0: $Component$Type, arg1: integer, arg2: long, arg3: $AttributedCharacterIterator$Type, arg4: integer, arg5: $TextHitInfo$Type, arg6: $TextHitInfo$Type)

public "paramString"(): string
public "getText"(): $AttributedCharacterIterator
public "consume"(): void
public "getVisiblePosition"(): $TextHitInfo
public "isConsumed"(): boolean
public "getCommittedCharacterCount"(): integer
public "getCaret"(): $TextHitInfo
public "getWhen"(): long
get "text"(): $AttributedCharacterIterator
get "visiblePosition"(): $TextHitInfo
get "consumed"(): boolean
get "committedCharacterCount"(): integer
get "caret"(): $TextHitInfo
get "when"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputMethodEvent$Type = ($InputMethodEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputMethodEvent_ = $InputMethodEvent$Type;
}}
declare module "packages/java/awt/$PaintContext" {
import {$Raster, $Raster$Type} from "packages/java/awt/image/$Raster"
import {$ColorModel, $ColorModel$Type} from "packages/java/awt/image/$ColorModel"

export interface $PaintContext {

 "dispose"(): void
 "getColorModel"(): $ColorModel
 "getRaster"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): $Raster
}

export namespace $PaintContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PaintContext$Type = ($PaintContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PaintContext_ = $PaintContext$Type;
}}
declare module "packages/java/awt/$MenuContainer" {
import {$Event, $Event$Type} from "packages/java/awt/$Event"
import {$MenuComponent, $MenuComponent$Type} from "packages/java/awt/$MenuComponent"
import {$Font, $Font$Type} from "packages/java/awt/$Font"

export interface $MenuContainer {

 "remove"(arg0: $MenuComponent$Type): void
/**
 * 
 * @deprecated
 */
 "postEvent"(arg0: $Event$Type): boolean
 "getFont"(): $Font
}

export namespace $MenuContainer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuContainer$Type = ($MenuContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuContainer_ = $MenuContainer$Type;
}}
declare module "packages/java/awt/$AWTEvent" {
import {$Event, $Event$Type} from "packages/java/awt/$Event"
import {$EventObject, $EventObject$Type} from "packages/java/util/$EventObject"

export class $AWTEvent extends $EventObject {
static readonly "COMPONENT_EVENT_MASK": long
static readonly "CONTAINER_EVENT_MASK": long
static readonly "FOCUS_EVENT_MASK": long
static readonly "KEY_EVENT_MASK": long
static readonly "MOUSE_EVENT_MASK": long
static readonly "MOUSE_MOTION_EVENT_MASK": long
static readonly "WINDOW_EVENT_MASK": long
static readonly "ACTION_EVENT_MASK": long
static readonly "ADJUSTMENT_EVENT_MASK": long
static readonly "ITEM_EVENT_MASK": long
static readonly "TEXT_EVENT_MASK": long
static readonly "INPUT_METHOD_EVENT_MASK": long
static readonly "PAINT_EVENT_MASK": long
static readonly "INVOCATION_EVENT_MASK": long
static readonly "HIERARCHY_EVENT_MASK": long
static readonly "HIERARCHY_BOUNDS_EVENT_MASK": long
static readonly "MOUSE_WHEEL_EVENT_MASK": long
static readonly "WINDOW_STATE_EVENT_MASK": long
static readonly "WINDOW_FOCUS_EVENT_MASK": long
static readonly "RESERVED_ID_MAX": integer

constructor(arg0: any, arg1: integer)
/**
 * 
 * @deprecated
 */
constructor(arg0: $Event$Type)

public "toString"(): string
public "paramString"(): string
public "getID"(): integer
public "setSource"(arg0: any): void
get "iD"(): integer
set "source"(value: any)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AWTEvent$Type = ($AWTEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AWTEvent_ = $AWTEvent$Type;
}}
declare module "packages/java/awt/datatransfer/$Transferable" {
import {$DataFlavor, $DataFlavor$Type} from "packages/java/awt/datatransfer/$DataFlavor"

export interface $Transferable {

 "isDataFlavorSupported"(arg0: $DataFlavor$Type): boolean
 "getTransferDataFlavors"(): ($DataFlavor)[]
 "getTransferData"(arg0: $DataFlavor$Type): any
}

export namespace $Transferable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Transferable$Type = ($Transferable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Transferable_ = $Transferable$Type;
}}
declare module "packages/java/awt/$Dialog$ModalExclusionType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Dialog$ModalExclusionType extends $Enum<($Dialog$ModalExclusionType)> {
static readonly "NO_EXCLUDE": $Dialog$ModalExclusionType
static readonly "APPLICATION_EXCLUDE": $Dialog$ModalExclusionType
static readonly "TOOLKIT_EXCLUDE": $Dialog$ModalExclusionType


public static "values"(): ($Dialog$ModalExclusionType)[]
public static "valueOf"(arg0: string): $Dialog$ModalExclusionType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Dialog$ModalExclusionType$Type = (("no_exclude") | ("toolkit_exclude") | ("application_exclude")) | ($Dialog$ModalExclusionType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Dialog$ModalExclusionType_ = $Dialog$ModalExclusionType$Type;
}}
declare module "packages/java/awt/$Color" {
import {$RenderingHints, $RenderingHints$Type} from "packages/java/awt/$RenderingHints"
import {$ColorSpace, $ColorSpace$Type} from "packages/java/awt/color/$ColorSpace"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$ColorModel, $ColorModel$Type} from "packages/java/awt/image/$ColorModel"
import {$Rectangle2D, $Rectangle2D$Type} from "packages/java/awt/geom/$Rectangle2D"
import {$Paint, $Paint$Type} from "packages/java/awt/$Paint"
import {$PaintContext, $PaintContext$Type} from "packages/java/awt/$PaintContext"
import {$AffineTransform, $AffineTransform$Type} from "packages/java/awt/geom/$AffineTransform"

export class $Color implements $Paint, $Serializable {
static readonly "white": $Color
static readonly "WHITE": $Color
static readonly "lightGray": $Color
static readonly "LIGHT_GRAY": $Color
static readonly "gray": $Color
static readonly "GRAY": $Color
static readonly "darkGray": $Color
static readonly "DARK_GRAY": $Color
static readonly "black": $Color
static readonly "BLACK": $Color
static readonly "red": $Color
static readonly "RED": $Color
static readonly "pink": $Color
static readonly "PINK": $Color
static readonly "orange": $Color
static readonly "ORANGE": $Color
static readonly "yellow": $Color
static readonly "YELLOW": $Color
static readonly "green": $Color
static readonly "GREEN": $Color
static readonly "magenta": $Color
static readonly "MAGENTA": $Color
static readonly "cyan": $Color
static readonly "CYAN": $Color
static readonly "blue": $Color
static readonly "BLUE": $Color

constructor(arg0: float, arg1: float, arg2: float, arg3: float)
constructor(arg0: integer)
constructor(arg0: integer, arg1: boolean)
constructor(arg0: float, arg1: float, arg2: float)
constructor(arg0: integer, arg1: integer, arg2: integer)
constructor(arg0: $ColorSpace$Type, arg1: (float)[], arg2: float)
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "decode"(arg0: string): $Color
public "getRGB"(): integer
public "getRed"(): integer
public "getGreen"(): integer
public "getBlue"(): integer
public "getAlpha"(): integer
public static "getColor"(arg0: string, arg1: $Color$Type): $Color
public static "getColor"(arg0: string): $Color
public static "getColor"(arg0: string, arg1: integer): $Color
public static "HSBtoRGB"(arg0: float, arg1: float, arg2: float): integer
public "getRGBComponents"(arg0: (float)[]): (float)[]
public "getRGBColorComponents"(arg0: (float)[]): (float)[]
public "brighter"(): $Color
public "darker"(): $Color
public static "RGBtoHSB"(arg0: integer, arg1: integer, arg2: integer, arg3: (float)[]): (float)[]
public static "getHSBColor"(arg0: float, arg1: float, arg2: float): $Color
public "getColorComponents"(arg0: $ColorSpace$Type, arg1: (float)[]): (float)[]
public "getColorComponents"(arg0: (float)[]): (float)[]
public "getColorSpace"(): $ColorSpace
public "getTransparency"(): integer
public "createContext"(arg0: $ColorModel$Type, arg1: $Rectangle$Type, arg2: $Rectangle2D$Type, arg3: $AffineTransform$Type, arg4: $RenderingHints$Type): $PaintContext
public "getComponents"(arg0: (float)[]): (float)[]
public "getComponents"(arg0: $ColorSpace$Type, arg1: (float)[]): (float)[]
get "rGB"(): integer
get "red"(): integer
get "green"(): integer
get "blue"(): integer
get "alpha"(): integer
get "colorSpace"(): $ColorSpace
get "transparency"(): integer
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
declare module "packages/java/awt/$FontMetrics" {
import {$CharacterIterator, $CharacterIterator$Type} from "packages/java/text/$CharacterIterator"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$LineMetrics, $LineMetrics$Type} from "packages/java/awt/font/$LineMetrics"
import {$Rectangle2D, $Rectangle2D$Type} from "packages/java/awt/geom/$Rectangle2D"
import {$Font, $Font$Type} from "packages/java/awt/$Font"
import {$FontRenderContext, $FontRenderContext$Type} from "packages/java/awt/font/$FontRenderContext"

export class $FontMetrics implements $Serializable {


public "toString"(): string
public "getMaxCharBounds"(arg0: $Graphics$Type): $Rectangle2D
public "getHeight"(): integer
public "getMaxAdvance"(): integer
public "stringWidth"(arg0: string): integer
public "getFont"(): $Font
public "charWidth"(arg0: character): integer
public "charWidth"(arg0: integer): integer
public "charsWidth"(arg0: (character)[], arg1: integer, arg2: integer): integer
public "getWidths"(): (integer)[]
public "getMaxDescent"(): integer
public "getMaxAscent"(): integer
/**
 * 
 * @deprecated
 */
public "getMaxDecent"(): integer
public "bytesWidth"(arg0: (byte)[], arg1: integer, arg2: integer): integer
public "getFontRenderContext"(): $FontRenderContext
public "getStringBounds"(arg0: string, arg1: integer, arg2: integer, arg3: $Graphics$Type): $Rectangle2D
public "getStringBounds"(arg0: string, arg1: $Graphics$Type): $Rectangle2D
public "getStringBounds"(arg0: (character)[], arg1: integer, arg2: integer, arg3: $Graphics$Type): $Rectangle2D
public "getStringBounds"(arg0: $CharacterIterator$Type, arg1: integer, arg2: integer, arg3: $Graphics$Type): $Rectangle2D
public "getAscent"(): integer
public "getDescent"(): integer
public "getLeading"(): integer
public "hasUniformLineMetrics"(): boolean
public "getLineMetrics"(arg0: $CharacterIterator$Type, arg1: integer, arg2: integer, arg3: $Graphics$Type): $LineMetrics
public "getLineMetrics"(arg0: (character)[], arg1: integer, arg2: integer, arg3: $Graphics$Type): $LineMetrics
public "getLineMetrics"(arg0: string, arg1: integer, arg2: integer, arg3: $Graphics$Type): $LineMetrics
public "getLineMetrics"(arg0: string, arg1: $Graphics$Type): $LineMetrics
get "height"(): integer
get "maxAdvance"(): integer
get "font"(): $Font
get "widths"(): (integer)[]
get "maxDescent"(): integer
get "maxAscent"(): integer
get "maxDecent"(): integer
get "fontRenderContext"(): $FontRenderContext
get "ascent"(): integer
get "descent"(): integer
get "leading"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FontMetrics$Type = ($FontMetrics);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FontMetrics_ = $FontMetrics$Type;
}}
declare module "packages/java/awt/$Composite" {
import {$RenderingHints, $RenderingHints$Type} from "packages/java/awt/$RenderingHints"
import {$CompositeContext, $CompositeContext$Type} from "packages/java/awt/$CompositeContext"
import {$ColorModel, $ColorModel$Type} from "packages/java/awt/image/$ColorModel"

export interface $Composite {

 "createContext"(arg0: $ColorModel$Type, arg1: $ColorModel$Type, arg2: $RenderingHints$Type): $CompositeContext

(arg0: $ColorModel$Type, arg1: $ColorModel$Type, arg2: $RenderingHints$Type): $CompositeContext
}

export namespace $Composite {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Composite$Type = ($Composite);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Composite_ = $Composite$Type;
}}
declare module "packages/java/awt/dnd/$DragGestureRecognizer" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$DragGestureListener, $DragGestureListener$Type} from "packages/java/awt/dnd/$DragGestureListener"
import {$DragSource, $DragSource$Type} from "packages/java/awt/dnd/$DragSource"
import {$InputEvent, $InputEvent$Type} from "packages/java/awt/event/$InputEvent"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export class $DragGestureRecognizer implements $Serializable {


public "getComponent"(): $Component
public "getDragSource"(): $DragSource
public "addDragGestureListener"(arg0: $DragGestureListener$Type): void
public "setSourceActions"(arg0: integer): void
public "getTriggerEvent"(): $InputEvent
public "resetRecognizer"(): void
public "removeDragGestureListener"(arg0: $DragGestureListener$Type): void
public "getSourceActions"(): integer
public "setComponent"(arg0: $Component$Type): void
get "component"(): $Component
get "dragSource"(): $DragSource
set "sourceActions"(value: integer)
get "triggerEvent"(): $InputEvent
get "sourceActions"(): integer
set "component"(value: $Component$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DragGestureRecognizer$Type = ($DragGestureRecognizer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DragGestureRecognizer_ = $DragGestureRecognizer$Type;
}}
declare module "packages/java/awt/$MenuComponent" {
import {$AWTEvent, $AWTEvent$Type} from "packages/java/awt/$AWTEvent"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Event, $Event$Type} from "packages/java/awt/$Event"
import {$MenuContainer, $MenuContainer$Type} from "packages/java/awt/$MenuContainer"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$Font, $Font$Type} from "packages/java/awt/$Font"

export class $MenuComponent implements $Serializable {

constructor()

public "getName"(): string
public "toString"(): string
public "getParent"(): $MenuContainer
public "setName"(arg0: string): void
public "dispatchEvent"(arg0: $AWTEvent$Type): void
/**
 * 
 * @deprecated
 */
public "postEvent"(arg0: $Event$Type): boolean
public "removeNotify"(): void
public "getAccessibleContext"(): $AccessibleContext
public "getFont"(): $Font
public "setFont"(arg0: $Font$Type): void
get "name"(): string
get "parent"(): $MenuContainer
set "name"(value: string)
get "accessibleContext"(): $AccessibleContext
get "font"(): $Font
set "font"(value: $Font$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuComponent$Type = ($MenuComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuComponent_ = $MenuComponent$Type;
}}
declare module "packages/java/awt/geom/$PathIterator" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $PathIterator {

 "next"(): void
 "isDone"(): boolean
 "currentSegment"(arg0: (float)[]): integer
 "currentSegment"(arg0: (double)[]): integer
 "getWindingRule"(): integer
}

export namespace $PathIterator {
const WIND_EVEN_ODD: integer
const WIND_NON_ZERO: integer
const SEG_MOVETO: integer
const SEG_LINETO: integer
const SEG_QUADTO: integer
const SEG_CUBICTO: integer
const SEG_CLOSE: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PathIterator$Type = ($PathIterator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PathIterator_ = $PathIterator$Type;
}}
declare module "packages/java/awt/$PageAttributes$OrientationRequestedType" {
import {$AttributeValue, $AttributeValue$Type} from "packages/java/awt/$AttributeValue"

export class $PageAttributes$OrientationRequestedType extends $AttributeValue {
static readonly "PORTRAIT": $PageAttributes$OrientationRequestedType
static readonly "LANDSCAPE": $PageAttributes$OrientationRequestedType


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageAttributes$OrientationRequestedType$Type = ($PageAttributes$OrientationRequestedType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageAttributes$OrientationRequestedType_ = $PageAttributes$OrientationRequestedType$Type;
}}
declare module "packages/java/awt/event/$HierarchyEvent" {
import {$AWTEvent, $AWTEvent$Type} from "packages/java/awt/$AWTEvent"
import {$Container, $Container$Type} from "packages/java/awt/$Container"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export class $HierarchyEvent extends $AWTEvent {
static readonly "HIERARCHY_FIRST": integer
static readonly "HIERARCHY_CHANGED": integer
static readonly "ANCESTOR_MOVED": integer
static readonly "ANCESTOR_RESIZED": integer
static readonly "HIERARCHY_LAST": integer
static readonly "PARENT_CHANGED": integer
static readonly "DISPLAYABILITY_CHANGED": integer
static readonly "SHOWING_CHANGED": integer
static readonly "COMPONENT_EVENT_MASK": long
static readonly "CONTAINER_EVENT_MASK": long
static readonly "FOCUS_EVENT_MASK": long
static readonly "KEY_EVENT_MASK": long
static readonly "MOUSE_EVENT_MASK": long
static readonly "MOUSE_MOTION_EVENT_MASK": long
static readonly "WINDOW_EVENT_MASK": long
static readonly "ACTION_EVENT_MASK": long
static readonly "ADJUSTMENT_EVENT_MASK": long
static readonly "ITEM_EVENT_MASK": long
static readonly "TEXT_EVENT_MASK": long
static readonly "INPUT_METHOD_EVENT_MASK": long
static readonly "PAINT_EVENT_MASK": long
static readonly "INVOCATION_EVENT_MASK": long
static readonly "HIERARCHY_EVENT_MASK": long
static readonly "HIERARCHY_BOUNDS_EVENT_MASK": long
static readonly "MOUSE_WHEEL_EVENT_MASK": long
static readonly "WINDOW_STATE_EVENT_MASK": long
static readonly "WINDOW_FOCUS_EVENT_MASK": long
static readonly "RESERVED_ID_MAX": integer

constructor(arg0: $Component$Type, arg1: integer, arg2: $Component$Type, arg3: $Container$Type)
constructor(arg0: $Component$Type, arg1: integer, arg2: $Component$Type, arg3: $Container$Type, arg4: long)

public "paramString"(): string
public "getComponent"(): $Component
public "getChanged"(): $Component
public "getChangedParent"(): $Container
public "getChangeFlags"(): long
get "component"(): $Component
get "changed"(): $Component
get "changedParent"(): $Container
get "changeFlags"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HierarchyEvent$Type = ($HierarchyEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HierarchyEvent_ = $HierarchyEvent$Type;
}}
declare module "packages/java/awt/image/renderable/$RenderContext" {
import {$RenderingHints, $RenderingHints$Type} from "packages/java/awt/$RenderingHints"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$Shape, $Shape$Type} from "packages/java/awt/$Shape"
import {$AffineTransform, $AffineTransform$Type} from "packages/java/awt/geom/$AffineTransform"

export class $RenderContext implements $Cloneable {

constructor(arg0: $AffineTransform$Type, arg1: $Shape$Type)
constructor(arg0: $AffineTransform$Type, arg1: $RenderingHints$Type)
constructor(arg0: $AffineTransform$Type)
constructor(arg0: $AffineTransform$Type, arg1: $Shape$Type, arg2: $RenderingHints$Type)

public "clone"(): any
public "getRenderingHints"(): $RenderingHints
/**
 * 
 * @deprecated
 */
public "preConcetenateTransform"(arg0: $AffineTransform$Type): void
/**
 * 
 * @deprecated
 */
public "concetenateTransform"(arg0: $AffineTransform$Type): void
public "preConcatenateTransform"(arg0: $AffineTransform$Type): void
public "concatenateTransform"(arg0: $AffineTransform$Type): void
public "setAreaOfInterest"(arg0: $Shape$Type): void
public "getAreaOfInterest"(): $Shape
public "getTransform"(): $AffineTransform
public "setTransform"(arg0: $AffineTransform$Type): void
public "setRenderingHints"(arg0: $RenderingHints$Type): void
get "renderingHints"(): $RenderingHints
set "areaOfInterest"(value: $Shape$Type)
get "areaOfInterest"(): $Shape
get "transform"(): $AffineTransform
set "transform"(value: $AffineTransform$Type)
set "renderingHints"(value: $RenderingHints$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderContext$Type = ($RenderContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderContext_ = $RenderContext$Type;
}}
declare module "packages/java/awt/$AttributeValue" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $AttributeValue {


public "toString"(): string
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributeValue$Type = ($AttributeValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributeValue_ = $AttributeValue$Type;
}}
declare module "packages/java/awt/$Transparency" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Transparency {

 "getTransparency"(): integer

(): integer
}

export namespace $Transparency {
const OPAQUE: integer
const BITMASK: integer
const TRANSLUCENT: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Transparency$Type = ($Transparency);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Transparency_ = $Transparency$Type;
}}
declare module "packages/java/awt/geom/$Area" {
import {$Point2D, $Point2D$Type} from "packages/java/awt/geom/$Point2D"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$PathIterator, $PathIterator$Type} from "packages/java/awt/geom/$PathIterator"
import {$Rectangle2D, $Rectangle2D$Type} from "packages/java/awt/geom/$Rectangle2D"
import {$Shape, $Shape$Type} from "packages/java/awt/$Shape"
import {$AffineTransform, $AffineTransform$Type} from "packages/java/awt/geom/$AffineTransform"

export class $Area implements $Shape, $Cloneable {

constructor()
constructor(arg0: $Shape$Type)

public "add"(arg0: $Area$Type): void
public "equals"(arg0: $Area$Type): boolean
public "clone"(): any
public "isEmpty"(): boolean
public "contains"(arg0: $Point2D$Type): boolean
public "contains"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
public "contains"(arg0: $Rectangle2D$Type): boolean
public "contains"(arg0: double, arg1: double): boolean
public "transform"(arg0: $AffineTransform$Type): void
public "getBounds"(): $Rectangle
public "reset"(): void
public "subtract"(arg0: $Area$Type): void
public "intersects"(arg0: $Rectangle2D$Type): boolean
public "intersects"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
public "intersect"(arg0: $Area$Type): void
public "isRectangular"(): boolean
public "isSingular"(): boolean
public "getBounds2D"(): $Rectangle2D
public "getPathIterator"(arg0: $AffineTransform$Type, arg1: double): $PathIterator
public "getPathIterator"(arg0: $AffineTransform$Type): $PathIterator
public "exclusiveOr"(arg0: $Area$Type): void
public "isPolygonal"(): boolean
public "createTransformedArea"(arg0: $AffineTransform$Type): $Area
get "empty"(): boolean
get "bounds"(): $Rectangle
get "rectangular"(): boolean
get "singular"(): boolean
get "bounds2D"(): $Rectangle2D
get "polygonal"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Area$Type = ($Area);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Area_ = $Area$Type;
}}
declare module "packages/java/awt/event/$WindowFocusListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$WindowEvent, $WindowEvent$Type} from "packages/java/awt/event/$WindowEvent"

export interface $WindowFocusListener extends $EventListener {

 "windowGainedFocus"(arg0: $WindowEvent$Type): void
 "windowLostFocus"(arg0: $WindowEvent$Type): void
}

export namespace $WindowFocusListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WindowFocusListener$Type = ($WindowFocusListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WindowFocusListener_ = $WindowFocusListener$Type;
}}
declare module "packages/java/awt/$PageAttributes$PrintQualityType" {
import {$AttributeValue, $AttributeValue$Type} from "packages/java/awt/$AttributeValue"

export class $PageAttributes$PrintQualityType extends $AttributeValue {
static readonly "HIGH": $PageAttributes$PrintQualityType
static readonly "NORMAL": $PageAttributes$PrintQualityType
static readonly "DRAFT": $PageAttributes$PrintQualityType


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageAttributes$PrintQualityType$Type = ($PageAttributes$PrintQualityType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageAttributes$PrintQualityType_ = $PageAttributes$PrintQualityType$Type;
}}
declare module "packages/java/awt/$FocusTraversalPolicy" {
import {$Container, $Container$Type} from "packages/java/awt/$Container"
import {$Window, $Window$Type} from "packages/java/awt/$Window"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export class $FocusTraversalPolicy {


public "getComponentAfter"(arg0: $Container$Type, arg1: $Component$Type): $Component
public "getDefaultComponent"(arg0: $Container$Type): $Component
public "getComponentBefore"(arg0: $Container$Type, arg1: $Component$Type): $Component
public "getInitialComponent"(arg0: $Window$Type): $Component
public "getFirstComponent"(arg0: $Container$Type): $Component
public "getLastComponent"(arg0: $Container$Type): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FocusTraversalPolicy$Type = ($FocusTraversalPolicy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FocusTraversalPolicy_ = $FocusTraversalPolicy$Type;
}}
declare module "packages/java/awt/image/$BufferedImageOp" {
import {$RenderingHints, $RenderingHints$Type} from "packages/java/awt/$RenderingHints"
import {$Point2D, $Point2D$Type} from "packages/java/awt/geom/$Point2D"
import {$BufferedImage, $BufferedImage$Type} from "packages/java/awt/image/$BufferedImage"
import {$ColorModel, $ColorModel$Type} from "packages/java/awt/image/$ColorModel"
import {$Rectangle2D, $Rectangle2D$Type} from "packages/java/awt/geom/$Rectangle2D"

export interface $BufferedImageOp {

 "filter"(arg0: $BufferedImage$Type, arg1: $BufferedImage$Type): $BufferedImage
 "getRenderingHints"(): $RenderingHints
 "getBounds2D"(arg0: $BufferedImage$Type): $Rectangle2D
 "createCompatibleDestImage"(arg0: $BufferedImage$Type, arg1: $ColorModel$Type): $BufferedImage
 "getPoint2D"(arg0: $Point2D$Type, arg1: $Point2D$Type): $Point2D
}

export namespace $BufferedImageOp {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BufferedImageOp$Type = ($BufferedImageOp);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BufferedImageOp_ = $BufferedImageOp$Type;
}}
declare module "packages/java/awt/event/$MouseWheelListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$MouseWheelEvent, $MouseWheelEvent$Type} from "packages/java/awt/event/$MouseWheelEvent"

export interface $MouseWheelListener extends $EventListener {

 "mouseWheelMoved"(arg0: $MouseWheelEvent$Type): void

(arg0: $MouseWheelEvent$Type): void
}

export namespace $MouseWheelListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MouseWheelListener$Type = ($MouseWheelListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MouseWheelListener_ = $MouseWheelListener$Type;
}}
declare module "packages/java/awt/$Menu" {
import {$Event, $Event$Type} from "packages/java/awt/$Event"
import {$MenuContainer, $MenuContainer$Type} from "packages/java/awt/$MenuContainer"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$MenuComponent, $MenuComponent$Type} from "packages/java/awt/$MenuComponent"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$Font, $Font$Type} from "packages/java/awt/$Font"
import {$MenuItem, $MenuItem$Type} from "packages/java/awt/$MenuItem"

export class $Menu extends $MenuItem implements $MenuContainer, $Accessible {

constructor(arg0: string, arg1: boolean)
constructor(arg0: string)
constructor()

public "add"(arg0: $MenuItem$Type): $MenuItem
public "add"(arg0: string): void
public "remove"(arg0: integer): void
public "remove"(arg0: $MenuComponent$Type): void
public "insert"(arg0: $MenuItem$Type, arg1: integer): void
public "insert"(arg0: string, arg1: integer): void
public "paramString"(): string
public "removeAll"(): void
public "getItemCount"(): integer
public "getItem"(arg0: integer): $MenuItem
public "addSeparator"(): void
public "removeNotify"(): void
public "addNotify"(): void
public "getAccessibleContext"(): $AccessibleContext
/**
 * 
 * @deprecated
 */
public "countItems"(): integer
public "isTearOff"(): boolean
public "insertSeparator"(arg0: integer): void
/**
 * 
 * @deprecated
 */
public "postEvent"(arg0: $Event$Type): boolean
public "getFont"(): $Font
get "itemCount"(): integer
get "accessibleContext"(): $AccessibleContext
get "tearOff"(): boolean
get "font"(): $Font
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Menu$Type = ($Menu);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Menu_ = $Menu$Type;
}}
declare module "packages/java/awt/$Font" {
import {$CharacterIterator, $CharacterIterator$Type} from "packages/java/text/$CharacterIterator"
import {$GlyphVector, $GlyphVector$Type} from "packages/java/awt/font/$GlyphVector"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$AttributedCharacterIterator$Attribute, $AttributedCharacterIterator$Attribute$Type} from "packages/java/text/$AttributedCharacterIterator$Attribute"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"
import {$TextAttribute, $TextAttribute$Type} from "packages/java/awt/font/$TextAttribute"
import {$File, $File$Type} from "packages/java/io/$File"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$LineMetrics, $LineMetrics$Type} from "packages/java/awt/font/$LineMetrics"
import {$Rectangle2D, $Rectangle2D$Type} from "packages/java/awt/geom/$Rectangle2D"
import {$FontRenderContext, $FontRenderContext$Type} from "packages/java/awt/font/$FontRenderContext"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$AffineTransform, $AffineTransform$Type} from "packages/java/awt/geom/$AffineTransform"

export class $Font implements $Serializable {
static readonly "DIALOG": string
static readonly "DIALOG_INPUT": string
static readonly "SANS_SERIF": string
static readonly "SERIF": string
static readonly "MONOSPACED": string
static readonly "PLAIN": integer
static readonly "BOLD": integer
static readonly "ITALIC": integer
static readonly "ROMAN_BASELINE": integer
static readonly "CENTER_BASELINE": integer
static readonly "HANGING_BASELINE": integer
static readonly "TRUETYPE_FONT": integer
static readonly "TYPE1_FONT": integer
static readonly "LAYOUT_LEFT_TO_RIGHT": integer
static readonly "LAYOUT_RIGHT_TO_LEFT": integer
static readonly "LAYOUT_NO_START_CONTEXT": integer
static readonly "LAYOUT_NO_LIMIT_CONTEXT": integer

constructor(arg0: $Map$Type<(any), (any)>)
constructor(arg0: string, arg1: integer, arg2: integer)

public "getName"(): string
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "decode"(arg0: string): $Font
public "getSize"(): integer
public "getAttributes"(): $Map<($TextAttribute), (any)>
public "getFamily"(arg0: $Locale$Type): string
public "getFamily"(): string
public "getMaxCharBounds"(arg0: $FontRenderContext$Type): $Rectangle2D
public "createGlyphVector"(arg0: $FontRenderContext$Type, arg1: $CharacterIterator$Type): $GlyphVector
public "createGlyphVector"(arg0: $FontRenderContext$Type, arg1: (character)[]): $GlyphVector
public "createGlyphVector"(arg0: $FontRenderContext$Type, arg1: string): $GlyphVector
public "createGlyphVector"(arg0: $FontRenderContext$Type, arg1: (integer)[]): $GlyphVector
public "layoutGlyphVector"(arg0: $FontRenderContext$Type, arg1: (character)[], arg2: integer, arg3: integer, arg4: integer): $GlyphVector
public static "getFont"(arg0: $Map$Type<(any), (any)>): $Font
public static "getFont"(arg0: string, arg1: $Font$Type): $Font
public static "getFont"(arg0: string): $Font
public "getStyle"(): integer
public "getFontName"(): string
public "getFontName"(arg0: $Locale$Type): string
public "hasLayoutAttributes"(): boolean
public "getTransform"(): $AffineTransform
public "isBold"(): boolean
public "isItalic"(): boolean
public "getNumGlyphs"(): integer
public "getMissingGlyphCode"(): integer
public "getBaselineFor"(arg0: character): byte
public "canDisplay"(arg0: integer): boolean
public "canDisplay"(arg0: character): boolean
public "getItalicAngle"(): float
public "getStringBounds"(arg0: string, arg1: $FontRenderContext$Type): $Rectangle2D
public "getStringBounds"(arg0: $CharacterIterator$Type, arg1: integer, arg2: integer, arg3: $FontRenderContext$Type): $Rectangle2D
public "getStringBounds"(arg0: (character)[], arg1: integer, arg2: integer, arg3: $FontRenderContext$Type): $Rectangle2D
public "getStringBounds"(arg0: string, arg1: integer, arg2: integer, arg3: $FontRenderContext$Type): $Rectangle2D
public static "textRequiresLayout"(arg0: (character)[], arg1: integer, arg2: integer): boolean
public static "createFonts"(arg0: $File$Type): ($Font)[]
public static "createFonts"(arg0: $InputStream$Type): ($Font)[]
public static "createFont"(arg0: integer, arg1: $File$Type): $Font
public static "createFont"(arg0: integer, arg1: $InputStream$Type): $Font
public "getPSName"(): string
public "getSize2D"(): float
public "isPlain"(): boolean
public "getAvailableAttributes"(): ($AttributedCharacterIterator$Attribute)[]
public "deriveFont"(arg0: float): $Font
public "deriveFont"(arg0: integer, arg1: float): $Font
public "deriveFont"(arg0: integer, arg1: $AffineTransform$Type): $Font
public "deriveFont"(arg0: $AffineTransform$Type): $Font
public "deriveFont"(arg0: integer): $Font
public "deriveFont"(arg0: $Map$Type<(any), (any)>): $Font
public "canDisplayUpTo"(arg0: (character)[], arg1: integer, arg2: integer): integer
public "canDisplayUpTo"(arg0: string): integer
public "canDisplayUpTo"(arg0: $CharacterIterator$Type, arg1: integer, arg2: integer): integer
public "hasUniformLineMetrics"(): boolean
public "getLineMetrics"(arg0: string, arg1: $FontRenderContext$Type): $LineMetrics
public "getLineMetrics"(arg0: string, arg1: integer, arg2: integer, arg3: $FontRenderContext$Type): $LineMetrics
public "getLineMetrics"(arg0: (character)[], arg1: integer, arg2: integer, arg3: $FontRenderContext$Type): $LineMetrics
public "getLineMetrics"(arg0: $CharacterIterator$Type, arg1: integer, arg2: integer, arg3: $FontRenderContext$Type): $LineMetrics
public "isTransformed"(): boolean
get "name"(): string
get "size"(): integer
get "attributes"(): $Map<($TextAttribute), (any)>
get "family"(): string
get "style"(): integer
get "fontName"(): string
get "transform"(): $AffineTransform
get "bold"(): boolean
get "italic"(): boolean
get "numGlyphs"(): integer
get "missingGlyphCode"(): integer
get "italicAngle"(): float
get "pSName"(): string
get "size2D"(): float
get "plain"(): boolean
get "availableAttributes"(): ($AttributedCharacterIterator$Attribute)[]
get "transformed"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Font$Type = ($Font);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Font_ = $Font$Type;
}}
declare module "packages/java/awt/$JobAttributes$SidesType" {
import {$AttributeValue, $AttributeValue$Type} from "packages/java/awt/$AttributeValue"

export class $JobAttributes$SidesType extends $AttributeValue {
static readonly "ONE_SIDED": $JobAttributes$SidesType
static readonly "TWO_SIDED_LONG_EDGE": $JobAttributes$SidesType
static readonly "TWO_SIDED_SHORT_EDGE": $JobAttributes$SidesType


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JobAttributes$SidesType$Type = ($JobAttributes$SidesType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JobAttributes$SidesType_ = $JobAttributes$SidesType$Type;
}}
declare module "packages/java/awt/$JobAttributes$DialogType" {
import {$AttributeValue, $AttributeValue$Type} from "packages/java/awt/$AttributeValue"

export class $JobAttributes$DialogType extends $AttributeValue {
static readonly "COMMON": $JobAttributes$DialogType
static readonly "NATIVE": $JobAttributes$DialogType
static readonly "NONE": $JobAttributes$DialogType


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JobAttributes$DialogType$Type = ($JobAttributes$DialogType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JobAttributes$DialogType_ = $JobAttributes$DialogType$Type;
}}
declare module "packages/java/awt/dnd/$DropTargetListener" {
import {$DropTargetDropEvent, $DropTargetDropEvent$Type} from "packages/java/awt/dnd/$DropTargetDropEvent"
import {$DropTargetDragEvent, $DropTargetDragEvent$Type} from "packages/java/awt/dnd/$DropTargetDragEvent"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$DropTargetEvent, $DropTargetEvent$Type} from "packages/java/awt/dnd/$DropTargetEvent"

export interface $DropTargetListener extends $EventListener {

 "drop"(arg0: $DropTargetDropEvent$Type): void
 "dragEnter"(arg0: $DropTargetDragEvent$Type): void
 "dragOver"(arg0: $DropTargetDragEvent$Type): void
 "dropActionChanged"(arg0: $DropTargetDragEvent$Type): void
 "dragExit"(arg0: $DropTargetEvent$Type): void
}

export namespace $DropTargetListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropTargetListener$Type = ($DropTargetListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropTargetListener_ = $DropTargetListener$Type;
}}
declare module "packages/java/awt/dnd/$DragGestureListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$DragGestureEvent, $DragGestureEvent$Type} from "packages/java/awt/dnd/$DragGestureEvent"

export interface $DragGestureListener extends $EventListener {

 "dragGestureRecognized"(arg0: $DragGestureEvent$Type): void

(arg0: $DragGestureEvent$Type): void
}

export namespace $DragGestureListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DragGestureListener$Type = ($DragGestureListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DragGestureListener_ = $DragGestureListener$Type;
}}
declare module "packages/java/awt/$Rectangle" {
import {$Point2D, $Point2D$Type} from "packages/java/awt/geom/$Point2D"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$PathIterator, $PathIterator$Type} from "packages/java/awt/geom/$PathIterator"
import {$Dimension, $Dimension$Type} from "packages/java/awt/$Dimension"
import {$Rectangle2D, $Rectangle2D$Type} from "packages/java/awt/geom/$Rectangle2D"
import {$Shape, $Shape$Type} from "packages/java/awt/$Shape"
import {$AffineTransform, $AffineTransform$Type} from "packages/java/awt/geom/$AffineTransform"

export class $Rectangle extends $Rectangle2D implements $Shape, $Serializable {
 "x": integer
 "y": integer
 "width": integer
 "height": integer
static readonly "OUT_LEFT": integer
static readonly "OUT_TOP": integer
static readonly "OUT_RIGHT": integer
static readonly "OUT_BOTTOM": integer

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer)
constructor(arg0: integer, arg1: integer)
constructor(arg0: $Dimension$Type)
constructor(arg0: $Point$Type)
constructor(arg0: $Rectangle$Type)
constructor()
constructor(arg0: $Point$Type, arg1: $Dimension$Type)

public "add"(arg0: $Rectangle$Type): void
public "add"(arg0: $Point$Type): void
public "add"(arg0: integer, arg1: integer): void
public "equals"(arg0: any): boolean
public "toString"(): string
public "isEmpty"(): boolean
public "contains"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): boolean
public "contains"(arg0: $Rectangle$Type): boolean
public "contains"(arg0: $Point$Type): boolean
public "contains"(arg0: integer, arg1: integer): boolean
public "getBounds"(): $Rectangle
public "getLocation"(): $Point
public "getSize"(): $Dimension
public "grow"(arg0: integer, arg1: integer): void
/**
 * 
 * @deprecated
 */
public "resize"(arg0: integer, arg1: integer): void
/**
 * 
 * @deprecated
 */
public "move"(arg0: integer, arg1: integer): void
public "setSize"(arg0: integer, arg1: integer): void
public "setSize"(arg0: $Dimension$Type): void
public "union"(arg0: $Rectangle$Type): $Rectangle
public "intersects"(arg0: $Rectangle$Type): boolean
public "getY"(): double
public "translate"(arg0: integer, arg1: integer): void
public "getX"(): double
public "getWidth"(): double
public "getHeight"(): double
public "setLocation"(arg0: $Point$Type): void
public "setLocation"(arg0: integer, arg1: integer): void
public "setBounds"(arg0: $Rectangle$Type): void
public "setBounds"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
/**
 * 
 * @deprecated
 */
public "inside"(arg0: integer, arg1: integer): boolean
public "setRect"(arg0: double, arg1: double, arg2: double, arg3: double): void
/**
 * 
 * @deprecated
 */
public "reshape"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "getBounds2D"(): $Rectangle2D
public "outcode"(arg0: double, arg1: double): integer
public "createIntersection"(arg0: $Rectangle2D$Type): $Rectangle2D
public "createUnion"(arg0: $Rectangle2D$Type): $Rectangle2D
public "intersection"(arg0: $Rectangle$Type): $Rectangle
public "contains"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
public "contains"(arg0: double, arg1: double): boolean
public "contains"(arg0: $Rectangle2D$Type): boolean
public "contains"(arg0: $Point2D$Type): boolean
public "intersects"(arg0: $Rectangle2D$Type): boolean
public "intersects"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
public "getPathIterator"(arg0: $AffineTransform$Type, arg1: double): $PathIterator
public "getPathIterator"(arg0: $AffineTransform$Type): $PathIterator
get "empty"(): boolean
get "bounds"(): $Rectangle
get "location"(): $Point
get "size"(): $Dimension
set "size"(value: $Dimension$Type)
get "y"(): double
get "x"(): double
get "width"(): double
get "height"(): double
set "location"(value: $Point$Type)
set "bounds"(value: $Rectangle$Type)
get "bounds2D"(): $Rectangle2D
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
declare module "packages/java/awt/event/$MouseEvent" {
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$InputEvent, $InputEvent$Type} from "packages/java/awt/event/$InputEvent"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export class $MouseEvent extends $InputEvent {
static readonly "MOUSE_FIRST": integer
static readonly "MOUSE_LAST": integer
static readonly "MOUSE_CLICKED": integer
static readonly "MOUSE_PRESSED": integer
static readonly "MOUSE_RELEASED": integer
static readonly "MOUSE_MOVED": integer
static readonly "MOUSE_ENTERED": integer
static readonly "MOUSE_EXITED": integer
static readonly "MOUSE_DRAGGED": integer
static readonly "MOUSE_WHEEL": integer
static readonly "NOBUTTON": integer
static readonly "BUTTON1": integer
static readonly "BUTTON2": integer
static readonly "BUTTON3": integer
/**
 * 
 * @deprecated
 */
static readonly "SHIFT_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "CTRL_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "META_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "ALT_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "ALT_GRAPH_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "BUTTON1_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "BUTTON2_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "BUTTON3_MASK": integer
static readonly "SHIFT_DOWN_MASK": integer
static readonly "CTRL_DOWN_MASK": integer
static readonly "META_DOWN_MASK": integer
static readonly "ALT_DOWN_MASK": integer
static readonly "BUTTON1_DOWN_MASK": integer
static readonly "BUTTON2_DOWN_MASK": integer
static readonly "BUTTON3_DOWN_MASK": integer
static readonly "ALT_GRAPH_DOWN_MASK": integer
static readonly "COMPONENT_FIRST": integer
static readonly "COMPONENT_LAST": integer
static readonly "COMPONENT_MOVED": integer
static readonly "COMPONENT_RESIZED": integer
static readonly "COMPONENT_SHOWN": integer
static readonly "COMPONENT_HIDDEN": integer
static readonly "COMPONENT_EVENT_MASK": long
static readonly "CONTAINER_EVENT_MASK": long
static readonly "FOCUS_EVENT_MASK": long
static readonly "KEY_EVENT_MASK": long
static readonly "MOUSE_EVENT_MASK": long
static readonly "MOUSE_MOTION_EVENT_MASK": long
static readonly "WINDOW_EVENT_MASK": long
static readonly "ACTION_EVENT_MASK": long
static readonly "ADJUSTMENT_EVENT_MASK": long
static readonly "ITEM_EVENT_MASK": long
static readonly "TEXT_EVENT_MASK": long
static readonly "INPUT_METHOD_EVENT_MASK": long
static readonly "PAINT_EVENT_MASK": long
static readonly "INVOCATION_EVENT_MASK": long
static readonly "HIERARCHY_EVENT_MASK": long
static readonly "HIERARCHY_BOUNDS_EVENT_MASK": long
static readonly "MOUSE_WHEEL_EVENT_MASK": long
static readonly "WINDOW_STATE_EVENT_MASK": long
static readonly "WINDOW_FOCUS_EVENT_MASK": long
static readonly "RESERVED_ID_MAX": integer

constructor(arg0: $Component$Type, arg1: integer, arg2: long, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: boolean, arg8: integer)
constructor(arg0: $Component$Type, arg1: integer, arg2: long, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: boolean)
constructor(arg0: $Component$Type, arg1: integer, arg2: long, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: boolean, arg10: integer)

public "paramString"(): string
public "getLocationOnScreen"(): $Point
public "getPoint"(): $Point
public "getY"(): integer
public "getX"(): integer
public "getButton"(): integer
public "getXOnScreen"(): integer
public "getYOnScreen"(): integer
public "getClickCount"(): integer
public "isPopupTrigger"(): boolean
public static "getMouseModifiersText"(arg0: integer): string
public "translatePoint"(arg0: integer, arg1: integer): void
public "getModifiersEx"(): integer
get "locationOnScreen"(): $Point
get "point"(): $Point
get "y"(): integer
get "x"(): integer
get "button"(): integer
get "xOnScreen"(): integer
get "yOnScreen"(): integer
get "clickCount"(): integer
get "popupTrigger"(): boolean
get "modifiersEx"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MouseEvent$Type = ($MouseEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MouseEvent_ = $MouseEvent$Type;
}}
declare module "packages/java/awt/event/$KeyEvent" {
import {$InputEvent, $InputEvent$Type} from "packages/java/awt/event/$InputEvent"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export class $KeyEvent extends $InputEvent {
static readonly "KEY_FIRST": integer
static readonly "KEY_LAST": integer
static readonly "KEY_TYPED": integer
static readonly "KEY_PRESSED": integer
static readonly "KEY_RELEASED": integer
static readonly "VK_ENTER": integer
static readonly "VK_BACK_SPACE": integer
static readonly "VK_TAB": integer
static readonly "VK_CANCEL": integer
static readonly "VK_CLEAR": integer
static readonly "VK_SHIFT": integer
static readonly "VK_CONTROL": integer
static readonly "VK_ALT": integer
static readonly "VK_PAUSE": integer
static readonly "VK_CAPS_LOCK": integer
static readonly "VK_ESCAPE": integer
static readonly "VK_SPACE": integer
static readonly "VK_PAGE_UP": integer
static readonly "VK_PAGE_DOWN": integer
static readonly "VK_END": integer
static readonly "VK_HOME": integer
static readonly "VK_LEFT": integer
static readonly "VK_UP": integer
static readonly "VK_RIGHT": integer
static readonly "VK_DOWN": integer
static readonly "VK_COMMA": integer
static readonly "VK_MINUS": integer
static readonly "VK_PERIOD": integer
static readonly "VK_SLASH": integer
static readonly "VK_0": integer
static readonly "VK_1": integer
static readonly "VK_2": integer
static readonly "VK_3": integer
static readonly "VK_4": integer
static readonly "VK_5": integer
static readonly "VK_6": integer
static readonly "VK_7": integer
static readonly "VK_8": integer
static readonly "VK_9": integer
static readonly "VK_SEMICOLON": integer
static readonly "VK_EQUALS": integer
static readonly "VK_A": integer
static readonly "VK_B": integer
static readonly "VK_C": integer
static readonly "VK_D": integer
static readonly "VK_E": integer
static readonly "VK_F": integer
static readonly "VK_G": integer
static readonly "VK_H": integer
static readonly "VK_I": integer
static readonly "VK_J": integer
static readonly "VK_K": integer
static readonly "VK_L": integer
static readonly "VK_M": integer
static readonly "VK_N": integer
static readonly "VK_O": integer
static readonly "VK_P": integer
static readonly "VK_Q": integer
static readonly "VK_R": integer
static readonly "VK_S": integer
static readonly "VK_T": integer
static readonly "VK_U": integer
static readonly "VK_V": integer
static readonly "VK_W": integer
static readonly "VK_X": integer
static readonly "VK_Y": integer
static readonly "VK_Z": integer
static readonly "VK_OPEN_BRACKET": integer
static readonly "VK_BACK_SLASH": integer
static readonly "VK_CLOSE_BRACKET": integer
static readonly "VK_NUMPAD0": integer
static readonly "VK_NUMPAD1": integer
static readonly "VK_NUMPAD2": integer
static readonly "VK_NUMPAD3": integer
static readonly "VK_NUMPAD4": integer
static readonly "VK_NUMPAD5": integer
static readonly "VK_NUMPAD6": integer
static readonly "VK_NUMPAD7": integer
static readonly "VK_NUMPAD8": integer
static readonly "VK_NUMPAD9": integer
static readonly "VK_MULTIPLY": integer
static readonly "VK_ADD": integer
static readonly "VK_SEPARATER": integer
static readonly "VK_SEPARATOR": integer
static readonly "VK_SUBTRACT": integer
static readonly "VK_DECIMAL": integer
static readonly "VK_DIVIDE": integer
static readonly "VK_DELETE": integer
static readonly "VK_NUM_LOCK": integer
static readonly "VK_SCROLL_LOCK": integer
static readonly "VK_F1": integer
static readonly "VK_F2": integer
static readonly "VK_F3": integer
static readonly "VK_F4": integer
static readonly "VK_F5": integer
static readonly "VK_F6": integer
static readonly "VK_F7": integer
static readonly "VK_F8": integer
static readonly "VK_F9": integer
static readonly "VK_F10": integer
static readonly "VK_F11": integer
static readonly "VK_F12": integer
static readonly "VK_F13": integer
static readonly "VK_F14": integer
static readonly "VK_F15": integer
static readonly "VK_F16": integer
static readonly "VK_F17": integer
static readonly "VK_F18": integer
static readonly "VK_F19": integer
static readonly "VK_F20": integer
static readonly "VK_F21": integer
static readonly "VK_F22": integer
static readonly "VK_F23": integer
static readonly "VK_F24": integer
static readonly "VK_PRINTSCREEN": integer
static readonly "VK_INSERT": integer
static readonly "VK_HELP": integer
static readonly "VK_META": integer
static readonly "VK_BACK_QUOTE": integer
static readonly "VK_QUOTE": integer
static readonly "VK_KP_UP": integer
static readonly "VK_KP_DOWN": integer
static readonly "VK_KP_LEFT": integer
static readonly "VK_KP_RIGHT": integer
static readonly "VK_DEAD_GRAVE": integer
static readonly "VK_DEAD_ACUTE": integer
static readonly "VK_DEAD_CIRCUMFLEX": integer
static readonly "VK_DEAD_TILDE": integer
static readonly "VK_DEAD_MACRON": integer
static readonly "VK_DEAD_BREVE": integer
static readonly "VK_DEAD_ABOVEDOT": integer
static readonly "VK_DEAD_DIAERESIS": integer
static readonly "VK_DEAD_ABOVERING": integer
static readonly "VK_DEAD_DOUBLEACUTE": integer
static readonly "VK_DEAD_CARON": integer
static readonly "VK_DEAD_CEDILLA": integer
static readonly "VK_DEAD_OGONEK": integer
static readonly "VK_DEAD_IOTA": integer
static readonly "VK_DEAD_VOICED_SOUND": integer
static readonly "VK_DEAD_SEMIVOICED_SOUND": integer
static readonly "VK_AMPERSAND": integer
static readonly "VK_ASTERISK": integer
static readonly "VK_QUOTEDBL": integer
static readonly "VK_LESS": integer
static readonly "VK_GREATER": integer
static readonly "VK_BRACELEFT": integer
static readonly "VK_BRACERIGHT": integer
static readonly "VK_AT": integer
static readonly "VK_COLON": integer
static readonly "VK_CIRCUMFLEX": integer
static readonly "VK_DOLLAR": integer
static readonly "VK_EURO_SIGN": integer
static readonly "VK_EXCLAMATION_MARK": integer
static readonly "VK_INVERTED_EXCLAMATION_MARK": integer
static readonly "VK_LEFT_PARENTHESIS": integer
static readonly "VK_NUMBER_SIGN": integer
static readonly "VK_PLUS": integer
static readonly "VK_RIGHT_PARENTHESIS": integer
static readonly "VK_UNDERSCORE": integer
static readonly "VK_WINDOWS": integer
static readonly "VK_CONTEXT_MENU": integer
static readonly "VK_FINAL": integer
static readonly "VK_CONVERT": integer
static readonly "VK_NONCONVERT": integer
static readonly "VK_ACCEPT": integer
static readonly "VK_MODECHANGE": integer
static readonly "VK_KANA": integer
static readonly "VK_KANJI": integer
static readonly "VK_ALPHANUMERIC": integer
static readonly "VK_KATAKANA": integer
static readonly "VK_HIRAGANA": integer
static readonly "VK_FULL_WIDTH": integer
static readonly "VK_HALF_WIDTH": integer
static readonly "VK_ROMAN_CHARACTERS": integer
static readonly "VK_ALL_CANDIDATES": integer
static readonly "VK_PREVIOUS_CANDIDATE": integer
static readonly "VK_CODE_INPUT": integer
static readonly "VK_JAPANESE_KATAKANA": integer
static readonly "VK_JAPANESE_HIRAGANA": integer
static readonly "VK_JAPANESE_ROMAN": integer
static readonly "VK_KANA_LOCK": integer
static readonly "VK_INPUT_METHOD_ON_OFF": integer
static readonly "VK_CUT": integer
static readonly "VK_COPY": integer
static readonly "VK_PASTE": integer
static readonly "VK_UNDO": integer
static readonly "VK_AGAIN": integer
static readonly "VK_FIND": integer
static readonly "VK_PROPS": integer
static readonly "VK_STOP": integer
static readonly "VK_COMPOSE": integer
static readonly "VK_ALT_GRAPH": integer
static readonly "VK_BEGIN": integer
static readonly "VK_UNDEFINED": integer
static readonly "CHAR_UNDEFINED": character
static readonly "KEY_LOCATION_UNKNOWN": integer
static readonly "KEY_LOCATION_STANDARD": integer
static readonly "KEY_LOCATION_LEFT": integer
static readonly "KEY_LOCATION_RIGHT": integer
static readonly "KEY_LOCATION_NUMPAD": integer
/**
 * 
 * @deprecated
 */
static readonly "SHIFT_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "CTRL_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "META_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "ALT_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "ALT_GRAPH_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "BUTTON1_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "BUTTON2_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "BUTTON3_MASK": integer
static readonly "SHIFT_DOWN_MASK": integer
static readonly "CTRL_DOWN_MASK": integer
static readonly "META_DOWN_MASK": integer
static readonly "ALT_DOWN_MASK": integer
static readonly "BUTTON1_DOWN_MASK": integer
static readonly "BUTTON2_DOWN_MASK": integer
static readonly "BUTTON3_DOWN_MASK": integer
static readonly "ALT_GRAPH_DOWN_MASK": integer
static readonly "COMPONENT_FIRST": integer
static readonly "COMPONENT_LAST": integer
static readonly "COMPONENT_MOVED": integer
static readonly "COMPONENT_RESIZED": integer
static readonly "COMPONENT_SHOWN": integer
static readonly "COMPONENT_HIDDEN": integer
static readonly "COMPONENT_EVENT_MASK": long
static readonly "CONTAINER_EVENT_MASK": long
static readonly "FOCUS_EVENT_MASK": long
static readonly "KEY_EVENT_MASK": long
static readonly "MOUSE_EVENT_MASK": long
static readonly "MOUSE_MOTION_EVENT_MASK": long
static readonly "WINDOW_EVENT_MASK": long
static readonly "ACTION_EVENT_MASK": long
static readonly "ADJUSTMENT_EVENT_MASK": long
static readonly "ITEM_EVENT_MASK": long
static readonly "TEXT_EVENT_MASK": long
static readonly "INPUT_METHOD_EVENT_MASK": long
static readonly "PAINT_EVENT_MASK": long
static readonly "INVOCATION_EVENT_MASK": long
static readonly "HIERARCHY_EVENT_MASK": long
static readonly "HIERARCHY_BOUNDS_EVENT_MASK": long
static readonly "MOUSE_WHEEL_EVENT_MASK": long
static readonly "WINDOW_STATE_EVENT_MASK": long
static readonly "WINDOW_FOCUS_EVENT_MASK": long
static readonly "RESERVED_ID_MAX": integer

constructor(arg0: $Component$Type, arg1: integer, arg2: long, arg3: integer, arg4: integer, arg5: character)
constructor(arg0: $Component$Type, arg1: integer, arg2: long, arg3: integer, arg4: integer, arg5: character, arg6: integer)
/**
 * 
 * @deprecated
 */
constructor(arg0: $Component$Type, arg1: integer, arg2: long, arg3: integer, arg4: integer)

public "paramString"(): string
/**
 * 
 * @deprecated
 */
public static "getKeyModifiersText"(arg0: integer): string
/**
 * 
 * @deprecated
 */
public "setModifiers"(arg0: integer): void
public "getKeyCode"(): integer
public "setKeyChar"(arg0: character): void
public "getKeyChar"(): character
public "isActionKey"(): boolean
public static "getKeyText"(arg0: integer): string
public static "getExtendedKeyCodeForChar"(arg0: integer): integer
public "setKeyCode"(arg0: integer): void
public "getKeyLocation"(): integer
public "getExtendedKeyCode"(): integer
set "modifiers"(value: integer)
get "keyCode"(): integer
set "keyChar"(value: character)
get "keyChar"(): character
get "actionKey"(): boolean
set "keyCode"(value: integer)
get "keyLocation"(): integer
get "extendedKeyCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyEvent$Type = ($KeyEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyEvent_ = $KeyEvent$Type;
}}
declare module "packages/java/awt/datatransfer/$FlavorEvent" {
import {$EventObject, $EventObject$Type} from "packages/java/util/$EventObject"
import {$Clipboard, $Clipboard$Type} from "packages/java/awt/datatransfer/$Clipboard"

export class $FlavorEvent extends $EventObject {

constructor(arg0: $Clipboard$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlavorEvent$Type = ($FlavorEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlavorEvent_ = $FlavorEvent$Type;
}}
declare module "packages/java/awt/$PageAttributes$MediaType" {
import {$AttributeValue, $AttributeValue$Type} from "packages/java/awt/$AttributeValue"

export class $PageAttributes$MediaType extends $AttributeValue {
static readonly "ISO_4A0": $PageAttributes$MediaType
static readonly "ISO_2A0": $PageAttributes$MediaType
static readonly "ISO_A0": $PageAttributes$MediaType
static readonly "ISO_A1": $PageAttributes$MediaType
static readonly "ISO_A2": $PageAttributes$MediaType
static readonly "ISO_A3": $PageAttributes$MediaType
static readonly "ISO_A4": $PageAttributes$MediaType
static readonly "ISO_A5": $PageAttributes$MediaType
static readonly "ISO_A6": $PageAttributes$MediaType
static readonly "ISO_A7": $PageAttributes$MediaType
static readonly "ISO_A8": $PageAttributes$MediaType
static readonly "ISO_A9": $PageAttributes$MediaType
static readonly "ISO_A10": $PageAttributes$MediaType
static readonly "ISO_B0": $PageAttributes$MediaType
static readonly "ISO_B1": $PageAttributes$MediaType
static readonly "ISO_B2": $PageAttributes$MediaType
static readonly "ISO_B3": $PageAttributes$MediaType
static readonly "ISO_B4": $PageAttributes$MediaType
static readonly "ISO_B5": $PageAttributes$MediaType
static readonly "ISO_B6": $PageAttributes$MediaType
static readonly "ISO_B7": $PageAttributes$MediaType
static readonly "ISO_B8": $PageAttributes$MediaType
static readonly "ISO_B9": $PageAttributes$MediaType
static readonly "ISO_B10": $PageAttributes$MediaType
static readonly "JIS_B0": $PageAttributes$MediaType
static readonly "JIS_B1": $PageAttributes$MediaType
static readonly "JIS_B2": $PageAttributes$MediaType
static readonly "JIS_B3": $PageAttributes$MediaType
static readonly "JIS_B4": $PageAttributes$MediaType
static readonly "JIS_B5": $PageAttributes$MediaType
static readonly "JIS_B6": $PageAttributes$MediaType
static readonly "JIS_B7": $PageAttributes$MediaType
static readonly "JIS_B8": $PageAttributes$MediaType
static readonly "JIS_B9": $PageAttributes$MediaType
static readonly "JIS_B10": $PageAttributes$MediaType
static readonly "ISO_C0": $PageAttributes$MediaType
static readonly "ISO_C1": $PageAttributes$MediaType
static readonly "ISO_C2": $PageAttributes$MediaType
static readonly "ISO_C3": $PageAttributes$MediaType
static readonly "ISO_C4": $PageAttributes$MediaType
static readonly "ISO_C5": $PageAttributes$MediaType
static readonly "ISO_C6": $PageAttributes$MediaType
static readonly "ISO_C7": $PageAttributes$MediaType
static readonly "ISO_C8": $PageAttributes$MediaType
static readonly "ISO_C9": $PageAttributes$MediaType
static readonly "ISO_C10": $PageAttributes$MediaType
static readonly "ISO_DESIGNATED_LONG": $PageAttributes$MediaType
static readonly "EXECUTIVE": $PageAttributes$MediaType
static readonly "FOLIO": $PageAttributes$MediaType
static readonly "INVOICE": $PageAttributes$MediaType
static readonly "LEDGER": $PageAttributes$MediaType
static readonly "NA_LETTER": $PageAttributes$MediaType
static readonly "NA_LEGAL": $PageAttributes$MediaType
static readonly "QUARTO": $PageAttributes$MediaType
static readonly "A": $PageAttributes$MediaType
static readonly "B": $PageAttributes$MediaType
static readonly "C": $PageAttributes$MediaType
static readonly "D": $PageAttributes$MediaType
static readonly "E": $PageAttributes$MediaType
static readonly "NA_10X15_ENVELOPE": $PageAttributes$MediaType
static readonly "NA_10X14_ENVELOPE": $PageAttributes$MediaType
static readonly "NA_10X13_ENVELOPE": $PageAttributes$MediaType
static readonly "NA_9X12_ENVELOPE": $PageAttributes$MediaType
static readonly "NA_9X11_ENVELOPE": $PageAttributes$MediaType
static readonly "NA_7X9_ENVELOPE": $PageAttributes$MediaType
static readonly "NA_6X9_ENVELOPE": $PageAttributes$MediaType
static readonly "NA_NUMBER_9_ENVELOPE": $PageAttributes$MediaType
static readonly "NA_NUMBER_10_ENVELOPE": $PageAttributes$MediaType
static readonly "NA_NUMBER_11_ENVELOPE": $PageAttributes$MediaType
static readonly "NA_NUMBER_12_ENVELOPE": $PageAttributes$MediaType
static readonly "NA_NUMBER_14_ENVELOPE": $PageAttributes$MediaType
static readonly "INVITE_ENVELOPE": $PageAttributes$MediaType
static readonly "ITALY_ENVELOPE": $PageAttributes$MediaType
static readonly "MONARCH_ENVELOPE": $PageAttributes$MediaType
static readonly "PERSONAL_ENVELOPE": $PageAttributes$MediaType
static readonly "A0": $PageAttributes$MediaType
static readonly "A1": $PageAttributes$MediaType
static readonly "A2": $PageAttributes$MediaType
static readonly "A3": $PageAttributes$MediaType
static readonly "A4": $PageAttributes$MediaType
static readonly "A5": $PageAttributes$MediaType
static readonly "A6": $PageAttributes$MediaType
static readonly "A7": $PageAttributes$MediaType
static readonly "A8": $PageAttributes$MediaType
static readonly "A9": $PageAttributes$MediaType
static readonly "A10": $PageAttributes$MediaType
static readonly "B0": $PageAttributes$MediaType
static readonly "B1": $PageAttributes$MediaType
static readonly "B2": $PageAttributes$MediaType
static readonly "B3": $PageAttributes$MediaType
static readonly "B4": $PageAttributes$MediaType
static readonly "ISO_B4_ENVELOPE": $PageAttributes$MediaType
static readonly "B5": $PageAttributes$MediaType
static readonly "ISO_B5_ENVELOPE": $PageAttributes$MediaType
static readonly "B6": $PageAttributes$MediaType
static readonly "B7": $PageAttributes$MediaType
static readonly "B8": $PageAttributes$MediaType
static readonly "B9": $PageAttributes$MediaType
static readonly "B10": $PageAttributes$MediaType
static readonly "C0": $PageAttributes$MediaType
static readonly "ISO_C0_ENVELOPE": $PageAttributes$MediaType
static readonly "C1": $PageAttributes$MediaType
static readonly "ISO_C1_ENVELOPE": $PageAttributes$MediaType
static readonly "C2": $PageAttributes$MediaType
static readonly "ISO_C2_ENVELOPE": $PageAttributes$MediaType
static readonly "C3": $PageAttributes$MediaType
static readonly "ISO_C3_ENVELOPE": $PageAttributes$MediaType
static readonly "C4": $PageAttributes$MediaType
static readonly "ISO_C4_ENVELOPE": $PageAttributes$MediaType
static readonly "C5": $PageAttributes$MediaType
static readonly "ISO_C5_ENVELOPE": $PageAttributes$MediaType
static readonly "C6": $PageAttributes$MediaType
static readonly "ISO_C6_ENVELOPE": $PageAttributes$MediaType
static readonly "C7": $PageAttributes$MediaType
static readonly "ISO_C7_ENVELOPE": $PageAttributes$MediaType
static readonly "C8": $PageAttributes$MediaType
static readonly "ISO_C8_ENVELOPE": $PageAttributes$MediaType
static readonly "C9": $PageAttributes$MediaType
static readonly "ISO_C9_ENVELOPE": $PageAttributes$MediaType
static readonly "C10": $PageAttributes$MediaType
static readonly "ISO_C10_ENVELOPE": $PageAttributes$MediaType
static readonly "ISO_DESIGNATED_LONG_ENVELOPE": $PageAttributes$MediaType
static readonly "STATEMENT": $PageAttributes$MediaType
static readonly "TABLOID": $PageAttributes$MediaType
static readonly "LETTER": $PageAttributes$MediaType
static readonly "NOTE": $PageAttributes$MediaType
static readonly "LEGAL": $PageAttributes$MediaType
static readonly "ENV_10X15": $PageAttributes$MediaType
static readonly "ENV_10X14": $PageAttributes$MediaType
static readonly "ENV_10X13": $PageAttributes$MediaType
static readonly "ENV_9X12": $PageAttributes$MediaType
static readonly "ENV_9X11": $PageAttributes$MediaType
static readonly "ENV_7X9": $PageAttributes$MediaType
static readonly "ENV_6X9": $PageAttributes$MediaType
static readonly "ENV_9": $PageAttributes$MediaType
static readonly "ENV_10": $PageAttributes$MediaType
static readonly "ENV_11": $PageAttributes$MediaType
static readonly "ENV_12": $PageAttributes$MediaType
static readonly "ENV_14": $PageAttributes$MediaType
static readonly "ENV_INVITE": $PageAttributes$MediaType
static readonly "ENV_ITALY": $PageAttributes$MediaType
static readonly "ENV_MONARCH": $PageAttributes$MediaType
static readonly "ENV_PERSONAL": $PageAttributes$MediaType
static readonly "INVITE": $PageAttributes$MediaType
static readonly "ITALY": $PageAttributes$MediaType
static readonly "MONARCH": $PageAttributes$MediaType
static readonly "PERSONAL": $PageAttributes$MediaType


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageAttributes$MediaType$Type = ($PageAttributes$MediaType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageAttributes$MediaType_ = $PageAttributes$MediaType$Type;
}}
declare module "packages/java/awt/$Paint" {
import {$RenderingHints, $RenderingHints$Type} from "packages/java/awt/$RenderingHints"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$ColorModel, $ColorModel$Type} from "packages/java/awt/image/$ColorModel"
import {$Transparency, $Transparency$Type} from "packages/java/awt/$Transparency"
import {$Rectangle2D, $Rectangle2D$Type} from "packages/java/awt/geom/$Rectangle2D"
import {$PaintContext, $PaintContext$Type} from "packages/java/awt/$PaintContext"
import {$AffineTransform, $AffineTransform$Type} from "packages/java/awt/geom/$AffineTransform"

export interface $Paint extends $Transparency {

 "createContext"(arg0: $ColorModel$Type, arg1: $Rectangle$Type, arg2: $Rectangle2D$Type, arg3: $AffineTransform$Type, arg4: $RenderingHints$Type): $PaintContext
 "getTransparency"(): integer
}

export namespace $Paint {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Paint$Type = ($Paint);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Paint_ = $Paint$Type;
}}
declare module "packages/java/awt/$BufferCapabilities$FlipContents" {
import {$AttributeValue, $AttributeValue$Type} from "packages/java/awt/$AttributeValue"

export class $BufferCapabilities$FlipContents extends $AttributeValue {
static readonly "UNDEFINED": $BufferCapabilities$FlipContents
static readonly "BACKGROUND": $BufferCapabilities$FlipContents
static readonly "PRIOR": $BufferCapabilities$FlipContents
static readonly "COPIED": $BufferCapabilities$FlipContents


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BufferCapabilities$FlipContents$Type = ($BufferCapabilities$FlipContents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BufferCapabilities$FlipContents_ = $BufferCapabilities$FlipContents$Type;
}}
declare module "packages/java/awt/image/$SampleModel" {
import {$DataBuffer, $DataBuffer$Type} from "packages/java/awt/image/$DataBuffer"

export class $SampleModel {

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer)

public "getWidth"(): integer
public "getHeight"(): integer
public "getNumBands"(): integer
public "getDataElements"(arg0: integer, arg1: integer, arg2: any, arg3: $DataBuffer$Type): any
public "getDataElements"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: any, arg5: $DataBuffer$Type): any
public "setDataElements"(arg0: integer, arg1: integer, arg2: any, arg3: $DataBuffer$Type): void
public "setDataElements"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: any, arg5: $DataBuffer$Type): void
public "createCompatibleSampleModel"(arg0: integer, arg1: integer): $SampleModel
public "setPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (double)[], arg5: $DataBuffer$Type): void
public "setPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (integer)[], arg5: $DataBuffer$Type): void
public "setPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (float)[], arg5: $DataBuffer$Type): void
public "getDataType"(): integer
public "getPixel"(arg0: integer, arg1: integer, arg2: (integer)[], arg3: $DataBuffer$Type): (integer)[]
public "getPixel"(arg0: integer, arg1: integer, arg2: (double)[], arg3: $DataBuffer$Type): (double)[]
public "getPixel"(arg0: integer, arg1: integer, arg2: (float)[], arg3: $DataBuffer$Type): (float)[]
public "getTransferType"(): integer
public "setPixel"(arg0: integer, arg1: integer, arg2: (integer)[], arg3: $DataBuffer$Type): void
public "setPixel"(arg0: integer, arg1: integer, arg2: (float)[], arg3: $DataBuffer$Type): void
public "setPixel"(arg0: integer, arg1: integer, arg2: (double)[], arg3: $DataBuffer$Type): void
public "getSampleSize"(): (integer)[]
public "getSampleSize"(arg0: integer): integer
public "createDataBuffer"(): $DataBuffer
public "getNumDataElements"(): integer
public "createSubsetSampleModel"(arg0: (integer)[]): $SampleModel
public "getSample"(arg0: integer, arg1: integer, arg2: integer, arg3: $DataBuffer$Type): integer
public "getSampleFloat"(arg0: integer, arg1: integer, arg2: integer, arg3: $DataBuffer$Type): float
public "getSampleDouble"(arg0: integer, arg1: integer, arg2: integer, arg3: $DataBuffer$Type): double
public "getSamples"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: (integer)[], arg6: $DataBuffer$Type): (integer)[]
public "getSamples"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: (float)[], arg6: $DataBuffer$Type): (float)[]
public "getSamples"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: (double)[], arg6: $DataBuffer$Type): (double)[]
public "setSample"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $DataBuffer$Type): void
public "setSample"(arg0: integer, arg1: integer, arg2: integer, arg3: float, arg4: $DataBuffer$Type): void
public "setSample"(arg0: integer, arg1: integer, arg2: integer, arg3: double, arg4: $DataBuffer$Type): void
public "setSamples"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: (float)[], arg6: $DataBuffer$Type): void
public "setSamples"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: (integer)[], arg6: $DataBuffer$Type): void
public "setSamples"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: (double)[], arg6: $DataBuffer$Type): void
public "getPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (float)[], arg5: $DataBuffer$Type): (float)[]
public "getPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (integer)[], arg5: $DataBuffer$Type): (integer)[]
public "getPixels"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: (double)[], arg5: $DataBuffer$Type): (double)[]
get "width"(): integer
get "height"(): integer
get "numBands"(): integer
get "dataType"(): integer
get "transferType"(): integer
get "sampleSize"(): (integer)[]
get "numDataElements"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SampleModel$Type = ($SampleModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SampleModel_ = $SampleModel$Type;
}}
declare module "packages/java/awt/image/$VolatileImage" {
import {$BufferedImage, $BufferedImage$Type} from "packages/java/awt/image/$BufferedImage"
import {$GraphicsConfiguration, $GraphicsConfiguration$Type} from "packages/java/awt/$GraphicsConfiguration"
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$Transparency, $Transparency$Type} from "packages/java/awt/$Transparency"
import {$ImageProducer, $ImageProducer$Type} from "packages/java/awt/image/$ImageProducer"
import {$Graphics2D, $Graphics2D$Type} from "packages/java/awt/$Graphics2D"
import {$ImageCapabilities, $ImageCapabilities$Type} from "packages/java/awt/$ImageCapabilities"
import {$Image, $Image$Type} from "packages/java/awt/$Image"

export class $VolatileImage extends $Image implements $Transparency {
static readonly "IMAGE_OK": integer
static readonly "IMAGE_RESTORED": integer
static readonly "IMAGE_INCOMPATIBLE": integer
static readonly "UndefinedProperty": any
static readonly "SCALE_DEFAULT": integer
static readonly "SCALE_FAST": integer
static readonly "SCALE_SMOOTH": integer
static readonly "SCALE_REPLICATE": integer
static readonly "SCALE_AREA_AVERAGING": integer


public "validate"(arg0: $GraphicsConfiguration$Type): integer
public "getSource"(): $ImageProducer
public "getCapabilities"(): $ImageCapabilities
public "getGraphics"(): $Graphics
public "getWidth"(): integer
public "getHeight"(): integer
public "createGraphics"(): $Graphics2D
public "contentsLost"(): boolean
public "getTransparency"(): integer
public "getSnapshot"(): $BufferedImage
get "source"(): $ImageProducer
get "capabilities"(): $ImageCapabilities
get "graphics"(): $Graphics
get "width"(): integer
get "height"(): integer
get "transparency"(): integer
get "snapshot"(): $BufferedImage
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VolatileImage$Type = ($VolatileImage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VolatileImage_ = $VolatileImage$Type;
}}
declare module "packages/java/awt/im/$InputContext" {
import {$Character$Subset, $Character$Subset$Type} from "packages/java/lang/$Character$Subset"
import {$AWTEvent, $AWTEvent$Type} from "packages/java/awt/$AWTEvent"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export class $InputContext {


public static "getInstance"(): $InputContext
public "getLocale"(): $Locale
public "dispose"(): void
public "dispatchEvent"(arg0: $AWTEvent$Type): void
public "removeNotify"(arg0: $Component$Type): void
public "endComposition"(): void
public "selectInputMethod"(arg0: $Locale$Type): boolean
public "setCharacterSubsets"(arg0: ($Character$Subset$Type)[]): void
public "setCompositionEnabled"(arg0: boolean): void
public "isCompositionEnabled"(): boolean
public "reconvert"(): void
public "getInputMethodControlObject"(): any
get "instance"(): $InputContext
get "locale"(): $Locale
set "characterSubsets"(value: ($Character$Subset$Type)[])
set "compositionEnabled"(value: boolean)
get "compositionEnabled"(): boolean
get "inputMethodControlObject"(): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputContext$Type = ($InputContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputContext_ = $InputContext$Type;
}}
declare module "packages/java/awt/event/$WindowEvent" {
import {$Window, $Window$Type} from "packages/java/awt/$Window"
import {$ComponentEvent, $ComponentEvent$Type} from "packages/java/awt/event/$ComponentEvent"

export class $WindowEvent extends $ComponentEvent {
static readonly "WINDOW_FIRST": integer
static readonly "WINDOW_OPENED": integer
static readonly "WINDOW_CLOSING": integer
static readonly "WINDOW_CLOSED": integer
static readonly "WINDOW_ICONIFIED": integer
static readonly "WINDOW_DEICONIFIED": integer
static readonly "WINDOW_ACTIVATED": integer
static readonly "WINDOW_DEACTIVATED": integer
static readonly "WINDOW_GAINED_FOCUS": integer
static readonly "WINDOW_LOST_FOCUS": integer
static readonly "WINDOW_STATE_CHANGED": integer
static readonly "WINDOW_LAST": integer
static readonly "COMPONENT_FIRST": integer
static readonly "COMPONENT_LAST": integer
static readonly "COMPONENT_MOVED": integer
static readonly "COMPONENT_RESIZED": integer
static readonly "COMPONENT_SHOWN": integer
static readonly "COMPONENT_HIDDEN": integer
static readonly "COMPONENT_EVENT_MASK": long
static readonly "CONTAINER_EVENT_MASK": long
static readonly "FOCUS_EVENT_MASK": long
static readonly "KEY_EVENT_MASK": long
static readonly "MOUSE_EVENT_MASK": long
static readonly "MOUSE_MOTION_EVENT_MASK": long
static readonly "WINDOW_EVENT_MASK": long
static readonly "ACTION_EVENT_MASK": long
static readonly "ADJUSTMENT_EVENT_MASK": long
static readonly "ITEM_EVENT_MASK": long
static readonly "TEXT_EVENT_MASK": long
static readonly "INPUT_METHOD_EVENT_MASK": long
static readonly "PAINT_EVENT_MASK": long
static readonly "INVOCATION_EVENT_MASK": long
static readonly "HIERARCHY_EVENT_MASK": long
static readonly "HIERARCHY_BOUNDS_EVENT_MASK": long
static readonly "MOUSE_WHEEL_EVENT_MASK": long
static readonly "WINDOW_STATE_EVENT_MASK": long
static readonly "WINDOW_FOCUS_EVENT_MASK": long
static readonly "RESERVED_ID_MAX": integer

constructor(arg0: $Window$Type, arg1: integer)
constructor(arg0: $Window$Type, arg1: integer, arg2: integer, arg3: integer)
constructor(arg0: $Window$Type, arg1: integer, arg2: $Window$Type)
constructor(arg0: $Window$Type, arg1: integer, arg2: $Window$Type, arg3: integer, arg4: integer)

public "paramString"(): string
public "getWindow"(): $Window
public "getOppositeWindow"(): $Window
public "getNewState"(): integer
public "getOldState"(): integer
get "window"(): $Window
get "oppositeWindow"(): $Window
get "newState"(): integer
get "oldState"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WindowEvent$Type = ($WindowEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WindowEvent_ = $WindowEvent$Type;
}}
declare module "packages/java/awt/image/$TileObserver" {
import {$WritableRenderedImage, $WritableRenderedImage$Type} from "packages/java/awt/image/$WritableRenderedImage"

export interface $TileObserver {

 "tileUpdate"(arg0: $WritableRenderedImage$Type, arg1: integer, arg2: integer, arg3: boolean): void

(arg0: $WritableRenderedImage$Type, arg1: integer, arg2: integer, arg3: boolean): void
}

export namespace $TileObserver {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TileObserver$Type = ($TileObserver);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TileObserver_ = $TileObserver$Type;
}}
declare module "packages/java/awt/$PrintJob" {
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$Dimension, $Dimension$Type} from "packages/java/awt/$Dimension"

export class $PrintJob {


/**
 * 
 * @deprecated
 */
public "finalize"(): void
public "end"(): void
public "getGraphics"(): $Graphics
public "getPageDimension"(): $Dimension
public "getPageResolution"(): integer
public "lastPageFirst"(): boolean
get "graphics"(): $Graphics
get "pageDimension"(): $Dimension
get "pageResolution"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PrintJob$Type = ($PrintJob);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PrintJob_ = $PrintJob$Type;
}}
declare module "packages/java/awt/$CompositeContext" {
import {$WritableRaster, $WritableRaster$Type} from "packages/java/awt/image/$WritableRaster"
import {$Raster, $Raster$Type} from "packages/java/awt/image/$Raster"

export interface $CompositeContext {

 "compose"(arg0: $Raster$Type, arg1: $Raster$Type, arg2: $WritableRaster$Type): void
 "dispose"(): void
}

export namespace $CompositeContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompositeContext$Type = ($CompositeContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompositeContext_ = $CompositeContext$Type;
}}
declare module "packages/java/awt/$RenderingHints$Key" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $RenderingHints$Key {


public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "isCompatibleValue"(arg0: any): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderingHints$Key$Type = ($RenderingHints$Key);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderingHints$Key_ = $RenderingHints$Key$Type;
}}
declare module "packages/java/awt/dnd/$DragSource" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Cursor, $Cursor$Type} from "packages/java/awt/$Cursor"
import {$FlavorMap, $FlavorMap$Type} from "packages/java/awt/datatransfer/$FlavorMap"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$DragGestureListener, $DragGestureListener$Type} from "packages/java/awt/dnd/$DragGestureListener"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$DragGestureRecognizer, $DragGestureRecognizer$Type} from "packages/java/awt/dnd/$DragGestureRecognizer"
import {$Component, $Component$Type} from "packages/java/awt/$Component"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$DragGestureEvent, $DragGestureEvent$Type} from "packages/java/awt/dnd/$DragGestureEvent"
import {$DragSourceListener, $DragSourceListener$Type} from "packages/java/awt/dnd/$DragSourceListener"
import {$Image, $Image$Type} from "packages/java/awt/$Image"
import {$DragSourceMotionListener, $DragSourceMotionListener$Type} from "packages/java/awt/dnd/$DragSourceMotionListener"
import {$Transferable, $Transferable$Type} from "packages/java/awt/datatransfer/$Transferable"

export class $DragSource implements $Serializable {
static readonly "DefaultCopyDrop": $Cursor
static readonly "DefaultMoveDrop": $Cursor
static readonly "DefaultLinkDrop": $Cursor
static readonly "DefaultCopyNoDrop": $Cursor
static readonly "DefaultMoveNoDrop": $Cursor
static readonly "DefaultLinkNoDrop": $Cursor

constructor()

public "getListeners"<T extends $EventListener>(arg0: $Class$Type<(T)>): (T)[]
public "createDragGestureRecognizer"<T extends $DragGestureRecognizer>(arg0: $Class$Type<(T)>, arg1: $Component$Type, arg2: integer, arg3: $DragGestureListener$Type): T
public "addDragSourceListener"(arg0: $DragSourceListener$Type): void
public "removeDragSourceListener"(arg0: $DragSourceListener$Type): void
public "addDragSourceMotionListener"(arg0: $DragSourceMotionListener$Type): void
public static "getDefaultDragSource"(): $DragSource
public static "isDragImageSupported"(): boolean
public "createDefaultDragGestureRecognizer"(arg0: $Component$Type, arg1: integer, arg2: $DragGestureListener$Type): $DragGestureRecognizer
public "getDragSourceListeners"(): ($DragSourceListener)[]
public "removeDragSourceMotionListener"(arg0: $DragSourceMotionListener$Type): void
public "getDragSourceMotionListeners"(): ($DragSourceMotionListener)[]
public static "getDragThreshold"(): integer
public "getFlavorMap"(): $FlavorMap
public "startDrag"(arg0: $DragGestureEvent$Type, arg1: $Cursor$Type, arg2: $Image$Type, arg3: $Point$Type, arg4: $Transferable$Type, arg5: $DragSourceListener$Type, arg6: $FlavorMap$Type): void
public "startDrag"(arg0: $DragGestureEvent$Type, arg1: $Cursor$Type, arg2: $Image$Type, arg3: $Point$Type, arg4: $Transferable$Type, arg5: $DragSourceListener$Type): void
public "startDrag"(arg0: $DragGestureEvent$Type, arg1: $Cursor$Type, arg2: $Transferable$Type, arg3: $DragSourceListener$Type, arg4: $FlavorMap$Type): void
public "startDrag"(arg0: $DragGestureEvent$Type, arg1: $Cursor$Type, arg2: $Transferable$Type, arg3: $DragSourceListener$Type): void
get "defaultDragSource"(): $DragSource
get "dragImageSupported"(): boolean
get "dragSourceListeners"(): ($DragSourceListener)[]
get "dragSourceMotionListeners"(): ($DragSourceMotionListener)[]
get "dragThreshold"(): integer
get "flavorMap"(): $FlavorMap
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DragSource$Type = ($DragSource);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DragSource_ = $DragSource$Type;
}}
declare module "packages/java/awt/dnd/$DropTargetEvent" {
import {$DropTargetContext, $DropTargetContext$Type} from "packages/java/awt/dnd/$DropTargetContext"
import {$EventObject, $EventObject$Type} from "packages/java/util/$EventObject"

export class $DropTargetEvent extends $EventObject {

constructor(arg0: $DropTargetContext$Type)

public "getDropTargetContext"(): $DropTargetContext
get "dropTargetContext"(): $DropTargetContext
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropTargetEvent$Type = ($DropTargetEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropTargetEvent_ = $DropTargetEvent$Type;
}}
declare module "packages/java/awt/$Graphics2D" {
import {$RenderingHints, $RenderingHints$Type} from "packages/java/awt/$RenderingHints"
import {$GlyphVector, $GlyphVector$Type} from "packages/java/awt/font/$GlyphVector"
import {$BufferedImage, $BufferedImage$Type} from "packages/java/awt/image/$BufferedImage"
import {$BufferedImageOp, $BufferedImageOp$Type} from "packages/java/awt/image/$BufferedImageOp"
import {$Stroke, $Stroke$Type} from "packages/java/awt/$Stroke"
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$ImageObserver, $ImageObserver$Type} from "packages/java/awt/image/$ImageObserver"
import {$RenderedImage, $RenderedImage$Type} from "packages/java/awt/image/$RenderedImage"
import {$Shape, $Shape$Type} from "packages/java/awt/$Shape"
import {$Color, $Color$Type} from "packages/java/awt/$Color"
import {$Composite, $Composite$Type} from "packages/java/awt/$Composite"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$AttributedCharacterIterator, $AttributedCharacterIterator$Type} from "packages/java/text/$AttributedCharacterIterator"
import {$GraphicsConfiguration, $GraphicsConfiguration$Type} from "packages/java/awt/$GraphicsConfiguration"
import {$RenderableImage, $RenderableImage$Type} from "packages/java/awt/image/renderable/$RenderableImage"
import {$Paint, $Paint$Type} from "packages/java/awt/$Paint"
import {$FontRenderContext, $FontRenderContext$Type} from "packages/java/awt/font/$FontRenderContext"
import {$Image, $Image$Type} from "packages/java/awt/$Image"
import {$AffineTransform, $AffineTransform$Type} from "packages/java/awt/geom/$AffineTransform"
import {$RenderingHints$Key, $RenderingHints$Key$Type} from "packages/java/awt/$RenderingHints$Key"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Graphics2D extends $Graphics {


public "scale"(arg0: double, arg1: double): void
public "fill"(arg0: $Shape$Type): void
public "transform"(arg0: $AffineTransform$Type): void
public "rotate"(arg0: double): void
public "rotate"(arg0: double, arg1: double, arg2: double): void
public "hit"(arg0: $Rectangle$Type, arg1: $Shape$Type, arg2: boolean): boolean
public "drawString"(arg0: $AttributedCharacterIterator$Type, arg1: integer, arg2: integer): void
public "drawString"(arg0: string, arg1: float, arg2: float): void
public "drawString"(arg0: $AttributedCharacterIterator$Type, arg1: float, arg2: float): void
public "drawString"(arg0: string, arg1: integer, arg2: integer): void
public "draw"(arg0: $Shape$Type): void
public "translate"(arg0: double, arg1: double): void
public "translate"(arg0: integer, arg1: integer): void
public "drawImage"(arg0: $BufferedImage$Type, arg1: $BufferedImageOp$Type, arg2: integer, arg3: integer): void
public "drawImage"(arg0: $Image$Type, arg1: $AffineTransform$Type, arg2: $ImageObserver$Type): boolean
public "getPaint"(): $Paint
public "getDeviceConfiguration"(): $GraphicsConfiguration
public "drawGlyphVector"(arg0: $GlyphVector$Type, arg1: float, arg2: float): void
public "setRenderingHint"(arg0: $RenderingHints$Key$Type, arg1: any): void
public "setComposite"(arg0: $Composite$Type): void
public "getRenderingHints"(): $RenderingHints
public "getBackground"(): $Color
public "setBackground"(arg0: $Color$Type): void
public "clip"(arg0: $Shape$Type): void
public "getFontRenderContext"(): $FontRenderContext
public "getTransform"(): $AffineTransform
public "setTransform"(arg0: $AffineTransform$Type): void
public "setPaint"(arg0: $Paint$Type): void
public "drawRenderedImage"(arg0: $RenderedImage$Type, arg1: $AffineTransform$Type): void
public "setStroke"(arg0: $Stroke$Type): void
public "getRenderingHint"(arg0: $RenderingHints$Key$Type): any
public "setRenderingHints"(arg0: $Map$Type<(any), (any)>): void
public "addRenderingHints"(arg0: $Map$Type<(any), (any)>): void
public "getComposite"(): $Composite
public "getStroke"(): $Stroke
public "drawRenderableImage"(arg0: $RenderableImage$Type, arg1: $AffineTransform$Type): void
public "draw3DRect"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: boolean): void
public "fill3DRect"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: boolean): void
public "shear"(arg0: double, arg1: double): void
get "paint"(): $Paint
get "deviceConfiguration"(): $GraphicsConfiguration
set "composite"(value: $Composite$Type)
get "renderingHints"(): $RenderingHints
get "background"(): $Color
set "background"(value: $Color$Type)
get "fontRenderContext"(): $FontRenderContext
set "paint"(value: $Paint$Type)
set "stroke"(value: $Stroke$Type)
set "renderingHints"(value: $Map$Type<(any), (any)>)
get "composite"(): $Composite
get "stroke"(): $Stroke
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Graphics2D$Type = ($Graphics2D);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Graphics2D_ = $Graphics2D$Type;
}}
declare module "packages/java/awt/dnd/$DropTargetDragEvent" {
import {$DropTargetContext, $DropTargetContext$Type} from "packages/java/awt/dnd/$DropTargetContext"
import {$DataFlavor, $DataFlavor$Type} from "packages/java/awt/datatransfer/$DataFlavor"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$DropTargetEvent, $DropTargetEvent$Type} from "packages/java/awt/dnd/$DropTargetEvent"
import {$Transferable, $Transferable$Type} from "packages/java/awt/datatransfer/$Transferable"

export class $DropTargetDragEvent extends $DropTargetEvent {

constructor(arg0: $DropTargetContext$Type, arg1: $Point$Type, arg2: integer, arg3: integer)

public "getLocation"(): $Point
public "isDataFlavorSupported"(arg0: $DataFlavor$Type): boolean
public "getDropAction"(): integer
public "getCurrentDataFlavors"(): ($DataFlavor)[]
public "getCurrentDataFlavorsAsList"(): $List<($DataFlavor)>
public "acceptDrag"(arg0: integer): void
public "rejectDrag"(): void
public "getSourceActions"(): integer
public "getTransferable"(): $Transferable
get "location"(): $Point
get "dropAction"(): integer
get "currentDataFlavors"(): ($DataFlavor)[]
get "currentDataFlavorsAsList"(): $List<($DataFlavor)>
get "sourceActions"(): integer
get "transferable"(): $Transferable
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropTargetDragEvent$Type = ($DropTargetDragEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropTargetDragEvent_ = $DropTargetDragEvent$Type;
}}
declare module "packages/java/awt/image/$BufferStrategy" {
import {$BufferCapabilities, $BufferCapabilities$Type} from "packages/java/awt/$BufferCapabilities"
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"

export class $BufferStrategy {


public "dispose"(): void
public "getCapabilities"(): $BufferCapabilities
public "contentsLost"(): boolean
public "show"(): void
public "getDrawGraphics"(): $Graphics
public "contentsRestored"(): boolean
get "capabilities"(): $BufferCapabilities
get "drawGraphics"(): $Graphics
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BufferStrategy$Type = ($BufferStrategy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BufferStrategy_ = $BufferStrategy$Type;
}}
declare module "packages/java/awt/image/$ImageObserver" {
import {$Image, $Image$Type} from "packages/java/awt/$Image"

export interface $ImageObserver {

 "imageUpdate"(arg0: $Image$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): boolean

(arg0: $Image$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): boolean
}

export namespace $ImageObserver {
const WIDTH: integer
const HEIGHT: integer
const PROPERTIES: integer
const SOMEBITS: integer
const FRAMEBITS: integer
const ALLBITS: integer
const ERROR: integer
const ABORT: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImageObserver$Type = ($ImageObserver);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImageObserver_ = $ImageObserver$Type;
}}
declare module "packages/java/awt/$GraphicsDevice" {
import {$GraphicsDevice$WindowTranslucency, $GraphicsDevice$WindowTranslucency$Type} from "packages/java/awt/$GraphicsDevice$WindowTranslucency"
import {$GraphicsConfigTemplate, $GraphicsConfigTemplate$Type} from "packages/java/awt/$GraphicsConfigTemplate"
import {$Window, $Window$Type} from "packages/java/awt/$Window"
import {$DisplayMode, $DisplayMode$Type} from "packages/java/awt/$DisplayMode"
import {$GraphicsConfiguration, $GraphicsConfiguration$Type} from "packages/java/awt/$GraphicsConfiguration"

export class $GraphicsDevice {
static readonly "TYPE_RASTER_SCREEN": integer
static readonly "TYPE_PRINTER": integer
static readonly "TYPE_IMAGE_BUFFER": integer


public "getType"(): integer
public "getDisplayMode"(): $DisplayMode
public "getIDstring"(): string
public "getConfigurations"(): ($GraphicsConfiguration)[]
public "getBestConfiguration"(arg0: $GraphicsConfigTemplate$Type): $GraphicsConfiguration
public "getFullScreenWindow"(): $Window
public "setFullScreenWindow"(arg0: $Window$Type): void
public "isFullScreenSupported"(): boolean
public "isDisplayChangeSupported"(): boolean
public "setDisplayMode"(arg0: $DisplayMode$Type): void
public "getDisplayModes"(): ($DisplayMode)[]
public "getAvailableAcceleratedMemory"(): integer
public "getDefaultConfiguration"(): $GraphicsConfiguration
public "isWindowTranslucencySupported"(arg0: $GraphicsDevice$WindowTranslucency$Type): boolean
get "type"(): integer
get "displayMode"(): $DisplayMode
get "iDstring"(): string
get "configurations"(): ($GraphicsConfiguration)[]
get "fullScreenWindow"(): $Window
set "fullScreenWindow"(value: $Window$Type)
get "fullScreenSupported"(): boolean
get "displayChangeSupported"(): boolean
set "displayMode"(value: $DisplayMode$Type)
get "displayModes"(): ($DisplayMode)[]
get "availableAcceleratedMemory"(): integer
get "defaultConfiguration"(): $GraphicsConfiguration
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GraphicsDevice$Type = ($GraphicsDevice);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GraphicsDevice_ = $GraphicsDevice$Type;
}}
declare module "packages/java/awt/datatransfer/$ClipboardOwner" {
import {$Clipboard, $Clipboard$Type} from "packages/java/awt/datatransfer/$Clipboard"
import {$Transferable, $Transferable$Type} from "packages/java/awt/datatransfer/$Transferable"

export interface $ClipboardOwner {

 "lostOwnership"(arg0: $Clipboard$Type, arg1: $Transferable$Type): void

(arg0: $Clipboard$Type, arg1: $Transferable$Type): void
}

export namespace $ClipboardOwner {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClipboardOwner$Type = ($ClipboardOwner);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClipboardOwner_ = $ClipboardOwner$Type;
}}
declare module "packages/java/awt/$AWTKeyStroke" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$KeyEvent, $KeyEvent$Type} from "packages/java/awt/event/$KeyEvent"

export class $AWTKeyStroke implements $Serializable {


public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "getModifiers"(): integer
public "getKeyCode"(): integer
public "getKeyChar"(): character
public static "getAWTKeyStroke"(arg0: integer, arg1: integer): $AWTKeyStroke
public static "getAWTKeyStroke"(arg0: integer, arg1: integer, arg2: boolean): $AWTKeyStroke
public static "getAWTKeyStroke"(arg0: character, arg1: integer): $AWTKeyStroke
public static "getAWTKeyStroke"(arg0: character): $AWTKeyStroke
public static "getAWTKeyStroke"(arg0: string): $AWTKeyStroke
public static "getAWTKeyStrokeForEvent"(arg0: $KeyEvent$Type): $AWTKeyStroke
public "isOnKeyRelease"(): boolean
public "getKeyEventType"(): integer
get "modifiers"(): integer
get "keyCode"(): integer
get "keyChar"(): character
get "onKeyRelease"(): boolean
get "keyEventType"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AWTKeyStroke$Type = ($AWTKeyStroke);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AWTKeyStroke_ = $AWTKeyStroke$Type;
}}
declare module "packages/java/awt/event/$AdjustmentEvent" {
import {$AWTEvent, $AWTEvent$Type} from "packages/java/awt/$AWTEvent"
import {$Adjustable, $Adjustable$Type} from "packages/java/awt/$Adjustable"

export class $AdjustmentEvent extends $AWTEvent {
static readonly "ADJUSTMENT_FIRST": integer
static readonly "ADJUSTMENT_LAST": integer
static readonly "ADJUSTMENT_VALUE_CHANGED": integer
static readonly "UNIT_INCREMENT": integer
static readonly "UNIT_DECREMENT": integer
static readonly "BLOCK_DECREMENT": integer
static readonly "BLOCK_INCREMENT": integer
static readonly "TRACK": integer
static readonly "COMPONENT_EVENT_MASK": long
static readonly "CONTAINER_EVENT_MASK": long
static readonly "FOCUS_EVENT_MASK": long
static readonly "KEY_EVENT_MASK": long
static readonly "MOUSE_EVENT_MASK": long
static readonly "MOUSE_MOTION_EVENT_MASK": long
static readonly "WINDOW_EVENT_MASK": long
static readonly "ACTION_EVENT_MASK": long
static readonly "ADJUSTMENT_EVENT_MASK": long
static readonly "ITEM_EVENT_MASK": long
static readonly "TEXT_EVENT_MASK": long
static readonly "INPUT_METHOD_EVENT_MASK": long
static readonly "PAINT_EVENT_MASK": long
static readonly "INVOCATION_EVENT_MASK": long
static readonly "HIERARCHY_EVENT_MASK": long
static readonly "HIERARCHY_BOUNDS_EVENT_MASK": long
static readonly "MOUSE_WHEEL_EVENT_MASK": long
static readonly "WINDOW_STATE_EVENT_MASK": long
static readonly "WINDOW_FOCUS_EVENT_MASK": long
static readonly "RESERVED_ID_MAX": integer

constructor(arg0: $Adjustable$Type, arg1: integer, arg2: integer, arg3: integer)
constructor(arg0: $Adjustable$Type, arg1: integer, arg2: integer, arg3: integer, arg4: boolean)

public "getValue"(): integer
public "paramString"(): string
public "getAdjustable"(): $Adjustable
public "getAdjustmentType"(): integer
public "getValueIsAdjusting"(): boolean
get "value"(): integer
get "adjustable"(): $Adjustable
get "adjustmentType"(): integer
get "valueIsAdjusting"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AdjustmentEvent$Type = ($AdjustmentEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AdjustmentEvent_ = $AdjustmentEvent$Type;
}}
declare module "packages/java/awt/$Polygon" {
import {$Point2D, $Point2D$Type} from "packages/java/awt/geom/$Point2D"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$PathIterator, $PathIterator$Type} from "packages/java/awt/geom/$PathIterator"
import {$Rectangle2D, $Rectangle2D$Type} from "packages/java/awt/geom/$Rectangle2D"
import {$Shape, $Shape$Type} from "packages/java/awt/$Shape"
import {$AffineTransform, $AffineTransform$Type} from "packages/java/awt/geom/$AffineTransform"

export class $Polygon implements $Shape, $Serializable {
 "npoints": integer
 "xpoints": (integer)[]
 "ypoints": (integer)[]

constructor()
constructor(arg0: (integer)[], arg1: (integer)[], arg2: integer)

public "contains"(arg0: integer, arg1: integer): boolean
public "contains"(arg0: double, arg1: double): boolean
public "contains"(arg0: $Point$Type): boolean
public "contains"(arg0: $Point2D$Type): boolean
public "contains"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
public "contains"(arg0: $Rectangle2D$Type): boolean
public "getBounds"(): $Rectangle
public "reset"(): void
public "intersects"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
public "intersects"(arg0: $Rectangle2D$Type): boolean
public "invalidate"(): void
public "translate"(arg0: integer, arg1: integer): void
/**
 * 
 * @deprecated
 */
public "getBoundingBox"(): $Rectangle
/**
 * 
 * @deprecated
 */
public "inside"(arg0: integer, arg1: integer): boolean
public "addPoint"(arg0: integer, arg1: integer): void
public "getBounds2D"(): $Rectangle2D
public "getPathIterator"(arg0: $AffineTransform$Type): $PathIterator
public "getPathIterator"(arg0: $AffineTransform$Type, arg1: double): $PathIterator
get "bounds"(): $Rectangle
get "boundingBox"(): $Rectangle
get "bounds2D"(): $Rectangle2D
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Polygon$Type = ($Polygon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Polygon_ = $Polygon$Type;
}}
declare module "packages/java/awt/im/$InputMethodRequests" {
import {$AttributedCharacterIterator$Attribute, $AttributedCharacterIterator$Attribute$Type} from "packages/java/text/$AttributedCharacterIterator$Attribute"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$AttributedCharacterIterator, $AttributedCharacterIterator$Type} from "packages/java/text/$AttributedCharacterIterator"
import {$TextHitInfo, $TextHitInfo$Type} from "packages/java/awt/font/$TextHitInfo"

export interface $InputMethodRequests {

 "getLocationOffset"(arg0: integer, arg1: integer): $TextHitInfo
 "getSelectedText"(arg0: ($AttributedCharacterIterator$Attribute$Type)[]): $AttributedCharacterIterator
 "getTextLocation"(arg0: $TextHitInfo$Type): $Rectangle
 "getInsertPositionOffset"(): integer
 "getCommittedText"(arg0: integer, arg1: integer, arg2: ($AttributedCharacterIterator$Attribute$Type)[]): $AttributedCharacterIterator
 "getCommittedTextLength"(): integer
 "cancelLatestCommittedText"(arg0: ($AttributedCharacterIterator$Attribute$Type)[]): $AttributedCharacterIterator
}

export namespace $InputMethodRequests {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputMethodRequests$Type = ($InputMethodRequests);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputMethodRequests_ = $InputMethodRequests$Type;
}}
declare module "packages/java/awt/$JobAttributes$MultipleDocumentHandlingType" {
import {$AttributeValue, $AttributeValue$Type} from "packages/java/awt/$AttributeValue"

export class $JobAttributes$MultipleDocumentHandlingType extends $AttributeValue {
static readonly "SEPARATE_DOCUMENTS_COLLATED_COPIES": $JobAttributes$MultipleDocumentHandlingType
static readonly "SEPARATE_DOCUMENTS_UNCOLLATED_COPIES": $JobAttributes$MultipleDocumentHandlingType


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JobAttributes$MultipleDocumentHandlingType$Type = ($JobAttributes$MultipleDocumentHandlingType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JobAttributes$MultipleDocumentHandlingType_ = $JobAttributes$MultipleDocumentHandlingType$Type;
}}
declare module "packages/java/awt/font/$TextHitInfo" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $TextHitInfo {


public "equals"(arg0: any): boolean
public "equals"(arg0: $TextHitInfo$Type): boolean
public "toString"(): string
public "hashCode"(): integer
public static "trailing"(arg0: integer): $TextHitInfo
public "isLeadingEdge"(): boolean
public "getCharIndex"(): integer
public "getOtherHit"(): $TextHitInfo
public "getOffsetHit"(arg0: integer): $TextHitInfo
public static "leading"(arg0: integer): $TextHitInfo
public "getInsertionIndex"(): integer
public static "afterOffset"(arg0: integer): $TextHitInfo
public static "beforeOffset"(arg0: integer): $TextHitInfo
get "leadingEdge"(): boolean
get "charIndex"(): integer
get "otherHit"(): $TextHitInfo
get "insertionIndex"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextHitInfo$Type = ($TextHitInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextHitInfo_ = $TextHitInfo$Type;
}}
declare module "packages/java/awt/$JobAttributes$DestinationType" {
import {$AttributeValue, $AttributeValue$Type} from "packages/java/awt/$AttributeValue"

export class $JobAttributes$DestinationType extends $AttributeValue {
static readonly "FILE": $JobAttributes$DestinationType
static readonly "PRINTER": $JobAttributes$DestinationType


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JobAttributes$DestinationType$Type = ($JobAttributes$DestinationType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JobAttributes$DestinationType_ = $JobAttributes$DestinationType$Type;
}}
declare module "packages/java/awt/geom/$Rectangle2D" {
import {$Point2D, $Point2D$Type} from "packages/java/awt/geom/$Point2D"
import {$Line2D, $Line2D$Type} from "packages/java/awt/geom/$Line2D"
import {$RectangularShape, $RectangularShape$Type} from "packages/java/awt/geom/$RectangularShape"
import {$PathIterator, $PathIterator$Type} from "packages/java/awt/geom/$PathIterator"
import {$AffineTransform, $AffineTransform$Type} from "packages/java/awt/geom/$AffineTransform"

export class $Rectangle2D extends $RectangularShape {
static readonly "OUT_LEFT": integer
static readonly "OUT_TOP": integer
static readonly "OUT_RIGHT": integer
static readonly "OUT_BOTTOM": integer


public "add"(arg0: double, arg1: double): void
public "add"(arg0: $Point2D$Type): void
public "add"(arg0: $Rectangle2D$Type): void
public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "contains"(arg0: double, arg1: double): boolean
public "contains"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
public static "union"(arg0: $Rectangle2D$Type, arg1: $Rectangle2D$Type, arg2: $Rectangle2D$Type): void
public "intersects"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
public static "intersect"(arg0: $Rectangle2D$Type, arg1: $Rectangle2D$Type, arg2: $Rectangle2D$Type): void
public "setFrame"(arg0: double, arg1: double, arg2: double, arg3: double): void
public "setRect"(arg0: $Rectangle2D$Type): void
public "setRect"(arg0: double, arg1: double, arg2: double, arg3: double): void
public "getBounds2D"(): $Rectangle2D
public "outcode"(arg0: double, arg1: double): integer
public "outcode"(arg0: $Point2D$Type): integer
public "createIntersection"(arg0: $Rectangle2D$Type): $Rectangle2D
public "createUnion"(arg0: $Rectangle2D$Type): $Rectangle2D
public "getPathIterator"(arg0: $AffineTransform$Type): $PathIterator
public "getPathIterator"(arg0: $AffineTransform$Type, arg1: double): $PathIterator
public "intersectsLine"(arg0: $Line2D$Type): boolean
public "intersectsLine"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
set "rect"(value: $Rectangle2D$Type)
get "bounds2D"(): $Rectangle2D
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Rectangle2D$Type = ($Rectangle2D);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Rectangle2D_ = $Rectangle2D$Type;
}}
declare module "packages/java/awt/event/$ItemEvent" {
import {$ItemSelectable, $ItemSelectable$Type} from "packages/java/awt/$ItemSelectable"
import {$AWTEvent, $AWTEvent$Type} from "packages/java/awt/$AWTEvent"

export class $ItemEvent extends $AWTEvent {
static readonly "ITEM_FIRST": integer
static readonly "ITEM_LAST": integer
static readonly "ITEM_STATE_CHANGED": integer
static readonly "SELECTED": integer
static readonly "DESELECTED": integer
static readonly "COMPONENT_EVENT_MASK": long
static readonly "CONTAINER_EVENT_MASK": long
static readonly "FOCUS_EVENT_MASK": long
static readonly "KEY_EVENT_MASK": long
static readonly "MOUSE_EVENT_MASK": long
static readonly "MOUSE_MOTION_EVENT_MASK": long
static readonly "WINDOW_EVENT_MASK": long
static readonly "ACTION_EVENT_MASK": long
static readonly "ADJUSTMENT_EVENT_MASK": long
static readonly "ITEM_EVENT_MASK": long
static readonly "TEXT_EVENT_MASK": long
static readonly "INPUT_METHOD_EVENT_MASK": long
static readonly "PAINT_EVENT_MASK": long
static readonly "INVOCATION_EVENT_MASK": long
static readonly "HIERARCHY_EVENT_MASK": long
static readonly "HIERARCHY_BOUNDS_EVENT_MASK": long
static readonly "MOUSE_WHEEL_EVENT_MASK": long
static readonly "WINDOW_STATE_EVENT_MASK": long
static readonly "WINDOW_FOCUS_EVENT_MASK": long
static readonly "RESERVED_ID_MAX": integer

constructor(arg0: $ItemSelectable$Type, arg1: integer, arg2: any, arg3: integer)

public "paramString"(): string
public "getItem"(): any
public "getItemSelectable"(): $ItemSelectable
public "getStateChange"(): integer
get "item"(): any
get "itemSelectable"(): $ItemSelectable
get "stateChange"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemEvent$Type = ($ItemEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemEvent_ = $ItemEvent$Type;
}}
declare module "packages/java/awt/dnd/$DropTargetDropEvent" {
import {$DropTargetContext, $DropTargetContext$Type} from "packages/java/awt/dnd/$DropTargetContext"
import {$DataFlavor, $DataFlavor$Type} from "packages/java/awt/datatransfer/$DataFlavor"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$DropTargetEvent, $DropTargetEvent$Type} from "packages/java/awt/dnd/$DropTargetEvent"
import {$Transferable, $Transferable$Type} from "packages/java/awt/datatransfer/$Transferable"

export class $DropTargetDropEvent extends $DropTargetEvent {

constructor(arg0: $DropTargetContext$Type, arg1: $Point$Type, arg2: integer, arg3: integer)
constructor(arg0: $DropTargetContext$Type, arg1: $Point$Type, arg2: integer, arg3: integer, arg4: boolean)

public "getLocation"(): $Point
public "isDataFlavorSupported"(arg0: $DataFlavor$Type): boolean
public "getDropAction"(): integer
public "getCurrentDataFlavors"(): ($DataFlavor)[]
public "getCurrentDataFlavorsAsList"(): $List<($DataFlavor)>
public "acceptDrop"(arg0: integer): void
public "dropComplete"(arg0: boolean): void
public "isLocalTransfer"(): boolean
public "getSourceActions"(): integer
public "getTransferable"(): $Transferable
public "rejectDrop"(): void
get "location"(): $Point
get "dropAction"(): integer
get "currentDataFlavors"(): ($DataFlavor)[]
get "currentDataFlavorsAsList"(): $List<($DataFlavor)>
get "localTransfer"(): boolean
get "sourceActions"(): integer
get "transferable"(): $Transferable
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropTargetDropEvent$Type = ($DropTargetDropEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropTargetDropEvent_ = $DropTargetDropEvent$Type;
}}
declare module "packages/java/awt/$PopupMenu" {
import {$Event, $Event$Type} from "packages/java/awt/$Event"
import {$MenuContainer, $MenuContainer$Type} from "packages/java/awt/$MenuContainer"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$Menu, $Menu$Type} from "packages/java/awt/$Menu"
import {$Font, $Font$Type} from "packages/java/awt/$Font"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export class $PopupMenu extends $Menu {

constructor()
constructor(arg0: string)

public "getParent"(): $MenuContainer
public "addNotify"(): void
public "getAccessibleContext"(): $AccessibleContext
public "show"(arg0: $Component$Type, arg1: integer, arg2: integer): void
/**
 * 
 * @deprecated
 */
public "postEvent"(arg0: $Event$Type): boolean
public "getFont"(): $Font
get "parent"(): $MenuContainer
get "accessibleContext"(): $AccessibleContext
get "font"(): $Font
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PopupMenu$Type = ($PopupMenu);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PopupMenu_ = $PopupMenu$Type;
}}
declare module "packages/java/awt/$RenderingHints" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$RenderingHints$Key, $RenderingHints$Key$Type} from "packages/java/awt/$RenderingHints$Key"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Map$Entry, $Map$Entry$Type} from "packages/java/util/$Map$Entry"

export class $RenderingHints implements $Map<(any), (any)>, $Cloneable {
static readonly "KEY_ANTIALIASING": $RenderingHints$Key
static readonly "VALUE_ANTIALIAS_ON": any
static readonly "VALUE_ANTIALIAS_OFF": any
static readonly "VALUE_ANTIALIAS_DEFAULT": any
static readonly "KEY_RENDERING": $RenderingHints$Key
static readonly "VALUE_RENDER_SPEED": any
static readonly "VALUE_RENDER_QUALITY": any
static readonly "VALUE_RENDER_DEFAULT": any
static readonly "KEY_DITHERING": $RenderingHints$Key
static readonly "VALUE_DITHER_DISABLE": any
static readonly "VALUE_DITHER_ENABLE": any
static readonly "VALUE_DITHER_DEFAULT": any
static readonly "KEY_TEXT_ANTIALIASING": $RenderingHints$Key
static readonly "VALUE_TEXT_ANTIALIAS_ON": any
static readonly "VALUE_TEXT_ANTIALIAS_OFF": any
static readonly "VALUE_TEXT_ANTIALIAS_DEFAULT": any
static readonly "VALUE_TEXT_ANTIALIAS_GASP": any
static readonly "VALUE_TEXT_ANTIALIAS_LCD_HRGB": any
static readonly "VALUE_TEXT_ANTIALIAS_LCD_HBGR": any
static readonly "VALUE_TEXT_ANTIALIAS_LCD_VRGB": any
static readonly "VALUE_TEXT_ANTIALIAS_LCD_VBGR": any
static readonly "KEY_TEXT_LCD_CONTRAST": $RenderingHints$Key
static readonly "KEY_FRACTIONALMETRICS": $RenderingHints$Key
static readonly "VALUE_FRACTIONALMETRICS_OFF": any
static readonly "VALUE_FRACTIONALMETRICS_ON": any
static readonly "VALUE_FRACTIONALMETRICS_DEFAULT": any
static readonly "KEY_INTERPOLATION": $RenderingHints$Key
static readonly "VALUE_INTERPOLATION_NEAREST_NEIGHBOR": any
static readonly "VALUE_INTERPOLATION_BILINEAR": any
static readonly "VALUE_INTERPOLATION_BICUBIC": any
static readonly "KEY_ALPHA_INTERPOLATION": $RenderingHints$Key
static readonly "VALUE_ALPHA_INTERPOLATION_SPEED": any
static readonly "VALUE_ALPHA_INTERPOLATION_QUALITY": any
static readonly "VALUE_ALPHA_INTERPOLATION_DEFAULT": any
static readonly "KEY_COLOR_RENDERING": $RenderingHints$Key
static readonly "VALUE_COLOR_RENDER_SPEED": any
static readonly "VALUE_COLOR_RENDER_QUALITY": any
static readonly "VALUE_COLOR_RENDER_DEFAULT": any
static readonly "KEY_STROKE_CONTROL": $RenderingHints$Key
static readonly "VALUE_STROKE_DEFAULT": any
static readonly "VALUE_STROKE_NORMALIZE": any
static readonly "VALUE_STROKE_PURE": any
static readonly "KEY_RESOLUTION_VARIANT": $RenderingHints$Key
static readonly "VALUE_RESOLUTION_VARIANT_DEFAULT": any
static readonly "VALUE_RESOLUTION_VARIANT_BASE": any
static readonly "VALUE_RESOLUTION_VARIANT_SIZE_FIT": any
static readonly "VALUE_RESOLUTION_VARIANT_DPI_FIT": any

constructor(arg0: $Map$Type<($RenderingHints$Key$Type), (any)>)
constructor(arg0: $RenderingHints$Key$Type, arg1: any)

public "add"(arg0: $RenderingHints$Type): void
public "remove"(arg0: any): any
public "get"(arg0: any): any
public "put"(arg0: any, arg1: any): any
public "equals"(arg0: any): boolean
public "toString"(): string
public "values"(): $Collection<(any)>
public "hashCode"(): integer
public "clone"(): any
public "clear"(): void
public "isEmpty"(): boolean
public "size"(): integer
public "entrySet"(): $Set<($Map$Entry<(any), (any)>)>
public "putAll"(arg0: $Map$Type<(any), (any)>): void
public "containsKey"(arg0: any): boolean
public "keySet"(): $Set<(any)>
public "containsValue"(arg0: any): boolean
public "remove"(arg0: any, arg1: any): boolean
public static "copyOf"<K, V>(arg0: $Map$Type<(any), (any)>): $Map<(any), (any)>
public "replace"(arg0: any, arg1: any): any
public "replace"(arg0: any, arg1: any, arg2: any): boolean
public "replaceAll"(arg0: $BiFunction$Type<(any), (any), (any)>): void
public static "of"<K, V>(arg0: any, arg1: any, arg2: any, arg3: any, arg4: any, arg5: any, arg6: any, arg7: any, arg8: any, arg9: any): $Map<(any), (any)>
public static "of"<K, V>(arg0: any, arg1: any, arg2: any, arg3: any, arg4: any, arg5: any, arg6: any, arg7: any): $Map<(any), (any)>
public static "of"<K, V>(arg0: any, arg1: any, arg2: any, arg3: any, arg4: any, arg5: any): $Map<(any), (any)>
public static "of"<K, V>(): $Map<(any), (any)>
public static "of"<K, V>(arg0: any, arg1: any, arg2: any, arg3: any): $Map<(any), (any)>
public static "of"<K, V>(arg0: any, arg1: any): $Map<(any), (any)>
public static "of"<K, V>(arg0: any, arg1: any, arg2: any, arg3: any, arg4: any, arg5: any, arg6: any, arg7: any, arg8: any, arg9: any, arg10: any, arg11: any, arg12: any, arg13: any, arg14: any, arg15: any, arg16: any, arg17: any, arg18: any, arg19: any): $Map<(any), (any)>
public static "of"<K, V>(arg0: any, arg1: any, arg2: any, arg3: any, arg4: any, arg5: any, arg6: any, arg7: any, arg8: any, arg9: any, arg10: any, arg11: any, arg12: any, arg13: any, arg14: any, arg15: any, arg16: any, arg17: any): $Map<(any), (any)>
public static "of"<K, V>(arg0: any, arg1: any, arg2: any, arg3: any, arg4: any, arg5: any, arg6: any, arg7: any, arg8: any, arg9: any, arg10: any, arg11: any, arg12: any, arg13: any, arg14: any, arg15: any): $Map<(any), (any)>
public static "of"<K, V>(arg0: any, arg1: any, arg2: any, arg3: any, arg4: any, arg5: any, arg6: any, arg7: any, arg8: any, arg9: any, arg10: any, arg11: any, arg12: any, arg13: any): $Map<(any), (any)>
public static "of"<K, V>(arg0: any, arg1: any, arg2: any, arg3: any, arg4: any, arg5: any, arg6: any, arg7: any, arg8: any, arg9: any, arg10: any, arg11: any): $Map<(any), (any)>
public "merge"(arg0: any, arg1: any, arg2: $BiFunction$Type<(any), (any), (any)>): any
public "putIfAbsent"(arg0: any, arg1: any): any
public "compute"(arg0: any, arg1: $BiFunction$Type<(any), (any), (any)>): any
public static "entry"<K, V>(arg0: any, arg1: any): $Map$Entry<(any), (any)>
public "forEach"(arg0: $BiConsumer$Type<(any), (any)>): void
public "computeIfAbsent"(arg0: any, arg1: $Function$Type<(any), (any)>): any
public "getOrDefault"(arg0: any, arg1: any): any
public "computeIfPresent"(arg0: any, arg1: $BiFunction$Type<(any), (any), (any)>): any
public static "ofEntries"<K, V>(...arg0: ($Map$Entry$Type<(any), (any)>)[]): $Map<(any), (any)>
[index: string | number]: any
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderingHints$Type = ($RenderingHints);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderingHints_ = $RenderingHints$Type;
}}
declare module "packages/java/awt/event/$InputEvent" {
import {$ComponentEvent, $ComponentEvent$Type} from "packages/java/awt/event/$ComponentEvent"

export class $InputEvent extends $ComponentEvent {
/**
 * 
 * @deprecated
 */
static readonly "SHIFT_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "CTRL_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "META_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "ALT_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "ALT_GRAPH_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "BUTTON1_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "BUTTON2_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "BUTTON3_MASK": integer
static readonly "SHIFT_DOWN_MASK": integer
static readonly "CTRL_DOWN_MASK": integer
static readonly "META_DOWN_MASK": integer
static readonly "ALT_DOWN_MASK": integer
static readonly "BUTTON1_DOWN_MASK": integer
static readonly "BUTTON2_DOWN_MASK": integer
static readonly "BUTTON3_DOWN_MASK": integer
static readonly "ALT_GRAPH_DOWN_MASK": integer
static readonly "COMPONENT_FIRST": integer
static readonly "COMPONENT_LAST": integer
static readonly "COMPONENT_MOVED": integer
static readonly "COMPONENT_RESIZED": integer
static readonly "COMPONENT_SHOWN": integer
static readonly "COMPONENT_HIDDEN": integer
static readonly "COMPONENT_EVENT_MASK": long
static readonly "CONTAINER_EVENT_MASK": long
static readonly "FOCUS_EVENT_MASK": long
static readonly "KEY_EVENT_MASK": long
static readonly "MOUSE_EVENT_MASK": long
static readonly "MOUSE_MOTION_EVENT_MASK": long
static readonly "WINDOW_EVENT_MASK": long
static readonly "ACTION_EVENT_MASK": long
static readonly "ADJUSTMENT_EVENT_MASK": long
static readonly "ITEM_EVENT_MASK": long
static readonly "TEXT_EVENT_MASK": long
static readonly "INPUT_METHOD_EVENT_MASK": long
static readonly "PAINT_EVENT_MASK": long
static readonly "INVOCATION_EVENT_MASK": long
static readonly "HIERARCHY_EVENT_MASK": long
static readonly "HIERARCHY_BOUNDS_EVENT_MASK": long
static readonly "MOUSE_WHEEL_EVENT_MASK": long
static readonly "WINDOW_STATE_EVENT_MASK": long
static readonly "WINDOW_FOCUS_EVENT_MASK": long
static readonly "RESERVED_ID_MAX": integer


/**
 * 
 * @deprecated
 */
public "getModifiers"(): integer
public "consume"(): void
public "isShiftDown"(): boolean
public "isAltDown"(): boolean
public "isConsumed"(): boolean
public static "getMaskForButton"(arg0: integer): integer
public static "getModifiersExText"(arg0: integer): string
public "isMetaDown"(): boolean
public "isAltGraphDown"(): boolean
public "isControlDown"(): boolean
public "getModifiersEx"(): integer
public "getWhen"(): long
get "modifiers"(): integer
get "shiftDown"(): boolean
get "altDown"(): boolean
get "consumed"(): boolean
get "metaDown"(): boolean
get "altGraphDown"(): boolean
get "controlDown"(): boolean
get "modifiersEx"(): integer
get "when"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputEvent$Type = ($InputEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputEvent_ = $InputEvent$Type;
}}
declare module "packages/java/awt/$Insets" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"

export class $Insets implements $Cloneable, $Serializable {
 "top": integer
 "left": integer
 "bottom": integer
 "right": integer

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "set"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Insets$Type = ($Insets);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Insets_ = $Insets$Type;
}}
declare module "packages/java/awt/event/$ContainerListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$ContainerEvent, $ContainerEvent$Type} from "packages/java/awt/event/$ContainerEvent"

export interface $ContainerListener extends $EventListener {

 "componentAdded"(arg0: $ContainerEvent$Type): void
 "componentRemoved"(arg0: $ContainerEvent$Type): void
}

export namespace $ContainerListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContainerListener$Type = ($ContainerListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContainerListener_ = $ContainerListener$Type;
}}
declare module "packages/java/awt/dnd/$DragSourceMotionListener" {
import {$DragSourceDragEvent, $DragSourceDragEvent$Type} from "packages/java/awt/dnd/$DragSourceDragEvent"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"

export interface $DragSourceMotionListener extends $EventListener {

 "dragMouseMoved"(arg0: $DragSourceDragEvent$Type): void

(arg0: $DragSourceDragEvent$Type): void
}

export namespace $DragSourceMotionListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DragSourceMotionListener$Type = ($DragSourceMotionListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DragSourceMotionListener_ = $DragSourceMotionListener$Type;
}}
declare module "packages/java/awt/$JobAttributes" {
import {$JobAttributes$DefaultSelectionType, $JobAttributes$DefaultSelectionType$Type} from "packages/java/awt/$JobAttributes$DefaultSelectionType"
import {$JobAttributes$DialogType, $JobAttributes$DialogType$Type} from "packages/java/awt/$JobAttributes$DialogType"
import {$JobAttributes$SidesType, $JobAttributes$SidesType$Type} from "packages/java/awt/$JobAttributes$SidesType"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$JobAttributes$DestinationType, $JobAttributes$DestinationType$Type} from "packages/java/awt/$JobAttributes$DestinationType"
import {$JobAttributes$MultipleDocumentHandlingType, $JobAttributes$MultipleDocumentHandlingType$Type} from "packages/java/awt/$JobAttributes$MultipleDocumentHandlingType"

export class $JobAttributes implements $Cloneable {

constructor(arg0: integer, arg1: $JobAttributes$DefaultSelectionType$Type, arg2: $JobAttributes$DestinationType$Type, arg3: $JobAttributes$DialogType$Type, arg4: string, arg5: integer, arg6: integer, arg7: $JobAttributes$MultipleDocumentHandlingType$Type, arg8: ((integer)[])[], arg9: string, arg10: $JobAttributes$SidesType$Type)
constructor(arg0: $JobAttributes$Type)
constructor()

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "set"(arg0: $JobAttributes$Type): void
public "getFileName"(): string
public "setDestination"(arg0: $JobAttributes$DestinationType$Type): void
public "setCopiesToDefault"(): void
public "setDefaultSelection"(arg0: $JobAttributes$DefaultSelectionType$Type): void
public "setDialog"(arg0: $JobAttributes$DialogType$Type): void
public "setMaxPage"(arg0: integer): void
public "setMinPage"(arg0: integer): void
public "setMultipleDocumentHandlingToDefault"(): void
public "setSidesToDefault"(): void
public "setCopies"(arg0: integer): void
public "setMultipleDocumentHandling"(arg0: $JobAttributes$MultipleDocumentHandlingType$Type): void
public "setPageRanges"(arg0: ((integer)[])[]): void
public "setPrinter"(arg0: string): void
public "setSides"(arg0: $JobAttributes$SidesType$Type): void
public "getMinPage"(): integer
public "getFromPage"(): integer
public "getToPage"(): integer
public "getPageRanges"(): ((integer)[])[]
public "getCopies"(): integer
public "getDefaultSelection"(): $JobAttributes$DefaultSelectionType
public "getMultipleDocumentHandling"(): $JobAttributes$MultipleDocumentHandlingType
public "getSides"(): $JobAttributes$SidesType
public "setFromPage"(arg0: integer): void
public "setToPage"(arg0: integer): void
public "setFileName"(arg0: string): void
public "getDestination"(): $JobAttributes$DestinationType
public "getDialog"(): $JobAttributes$DialogType
public "getMaxPage"(): integer
public "getPrinter"(): string
get "fileName"(): string
set "destination"(value: $JobAttributes$DestinationType$Type)
set "defaultSelection"(value: $JobAttributes$DefaultSelectionType$Type)
set "dialog"(value: $JobAttributes$DialogType$Type)
set "maxPage"(value: integer)
set "minPage"(value: integer)
set "copies"(value: integer)
set "multipleDocumentHandling"(value: $JobAttributes$MultipleDocumentHandlingType$Type)
set "pageRanges"(value: ((integer)[])[])
set "printer"(value: string)
set "sides"(value: $JobAttributes$SidesType$Type)
get "minPage"(): integer
get "fromPage"(): integer
get "toPage"(): integer
get "pageRanges"(): ((integer)[])[]
get "copies"(): integer
get "defaultSelection"(): $JobAttributes$DefaultSelectionType
get "multipleDocumentHandling"(): $JobAttributes$MultipleDocumentHandlingType
get "sides"(): $JobAttributes$SidesType
set "fromPage"(value: integer)
set "toPage"(value: integer)
set "fileName"(value: string)
get "destination"(): $JobAttributes$DestinationType
get "dialog"(): $JobAttributes$DialogType
get "maxPage"(): integer
get "printer"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JobAttributes$Type = ($JobAttributes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JobAttributes_ = $JobAttributes$Type;
}}
declare module "packages/java/awt/event/$ActionEvent" {
import {$AWTEvent, $AWTEvent$Type} from "packages/java/awt/$AWTEvent"

export class $ActionEvent extends $AWTEvent {
static readonly "SHIFT_MASK": integer
static readonly "CTRL_MASK": integer
static readonly "META_MASK": integer
static readonly "ALT_MASK": integer
static readonly "ACTION_FIRST": integer
static readonly "ACTION_LAST": integer
static readonly "ACTION_PERFORMED": integer
static readonly "COMPONENT_EVENT_MASK": long
static readonly "CONTAINER_EVENT_MASK": long
static readonly "FOCUS_EVENT_MASK": long
static readonly "KEY_EVENT_MASK": long
static readonly "MOUSE_EVENT_MASK": long
static readonly "MOUSE_MOTION_EVENT_MASK": long
static readonly "WINDOW_EVENT_MASK": long
static readonly "ACTION_EVENT_MASK": long
static readonly "ADJUSTMENT_EVENT_MASK": long
static readonly "ITEM_EVENT_MASK": long
static readonly "TEXT_EVENT_MASK": long
static readonly "INPUT_METHOD_EVENT_MASK": long
static readonly "PAINT_EVENT_MASK": long
static readonly "INVOCATION_EVENT_MASK": long
static readonly "HIERARCHY_EVENT_MASK": long
static readonly "HIERARCHY_BOUNDS_EVENT_MASK": long
static readonly "MOUSE_WHEEL_EVENT_MASK": long
static readonly "WINDOW_STATE_EVENT_MASK": long
static readonly "WINDOW_FOCUS_EVENT_MASK": long
static readonly "RESERVED_ID_MAX": integer

constructor(arg0: any, arg1: integer, arg2: string, arg3: long, arg4: integer)
constructor(arg0: any, arg1: integer, arg2: string, arg3: integer)
constructor(arg0: any, arg1: integer, arg2: string)

public "getModifiers"(): integer
public "paramString"(): string
public "getActionCommand"(): string
public "getWhen"(): long
get "modifiers"(): integer
get "actionCommand"(): string
get "when"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ActionEvent$Type = ($ActionEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ActionEvent_ = $ActionEvent$Type;
}}
declare module "packages/java/awt/event/$HierarchyBoundsListener" {
import {$HierarchyEvent, $HierarchyEvent$Type} from "packages/java/awt/event/$HierarchyEvent"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"

export interface $HierarchyBoundsListener extends $EventListener {

 "ancestorMoved"(arg0: $HierarchyEvent$Type): void
 "ancestorResized"(arg0: $HierarchyEvent$Type): void
}

export namespace $HierarchyBoundsListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HierarchyBoundsListener$Type = ($HierarchyBoundsListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HierarchyBoundsListener_ = $HierarchyBoundsListener$Type;
}}
declare module "packages/java/awt/image/$DataBuffer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $DataBuffer {
static readonly "TYPE_BYTE": integer
static readonly "TYPE_USHORT": integer
static readonly "TYPE_SHORT": integer
static readonly "TYPE_INT": integer
static readonly "TYPE_FLOAT": integer
static readonly "TYPE_DOUBLE": integer
static readonly "TYPE_UNDEFINED": integer


public "getSize"(): integer
public "getOffset"(): integer
public "getOffsets"(): (integer)[]
public "getDataType"(): integer
public "setElem"(arg0: integer, arg1: integer, arg2: integer): void
public "setElem"(arg0: integer, arg1: integer): void
public "getElem"(arg0: integer, arg1: integer): integer
public "getElem"(arg0: integer): integer
public static "getDataTypeSize"(arg0: integer): integer
public "getNumBanks"(): integer
public "getElemFloat"(arg0: integer, arg1: integer): float
public "getElemFloat"(arg0: integer): float
public "setElemFloat"(arg0: integer, arg1: integer, arg2: float): void
public "setElemFloat"(arg0: integer, arg1: float): void
public "getElemDouble"(arg0: integer): double
public "getElemDouble"(arg0: integer, arg1: integer): double
public "setElemDouble"(arg0: integer, arg1: double): void
public "setElemDouble"(arg0: integer, arg1: integer, arg2: double): void
get "size"(): integer
get "offset"(): integer
get "offsets"(): (integer)[]
get "dataType"(): integer
get "numBanks"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataBuffer$Type = ($DataBuffer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataBuffer_ = $DataBuffer$Type;
}}
declare module "packages/java/awt/$PageAttributes" {
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$PageAttributes$MediaType, $PageAttributes$MediaType$Type} from "packages/java/awt/$PageAttributes$MediaType"
import {$PageAttributes$ColorType, $PageAttributes$ColorType$Type} from "packages/java/awt/$PageAttributes$ColorType"
import {$PageAttributes$OrientationRequestedType, $PageAttributes$OrientationRequestedType$Type} from "packages/java/awt/$PageAttributes$OrientationRequestedType"
import {$PageAttributes$OriginType, $PageAttributes$OriginType$Type} from "packages/java/awt/$PageAttributes$OriginType"
import {$PageAttributes$PrintQualityType, $PageAttributes$PrintQualityType$Type} from "packages/java/awt/$PageAttributes$PrintQualityType"

export class $PageAttributes implements $Cloneable {

constructor(arg0: $PageAttributes$ColorType$Type, arg1: $PageAttributes$MediaType$Type, arg2: $PageAttributes$OrientationRequestedType$Type, arg3: $PageAttributes$OriginType$Type, arg4: $PageAttributes$PrintQualityType$Type, arg5: (integer)[])
constructor(arg0: $PageAttributes$Type)
constructor()

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "clone"(): any
public "set"(arg0: $PageAttributes$Type): void
public "setColor"(arg0: $PageAttributes$ColorType$Type): void
public "setOrigin"(arg0: $PageAttributes$OriginType$Type): void
public "setMediaToDefault"(): void
public "setOrientationRequestedToDefault"(): void
public "setPrintQualityToDefault"(): void
public "setPrinterResolutionToDefault"(): void
public "setMedia"(arg0: $PageAttributes$MediaType$Type): void
public "setOrientationRequested"(arg0: integer): void
public "setOrientationRequested"(arg0: $PageAttributes$OrientationRequestedType$Type): void
public "setPrintQuality"(arg0: integer): void
public "setPrintQuality"(arg0: $PageAttributes$PrintQualityType$Type): void
public "setPrinterResolution"(arg0: (integer)[]): void
public "setPrinterResolution"(arg0: integer): void
public "getMedia"(): $PageAttributes$MediaType
public "getOrientationRequested"(): $PageAttributes$OrientationRequestedType
public "getPrintQuality"(): $PageAttributes$PrintQualityType
public "getPrinterResolution"(): (integer)[]
public "getColor"(): $PageAttributes$ColorType
public "getOrigin"(): $PageAttributes$OriginType
set "color"(value: $PageAttributes$ColorType$Type)
set "origin"(value: $PageAttributes$OriginType$Type)
set "media"(value: $PageAttributes$MediaType$Type)
set "orientationRequested"(value: integer)
set "orientationRequested"(value: $PageAttributes$OrientationRequestedType$Type)
set "printQuality"(value: integer)
set "printQuality"(value: $PageAttributes$PrintQualityType$Type)
set "printerResolution"(value: (integer)[])
set "printerResolution"(value: integer)
get "media"(): $PageAttributes$MediaType
get "orientationRequested"(): $PageAttributes$OrientationRequestedType
get "printQuality"(): $PageAttributes$PrintQualityType
get "printerResolution"(): (integer)[]
get "color"(): $PageAttributes$ColorType
get "origin"(): $PageAttributes$OriginType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageAttributes$Type = ($PageAttributes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageAttributes_ = $PageAttributes$Type;
}}
declare module "packages/java/awt/geom/$Line2D" {
import {$Point2D, $Point2D$Type} from "packages/java/awt/geom/$Point2D"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$PathIterator, $PathIterator$Type} from "packages/java/awt/geom/$PathIterator"
import {$Rectangle2D, $Rectangle2D$Type} from "packages/java/awt/geom/$Rectangle2D"
import {$Shape, $Shape$Type} from "packages/java/awt/$Shape"
import {$AffineTransform, $AffineTransform$Type} from "packages/java/awt/geom/$AffineTransform"

export class $Line2D implements $Shape, $Cloneable {


public "clone"(): any
public "contains"(arg0: double, arg1: double): boolean
public "contains"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
public "contains"(arg0: $Rectangle2D$Type): boolean
public "contains"(arg0: $Point2D$Type): boolean
public "getBounds"(): $Rectangle
public "intersects"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
public "intersects"(arg0: $Rectangle2D$Type): boolean
public "relativeCCW"(arg0: $Point2D$Type): integer
public "relativeCCW"(arg0: double, arg1: double): integer
public static "relativeCCW"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): integer
public static "linesIntersect"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double, arg6: double, arg7: double): boolean
public static "ptSegDistSq"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): double
public "ptSegDistSq"(arg0: double, arg1: double): double
public "ptSegDistSq"(arg0: $Point2D$Type): double
public "ptSegDist"(arg0: $Point2D$Type): double
public "ptSegDist"(arg0: double, arg1: double): double
public static "ptSegDist"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): double
public "ptLineDistSq"(arg0: $Point2D$Type): double
public static "ptLineDistSq"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): double
public "ptLineDistSq"(arg0: double, arg1: double): double
public "ptLineDist"(arg0: double, arg1: double): double
public "ptLineDist"(arg0: $Point2D$Type): double
public static "ptLineDist"(arg0: double, arg1: double, arg2: double, arg3: double, arg4: double, arg5: double): double
public "getP1"(): $Point2D
public "getP2"(): $Point2D
public "setLine"(arg0: double, arg1: double, arg2: double, arg3: double): void
public "setLine"(arg0: $Point2D$Type, arg1: $Point2D$Type): void
public "setLine"(arg0: $Line2D$Type): void
public "getPathIterator"(arg0: $AffineTransform$Type): $PathIterator
public "getPathIterator"(arg0: $AffineTransform$Type, arg1: double): $PathIterator
public "getX1"(): double
public "getY1"(): double
public "getX2"(): double
public "getY2"(): double
public "intersectsLine"(arg0: $Line2D$Type): boolean
public "intersectsLine"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
public "getBounds2D"(): $Rectangle2D
get "bounds"(): $Rectangle
get "p1"(): $Point2D
get "p2"(): $Point2D
set "line"(value: $Line2D$Type)
get "x1"(): double
get "y1"(): double
get "x2"(): double
get "y2"(): double
get "bounds2D"(): $Rectangle2D
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Line2D$Type = ($Line2D);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Line2D_ = $Line2D$Type;
}}
declare module "packages/java/awt/dnd/$DragGestureEvent" {
import {$Cursor, $Cursor$Type} from "packages/java/awt/$Cursor"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$InputEvent, $InputEvent$Type} from "packages/java/awt/event/$InputEvent"
import {$DragSource, $DragSource$Type} from "packages/java/awt/dnd/$DragSource"
import {$DragGestureRecognizer, $DragGestureRecognizer$Type} from "packages/java/awt/dnd/$DragGestureRecognizer"
import {$Component, $Component$Type} from "packages/java/awt/$Component"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"
import {$DragSourceListener, $DragSourceListener$Type} from "packages/java/awt/dnd/$DragSourceListener"
import {$Image, $Image$Type} from "packages/java/awt/$Image"
import {$EventObject, $EventObject$Type} from "packages/java/util/$EventObject"
import {$Transferable, $Transferable$Type} from "packages/java/awt/datatransfer/$Transferable"

export class $DragGestureEvent extends $EventObject {

constructor(arg0: $DragGestureRecognizer$Type, arg1: integer, arg2: $Point$Type, arg3: $List$Type<(any)>)

public "toArray"(arg0: (any)[]): (any)[]
public "toArray"(): (any)[]
public "iterator"(): $Iterator<($InputEvent)>
public "getComponent"(): $Component
public "getDragSource"(): $DragSource
public "getSourceAsDragGestureRecognizer"(): $DragGestureRecognizer
public "getDragAction"(): integer
public "getTriggerEvent"(): $InputEvent
public "getDragOrigin"(): $Point
public "startDrag"(arg0: $Cursor$Type, arg1: $Transferable$Type, arg2: $DragSourceListener$Type): void
public "startDrag"(arg0: $Cursor$Type, arg1: $Transferable$Type): void
public "startDrag"(arg0: $Cursor$Type, arg1: $Image$Type, arg2: $Point$Type, arg3: $Transferable$Type, arg4: $DragSourceListener$Type): void
get "component"(): $Component
get "dragSource"(): $DragSource
get "sourceAsDragGestureRecognizer"(): $DragGestureRecognizer
get "dragAction"(): integer
get "triggerEvent"(): $InputEvent
get "dragOrigin"(): $Point
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DragGestureEvent$Type = ($DragGestureEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DragGestureEvent_ = $DragGestureEvent$Type;
}}
declare module "packages/java/awt/dnd/$DragSourceContext" {
import {$DragSourceDropEvent, $DragSourceDropEvent$Type} from "packages/java/awt/dnd/$DragSourceDropEvent"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Cursor, $Cursor$Type} from "packages/java/awt/$Cursor"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$DragSource, $DragSource$Type} from "packages/java/awt/dnd/$DragSource"
import {$Component, $Component$Type} from "packages/java/awt/$Component"
import {$DragSourceDragEvent, $DragSourceDragEvent$Type} from "packages/java/awt/dnd/$DragSourceDragEvent"
import {$DragGestureEvent, $DragGestureEvent$Type} from "packages/java/awt/dnd/$DragGestureEvent"
import {$DragSourceEvent, $DragSourceEvent$Type} from "packages/java/awt/dnd/$DragSourceEvent"
import {$DragSourceListener, $DragSourceListener$Type} from "packages/java/awt/dnd/$DragSourceListener"
import {$Image, $Image$Type} from "packages/java/awt/$Image"
import {$Transferable, $Transferable$Type} from "packages/java/awt/datatransfer/$Transferable"
import {$DragSourceMotionListener, $DragSourceMotionListener$Type} from "packages/java/awt/dnd/$DragSourceMotionListener"

export class $DragSourceContext implements $DragSourceListener, $DragSourceMotionListener, $Serializable {

constructor(arg0: $DragGestureEvent$Type, arg1: $Cursor$Type, arg2: $Image$Type, arg3: $Point$Type, arg4: $Transferable$Type, arg5: $DragSourceListener$Type)

public "getCursor"(): $Cursor
public "getComponent"(): $Component
public "getDragSource"(): $DragSource
public "transferablesFlavorsChanged"(): void
public "getTrigger"(): $DragGestureEvent
public "addDragSourceListener"(arg0: $DragSourceListener$Type): void
public "removeDragSourceListener"(arg0: $DragSourceListener$Type): void
public "dragMouseMoved"(arg0: $DragSourceDragEvent$Type): void
public "dragDropEnd"(arg0: $DragSourceDropEvent$Type): void
public "getSourceActions"(): integer
public "getTransferable"(): $Transferable
public "dragEnter"(arg0: $DragSourceDragEvent$Type): void
public "dragOver"(arg0: $DragSourceDragEvent$Type): void
public "dropActionChanged"(arg0: $DragSourceDragEvent$Type): void
public "dragExit"(arg0: $DragSourceEvent$Type): void
public "setCursor"(arg0: $Cursor$Type): void
get "cursor"(): $Cursor
get "component"(): $Component
get "dragSource"(): $DragSource
get "trigger"(): $DragGestureEvent
get "sourceActions"(): integer
get "transferable"(): $Transferable
set "cursor"(value: $Cursor$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DragSourceContext$Type = ($DragSourceContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DragSourceContext_ = $DragSourceContext$Type;
}}
declare module "packages/java/awt/font/$GlyphJustificationInfo" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $GlyphJustificationInfo {
static readonly "PRIORITY_KASHIDA": integer
static readonly "PRIORITY_WHITESPACE": integer
static readonly "PRIORITY_INTERCHAR": integer
static readonly "PRIORITY_NONE": integer
readonly "weight": float
readonly "growPriority": integer
readonly "growAbsorb": boolean
readonly "growLeftLimit": float
readonly "growRightLimit": float
readonly "shrinkPriority": integer
readonly "shrinkAbsorb": boolean
readonly "shrinkLeftLimit": float
readonly "shrinkRightLimit": float

constructor(arg0: float, arg1: boolean, arg2: integer, arg3: float, arg4: float, arg5: boolean, arg6: integer, arg7: float, arg8: float)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlyphJustificationInfo$Type = ($GlyphJustificationInfo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlyphJustificationInfo_ = $GlyphJustificationInfo$Type;
}}
declare module "packages/java/awt/$Dialog$ModalityType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Dialog$ModalityType extends $Enum<($Dialog$ModalityType)> {
static readonly "MODELESS": $Dialog$ModalityType
static readonly "DOCUMENT_MODAL": $Dialog$ModalityType
static readonly "APPLICATION_MODAL": $Dialog$ModalityType
static readonly "TOOLKIT_MODAL": $Dialog$ModalityType


public static "values"(): ($Dialog$ModalityType)[]
public static "valueOf"(arg0: string): $Dialog$ModalityType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Dialog$ModalityType$Type = (("application_modal") | ("modeless") | ("document_modal") | ("toolkit_modal")) | ($Dialog$ModalityType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Dialog$ModalityType_ = $Dialog$ModalityType$Type;
}}
declare module "packages/java/awt/font/$TextAttribute" {
import {$AttributedCharacterIterator$Attribute, $AttributedCharacterIterator$Attribute$Type} from "packages/java/text/$AttributedCharacterIterator$Attribute"

export class $TextAttribute extends $AttributedCharacterIterator$Attribute {
static readonly "FAMILY": $TextAttribute
static readonly "WEIGHT": $TextAttribute
static readonly "WEIGHT_EXTRA_LIGHT": float
static readonly "WEIGHT_LIGHT": float
static readonly "WEIGHT_DEMILIGHT": float
static readonly "WEIGHT_REGULAR": float
static readonly "WEIGHT_SEMIBOLD": float
static readonly "WEIGHT_MEDIUM": float
static readonly "WEIGHT_DEMIBOLD": float
static readonly "WEIGHT_BOLD": float
static readonly "WEIGHT_HEAVY": float
static readonly "WEIGHT_EXTRABOLD": float
static readonly "WEIGHT_ULTRABOLD": float
static readonly "WIDTH": $TextAttribute
static readonly "WIDTH_CONDENSED": float
static readonly "WIDTH_SEMI_CONDENSED": float
static readonly "WIDTH_REGULAR": float
static readonly "WIDTH_SEMI_EXTENDED": float
static readonly "WIDTH_EXTENDED": float
static readonly "POSTURE": $TextAttribute
static readonly "POSTURE_REGULAR": float
static readonly "POSTURE_OBLIQUE": float
static readonly "SIZE": $TextAttribute
static readonly "TRANSFORM": $TextAttribute
static readonly "SUPERSCRIPT": $TextAttribute
static readonly "SUPERSCRIPT_SUPER": integer
static readonly "SUPERSCRIPT_SUB": integer
static readonly "FONT": $TextAttribute
static readonly "CHAR_REPLACEMENT": $TextAttribute
static readonly "FOREGROUND": $TextAttribute
static readonly "BACKGROUND": $TextAttribute
static readonly "UNDERLINE": $TextAttribute
static readonly "UNDERLINE_ON": integer
static readonly "STRIKETHROUGH": $TextAttribute
static readonly "STRIKETHROUGH_ON": boolean
static readonly "RUN_DIRECTION": $TextAttribute
static readonly "RUN_DIRECTION_LTR": boolean
static readonly "RUN_DIRECTION_RTL": boolean
static readonly "BIDI_EMBEDDING": $TextAttribute
static readonly "JUSTIFICATION": $TextAttribute
static readonly "JUSTIFICATION_FULL": float
static readonly "JUSTIFICATION_NONE": float
static readonly "INPUT_METHOD_HIGHLIGHT": $TextAttribute
static readonly "INPUT_METHOD_UNDERLINE": $TextAttribute
static readonly "UNDERLINE_LOW_ONE_PIXEL": integer
static readonly "UNDERLINE_LOW_TWO_PIXEL": integer
static readonly "UNDERLINE_LOW_DOTTED": integer
static readonly "UNDERLINE_LOW_GRAY": integer
static readonly "UNDERLINE_LOW_DASHED": integer
static readonly "SWAP_COLORS": $TextAttribute
static readonly "SWAP_COLORS_ON": boolean
static readonly "NUMERIC_SHAPING": $TextAttribute
static readonly "KERNING": $TextAttribute
static readonly "KERNING_ON": integer
static readonly "LIGATURES": $TextAttribute
static readonly "LIGATURES_ON": integer
static readonly "TRACKING": $TextAttribute
static readonly "TRACKING_TIGHT": float
static readonly "TRACKING_LOOSE": float
static readonly "LANGUAGE": $AttributedCharacterIterator$Attribute
static readonly "READING": $AttributedCharacterIterator$Attribute
static readonly "INPUT_METHOD_SEGMENT": $AttributedCharacterIterator$Attribute


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextAttribute$Type = ($TextAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextAttribute_ = $TextAttribute$Type;
}}
declare module "packages/java/awt/$ImageCapabilities" {
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"

export class $ImageCapabilities implements $Cloneable {

constructor(arg0: boolean)

public "clone"(): any
public "isAccelerated"(): boolean
public "isTrueVolatile"(): boolean
get "accelerated"(): boolean
get "trueVolatile"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImageCapabilities$Type = ($ImageCapabilities);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImageCapabilities_ = $ImageCapabilities$Type;
}}
declare module "packages/java/awt/image/renderable/$RenderableImage" {
import {$Vector, $Vector$Type} from "packages/java/util/$Vector"
import {$RenderingHints, $RenderingHints$Type} from "packages/java/awt/$RenderingHints"
import {$RenderContext, $RenderContext$Type} from "packages/java/awt/image/renderable/$RenderContext"
import {$RenderedImage, $RenderedImage$Type} from "packages/java/awt/image/$RenderedImage"

export interface $RenderableImage {

 "getProperty"(arg0: string): any
 "getMinX"(): float
 "getMinY"(): float
 "getPropertyNames"(): (string)[]
 "getWidth"(): float
 "getHeight"(): float
 "isDynamic"(): boolean
 "getSources"(): $Vector<($RenderableImage)>
 "createRendering"(arg0: $RenderContext$Type): $RenderedImage
 "createScaledRendering"(arg0: integer, arg1: integer, arg2: $RenderingHints$Type): $RenderedImage
 "createDefaultRendering"(): $RenderedImage
}

export namespace $RenderableImage {
const HINTS_OBSERVED: string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderableImage$Type = ($RenderableImage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderableImage_ = $RenderableImage$Type;
}}
declare module "packages/java/awt/event/$HierarchyListener" {
import {$HierarchyEvent, $HierarchyEvent$Type} from "packages/java/awt/event/$HierarchyEvent"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"

export interface $HierarchyListener extends $EventListener {

 "hierarchyChanged"(arg0: $HierarchyEvent$Type): void

(arg0: $HierarchyEvent$Type): void
}

export namespace $HierarchyListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HierarchyListener$Type = ($HierarchyListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HierarchyListener_ = $HierarchyListener$Type;
}}
declare module "packages/java/awt/$Image" {
import {$GraphicsConfiguration, $GraphicsConfiguration$Type} from "packages/java/awt/$GraphicsConfiguration"
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$ImageObserver, $ImageObserver$Type} from "packages/java/awt/image/$ImageObserver"
import {$ImageProducer, $ImageProducer$Type} from "packages/java/awt/image/$ImageProducer"
import {$ImageCapabilities, $ImageCapabilities$Type} from "packages/java/awt/$ImageCapabilities"

export class $Image {
static readonly "UndefinedProperty": any
static readonly "SCALE_DEFAULT": integer
static readonly "SCALE_FAST": integer
static readonly "SCALE_SMOOTH": integer
static readonly "SCALE_REPLICATE": integer
static readonly "SCALE_AREA_AVERAGING": integer


public "getProperty"(arg0: string, arg1: $ImageObserver$Type): any
public "flush"(): void
public "getSource"(): $ImageProducer
public "getCapabilities"(arg0: $GraphicsConfiguration$Type): $ImageCapabilities
public "getGraphics"(): $Graphics
public "getWidth"(arg0: $ImageObserver$Type): integer
public "getHeight"(arg0: $ImageObserver$Type): integer
public "setAccelerationPriority"(arg0: float): void
public "getScaledInstance"(arg0: integer, arg1: integer, arg2: integer): $Image
public "getAccelerationPriority"(): float
get "source"(): $ImageProducer
get "graphics"(): $Graphics
set "accelerationPriority"(value: float)
get "accelerationPriority"(): float
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Image$Type = ($Image);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Image_ = $Image$Type;
}}
declare module "packages/java/awt/$Frame" {
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$Font, $Font$Type} from "packages/java/awt/$Font"
import {$Shape, $Shape$Type} from "packages/java/awt/$Shape"
import {$Color, $Color$Type} from "packages/java/awt/$Color"
import {$MenuBar, $MenuBar$Type} from "packages/java/awt/$MenuBar"
import {$Event, $Event$Type} from "packages/java/awt/$Event"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$MenuContainer, $MenuContainer$Type} from "packages/java/awt/$MenuContainer"
import {$Window, $Window$Type} from "packages/java/awt/$Window"
import {$MenuComponent, $MenuComponent$Type} from "packages/java/awt/$MenuComponent"
import {$GraphicsConfiguration, $GraphicsConfiguration$Type} from "packages/java/awt/$GraphicsConfiguration"
import {$Image, $Image$Type} from "packages/java/awt/$Image"

export class $Frame extends $Window implements $MenuContainer {
/**
 * 
 * @deprecated
 */
static readonly "DEFAULT_CURSOR": integer
/**
 * 
 * @deprecated
 */
static readonly "CROSSHAIR_CURSOR": integer
/**
 * 
 * @deprecated
 */
static readonly "TEXT_CURSOR": integer
/**
 * 
 * @deprecated
 */
static readonly "WAIT_CURSOR": integer
/**
 * 
 * @deprecated
 */
static readonly "SW_RESIZE_CURSOR": integer
/**
 * 
 * @deprecated
 */
static readonly "SE_RESIZE_CURSOR": integer
/**
 * 
 * @deprecated
 */
static readonly "NW_RESIZE_CURSOR": integer
/**
 * 
 * @deprecated
 */
static readonly "NE_RESIZE_CURSOR": integer
/**
 * 
 * @deprecated
 */
static readonly "N_RESIZE_CURSOR": integer
/**
 * 
 * @deprecated
 */
static readonly "S_RESIZE_CURSOR": integer
/**
 * 
 * @deprecated
 */
static readonly "W_RESIZE_CURSOR": integer
/**
 * 
 * @deprecated
 */
static readonly "E_RESIZE_CURSOR": integer
/**
 * 
 * @deprecated
 */
static readonly "HAND_CURSOR": integer
/**
 * 
 * @deprecated
 */
static readonly "MOVE_CURSOR": integer
static readonly "NORMAL": integer
static readonly "ICONIFIED": integer
static readonly "MAXIMIZED_HORIZ": integer
static readonly "MAXIMIZED_VERT": integer
static readonly "MAXIMIZED_BOTH": integer
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float

constructor(arg0: string, arg1: $GraphicsConfiguration$Type)
constructor(arg0: string)
constructor(arg0: $GraphicsConfiguration$Type)
constructor()

public "remove"(arg0: $MenuComponent$Type): void
public "getState"(): integer
public "setState"(arg0: integer): void
public "setOpacity"(arg0: float): void
public "setUndecorated"(arg0: boolean): void
public static "getFrames"(): ($Frame)[]
public "setTitle"(arg0: string): void
public "removeNotify"(): void
public "addNotify"(): void
public "getTitle"(): string
public "getAccessibleContext"(): $AccessibleContext
public "setBackground"(arg0: $Color$Type): void
public "setIconImage"(arg0: $Image$Type): void
public "setMenuBar"(arg0: $MenuBar$Type): void
public "setMaximizedBounds"(arg0: $Rectangle$Type): void
public "setResizable"(arg0: boolean): void
public "getExtendedState"(): integer
public "setExtendedState"(arg0: integer): void
public "isUndecorated"(): boolean
public "getIconImage"(): $Image
public "getMenuBar"(): $MenuBar
public "isResizable"(): boolean
public "getMaximizedBounds"(): $Rectangle
/**
 * 
 * @deprecated
 */
public "getCursorType"(): integer
/**
 * 
 * @deprecated
 */
public "setCursor"(arg0: integer): void
public "setShape"(arg0: $Shape$Type): void
/**
 * 
 * @deprecated
 */
public "postEvent"(arg0: $Event$Type): boolean
public "getFont"(): $Font
get "state"(): integer
set "state"(value: integer)
set "opacity"(value: float)
set "undecorated"(value: boolean)
get "frames"(): ($Frame)[]
set "title"(value: string)
get "title"(): string
get "accessibleContext"(): $AccessibleContext
set "background"(value: $Color$Type)
set "iconImage"(value: $Image$Type)
set "menuBar"(value: $MenuBar$Type)
set "maximizedBounds"(value: $Rectangle$Type)
set "resizable"(value: boolean)
get "extendedState"(): integer
set "extendedState"(value: integer)
get "undecorated"(): boolean
get "iconImage"(): $Image
get "menuBar"(): $MenuBar
get "resizable"(): boolean
get "maximizedBounds"(): $Rectangle
get "cursorType"(): integer
set "cursor"(value: integer)
set "shape"(value: $Shape$Type)
get "font"(): $Font
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Frame$Type = ($Frame);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Frame_ = $Frame$Type;
}}
declare module "packages/java/awt/$GraphicsDevice$WindowTranslucency" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $GraphicsDevice$WindowTranslucency extends $Enum<($GraphicsDevice$WindowTranslucency)> {
static readonly "PERPIXEL_TRANSPARENT": $GraphicsDevice$WindowTranslucency
static readonly "TRANSLUCENT": $GraphicsDevice$WindowTranslucency
static readonly "PERPIXEL_TRANSLUCENT": $GraphicsDevice$WindowTranslucency


public static "values"(): ($GraphicsDevice$WindowTranslucency)[]
public static "valueOf"(arg0: string): $GraphicsDevice$WindowTranslucency
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GraphicsDevice$WindowTranslucency$Type = (("perpixel_transparent") | ("translucent") | ("perpixel_translucent")) | ($GraphicsDevice$WindowTranslucency);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GraphicsDevice$WindowTranslucency_ = $GraphicsDevice$WindowTranslucency$Type;
}}
declare module "packages/java/awt/event/$FocusEvent" {
import {$FocusEvent$Cause, $FocusEvent$Cause$Type} from "packages/java/awt/event/$FocusEvent$Cause"
import {$Component, $Component$Type} from "packages/java/awt/$Component"
import {$ComponentEvent, $ComponentEvent$Type} from "packages/java/awt/event/$ComponentEvent"

export class $FocusEvent extends $ComponentEvent {
static readonly "FOCUS_FIRST": integer
static readonly "FOCUS_LAST": integer
static readonly "FOCUS_GAINED": integer
static readonly "FOCUS_LOST": integer
static readonly "COMPONENT_FIRST": integer
static readonly "COMPONENT_LAST": integer
static readonly "COMPONENT_MOVED": integer
static readonly "COMPONENT_RESIZED": integer
static readonly "COMPONENT_SHOWN": integer
static readonly "COMPONENT_HIDDEN": integer
static readonly "COMPONENT_EVENT_MASK": long
static readonly "CONTAINER_EVENT_MASK": long
static readonly "FOCUS_EVENT_MASK": long
static readonly "KEY_EVENT_MASK": long
static readonly "MOUSE_EVENT_MASK": long
static readonly "MOUSE_MOTION_EVENT_MASK": long
static readonly "WINDOW_EVENT_MASK": long
static readonly "ACTION_EVENT_MASK": long
static readonly "ADJUSTMENT_EVENT_MASK": long
static readonly "ITEM_EVENT_MASK": long
static readonly "TEXT_EVENT_MASK": long
static readonly "INPUT_METHOD_EVENT_MASK": long
static readonly "PAINT_EVENT_MASK": long
static readonly "INVOCATION_EVENT_MASK": long
static readonly "HIERARCHY_EVENT_MASK": long
static readonly "HIERARCHY_BOUNDS_EVENT_MASK": long
static readonly "MOUSE_WHEEL_EVENT_MASK": long
static readonly "WINDOW_STATE_EVENT_MASK": long
static readonly "WINDOW_FOCUS_EVENT_MASK": long
static readonly "RESERVED_ID_MAX": integer

constructor(arg0: $Component$Type, arg1: integer)
constructor(arg0: $Component$Type, arg1: integer, arg2: boolean)
constructor(arg0: $Component$Type, arg1: integer, arg2: boolean, arg3: $Component$Type, arg4: $FocusEvent$Cause$Type)
constructor(arg0: $Component$Type, arg1: integer, arg2: boolean, arg3: $Component$Type)

public "getCause"(): $FocusEvent$Cause
public "paramString"(): string
public "isTemporary"(): boolean
public "getOppositeComponent"(): $Component
get "cause"(): $FocusEvent$Cause
get "temporary"(): boolean
get "oppositeComponent"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FocusEvent$Type = ($FocusEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FocusEvent_ = $FocusEvent$Type;
}}
declare module "packages/java/awt/$Window" {
import {$Dialog$ModalExclusionType, $Dialog$ModalExclusionType$Type} from "packages/java/awt/$Dialog$ModalExclusionType"
import {$AWTKeyStroke, $AWTKeyStroke$Type} from "packages/java/awt/$AWTKeyStroke"
import {$WindowListener, $WindowListener$Type} from "packages/java/awt/event/$WindowListener"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$WindowStateListener, $WindowStateListener$Type} from "packages/java/awt/event/$WindowStateListener"
import {$BufferCapabilities, $BufferCapabilities$Type} from "packages/java/awt/$BufferCapabilities"
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$Component, $Component$Type} from "packages/java/awt/$Component"
import {$BufferStrategy, $BufferStrategy$Type} from "packages/java/awt/image/$BufferStrategy"
import {$Color, $Color$Type} from "packages/java/awt/$Color"
import {$WindowFocusListener, $WindowFocusListener$Type} from "packages/java/awt/event/$WindowFocusListener"
import {$Event, $Event$Type} from "packages/java/awt/$Event"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$List, $List$Type} from "packages/java/util/$List"
import {$GraphicsConfiguration, $GraphicsConfiguration$Type} from "packages/java/awt/$GraphicsConfiguration"
import {$Window$Type, $Window$Type$Type} from "packages/java/awt/$Window$Type"
import {$InputContext, $InputContext$Type} from "packages/java/awt/im/$InputContext"
import {$Image, $Image$Type} from "packages/java/awt/$Image"
import {$PropertyChangeListener, $PropertyChangeListener$Type} from "packages/java/beans/$PropertyChangeListener"
import {$Cursor, $Cursor$Type} from "packages/java/awt/$Cursor"
import {$Container, $Container$Type} from "packages/java/awt/$Container"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$Shape, $Shape$Type} from "packages/java/awt/$Shape"
import {$ResourceBundle, $ResourceBundle$Type} from "packages/java/util/$ResourceBundle"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"
import {$Frame, $Frame$Type} from "packages/java/awt/$Frame"
import {$Toolkit, $Toolkit$Type} from "packages/java/awt/$Toolkit"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Dimension, $Dimension$Type} from "packages/java/awt/$Dimension"

export class $Window extends $Container implements $Accessible {
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float

constructor(arg0: $Frame$Type)
constructor(arg0: $Window$Type)
constructor(arg0: $Window$Type, arg1: $GraphicsConfiguration$Type)

public "getType"(): $Window$Type
public "isOpaque"(): boolean
public "isActive"(): boolean
public "getOwner"(): $Window
public "getLocale"(): $Locale
public "setSize"(arg0: $Dimension$Type): void
public "setSize"(arg0: integer, arg1: integer): void
public "dispose"(): void
public "getShape"(): $Shape
public "setType"(arg0: $Window$Type$Type): void
public "pack"(): void
public "setOpacity"(arg0: float): void
public "setVisible"(arg0: boolean): void
public "getListeners"<T extends $EventListener>(arg0: $Class$Type<(T)>): (T)[]
/**
 * 
 * @deprecated
 */
public "hide"(): void
public "isAlwaysOnTopSupported"(): boolean
/**
 * 
 * @deprecated
 */
public "postEvent"(arg0: $Event$Type): boolean
public "removeNotify"(): void
public "addNotify"(): void
public "isShowing"(): boolean
public "getInputContext"(): $InputContext
public "isValidateRoot"(): boolean
public "createBufferStrategy"(arg0: integer): void
public "createBufferStrategy"(arg0: integer, arg1: $BufferCapabilities$Type): void
public "setLocation"(arg0: integer, arg1: integer): void
public "setLocation"(arg0: $Point$Type): void
public "getFocusTraversalKeys"(arg0: integer): $Set<($AWTKeyStroke)>
public "isFocusableWindow"(): boolean
public "getMostRecentFocusOwner"(): $Component
public "getFocusOwner"(): $Component
public "isFocusCycleRoot"(): boolean
public "getFocusCycleRootAncestor"(): $Container
public "getAccessibleContext"(): $AccessibleContext
public "setMinimumSize"(arg0: $Dimension$Type): void
public "getBufferStrategy"(): $BufferStrategy
public "getBackground"(): $Color
public "setBackground"(arg0: $Color$Type): void
public "getToolkit"(): $Toolkit
/**
 * 
 * @deprecated
 */
public "show"(): void
public "paint"(arg0: $Graphics$Type): void
public "setBounds"(arg0: $Rectangle$Type): void
public "setBounds"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "isFocused"(): boolean
public "setIconImage"(arg0: $Image$Type): void
public "setLocationByPlatform"(arg0: boolean): void
public "isAlwaysOnTop"(): boolean
public "setAlwaysOnTop"(arg0: boolean): void
public "setIconImages"(arg0: $List$Type<(any)>): void
public "toBack"(): void
public "getOwnedWindows"(): ($Window)[]
public "getFocusableWindowState"(): boolean
/**
 * 
 * @deprecated
 */
public "applyResourceBundle"(arg0: $ResourceBundle$Type): void
/**
 * 
 * @deprecated
 */
public "applyResourceBundle"(arg0: string): void
public "addWindowListener"(arg0: $WindowListener$Type): void
public "addWindowFocusListener"(arg0: $WindowFocusListener$Type): void
public "addWindowStateListener"(arg0: $WindowStateListener$Type): void
public "getIconImages"(): $List<($Image)>
public "getWarningString"(): string
public static "getOwnerlessWindows"(): ($Window)[]
public "removeWindowListener"(arg0: $WindowListener$Type): void
public "removeWindowStateListener"(arg0: $WindowStateListener$Type): void
public "removeWindowFocusListener"(arg0: $WindowFocusListener$Type): void
public "getWindowListeners"(): ($WindowListener)[]
public "getWindowFocusListeners"(): ($WindowFocusListener)[]
public "getWindowStateListeners"(): ($WindowStateListener)[]
public "setFocusableWindowState"(arg0: boolean): void
public "setAutoRequestFocus"(arg0: boolean): void
public "isAutoRequestFocus"(): boolean
public "setLocationRelativeTo"(arg0: $Component$Type): void
public "isLocationByPlatform"(): boolean
public "addPropertyChangeListener"(arg0: $PropertyChangeListener$Type): void
public "addPropertyChangeListener"(arg0: string, arg1: $PropertyChangeListener$Type): void
/**
 * 
 * @deprecated
 */
public "reshape"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "setCursor"(arg0: $Cursor$Type): void
public "setShape"(arg0: $Shape$Type): void
public static "getWindows"(): ($Window)[]
public "toFront"(): void
public "setModalExclusionType"(arg0: $Dialog$ModalExclusionType$Type): void
public "getModalExclusionType"(): $Dialog$ModalExclusionType
public "getOpacity"(): float
public "setFocusCycleRoot"(arg0: boolean): void
get "type"(): $Window$Type
get "opaque"(): boolean
get "active"(): boolean
get "owner"(): $Window
get "locale"(): $Locale
set "size"(value: $Dimension$Type)
get "shape"(): $Shape
set "type"(value: $Window$Type$Type)
set "opacity"(value: float)
set "visible"(value: boolean)
get "alwaysOnTopSupported"(): boolean
get "showing"(): boolean
get "inputContext"(): $InputContext
get "validateRoot"(): boolean
set "location"(value: $Point$Type)
get "focusableWindow"(): boolean
get "mostRecentFocusOwner"(): $Component
get "focusOwner"(): $Component
get "focusCycleRoot"(): boolean
get "focusCycleRootAncestor"(): $Container
get "accessibleContext"(): $AccessibleContext
set "minimumSize"(value: $Dimension$Type)
get "bufferStrategy"(): $BufferStrategy
get "background"(): $Color
set "background"(value: $Color$Type)
get "toolkit"(): $Toolkit
set "bounds"(value: $Rectangle$Type)
get "focused"(): boolean
set "iconImage"(value: $Image$Type)
set "locationByPlatform"(value: boolean)
get "alwaysOnTop"(): boolean
set "alwaysOnTop"(value: boolean)
set "iconImages"(value: $List$Type<(any)>)
get "ownedWindows"(): ($Window)[]
get "focusableWindowState"(): boolean
get "iconImages"(): $List<($Image)>
get "warningString"(): string
get "ownerlessWindows"(): ($Window)[]
get "windowListeners"(): ($WindowListener)[]
get "windowFocusListeners"(): ($WindowFocusListener)[]
get "windowStateListeners"(): ($WindowStateListener)[]
set "focusableWindowState"(value: boolean)
set "autoRequestFocus"(value: boolean)
get "autoRequestFocus"(): boolean
set "locationRelativeTo"(value: $Component$Type)
get "locationByPlatform"(): boolean
set "cursor"(value: $Cursor$Type)
set "shape"(value: $Shape$Type)
get "windows"(): ($Window)[]
set "modalExclusionType"(value: $Dialog$ModalExclusionType$Type)
get "modalExclusionType"(): $Dialog$ModalExclusionType
get "opacity"(): float
set "focusCycleRoot"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Window$Type = ($Window);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Window_ = $Window$Type;
}}
declare module "packages/java/awt/$LayoutManager" {
import {$Container, $Container$Type} from "packages/java/awt/$Container"
import {$Dimension, $Dimension$Type} from "packages/java/awt/$Dimension"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export interface $LayoutManager {

 "removeLayoutComponent"(arg0: $Component$Type): void
 "addLayoutComponent"(arg0: string, arg1: $Component$Type): void
 "layoutContainer"(arg0: $Container$Type): void
 "preferredLayoutSize"(arg0: $Container$Type): $Dimension
 "minimumLayoutSize"(arg0: $Container$Type): $Dimension
}

export namespace $LayoutManager {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LayoutManager$Type = ($LayoutManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LayoutManager_ = $LayoutManager$Type;
}}
declare module "packages/java/awt/event/$ComponentListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$ComponentEvent, $ComponentEvent$Type} from "packages/java/awt/event/$ComponentEvent"

export interface $ComponentListener extends $EventListener {

 "componentResized"(arg0: $ComponentEvent$Type): void
 "componentMoved"(arg0: $ComponentEvent$Type): void
 "componentShown"(arg0: $ComponentEvent$Type): void
 "componentHidden"(arg0: $ComponentEvent$Type): void
}

export namespace $ComponentListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComponentListener$Type = ($ComponentListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComponentListener_ = $ComponentListener$Type;
}}
declare module "packages/java/awt/$ItemSelectable" {
import {$ItemListener, $ItemListener$Type} from "packages/java/awt/event/$ItemListener"

export interface $ItemSelectable {

 "removeItemListener"(arg0: $ItemListener$Type): void
 "addItemListener"(arg0: $ItemListener$Type): void
 "getSelectedObjects"(): (any)[]
}

export namespace $ItemSelectable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemSelectable$Type = ($ItemSelectable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemSelectable_ = $ItemSelectable$Type;
}}
declare module "packages/java/awt/image/$ColorModel" {
import {$WritableRaster, $WritableRaster$Type} from "packages/java/awt/image/$WritableRaster"
import {$ColorSpace, $ColorSpace$Type} from "packages/java/awt/color/$ColorSpace"
import {$Raster, $Raster$Type} from "packages/java/awt/image/$Raster"
import {$SampleModel, $SampleModel$Type} from "packages/java/awt/image/$SampleModel"
import {$Transparency, $Transparency$Type} from "packages/java/awt/$Transparency"

export class $ColorModel implements $Transparency {

constructor(arg0: integer)

/**
 * 
 * @deprecated
 */
public "finalize"(): void
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "createCompatibleWritableRaster"(arg0: integer, arg1: integer): $WritableRaster
public static "getRGBdefault"(): $ColorModel
public "hasAlpha"(): boolean
public "isAlphaPremultiplied"(): boolean
public "isCompatibleRaster"(arg0: $Raster$Type): boolean
public "coerceData"(arg0: $WritableRaster$Type, arg1: boolean): $ColorModel
public "getComponentSize"(): (integer)[]
public "getComponentSize"(arg0: integer): integer
public "getPixelSize"(): integer
public "getAlphaRaster"(arg0: $WritableRaster$Type): $WritableRaster
public "getDataElements"(arg0: integer, arg1: any): any
public "getDataElements"(arg0: (integer)[], arg1: integer, arg2: any): any
public "getDataElements"(arg0: (float)[], arg1: integer, arg2: any): any
public "createCompatibleSampleModel"(arg0: integer, arg1: integer): $SampleModel
public "getNumComponents"(): integer
public "getRGB"(arg0: integer): integer
public "getRGB"(arg0: any): integer
public "getRed"(arg0: any): integer
public "getRed"(arg0: integer): integer
public "getGreen"(arg0: integer): integer
public "getGreen"(arg0: any): integer
public "getBlue"(arg0: any): integer
public "getBlue"(arg0: integer): integer
public "getAlpha"(arg0: integer): integer
public "getAlpha"(arg0: any): integer
public "getColorSpace"(): $ColorSpace
public "getTransparency"(): integer
public "getComponents"(arg0: any, arg1: (integer)[], arg2: integer): (integer)[]
public "getComponents"(arg0: integer, arg1: (integer)[], arg2: integer): (integer)[]
public "getUnnormalizedComponents"(arg0: (float)[], arg1: integer, arg2: (integer)[], arg3: integer): (integer)[]
public "getDataElement"(arg0: (integer)[], arg1: integer): integer
public "getDataElement"(arg0: (float)[], arg1: integer): integer
public "getNormalizedComponents"(arg0: any, arg1: (float)[], arg2: integer): (float)[]
public "getNormalizedComponents"(arg0: (integer)[], arg1: integer, arg2: (float)[], arg3: integer): (float)[]
public "getTransferType"(): integer
public "getNumColorComponents"(): integer
public "isCompatibleSampleModel"(arg0: $SampleModel$Type): boolean
get "rGBdefault"(): $ColorModel
get "alphaPremultiplied"(): boolean
get "componentSize"(): (integer)[]
get "pixelSize"(): integer
get "numComponents"(): integer
get "colorSpace"(): $ColorSpace
get "transparency"(): integer
get "transferType"(): integer
get "numColorComponents"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorModel$Type = ($ColorModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorModel_ = $ColorModel$Type;
}}
declare module "packages/java/awt/dnd/$DragSourceEvent" {
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$DragSourceContext, $DragSourceContext$Type} from "packages/java/awt/dnd/$DragSourceContext"
import {$EventObject, $EventObject$Type} from "packages/java/util/$EventObject"

export class $DragSourceEvent extends $EventObject {

constructor(arg0: $DragSourceContext$Type)
constructor(arg0: $DragSourceContext$Type, arg1: integer, arg2: integer)

public "getLocation"(): $Point
public "getY"(): integer
public "getX"(): integer
public "getDragSourceContext"(): $DragSourceContext
get "location"(): $Point
get "y"(): integer
get "x"(): integer
get "dragSourceContext"(): $DragSourceContext
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DragSourceEvent$Type = ($DragSourceEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DragSourceEvent_ = $DragSourceEvent$Type;
}}
declare module "packages/java/awt/event/$KeyListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$KeyEvent, $KeyEvent$Type} from "packages/java/awt/event/$KeyEvent"

export interface $KeyListener extends $EventListener {

 "keyPressed"(arg0: $KeyEvent$Type): void
 "keyTyped"(arg0: $KeyEvent$Type): void
 "keyReleased"(arg0: $KeyEvent$Type): void
}

export namespace $KeyListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyListener$Type = ($KeyListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyListener_ = $KeyListener$Type;
}}
declare module "packages/java/awt/geom/$RectangularShape" {
import {$Point2D, $Point2D$Type} from "packages/java/awt/geom/$Point2D"
import {$Dimension2D, $Dimension2D$Type} from "packages/java/awt/geom/$Dimension2D"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$PathIterator, $PathIterator$Type} from "packages/java/awt/geom/$PathIterator"
import {$Rectangle2D, $Rectangle2D$Type} from "packages/java/awt/geom/$Rectangle2D"
import {$Shape, $Shape$Type} from "packages/java/awt/$Shape"
import {$AffineTransform, $AffineTransform$Type} from "packages/java/awt/geom/$AffineTransform"

export class $RectangularShape implements $Shape, $Cloneable {


public "clone"(): any
public "isEmpty"(): boolean
public "contains"(arg0: $Point2D$Type): boolean
public "contains"(arg0: $Rectangle2D$Type): boolean
public "getBounds"(): $Rectangle
public "intersects"(arg0: $Rectangle2D$Type): boolean
public "getY"(): double
public "getMaxX"(): double
public "getMinX"(): double
public "getMaxY"(): double
public "getMinY"(): double
public "getX"(): double
public "getWidth"(): double
public "getHeight"(): double
public "getFrame"(): $Rectangle2D
public "setFrame"(arg0: double, arg1: double, arg2: double, arg3: double): void
public "setFrame"(arg0: $Rectangle2D$Type): void
public "setFrame"(arg0: $Point2D$Type, arg1: $Dimension2D$Type): void
public "getCenterX"(): double
public "getPathIterator"(arg0: $AffineTransform$Type, arg1: double): $PathIterator
public "setFrameFromDiagonal"(arg0: $Point2D$Type, arg1: $Point2D$Type): void
public "setFrameFromDiagonal"(arg0: double, arg1: double, arg2: double, arg3: double): void
public "setFrameFromCenter"(arg0: double, arg1: double, arg2: double, arg3: double): void
public "setFrameFromCenter"(arg0: $Point2D$Type, arg1: $Point2D$Type): void
public "getCenterY"(): double
public "contains"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
public "contains"(arg0: double, arg1: double): boolean
public "intersects"(arg0: double, arg1: double, arg2: double, arg3: double): boolean
public "getBounds2D"(): $Rectangle2D
public "getPathIterator"(arg0: $AffineTransform$Type): $PathIterator
get "empty"(): boolean
get "bounds"(): $Rectangle
get "y"(): double
get "maxX"(): double
get "minX"(): double
get "maxY"(): double
get "minY"(): double
get "x"(): double
get "width"(): double
get "height"(): double
get "frame"(): $Rectangle2D
set "frame"(value: $Rectangle2D$Type)
get "centerX"(): double
get "centerY"(): double
get "bounds2D"(): $Rectangle2D
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RectangularShape$Type = ($RectangularShape);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RectangularShape_ = $RectangularShape$Type;
}}
declare module "packages/java/awt/datatransfer/$FlavorMap" {
import {$DataFlavor, $DataFlavor$Type} from "packages/java/awt/datatransfer/$DataFlavor"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export interface $FlavorMap {

 "getNativesForFlavors"(arg0: ($DataFlavor$Type)[]): $Map<($DataFlavor), (string)>
 "getFlavorsForNatives"(arg0: (string)[]): $Map<(string), ($DataFlavor)>
}

export namespace $FlavorMap {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlavorMap$Type = ($FlavorMap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlavorMap_ = $FlavorMap$Type;
}}
declare module "packages/java/awt/font/$GlyphMetrics" {
import {$Rectangle2D, $Rectangle2D$Type} from "packages/java/awt/geom/$Rectangle2D"

export class $GlyphMetrics {
static readonly "STANDARD": byte
static readonly "LIGATURE": byte
static readonly "COMBINING": byte
static readonly "COMPONENT": byte
static readonly "WHITESPACE": byte

constructor(arg0: float, arg1: $Rectangle2D$Type, arg2: byte)
constructor(arg0: boolean, arg1: float, arg2: float, arg3: $Rectangle2D$Type, arg4: byte)

public "isWhitespace"(): boolean
public "getType"(): integer
public "isStandard"(): boolean
public "getAdvance"(): float
public "getAdvanceX"(): float
public "getAdvanceY"(): float
public "getLSB"(): float
public "getRSB"(): float
public "isLigature"(): boolean
public "isComponent"(): boolean
public "isCombining"(): boolean
public "getBounds2D"(): $Rectangle2D
get "whitespace"(): boolean
get "type"(): integer
get "standard"(): boolean
get "advance"(): float
get "advanceX"(): float
get "advanceY"(): float
get "lSB"(): float
get "rSB"(): float
get "ligature"(): boolean
get "component"(): boolean
get "combining"(): boolean
get "bounds2D"(): $Rectangle2D
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlyphMetrics$Type = ($GlyphMetrics);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlyphMetrics_ = $GlyphMetrics$Type;
}}
declare module "packages/java/awt/datatransfer/$FlavorListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$FlavorEvent, $FlavorEvent$Type} from "packages/java/awt/datatransfer/$FlavorEvent"

export interface $FlavorListener extends $EventListener {

 "flavorsChanged"(arg0: $FlavorEvent$Type): void

(arg0: $FlavorEvent$Type): void
}

export namespace $FlavorListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FlavorListener$Type = ($FlavorListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FlavorListener_ = $FlavorListener$Type;
}}
declare module "packages/java/awt/dnd/$DropTarget" {
import {$DropTargetDropEvent, $DropTargetDropEvent$Type} from "packages/java/awt/dnd/$DropTargetDropEvent"
import {$DropTargetContext, $DropTargetContext$Type} from "packages/java/awt/dnd/$DropTargetContext"
import {$DropTargetDragEvent, $DropTargetDragEvent$Type} from "packages/java/awt/dnd/$DropTargetDragEvent"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$DropTargetListener, $DropTargetListener$Type} from "packages/java/awt/dnd/$DropTargetListener"
import {$FlavorMap, $FlavorMap$Type} from "packages/java/awt/datatransfer/$FlavorMap"
import {$DropTargetEvent, $DropTargetEvent$Type} from "packages/java/awt/dnd/$DropTargetEvent"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export class $DropTarget implements $DropTargetListener, $Serializable {

constructor(arg0: $Component$Type, arg1: integer, arg2: $DropTargetListener$Type)
constructor(arg0: $Component$Type, arg1: $DropTargetListener$Type)
constructor()
constructor(arg0: $Component$Type, arg1: integer, arg2: $DropTargetListener$Type, arg3: boolean)
constructor(arg0: $Component$Type, arg1: integer, arg2: $DropTargetListener$Type, arg3: boolean, arg4: $FlavorMap$Type)

public "isActive"(): boolean
public "drop"(arg0: $DropTargetDropEvent$Type): void
public "setActive"(arg0: boolean): void
public "getComponent"(): $Component
public "removeNotify"(): void
public "addNotify"(): void
public "setDefaultActions"(arg0: integer): void
public "addDropTargetListener"(arg0: $DropTargetListener$Type): void
public "getDropTargetContext"(): $DropTargetContext
public "dragEnter"(arg0: $DropTargetDragEvent$Type): void
public "dragOver"(arg0: $DropTargetDragEvent$Type): void
public "dropActionChanged"(arg0: $DropTargetDragEvent$Type): void
public "dragExit"(arg0: $DropTargetEvent$Type): void
public "getDefaultActions"(): integer
public "removeDropTargetListener"(arg0: $DropTargetListener$Type): void
public "getFlavorMap"(): $FlavorMap
public "setFlavorMap"(arg0: $FlavorMap$Type): void
public "setComponent"(arg0: $Component$Type): void
get "active"(): boolean
set "active"(value: boolean)
get "component"(): $Component
set "defaultActions"(value: integer)
get "dropTargetContext"(): $DropTargetContext
get "defaultActions"(): integer
get "flavorMap"(): $FlavorMap
set "flavorMap"(value: $FlavorMap$Type)
set "component"(value: $Component$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropTarget$Type = ($DropTarget);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropTarget_ = $DropTarget$Type;
}}
declare module "packages/java/awt/image/$ImageProducer" {
import {$ImageConsumer, $ImageConsumer$Type} from "packages/java/awt/image/$ImageConsumer"

export interface $ImageProducer {

 "removeConsumer"(arg0: $ImageConsumer$Type): void
 "addConsumer"(arg0: $ImageConsumer$Type): void
 "isConsumer"(arg0: $ImageConsumer$Type): boolean
 "startProduction"(arg0: $ImageConsumer$Type): void
 "requestTopDownLeftRightResend"(arg0: $ImageConsumer$Type): void
}

export namespace $ImageProducer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImageProducer$Type = ($ImageProducer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImageProducer_ = $ImageProducer$Type;
}}
declare module "packages/java/awt/geom/$Dimension2D" {
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"

export class $Dimension2D implements $Cloneable {


public "clone"(): any
public "setSize"(arg0: double, arg1: double): void
public "setSize"(arg0: $Dimension2D$Type): void
public "getWidth"(): double
public "getHeight"(): double
set "size"(value: $Dimension2D$Type)
get "width"(): double
get "height"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Dimension2D$Type = ($Dimension2D);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Dimension2D_ = $Dimension2D$Type;
}}
declare module "packages/java/awt/image/$WritableRenderedImage" {
import {$WritableRaster, $WritableRaster$Type} from "packages/java/awt/image/$WritableRaster"
import {$Vector, $Vector$Type} from "packages/java/util/$Vector"
import {$Raster, $Raster$Type} from "packages/java/awt/image/$Raster"
import {$TileObserver, $TileObserver$Type} from "packages/java/awt/image/$TileObserver"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$SampleModel, $SampleModel$Type} from "packages/java/awt/image/$SampleModel"
import {$ColorModel, $ColorModel$Type} from "packages/java/awt/image/$ColorModel"
import {$RenderedImage, $RenderedImage$Type} from "packages/java/awt/image/$RenderedImage"

export interface $WritableRenderedImage extends $RenderedImage {

 "setData"(arg0: $Raster$Type): void
 "addTileObserver"(arg0: $TileObserver$Type): void
 "removeTileObserver"(arg0: $TileObserver$Type): void
 "isTileWritable"(arg0: integer, arg1: integer): boolean
 "getWritableTileIndices"(): ($Point)[]
 "hasTileWriters"(): boolean
 "getWritableTile"(arg0: integer, arg1: integer): $WritableRaster
 "releaseWritableTile"(arg0: integer, arg1: integer): void
 "getProperty"(arg0: string): any
 "getData"(arg0: $Rectangle$Type): $Raster
 "getData"(): $Raster
 "copyData"(arg0: $WritableRaster$Type): $WritableRaster
 "getMinX"(): integer
 "getMinY"(): integer
 "getPropertyNames"(): (string)[]
 "getWidth"(): integer
 "getHeight"(): integer
 "getColorModel"(): $ColorModel
 "getSampleModel"(): $SampleModel
 "getNumXTiles"(): integer
 "getNumYTiles"(): integer
 "getMinTileX"(): integer
 "getMinTileY"(): integer
 "getTileWidth"(): integer
 "getTileHeight"(): integer
 "getTileGridXOffset"(): integer
 "getTileGridYOffset"(): integer
 "getTile"(arg0: integer, arg1: integer): $Raster
 "getSources"(): $Vector<($RenderedImage)>
}

export namespace $WritableRenderedImage {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WritableRenderedImage$Type = ($WritableRenderedImage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WritableRenderedImage_ = $WritableRenderedImage$Type;
}}
declare module "packages/java/awt/event/$ComponentEvent" {
import {$AWTEvent, $AWTEvent$Type} from "packages/java/awt/$AWTEvent"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export class $ComponentEvent extends $AWTEvent {
static readonly "COMPONENT_FIRST": integer
static readonly "COMPONENT_LAST": integer
static readonly "COMPONENT_MOVED": integer
static readonly "COMPONENT_RESIZED": integer
static readonly "COMPONENT_SHOWN": integer
static readonly "COMPONENT_HIDDEN": integer
static readonly "COMPONENT_EVENT_MASK": long
static readonly "CONTAINER_EVENT_MASK": long
static readonly "FOCUS_EVENT_MASK": long
static readonly "KEY_EVENT_MASK": long
static readonly "MOUSE_EVENT_MASK": long
static readonly "MOUSE_MOTION_EVENT_MASK": long
static readonly "WINDOW_EVENT_MASK": long
static readonly "ACTION_EVENT_MASK": long
static readonly "ADJUSTMENT_EVENT_MASK": long
static readonly "ITEM_EVENT_MASK": long
static readonly "TEXT_EVENT_MASK": long
static readonly "INPUT_METHOD_EVENT_MASK": long
static readonly "PAINT_EVENT_MASK": long
static readonly "INVOCATION_EVENT_MASK": long
static readonly "HIERARCHY_EVENT_MASK": long
static readonly "HIERARCHY_BOUNDS_EVENT_MASK": long
static readonly "MOUSE_WHEEL_EVENT_MASK": long
static readonly "WINDOW_STATE_EVENT_MASK": long
static readonly "WINDOW_FOCUS_EVENT_MASK": long
static readonly "RESERVED_ID_MAX": integer

constructor(arg0: $Component$Type, arg1: integer)

public "paramString"(): string
public "getComponent"(): $Component
get "component"(): $Component
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComponentEvent$Type = ($ComponentEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComponentEvent_ = $ComponentEvent$Type;
}}
declare module "packages/java/awt/$Toolkit" {
import {$Dialog$ModalExclusionType, $Dialog$ModalExclusionType$Type} from "packages/java/awt/$Dialog$ModalExclusionType"
import {$PrintJob, $PrintJob$Type} from "packages/java/awt/$PrintJob"
import {$ImageProducer, $ImageProducer$Type} from "packages/java/awt/image/$ImageProducer"
import {$DragGestureRecognizer, $DragGestureRecognizer$Type} from "packages/java/awt/dnd/$DragGestureRecognizer"
import {$Clipboard, $Clipboard$Type} from "packages/java/awt/datatransfer/$Clipboard"
import {$Component, $Component$Type} from "packages/java/awt/$Component"
import {$AWTEventListener, $AWTEventListener$Type} from "packages/java/awt/event/$AWTEventListener"
import {$GraphicsConfiguration, $GraphicsConfiguration$Type} from "packages/java/awt/$GraphicsConfiguration"
import {$URL, $URL$Type} from "packages/java/net/$URL"
import {$Image, $Image$Type} from "packages/java/awt/$Image"
import {$EventQueue, $EventQueue$Type} from "packages/java/awt/$EventQueue"
import {$Cursor, $Cursor$Type} from "packages/java/awt/$Cursor"
import {$PropertyChangeListener, $PropertyChangeListener$Type} from "packages/java/beans/$PropertyChangeListener"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Dialog$ModalityType, $Dialog$ModalityType$Type} from "packages/java/awt/$Dialog$ModalityType"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$DragGestureListener, $DragGestureListener$Type} from "packages/java/awt/dnd/$DragGestureListener"
import {$FontMetrics, $FontMetrics$Type} from "packages/java/awt/$FontMetrics"
import {$Insets, $Insets$Type} from "packages/java/awt/$Insets"
import {$Properties, $Properties$Type} from "packages/java/util/$Properties"
import {$DragSource, $DragSource$Type} from "packages/java/awt/dnd/$DragSource"
import {$ImageObserver, $ImageObserver$Type} from "packages/java/awt/image/$ImageObserver"
import {$PageAttributes, $PageAttributes$Type} from "packages/java/awt/$PageAttributes"
import {$Font, $Font$Type} from "packages/java/awt/$Font"
import {$InputMethodHighlight, $InputMethodHighlight$Type} from "packages/java/awt/im/$InputMethodHighlight"
import {$Frame, $Frame$Type} from "packages/java/awt/$Frame"
import {$JobAttributes, $JobAttributes$Type} from "packages/java/awt/$JobAttributes"
import {$TextAttribute, $TextAttribute$Type} from "packages/java/awt/font/$TextAttribute"
import {$ColorModel, $ColorModel$Type} from "packages/java/awt/image/$ColorModel"
import {$Dimension, $Dimension$Type} from "packages/java/awt/$Dimension"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $Toolkit {


public static "getProperty"(arg0: string, arg1: string): string
public "sync"(): void
public "prepareImage"(arg0: $Image$Type, arg1: integer, arg2: integer, arg3: $ImageObserver$Type): boolean
public "beep"(): void
public "getColorModel"(): $ColorModel
public "setDynamicLayout"(arg0: boolean): void
public "isDynamicLayoutActive"(): boolean
public "getScreenInsets"(arg0: $GraphicsConfiguration$Type): $Insets
public "createImage"(arg0: string): $Image
public "createImage"(arg0: (byte)[], arg1: integer, arg2: integer): $Image
public "createImage"(arg0: $URL$Type): $Image
public "createImage"(arg0: $ImageProducer$Type): $Image
public "createImage"(arg0: (byte)[]): $Image
public "getPrintJob"(arg0: $Frame$Type, arg1: string, arg2: $JobAttributes$Type, arg3: $PageAttributes$Type): $PrintJob
public "getPrintJob"(arg0: $Frame$Type, arg1: string, arg2: $Properties$Type): $PrintJob
public "getSystemSelection"(): $Clipboard
public "createCustomCursor"(arg0: $Image$Type, arg1: $Point$Type, arg2: string): $Cursor
public "getBestCursorSize"(arg0: integer, arg1: integer): $Dimension
public "getMaximumCursorColors"(): integer
public "isFrameStateSupported"(arg0: integer): boolean
public "getPropertyChangeListeners"(arg0: string): ($PropertyChangeListener)[]
public "getPropertyChangeListeners"(): ($PropertyChangeListener)[]
public "areExtraMouseButtonsEnabled"(): boolean
public "getScreenSize"(): $Dimension
public "getScreenResolution"(): integer
/**
 * 
 * @deprecated
 */
public "getFontList"(): (string)[]
public "checkImage"(arg0: $Image$Type, arg1: integer, arg2: integer, arg3: $ImageObserver$Type): integer
public "getSystemClipboard"(): $Clipboard
/**
 * 
 * @deprecated
 */
public "getMenuShortcutKeyMask"(): integer
public "getMenuShortcutKeyMaskEx"(): integer
public "getLockingKeyState"(arg0: integer): boolean
public "setLockingKeyState"(arg0: integer, arg1: boolean): void
public "getSystemEventQueue"(): $EventQueue
public "createDragGestureRecognizer"<T extends $DragGestureRecognizer>(arg0: $Class$Type<(T)>, arg1: $DragSource$Type, arg2: $Component$Type, arg3: integer, arg4: $DragGestureListener$Type): T
public "isAlwaysOnTopSupported"(): boolean
public "isModalityTypeSupported"(arg0: $Dialog$ModalityType$Type): boolean
public "isModalExclusionTypeSupported"(arg0: $Dialog$ModalExclusionType$Type): boolean
public "addAWTEventListener"(arg0: $AWTEventListener$Type, arg1: long): void
public "removeAWTEventListener"(arg0: $AWTEventListener$Type): void
public "getAWTEventListeners"(): ($AWTEventListener)[]
public "getAWTEventListeners"(arg0: long): ($AWTEventListener)[]
public "mapInputMethodHighlight"(arg0: $InputMethodHighlight$Type): $Map<($TextAttribute), (any)>
public static "getDefaultToolkit"(): $Toolkit
public "getDesktopProperty"(arg0: string): any
public "getImage"(arg0: string): $Image
public "getImage"(arg0: $URL$Type): $Image
public "addPropertyChangeListener"(arg0: string, arg1: $PropertyChangeListener$Type): void
public "removePropertyChangeListener"(arg0: string, arg1: $PropertyChangeListener$Type): void
/**
 * 
 * @deprecated
 */
public "getFontMetrics"(arg0: $Font$Type): $FontMetrics
get "colorModel"(): $ColorModel
set "dynamicLayout"(value: boolean)
get "dynamicLayoutActive"(): boolean
get "systemSelection"(): $Clipboard
get "maximumCursorColors"(): integer
get "propertyChangeListeners"(): ($PropertyChangeListener)[]
get "screenSize"(): $Dimension
get "screenResolution"(): integer
get "fontList"(): (string)[]
get "systemClipboard"(): $Clipboard
get "menuShortcutKeyMask"(): integer
get "menuShortcutKeyMaskEx"(): integer
get "systemEventQueue"(): $EventQueue
get "alwaysOnTopSupported"(): boolean
get "aWTEventListeners"(): ($AWTEventListener)[]
get "defaultToolkit"(): $Toolkit
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Toolkit$Type = ($Toolkit);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Toolkit_ = $Toolkit$Type;
}}
declare module "packages/java/awt/$PageAttributes$OriginType" {
import {$AttributeValue, $AttributeValue$Type} from "packages/java/awt/$AttributeValue"

export class $PageAttributes$OriginType extends $AttributeValue {
static readonly "PHYSICAL": $PageAttributes$OriginType
static readonly "PRINTABLE": $PageAttributes$OriginType


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PageAttributes$OriginType$Type = ($PageAttributes$OriginType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PageAttributes$OriginType_ = $PageAttributes$OriginType$Type;
}}
declare module "packages/java/awt/print/$Printable" {
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$PageFormat, $PageFormat$Type} from "packages/java/awt/print/$PageFormat"

export interface $Printable {

 "print"(arg0: $Graphics$Type, arg1: $PageFormat$Type, arg2: integer): integer

(arg0: $Graphics$Type, arg1: $PageFormat$Type, arg2: integer): integer
}

export namespace $Printable {
const PAGE_EXISTS: integer
const NO_SUCH_PAGE: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Printable$Type = ($Printable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Printable_ = $Printable$Type;
}}
declare module "packages/java/awt/geom/$Point2D$Double" {
import {$Point2D, $Point2D$Type} from "packages/java/awt/geom/$Point2D"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

export class $Point2D$Double extends $Point2D implements $Serializable {
 "x": double
 "y": double

constructor()
constructor(arg0: double, arg1: double)

public "toString"(): string
public "getY"(): double
public "getX"(): double
public "setLocation"(arg0: double, arg1: double): void
get "y"(): double
get "x"(): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Point2D$Double$Type = ($Point2D$Double);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Point2D$Double_ = $Point2D$Double$Type;
}}
declare module "packages/java/awt/dnd/$DropTargetContext" {
import {$DropTarget, $DropTarget$Type} from "packages/java/awt/dnd/$DropTarget"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export class $DropTargetContext implements $Serializable {


public "getComponent"(): $Component
public "getDropTarget"(): $DropTarget
public "dropComplete"(arg0: boolean): void
get "component"(): $Component
get "dropTarget"(): $DropTarget
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropTargetContext$Type = ($DropTargetContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropTargetContext_ = $DropTargetContext$Type;
}}
declare module "packages/java/awt/font/$GlyphVector" {
import {$Point2D, $Point2D$Type} from "packages/java/awt/geom/$Point2D"
import {$GlyphJustificationInfo, $GlyphJustificationInfo$Type} from "packages/java/awt/font/$GlyphJustificationInfo"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$GlyphMetrics, $GlyphMetrics$Type} from "packages/java/awt/font/$GlyphMetrics"
import {$Rectangle2D, $Rectangle2D$Type} from "packages/java/awt/geom/$Rectangle2D"
import {$Font, $Font$Type} from "packages/java/awt/$Font"
import {$FontRenderContext, $FontRenderContext$Type} from "packages/java/awt/font/$FontRenderContext"
import {$Shape, $Shape$Type} from "packages/java/awt/$Shape"
import {$AffineTransform, $AffineTransform$Type} from "packages/java/awt/geom/$AffineTransform"

export class $GlyphVector implements $Cloneable {
static readonly "FLAG_HAS_TRANSFORMS": integer
static readonly "FLAG_HAS_POSITION_ADJUSTMENTS": integer
static readonly "FLAG_RUN_RTL": integer
static readonly "FLAG_COMPLEX_GLYPHS": integer
static readonly "FLAG_MASK": integer


public "equals"(arg0: $GlyphVector$Type): boolean
public "getGlyphCode"(arg0: integer): integer
public "getGlyphPosition"(arg0: integer): $Point2D
public "getPixelBounds"(arg0: $FontRenderContext$Type, arg1: float, arg2: float): $Rectangle
public "getOutline"(): $Shape
public "getOutline"(arg0: float, arg1: float): $Shape
public "getGlyphMetrics"(arg0: integer): $GlyphMetrics
public "getGlyphOutline"(arg0: integer): $Shape
public "getGlyphOutline"(arg0: integer, arg1: float, arg2: float): $Shape
public "getLayoutFlags"(): integer
public "getGlyphVisualBounds"(arg0: integer): $Shape
public "getGlyphTransform"(arg0: integer): $AffineTransform
public "setGlyphTransform"(arg0: integer, arg1: $AffineTransform$Type): void
public "getGlyphCharIndices"(arg0: integer, arg1: integer, arg2: (integer)[]): (integer)[]
public "performDefaultLayout"(): void
public "getGlyphCharIndex"(arg0: integer): integer
public "getLogicalBounds"(): $Rectangle2D
public "getVisualBounds"(): $Rectangle2D
public "setGlyphPosition"(arg0: integer, arg1: $Point2D$Type): void
public "getGlyphLogicalBounds"(arg0: integer): $Shape
public "getGlyphPixelBounds"(arg0: integer, arg1: $FontRenderContext$Type, arg2: float, arg3: float): $Rectangle
public "getGlyphJustificationInfo"(arg0: integer): $GlyphJustificationInfo
public "getFont"(): $Font
public "getGlyphCodes"(arg0: integer, arg1: integer, arg2: (integer)[]): (integer)[]
public "getGlyphPositions"(arg0: integer, arg1: integer, arg2: (float)[]): (float)[]
public "getFontRenderContext"(): $FontRenderContext
public "getNumGlyphs"(): integer
get "outline"(): $Shape
get "layoutFlags"(): integer
get "logicalBounds"(): $Rectangle2D
get "visualBounds"(): $Rectangle2D
get "font"(): $Font
get "fontRenderContext"(): $FontRenderContext
get "numGlyphs"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GlyphVector$Type = ($GlyphVector);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GlyphVector_ = $GlyphVector$Type;
}}
declare module "packages/java/awt/color/$ColorSpace" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

export class $ColorSpace implements $Serializable {
static readonly "TYPE_XYZ": integer
static readonly "TYPE_Lab": integer
static readonly "TYPE_Luv": integer
static readonly "TYPE_YCbCr": integer
static readonly "TYPE_Yxy": integer
static readonly "TYPE_RGB": integer
static readonly "TYPE_GRAY": integer
static readonly "TYPE_HSV": integer
static readonly "TYPE_HLS": integer
static readonly "TYPE_CMYK": integer
static readonly "TYPE_CMY": integer
static readonly "TYPE_2CLR": integer
static readonly "TYPE_3CLR": integer
static readonly "TYPE_4CLR": integer
static readonly "TYPE_5CLR": integer
static readonly "TYPE_6CLR": integer
static readonly "TYPE_7CLR": integer
static readonly "TYPE_8CLR": integer
static readonly "TYPE_9CLR": integer
static readonly "TYPE_ACLR": integer
static readonly "TYPE_BCLR": integer
static readonly "TYPE_CCLR": integer
static readonly "TYPE_DCLR": integer
static readonly "TYPE_ECLR": integer
static readonly "TYPE_FCLR": integer
static readonly "CS_sRGB": integer
static readonly "CS_LINEAR_RGB": integer
static readonly "CS_CIEXYZ": integer
static readonly "CS_PYCC": integer
static readonly "CS_GRAY": integer


public "getName"(arg0: integer): string
public static "getInstance"(arg0: integer): $ColorSpace
public "getType"(): integer
public "getMinValue"(arg0: integer): float
public "getMaxValue"(arg0: integer): float
public "getNumComponents"(): integer
public "toRGB"(arg0: (float)[]): (float)[]
public "toCIEXYZ"(arg0: (float)[]): (float)[]
public "fromCIEXYZ"(arg0: (float)[]): (float)[]
public "fromRGB"(arg0: (float)[]): (float)[]
public "isCS_sRGB"(): boolean
get "type"(): integer
get "numComponents"(): integer
get "cS_sRGB"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ColorSpace$Type = ($ColorSpace);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ColorSpace_ = $ColorSpace$Type;
}}
declare module "packages/java/awt/event/$WindowStateListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$WindowEvent, $WindowEvent$Type} from "packages/java/awt/event/$WindowEvent"

export interface $WindowStateListener extends $EventListener {

 "windowStateChanged"(arg0: $WindowEvent$Type): void

(arg0: $WindowEvent$Type): void
}

export namespace $WindowStateListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WindowStateListener$Type = ($WindowStateListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WindowStateListener_ = $WindowStateListener$Type;
}}
declare module "packages/java/awt/$Cursor" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"

export class $Cursor implements $Serializable {
static readonly "DEFAULT_CURSOR": integer
static readonly "CROSSHAIR_CURSOR": integer
static readonly "TEXT_CURSOR": integer
static readonly "WAIT_CURSOR": integer
static readonly "SW_RESIZE_CURSOR": integer
static readonly "SE_RESIZE_CURSOR": integer
static readonly "NW_RESIZE_CURSOR": integer
static readonly "NE_RESIZE_CURSOR": integer
static readonly "N_RESIZE_CURSOR": integer
static readonly "S_RESIZE_CURSOR": integer
static readonly "W_RESIZE_CURSOR": integer
static readonly "E_RESIZE_CURSOR": integer
static readonly "HAND_CURSOR": integer
static readonly "MOVE_CURSOR": integer
static readonly "CUSTOM_CURSOR": integer

constructor(arg0: integer)

public "getName"(): string
public "toString"(): string
public "getType"(): integer
public static "getPredefinedCursor"(arg0: integer): $Cursor
public static "getDefaultCursor"(): $Cursor
public static "getSystemCustomCursor"(arg0: string): $Cursor
get "name"(): string
get "type"(): integer
get "defaultCursor"(): $Cursor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Cursor$Type = ($Cursor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Cursor_ = $Cursor$Type;
}}
declare module "packages/java/awt/$BufferCapabilities" {
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$BufferCapabilities$FlipContents, $BufferCapabilities$FlipContents$Type} from "packages/java/awt/$BufferCapabilities$FlipContents"
import {$ImageCapabilities, $ImageCapabilities$Type} from "packages/java/awt/$ImageCapabilities"

export class $BufferCapabilities implements $Cloneable {

constructor(arg0: $ImageCapabilities$Type, arg1: $ImageCapabilities$Type, arg2: $BufferCapabilities$FlipContents$Type)

public "clone"(): any
public "isPageFlipping"(): boolean
public "getFlipContents"(): $BufferCapabilities$FlipContents
public "getFrontBufferCapabilities"(): $ImageCapabilities
public "getBackBufferCapabilities"(): $ImageCapabilities
public "isFullScreenRequired"(): boolean
public "isMultiBufferAvailable"(): boolean
get "pageFlipping"(): boolean
get "flipContents"(): $BufferCapabilities$FlipContents
get "frontBufferCapabilities"(): $ImageCapabilities
get "backBufferCapabilities"(): $ImageCapabilities
get "fullScreenRequired"(): boolean
get "multiBufferAvailable"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BufferCapabilities$Type = ($BufferCapabilities);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BufferCapabilities_ = $BufferCapabilities$Type;
}}
declare module "packages/java/awt/$Window$Type" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Window$Type extends $Enum<($Window$Type)> {
static readonly "NORMAL": $Window$Type
static readonly "UTILITY": $Window$Type
static readonly "POPUP": $Window$Type


public static "values"(): ($Window$Type)[]
public static "valueOf"(arg0: string): $Window$Type
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Window$Type$Type = (("normal") | ("popup") | ("utility")) | ($Window$Type);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Window$Type_ = $Window$Type$Type;
}}
declare module "packages/java/awt/datatransfer/$Clipboard" {
import {$DataFlavor, $DataFlavor$Type} from "packages/java/awt/datatransfer/$DataFlavor"
import {$FlavorListener, $FlavorListener$Type} from "packages/java/awt/datatransfer/$FlavorListener"
import {$ClipboardOwner, $ClipboardOwner$Type} from "packages/java/awt/datatransfer/$ClipboardOwner"
import {$Transferable, $Transferable$Type} from "packages/java/awt/datatransfer/$Transferable"

export class $Clipboard {

constructor(arg0: string)

public "getName"(): string
public "getContents"(arg0: any): $Transferable
public "getData"(arg0: $DataFlavor$Type): any
public "getAvailableDataFlavors"(): ($DataFlavor)[]
public "isDataFlavorAvailable"(arg0: $DataFlavor$Type): boolean
public "addFlavorListener"(arg0: $FlavorListener$Type): void
public "removeFlavorListener"(arg0: $FlavorListener$Type): void
public "getFlavorListeners"(): ($FlavorListener)[]
public "setContents"(arg0: $Transferable$Type, arg1: $ClipboardOwner$Type): void
get "name"(): string
get "availableDataFlavors"(): ($DataFlavor)[]
get "flavorListeners"(): ($FlavorListener)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Clipboard$Type = ($Clipboard);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Clipboard_ = $Clipboard$Type;
}}
declare module "packages/java/awt/$Adjustable" {
import {$AdjustmentListener, $AdjustmentListener$Type} from "packages/java/awt/event/$AdjustmentListener"

export interface $Adjustable {

 "getValue"(): integer
 "setValue"(arg0: integer): void
 "getMaximum"(): integer
 "getMinimum"(): integer
 "getUnitIncrement"(): integer
 "setUnitIncrement"(arg0: integer): void
 "getBlockIncrement"(): integer
 "setBlockIncrement"(arg0: integer): void
 "getVisibleAmount"(): integer
 "setVisibleAmount"(arg0: integer): void
 "addAdjustmentListener"(arg0: $AdjustmentListener$Type): void
 "removeAdjustmentListener"(arg0: $AdjustmentListener$Type): void
 "setMaximum"(arg0: integer): void
 "setMinimum"(arg0: integer): void
 "getOrientation"(): integer
}

export namespace $Adjustable {
const HORIZONTAL: integer
const VERTICAL: integer
const NO_ORIENTATION: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Adjustable$Type = ($Adjustable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Adjustable_ = $Adjustable$Type;
}}
declare module "packages/java/awt/dnd/$DragSourceDragEvent" {
import {$DragSourceEvent, $DragSourceEvent$Type} from "packages/java/awt/dnd/$DragSourceEvent"
import {$DragSourceContext, $DragSourceContext$Type} from "packages/java/awt/dnd/$DragSourceContext"

export class $DragSourceDragEvent extends $DragSourceEvent {

constructor(arg0: $DragSourceContext$Type, arg1: integer, arg2: integer, arg3: integer)
constructor(arg0: $DragSourceContext$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer)

public "getDropAction"(): integer
public "getGestureModifiers"(): integer
public "getGestureModifiersEx"(): integer
public "getTargetActions"(): integer
public "getUserAction"(): integer
get "dropAction"(): integer
get "gestureModifiers"(): integer
get "gestureModifiersEx"(): integer
get "targetActions"(): integer
get "userAction"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DragSourceDragEvent$Type = ($DragSourceDragEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DragSourceDragEvent_ = $DragSourceDragEvent$Type;
}}
declare module "packages/java/awt/$ComponentOrientation" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$ResourceBundle, $ResourceBundle$Type} from "packages/java/util/$ResourceBundle"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"

export class $ComponentOrientation implements $Serializable {
static readonly "LEFT_TO_RIGHT": $ComponentOrientation
static readonly "RIGHT_TO_LEFT": $ComponentOrientation
static readonly "UNKNOWN": $ComponentOrientation


public "isLeftToRight"(): boolean
public "isHorizontal"(): boolean
public static "getOrientation"(arg0: $Locale$Type): $ComponentOrientation
/**
 * 
 * @deprecated
 */
public static "getOrientation"(arg0: $ResourceBundle$Type): $ComponentOrientation
get "leftToRight"(): boolean
get "horizontal"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComponentOrientation$Type = ($ComponentOrientation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComponentOrientation_ = $ComponentOrientation$Type;
}}
